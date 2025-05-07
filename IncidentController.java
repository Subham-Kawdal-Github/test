package com.incident.management.controller;

import com.incident.management.dto.IncidentDto;
import com.incident.management.service.IncidentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentService incidentService;
    
    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }
    
    @PostMapping
    public ResponseEntity<IncidentDto> createIncident(@Valid @RequestBody IncidentDto incidentDto) {
        return new ResponseEntity<>(incidentService.createIncident(incidentDto), HttpStatus.CREATED);
    }
    
    @PutMapping("/{incidentId}")
    public ResponseEntity<IncidentDto> updateIncident(
            @PathVariable String incidentId,
            @Valid @RequestBody IncidentDto incidentDto) {
        return ResponseEntity.ok(incidentService.updateIncident(incidentId, incidentDto));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<IncidentDto> getIncidentById(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.getIncidentById(id));
    }
    
    @GetMapping("/search")
    public ResponseEntity<IncidentDto> getIncidentByIncidentId(@RequestParam String incidentId) {
        return ResponseEntity.ok(incidentService.getIncidentByIncidentId(incidentId));
    }
    
    @GetMapping
    public ResponseEntity<List<IncidentDto>> getAllIncidents() {
        return ResponseEntity.ok(incidentService.getAllIncidentsByCurrentUser());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncident(@PathVariable Long id) {
        incidentService.deleteIncident(id);
        return ResponseEntity.noContent().build();
    }
}