package ch.zli.m223.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ch.zli.m223.model.Entry;

public record EntryRequestDto(
    @NotNull(message = "checkIn is required.") LocalDateTime checkIn,
    @NotNull(message = "checkOut is required.") LocalDateTime checkOut,
    Long categoryId,
    List<@NotNull(message = "tagIds must not contain null.") Long> tagIds) {

    @JsonIgnore
    @Schema(hidden = true)
    @AssertTrue(message = "checkIn must be before checkOut.")
    public boolean isCheckInBeforeCheckOut() {
        return checkIn == null || checkOut == null || checkIn.isBefore(checkOut);
    }

    public Entry toEntity() {
        Entry entry = new Entry();
        entry.setCheckIn(checkIn);
        entry.setCheckOut(checkOut);
        return entry;
    }
}
