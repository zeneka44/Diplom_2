import data.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.OrderClient;
import user.UserClient;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrderTest extends TestBase {
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
    public void getOrdersAuthorized() {
        OrderClient.getOrders(UserClient.getAccessToken(email, password))
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
    }
    @Test
    public void getOrdersNotAuthorized() {
        OrderClient.getOrdersWithoutAuthorisation()
                .then()
                .statusCode(401)
                .body("message", equalTo("You should be authorised"));
    }

}