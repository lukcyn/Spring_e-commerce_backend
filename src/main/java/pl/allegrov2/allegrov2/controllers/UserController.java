package pl.allegrov2.allegrov2.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.allegrov2.allegrov2.data.dto.user.UserDetailsDto;
import pl.allegrov2.allegrov2.data.entities.AppUser;
import pl.allegrov2.allegrov2.data.enums.AppUserRole;
import pl.allegrov2.allegrov2.helpers.assemblers.AdminUserDetailsAssembler;
import pl.allegrov2.allegrov2.helpers.assemblers.UserDetailsAssembler;
import pl.allegrov2.allegrov2.repositories.IUserRepository;
import pl.allegrov2.allegrov2.services.JwtService;
import pl.allegrov2.allegrov2.services.MappingService;
import pl.allegrov2.allegrov2.validation.exceptions.NotFoundException;
import pl.allegrov2.allegrov2.validation.exceptions.UnauthorizedException;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

//todo password update

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final IUserRepository userRepository;
    private final MappingService mapper;
    private final JwtService jwtService;

    private final UserDetailsAssembler userAssembler;
    private final AdminUserDetailsAssembler adminAssembler;


    //todo move to service
    @GetMapping("user/details")
    @ResponseBody
    public EntityModel<UserDetailsDto> getDetails(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader){
        String email = jwtService.extractUsername(
                            jwtService.extractTokenFromAuthHeader(authHeader)
                    );

        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Bad token"));

        UserDetailsDto userDetails = mapper.convertToDto(user);

        return userAssembler.toModel(userDetails);
    }

    @PutMapping("user/details")
    public ResponseEntity<?> replaceDetails(@RequestBody @Valid UserDetailsDto newDetails,
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader){

        String email = jwtService.extractUsername(
                jwtService.extractTokenFromAuthHeader(authHeader)
        );

        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Bad credentials."));

        user.updateDetails(newDetails);

        userRepository.save(user);

        newDetails.setId(user.getId());

        EntityModel<UserDetailsDto> userEntityModel = userAssembler.toModel(newDetails);
        return ResponseEntity
                .created(userEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(userEntityModel);
    }


    //todo change assembler so that role USER get different _links than ADMIN

    // todo pagination
    @GetMapping("/admin/user/all")
    @PreAuthorize("hasRole('ADMIN')")
    public CollectionModel<EntityModel<UserDetailsDto>> all() {
        List<UserDetailsDto> users = userRepository.findAll().stream()
                .map(mapper::convertToDto)
                .toList();

        return adminAssembler.toCollectionModel(users);
    }

    @GetMapping("/admin/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public EntityModel<UserDetailsDto> one(@PathVariable Long id){
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No user with id " + id));

        UserDetailsDto dto = mapper.convertToDto(user);

        if(user.getRole().equals(AppUserRole.USER))
            return userAssembler.toModel(dto);

        return adminAssembler.toModel(dto);
    }


}
