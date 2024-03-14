package ru.jsms.backend.common.dto;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import ru.jsms.backend.profile.entity.User;
import ru.jsms.backend.profile.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;

@Component
@RequestScope
@Data
public class HeadersDto {

    private final User user;
    private final Long userId;
    private final boolean isAdmin;
    private final String ip;

    public HeadersDto(HttpServletRequest request, UserRepository repository) {
        var authInfo = (JwtAuthentication) request.getUserPrincipal();
        userId = authInfo.getUserId();
        isAdmin = authInfo.getAuthorities().contains(Role.ADMIN);
        user = repository.findById(userId).orElse(null);
        ip = request.getRemoteAddr();
    }
}
