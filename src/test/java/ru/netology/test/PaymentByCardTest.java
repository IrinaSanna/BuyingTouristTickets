package ru.netology.test;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.data.DataGenerator;
import ru.netology.data.SQLHelper;
import ru.netology.page.PaymentPage;
import ru.netology.page.StartPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataGenerator.*;
import static ru.netology.data.SQLHelper.cleanDatabase;


public class PaymentByCardTest {

    @BeforeAll
    static void tearDownAll() {
        cleanDatabase();
    }

    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAllReport() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:8080");
    }

    @Test
    @DisplayName("Successful transaction")
    void shouldBeSuccessfulTransaction() {
        var startPage = new StartPage();
        var paymentPage = startPage.payByDebitCard();
        var cardInfo = DataGenerator.generateInfoCard();
        paymentPage.fillForm(cardInfo);
        paymentPage.findMessageTransaction("Успешно Операция одобрена Банком.");
    }

    @Test
    @DisplayName("Empty card number field")
    void shouldNotPaymentWithoutCardNumber() {
        var startPage = new StartPage();
        var paymentPage = startPage.payByDebitCard();
        var cardInfo = new DataGenerator.CardInfo(getEmptyCardNumberField(), generateMonth(), generateYear(),
                generateOwner(), String.valueOf(generateCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.errorForCardEmptyField();
    }

    @Test
    @DisplayName("Incomplete card number")
    void shouldNotPaymentWithIncompleteCardNumber() {
        var startPage = new StartPage();
        var paymentPage = startPage.payByDebitCard();
        var cardInfo = new DataGenerator.CardInfo(getIncompleteCardNumber(), generateMonth(), generateYear(),
                generateOwner(), String.valueOf(generateCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.errorForCardEmptyField();
    }

    @Test
    @DisplayName("Empty month field")
    void shouldNotPaymentWithoutMonth() {
        var startPage = new StartPage();
        var paymentPage = startPage.payByDebitCard();
        var cardInfo = new DataGenerator.CardInfo(getCardNumber(), getEmptyMonthField(), generateYear(), generateOwner(),
                String.valueOf(generateCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.errorForEmptyMonthField();
    }

    @Test
    @DisplayName("Filling the month field with zeros")
    void shouldNotMonthZeros() {
        var startPage = new StartPage();
        var paymentPage = startPage.payByDebitCard();
        var cardInfo = new DataGenerator.CardInfo(getCardNumber(), getMonthWithZeros(), generateYear(), generateOwner(),
                String.valueOf(generateCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.invalidMonthFieldError();
    }

    @Test
    @DisplayName("Filling the month field with the number 13")
    void monthShouldNotBeInvalid() {
        var startPage = new StartPage();
        var paymentPage = startPage.payByDebitCard();
        var cardInfo = new DataGenerator.CardInfo(getCardNumber(), getInvalidMonth(), generateYear(), generateOwner(),
                String.valueOf(generateCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.invalidMonthFieldError();
    }

    @Test
    @DisplayName("Empty year field")
    void shouldNotPaymentWithoutYear() {
        var startPage = new StartPage();
        var paymentPage = startPage.payByDebitCard();
        var cardInfo = new DataGenerator.CardInfo(getCardNumber(), generateMonth(), getEmptyYearField(), generateOwner(),
                String.valueOf(generateCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.errorForEmptyYearField();
    }

    @Test
    @DisplayName("Filling the year field with the previous year")
    void yearShouldNotBeInvalid() {
        var startPage = new StartPage();
        var paymentPage = startPage.payByDebitCard();
        var cardInfo = new DataGenerator.CardInfo(getCardNumber(), generateMonth(), getInvalidYear(), generateOwner(),
                String.valueOf(generateCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.errorInvalidYear();
    }

    @Test
    @DisplayName("Empty owner field")
    void shouldNotPaymentWithoutOwner() {
        var startPage = new StartPage();
        var paymentPage = startPage.payByDebitCard();
        var cardInfo = new DataGenerator.CardInfo(getCardNumber(), generateMonth(), generateYear(), getEmptyOwnerField(),
                String.valueOf(generateCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.errorForEmptyOwnerField();
    }

    @Test
    @DisplayName("Filling out the owner field in Cyrillic")
    void ownerShouldNotBeInCyrillic() {
        var startPage = new StartPage();
        var paymentPage = startPage.payByDebitCard();
        var cardInfo = new DataGenerator.CardInfo(getCardNumber(), generateMonth(), generateYear(), getOwnerInCyrillic(),
                String.valueOf(generateCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.errorOwnerInCyrillic();
    }

    @Test
    @DisplayName("Filling the owner field with numbers")
    void ownerShouldNotBeInvalid() {
        var startPage = new StartPage();
        var paymentPage = startPage.payByDebitCard();
        var cardInfo = new DataGenerator.CardInfo(getCardNumber(), generateMonth(), generateYear(), getOwnerNumbers(),
                String.valueOf(generateCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.errorOwnerNumbers();
    }

    @Test
    @DisplayName("Empty CVC/CVV field")
    void shouldNotPaymentWithoutCVC() {
        var startPage = new StartPage();
        var paymentPage = startPage.payByDebitCard();
        var cardInfo = new DataGenerator.CardInfo(getCardNumber(), generateMonth(), generateYear(), generateOwner(), getEmptyCVCField());
        paymentPage.fillForm(cardInfo);
        paymentPage.errorForEmptyCVCField();
    }

    @Test
    @DisplayName("Filling the CVC/CVV field with two digits")
    void CVCShouldNotBeInvalid() {
        var startPage = new StartPage();
        var paymentPage = startPage.payByDebitCard();
        var cardInfo = new DataGenerator.CardInfo(getCardNumber(), generateMonth(), generateYear(), generateOwner(),
                String.valueOf(getInvalidCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.errorInvalidCVCCVV();
    }

    @Test
    @DisplayName("When a transaction is declined, show an error notification")
    void shouldBeTransactionError() {
        var startPage = new StartPage();
        var paymentPage = startPage.payByDebitCard();
        var cardInfo = new DataGenerator.CardInfo(getInvalidCardNumber(), generateMonth(), generateYear(), generateOwner(),
                String.valueOf(generateCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.findMessageTransaction("Ошибка Ошибка! Банк отказал в проведении операции.");
    }

    @Test
    @DisplayName("Checking the database record of a successful transaction")
    void shouldSuccessfulTransactionInDatabase() {
        var startPage = new StartPage();
        var paymentPage = startPage.payByDebitCard();
        var cardInfo = new DataGenerator.CardInfo(getCardNumber(), generateMonth(), generateYear(), generateOwner(),
                String.valueOf(generateCVC()));
        paymentPage.fillForm(cardInfo);
        paymentPage.findMessageTransaction("Успешно Операция одобрена Банком.");
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());
    }
}