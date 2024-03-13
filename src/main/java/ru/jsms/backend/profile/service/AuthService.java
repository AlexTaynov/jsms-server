package ru.jsms.backend.profile.service;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.jsms.backend.common.dto.JwtAuthentication;
import ru.jsms.backend.profile.dto.request.JwtRequest;
import ru.jsms.backend.profile.dto.response.JwtResponse;
import ru.jsms.backend.profile.dto.request.RegisterRequest;
import ru.jsms.backend.common.dto.Role;
import ru.jsms.backend.profile.entity.RefreshToken;
import ru.jsms.backend.profile.entity.User;
import ru.jsms.backend.profile.repository.RefreshTokenRepository;
import ru.jsms.backend.profile.repository.UserDataRepository;
import ru.jsms.backend.common.service.JwtProvider;

import java.util.Set;

import static ru.jsms.backend.profile.enums.AuthExceptionCode.ACCOUNT_NOT_FOUND;
import static ru.jsms.backend.profile.enums.AuthExceptionCode.EMAIL_ALREADY_EXISTS;
import static ru.jsms.backend.profile.enums.AuthExceptionCode.TOKEN_INVALID;
import static ru.jsms.backend.profile.enums.AuthExceptionCode.WRONG_PASSWORD;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final UserDataRepository userDataRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public JwtResponse register(@NonNull RegisterRequest registerRequest) {
        if (userDataRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw EMAIL_ALREADY_EXISTS.getException();
        }
        User user = userService.createUser(registerRequest, Set.of(Role.USER));
        return buildJwtResponse(user);
    }

    public JwtResponse login(@NonNull JwtRequest authRequest) {
        final User user = userService.getByEmail(authRequest.getEmail())
                .orElseThrow(ACCOUNT_NOT_FOUND.getException());
        if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            return buildJwtResponse(user);
        }
        throw WRONG_PASSWORD.getException();
    }

    private JwtResponse buildJwtResponse(User user) {
        final String accessToken = jwtProvider.generateAccessToken(user);
        RefreshToken refreshToken = jwtProvider.generateRefreshToken(user);
        refreshTokenRepository.save(refreshToken);
        return new JwtResponse(accessToken, refreshToken.getToken());
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final Long userId = Long.valueOf(claims.getSubject());
            final RefreshToken saveRefreshToken = refreshTokenRepository.findById(userId)
                    .orElseThrow(TOKEN_INVALID.getException());
            if (saveRefreshToken.getToken().equals(refreshToken)) {
                final User user = userService.getById(userId)
                        .orElseThrow(ACCOUNT_NOT_FOUND.getException());
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        throw TOKEN_INVALID.getException();
    }

    public JwtResponse refresh(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final Long userId = Long.valueOf(claims.getSubject());
            final RefreshToken saveRefreshToken = refreshTokenRepository.findById(userId)
                    .orElseThrow(TOKEN_INVALID.getException());
            if (saveRefreshToken.getToken().equals(refreshToken)) {
                final User user = userService.getById(userId)
                        .orElseThrow(ACCOUNT_NOT_FOUND.getException());
                final String accessToken = jwtProvider.generateAccessToken(user);
                final RefreshToken newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshTokenRepository.save(newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken.getToken());
            }
        }
        throw TOKEN_INVALID.getException();
    }

    public Long getUserId() {
        return (Long) getAuthInfo().getPrincipal();
    }

    public boolean isAdmin() {
        return getAuthInfo().getAuthorities().contains(Role.ADMIN);
    }

    private JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
}
