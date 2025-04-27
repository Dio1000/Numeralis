package me.dariansandru.numeralis.utils.algorithms;

import me.dariansandru.numeralis.utils.structures.BaseNumber;

/**
 * Using this abstract utility class will allow the user to convert instances of the
 * BaseNumber class from one base to another.
 */
public abstract class BaseConverter {

    private static final int CHARACTER_GAP = 'a' - 'A';

    /**
     * Auxiliary function to get the numeric value of a character (including ASCII characters A-Z).
     * @param character Character to get the numeric value of.
     * @return The numeric value of the character.
     */
    private static int getValue(Character character){
        if ('0' <= character && character <= '9'){
            return Character.getNumericValue(character);
        }

        if ('a' <= character && character <= 'z') character = (char) (character - CHARACTER_GAP);
        if ('A' <= character && character <= 'Z') return 10 + (character - 'A');
        return -1;
    }

    /**
     * Auxiliary function to get the character from a numeric value.
     * @param number Number to get the character from.
     * @return Character corresponding to the numeric value.
     */
    private static char getChar(int number) {
        if (0 <= number && number <= 9) {
            return (char) ('0' + number);
        }
        else if (10 <= number && number < 36) {
            return (char) ('A' + (number - 10));
        }
        else throw new IllegalArgumentException("Number out of range for base conversion: " + number);
    }

    /**
     * Auxiliary function to check whether a given number in String format can be in a given base.
     * @param number String containing a number to check for.
     * @param base Base to check for.
     * @return True if the number can be in the given string, false otherwise.
     */
    public static boolean isCorrectBase(String number, int base) {
        char maxDigit = (base <= 10) ? (char) ('0' + (base - 1)) : getChar(base - 1);

        int index = 0;
        while (index < number.length()) {
            if (number.charAt(index) > maxDigit) return false;
            index++;
        }

        return true;
    }

    /**
     * Converts an instance of BaseNumber from any base to base10.
     * @param number Instance of BaseNumber to convert to base10.
     * @return The numeric value in base10 of the number.
     */
    public static long convertToDecimal(BaseNumber number){
        String representation = number.getRepresentation();
        int base = number.getBase();

        long total = 0;
        int power = 0;

        for (int i = representation.length() - 1; i >= 0; i--){
            int digitValue = getValue(representation.charAt(i));

            if (digitValue == -1 || digitValue >= base) return -1;
            total += (long) (digitValue * Math.pow(base, power));
            power++;
        }

        return total;
    }

    /**
     * Converts an instance of BaseNumber to a given base. This function assumes the number is in base10.
     * @param number Instance of BaseNumber to convert.
     * @param base The base to convert the number to.
     * @return The result of the conversion as an instance of BaseNumber.
     */
    public static BaseNumber convertToBase(BaseNumber number, int base) {
        String representation = number.getRepresentation();
        if (representation == null) return new BaseNumber("NaN", -1);

        long decimalNumber = Long.parseLong(representation);
        if (decimalNumber == 0) return new BaseNumber("0", base);

        StringBuilder builder = new StringBuilder();

        while (decimalNumber > 0) {
            long remainder = decimalNumber % base;
            builder.insert(0, getChar((int) remainder));
            decimalNumber /= base;
        }

        return new BaseNumber(builder.toString(), base);
    }

}
