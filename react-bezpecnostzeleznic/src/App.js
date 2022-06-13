import './App.css';

import Home from "./app/component/home/Home";
import SignUp from "./app/component/authentication/SignUp";
import Login from "./app/component/authentication/Login";

import {HashRouter, Route, Switch} from "react-router-dom";
import PrivateRoute from './app/component/route/PrivateRoute'
import Profile from './app/component/profile/Profile'
import WagonTable from './app/component/wagon/WagonTable'

function App() {
    return (
        <HashRouter>
            <Switch>
                <Route path="/" exact={true} component={Home}/>
                <Route path="/home" exact={true} component={Home}/>
                <Route path="/signin" exact={true} component={Login}/>
                <Route path="/signup" exact={true} component={SignUp}/>
                <Route path="/wagon" exact={true} component={WagonTable}/>

                <PrivateRoute path="/profile" exact={true} component={Profile}/>
            </Switch>
        </HashRouter>
    );
}

export default App;
