package com.finTracker.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finTracker.request.AccountRequestDTO;
import com.finTracker.response.AccountResponseDTO;
import com.finTracker.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
	
	private final AccountService accountService;
	
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@PostMapping
	public ResponseEntity<AccountResponseDTO> createAccount(@AuthenticationPrincipal UserDetails userDetails, 
			@RequestBody AccountRequestDTO request) throws Exception {
		AccountResponseDTO response = accountService.createAccount(userDetails.getUsername(), request);
		
		return new ResponseEntity<AccountResponseDTO>(response, HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<AccountResponseDTO>> getAllAccounts(@AuthenticationPrincipal UserDetails userDetails) throws Exception {
		List<AccountResponseDTO> response = accountService.getAllAccounts(userDetails.getUsername());
		
		return new ResponseEntity<List<AccountResponseDTO>>(response, HttpStatus.OK);
	}

}
