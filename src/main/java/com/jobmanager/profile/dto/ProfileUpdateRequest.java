package com.jobmanager.profile.dto;

import com.jobmanager.profile.entity.ProfileStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request payload for updating an existing master professional profile")
public class ProfileUpdateRequest {

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    @Schema(description = "Full legal or professional name", example = "John Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fullName;

    @NotBlank(message = "Email address is required")
    @Email(message = "Must be a well-formed email address")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    @Schema(description = "Primary contact email address", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Size(max = 30, message = "Phone number must not exceed 30 characters")
    @Schema(description = "Primary contact phone number", example = "+1-555-0199")
    private String phone;

    @Size(max = 200, message = "Headline must not exceed 200 characters")
    @Schema(description = "Professional punchline/headline", example = "Principal Architect & Tech Lead")
    private String headline;

    @NotBlank(message = "Current designation is required")
    @Size(max = 150, message = "Current designation must not exceed 150 characters")
    @Schema(description = "Current job title or designation", example = "Principal Software Architect", requiredMode = Schema.RequiredMode.REQUIRED)
    private String currentDesignation;

    @Size(max = 5000, message = "Summary must not exceed 5000 characters")
    @Schema(description = "Comprehensive professional executive summary", example = "Updated summary reflecting architectural leadership and cloud migrations.")
    private String summary;

    @Size(max = 150, message = "Location must not exceed 150 characters")
    @Schema(description = "Geographic location / City, Country", example = "Seattle, WA, USA")
    private String location;

    @Size(max = 255, message = "Website URL must not exceed 255 characters")
    @Schema(description = "Personal portfolio or blog URL", example = "https://johndoe.dev")
    private String websiteUrl;

    @Size(max = 255, message = "LinkedIn URL must not exceed 255 characters")
    @Schema(description = "LinkedIn profile URL", example = "https://linkedin.com/in/johndoe")
    private String linkedinUrl;

    @Size(max = 255, message = "GitHub URL must not exceed 255 characters")
    @Schema(description = "GitHub profile URL", example = "https://github.com/johndoe")
    private String githubUrl;

    @NotNull(message = "Profile status is required")
    @Schema(description = "Lifecycle status of the profile", example = "ACTIVE", requiredMode = Schema.RequiredMode.REQUIRED)
    private ProfileStatus status;

    public ProfileUpdateRequest() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getCurrentDesignation() {
        return currentDesignation;
    }

    public void setCurrentDesignation(String currentDesignation) {
        this.currentDesignation = currentDesignation;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public ProfileStatus getStatus() {
        return status;
    }

    public void setStatus(ProfileStatus status) {
        this.status = status;
    }
}
