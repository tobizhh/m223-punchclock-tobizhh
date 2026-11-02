package ch.zli.m223.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/secure")
@Tag(name = "Secure", description = "Protected endpoints for testing the JWT authentication")
@SecurityRequirement(name = "BearerAuth")
public class SecureController {

    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/data")
    @RolesAllowed("Admin")
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(summary = "Protected data.", description = "Only reachable with a valid JWT and the role Admin.")
    public String getSecureData() {
        return "This is protected data for " + jwt.getName() + ".";
    }
}
