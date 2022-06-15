import {Alert, Button, Form} from "react-bootstrap";
import React, {useState, useEffect} from "react";
import DamageService from "../../service/DamageService";
import {useHistory} from "react-router-dom";
import {confirmAlert} from 'react-confirm-alert';
import 'react-confirm-alert/src/react-confirm-alert.css';

function DamageForm(props) {
    const [data, setData] =
        useState({
            "incidentID": props.incidentID,
            "name": '',
            "price": 0
        });

    const [error, setError] = useState(null);
    const history = useHistory();

    useEffect(() => {
        onFormLoad();
    }, []);

    function changeValueHandler(name, value) {
        setData({...data, [name]: value});
    }

    function onFormSubmit(e) {
        e.preventDefault();
        setError(null);

        if (data.name == null || data.name.trim() === '' || data.price <= 0) {
            return false;
        }

        if (props.damageID == null) {
            DamageService.postDamage(data).then(
                response => {
                    props.onChangeValue(response.data);
                }).catch(
                err => setErrorMessage(err));
        } else {
            DamageService.putDamageById(props.damageID, data).then(
                response => {
                    props.onChangeValue(response.data);
                }).catch(
                err => setErrorMessage(err));
        }

        redirect();
    }

    function onDeleteButtonClick(e) {
        e.preventDefault();
        setError(null);

        confirmAlert({
            message: 'Opravdu chcete odstranit škodu č. ' + props.damageID
                + ' patřící incidentu č. ' + props.incidentID + '?',
            buttons: [
                {
                    label: 'Ano',
                    onClick: () => deleteDamage(),
                    style: {backgroundColor: "#dc3545", fontWeight: "bold", color: "white"}
                },
                {
                    label: 'Ne',
                    style: {backgroundColor: "gray", fontWeight: "bold", color: "white"}
                }
            ]
        });
    }

    function deleteDamage() {
        if (props.damageID != null) {
            DamageService.deleteDamageById(props.damageID, data).then(
                response => {
                    props.onChangeValue(response.data);
                }).catch(
                err => setErrorMessage(err));
        }

        redirect();
    }


    function setErrorMessage(err) {
        return err.message && err.response && err.response.data.message
            && setError(err.message.toUpperCase() + ': '
                + err.response.data.message);
    }

    function redirect() {
        let path = '/incident/' + props.incidentID;
        history.push(path);
    }

    function onFormLoad() {
        DamageService.getDamageById(props.damageID).then((response) => {
            setData(response.data);
        });
    }

    return (
        <Form onSubmit={onFormSubmit}>
            <Form.Group controlId="name">
                <Form.Label>Název škody</Form.Label>
                <Form.Control onChange={(e) => {
                    changeValueHandler(e.target.name, e.target.value)
                }} name="name" type="text" defaultValue={data.name} placeholder="Název škody"
                              isInvalid={data.name == null || data.name.trim() === ''}/>
            </Form.Group>

            <Form.Group controlId="price">
                <Form.Label>Způsobená škoda v CZK</Form.Label>
                <Form.Control onChange={(e) => {
                    changeValueHandler(e.target.name, e.target.value)
                }} name="price" type="number" placeholder="Způsobená škoda v CZK"
                              value={data.price} isInvalid={data.price <= 0} aria-valuemin={0}/>
            </Form.Group><br/>
            {error && <Alert variant="danger">{error}</Alert>}
            <Button variant="primary" type="submit">{props.damageID ? 'Upravit' : 'Vložit'}</Button>
            {props.damageID && (
                <Button variant="danger" type="button" style={{marginLeft: '1.5em'}}
                        onClick={onDeleteButtonClick}>Odstranit</Button>)}
        </Form>
    );
}

export default DamageForm;