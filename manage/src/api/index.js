import axios from "axios";
import NProgress from "nprogress"
import 'nprogress/nprogress.css'
import Vue from "vue";

// const baseUrl = "http://192.168.1.114:8080/";
const baseUrl = "http://localhost:8080/";
const Bearer = "Bearer ";

let data = {
    code: 404,
    data: null,
    message: "",
}

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
    const token = localStorage.getItem("token");
    if (token && token !== "") {
        config.headers["Authorization"] = Bearer + token;
    }
    return config;
})

//响应拦截
http.interceptors.response.use(response => {
    NProgress.done();
    data = response.data;
    if (data.code !== 200) {
        //如果状态不为200,打印错误
        Vue.prototype.$message.error(data.message)
        return Promise.reject(data);
    } else {
        return data;
    }
}, error => {
    NProgress.done();
    Vue.prototype.$message.error(error.message)
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

/**
 * 发起delete请求
 * @param url 请求路径
 * @param params 参数
 * @returns {Promise<unknown>}
 */
export function remove(url, params) {
    return new Promise((resolve, reject) => {
        http.delete(url, JSON.stringify(params))
            .then(res => {
                resolve(res.data)
            }).catch(err => {
            reject(err)
        })
    })
}

/**
 * 发起put请求
 * @param url 请求路径
 * @param params 参数
 * @returns {Promise<unknown>}
 */
export function put(url, params) {
    return new Promise((resolve, reject) => {
        http.put(url, JSON.stringify(params))
            .then(res => {
                resolve(res.data)
            }).catch(err => {
            reject(err)
        })
    })
}

const ImageType = ["image/png", "image/jpeg", "image/jpg"]

export function upload(url, file) {
    const fileType = file.type
    let formData = new FormData()
    if (ImageType.includes(fileType)) {
        formData.append("type", 1)
    } else {
        return Promise.reject("文件类型不支持")
    }
    formData.append("file", file)
    const options = {
        url: baseUrl + url,
        method: 'POST',
        headers: {
            'Content-Type': 'multipart/form-data'
        },
        data: formData
    }
    return new Promise((resolve, reject) => {
        axios(options).then(res => {
            resolve(res.data)
        }).catch(err => {
            reject(err)
        })
    })
}