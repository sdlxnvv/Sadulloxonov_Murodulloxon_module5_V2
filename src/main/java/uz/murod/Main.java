package uz.murod;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.jetbrains.annotations.NotNull;
import uz.murod.bot.ButtonNames;
import uz.murod.bot.UserState;
import uz.murod.entity.Homework;
import uz.murod.entity.User;
import uz.murod.storage.Storage;

import java.io.*;
import java.nio.file.*;
import java.sql.Time;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final ResourceBundle resourceBundle;

    static {
        // Безопасная загрузка настроек с проверкой на наличие всех обязательных ключей
        ResourceBundle tempBundle;
        try {
            tempBundle = ResourceBundle.getBundle("settings");
            // Убедимся, что все необходимые ключи существуют
            Objects.requireNonNull(tempBundle.getString("bot.token"), "Ключ bot.token отсутствует!");
            Objects.requireNonNull(tempBundle.getString("bot.adminID"), "Ключ bot.adminID отсутствует!");
        } catch (MissingResourceException | NullPointerException e) {
            logger.severe("Ошибка загрузки settings.properties: " + e.getMessage());
            throw new IllegalStateException("Не удалось загрузить настройки приложения", e);
        }
        resourceBundle = tempBundle;
    }

    // Хранилище состояний пользователей
    private static final Map<Long, UserState> userStates = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        TelegramBot bot = new TelegramBot(resourceBundle.getString("bot.token"));

        bot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                try {
                    handleUpdate(bot, update);
                } catch (Exception e) {
                    logger.severe("Ошибка обработки данных: " + e.getMessage());
                    e.printStackTrace();
                }
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private static void handleUpdate(TelegramBot bot, Update update) {
        if (update.message() != null) {
            Long chatId = update.message().chat().id();
            String message = update.message().text();

            if (message == null) {
                return;
            }

            try {
                Long adminId = Long.parseLong(resourceBundle.getString("bot.adminID"));
                if (chatId.equals(adminId)) {
                    bot.execute(new SendMessage(chatId, "Welcome, Admin!"));
                } else {
                    handleUserInput(bot, chatId, message, update);
                }
            } catch (NumberFormatException e) {
                logger.severe("Неверный формат adminID в настройках: " + e.getMessage());
            }
        }
    }

    private static void handleUserInput(TelegramBot bot, Long chatId, String message, Update update) {
        userStates.putIfAbsent(chatId, UserState.NEW);
        UserState state = userStates.get(chatId);

        if ("/start".equals(message)) {
            userStates.put(chatId, UserState.NEW);
            bot.execute(sendFirstMessage(chatId, new User(chatId,
                    Optional.ofNullable(update.message().from().firstName()).orElse(""),
                    Optional.ofNullable(update.message().from().lastName()).orElse(""),
                    Optional.ofNullable(update.message().from().username()).orElse(""))));
        } else if (isClickAnyButton(message)) {
            processButtonClick(bot, chatId, message);
        } else {
            processUserState(bot, chatId, message, update, state);
        }
    }

    private static void processButtonClick(TelegramBot bot, Long chatId, String message) {
        ButtonNames button = getButtonNamesByText(message);
        if (button != null) {
            switch (button) {
                case SEND_HOMEWORK -> userStates.put(chatId, UserState.SEND_THEME_OR_DESCRIPTION);
                case SHOW_OLD_HOMEWORK -> userStates.put(chatId, UserState.SHOW_OLD_HOMEWORK);
                case CANCEL -> {
                    Storage.users.removeIf(user -> user.getChatId().equals(chatId));
                    userStates.remove(chatId);
                    bot.execute(new SendMessage(chatId, "Действие отменено!"));
                }
            }
        }
    }

    private static void processUserState(TelegramBot bot, Long chatId, String message, Update update, UserState state) {
        switch (state) {
            case SEND_THEME_OR_DESCRIPTION -> {
                bot.execute(new SendMessage(chatId, "Отправьте тему или описание домашнего задания."));
                Homework homework = new Homework();
                homework.setThemeOrDescription(message);
                homework.setUser(Storage.users.stream()
                        .filter(user -> user.getChatId().equals(chatId))
                        .findFirst()
                        .orElse(null));
                homework.setSendTime(Time.valueOf(LocalTime.now()));
                userStates.put(chatId, UserState.SEND_ZIP_FILE_ID);
            }
            case SEND_ZIP_FILE_ID -> {
                Message messageObject = update.message();
                Document document = messageObject.document();

                if (document != null && "application/zip".equals(document.mimeType())) {
                    handleZipFile(bot, chatId, document);
                } else {
                    bot.execute(new SendMessage(chatId, "Отправьте файл в формате ZIP."));
                }
            }
            case HOMEWORK_SENT -> {
                bot.execute(new SendMessage(chatId, "Домашнее задание успешно отправлено!"));
                userStates.remove(chatId);
            }
        }
    }

    private static void handleZipFile(TelegramBot bot, Long chatId, Document document) {
        try {
            String fileId = document.fileId();
            String outputDirectory = "unzipped_files";

            GetFileResponse fileResponse = bot.execute(new GetFile(fileId));
            byte[] fileStream = bot.getFileContent(fileResponse.file());
            unzip(fileStream, outputDirectory);

            bot.execute(new SendMessage(chatId, "Файл успешно загружен и распакован!"));
            userStates.put(chatId, UserState.HOMEWORK_SENT);

            clearTemporaryFiles(outputDirectory);
        } catch (Exception e) {
            logger.severe("Ошибка обработки ZIP-файла: " + e.getMessage());
            bot.execute(new SendMessage(chatId, "Ошибка при обработке файла: " + e.getMessage()));
        }
    }

    private static void unzip(byte[] zipFile, String outputFolderPath) throws Exception {
        Path outputFolder = Paths.get(outputFolderPath);
        if (!Files.exists(outputFolder)) {
            Files.createDirectories(outputFolder);
        }

        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                File outFile = new File(outputFolder.toFile(), entry.getName());

                if (!outFile.toPath().normalize().startsWith(outputFolder)) {
                    throw new SecurityException("Попытка извлечения файла за пределы директории: " + entry.getName());
                }

                if (entry.isDirectory()) {
                    outFile.mkdirs();
                } else {
                    try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
                        zipInputStream.transferTo(outputStream);
                    }
                }
                zipInputStream.closeEntry();
            }
        }
    }

    private static void clearTemporaryFiles(String directory) {
        try {
            Files.walk(Paths.get(directory))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            logger.severe("Ошибка при удалении временных файлов: " + e.getMessage());
        }
    }

    @NotNull
    private static SendMessage sendFirstMessage(Long chatId, User user) {
        Storage.users.add(user);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(
                ButtonNames.SEND_HOMEWORK.getString(),
                ButtonNames.SHOW_OLD_HOMEWORK.getString());
        keyboardMarkup.resizeKeyboard(true).oneTimeKeyboard(true);

        return new SendMessage(chatId, "Welcome User!")
                .replyMarkup(keyboardMarkup);
    }

    private static ButtonNames getButtonNamesByText(String text) {
        return Arrays.stream(ButtonNames.values())
                .filter(button -> button.getString().equals(text))
                .findFirst()
                .orElse(null);
    }

    private static boolean isClickAnyButton(String text) {
        return Arrays.stream(ButtonNames.values())
                .anyMatch(button -> button.getString().equals(text));
    }
}