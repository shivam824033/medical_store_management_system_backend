package com.medical.store.management.dao;

import java.util.List;
import java.util.Map;

public interface BillingDAO {
	
    Long insertInvoiceMaster(String invoiceNumber, String customerName, String customerMobile, Long storeId, Long userId, double totalAmount);
    void insertInvoiceProduct(Long invoiceId, String batchNumber, String productName, int quantity, double price, double discount, double subtotal);
    Map<String, Object> getInvoiceMaster(Long invoiceId);
    List<Map<String, Object>> getInvoiceProducts(Long invoiceId);

}
