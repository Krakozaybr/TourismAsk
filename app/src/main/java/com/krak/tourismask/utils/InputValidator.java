package com.krak.tourismask.utils;

public class InputValidator {
    public static boolean isValidUserName(String name){
        // Имя содержит только буквы/цифры/подчёркивание, и его длина >= 3
        return name.matches("\\w{3,}");
    }

    // Проверяем входные данные на возможность конвертирования в Double
    public static boolean isValidDouble(String input){
        try {
            double d = Double.parseDouble(input);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
