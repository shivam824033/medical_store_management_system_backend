/**
 * 
 */
package com.medical.store.management.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public String uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
        	fileUploadService.processExcel(file);
            return "Excel file processed successfully!";
        } catch (Exception e) {
            return "Error processing Excel: " + e.getMessage();
        }
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

}
