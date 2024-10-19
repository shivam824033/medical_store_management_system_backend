/**
 * 
 */
package com.medical.store.management.secretkey;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medical.store.management.model.UserDetailsDTO;
import com.medical.store.management.model.UserInfo;

/**
 * @author Shivam jaiswal 25-Aug-2024
 */

@Service
public class SecretKeyService {

//	@Autowired
//	private SecretKeyRepository secretKeyRepo;
	
	@Autowired
	private SecretKeyDAO secretKeyDAO;

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

			secretKeyDAO.insertSecretKeyDetails(secretkeyObj);

			res.setSecretKey(key);
			res.setStatusCode(200);
			return res;
		} catch (Exception e) {

			res.setErrorMessage("Error occurred while generating Secret Key");
			res.setStatusCode(403);
			return res;
		}

	}

	public void updateSecretKeyStatus(UserDetailsDTO user) {

		if(user.getRoles().equalsIgnoreCase("PUBLIC")) {
			return;
		}
		
		SecretKey keyObj = secretKeyDAO.findBySecretKey(user.getSecretKey());
		if(keyObj.getSecretId()!=0) {
			keyObj.setUserId(user.getUserId());
			keyObj.setUsername(user.getUsername());
			keyObj.setKeyStatus("USED");
			keyObj.setUpdatedDate(new Date());
			keyObj.setUpdatedBy(user.getUserId());
			secretKeyDAO.updateSecretKeyDetails(keyObj);
		}


	}

	public boolean validSecretKey(UserDetailsDTO user) {

		if(user.getRoles().equalsIgnoreCase("PUBLIC")) {
			return true;
		}
		
		SecretKey keyObj = secretKeyDAO.findBySecretKey(user.getSecretKey());

		if (keyObj!=null) {  
			if (keyObj.getKeyStatus().equalsIgnoreCase("NEW")) {
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
