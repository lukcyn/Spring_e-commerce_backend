package pl.allegrov2.allegrov2.helpers.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import pl.allegrov2.allegrov2.controllers.UserController;
import pl.allegrov2.allegrov2.data.dto.user.TokenDto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TokenAssembler implements RepresentationModelAssembler<TokenDto, EntityModel<TokenDto>>{

    @Override
    public EntityModel<TokenDto> toModel(TokenDto entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(UserController.class).getDetails("(authHeader)")).withSelfRel()
        );
    }
}
