package com.finTracker.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {
	private Integer userId;
	private String name;
	private String email;
	private String preferredCurrency;
	private String token;
	private String tokenType;

}
