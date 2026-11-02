package com.jobmanager.profile;

import com.jobmanager.common.exception.DuplicateResourceException;
import com.jobmanager.common.exception.ResourceNotFoundException;
import com.jobmanager.profile.dto.ProfileCreateRequest;
import com.jobmanager.profile.dto.ProfileResponse;
import com.jobmanager.profile.dto.ProfileUpdateRequest;
import com.jobmanager.profile.entity.Profile;
import com.jobmanager.profile.entity.ProfileStatus;
import com.jobmanager.profile.mapper.ProfileMapper;
import com.jobmanager.profile.repository.ProfileRepository;
import com.jobmanager.profile.service.ProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Spy
    private ProfileMapper profileMapper;

    @InjectMocks
    private ProfileServiceImpl profileService;

    private Profile sampleProfile;
    private UUID sampleId;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();
        sampleProfile = new Profile();
        sampleProfile.setId(sampleId);
        sampleProfile.setFullName("Jane Architect");
        sampleProfile.setEmail("jane.architect@example.com");
        sampleProfile.setCurrentDesignation("Principal Cloud Architect");
        sampleProfile.setSummary("12+ years in cloud native architectures");
        sampleProfile.setLocation("New York, NY");
        sampleProfile.setStatus(ProfileStatus.ACTIVE);
        sampleProfile.setCreatedAt(Instant.now());
        sampleProfile.setUpdatedAt(Instant.now());
    }

    @Test
    @DisplayName("createProfile should persist and return ProfileResponse when email is unique")
    void createProfile_Success() {
        ProfileCreateRequest request = new ProfileCreateRequest();
        request.setFullName("Jane Architect");
        request.setEmail("jane.architect@example.com");
        request.setCurrentDesignation("Principal Cloud Architect");
        request.setLocation("New York, NY");

        when(profileRepository.existsByEmail("jane.architect@example.com")).thenReturn(false);
        when(profileRepository.save(any(Profile.class))).thenAnswer(invocation -> {
            Profile toSave = invocation.getArgument(0);
            toSave.setId(sampleId);
            toSave.setCreatedAt(Instant.now());
            toSave.setUpdatedAt(Instant.now());
            return toSave;
        });

        ProfileResponse response = profileService.createProfile(request);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(sampleId);
        assertThat(response.fullName()).isEqualTo("Jane Architect");
        assertThat(response.email()).isEqualTo("jane.architect@example.com");
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    @DisplayName("createProfile should throw DuplicateResourceException when email already exists")
    void createProfile_DuplicateEmail_ThrowsException() {
        ProfileCreateRequest request = new ProfileCreateRequest();
        request.setFullName("Jane Architect");
        request.setEmail("jane.architect@example.com");
        request.setCurrentDesignation("Principal Cloud Architect");

        when(profileRepository.existsByEmail("jane.architect@example.com")).thenReturn(true);

        assertThatThrownBy(() -> profileService.createProfile(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("jane.architect@example.com");

        verify(profileRepository, never()).save(any(Profile.class));
    }

    @Test
    @DisplayName("getProfileById should return ProfileResponse when profile exists")
    void getProfileById_Success() {
        when(profileRepository.findById(sampleId)).thenReturn(Optional.of(sampleProfile));

        ProfileResponse response = profileService.getProfileById(sampleId);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(sampleId);
        assertThat(response.fullName()).isEqualTo("Jane Architect");
    }

    @Test
    @DisplayName("getProfileById should throw ResourceNotFoundException when profile does not exist")
    void getProfileById_NotFound_ThrowsException() {
        UUID unknownId = UUID.randomUUID();
        when(profileRepository.findById(unknownId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> profileService.getProfileById(unknownId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(unknownId.toString());
    }

    @Test
    @DisplayName("updateProfile should update fields and save when valid")
    void updateProfile_Success() {
        ProfileUpdateRequest request = new ProfileUpdateRequest();
        request.setFullName("Jane Architect Updated");
        request.setEmail("jane.updated@example.com");
        request.setCurrentDesignation("Chief Enterprise Architect");
        request.setStatus(ProfileStatus.ACTIVE);

        when(profileRepository.findById(sampleId)).thenReturn(Optional.of(sampleProfile));
        when(profileRepository.existsByEmailAndIdNot("jane.updated@example.com", sampleId)).thenReturn(false);
        when(profileRepository.save(any(Profile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProfileResponse response = profileService.updateProfile(sampleId, request);

        assertThat(response).isNotNull();
        assertThat(response.fullName()).isEqualTo("Jane Architect Updated");
        assertThat(response.currentDesignation()).isEqualTo("Chief Enterprise Architect");
        verify(profileRepository).save(sampleProfile);
    }

    @Test
    @DisplayName("deleteProfile should delete entity when exists")
    void deleteProfile_Success() {
        when(profileRepository.findById(sampleId)).thenReturn(Optional.of(sampleProfile));

        profileService.deleteProfile(sampleId);

        verify(profileRepository).delete(sampleProfile);
    }

    @Test
    @DisplayName("deleteProfile should throw ResourceNotFoundException when not found")
    void deleteProfile_NotFound_ThrowsException() {
        UUID unknownId = UUID.randomUUID();
        when(profileRepository.findById(unknownId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> profileService.deleteProfile(unknownId))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(profileRepository, never()).delete(any(Profile.class));
    }
}
