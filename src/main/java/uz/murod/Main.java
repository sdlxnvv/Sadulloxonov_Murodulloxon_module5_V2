package uz.murod;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.jetbrains.annotations.NotNull;
import uz.murod.storage.DB;
import uz.murod.bot.ButtonNames;
import uz.murod.bot.UserState;
import uz.murod.entity.User;

import java.util.Arrays;
import java.util.ResourceBundle;

public class Main {
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("settings");
    private static UserState state = null;
    public static void main(String[] args) {
        TelegramBot bot = new TelegramBot(resourceBundle.getString("bot.token"));

        bot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                if (update.message() != null) {
                    String message = update.message().text();
                    Long chatId = update.message().chat().id();
                    if (chatId == Integer.parseInt(resourceBundle.getString("bot.adminID"))) {
                    } else {
                        if (message.equals("/start")) {
                            state = UserState.NEW;
                            bot.execute(sendFirstMessage(chatId, new User(chatId, update.message().from().firstName(), update.message().from().lastName(), update.message().from().username())));
                        }
                        if (isClickAnyButton(message)) {
                            switch () {
                                case SEND_HOMEWORK:
                                    state = UserState.HOMEWORK_SENT;
                                    break;
                                case SHOW_OLD_HOMEWORK:
                                    state = UserState.HOMEWORK_CHECKER;
                                    break;
                                case CANCEL:
                                    state = null;
                                    break;
                            }
                        }

                    }
                } else if (update.callbackQuery() != null) {
                }
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    @NotNull
    private static SendMessage sendFirstMessage(Long chatId, User user) {
        SendMessage sendMessage = new SendMessage(
                chatId,
                "Welcome User!"
        );
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(
               ButtonNames.SEND_HOMEWORK.getString(),ButtonNames.SHOW_OLD_HOMEWORK.getString());
        keyboardMarkup.resizeKeyboard(true);
        keyboardMarkup.oneTimeKeyboard(true);
        sendMessage.replyMarkup(keyboardMarkup);
        DB.users.add(user);
        return sendMessage;
    }

    private static ButtonNames getButtonNamesByText(String text) {
        ButtonNames[] values = ButtonNames.values();

        for (ButtonNames value : values) {
            if (value.getString().equals(text)) {
                return value;
            }
        }
        return null;
    }

    private static boolean isClickAnyButton(String text) {
        ButtonNames[] values = ButtonNames.values();
        return Arrays.stream(values).anyMatch(buttonNames -> buttonNames.getString().equals(text));
    }
}