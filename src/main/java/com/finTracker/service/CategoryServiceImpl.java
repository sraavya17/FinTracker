package com.finTracker.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.finTracker.entity.Category;
import com.finTracker.entity.CategoryType;
import com.finTracker.entity.User;
import com.finTracker.exception.FinTrackerException;
import com.finTracker.repository.CategoryRepository;
import com.finTracker.repository.UserRepository;
import com.finTracker.request.CategoryRequest;
import com.finTracker.request.CategoryUpdateRequest;
import com.finTracker.response.CategoryResponse;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
	
	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;
	
	public CategoryServiceImpl(UserRepository userRepository, CategoryRepository categoryRepository) {
		this.userRepository = userRepository;
		this.categoryRepository = categoryRepository;
	}

	@Override
	public CategoryResponse createCategory(String email, CategoryRequest request) throws FinTrackerException {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new FinTrackerException("UserService.USER_NOT_FOUND"));
		
		if(categoryRepository.existsByUser_UserIdAndCategoryNameIgnoreCaseAndCategoryType(user.getUserId(), request.getCategoryName(), 
				request.getCategoryType())) {
			throw new FinTrackerException("CategoryService.CATEGORY_EXISTS");
		}
		
		Category category = Category.builder()
									.user(user)
									.categoryName(request.getCategoryName())
									.categoryType(request.getCategoryType())
									.build();
		Category save = categoryRepository.save(category);
		
		return mapToResponse(save);
	}
	
	@Override
	public List<CategoryResponse> getAllCategories(String email, CategoryType categoryType) throws FinTrackerException {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new FinTrackerException("UserService.USER_NOT_FOUND"));
		List<Category> categories;
		if(categoryType != null) {
			categories = categoryRepository.findByUser_UserIdAndCategoryType(user.getUserId(), categoryType);
		} else {
			categories = categoryRepository.findByUser_UserId(user.getUserId());
		}
		
		return categories.stream().map(this::mapToResponse).toList();
	}
	
	@Override
	public CategoryResponse updateCategory(String email, Integer categoryId, CategoryUpdateRequest request)
			throws FinTrackerException {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new FinTrackerException("UserService.USER_NOT_FOUND"));
		Category category = categoryRepository.findByUser_UserIdAndCategoryId(user.getUserId(), categoryId).orElseThrow(
				() -> new FinTrackerException("CategoryService.CATEGORY_NOT_FOUND"));
		if(categoryRepository.existsByUser_UserIdAndCategoryNameIgnoreCaseAndCategoryTypeAndCategoryIdNot(user.getUserId(),
				category.getCategoryName(), category.getCategoryType(), categoryId)) {
			throw new FinTrackerException("CategoryService.CATEGORY_EXISTS");
		}
		category.setCategoryName(request.getCategoryName());
		categoryRepository.save(category);
		return mapToResponse(category);
	}
	
	//private helper method
	private CategoryResponse mapToResponse(Category category) {
		return CategoryResponse.builder()
							   .categoryId(category.getCategoryId())
							   .categoryName(category.getCategoryName())
							   .categoryType(category.getCategoryType())
							   .createdAt(category.getCreatedAt())
							   .build();
	}

	@Override
	public void deleteCategory(String email, Integer categoryId) throws FinTrackerException {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new FinTrackerException("UserService.USER_NOT_FOUND"));
		Category category = categoryRepository.findByUser_UserIdAndCategoryId(user.getUserId(), categoryId).orElseThrow(
				() -> new FinTrackerException("CategoryService.CATEGORY_NOT_FOUND"));
		
		categoryRepository.delete(category);
		
	}



}
