package com.medical.store.management.secretkey;

public interface SecretKeyDAO {
	
	public SecretKey findBySecretKey(String secretKey);
	
	public int insertSecretKeyDetails(SecretKey secretKey);
	
	public int updateSecretKeyDetails(SecretKey secretKey);

}
