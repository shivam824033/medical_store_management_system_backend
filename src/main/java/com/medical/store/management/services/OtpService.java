/**
 * 
 */
package com.medical.store.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.medical.store.management.model.OtpDetails;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Shivam jaiswal
 * 28-Sept-2025
 */

@Service
public class OtpService {

    @Autowired
    private JavaMailSender mailSender;

    private final Map<String, OtpDetails> otpStorage = new HashMap<>();

    // Generate 6-digit OTP with 5 min expiry
    public String generateOtp(String email) {
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);

        otpStorage.put(email, new OtpDetails(otp, expiryTime));
        sendOtpEmail(email, otp);
        return otp;
    }

    // Send email
    private void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP is: " + otp + " (valid for 5 minutes)");
        mailSender.send(message);
    }

    // Validate OTP with expiry
    public boolean validateOtp(String email, String otp) {
        OtpDetails details = otpStorage.get(email);

        if (details == null) {
            return false; // no OTP generated
        }

        // Check expiry
        if (LocalDateTime.now().isAfter(details.getExpiryTime())) {
            otpStorage.remove(email); // remove expired OTP
            return false;
        }

        // Check match
        return details.getOtp().equals(otp);
    }
}
