import React, {useCallback, useEffect, useState} from "react";
import {Alert} from "react-bootstrap";
import BootstrapTable from "react-bootstrap-table-next";
import paginationFactory from 'react-bootstrap-table2-paginator';
import filterFactory, {textFilter, selectFilter, customFilter, dateFilter} from 'react-bootstrap-table2-filter';
import AppNavbar from "../navbar/AppNavbar";
import {Container} from "reactstrap";
import IncidentService from "../../services/IncidentService";
import RegionService from "../../services/RegionService";
import UserService from "../../services/UserService";
import AuthenticationService from "../../services/AuthenticationService";
import CarrierService from "../../services/CarrierService";
import {RangeFilter} from "./RangeFilter";
import {useHistory} from "react-router-dom";
import DateFormatter from "../../services/DateFormatter";

function IncidentTable(){
    const [incidents, setIncidents] = useState([]);
    const [regions, setRegions] = useState([]);
    const [usernames, setUsernames] = useState([]);
    const [carriers, setCarriers] = useState([]);
    const [isAdmin, setAdmin] = useState(false);
    const history = useHistory()


    const parseIncidents = useCallback((response) => {
        if(response){ setIncidents(response.map(it => {
            let date = it.incidentTime;
            it.incidentTime = new Date(date);
            return it})); }
    }, []);

    useEffect(() => {
        IncidentService.getAllIncidents().then((response) => { parseIncidents(response.data); });
    }, [parseIncidents]);

    useEffect(() => {
        RegionService.getRegions().then(response => {
            setRegions(response.data);
        });
    }, [setRegions]);

    useEffect(() => {
        UserService.getCarriersUsers().then(response => {
            setUsernames(response.data);
        });
    }, [setUsernames]);

    useEffect(() => {
        const authorities = AuthenticationService.getAuthorities();
        const admin = authorities.find(it => it.authority === 'ROLE_ADMIN_SZ')
        if (admin != null){
            setAdmin(true);
            CarrierService.getCarriers().then(response => {
                setCarriers(response.data);
            });
        }else {
            setCarriers([]);
            setAdmin(false);
        }
    }, [setCarriers]);

    const incidentOptions = {
        'Bezpečnostní incident' : 'Bezpečnostní incident',
        'Požární incident' : 'Požární incident',
        'Předpokládaný incident': 'Předpokládaný incident'
    };

    const regionOptions = regions.map(region => (
        {
            value: region.name,
            label: region.name
        }
    ));

    const usernameOptions = usernames.map(username => (
        {
            value: username,
            label: username
        }
    ));

    const carrierOptions = carriers.map(carrier => (
        {
            value: carrier.name,
            label: carrier.name
        }
    ));

    const columns = [{
        dataField: 'id',
        text: 'Id',
        filter: textFilter(),
        sort: true
    }, {
        dataField: 'regionName',
        text: 'Region',
        filter: selectFilter({
            options: () => regionOptions
        })
    }, {
        dataField: 'wagonId',
        text: 'Vagón',
        filter: textFilter()
    }, {
        dataField: 'tenementsName',
        text: 'Nemovitost',
        filter: textFilter()
    }, {
        dataField: 'username',
        text: 'Zadal Uživatel',
        filter: selectFilter({
            options: () => usernameOptions
        })
    }, {
        dataField: 'incidentType',
        text: 'Typ incidentu',
        filter: selectFilter({
            options: () => incidentOptions
        })
    }, {
        dataField: 'carrierName',
        text: 'Dopravní společnost',
        filter: isAdmin ? selectFilter({
            options: () => carrierOptions
        }) : null
    }, {
        dataField: 'incidentTime',
        text: 'Čas incidentu',
        formatter: DateFormatter.formatDateWithTime,
        filter: dateFilter(),
        sort: true
    }];

    const handleOnSelect = (row) => {
        let path = '/incident/';
        if (row.incidentType === 'Bezpečnostní incident'){
            path += 'securityIncident'
        }else if (row.incidentType === 'Požární incident'){
            path += 'fireIncident'
        }else {
            path += 'anticipatedIncident'
        }
        path += '/' + row.detailIncidentId;
        history.push(path);
    }

    const selectRow = {
        mode: 'checkbox',
        clickToSelect: true,
        hideSelectColumn: true,
        onSelect: handleOnSelect
    };

    const defaultSorted = [{
        dataField: 'id',
        order: 'desc'
    }];

    let dataTable = (
        <div>
            <Alert variant="info">
                <h1>Incidenty</h1>
            </Alert>
            <BootstrapTable keyField='id' data={ incidents } columns={ columns } pagination={ paginationFactory() }
                            filter={ filterFactory() } selectRow={ selectRow } defaultSorted={ defaultSorted } />
        </div>
    );

    return (
        <div>
            <AppNavbar/>
            <Container fluid>{dataTable}</Container>
        </div>
    );

}

export default IncidentTable