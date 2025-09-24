package com.medical.store.management.dao;

import java.util.List;
import java.util.Map;

import com.medical.store.management.model.MasterProduct;
import com.medical.store.management.model.PharmacyMasterProduct;

public interface SellerDAO {
	
	public int addProductDetails(PharmacyMasterProduct productDetails);
	
	public List<PharmacyMasterProduct> findAllSellerProduct(int storeId,  String searchKeyword);

	public List<MasterProduct> getMasterProduct(String searchKeyword);
	
	public boolean isBatchNumberPresent(String BatchNumber, long userId);
	
	public void saveProductFromFile(List<PharmacyMasterProduct> productDetails);
	
	// ...existing code...
	void reduceProductStock(String batchNumber, long userId, int quantity, long storeId);
	int getProductStock(String batchNumber, long userId);
	List<Map<String, Object>> findExpiredProducts(long userId, long storeId, String date);
	// ...existing code...

}
