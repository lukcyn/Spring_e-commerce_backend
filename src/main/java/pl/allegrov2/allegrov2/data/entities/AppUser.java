package pl.allegrov2.allegrov2.data.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.allegrov2.allegrov2.data.dto.user.UserDetailsDto;
import pl.allegrov2.allegrov2.data.enums.AppUserRole;

import java.util.Collection;
import java.util.Collections;


@Getter @Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode

@Entity
@Table(name = "app_user")
public class AppUser implements UserDetails {


    @SequenceGenerator(
            name="app_user_sequence",
            sequenceName = "app_user_sequence",
            allocationSize = 1
    )
    @Id @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "app_user_sequence"
    )
    private Long id;

    @Column(length = 50, nullable = false)
    @Nationalized
    private String name;

    @Column(length = 50, nullable = false)
    @Nationalized
    private String surname;

    @Column(length = 254, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private int phoneNumber;

    @Column(length = 100, nullable = false)
    @Enumerated(EnumType.STRING)
    private AppUserRole role;

    @Column(nullable = false, length = 100)
    private boolean locked;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private String password;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "address_id", referencedColumnName = "id", nullable = false)
    private Address address;

    public AppUser(String name,
                   String surname,
                   String email,
                   int phoneNumber,
                   AppUserRole role,
                   boolean locked,
                   boolean enabled,
                   String password,
                   Address address) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.locked = locked;
        this.enabled = enabled;
        this.password = password;
        this.address = address;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(role.name());

        return Collections.singleton(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void updateDetails(UserDetailsDto detailsDto){
        this.name = detailsDto.getName();
        this.surname = detailsDto.getSurname();
        this.email = detailsDto.getEmail();
        this.phoneNumber = detailsDto.getPhoneNumber();
        this.address.updateDetails(detailsDto.getAddress());
    }
}
