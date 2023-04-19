package pl.allegrov2.allegrov2.assemblers;

import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import pl.allegrov2.allegrov2.controllers.UserController;
import pl.allegrov2.allegrov2.data.dto.UserDetailsEmailDto;
import pl.allegrov2.allegrov2.data.enums.AppUserRole;


import java.util.LinkedList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserDetailsAssembler implements RepresentationModelAssembler<UserDetailsEmailDto, EntityModel<UserDetailsEmailDto>> {

    @Override
    public @NotNull EntityModel<UserDetailsEmailDto> toModel(@NotNull UserDetailsEmailDto entity) {
        return EntityModel.of(entity,
                generateCommonLinks()
        );
    }

    public @NotNull EntityModel<UserDetailsEmailDto> toModel(UserDetailsEmailDto entity, AppUserRole role){
        if (role == AppUserRole.ADMIN) {
            return toAdminModel(entity);
        }

        //If user does not have special authorities, then return default links
        return toModel(entity);
    }

    private @NotNull EntityModel<UserDetailsEmailDto> toAdminModel(UserDetailsEmailDto entity){
        return EntityModel.of(entity,
                generateAdminLinks()
        );
    }

    private List<Link> generateCommonLinks(){
        List<Link> links = new LinkedList<>();

        links.add(linkTo(methodOn(UserController.class).getDetails("(authHeader)")).withSelfRel());
        links.add(linkTo(methodOn(UserController.class).replaceDetails(null, "")).withRel("(post) change details"));
        links.add(linkTo(methodOn(UserController.class).changePassword(null, "")).withRel("(post) change password"));

        return links;
    }

    private List<Link> generateAdminLinks(){
        List<Link> links = generateCommonLinks();
        links.add(
                linkTo(
                methodOn(UserController.class).getAllPaginated(0,10))
                .withRel("all paginated")
        );

        return links;
    }
}
