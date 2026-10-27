package ch.zli.m223.dto;

import ch.zli.m223.model.Category;

public record CategoryResponseDto(Long id, String title) {

    public static CategoryResponseDto fromEntity(Category category) {
        return new CategoryResponseDto(category.getId(), category.getTitle());
    }
}
