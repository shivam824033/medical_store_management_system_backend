/**
 * 
 */
package com.medical.store.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medical.store.management.model.ProductDetails;

/**
 * @author Shivam jaiswal
 * 01-Sep-2024
 */

@Repository
public interface SellerRepo  extends JpaRepository<ProductDetails, Long>{
	
	//void deleteZeroQuantityProduct(String userId);
	
	Optional<ProductDetails> findByBatchNumberAndUserId(String batchNumber, long userId);
	List<ProductDetails> findByUserId(long userId);

}
