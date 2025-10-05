package com.travelbookingsystem.edgeserver.user.controller;

import com.travelbookingsystem.edgeserver.user.dto.response.UserResponse;
import com.travelbookingsystem.edgeserver.user.mapper.UserMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserMapper userMapper;

//    @GetMapping
//    public Mono<UserResponse> user() {
//        return ReactiveSecurityContextHolder.getContext()
//                .map(SecurityContext::getAuthentication)
//                .map(Authentication::getPrincipal)
//                .cast(OidcUser.class)
//                .map(userMapper::oidcUserToResponse);
//    }

    @GetMapping
    public Mono<UserResponse> user(@AuthenticationPrincipal OidcUser user) {
        return Mono.just(userMapper.oidcUserToResponse(user));
    }

}
