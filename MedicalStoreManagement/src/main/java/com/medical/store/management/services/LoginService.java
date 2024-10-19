/**
 * 
 */
package com.medical.store.management.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.medical.store.management.dao.UserDetailsDAO;
import com.medical.store.management.model.LoginRequest;
import com.medical.store.management.model.LoginResponse;
import com.medical.store.management.model.UserDetailsDTO;
import com.medical.store.management.model.UserInfo;
import com.medical.store.management.secretkey.SecretKeyService;
import com.medical.store.management.security.config.JwtTokenUtility;
import com.medical.store.management.token.Token;
import com.medical.store.management.token.TokenDAO;

/**
 * @author Shivam jaiswal 24-Aug-2024
 */

@Service
public class LoginService {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsDAO userDetailsDAO;
	
	@Autowired
	private TokenDAO tokenDAO;

	@Autowired
	private JwtTokenUtility jwtUtil;

//	@Autowired
//	private TokenRepository tokenRepository;

	@Autowired
	private SecretKeyService keyService;

	public LoginResponse getLogin(LoginRequest request) {

		LoginResponse res = new LoginResponse();
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

			var user = userDetailsDAO.findByUserName(request.getUsername());
			var jwtToken = jwtUtil.GenerateToken(user.getUsername());
			var refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

			revokeAllUserTokens(user.getUserId());
			saveUserToken(user.getUserId(), jwtToken);

			res.setAccessToken(jwtToken);
			res.setRefreshToken(refreshToken);
			res.setStatusCode(200);
			
			res.setResponse(user);
			return res;
		} catch (Exception e) {
			res.setErrorMessage("Either username or password is wrong");
			res.setStatusCode(403);
			return res;
		}

	}

//	public LoginResponse registerNewUser(UserInfo userInfo) {
//
//		LoginResponse res = new LoginResponse();
//
//		try {
//			if (keyService.validSecretKey(userInfo)) {
//				userRepo.findByUsername(userInfo.getUsername());
//				if(userRepo.findByUsername(userInfo.getUsername()).isPresent()) {
//					res.setErrorMessage("Username already present");
//					res.setStatusCode(403);
//					return res;
//				}
//				if(userRepo.findByEmail(userInfo.getEmail()).isPresent()) {
//					res.setErrorMessage("Email already present");
//					res.setStatusCode(403);
//					return res;
//				}
//				UserInfo user = userRepo.save(userInfo);
//				var jwtToken = jwtUtil.GenerateToken(user.getUsername());
//				var refreshToken = jwtUtil.generateRefreshToken(user.getUsername());
//				saveUserToken(userInfo, jwtToken);
//				keyService.updateSecretKeyStatus(userInfo);
//				res.setAccessToken(jwtToken);
//				res.setRefreshToken(refreshToken);
//				res.setStatusCode(200);
//				res.setResponse(new UserDetails(user.getUserId(), user.getUsername(), user.getEmail(), user.getRoles(), user.getAccountStatus(), user.getStoreName(), user.getAddress()));
//				return res;
//			}else {
//				res.setErrorMessage("Your Secret Key is invalid");
//				res.setStatusCode(403);
//				return res;
//			}
//		} catch (Exception e) {
//			res.setErrorMessage("Your Secret Key is invalid");
//			res.setStatusCode(403);
//			return res;
//		}
//	}
//
//	
	
	public LoginResponse registerNewUser(UserDetailsDTO userDetails) {

		LoginResponse res = new LoginResponse();

		try {
			UserInfo userInfo = new UserInfo();
			 userInfo = userDetailsDAO.findByUserName(userDetails.getUsername());
			if(userInfo.getUsername() !=null && userInfo.getUsername()!="") {
				res.setErrorMessage("Username already present");
				res.setStatusCode(403);
				return res;
			}
			 userInfo = userDetailsDAO.findByEmail(userDetails.getUsername());

			if(userInfo.getEmail() !=null && userInfo.getEmail()!="") {
				res.setErrorMessage("Email already present");
				res.setStatusCode(403);
				return res;
			}
			if (keyService.validSecretKey(userDetails)) {
				
				int count = userDetailsDAO.registerUserDetails(userDetails);
				if(count>0) {
					var jwtToken = jwtUtil.GenerateToken(userDetails.getUsername());
					var refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());
					saveUserToken(userDetails.getUserId(), jwtToken);
					keyService.updateSecretKeyStatus(userDetails);
					res.setAccessToken(jwtToken);
					res.setRefreshToken(refreshToken);
					res.setStatusCode(200);
					userInfo = userDetailsDAO.findByUserName(userDetails.getUsername());
					res.setResponse(userInfo);
					return res;
				} else {
					res.setErrorMessage("Some error occured");
					res.setStatusCode(403);
					return res;
				}

			}else {
				res.setErrorMessage("Your Secret Key is invalid");
				res.setStatusCode(403);
				return res;
			}
		} catch (Exception e) {
			res.setErrorMessage("Some error occured");
			res.setStatusCode(403);
			return res;
		}
	}

	
	
	private void saveUserToken(long userId, String jwtToken) {
		Token token = new Token();

		token.setToken(jwtToken);
		token.setTokenType("BEARER");
		token.setExpired(false);
		token.setRevoked(false);
		token.setUserId(userId);
		tokenDAO.insertTokenDetails(token);

	}

	private void revokeAllUserTokens(long userId) {
		List<Token> validUserTokens = tokenDAO.findByUserId(userId);
		if (validUserTokens.isEmpty())
			return;
		tokenDAO.revokeAllUserTokens(userId);
	}

}
