import {get, post, upload} from "@/api/index";

//登录
export const loginApi = (params) => post("api/auth/login", params)
//获取登录用户的用户信息
export const getCurrentUser = () => get("api/v1/user/index")
//注册
export const registerApi = (params) => post("api/auth/register", params)
//上传图片
export const uploadApi = file => upload("api/upload", file)