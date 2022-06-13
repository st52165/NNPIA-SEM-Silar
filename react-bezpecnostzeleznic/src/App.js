import './App.css';

import Home from "./app/component/home/Home";
import SignUp from "./app/component/authentication/SignUp";
import Login from "./app/component/authentication/Login";

import {HashRouter, Route, Switch} from "react-router-dom";
import PrivateRoute from './app/component/route/PrivateRoute'
import Profile from './app/component/profile/Profile'
import WagonTable from './app/component/wagon/WagonTable'
import IncidentTable from './app/component/incident/IncidentTable'

function App() {
    return (
        <HashRouter>
            <Switch>
                <Route path="/" exact={true} component={Home}/>
                <Route path="/home" exact={true} component={Home}/>
                <Route path="/signin" exact={true} component={Login}/>
                <Route path="/signup" exact={true} component={SignUp}/>
                <Route path="/wagon" exact={true} component={WagonTable}/>
                <Route path="/incidentTable" exact={true} component={IncidentTable}/>
                {/*<Route path="/incident/:id" exact={true} component={IncidentDetail}/>*/}
                {/*<Route path="/incident" exact={true} component={IncidentForm}/>*/}
                {/*<Route path="/userManage" exact={true} component={UserManage}/>*/}
                {/*<Route path="/userEdit" exact={true} component={UserEdit}/>*/}
                {/*<Route path="/setting" exact={true} component={Setting}/>*/}

                <PrivateRoute path="/profile" exact={true} component={Profile}/>
            </Switch>
        </HashRouter>
    );
}

export default App;
