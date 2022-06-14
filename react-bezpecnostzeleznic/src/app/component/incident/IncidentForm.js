import {Form, Button, Container} from "react-bootstrap";
import React, {useEffect, useState} from "react";
import RegionService from "../../service/RegionService";
import AppNavbar from "../nav/AppNavbar";
import IncidentService from "../../service/IncidentService";
import DatePicker, {registerLocale} from "react-datepicker";
import cs from "date-fns/locale/cs";

registerLocale("cs", cs);

function IncidentForm(props) {
    const [regions, setRegions] = useState([]);
    const [incidentTypes, setIncidentTypes] = useState([]);
    const [data, setData] = useState({
        description: null,
        incidentType: '',
        isCriminalOffense: false,
        isSolvedByPolice: false,
        gpsLat: 50.0375792,
        gpsLon: 15.7774239,
        validityFrom: new Date(),
        validityTo: new Date(),
        regionID: -1
    });

    useEffect(() => {
        RegionService.getRegions().then(response => {
            setRegions(response.data);
        });
    }, [setRegions]);
    useEffect(() => {
        IncidentService.getAllIncidentTypes().then(response => {
            setIncidentTypes(response.data);
        });
    }, [setIncidentTypes]);

    function changeValueHandler(name, value) {
        setData({...data, [name]: value});
    }

    function handleOnSubmit(e) {
        e.preventDefault();
        if (data.incidentType === '' || data.regionID === -1) {
            return false;
        }
        IncidentService.postIncident(data).then((response) => {
            if (response.data && response.data.id) {
                props.history.push(`/incident/${response.data.id}`);
            }
        });
    }

    let form = (
        <Form onSubmit={handleOnSubmit}>
            <Form.Group className="mb-3" controlId="incidentType">
                <Form.Label>Výběr typu incidentu</Form.Label>
                <Form.Control onChange={(e) => {
                    changeValueHandler("incidentType", e.target.value);
                }} defaultValue={''} as="select" isInvalid={data.incidentType === ''}>
                    <option key='default' id='default' value='' disabled>Vyberte typ incidentu:</option>
                    {incidentTypes && incidentTypes.map((incidentType) =>
                        <option key={incidentType.name} id={incidentType.name}
                                value={incidentType.name}>{incidentType.name}</option>
                    )}

                </Form.Control>
            </Form.Group>

            <Form.Group className="mb-3" controlId="region">
                <Form.Label>Výběr regionu</Form.Label>
                <Form.Control onChange={(e) => {
                    changeValueHandler("regionID", e.target.value);
                }} defaultValue={'-1'} as="select" isInvalid={data.regionID === -1}>
                    <option key='default' id='default' value='-1' disabled>Vyberte region:</option>
                    {regions && regions.map((region) =>
                        <option key={region.id} id={region.id} value={region.id}>{region.name}</option>
                    )}

                </Form.Control>
            </Form.Group>

            <Form.Group className="mb-3" controlId="validityFrom">
                <Form.Label>Začátek incidentu</Form.Label>
                <div style={{display: "flex"}}>
                    <DatePicker
                        selected={data.validityFrom}
                        onChange={(e) => {
                            changeValueHandler("validityFrom", e)
                        }}
                        id="dateFrom"
                        name="dateFrom"
                        locale="cs"
                        showTimeSelect
                        timeFormat="p"
                        timeIntervals={15}
                        dateFormat="dd. MMMM yyyy H:mm:ss"
                    />
                </div>
            </Form.Group>

            <Form.Group className="mb-3" controlId="validityTo">
                <Form.Label>Ukončení incidentu</Form.Label>
                <div style={{display: "flex"}}>
                    <DatePicker
                        selected={data.validityTo}
                        onChange={(e) => {
                            changeValueHandler("validityTo", e)
                        }}
                        id="dateTo"
                        name="dateTo"
                        locale="cs"
                        showTimeSelect
                        timeFormat="p"
                        timeIntervals={15}
                        dateFormat="dd. MMMM yyyy H:mm:ss"
                    />
                </div>
            </Form.Group>

            <Form.Group className="mb-3" controlId="gpsLat">
                <Form.Label>Souřadnice (Latitude)</Form.Label>
                <Form.Control
                    onChange={(e) => {
                        changeValueHandler(e.target.name, e.target.value)
                    }}
                    type="number"
                    name="gpsLat"
                    defaultValue={50.0375792}

                    placeholder="Vložte zeměpisnou šířku."/>
            </Form.Group>

            <Form.Group className="mb-3" controlId="gpsLon">
                <Form.Label>Souřadnice (Longitude)</Form.Label>
                <Form.Control
                    onChange={(e) => {
                        changeValueHandler(e.target.name, e.target.value)
                    }}
                    type="number"
                    name="gpsLon"
                    defaultValue={15.7774239}
                    placeholder="Vložte zeměpisnou délku."/>
            </Form.Group>

            <Form.Group className="mb-3" controlId="description">
                <Form.Label>Popis</Form.Label>
                <Form.Control
                    onChange={(e) => {
                        changeValueHandler(e.target.name, e.target.value)
                    }}
                    as="textarea"
                    name="description"
                    placeholder="Vložte popisek události."
                />
            </Form.Group>

            <Form.Group className="mb-3" controlId="isCriminalOffense">
                <Form.Label>Tresný čin</Form.Label>
                <Form.Check type="checkbox" id="isCriminalOffense" name="isCriminalOffense" onChange={(e) => {
                    changeValueHandler(e.target.name, e.target.checked)
                }}/>
            </Form.Group>

            <Form.Group className="mb-3" controlId="isSolvedByPolice">
                <Form.Label>Řešeno policií</Form.Label>
                <Form.Check type="checkbox" id="isSolvedByPolice" name="isSolvedByPolice" onChange={(e) => {
                    changeValueHandler(e.target.name, e.target.checked)
                }}/>
            </Form.Group>

            <Button variant="primary" type="submit">
                Nahlásit
            </Button>
        </Form>
    );

    return (<div>
        <AppNavbar/>
        <Container fluid="md">
            <h1>Nahlásit incident</h1>
            {form}
        </Container>
    </div>);
}

export default IncidentForm;