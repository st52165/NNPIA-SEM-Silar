import BootstrapTable from 'react-bootstrap-table-next';
import AppNavbar from "../nav/AppNavbar";
import {Container} from "reactstrap";
import React, {useEffect, useState} from "react";
import {Alert, Pagination} from "react-bootstrap";
import AuthenticationService from "../../service/AuthenticationService";
import CarrierService from "../../service/CarrierService";
import WagonService from "../../service/WagonService";
import filterFactory, {selectFilter, textFilter} from "react-bootstrap-table2-filter";


function WagonTable() {
    const [wagons, setWagons] = useState([]);
    const [wagonTypes, setWagonTypes] = useState([]);
    const [carriers, setCarriers] = useState([]);
    const [isAdmin, setAdmin] = useState(false);
    const [pageNumber, setPageNumber] = useState(1);
    const [pageSize, setPageSize] = useState(10);
    const [sortBy, setSortBy] = useState("id");
    const [direction, setDirection] = useState("asc");

    useEffect(() => {
        WagonService.getAllWagons(pageNumber, pageSize, sortBy, direction).then((response) => {
            setWagons(response.data);
        });
    }, [setWagons, pageNumber, pageSize, sortBy, direction]);

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

    let onSortFunction = (field, newDirection) => {
        setDirection(newDirection)
        setSortBy(field);
    }
    let sortFunction = null;

    const columns = [{
        dataField: 'id',
        text: 'ID',
        filter: textFilter(),
        sort: true,
        onSort: onSortFunction,
        sortFunc: sortFunction,
        isKey: true
    }, {
        dataField: 'wagonType',
        text: 'Typ vagónu',
        filter: selectFilter({
            options: wagonTypesOptions
        }),
        sort: true,
        onSort: onSortFunction,
        sortFunc: sortFunction
    }, {
        dataField: 'carrierDto.name',
        text: 'Dopravce',
        filter: isAdmin ? selectFilter({
            options: carrierOptions
        }) : null,
        sort: true,
        onSort: (_field, newDirection) => onSortFunction('carrier.name', newDirection),
        sortFunc: sortFunction
    }, {
        dataField: 'length',
        text: 'Délka',
        sort: true,
        direction: direction
    }, {
        dataField: 'weight',
        text: 'Váha',
        sort: true,
        onSort: onSortFunction,
        sortFunc: sortFunction
    }];

    const onSizePerPageChange = (e) => {
        setPageSize(e.target.value);
        setPageNumber(1);
    };

    let dataTable = (
            <div>
                <Alert variant="info">
                    <h1>Vagóny</h1>
                </Alert>
                <br/>
                <BootstrapTable keyField='id' data={wagons} columns={columns} filter={filterFactory()}
                                hover bootstrap4 label/>

                <div style={{display: "flex", justifyContent: "flex-start", gap: "22.5em"}}>
                    <select className=" btn dropdown-toggle react-bs-table-sizePerPage-dropdown dropdown"
                            onChange={onSizePerPageChange}
                            style={{color: "white", backgroundColor: "#6c757d", maxWidth: "62px", maxHeight: "37px"}}>
                        <option value="10">10</option>
                        <option value="20">20</option>
                        <option value="30">30</option>
                        <option value="40">40</option>
                        <option value="50">50</option>
                    </select>

                    <Pagination style={{display: "flex", justifyContent: "center"}}>
                        <Pagination.First onClick={() => {
                            pageNumber > 1 && setPageNumber(pageNumber - 1)
                        }}/>
                        <Pagination.Item active>{pageNumber}</Pagination.Item>
                        <Pagination.Last onClick={() => {
                            wagons.length >= pageSize && setPageNumber(pageNumber + 1)
                        }}/>
                    </Pagination>
                </div>
            </div>
        )
    ;

    return (
        <div>
            <AppNavbar/>
            <Container fluid>{dataTable}</Container>
        </div>
    );
}

export default WagonTable;