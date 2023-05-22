const baseUrl = "http://192.168.1.113:8080/"
// const baseUrl = "http://127.0.0.1:8080/"

const http = (url, method, data, header) => {
	uni.showLoading({
		title: "加载中"
	})
	return new Promise((resolve, reject) => {
		uni.request({
			url: baseUrl + url,
			method: method || "GET",
			data: JSON.stringify(data),
			dataType: "string",
			header: header || {
				"Authorization": "Bearer " + uni.getStorageSync("current_user")
			}
		}).then(res => {
			//todo 此处是为了解决在后端传过来的serviceId等数据在长度过长时会在js中精度丢失的问题
			const json = res.data.replace(/:s*([0-9]{15,})s*(,?)/g, ': "$1" $2')
			const json1 = json.replace(/:s*([0-9]{15,})s*(,?)/g, ': "$1" $2')
			const resData = JSON.parse(json1)
			const data = resData.data
			const code = resData.code
			const msg = resData.message
			//出现未登录或者token过期直接跳到登录界面
			if (code === 401 || code === 5001 || code === 5002 || code === 5004) {
				//将websocket关闭
				uni.$emit("closeSocket")
				uni.redirectTo({
					url: "/pages/index/login"
				})
			}
			resolve(resData)
		}).catch(e => {
			console.log(e)
			reject(e)
		}).finally(() => {
			uni.hideLoading()
		})
	})
}

export default {
	http,
	baseUrl
}