package com.finTracker.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.finTracker.entity.AccountType;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AccountResponseDTO {
	
	private Integer accountId;
	private String accountName;
	private AccountType accountType;
	private BigDecimal balance;
	private Boolean isActive;
	private LocalDateTime createdAt;

}
