package uz.murod.bot;

public enum ButtonNames {
    // user uchun
    SEND_HOMEWORK("Send Homework"),
    SHOW_OLD_HOMEWORK("Show Old Homework"),

    // admin uchun

    // hamma uchun
    CANCEL("Cancel");


    private final String string;
    ButtonNames(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }
}
