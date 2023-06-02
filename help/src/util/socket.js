class socket {
	Connect = 0
	PING = 1

	constructor() {
		this.socketTask = null
		// this.url = "ws://127.0.0.1:8082"
		this.url = "ws://123.60.140.200:8082"
		//是否关闭
		this.isAlive = false
		this.heartbeatInterval = null
		this.reconnectTimeOut = null
		//重连次数
		this.connectNum = 1
		this.uuid = null
	}

	init(uuid) {
		if (this.uuid === null || this.uuid === "") {
			this.uuid = uuid
		}
		this.socketTask = uni.connectSocket({
			url: this.url,
			success() {
				console.log("连接中---------->")
				return this.socketTask
			}
		});
		this.socketTask.onOpen(res => {
			console.log("连接成功")
			this.connectNum = 1
			clearInterval(this.heartbeatInterval)
			clearInterval(this.reconnectTimeOut)
			this.isAlive = true
			this.send(this.Connect, this.uuid)
			//心跳
			this.heartbeatInterval = setInterval(() => {
				this.send(this.PING)
			}, 30000)
			this.socketTask.onMessage((e) => {
				if (e.data !== "PONG") {
					const json = e.data.replace(/:s*([0-9]{15,})s*(,?)/g, ': "$1" $2')
					const json1 = json.replace(/:s*([0-9]{15,})s*(,?)/g, ': "$1" $2')
					const order = JSON.parse(json1)
					if (order.status === 0) {
						uni.$emit("order", order)
					} else if (order.status === 1) {
						uni.showModal({
							title: '申请已通过',
							content: `订单${order.orderId}接单申请已通过!`,
							showCancel: false,
						});
					} else if (order.status === 2) {
						uni.showModal({
							title: '您的订单已完成!',
							content: `订单${order.orderId}已完成!`,
							showCancel: false,
						});
					}
				}
			})
		})
		uni.onSocketError(res => {
			console.log("连接异常")
			this.socketTask = null
			clearInterval(this.heartbeatInterval)
			clearInterval(this.reconnectTimeOut)
			if (this.connectNum < 6) {
				uni.showToast({
					title: `WebSocket连接失败，正尝试第${this.connectNum}次连接`,
					icon: "none"
				})
				this.reconnect();
				this.connectNum += 1
			} else {
				this.connectNum = 1
			}

		})

		this.socketTask.onClose(() => {
			clearInterval(this.heartbeatInterval)
			clearInterval(this.reconnectTimeOut)
			this.socketTask = null
			if (this.connectNum < 6) {
				this.reconnect();
			} else {
				this.connectNum = 1
			}

		})
	}

	send(type, uuid, msg) {
		let data = {
			type: type,
			userType: 0,
			uuid: uuid,
			msg: msg
		}
		if (this.socketTask) {
			this.socketTask.send({
				data: JSON.stringify(data),
				success() {
					console.log("send---------->", JSON.stringify(data))
				}
			})
		}
	}

	reconnect() {
		clearInterval(this.heartbeatInterval)
		if (this.isAlive) {
			this.reconnectTimeOut = setInterval(() => {
				this.init(this.uuid)
			}, 10000)
		}
	}

	close() {
		this.isAlive = false
		this.socketTask.close({
			success() {
				uni.showToast({
					title: 'SocketTask 关闭成功',
					icon: "none"
				});
			}
		})
	}
}

module.exports = socket