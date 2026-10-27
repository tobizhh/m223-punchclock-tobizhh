package ch.zli.m223.dto;

import ch.zli.m223.model.Tag;

public record TagResponseDto(Long id, String title) {

    public static TagResponseDto fromEntity(Tag tag) {
        return new TagResponseDto(tag.getId(), tag.getTitle());
    }
}
