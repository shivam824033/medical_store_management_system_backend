/**
 * 
 */
package com.medical.store.management.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.medical.store.management.model.LoginRequest;
import com.medical.store.management.model.LoginResponse;
import com.medical.store.management.model.UserDetails;
import com.medical.store.management.model.UserInfo;
import com.medical.store.management.repository.UserInfoRepo;
import com.medical.store.management.secretkey.SecretKeyService;
import com.medical.store.management.security.config.JwtTokenUtility;
import com.medical.store.management.token.Token;
import com.medical.store.management.token.TokenRepository;
import com.medical.store.management.token.TokenType;

/**
 * @author Shivam jaiswal 24-Aug-2024
 */

@Service
public class LoginService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserInfoRepo userRepo;

	@Autowired
	private JwtTokenUtility jwtUtil;

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private SecretKeyService keyService;

	public LoginResponse getLogin(LoginRequest request) {

		LoginResponse res = new LoginResponse();
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

			var user = userRepo.findByUsername(request.getUsername()).get();
			var jwtToken = jwtUtil.GenerateToken(user.getUsername());
			var refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

			revokeAllUserTokens(user);
			saveUserToken(user, jwtToken);

			res.setAccessToken(jwtToken);
			res.setRefreshToken(refreshToken);
			res.setStatusCode(200);
			
			res.setResponse(new UserDetails(user.getUserId(), user.getUsername(), user.getEmail(), user.getRoles(), user.getAccountStatus(), user.getStoreName(), user.getAddress()));
			return res;
		} catch (Exception e) {
			res.setErrorMessage("Either username or password is wrong");
			res.setStatusCode(403);
			return res;
		}

	}

	public LoginResponse registerNewUser(UserInfo userInfo) {

		LoginResponse res = new LoginResponse();

		try {
			if (keyService.validSecretKey(userInfo)) {
				userRepo.findByUsername(userInfo.getUsername());
				if(userRepo.findByUsername(userInfo.getUsername()).isPresent()) {
					res.setErrorMessage("Username already present");
					res.setStatusCode(403);
					return res;
				}
				if(userRepo.findByEmail(userInfo.getEmail()).isPresent()) {
					res.setErrorMessage("Email already present");
					res.setStatusCode(403);
					return res;
				}
				UserInfo user = userRepo.save(userInfo);
				var jwtToken = jwtUtil.GenerateToken(user.getUsername());
				var refreshToken = jwtUtil.generateRefreshToken(user.getUsername());
				saveUserToken(userInfo, jwtToken);
				keyService.updateSecretKeyStatus(userInfo);
				res.setAccessToken(jwtToken);
				res.setRefreshToken(refreshToken);
				res.setStatusCode(200);
				res.setResponse(new UserDetails(user.getUserId(), user.getUsername(), user.getEmail(), user.getRoles(), user.getAccountStatus(), user.getStoreName(), user.getAddress()));
				return res;
			}else {
				res.setErrorMessage("Your Secret Key is invalid");
				res.setStatusCode(403);
				return res;
			}
		} catch (Exception e) {
			res.setErrorMessage("Your Secret Key is invalid");
			res.setStatusCode(403);
			return res;
		}
	}

	private void saveUserToken(UserInfo user, String jwtToken) {
		Token token = new Token();

		token.setToken(jwtToken);
		token.setTokenType(TokenType.BEARER);
		token.setExpired(false);
		token.setRevoked(false);
		token.setUserId(user.getUserId());

		tokenRepository.save(token);

	}

	private void revokeAllUserTokens(UserInfo user) {
		List<Token> validUserTokens = tokenRepository.findByUserId(user.getUserId());
		if (validUserTokens.isEmpty())
			return;
		validUserTokens.forEach(token -> {
			token.setExpired(true);
			token.setRevoked(true);
		});
		tokenRepository.saveAll(validUserTokens);
	}

}
