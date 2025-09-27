package com.medical.store.management.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BillingDAOImpl implements BillingDAO {
	
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Long insertInvoiceMaster(String invoiceNumber, String customerName, String customerMobile, Long storeId, Long userId, double totalAmount) {
        jdbcTemplate.update(
            "INSERT INTO medical_store.invoice_details (invoice_number, customer_name, customer_mobile, store_id, user_id, total_amount) VALUES (?, ?, ?, ?, ?, ?)",
            invoiceNumber, customerName, customerMobile, storeId, userId, totalAmount
        );
        return jdbcTemplate.queryForObject("SELECT invoice_id FROM medical_store.invoice_details WHERE invoice_number = ?", Long.class, invoiceNumber);
    }

    @Override
    public void insertInvoiceProduct(Long invoiceId, String batchNumber, String productName, int quantity, double price, double discount, double subtotal) {
        jdbcTemplate.update(
            "INSERT INTO medical_store.invoice_product_details (invoice_id, batch_number, product_name, quantity, price, discount, subtotal) VALUES (?, ?, ?, ?, ?, ?, ?)",
            invoiceId, batchNumber, productName, quantity, price, discount, subtotal
        );
    }

    @Override
    public Map<String, Object> getInvoiceMaster(Long invoiceId) {
        return jdbcTemplate.queryForMap("SELECT * FROM medical_store.invoice_details WHERE invoice_id = ?", invoiceId);
    }

    @Override
    public List<Map<String, Object>> getInvoiceProducts(Long invoiceId) {
        return jdbcTemplate.queryForList("SELECT * FROM medical_store.invoice_product_details WHERE invoice_id = ?", invoiceId);
    }

}
