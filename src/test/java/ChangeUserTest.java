import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class ChangeUserTest {
    User user;
    UserClient client;
    String accessToken;

    @Before
    public void setUp(){
        client = new UserClient();
        user = User.createRandomUser();
    }

    @Test
    @DisplayName("Изменение адреса электронной почты пользователя с авторизацией")
    @Description("Проверка на успешное изменение адреса электронной почты пользователя с авторизацией")
    public void changeUserEmailWithAuthorizationTest() {
        client.createUser(user);
        accessToken = client.userLogin(user).then().extract().path("accessToken");
        String newEmail = User.createRandomEmail();
        user = new User(newEmail, this.user.getPassword(), this.user.getName());
        client.updateUserDataWithAuth(accessToken, user)
                .then().log().all()
                .statusCode(SC_OK)
                .and().body("success", equalTo(true))
                .and().body("user.email", equalTo(newEmail));
    }

    @Test
    @DisplayName("Изменение пароля пользователя с авторизацией")
    @Description("Проверка на успешное изменение пароля пользователя с авторизацией")
    public void changeUserPasswordWithAuthorizationTest() {
        client.createUser(user);
        accessToken = client.userLogin(user).then().extract().path("accessToken");
        String newPassword = User.createRandomString();
        user = new User(this.user.getEmail(), newPassword, this.user.getName());
        client.updateUserDataWithAuth(accessToken, user)
                .then().log().all()
                .statusCode(SC_OK)
                .and().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение имени пользователя с авторизацией")
    @Description("Проверка на успешное изменение имени пользователя с авторизацией")
    public void changeUserNameWithAuthorizationTest() {
        client.createUser(user);
        accessToken = client.userLogin(user).then().extract().path("accessToken");
        String newName = User.createRandomString();
        user = new User(this.user.getEmail(), this.user.getPassword(), newName);
        client.updateUserDataWithAuth(accessToken, user)
                .then().log().all()
                .statusCode(SC_OK)
                .and().body("success", equalTo(true))
                .and().body("user.name", equalTo(newName));
    }

    @Test
    @DisplayName("Изменение электронной почты пользователя без авторизации")
    @Description("Проверка на невозможность изменения электронной почты пользователя без авторизации")
    public void changeUserEmailWithoutAuthorizationTest() {
        client.createUser(user);
        String newEmail = User.createRandomEmail();
        user = new User(newEmail, this.user.getPassword(), this.user.getName());
        client.updateUserDataWithoutAuth(user)
                .then().log().all()
                .statusCode(SC_UNAUTHORIZED)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение пароля пользователя без авторизации")
    @Description("Проверка на невозможность изменения пароля пользователя без авторизации")
    public void changeUserPasswordWithoutAuthorizationTest() {
        client.createUser(user);
        String newPassword = User.createRandomString();
        user = new User(this.user.getEmail(), newPassword, this.user.getName());
        client.updateUserDataWithoutAuth(user)
                .then().log().all()
                .statusCode(SC_UNAUTHORIZED)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение имени пользователя без авторизации")
    @Description("Проверка на невозможность изменения имени пользователя без авторизации")
    public void changeUserNamedWithoutAuthorizationTest() {
        client.createUser(user);
        String newName = User.createRandomString();
        user = new User(this.user.getEmail(), this.user.getPassword(), newName);
        client.updateUserDataWithoutAuth(user)
                .then().log().all()
                .statusCode(SC_UNAUTHORIZED)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"));
    }

    @After
    public void deleteUser(){
        client.deleteUser(client.getToken(user));
    }
}
