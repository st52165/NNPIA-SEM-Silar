import axios from "axios";

axios.interceptors.request.use((config) => {
    const user = JSON.parse(localStorage.getItem("user"));

    if (user && user.accessToken) {
        config.headers.Authorization = "Bearer " + user.accessToken;
    }

    return config;
});

const SERVER_PREFIX = process.env.REACT_APP_BASE_URI

const DamageService = {
    postDamage: async function (data) {
        return await axios.post(`${SERVER_PREFIX}/api/damage`, data);
    },

    getAllDamages: async function () {
        return await axios.get(`${SERVER_PREFIX}/api/damage`);
    },

    getDamageById: async function (id) {
        return await axios.get(`${SERVER_PREFIX}/api/damage/${id}`);
    },
    putDamageById: async function (id, data) {
        return await axios.put(`${SERVER_PREFIX}/api/damage/${id}`, data);
    },
    deleteDamageById: async function (id, data) {
        return await axios.delete(`${SERVER_PREFIX}/api/damage/${id}`, data);
    },
    getAllDamagesByIncidentId: async function (id) {
        return await axios.get(`${SERVER_PREFIX}/api/damage/incident/${id}`);
    }
}
export default DamageService;