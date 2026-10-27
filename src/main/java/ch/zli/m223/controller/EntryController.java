package ch.zli.m223.controller;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.zli.m223.dto.EntryRequestDto;
import ch.zli.m223.dto.EntryResponseDto;
import ch.zli.m223.service.EntryService;

@Path("/entries")
@Tag(name = "Entries", description = "Handling of entries")
public class EntryController {

    @Inject
    EntryService entryService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Index all Entries.", description = "Returns a list of all entries.")
    public List<EntryResponseDto> index() {
        return entryService.findAll().stream()
            .map(EntryResponseDto::fromEntity)
            .toList();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Creates a new entry.", description = "Creates a new entry and returns the newly added entry.")
    public EntryResponseDto create(@Valid EntryRequestDto entryDto) {
        return EntryResponseDto.fromEntity(entryService.createEntry(entryDto.toEntity(), entryDto.categoryId(), entryDto.tagIds()));
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Updates an entry.", description = "Updates the entry with the given id and returns the updated entry. Returns 404 if it does not exist.")
    public EntryResponseDto update(@PathParam("id") Long id, @Valid EntryRequestDto entryDto) {
        return EntryResponseDto.fromEntity(entryService.updateEntry(id, entryDto.toEntity(), entryDto.categoryId(), entryDto.tagIds()));
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Deletes an entry.", description = "Deletes the entry with the given id. Returns 404 if it does not exist.")
    public void delete(@PathParam("id") Long id) {
        entryService.deleteEntry(id);
    }

}
