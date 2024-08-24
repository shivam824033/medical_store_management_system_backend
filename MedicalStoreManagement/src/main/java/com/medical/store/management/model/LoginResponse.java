/**
 * 
 */
package com.medical.store.management.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Shivam jaiswal 24-Aug-2024
 */

public class LoginResponse {

	@JsonProperty("access_token")
	private String accessToken;
	@JsonProperty("refresh_token")
	private String refreshToken;
	
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	

}
