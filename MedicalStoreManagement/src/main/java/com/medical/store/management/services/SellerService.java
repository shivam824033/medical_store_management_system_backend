/**
 * 
 */
package com.medical.store.management.services;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

			if(sellerDAO.isBatchNumberPresent(productDetails.getBatchNumber(), user.getUserId())) {
				//update product details
				res.setResponse("product Updated succesfully");
			} else {
				int count = sellerDAO.addProductDetails(productDetails);

				res.setResponse("product added successfully : " + count);
			}
			
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

	public ResponseDTO findAllSellerProduct(long storeId, String searchKeyword) {
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
	
	  @Transactional
	    public ResponseDTO finalizeBill(List<Map<String, Object>> billItems, UserInfo user) {
	        ResponseDTO res = new ResponseDTO();
	        try {
	            Long storeId = null;
	            for (Map<String, Object> item : billItems) {
	                String batchNumber = (String) item.get("batchNumber");
	                Integer quantity = (Integer) item.get("quantity");
	                long itemStoreId = ((Number) item.get("storeId")).longValue();
	                long itemUserId = ((Number) item.get("userId")).longValue();

	                // Validation: All items must belong to the same store and user
	                if (storeId == null) storeId = itemStoreId;
	                if (storeId!=itemStoreId || user.getUserId() != itemUserId) {
	                    throw new RuntimeException("All products must belong to the same store and user.");
	                }

	                // Validation: Check stock
	                int available = sellerDAO.getProductStock(batchNumber, itemUserId);
	                if (available < quantity) {
	                    throw new RuntimeException("Insufficient stock for batch " + batchNumber + ". Available: " + available);
	                }
	            }

	            // If all validations pass, update stock
	            try {
		            for (Map<String, Object> item : billItems) {
		                String batchNumber = (String) item.get("batchNumber");
		                Integer quantity = (Integer) item.get("quantity");
		                Long itemUserId = ((Number) item.get("userId")).longValue();
		                sellerDAO.reduceProductStock(batchNumber, user.getUserId(), quantity, user.getStoreId());
		            }
				} catch (Exception e) {
					 throw new RuntimeException("Error Occurred while updating product details");
				}


	            res.setResponse("Bill finalized and stock updated successfully!");
	            res.setStatusCode(200);
	        } catch (Exception e) {
	            // Transaction will rollback automatically on exception
	            res.setErrorMessage("Failed to finalize bill: " + e.getMessage());
	            res.setStatusCode(400);
	        }
	        return res;
	    }
	  
	  public List<Map<String, Object>> getExpiredProducts(UserInfo user, String date) {
		    return sellerDAO.findExpiredProducts(user.getUserId(), user.getStoreId(), date);
		}

}
