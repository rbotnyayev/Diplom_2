package model;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

@Data
public class User {
    String email;
    String password;
    String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static User createRandomUser(){
        final String email = RandomStringUtils.randomAlphabetic(10)+"@ya.ru".toLowerCase();
        final String password = RandomStringUtils.randomAlphabetic(10).toLowerCase();
        final String name = RandomStringUtils.randomAlphabetic(10).toLowerCase();
        return new User(email, password, name);
    }

    public static String createRandomEmail(){
        return RandomStringUtils.randomAlphabetic(10)+"@ya.ru".toLowerCase();
    }
    public static String createRandomString(){
        return RandomStringUtils.randomAlphabetic(10).toLowerCase();
    }
}
