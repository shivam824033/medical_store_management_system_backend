/**
 * 
 */
package com.medical.store.management.secretkey;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Shivam jaiswal
 * 25-Aug-2024
 */

@Repository
public interface SecretKeyRepository extends JpaRepository<SecretKey, Long>{
	
	 Optional<SecretKey> findBySecretKey(String key);

}
