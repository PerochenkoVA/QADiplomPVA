package test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SqlHelper;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pageObject.DashboardPage;
import pageObject.Debit;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DebitTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setUp() {
        Configuration.browser = "chrome";
        open("http://localhost:8080/");
    }


    @AfterAll
    static void openSql(){
        SqlHelper.getStatusPaymentEntity();
        SqlHelper.getStatusPaymentEntity();

    }
    static void tearDownAll() {
        SqlHelper.cleanTables();
    }


    //Оплата по `дебетовой` карте со статусом `APPROVED` с валидными данными остальных полей

    @Test
    void paymentDebitCardStatus_Approved() {
        val dashboardPage = new DashboardPage();
        val debit = new Debit();
        dashboardPage.getDebitCardPayment();
        val cardNumber = DataHelper.getApprovedCardNumber();
        val month = DataHelper.getMonth();
        val year = DataHelper.getYear();
        val cvccvv = DataHelper.getCorrectCVCCVV();
        val owner = DataHelper.getValidOwner();
        debit.fillingFieldsFormat(cardNumber, month, year, cvccvv, owner);
        debit.checkSuccessNotification();
        val paymentStatus = SqlHelper.getStatusPaymentEntity();
        assertEquals("APPROVED", paymentStatus);
    }

    //Оплата по `дебетовой` карте со статусом `DECLINED` с валидными данными остальных полей

    @Test
    void paymentDebitCardStatus_Declined() {
        val dashboardPage = new DashboardPage();
        val  debit = new Debit();
        dashboardPage.getDebitCardPayment();
        val cardNumber = DataHelper.getDeclinedCardNumber();
        val month = DataHelper.getMonth();
        val year = DataHelper.getYear();
        val cvccvv = DataHelper.getCorrectCVCCVV();
        val owner = DataHelper.getValidOwner();
        debit.fillingFieldsFormat(cardNumber, month, year, cvccvv, owner);
        debit.checkErrorNotification();
        val paymentStatus = SqlHelper.getStatusPaymentEntity();
        assertEquals("DECLINES", paymentStatus);
    }

    //Тестирование поля `Номер карты` не валидные данные

    @Test
    void emptyCardNumberField() {
        val dashboardPage = new DashboardPage();
        val debit = new Debit();
        dashboardPage.getDebitCardPayment();
        val cardNumber = DataHelper.getCardWithoutNumber();
        val month = DataHelper.getMonth();
        val year = DataHelper.getYear();
        val cvccvv = DataHelper.getCorrectCVCCVV();
        val owner = DataHelper.getValidOwner();
        debit.fillingFieldsFormat(cardNumber, month, year, cvccvv, owner);
        debit.checkWrongFormatMessage();
    }

    // Тестирование поля `Месяц`

    @Test
    void invalidMonthFieldValues() {
        val dashboardPage = new DashboardPage();
        val debit = new Debit();
        dashboardPage.getDebitCardPayment();
        val cardNumber = DataHelper.getApprovedCardNumber();
        val month = DataHelper.getInvalidMonth();
        val year = DataHelper.getYear();
        val cvccvv = DataHelper.getCorrectCVCCVV();
        val owner = DataHelper.getValidOwner();
        debit.fillingFieldsFormat(cardNumber, month, year, cvccvv, owner);
        debit.checkWrongCardExpirationMessage();
    }

    @Test
    void emptyMonthFieldValues() {
        val dashboardPage = new DashboardPage();
        val debit = new Debit();
        dashboardPage.getDebitCardPayment();
        val cardNumber = DataHelper.getApprovedCardNumber();
        val month = DataHelper.getEmptyMonth();
        val year = DataHelper.getYear();
        val cvccvv = DataHelper.getCorrectCVCCVV();
        val owner = DataHelper.getValidOwner();
        debit.fillingFieldsFormat(cardNumber, month, year, cvccvv, owner);
        debit.checkWrongFormatMessage();
    }

    //Тестирование поля `Год`

    @Test
    void overdueYearValue() {
        val dashboardPage = new DashboardPage();
        val debit = new Debit();
        dashboardPage.getDebitCardPayment();
        val cardNumber = DataHelper.getApprovedCardNumber();
        val month = DataHelper.getMonth();
        val year = DataHelper.getLastYear();
        val cvccvv = DataHelper.getCorrectCVCCVV();
        val owner = DataHelper.getValidOwner();
        debit.fillingFieldsFormat(cardNumber, month, year, cvccvv, owner);
        debit.checkCardExpiredMessage();
    }

    @Test
    void emptyYearFieldValues() {
        val dashboardPage = new DashboardPage();
        val debit = new Debit();
        dashboardPage.getDebitCardPayment();
        val cardNumber = DataHelper.getApprovedCardNumber();
        val month = DataHelper.getMonth();
        val year = DataHelper.getEmptyYear();
        val cvccvv = DataHelper.getCorrectCVCCVV();
        val owner = DataHelper.getValidOwner();
        debit.fillingFieldsFormat(cardNumber, month, year, cvccvv, owner);
        debit.checkWrongFormatMessage();
    }

    //Тестирование поля `CVCCVV`

    @Test
    void correctCvccvv() {
        val dashboardPage = new DashboardPage();
        val debit = new Debit();
        dashboardPage.getDebitCardPayment();
        val cardNumber = DataHelper.getApprovedCardNumber();
        val month = DataHelper.getMonth();
        val year = DataHelper.getYear();
        val cvccvv = DataHelper.getCorrectCVCCVV();
        val owner = DataHelper.getValidOwner();
        debit.fillingFieldsFormat(cardNumber, month, year, cvccvv, owner);
        debit.checkSuccessNotification();

    }

    @Test
    void emptyCVVCVCFieldValues() {
        val dashboardPage = new DashboardPage();
        val debit = new Debit();
        dashboardPage.getDebitCardPayment();
        val cardNumber = DataHelper.getApprovedCardNumber();
        val month = DataHelper.getMonth();
        val year = DataHelper.getYear();
        val cvccvv = DataHelper.getEmptyCVCCVV();
        val owner = DataHelper.getValidOwner();
        debit.fillingFieldsFormat(cardNumber, month, year, cvccvv, owner);
        debit.checkWrongFormatMessage();
    }

    //Тестирование поля `Владелец`

    @Test
    void numericValuesInTheOwnerField() {
        val dashboardPage = new DashboardPage();
        val debit = new Debit();
        dashboardPage.getDebitCardPayment();
        val cardNumber = DataHelper.getApprovedCardNumber();
        val month = DataHelper.getMonth();
        val year = DataHelper.getYear();
        val cvccvv = DataHelper.getCorrectCVCCVV();
        val owner = DataHelper.getOwnerWithNumbers();
        debit.fillingFieldsFormat(cardNumber, month, year, cvccvv, owner);
        debit.checkWrongFormatMessage();
    }


    @Test
    void emptyOwnerFieldValues() {
        val dashboardPage = new DashboardPage();
        val debit = new Debit();
        dashboardPage.getDebitCardPayment();
        val cardNumber = DataHelper.getApprovedCardNumber();
        val month = DataHelper.getMonth();
        val year = DataHelper.getYear();
        val cvccvv = DataHelper.getCorrectCVCCVV();
        val owner = DataHelper.getEmptyOwner();
        debit.fillingFieldsFormat(cardNumber, month, year, cvccvv, owner);
        debit.checkEmptyFieldMessage();
    }
    @Test
    void emptyOwnerCyrillicName() {
        val dashboardPage = new DashboardPage();
        val debit = new Debit();
        dashboardPage.getDebitCardPayment();
        val cardNumber = DataHelper.getApprovedCardNumber();
        val month = DataHelper.getMonth();
        val year = DataHelper.getYear();
        val cvccvv = DataHelper.getCorrectCVCCVV();
        val owner = DataHelper.getOwnerCyrillic();
        debit.fillingFieldsFormat(cardNumber, month, year, cvccvv, owner);
        debit.checkEmptyFieldMessage();
    }
}



