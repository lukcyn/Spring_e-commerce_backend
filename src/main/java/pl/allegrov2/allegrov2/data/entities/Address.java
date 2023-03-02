package pl.allegrov2.allegrov2.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import pl.allegrov2.allegrov2.data.dto.user.AddressDto;

@Getter @Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode

@Entity
@Table(name = "address")
public class Address {

    @SequenceGenerator(
            name="address_sequence",
            sequenceName = "address_sequence",
            allocationSize = 1
    )
    @Id @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "address_sequence"
    )
    private Long id;

    @Column(length = 200, nullable = false)
    @Nationalized
    private String streetName;

    @Column(nullable = false)
    private int streetNumber;

    @Column(nullable = true)
    private int residenceNumber;

    @Column(length = 20, nullable = false)
    private String zipcode;

    @OneToOne(mappedBy = "address", optional = false)
    @JsonIgnore
    private AppUser user;

    public Address(String streetName,
                   int streetNumber,
                   int residenceNumber,
                   String zipcode,
                   AppUser user) {
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.residenceNumber = residenceNumber;
        this.zipcode = zipcode;
        this.user = user;
    }

    public void updateDetails(AddressDto address) {
        this.streetName = address.getStreetName();
        this.streetNumber = address.getStreetNumber();
        this.residenceNumber = address.getResidenceNumber();
        this.zipcode = address.getZipcode();
    }
}
