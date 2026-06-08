package com.finTracker.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.finTracker.entity.Account;
import com.finTracker.entity.User;
import com.finTracker.exception.FinTrackerException;
import com.finTracker.repository.AccountRepository;
import com.finTracker.repository.UserRepository;
import com.finTracker.request.AccountRequestDTO;
import com.finTracker.response.AccountResponseDTO;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService{
	
	private final UserRepository userRepository;
	private final AccountRepository accountRepository;
	
	public AccountServiceImpl(UserRepository userRepository, AccountRepository accountRepository) {
		this.userRepository = userRepository;
		this.accountRepository = accountRepository;
	}

	@Override
	public AccountResponseDTO createAccount(String email, AccountRequestDTO request) throws FinTrackerException {
		//Check if user email exists
		User user = userRepository.findByEmail(email).orElseThrow(() -> new FinTrackerException("UserService.USER_NOT_FOUND"));
		//Checking if given account name already exists
		if(accountRepository.existsByUser_UserIdAndAccountNameIgnoreCase(user.getUserId(), request.getAccountName())) {
			throw new FinTrackerException("AccountService.ACCOUNT_NAME_EXISTS");
		}
		
		Account account = Account.builder()
								 .user(user)
								 .accountName(request.getAccountName())
								 .accountType(request.getAccountType())
								 .balance(request.getInitialBalance())
								 .build();
		
		Account save = accountRepository.save(account);
		return mapToRequestDTO(save);
	}

	@Override
	public List<AccountResponseDTO> getAllAccounts(String email) throws FinTrackerException {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new FinTrackerException("UserService.USER_NOT_FOUND"));
		//Fetching active accounts of a user
		List<Account> activeAccounts = accountRepository.findByUser_UserIdAndIsActiveTrue(user.getUserId());
		//Converting list from Account to AccountResponseDTO
		List<AccountResponseDTO> result = activeAccounts.stream().map(this::mapToRequestDTO).toList();
		return result;
	}
	
	//Helper method to convert Account object to AccountResponseDTO object
	private AccountResponseDTO mapToRequestDTO(Account account) {
		
		return AccountResponseDTO.builder()
							     .accountId(account.getAccountId())
							     .accountName(account.getAccountName())
							     .accountType(account.getAccountType())
							     .balance(account.getBalance())
							     .isActive(account.getIsActive())
							     .createdAt(account.getCreatedAt())
							     .build();
	}

}
