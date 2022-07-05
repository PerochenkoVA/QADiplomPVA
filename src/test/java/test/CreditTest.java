package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SqlHelper;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pageObject.Credit;
import pageObject.DashboardPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:8080/");
    }


    @AfterAll
    static void tearDownAll() {
        SqlHelper.cleanTables();
    }

    //Оплата через `кредит` со статусом карты `APPROVED` с валидными данными остальных полей


    @Test
    void paymentCreditCardStatus_Approved() {
        val dashboardPage = new DashboardPage();
        val paymentFormCredit = new Credit();
        dashboardPage.getCreditPayment();
        val cardNumber = DataHelper.getApprovedCardNumber();
        val month = DataHelper.getMonth();
        val year = DataHelper.getYear();
        val cvccvv = DataHelper.getCorrectCVCCVV();
        val owner = DataHelper.getValidOwner();
        paymentFormCredit.fillingFieldsFormat(cardNumber, month, year, cvccvv, owner);
        paymentFormCredit.checkSuccessNotification();
        val paymentStatus = SqlHelper.getStatusCreditRequestEntity();
        assertEquals("APPROVED", paymentStatus);
    }

    //Оплата через `кредит` со статусом карты `DECLINED` с валидными данными остальных полей

    @Test
    void paymentCreditCardStatus_Declined() {
        val dashboardPage = new DashboardPage();
        val paymentFormCredit = new Credit();
        dashboardPage.getCreditPayment();
        val cardNumber = DataHelper.getDeclinedCardNumber();
        val month = DataHelper.getMonth();
        val year = DataHelper.getYear();
        val cvccvv = DataHelper.getCorrectCVCCVV();
        val owner = DataHelper.getValidOwner();
        paymentFormCredit.fillingFieldsFormat(cardNumber, month, year, cvccvv, owner);
        paymentFormCredit.checkErrorNotification();
        val paymentStatus = SqlHelper.getStatusCreditRequestEntity();
        assertEquals("DECLINES", paymentStatus);
    }

    //Тестирование поля `Номер карты` не валидные данные.

    @Test
    void emptyCardNumberField_Credit() {
        val dashboardPage = new DashboardPage();
        val paymentFormCredit = new Credit();
        dashboardPage.getCreditPayment();
        val cardNumber = DataHelper.getCardWithoutNumber();
        val month = DataHelper.getMonth();
        val year = DataHelper.getYear();
        val cvccvv = DataHelper.getCorrectCVCCVV();
        val owner = DataHelper.getValidOwner();
        paymentFormCredit.fillingFieldsFormat(cardNumber, month, year, cvccvv, owner);
        paymentFormCredit.checkWrongFormatMessage();
    }


    // Тестирование поля 'Месяц'

    @Test
    void invalidMonthFieldValues_Credit() {
        val dashboardPage = new DashboardPage();
        val paymentFormCredit = new Credit();
        dashboardPage.getCreditPayment();
        val cardNumber = DataHelper.getApprovedCardNumber();
        val month = DataHelper.getInvalidMonth();
        val year = DataHelper.getYear();
        val cvccvv = DataHelper.getCorrectCVCCVV();
        val owner = DataHelper.getValidOwner();
        paymentFormCredit.fillingFieldsFormat(cardNumber, month, year, cvccvv, owner);
        paymentFormCredit.checkWrongCardExpirationMessage();
    }

    @Test
    void emptyMonthFieldValues_Credit() {
        val dashboardPage = new DashboardPage();
        val paymentFormCredit = new Credit();
        dashboardPage.getCreditPayment();
        val cardNumber = DataHelper.getApprovedCardNumber();
        val month = DataHelper.getEmptyMonth();
        val year = DataHelper.getYear();
        val cvccvv = DataHelper.getCorrectCVCCVV();
        val owner = DataHelper.getValidOwner();
        paymentFormCredit.fillingFieldsFormat(cardNumber, month, year, cvccvv, owner);
        paymentFormCredit.checkWrongFormatMessage();
    }

    //Тестирование поля 'Год'

    @Test
    void overdueYearValue_Credit() {
        val dashboardPage = new DashboardPage();
        val paymentFormCredit = new Credit();
        dashboardPage.getCreditPayment();
        val cardNumber = DataHelper.getApprovedCardNumber();
        val month = DataHelper.getMonth();
        val year = DataHelper.getLastYear();
        val cvccvv = DataHelper.getCorrectCVCCVV();
        val owner = DataHelper.getValidOwner();
        paymentFormCredit.fillingFieldsFormat(cardNumber, month, year, cvccvv, owner);
        paymentFormCredit.checkCardExpiredMessage();
    }

    @Test
    void emptyYearFieldValues_Credit() {
        val dashboardPage = new DashboardPage();
        val paymentFormCredit = new Credit();
        dashboardPage.getCreditPayment();
        val cardNumber = DataHelper.getApprovedCardNumber();
        val month = DataHelper.getMonth();
        val year = DataHelper.getEmptyYear();
        val cvccvv = DataHelper.getCorrectCVCCVV();
        val owner = DataHelper.getValidOwner();
        paymentFormCredit.fillingFieldsFormat(cardNumber, month, year, cvccvv, owner);
        paymentFormCredit.checkWrongFormatMessage();
    }


    //Тестирование поля 'CVCCVV'

    @Test
    void correctCvccvv_Credit() {
        val dashboardPage = new DashboardPage();
        val paymentFormCredit = new Credit();
        dashboardPage.getCreditPayment();
        val cardNumber = DataHelper.getApprovedCardNumber();
        val month = DataHelper.getMonth();
        val year = DataHelper.getYear();
        val cvccvv = DataHelper.getCorrectCVCCVV();
        val owner = DataHelper.getValidOwner();
        paymentFormCredit.fillingFieldsFormat(cardNumber, month, year, cvccvv, owner);
        paymentFormCredit.checkSuccessNotification();
    }


    @Test
    void emptyCVVCVCFieldValues_Credit() {
        val dashboardPage = new DashboardPage();
        val paymentFormCredit = new Credit();
        dashboardPage.getCreditPayment();
        val cardNumber = DataHelper.getApprovedCardNumber();
        val month = DataHelper.getMonth();
        val year = DataHelper.getYear();
        val cvccvv = DataHelper.getEmptyCVCCVV();
        val owner = DataHelper.getValidOwner();
        paymentFormCredit.fillingFieldsFormat(cardNumber, month, year, cvccvv, owner);
        paymentFormCredit.checkWrongFormatMessage();
    }

    //Тестирование поля 'Владелец'

    @Test
    void numericValuesInTheOwnerField_Credit() {
        val dashboardPage = new DashboardPage();
        val paymentFormCredit = new Credit();
        dashboardPage.getCreditPayment();
        val cardNumber = DataHelper.getApprovedCardNumber();
        val month = DataHelper.getMonth();
        val year = DataHelper.getYear();
        val cvccvv = DataHelper.getCorrectCVCCVV();
        val owner = DataHelper.getOwnerWithNumbers();
        paymentFormCredit.fillingFieldsFormat(cardNumber, month, year, cvccvv, owner);
        paymentFormCredit.checkWrongFormatMessage();
    }

    @Test
    void emptyOwnerFieldValues_Credit() {
        val dashboardPage = new DashboardPage();
        val paymentFormCredit = new Credit();
        dashboardPage.getCreditPayment();
        val cardNumber = DataHelper.getApprovedCardNumber();
        val month = DataHelper.getMonth();
        val year = DataHelper.getYear();
        val cvccvv = DataHelper.getCorrectCVCCVV();
        val owner = DataHelper.getEmptyOwner();
        paymentFormCredit.fillingFieldsFormat(cardNumber, month, year, cvccvv, owner);
        paymentFormCredit.checkEmptyFieldMessage();
    }
    @Test
    void emptyOwnerCyrillicName_Credit() {
        val dashboardPage = new DashboardPage();
        val paymentFormCredit = new Credit();
        dashboardPage.getCreditPayment();
        val cardNumber = DataHelper.getApprovedCardNumber();
        val month = DataHelper.getMonth();
        val year = DataHelper.getYear();
        val cvccvv = DataHelper.getCorrectCVCCVV();
        val owner = DataHelper.getOwnerCyrillic();
        paymentFormCredit.fillingFieldsFormat(cardNumber, month, year, cvccvv, owner);
        paymentFormCredit.checkEmptyFieldMessage();
    }

}

