package ch.zli.m223.dto;

import java.time.LocalDateTime;
import java.util.List;

import ch.zli.m223.model.Entry;
import ch.zli.m223.model.Tag;

public record EntryResponseDto(Long id, LocalDateTime checkIn, LocalDateTime checkOut, Long categoryId, List<Long> tagIds, Long userId) {

    public static EntryResponseDto fromEntity(Entry entry) {
        Long categoryId = entry.getCategory() == null ? null : entry.getCategory().getId();
        List<Long> tagIds = entry.getTags().stream()
            .map(Tag::getId)
            .sorted()
            .toList();
        Long userId = entry.getUser() == null ? null : entry.getUser().getId();
        return new EntryResponseDto(entry.getId(), entry.getCheckIn(), entry.getCheckOut(), categoryId, tagIds, userId);
    }
}
