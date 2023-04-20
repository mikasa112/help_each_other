import axios from "axios";
import NProgress from "nprogress"
import 'nprogress/nprogress.css'

const baseUrl = "http://127.0.0.1:8080/";
const Bearer = "Bearer ";

export const http = axios.create({
    timeout: 12000,
    baseURL: baseUrl,
    headers: {
        "Content-Type": "application/json;charset=UTF-8"
    }
})

//请求拦截
http.interceptors.request.use(config => {
    NProgress.start();
    const token = sessionStorage.getItem("token");
    if (token && token !== "") {
        config.headers["Authorization"] = Bearer + token;
    }
    return config;
})

http.interceptors.response.use(response => {
    NProgress.done();
    return response;
}, error => {
    NProgress.done();
    return Promise.reject(error)
})

/**
 * 发起post请求
 * @param url 请求路径
 * @param params 请求参数
 * @returns {Promise<unknown>}
 */
export function post(url, params) {
    return new Promise((resolve, reject) => {
        http.post(url, JSON.stringify(params))
            .then(res => {
                resolve(res.data)
            }).catch(err => {
            reject(err)
        })
    })
}

/**
 * 发起get请求
 * @param url 请求路径
 * @returns {Promise<unknown>}
 */
export function get(url) {
    return new Promise((resolve, reject) => {
        http.get(url).then(res => {
            resolve(res.data)
        }).catch(err => {
            reject(err)
        })
    })
}