package ru.jsms.backend.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageDto<T> {
    private int page;
    private int size;
    private long total;
    private List<T> data;

    public PageDto(Page<T> page) {
        this.page = page.getNumber();
        this.size = page.getSize();
        this.total = page.getTotalElements();
        this.data = page.getContent();
    }
}
