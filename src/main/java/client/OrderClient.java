package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Ingredients;
import model.Order;

public class OrderClient extends BaseHttpClient {
    private static final String ORDERS_ENDPOINT = "api/orders";
    private static final String INGREDIENTS_ENDPOINT = "api/ingredients";

    @Step("Создание заказа с авторизацией и ингредиентами")
    public Response createOrderWithAuth(String token, Order order){
        return doPostRequestWithToken(ORDERS_ENDPOINT, token, order);
    }

    @Step("Получение данных об ингредиентах")
    public Ingredients getIngredientsData(){
        return doGetRequest(INGREDIENTS_ENDPOINT).body().as(Ingredients.class);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuth(Order order){
        return doPostRequest(ORDERS_ENDPOINT, order);
    }

    @Step("Получение списка заказов после авторизации")
    public Response getOrderListWithAuth(String token){
        return doGetRequestWithToken(ORDERS_ENDPOINT, token);
    }

    @Step("Получение списка заказов без авторизации")
    public Response getOrderListWithoutAuth(){
        return doGetRequest(ORDERS_ENDPOINT);
    }


}
