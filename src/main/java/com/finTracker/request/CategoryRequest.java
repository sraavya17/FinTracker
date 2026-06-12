package com.finTracker.request;

import com.finTracker.entity.CategoryType;

import lombok.Data;

@Data
public class CategoryRequest {
	
	private String categoryName;
	private CategoryType categoryType;

}
