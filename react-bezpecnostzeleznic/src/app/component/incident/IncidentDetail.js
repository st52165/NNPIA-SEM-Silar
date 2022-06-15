import React, {useEffect, useState} from "react";
import IncidentService from "../../service/IncidentService";
import {Container} from "react-bootstrap";
import AppNavbar from "../nav/AppNavbar";
import {useParams} from "react-router-dom";
import IncidentFormatter from "../../service/IncidentFormatter"
import DateFormatter from "../../service/DateFormatter";
import RegionFormatter from "../../service/RegionFormatter";
import UserFormatter from "../../service/UserFormatter";
import DamageTable from "../damage/DamageTable";

function IncidentDetail() {
    const {id} = useParams();
    const [incident, setIncident] = useState(undefined);

    useEffect(() => {
        IncidentService.getIncidentByIncidentId(id)
            .then((response) => {
                setIncident(response.data);
            });
    }, [id]);

    let detail = (
        <div>
            {incident && <div>
                <h1>Detail incidentu č. {incident.id}</h1>
                <div>
                    <h6 className={'fw-bold'}>Popis: </h6>
                    <p>{incident.description ? incident.description : "-"}</p>
                </div>
                <div style={{display: "flex", flexFlow: "wrap", gap: "2rem"}}>
                    <div>
                        <h6 className={'fw-bold'}>Lokace:</h6>
                        <p>{incident.position && IncidentFormatter.gpsFormatter(incident.position)}</p>
                    </div>
                    <div>
                        <h6 className={'fw-bold'}>Typ: </h6>
                        <p>{incident.incidentType}</p>
                    </div>
                    <div>
                        <h6 className={'fw-bold'}>Trestný čin: </h6>
                        <p>{IncidentFormatter.booleanFormatter(incident.isCriminalOffense)}</p>
                    </div>
                    <div>
                        <h6 className={'fw-bold'}>Řešen policíí: </h6>
                        <p>{IncidentFormatter.booleanFormatter(incident.isSolvedByPolice)}</p>
                    </div>
                    <div>
                        <h6 className={'fw-bold'}>Začátek incidentu: </h6>
                        <p>{incident.validityFrom && DateFormatter.formatDateWithTime(incident.validityFrom)}</p>
                    </div>
                    <div>
                        <h6 className={'fw-bold'}>Ukončení incidentu: </h6>
                        <p>{incident.validityTo && DateFormatter.formatDateWithTime(incident.validityTo)}</p>
                    </div>
                    <div>
                        <h6 className={'fw-bold'}>Region: </h6>
                        <p>{incident.regionDto && RegionFormatter.nameFormatter(incident.regionDto)}</p>
                    </div>
                    <div>
                        <h6 className={'fw-bold'}>Naposledy upravil: </h6>
                        <p>{incident.userInfoDto && UserFormatter.nameFormatter(incident.userInfoDto)}</p>
                    </div>
                </div>
            </div>
            }
        </div>
    );

    return (<div>
            <AppNavbar/>
            <Container fluid="md">{detail}</Container>
            <Container fluid="md">
                <h2>Nahlášené škody u incidentu č. {id}</h2>
                {incident && <DamageTable id={incident.id}/>}
            </Container>
        </div>
    );
}

export default IncidentDetail;