package pl.allegrov2.allegrov2.data.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdatePasswordDto {

    @NotNull @NotBlank
    private String newPassword;

    @NotNull @NotBlank
    private String oldPassword;
}
