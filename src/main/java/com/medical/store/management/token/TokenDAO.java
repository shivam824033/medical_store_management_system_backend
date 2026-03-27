package com.medical.store.management.token;

import java.util.List;
import java.util.Optional;

public interface TokenDAO {

	  public Token findByToken(String token);
	  public List<Token> findByUserId(long userId);  
	  public int insertTokenDetails(Token token);
	  public int revokeAllUserTokens(long userId);
	  
}
