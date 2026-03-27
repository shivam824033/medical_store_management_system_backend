package com.medical.store.management.token;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.medical.store.management.queryconstants.TokenQueryConstants;

@Repository
public class TokenDAOImpl implements TokenDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Token findByToken(String token) {
		try {
			Token result = jdbcTemplate.queryForObject(TokenQueryConstants.FIND_BY_TOKEN.toString(),
					BeanPropertyRowMapper.newInstance(Token.class), token);
			return result;
		} catch (IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}

	@Override
	public List<Token> findByUserId(long userId) {
		try {
			List<Token> result = jdbcTemplate.query(TokenQueryConstants.FIND_TOKEN_BY_USERID.toString(),
					BeanPropertyRowMapper.newInstance(Token.class), userId);
			return result;
		} catch (IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}

	@Override
	public int insertTokenDetails(Token token) {

		try {
			int count = jdbcTemplate.update(TokenQueryConstants.INSERT_TOKEN_DETAILS.toString(), new Object[] {
					token.getToken(), token.getTokenType(),  token.isRevoked(),token.isExpired(), token.getUserId() });
			return count;
		} catch (Exception e) {
			
		}

		return 0;
	}

	@Override
	public int revokeAllUserTokens(long userId) {
		try {
			int count = jdbcTemplate.update(TokenQueryConstants.UPDATE_TOKEN_DETAILS.toString(), new Object[] {userId });
			return count;
		} catch (Exception e) {
			
		}

		return 0;
	}
	
	

}
