package com.incident.management.dto;

import com.incident.management.enums.Priority;
import com.incident.management.enums.Status;
import com.incident.management.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentDto {
    
    private Long id;
    private String incidentId;
    private Long reporterId;
    private String reporterName;
    
    @NotBlank(message = "Details are required")
    private String details;
    
    private LocalDateTime reportedDateTime;
    
    @NotNull(message = "Priority must be specified")
    private Priority priority;
    
    private Status status;
    
    @NotNull(message = "Type must be specified")
    private Type type;
}