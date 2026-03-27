/**
 * 
 */
package com.medical.store.management.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
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

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

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
    public List<String> processExcel(MultipartFile file) throws Exception {
        try {
        	List<String> requiredColumns = Arrays.asList("Qty", "BatchNo", "ProductDesc", "ExpDate", "MRP");
            Path tempFile = Files.createTempFile(null, null); // Creates a temp file with a unique name
            file.transferTo(tempFile.toFile());
            List<Map<String, Object>> dataList = readExcel(tempFile.toFile(), requiredColumns);
            
            dataList = dataList.stream()
                .filter(map -> map.get("BatchNo") != null && !map.get("BatchNo").toString().trim().isEmpty())
                .collect(Collectors.toList());
            
            List<String> list = insertProductList(dataList);
            return list;
        } catch (Exception e) {
			e.printStackTrace();
		}
        return new ArrayList<>();
    }
    
    public List<String> insertProductList(List<Map<String, Object>> dataList) throws ParseException {
    	List<PharmacyMasterProduct> productDetailsList = new ArrayList<>();
    	List<String> list = new ArrayList<>();
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
        	list.add(product.getBatchNumber());
        }
        
        if(!productDetailsList.isEmpty()) {
        	sellerDAO.saveProductFromFile(productDetailsList);
        }
        
        return list;
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
    
//    public byte[] createPdfForIds(List<String> batchNumberList, int cols, int rows) throws Exception {
//        if (batchNumberList.isEmpty()) return new byte[0];
//
//        // Page size: use A4 in points (595 x 842)
//        final float PAGE_WIDTH = 595;
//        final float PAGE_HEIGHT = 842;
//        final float MARGIN = 20;
//        final float usableW = PAGE_WIDTH - 2 * MARGIN;
//        final float usableH = PAGE_HEIGHT - 2 * MARGIN;
//
//        float cellW = usableW / cols;
//        float cellH = usableH / rows;
//
//        try (PDDocument doc = new PDDocument()) {
//            // embed a simple font for text
//            PDType0Font font = PDType0Font.load(doc, PDType0Font.class.getResourceAsStream("/org/apache/pdfbox/resources/ttf/LiberationSans-Regular.ttf"));
//
//            int labelIndex = 0;
//            PDPage page = null;
//            PDPageContentStream content = null;
//
//            for (String batchNumber : batchNumberList) {
//                if (labelIndex % (cols * rows) == 0) {
//                    if (content != null) {
//                        content.close();
//                    }
//                    page = new PDPage(); // default is A4
//                    doc.addPage(page);
//                    content = new PDPageContentStream(doc, page);
//                }
//
//                // compute position
//                int posOnPage = labelIndex % (cols * rows);
//                int row = posOnPage / cols;
//                int col = posOnPage % cols;
//
//                float x = MARGIN + col * cellW;
//                float y = PAGE_HEIGHT - MARGIN - (row + 1) * cellH;
//
//                // draw bounding box (optional)
//                // content.setStrokingColor(150,150,150);
//                // content.addRect(x, y, cellW, cellH);
//                // content.stroke();
//
//                // Prepare images (scale to fit)
//                // QR image on left, barcode on right within each label box
//           		  byte[] qrcode = generateQrWithText(batchNumber, 300);
//        		  byte[] barcode = generateCode128WithText(batchNumber, 400, 80);
//                BufferedImage qrImg = ImageIO.read(new ByteArrayInputStream(qrcode));
//                BufferedImage barImg = ImageIO.read(new ByteArrayInputStream(barcode));
//
//                // Create PDImageXObject
////                PDImageXObject pdQr = PDImageXObject.createFromByteArray(doc, qrcode, "qr-" + batchNumber);
//                PDImageXObject pdBar = PDImageXObject.createFromByteArray(doc, barcode, "bar-" + batchNumber);
//
//                // Layout inside cell: leave some padding
//                float pad = 8;
//                float contentW = cellW - 2 * pad;
//                float contentH = cellH - 2 * pad;
//
//                // decide sizes: QR square, barcode wide but short.
//                float qrBoxSize = Math.min(contentH * 0.6f, contentW * 0.45f);
//                float barBoxW = contentW - qrBoxSize - pad;
//                float barBoxH = qrBoxSize * 0.4f;
//
//                // positions inside label
//                float qrX = x + pad;
//                float qrY = y + contentH - qrBoxSize - pad;
//
//                float barX = qrX + qrBoxSize + pad;
//                float barY = qrY + (qrBoxSize - barBoxH) / 2f;
//
//                // draw images
////                content.drawImage(pdQr, qrX, qrY, qrBoxSize, qrBoxSize);
//                content.drawImage(pdBar, barX, barY, barBoxW, barBoxH);
//
//                // draw batch number text below images
////                float textX = x + pad;
////                float textY = y + pad + 4;
////                content.beginText();
////                content.setFont(font, 10);
////                content.newLineAtOffset(textX, textY);
////                String labelText = "Batch: " + batchNumber;
////                content.showText(labelText);
////                content.endText();
//
//                labelIndex++;
//            }
//
//            if (content != null) content.close();
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            doc.save(baos);
//            return baos.toByteArray();
//        }
//    }
    
    public byte[] createPdfForIds(List<String> batchNumberList, int cols, int rows) throws Exception {
        if (batchNumberList.isEmpty()) return new byte[0];

        // PDF page setup — A4
//        List<String> batchNumberList = new ArrayList<>();
//        
//        for(int i = 0; i<30; i++) {
//        	batchNumberList.add(tempList.get(i));
//        }
        final float PAGE_WIDTH = 595;
        final float PAGE_HEIGHT = 842;
        final float MARGIN = 20f; // outer page margin
        final float H_GAP = 2f;  // horizontal gap between barcodes
        final float V_GAP = 12f;  // vertical gap between rows

        // Layout grid
        final int COLUMNS = cols;
        float usableWidth = PAGE_WIDTH - 2 * MARGIN - (COLUMNS - 1) * H_GAP;
        float cellWidth = usableWidth / COLUMNS;
        float cellHeight = 80; // fixed height for barcode label

        try (PDDocument doc = new PDDocument()) {
            // Load font for text
            PDType0Font font = PDType0Font.load(
                    doc,
                    PDType0Font.class.getResourceAsStream("/org/apache/pdfbox/resources/ttf/LiberationSans-Regular.ttf")
            );

            PDPage page = null;
            PDPageContentStream content = null;
            int labelIndex = 0;

            for (String batchNumber : batchNumberList) {
                // Create new page when needed
                if (labelIndex % (COLUMNS * rows) == 0) {
                    if (content != null) content.close();
                    page = new PDPage(PDRectangle.A4);
                    doc.addPage(page);
                    content = new PDPageContentStream(doc, page);
                }

                int posOnPage = labelIndex % (COLUMNS * rows);
                int row = posOnPage / COLUMNS;
                int col = posOnPage % COLUMNS;

                // Compute x,y position (top-left of each label area)
                float x = MARGIN + col * (cellWidth + H_GAP);
                float y = PAGE_HEIGHT - MARGIN - (row + 1) * (cellHeight + V_GAP);

                // Generate barcode
                byte[] barcodeBytes = generateCode128WithText(batchNumber, (int) cellWidth, (int) cellHeight - 20);
                BufferedImage barcodeImage = ImageIO.read(new ByteArrayInputStream(barcodeBytes));
                PDImageXObject pdBarcode = PDImageXObject.createFromByteArray(doc, barcodeBytes, "bar-" + batchNumber);

                // Draw barcode centered inside the cell
                float barcodeX = x;
                float barcodeY = y + 20;
                float barcodeW = cellWidth;
                float barcodeH = cellHeight - 30;

                content.drawImage(pdBarcode, barcodeX, barcodeY, barcodeW, barcodeH);

                // Draw batch number text under barcode
//                content.beginText();
//                content.setFont(font, 10);
//                float textX = x + 5;
//                float textY = y + 5;
//                content.newLineAtOffset(textX, textY);
//                content.showText(batchNumber);
//                content.endText();

                labelIndex++;
            }

            if (content != null) content.close();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos);
            return baos.toByteArray();
        }
    }


    public byte[] createPdfForSingleId(String batchNumber) throws Exception {
      
            try {
                return createPdfForIds(List.of(batchNumber), 1, 1);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }
    
    public static byte[] generateQrWithText(String text, int sizePx) throws Exception {
	      Map<EncodeHintType, Object> hints = new HashMap<>();
	      hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
	      hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

	      BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, sizePx, sizePx, hints);
	      BufferedImage qr = MatrixToImageWriter.toBufferedImage(bitMatrix);

	      // Add text under QR
	      BufferedImage withText = addTextBelowImage(qr, text, 20);
	      ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      ImageIO.write(withText, "png", baos);
	      return baos.toByteArray();
	  }
	  
	    public static byte[] generateCode128WithText(String text, int width, int height) throws Exception {
	        Map<EncodeHintType, Object> hints = new HashMap<>();
	        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

	        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.CODE_128, width, height, hints);
	        BufferedImage bar = MatrixToImageWriter.toBufferedImage(bitMatrix);

	        // Add text under barcode
	        BufferedImage withText = addTextBelowImage(bar, text, 18);
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ImageIO.write(withText, "png", baos);
	        return baos.toByteArray();
	    }
	  
	  private static BufferedImage addTextBelowImage(BufferedImage src, String text, int fontSize) {
	      int padding = 10;
	      int textHeight = fontSize + 6;
	      int w = Math.max(src.getWidth(),  text.length() * fontSize / 2);
	      int h = src.getHeight() + textHeight + padding;

	      BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	      Graphics2D g = out.createGraphics();
	      // white background
	      g.setColor(Color.WHITE);
	      g.fillRect(0, 0, w, h);

	      // center image horizontally
	      int imgX = (w - src.getWidth()) / 2;
	      g.drawImage(src, imgX, 0, null);

	      // draw text
	      g.setColor(Color.BLACK);
	      g.setFont(new Font("Arial", Font.PLAIN, fontSize));

	      FontMetrics fm = g.getFontMetrics();
	      int textW = fm.stringWidth(text);
	      int tx = (w - textW) / 2;
	      int ty = src.getHeight() + (textHeight + fm.getAscent())/2;

	      g.drawString(text, tx, ty);
	      g.dispose();
	      return out;
	  }

}
