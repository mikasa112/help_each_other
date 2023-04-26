import Vue from "vue"
import VueRouter from 'vue-router'
import Login from "@/views/Login.vue";
import Main from "@/views/Main.vue";
import Home from "@/components/Home.vue";
import UserList from "@/components/user/UserList.vue";
import AddUser from "@/components/user/AddUser.vue";
import UpdateUser from "@/components/user/UpdateUser.vue";
import NewsLetterList from "@/components/newsletter/NewsLetterList.vue";
import AddNewsLetter from "@/components/newsletter/AddNewsLetter.vue";
import CategoryList from "@/components/category/CategoryList.vue";
import OrderList from "@/components/order/OrderList.vue";

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
            {name: "updateUser", path: "/user/update", component: UpdateUser},

            {name: "newsletterList", path: "/newsletter/list", component: NewsLetterList},
            {name: "addNewsLetter", path: "/newsletter/add", component: AddNewsLetter},

            {name: "categoryList", path: "/category/list", component: CategoryList},

            {name: "orderList", path: "/order/list", component: OrderList},
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
    const token = localStorage.getItem("token")
    if (to.path !== "/login") {
        if (!token) {
            next("/login")
        }
    }
    next()
})
export default router