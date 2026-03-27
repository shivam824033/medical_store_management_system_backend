/**
 * 
 */
package com.medical.store.management.services;

/**
 * @author Shivam jaiswal
 * 14-Sept-2025
 */
public class EmailService {
	
	
//	spring.mail.host=smtp.gmail.com
//			spring.mail.port=587
//			spring.mail.username=your_email@gmail.com
//			spring.mail.password=your_app_password   # Use app password if Gmail
//			spring.mail.properties.mail.smtp.auth=true
//			spring.mail.properties.mail.smtp.starttls.enable=true
	
	

//    private final JavaMailSender mailSender;
//
//    public EmailService(JavaMailSender mailSender) {
//        this.mailSender = mailSender;
//    }
//
//    public void sendInvoiceEmail(String toEmail, Invoice invoice) throws MessagingException {
//        ByteArrayInputStream bis = InvoicePdfGenerator.generateInvoicePdf(invoice);
//
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//        helper.setFrom("your_email@gmail.com");
//        helper.setTo(toEmail);
//        helper.setSubject("Your Pharmacy Invoice - #" + invoice.getInvoiceId());
//        helper.setText("Dear " + invoice.getCustomerName() + ",\n\nPlease find attached your invoice.\n\nThank you!");
//
//        // Attach PDF
//        helper.addAttachment("invoice_" + invoice.getInvoiceId() + ".pdf",
//                new ByteArrayResource(bis.readAllBytes()));
//
//        mailSender.send(message);
//    }
	
//	@PostMapping("/{id}/send-email")
//	public ResponseEntity<String> sendInvoiceEmail(@PathVariable Long id, @RequestParam String email) {
//	    Invoice invoice = billingService.getInvoice(id)
//	            .orElseThrow(() -> new RuntimeException("Invoice not found"));
//
//	    try {
//	        emailService.sendInvoiceEmail(email, invoice);
//	        return ResponseEntity.ok("Invoice sent to " + email);
//	    } catch (Exception e) {
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//	                .body("Failed to send email: " + e.getMessage());
//	    }

}
