/**
 * 
 */
package com.medical.store.management.repository;

import org.springframework.stereotype.Repository;

import com.medical.store.management.model.UserInfo;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Shivam jaiswal
 * 24-Aug-2024
 */

@Repository
public interface UserInfoRepo extends JpaRepository<UserInfo, Long> {
	
	public UserInfo findByUsername(String username);

}
