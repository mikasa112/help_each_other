import api from "@/api/http.js"

const GET = "GET"
const POST = "POST"

export default {
	baseUrl: () => {
		return api.baseUrl
	},
	login: (data) => {
		return api.http("api/auth/login", POST, data, null)
	},
	register: (data) => {
		return api.http("api/auth/register", POST, data, null)
	},
	getCurrentUser: () => {
		return api.http("api/v1/user/index", GET)
	},
	getCategory: () => {
		return api.http("api/v1/category", GET)
	},
	getServiceByCategory: (id, params) => {
		return api.http(`api/v1/service/category/${id}?${params}`, GET)
	},
	getServiceByName: (name, params) => {
		return api.http(`api/v1/service/name/${name}?${params}`, GET)
	},
	getHotService: () => {
		return api.http("api/v1/service/hot", GET)
	},
	addService: (data) => {
		return api.http("api/v1/service", POST, data)
	},
	getService: (id) => {
		return api.http(`api/v1/service/${id}`, GET)
	},
	takeOrder: (data) => {
		return api.http(`api/v1/order/take`, POST, data)
	},
	confimOrder: (data) => {
		return api.http(`api/v1/order/confim`, POST, data)
	},
	getOrder: (uuid) => {
		return api.http(`api/v1/order/uuid/${uuid}`, GET)
	},
	finishOrder: (data) => {
		return api.http(`api/v1/order/finish`, POST, data)
	},
	payOrder: (data) => {
		return api.http(`api/v1/order/pay`, POST, data)
	},
	getOrderById: (id) => {
		return api.http(`api/v1/order/${id}`, GET)
	},
	getUserInfo: (uuid) => {
		return api.http(`api/v1/user/${uuid}`, GET)
	},
	getNewsletter: (params) => {
		return api.http(`api/v1/newsletter?${params}`, GET)
	},
	getPoints: () => {
		return api.http(`api/v1/points/mine`, GET)
	},
	pubComment: (data) => {
		return api.http(`api/v1/comment`, POST, data)
	},
	getComments: (serviceId, params) => {
		return api.http(`api/v1/comment/${serviceId}?${params}`, GET)
	}
}