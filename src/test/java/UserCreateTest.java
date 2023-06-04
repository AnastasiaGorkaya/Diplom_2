import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import clients.AuthClient;
import generators.UserGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserCreateTest {
    public static final int USER_EXISTS_CODE = 403;
    public static final int USER_CREATED_CODE = 200;
    public static final int USER_REQUIRED_FIELD_EMPTY = 403;

    private AuthClient authClient;
    private User user;
    ValidatableResponse createUniqueUserResponse;
    private String createdUserAuth;

    @Before
    public void setUp() {
        authClient = new AuthClient();
        user = UserGenerator.getRandomUser();
    }

    @After
    public void tearDown() {
        createdUserAuth = createUniqueUserResponse.extract().path("accessToken");
        if(createdUserAuth != null) {
            authClient.deleteUser(createdUserAuth);
        }
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверяется возможность создания пользователя")
    public void userCanBeCreatedSuccess() {
        createUniqueUserResponse = authClient.createUser(user);
        createUniqueUserResponse.log().body();

        int statusCode = createUniqueUserResponse.extract().statusCode();
        boolean responseSuccess = createUniqueUserResponse.extract().path("success");
        createdUserAuth = createUniqueUserResponse.extract().path("accessToken");

        assertEquals(USER_CREATED_CODE, statusCode);
        assertTrue(responseSuccess);
    }

    @Test
    @DisplayName("Создание двух одинаковых пользователей")
    @Description("Проверяется невозможность создания двух одинаковых пользователей")
    public void createTwoEqualUsersFail() {
        createUniqueUserResponse = authClient.createUser(user);
        ValidatableResponse createExistedUserResponse = authClient.createUser(user);

        createUniqueUserResponse.log().body();
        createExistedUserResponse.log().body();

        int statusCode = createExistedUserResponse.extract().statusCode();
        String responseMessage = createExistedUserResponse.extract().path("message");
        boolean responseSuccess = createExistedUserResponse.extract().path("success");

        assertEquals(USER_EXISTS_CODE, statusCode);
        assertEquals("User already exists", responseMessage);
        assertFalse(responseSuccess);
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля name")
    @Description("Проверяется невозможность создания пользователя без обязательного поля")
    public void userCanNotBeCratedWithoutNameField() {
        createUniqueUserResponse = authClient.createUser(UserGenerator.getUserWithoutField("name"));
        createUniqueUserResponse.log().body();

        int statusCode = createUniqueUserResponse.extract().statusCode();
        boolean responseSuccess = createUniqueUserResponse.extract().path("success");

        assertEquals(USER_REQUIRED_FIELD_EMPTY, statusCode);
        assertFalse(responseSuccess);
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля email")
    @Description("Проверяется невозможность создания пользователя без обязательного поля")
    public void userCanNotBeCratedWithoutEmailField() {
        createUniqueUserResponse = authClient.createUser(UserGenerator.getUserWithoutField("email"));
        createUniqueUserResponse.log().body();

        int statusCode = createUniqueUserResponse.extract().statusCode();
        boolean responseSuccess = createUniqueUserResponse.extract().path("success");

        assertEquals(USER_REQUIRED_FIELD_EMPTY, statusCode);
        assertFalse(responseSuccess);
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля password")
    @Description("Проверяется невозможность создания пользователя без обязательного поля")
    public void userCanNotBeCratedWithoutPasswordField() {
        createUniqueUserResponse = authClient.createUser(UserGenerator.getUserWithoutField("password"));
        createUniqueUserResponse.log().body();

        int statusCode = createUniqueUserResponse.extract().statusCode();
        boolean responseSuccess = createUniqueUserResponse.extract().path("success");

        assertEquals(USER_REQUIRED_FIELD_EMPTY, statusCode);
        assertFalse(responseSuccess);
    }
}