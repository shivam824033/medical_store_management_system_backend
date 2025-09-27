/**
 * 
 */
package com.medical.store.management.services;

import com.medical.store.management.dao.SellerDAO;
import com.medical.store.management.model.PharmacyMasterProduct;
import com.medical.store.management.model.UserInfo;
import com.medical.store.management.security.config.JwtTokenUtility;
import com.opencsv.CSVReader;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Shivam jaiswal
 * 13-Sept-2025
 */

@Service
public class FileUploadService {

	@Autowired
	private SellerDAO sellerDAO;
	
    @Autowired
    private JwtTokenUtility jwtService;

    // Process CSV
    public void processCsv(MultipartFile file) throws Exception {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] line;
            reader.readNext(); // skip header
            
            while ((line = reader.readNext()) != null) {
            	PharmacyMasterProduct product = new PharmacyMasterProduct();
//                Employee emp = new Employee();
//                emp.setName(line[0]);
//                emp.setEmail(line[1]);
//                emp.setSalary(Double.valueOf(line[2]));
//                repo.save(emp);
            }
        }
    }

    // Process Excel
    public void processExcel(MultipartFile file) throws Exception {
        try {
        	List<String> requiredColumns = Arrays.asList("Qty", "BatchNo", "ProductDesc", "ExpDate", "MRP");
            Path tempFile = Files.createTempFile(null, null); // Creates a temp file with a unique name
            file.transferTo(tempFile.toFile());
            List<Map<String, Object>> dataList = readExcel(tempFile.toFile(), requiredColumns);
            
            dataList = dataList.stream()
                .filter(map -> map.get("BatchNo") != null && !map.get("BatchNo").toString().trim().isEmpty())
                .collect(Collectors.toList());
            
            insertProductList(dataList);
            
        } catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void insertProductList(List<Map<String, Object>> dataList) throws ParseException {
    	List<PharmacyMasterProduct> productDetailsList = new ArrayList<>();
    	
    	 ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
         HttpServletRequest request = attributes.getRequest();
         UserInfo user = jwtService.extractUserInfoFromJWT(request.getHeader("Authorization"));
         
        for(Map<String, Object> data : dataList) {
        	PharmacyMasterProduct product = new PharmacyMasterProduct();
        	
        	product.setBatchNumber((String) data.get("BatchNo"));
        	product.setProductDescription((String) data.get("ProductDesc"));
        	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        	product.setProductExpiryDate(formatter.parse(data.get("ExpDate").toString()));
        	product.setProductName((String) data.get("ProductDesc"));
        	product.setProductPerPrice(Double.parseDouble(data.get("MRP").toString()));
        	product.setProductStripCount(Integer.parseInt(data.get("Qty").toString()));
        	
        	product.setCreatedDate(new Date());
        	product.setUpdatedDate(new Date());
        	
        	product.setStoreId(user.getStoreId());
        	product.setUserId(user.getUserId());
        	product.setUsername(user.getUsername());
      
        	productDetailsList.add(product);	
        }
        
        if(!productDetailsList.isEmpty()) {
        	sellerDAO.saveProductFromFile(productDetailsList);
        }
        
        
    }
    
    public static List<Map<String, Object>> readExcel(File file, List<String> requiredColumns) {
        List<Map<String, Object>> dataList = new ArrayList<>();

        try {

            Workbook workbook = Workbook.getWorkbook(file);
            Sheet sheet = workbook.getSheet(0);

            // Step 1: Find indexes of required columns from header row
            Map<String, Integer> columnIndexMap = new HashMap<>();
            for (int col = 0; col < sheet.getColumns(); col++) {
                String columnName = sheet.getCell(col, 0).getContents().trim();
                if (requiredColumns.contains(columnName)) {
                    columnIndexMap.put(columnName, col);
                }
            }

            // Step 2: Read each row only for required columns
            for (int row = 1; row < sheet.getRows(); row++) {
                Map<String, Object> rowData = new HashMap<>();
                for (String colName : requiredColumns) {
                    Integer colIndex = columnIndexMap.get(colName);
                    if (colIndex != null) {
                        Cell cell = sheet.getCell(colIndex, row);
                        rowData.put(colName, cell.getContents());
                    }
                }
                dataList.add(rowData);
            }

            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.print("data list : " + dataList);

        return dataList;
    }
}
