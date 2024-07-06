package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PaymentPage {
    private final SelenideElement numberCard = $(byText("Номер карты")).parent().$(".input__control");
    private final SelenideElement monthCard = $(byText("Месяц")).parent().$(".input__control");
    private final SelenideElement yearCard = $(byText("Год")).parent().$(".input__control");
    private final SelenideElement ownerCard = $(byText("Владелец")).parent().$(".input__control");
    private final SelenideElement numberCVC = $(byText("CVC/CVV")).parent().$(".input__control");
    private final SelenideElement buttonContinue = $(byText("Продолжить"));
    private final SelenideElement messageSuccessful = $(".notification_status_ok");
    private final SelenideElement errorInCardNumber = $(byText("Неверный формат")).parent().$(".input__sub");
    private final SelenideElement errorMonth = $(byText("Неверный формат")).parent().$(".input__sub");
    private final SelenideElement errorInvalidMonth = $(byText("Неверно указан срок действия карты")).parent().$(".input__sub");
    private final SelenideElement errorYear = $(byText("Неверный формат")).parent().$(".input__sub");
    private final SelenideElement errorInvalidYear = $(byText("Истёк срок действия карты")).parent().$(".input__sub");
    private final SelenideElement errorOwner = $(byText("Поле обязательно для заполнения")).parent().$(".input__sub");
    private final SelenideElement errorOwnerInCyrillic = $(byText("Введите фамилию и имя латинскими буквами как на карте")).parent().$(".input__sub");
    private final SelenideElement errorOwnerNumbers = $(byText("Введите фамилию и имя латинскими буквами как на карте")).parent().$(".input__sub");
    private final SelenideElement errorCVC = $(byText("Поле обязательно для заполнения")).parent().$(".input__sub");
    private final SelenideElement errorInvalidCVC = $(byText("Неверный формат")).parent().$(".input__sub");
    private final SelenideElement transactionErrorMessage = $(".notification_status_error");

    public void fillForm(DataGenerator.CardInfo cardInfo) {
        numberCard.setValue(cardInfo.getCardNumber());
        monthCard.setValue(cardInfo.getMonth());
        yearCard.setValue(cardInfo.getYear());
        ownerCard.setValue(cardInfo.getOwner());
        numberCVC.setValue(cardInfo.getCvc());
        buttonContinue.click();
    }

    public void findMessageSuccessful(String expectedText) {
        messageSuccessful.shouldBe(text(expectedText), Duration.ofSeconds(15));
    }

    public void errorForCardEmptyField() {
        errorInCardNumber.shouldBe(visible);
    }

    public void errorForEmptyMonthField() {
        errorMonth.shouldBe(visible);
    }

    public void invalidMonthFieldError() {
        errorInvalidMonth.shouldBe(visible);
    }

    public void errorForEmptyYearField() {
        errorYear.shouldBe(visible);
    }

    public void errorInvalidYear() {
        errorInvalidYear.shouldBe(visible);
    }

    public void errorForEmptyOwnerField() {
        errorOwner.shouldBe(visible);
    }

    public void errorOwnerInCyrillic() {
        errorOwnerInCyrillic.shouldBe(visible);
    }

    public void errorOwnerNumbers() {
        errorOwnerNumbers.shouldBe(visible);
    }

    public void errorForEmptyCVCField() {
        errorCVC.shouldBe(visible);
    }

    public void errorInvalidCVCCVV() {
        errorInvalidCVC.shouldBe(visible);
    }

    public void findErrorTransaction(String expectedText) {
        transactionErrorMessage.shouldBe(text(expectedText), Duration.ofSeconds(15));
    }
}