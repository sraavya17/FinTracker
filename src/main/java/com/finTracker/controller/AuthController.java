package com.finTracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finTracker.request.LoginRequestDTO;
import com.finTracker.request.UserRequestDTO;
import com.finTracker.response.LoginResponseDTO;
import com.finTracker.response.UserResponseDTO;
import com.finTracker.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private final AuthService authService;
	
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDTO request) throws Exception {
		UserResponseDTO response = authService.register(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) throws Exception {
		LoginResponseDTO response = authService.login(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
