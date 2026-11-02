package com.jobmanager.profile.dto;

import com.jobmanager.profile.entity.ProfileStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "Response payload representing a master professional profile")
public record ProfileResponse(
        @Schema(description = "Unique identifier of the profile", example = "9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d")
        UUID id,

        @Schema(description = "User ID associated with this profile", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID userId,

        @Schema(description = "Full name of the professional", example = "John Doe")
        String fullName,

        @Schema(description = "Email address", example = "john.doe@example.com")
        String email,

        @Schema(description = "Phone number", example = "+1-555-0199")
        String phone,

        @Schema(description = "Professional punchline/headline", example = "Senior Cloud Architect & Java Backend Specialist")
        String headline,

        @Schema(description = "Current job title or designation", example = "Lead Software Engineer")
        String currentDesignation,

        @Schema(description = "Professional summary", example = "Accomplished engineer with 10+ years designing enterprise distributed systems.")
        String summary,

        @Schema(description = "Geographic location", example = "San Francisco, CA, USA")
        String location,

        @Schema(description = "Website URL", example = "https://johndoe.dev")
        String websiteUrl,

        @Schema(description = "LinkedIn profile URL", example = "https://linkedin.com/in/johndoe")
        String linkedinUrl,

        @Schema(description = "GitHub profile URL", example = "https://github.com/johndoe")
        String githubUrl,

        @Schema(description = "Current lifecycle status", example = "ACTIVE")
        ProfileStatus status,

        @Schema(description = "Entity version", example = "0")
        Integer version,

        @Schema(description = "Timestamp when profile was created")
        Instant createdAt,

        @Schema(description = "Timestamp when profile was last modified")
        Instant updatedAt
) {
}
