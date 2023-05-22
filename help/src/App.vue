<script>
	import pageAnimation from 'components/page-animation/index.vue'
	export default {
		mixins: [pageAnimation],
		onLaunch: function() {
			console.log('App Launch')
			//首次启动获取user信息
			let user = uni.getStorageSync("current_user")
			this.load()
			uni.$on("loadData", () => {
				this.load()
			})
			uni.$on("order", (data) => {
				// console.log(data)
				uni.showModal({
					title: '提示',
					content: '您的服务已被接单，请到我的订单里查看!',
					showCancel: false,
					success: () => {
						this.$store.commit("addOrder", data)
					}
				});
			})
			uni.$on("closeSocket", () => {
				this.socket.close()
			})
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		},
		beforeDestroy() {
			this.socket.close()
		},
		methods: {
			load() {
				this.$api.getCurrentUser().then(res => {
					//将user信息存入vuex
					this.$store.commit("setUserInfo", res.data)
					//启动socket
					this.socket.init(res.data.user.uuid)
				}).catch(err => {
					console.log(err)
				})
			}
		}
	}
</script>

<style lang="scss">
	/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
	@import "@/uni_modules/uview-ui/index.scss";
</style>