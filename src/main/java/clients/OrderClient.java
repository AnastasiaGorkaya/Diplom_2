package clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {
    private static final String CREATE_OR_GET_ORDER_PATH = "/api/orders";
    private static final String GET_INGREDIENTS_PATH = "/api/ingredients";

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order, String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(CREATE_OR_GET_ORDER_PATH)
                .then();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(CREATE_OR_GET_ORDER_PATH)
                .then();
    }

    @Step("Получение доступных ингредиентов")
    public String getFirstAvailableIngredient() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(GET_INGREDIENTS_PATH)
                .then()
                .extract()
                .path("data[0]._id");
    }

    @Step("Получение заказов пользователя")
    public ValidatableResponse getOrder(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .get(CREATE_OR_GET_ORDER_PATH)
                .then();
    }

    @Step("Получение заказов пользователя без авторизации")
    public ValidatableResponse getOrder() {
        return given()
                .spec(getBaseSpec())
                .get(CREATE_OR_GET_ORDER_PATH)
                .then();
    }
}