public enum State {
    ACCEPT_COMMAND, DATE, PRESCRIPTION, APPROV, STOCK, END;

    static State from(String s) {
        return State.valueOf(s);
    }
}
