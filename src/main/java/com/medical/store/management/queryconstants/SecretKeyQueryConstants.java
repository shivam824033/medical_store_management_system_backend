package com.medical.store.management.queryconstants;

public class SecretKeyQueryConstants {

	public static final StringBuilder FIND_BY_SECRET_KEY = new StringBuilder("select * from medical_store.secret_key_details where secret_key = ?");
	public static final StringBuilder INSERT_SECRET_KEY_DETAILS = new StringBuilder("INSERT INTO medical_store.secret_key_details( secret_id, secret_key, created_date, updated_date, created_by, updated_by, user_id, username, key_status) VALUES (nextval('medical_store.secret_key_seq'), ?, ?, ?, ?, ?, ?, ?, ?)");
	public static final StringBuilder UPDATE_SECRET_KEY_DETAILS = new StringBuilder("UPDATE medical_store.secret_key_details SET  updated_date=?, updated_by=?, user_id=?, username=?, key_status=? WHERE secret_id = ?;");

}
