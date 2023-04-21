import Vue from "vue"
import VueRouter from 'vue-router'
import Login from "@/views/Login.vue";
import Main from "@/views/Main.vue";
import Home from "@/components/Home.vue";
import UserList from "@/components/user/UserList.vue";
import AddUser from "@/components/user/AddUser.vue";

Vue.use(VueRouter)

const routes = [
    {name: "login", path: "/login", component: Login},
    {
        name: "main", path: "/", component: Main,
        redirect: "/home",
        children: [
            {name: "home", path: "/home", component: Home},
            {name: "userList", path: "/user/list", component: UserList},
            {name: "addUser", path: "/user/add", component: AddUser},
        ],
    },
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