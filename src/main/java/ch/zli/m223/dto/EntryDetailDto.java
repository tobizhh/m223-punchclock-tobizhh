package ch.zli.m223.dto;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import ch.zli.m223.model.Entry;

public record EntryDetailDto(
    Long id,
    LocalDateTime checkIn,
    LocalDateTime checkOut,
    CategoryResponseDto category,
    List<TagResponseDto> tags) {

    public static EntryDetailDto fromEntity(Entry entry) {
        CategoryResponseDto category = entry.getCategory() == null
            ? null
            : CategoryResponseDto.fromEntity(entry.getCategory());
        List<TagResponseDto> tags = entry.getTags().stream()
            .map(TagResponseDto::fromEntity)
            .sorted(Comparator.comparing(TagResponseDto::id))
            .toList();
        return new EntryDetailDto(entry.getId(), entry.getCheckIn(), entry.getCheckOut(), category, tags);
    }
}
