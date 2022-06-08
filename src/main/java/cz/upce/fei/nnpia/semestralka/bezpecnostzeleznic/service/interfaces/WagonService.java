package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.WagonDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Wagon;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.security.service.UserPrinciple;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

public interface WagonService {

    List<Wagon> getWagonsList();

    List<Wagon> getWagonsListByWagonTypeID(Long wagonTypeID);

    Wagon getWagonFromID(Long wagonID) throws HttpServerErrorException;

    boolean existsByWagonID(Long wagonID);

    Wagon insert(WagonDto wagonRequest) throws HttpServerErrorException;

    Wagon update(Long id, WagonDto wagonRequest) throws HttpServerErrorException;

    Wagon delete(Long wagonID) throws HttpServerErrorException;

    Wagon addIncident(Long wagonID, Long incidentId) throws HttpServerErrorException;

    Wagon deleteIncident(Long wagonID, Long incidentId) throws HttpServerErrorException;

    List<Wagon> getWagonsByCarrierId();

    List<Wagon> getAllWagons(UserPrinciple currentUser);
}
