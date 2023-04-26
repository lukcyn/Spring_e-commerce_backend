package pl.allegrov2.allegrov2.data.entities.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.allegrov2.allegrov2.data.entities.Product;
import pl.allegrov2.allegrov2.data.entities.cart.CartItem;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@IdClass(OrderItemCompositeKey.class)
public class OrderItem {

    @Id
    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
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
}
