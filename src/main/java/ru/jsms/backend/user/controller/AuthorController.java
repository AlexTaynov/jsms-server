package ru.jsms.backend.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.jsms.backend.user.dto.request.CreateAuthorRequest;
import ru.jsms.backend.user.dto.response.AuthorFullResponse;
import ru.jsms.backend.user.dto.response.AuthorResponse;
import ru.jsms.backend.user.service.AuthorService;
import ru.jsms.backend.common.dto.PageDto;
import ru.jsms.backend.common.dto.PageParam;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<PageDto<AuthorResponse>> getAuthorsByFullname(
            PageParam pageParam,
            @RequestParam(name = "fullname", defaultValue = "") String fullnameSubstring
    ) {
        return ResponseEntity.ok(authorService.getAuthorsByFullname(pageParam, fullnameSubstring));
    }

    @GetMapping("/{authorId}")
    public ResponseEntity<AuthorFullResponse> getAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(authorService.getAuthor(authorId));
    }

    @PostMapping
    public ResponseEntity<AuthorResponse> createAuthor(@Valid @RequestBody CreateAuthorRequest request) {
        return ResponseEntity.ok(authorService.createAuthor(request));
    }

    @DeleteMapping("/{authorId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long authorId) {
        authorService.deleteAuthor(authorId);
        return ResponseEntity.ok().build();
    }
}
