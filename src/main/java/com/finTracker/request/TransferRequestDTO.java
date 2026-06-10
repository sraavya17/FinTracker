package com.finTracker.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransferRequestDTO {
	
	private Integer fromAccountId;
	private Integer toAccountId;
	private BigDecimal amount;
	private String note;

}
