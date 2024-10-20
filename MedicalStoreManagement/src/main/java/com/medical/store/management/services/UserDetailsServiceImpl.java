/**
 * 
 */
package com.medical.store.management.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.medical.store.management.dao.UserDetailsDAO;
import com.medical.store.management.model.CustomUserDetails;
import com.medical.store.management.model.UserDetailsDTO;

/**
 * @author Shivam jaiswal
 * 24-Aug-2024
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	
	@Autowired
	private UserDetailsDAO userDetailsDAO;
	  
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Entering in loadUserByUsername Method...");
        UserDetailsDTO user = userDetailsDAO.loadByUserName(username);
        if(user == null){
            logger.error("Username not found: " + username);
            throw new UsernameNotFoundException("could not found user..!!");
        }
        logger.info("User Authenticated Successfully..!!!");
        return new CustomUserDetails(user);
	}

}
