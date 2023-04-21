<template>
    <div id="myapp">
        <el-container>
            <!-- 侧边栏组件 -->
            <el-aside width="">
                <common-aside></common-aside>
            </el-aside>
            <el-container>
                <!-- 顶部组件 -->
                <el-header>
                    <common-header></common-header>
                </el-header>
                <!-- 首页组件 -->
                <el-main>
                    <router-view></router-view>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>
import CommonAside from "@/components/CommonAside.vue";
import CommonHeader from "@/components/CommonHeader.vue";

export default {
    name: "MainView",
    components: {
        CommonHeader, CommonAside
    }
}
</script>

<style scoped>
.el-header {
    background-color: #303133;
    color: #333;
    text-align: center;
    line-height: 60px;
}

.el-aside {
    background-color: #545c64;
    color: #333;
    text-align: center;
    line-height: 200px;
    height: 100vh;
}

.el-main {
    background-color: #F2F6FC;
    color: #333;
    text-align: center;
    padding: 10px;
}

body > .el-container {
    margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
    line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
    line-height: 320px;
}

</style>