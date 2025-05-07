package com.incident.management.repository;

import com.incident.management.entity.Incident;
import com.incident.management.entity.User;
import com.incident.management.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByReporter(User reporter);
    Optional<Incident> findByIncidentId(String incidentId);
    boolean existsByIncidentId(String incidentId);
    List<Incident> findByReporterAndStatus(User reporter, Status status);
}