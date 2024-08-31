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
		user.setPassword(passwordEncoder.encode("Pass@1234"));
		user.setAccountStatus("Active");
		user.setSecretKey(keyService.generateSecretKey(user).getSecretKey());
		
		LoginResponse res = loginService.registerNewUser(user);	

		
	}

}
