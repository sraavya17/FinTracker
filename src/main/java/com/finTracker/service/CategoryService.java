package com.finTracker.service;

import java.util.List;

import com.finTracker.entity.CategoryType;
import com.finTracker.exception.FinTrackerException;
import com.finTracker.request.CategoryRequest;
import com.finTracker.request.CategoryUpdateRequest;
import com.finTracker.response.CategoryResponse;

public interface CategoryService {
	
	CategoryResponse createCategory(String email, CategoryRequest request) throws FinTrackerException;
	
	List<CategoryResponse> getAllCategories(String email, CategoryType categoryType) throws FinTrackerException;
	
	CategoryResponse updateCategory(String email, Integer categoryId, CategoryUpdateRequest request) throws FinTrackerException;
	
	void deleteCategory(String email, Integer categoryId) throws FinTrackerException;

}
