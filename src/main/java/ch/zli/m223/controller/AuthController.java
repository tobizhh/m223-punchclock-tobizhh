package ch.zli.m223.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.zli.m223.dto.LoginRequestDto;
import ch.zli.m223.dto.TokenResponseDto;
import ch.zli.m223.service.AuthService;

@Path("/auth")
@Tag(name = "Auth", description = "Login and token generation")
public class AuthController {

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Login.", description = "Checks username and password and returns a signed JWT.")
    public TokenResponseDto login(@Valid LoginRequestDto loginDto) {
        return new TokenResponseDto(authService.login(loginDto.username(), loginDto.password()));
    }
}
