import {
    Collapse,
    Nav,
    Navbar,
    NavbarToggler,
    NavbarText,
    NavItem,
    NavLink,
} from "reactstrap";
import {withRouter} from "react-router-dom";
import AuthenticationService from "../../service/AuthenticationService";
import React, {useState, useEffect} from "react";
import {NavDropdown} from "react-bootstrap";

function AppNavbar(props) {
    const [showAdminDS, setShowAdminDS] = useState(false);
    const [showUserDS, setShowUserDS] = useState(false);
    const [userName, setUserName] = useState(undefined);
    const [login, setLogin] = useState(false);
    const [isOpen, setIsOpen] = useState(false);

    useEffect(() => {
        const user = AuthenticationService.getCurrentUser();

        if (user) {
            setShowAdminDS(AuthenticationService.isAdminDS());
            setShowUserDS(true);
            setLogin(true);
            setUserName(user.username);
        }
    }, []);

    const signOut = () => {
        AuthenticationService.signOut();
        props.history.push("/home");
        window.location.reload();
    };

    const toggle = () => {
        setIsOpen(!isOpen);
    };

    return (
        <Navbar color="dark" dark expand="md">
            <Nav className="mr-auto">
                <NavLink href="/#/home">Domů</NavLink>
                {(showUserDS || showAdminDS) && <NavLink href="/#/profile">Uživatel</NavLink>}
                {(showUserDS) && <NavLink href="/#/wagon">Vagóny</NavLink>}
            </Nav>
            {(showUserDS) && <NavDropdown title="Incidenty" id="basic-nav-dropdown">
                <NavDropdown.Item href="/#/incident">Nahlásit incident</NavDropdown.Item>
                <NavDropdown.Item href="/#/incidentTable">Všechny incidenty</NavDropdown.Item>
            </NavDropdown>}
            <NavbarToggler onClick={toggle}/>
            {(showAdminDS) && <NavLink href="/#/signup">Vytvořit účet</NavLink>}
            {(showAdminDS) && (
                <NavLink href="/#/userManage">Správa uživatelů</NavLink>
            )}
            {(showUserDS || showAdminDS) && <NavLink href="/#/setting">Nastavení</NavLink>}
            <Collapse isOpen={isOpen} navbar>
                {login ? (
                    <Nav className="ml-auto" navbar>
                        <NavItem>
                            <NavbarText>
                                Přihlášen jako: <a href="/#/profile">{userName}</a>
                            </NavbarText>
                        </NavItem>
                        <NavItem>
                            <NavLink href="#" onClick={signOut}>
                                Odhlásit se
                            </NavLink>
                        </NavItem>
                    </Nav>
                ) : (
                    <Nav className="ml-auto" navbar>
                        <NavItem>
                            <NavLink href="/#/signin">Přihlásit se</NavLink>
                        </NavItem>
                    </Nav>
                )}
            </Collapse>
        </Navbar>
    );
}

export default withRouter(AppNavbar);