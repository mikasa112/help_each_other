<template>
	<scroll-view>
		<u--text class="item" text="当前服务状态" size="10" type="info"></u--text>
		<u-steps :current="current" class="item">
			<u-steps-item title="已下单" :desc="order.createdAt">
			</u-steps-item>
			<u-steps-item title="正在服务" :desc="order.startAt"></u-steps-item>
			<u-steps-item title="完成服务" :desc="order.endAt"></u-steps-item>
			<u-steps-item title="已付款" desc=""></u-steps-item>
		</u-steps>
		<u--text class="item" v-show="order.pay===0&&order.status===2" text="给个评价" size="10" type="info"></u--text>
		<view class="item" v-show="order.status===2"> <u-rate :readonly="order.pay===1" v-model="heart" size="25"
				class="item" activeIcon="heart-fill" inactiveIcon="heart"></u-rate></view>
		<view class="item" style="margin-top: 30px;">
			<u-button type="primary" v-show="role!=='顾客'" @click="finish(order.orderId)">完成</u-button>
			<u-button :disabled="order.pay===1" type="primary" v-show="order.status===2"
				@click="pay(order.orderId)">支付积分</u-button>
		</view>
	</scroll-view>
</template>

<script>
	export default {
		data() {
			return {
				order: {},
				role: "",
				current: 0,
				heart: 3,
			}
		},
		methods: {
			finish(id) {
				uni.showModal({
					title: "提示",
					content: "您是否已经完成服务?",
					success: async () => {
						let res = await this.$api.finishOrder({
							"orderId": id
						})
						uni.showToast({
							title: "成功",
							success: () => {
								uni.navigateBack()
							}
						})
					}
				})

			},
			async pay(id) {
				let res = await this.$api.payOrder({
					"orderId": id,
					"evaluate": this.heart
				})
				if (res.code === 200) {
					await this.getData(id)
				}
			},
			async getData(id) {
				let res = await this.$api.getOrderById(id)
				this.order = res.data
				console.log(this.order)
				if (this.order.pay === 1) {
					this.current = 3
					this.heart = this.order.evaluate
				} else {
					this.current = this.order.status
				}
			}
		},
		async onLoad(option) {
			await this.getData(option.id)
			this.role = option.role
		}
	}
</script>

<style scoped>
	.item {
		padding: 30rpx 20rpx;
		display: flex;
		justify-content: center;
	}
</style>