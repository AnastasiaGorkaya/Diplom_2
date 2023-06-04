package generators;

import models.Order;
import clients.OrderClient;

import java.util.ArrayList;

public class OrderGenerator {
    public static Order getOrderWithFirstAvailableIngredient(OrderClient orderClient) {
        Order generatedOrder = new Order();
        String ingredient = orderClient.getFirstAvailableIngredient();
        ArrayList<String> orderIngredients = new ArrayList<>();
        orderIngredients.add(ingredient);
        generatedOrder.setIngredients(orderIngredients);
        return generatedOrder;
    }
}