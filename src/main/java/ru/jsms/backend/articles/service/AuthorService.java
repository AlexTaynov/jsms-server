package ru.jsms.backend.articles.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.jsms.backend.articles.dto.request.CreateAuthorRequest;
import ru.jsms.backend.articles.dto.response.AuthorFullResponse;
import ru.jsms.backend.articles.dto.response.AuthorResponse;
import ru.jsms.backend.articles.entity.Author;
import ru.jsms.backend.articles.repository.AuthorRepository;
import ru.jsms.backend.common.dto.HeadersDto;
import ru.jsms.backend.common.dto.PageDto;
import ru.jsms.backend.common.dto.PageParam;

import java.util.stream.Collectors;

import static ru.jsms.backend.articles.enums.ArticleExceptionCode.AUTHOR_DELETE_DENIED;
import static ru.jsms.backend.articles.enums.ArticleExceptionCode.AUTHOR_NOT_FOUND;
import static ru.jsms.backend.common.utils.BaseOwneredEntityUtils.validateAccess;

@RequiredArgsConstructor
@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final OfferArticleService offerArticleService;
    private final HeadersDto headersDto;

    public PageDto<AuthorResponse> getAuthors(PageParam pageParam) {
        return new PageDto<>(authorRepository.findAll(pageParam.toPageable())
                .map(this::convertToResponse));
    }

    public AuthorFullResponse getAuthor(Long authorId) {
        return convertToFullResponse(authorRepository.findById(authorId).orElseThrow(AUTHOR_NOT_FOUND.getException()));
    }

    public AuthorResponse createAuthor(CreateAuthorRequest request) {
        Author author = Author.builder()
                .firstName(request.getFirstName())
                .secondName(request.getSecondName())
                .patronymic(request.getPatronymic())
                .email(request.getEmail())
                .ownerId(headersDto.getUserId())
                .build();
        return convertToResponse(authorRepository.save(author));
    }

    public void deleteAuthor(Long authorId) {
        Author author = authorRepository.findById(authorId).orElse(null);
        if (author == null) {
            return;
        }
        validateAccess(author, headersDto.getUserId());
        if (!author.getArticles().isEmpty()) {
            throw AUTHOR_DELETE_DENIED.getException();
        }
        authorRepository.deleteById(authorId);
    }

    public AuthorResponse convertToResponse(Author author) {
        return AuthorResponse.builder()
                .id(author.getId())
                .email(author.getEmail())
                .firstName(author.getFirstName())
                .secondName(author.getSecondName())
                .patronymic(author.getPatronymic())
                .build();
    }

    private AuthorFullResponse convertToFullResponse(Author author) {
        return AuthorFullResponse.builder()
                .id(author.getId())
                .email(author.getEmail())
                .firstName(author.getFirstName())
                .secondName(author.getSecondName())
                .patronymic(author.getPatronymic())
                .articles(author.getArticles().stream().map(offerArticleService::convertToResponse)
                        .collect(Collectors.toList()))
                .build();
    }
}
