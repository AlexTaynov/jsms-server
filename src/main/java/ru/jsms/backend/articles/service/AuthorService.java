package ru.jsms.backend.articles.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.jsms.backend.articles.dto.request.CreateAuthorRequest;
import ru.jsms.backend.articles.dto.response.AuthorFullResponse;
import ru.jsms.backend.articles.dto.response.AuthorResponse;
import ru.jsms.backend.articles.dto.response.OfferArticleResponse;
import ru.jsms.backend.articles.entity.Author;
import ru.jsms.backend.articles.repository.AuthorRepository;
import ru.jsms.backend.common.dto.PageDto;
import ru.jsms.backend.common.dto.PageParam;

import java.util.stream.Collectors;

import static ru.jsms.backend.articles.enums.ArticleExceptionCode.AUTHOR_ALREADY_EXISTS;
import static ru.jsms.backend.articles.enums.ArticleExceptionCode.AUTHOR_DELETE_DENIED;
import static ru.jsms.backend.articles.enums.ArticleExceptionCode.AUTHOR_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public PageDto<AuthorResponse> getAuthorsByFullname(PageParam pageParam, String fullnameSubstring) {
        return new PageDto<>(authorRepository.findByFullnameLike(fullnameSubstring, pageParam.toPageable())
                .map(this::convertToResponse));
    }

    public AuthorFullResponse getAuthor(Long authorId) {
        return convertToFullResponse(authorRepository.findById(authorId).orElseThrow(AUTHOR_NOT_FOUND.getException()));
    }

    public AuthorResponse createAuthor(CreateAuthorRequest request) {
        if (authorRepository.findByEmail(request.getEmail()).isPresent()) {
            throw AUTHOR_ALREADY_EXISTS.getException();
        }
        Author author = Author.builder()
                .firstName(request.getFirstName())
                .secondName(request.getSecondName())
                .patronymic(request.getPatronymic())
                .email(request.getEmail())
                .build();
        return convertToResponse(authorRepository.save(author));
    }

    public void deleteAuthor(Long authorId) {
        Author author = authorRepository.findById(authorId).orElse(null);
        if (author == null) {
            return;
        }
        if (!author.getArticles().isEmpty()) {
            throw AUTHOR_DELETE_DENIED.getException();
        }
        authorRepository.deleteById(authorId);
    }

    private AuthorResponse convertToResponse(Author author) {
        return new AuthorResponse(author);
    }

    private AuthorFullResponse convertToFullResponse(Author author) {
        return AuthorFullResponse.builder()
                .id(author.getId())
                .email(author.getEmail())
                .firstName(author.getFirstName())
                .secondName(author.getSecondName())
                .patronymic(author.getPatronymic())
                .articles(author.getArticles().stream().map(OfferArticleResponse::new).collect(Collectors.toList()))
                .build();
    }
}
