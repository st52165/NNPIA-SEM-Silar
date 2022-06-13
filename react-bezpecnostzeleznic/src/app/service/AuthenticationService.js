import axios from "axios";

const SERVER_PREFIX = process.env.REACT_APP_BASE_URI

const AuthenticationService = {

    signIn: function (username, password) {
        return axios
            .post(`${SERVER_PREFIX}/api/authentication/signin`, {username, password})
            .then((response) => {
                if (response.data.accessToken) {
                    localStorage.setItem("user", JSON.stringify(response.data));

                    const token = this.getCurrentUser().accessToken;
                    localStorage.setItem("accessToken", token);
                }
                return response.data;
            })
            .catch((error) => {
                throw error.response;
            });
    },

    signOut: function () {
        localStorage.removeItem("user");
    },

    register: function async(firstname, lastname, username, email, password, adminAccount, carrier) {
        const role = adminAccount ? "admin_ds" : "user_ds";

        return axios.post(`${SERVER_PREFIX}/api/authentication/signup`, {
            firstname,
            lastname,
            username,
            email,
            password,
            role,
            carrier
        })
            .catch((error) => {
                throw error.response.data.message;
            });
    },

    getCurrentUser: function () {
        return JSON.parse(localStorage.getItem("user"));
    },

    isUserLogin: function () {
        return !!localStorage.getItem("user");
    },

    getUsername: function () {
        return this.getCurrentUser().username;
    },

    getAuthorities: function () {
        return this.getCurrentUser().authorities;
    },

    getAuthoritiesArray: function () {
        return this.getAuthorities().map((authority) => authority.authority)
    },

    isUserDS: function () {
        return this.getAuthoritiesArray().includes("ROLE_USER_DS");
    },

    isAdminDS: function () {
        return this.getAuthoritiesArray().includes("ROLE_ADMIN_DS");
    }
};


export default AuthenticationService;
