package com.medical.store.management.queryconstants;

public class TokenQueryConstants {
	
	public final static StringBuilder FIND_BY_TOKEN = new StringBuilder("select * from medical_store.jwt_token where token = ?");
	public final static StringBuilder FIND_TOKEN_BY_USERID = new StringBuilder("select * from medical_store.jwt_token where user_id = ?");
	public static final StringBuilder INSERT_TOKEN_DETAILS = new StringBuilder("INSERT INTO medical_store.jwt_token( token_id, token, token_type, revoked, expired, user_id) VALUES (nextval('medical_store.token_seq'), ?, ?, ?, ?, ?)");
	public static final StringBuilder UPDATE_TOKEN_DETAILS = new StringBuilder("UPDATE medical_store.jwt_token SET revoked=true, expired=true WHERE user_id=?;");


}
