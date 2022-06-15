import React from "react";
import DamageForm from "./DamageForm";
import {Container} from "react-bootstrap";
import AppNavbar from "../nav/AppNavbar";
import {useParams} from "react-router-dom";

function DamageEdit() {
    const {incidentID, damageID} = useParams();

    return (
        <div>
            <AppNavbar/>
            <Container fluid="md">
                <h3>{damageID == null ? 'Přidat novou škodu k' : 'Upravit škodu č. ' + damageID + ' u '} incidentu
                    č. {incidentID}</h3>
                <DamageForm incidentID={incidentID} damageID={damageID}/>
            </Container>
        </div>
    );
}

export default DamageEdit;