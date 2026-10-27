package ch.zli.m223.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import ch.zli.m223.model.Category;

public record CategoryRequestDto(
    @NotBlank(message = "title is required.")
    @Size(max = 255, message = "title must not be longer than 255 characters.") String title) {

    public Category toEntity() {
        Category category = new Category();
        category.setTitle(title);
        return category;
    }
}
