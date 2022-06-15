import React, {useCallback, useEffect, useState} from "react";
import {Alert} from "react-bootstrap";
import BootstrapTable from "react-bootstrap-table-next";
import paginationFactory from 'react-bootstrap-table2-paginator';
import filterFactory, {textFilter, selectFilter, dateFilter} from 'react-bootstrap-table2-filter';
import AppNavbar from "../nav/AppNavbar";
import {Container} from "reactstrap";
import IncidentService from "../../service/IncidentService";
import RegionService from "../../service/RegionService";
import UserService from "../../service/UserService";
import CarrierService from "../../service/CarrierService";
import DateFormatter from "../../service/DateFormatter";
import {useHistory} from "react-router-dom";
import AuthenticationService from "../../service/AuthenticationService";
import IncidentFormatter from "../../service/IncidentFormatter"


function IncidentTable() {
    const [incidents, setIncidents] = useState([]);
    const [incidentTypes, setIncidentTypes] = useState([]);
    const [regions, setRegions] = useState([]);
    const [users, setUsers] = useState([]);
    const [carriers, setCarriers] = useState([]);
    const history = useHistory()


    const parseIncidents = useCallback((response) => {
        if (response) {
            setIncidents(response.map(it => {
                return it;
            }));
        }
    }, []);
    const parseUsers = useCallback((response) => {
        if (response) {
            setUsers(response.map(user => {
                return user;
            }));
        }
    }, []);

    useEffect(() => {
        if (AuthenticationService.isAdminDS()) {
            CarrierService.getCarriers().then(response => {
                setCarriers(response.data);
            });
        } else {
            setCarriers([]);
        }
    }, [setCarriers]);

    useEffect(() => {
        if (AuthenticationService.isAdminDS()) {
            IncidentService.getAllIncidents()
                .then((response) => {
                    parseIncidents(response.data);
                });
        } else {
            IncidentService.getAllIncidentsByCurrentUser()
                .then((response) => {
                    parseIncidents(response.data);
                });
        }
    }, [parseIncidents]);

    useEffect(() => {
        IncidentService.getAllIncidentTypes().then((response) => {
            setIncidentTypes(response.data);
        });
    }, []);

    useEffect(() => {
        RegionService.getRegions().then(response => {
            setRegions(response.data);
        });
    }, [setRegions]);

    useEffect(() => {
        UserService.getUsersList().then(response => {
            parseUsers(response.data);
        });
    }, [parseUsers]);


    const incidentTypeOptions = incidentTypes.map(type => (
        {
            value: type.name,
            label: type.name
        }));

    const regionOptions = regions.map(region => (
        {
            value: region.name,
            label: region.name
        }
    ));

    const usernameOptions = users.map(user => (
        {
            value: user.username,
            label: `${user.firstname} ${user.lastname}`
        }
    ));

    const carrierOptions = carriers.map(carrier => (
        {
            value: carrier.name,
            label: carrier.name
        }
    ));
    const booleanOptions =
        [{
            value: "Ano",
            label: "Ano"
        },
            {
                value: "Ne",
                label: "Ne"
            }];

    const datumFiltr = dateFilter({
        style: {display: 'inline-grid', width: '100%'}
    });
    const userFormatter = (user) => {
        return `${user.firstname} ${user.lastname}`;
    }
    const userFilterValue = (user) => {
        return user.username;
    }

    const columns = [{
        dataField: 'id',
        text: 'Id',
        filter: textFilter(),
        isKey: true,
        sort: true
    }, {
        dataField: 'incidentType',
        text: 'Typ incidentu',
        filter: selectFilter({
            options: incidentTypeOptions
        }),
        sort: true
    }, {
        dataField: 'regionDto.name',
        text: 'Region',
        filter: selectFilter({
            options: regionOptions
        }),
        sort: true
    }, {
        dataField: 'userInfoDto',
        formatter: userFormatter,
        filterValue: userFilterValue,
        text: 'Zadal Uživatel',
        filter: selectFilter({
            options: usernameOptions
        }),
        sort: true
    }, {
        dataField: 'userInfoDto.carrierInfoDto.name',
        text: 'Dopravní společnost',
        filter: AuthenticationService.isAdminDS() ? selectFilter({
            options: carrierOptions
        }) : null,
        sort: true
    }, {
        dataField: 'position',
        text: "GPS Poloha",
        formatter: IncidentFormatter.gpsFormatter,
        sort: true
    },
        {
            dataField: 'validityFrom',
            text: 'Začátek incidentu',
            filter: datumFiltr,
            formatter: DateFormatter.formatDateWithTime,
            sort: true
        }, {
            dataField: 'validityTo',
            text: 'Ukončení incidentu',
            filter: datumFiltr,
            formatter: DateFormatter.formatDateWithTime,
            sort: true
        },
        {
            dataField: 'criminalOffense',
            text: 'Trestný čin',
            filter: selectFilter({
                options: booleanOptions
            }),
            formatter: IncidentFormatter.booleanFormatter,
            filterValue: IncidentFormatter.booleanFormatter,
            sort: true
        },
        {
            dataField: 'solvedByPolice',
            text: 'Řešeno policí',
            filter: selectFilter({
                options: booleanOptions
            }),
            formatter: IncidentFormatter.booleanFormatter,
            filterValue: IncidentFormatter.booleanFormatter,
            sort: true
        }];

    const handleOnSelect = (row, _isSelect, _rowIndex, e) => {
        if (!(e.target.cellIndex == null || e.target.cellIndex === 5)) {
            let path = '/incident/' + row.id;
            history.push(path);
        }
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
            <BootstrapTable keyField='id' data={incidents} columns={columns} pagination={paginationFactory()}
                            filter={filterFactory()} selectRow={selectRow} defaultSorted={defaultSorted}
                            hover bootstrap4/>
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