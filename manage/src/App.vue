<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>


<script>
// import {getCurrentUser} from "@/api/api";

export default {
  data() {
    return {}
  },
  // async created() {
  //     //开局就获得user，怎么说
  //     let info = await getCurrentUser()
  //     this.$store.commit("setUser", info.user)
  // }
}
</script>
<style>
html,
body,
#app {
  height: 100%;
  padding: 0;
  margin: 0;
}
</style>
