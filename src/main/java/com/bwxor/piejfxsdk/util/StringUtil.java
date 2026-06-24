package com.bwxor.piejfxsdk.util;

public class StringUtil {
    public static boolean isNullOrEmpty(String input) {
        return null == input || input.trim().isEmpty();
    }
}
