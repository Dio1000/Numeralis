package me.dariansandru.numeralis.utils.algorithms.logic;

public abstract class BitwiseHelper {

    public static boolean isBinaryNumber(String number) {
        int index = 0;
        while (index != number.length()) {
            if (number.charAt(index) != '1' && number.charAt(index) != '0') return false;
            index++;
        }

        return true;
    }

    public static String bitwiseAND(String num1, String num2) {
        if (num1 == null || num2 == null || num1.isEmpty() || num2.isEmpty()) return "0";

        int maxLength = Math.max(num1.length(), num2.length());
        num1 = padWithLeadingZeros(num1, maxLength);
        num2 = padWithLeadingZeros(num2, maxLength);

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < maxLength; i++) {
            char bit1 = num1.charAt(i);
            char bit2 = num2.charAt(i);

            if (bit1 == '1' && bit2 == '1') result.append('1');
            else result.append('0');
        }

        return removeLeadingZeros(result.toString());
    }

    public static String bitwiseOR(String num1, String num2) {
        if (num1 == null || num2 == null || num1.isEmpty() || num2.isEmpty()) return "0";

        int maxLength = Math.max(num1.length(), num2.length());
        num1 = padWithLeadingZeros(num1, maxLength);
        num2 = padWithLeadingZeros(num2, maxLength);

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < maxLength; i++) {
            char bit1 = num1.charAt(i);
            char bit2 = num2.charAt(i);

            if (bit1 == '1' || bit2 == '1') result.append('1');
            else result.append('0');
        }

        return removeLeadingZeros(result.toString());
    }

    public static String bitwiseXOR(String num1, String num2) {
        if (num1 == null || num2 == null || num1.isEmpty() || num2.isEmpty()) return "0";

        int maxLength = Math.max(num1.length(), num2.length());
        num1 = padWithLeadingZeros(num1, maxLength);
        num2 = padWithLeadingZeros(num2, maxLength);

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < maxLength; i++) {
            char bit1 = num1.charAt(i);
            char bit2 = num2.charAt(i);

            if ((bit1 == '1' && bit2 == '0') || (bit1 == '0' && bit2 == '1')) result.append('1');
            else result.append('0');
        }

        return removeLeadingZeros(result.toString());
    }

    private static String padWithLeadingZeros(String binary, int length) {
        StringBuilder binaryBuilder = new StringBuilder(binary);
        while (binaryBuilder.length() < length) {
            binaryBuilder.insert(0, "0");
        }
        binary = binaryBuilder.toString();
        return binary;
    }

    private static String removeLeadingZeros(String binary) {
        int firstOne = binary.indexOf('1');
        if (firstOne == -1) return "0";

        return binary.substring(firstOne);
    }
}
