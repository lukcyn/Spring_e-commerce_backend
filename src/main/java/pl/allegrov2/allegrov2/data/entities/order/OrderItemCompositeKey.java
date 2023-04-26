package pl.allegrov2.allegrov2.data.entities.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.allegrov2.allegrov2.data.entities.Product;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemCompositeKey implements Serializable {

    private Order order;

    private Product product;
}
