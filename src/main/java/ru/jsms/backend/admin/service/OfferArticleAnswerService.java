package ru.jsms.backend.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.jsms.backend.admin.dto.request.EditOfferArticleAnswerRequest;
import ru.jsms.backend.admin.dto.response.OfferArticleAnswerResponse;
import ru.jsms.backend.admin.entity.OfferArticleAnswer;
import ru.jsms.backend.admin.repository.OfferArticleAnswerRepository;
import ru.jsms.backend.files.service.FileService;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.jsms.backend.admin.enums.AdminArticleExceptionCode.ANSWER_EDIT_DENIED;
import static ru.jsms.backend.admin.enums.AdminArticleExceptionCode.ANSWER_NOT_COMPLETE;
import static ru.jsms.backend.admin.enums.AdminArticleExceptionCode.ANSWER_NOT_FOUND;
import static ru.jsms.backend.common.utils.UuidUtils.parseUuid;

@Service
@RequiredArgsConstructor
public class OfferArticleAnswerService {

    private final OfferArticleAnswerRepository answerRepository;
    private final FileService fileService;

    public void submit(Long versionId) {
        OfferArticleAnswer answer = answerRepository.findByVersionId(versionId).orElseThrow(ANSWER_NOT_FOUND.getException());
        validCompleteAnswer(answer);
        answer.setDraft(false);
        answerRepository.save(answer);
    }

    private void validCompleteAnswer(OfferArticleAnswer answer) {
        if (answer.getDocumentId() == null && isBlank(answer.getComment())) {
            throw ANSWER_NOT_COMPLETE.getException();
        }
    }

    public OfferArticleAnswerResponse editAnswer(Long versionId, EditOfferArticleAnswerRequest request) {
        OfferArticleAnswer answer = answerRepository.findByVersionId(versionId)
                .orElse(OfferArticleAnswer.builder().versionId(versionId).build());
        if (!answer.isDraft()) {
            throw ANSWER_EDIT_DENIED.getException();
        }
        if (request.getDocumentId() != null) {
            fileService.validateAccess(request.getDocumentId());
        }
        answer.setDocumentId(parseUuid(request.getDocumentId()));
        answer.setComment(request.getComment());
        return convertToResponse(answerRepository.save(answer));
    }

    private OfferArticleAnswerResponse convertToResponse(OfferArticleAnswer answer) {
        return new OfferArticleAnswerResponse(answer);
    }
}
