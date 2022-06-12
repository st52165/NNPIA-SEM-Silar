import AuthenticationService from '../../service/AuthenticationService'
import {Redirect, Route} from 'react-router-dom'

const PrivateRoute = ({component: Component, ...rest}) => {

    const isLoggedIn = AuthenticationService.isUserLogin()

    return (
        <Route
            {...rest}
            render={props =>
                isLoggedIn ? (
                    <Component {...props} />
                ) : (
                    <Redirect to={{pathname: '/signin', state: {from: props.location}}}/>
                )
            }
        />
    )
}

export default PrivateRoute