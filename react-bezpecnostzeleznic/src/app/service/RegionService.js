import axios from "axios";

axios.interceptors.request.use((config) => {
    const user = JSON.parse(localStorage.getItem("user"));

    if (user && user.accessToken) {
        const token = "Bearer " + user.accessToken;
        config.headers.Authorization = token;
    }

    return config;
});

const SERVER_PREFIX = process.env.REACT_APP_BASE_URI

const RegionService = {
    getRegions: async function () {
        return await axios.get(`${SERVER_PREFIX}/api/region`)
    },
    getRegionsByName: async function () {
        return await axios.get(`${SERVER_PREFIX}/api/region/name`);
    },
}

export default RegionService;