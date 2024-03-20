package ru.jsms.backend.common.service;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.jsms.backend.common.dto.JwtAuthentication;
import ru.jsms.backend.common.dto.Role;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.jsms.backend.profile.enums.Claims.USER_ID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setUserId(claims.get(USER_ID.getName(), Long.class));
        return jwtInfoToken;
    }

    private static Set<Role> getRoles(Claims claims) {
        final List<String> roles = claims.get("roles", List.class);
        return roles.stream().map(Role::valueOf).collect(Collectors.toSet());
    }

}
