package user;

import com.google.gson.Gson;
import data.Ingredient;
import data.Ingredients;
import data.Order;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class OrderClient {
    public static final String INGREDIENTS_ENDPOINT = "/api/ingredients";
    public static final String ORDER_ENDPOINT = "/api/orders";


    public static List<Ingredient> getIngredients() {
        return get(INGREDIENTS_ENDPOINT)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .extract().body().as(Ingredients.class).getData();
    }

    public static Response createOrder(Order order, String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .and()
                .body(new Gson().toJson(order))
                .when()
                .post(ORDER_ENDPOINT);
    }
    public static Response createOrderWithoutAuthorisation(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(new Gson().toJson(order))
                .when()
                .post(ORDER_ENDPOINT);
    }
    public static Response getOrders(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(ORDER_ENDPOINT);
    }
    public static Response getOrdersWithoutAuthorisation() {
        return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(ORDER_ENDPOINT);
    }
}