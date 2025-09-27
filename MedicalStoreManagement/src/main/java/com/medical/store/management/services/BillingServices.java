/**
 * 
 */
package com.medical.store.management.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medical.store.management.dao.BillingDAO;
import com.medical.store.management.dao.SellerDAO;
import com.medical.store.management.model.ResponseDTO;
import com.medical.store.management.model.UserInfo;

/**
 * @author Shivam jaiswal
 * 14-Sept-2025
 */

@Service
public class BillingServices {
	
	@Autowired
	private SellerDAO sellerDAO;
	
	@Autowired
	private BillingDAO billingDAO;

	@Transactional
	public Object createInvoice(List<Map<String, Object>> billItems, UserInfo user) {
		
		 ResponseDTO res = new ResponseDTO();
		    try {
		        // 1. Validate and update stock (existing logic)
		        Long storeId = null;
		        double totalAmount = 0;
		        for (Map<String, Object> item : billItems) {
		            String batchNumber = (String) item.get("batchNumber");
		            Integer quantity = (Integer) item.get("quantity");
		            double price = ((Number) item.get("productPerPrice")).doubleValue();
		            double discount = item.get("discount") != null ? ((Number) item.get("discount")).doubleValue() : 0.0;
		            double subtotal = (quantity * price) - ((quantity * price * discount) / 100.0);
		            
		            totalAmount += subtotal;

		            long itemStoreId = ((Number) item.get("storeId")).longValue();
		            long itemUserId = ((Number) item.get("userId")).longValue();

		            if (storeId == null) storeId = itemStoreId;
		            if (storeId != itemStoreId || user.getUserId() != itemUserId) {
		                throw new RuntimeException("All products must belong to the same store and user.");
		            }
		            int available = sellerDAO.getProductStock(batchNumber, itemUserId);
		            if (available < quantity) {
		                throw new RuntimeException("Insufficient stock for batch " + batchNumber + ". Available: " + available);
		            }
		        }
		        for (Map<String, Object> item : billItems) {
		            String batchNumber = (String) item.get("batchNumber");
		            Integer quantity = (Integer) item.get("quantity");
		            sellerDAO.reduceProductStock(batchNumber, user.getUserId(), quantity, user.getStoreId());
		        }

		        // 2. Generate invoice number (simple timestamp-based, you can improve)
		        String invoiceNumber = "INV-" + System.currentTimeMillis();

		        // 3. Insert into invoice_details
		        String customerName = billItems.get(0).get("customerName") != null ? billItems.get(0).get("customerName").toString() : "";
		        String customerMobile = billItems.get(0).get("customerMobile") != null ? billItems.get(0).get("customerMobile").toString() : "";
		        Long invoiceId = billingDAO.insertInvoiceMaster(invoiceNumber, customerName, customerMobile, user.getStoreId(), user.getUserId(), totalAmount);


		        // 4. Insert each product into invoice_product_details
		        for (Map<String, Object> item : billItems) {
		            String batchNumber = (String) item.get("batchNumber");
		            String productName = (String) item.get("productName");
		            Integer quantity = (Integer) item.get("quantity");
		            double price = ((Number) item.get("productPerPrice")).doubleValue();
		            double discount = item.get("discount") != null ? ((Number) item.get("discount")).doubleValue() : 0.0;
		            double subtotal = (quantity * price) - ((quantity * price * discount) / 100.0);

		            billingDAO.insertInvoiceProduct(invoiceId, batchNumber, productName, quantity, price, discount, subtotal);

		        }

		        // 5. Fetch invoice, customer, and product details for response
		        Map<String, Object> invoiceMaster = billingDAO.getInvoiceMaster(invoiceId);
		        List<Map<String, Object>> invoiceProducts = billingDAO.getInvoiceProducts(invoiceId);
		        res.setResponse(Map.of(
		            "customer", Map.of("name", customerName, "mobile", customerMobile),
		            "invoice", invoiceMaster,
		            "products", invoiceProducts
		        ));
		        res.setStatusCode(200);
		    } catch (Exception e) {
		        res.setErrorMessage("Failed to finalize bill: " + e.getMessage());
		        res.setStatusCode(400);
		    }
		    return res;
	
	}

//    private final ProductRepository productRepository;
//    private final InvoiceRepository invoiceRepository;
//
//    public BillingService(ProductRepository productRepository, InvoiceRepository invoiceRepository) {
//        this.productRepository = productRepository;
//        this.invoiceRepository = invoiceRepository;
//    }
//
//    public Invoice createInvoice(InvoiceRequest request) {
//        Invoice invoice = new Invoice();
//        invoice.setCustomerName(request.getCustomerName());
//
//        double totalAmount = 0.0;
//        List<InvoiceItem> invoiceItems = new ArrayList<>();
//
//        for (InvoiceItemRequest itemRequest : request.getItems()) {
//            Product product = productRepository.findById(itemRequest.getProductId())
//                    .orElseThrow(() -> new RuntimeException("Product not found"));
//
//            if (product.getStockQuantity() < itemRequest.getQuantity()) {
//                throw new RuntimeException("Insufficient stock for product: " + product.getName());
//            }
//
//            // reduce stock
//            product.setStockQuantity(product.getStockQuantity() - itemRequest.getQuantity());
//            productRepository.save(product);
//
//            // create invoice item
//            InvoiceItem invoiceItem = new InvoiceItem();
//            invoiceItem.setInvoice(invoice);
//            invoiceItem.setProduct(product);
//            invoiceItem.setQuantity(itemRequest.getQuantity());
//            invoiceItem.setPrice(product.getPrice() * itemRequest.getQuantity());
//
//            invoiceItems.add(invoiceItem);
//            totalAmount += invoiceItem.getPrice();
//        }
//
//        invoice.setItems(invoiceItems);
//        invoice.setTotalAmount(totalAmount);
//
//        return invoiceRepository.save(invoice);
//    }
    
}
