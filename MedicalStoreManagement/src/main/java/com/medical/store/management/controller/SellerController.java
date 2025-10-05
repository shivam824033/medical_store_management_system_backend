/**
 * 
 */
package com.medical.store.management.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.medical.store.management.model.PharmacyMasterProduct;
import com.medical.store.management.model.UserInfo;
import com.medical.store.management.security.config.JwtTokenUtility;
import com.medical.store.management.services.FileUploadService;
import com.medical.store.management.services.SellerService;

/**
 * @author Shivam jaiswal
 * 25-Aug-2024
 */

@RestController
@RequestMapping("/api/seller")
public class SellerController {
	
	@Autowired
	private SellerService sellerService;
	
    @Autowired
    private JwtTokenUtility jwtService;
    
    @Autowired
    private FileUploadService fileUploadService;
    
	
//    @CrossOrigin(origins = "http://localhost:4200")
//	@PostMapping("/getAllProduct")
//	public Object findAllSellerProduct(@RequestHeader("Authorization") String reqHeader) {
//	
//	    UserInfo user = jwtService.extractUserInfoFromJWT(reqHeader);
//		
//	   return sellerService.findAllSellerProductoduct(user);
//	   
//	}
    
	
    @CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/addProduct")
	public Object addProduct(@RequestHeader("Authorization") String reqHeader, @RequestBody PharmacyMasterProduct productsDetails) {
	
	    UserInfo user = jwtService.extractUserInfoFromJWT(reqHeader);
		
	   return sellerService.addProduct(productsDetails, user);
	   
	}
	
    @CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/sellProduct")
	public Object sellProduct(@RequestHeader("Authorization") String reqHeader, @RequestBody PharmacyMasterProduct productsDetails) {
	
	    UserInfo user = jwtService.extractUserInfoFromJWT(reqHeader);
		
	   return sellerService.sellProduct(productsDetails, user);
	   
	}
    
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/finalizeBill")
    public Object finalizeBill(@RequestHeader("Authorization") String reqHeader, @RequestBody List<Map<String, Object>> billItems) {
        UserInfo user = jwtService.extractUserInfoFromJWT(reqHeader);
        // Call a service to process the bill (update stock, save bill, etc.)
        return sellerService.finalizeBill(billItems, user);
    }
    
    @PostMapping("/csv")
    public String uploadCsv(@RequestParam("file") MultipartFile file) {
        try {
        	fileUploadService.processCsv(file);
            return "CSV file processed successfully!";
        } catch (Exception e) {
            return "Error processing CSV: " + e.getMessage();
        }
    }

    @PostMapping("/excel")
    public ResponseEntity<byte[]> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
        	List<String> list = fileUploadService.processExcel(file);
        	return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"batches.pdf\"")
            .contentType(MediaType.APPLICATION_PDF)
            .body(fileUploadService.createPdfForIds(list, 3, 8));
        } catch (Exception e) {
        	return ResponseEntity.status(500).body(null);        }
    }
    
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/expiredProducts")
    public ResponseEntity<?> getExpiredProducts(
            @RequestHeader("Authorization") String reqHeader,
            @RequestBody Map<String, String> payload) {
        UserInfo user = jwtService.extractUserInfoFromJWT(reqHeader);
        String date = payload.get("date");
        List<Map<String, Object>> expiredProducts = sellerService.getExpiredProducts(user, date);
        return ResponseEntity.ok(expiredProducts);
    }
    
    
 // single id
    @GetMapping("/batches/{id}")
    public ResponseEntity<byte[]> getPdfSingle(@PathVariable String id, @RequestHeader("Authorization") String reqHeader) {
        try {
        	
            return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"batch-" + id + ".pdf\"")
                            .contentType(MediaType.APPLICATION_PDF)
                            .body(fileUploadService.createPdfForSingleId(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // multiple ids via query param: ?ids=1,2,3 and optional cols,rows
    @GetMapping("/batches/pdf")
    public ResponseEntity<byte[]> getPdfMultiple(@RequestParam("batchIds") String batchNumbers,
                                                 @RequestParam(value = "cols", defaultValue = "2") int cols,
                                                 @RequestParam(value = "rows", defaultValue = "4") int rows) {
        try {
            List<String> batchNumberList = Arrays.stream(batchNumbers.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            byte[] pdf = fileUploadService.createPdfForIds(batchNumberList, cols, rows);
            if (pdf.length == 0) return ResponseEntity.notFound().build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"batches.pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    
//    private String generateListOfBarQrCode(List<String> list) throws Exception {
//    	
//    	StringBuilder stringBuilder = new StringBuilder();
//    	StringBuilder titleBuilder = new StringBuilder();
//    	int count = 0;
//    	for(String batchNumber : list) {
//    		count++;
// // 		  byte[] qr = generateQrWithText(batchNumber, 300);
//  		  byte[] barcode = generateCode128WithText(batchNumber, 400, 80);
//  		  boolean auto = false;
////  		  String qrbase64EncodedImage = Base64.getEncoder().encodeToString(qr);
//  		  String barCodebase64EncodedImage = Base64.getEncoder().encodeToString(barcode);
//
//  		  
////  		  String qrUrl = "data:image/png;base64," + qrbase64EncodedImage;
//  		  String barCodeUrl = "data:image/png;base64," + barCodebase64EncodedImage;
//  		  
//  		  stringBuilder.append("<div><img src='" + barCodeUrl + "' alt='BAR' style='width:420px; height:120px'/></div>");
//  		  titleBuilder.append("<title>Print Batch :" + batchNumber + "</title>");
//  		  if(count>10) {
//  			  break;
//  		  }
//    	}
//    	
//	      String html = "<!doctype html>\n<html><head><meta charset='utf-8'>\n"
//	    		  + titleBuilder.toString()
//	              + "<style>@media print{@page {size: auto}} body { font-family: Arial; padding: 10px }</style>\n"
//	              + "</head><body>\n"
//	              + "<div style='display:flex;gap:20px;align-items:center'>"
////	              + "<div><img src='" + qrUrl + "' alt='QR' style='width:220px;height:220px'/></div>"
//	              + stringBuilder.toString()
//
//	              + "</div>\n"
//	              + (true ? "<script>window.print();</script>" : "")
//	              + "</body></html>";
//	      
//	      String tempHtml = "<div style='display:flex;gap:20px;align-items:center'>"
////	              + "<div><img src='" + qrUrl + "' alt='QR' style='width:220px;height:220px'/></div>"
//	              + stringBuilder.toString()
//
//	              + "</div>\n"
//	              + (true ? "<script>window.print();</script>" : "");
//    	
//    	
//    	return tempHtml;
//    }
//    
//	  @GetMapping("/generate/qr/{batchNumber}")
//	  public ResponseEntity<String> getQrCode(@PathVariable String batchNumber) throws Exception {
//		  
//		  byte[] qr = generateQrWithText(batchNumber, 300);
//		  byte[] barcode = generateCode128WithText(batchNumber, 400, 80);
//		  boolean auto = false;
//		  String qrbase64EncodedImage = Base64.getEncoder().encodeToString(qr);
//		  String barCodebase64EncodedImage = Base64.getEncoder().encodeToString(barcode);
//
//		  
//		  String qrUrl = "data:image/png;base64," + qrbase64EncodedImage;
//		  String barCodeUrl = "data:image/png;base64," + barCodebase64EncodedImage;
//
//
//	      String html = "<!doctype html>\n<html><head><meta charset='utf-8'><title>Print Batch " + batchNumber + "</title>\n"
//	              + "<style>@media print{@page {size: auto}} body { font-family: Arial; padding: 10px }</style>\n"
//	              + "</head><body>\n"
//	              + "<div style='display:flex;gap:20px;align-items:center'>"
////	              + "<div><img src='" + qrUrl + "' alt='QR' style='width:220px;height:220px'/></div>"
//	              + "<div><img src='" + barCodeUrl + "' alt='BAR' style='width:420px; height:120px'/></div>"
//
//	              + "</div>\n"
//	              + (auto ? "<script>window.print();</script>" : "")
//	              + "</body></html>";
//	      return ResponseEntity.ok(html);
//	  }
//	  
//	  private String escapeHtml(String s) {
//	      if (s == null) return "";
//	      return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;");
//	  }
//	  
//	  public static byte[] generateQrWithText(String text, int sizePx) throws Exception {
//	      Map<EncodeHintType, Object> hints = new HashMap<>();
//	      hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
//	      hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//
//	      BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, sizePx, sizePx, hints);
//	      BufferedImage qr = MatrixToImageWriter.toBufferedImage(bitMatrix);
//
//	      // Add text under QR
//	      BufferedImage withText = addTextBelowImage(qr, text, 20);
//	      ByteArrayOutputStream baos = new ByteArrayOutputStream();
//	      ImageIO.write(withText, "png", baos);
//	      return baos.toByteArray();
//	  }
//	  
//	    public static byte[] generateCode128WithText(String text, int width, int height) throws Exception {
//	        Map<EncodeHintType, Object> hints = new HashMap<>();
//	        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//
//	        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.CODE_128, width, height, hints);
//	        BufferedImage bar = MatrixToImageWriter.toBufferedImage(bitMatrix);
//
//	        // Add text under barcode
//	        BufferedImage withText = addTextBelowImage(bar, text, 18);
//	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//	        ImageIO.write(withText, "png", baos);
//	        return baos.toByteArray();
//	    }
//	  
//	  private static BufferedImage addTextBelowImage(BufferedImage src, String text, int fontSize) {
//	      int padding = 10;
//	      int textHeight = fontSize + 6;
//	      int w = Math.max(src.getWidth(),  text.length() * fontSize / 2);
//	      int h = src.getHeight() + textHeight + padding;
//
//	      BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
//	      Graphics2D g = out.createGraphics();
//	      // white background
//	      g.setColor(Color.WHITE);
//	      g.fillRect(0, 0, w, h);
//
//	      // center image horizontally
//	      int imgX = (w - src.getWidth()) / 2;
//	      g.drawImage(src, imgX, 0, null);
//
//	      // draw text
//	      g.setColor(Color.BLACK);
//	      g.setFont(new Font("Arial", Font.PLAIN, fontSize));
//
//	      FontMetrics fm = g.getFontMetrics();
//	      int textW = fm.stringWidth(text);
//	      int tx = (w - textW) / 2;
//	      int ty = src.getHeight() + (textHeight + fm.getAscent())/2;
//
//	      g.drawString(text, tx, ty);
//	      g.dispose();
//	      return out;
//	  }
//

}
