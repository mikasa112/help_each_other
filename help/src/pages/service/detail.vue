<template>
	<scroll-view>
		<u-toast ref="toast"></u-toast>
		<u-swiper :list="serviceImage" previousMargin="30" nextMargin="30" circular :autoplay="true" radius="5"
			bgColor="#ffffff" height="280"></u-swiper>
		<view class="content">
			<u-row justify="between">
				<u-col :span="3" align="center">
					<u-row justify="center">
						<u-icon name="rmb-circle-fill" color="gold"></u-icon>
						<u--text style="margin-left: 10rpx;" color="#cc0000" align="center" bold
							:text="data.pointsPrice+'积分'"></u--text>
					</u-row>
				</u-col>
				<u-col :span="2">
					<u--text size="10" align="center" decoration="underline" type="info"
						:text="data.visited+'次浏览'"></u--text>
				</u-col>
			</u-row>
			<u-line dashed margin="20rpx"></u-line>
			<view class="title">
				<u--text size="13" :lines="2" bold :text="data.name"></u--text>
				<u-icon name="share-fill" size="20"></u-icon>
			</view>
			<u-line dashed margin="20rpx"></u-line>
			<view class="introduction">
				<u-tag style="margin-right: 20rpx;" text="位置" size="mini" icon="map" type="primary" plain
					plainFill></u-tag>
				<u--text size="10" type="info" align="left" :text="data.address"></u--text>
				<u--text size="12" :lines="1" bold type="info" align="center"
					:text="'距离您:'+distance.distance_str"></u--text>
			</view>
			<u-line dashed margin="20rpx"></u-line>
			<view class="introduction">
				<u-tag style="margin-right: 20rpx;" text="详情" icon="https://cdn.uviewui.com/uview/example/tag.png" plain
					size="mini" type="primary" plain plainFill></u-tag>
				<u--text size="10" type="info" align="left" :text="data.introduction"></u--text>
			</view>
			<u-line dashed margin="20rpx"></u-line>
			<view class="introduction">
				<u-tag style="margin-right: 20rpx;" @click="toggleMask('show')" text="发表评论" size="mini" icon="chat"
					type="primary" plain plainFill></u-tag>
				<u--text size="10" type="info" align="left" text=""></u--text>
			</view>
			<view v-if="comments.length>0">
				<Comment :comments="comments"></Comment>
				<u-loadmore :status="status" dashed line @loadmore="loadmore" />
			</view>
			<!-- 当服务单是用户发起的就不可接单 -->
			<u-button :disabled="uuid===data.uuid||data.status!==0" @click="take(data.serviceId)" type="primary"
				style="margin-top: 50rpx;" shape="circle" :text="btnText"></u-button>
		</view>
		<ygc-comment ref="ygcComment" :placeholder="'发布评论'" @pubComment="pubComment"></ygc-comment>
	</scroll-view>
</template>

<script>
	import getDistance from "@/util/util.js"
	import tip from "@/components/tip.vue"
	import Comment from "@/components/Comment.vue"
	import ygcComment from '@/components/ygc-comment/ygc-comment.vue'
	export default {
		mixins: [tip],
		components: {
			Comment,
			ygcComment,
		},
		data() {
			return {
				status: "loadmore",
				serviceImage: [],
				data: {},
				distance: {},
				uuid: "",
				btnText: "接单",
				serviceId: null,
				page: 1,
				pageSize: 10,
				total: 0,
				comments: [],
			}
		},
		async onLoad(option) {
			this.serviceId = option.serviceId
			await this.getData(option.serviceId)
			const lat = this.data.latitude
			const lng = this.data.longitude
			uni.getLocation({
				type: 'wgs84',
				success: res => {
					const distance =
						getDistance(res.latitude, res.longitude, lat, lng)
					this.distance = distance
				}
			});
			const userInfo = this.$store.getters.getUserInfo;
			this.uuid = userInfo.user.uuid
		},
		computed: {
			params() {
				return `page=${this.page}&size=${this.pageSize}`;
			}
		},
		methods: {
			async getComment() {
				let commentsRes = await this.$api.getComments(this.serviceId, this.params)
				this.total = commentsRes.data.total
				this.comments.push(...commentsRes.data.list)
				if (this.total === this.comments.length) {
					this.status = 'nomore';
				}
			},
			async getData(id) {
				let res = await this.$api.getService(id)
				await this.getComment()
				const data = res.data
				this.data = data
				if (this.data.status === 1) {
					this.btnText = "已被接单"
				} else if (this.data.status === 2) {
					this.btnText = "已完成"
				}
				if (data.pictures) {
					//去除末尾的 ""
					this.serviceImage = data.pictures.split(";").filter(item => item !== "")
				} else {
					this.serviceImage.push("")
				}
				// console.log(this.data)
			},
			//接单
			async take(id) {
				let res = await this.$api.takeOrder({
					serviceId: id
				})
				this.showToast("success", res.message)
			},
			toggleMask(type) {
				this.$refs.ygcComment.toggleMask(type);
			},
			//发布评论
			async pubComment(content) {
				this.$refs.ygcComment.toggleMask();
				console.log(content);
				if (content === null || content === "") {
					this.showToast("warning", "评论内容不能为空")
				} else {
					const user = this.$store.getters.getUserInfo.user
					let res = await this.$api.pubComment({
						"content": content,
						"uuid": this.uuid,
						"serviceId": this.serviceId,
						"avatar": user.avatar,
						"nickname": user.nickname,
					})
					if (res.code === 200) {
						this.showToast("success", "评论成功")
						await this.getComment()
					}
					this.$refs.ygcComment.content = '';
				}
			}, //屏幕下滑触发
			onReachBottom() {
				this.loadmore()
			},
			loadmore() {
				this.status = 'loading';
				this.page = ++this.page;
				setTimeout(async () => {
					await this.getComment()
					if (this.comments.length >= this.total) this.status = 'nomore'
					else this.status = 'loadmore'
				}, 1000)
			}
		}
	}
</script>

<style scoped>
	.content {
		padding: 30rpx;
	}

	.title {
		display: flex;
	}

	.introduction {
		display: flex;
		justify-content: space-around;
	}
</style>