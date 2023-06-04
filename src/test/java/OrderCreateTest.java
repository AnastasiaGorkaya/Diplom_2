import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.Order;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import clients.AuthClient;
import clients.OrderClient;
import generators.UserGenerator;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class OrderCreateTest {
    private OrderClient orderClient;
    private AuthClient authClient;
    private Order order;
    private User user;
    private String createdUserAuth;
    ValidatableResponse createOrderResponse;
    ValidatableResponse createUserResponse;

    @Before
    public void setUp() {
        authClient = new AuthClient();
        orderClient = new OrderClient();
        user = UserGenerator.getRandomUser();
        order = new Order();
        createUserResponse = authClient.createUser(user);
    }

    @After
    public void tearDown() {
        if (createUserResponse != null) {
            createdUserAuth = createUserResponse.extract().path("accessToken");
            if (createdUserAuth != null) {
                authClient.deleteUser(createdUserAuth);
            }
        }
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверяется возможность создания заказа без авторизации")
    public void createOrderWithoutAuth() {
        String ingredient = orderClient.getFirstAvailableIngredient();
        ArrayList<String> orderIngredients = new ArrayList<>();
        orderIngredients.add(ingredient);

        order.setIngredients(orderIngredients);

        createOrderResponse = orderClient.createOrder(order);
        createOrderResponse.log().all();

        int statusCode = createOrderResponse.extract().statusCode();
        boolean responseSuccess = createOrderResponse.extract().path("success");

        assertEquals(200, statusCode);
        assertTrue(responseSuccess);
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("Проверяется возможность создания заказа с ингредиентами")
    public void createOrderWithIngredientSuccess() {
        String ingredient = orderClient.getFirstAvailableIngredient();
        ArrayList<String> orderIngredients = new ArrayList<>();
        orderIngredients.add(ingredient);

        createdUserAuth = createUserResponse.extract().path("accessToken");
        order.setIngredients(orderIngredients);

        createOrderResponse = orderClient.createOrder(order, createdUserAuth);
        createOrderResponse.log().all();

        int statusCode = createOrderResponse.extract().statusCode();
        boolean responseSuccess = createOrderResponse.extract().path("success");
        String responseMessageIngredientId = createOrderResponse.extract().path("order.ingredients[0]._id");

        assertEquals(ingredient, responseMessageIngredientId);
        assertEquals(200, statusCode);
        assertTrue(responseSuccess);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверяется невозможность создания заказа без ингредиентов")
    public void createOrderWithOutIngredientFail() {
        createdUserAuth = createUserResponse.extract().path("accessToken");
        createOrderResponse = orderClient.createOrder(order, createdUserAuth);
        createOrderResponse.log().all();

        int statusCode = createOrderResponse.extract().statusCode();
        boolean responseSuccess = createOrderResponse.extract().path("success");

        assertEquals(400, statusCode);
        assertFalse(responseSuccess);
    }

    @Test
    @DisplayName("Создание заказа с некорректным id ингредиента")
    @Description("Проверяется невозможность создания заказа с некорректным id ингредиента")
    public void createOrderWithInvalidIngredientFail() {
        ArrayList<String> orderIngredients = new ArrayList<>();
        orderIngredients.add("invalid_ingredient");

        createdUserAuth = createUserResponse.extract().path("accessToken");
        order.setIngredients(orderIngredients);

        createOrderResponse = orderClient.createOrder(order, createdUserAuth);
        createOrderResponse.log().all();

        int statusCode = createOrderResponse.extract().statusCode();

        assertEquals(500, statusCode);
    }
}