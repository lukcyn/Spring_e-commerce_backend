package pl.allegrov2.allegrov2.helpers.assemblers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import pl.allegrov2.allegrov2.controllers.UserController;
import pl.allegrov2.allegrov2.data.dto.user.UserDetailsDto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AdminUserDetailsAssembler implements RepresentationModelAssembler<UserDetailsDto, EntityModel<UserDetailsDto>> {


    @Override
    public EntityModel<UserDetailsDto> toModel(UserDetailsDto entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(UserController.class).getDetails("(authHeader)")).withSelfRel(),
                linkTo(methodOn(UserController.class).all()).withRel("all"),
                linkTo(methodOn(UserController.class).one(entity.getId())).withRel("one")
        );
    }
}
