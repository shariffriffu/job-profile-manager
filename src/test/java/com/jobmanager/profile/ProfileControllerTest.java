package com.jobmanager.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobmanager.common.exception.GlobalExceptionHandler;
import com.jobmanager.common.exception.ResourceNotFoundException;
import com.jobmanager.profile.controller.ProfileController;
import com.jobmanager.profile.dto.ProfileCreateRequest;
import com.jobmanager.profile.dto.ProfileResponse;
import com.jobmanager.profile.dto.ProfileUpdateRequest;
import com.jobmanager.profile.entity.ProfileStatus;
import com.jobmanager.profile.service.ProfileService;
import com.jobmanager.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProfileController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProfileService profileService;

    @Test
    @DisplayName("POST /api/v1/profiles should return 201 Created and Location header on valid input")
    void createProfile_ValidInput_Returns201() throws Exception {
        UUID profileId = UUID.randomUUID();
        ProfileCreateRequest request = new ProfileCreateRequest();
        request.setFullName("Alice Engineer");
        request.setEmail("alice@example.com");
        request.setCurrentDesignation("Staff Backend Engineer");
        request.setLocation("Boston, MA");

        ProfileResponse response = new ProfileResponse(
                profileId,
                null,
                "Alice Engineer",
                "alice@example.com",
                null,
                null,
                "Staff Backend Engineer",
                null,
                "Boston, MA",
                null,
                null,
                null,
                ProfileStatus.DRAFT,
                0,
                Instant.now(),
                Instant.now()
        );

        when(profileService.createProfile(any(ProfileCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/profiles/" + profileId))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(profileId.toString()))
                .andExpect(jsonPath("$.data.fullName").value("Alice Engineer"))
                .andExpect(jsonPath("$.data.email").value("alice@example.com"));
    }

    @Test
    @DisplayName("POST /api/v1/profiles should return 400 Bad Request when required fields are missing")
    void createProfile_InvalidInput_Returns400() throws Exception {
        ProfileCreateRequest invalidRequest = new ProfileCreateRequest();
        // Missing fullName, email, currentDesignation

        mockMvc.perform(post("/api/v1/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.validationErrors.fullName").exists())
                .andExpect(jsonPath("$.validationErrors.email").exists())
                .andExpect(jsonPath("$.validationErrors.currentDesignation").exists());
    }

    @Test
    @DisplayName("GET /api/v1/profiles/{id} should return 200 OK when profile exists")
    void getProfileById_Existing_Returns200() throws Exception {
        UUID profileId = UUID.randomUUID();
        ProfileResponse response = new ProfileResponse(
                profileId,
                null,
                "Alice Engineer",
                "alice@example.com",
                null,
                null,
                "Staff Backend Engineer",
                null,
                "Boston, MA",
                null,
                null,
                null,
                ProfileStatus.ACTIVE,
                0,
                Instant.now(),
                Instant.now()
        );

        when(profileService.getProfileById(profileId)).thenReturn(response);

        mockMvc.perform(get("/api/v1/profiles/{id}", profileId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(profileId.toString()))
                .andExpect(jsonPath("$.data.fullName").value("Alice Engineer"));
    }

    @Test
    @DisplayName("GET /api/v1/profiles/{id} should return 404 Not Found when profile does not exist")
    void getProfileById_NotFound_Returns404() throws Exception {
        UUID profileId = UUID.randomUUID();
        when(profileService.getProfileById(profileId))
                .thenThrow(new ResourceNotFoundException("Profile", "id", profileId));

        mockMvc.perform(get("/api/v1/profiles/{id}", profileId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(containsString(profileId.toString())));
    }

    @Test
    @DisplayName("PUT /api/v1/profiles/{id} should return 200 OK on successful update")
    void updateProfile_Success_Returns200() throws Exception {
        UUID profileId = UUID.randomUUID();
        ProfileUpdateRequest updateRequest = new ProfileUpdateRequest();
        updateRequest.setFullName("Alice Engineer Updated");
        updateRequest.setEmail("alice.updated@example.com");
        updateRequest.setCurrentDesignation("Principal Engineer");
        updateRequest.setStatus(ProfileStatus.ACTIVE);

        ProfileResponse response = new ProfileResponse(
                profileId,
                null,
                "Alice Engineer Updated",
                "alice.updated@example.com",
                null,
                null,
                "Principal Engineer",
                null,
                null,
                null,
                null,
                null,
                ProfileStatus.ACTIVE,
                1,
                Instant.now(),
                Instant.now()
        );

        when(profileService.updateProfile(eq(profileId), any(ProfileUpdateRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/profiles/{id}", profileId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.fullName").value("Alice Engineer Updated"))
                .andExpect(jsonPath("$.data.currentDesignation").value("Principal Engineer"));
    }

    @Test
    @DisplayName("DELETE /api/v1/profiles/{id} should return 204 No Content")
    void deleteProfile_Success_Returns204() throws Exception {
        UUID profileId = UUID.randomUUID();
        doNothing().when(profileService).deleteProfile(profileId);

        mockMvc.perform(delete("/api/v1/profiles/{id}", profileId))
                .andExpect(status().isNoContent());

        verify(profileService).deleteProfile(profileId);
    }
}
