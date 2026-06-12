package com.finTracker.response;

import java.time.LocalDateTime;

import com.finTracker.entity.CategoryType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {

	private Integer categoryId;
	private String categoryName;
	private CategoryType categoryType;
	private LocalDateTime createdAt;
}
