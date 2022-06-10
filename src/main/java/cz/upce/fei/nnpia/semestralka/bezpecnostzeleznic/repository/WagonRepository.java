package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Carrier;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Wagon;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.WagonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WagonRepository extends JpaRepository<Wagon, Long> {

    List<Wagon> findAllByWagonType(WagonType wagonType);

    List<Wagon> findAllByCarrier(Carrier carrier);

    List<Wagon> findAllByCarrier_Id(Long carrierId);
}
