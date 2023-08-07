import data.User;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserClient;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UserLogintest extends TestBase {
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
                .body("user.email", notNullValue())
                .body("user.name", notNullValue())
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
    public void loginUser() {
        UserClient.login(email, password)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", CoreMatchers.notNullValue())
                .body("refreshToken", CoreMatchers.notNullValue())
                .body("user.email", CoreMatchers.notNullValue())
                .body("user.name", CoreMatchers.notNullValue());
    }

    @Test
    public void loginUserWithInvalidEmail() {
        UserClient.login(TestBase.faker.internet().emailAddress(), password)
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
    @Test
    public void loginUserWithInvalidPassword() {
        UserClient.login(email, TestBase.faker.random().hex(8))
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
