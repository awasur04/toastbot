package utils;

public class Util {
    public static String getString(String[] input, int startIndex, int endIndex) {
        String message = "";
        for (int i = startIndex ; i < endIndex ; i++) {
            if (i < endIndex - 1) {
                message = message + input[i] + " ";
            } else {
                message = message + input[i];
            }
        }
        if (message.isEmpty()) {
            return "Error message returned empty";
        } else {
            return message;
        }
    }
}
