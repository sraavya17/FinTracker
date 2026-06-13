package com.finTracker.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finTracker.entity.CategoryType;
import com.finTracker.request.CategoryRequest;
import com.finTracker.request.CategoryUpdateRequest;
import com.finTracker.response.CategoryResponse;
import com.finTracker.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	
	private final CategoryService categoryService;
	
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	@PostMapping
	public ResponseEntity<CategoryResponse> createCategory(@AuthenticationPrincipal UserDetails userDetails, 
			@RequestBody CategoryRequest request) throws Exception {
		CategoryResponse response = categoryService.createCategory(userDetails.getUsername(), request);
		
		return new ResponseEntity<CategoryResponse>(response, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<CategoryResponse>> getAllCategories(@AuthenticationPrincipal UserDetails userDetails, 
			@RequestParam(required = false) CategoryType categoryType) throws Exception {
		List<CategoryResponse> response = categoryService.getAllCategories(userDetails.getUsername(), categoryType);
		
		return new ResponseEntity<List<CategoryResponse>>(response, HttpStatus.OK);
	}
	
	@PatchMapping("/{categoryId}")
	public ResponseEntity<CategoryResponse> updateCategory(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer categoryId, 
			@RequestBody CategoryUpdateRequest request) throws Exception {
		CategoryResponse response = categoryService.updateCategory(userDetails.getUsername(), categoryId, request);
		
		return new ResponseEntity<CategoryResponse>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<String> deleteCategory(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer categoryId) throws Exception {
		categoryService.deleteCategory(userDetails.getUsername(), categoryId);
		
		return new ResponseEntity<String>("Category Deleted Successfully", HttpStatus.OK);
		
	}
}
