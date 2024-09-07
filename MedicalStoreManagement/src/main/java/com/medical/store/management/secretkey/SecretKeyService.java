/**
 * 
 */
package com.medical.store.management.secretkey;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medical.store.management.model.UserInfo;

/**
 * @author Shivam jaiswal 25-Aug-2024
 */

@Service
public class SecretKeyService {

	@Autowired
	private SecretKeyRepository secretKeyRepo;

	public SecretKeyResponse generateSecretKey(UserInfo user) {

		SecretKeyResponse res = new SecretKeyResponse();

		try {
			String key = generateUniqueKeyUsingUID();

			SecretKey secretkeyObj = new SecretKey();
			secretkeyObj.setSecretKey(key);
			secretkeyObj.setKeyStatus("NEW");
			secretkeyObj.setCreatedDate(new Date());
			secretkeyObj.setCreatedBy(user.getUserId());
			secretkeyObj.setUpdatedDate(null);
			secretkeyObj.setUpdatedBy(0);

			secretKeyRepo.save(secretkeyObj);

			res.setSecretKey(key);
			res.setStatusCode(200);
			return res;
		} catch (Exception e) {

			res.setErrorMessage("Error occurred while generating Secret Key");
			res.setStatusCode(403);
			return res;
		}

	}

	public void updateSecretKeyStatus(UserInfo user) {

		if(user.getRoles()=="PUBLIC") {
			return;
		}
		
		Optional<SecretKey> keyObj = secretKeyRepo.findBySecretKey(user.getSecretKey());
		keyObj.get().setUserId(user.getUserId());
		keyObj.get().setUsername(user.getUsername());
		keyObj.get().setKeyStatus("USED");
		keyObj.get().setUpdatedDate(new Date());
		keyObj.get().setUpdatedBy(user.getUserId());
		secretKeyRepo.save(keyObj.get());

	}

	public boolean validSecretKey(UserInfo user) {

		if(user.getRoles()=="PUBLIC") {
			return true;
		}
		
		Optional<SecretKey> keyObj = secretKeyRepo.findBySecretKey(user.getSecretKey());

		if (!keyObj.isEmpty()) {
			if (keyObj.get().getKeyStatus().equalsIgnoreCase("NEW")) {
				return true;
			}
		}

		return false;
	}

	public String generateUniqueKeyUsingUID() {
		// UID that is unique over time with respect to the host that it was generated
		// on
//        UID secretUID = new UID();
		UUID secretUID = UUID.randomUUID();
		System.out.print("secret id = " + secretUID);
		return secretUID.toString();
	}

}
