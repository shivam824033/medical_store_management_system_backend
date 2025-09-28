package com.medical.store.management.dao;

import java.util.List;

import com.medical.store.management.model.UserDetailsDTO;
import com.medical.store.management.model.UserInfo;

public interface UserDetailsDAO {
	
	public UserDetailsDTO loadByUserName(String username);
	public UserInfo findByUserName(String username);
    public UserInfo findByEmail(String email);
    public boolean isAcountActive(String username);
    public int registerUserDetails(UserDetailsDTO userDeatils);
    List<UserInfo> getAllUsersByRole(String role);
    public boolean deactivateUser(Long userId, String status);
}
