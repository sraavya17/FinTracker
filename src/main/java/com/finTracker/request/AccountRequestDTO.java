package com.finTracker.request;

import java.math.BigDecimal;

import com.finTracker.entity.AccountType;

import lombok.Data;

@Data
public class AccountRequestDTO {
	
	private String accountName;
	private AccountType accountType;
	private BigDecimal initialBalance;

}
