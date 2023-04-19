package pl.allegrov2.allegrov2.data.entities.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import pl.allegrov2.allegrov2.data.entities.Product;

import javax.annotation.Nonnegative;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "cart_item")
@IdClass(CartItemCompositeKey.class)
public class CartItem {

    @Id
    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
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
            this.quantity = 0;
    }
}
