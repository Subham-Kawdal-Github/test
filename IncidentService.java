package com.incident.management.service;

import com.incident.management.dto.IncidentDto;
import com.incident.management.entity.Incident;

import java.util.List;

public interface IncidentService {
    IncidentDto createIncident(IncidentDto incidentDto);
    IncidentDto updateIncident(String incidentId, IncidentDto incidentDto);
    IncidentDto getIncidentById(Long id);
    IncidentDto getIncidentByIncidentId(String incidentId);
    List<IncidentDto> getAllIncidentsByCurrentUser();
    void deleteIncident(Long id);
}
