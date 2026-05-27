package com.finTracker.service;

import com.finTracker.exception.FinTrackerException;
import com.finTracker.request.ProfileUpdateRequestDTO;
import com.finTracker.response.UserResponseDTO;

public interface UserService {
	public UserResponseDTO getProfile(String email) throws FinTrackerException;
	public UserResponseDTO updateProfile(String email, ProfileUpdateRequestDTO request) throws FinTrackerException;

}
