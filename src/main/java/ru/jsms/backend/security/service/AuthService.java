package ru.jsms.backend.security.service;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.jsms.backend.security.domain.JwtAuthentication;
import ru.jsms.backend.security.domain.JwtRequest;
import ru.jsms.backend.security.domain.JwtResponse;
import ru.jsms.backend.security.domain.Role;
import ru.jsms.backend.security.entity.RefreshToken;
import ru.jsms.backend.security.entity.User;
import ru.jsms.backend.security.exception.AuthException;
import ru.jsms.backend.security.repository.RefreshTokenRepository;

import java.util.Set;

import static ru.jsms.backend.security.enums.AuthExceptionCode.ACCOUNT_NOT_FOUND;
import static ru.jsms.backend.security.enums.AuthExceptionCode.TOKEN_INVALID;
import static ru.jsms.backend.security.enums.AuthExceptionCode.WRONG_PASSWORD;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    public JwtResponse register(@NonNull JwtRequest authRequest) {
        if (userService.getById(authRequest.getLogin()).isPresent())
            throw new AuthException("Пользователь с таким id уже существует");
        User user = userService.createUser(authRequest.getLogin(), authRequest.getPassword(), Set.of(Role.USER));

        final String accessToken = jwtProvider.generateAccessToken(user);
        RefreshToken refreshToken = jwtProvider.generateRefreshToken(user);
        refreshTokenRepository.save(refreshToken);
        return new JwtResponse(accessToken, refreshToken.getToken());
    }

    public JwtResponse login(@NonNull JwtRequest authRequest) {
        final User user = userService.getById(authRequest.getLogin())
                .orElseThrow(ACCOUNT_NOT_FOUND.getException());
        if (user.getPassword().equals(authRequest.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            RefreshToken refreshToken = jwtProvider.generateRefreshToken(user);
            refreshTokenRepository.save(refreshToken);
            return new JwtResponse(accessToken, refreshToken.getToken());
        } else {
            throw WRONG_PASSWORD.getException();
        }
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final Long userId = Long.valueOf(claims.getSubject());
            final RefreshToken saveRefreshToken = refreshTokenRepository.findById(userId)
                    .orElseThrow(() -> new AuthException("Рефреш токен не найден"));
            if (saveRefreshToken.getToken().equals(refreshToken)) {
                final User user = userService.getById(userId)
                        .orElseThrow(ACCOUNT_NOT_FOUND.getException());
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final Long userId = Long.valueOf(claims.getSubject());
            final RefreshToken saveRefreshToken = refreshTokenRepository.findById(userId)
                    .orElseThrow(() -> new AuthException("Рефреш токен не найден"));
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

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}
