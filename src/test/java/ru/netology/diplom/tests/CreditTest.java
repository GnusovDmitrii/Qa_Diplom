package ru.netology.diplom.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import ru.netology.diplom.data.DataGenerator;
import ru.netology.diplom.db.DatabaseHelper;
import ru.netology.diplom.pages.MainPage;
import ru.netology.diplom.pages.PaymentPage;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Credit Tests")
public class CreditTest {
    private MainPage mainPage;
    private PaymentPage paymentPage;

    @BeforeEach
    void setUp() {
        DatabaseHelper.cleanDatabase();
        mainPage = new MainPage();
        paymentPage = mainPage.chooseCredit();
    }

    @AfterEach
    void tearDown() {
        DatabaseHelper.cleanDatabase();
        // closeWebDriver(); // ← УДАЛЯЕМ - Selenide управляет драйвером сам
    }

    @Test
    @DisplayName("2. Successful credit with APPROVED card")
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Credit")
    @Story("Successful credit")
    void shouldSuccessCreditWithApprovedCard() {
        var cardInfo = DataGenerator.generateValidCard("APPROVED");

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkSuccessNotification();

        var creditId = DatabaseHelper.getLastCreditId();
        assertTrue(creditId.isPresent(), "Credit should be saved in DB");

        var status = DatabaseHelper.getCreditStatus(creditId.get());
        assertTrue(status.isPresent(), "Credit status should be in DB");
        assertEquals("APPROVED", status.get(), "Status should be APPROVED");
    }

    @Test
    @DisplayName("4. Decline with DECLINED card in credit")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Credit")
    @Story("Declined credit")
    void shouldFailCreditWithDeclinedCard() {
        var cardInfo = DataGenerator.generateValidCard("DECLINED");

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkErrorNotification();

        var creditId = DatabaseHelper.getLastCreditId();
        assertTrue(creditId.isPresent(), "Credit should be saved in DB");

        var status = DatabaseHelper.getCreditStatus(creditId.get());
        assertTrue(status.isPresent(), "Credit status should be in DB");
        assertEquals("DECLINED", status.get(), "Status should be DECLINED");
    }

    @Test
    @DisplayName("8. Credit: empty card number")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForEmptyCardNumberCredit() {
        var cardInfo = DataGenerator.getEmptyCardNumber();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkCardNumberError("Field is required");
    }

    @Test
    @DisplayName("9. Credit: card number less than 16 digits")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForShortCardNumberCredit() {
        var cardInfo = DataGenerator.getShortCardNumber();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkCardNumberError("Card number must contain 16 digits");
    }

    @Test
    @DisplayName("10. Credit: card number not in bank DB")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForInvalidCardNumberCredit() {
        var cardInfo = DataGenerator.getInvalidCardNumber();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkErrorNotification();
    }

    @Test
    @DisplayName("15. Credit: empty month")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForEmptyMonthCredit() {
        var cardInfo = DataGenerator.getEmptyMonth();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkMonthError("Field is required");
    }

    @Test
    @DisplayName("16. Credit: month with one digit")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForOneDigitMonthCredit() {
        var cardInfo = DataGenerator.getOneDigitMonth();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkMonthError("Invalid format");
    }

    @Test
    @DisplayName("17. Credit: month greater than 12")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForMonthGreaterThan12Credit() {
        var cardInfo = DataGenerator.getMonthGreaterThan12();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkMonthError("Invalid month");
    }

    @Test
    @DisplayName("18. Credit: month equals 00")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForMonth00Credit() {
        var cardInfo = DataGenerator.getMonth00();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkMonthError("Invalid month");
    }

    @Test
    @DisplayName("24. Credit: empty year")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForEmptyYearCredit() {
        var cardInfo = DataGenerator.getEmptyYear();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkYearError("Field is required");
    }

    @Test
    @DisplayName("25. Credit: year with one digit")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForOneDigitYearCredit() {
        var cardInfo = DataGenerator.getOneDigitYear();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkYearError("Invalid format");
    }

    @Test
    @DisplayName("26. Credit: year less than current")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForYearLessThanCurrentCredit() {
        var cardInfo = DataGenerator.getYearLessThanCurrent();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkYearError("Card has expired");
    }

    @Test
    @DisplayName("27. Credit: year 5+ years ahead")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForYearTooFarInFutureCredit() {
        var cardInfo = DataGenerator.getYearTooFarInFuture();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkYearError("Invalid year");
    }

    @Test
    @DisplayName("28. Credit: year equals 00")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForYear00Credit() {
        var cardInfo = DataGenerator.getYear00();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkYearError("Card has expired");
    }

    @Test
    @DisplayName("34. Credit: empty owner")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForEmptyOwnerCredit() {
        var cardInfo = DataGenerator.getEmptyOwner();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkOwnerError("Field is required");
    }

    @Test
    @DisplayName("35. Credit: owner with one word")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForOneWordOwnerCredit() {
        var cardInfo = DataGenerator.getOneWordOwner();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkOwnerError("Enter first and last name");
    }

    @Test
    @DisplayName("36. Credit: owner with Cyrillic")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForCyrillicOwnerCredit() {
        var cardInfo = DataGenerator.getCyrillicOwner();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkOwnerError("Name must contain only Latin letters");
    }

    @Test
    @DisplayName("37. Credit: owner with digits")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForOwnerWithDigitsCredit() {
        var cardInfo = DataGenerator.getOwnerWithDigits();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkOwnerError("Name must contain only letters");
    }

    @Test
    @DisplayName("38. Credit: owner with special chars")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForOwnerWithSpecialCharsCredit() {
        var cardInfo = DataGenerator.getOwnerWithSpecialChars();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkOwnerError("Name contains invalid characters");
    }

    @Test
    @DisplayName("42. Credit: empty CVC")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForEmptyCvcCredit() {
        var cardInfo = DataGenerator.getEmptyCvc();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkCvcError("Field is required");
    }

    @Test
    @DisplayName("43. Credit: CVC with one digit")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForOneDigitCvcCredit() {
        var cardInfo = DataGenerator.getOneDigitCvc();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkCvcError("CVC must contain 3 digits");
    }

    @Test
    @DisplayName("44. Credit: CVC with two digits")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForTwoDigitCvcCredit() {
        var cardInfo = DataGenerator.getTwoDigitCvc();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkCvcError("CVC must contain 3 digits");
    }

    @Test
    @DisplayName("51. Credit: only card number empty")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorOnlyForCardNumberEmptyCredit() {
        var cardInfo = DataGenerator.getEmptyCardNumber();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkErrorOnlyForCardNumber();
    }

    @Test
    @DisplayName("56. Credit: all fields empty")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorsForAllFieldsEmptyCredit() {
        var cardInfo = DataGenerator.getAllFieldsEmpty();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkErrorsForAllFields();
    }
}