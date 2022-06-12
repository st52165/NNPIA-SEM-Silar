import axios from "axios";

const SERVER_PREFIX = process.env.REACT_APP_BASE_URI

axios.interceptors.request.use((config) => {
    const user = JSON.parse(localStorage.getItem("user"));

    if (user && user.accessToken) {
        config.headers.Authorization = "Bearer " + user.accessToken;
    }

    return config;
});


const CarrierService = {
    getCarriers: async function () {
        return await axios.get(`${SERVER_PREFIX}/api/carriers`);
    }
};

export default CarrierService;