<template>
    <el-menu
            :default-active="$route.path"
            class="el-menu-vertical"
            :collapse="isCollapse"
            background-color="#545c64"
            text-color="#fff"
            active-text-color="#ffd04b"
            :router="true">
        <h3>{{ isCollapse ? '后台' : '助老互帮后台管理系统' }}</h3>
        <el-menu-item v-for="item in noChildren" :key="item.path" :index="item.path+''">
            <i :class="'el-icon-'+ item.icon"></i>
            <span slot="title">{{ item.label }}</span>
        </el-menu-item>

        <el-submenu v-for="item in hasChildren" :key="item.path" :index="item.path+''">
            <template slot="title">
                <i :class="'el-icon-'+ item.icon"></i>
                <span slot="title">{{ item.label }}</span>
            </template>
            <el-menu-item-group v-for="subItem in item.children" :key="subItem.path">
                <el-menu-item :index="subItem.path">{{ subItem.label }}</el-menu-item>
            </el-menu-item-group>
        </el-submenu>
    </el-menu>
</template>

<script>
export default {
    name: "HelloWorld",
    props: {
        msg: String,
    },
    data() {
        return {
            menu: [
                {
                    path: '/home',
                    name: 'home',
                    label: '首页',
                    icon: 's-home',
                },
                {
                    path: '/user',
                    name: 'user',
                    label: '用户管理',
                    icon: 'user',
                    children: [
                        {
                            path: '/user/list',
                            name: 'userList',
                            label: '用户列表',
                        }, {
                            path: '/user/add',
                            name: 'addUser',
                            label: '添加用户',
                        }
                    ]
                },
                {
                    path: '/service',
                    name: 'service',
                    label: '服务管理',
                    icon: 'shopping-cart-2',
                },
                {
                    path: '/category',
                    name: 'category',
                    label: '分类管理',
                    icon: 's-grid',
                },
                {
                    path: '/order',
                    name: 'order',
                    label: '订单管理',
                    icon: 's-order',
                },
                {
                    path: '/newsletter',
                    name: 'newsletter',
                    label: '资讯管理',
                    icon: 'news',
                    children: [
                        {
                            path: '/newsletter/list',
                            name: 'newsLetterList',
                            label: '资讯列表',
                        },
                        {
                            path: '/newsletter/add',
                            name: 'addNewsLetter',
                            label: '添加资讯',
                        }
                    ]
                },
                {
                    path: '/comment',
                    name: 'comment',
                    label: '评论管理',
                    icon: 's-comment',
                },
            ]
        }
    },
    computed: {
        noChildren() {
            return this.menu.filter(item => !item.children)
        },
        hasChildren() {
            return this.menu.filter(item => item.children)
        },
        isCollapse() {
            return this.$store.getters.getCollapse;
        }
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="less" scoped>
.el-menu-vertical:not(.el-menu--collapse) {
  width: 200px;
  min-height: 100vh;
  border: none;
}

.el-menu {
  border: none;
}

.el-menu-item-group__title {
  padding: 0;
}

h3 {
  color: aliceblue;
  line-height: 30px;
  font-size: 1em;
}

span, .el-menu-item {
  font-size: 16px;
}

</style>