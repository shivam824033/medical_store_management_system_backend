/**
 * 
 */
package com.medical.store.management.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medical.store.management.dao.SellerDAO;
import com.medical.store.management.model.MasterProduct;
import com.medical.store.management.model.PharmacyMasterProduct;
import com.medical.store.management.model.ResponseDTO;
import com.medical.store.management.model.UserInfo;

/**
 * @author Shivam jaiswal 01-Sep-2024
 */

@Service
public class SellerService {

	@Autowired
	private SellerDAO sellerDAO;

	public ResponseDTO addProduct(PharmacyMasterProduct productDetails, UserInfo user) {

		ResponseDTO res = new ResponseDTO();

		if (productDetails.getBatchNumber() != null && productDetails.getBatchNumber() != "") {
			productDetails.setUserId(user.getUserId());
			productDetails.setUsername(user.getUsername());
			productDetails.setStoreId(user.getStoreId());
			productDetails.setCreatedDate(new Date());

			int count = sellerDAO.addProductDetails(productDetails);

			res.setResponse("product added successfully : " + count);
			res.setStatusCode(200);
			return res;
		}
		res.setErrorMessage("please enter batch number");
		res.setStatusCode(403);
		return res;
	}

	public ResponseDTO sellProduct(PharmacyMasterProduct productDetails, UserInfo user) {
		ResponseDTO res = new ResponseDTO();

//		Optional<ProductDetails> productDtls = sellerRepo.findByBatchNumberAndUserId(productDetails.getBatchNumber(), user.getUserId());
//		
//		
//		if(productDtls.isPresent() && productDtls.get().getProductQuantity()!=0) {
//			
//			int quantity = productDtls.get().getProductQuantity() - productDetails.getProductQuantity();
//			productDtls.get().setProductQuantity(quantity);
//			productDtls.get().setUpdatedDate(new Date());
//			ProductDetails updatedProduct =	sellerRepo.save(productDtls.get());
//			res.setResponse(updatedProduct);
//			res.setStatusCode(200);
//			return res;
//		}
//		
//		res.setErrorMessage("Please select correct product");
//		res.setStatusCode(403);

		return res;
	}

	public ResponseDTO findAllSellerProduct(int storeId, String searchKeyword) {
		ResponseDTO res = new ResponseDTO();

		try {
			List<PharmacyMasterProduct> productList = sellerDAO.findAllSellerProduct(storeId, searchKeyword);
			res.setResponse(productList);
			res.setStatusCode(200);
			return res;
		} catch (Exception e) {
			res.setErrorMessage("Error while fetching product details");
			res.setStatusCode(500);
			return res;
		}
	}

	public ResponseDTO getMasterProduct(String searchKeyword) {
		ResponseDTO res = new ResponseDTO();

		try {
			List<MasterProduct> productList = sellerDAO.getMasterProduct(searchKeyword);
			res.setResponse(productList);
			res.setStatusCode(200);
			return res;
		} catch (Exception e) {
			res.setErrorMessage("Error while fetching product details");
			res.setStatusCode(500);
			return res;
		}
	}

}
