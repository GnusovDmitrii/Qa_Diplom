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

@DisplayName("Тесты оплаты в кредит")
public class CreditTest {
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
        paymentPage = mainPage.chooseCredit();
    }

    @AfterEach
    void tearDown() {
        DatabaseHelper.cleanDatabase();
        closeWebDriver();
    }

    @Test
    @DisplayName("Позитивный сценарий: кредит с APPROVED картой")
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Кредит")
    @Story("Успешный кредит")
    void shouldSuccessCreditWithApprovedCard() {
        var cardInfo = DataGenerator.generateValidCard("APPROVED");

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkSuccessNotification();

        var creditId = DatabaseHelper.getLastPaymentId();
        assertTrue(creditId.isPresent(), "Кредит должен быть сохранен в БД");

        var status = DatabaseHelper.getCreditStatus(creditId.get());
        assertTrue(status.isPresent(), "Статус кредита должен быть в БД");
        assertEquals("APPROVED", status.get(), "Статус должен быть APPROVED");
    }

    @Test
    @DisplayName("Негативный сценарий: кредит с DECLINED картой")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Кредит")
    @Story("Отказ в кредите")
    void shouldFailCreditWithDeclinedCard() {
        var cardInfo = DataGenerator.generateValidCard("DECLINED");

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkErrorNotification();

        var creditId = DatabaseHelper.getLastPaymentId();
        assertTrue(creditId.isPresent(), "Кредит должен быть сохранен в БД");

        var status = DatabaseHelper.getCreditStatus(creditId.get());
        assertTrue(status.isPresent(), "Статус кредита должен быть в БД");
        assertEquals("DECLINED", status.get(), "Статус должен быть DECLINED");
    }

    @Test
    @DisplayName("Негативный сценарий: невалидный номер карты для кредита")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Валидация полей")
    @Story("Невалидный номер карты")
    void shouldShowErrorForInvalidCardNumberCredit() {
        var cardInfo = DataGenerator.generateInvalidCardNumber();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkCardNumberError("Номер карты должен содержать 16 цифр");
    }

    @Test
    @DisplayName("Негативный сценарий: пустые поля при кредите")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Валидация полей")
    @Story("Пустые поля")
    void shouldShowErrorsForEmptyFieldsCredit() {
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
    @DisplayName("Негативный сценарий: истекший срок для кредита")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Валидация полей")
    @Story("Истекший срок")
    void shouldShowErrorForExpiredCardCredit() {
        var cardInfo = DataGenerator.generateExpiredCard();

        paymentPage.fillCardData(cardInfo)
                .submit()
                .checkYearError("Срок действия карты истек");
    }
}