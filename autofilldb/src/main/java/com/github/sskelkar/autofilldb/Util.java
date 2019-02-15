package com.github.sskelkar.autofilldb;

final class Util {
    public static boolean isNullOrEmpty(String key) {
        return key == null || key.trim().length() == 0;
    }
    public static String enquote(String value) {
        return "'" + value + "'";
    }
}
