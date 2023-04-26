package pl.allegrov2.allegrov2.data.entities.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.allegrov2.allegrov2.data.entities.AppUser;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCompositeKey implements Serializable {

    private Long id;

    private AppUser user;
}
