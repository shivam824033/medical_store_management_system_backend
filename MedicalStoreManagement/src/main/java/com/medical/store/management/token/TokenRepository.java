/**
 * 
 */
package com.medical.store.management.token;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Shivam jaiswal
 * 24-Aug-2024
 */

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer>{
	
	  Optional<Token> findByToken(String token);
	  List<Token> findByUserId(long userId);

}
