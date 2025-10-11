package com.travelbookingsystem.edgeserver.user.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserResponse that)) return false;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }

}
