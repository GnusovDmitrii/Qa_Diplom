package ru.netology.diplom.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import ru.netology.diplom.data.DataGenerator;
import ru.netology.diplom.db.DatabaseHelper;
import ru.netology.diplom.pages.MainPage;
import ru.netology.diplom.pages.PaymentPage;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Payment Tests")
public class PaymentTest {
    private MainPage mainPage;
    private PaymentPage paymentPage;

    @BeforeEach
    void setUp() {
        DatabaseHelper.cleanDatabase();
        mainPage = new MainPage();
        paymentPage = mainPage.choosePayment();
    }

    @AfterEach
    void tearDown() {
        DatabaseHelper.cleanDatabase();
        // closeWebDriver(); // ← УДАЛЯЕМ - Selenide управляет драйвером сам
    }

    @Test
    @DisplayName("1. Successful payment with APPROVED card")
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Payment")
    @Story("Successful payment")
    void shouldSuccessPaymentWithApprovedCard() {
        var cardInfo = DataGenerator.generateValidCard("APPROVED");

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkSuccessNotification();

        var paymentId = DatabaseHelper.getLastPaymentId();
        assertTrue(paymentId.isPresent(), "Payment should be saved in DB");

        var status = DatabaseHelper.getPaymentStatus(paymentId.get());
        assertTrue(status.isPresent(), "Payment status should be in DB");
        assertEquals("APPROVED", status.get(), "Status should be APPROVED");
    }

    @Test
    @DisplayName("3. Decline with DECLINED card")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Payment")
    @Story("Declined payment")
    void shouldFailPaymentWithDeclinedCard() {
        var cardInfo = DataGenerator.generateValidCard("DECLINED");

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkErrorNotification();

        var paymentId = DatabaseHelper.getLastPaymentId();
        assertTrue(paymentId.isPresent(), "Payment should be saved in DB");

        var status = DatabaseHelper.getPaymentStatus(paymentId.get());
        assertTrue(status.isPresent(), "Payment status should be in DB");
        assertEquals("DECLINED", status.get(), "Status should be DECLINED");
    }

    @Test
    @DisplayName("5. Empty card number")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForEmptyCardNumber() {
        var cardInfo = DataGenerator.getEmptyCardNumber();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkCardNumberError("Field is required");
    }

    @Test
    @DisplayName("6. Card number less than 16 digits")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForShortCardNumber() {
        var cardInfo = DataGenerator.getShortCardNumber();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkCardNumberError("Card number must contain 16 digits");
    }

    @Test
    @DisplayName("7. Card number not in bank DB")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForInvalidCardNumber() {
        var cardInfo = DataGenerator.getInvalidCardNumber();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkErrorNotification();
    }

    @Test
    @DisplayName("11. Empty month")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForEmptyMonth() {
        var cardInfo = DataGenerator.getEmptyMonth();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkMonthError("Field is required");
    }

    @Test
    @DisplayName("12. Month with one digit")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForOneDigitMonth() {
        var cardInfo = DataGenerator.getOneDigitMonth();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkMonthError("Invalid format");
    }

    @Test
    @DisplayName("13. Month greater than 12")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForMonthGreaterThan12() {
        var cardInfo = DataGenerator.getMonthGreaterThan12();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkMonthError("Invalid month");
    }

    @Test
    @DisplayName("14. Month equals 00")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForMonth00() {
        var cardInfo = DataGenerator.getMonth00();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkMonthError("Invalid month");
    }

    @Test
    @DisplayName("19. Empty year")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForEmptyYear() {
        var cardInfo = DataGenerator.getEmptyYear();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkYearError("Field is required");
    }

    @Test
    @DisplayName("20. Year with one digit")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForOneDigitYear() {
        var cardInfo = DataGenerator.getOneDigitYear();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkYearError("Invalid format");
    }

    @Test
    @DisplayName("21. Year less than current")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForYearLessThanCurrent() {
        var cardInfo = DataGenerator.getYearLessThanCurrent();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkYearError("Card has expired");
    }

    @Test
    @DisplayName("22. Year 5+ years ahead")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForYearTooFarInFuture() {
        var cardInfo = DataGenerator.getYearTooFarInFuture();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkYearError("Invalid year");
    }

    @Test
    @DisplayName("23. Year equals 00")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForYear00() {
        var cardInfo = DataGenerator.getYear00();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkYearError("Card has expired");
    }

    @Test
    @DisplayName("29. Empty owner")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForEmptyOwner() {
        var cardInfo = DataGenerator.getEmptyOwner();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkOwnerError("Field is required");
    }

    @Test
    @DisplayName("30. Owner with one word")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForOneWordOwner() {
        var cardInfo = DataGenerator.getOneWordOwner();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkOwnerError("Enter first and last name");
    }

    @Test
    @DisplayName("31. Owner with Cyrillic")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForCyrillicOwner() {
        var cardInfo = DataGenerator.getCyrillicOwner();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkOwnerError("Name must contain only Latin letters");
    }

    @Test
    @DisplayName("32. Owner with digits")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForOwnerWithDigits() {
        var cardInfo = DataGenerator.getOwnerWithDigits();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkOwnerError("Name must contain only letters");
    }

    @Test
    @DisplayName("33. Owner with special chars")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForOwnerWithSpecialChars() {
        var cardInfo = DataGenerator.getOwnerWithSpecialChars();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkOwnerError("Name contains invalid characters");
    }

    @Test
    @DisplayName("39. Empty CVC")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForEmptyCvc() {
        var cardInfo = DataGenerator.getEmptyCvc();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkCvcError("Field is required");
    }

    @Test
    @DisplayName("40. CVC with one digit")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForOneDigitCvc() {
        var cardInfo = DataGenerator.getOneDigitCvc();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkCvcError("CVC must contain 3 digits");
    }

    @Test
    @DisplayName("41. CVC with two digits")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorForTwoDigitCvc() {
        var cardInfo = DataGenerator.getTwoDigitCvc();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkCvcError("CVC must contain 3 digits");
    }

    @Test
    @DisplayName("45. Only card number empty")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorOnlyForCardNumberEmpty() {
        var cardInfo = DataGenerator.getEmptyCardNumber();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkErrorOnlyForCardNumber();
    }

    @Test
    @DisplayName("50. All fields empty")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Validation")
    void shouldShowErrorsForAllFieldsEmpty() {
        var cardInfo = DataGenerator.getAllFieldsEmpty();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkErrorsForAllFields();
    }
}