/**
 * 
 */
package com.medical.store.management.utility;

/**
 * @author Shivam jaiswal
 * 14-Sept-2025
 */
public class InvoicePdfGenerator {
	
//    public static ByteArrayInputStream generateInvoicePdf(Invoice invoice) {
//        Document document = new Document();
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//        try {
//            PdfWriter.getInstance(document, out);
//            document.open();
//
//            // Title
//            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
//            Paragraph title = new Paragraph("Pharmacy Invoice", titleFont);
//            title.setAlignment(Element.ALIGN_CENTER);
//            document.add(title);
//            document.add(Chunk.NEWLINE);
//
//            // Customer details
//            document.add(new Paragraph("Invoice ID: " + invoice.getInvoiceId()));
//            document.add(new Paragraph("Customer Name: " + invoice.getCustomerName()));
//            document.add(new Paragraph("Date: " + invoice.getCreatedAt()));
//            document.add(Chunk.NEWLINE);
//
//            // Table
//            PdfPTable table = new PdfPTable(4);
//            table.setWidthPercentage(100);
//            table.setWidths(new int[]{4, 2, 2, 2});
//
//            // Table Header
//            Stream.of("Product", "Quantity", "Price", "Total")
//                    .forEach(headerTitle -> {
//                        PdfPCell header = new PdfPCell();
//                        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
//                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
//                        header.setHorizontalAlignment(Element.ALIGN_CENTER);
//                        header.setPhrase(new Phrase(headerTitle, headFont));
//                        table.addCell(header);
//                    });
//
//            // Table Rows
//            for (InvoiceItem item : invoice.getItems()) {
//                table.addCell(item.getProduct().getName());
//                table.addCell(String.valueOf(item.getQuantity()));
//                table.addCell(String.valueOf(item.getProduct().getPrice()));
//                table.addCell(String.valueOf(item.getPrice()));
//            }
//
//            document.add(table);
//            document.add(Chunk.NEWLINE);
//
//            // Total Amount
//            Font bold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
//            Paragraph total = new Paragraph("Total: " + invoice.getTotalAmount(), bold);
//            total.setAlignment(Element.ALIGN_RIGHT);
//            document.add(total);
//
//            document.close();
//        } catch (DocumentException ex) {
//            ex.printStackTrace();
//        }
//
//        return new ByteArrayInputStream(out.toByteArray());
//    }

}
