<template>
	<view>
		<view v-for="(item,index) in comments" :key="index" class="comment">
			<view class="item">
				<view class="info">
					<view style="width: 15%">
						<u-avatar :src="item.avatar"></u-avatar>
					</view>
					<view style="width: 10%">
						<text class="text">沙发</text>
					</view>
					<view style="width: 65%">
						<u--text :text="item.nickname" lines="1" bold type="info" size="10"></u--text>
					</view>
					<view style="width: 10%; display: flex;">
						<u--text :text="item.likeCount" lines="1" v-show="item.likeCount>0" bold type="info"
							size="10"></u--text>
						<u-icon style="margin-bottom: 4px;" name="thumb-up-fill" color="indianred" size="20"></u-icon>
					</view>
				</view>
				<view style="margin: 10px;">
					<u--text align="center" lines="5" :text="item.content" type="info" size="12"></u--text>
				</view>
				<u--text align="left" lines="1" :text="item.createdAt" type="info" size="10"></u--text>
			</view>
			<u-divider :hairline="true" dashed="true"></u-divider>
		</view>
	</view>
</template>

<script>
	export default {
		name: "Comment",
		props: ["comments"],
		data() {
			return {

			};
		},
		mounted() {
			console.log(this.comments)
		}
	}
</script>

<style scoped>
	.comment {
		padding-top: 20rpx;
	}

	.item {
		display: flex;
		flex-direction: column;
	}

	.info {
		display: flex;
		flex-direction: row;
	}

	.text {
		font-size: 8px;
		background-color: indianred;
		padding: 3px;
		color: white;
		border-radius: 5px;
	}
</style>