package com.bwxor.piejfxsdk.type;

public enum Credential {
    AUTHOR_NAME("authorName"), AUTHOR_EMAIL("authorEmail"), USERNAME("username"), PASSKEY("passkey");

    private final String value;

    Credential(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Credential parse(String input) {
        for(Credential m : values()) {
            if (m.value.equals(input)) {
                return m;
            }
        }

        return null;
    }
}
