package autofilldb;

public class Util {
    public static boolean isNullOrEmpty(String key) {
        return key == null || key.trim().length() == 0;
    }
}
