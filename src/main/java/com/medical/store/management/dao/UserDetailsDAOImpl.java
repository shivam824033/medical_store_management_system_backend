package com.medical.store.management.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.medical.store.management.exception.handler.UnauthorizedException;
import com.medical.store.management.model.UserDetailsDTO;
import com.medical.store.management.model.UserInfo;
import com.medical.store.management.queryconstants.UserDetailsQueryConstants;
import com.medical.store.management.utility.BasicUtil;

@Repository
public class UserDetailsDAOImpl implements UserDetailsDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public UserDetailsDTO loadByUserName(String username) {
		UserDetailsDTO user = new UserDetailsDTO();
		List<Map<String, Object>> results = jdbcTemplate
				.queryForList(UserDetailsQueryConstants.LOAD_USER_BY_USERNAME.toString(), new Object[] { username });

		if (!results.isEmpty()) {
			for (Map<String, Object> result : results) {
				user.setUserId(BasicUtil.getResultInteger(result.get("user_id")));
				user.setUsername(BasicUtil.getResultString(result.get("username")));
				user.setPassword(BasicUtil.getResultString(result.get("password")));
				user.setRoles(BasicUtil.getResultString(result.get("roles")));
			}
		}
		return user;
	}

	@Override
	public boolean isAcountActive(String username) {
		try {
			String status = jdbcTemplate.queryForObject(
					"select account_status from medical_store.user_details where username = ?", String.class,
					new Object[] { username });
			if ("ACTIVE".equals(status)) {
				return true;
			}
		} catch (Exception e) {
			throw new UnauthorizedException("You are not Authorized;");
		}
		return false;
	}

	@Override
	public UserInfo findByUserName(String username) {
		UserInfo user = new UserInfo();
		List<Map<String, Object>> results = jdbcTemplate.queryForList(
				UserDetailsQueryConstants.FIND_USERDETAILS_BY_USERNAME.toString(), new Object[] { username });

		if (!results.isEmpty()) {
			for (Map<String, Object> result : results) {
				user.setUserId(BasicUtil.getResultInteger(result.get("user_id")));
				user.setUsername(BasicUtil.getResultString(result.get("username")));
				user.setEmail(BasicUtil.getResultString(result.get("email")));
				user.setFirstName(BasicUtil.getResultString(result.get("firstname")));
				user.setLastName(BasicUtil.getResultString(result.get("lastname")));
				user.setFullName(BasicUtil.getResultString(result.get("fullname")));
				user.setGender(BasicUtil.getResultString(result.get("gender")));
				user.setRoles(BasicUtil.getResultString(result.get("roles")));
				user.setStoreId(BasicUtil.getResultLong(result.get("store_id")));
				user.setStoreName(BasicUtil.getResultString(result.get("store_name")));
				user.setAddress1(BasicUtil.getResultString(result.get("address_line1")));
				user.setAddress2(BasicUtil.getResultString(result.get("address_line2")));
				user.setPincode(BasicUtil.getResultInteger(result.get("pincode")));
				user.setDistrict(BasicUtil.getResultString(result.get("district")));
				user.setState(BasicUtil.getResultString(result.get("state")));
				user.setCountry(BasicUtil.getResultString(result.get("country")));
				user.setAccountStatus(BasicUtil.getResultString(result.get("account_status")));
			}

		}

		return user;
	}

	@Override
	public UserInfo findByEmail(String email) {
		UserInfo user = new UserInfo();
		List<Map<String, Object>> results = jdbcTemplate
				.queryForList(UserDetailsQueryConstants.FIND_USERDETAILS_BY_EMAIL.toString(), new Object[] { email });

		if (!results.isEmpty()) {
			for (Map<String, Object> result : results) {
				user.setUserId(BasicUtil.getResultInteger(result.get("user_id")));
				user.setUsername(BasicUtil.getResultString(result.get("username")));
				user.setEmail(BasicUtil.getResultString(result.get("email")));
				user.setFirstName(BasicUtil.getResultString(result.get("firstname")));
				user.setLastName(BasicUtil.getResultString(result.get("lastname")));
				user.setFullName(BasicUtil.getResultString(result.get("fullname")));
				user.setGender(BasicUtil.getResultString(result.get("gender")));
				user.setRoles(BasicUtil.getResultString(result.get("roles")));
				user.setStoreId(BasicUtil.getResultLong(result.get("store_id")));
				user.setStoreName(BasicUtil.getResultString(result.get("store_name")));
				user.setAddress1(BasicUtil.getResultString(result.get("address_line1")));
				user.setAddress2(BasicUtil.getResultString(result.get("address_line2")));
				user.setPincode(BasicUtil.getResultInteger(result.get("pincode")));
				user.setDistrict(BasicUtil.getResultString(result.get("district")));
				user.setState(BasicUtil.getResultString(result.get("state")));
				user.setCountry(BasicUtil.getResultString(result.get("country")));
				user.setAccountStatus(BasicUtil.getResultString(result.get("account_status")));

			}
		}

		return user;
	}

	@Override
	public int registerUserDetails(UserDetailsDTO userDeatils) {

		if (userDeatils.getRoles().equalsIgnoreCase("SELLER")) {
			long storeId = jdbcTemplate.queryForObject("select nextval('medical_store.store_id_seq')", Long.class);
			userDeatils.setStoreId(storeId);
		}

		int count = jdbcTemplate.update(UserDetailsQueryConstants.INSERT_USERDETAILS.toString(),
				new Object[] { userDeatils.getUsername(), userDeatils.getEmail(), userDeatils.getFirstName(),
						userDeatils.getLastName(), userDeatils.getFullName(), userDeatils.getGender(),
						userDeatils.getRoles(), userDeatils.getPassword(), userDeatils.getStoreId(),
						userDeatils.getStoreName(), userDeatils.getAddress1(), userDeatils.getAddress2(),
						userDeatils.getPincode(), userDeatils.getDistrict(), userDeatils.getState(),
						userDeatils.getCountry(), userDeatils.getSecretKey(), userDeatils.getAccountStatus() });

		return count;
	}

	@Override
	public List<UserInfo> getAllUsersByRole(String role) {
		List<UserInfo> userList = new ArrayList<>();

		List<Map<String, Object>> results = new ArrayList<>();
		if (role == null || role.isEmpty()) {
			results = jdbcTemplate.queryForList(UserDetailsQueryConstants.FIND_ALL_USERDETAILS.toString());
		} else {
			results = jdbcTemplate.queryForList(UserDetailsQueryConstants.FIND_USERDETAILS_BY_ROLE.toString(),
					new Object[] { role });
		}

		if (!results.isEmpty()) {
			for (Map<String, Object> result : results) {
				UserInfo user = new UserInfo();
				user.setUserId(BasicUtil.getResultInteger(result.get("user_id")));
				user.setUsername(BasicUtil.getResultString(result.get("username")));
				user.setEmail(BasicUtil.getResultString(result.get("email")));
				user.setFirstName(BasicUtil.getResultString(result.get("firstname")));
				user.setLastName(BasicUtil.getResultString(result.get("lastname")));
				user.setFullName(BasicUtil.getResultString(result.get("fullname")));
				user.setGender(BasicUtil.getResultString(result.get("gender")));
				user.setRoles(BasicUtil.getResultString(result.get("roles")));
				user.setStoreId(BasicUtil.getResultLong(result.get("store_id")));
				user.setStoreName(BasicUtil.getResultString(result.get("store_name")));
				user.setAddress1(BasicUtil.getResultString(result.get("address_line1")));
				user.setAddress2(BasicUtil.getResultString(result.get("address_line2")));
				user.setPincode(BasicUtil.getResultInteger(result.get("pincode")));
				user.setDistrict(BasicUtil.getResultString(result.get("district")));
				user.setState(BasicUtil.getResultString(result.get("state")));
				user.setCountry(BasicUtil.getResultString(result.get("country")));
				user.setAccountStatus(BasicUtil.getResultString(result.get("account_status")));

				userList.add(user);
			}

		}

		return userList;
	}

	@Override
	public boolean deactivateUser(Long userId, String status) {
		String accountStatus = null;
		if ("activate".equalsIgnoreCase(status)) {
			accountStatus = "ACTIVE";
		} else if ("deactivate".equalsIgnoreCase(status)) {
			accountStatus = "BLOCKED";
		} else {
			return false;
		}
		int updated = jdbcTemplate.update(UserDetailsQueryConstants.UPDATE_ACCOUNT_STATUS.toString(),
				new Object[] { accountStatus, userId });

		return updated > 0;
	}

}
