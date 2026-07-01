package ru.netology.diplom.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import ru.netology.diplom.data.DataGenerator;
import ru.netology.diplom.db.DatabaseHelper;
import ru.netology.diplom.pages.MainPage;
import ru.netology.diplom.pages.PaymentPage;

// ✅ ДОБАВЬТЕ ЭТИ ИМПОРТЫ
import static org.junit.jupiter.api.Assertions.*;

import static com.codeborne.selenide.Selenide.closeWebDriver;

@DisplayName("Тесты оплаты по карте")
public class PaymentTest {
    private MainPage mainPage;
    private PaymentPage paymentPage;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
        System.setProperty("selenide.browser", "chrome");
        System.setProperty("selenide.headless", "false");
        System.setProperty("selenide.timeout", "10000");
    }

    @BeforeEach
    void setUp() {
        DatabaseHelper.cleanDatabase();
        mainPage = new MainPage();
        mainPage.openPage();
        paymentPage = mainPage.choosePayment();
    }

    @AfterEach
    void tearDown() {
        DatabaseHelper.cleanDatabase();
        closeWebDriver();
    }

    @Test
    @DisplayName("Позитивный сценарий: успешная оплата с APPROVED картой")
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Оплата по карте")
    @Story("Успешная оплата")
    void shouldSuccessPaymentWithApprovedCard() {
        var cardInfo = DataGenerator.generateValidCard("APPROVED");

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkSuccessNotification();

        var paymentId = DatabaseHelper.getLastPaymentId();
        assertTrue(paymentId.isPresent(), "Платеж должен быть сохранен в БД");

        var status = DatabaseHelper.getPaymentStatus(paymentId.get());
        assertTrue(status.isPresent(), "Статус платежа должен быть в БД");
        assertEquals("APPROVED", status.get(), "Статус должен быть APPROVED");
    }

    @Test
    @DisplayName("Негативный сценарий: отказ с DECLINED картой")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Оплата по карте")
    @Story("Отказ в оплате")
    void shouldFailPaymentWithDeclinedCard() {
        var cardInfo = DataGenerator.generateValidCard("DECLINED");

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkErrorNotification();

        var paymentId = DatabaseHelper.getLastPaymentId();
        assertTrue(paymentId.isPresent(), "Платеж должен быть сохранен в БД");

        var status = DatabaseHelper.getPaymentStatus(paymentId.get());
        assertTrue(status.isPresent(), "Статус платежа должен быть в БД");
        assertEquals("DECLINED", status.get(), "Статус должен быть DECLINED");
    }

    @Test
    @DisplayName("Негативный сценарий: невалидный номер карты")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Валидация полей")
    @Story("Невалидный номер карты")
    void shouldShowErrorForInvalidCardNumber() {
        var cardInfo = DataGenerator.generateInvalidCardNumber();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkCardNumberError("Номер карты должен содержать 16 цифр");
    }

    @Test
    @DisplayName("Негативный сценарий: пустые поля")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Валидация полей")
    @Story("Пустые поля")
    void shouldShowErrorsForEmptyFields() {
        var cardInfo = DataGenerator.generateEmptyCard();

        paymentPage.fillCardData(cardInfo)
                .submit();

        paymentPage.checkCardNumberError("Поле обязательно для заполнения")
                .checkMonthError("Поле обязательно для заполнения")
                .checkYearError("Поле обязательно для заполнения")
                .checkOwnerError("Поле обязательно для заполнения")
                .checkCvcError("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Негативный сценарий: истекший срок действия карты")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Валидация полей")
    @Story("Истекший срок действия")
    void shouldShowErrorForExpiredCard() {
        var cardInfo = DataGenerator.generateExpiredCard();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkYearError("Срок действия карты истек");
    }

    @Test
    @DisplayName("Негативный сценарий: невалидный месяц")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Валидация полей")
    @Story("Невалидный месяц")
    void shouldShowErrorForInvalidMonth() {
        var cardInfo = DataGenerator.generateInvalidMonth();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkMonthError("Неверный месяц");
    }

    @Test
    @DisplayName("Негативный сценарий: невалидный год (истекший)")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Валидация полей")
    @Story("Невалидный год")
    void shouldShowErrorForInvalidYear() {
        var cardInfo = DataGenerator.generateInvalidYear();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkYearError("Неверный год");
    }

    @Test
    @DisplayName("Негативный сценарий: невалидный CVV")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Валидация полей")
    @Story("Невалидный CVV")
    void shouldShowErrorForInvalidCvv() {
        var cardInfo = DataGenerator.generateInvalidCvv();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkCvcError("CVV должен содержать 3 цифры");
    }

    @Test
    @DisplayName("Негативный сценарий: невалидное имя владельца")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Валидация полей")
    @Story("Невалидное имя владельца")
    void shouldShowErrorForInvalidOwner() {
        var cardInfo = DataGenerator.generateInvalidOwner();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkOwnerError("Имя должно содержать только буквы");
    }
}