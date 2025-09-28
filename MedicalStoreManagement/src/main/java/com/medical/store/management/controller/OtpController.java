package com.medical.store.management.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.medical.store.management.model.ResponseDTO;
import com.medical.store.management.services.OtpService;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @GetMapping("/send")
    public Object sendOtp(@RequestParam String email) {
		ResponseDTO res = new ResponseDTO();

		try {
//	        otpService.generateOtp(email);
	        res.setResponse("OTP sent successfully!");
	        res.setStatusCode(200);
	        
		} catch (Exception e) {
			res.setErrorMessage("Some error occurred while generating OTP. Please try after sometime.");
	        res.setStatusCode(500);
		}
        return res;
    }

    @GetMapping("/verify")
    public Object verifyOtp(@RequestParam String email, @RequestParam String otp) {
		ResponseDTO res = new ResponseDTO();

		try {
			boolean isValid = otpService.validateOtp(email, otp);
			Map<String, Object> resultMap = new HashMap<>();
			if(isValid) {
				resultMap.put("message", "OTP verified successfully!");
				resultMap.put("flag", true);
			}else {
				resultMap.put("message", "Invalid or expired OTP!");
				resultMap.put("flag", false);
			}
	        res.setResponse(resultMap);
	        res.setStatusCode(200);
	        
		} catch (Exception e) {
			res.setErrorMessage("Some error occurred while verifying OTP. Please try after sometime.");
	        res.setStatusCode(500);
		}
        
        return res;// "OTP verified successfully!" : "Invalid or expired OTP!";
    }
}

