<template>
	<view class="content">
		<scroll-view>
			<u-toast ref="toast"></u-toast>
			<u-loading-page :loading="loading"></u-loading-page>
			<u--form labelPosition="left" labelWidth="80" labelAlign="center" ref="form" :model="user" :rules="rules">
				<u-form-item label="用户名" borderBottom prop="username">
					<u--input v-model="user.username">
					</u--input>
				</u-form-item>
				<u-form-item label="密码" borderBottom prop="password">
					<u--input type="password" v-model="user.password">

					</u--input>
				</u-form-item>
				<u-form-item label="确认密码" borderBottom prop="repwd">
					<u--input type="password" v-model="user.repwd">

					</u--input>
				</u-form-item>
				<u-form-item label="昵称" borderBottom>
					<u--input v-model="user.nickname">

					</u--input>
				</u-form-item>
				<u-form-item label="性别" prop="userInfo.sex" borderBottom @click="showSex = true;">
					<u--input v-model="user.sex" disabled disabledColor="#ffffff" placeholder="请选择性别"
						border="none"></u--input>
					<u-icon slot="right" name="arrow-right"></u-icon>
				</u-form-item>
				<u-form-item label="邮箱" borderBottom prop="email">
					<u--input v-model="user.email" clearable>

					</u--input>
				</u-form-item>
				<u-form-item label="手机号" borderBottom prop="phone">
					<u--input v-model="user.phone" clearable>

					</u--input>
				</u-form-item>
				<u-form-item label="年龄" borderBottom>
					<u--input v-model="user.age" type="digit" clearable>

					</u--input>
				</u-form-item>
				<u-form-item label="头像" borderBottom> <u-upload @delete="deletePic" @afterRead="afterRead" multiple
						:max-count="1" :fileList="imageList"></u-upload></u-form-item>
			</u--form>
			<u-action-sheet :show="showSex" :actions="actions" title="请选择性别" @close="showSex = false"
				@select="sexSelect">
			</u-action-sheet>
			<u-button type="primary" @click="register" style="width: 40%; margin-top: 100rpx;">注册</u-button>
		</scroll-view>
	</view>
</template>

<script>
	import tip from "@/components/tip.vue"
	export default {
		mixins: [tip],
		data() {
			return {
				imageList: [],
				showSex: false,
				loading: false,
				actions: [{
						name: '男',
					},
					{
						name: '女',
					},
				],
				user: {
					username: "",
					password: "",
					repwd: "",
					role: "user",
					nickname: "",
					sex: "男",
					avatar: "",
					email: "",
					phone: "",
					age: 18,
				},
				rules: {
					"username": [{
							type: "string",
							required: true,
							message: '请填写姓名',
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
					"repwd": [{
						type: "string",
						required: true,
						message: '请输入密码',
						trigger: ['blur', 'change']
					}, {
						validator: (rule, value, callback) => {
							return this.user.password === value
						},
						message: "两次密码不一致",
					}, {
						min: 8,
						max: 26,
						message: '长度在8-26个字符之间'
					}],
					"email": {
						pattern: /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
						transform(value) {
							return String(value);
						},
						message: '邮箱格式不正确',
						trigger: ['blur', 'change']
					},
					"phone": {
						pattern: /^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/,
						transform(value) {
							return String(value);
						},
						message: '手机号格式不正确',
						trigger: ['blur', 'change']
					},
				}
			}
		},
		methods: {
			sexSelect(e) {
				this.user.sex = e.name
			},
			async afterRead(event) {
				// 当设置 multiple 为 true 时, file 为数组格式，否则为对象格式
				let lists = [].concat(event.file)
				let fileListLen = this[`imageList${event.name}`].length
				lists.map((item) => {
					this[`imageList${event.name}`].push({
						...item,
						status: 'uploading',
						message: '上传中'
					})
				})
				for (let i = 0; i < lists.length; i++) {
					const result = await this.uploadFile(lists[i].url)
					let item = this[`imageList${event.name}`][fileListLen]
					this[`imageList${event.name}`].splice(fileListLen, 1, Object.assign(item, {
						status: 'success',
						message: '',
						url: result.data
					}))
					fileListLen++
				}
			},
			deletePic(event) {
				this[`imageList${event.name}`].splice(event.index, 1)
			},
			uploadFile(url) {
				return new Promise((resolve, reject) => {
					uni.uploadFile({
						url: this.$api.baseUrl() + "api/upload",
						filePath: url,
						name: 'file',
						head: {
							'Content-Type': 'multipart/form-data'
						},
						success: (res) => {
							const data = JSON.parse(res.data)
							this.user.avatar = data.data
							resolve(data)
						}
					});
				})
			},
			register() {
				this.$refs.form.validate().then(async () => {
					const user = {
						username: this.user.username,
						password: this.user.password,
						role: this.user.role,
					}
					if (this.user.nickname !== "") {
						user["nickname"] = this.user.nickname
					}
					if (this.user.avatar !== "") {
						user["avatar"] = this.user.avatar
					}
					if (this.user.email !== "") {
						user["email"] = this.user.email
					}
					if (this.user.phone !== "") {
						user["phone"] = this.user.phone
					}
					if (this.user.age !== "") {
						user["age"] = this.user.age
					}
					if (this.user.sex !== "") {
						user["sex"] = this.user.sex
					}
					console.log(user)
					const res = await this.$api.register(user)
					if (res.code !== 200) {
						this.$refs.toast.show({
							message: res.message,
							type: "error",
						});
					} else {
						this.$refs.toast.show({
							message: "注册成功!",
							type: "success"
						})
					}
					this.loading = false
				}).catch(errors => {
					uni.$u.toast('校验失败')
				})
			},
		}
	}
</script>

<style scoped>
	.content {
		padding: 30rpx;
	}
</style>