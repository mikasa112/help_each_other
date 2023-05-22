<template>
	<view class="content">
		<u-search :clearabled="true" v-model="keyword" placeholder="服务名称" @custom="search"></u-search>
		<sl-filter @result="result" :menuList="menuList"></sl-filter>
		<scroll-view v-if="list.length>0">
			<!-- 延迟传递数据 -->
			<service v-for="(item,index) in list" :key="index" :service="item"></service>
		</scroll-view>
		<u-loadmore v-if="list.length>0" :status="status" dashed line @loadmore="loadmore" />
		<u-toast ref="toast"></u-toast>
		<u-empty v-show="list.length===0" mode="search" icon="http://cdn.uviewui.com/uview/empty/search.png">
		</u-empty>
	</view>
</template>

<script>
	import slFilter from '@/components/sl-filter/sl-filter.vue';
	import service from "@/components/service.vue"
	import tip from "@/components/tip.vue"
	export default {
		components: {
			slFilter,
			service,
		},
		mixins: [tip],
		data() {
			return {
				status: "loadmore",
				keyword: "",
				page: 1,
				size: 10,
				total: 0,
				list: [],
				sort: "id",
				order: "asc",
				menuList: [{
						'title': '条件',
						'detailTitle': '排序依据',
						'key': 'sort',
						'isMutiple': false,
						'detailList': [{
								'title': '不限',
								'value': ''
							},
							{
								'title': '积分价格',
								'value': 'points_price'
							},
							{
								'title': '名称',
								'value': 'name'
							},
							{
								'title': '创建时间',
								'value': 'created_at'
							}
						]
					},
					{
						'title': '顺序',
						'detailTitle': '排序顺序',
						'key': 'order',
						'isSort': true,
						'defaultSelectedIndex': 0,
						'isMutiple': false,
						'detailList': [{
								'title': '升序',
								'value': 'asc'
							},
							{
								'title': '降序',
								'value': 'desc'
							},
						]
					}
				]
			}
		},
		onReady() {},
		methods: {
			async getData() {
				if (this.keyword === "") {
					this.showToast("warning", "查询服务名称不能为空")
					return
				}
				const msg = await this.$api.getServiceByName(this.keyword, this.params)
				const data = msg.data
				console.log(data)
				this.list.push(...data.list)
				this.total = data.total
			},
			reset() {
				//重置
				this.list = [];
				this.page = 1;
				this.total = 0;
				this.status = 'loadmore';
			},
			//筛选栏确认事件
			result(val) {
				this.reset()
				this.order = val.order
				this.sort = val.sort
				this.getData()
			},
			//搜索事件
			search() {
				this.reset()
				this.getData()
			},
			//屏幕下滑触发
			onReachBottom() {
				this.loadmore()
			},
			loadmore() {
				this.status = 'loading';
				this.page = ++this.page;
				setTimeout(async () => {
					await this.getData()
					if (this.list.length >= this.total) this.status = 'nomore'
					else this.status = 'loadmore'
				}, 500)
			}
		},
		computed: {
			params() {
				let url = `page=${this.page}&size=${this.size}&order=${this.order}`
				if (this.sort !== "") {
					url += `&sort=${this.sort}`
				}
				return url
			}
		}
	}
</script>

<style scoped>
	.content {
		padding: 30rpx;
		margin: 0;
	}
</style>