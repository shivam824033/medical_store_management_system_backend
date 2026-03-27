package com.medical.store.management.queryconstants;

public class UserDetailsQueryConstants {
	
	public final static StringBuilder LOAD_USER_BY_USERNAME = new StringBuilder("select user_id, username, email, firstname, lastname, fullname, gender, roles, password from medical_store.user_details where username= ?");

	public final static StringBuilder FIND_USERDETAILS_BY_USERNAME = new StringBuilder("select user_id, username, email, firstname, lastname, fullname, gender, roles, store_id, store_name, address_line1, address_line2, pincode, district, state, country, account_status from medical_store.user_details where username= ?");

	public final static StringBuilder FIND_USERDETAILS_BY_EMAIL = new StringBuilder("select user_id, username, email, firstname, lastname, fullname, gender, roles, store_id, store_name, address_line1, address_line2, pincode, district, state, country, account_status from medical_store.user_details where email= ?");

	public final static StringBuilder INSERT_USERDETAILS = new StringBuilder("INSERT INTO medical_store.user_details( user_id, username, email, firstname, lastname, fullname, gender, roles, password, store_id, store_name, address_line1, address_line2, pincode, district, state, country, secret_key, account_status, created_date) VALUES ( nextval('medical_store.user_details_user_id'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)");

	public final static StringBuilder FIND_ALL_USERDETAILS = new StringBuilder("select user_id, username, email, firstname, lastname, fullname, gender, roles, store_id, store_name, address_line1, address_line2, pincode, district, state, country, account_status from medical_store.user_details");

	public final static StringBuilder FIND_USERDETAILS_BY_ROLE = new StringBuilder("select user_id, username, email, firstname, lastname, fullname, gender, roles, store_id, store_name, address_line1, address_line2, pincode, district, state, country, account_status from medical_store.user_details where roles= ?");

	public final static StringBuilder UPDATE_ACCOUNT_STATUS = new StringBuilder("UPDATE medical_store.user_details SET account_status = ? WHERE user_id = ?");

}
