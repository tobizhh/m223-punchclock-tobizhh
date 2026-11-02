package ch.zli.m223.controller;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.zli.m223.dto.EntryDetailDto;
import ch.zli.m223.service.EntryService;

@Path("/entry-details")
@Tag(name = "Entry Details", description = "Entries including their category and tags")
public class EntryDetailController {

    @Inject
    EntryService entryService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Index all Entries with details.", description = "Returns a list of all entries including their category and tags.")
    public List<EntryDetailDto> index() {
        return entryService.findAll().stream()
            .map(EntryDetailDto::fromEntity)
            .toList();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Gets an entry with details.", description = "Returns the entry with the given id including its category and tags. Returns 404 if it does not exist.")
    public EntryDetailDto get(@PathParam("id") Long id) {
        return EntryDetailDto.fromEntity(entryService.findById(id));
    }

}
