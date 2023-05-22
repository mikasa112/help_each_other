import App from './App'

// #ifndef VUE3
import Vue from 'vue'
import './uni.promisify.adaptor'
import uView from '@/uni_modules/uview-ui'
import api from "@/api/api.js"
import store from "@/store/index.js"
import socket from "@/util/socket.js"
Vue.use(uView)
Vue.config.productionTip = false
Vue.prototype.$api = api
Vue.prototype.socket = new socket()
App.mpType = 'app'
const app = new Vue({
	...App,
	store
})
app.$mount()
// #endif

// #ifdef VUE3
import {
	createSSRApp
} from 'vue'
export function createApp() {
	const app = createSSRApp(App)
	return {
		app
	}
}
// #endif