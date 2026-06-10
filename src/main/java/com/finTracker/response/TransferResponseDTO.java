package com.finTracker.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferResponseDTO {
	
	private Integer fromAccountId;
	private String fromAccountName;
	private BigDecimal fromAccountBalance;
	
	private Integer toAccountId;
	private String toAccountName;
	private BigDecimal toAccountBalance;
	
	private BigDecimal transferAmount;
	private String note;
	private LocalDateTime transferredAt;

}
