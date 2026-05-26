package com.finTracker.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {
	private Integer userId;
	private String name;
	private String email;
	private String preferredCurrency;

}
