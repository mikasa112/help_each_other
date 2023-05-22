import Vue from "vue";
import Vuex from "vuex"

Vue.use(Vuex)

const state = {
	userInfo: {},
	orders: [],
}

const mutations = {
	setUserInfo(context, val) {
		context.userInfo = val
	},
	addOrder(context, val) {
		context.orders.push(val)
	},
	clearOrder(context) {
		context.orders = []
	}
}

const actions = {}

const getters = {
	getUserInfo(context) {
		return context.userInfo
	},
	getOrders(context) {
		return context.orders
	}
}


export default new Vuex.Store({
	state,
	mutations,
	actions,
	getters
})