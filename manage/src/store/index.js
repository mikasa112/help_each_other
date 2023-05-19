import Vue from "vue";
import Vuex from "vuex"

Vue.use(Vuex)

const state = {
    isCollapse: false,
    currentUser: null,
}
const mutations = {
    setCollapse(context, val) {
        context.isCollapse = val
    },
    //将user存入store
    setUser(context, val) {
        context.currentUser = val
    }
}
const actions = {}
const getters = {
    getCollapse(context) {
        return context.isCollapse;
    },
    //获得当前用户
    getUserInfo(context) {
        return context.currentUser
    }
}


export default new Vuex.Store({
    state,
    mutations,
    actions,
    getters
})