import client.OrderClient;
import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.Ingredients;
import model.Order;
import model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTest {
    User user;
    Order order;
    UserClient userClient;
    OrderClient orderClient;
    List<String> ingredient;
    String accessToken;

    @Before
    public void setUp(){
        user = User.createRandomUser();
        userClient = new UserClient();
        orderClient = new OrderClient();
        ingredient = new ArrayList<>();
        order = new Order(ingredient);
    }

    @Test
    @DisplayName("Создание заказа с авторизациией и ингредиентами")
    @Description("Успешное создание заказа с авторизацией и ингредиентами")
    public void createOrderWithAuthorizationTest() {
        userClient.createUser(user);
        accessToken = userClient.getToken(user);
        Ingredients ingredients = orderClient.getIngredientsData();
        ingredient.add(ingredients.getData().get(1).get_id());
        ingredient.add(ingredients.getData().get(2).get_id());
        ingredient.add(ingredients.getData().get(3).get_id());
        ingredient.add(ingredients.getData().get(4).get_id());
        ingredient.add(ingredients.getData().get(5).get_id());
        ingredient.add(ingredients.getData().get(6).get_id());
        ingredient.add(ingredients.getData().get(7).get_id());
        ingredient.add(ingredients.getData().get(8).get_id());
        orderClient.createOrderWithAuth(accessToken, order)
                .then().statusCode(SC_OK)
                .and().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Успешное создание заказа без авторизации")
    public void createOrderWithoutAuthorizationTest() {
        Ingredients ingredients = orderClient.getIngredientsData();
        ingredient.add(ingredients.getData().get(1).get_id());
        ingredient.add(ingredients.getData().get(2).get_id());
        ingredient.add(ingredients.getData().get(3).get_id());
        orderClient.createOrderWithoutAuth(order)
                .then().statusCode(SC_OK)
                .and().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией без ингредиентов")
    @Description("Проверка создания заказа с авторизацией без ингредиентов")
    public void createEmptyOrderWithAuthorization() {
        userClient.createUser(user);
        accessToken = userClient.getToken(user);
        orderClient.createOrderWithAuth(accessToken, order)
                .then().statusCode(SC_BAD_REQUEST)
                .and().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией с неверным хешем ингредиентов")
    @Description("Проверка создания заказа с авторизацией с неверным хешем ингредиентов")
    public void createOrderWithAuthorizationWithWrongHashTest() {
        userClient.createUser(user);
        accessToken = userClient.getToken(user);
        Ingredients ingredients = orderClient.getIngredientsData();
        ingredient.add(ingredients.getData().get(1).get_id() + "5");
        ingredient.add(ingredients.getData().get(2).get_id() + "5");
        orderClient.createOrderWithAuth(accessToken, order)
                .then().statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
