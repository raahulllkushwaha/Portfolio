package com.portfolio.service;

import com.portfolio.dto.ProfileDto;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.model.Profile;
import com.portfolio.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    /** Returns the single profile (portfolio owner). Creates empty one if none exists. */
    public ProfileDto.Response getProfile() {
        Profile profile = profileRepository.findAll()
                .stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found. Please create one."));
        return toResponse(profile);
    }

    public ProfileDto.Response createProfile(ProfileDto.Request request) {
        if (!profileRepository.findAll().isEmpty()) {
            throw new IllegalStateException("A profile already exists. Use update instead.");
        }
        Profile profile = toEntity(request);
        return toResponse(profileRepository.save(profile));
    }

    public ProfileDto.Response updateProfile(Long id, ProfileDto.Request request) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + id));
        mapRequestToEntity(request, profile);
        return toResponse(profileRepository.save(profile));
    }

    public void deleteProfile(Long id) {
        if (!profileRepository.existsById(id)) {
            throw new ResourceNotFoundException("Profile not found with id: " + id);
        }
        profileRepository.deleteById(id);
    }

    // ---- Mappers ----

    private Profile toEntity(ProfileDto.Request req) {
        return Profile.builder()
                .fullName(req.getFullName())
                .title(req.getTitle())
                .bio(req.getBio())
                .email(req.getEmail())
                .phone(req.getPhone())
                .location(req.getLocation())
                .profileImageUrl(req.getProfileImageUrl())
                .resumeUrl(req.getResumeUrl())
                .githubUrl(req.getGithubUrl())
                .linkedinUrl(req.getLinkedinUrl())
                .twitterUrl(req.getTwitterUrl())
                .websiteUrl(req.getWebsiteUrl())
                .build();
    }

    private void mapRequestToEntity(ProfileDto.Request req, Profile profile) {
        profile.setFullName(req.getFullName());
        profile.setTitle(req.getTitle());
        profile.setBio(req.getBio());
        profile.setEmail(req.getEmail());
        profile.setPhone(req.getPhone());
        profile.setLocation(req.getLocation());
        profile.setProfileImageUrl(req.getProfileImageUrl());
        profile.setResumeUrl(req.getResumeUrl());
        profile.setGithubUrl(req.getGithubUrl());
        profile.setLinkedinUrl(req.getLinkedinUrl());
        profile.setTwitterUrl(req.getTwitterUrl());
        profile.setWebsiteUrl(req.getWebsiteUrl());
    }

    private ProfileDto.Response toResponse(Profile p) {
        ProfileDto.Response res = new ProfileDto.Response();
        res.setId(p.getId());
        res.setFullName(p.getFullName());
        res.setTitle(p.getTitle());
        res.setBio(p.getBio());
        res.setEmail(p.getEmail());
        res.setPhone(p.getPhone());
        res.setLocation(p.getLocation());
        res.setProfileImageUrl(p.getProfileImageUrl());
        res.setResumeUrl(p.getResumeUrl());
        res.setGithubUrl(p.getGithubUrl());
        res.setLinkedinUrl(p.getLinkedinUrl());
        res.setTwitterUrl(p.getTwitterUrl());
        res.setWebsiteUrl(p.getWebsiteUrl());
        return res;
    }
}
