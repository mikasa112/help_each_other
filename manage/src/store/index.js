import Vue from "vue";
import Vuex from "vuex"

Vue.use(Vuex)

const state = {
    isCollapse: false
}
const mutations = {
    setCollapse(context, val) {
        context.isCollapse = val
    }
}
const actions = {}
const getters = {
    getCollapse(context) {
        return context.isCollapse;
    }
}


export default new Vuex.Store({
    state,
    mutations,
    actions,
    getters
})