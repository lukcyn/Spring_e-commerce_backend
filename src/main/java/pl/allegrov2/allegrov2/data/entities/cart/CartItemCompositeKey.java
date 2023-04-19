package pl.allegrov2.allegrov2.data.entities.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.*;
import pl.allegrov2.allegrov2.data.entities.Product;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemCompositeKey implements Serializable {

    private Cart cart;

    private Product product;
}
