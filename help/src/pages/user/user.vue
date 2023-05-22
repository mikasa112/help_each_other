<template>
	<view class="content">
		<u-cell-group>
			<u-cell title="头像">
				<u-avatar slot="value" :size="50" :src="user.avatar"></u-avatar>
			</u-cell>
			<u-cell title="用户名">
				<text slot="value" class="u-slot-value">{{user.username}}</text>
			</u-cell>
			<u-cell title="昵称">
				<text slot="value" class="u-slot-value">{{user.nickname}}</text>
			</u-cell>
			<u-cell title="手机号">
				<text slot="value" class="u-slot-value">{{user.phone}}</text>
			</u-cell>
			<u-cell title="邮箱">
				<text slot="value" class="u-slot-value">{{user.email}}</text>
			</u-cell>
			<u-cell title="注册时间">
				<text slot="value" class="u-slot-value">{{user.createdAt}}</text>
			</u-cell>
			<u-cell title="评价">
				<u-rate slot="value" :value="info.evaluate" readonly></u-rate>
			</u-cell>
		</u-cell-group>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				info: null,
				user: null,
			}
		},
		methods: {
			async getData(uuid) {
				let res = await this.$api.getUserInfo(uuid)
				this.info = res.data
				this.user = this.info.user
				console.log(res)
			}
		},
		onLoad(data) {
			this.getData(data.uuid)
		}
	}
</script>

<style>
	.content {}
</style>