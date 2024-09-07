package com.medical.store.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.medical.store.management.model.LoginResponse;
import com.medical.store.management.model.UserDetails;
import com.medical.store.management.model.UserInfo;
import com.medical.store.management.secretkey.SecretKeyService;
import com.medical.store.management.services.LoginService;

@SpringBootApplication
public class MedicalStoreManagementApplication implements CommandLineRunner {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private SecretKeyService keyService;

	public static void main(String[] args) {
		SpringApplication.run(MedicalStoreManagementApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		UserInfo user = new UserInfo();
		
		user.setUsername("Shivam");
		user.setRoles("OWNER");
		user.setEmail("shivam@gmail.com");
		user.setPassword(passwordEncoder.encode("Shivam@123"));
		user.setAccountStatus("Active");
		user.setSecretKey(keyService.generateSecretKey(user).getSecretKey());
		
		LoginResponse userRes = loginService.registerNewUser(user);	
		
		UserInfo seller = new UserInfo();
		
		seller.setUsername("Shubham");
		seller.setRoles("SELLER");
		seller.setEmail("shubham@gmail.com");
		seller.setPassword(passwordEncoder.encode("Shubham@1234"));
		seller.setAccountStatus("Active");
		seller.setSecretKey(keyService.generateSecretKey(seller).getSecretKey());
		
		LoginResponse sellerRes = loginService.registerNewUser(seller);	
		
	}

}
