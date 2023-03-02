package pl.allegrov2.allegrov2.data.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TokenDto {

    @NotBlank
    @NotNull
    private String email;

    @NotBlank
    @NotNull
    private String token;
}
