package com.finTracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finTracker.request.ProfileUpdateRequestDTO;
import com.finTracker.response.UserResponseDTO;
import com.finTracker.service.UserService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/profile")
	public ResponseEntity<UserResponseDTO> getProfile(@AuthenticationPrincipal UserDetails userDetails) throws Exception {
		UserResponseDTO response = userService.getProfile(userDetails.getUsername());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PatchMapping("/profile")
	public ResponseEntity<UserResponseDTO> updateProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ProfileUpdateRequestDTO request) throws Exception {
		UserResponseDTO response = userService.updateProfile(userDetails.getUsername(), request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
