package com.travelbookingsystem.edgeserver.controller;

import com.travelbookingsystem.edgeserver.config.SecurityConfig;
import com.travelbookingsystem.edgeserver.user.controller.UserController;
import com.travelbookingsystem.edgeserver.user.dto.response.UserResponse;
import com.travelbookingsystem.edgeserver.user.mapper.UserMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import java.util.List;

import static org.mockito.Mockito.*;

@WebFluxTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTests {

    @Autowired
    WebTestClient webTestClient;

    @MockitoBean
    UserMapper userMapper;

    @MockitoBean
    ReactiveClientRegistrationRepository clientRegistrationRepository;

    @Test
    void whenNotAuthenticatedThen401() {
        webTestClient
                .get()
                .uri("/user")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void whenAuthenticatedThenReturnUser() {
        var user = UserResponse
                .builder()
                .username("username-edge-server")
                .firstName("first-name-edge-server")
                .lastName("last-name-edge-server")
                .roles(List.of("employee", "customer"))
                .build();

        Mockito.when(userMapper.oidcUserToResponse(any())).thenReturn(user);

        webTestClient
                .mutateWith(configureMockOidcLogin(user))
                .get()
                .uri("/user")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(UserResponse.class)
                .value(payload -> {
                    Assertions.assertThat(payload).isEqualTo(user);
                    Assertions.assertThat(payload.getRoles()).containsAll(user.getRoles());
                });
    }

    private SecurityMockServerConfigurers.OidcLoginMutator configureMockOidcLogin(UserResponse user) {
        return SecurityMockServerConfigurers.mockOidcLogin().idToken(
                builder -> {
                    builder.claim(StandardClaimNames.PREFERRED_USERNAME, user.getUsername());
                    builder.claim(StandardClaimNames.GIVEN_NAME, user.getFirstName());
                    builder.claim(StandardClaimNames.FAMILY_NAME, user.getLastName());
                    builder.claim("roles", user.getRoles());
                }
        );
    }

}
