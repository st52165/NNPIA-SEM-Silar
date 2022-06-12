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

const IncidentService = {
    getAllIncidents: async function () {
        return await axios.get(`${SERVER_PREFIX}/api/incident/all`);
    },
    getAllIncidentsByName: async function (carrierName) {
        return await axios.get(`${SERVER_PREFIX}/api/incident/carrier/${carrierName}`);
    },
    getAllIncidentsByType: async function (incidentType) {
        return await axios.get(`${SERVER_PREFIX}/api/incident/type/${incidentType}`);
    },
    postIncident: async function (data) {
        return await axios.post(`${SERVER_PREFIX}/api/incident`, data);
    },
    getIncidentByIncidentId: async function (id) {
        return await axios.get(`${SERVER_PREFIX}/api/incident/${id}`);
    },

    addWagon: async function (id, data) {
        return await axios.post(`${SERVER_PREFIX}/api/incident/${id}/wagon`, data);
    }
};

export default IncidentService;