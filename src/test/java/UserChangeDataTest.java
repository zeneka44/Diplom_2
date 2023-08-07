import data.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserClient;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UserChangeDataTest extends TestBase {
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
    public void changeUserDataWithAuthorisation() {
        User user = new User(
                TestBase.faker.internet().emailAddress(),
                TestBase.faker.random().hex(8),
                TestBase.faker.name().firstName());
        User newUser = new User(
                TestBase.faker.internet().emailAddress(),
                TestBase.faker.random().hex(8),
                TestBase.faker.name().firstName());
        UserClient.create(user);
        UserClient.changeUserDataWithToken(newUser, UserClient.getAccessToken(user.getEmail(), user.getPassword()))
                .then()
                .statusCode(200)
                .body("user.email", equalTo(newUser.getEmail()))
                .body("user.name", equalTo(newUser.getName()));
    }

    @Test
    public void changeUserDataWithoutAuthorisation() {
        UserClient.changeUserDataWithoutToken()
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}
