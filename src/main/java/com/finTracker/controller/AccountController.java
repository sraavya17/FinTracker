package com.finTracker.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finTracker.request.AccountRequestDTO;
import com.finTracker.request.AccountUpdateRequestDTO;
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
	
	@PatchMapping("/{accountId}")
	public ResponseEntity<AccountResponseDTO> updateAccount(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer accountId, 
			@RequestBody AccountUpdateRequestDTO request) throws Exception {
		AccountResponseDTO response = accountService.updateAccount(userDetails.getUsername(), accountId, request);
		
		return new ResponseEntity<AccountResponseDTO>(response, HttpStatus.OK);
	}
	
	@PatchMapping("/{accountId}/deactivate")
	public ResponseEntity<String> deactivateAccount(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer accountId) 
			throws Exception {
		accountService.deactivateAccount(userDetails.getUsername(), accountId);
		return new ResponseEntity<String>("Account deactivated successfully", HttpStatus.OK);
		
	}

}
