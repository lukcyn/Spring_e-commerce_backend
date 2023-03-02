package pl.allegrov2.allegrov2.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.allegrov2.allegrov2.data.dto.user.LoginDto;
import pl.allegrov2.allegrov2.data.dto.user.RegistrationDto;
import pl.allegrov2.allegrov2.data.dto.user.TokenDto;
import pl.allegrov2.allegrov2.helpers.assemblers.TokenAssembler;
import pl.allegrov2.allegrov2.services.ConfirmationTokenService;
import pl.allegrov2.allegrov2.services.UserService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@AllArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    private final TokenAssembler userAssembler;
    private final UserService userService;
    private final ConfirmationTokenService tokenService;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationDto registerDto){
        return ResponseEntity.ok(
                new TokenDto(registerDto.getEmail(),
                userService.signUpUser(registerDto).getToken())
        );
    }

    // Ma byc put?
    @PutMapping("/confirm-email")
    public ResponseEntity<?> confirmEmailFromLink(@RequestParam String token){
        tokenService.confirmToken(token);
        return ResponseEntity.ok().body("Email address successfully confirmed!");
    }

    @GetMapping("/login")
    @ResponseBody
    public EntityModel<TokenDto> login(@Valid @RequestBody LoginDto loginDto){
        return userAssembler.toModel(userService.login(loginDto));
    }
}
