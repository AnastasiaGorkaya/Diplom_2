package generators;

import com.github.javafaker.Faker;
import models.User;

import java.util.Locale;

public class UserGenerator {
    public static User getRandomUser() {
        Faker fakerRu = new Faker(Locale.forLanguageTag("ru"));
        Faker fakerEn = new Faker();
        return new User(fakerRu.name().firstName(), fakerEn.internet().emailAddress(), fakerRu.internet().password());
    }

    public static User getUserWithoutField(String fieldName) {
        Faker fakerRu = new Faker(Locale.forLanguageTag("ru"));
        Faker fakerEn = new Faker();

        switch (fieldName) {
            case "name":
                return new User(null, fakerEn.internet().emailAddress(), fakerRu.internet().password());
            case "email":
                return new User(fakerRu.name().firstName(), null, fakerRu.internet().password());
            case "password":
                return new User(fakerRu.name().firstName(), fakerEn.internet().emailAddress(), null);
            default:
                return null;
        }
    }
}