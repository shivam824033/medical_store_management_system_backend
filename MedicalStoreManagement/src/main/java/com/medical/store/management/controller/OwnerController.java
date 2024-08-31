/**
 * 
 */
package com.medical.store.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medical.store.management.model.UserInfo;
import com.medical.store.management.secretkey.SecretKeyService;
import com.medical.store.management.security.config.JwtTokenUtility;

/**
 * @author Shivam jaiswal
 * 25-Aug-2024
 */

@RestController
@RequestMapping("/api/owner")
public class OwnerController {
	
	@Autowired
	private SecretKeyService keyService;
	
    @Autowired
    private JwtTokenUtility jwtService;
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/generatekey")
	public Object generateSecretKey( @RequestHeader("Authorization") String reqHeader) {
	
		System.out.print("key token : " + reqHeader);
	UserInfo user = jwtService.extractUserInfoFromJWT(reqHeader);
		
	   return keyService.generateSecretKey(user);
	   
	}

}
