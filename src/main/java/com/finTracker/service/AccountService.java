package com.finTracker.service;


import java.util.List;

import com.finTracker.exception.FinTrackerException;
import com.finTracker.request.AccountRequestDTO;
import com.finTracker.response.AccountResponseDTO;

public interface AccountService {
	
	AccountResponseDTO createAccount(String email, AccountRequestDTO request) throws FinTrackerException;
	
	List<AccountResponseDTO> getAllAccounts(String email) throws FinTrackerException;

}
