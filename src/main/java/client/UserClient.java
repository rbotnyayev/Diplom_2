package client;
import model.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class UserClient extends BaseHttpClient {

    private final String CREATE_USER_ENDPOINT = "api/auth/register";
    private final String LOGIN_USER_ENDPOINT = "api/auth/login";
    private static final String USER_CHANGE_ENDPOINT = "api/auth/user";


    @Step("Создание уникального пользователя")
    public Response createUser(User user){
        return doPostRequest(CREATE_USER_ENDPOINT, user);
    }

    @Step("Логин существуюшего пользователя")
    public Response userLogin(User user){
        return doPostRequest(LOGIN_USER_ENDPOINT, user);
    }

    @Step("Получение токена")
    public String getToken(User user){
        return userLogin(user)
                .then()
                .extract()
                .path("accessToken");
    }

    @Step("Изменение данных пользователя без авторизации")
    public Response updateUserDataWithoutAuth(User user){
        return doPatchRequest(USER_CHANGE_ENDPOINT, user);
    }

    @Step("Изменение данных пользователя с авторизацией")
    public Response updateUserDataWithAuth(String token, User user){
        return doPatchRequestWithToken(USER_CHANGE_ENDPOINT, token, user);
    }

    @Step("Удаление пользователя")
    public Response deleteUser(String accessToken){
        return doDeleteRequest(USER_CHANGE_ENDPOINT, accessToken);
    }

}
