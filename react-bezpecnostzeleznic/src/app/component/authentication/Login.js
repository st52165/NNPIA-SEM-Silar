import AppNavbar from "../nav/AppNavbar";
import {Container, Form, Alert, FormGroup, Input, Label, Row, Col} from "reactstrap";
import {Button} from "react-bootstrap";
import AuthenticationService from "../../service/AuthenticationService";
import avatar from "../../../avatar.png";
import {useState} from "react";
import "../../../App.css";

function Login(props) {
    const [userName, setUserName] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");


    const handleUserNameInput = (e) => {
        setUserName(e.target.value);
    };

    const handlePasswordInput = (e) => {
        setPassword(e.target.value);
    };

    const doLogin = async (event) => {
        event.preventDefault();

        AuthenticationService.signIn(userName, password).then(
            () => {
                props.history.push("/profile");
            },
            (error) => {
                console.log("Login fail: error = { " + error.toString() + " }");
                setError(
                    "Nepodařilo se přihlásit! Prosím zkontrolujte uživatelské jméno a heslo."
                );
            }
        );
    };

    return (
        <div>
            <AppNavbar/>
            <Container fluid>
                <Row style={{marginTop: "20px"}}>
                    <Col sm="12" md={{size: 3, offset: 4}}>
                        <div style={{marginBottom: "10px"}}>
                            <img
                                src={avatar}
                                alt="Avatar"
                                className="avatar center"
                                style={{width: "50%", height: "auto"}}
                            />
                        </div>
                        <Form onSubmit={doLogin}>
                            <FormGroup>
                                <Label for="username">
                                    <strong>Uživatelské jméno</strong>
                                </Label>
                                <Input
                                    autoFocus
                                    type="text"
                                    name="username"
                                    id="username"
                                    value={userName}
                                    placeholder="Zadejte uživatelské jméno"
                                    autoComplete="username"
                                    onChange={handleUserNameInput}
                                />
                            </FormGroup>

                            <FormGroup>
                                <Label for="password">
                                    <strong>Heslo</strong>
                                </Label>
                                <Input
                                    type="password"
                                    name="password"
                                    id="password"
                                    value={password}
                                    placeholder="Zadejte heslo"
                                    autoComplete="password"
                                    onChange={handlePasswordInput}
                                />
                            </FormGroup>

                            <Button type="submit" variant="primary" size="lg">
                                Přihlásit se
                            </Button>
                            {error && <Alert color="danger">{error}</Alert>}
                        </Form>
                    </Col>
                </Row>
            </Container>
        </div>
    );
}

export default Login;