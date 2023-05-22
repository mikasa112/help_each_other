<template>
	<view class="content">
		<u-search @custom="toSearch" placeholder="服务名称"></u-search>
		<u-swiper :list="swiperList" indicator indicatorMode="line" style="margin-top: 30rpx;"></u-swiper>
		<view class="text"> <u--text type="info" text="常用功能" size="12"></u--text></view>
		<u-scroll-list style="margin: 5rpx;">
			<view v-for="(item,index) in appList" class="scroll-list" :key="index" @click="selectApp(item.path)">
				<image style="width: 100rpx; height: 100rpx;" :src="item.img"></image>
				<text style="font-size: 16rpx;">{{item.name}}</text>
			</view>
		</u-scroll-list>
		<u-divider text="热门服务" :dashed="true"></u-divider>
		<service v-for="(item,index) in services" :key="index" :service="item">
		</service>
		</scroll-view>
	</view>
</template>

<script>
	import service from "@/components/service.vue"
	export default {
		components: {
			service
		},
		data() {
			return {
				services: null,
				value: "3",
				swiperList: [
					'https://cdn.uviewui.com/uview/swiper/swiper1.png',
					'https://cdn.uviewui.com/uview/swiper/swiper2.png',
					'https://cdn.uviewui.com/uview/swiper/swiper3.png',
				],
				appList: [{
						"img": "/static/image/shangjiadianpu.png",
						"name": "全部服务",
						"path": "/pages/service/search"
					},
					{
						"img": "/static/image/zixun.png",
						"name": "养老咨询",
						"path": "/pages/newsletter/index",
					}, {
						"img": "/static/image/wodeqianbao.png",
						"name": "积分服务",
						"path": "/pages/points/points"
					}, {
						"img": "/static/image/shoucangwenjian.png",
						"name": "我的订单",
						"path": "/pages/order/index"
					}, {
						"img": "/static/image/yaoqinghaoyou.png",
						"name": "申请服务",
						"path": "/pages/service/addservice"
					},
					{
						"img": "/static/image/rili.png",
						"name": "应用"
					}
				]
			}
		},
		async onLoad() {
			let res = await this.$api.getHotService()
			this.services = res.data
		},
		methods: {
			selectApp(path) {
				uni.navigateTo({
					url: path,
				})
			},
			toSearch() {
				uni.navigateTo({
					url: "/pages/service/search",
				})
			},
		}
	}
</script>

<style scoped>
	.content {
		padding: 30rpx;
	}

	.scroll-list {
		display: flex;
		flex-flow: column;
		text-align: center;
		margin-right: 40rpx;
	}

	.text {
		margin-top: 20rpx;
		margin-bottom: 20rpx;
	}
</style>