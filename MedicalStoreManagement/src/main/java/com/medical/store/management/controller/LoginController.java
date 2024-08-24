/**
 * 
 */
package com.medical.store.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.medical.store.management.model.LoginRequest;
import com.medical.store.management.model.UserInfo;
import com.medical.store.management.repository.UserInfoRepo;
import com.medical.store.management.security.config.JwtTokenUtility;
import com.medical.store.management.services.LoginService;

/**
 * @author Shivam jaiswal
 * 24-Aug-2024
 */

@RestController
public class LoginController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private LoginService loginService;
	
	
	@PostMapping("/api/auth/login")
	public Object AuthenticateAndGetToken(@RequestBody LoginRequest loginRequest){
	    	
	    return  loginService.getLogin(loginRequest);   

	}
	
	@PostMapping("/api/auth/signUp")
	public Object signUp(@RequestBody UserInfo userInfo) {
		
		userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
		
		return loginService.registerNewUser(userInfo);
	}

}
