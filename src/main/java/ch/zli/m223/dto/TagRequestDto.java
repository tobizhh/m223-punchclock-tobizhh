package ch.zli.m223.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import ch.zli.m223.model.Tag;

public record TagRequestDto(
    @NotBlank(message = "title is required.")
    @Size(max = 255, message = "title must not be longer than 255 characters.") String title) {

    public Tag toEntity() {
        Tag tag = new Tag();
        tag.setTitle(title);
        return tag;
    }
}
