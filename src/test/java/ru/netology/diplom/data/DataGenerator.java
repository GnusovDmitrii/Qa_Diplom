package ru.netology.diplom.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private static final Faker faker = new Faker(new Locale("en"));
    private static final Random random = new Random();
    private static final LocalDate now = LocalDate.now();

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


    // === НОМЕР КАРТЫ ===

    public static String generateValidCardNumber() {
        return String.format("%04d %04d %04d %04d",
                random.nextInt(10000),
                random.nextInt(10000),
                random.nextInt(10000),
                random.nextInt(10000));
    }

    public static String generateCardNumberByStatus(String status) {
        if ("APPROVED".equals(status)) {
            return "4444 4444 4444 4441";
        } else {
            return "4444 4444 4444 4442";
        }
    }

    public static String generateShortCardNumberString() {
        int length = 8 + random.nextInt(7);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static String generateInvalidCardNumberString() {
        String number;
        do {
            number = String.format("%04d%04d%04d%04d",
                    random.nextInt(10000),
                    random.nextInt(10000),
                    random.nextInt(10000),
                    random.nextInt(10000));
        } while (number.equals("4444444444444441") ||
                number.equals("4444444444444442"));
        return number;
    }

    // === МЕСЯЦ ===

    public static String generateValidMonthString() {
        int month = now.getMonthValue() + random.nextInt(12 - now.getMonthValue() + 1);
        return String.format("%02d", month);
    }

    public static String generateOneDigitMonthString() {
        return String.valueOf(1 + random.nextInt(9));
    }

    public static String generateMonthGreaterThan12String() {
        return String.valueOf(13 + random.nextInt(6));
    }

    public static String generateMonth00String() {
        return "00";
    }

    public static String generateEmptyMonthString() {
        return "";
    }

    // === ГОД ===

    public static String generateValidYearString() {
        int year = now.getYear() % 100 + random.nextInt(6);
        return String.format("%02d", year);
    }

    public static String generateOneDigitYearString() {
        return String.valueOf(random.nextInt(10));
    }

    public static String generateYearLessThanCurrentString() {
        int year = now.getYear() % 100 - 1 - random.nextInt(5);
        if (year < 0) year = 0;
        return String.format("%02d", year);
    }

    public static String generateYearTooFarInFutureString() {
        int year = now.getYear() % 100 + 6 + random.nextInt(5);
        return String.format("%02d", year);
    }

    public static String generateYear00String() {
        return "00";
    }

    public static String generateEmptyYearString() {
        return "";
    }

    // === ВЛАДЕЛЕЦ ===

    public static String generateValidOwnerString() {
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    public static String generateOneWordOwnerString() {
        return faker.name().firstName();
    }

    public static String generateCyrillicOwnerString() {
        Faker russianFaker = new Faker(new Locale("ru"));
        return russianFaker.name().firstName() + " " + russianFaker.name().lastName();
    }

    public static String generateOwnerWithDigitsString() {
        return faker.name().firstName() + random.nextInt(100);
    }

    public static String generateOwnerWithSpecialCharsString() {
        String[] specialChars = {"@#$", "!@#", "$$$", "%%%", "&**", "(){}", "[]"};
        return faker.name().firstName() + specialChars[random.nextInt(specialChars.length)];
    }

    public static String generateEmptyOwnerString() {
        return "";
    }

    // === CVC ===

    public static String generateValidCvcString() {
        return String.format("%03d", 1 + random.nextInt(999));
    }

    public static String generateOneDigitCvcString() {
        return String.valueOf(random.nextInt(10));
    }

    public static String generateTwoDigitCvcString() {
        return String.format("%02d", 1 + random.nextInt(99));
    }

    public static String generateEmptyCvcString() {
        return "";
    }


    // === ВАЛИДНЫЕ ДАННЫЕ ===

    public static CardInfo generateValidCard(String status) {
        return new CardInfo(
                generateCardNumberByStatus(status),
                generateValidMonthString(),
                generateValidYearString(),
                generateValidOwnerString(),
                generateValidCvcString()
        );
    }

    // === НЕГАТИВНЫЕ СЦЕНАРИИ: НОМЕР КАРТЫ ===

    public static CardInfo getEmptyCardNumber() {
        return new CardInfo(
                "",
                generateValidMonthString(),
                generateValidYearString(),
                generateValidOwnerString(),
                generateValidCvcString()
        );
    }

    public static CardInfo getShortCardNumber() {
        return new CardInfo(
                generateShortCardNumberString(),
                generateValidMonthString(),
                generateValidYearString(),
                generateValidOwnerString(),
                generateValidCvcString()
        );
    }

    public static CardInfo getInvalidCardNumber() {
        return new CardInfo(
                generateInvalidCardNumberString(),
                generateValidMonthString(),
                generateValidYearString(),
                generateValidOwnerString(),
                generateValidCvcString()
        );
    }

    // === НЕГАТИВНЫЕ СЦЕНАРИИ: МЕСЯЦ ===

    public static CardInfo getEmptyMonth() {
        return new CardInfo(
                generateValidCardNumber(),
                "",
                generateValidYearString(),
                generateValidOwnerString(),
                generateValidCvcString()
        );
    }

    public static CardInfo getOneDigitMonth() {
        return new CardInfo(
                generateValidCardNumber(),
                generateOneDigitMonthString(),
                generateValidYearString(),
                generateValidOwnerString(),
                generateValidCvcString()
        );
    }

    public static CardInfo getMonthGreaterThan12() {
        return new CardInfo(
                generateValidCardNumber(),
                generateMonthGreaterThan12String(),
                generateValidYearString(),
                generateValidOwnerString(),
                generateValidCvcString()
        );
    }

    public static CardInfo getMonth00() {
        return new CardInfo(
                generateValidCardNumber(),
                "00",
                generateValidYearString(),
                generateValidOwnerString(),
                generateValidCvcString()
        );
    }

    // === НЕГАТИВНЫЕ СЦЕНАРИИ: ГОД ===

    public static CardInfo getEmptyYear() {
        return new CardInfo(
                generateValidCardNumber(),
                generateValidMonthString(),
                "",
                generateValidOwnerString(),
                generateValidCvcString()
        );
    }

    public static CardInfo getOneDigitYear() {
        return new CardInfo(
                generateValidCardNumber(),
                generateValidMonthString(),
                generateOneDigitYearString(),
                generateValidOwnerString(),
                generateValidCvcString()
        );
    }

    public static CardInfo getYearLessThanCurrent() {
        return new CardInfo(
                generateValidCardNumber(),
                generateValidMonthString(),
                generateYearLessThanCurrentString(),
                generateValidOwnerString(),
                generateValidCvcString()
        );
    }

    public static CardInfo getYearTooFarInFuture() {
        return new CardInfo(
                generateValidCardNumber(),
                generateValidMonthString(),
                generateYearTooFarInFutureString(),
                generateValidOwnerString(),
                generateValidCvcString()
        );
    }

    public static CardInfo getYear00() {
        return new CardInfo(
                generateValidCardNumber(),
                generateValidMonthString(),
                "00",
                generateValidOwnerString(),
                generateValidCvcString()
        );
    }

    // === НЕГАТИВНЫЕ СЦЕНАРИИ: ВЛАДЕЛЕЦ ===

    public static CardInfo getEmptyOwner() {
        return new CardInfo(
                generateValidCardNumber(),
                generateValidMonthString(),
                generateValidYearString(),
                "",
                generateValidCvcString()
        );
    }

    public static CardInfo getOneWordOwner() {
        return new CardInfo(
                generateValidCardNumber(),
                generateValidMonthString(),
                generateValidYearString(),
                generateOneWordOwnerString(),
                generateValidCvcString()
        );
    }

    public static CardInfo getCyrillicOwner() {
        return new CardInfo(
                generateValidCardNumber(),
                generateValidMonthString(),
                generateValidYearString(),
                generateCyrillicOwnerString(),
                generateValidCvcString()
        );
    }

    public static CardInfo getOwnerWithDigits() {
        return new CardInfo(
                generateValidCardNumber(),
                generateValidMonthString(),
                generateValidYearString(),
                generateOwnerWithDigitsString(),
                generateValidCvcString()
        );
    }

    public static CardInfo getOwnerWithSpecialChars() {
        return new CardInfo(
                generateValidCardNumber(),
                generateValidMonthString(),
                generateValidYearString(),
                generateOwnerWithSpecialCharsString(),
                generateValidCvcString()
        );
    }

    // === НЕГАТИВНЫЕ СЦЕНАРИИ: CVC ===

    public static CardInfo getEmptyCvc() {
        return new CardInfo(
                generateValidCardNumber(),
                generateValidMonthString(),
                generateValidYearString(),
                generateValidOwnerString(),
                ""
        );
    }

    public static CardInfo getOneDigitCvc() {
        return new CardInfo(
                generateValidCardNumber(),
                generateValidMonthString(),
                generateValidYearString(),
                generateValidOwnerString(),
                generateOneDigitCvcString()
        );
    }

    public static CardInfo getTwoDigitCvc() {
        return new CardInfo(
                generateValidCardNumber(),
                generateValidMonthString(),
                generateValidYearString(),
                generateValidOwnerString(),
                generateTwoDigitCvcString()
        );
    }

    // === ПУСТЫЕ ПОЛЯ (ИЗОЛИРОВАННЫЕ ПРОВЕРКИ) ===

    public static CardInfo getAllFieldsEmpty() {
        return new CardInfo("", "", "", "", "");
    }

    // === ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ ДЛЯ ТЕСТОВ ===

    public static String getApprovedCardNumber() {
        return "4444 4444 4444 4441";
    }

    public static String getDeclinedCardNumber() {
        return "4444 4444 4444 4442";
    }
}