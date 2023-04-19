package pl.allegrov2.allegrov2.data.entities.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @Column(name = "app_user_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "app_user_id")
    @JsonIgnore
    @ToString.Exclude
    private AppUser user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart")
    private List<CartItem> cartItems;

    public void addItem(CartItem cartItem){
        this.cartItems.add(cartItem);
    }
}
