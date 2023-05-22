<template>
	<view class="content">
		<view class="category">
			<text v-for="(item,index) in categorys" :key="index" ref="categoryItem"
				@click="changeCategory(index,item.id)">{{item.category}}</text>
		</view>
		<view class="list">
			<u-swiper :list="images" indicator indicatorMode="line" circular></u-swiper>
			<u-line dashed style="margin-top: 15rpx;"></u-line>
			<scroll-view v-if="services.length>0">
				<!-- 延迟传递数据 -->
				<service v-for="(item,index) in services" :key="index" :service="item"></service>
			</scroll-view>
			<u-loadmore :status="status" dashed line @loadmore="loadmore" />
		</view>
	</view>
</template>

<script>
	import service from "@/components/service.vue"
	export default {
		components: {
			service
		},
		data() {
			return {
				status: "loadmore",
				images: ["/static/image/banner1.jpg", "/static/image/banner2.jpg"],
				categorys: [],
				services: [],
				page: 1,
				pageSize: 10,
				total: 0,
				category: 0,
			}
		},
		computed: {
			params() {
				return `page=${this.page}&size=${this.pageSize}`;
			}
		},
		methods: {
			//修改分类
			changeCategory(k, id) {
				//重置
				this.services = [];
				this.page = 1;
				this.total = 0;
				this.category = 0;
				this.status = 'loadmore';

				this.$refs.categoryItem.forEach(item => this.setColor(item, ""))
				let item = this.$refs.categoryItem[k];
				this.setColor(item, "#fff")
				this.category = id;
				this.getService(this.category)
			},
			setColor(item, color) {
				item.$el.style.backgroundColor = color
			},
			async getService(categoryId) {
				let msg = await this.$api.getServiceByCategory(categoryId, this.params)
				this.total = msg.data.total
				//往集合里面追加
				this.services.push(...msg.data.list)
			},
			//屏幕下滑触发
			onReachBottom() {
				this.loadmore()
			},
			loadmore() {
				this.status = 'loading';
				this.page = ++this.page;
				setTimeout(async () => {
					await this.getService(this.category)
					if (this.services.length >= this.total) this.status = 'nomore'
					else this.status = 'loadmore'
				}, 1000)
			}
		},
		async created() {
			let data = await this.$api.getCategory()
			this.categorys = data.data
			this.$nextTick(async () => {
				this.setColor(this.$refs.categoryItem[0], "#fff")
				this.category = this.categorys[0].id
				this.getService(this.category)
			})
		}
	}
</script>

<style scoped>
	.content {
		display: flex;
		flex-direction: row;
		padding: 10rpx;
	}

	.category {
		width: 25%;
		height: 100%;
		display: flex;
		flex-direction: column;
		text-align: center;
		background-color: #f2f2f2;
	}

	.category>text {
		padding: 35rpx;
		font-size: 20rpx;
	}

	.list {
		margin-left: 10rpx;
		width: 80%;
		height: 100%;
		display: flex;
		flex-direction: column;
	}
</style>