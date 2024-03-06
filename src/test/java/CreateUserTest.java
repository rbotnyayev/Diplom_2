import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateUserTest {
    User user;
    UserClient client;

    @Before
    public void setUp(){
        client = new UserClient();
        user = User.createRandomUser();
    }

    @Test
    @DisplayName("Проверка создания уникального пользователя")
    @Description("Регистрация уникального пользователя c корректными данными")
    public void userCreatingTest(){
        client.createUser(user)
                .then()
                .statusCode(SC_OK)
                .and().assertThat().body("success", equalTo(true))
                .and().body("accessToken", notNullValue())
                .and().body("refreshToken", notNullValue())
                .and().body("user.email", notNullValue())
                .and().body("user.name", notNullValue());
    }

    @Test
    @DisplayName("Проверка создания пользователя, который уже зарегистрирован")
    @Description("Повторная попытка егистрации ранее зарегистрированного пользователя")
    public void checkAlreadyRegistredUserTest(){
        client.createUser(user);
        client.createUser(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .and().assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo("model.User already exists"));
    }

    @Test
    @DisplayName("Проверка создания пользователя без email")
    @Description("Регистрация пользователя без email, но с заполненными имени и пароля")
    public void createUserWithoutEmailTest() {
        user = new User("", user.getEmail(), user.getPassword());
        client.createUser(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .and().assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Проверка создания пользователя без пароля")
    @Description("Регистрация пользователя без пароля, но с заполненными email и имени")
    public void createUserWithoutPasswordTest() {
        user = new User(user.getEmail(), "", user.getName());
        client.createUser(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .and().assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Проверка создания пользователя без имени")
    @Description("Регистрация пользователя без имени, но с заполненными email и паролем")
    public void createUserWithoutNameTest() {
        user = new User(user.getEmail(), user.getPassword(), "");
        client.createUser(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .and().assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void deleteUser(){
        client.deleteUser(client.getToken(user));
    }
}
