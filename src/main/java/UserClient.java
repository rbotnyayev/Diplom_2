import io.restassured.response.Response;

public class UserClient extends BaseHttpClient {

    private final String CREATE_USER_ENDPOINT = "api/auth/register";
    private final String LOGIN_USER_ENDPOINT = "api/auth/login";
    private final String ORDER_CREATING_ENDPOINT = "api/orders";


    public Response createUser(User user){
        return doPostRequest(CREATE_USER_ENDPOINT, user);
    }

}
