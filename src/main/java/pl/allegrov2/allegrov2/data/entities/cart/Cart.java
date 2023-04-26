package pl.allegrov2.allegrov2.data.entities.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import pl.allegrov2.allegrov2.data.entities.AppUser;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @Column(name = "customer_id")
    private Long id;

    @MapsId
    @JsonIgnore
    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private AppUser user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart", orphanRemoval = true)
    private List<CartItem> cartItems;

    public void addItem(CartItem cartItem){
        this.cartItems.add(cartItem);
    }
}
