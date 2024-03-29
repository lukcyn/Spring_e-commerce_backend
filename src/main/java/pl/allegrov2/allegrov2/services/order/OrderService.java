package pl.allegrov2.allegrov2.services.order;

import pl.allegrov2.allegrov2.data.entities.AppUser;
import pl.allegrov2.allegrov2.data.entities.order.Order;

import java.util.List;

public interface OrderService {

    /**
     * @param username: creates an order for the cart of the user with the given username
     * */
    Order createOrderForCart(String username);

    /**
     * @param user: creates an order for the cart of the given user
     * */
    Order createOrderForCart(AppUser user);

    Order getUserOrderById(String username, long id);

    List<Order> getUserOrders(String username);
}
