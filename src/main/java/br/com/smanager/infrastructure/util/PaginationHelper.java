package br.com.smanager.infrastructure.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public final class PaginationHelper {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private PaginationHelper() {}

    public static Pageable createPageable(Integer page, Integer pageSize, String directionStr, List<String> properties) {
        return PageRequest.of(
                Optional.ofNullable(page).orElse(0),
                Optional.of(pageSize).orElse(DEFAULT_PAGE_SIZE),
                createSort(directionStr, properties)
        );
    }

    private static Sort createSort(String directionStr, List<String> properties) {
        if (!properties.isEmpty()) {
            var direction = Sort.Direction.fromOptionalString(directionStr).orElse(Sort.Direction.ASC);
            return Sort.by(direction, properties.toArray(new String[0]));
        }
        return Sort.unsorted();
    }
}
