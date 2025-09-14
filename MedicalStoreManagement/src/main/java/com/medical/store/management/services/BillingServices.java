/**
 * 
 */
package com.medical.store.management.services;

/**
 * @author Shivam jaiswal
 * 14-Sept-2025
 */
public class BillingServices {

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
