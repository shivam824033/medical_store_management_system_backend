/**
 * 
 */
package com.medical.store.management.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medical.store.management.model.UserInfo;
import com.medical.store.management.secretkey.SecretKeyService;
import com.medical.store.management.security.config.JwtTokenUtility;
import com.medical.store.management.services.OwnerService;

/**
 * @author Shivam jaiswal 25-Aug-2024
 */

@RestController
@RequestMapping("/api/owner")
public class OwnerController {

	@Autowired
	private SecretKeyService keyService;

	@Autowired
	private JwtTokenUtility jwtService;
	
	@Autowired
	private OwnerService ownerService;

	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/generatekey")
	public Object generateSecretKey(@RequestHeader("Authorization") String reqHeader) {

		System.out.print("key token : " + reqHeader);
		UserInfo user = jwtService.extractUserInfoFromJWT(reqHeader);

		return keyService.generateSecretKey(user);

	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/users")
	public Map<String, Object> getAllUsers(@RequestParam(required = false) String role) {
	    // Fetch users from DB based on role (implement in your service/DAO)
	    List<UserInfo> users = ownerService.getAllUsersByRole(role);
	    return Map.of("response", users);
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/accountAction")
	public Map<String, Object> deactivateUser(@RequestBody Map<String, Object> req) {
	    Long userId = Long.valueOf(req.get("userId").toString());
	    String status = req.get("accountStatus").toString();
	    boolean result = ownerService.deactivateUser(userId, status);
	    return Map.of("success", result);
	}

}
