package com.github.sskelkar.autofilldb;

final class Util {
    static boolean isNullOrEmpty(String key) {
        return key == null || key.trim().length() == 0;
    }
    static String enquote(String value) {
        return "'" + value + "'";
    }
}
