import axios from "axios";

axios.interceptors.request.use((config) => {
    const user = JSON.parse(localStorage.getItem("user"));

    if (user && user.accessToken) {
        config.headers.Authorization = "Bearer " + user.accessToken;
    }

    return config;
});

const SERVER_PREFIX = process.env.REACT_APP_BASE_URI

const UserService = {

    getUser: async function (username) {
        if (username === null) {
            return this.getCurrentUser();
        }

        return this.getUserByUsername(username);
    },

    getCurrentUser: async function () {
        return await axios.get(`${SERVER_PREFIX}/api/user`)
    },

    getUserByUsername: async function (username) {
        return await axios.get(`${SERVER_PREFIX}/api/user/${username}`)
    },

    getUsernames: async function () {
        return await axios.get(`${SERVER_PREFIX}/api/user/names`)
    },

    getUsersList: async function () {
        return await axios.get(`${SERVER_PREFIX}/api/user/list`)
    },

    getExistUsername: async function (username) {
        return await axios.get(`${SERVER_PREFIX}/api/user/name/${username}`)
    }
}

export default UserService;