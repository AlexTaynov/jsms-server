package ru.jsms.backend.articles.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.jsms.backend.articles.dto.request.CreateAuthorRequest;
import ru.jsms.backend.articles.dto.response.AuthorFullResponse;
import ru.jsms.backend.articles.dto.response.AuthorResponse;
import ru.jsms.backend.articles.service.AuthorService;
import ru.jsms.backend.common.dto.PageDto;
import ru.jsms.backend.common.dto.PageParam;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('USER')")
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<PageDto<AuthorResponse>> getAuthors(PageParam pageParam) {
        return ResponseEntity.ok(authorService.getAuthors(pageParam));
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
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long authorId) {
        authorService.deleteAuthor(authorId);
        return ResponseEntity.ok().build();
    }
}
