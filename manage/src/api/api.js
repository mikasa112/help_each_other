import {post} from "@/api/index";

export const loginApi = (params) => post("api/auth/login", params)