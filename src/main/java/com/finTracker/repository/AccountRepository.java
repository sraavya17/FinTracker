package com.finTracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finTracker.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{
	
	Boolean existsByUser_UserIdAndAccountNameIgnoreCase(Integer userId, String accountName);
	
	List<Account> findByUser_UserIdAndIsActiveTrue(Integer userId);

}
