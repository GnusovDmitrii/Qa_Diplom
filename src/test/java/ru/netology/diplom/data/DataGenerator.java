package ru.netology.diplom.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private static final Faker faker = new Faker(new Locale("ru"));
    private static final Random random = new Random();

    private DataGenerator() {
    }

    @Value
    public static class CardInfo {
        String cardNumber;
        String month;
        String year;
        String owner;
        String cvc;
    }

    // Генерация валидной карты с нужным статусом
    public static CardInfo generateValidCard(String status) {
        String cardNumber;
        if ("APPROVED".equals(status)) {
            cardNumber = "4444 4444 4444 4441";
        } else {
            cardNumber = "4444 4444 4444 4442";
        }

        LocalDate now = LocalDate.now();
        String month = String.format("%02d", now.plusMonths(1).getMonthValue());
        String year = String.format("%02d", now.plusYears(1).getYear() % 100);
        String owner = faker.name().firstName() + " " + faker.name().lastName();
        String cvc = String.format("%03d", random.nextInt(1000));

        return new CardInfo(cardNumber, month, year, owner, cvc);
    }

    // Невалидный номер карты (короткий)
    public static CardInfo generateInvalidCardNumber() {
        String cardNumber = "4444 4444 4444";
        String month = "12";
        String year = "25";
        String owner = faker.name().firstName() + " " + faker.name().lastName();
        String cvc = "123";

        return new CardInfo(cardNumber, month, year, owner, cvc);
    }

    // Невалидный месяц
    public static CardInfo generateInvalidMonth() {
        String cardNumber = "4444 4444 4444 4441";
        String month = "13";
        String year = "25";
        String owner = faker.name().firstName() + " " + faker.name().lastName();
        String cvc = "123";

        return new CardInfo(cardNumber, month, year, owner, cvc);
    }

    // Невалидный год
    public static CardInfo generateInvalidYear() {
        String cardNumber = "4444 4444 4444 4441";
        String month = "12";
        String year = "20"; // прошлый год
        String owner = faker.name().firstName() + " " + faker.name().lastName();
        String cvc = "123";

        return new CardInfo(cardNumber, month, year, owner, cvc);
    }

    // Истекший срок
    public static CardInfo generateExpiredCard() {
        String cardNumber = "4444 4444 4444 4441";
        LocalDate now = LocalDate.now();
        String month = String.format("%02d", now.minusMonths(1).getMonthValue());
        String year = String.format("%02d", now.minusYears(1).getYear() % 100);
        String owner = faker.name().firstName() + " " + faker.name().lastName();
        String cvc = "123";

        return new CardInfo(cardNumber, month, year, owner, cvc);
    }

    // Невалидный CVV
    public static CardInfo generateInvalidCvv() {
        String cardNumber = "4444 4444 4444 4441";
        String month = "12";
        String year = "25";
        String owner = faker.name().firstName() + " " + faker.name().lastName();
        String cvc = "12";

        return new CardInfo(cardNumber, month, year, owner, cvc);
    }

    // Невалидное имя владельца
    public static CardInfo generateInvalidOwner() {
        String cardNumber = "4444 4444 4444 4441";
        String month = "12";
        String year = "25";
        String owner = "Test123!@#";
        String cvc = "123";

        return new CardInfo(cardNumber, month, year, owner, cvc);
    }

    // Пустая карта
    public static CardInfo generateEmptyCard() {
        return new CardInfo("", "", "", "", "");
    }
}