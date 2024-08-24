/**
 * 
 */
package com.medical.store.management.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Shivam jaiswal
 *
 */


@Entity
@Table(name = "USERS")
public class UserInfo {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userId")
    private long userId;
    
    private String username;
    
    private String email;
    
    private String password;
    
    private String roles;
    
    private String secretKey;
    
    private String accountStatus;
    

	public UserInfo() {}

	public UserInfo(long userId, String username, String email, String password, String roles, String secretKey,
			String accountStatus) {
		super();
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.password = password;
		this.roles = roles;
		this.secretKey = secretKey;
		this.accountStatus = accountStatus;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String isActive) {
		this.accountStatus = isActive;
	}
    
}
