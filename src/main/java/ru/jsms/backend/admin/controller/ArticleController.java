package ru.jsms.backend.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.jsms.backend.admin.dto.request.EditArticleRequest;
import ru.jsms.backend.admin.dto.response.ArticleFullResponse;
import ru.jsms.backend.admin.dto.response.ArticleResponse;
import ru.jsms.backend.admin.service.ArticleService;
import ru.jsms.backend.common.dto.PageDto;
import ru.jsms.backend.common.dto.PageParam;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<PageDto<ArticleResponse>> getAllArticles(PageParam pageParam) {
        return ResponseEntity.ok(articleService.getAllArticles(pageParam));
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleFullResponse> getArticle(@PathVariable Long articleId) {
        return ResponseEntity.ok(articleService.getArticle(articleId));
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<ArticleFullResponse> editArticle(@PathVariable Long articleId,
                                                       @RequestBody EditArticleRequest request) {
        return ResponseEntity.ok(articleService.editArticle(articleId, request));
    }

}
