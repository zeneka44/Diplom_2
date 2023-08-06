import data.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserClient;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UserCreateTest extends TestBase {
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
    public void createNewUser() {
        User user = new User(
                TestBase.faker.internet().emailAddress(),
                TestBase.faker.random().hex(8),
                TestBase.faker.name().firstName());
        UserClient.create(user)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", notNullValue())
                .body("user.name", notNullValue())
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    public void createDuplicateUser() {
        User user = new User(email, password, name);
        UserClient.create(user)
                .then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    public void createUserWithMissingEmail() {
        User user = new User(
                "",
                TestBase.faker.random().hex(8),
                TestBase.faker.name().firstName());
        UserClient.create(user)
                .then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    public void createUserWithMissingPassword() {
        User user = new User(
                TestBase.faker.internet().emailAddress(),
                "",
                TestBase.faker.name().firstName());
        UserClient.create(user)
                .then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    public void createUserWithMissingName() {
        User user = new User(
                TestBase.faker.internet().emailAddress(),
                TestBase.faker.random().hex(8),
                "");
        UserClient.create(user)
                .then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
