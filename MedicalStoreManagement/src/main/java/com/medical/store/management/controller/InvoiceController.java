package com.medical.store.management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medical.store.management.services.BillingServices;

@RestController
public class InvoiceController {
	
//    private final BillingServices billingService;
//
//    public InvoiceController(BillingServices billingService) {
//        this.billingService = billingService;
//    }
//
//    @PostMapping
//    public ResponseEntity<Invoice> createInvoice(@RequestBody InvoiceRequest request) {
//        Invoice invoice = billingService.createInvoice(request);
//        return ResponseEntity.ok(invoice);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Invoice> getInvoice(@PathVariable Long id) {
//        return ResponseEntity.of(billingService.getInvoice(id));
//    }
	
//	@GetMapping("/{id}/pdf")
//	public ResponseEntity<byte[]> downloadInvoicePdf(@PathVariable Long id) {
//	    Invoice invoice = billingService.getInvoice(id)
//	            .orElseThrow(() -> new RuntimeException("Invoice not found"));
//
//	    ByteArrayInputStream bis = InvoicePdfGenerator.generateInvoicePdf(invoice);
//
//	    HttpHeaders headers = new HttpHeaders();
//	    headers.add("Content-Disposition", "inline; filename=invoice_" + id + ".pdf");
//
//	    return ResponseEntity
//	            .ok()
//	            .headers(headers)
//	            .contentType(MediaType.APPLICATION_PDF)
//	            .body(bis.readAllBytes());
//	}

}
