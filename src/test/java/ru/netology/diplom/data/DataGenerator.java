package ru.netology.diplom.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private static final Faker faker = new Faker(new Locale("en"));
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

    // === ВАЛИДНЫЕ ДАННЫЕ ===
    public static CardInfo generateValidCard(String status) {
        String cardNumber;
        if ("APPROVED".equals(status)) {
            cardNumber = "4444 4444 4444 4441";
        } else {
            cardNumber = "4444 4444 4444 4442";
        }

        LocalDate now = LocalDate.now();
        String month = String.format("%02d", now.getMonthValue());
        String year = String.format("%02d", now.getYear() % 100);
        String owner = faker.name().firstName() + " " + faker.name().lastName();
        String cvc = String.format("%03d", random.nextInt(1000));

        return new CardInfo(cardNumber, month, year, owner, cvc);
    }

    // === НЕГАТИВНЫЕ СЦЕНАРИИ: НОМЕР КАРТЫ ===
    public static CardInfo generateEmptyCardNumber() {
        return new CardInfo("", "12", "25", "Ivan Ivanov", "123");
    }

    public static CardInfo generateShortCardNumber() {
        return new CardInfo("4444 4444 4444", "12", "25", "Ivan Ivanov", "123");
    }

    public static CardInfo generateInvalidCardNumber() {
        return new CardInfo("4444 4444 4444 4443", "12", "25", "Ivan Ivanov", "123");
    }

    // === НЕГАТИВНЫЕ СЦЕНАРИИ: МЕСЯЦ ===
    public static CardInfo generateEmptyMonth() {
        return new CardInfo("4444 4444 4444 4441", "", "25", "Ivan Ivanov", "123");
    }

    public static CardInfo generateOneDigitMonth() {
        return new CardInfo("4444 4444 4444 4441", "1", "25", "Ivan Ivanov", "123");
    }

    public static CardInfo generateMonthGreaterThan12() {
        return new CardInfo("4444 4444 4444 4441", "13", "25", "Ivan Ivanov", "123");
    }

    public static CardInfo generateMonth00() {
        return new CardInfo("4444 4444 4444 4441", "00", "25", "Ivan Ivanov", "123");
    }

    // === НЕГАТИВНЫЕ СЦЕНАРИИ: ГОД ===
    public static CardInfo generateEmptyYear() {
        return new CardInfo("4444 4444 4444 4441", "12", "", "Ivan Ivanov", "123");
    }

    public static CardInfo generateOneDigitYear() {
        return new CardInfo("4444 4444 4444 4441", "12", "5", "Ivan Ivanov", "123");
    }

    public static CardInfo generateYearLessThanCurrent() {
        int year = LocalDate.now().getYear() % 100 - 1;
        return new CardInfo("4444 4444 4444 4441", "12", String.format("%02d", year), "Ivan Ivanov", "123");
    }

    public static CardInfo generateYearTooFarInFuture() {
        int year = LocalDate.now().getYear() % 100 + 6;
        return new CardInfo("4444 4444 4444 4441", "12", String.format("%02d", year), "Ivan Ivanov", "123");
    }

    public static CardInfo generateYear00() {
        return new CardInfo("4444 4444 4444 4441", "12", "00", "Ivan Ivanov", "123");
    }

    // === НЕГАТИВНЫЕ СЦЕНАРИИ: ВЛАДЕЛЕЦ ===
    public static CardInfo generateEmptyOwner() {
        return new CardInfo("4444 4444 4444 4441", "12", "25", "", "123");
    }

    public static CardInfo generateOneWordOwner() {
        return new CardInfo("4444 4444 4444 4441", "12", "25", "Ivan", "123");
    }

    public static CardInfo generateCyrillicOwner() {
        return new CardInfo("4444 4444 4444 4441", "12", "25", "Иван Иванов", "123");
    }

    public static CardInfo generateOwnerWithDigits() {
        return new CardInfo("4444 4444 4444 4441", "12", "25", "Ivan123", "123");
    }

    public static CardInfo generateOwnerWithSpecialChars() {
        return new CardInfo("4444 4444 4444 4441", "12", "25", "Ivan@#$", "123");
    }

    // === НЕГАТИВНЫЕ СЦЕНАРИИ: CVC ===
    public static CardInfo generateEmptyCvc() {
        return new CardInfo("4444 4444 4444 4441", "12", "25", "Ivan Ivanov", "");
    }

    public static CardInfo generateOneDigitCvc() {
        return new CardInfo("4444 4444 4444 4441", "12", "25", "Ivan Ivanov", "1");
    }

    public static CardInfo generateTwoDigitCvc() {
        return new CardInfo("4444 4444 4444 4441", "12", "25", "Ivan Ivanov", "12");
    }

    // === ПУСТЫЕ ПОЛЯ (ИЗОЛИРОВАННЫЕ ПРОВЕРКИ) ===
    public static CardInfo generateAllFieldsEmpty() {
        return new CardInfo("", "", "", "", "");
    }
}