import AuthenticationService from '../../service/AuthenticationService'
import {Redirect, Route} from 'react-router-dom'

const PrivateRoute = ({component: Component, ...rest}) => {

    const isAdminLoggedIn = AuthenticationService.isAdminDS()

    return (
        <Route
            {...rest}
            render={props =>
                isAdminLoggedIn ? (
                    <Component {...props} />
                ) : (
                    <Redirect to={{pathname: '/signin', state: {from: props.location}}}/>
                )
            }
        />
    )
}

export default PrivateRoute