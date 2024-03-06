import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginUserTest {
    User user;
    UserClient client;

    @Before
    public void setUp(){
        user = User.createRandomUser();
        client = new UserClient();
    }

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Авторизация пользователя под существующем логином")
    public void authorizationTest() {
        client.createUser(user);
        client.userLogin(user)
                .then().log().all()
                .statusCode(SC_OK)
                .and().body("accessToken", notNullValue())
                .and().body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Авторизация пользователя с некорректным логином")
    @Description("Попытка авторизации пользователя с несуществующим логином")
    public void authorizationWithIncorrectLoginTest() {
        user = new User("edfsfsewwd", this.user.getPassword(), this.user.getName());
        client.userLogin(user)
                .then().log().all()
                .statusCode(SC_UNAUTHORIZED)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация пользователя с некорректным паролем")
    @Description("Попытка авторизации пользователя с несуществующим паролем")
    public void authorizationWithIncorrectPasswordTest() {
        user = new User(this.user.getEmail(), "gsddfscv5", this.user.getName());
        client.userLogin(user)
                .then().log().all()
                .statusCode(SC_UNAUTHORIZED)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация пользователя без логина и пароля")
    @Description("Попытка авторизации пользователя без логина и пароля")
    public void authorizationWithoutAnyDataTest() {
        client.userLogin(new User("", "", ""))
                .then().log().all()
                .statusCode(SC_UNAUTHORIZED)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("email or password are incorrect"));
    }
}
