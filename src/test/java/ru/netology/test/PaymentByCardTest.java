package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;
import ru.netology.data.SQLHelper;
import ru.netology.page.PaymentPage;
import ru.netology.page.StartPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataGenerator.*;
import static ru.netology.data.SQLHelper.cleanDatabase;


public class PaymentByCardTest {

    @AfterAll
    static void tearDownAll() {
        cleanDatabase();
    }

    @BeforeEach
    void setup() {
        open("http://localhost:8080");
    }

    @Test
    void shouldBeSuccessfulTransaction() {
        val startPage = new StartPage();
        val paymentPage = startPage.payByDebitCard();
        val cardInfo = DataGenerator.generateInfoCard();
        paymentPage.fillForm(cardInfo);
        paymentPage.findMessageSuccessful("Успешно Операция одобрена Банком.");
    }

    @Test
    void shouldNotPaymentWithoutCardNumber() { // пустое поле "Номер карты"
        val startPage = new StartPage();
        val paymentPage = startPage.payByDebitCard();
        val cardInfo = new DataGenerator.CardInfo(getEmptyCardNumberField(), generateMonth(), generateYear(),
                generateOwner(), String.valueOf(generateCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.errorForCardEmptyField();
    }

    @Test
    void shouldNotPaymentWithIncompleteCardNumber() { // неполный номер карты
        val startPage = new StartPage();
        val paymentPage = startPage.payByDebitCard();
        val cardInfo = new DataGenerator.CardInfo(getIncompleteCardNumber(), generateMonth(), generateYear(),
                generateOwner(), String.valueOf(generateCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.errorForCardEmptyField();
    }

    @Test
    void shouldNotPaymentWithoutMonth() { // пустое поле "Месяц"
        val startPage = new StartPage();
        val paymentPage = startPage.payByDebitCard();
        val cardInfo = new DataGenerator.CardInfo(getCardNumber(), getEmptyMonthField(), generateYear(), generateOwner(),
                String.valueOf(generateCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.errorForEmptyMonthField();
    }

    @Test
    void shouldNotMonthZeros() { // заполнение поля "Месяц" нулями
        val startPage = new StartPage();
        val paymentPage = startPage.payByDebitCard();
        val cardInfo = new DataGenerator.CardInfo(getCardNumber(), getMonthWithZeros(), generateYear(), generateOwner(),
                String.valueOf(generateCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.invalidMonthFieldError();
    }

    @Test
    void monthShouldNotBeInvalid() { // заполнение поля "Месяц" цифрой 13
        val startPage = new StartPage();
        val paymentPage = startPage.payByDebitCard();
        val cardInfo = new DataGenerator.CardInfo(getCardNumber(), getInvalidMonth(), generateYear(), generateOwner(),
                String.valueOf(generateCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.invalidMonthFieldError();
    }

    @Test
    void shouldNotPaymentWithoutYear() { // пустое поле "Год"
        val startPage = new StartPage();
        val paymentPage = startPage.payByDebitCard();
        val cardInfo = new DataGenerator.CardInfo(getCardNumber(), generateMonth(), getEmptyYearField(), generateOwner(),
                String.valueOf(generateCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.errorForEmptyYearField();
    }

    @Test
    void yearShouldNotBeInvalid() { // заполнение поля "Год" прошедшим годом
        val startPage = new StartPage();
        val paymentPage = startPage.payByDebitCard();
        val cardInfo = new DataGenerator.CardInfo(getCardNumber(), generateMonth(), getInvalidYear(), generateOwner(),
                String.valueOf(generateCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.errorInvalidYear();
    }

    @Test
    void shouldNotPaymentWithoutOwner() { // пустое поле "Владелец"
        val startPage = new StartPage();
        val paymentPage = startPage.payByDebitCard();
        val cardInfo = new DataGenerator.CardInfo(getCardNumber(), generateMonth(), generateYear(), getEmptyOwnerField(),
                String.valueOf(generateCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.errorForEmptyOwnerField();
    }

    @Test
    void ownerShouldNotBeInCyrillic() { // заполнение поля "Владелец" на кириллице
        val startPage = new StartPage();
        val paymentPage = startPage.payByDebitCard();
        val cardInfo = new DataGenerator.CardInfo(getCardNumber(), generateMonth(), generateYear(), getOwnerInCyrillic(),
                String.valueOf(generateCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.errorOwnerInCyrillic();
    }

    @Test
    void ownerShouldNotBeInvalid() { // заполнение поля "Владелец" цифрами
        val startPage = new StartPage();
        val paymentPage = startPage.payByDebitCard();
        val cardInfo = new DataGenerator.CardInfo(getCardNumber(), generateMonth(), generateYear(), getOwnerNumbers(),
                String.valueOf(generateCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.errorOwnerNumbers();
    }

    @Test
    void shouldNotPaymentWithoutCVC() { // пустое поле "CVC/CVV"
        val startPage = new StartPage();
        val paymentPage = startPage.payByDebitCard();
        val cardInfo = new DataGenerator.CardInfo(getCardNumber(), generateMonth(), generateYear(), generateOwner(), getEmptyCVCField());
        paymentPage.fillForm(cardInfo);
        paymentPage.errorForEmptyCVCField();
    }

    @Test
    void CVCShouldNotBeInvalid() { // заполнение поля "CVC/CVV" из двух цифр
        val startPage = new StartPage();
        val paymentPage = startPage.payByDebitCard();
        val cardInfo = new DataGenerator.CardInfo(getCardNumber(), generateMonth(), generateYear(), generateOwner(),
                String.valueOf(getInvalidCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.errorInvalidCVCCVV();
    }

    @Test
    void shouldBeTransactionError() {
        val startPage = new StartPage();
        val paymentPage = startPage.payByDebitCard();
        val cardInfo = new DataGenerator.CardInfo(getInvalidCardNumber(), generateMonth(), generateYear(), generateOwner(),
                String.valueOf(generateCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.findErrorTransaction("Ошибка Ошибка! Банк отказал в проведении операции.");
    }

    @Test
    @DisplayName("Should be transaction successful")
    void shouldSuccessfulTransactionInDatabase() {
        val startPage = new StartPage();
        val paymentPage = startPage.payByDebitCard();
        val cardInfo = new DataGenerator.CardInfo(getCardNumber(), generateMonth(), generateYear(), generateOwner(),
                String.valueOf(generateCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.findMessageSuccessful("Успешно Операция одобрена Банком.");
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());
    }
}