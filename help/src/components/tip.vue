<script>
	export default {
		name: "tip",
		data() {
			return {
				showLoading(callback) {
					this.$refs.toast.show({
						type: 'loading',
						message: "正在加载",
					})
				},
				showToast(type, msg) {
					this.$refs.toast.show({
						type: type,
						message: msg,
					})
				}
			};
		}
	}
</script>

<style>

</style>