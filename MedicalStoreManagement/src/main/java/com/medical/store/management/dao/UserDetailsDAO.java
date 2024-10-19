package com.medical.store.management.dao;

import com.medical.store.management.model.UserDetailsDTO;
import com.medical.store.management.model.UserInfo;

public interface UserDetailsDAO {
	
	public UserDetailsDTO loadByUserName(String username);
	public UserInfo findByUserName(String username);
    public UserInfo findByEmail(String email);
    
    public int registerUserDetails(UserDetailsDTO userDeatils);
}
