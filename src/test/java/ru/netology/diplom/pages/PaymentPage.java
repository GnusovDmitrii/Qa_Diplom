package ru.netology.diplom.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import ru.netology.diplom.data.DataGenerator;

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

    // Кнопка "Продолжить"
    private final SelenideElement continueButton = $$(".button").findBy(text("Продолжить"));

    // Уведомления
    private final SelenideElement successNotification = $(".notification_status_ok");
    private final SelenideElement errorNotification = $(".notification_status_error");

    // Подсказки об ошибках
    private final SelenideElement cardNumberError = $(".input__sub", 0);
    private final SelenideElement monthError = $(".input__sub", 1);
    private final SelenideElement yearError = $(".input__sub", 2);
    private final SelenideElement ownerError = $(".input__sub", 3);
    private final SelenideElement cvcError = $(".input__sub", 4);

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
        successNotification.shouldBe(visible).shouldHave(text("Операция одобрена Банком"));
        return this;
    }

    @Step("Проверить уведомление об ошибке")
    public PaymentPage checkErrorNotification() {
        errorNotification.shouldBe(visible).shouldHave(text("Ошибка! Банк отказал в проведении операции"));
        return this;
    }

    @Step("Проверить наличие ошибки валидации в поле номера карты")
    public PaymentPage checkCardNumberError(String expectedMessage) {
        cardNumberError.shouldBe(visible).shouldHave(text(expectedMessage));
        return this;
    }

    @Step("Проверить наличие ошибки валидации в поле месяца")
    public PaymentPage checkMonthError(String expectedMessage) {
        monthError.shouldBe(visible).shouldHave(text(expectedMessage));
        return this;
    }

    @Step("Проверить наличие ошибки валидации в поле года")
    public PaymentPage checkYearError(String expectedMessage) {
        yearError.shouldBe(visible).shouldHave(text(expectedMessage));
        return this;
    }

    @Step("Проверить наличие ошибки валидации в поле владельца")
    public PaymentPage checkOwnerError(String expectedMessage) {
        ownerError.shouldBe(visible).shouldHave(text(expectedMessage));
        return this;
    }

    @Step("Проверить наличие ошибки валидации в поле CVC")
    public PaymentPage checkCvcError(String expectedMessage) {
        cvcError.shouldBe(visible).shouldHave(text(expectedMessage));
        return this;
    }

    @Step("Проверить, что все поля валидны")
    public PaymentPage checkAllFieldsValid() {
        $(".input_invalid").shouldNotBe(visible);
        return this;
    }
}