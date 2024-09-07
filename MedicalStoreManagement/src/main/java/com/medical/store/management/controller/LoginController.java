/**
 * 
 */
package com.medical.store.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medical.store.management.model.LoginRequest;
import com.medical.store.management.model.UserInfo;
import com.medical.store.management.services.LoginService;

/**
 * @author Shivam jaiswal
 * 24-Aug-2024
 */

@RestController
@RequestMapping("/api/auth")
public class LoginController {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private LoginService loginService;
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/login")
	public Object AuthenticateAndGetToken(@RequestBody LoginRequest loginRequest){
	    	
	    return  loginService.getLogin(loginRequest);   

	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/signUp")
	public Object signUp(@RequestBody UserInfo userInfo) {
		
		userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
		
		return loginService.registerNewUser(userInfo);
	}

}
