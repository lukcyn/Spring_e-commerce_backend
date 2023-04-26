package pl.allegrov2.allegrov2.data.entities.order;

import lombok.*;
import pl.allegrov2.allegrov2.data.entities.Product;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class OrderItemCompositeKey implements Serializable {

    private Order order;

    private Product product;
}
