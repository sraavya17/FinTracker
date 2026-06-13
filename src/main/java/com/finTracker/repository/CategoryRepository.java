package com.finTracker.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finTracker.entity.Category;
import com.finTracker.entity.CategoryType;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	Boolean existsByUser_UserIdAndCategoryNameIgnoreCaseAndCategoryType(Integer userId, String categoryName, CategoryType categoryType);
	
	List<Category> findByUser_UserIdAndCategoryType(Integer userId, CategoryType categoryType);
	
	List<Category> findByUser_UserId(Integer userId);
	
	Optional<Category> findByUser_UserIdAndCategoryId(Integer userId, Integer categoryId);
	
	Boolean existsByUser_UserIdAndCategoryNameIgnoreCaseAndCategoryTypeAndCategoryIdNot(Integer userId, String categoryName, CategoryType categoryType, Integer categoryId);
	

}
