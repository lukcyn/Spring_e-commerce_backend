package pl.allegrov2.allegrov2.data.entities.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import pl.allegrov2.allegrov2.data.entities.Product;
import pl.allegrov2.allegrov2.data.entities.cart.CartItem;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@IdClass(OrderItemCompositeKey.class)
public class OrderItem {

    @Id
    @ManyToOne
    @JsonIgnore
    private Order order;

    @Id
    @OneToOne
    private Product product;

    @Positive
    private int quantity;

    public OrderItem(CartItem item, Order order){
        this.product = item.getProduct();
        this.quantity = item.getQuantity();
        this.order = order;
    }

    public BigDecimal getPrice(){
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(order, orderItem.order) && Objects.equals(product, orderItem.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, product);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "order=" + order +
                ", product=" + product +
                '}';
    }
}
