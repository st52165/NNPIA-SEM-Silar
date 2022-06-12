import AppNavbar from "../nav/AppNavbar";
import {Link} from "react-router-dom";
import {Button, Container} from "reactstrap";
import {Alert} from "react-bootstrap";
import AuthenticationService from "../../service/AuthenticationService";
import React from "react";

function Home() {
    return (
        <div>
            <AppNavbar/>
            <Container fluid>
                <div style={{marginTop: "20px", maxWidth: "70%", marginLeft: "auto", marginRight: "auto"}}>
                    <Alert variant="primary">
                        <h2>Informační systém pro správu incidentů na železnici</h2>
                        <p>Tato aplikace slouží k zadávání a správě incidentů na železnici.</p>
                        {!AuthenticationService.isUserLogin() && (
                            <Link to="/signin">
                                <Button color="success">Přihlásit se</Button>
                            </Link>
                        )}
                    </Alert>
                </div>
            </Container>
        </div>
    );
}

export default Home;