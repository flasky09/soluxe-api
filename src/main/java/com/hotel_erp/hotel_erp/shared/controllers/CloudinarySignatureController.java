package com.hotel_erp.hotel_erp.shared.controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/cloudinary")
@RequiredArgsConstructor
public class CloudinarySignatureController {

    private final Cloudinary cloudinary;

    @GetMapping("/signature")
    public Map<String, Object> getSignature(@RequestParam Map<String, Object> params) {
        long timestamp = System.currentTimeMillis() / 1000L;
        params.put("timestamp", timestamp);
        
        String signature = cloudinary.apiSignRequest(params, cloudinary.config.apiSecret);
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("signature", signature);
        result.put("timestamp", timestamp);
        result.put("api_key", cloudinary.config.apiKey);
        result.put("cloud_name", cloudinary.config.cloudName);
        return result;
    }
}
