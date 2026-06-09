package com.finTracker.request;



import com.finTracker.entity.AccountType;

import lombok.Data;

@Data
public class AccountUpdateRequestDTO {
	
	private String accountName;
	private AccountType accountType;
//	private BigDecimal balance;

}
