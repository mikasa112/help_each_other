import {get, post, put, remove, upload} from "@/api/index";

//登录
export const loginApi = (params) => post("api/auth/login", params)
//退出登录
export const logoutApi = () => post("api/auth/logout")
//获取登录用户的用户信息
export const getCurrentUser = () => get("api/v1/user/index")
//获取用户列表
export const getUserList = params => get(`api/v1/user?${params}`)
//获取用户的信息从名字
export const getUserByName = params => get(`api/v1/user/name/${params}`)
//更新用户信息
export const updateUserInfo = (uuid, params) => put(`api/v1/user/${uuid}`, params)
//删除用户从uuid
export const removeUserByUUID = uuid => remove(`api/v1/user/${uuid}`)
//注册
export const registerApi = (params) => post("api/auth/register", params)
//上传图片
export const uploadApi = file => upload("api/upload", file)