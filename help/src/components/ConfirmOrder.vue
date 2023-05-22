<template>
	<view>
		<u-collapse accordion v-for="(item,index) in orders" :key="index">
			<u-collapse-item isLink :title="`订单 ${item.orderId}`" :name="item.providerUuid">
				<u-icon name="tags-fill" size="20" slot="icon"></u-icon>
				<view class="button-content">
					<u-button @click="goUserInfo(item.providerUuid)" hairline plain type="primary" size="mini"
						text="查看用户信息"></u-button>
					<u-button style="margin-left: 200rpx;" @click="choise(item.orderId)" hairline plain type="primary"
						size="mini" text="选择"></u-button>
				</view>
			</u-collapse-item>
		</u-collapse>
	</view>
</template>

<script>
	export default {
		name: "ConfirmOrder",
		data() {
			return {
				orders: [],
			};
		},
		methods: {
			goUserInfo(uuid) {
				uni.navigateTo({
					url: `/pages/user/user?uuid=${uuid}`
				})
			},
			async choise(orderId) {
				let res = await this.$api.confimOrder({
					"orderId": orderId
				})
				if (res.code === 200) {
					uni.showToast({
						title: "确认成功"
					})
					this.$store.commit("clearOrder")
					this.orders = []
				}
			}
		},
		created() {
			this.orders = this.$store.getters.getOrders
		}
	}
</script>

<style>
	.button-content {
		display: flex;
		flex-direction: row;
	}
</style>