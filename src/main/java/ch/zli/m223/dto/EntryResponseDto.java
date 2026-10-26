package ch.zli.m223.dto;

import java.time.LocalDateTime;

import ch.zli.m223.model.Entry;

public record EntryResponseDto(Long id, LocalDateTime checkIn, LocalDateTime checkOut) {

    public static EntryResponseDto fromEntity(Entry entry) {
        return new EntryResponseDto(entry.getId(), entry.getCheckIn(), entry.getCheckOut());
    }
}
