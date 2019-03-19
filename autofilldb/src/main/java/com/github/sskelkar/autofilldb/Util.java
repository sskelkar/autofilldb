package com.github.sskelkar.autofilldb;

final class Util {
  static boolean isNullOrEmpty(String key) {
    return key == null || key.trim().length() == 0;
  }

  static String enquote(String value) {
    return "'" + value + "'";
  }

  static String removeQuotes(String value) {
    char singleQuote = '\'';
    if (value.length() > 1 && value.charAt(0) == singleQuote && value.charAt(value.length() - 1) == singleQuote) {
      value = value.substring(1, value.length() - 1);
    }
    return value;
  }
}
