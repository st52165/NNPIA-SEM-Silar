import './App.css';
import React from "react";
import { BrowserRouter as Router, Link, Redirect, Route, Switch } from "react-router-dom";
import { useAuth } from "./component/AuthContext";
import LoginForm from "./component/LoginForm";
import EmployeeManager from "./component/employee/EmployeeManager";

function App() {

  const { user } = useAuth()

  const nav = user && [
    {
      title: "Home",
      to: "/",
    },
    {
      title: "Employees",
      to: "/employees",
    },
    {
      title: "Profile",
      to: "/profile",
    },
    {
      title: "Logout",
      to: "/logout",
    }
  ]

  const authenticatedRoutes = (<Router>
    <div>
      <nav>
        <ul>
          {nav?.map(navItem => {
            return <li>
              <Link to={navItem.to}>{navItem.title}</Link>
            </li>
          })}
        </ul>
      </nav>

      {/* A <Switch> looks through its children <Route>s and
            renders the first one that matches the current URL. */}
      <Switch>
        <Route exact path="/employees">
          <EmployeeManager/>
        </Route>
        <Route exact path="/profile">
          <Profile/>
        </Route>
        <Route exact path="/Logout">
          <Logout/>
        </Route>
        <Route exact path="/">
          <Home/>
        </Route>
        <Route render={() => <Redirect to="/"/>}/>
      </Switch>
    </div>
  </Router>)

  const nonAuthenticatedRoutes = (<Router>
    <Switch>
      <Route path="/login"><LoginForm/></Route>
      <Route render={() => <Redirect to="/login"/>}/>
    </Switch>
  </Router>)

  return (<>{user ? authenticatedRoutes : nonAuthenticatedRoutes}</>)

}

function Home() {
  return <h2>Home</h2>;
}

function About() {
  return <h2>About</h2>;
}

function Logout() {
  const { removeTokens } = useAuth()
  removeTokens()
  return <Redirect to="/"/>;
}

function Profile() {
  const { user, token } = useAuth()
  return <>
    <h2>Profile</h2>
    <div>
      <strong>User</strong> {JSON.stringify(user)}
    </div>
    <div>
      <strong>Token</strong> {token}
    </div>
  </>;
}

export default App;
