package com.finTracker.service;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finTracker.entity.User;
import com.finTracker.exception.FinTrackerException;
import com.finTracker.repository.UserRepository;
import com.finTracker.request.LoginRequestDTO;
import com.finTracker.request.UserRequestDTO;
import com.finTracker.response.LoginResponseDTO;
import com.finTracker.response.UserResponseDTO;
import com.finTracker.utility.JwtUtil;

@Service
@Transactional
public class AuthServiceImpl implements AuthService{
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	
	public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, 
			AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
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
			   .createdAt(LocalDateTime.now())
			   .build();
			   
	}
	
	public LoginResponseDTO login(LoginRequestDTO request) throws FinTrackerException {
		
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		} catch(BadCredentialsException exception) {
			throw new FinTrackerException("UserService.INVALID_CREDENTIALS");
		}
		
		User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new FinTrackerException("UserService.USER_NOT_FOUND"));
		String token = jwtUtil.generateToken(user.getEmail());
		
		return LoginResponseDTO.builder()
							   .userId(user.getUserId())
							   .name(user.getName())
							   .email(user.getEmail())
							   .preferredCurrency(user.getPreferredCurrency())
							   .token(token)
							   .tokenType("Bearer")
							   .build();
	}

}
