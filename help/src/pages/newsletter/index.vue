<template>
	<view>
		<u-collapse accordion v-for="(item,index) in newsletters" :key="index">
			<u-collapse-item :title="item.title">
				<u-icon name="tags-fill" size="20" slot="icon"></u-icon>
				<text class="u-collapse-content">{{item.content}}</text>
			</u-collapse-item>
		</u-collapse>
		<u-loadmore :status="status" dashed line @loadmore="loadmore" />
	</view>
</template>

<script>
	export default {
		data() {
			return {
				status: "loadmore",
				page: 1,
				pageSize: 10,
				newsletters: [],
				total: 0
			}
		},
		methods: {
			async getData() {
				let res = await this.$api.getNewsletter(this.params)
				this.total = res.data.total
				const list = res.data.list.map(item => {
					let str = item.content.slice(0, 18)
					item["title"] = str + "...."
					return item
				})
				this.newsletters.push(...list)
			},
			onReachBottom() {
				this.loadmore()
			},
			loadmore() {
				this.status = 'loading';
				this.page = ++this.page;
				setTimeout(async () => {
					await this.getData()
					if (this.newsletters.length >= this.total) this.status = 'nomore'
					else this.status = 'loadmore'
				}, 1000)
			}
		},
		onLoad() {
			this.getData()
		},
		computed: {
			params() {
				return `page=${this.page}&size=${this.pageSize}`;
			}
		},
	}
</script>

<style scoped>

</style>