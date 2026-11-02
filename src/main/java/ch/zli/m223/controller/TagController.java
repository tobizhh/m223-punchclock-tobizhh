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

import ch.zli.m223.dto.TagRequestDto;
import ch.zli.m223.dto.TagResponseDto;
import ch.zli.m223.service.TagService;

@Path("/tags")
@Tag(name = "Tags", description = "Handling of tags")
public class TagController {

    @Inject
    TagService tagService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Index all Tags.", description = "Returns a list of all tags.")
    public List<TagResponseDto> index() {
        return tagService.findAll().stream()
            .map(TagResponseDto::fromEntity)
            .toList();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Gets a tag.", description = "Returns the tag with the given id. Returns 404 if it does not exist.")
    public TagResponseDto get(@PathParam("id") Long id) {
        return TagResponseDto.fromEntity(tagService.findById(id));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Creates a new tag.", description = "Creates a new tag and returns the newly added tag.")
    public TagResponseDto create(@Valid TagRequestDto tagDto) {
        return TagResponseDto.fromEntity(tagService.createTag(tagDto.toEntity()));
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Updates a tag.", description = "Updates the tag with the given id and returns the updated tag. Returns 404 if it does not exist.")
    public TagResponseDto update(@PathParam("id") Long id, @Valid TagRequestDto tagDto) {
        return TagResponseDto.fromEntity(tagService.updateTag(id, tagDto.toEntity()));
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Deletes a tag.", description = "Deletes the tag with the given id. Entries referencing it lose this tag. Returns 404 if it does not exist.")
    public void delete(@PathParam("id") Long id) {
        tagService.deleteTag(id);
    }

}
