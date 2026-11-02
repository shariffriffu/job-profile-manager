package com.jobmanager.profile.service;

import com.jobmanager.profile.dto.ProfileCreateRequest;
import com.jobmanager.profile.dto.ProfileResponse;
import com.jobmanager.profile.dto.ProfileUpdateRequest;

import java.util.UUID;

public interface ProfileService {

    ProfileResponse createProfile(ProfileCreateRequest request);

    ProfileResponse getProfileById(UUID id);

    ProfileResponse updateProfile(UUID id, ProfileUpdateRequest request);

    void deleteProfile(UUID id);
}
