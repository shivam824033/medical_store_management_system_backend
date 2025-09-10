package com.medical.store.management.dao;

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
	public List<PharmacyMasterProduct> findAllSellerProduct(int storeId, String searchKeyword) {
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

}
