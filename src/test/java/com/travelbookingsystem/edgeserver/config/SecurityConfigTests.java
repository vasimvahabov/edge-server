package com.travelbookingsystem.edgeserver.config;

import com.travelbookingsystem.edgeserver.user.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@WebFluxTest
@Import(SecurityConfig.class)
public class SecurityConfigTests {

    private static final String REGISTRATION_ID = "test-edge-server";

    @Autowired
    WebTestClient webTestClient;

    @MockitoBean
    UserMapper userMapper;

    @MockitoBean
    ReactiveClientRegistrationRepository clientRegistrationRepository;

    @Test
    void whenLogoutWithoutCsrfTokenThen403() {
        webTestClient
                .mutateWith(SecurityMockServerConfigurers.mockOidcLogin())
                .post()
                .uri("/logout")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void whenLogoutWithCsrfThenReturn302() {
        when(clientRegistrationRepository.findByRegistrationId(REGISTRATION_ID))
                .thenReturn(Mono.just(testClientRegistration()));

        webTestClient
                .mutateWith(SecurityMockServerConfigurers.mockOidcLogin().clientRegistration(testClientRegistration()))
                .mutateWith(SecurityMockServerConfigurers.csrf())
                .post()
                .uri("/logout")
                .exchange()
                .expectStatus().isFound();

    }

    private ClientRegistration testClientRegistration() {
        return ClientRegistration
                .withRegistrationId(REGISTRATION_ID)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientId(REGISTRATION_ID)
                .authorizationUri("https://www.keycloak.org")
                .tokenUri("https://www.keycloak.org")
                .redirectUri("https://www.keycloak.org")
                .build();
    }

}
