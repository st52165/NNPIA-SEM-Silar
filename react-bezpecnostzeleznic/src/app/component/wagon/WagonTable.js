import BootstrapTable from 'react-bootstrap-table-next';
import AppNavbar from "../nav/AppNavbar";
import {Container} from "reactstrap";
import React, {useEffect, useState} from "react";
import {Alert} from "react-bootstrap";
import AuthenticationService from "../../service/AuthenticationService";
import CarrierService from "../../service/CarrierService";
import WagonService from "../../service/WagonService";
import filterFactory, {selectFilter, textFilter} from "react-bootstrap-table2-filter";
import paginationFactory from "react-bootstrap-table2-paginator";


function WagonTable() {
    const [wagons, setWagons] = useState([]);
    const [wagonTypes, setWagonTypes] = useState([]);
    const [carriers, setCarriers] = useState([]);
    const [isAdmin, setAdmin] = useState(false);

    useEffect(() => {
        WagonService.getAllWagons().then((response) => {
            setWagons(response.data);
        });
    }, [setWagons]);

    useEffect(() => {
        const authorities = AuthenticationService.getAuthorities();
        const admin = authorities.find(it => it.authority === 'ROLE_ADMIN_DS')
        if (admin != null) {
            setAdmin(true);
            CarrierService.getCarriers().then(response => {
                setCarriers(response.data);
            });
        } else {
            setCarriers([]);
            setAdmin(false);
        }
    }, [setCarriers]);

    useEffect(() => {
        WagonService.getAllWagonTypes().then((response) => {
            setWagonTypes(response.data);
        });
    }, [setWagonTypes]);


    const carrierOptions = carriers.map(carrier => (
        {
            value: carrier.name,
            label: carrier.name
        }
    ));

    const wagonTypesOptions = wagonTypes.map(wagonType => (
        {
            value: wagonType.name,
            label: wagonType.name
        }
    ));

    const columns = [{
        dataField: 'id',
        text: 'ID',
        filter: textFilter(),
        sort: true,
        isKey: true
    }, {
        dataField: 'wagonType',
        text: 'Typ vagónu',
        filter: selectFilter({
            options: () => wagonTypesOptions
        }),
        sort: true
    }, {
        dataField: 'carrierDto.name',
        text: 'Dopravce',
        filter: isAdmin ? selectFilter({
            options: () => carrierOptions
        }) : null,
        sort: true
    }, {
        dataField: 'length',
        text: 'Délka',
        sort: true
    }, {
        dataField: 'weight',
        text: 'Váha',
        sort: true
    }];

    const defaultSorted = [{
        dataField: 'id',
        order: 'asc'
    }];

    let dataTable = (
        <div>
            <Alert variant="info">
                <h1>Vagóny</h1>
            </Alert>
            <br/>
            <BootstrapTable keyField='id' data={wagons} columns={columns} pagination={paginationFactory()}
                            filter={filterFactory()} defaultSorted={defaultSorted} hover bootstrap4/>
        </div>
    );

    return (
        <div>
            <AppNavbar/>
            <Container fluid>{dataTable}</Container>
        </div>
    );
}

export default WagonTable;