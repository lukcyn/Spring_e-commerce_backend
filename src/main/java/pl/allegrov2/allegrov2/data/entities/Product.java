package pl.allegrov2.allegrov2.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import pl.allegrov2.allegrov2.validation.exceptions.QuantityCriteriaNotMetException;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "product")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Product extends RepresentationModel<Product> {

    @SequenceGenerator(
            name="product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    private List<Photo> photos;

    @Column(nullable = false)
    private String brandName;

    @Column(nullable = false)
    private String modelName;

    @Column(nullable = false)
    @DecimalMin("0.01")
    private BigDecimal price;

    @Column(nullable = false)
    private Long stock;

    public Product(List<Photo> photos, String brandName, String modelName, BigDecimal price, Long stock) {
        this.photos = photos;
        this.brandName = brandName;
        this.modelName = modelName;
        this.price = price;
        this.stock = stock;
    }

    @JsonIgnore
    public boolean isAvailable(){
        return stock > 0;
    }

    @JsonIgnore
    public String shortName(){
        return brandName + " " + modelName;
    }

    @JsonIgnore
    public void decreaseStock(int quantity){
        if (quantity > stock)
            throw new QuantityCriteriaNotMetException(this, quantity);

        stock -= quantity;
    }
}
