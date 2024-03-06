package ru.jsms.backend.common.dto;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
public class PageParam {
    private int page = 0;
    private int size = 10;

    public Pageable toPageable() {
        return PageRequest.of(page, size);
    }
}
