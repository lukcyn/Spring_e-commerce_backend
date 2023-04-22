package pl.allegrov2.allegrov2.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.allegrov2.allegrov2.data.dto.PasswordDto;
import pl.allegrov2.allegrov2.data.dto.UserDetailsBasicDto;
import pl.allegrov2.allegrov2.data.dto.UserDetailsEmailDto;
import pl.allegrov2.allegrov2.data.entities.AppUser;
import pl.allegrov2.allegrov2.data.enums.AppUserRole;
import pl.allegrov2.allegrov2.assemblers.UserDetailsAssembler;
import pl.allegrov2.allegrov2.services.mapping.MappingService;
import pl.allegrov2.allegrov2.services.token.JwtService;
import pl.allegrov2.allegrov2.services.user.UserService;
import pl.allegrov2.allegrov2.validation.exceptions.MismatchException;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final MappingService mapper;
    private final JwtService jwtService;
    private final UserService userService;

    private final PagedResourcesAssembler<UserDetailsEmailDto> pagedAssembler;
    private final UserDetailsAssembler userAssembler;

    @GetMapping("/users/details")
    @ResponseBody
    public EntityModel<UserDetailsEmailDto> getDetails(
            @RequestHeader(HttpHeaders.AUTHORIZATION)
            @NotBlank String authHeader) {

        String email = jwtService.extractUsernameFromAuthHeader(authHeader);

        AppUser user = userService.getUser(email);

        return userAssembler.toModel(
                mapper.convertToDto(user),
                user.getRole()
        );
    }


    @PutMapping("/users/details")
    public ResponseEntity<EntityModel<UserDetailsEmailDto>> replaceDetails(
            @RequestBody @Valid UserDetailsBasicDto newDetails,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader){

        String email = jwtService.extractUsernameFromAuthHeader(authHeader);

        // Update user details in database
        AppUser user = userService.getUser(email);
        userService.updateDetails(user, newDetails);

        // Return updated user details extended with user email (his identifier)
        UserDetailsEmailDto detailsToReturn = mapper.convertToDto(user);

        EntityModel<UserDetailsEmailDto> userEntityModel = userAssembler.toModel(detailsToReturn, user.getRole());
        return ResponseEntity
                .created(userEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(userEntityModel);
    }


    @PutMapping("/users/password")
    @ResponseBody
    public ResponseEntity<Void> changePassword(
            @RequestBody @Valid PasswordDto passwordDto,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader){

        if(passwordDto.getNewPassword().equals(passwordDto.getConfirmPassword()))
            throw new MismatchException("New password is different than confirmed one");

        String email = jwtService.extractUsernameFromAuthHeader(authHeader);

        AppUser user = userService.getUser(email);

        userService.updatePassword(user, passwordDto.getOldPassword(), passwordDto.getNewPassword());

        return ResponseEntity.noContent().build();
    }


    @GetMapping(path ="/admin/users", params = {"page", "size"})
    @PreAuthorize("hasRole('ADMIN')")
    public CollectionModel<EntityModel<UserDetailsEmailDto>> getAllPaginated(
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        Page<UserDetailsEmailDto> userPage =
                userService.getUserPage(PageRequest.of(page, size))
                        .map(mapper::convertToDto);

        return pagedAssembler.toModel(userPage, userAssembler);
    }


    @GetMapping("/admin/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public EntityModel<UserDetailsEmailDto> one(@PathVariable Long id){
        AppUser user = userService.getUser(id);

        UserDetailsEmailDto dto = mapper.convertToDto(user);

        if(user.getRole().equals(AppUserRole.USER))
            return userAssembler.toModel(dto);

        return userAssembler.toModel(dto, AppUserRole.ADMIN);
    }
}
