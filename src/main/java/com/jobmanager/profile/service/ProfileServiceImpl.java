package com.jobmanager.profile.service;

import com.jobmanager.common.exception.DuplicateResourceException;
import com.jobmanager.common.exception.ResourceNotFoundException;
import com.jobmanager.profile.dto.ProfileCreateRequest;
import com.jobmanager.profile.dto.ProfileResponse;
import com.jobmanager.profile.dto.ProfileUpdateRequest;
import com.jobmanager.profile.entity.Profile;
import com.jobmanager.profile.mapper.ProfileMapper;
import com.jobmanager.profile.repository.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private static final Logger log = LoggerFactory.getLogger(ProfileServiceImpl.class);

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    public ProfileServiceImpl(ProfileRepository profileRepository, ProfileMapper profileMapper) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
    }

    @Override
    public ProfileResponse createProfile(ProfileCreateRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        log.info("Creating profile for email: {}", normalizedEmail);

        if (profileRepository.existsByEmail(normalizedEmail)) {
            throw new DuplicateResourceException("Profile", "email", normalizedEmail);
        }

        Profile profile = profileMapper.toEntity(request);
        Profile savedProfile = profileRepository.save(profile);
        log.info("Profile created successfully with ID: {}", savedProfile.getId());

        return profileMapper.toResponse(savedProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileResponse getProfileById(UUID id) {
        log.debug("Fetching profile by ID: {}", id);
        Profile profile = findProfileOrThrow(id);
        return profileMapper.toResponse(profile);
    }

    @Override
    public ProfileResponse updateProfile(UUID id, ProfileUpdateRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        log.info("Updating profile with ID: {}", id);

        Profile profile = findProfileOrThrow(id);

        if (profileRepository.existsByEmailAndIdNot(normalizedEmail, id)) {
            throw new DuplicateResourceException("Profile", "email", normalizedEmail);
        }

        profileMapper.updateEntity(profile, request);
        Profile updatedProfile = profileRepository.save(profile);
        log.info("Profile updated successfully with ID: {}", updatedProfile.getId());

        return profileMapper.toResponse(updatedProfile);
    }

    @Override
    public void deleteProfile(UUID id) {
        log.info("Deleting profile with ID: {}", id);
        Profile profile = findProfileOrThrow(id);
        profileRepository.delete(profile);
        log.info("Profile deleted successfully with ID: {}", id);
    }

    private Profile findProfileOrThrow(UUID id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "id", id));
    }
}
