package client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BaseHttpClient {

    private RequestSpecification baseRequestSpec(){
        return new RequestSpecBuilder()
                .setBaseUri("https://stellarburgers.nomoreparties.site/")
                .addHeader("Content-type", "application/json")
                .setRelaxedHTTPSValidation()
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new ErrorLoggingFilter())
                .build();
    }

    protected Response doGetRequest(String path){
        return given()
                .spec(baseRequestSpec())
                .get(path)
                .thenReturn();
    }

    protected Response doPostRequest(String path, Object body){
        return given().log().all()
                .spec(baseRequestSpec())
                .body(body)
                .post(path);
    }

    protected Response doPatchRequest(String path, Object body){
        return given().log().all()
                .spec(baseRequestSpec())
                .body(body)
                .patch(path);
    }
    protected Response doPatchRequestWithToken(String path, String token, Object body){
        return given().log().all()
                .spec(baseRequestSpec())
                .header("authorization", token)
                .body(body)
                .patch(path);
    }

    protected Response doDeleteRequest(String path, String accessToken){
        return given()
                .spec(baseRequestSpec())
                .header("authorization", accessToken)
                .when()
                .delete(path);
    }
}
