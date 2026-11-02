package com.jobmanager.profile.mapper;

import com.jobmanager.profile.dto.ProfileCreateRequest;
import com.jobmanager.profile.dto.ProfileResponse;
import com.jobmanager.profile.dto.ProfileUpdateRequest;
import com.jobmanager.profile.entity.Profile;
import com.jobmanager.profile.entity.ProfileStatus;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public Profile toEntity(ProfileCreateRequest request) {
        if (request == null) {
            return null;
        }

        Profile profile = new Profile();
        profile.setUserId(request.getUserId());
        profile.setFullName(request.getFullName().trim());
        profile.setEmail(request.getEmail().trim().toLowerCase());
        profile.setPhone(request.getPhone() != null ? request.getPhone().trim() : null);
        profile.setHeadline(request.getHeadline() != null ? request.getHeadline().trim() : null);
        profile.setCurrentDesignation(request.getCurrentDesignation().trim());
        profile.setSummary(request.getSummary() != null ? request.getSummary().trim() : null);
        profile.setLocation(request.getLocation() != null ? request.getLocation().trim() : null);
        profile.setWebsiteUrl(request.getWebsiteUrl() != null ? request.getWebsiteUrl().trim() : null);
        profile.setLinkedinUrl(request.getLinkedinUrl() != null ? request.getLinkedinUrl().trim() : null);
        profile.setGithubUrl(request.getGithubUrl() != null ? request.getGithubUrl().trim() : null);
        profile.setStatus(ProfileStatus.DRAFT);
        return profile;
    }

    public void updateEntity(Profile profile, ProfileUpdateRequest request) {
        if (profile == null || request == null) {
            return;
        }

        profile.setFullName(request.getFullName().trim());
        profile.setEmail(request.getEmail().trim().toLowerCase());
        profile.setPhone(request.getPhone() != null ? request.getPhone().trim() : null);
        profile.setHeadline(request.getHeadline() != null ? request.getHeadline().trim() : null);
        profile.setCurrentDesignation(request.getCurrentDesignation().trim());
        profile.setSummary(request.getSummary() != null ? request.getSummary().trim() : null);
        profile.setLocation(request.getLocation() != null ? request.getLocation().trim() : null);
        profile.setWebsiteUrl(request.getWebsiteUrl() != null ? request.getWebsiteUrl().trim() : null);
        profile.setLinkedinUrl(request.getLinkedinUrl() != null ? request.getLinkedinUrl().trim() : null);
        profile.setGithubUrl(request.getGithubUrl() != null ? request.getGithubUrl().trim() : null);
        if (request.getStatus() != null) {
            profile.setStatus(request.getStatus());
        }
    }

    public ProfileResponse toResponse(Profile profile) {
        if (profile == null) {
            return null;
        }

        return new ProfileResponse(
                profile.getId(),
                profile.getUserId(),
                profile.getFullName(),
                profile.getEmail(),
                profile.getPhone(),
                profile.getHeadline(),
                profile.getCurrentDesignation(),
                profile.getSummary(),
                profile.getLocation(),
                profile.getWebsiteUrl(),
                profile.getLinkedinUrl(),
                profile.getGithubUrl(),
                profile.getStatus(),
                profile.getVersion(),
                profile.getCreatedAt(),
                profile.getUpdatedAt()
        );
    }
}
