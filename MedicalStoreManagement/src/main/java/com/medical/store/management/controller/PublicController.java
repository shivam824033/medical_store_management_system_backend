package com.medical.store.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medical.store.management.model.UserInfo;
import com.medical.store.management.security.config.JwtTokenUtility;
import com.medical.store.management.services.SellerService;

@RestController
@RequestMapping("/api/public")
public class PublicController {

	@Autowired
	private SellerService sellerService;

	@Autowired
	private JwtTokenUtility jwtService;

	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/getSellerProduct")
	public Object findSellerProduct(@RequestParam("storeId") int storeId,
			@RequestParam("keyword") String searchKeyword) {

		
//	    UserInfo user = jwtService.extractUserInfoFromJWT(reqHeader);

		return sellerService.findAllSellerProduct(storeId, searchKeyword);

	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/getMasterProduct")
	public Object getMasterProduct(@RequestParam("keyword") String searchKeyword) {

//	    UserInfo user = jwtService.extractUserInfoFromJWT(reqHeader);

		return sellerService.getMasterProduct(searchKeyword);

	}

}
