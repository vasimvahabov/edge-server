package com.travelbookingsystem.edgeserver.user.mapper;

import com.travelbookingsystem.edgeserver.user.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "roles", source = "user", qualifiedByName = "roles")
    @Mapping(target = "username", source = "preferredUsername")
    @Mapping(target = "firstName", source = "givenName")
    @Mapping(target = "lastName", source = "familyName")
    UserResponse oidcUserToResponse(OidcUser user);

    @Named("roles")
    default List<String> authoritiesToRoles(OidcUser user) {
        return user.getClaimAsStringList("roles");
    }

}
