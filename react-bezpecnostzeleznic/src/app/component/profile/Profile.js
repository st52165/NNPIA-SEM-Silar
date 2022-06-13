import AppNavbar from "../nav/AppNavbar";
import {Container} from "reactstrap";
import {Alert} from "react-bootstrap";
import React, {useState, useEffect} from "react";
import UserService from "../../service/UserService";
import AuthenticationService from "../../service/AuthenticationService";

function Profile() {
    const [username, setUsername] = useState('');
    const [firstname, setFirstname] = useState('');
    const [lastname, setLastname] = useState('');
    const [email, setEmail] = useState('');
    const [roles, setRole] = useState('');
    const [isPending, setIsPending] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        async function fetchData() {
            setIsPending(true);

            UserService.getCurrentUser()
                .then(
                    response => {
                        setUsername(response.data.username);
                        setFirstname(response.data.firstname);
                        setLastname(response.data.lastname);
                        setEmail(response.data.email);
                        setRole(response.data.role);
                        setIsPending(false);
                    },
                    error => (error.data && error.data.message && setError(error.data.message))
                );
        }

        fetchData();
    }, []);


    let userInfo = (
        <div>
            <Alert variant="info">
                <h1>Profile info</h1>
            </Alert>

            <div className="pending">{isPending && "Probíhá načítání dat."}</div>
            {error ? <Alert variant="danger">{error}</Alert>
                : (
                    <div style={{width: "800px", marginLeft: "auto", marginRight: "auto"}}>
                        <h2>Uživatelské jméno: {username}</h2>
                        <h2>Jméno: {firstname}</h2>
                        <h2>Příjmení: {lastname}</h2>
                        <h2>E-mail: {email}</h2>
                        <h2>Role: {roles}</h2>
                    </div>
                )}
        </div>
    );

    return (
        <div>
            <AppNavbar/>
            <Container fluid>{userInfo}</Container>
        </div>
    );
}

export default Profile;