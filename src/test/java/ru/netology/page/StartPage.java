package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class StartPage {
    private final SelenideElement paymentButton = $(byText("Купить"));
    private final SelenideElement paymentByCardText = $(byText("Оплата по карте"));

    public PaymentPage payByDebitCard() {
        paymentButton.click();
        paymentByCardText.shouldBe(Condition.visible);
        return new PaymentPage();
    }
}
