package com.travelbookingsystem.edgeserver.user.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

    String username;

    String firstName;

    String lastName;

    List<String> roles;

}
