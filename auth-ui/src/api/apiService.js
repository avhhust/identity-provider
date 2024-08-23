import axios from "axios";
import { handleErrors } from "./apiErrorHandler";

// export const BASE_URL = 'http://localhost:9000/api';

export const api = axios.create({
    // baseURL: BASE_URL
});

export const get = async (url) => {
    return api
            .get(url)
            .catch(handleErrors);
}

export const post = async (url, body) => {
    return api
            .post(url,body,
                { 
                    headers: { 
                        "Accept": "application/json",
                        "Content-Type": "application/json"
                    },
                }
            )
            .catch(handleErrors);
}

export const register = async (body) => {
    return api
        .post('/api/auth/register', body,{
            headers: { 
                "Accept": "application/json",
                "Content-Type": "application/json"
            },
            withCredentials: true
        })
        .catch(handleErrors);
}

export const login = async (body) => {
    return api
        .post('/api/auth/login', body,{
            headers: { 
            "Accept": "application/json",
            "Content-Type": "application/x-www-form-urlencoded"
            },
            withCredentials: true
        })
        .catch(handleErrors);
}

