<template>
	<view>
		<view v-for="(item,index) in orders" :key="index" class="content" @click="goToDetail(item.orderId,item.role)">
			<view class="item">
				<view class="text"> <u--text size="10" type="info" :text="'订单号：'+item.orderId"></u--text></view>
				<view class="text"> <u--text size="10" type="info" :text="'服务开始时间：'+item.createdAt"></u--text></view>
				<view class="text" id="container">
					<u-tag icon="bookmark-fill" plainFill style="margin-right: 20rpx;" shape="circle" :text="item.role"
						plain size="mini" :type="item.role==='顾客'?'primary':'warning'"></u-tag>
				</view>
			</view>
			<view id="container">
				<u-tag v-if="item.pay===0" plainFill icon="car" :type="item.status===1?'primary':'warning'"
					style="margin-right: 20rpx;" shape="circle" :text="item.status===1?'正在进行中':'已完成'" plain
					size="mini"></u-tag>
				<u-tag v-else="item.pay===1" plainFill icon="rmb" type="success" style="margin-right: 20rpx;"
					shape="circle" text="已支付" plain size="mini"></u-tag>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: "Order",
		props: ["orders", "uuid"],
		data() {
			return {};
		},
		methods: {
			goToDetail(id, role) {
				uni.navigateTo({
					url: `/pages/order/detail?id=${id}&role=${role}`
				})
			}
		},
		created() {}
	}
</script>

<style scoped>
	.content {
		background-color: white;
		box-shadow: 0px 0px 5px 2px rgba(0, 0, 0, 0.1);
		border-radius: 12px;
		background-repeat: no-repeat;
		background-position-y: 9%;
		margin-bottom: 20rpx;
		width: 100%;
		height: 80px;
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
	}

	.item {
		display: flex;
		align-items: flex-start;
		align-content: center;
		flex-direction: column;
	}

	.text {
		padding-left: 20rpx;
		padding-top: 15rpx;
	}

	#container {
		display: flex;
	}
</style>