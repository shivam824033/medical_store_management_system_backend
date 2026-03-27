package com.medical.store.management.secretkey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.medical.store.management.queryconstants.SecretKeyQueryConstants;

@Repository
public class SecretKeyDAOImpl implements SecretKeyDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public SecretKey findBySecretKey(String secretKey) {
		try {
			SecretKey result = jdbcTemplate.queryForObject(SecretKeyQueryConstants.FIND_BY_SECRET_KEY.toString(),
					BeanPropertyRowMapper.newInstance(SecretKey.class), secretKey);
			return result;
		} catch (IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}

	@Override
	public int insertSecretKeyDetails(SecretKey secretKey) {
		try {
			int count = jdbcTemplate.update(SecretKeyQueryConstants.INSERT_SECRET_KEY_DETAILS.toString(),
					new Object[] { secretKey.getSecretKey(), secretKey.getCreatedDate(), secretKey.getUpdatedDate(),
							secretKey.getCreatedBy(), secretKey.getUpdatedBy(), secretKey.getUserId(),
							secretKey.getUsername(), secretKey.getKeyStatus() });
			return count;
		} catch (Exception e) {
			return 0;
		}

		
	}

	@Override
	public int updateSecretKeyDetails(SecretKey secretKey) {
		try {
			int count = jdbcTemplate.update(SecretKeyQueryConstants.UPDATE_SECRET_KEY_DETAILS.toString(),
					new Object[] { secretKey.getUpdatedDate(), secretKey.getUpdatedBy(), secretKey.getUserId(),
							secretKey.getUsername(), secretKey.getKeyStatus(), secretKey.getSecretId() });
			return count;
		} catch (Exception e) {

		}

		return 0;
	}

}
