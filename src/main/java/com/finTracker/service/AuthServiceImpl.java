package com.finTracker.service;

import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.finTracker.entity.User;
import com.finTracker.exception.FinTrackerException;
import com.finTracker.repository.UserRepository;
import com.finTracker.request.UserRequestDTO;
import com.finTracker.response.UserResponseDTO;

@Service
public class AuthServiceImpl implements AuthService{
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final Environment environment;
	
	public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, Environment environment) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.environment = environment;
	}
	
	public UserResponseDTO register(UserRequestDTO request) throws FinTrackerException {
		
		if(userRepository.existsByEmail(request.getEmail())) {
			throw new FinTrackerException("UserService.EMAIL_EXISTS");
		}
		User user = User.builder()
					.name(request.getName())
					.email(request.getEmail())
					.password(passwordEncoder.encode(request.getPassword()))
					.build();
		
		User save = userRepository.save(user);
		return UserResponseDTO.builder()
			   .userId(save.getUserId())
			   .name(save.getName())
			   .email(save.getEmail())
			   .preferredCurrency(save.getPreferredCurrency())
			   .build();
			   
	}

}
