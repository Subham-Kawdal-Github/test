package com.incident.management.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PincodeServiceImpl implements PincodeService {

    @Value("${pincode.api.url}")
    private String pincodeApiUrl;
    
    private final RestTemplate restTemplate;
    
    public PincodeServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    @Override
    public String getCityByPincode(String pincode) {
        try {
            String apiUrl = pincodeApiUrl + pincode;
            List response = restTemplate.getForObject(apiUrl, List.class);
            
            if (response != null && !response.isEmpty()) {
                Map<String, Object> result = (LinkedHashMap) response.get(0);
                
                if ("Success".equals(result.get("Status"))) {
                    List<Map<String, Object>> postOffices = (List<Map<String, Object>>) result.get("PostOffice");
                    
                    if (postOffices != null && !postOffices.isEmpty()) {
                        return (String) postOffices.get(0).get("District");
                    }
                }
            }
            
            return null;
        } catch (Exception e) {
            // If API call fails, don't block the user registration process
            return null;
        }
    }
}
