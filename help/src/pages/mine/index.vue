<template>
	<view class="">
		<!-- 顶部个人信息 -->
		<view class="bg-white">
			<view class="flex padding">
				<view class="padding-lr-xs">
					<view class="cu-avatar lg round"
						style="background-image:url(https://image.meiye.art/Fha6tqRTIwHtlLW3xuZBJj8ZXSX3?imageMogr2/thumbnail/450x/interlace/1);">
						<u-avatar :src="data.user.avatar"></u-avatar>
					</view>
				</view>
				<view class="padding-xs text-xl text-black">
					<view>Hello，{{data.user.nickname}}</view>
					<view class="cu-tag round line-green sm">{{data.user.email}}</view>
				</view>
			</view>
		</view>
		<!-- 基本数据 -->
		<view class="cu-list grid col-4 no-border padding-lr-xs radius-lg-bottom">
			<view class="cu-item">
				<view class="text-black text-bold text-xxl">
					{{data.user.points}}
				</view>
				<text>积分</text>
			</view>
			<view class="cu-item">
				<view class="text-black text-bold text-xxl">
					{{data.evaluate}}
				</view>
				<text>评价</text>
			</view>
			<view class="cu-item">
				<view class="text-black text-bold text-xxl">
					{{data.services.length}}
				</view>
				<text>拥有服务</text>
			</view>
			<view class="cu-item">
				<view class="text-black text-bold text-xxl">
					{{data.user.age}}
				</view>
				<text>年龄</text>
			</view>
		</view>
		<!-- 助力/推荐/邀请 -->
		<view class="margin-top-sm padding-lr-xs">
			<view class="bg-brown light radius-lg shadow-blur">
				<view class="flex padding-tb-sm padding-lr-sm justify-between">
					<view class="padding-xs">
						<view>9.9元开通超级会员最高可省20积分</view>
					</view>
					<view class="">
						<view class="cu-btn round bg-black">开通会员</view>
					</view>
				</view>
			</view>
		</view>
		<!-- 常用功能 -->
		<view class="cu-bar margin-lr-xs margin-top-sm grid col-4 no-border bg-white radius-lg-top">
			<view class="action">
				<text class="text-xl text-black">我的订单</text>
			</view>
			<view class="action" @click="clickOrder">
				<text class="text-lg">全部订单<text class="cuIcon-right"></text></text>
			</view>
		</view>
		<view class="cu-list grid col-4 no-border text-black margin-lr-xs padding-bottom radius-lg-bottom">
			<view class="cu-item" v-for="(item,index) in iconList" :key="index" :bindtap="item.bindtap">
				<view :class="['cuIcon-'+item.icon,'text-'+item.color,'text-shadow']" style="font-size: 56rpx;">
					<view class="cu-tag badge" v-if="item.badge!=0">
						<block v-if="item.badge!=1">{{item.badge>99?"99+":item.badge}}</block>
					</view>
				</view>
				<text>{{item.name}}</text>
			</view>
		</view>

		<view class="margin-top-sm padding-lr-xs">
			<view class="bg-white light radius-lg shadow-blur">
				<view class="flex padding-tb-sm padding-lr-sm justify-between">
					<view class="padding-xs">
						<view class="text-xl text-black">惊喜连连·洁净一秋</view>
						<view class="padding-top-xs">家居清洗限时<text class="text-red text-bold"> 6.6折 </text><text
								class="cuIcon-roundrightfill text-red"></text></view>
					</view>
					<view class="">
						<view class="cu-btn round bg-gradual-pinknew margin-top-sm">6折优惠</view>
					</view>
				</view>
			</view>
		</view>
		<!-- 其他功能 -->
		<view class="cu-bar margin-lr-xs margin-top grid col-4 no-border bg-white radius-lg-top">
			<view class="action">
				<text class="text-xl">其他功能</text>
			</view>
		</view>
		<view class="cu-list grid col-4 no-border text-black margin-lr-sm padding-bottom radius-lg-bottom">
			<view class="cu-item" v-for="(item,index) in iconOtherList" :key="index" :bindtap="item.bindtap">
				<view :class="['cuIcon-'+item.icon,'text-'+item.color,'text-shadow']" style="font-size: 56rpx;">
					<view class="cu-tag badge" v-if="item.badge!=0">
						<block v-if="item.badge!=1">{{item.badge>99?"99+":item.badge}}</block>
					</view>
				</view>
				<text>{{item.name}}</text>
			</view>
		</view>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				data: null,
				iconList: [{
					icon: 'moneybagfill',
					color: 'blue',
					badge: 0,
					name: '待接单'
				}, {
					icon: 'presentfill',
					color: 'red',
					badge: 0,
					name: '待上门',
					bindtap: "bindZan"
				}, {
					icon: 'formfill',
					color: 'purple',
					badge: 11,
					name: '待评价',
					bindtap: "showResource"
				}, {
					icon: 'shopfill',
					color: 'green',
					badge: 0,
					name: '返修/售后',
					bindtap: "bindPoint"
				}],
				iconOtherList: [{
					icon: 'location',
					color: 'blue',
					badge: 0,
					name: '地址管理'
				}, {
					icon: 'service',
					color: 'blue',
					badge: 0,
					name: '电话客服',
					bindtap: "bindZan"
				}, {
					icon: 'mark',
					color: 'blue',
					badge: 0,
					name: '在线客服',
					bindtap: "showResource"
				}, {
					icon: 'mail',
					color: 'blue',
					badge: 0,
					name: '投诉',
					bindtap: "bindCollect"
				}, {
					icon: 'settings',
					color: 'blue',
					badge: 0,
					name: '设置',
					bindtap: "bindZan"
				}]
			}
		},
		methods: {
			clickOrder() {
				uni.navigateTo({
					url: "/pages/order/index"
				})
			}
		},
		onLoad() {
			let data = this.$store.getters.getUserInfo
			this.data = data
			console.log(this.data)
		}
	}
</script>

<style scoped>
	@import "/static/colorui/main.css";
	@import "/static/colorui/icon.css";
</style>