package com.mcarrental.userservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;

@AllArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ItemsDTO<T> {

    private List<T> items;
    private Integer page;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long totalItems;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer totalPages;
    private boolean hasPrevious;
    private boolean hasNext;

    public static <T> ItemsDTO<T> fromList(List<T> items) {
        return new ItemsDTO<>(items, 0, (long) items.size(), 1, false, false);
    }

    public static <T> ItemsDTO<T> fromPage(Page<T> page) {
        List<T> content = page.getContent();
        return new ItemsDTO<>(content, page.getNumber(), page.getTotalElements(), page.getTotalPages(), page.hasPrevious(), page.hasNext());
    }

    public static <T> ItemsDTO<T> fromSlice(Slice<T> slice) {
        List<T> content = slice.getContent();
        return new ItemsDTO<>(content, slice.getNumber(), null, null, slice.hasPrevious(), slice.hasNext());
    }
}