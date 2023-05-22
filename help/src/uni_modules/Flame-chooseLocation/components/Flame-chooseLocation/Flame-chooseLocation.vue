<template>
	<view class="loc-choose" v-if="showPop">
		<view class="head-btn">
			<view class="cancel" @click="cancel"><icon type="cancel" size="30" color="#333" /></view>
			<view class="submit">
				<button class="submit-btn" size="mini" type="primary" :disabled="!selectAdress.id" @click="confirm">
					<icon type="success_no_circle" size="20" color="#fff" />
				</button>
			</view>
		</view>
		<view class="map-box">
			<view class="marker"><image src="../static/loc.png" class="loc-icon"></image></view>
			<view id="container"></view>
		</view>
		<view class="search-content">
			<!-- 搜索框 -->
			<view class="search">
				<view class="search-input"><input placeholder="搜索地点" @focus="focusInput" @input="changeSearch" v-model="keyword" /></view>
				<text class="search-cancel" v-if="showCancel" @click="hideCancel">取消</text>
			</view>
			<!-- 搜索列表 -->
			<scroll-view scroll-y="true" class="scroll-Y">
				<view class="search-list">
					<view class="search-list-item" v-for="item in searchList" :key="item.id" @click="selectLoc(item)">
						<view class="list-item-left">
							<view class="">{{ item.title }}</view>
							<view class="font-12">{{ item.address }}</view>
						</view>
						<view class="is-checked" v-if="selectAdress.id == item.id"><icon type="success_no_circle" size="25" color="#007aff" /></view>
					</view>
				</view>
			</scroll-view>
		</view>
	</view>
</template>

<script>
/*
	基于腾讯地图的H5端选择地图组件
	* @property {Boolean} value = [false|true] 控制组件显示或隐藏
	* @property {Boolean} exact = [false|true] 是否开启移动端精确定位
	* @property {String} mapkey = '' 腾讯地图开放平台申请的key
	* @event {Function} confirm 确认选择 ,返回选择的位置信息
*/

let timeout = null
export default {
	components: {},
	mixins: [],
	model: {
		event: 'change'
	},
	props: {
		value: {},
		mapkey: {
			type: String,
			default: ''
		},
		exact: {
			type: Boolean,
			default: false
		}
	},
	data() {
		return {
			keyword: '',
			showPop: false,
			showCancel: false,
			map: {},
			initLoc: [],
			selectAdress: {},
			searchList: []
		}
	},
	computed: {},
	watch: {
		value(val) {
			this.showPop = val
			val && this.initCurrentLoc()
		}
	},
	created() {},
	onLoad(option) {
		// option路由传值接收参数
	},
	onShow() {},
	mounted() {},
	methods: {
		focusInput() {
			this.showCancel = true
		},
		hideCancel() {
			this.showCancel = false
			this.keyword = ''
			this.selectAdress = {}
			const { lat, lng } = this.initLoc[0].location
			this.searchList = this.initLoc
			this.map.setCenter(new TMap.LatLng(lat, lng))
		},
		changeSearch({ detail }) {
			if (!detail.value) {
				this.searchList = []
			} else {
				this.searchLoc({ keyword: encodeURI(detail.value) })
			}
		},
		selectLoc(item) {
			this.selectAdress = item
			const { lat, lng } = item.location
			this.map.setCenter(new TMap.LatLng(lat, lng))
		},
		async requireFunc(data = {}, path = '') {
			const jsonp = function(url, data) {
				return new Promise((resolve, reject) => {
					// 1.初始化url
					let dataString = url.indexOf('?') === -1 ? '?' : '&'
					let callbackName = `jsonpCB_${Date.now()}`
					url += `${dataString}callback=${callbackName}`
					if (data) {
						// 2.有请求参数，依次添加到url
						for (let k in data) {
							url += `&${k}=${data[k]}`
						}
					}
					let scriptNode = document.createElement('script')
					scriptNode.src = url
					// 3. callback
					window[callbackName] = result => {
						result ? resolve(result) : reject('没有返回数据')
						delete window[callbackName]
						document.body.removeChild(scriptNode)
					}
					// 4. 异常情况
					scriptNode.addEventListener(
						'error',
						() => {
							reject('接口返回数据失败')
							delete window[callbackName]
							document.body.removeChild(scriptNode)
						},
						false
					)
					// 5. 开始请求
					document.body.appendChild(scriptNode)
				})
			}
			try {
				const res = await jsonp(`https://apis.map.qq.com${path}`, {
					key: this.mapkey,
					output: 'jsonp',
					...data
				})
				return res
			} catch (e) {
				return e
			}
		},
		// 初始化当前位置
		initCurrentLoc() {
			if (this.exact) {
				// pc端通过IP定位获取当前城市位置，没法精确定位当前位置
				if (uni.getSystemInfoSync().deviceType == 'pc') {
					this.getIpLoc()
				} else {
					// 移动端可以先使用获取当前位置方法，定位失败再使用IP定位
					uni.getLocation({
						type: 'gcj02',
						isHighAccuracy: true,
						success: res => {
							const { longitude, latitude } = res
							let boundary = `nearby(${latitude},${longitude},1000)`
							this.setLoc({ boundary }, 1)
						},
						fail: e => {
							this.getIpLoc()
						}
					})
				}
			} else {
				this.getIpLoc()
			}
		},
		async getIpLoc() {
			const res = await this.requireFunc({}, '/ws/location/v1/ip')
			if (res.status == 0) {
				const {
					location: { lat, lng }
				} = res.result

				let boundary = `nearby(${lat},${lng},1000)`
				this.setLoc({ boundary }, 1)
			}
		},
		async setLoc(data, flag) {
			const params = {
				page_size: 20,
				page_index: 1,
				...data
			}
			const res = await this.requireFunc(params, '/ws/place/v1/search')
			if (res.status == 0) {
				if (res.data && res.data.length) {
					if (flag) {
						this.initLoc = res.data
						this.initMap(res.data[0].location)
						if (this.selectAdress.id) return
					}
					this.searchList = res.data
				}
			}
		},
		async searchLoc(data) {
			const params = {
				page_size: 20,
				page_index: 1,
				...data
			}
			const res = await this.requireFunc(params, '/ws/place/v1/suggestion')
			if (res.status == 0) {
				if (res.data && res.data.length) {
					this.searchList = res.data
				}
			}
		},
		initMap({ lat, lng }) {
			const { id, location } = this.selectAdress
			const center = id ? new TMap.LatLng(location.lat, location.lng) : new TMap.LatLng(lat, lng)
			//初始化地图
			this.map = new TMap.Map('container', {
				viewMode: '2D',
				zoom: 15, //设置地图缩放级别
				center: center, //设置地图中心点坐标
				showControl: false
			})
			this.map.on('dragend', () => {
				this.changeDragend()
			})
			this.map.on('dblclick', () => {
				this.changeDragend()
			})
			this.map.on('bounds_changed', () => {
				this.changeZoom()
			})
		},
		changeZoom() {
			if (timeout !== null) clearTimeout(timeout)
			timeout = setTimeout(() => {
				!Object.keys(this.selectAdress).length && this.changeDragend()
			}, 500)
		},
		// 拖拽结束更新列表
		changeDragend() {
			this.selectAdress = {}
			const info = this.map.getCenter()
			const { lat, lng } = info
			const boundary = `nearby(${lat},${lng},1000)`
			this.setLoc({ boundary })
		},
		cancel() {
			this.$emit('change', false)
		},
		confirm() {
			this.$emit('change', false)
			this.$emit('confirm', this.selectAdress)
		}
	},
	filters: {}
}
</script>
<style lang="scss" scoped>
.font-12 {
	font-size: 24rpx;
}
.loc-choose {
	width: 100%;
	height: 100vh;
	position: fixed;
	top: 0;
	left: 0;
	z-index: 999;
	background-color: #fff;
	view {
		box-sizing: border-box;
	}
	.head-btn {
		width: 100%;
		padding: 30rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		position: fixed;
		top: 0;
		left: 0;
		z-index: 1080;
		.submit-btn {
			display: flex;
			align-items: center;
			justify-content: center;
			padding: 10rpx 36rpx;
		}
	}
	.map-box {
		width: 100%;
		height: 50%;
		position: relative;
		.marker {
			position: absolute;
			top: 50%;
			left: 50%;
			transform: translate(-50%, -50%);
			z-index: 9;
			.loc-icon {
				width: 60rpx;
				height: 60rpx;
			}
		}
		#container {
			width: 100%;
			height: 100%;
		}
	}
	.search-content {
		width: 100%;
		height: 50%;
		background-color: #fff;
		padding: 20rpx 20rpx;
		.search {
			display: flex;
			align-items: center;
			margin-bottom: 24rpx;
		}
		.search-input {
			flex: 1;
			background-color: #ebebeb;
			border-radius: 10rpx;
			padding: 10rpx 20rpx;
			margin-right: 26rpx;
		}
		.search-cancel {
			font-size: 32rpx;
		}
		.scroll-Y {
			height: calc(100% - 34px);
		}
		.search-list {
			.search-list-item {
				display: flex;
				align-items: center;
				padding: 20rpx 0;
				border-bottom: 2rpx solid #d3d3d3;
				&:last-of-type {
					border: none;
				}
				.list-item-left {
					flex: 1;
				}
				.is-checked {
					padding: 0 20rpx;
				}
			}
		}
	}
}

.font-12 {
	font-size: 24rpx;
}
</style>
