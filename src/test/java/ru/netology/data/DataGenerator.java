package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.val;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private DataGenerator() {
    }

    // для тестов поля "Номер карты"

    public static String getCardNumber() {
        return ("1111 2222 3333 4444");
    }

    public static String getEmptyCardNumberField() {
        return ("");
    }

    public static String getIncompleteCardNumber() { // неполный номер карты
        return ("1111 2222 3333 444");
    }

    public static String getInvalidCardNumber() { // получение отказа в транзакции
        return "4444 3333 2222 1111";
    }

    // для тестов поля "Месяц"

    public static String generateMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getEmptyMonthField() {
        return "";
    }

    public static String getMonthWithZeros() {
        return "00";
    }

    public static String getInvalidMonth() {
        return "13";
    }

    // для тестов поля "Год"

    public static String generateYear() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getEmptyYearField() {
        return "";
    }

    public static String getInvalidYear() {
        return "23";
    }

    // для тестов поля "Владелец"

    public static String generateOwner() {
        var faker = new Faker(new Locale("en"));
        return faker.name().fullName();
    }

    public static String getEmptyOwnerField() {
        return "";
    }

    public static String getOwnerInCyrillic() {
        return "Тестов Тест";
    }

    public static String getOwnerNumbers(){
        return "123";
    }

    // для тестов поля "CVC/CVV"

    public static int generateCVC() {
        Random randomCVC = new Random();
        int cvcMin = 100;
        int cvcMax = 999;
        int cvc = cvcMin + randomCVC.nextInt(cvcMax - cvcMin + 1);
        return cvc;
    }

    public static String getEmptyCVCField() {
        return "";
    }

    public static String getInvalidCVC() {
        return "33";
    }

     public static CardInfo generateInfoCard() {
        val cardInfo = new CardInfo(getCardNumber(), generateMonth(), generateYear(), generateOwner(), String.valueOf(generateCVC()));
        return cardInfo;
      }

    @Value
    @RequiredArgsConstructor
    public static class CardInfo {
        String cardNumber;
        String month;
        String year;
        String owner;
        String cvc;
    }
}