package com.medical.store.management.model;

import java.util.Date;

public class PharmacyMasterProduct {
	
	private long productId;
	private String batchNumber;
	private String productName;
	private String productDescription;
	private int productCategory;
	private String productImage;
	private Double productPerPrice;
	private Double totalPoductPrice;
	private int productStripCount;
	private int productPerStripCount;
	private int totalProductQuantity;
	private Date productExpiryDate;
	private Date createdDate;
	private Date updatedDate;                                     
	
	private String username;
	private long userId;
	private long storeId;
	
	public String getProductImage() {
		return productImage;
	}
	public void setProductImage(String productImgae) {
		this.productImage = productImgae;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public String getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public int getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(int productCategory) {
		this.productCategory = productCategory;
	}
	public Double getProductPerPrice() {
		return productPerPrice;
	}
	public void setProductPerPrice(Double productPerPrice) {
		this.productPerPrice = productPerPrice;
	}
	public Double getTotalPoductPrice() {
		return totalPoductPrice;
	}
	public void setTotalPoductPrice(Double totalPoductPrice) {
		this.totalPoductPrice = totalPoductPrice;
	}
	public int getProductStripCount() {
		return productStripCount;
	}
	public void setProductStripCount(int productStripCount) {
		this.productStripCount = productStripCount;
	}
	public int getProductPerStripCount() {
		return productPerStripCount;
	}
	public void setProductPerStripCount(int productPerStripCount) {
		this.productPerStripCount = productPerStripCount;
	}
	public int getTotalProductQuantity() {
		return totalProductQuantity;
	}
	public void setTotalProductQuantity(int totalProductQuantity) {
		this.totalProductQuantity = totalProductQuantity;
	}
	public Date getProductExpiryDate() {
		return productExpiryDate;
	}
	public void setProductExpiryDate(Date productExpiryDate) {
		this.productExpiryDate = productExpiryDate;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getStoreId() {
		return storeId;
	}
	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}

}
