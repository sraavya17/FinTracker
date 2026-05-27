package com.finTracker.service;

import com.finTracker.exception.FinTrackerException;
import com.finTracker.request.LoginRequestDTO;
import com.finTracker.request.UserRequestDTO;
import com.finTracker.response.LoginResponseDTO;
import com.finTracker.response.UserResponseDTO;

public interface AuthService {

	UserResponseDTO register(UserRequestDTO request) throws FinTrackerException;
	
	LoginResponseDTO login(LoginRequestDTO request) throws FinTrackerException;
}
