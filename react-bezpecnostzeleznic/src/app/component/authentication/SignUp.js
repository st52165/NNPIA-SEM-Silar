import AppNavbar from "../nav/AppNavbar";
import {Container, Button, Form, FormGroup, Input, Label, Row, Col} from "reactstrap";
import {Alert} from "react-bootstrap";

import "./Authentication.css";
import Authentication from "../../service/AuthenticationService";
import React, {useEffect, useState} from "react";
import CarrierService from "../../service/CarrierService";

const validEmailRegex = RegExp(
    //eslint-disable-next-line
    /^(([^<>()\[\].,;:\s@"]+(\.[^<>()\[\].,;:\s@"]+)*)|(".+"))@(([^<>()[\].,;:\s@"]+\.)+[^<>()[\].,;:\s@"]{2,})$/i
);

const validateForm = (errors) => {
    let valid = true;
    Object.values(errors).forEach((val) => val.length > 0 && (valid = false));
    return valid;
};

function SignUp() {

    const [carriers, setCarriers] = useState([]);
    const [userCarrier, setUserCarrier] = useState();
    const [adminAccount, setAdminAccount] = useState(false);
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [userName, setUserName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");
    const [successful, setSuccessful] = useState(false);
    const [validForm, setValidForm] = useState(true);
    const [isPending, setIsPending] = useState(false);
    const [errorFirstName, setErrorFirstName] = useState("");
    const [errorLastName, setErrorLastnName] = useState("");
    const [errorUserName, setErrorUserName] = useState("");
    const [errorEmail, setErrorEmail] = useState("");
    const [errorPassword, setErrorPassoword] = useState("");
    const [errorCarriers, setErrorCarriers] = useState("");

    useEffect(() => {
        setIsPending(true);
        CarrierService.getCarriers().then(response => {
            setCarriers(response.data);

            if (response.data.length > 0) {
                setUserCarrier(response.data[0].name);
            } else {
                setErrorCarriers("Nelze vytvořit účet! Nejprve je potřeba vytvořit dopravní společnost!")
            }

            setIsPending(false);
        });
    }, [setCarriers]);

    const handleFirstNameInput = (e) => {
        setFirstName(e.target.value);
        setErrorFirstName(
            e.target.value.length < 3 ? "Jméno musí mít minimálně 3 znaky!" : ""
        );
    };

    const handleLastNameInput = (e) => {
        setLastName(e.target.value);
        setErrorLastnName(
            e.target.value.length < 3 ? "Příjmení musí mít minimálně 3 znaky!" : ""
        );
    };

    const handleUserNameInput = (e) => {
        setUserName(e.target.value);
        setErrorUserName(
            e.target.value.length < 5 ? "Uživatelské jméno musí mít minimálně 5 znaků!" : ""
        );
    };

    const handleEmailInput = (e) => {
        setEmail(e.target.value);
        setErrorEmail(
            validEmailRegex.test(e.target.value) ? "" : "E-mail není validní!"
        );
    };

    const handlePasswordInput = (e) => {
        setPassword(e.target.value);
        setErrorPassoword(
            e.target.value.length < 6 ? "Heslo musí být minimálně 6 znaků dlouhé!" : ""
        );
    };

    const signUp = (e) => {
        e.preventDefault();

        let errors = {
            firstname: errorFirstName,
            lastName: errorLastName,
            userName: errorUserName,
            email: errorEmail,
            password: errorPassword,
            carrier: errorCarriers
        };
        const valid = validateForm(errors);

        setValidForm(valid);
        if (valid) {
            Authentication.register(
                firstName,
                lastName,
                userName,
                email,
                password,
                adminAccount,
                userCarrier
            ).then(
                (response) => {
                    setMessage(response.data.message);
                    setSuccessful(true);
                },
                (error) => {
                    setMessage(error.toString());
                    setSuccessful(false);
                }
            );
        }
    };

    const title = <h2>Vytvořit nový účet</h2>;
    let alert = "";

    if (message) {
        if (successful) {
            alert = <Alert variant="success">{message}</Alert>;
        } else {
            alert = <Alert variant="danger">{message}</Alert>;
        }
    }

    return (
        <div>
            <AppNavbar/>
            <Container fluid>
                <Row style={{marginTop: "20px"}}>
                    <Col sm="12" md={{size: 4, offset: 4}}>
                        {title}
                        <div className="pending">{isPending && "Probíhá načítání dat."}</div>
                        <Form onSubmit={signUp}>
                            <FormGroup controlid="forFirstname">
                                <Label for="firstname">Jméno</Label>
                                <Input
                                    required
                                    type="text"
                                    placeholder="Zadejte jméno"
                                    name="firstname"
                                    id="firstname"
                                    value={firstName}
                                    autoComplete="firstname"
                                    onChange={handleFirstNameInput}
                                />
                                {errorFirstName && (
                                    <Alert variant="danger">{errorFirstName}</Alert>
                                )}
                            </FormGroup>

                            <FormGroup controld="forLastname">
                                <Label for="lastname">Příjmení</Label>
                                <Input
                                    required
                                    type="text"
                                    placeholder="Zadejte příjmení"
                                    name="lastname"
                                    id="lastname"
                                    value={lastName}
                                    autoComplete="lastname"
                                    onChange={handleLastNameInput}
                                />
                                {errorLastName && <Alert variant="danger">{errorLastName}</Alert>}
                            </FormGroup>

                            <FormGroup controlid="forUsername">
                                <Label for="username">Uživatelské jméno</Label>
                                <Input
                                    required
                                    type="text"
                                    placeholder="Zadejte uživatelské jméno"
                                    name="username"
                                    id="username"
                                    value={userName}
                                    autoComplete="username"
                                    onChange={handleUserNameInput}
                                />
                                {errorUserName && <Alert variant="danger">{errorUserName}</Alert>}
                            </FormGroup>

                            <FormGroup controlid="formEmail">
                                <Label for="email">E-mail</Label>
                                <Input
                                    required
                                    type="text"
                                    placeholder="Zadejte e-mail"
                                    name="email"
                                    id="email"
                                    value={email}
                                    autoComplete="email"
                                    onChange={handleEmailInput}
                                />
                                {errorEmail && <Alert variant="danger">{errorEmail}</Alert>}
                            </FormGroup>

                            <FormGroup controlid="formPassword">
                                <Label for="password">Heslo</Label>
                                <Input
                                    required
                                    type="password"
                                    placeholder="Zadejte heslo"
                                    name="password"
                                    id="password"
                                    value={password}
                                    autoComplete="password"
                                    onChange={handlePasswordInput}
                                />
                                {errorPassword && (
                                    <Alert variant="danger">
                                        {errorPassword}
                                    </Alert>
                                )}
                            </FormGroup>
                            {(Authentication.isAdminDS()) &&
                                <FormGroup controlId="formCarrier" class="md-form">
                                    <Label for="carrier" class="form-control timepicker">Dopravní společnost: </Label>
                                    <div style={{display: "flex"}}>
                                        <select name="carrier" id="carrier" style={{width: "200px", padding: "10px"}}
                                                onChange={(e) => {
                                                    setUserCarrier(e.target.value);
                                                }}>
                                            {carriers.map((carrier) => (
                                                <option value={carrier.name}>{carrier.name}</option>
                                            ))}
                                        </select>
                                    </div>

                                    {errorCarriers && (
                                        <Alert variant="danger">
                                            {errorCarriers}
                                        </Alert>
                                    )}
                                </FormGroup>}

                            <h6>Typ účtu:
                                <Input type="radio" id="adminAccount" name="gender" value="adminAccount"
                                       checked={adminAccount} onChange={() => setAdminAccount(!adminAccount)}/>
                                <Label className="label-for-radio" htmlFor="adminAccount">Administrátor</Label>
                                <Input type="radio" id="notAdminAccount" name="gender" value="notAdminAccount"
                                       checked={!adminAccount} onChange={() => setAdminAccount(!adminAccount)}
                                />
                                <Label className="label-for-radio" htmlFor="notAdminAccount">Uživatel</Label>
                            </h6>

                            <Button variant="primary" type="submit">
                                Vytvořit účet
                            </Button>
                            {!validForm && (
                                <Alert key="validForm" variant="danger">
                                    Prosím zkontrolujte zadané hodnoty!
                                </Alert>
                            )}
                            {alert}
                        </Form>
                    </Col>
                </Row>
            </Container>
        </div>
    );
}

export default SignUp;