package com.finTracker.service;


import java.util.List;

import com.finTracker.exception.FinTrackerException;
import com.finTracker.request.AccountRequestDTO;
import com.finTracker.request.AccountUpdateRequestDTO;
import com.finTracker.response.AccountResponseDTO;

public interface AccountService {
	
	AccountResponseDTO createAccount(String email, AccountRequestDTO request) throws FinTrackerException;
	
	List<AccountResponseDTO> getAllAccounts(String email) throws FinTrackerException;
	
	AccountResponseDTO updateAccount(String email, Integer accountId, AccountUpdateRequestDTO request) throws FinTrackerException;
	
	void deactivateAccount(String email, Integer accountId) throws FinTrackerException;

}
