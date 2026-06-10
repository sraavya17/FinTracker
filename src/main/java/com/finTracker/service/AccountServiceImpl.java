package com.finTracker.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.finTracker.entity.Account;
import com.finTracker.entity.User;
import com.finTracker.exception.FinTrackerException;
import com.finTracker.repository.AccountRepository;
import com.finTracker.repository.UserRepository;
import com.finTracker.request.AccountRequestDTO;
import com.finTracker.request.AccountUpdateRequestDTO;
import com.finTracker.request.TransferRequestDTO;
import com.finTracker.response.AccountResponseDTO;
import com.finTracker.response.TransferResponseDTO;

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
		return mapToResponseDTO(save);
	}
	
	@Override
	public AccountResponseDTO updateAccount(String email, Integer accountId, AccountUpdateRequestDTO request)
			throws FinTrackerException {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new FinTrackerException("UserService.USER_NOT_FOUND"));
		
		Account account = accountRepository.findByAccountIdAndUser_UserId(accountId, user.getUserId()).orElseThrow(
				() -> new FinTrackerException("AccountService.ACCOUNT_NOT_FOUND"));
		//Check is account is active/inactive
		if(!account.getIsActive()) {
			throw new FinTrackerException("AccountService.ACCOUNT_INACTIVE");
		}
		//Checks for same account name for the user except its own account
		if(accountRepository.existsByUser_UserIdAndAccountNameIgnoreCaseAndAccountIdNot(user.getUserId(), request.getAccountName(), accountId)) {
			throw new FinTrackerException("AccountService.ACCOUNT_NAME_EXISTS");
		}
		//updating account details
		account.setAccountName(request.getAccountName());
		account.setAccountType(request.getAccountType());
		//account.setBalance(request.getBalance());
		
		Account save = accountRepository.save(account);
		
		return mapToResponseDTO(save);
	}

	@Override
	public List<AccountResponseDTO> getAllAccounts(String email) throws FinTrackerException {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new FinTrackerException("UserService.USER_NOT_FOUND"));
		//Fetching active accounts of a user
		List<Account> activeAccounts = accountRepository.findByUser_UserIdAndIsActiveTrue(user.getUserId());
		//Converting list from Account to AccountResponseDTO
		List<AccountResponseDTO> result = activeAccounts.stream().map(this::mapToResponseDTO).toList();
		return result;
	}
	
	@Override
	public void deactivateAccount(String email, Integer accountId) throws FinTrackerException {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new FinTrackerException("UserService.USER_NOT_FOUND"));
		Account account = accountRepository.findByAccountIdAndUser_UserId(accountId, user.getUserId()).orElseThrow(
				() -> new FinTrackerException("AccountService.ACCOUNT_NOT_FOUND"));
		
		if(!account.getIsActive()) {
			throw new FinTrackerException("AccountService.ACCOUNT_INACTIVE");
		}
		
		account.setIsActive(false);
		accountRepository.save(account);
	}
	
	@Override
	public TransferResponseDTO transfer(String email, TransferRequestDTO request) throws FinTrackerException {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new FinTrackerException("UserService.USER_NOT_FOUND"));

		if(request.getFromAccountId() == request.getToAccountId()) {
			throw new FinTrackerException("AccountService.TRANSFER_SAME_ACCOUNT");
		}
		
		Account fromAccount = accountRepository.findByAccountIdAndUser_UserId(request.getFromAccountId(), user.getUserId()).orElseThrow(
				() -> new FinTrackerException("AccountService.ACCOUNT_NOT_FOUND"));
		
		Account toAccount = accountRepository.findByAccountIdAndUser_UserId(request.getToAccountId(), user.getUserId()).orElseThrow(
				() -> new FinTrackerException("AccountService.ACCOUNT_NOT_FOUND"));
		
		if(!fromAccount.getIsActive()) {
			throw new FinTrackerException("AccountService.FROM_ACCOUNT_INACTIVE");
		}
		if(!toAccount.getIsActive()) {
			throw new FinTrackerException("AccountService.TO_ACCOUNT_INACTIVE");
		}
		
		if(fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
			throw new FinTrackerException("AccountService.INSUFFICIENT_BALANCE");
		}
		
		fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
		toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));
		
		accountRepository.save(fromAccount);
		accountRepository.save(toAccount);
		
		return TransferResponseDTO.builder()
								  .fromAccountId(fromAccount.getAccountId())
								  .fromAccountName(fromAccount.getAccountName())
								  .fromAccountBalance(fromAccount.getBalance())
								  .toAccountId(toAccount.getAccountId())
								  .toAccountName(toAccount.getAccountName())
								  .toAccountBalance(toAccount.getBalance())
								  .transferAmount(request.getAmount())
								  .note(request.getNote())
								  .transferredAt(LocalDateTime.now())
								  .build();
	}

	
	//Helper method to convert Account object to AccountResponseDTO object
	private AccountResponseDTO mapToResponseDTO(Account account) {
		
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
