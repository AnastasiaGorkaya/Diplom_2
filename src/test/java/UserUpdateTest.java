import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import clients.AuthClient;
import generators.UserGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class UserUpdateTest {
    private AuthClient authClient;
    private User user;
    ValidatableResponse createUniqueUserResponse;
    private String createdUserAuth;

    public UserUpdateTest(User user) {
        this.user = user;
    }

    @Parameterized.Parameters
    public static Object[][] createUserTestData() {
        return new Object[][] {
                {new User("testName", null, null)},
                {new User(null, "testEmail", null)},
                {new User(null, null, "testPassword")}
        };
    }

    @Before
    public void setUp() {
        authClient = new AuthClient();
        createUniqueUserResponse = authClient.createUser(UserGenerator.getRandomUser());
        createdUserAuth = createUniqueUserResponse.extract().path("accessToken");
    }

    @After
    public void tearDown() {
        if( createdUserAuth != null ){
            authClient.deleteUser(createdUserAuth);
        }
    }

    @Test
    @DisplayName("Обновление пользователя")
    @Description("Проверяется возможность обновления пользователя")
    public void userCanBeUpdatedWithAuthSuccess() {
        ValidatableResponse updateCreatedUserResponse = authClient.updateUser(user, createdUserAuth);

        int statusCode = updateCreatedUserResponse.extract().statusCode();
        boolean responseSuccess = updateCreatedUserResponse.extract().path("success");

        assertEquals(200, statusCode);
        assertTrue(responseSuccess);
    }

    @Test
    @DisplayName("Обновление пользователя без авторизации")
    @Description("Проверяется возможность обновления пользователя")
    public void userCanBeUpdatedWithOutAuthFail() {
        ValidatableResponse updateCreatedUserResponse = authClient.updateUser(user);

        int statusCode = updateCreatedUserResponse.extract().statusCode();
        boolean responseSuccess = updateCreatedUserResponse.extract().path("success");

        assertEquals(401, statusCode);
        assertFalse(responseSuccess);
    }
}