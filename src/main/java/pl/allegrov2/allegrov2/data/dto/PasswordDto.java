package pl.allegrov2.allegrov2.data.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class PasswordDto {

    @NotBlank(message = "oldPassword field is required")
    private String oldPassword;

    @NotBlank(message = "newPassword field is required")
    private String newPassword;

    @NotBlank(message = "confirmPassword field is required")
    private String confirmPassword;
}
