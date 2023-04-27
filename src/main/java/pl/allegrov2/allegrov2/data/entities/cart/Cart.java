package pl.allegrov2.allegrov2.data.entities.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import pl.allegrov2.allegrov2.data.entities.AppUser;

import java.util.List;
import java.util.Objects;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class Cart {

    @Id
    @Column(name = "customer_id")
    private Long id;

    @MapsId
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "customer_id")
    private AppUser user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart", fetch = FetchType.EAGER, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private List<CartItem> cartItems;

    public void addItem(CartItem cartItem){
        this.cartItems.add(cartItem);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(id, cart.id) && Objects.equals(user, cart.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                '}';
    }
}
