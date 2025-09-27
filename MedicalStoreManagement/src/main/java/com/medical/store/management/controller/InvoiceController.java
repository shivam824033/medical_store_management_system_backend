package com.medical.store.management.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medical.store.management.model.UserInfo;
import com.medical.store.management.security.config.JwtTokenUtility;
import com.medical.store.management.services.BillingServices;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {
	
    private final BillingServices billingService;
    
    @Autowired
    private JwtTokenUtility jwtService;

    public InvoiceController(BillingServices billingService) {
        this.billingService = billingService;
    }
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
    
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/createInvoice")
    public Object createInvoice(@RequestHeader("Authorization") String reqHeader, @RequestBody List<Map<String, Object>> billItems) {
        UserInfo user = jwtService.extractUserInfoFromJWT(reqHeader);
        // Call a service to process the bill (update stock, save bill, etc.)
        return billingService.createInvoice(billItems, user);
    }

}
