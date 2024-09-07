/**
 * 
 */
package com.medical.store.management.model;

/**
 * @author Shivam jaiswal 25-Aug-2024
 */

public class ResponseDTO {

	private Object response;
	
	private String errorMessage;
	
	private int statusCode;

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
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
