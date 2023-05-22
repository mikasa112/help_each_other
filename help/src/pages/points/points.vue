<template>
	<scroll-view>
		<u-collapse accordion v-for="(item,index) in data" :key="index">
			<u-collapse-item :title="item.createdAt">
				<u-icon name="clock" size="20" slot="icon"></u-icon>
				<view class="item">
					<u-tag style="margin-right: 20px;" :text="item.remark"
						:type="item.remark==='系统扣除'?'error':'success'" plain plainFill></u-tag>
					<u--text bold suffixIcon="rmb-circle" iconStyle="font-size: 19px"
						:type="item.remark==='系统扣除'?'error':'success'"
						:text="item.record>0?'+'+item.record:item.record"></u--text>
					<u--text iconStyle="font-size: 19px" type="info" :text="item.orderId"></u--text>
				</view>
			</u-collapse-item>
		</u-collapse>
	</scroll-view>
</template>

<script>
	export default {
		data() {
			return {
				data: null
			}
		},
		methods: {
			async getData() {
				let res = await this.$api.getPoints()
				this.data = res.data
				console.log(this.data)
			}
		},
		onLoad() {
			this.getData()
		}
	}
</script>

<style scoped>
	.item {
		display: flex;
		flex-direction: row;
	}
</style>