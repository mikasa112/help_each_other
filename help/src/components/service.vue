<template>
	<view class="service-item" @click="go">
		<u--image class="service-image" width="90px" height="90px" :fade="true" duration="450" :src="image"
			shape="square" radius="8px">
			<template v-slot:loading>
				<u-loading-icon color="red"></u-loading-icon>
			</template></u--image>
		<view class="service-content">
			<u--text class="service-content-items" bold type="primary" align="center" size="15" :lines="1"
				:text="service.name"></u--text>
			<u--text class="service-content-items" v-show="service.address" type="info" prefixIcon="map-fill"
				align="center" size="10" :lines="1" :text="service.address"></u--text>
			<view class="service-content-items">
				<view class="item">
					<u-icon name="heart-fill" color="red"></u-icon>
					<u--text size="10" :lines="1" :text="service.visited"></u--text>
				</view>
				<view class="item">
					<u-icon name="rmb-circle-fill" color="gold"></u-icon>
					<u--text size="10" bold :lines="1" :text="service.pointsPrice">
					</u--text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: "service",
		props: ["service"],
		data() {
			return {
				image: "",
			};
		},
		methods: {
			go() {
				uni.navigateTo({
					url: "../service/detail?serviceId=" + this.service.serviceId
				})
			}
		},
		mounted() {
			const pictures = this.service.pictures
			this.image = pictures ? pictures.split(";")[0] : ""
		},
	}
</script>

<style scoped>
	.service-item {
		background-color: white;
		box-shadow: 0px 0px 5px 2px rgba(0, 0, 0, 0.1);
		border-radius: 12px;
		background-repeat: no-repeat;
		background-position-y: 9%;
		margin-bottom: 20rpx;
		width: 100%;
		height: 90px;
		display: flex;
		align-items: flex-start;
		flex-direction: row;
	}

	.service-image {}

	.service-content {
		width: 100%;
		height: 100%;
		display: -webkit-flex;
		flex-direction: column;
		flex-wrap: wrap;
	}

	.service-content>.service-content-items {
		padding: 5px;
		display: flex;
		justify-content: space-around;
	}

	.service-content-items {
		display: flex;
		flex-direction: row;
	}

	.item {
		display: flex;
	}
</style>