package pl.allegrov2.allegrov2.data.entities.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import pl.allegrov2.allegrov2.data.entities.Product;

import javax.annotation.Nonnegative;
import java.util.Objects;

@Entity
@Builder
@Table(name = "cart_item")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@IdClass(CartItemCompositeKey.class)
public class CartItem extends RepresentationModel<CartItem> {

    @Id
    @ManyToOne
    @JsonIgnore
    private Cart cart;

    @Id
    @ManyToOne
    private Product product;

    @Nonnegative
    @Column(nullable = false, columnDefinition = "int default 1")
    public Integer quantity;

    public void addQuantity(@Positive int quantity){
        this.quantity += quantity;
    }

    public void decreaseQuantity(@Positive int quantity){
        this.quantity -= quantity;

        if(this.quantity < 0)
            this.quantity = 0; // TODO throw
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(cart, cartItem.cart) && Objects.equals(product, cartItem.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cart, product);
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "cart=" + cart +
                ", product=" + product +
                '}';
    }
}
