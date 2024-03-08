import client.OrderClient;
import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;

public class GetOrderTest {
    User user = User.createRandomUser();
    UserClient userClient = new UserClient();
    String accessToken;
    OrderClient orderClient = new OrderClient();

    @Test
    @DisplayName("Получение списка заказов авторизованным пользователем")
    @Description("Успешная проверка получения списка заказов авторизованного пользователя")
    public void getUserOrderWithAuthorizationTest() {
        userClient.createUser(user);
        accessToken = userClient.getToken(user);
        orderClient.getOrderListWithAuth(accessToken)
                .then().statusCode(SC_OK)
                .and().body("success", equalTo(true))
    }

    @Test
    @DisplayName("Получение списка заказов без авторизации")
    @Description("Неуспешная проверка получение списка заказов без авторизации")
    public void getUserOrderWithoutAuthorizationTest() {
        orderClient.getOrderListWithoutAuth()
                .then().statusCode(SC_UNAUTHORIZED)
                .and().body("success", equalTo(false));
    }
}
