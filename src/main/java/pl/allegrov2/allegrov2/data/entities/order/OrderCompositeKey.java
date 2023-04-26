package pl.allegrov2.allegrov2.data.entities.order;

import lombok.*;
import pl.allegrov2.allegrov2.data.entities.AppUser;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class OrderCompositeKey implements Serializable {

    private Long id;

    private AppUser user;
}
