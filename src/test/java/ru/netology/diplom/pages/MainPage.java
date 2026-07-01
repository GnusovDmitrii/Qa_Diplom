package ru.netology.diplom.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class MainPage {
    private final SelenideElement buyButton = $$(".button").first();
    private final SelenideElement creditButton = $$(".button").last();
    private final SelenideElement heading = $("h2");

    @Step("Открыть главную страницу")
    public MainPage openPage() {
        open("http://localhost:8080");
        heading.shouldBe(visible);
        return this;
    }

    @Step("Выбрать оплату по карте")
    public PaymentPage choosePayment() {
        buyButton.click();
        return new PaymentPage();
    }

    @Step("Выбрать оплату в кредит")
    public PaymentPage chooseCredit() {
        creditButton.click();
        return new PaymentPage();
    }
}