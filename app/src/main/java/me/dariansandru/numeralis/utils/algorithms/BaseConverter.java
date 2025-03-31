package me.dariansandru.numeralis.utils.algorithms;

import me.dariansandru.numeralis.utils.structures.BaseNumber;

public abstract class BaseConverter {

    private static final int CHARACTER_GAP = 'a' - 'A';

    private static int getValue(Character character){
        if ('0' <= character && character <= '9'){
            return Character.getNumericValue(character);
        }

        if ('a' <= character && character <= 'z') character = (char) (character - CHARACTER_GAP);
        if ('A' <= character && character <= 'Z') return 10 + (character - 'A');
        return -1;
    }

    private static char getChar(int number) {
        if (0 <= number && number <= 9) {
            return (char) ('0' + number);
        }
        else if (10 <= number && number < 36) {
            return (char) ('A' + (number - 10));
        }
        else throw new IllegalArgumentException("Number out of range for base conversion: " + number);
    }


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

    // This function assumes that number is in base 10.
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
