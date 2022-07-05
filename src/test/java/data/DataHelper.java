package data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {
    private DataHelper() {

    }

    @Value
    public static class CardNumber {
        private String cardNumber;

    }
    //Подготовка тестовых данных

    //Номер карты
    public static CardNumber getApprovedCardNumber() {
        return new CardNumber("4444 4444 4444 4441");
    }

    public static CardNumber getDeclinedCardNumber() {
        return new CardNumber("4444 4444 4444 4442");
    }

    public static CardNumber getCardWithoutNumber() {
        return new CardNumber("");
    }

    //Месяц
    public static String getMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getInvalidMonth() {
        return "13";
    }

    public static String getEmptyMonth() {
        return "";
    }

    //Год
    public static String getYear() {
        return LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getLastYear() {
        return LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getEmptyYear() {
        return "";
    }

    //CVCCVV
    public static String getCorrectCVCCVV() {
        return "321";
    }

    public static String getEmptyCVCCVV() {
        return "";
    }

    //Владелец
    public static String getValidOwner() {
        Faker faker = new Faker(new Locale("en"));
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    public static String getOwnerWithNumbers() {
        return "4321";
    }

    public static String getEmptyOwner() {
        return "";
    }

    public static String getOwnerCyrillic () {return "Петроченко Владимир"; }
}
