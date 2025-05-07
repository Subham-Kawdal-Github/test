package com.incident.management.util;

import com.incident.management.repository.IncidentRepository;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.Random;

@Component
public class IncidentIdGenerator {

    private final IncidentRepository incidentRepository;
    private final Random random = new Random();
    
    public IncidentIdGenerator(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }
    
    public String generateUniqueIncidentId() {
        String incidentId;
        do {
            // Generate ID: RMG + 5-digit random + current year
            String randomDigits = String.format("%05d", random.nextInt(100000));
            int currentYear = Year.now().getValue();
            
            incidentId = "RMG" + randomDigits + currentYear;
        } while (incidentRepository.existsByIncidentId(incidentId));
        
        return incidentId;
    }
}