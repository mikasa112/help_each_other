import Vue from 'vue'
import App from './App.vue'
import router from "@/router";
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import VueCropper from "vue-cropper";
import store from "@/store/index"

Vue.config.productionTip = false
Vue.use(ElementUI);
Vue.use(VueCropper);
new Vue({
    router,
    render: h => h(App),
    store
}).$mount('#app')
