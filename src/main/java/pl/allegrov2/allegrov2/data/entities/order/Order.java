package pl.allegrov2.allegrov2.data.entities.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.allegrov2.allegrov2.data.entities.AppUser;
import pl.allegrov2.allegrov2.data.entities.cart.Cart;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@IdClass(OrderCompositeKey.class)
@NoArgsConstructor
@Table(name = "customer_order")
public class Order {

    @SequenceGenerator(
            name="customer_order_sequence",
            sequenceName = "customer_order_sequence",
            allocationSize = 1
    )
    @Id @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_order_sequence"
    )
    private Long id;

    @Id
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne
    private AppUser user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false, columnDefinition="datetime default now()")
    @JsonFormat(pattern = "dd.mm.yyyy HH:mm:ss")
    private LocalDateTime dateCreated;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order", orphanRemoval = true)
    private List<OrderItem> orderItems;


    public Order(Cart cart, AppUser user){
        this.user = user;
        this.orderStatus = OrderStatus.IN_PROGRESS;
        this.dateCreated = LocalDateTime.now();

        this.orderItems = cart.getCartItems().stream()
                .map(item -> new OrderItem(item, this))
                .toList();
    }

    private BigDecimal getTotalPrice(){
        return orderItems.stream().map(OrderItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
