/**
 * 
 */
package com.medical.store.management.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medical.store.management.model.ProductDetails;
import com.medical.store.management.model.ResponseDTO;
import com.medical.store.management.model.UserInfo;
import com.medical.store.management.repository.SellerRepo;

/**
 * @author Shivam jaiswal
 * 01-Sep-2024
 */

@Service
public class SellerService {
	
	@Autowired
	private SellerRepo sellerRepo;
	
	public ResponseDTO addProduct(ProductDetails productDetails, UserInfo user) {
		
		ResponseDTO res = new ResponseDTO();
		
		if(productDetails.getBatchNumber()!=null) {
			productDetails.setUserId(user.getUserId());
			productDetails.setUsername(user.getUsername());
			productDetails.setCreatedDate(new Date());
			ProductDetails product = sellerRepo.save(productDetails);
			res.setResponse(product);
			res.setStatusCode(200);
			return res;
		}
		res.setErrorMessage("please enter batch number");
		res.setStatusCode(403);
		return res;
	}
	
	public ResponseDTO sellProduct(ProductDetails productDetails, UserInfo user) {
		ResponseDTO res = new ResponseDTO();

		Optional<ProductDetails> productDtls = sellerRepo.findByBatchNumberAndUserId(productDetails.getBatchNumber(), user.getUserId());
		
		
		if(productDtls.isPresent() && productDtls.get().getProductQuantity()!=0) {
			
			int quantity = productDtls.get().getProductQuantity() - productDetails.getProductQuantity();
			productDtls.get().setProductQuantity(quantity);
			productDtls.get().setUpdatedDate(new Date());
			ProductDetails updatedProduct =	sellerRepo.save(productDtls.get());
			res.setResponse(updatedProduct);
			res.setStatusCode(200);
			return res;
		}
		
		res.setErrorMessage("Please select correct product");
		res.setStatusCode(403);
		
		return res;
	}

	public ResponseDTO findAllSellerProductoduct(UserInfo user) {
		ResponseDTO res = new ResponseDTO();
		
		try {
			List<ProductDetails> productList = sellerRepo.findByUserId(user.getUserId());
			res.setResponse(productList);
			res.setStatusCode(0);
			return res;
		} catch (Exception e) {
			res.setErrorMessage("Error while fetching product details");
			res.setStatusCode(403);
			return res;
		}
		
	}

}
