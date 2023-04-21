import Vue from "vue"
import VueRouter from 'vue-router'
import Login from "@/views/Login.vue";

Vue.use(VueRouter)

const routes = [
    {name: "login", path: "/login", component: Login},
    {name: "notfound", path: "/notfound", component: () => import('@/views/404.vue')},
    {path: "*", redirect: "/notfound"}
]
const router = new VueRouter({
    base: process.env.BASE_URL,
    routes
})

router.beforeEach((to, from, next) => {
    const token = sessionStorage.getItem("token")
    if (to.path !== "/login") {
        if (!token) {
            next("/login")
        }
    }
    next()
})
export default router