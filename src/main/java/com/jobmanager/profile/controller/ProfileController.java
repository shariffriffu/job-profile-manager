package com.jobmanager.profile.controller;

import com.jobmanager.common.dto.ApiResponse;
import com.jobmanager.common.dto.ErrorResponse;
import com.jobmanager.profile.dto.ProfileCreateRequest;
import com.jobmanager.profile.dto.ProfileResponse;
import com.jobmanager.profile.dto.ProfileUpdateRequest;
import com.jobmanager.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/profiles")
@Tag(name = "Master Profile", description = "Endpoints for managing the master career profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    @Operation(summary = "Create master profile", description = "Creates a new master career profile with baseline professional details.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Profile created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation failure or malformed payload",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Profile with given email already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ApiResponse<ProfileResponse>> createProfile(
            @Valid @RequestBody ProfileCreateRequest request) {
        ProfileResponse response = profileService.createProfile(request);
        URI location = URI.create(String.format("/api/v1/profiles/%s", response.id()));
        return ResponseEntity.created(location)
                .body(ApiResponse.success(response, "Profile created successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get master profile by ID", description = "Retrieves master career profile details for the given unique identifier.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Profile retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Profile not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfileById(
            @Parameter(description = "UUID of the profile to retrieve", required = true)
            @PathVariable UUID id) {
        ProfileResponse response = profileService.getProfileById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Profile retrieved successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update master profile", description = "Updates an existing master career profile by its unique identifier.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Profile updated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation failure or malformed payload",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Profile not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Email conflicts with another existing profile",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile(
            @Parameter(description = "UUID of the profile to update", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody ProfileUpdateRequest request) {
        ProfileResponse response = profileService.updateProfile(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Profile updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete master profile", description = "Deletes a master career profile by its unique identifier.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "204",
                    description = "Profile deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Profile not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> deleteProfile(
            @Parameter(description = "UUID of the profile to delete", required = true)
            @PathVariable UUID id) {
        profileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}
