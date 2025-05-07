package com.incident.management.service;

import com.incident.management.dto.IncidentDto;
import com.incident.management.entity.Incident;
import com.incident.management.entity.User;
import com.incident.management.enums.Status;
import com.incident.management.exception.ResourceNotFoundException;
import com.incident.management.exception.UnauthorizedException;
import com.incident.management.repository.IncidentRepository;
import com.incident.management.repository.UserRepository;
import com.incident.management.util.IncidentIdGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;
    private final UserService userService;
    private final IncidentIdGenerator incidentIdGenerator;
    
    public IncidentServiceImpl(IncidentRepository incidentRepository, 
                             UserService userService,
                             IncidentIdGenerator incidentIdGenerator) {
        this.incidentRepository = incidentRepository;
        this.userService = userService;
        this.incidentIdGenerator = incidentIdGenerator;
    }
    
    @Override
    public IncidentDto createIncident(IncidentDto incidentDto) {
        User currentUser = userService.getCurrentUser();
        
        // Generate unique incident ID
        String incidentId = incidentIdGenerator.generateUniqueIncidentId();
        
        Incident incident = new Incident();
        incident.setIncidentId(incidentId);
        incident.setReporter(currentUser);
        incident.setDetails(incidentDto.getDetails());
        incident.setReportedDateTime(LocalDateTime.now());
        incident.setPriority(incidentDto.getPriority());
        incident.setStatus(Status.OPEN); // Default status is OPEN
        incident.setType(incidentDto.getType());
        
        Incident savedIncident = incidentRepository.save(incident);
        
        return mapToDto(savedIncident);
    }
    
    @Override
    public IncidentDto updateIncident(String incidentId, IncidentDto incidentDto) {
        Incident incident = incidentRepository.findByIncidentId(incidentId)
                .orElseThrow(() -> new ResourceNotFoundException("Incident not found with id: " + incidentId));
        
        // Check if user is the reporter
        User currentUser = userService.getCurrentUser();
        if (!incident.getReporter().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only update your own incidents");
        }
        
        // Check if incident is closed
        if (incident.getStatus() == Status.CLOSED) {
            throw new UnauthorizedException("Cannot update a closed incident");
        }
        
        incident.setDetails(incidentDto.getDetails());
        incident.setPriority(incidentDto.getPriority());
        incident.setStatus(incidentDto.getStatus());
        incident.setType(incidentDto.getType());
        
        Incident updatedIncident = incidentRepository.save(incident);
        
        return mapToDto(updatedIncident);
    }
    
    @Override
    public IncidentDto getIncidentById(Long id) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incident not found with id: " + id));
        
        // Check if user is the reporter
        User currentUser = userService.getCurrentUser();
        if (!incident.getReporter().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only view your own incidents");
        }
        
        return mapToDto(incident);
    }
    
    @Override
    public IncidentDto getIncidentByIncidentId(String incidentId) {
        Incident incident = incidentRepository.findByIncidentId(incidentId)
                .orElseThrow(() -> new ResourceNotFoundException("Incident not found with id: " + incidentId));
        
        // Check if user is the reporter
        User currentUser = userService.getCurrentUser();
        if (!incident.getReporter().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only view your own incidents");
        }
        
        return mapToDto(incident);
    }
    
    @Override
    public List<IncidentDto> getAllIncidentsByCurrentUser() {
        User currentUser = userService.getCurrentUser();
        List<Incident> incidents = incidentRepository.findByReporter(currentUser);
        
        return incidents.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteIncident(Long id) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incident not found with id: " + id));
        
        // Check if user is the reporter
        User currentUser = userService.getCurrentUser();
        if (!incident.getReporter().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only delete your own incidents");
        }
        
        incidentRepository.delete(incident);
    }
    
    private IncidentDto mapToDto(Incident incident) {
            IncidentDto incidentDto = new IncidentDto();
            incidentDto.setId(incident.getId());
            incidentDto.setIncidentId(incident.getIncidentId());
            incidentDto.setReporterId(incident.getReporter().getId());
            incidentDto.setReporterName(incident.getReporter().getUsername());
            incidentDto.setDetails(incident.getDetails());
            incidentDto.setReportedDateTime(incident.getReportedDateTime());
            incidentDto.setPriority(incident.getPriority());
            incidentDto.setStatus(incident.getStatus());
            incidentDto.setType(incident.getType());
            
            return incidentDto;
        }
    
    }