<template>
	<view class="content">
		<u-subsection :list="list" :current="curNow" @change="sectionChange"></u-subsection>
		<u-transition :show="true" mode="slide-down">
			<view v-if="curNow===0">
				<ConfirmOrder></ConfirmOrder>
			</view>
			<view v-else-if="curNow===1">
				<Order :orders="waitOrders" :uuid="uuid"></Order>
			</view>
			<view v-else-if="curNow===2">
				<Order :orders="finishOrders" :uuid="uuid"></Order>
			</view>
		</u-transition>
	</view>
</template>

<script>
	import ConfirmOrder from "@/components/ConfirmOrder.vue"
	import Order from "@/components/Order.vue"
	export default {
		components: {
			ConfirmOrder,
			Order
		},
		data() {
			return {
				list: ['待确认', '待服务', '支付'],
				curNow: 0,
				waitOrders: null,
				finishOrders: null,
				uuid: "",
			}
		},
		async onShow() {
			let user = this.$store.getters.getUserInfo.user
			this.uuid = user.uuid
			let res = await this.$api.getOrder(user.uuid)
			const data = res.data.list.map(item => {
				if (item.customerUuid === this.uuid) {
					item["role"] = "顾客"
				} else if (item.providerUuid === this.uuid) {
					item["role"] = "服务提供者"
				}
				return item
			})
			this.waitOrders = data.filter(item => {
				return item.status === 1;
			})
			this.finishOrders = data.filter(item => {
				return item.status === 2 && item.customerUuid === this.uuid;
			})
		},
		methods: {
			sectionChange(index) {
				this.curNow = index;
			}
		},
	}
</script>

<style scoped>
	.content {
		padding: 30rpx;
	}
</style>