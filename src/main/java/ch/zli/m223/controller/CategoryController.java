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

import ch.zli.m223.dto.CategoryRequestDto;
import ch.zli.m223.dto.CategoryResponseDto;
import ch.zli.m223.service.CategoryService;

@Path("/categories")
@Tag(name = "Categories", description = "Handling of categories")
public class CategoryController {

    @Inject
    CategoryService categoryService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Index all Categories.", description = "Returns a list of all categories.")
    public List<CategoryResponseDto> index() {
        return categoryService.findAll().stream()
            .map(CategoryResponseDto::fromEntity)
            .toList();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Gets a category.", description = "Returns the category with the given id. Returns 404 if it does not exist.")
    public CategoryResponseDto get(@PathParam("id") Long id) {
        return CategoryResponseDto.fromEntity(categoryService.findById(id));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Creates a new category.", description = "Creates a new category and returns the newly added category.")
    public CategoryResponseDto create(@Valid CategoryRequestDto categoryDto) {
        return CategoryResponseDto.fromEntity(categoryService.createCategory(categoryDto.toEntity()));
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Updates a category.", description = "Updates the category with the given id and returns the updated category. Returns 404 if it does not exist.")
    public CategoryResponseDto update(@PathParam("id") Long id, @Valid CategoryRequestDto categoryDto) {
        return CategoryResponseDto.fromEntity(categoryService.updateCategory(id, categoryDto.toEntity()));
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Deletes a category.", description = "Deletes the category with the given id. Entries referencing it lose their category. Returns 404 if it does not exist.")
    public void delete(@PathParam("id") Long id) {
        categoryService.deleteCategory(id);
    }

}
