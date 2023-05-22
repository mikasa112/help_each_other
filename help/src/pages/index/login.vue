<template>
	<view class="content">
		<u-toast ref="toast"></u-toast>
		<u--form labelPosition="left" labelWidth="60" labelAlign="center" ref="form" :model="user" :rules="rules">
			<u-form-item label="用户名" borderBottom prop="username">
				<u--input v-model="user.username">
				</u--input>
			</u-form-item>
			<u-form-item label="密码" prop="password">
				<u--input type="password" v-model="user.password">

				</u--input>
			</u-form-item>
			<u-form-item label="记住我" style="margin-top: 16rpx;">
				<u-switch v-model="user.remember" size="15"></u-switch>
			</u-form-item>
		</u--form>
		<view class="btn-container">
			<u-button type="primary" @click="login" style="width: 40%;">登录</u-button>
			<u-button type="primary" @click="register" style="width: 40%;">注册</u-button>
		</view>
	</view>
</template>

<script>
	import tip from "@/components/tip.vue"
	export default {
		mixins: [tip],
		data() {
			return {
				user: {
					username: "",
					password: "",
					remember: false,
				},
				rules: {
					"username": [{
							type: "string",
							required: true,
							message: '请填写用户名',
							trigger: ['blur', 'change']
						},
						{
							pattern: /^[0-9a-zA-Z]*$/g,
							// 正则检验前先将值转为字符串
							transform(value) {
								return String(value);
							},
							message: '只能包含字母或数字'
						},
						// 6-8个字符之间的判断
						{
							min: 3,
							max: 21,
							message: '长度在3-21个字符之间'
						},
					],
					"password": [{
							type: "string",
							required: true,
							message: '请输入密码',
							trigger: ['blur', 'change']
						},
						{
							min: 8,
							max: 26,
							message: '长度在8-26个字符之间'
						}
					],
				}
			};
		},
		methods: {
			async login() {
				let isValidate = await this.$refs.form.validate()
				let res = await this.$api.login(this.user)
				if (res.code !== 200) {
					this.showToast("error", res.message)
				} else {
					uni.setStorageSync("current_user", res.data)
					uni.$emit("loadData")
					uni.reLaunch({
						url: "index"
					})
				}
			},
			register() {
				uni.navigateTo({
					url: "register"
				})
			}
		}
	}
</script>

<style scoped>
	.content {
		padding: 30rpx;
	}

	.btn-container {
		display: flex;
		margin-top: 32rpx;
	}
</style>