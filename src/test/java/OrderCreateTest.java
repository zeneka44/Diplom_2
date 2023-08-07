import data.Ingredient;
import data.Order;
import data.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.OrderClient;
import user.UserClient;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderCreateTest extends TestBase {
    private String email;
    private String password;
    private String name;

    @Before
    public void setUp() {
        email = TestBase.faker.internet().emailAddress();
        password = TestBase.faker.random().hex(8);
        name = TestBase.faker.name().firstName();
        User user = new User(email, password, name);
        UserClient.create(user)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @After
    public void tearDown() {
        UserClient.delete(UserClient.getAccessToken(email, password))
                .then()
                .statusCode(202)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));
    }

    @Test
    public void createOrderAuthorized() {
        List<Ingredient> ingredients = OrderClient.getIngredients();
        if (ingredients.size() < 2) {
            throw new Error("Не хватает ингредиентов!");
        }
        Order order = new Order(asList(ingredients.get(0).get_id(), ingredients.get(1).get_id()));

        OrderClient.createOrder(order, UserClient.getAccessToken(email, password))
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    public void createOrderNotAuthorized() {
        List<Ingredient> ingredients = OrderClient.getIngredients();
        if (ingredients.size() < 2) {
            throw new Error("Не хватает ингредиентов!");
        }
        Order order = new Order(asList(ingredients.get(0).get_id(), ingredients.get(1).get_id()));

        OrderClient.createOrderWithoutAuthorisation(order)
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    public void createOrderWithoutIngredients() {
        Order order = new Order();

        OrderClient.createOrder(order, UserClient.getAccessToken(email, password))
                .then()
                .statusCode(400)
                .body("message", equalTo("Ingredient ids must be provided"));
    }
    @Test
    public void createOrderWithIncorrectIngredients() {
        List<Ingredient> ingredients = OrderClient.getIngredients();
        if (ingredients.size() < 2) {
            throw new Error("Не хватает ингредиентов!");
        }
        Order order = new Order(asList(ingredients.get(0).get_id()+"1", ingredients.get(1).get_id()+"1"));

        OrderClient.createOrder(order, UserClient.getAccessToken(email, password))
                .then()
                .statusCode(500);
    }
}