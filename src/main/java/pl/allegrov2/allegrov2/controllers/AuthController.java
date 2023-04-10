package pl.allegrov2.allegrov2.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.allegrov2.allegrov2.data.dto.LoginDto;
import pl.allegrov2.allegrov2.data.dto.RegistrationDto;
import pl.allegrov2.allegrov2.data.dto.TokenDto;
import pl.allegrov2.allegrov2.services.auth.AuthService;
import pl.allegrov2.allegrov2.services.token.ConfirmationTokenService;


@RestController
@AllArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;
    private final ConfirmationTokenService tokenService;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<TokenDto> registerUser(@Valid @RequestBody RegistrationDto registerDto){
        return ResponseEntity.ok(
                new TokenDto(registerDto.getEmail(),
                        authService.signUpUser(registerDto).getToken())
        );
    }

    @PutMapping("/confirm-email")
    public ResponseEntity<String> confirmEmailFromLink(@RequestParam String token){
        tokenService.confirmToken(token);
        return ResponseEntity.ok().body("Email address successfully confirmed!");
    }

    @GetMapping("/login")
    @ResponseBody
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto){
        return ResponseEntity
                .ok()
                .body(authService.login(loginDto));
    }
}
