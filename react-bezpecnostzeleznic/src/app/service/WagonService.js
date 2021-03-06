import axios from "axios";

axios.interceptors.request.use((config) => {
    const user = JSON.parse(localStorage.getItem("user"));

    if (user && user.accessToken) {
        config.headers.Authorization = "Bearer " + user.accessToken;
    }

    return config;
});

const SERVER_PREFIX = process.env.REACT_APP_BASE_URI

const WagonService = {
    getWagonsByCarrier: async function () {
        return await axios.get(`${SERVER_PREFIX}/api/wagons/carrier`);
    },
    getAllWagons: async function (pageNumber = 1, pageSize = 10, sortBy = 'id', direction = 'asc') {
        return await axios.get(`${SERVER_PREFIX}/api/wagons?pageNumber=${pageNumber}&pageSize=${pageSize}&sortBy=${sortBy}&direction=${direction}`);
    },
    getAllWagonTypes: async function () {
        return await axios.get(`${SERVER_PREFIX}/api/wagons/type`);
    },
    updateWagons: async function () {
        return await axios.get(`${SERVER_PREFIX}/api/wagons/updateWagons`);
    }
};

export default WagonService;