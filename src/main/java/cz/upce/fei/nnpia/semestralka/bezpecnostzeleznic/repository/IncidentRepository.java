package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Incident;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.IncidentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findAllByIncidentType(IncidentType incidentType);

    List<Incident> findAllByUser_Carrier_Name(String carrierName);
}
