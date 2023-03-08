package pl.allegrov2.allegrov2.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "photo")
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Photo {

    @SequenceGenerator(
            name="photo_sequence",
            sequenceName = "photo_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "photo_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name="product_id", referencedColumnName = "id",nullable = false)
    @JsonIgnore
    private Product product;

    public Photo(String url, Product product) {
        this.url = url;
        this.product = product;
    }
}
