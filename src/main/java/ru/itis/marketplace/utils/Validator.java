package ru.itis.marketplace.utils;

import ru.itis.marketplace.exceptions.ValidationException;

public class Validator {

    /*
    *   Экранирует строку от xss инъекции
    *   Сохраняет спец символы переноса строки и табуляции
     */
    public static String escapeAndFormat(String input) {
        if(input == null)
            return "";

        return escapeHtml(input)
                .trim()
                .replaceAll("\n", "<br/>")
                .replaceAll("\t", "&nbsp;&nbsp;&nbsp;");
    }

    public static String escapeHtml(String input) {
        if(input == null)
            return "";

        return reEscape(input)
                .replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&#x27;");
    }

    //необходимо перед экранированием
    private static String reEscape(String input) {
        return input
                .replaceAll("<br/>", "\n")
                .replaceAll("&nbsp;&nbsp;&nbsp;", "\t");
    }

    public static boolean containsOnlyLettersAndDigits(String input) {
        if (input == null || input.isBlank())
            return false;

        for (char c : input.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }


    public static double validateDouble(String number) throws ValidationException {
        double numberDouble;
        validateNotBlank(number, "number");
        try{
           numberDouble = Double.parseDouble(number);
            if(numberDouble < 0)
                throw new ValidationException("Must be non-negative");

        } catch(NumberFormatException e){
            throw new ValidationException("Invalid number format: " + number);
        }
        return numberDouble;
    }

    public static int validateInt(String number) throws ValidationException {
        int numberInt;
        validateNotBlank(number, "number");
        try{
            numberInt = Integer.parseInt(number);
            if(numberInt < 0)
                throw new ValidationException("Must be non-negative");

        } catch(NumberFormatException e){
            throw new ValidationException("Invalid number format: " + number);
        }
        return numberInt;
    }

    public static long validateLong(String number) throws ValidationException {
        long numberLong;
        validateNotBlank(number, "number");
        try{
            numberLong = Long.parseLong(number);
            if(numberLong < 0)
                throw new ValidationException("Must be non-negative");

        } catch(NumberFormatException e){
            throw new ValidationException("Invalid number format: " + number);
        }
        return numberLong;
    }

    public static void validateNotBlank(String s, String paramName) throws ValidationException {
        if(s == null || s.isBlank())
            throw new ValidationException(paramName + " is null or blank");
    }

}
