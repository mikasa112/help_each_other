<template>
	<view class="choose-location">
		<scroll-view>
			<!-- 		<view class="info-box">
						<view class="info-box-item">位置：{{ selectLocatin.title || '' }}</view>
						<view class="info-box-item">地址：{{ selectLocatin.address || '' }}</view>
						<view class="info-box-item">纬度(latitude)：{{ selectLocatin.location.lat || '' }}</view>
						<view class="info-box-item">经度(longitude)：{{ selectLocatin.location.lng || '' }}</view>
					</view>
					<button type="primary" @click="show = true">选择位置</button> -->

			<u--form labelPosition="left" labelAlign="center" ref="form" :model="service" :rules="rules"
				labelWidth="100">
				<u-form-item label="服务名称" prop="name">
					<u--input border="bottom" v-model="service.name"></u--input>
				</u-form-item>
				<u-form-item label="服务详细描述">
					<u--textarea autoHeight count v-model="service.introduction"></u--textarea>
				</u-form-item>
				<u-form-item label="服务关键字">
					<u--input border="bottom" v-model="service.keywords"></u--input>
				</u-form-item>
				<u-form-item label="服务所需价格">
					<u-number-box style="margin-left: 30rpx;" button-size="30" color="#ffffff" bgColor="#2979ff"
						iconStyle="color: #fff" step="0.5" decimal-length="1"
						v-model="service.pointsPrice"></u-number-box>
				</u-form-item>
				<u-form-item label="服务所在地址">
					<u--input border="bottom" @focus="show=true" placeholder="" suffixIcon="map-fill"
						suffixIconStyle="color: blue" v-model="service.address"></u--input>
				</u-form-item>
				<u-form-item label="服务分类">
					<u-radio-group placement="column" v-model="service.category">
						<u-radio v-for="(item,index) in categorys" shape="square" activeColor="blue" :key="index"
							:name="item.id" @change="radioChange(item.id)" :label="item.category"></u-radio>
					</u-radio-group>
				</u-form-item>
				<u-form-item label="服务内容预览">
					<u-upload @delete="deletePic" @afterRead="afterRead" multiple :max-count="10"
						:fileList="imageList"></u-upload>
				</u-form-item>
				<u-button type="primary" @click="addService" style="width: 60%; margin-top: 100rpx;">发布</u-button>
			</u--form>

		</scroll-view>
		<Flame-chooseLocation v-model="show" mapkey="SUVBZ-EU5KU-JGWVI-G2S7M-VTEQT-O7FZH"
			@confirm="confirm"></Flame-chooseLocation>
		<u-toast ref="toast"></u-toast>
	</view>
</template>

<script>
	import api from '../../api/api'
	import tip from "@/components/tip.vue"
	export default {
		mixins: [tip],
		data() {
			return {
				categorys: [],
				imageList: [],
				//图片的在nginx中的请求路径集合
				pictureList: [],
				service: {
					name: "",
					introduction: "",
					keywords: "",
					pointsPrice: 1,
					pictures: "",
					address: "",
					category: 1,
					//经度
					longitude: 0,
					//纬度
					latitude: 0,
				},
				show: false,
				selectLocatin: {
					location: {}
				},
				rules: {
					"name": [{
						type: "string",
						required: true,
						message: '请填写服务名称',
						trigger: ['blur', 'change']
					}, {
						min: 3,
						max: 50,
						message: '长度在3-50个字符之间'
					}]
				}
			}
		},
		onLoad() {},
		async onReady() {
			let res = await this.$api.getCategory()
			this.categorys = res.data
		},
		methods: {
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
				//将图片集合中的index删除
				this.pictureList.splice(event.index, 1)
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
							//将返回的图片请求路径填入
							this.pictureList.push(data.data);
							resolve(data)
						}
					});
				})
			},
			//确认服务所在地址
			confirm(val) {
				// console.log(val)
				//将地址赋值给service
				this.service.address = val.address
				this.selectLocatin = val
				this.service.latitude = val.location.lat
				this.service.longitude = val.location.lng
			},
			radioChange(id) {
				this.service.category = id
			},
			async addService() {
				const valid = await this.$refs.form.validate()
				this.showLoading()
				this.imageList.forEach(item => {
					this.service.pictures += item.url + ";"
				})
				const res = await this.$api.addService(this.service)
				if (res.code !== 200) {
					this.showToast("error", res.message)
				} else {
					this.showToast("success", "发布成功")
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.choose-location {
		padding: 30rpx;

		.info-box {
			padding: 30rpx 0;

			.info-box-item {
				padding-bottom: 20rpx;
			}
		}
	}
</style>