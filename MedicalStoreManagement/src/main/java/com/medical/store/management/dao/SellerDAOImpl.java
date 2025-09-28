package com.medical.store.management.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.medical.store.management.model.MasterProduct;
import com.medical.store.management.model.PharmacyMasterProduct;
import com.medical.store.management.queryconstants.SellerQueryConstants;
import com.medical.store.management.utility.BasicUtil;

@Repository
public class SellerDAOImpl implements SellerDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final int BATCH_SIZE = 100; // You can tune this value


	@Override
	public int addProductDetails(PharmacyMasterProduct productDetails) {
		int count = jdbcTemplate.update(SellerQueryConstants.INSERT_PRODUCT_DETAILS.toString(),
				new Object[] { productDetails.getProductId(), productDetails.getBatchNumber(),
						productDetails.getProductName(), productDetails.getProductDescription(),
						productDetails.getProductCategory(), productDetails.getProductImage(),
						productDetails.getProductPerPrice(), productDetails.getTotalPoductPrice(),
						productDetails.getProductStripCount(), productDetails.getProductPerStripCount(),
						productDetails.getTotalProductQuantity(), productDetails.getProductExpiryDate(),
						productDetails.getCreatedDate(), productDetails.getUpdatedDate(), productDetails.getUsername(),
						productDetails.getUserId(), productDetails.getStoreId() });
		return count;
	}
	
	@Override
	public void saveProductFromFile(List<PharmacyMasterProduct> productDetailsList) {
	    String sql = """
	        INSERT INTO medical_store.pharmacy_product_details (
	            product_id, batch_number, product_name, product_descrption, 
	            category_id, product_image, pre_product_price, total_price, 
	            strip_count, product_per_strip, total_product_quantity, 
	            expiry_date, created_date, updated_date, username, user_id, store_id
	        )
	        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
	        ON CONFLICT (batch_number, store_id) DO UPDATE SET
	            product_id = EXCLUDED.product_id,
	            product_name = EXCLUDED.product_name,
	            product_descrption = EXCLUDED.product_descrption,
	            category_id = EXCLUDED.category_id,
	            product_image = EXCLUDED.product_image,
	            pre_product_price = EXCLUDED.pre_product_price,
	            total_price = EXCLUDED.total_price,
	            strip_count = EXCLUDED.strip_count,
	            product_per_strip = EXCLUDED.product_per_strip,
	            total_product_quantity = EXCLUDED.total_product_quantity,
	            expiry_date = EXCLUDED.expiry_date,
	            updated_date = EXCLUDED.updated_date,
	            username = EXCLUDED.username,
	            user_id = EXCLUDED.user_id;
	    """;

	    for (int start = 0; start < productDetailsList.size(); start += BATCH_SIZE) {
	        int end = Math.min(start + BATCH_SIZE, productDetailsList.size());
	        List<PharmacyMasterProduct> batchList = productDetailsList.subList(start, end);

	        jdbcTemplate.batchUpdate(sql, batchList, batchList.size(),
	            (ps, productDetails) -> {
	                int i = 1;
	                ps.setLong(i++, productDetails.getProductId());
	                ps.setString(i++, productDetails.getBatchNumber());
	                ps.setString(i++, productDetails.getProductName());
	                ps.setString(i++, productDetails.getProductDescription());
	                ps.setInt(i++, productDetails.getProductCategory());
	                ps.setString(i++, productDetails.getProductImage());
	                ps.setDouble(i++, productDetails.getProductPerPrice());
	                ps.setDouble(i++, productDetails.getTotalPoductPrice());
	                ps.setInt(i++, productDetails.getProductStripCount());
	                ps.setInt(i++, productDetails.getProductPerStripCount());
	                ps.setInt(i++, productDetails.getTotalProductQuantity());
	                ps.setDate(i++, new Date(productDetails.getProductExpiryDate().getTime()));
	                ps.setDate(i++, new Date(productDetails.getCreatedDate().getTime()));
	                ps.setDate(i++, new Date(productDetails.getUpdatedDate().getTime()));
	                ps.setString(i++, productDetails.getUsername());
	                ps.setLong(i++, productDetails.getUserId());
	                ps.setLong(i, productDetails.getStoreId());
	            }
	        );
	    }
	}

//	@Override
//	public void saveProductFromFile(List<PharmacyMasterProduct> productDetailsList) {
//	        // SQL query with ON CONFLICT clause for batch update/insert.
//	        // It has 17 parameters for the INSERT and 15 parameters for the UPDATE
//	        // portion, as `created_date` and the conflict keys are not updated.
//	        String sql = """
//	            INSERT INTO medical_store.pharmacy_product_details (
//	                product_id, batch_number, product_name, product_descrption, 
//	                category_id, product_image, pre_product_price, total_price, 
//	                strip_count, product_per_strip, total_product_quantity, 
//	                expiry_date, created_date, updated_date, username, user_id, store_id
//	            )
//	            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
//	            ON CONFLICT (batch_number, store_id) DO UPDATE SET
//	                product_id = EXCLUDED.product_id,
//	                product_name = EXCLUDED.product_name,
//	                product_descrption = EXCLUDED.product_descrption,
//	                category_id = EXCLUDED.category_id,
//	                product_image = EXCLUDED.product_image,
//	                pre_product_price = EXCLUDED.pre_product_price,
//	                total_price = EXCLUDED.total_price,
//	                strip_count = EXCLUDED.strip_count,
//	                product_per_strip = EXCLUDED.product_per_strip,
//	                total_product_quantity = EXCLUDED.total_product_quantity,
//	                expiry_date = EXCLUDED.expiry_date,
//	                updated_date = EXCLUDED.updated_date,
//	                username = EXCLUDED.username,
//	                user_id = EXCLUDED.user_id;
//	        """;
//
//	        int[][] count =  jdbcTemplate.batchUpdate(sql, productDetailsList, productDetailsList.size(),
//	            ( ps,  productDetails) -> {
//	                int i = 1;
//	                // Mapping the DTO fields to the PreparedStatement placeholders.
//	                // The order must exactly match the column order in the SQL query.
//	                
//	                // INSERT VALUES (17 parameters)
//	                ps.setLong(i++, productDetails.getProductId());
//	                ps.setString(i++, productDetails.getBatchNumber());
//	                ps.setString(i++, productDetails.getProductName());
//	                ps.setString(i++, productDetails.getProductDescription());
//	                ps.setInt(i++, productDetails.getProductCategory());
//	                ps.setString(i++, productDetails.getProductImage());
//	                ps.setDouble(i++, productDetails.getProductPerPrice());
//	                ps.setDouble(i++, productDetails.getTotalPoductPrice());
//	                ps.setInt(i++, productDetails.getProductStripCount());
//	                ps.setInt(i++, productDetails.getProductPerStripCount());
//	                ps.setInt(i++, productDetails.getTotalProductQuantity());
//	                ps.setDate(i++, new Date(productDetails.getProductExpiryDate().getTime()));
//	                ps.setDate(i++, new Date(productDetails.getCreatedDate().getTime()));
//	                ps.setDate(i++, new Date(productDetails.getUpdatedDate().getTime()));
//	                ps.setString(i++, productDetails.getUsername());
//	                ps.setLong(i++, productDetails.getUserId());
//	                ps.setLong(i, productDetails.getStoreId());
//	            });
//	        
//	        System.out.print("total product insert count : " + count);
//	    }


	@Override
	public List<PharmacyMasterProduct> findAllSellerProduct(long storeId, String searchKeyword) {
		List<PharmacyMasterProduct> productList = new ArrayList<>();

		searchKeyword = "%" + searchKeyword + "%";
		List<Map<String, Object>> results = jdbcTemplate.queryForList(
				SellerQueryConstants.FIND_SELLER_PRODUCT_DETAILS.toString(),
				new Object[] { storeId, searchKeyword, searchKeyword });

		if (!results.isEmpty()) {
			for (Map<String, Object> result : results) {
				PharmacyMasterProduct product = new PharmacyMasterProduct();

				product.setProductId(BasicUtil.getResultInteger(result.get("product_id")));
				product.setBatchNumber(BasicUtil.getResultString(result.get("batch_number")));
				product.setProductName(BasicUtil.getResultString(result.get("product_name")));
				product.setProductDescription(BasicUtil.getResultString(result.get("product_descrption")));
				product.setProductCategory(BasicUtil.getResultInteger(result.get("category_id")));
				product.setProductImage(BasicUtil.getResultString(result.get("product_image")));
				product.setProductPerPrice(BasicUtil.getResultDouble(result.get("pre_product_price")));
				product.setTotalPoductPrice(BasicUtil.getResultDouble(result.get("total_price")));
				product.setProductStripCount(BasicUtil.getResultInteger(result.get("strip_count")));
				product.setProductPerStripCount(BasicUtil.getResultInteger(result.get("product_per_strip")));
				product.setTotalProductQuantity(BasicUtil.getResultInteger(result.get("total_product_quantity")));
				product.setProductExpiryDate(BasicUtil.getResultDate(result.get("expiry_date")));
				product.setCreatedDate(BasicUtil.getResultDate(result.get("created_date")));
				product.setUpdatedDate(BasicUtil.getResultDate(result.get("updated_date")));
				product.setUsername(BasicUtil.getResultString(result.get("username")));
				product.setUserId(BasicUtil.getResultInteger(result.get("user_id")));
				product.setStoreId(BasicUtil.getResultInteger(result.get("store_id")));
				productList.add(product);
			}
		}

		return productList;
	}

	@Override
	public List<MasterProduct> getMasterProduct(String searchKeyword) {
		List<MasterProduct> productList = new ArrayList<>();
		searchKeyword = "%" + searchKeyword + "%";
		List<Map<String, Object>> results = jdbcTemplate.queryForList(
				SellerQueryConstants.GET_MASTER_PRODUCT_DETAILS.toString(),
				new Object[] { searchKeyword, searchKeyword});

		if (!results.isEmpty()) {
			for (Map<String, Object> result : results) {

				MasterProduct masterProduct = new MasterProduct();
				masterProduct.setProductId(BasicUtil.getResultInteger(result.get("product_id")));
				masterProduct.setProductName(BasicUtil.getResultString(result.get("product_name")));
				masterProduct.setProductDescription(BasicUtil.getResultString(result.get("product_description")));
				masterProduct.setProductImage(BasicUtil.getResultString(result.get("product_image")));
				masterProduct.setProductCategoryId(BasicUtil.getResultInteger(result.get("category_id")));
				productList.add(masterProduct);
			}
		}
		return productList;
	}

	@Override
	public boolean isBatchNumberPresent(String BatchNumber, long userId) {
		int count = jdbcTemplate.queryForObject(SellerQueryConstants.IS_BATCH_NUMBER_PRESENT.toString(), Integer.class, new Object[] { BatchNumber, userId});
		return count>0;
	}
	
	// ...existing code...
	@Override
	public void reduceProductStock(String batchNumber, long userId, int quantity, long storeId) {
	    String sql = "UPDATE medical_store.pharmacy_product_details SET strip_count = strip_count - ? WHERE batch_number = ? AND user_id = ? AND store_id = ?";
	    jdbcTemplate.update(sql, quantity, batchNumber, userId, storeId);
	}
	// ...existing code...
	
	@Override
	public int getProductStock(String batchNumber, long userId) {
	    String sql = "SELECT strip_count FROM medical_store.pharmacy_product_details WHERE batch_number = ? AND user_id = ?";
	    Integer count = jdbcTemplate.queryForObject(sql, Integer.class, new Object[]{batchNumber, userId});
	    return count != null ? count : 0;
	}
	
	@Override
	public List<Map<String, Object>> findExpiredProducts(long userId, long storeId, String date) {
	    String sql = "SELECT * FROM medical_store.pharmacy_product_details WHERE user_id = ? AND store_id = ? AND expiry_date < CAST(? AS DATE)";
	    return jdbcTemplate.queryForList(sql, userId, storeId, date);
	}

}
