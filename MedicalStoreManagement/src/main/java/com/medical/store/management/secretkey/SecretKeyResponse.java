/**
 * 
 */
package com.medical.store.management.secretkey;

/**
 * @author Shivam jaiswal
 * 25-Aug-2024
 */

public class SecretKeyResponse {
	
	private String secretKey;
	
	private String errorMessage;
	
	private int statusCode;

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

}
