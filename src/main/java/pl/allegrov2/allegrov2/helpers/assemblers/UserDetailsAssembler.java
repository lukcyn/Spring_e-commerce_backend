package pl.allegrov2.allegrov2.helpers.assemblers;

import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import pl.allegrov2.allegrov2.controllers.UserController;
import pl.allegrov2.allegrov2.data.dto.user.TokenDto;
import pl.allegrov2.allegrov2.data.dto.user.UserDetailsDto;
import pl.allegrov2.allegrov2.data.entities.AppUser;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserDetailsAssembler implements RepresentationModelAssembler<UserDetailsDto, EntityModel<UserDetailsDto>> {

    @Override
    public @NotNull EntityModel<UserDetailsDto> toModel(UserDetailsDto entity) {
        return EntityModel.of(entity,
            linkTo(methodOn(UserController.class).getDetails("(authHeader)")).withSelfRel()
        );
    }
}
