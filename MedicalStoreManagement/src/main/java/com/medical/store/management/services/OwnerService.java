/**
 * 
 */
package com.medical.store.management.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medical.store.management.dao.UserDetailsDAO;
import com.medical.store.management.model.UserInfo;

/**
 * @author Shivam jaiswal
 * 28-Sept-2025
 */

@Service
public class OwnerService {
	
	@Autowired
	private UserDetailsDAO userDetailsDAO;
	
	public List<UserInfo> getAllUsersByRole(String role) {
	    return userDetailsDAO.getAllUsersByRole(role);
	}

	public boolean deactivateUser(Long userId, String status) {
	   return userDetailsDAO.deactivateUser(userId, status);
	}

}
