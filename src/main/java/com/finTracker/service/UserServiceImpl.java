package com.finTracker.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finTracker.entity.User;
import com.finTracker.exception.FinTrackerException;
import com.finTracker.repository.UserRepository;
import com.finTracker.request.ProfileUpdateRequestDTO;
import com.finTracker.response.UserResponseDTO;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserResponseDTO getProfile(String email) throws FinTrackerException {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new FinTrackerException("UserService.USER_NOT_FOUND"));
		
		return mapToUserResponseDTO(user);
	}
	
	public UserResponseDTO updateProfile(String email, ProfileUpdateRequestDTO request) throws FinTrackerException {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new FinTrackerException("UserService.USER_NOT_FOUND"));
		
		user.setPreferredCurrency(request.getPreferredCurrency());
		user.setName(request.getName());
		
		User save = userRepository.save(user);
		
		return mapToUserResponseDTO(save);
		
	}
	
	//private helper methods
	private UserResponseDTO mapToUserResponseDTO(User user) {
		return UserResponseDTO.builder()
				  .userId(user.getUserId())
				  .name(user.getName())
				  .email(user.getEmail())
				  .preferredCurrency(user.getPreferredCurrency())
				  .createdAt(user.getCreatedAt())
				  .build();
		
	}

}
