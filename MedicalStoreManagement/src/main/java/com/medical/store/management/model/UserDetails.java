/**
 * 
 */
package com.medical.store.management.model;

/**
 * @author Shivam jaiswal
 * 25-Aug-2024
 */
public class UserDetails {
	
    private long userId;
    
    private String username;
    
    private String email;
    
    private String roles;
    
    private String accountStatus;
    
    private String storeName;
    
    private String address;
    
	public UserDetails() {}

	
	public UserDetails(long userId, String username, String email, String roles, String accountStatus, String storeName,
			String address) {
		super();
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.roles = roles;
		this.accountStatus = accountStatus;
		this.storeName = storeName;
		this.address = address;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	  
}
