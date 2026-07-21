package ru.netology.diplom.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import ru.netology.diplom.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class PaymentPage {
    // Поля ввода
    private final SelenideElement cardNumberField = $("[placeholder='0000 0000 0000 0000']");
    private final SelenideElement monthField = $("[placeholder='08']");
    private final SelenideElement yearField = $("[placeholder='22']");
    private final SelenideElement ownerField = $(byText("Владелец")).parent().$(".input__control");
    private final SelenideElement cvcField = $("[placeholder='999']");

    // Кнопка
    private final SelenideElement continueButton = $$(".button").findBy(text("Продолжить"));

    // Уведомления
    private final SelenideElement successNotification = $(".notification_status_ok");
    private final SelenideElement errorNotification = $(".notification_status_error");

    // Элементы ошибок валидации (теперь здесь, а не в тестах)
    private final SelenideElement cardNumberError = $(".input__sub", 0);
    private final SelenideElement monthError = $(".input__sub", 1);
    private final SelenideElement yearError = $(".input__sub", 2);
    private final SelenideElement ownerError = $(".input__sub", 3);
    private final SelenideElement cvcError = $(".input__sub", 4);

    private static final Duration TIMEOUT = Duration.ofSeconds(15);

    public PaymentPage() {
        continueButton.shouldBe(visible);
    }

    @Step("Заполнить форму данными карты")
    public PaymentPage fillCardData(DataGenerator.CardInfo cardInfo) {
        cardNumberField.setValue(cardInfo.getCardNumber());
        monthField.setValue(cardInfo.getMonth());
        yearField.setValue(cardInfo.getYear());
        ownerField.setValue(cardInfo.getOwner());
        cvcField.setValue(cardInfo.getCvc());
        return this;
    }

    @Step("Нажать кнопку 'Продолжить'")
    public PaymentPage submit() {
        continueButton.click();
        return this;
    }

    @Step("Проверить успешное уведомление")
    public PaymentPage checkSuccessNotification() {
        successNotification.shouldBe(visible, TIMEOUT)
                .shouldHave(text("Операция одобрена Банком"), TIMEOUT);
        return this;
    }

    @Step("Проверить уведомление об ошибке")
    public PaymentPage checkErrorNotification() {
        errorNotification.shouldBe(visible, TIMEOUT)
                .shouldHave(text("Ошибка! Банк отказал в проведении операции"), TIMEOUT);
        return this;
    }

    @Step("Проверить ошибку в поле номера карты")
    public PaymentPage checkCardNumberError(String expectedMessage) {
        cardNumberError.shouldBe(visible, TIMEOUT)
                .shouldHave(text(expectedMessage), TIMEOUT);
        return this;
    }

    @Step("Проверить ошибку в поле месяца")
    public PaymentPage checkMonthError(String expectedMessage) {
        monthError.shouldBe(visible, TIMEOUT)
                .shouldHave(text(expectedMessage), TIMEOUT);
        return this;
    }

    @Step("Проверить ошибку в поле года")
    public PaymentPage checkYearError(String expectedMessage) {
        yearError.shouldBe(visible, TIMEOUT)
                .shouldHave(text(expectedMessage), TIMEOUT);
        return this;
    }

    @Step("Проверить ошибку в поле владельца")
    public PaymentPage checkOwnerError(String expectedMessage) {
        ownerError.shouldBe(visible, TIMEOUT)
                .shouldHave(text(expectedMessage), TIMEOUT);
        return this;
    }

    @Step("Проверить ошибку в поле CVC")
    public PaymentPage checkCvcError(String expectedMessage) {
        cvcError.shouldBe(visible, TIMEOUT)
                .shouldHave(text(expectedMessage), TIMEOUT);
        return this;
    }

    // === МЕТОДЫ ДЛЯ ПРОВЕРКИ ИЗОЛИРОВАННЫХ ПУСТЫХ ПОЛЕЙ ===

    @Step("Проверить, что ошибка есть только под полем номера карты")
    public PaymentPage checkErrorOnlyForCardNumber() {
        cardNumberError.shouldBe(visible, TIMEOUT);
        monthError.shouldNotBe(visible);
        yearError.shouldNotBe(visible);
        ownerError.shouldNotBe(visible);
        cvcError.shouldNotBe(visible);
        return this;
    }

    @Step("Проверить, что ошибки есть под всеми полями")
    public PaymentPage checkErrorsForAllFields() {
        cardNumberError.shouldBe(visible, TIMEOUT);
        monthError.shouldBe(visible, TIMEOUT);
        yearError.shouldBe(visible, TIMEOUT);
        ownerError.shouldBe(visible, TIMEOUT);
        cvcError.shouldBe(visible, TIMEOUT);
        return this;
    }

    @Step("Проверить, что ошибки нет под полем номера карты")
    public PaymentPage checkNoCardNumberError() {
        cardNumberError.shouldNotBe(visible);
        return this;
    }

    @Step("Проверить, что ошибки нет под полем месяца")
    public PaymentPage checkNoMonthError() {
        monthError.shouldNotBe(visible);
        return this;
    }

    @Step("Проверить, что ошибки нет под полем года")
    public PaymentPage checkNoYearError() {
        yearError.shouldNotBe(visible);
        return this;
    }

    @Step("Проверить, что ошибки нет под полем владельца")
    public PaymentPage checkNoOwnerError() {
        ownerError.shouldNotBe(visible);
        return this;
    }

    @Step("Проверить, что ошибки нет под полем CVC")
    public PaymentPage checkNoCvcError() {
        cvcError.shouldNotBe(visible);
        return this;
    }
}