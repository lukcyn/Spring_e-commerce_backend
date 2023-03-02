package pl.allegrov2.allegrov2.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "product")
@Getter @Setter
@NoArgsConstructor
public class Product {

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
    private int quantity;

    public Product(List<Photo> photos, String brandName, String modelName, BigDecimal price, int quantity) {
        this.photos = photos;
        this.brandName = brandName;
        this.modelName = modelName;
        this.price = price;
        this.quantity = quantity;
    }

    public boolean isAvailable(){
        return quantity > 0;
    }
}
