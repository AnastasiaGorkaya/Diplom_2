import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import clients.AuthClient;
import generators.UserGenerator;
import helpers.UserCredentials;

import static org.junit.Assert.*;

public class UserLoginTest {
    public static final int USER_SUCCESS_LOGIN = 200;

    ValidatableResponse createUniqueUserResponse;
    private AuthClient authClient;
    private User user;
    private String createdUserAuth;

    @Before
    public void setUp() {
        authClient = new AuthClient();
        user = UserGenerator.getRandomUser();
    }

    @After
    public void tearDown() {
        if (createUniqueUserResponse != null) {
            createdUserAuth = createUniqueUserResponse.extract().path("accessToken");
            if (createdUserAuth != null) {
                authClient.deleteUser(createdUserAuth);
            }
        }
    }

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Проверяется возможность авторизации пользователя")
    public void userCanBeAuthorized() {
        createUniqueUserResponse = authClient.createUser(user);
        createUniqueUserResponse.log().body();

        ValidatableResponse loginResponse = authClient.loginUser(UserCredentials.from(user));
        loginResponse.log().body();

        int statusCode = loginResponse.extract().statusCode();
        boolean responseSuccess = loginResponse.extract().path("success");

        assertEquals(USER_SUCCESS_LOGIN, statusCode);
        assertTrue(responseSuccess);
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным логином")
    @Description("Проверяется гнвозможность авторизации пользователя с неверным логином")
    public void userAuthIncorrectLoginFail() {
        createUniqueUserResponse = authClient.createUser(user);
        createUniqueUserResponse.log().body();

        ValidatableResponse loginResponse = authClient.loginUser(UserCredentials.from(user.setEmail("INCORRECT")));
        loginResponse.log().body();

        int statusCode = loginResponse.extract().statusCode();
        boolean responseSuccess = loginResponse.extract().path("success");

        assertEquals(401, statusCode);
        assertFalse(responseSuccess);
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным паролем")
    @Description("Проверяется гнвозможность авторизации пользователя с неверным паролем")
    public void userAuthIncorrectPasswordFail() {
        createUniqueUserResponse = authClient.createUser(user);
        createUniqueUserResponse.log().body();

        ValidatableResponse loginResponse = authClient.loginUser(UserCredentials.from(user.setPassword("INCORRECT")));
        loginResponse.log().body();

        int statusCode = loginResponse.extract().statusCode();
        boolean responseSuccess = loginResponse.extract().path("success");

        assertEquals(401, statusCode);
        assertFalse(responseSuccess);
    }
}