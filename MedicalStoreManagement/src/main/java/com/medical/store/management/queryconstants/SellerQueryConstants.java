package com.medical.store.management.queryconstants;

public class SellerQueryConstants {
	
	public final static StringBuilder INSERT_PRODUCT_DETAILS = new StringBuilder("INSERT INTO medical_store.pharmacy_product_details( product_id, batch_number, product_name, product_descrption, category_id, product_image, pre_product_price, total_price, strip_count, product_per_strip, total_product_quantity, expiry_date, created_date, updated_date, username, user_id, store_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

	public final static StringBuilder FIND_SELLER_PRODUCT_DETAILS = new StringBuilder("SELECT product_id, batch_number, product_name, product_descrption, category_id, product_image, pre_product_price, total_price, strip_count, product_per_strip, total_product_quantity, expiry_date, created_date, updated_date, username, user_id, store_id FROM medical_store.pharmacy_product_details  where store_id = ? and LOWER(product_name) like LOWER(?) or LOWER(product_descrption) like LOWER(?)");

	public static final StringBuilder GET_MASTER_PRODUCT_DETAILS = new StringBuilder("SELECT product_id, product_name, product_description, product_image, category_id FROM medical_store.master_product where LOWER(product_name) like LOWER(?) or LOWER(product_description) like LOWER(?)");
	
	public static final StringBuilder IS_BATCH_NUMBER_PRESENT = new StringBuilder("SELECT count(*) from medical_store.pharmacy_product_details where batch_number=? and user_id=?");


}
