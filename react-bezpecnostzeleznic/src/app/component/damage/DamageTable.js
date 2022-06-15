import BootstrapTable from "react-bootstrap-table-next";
import React, {useEffect, useState} from "react";
import DamageService from "../../service/DamageService";
import {useHistory} from "react-router-dom";
import {Button} from "react-bootstrap";
import AuthenticationService from "../../service/AuthenticationService";

function DamageTable(props) {
    const [damages, setDamages] = useState([]);
    const history = useHistory();
    const columns = [{
        dataField: 'id',
        text: 'ID',
        sort: true
    }, {
        dataField: 'name',
        text: 'Název',
        sort: true
    }, {
        dataField: 'price',
        text: 'Cena (CZK)',
        sort: true
    }];

    useEffect(() => {
        DamageService.getAllDamagesByIncidentId(props.id).then((response) => {
            setDamages(response.data)
        })
    }, [setDamages, props])

    const handleOnSelect = (row) => {
        if (AuthenticationService.isAdminDS()) {
            let path = '/incident/' + props.id + '/damage/' + row.id;
            history.push(path);
        }
    }
    const handleOnBtnClick = () => {
        let path = '/incident/' + props.id + '/damage';
        history.push(path);
    }

    const selectRow = {
        mode: 'checkbox',
        clickToSelect: true,
        hideSelectColumn: true,
        onSelect: handleOnSelect
    };

    return (
        <div>
            {damages.length !== 0
                ? (<BootstrapTable keyField='id' data={damages} columns={columns} hover selectRow={selectRow}
                                   bootstrap4/>)
                : (<p>Doposud nebyla nahlášena žádná škoda.</p>)}
            <br/>
            <Button variant="primary" type="button" onClick={handleOnBtnClick}>Vložit novou škodu</Button>
        </div>
    );
}

export default DamageTable;