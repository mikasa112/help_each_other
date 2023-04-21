<template>
    <el-row>
        <el-col :span="1">
            <div>
                <el-button icon="el-icon-menu" size="mini" @click="menuHandler"></el-button>
            </div>
        </el-col>
        <el-col :span="1">
            <div class="table">首页</div>
        </el-col>
        <el-col :span="20">
            <div class="table">&nbsp;</div>
        </el-col>
        <el-col :span="2">
            <!--   头像下拉菜单 -->
            <el-dropdown trigger="click">
                <div class="circle">
                    <el-avatar :size="45" :src="imgUrl"></el-avatar>
                </div>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item icon="el-icon-plus">我的</el-dropdown-item>
                    <el-dropdown-item icon="el-icon-circle-plus">账单</el-dropdown-item>
                    <el-dropdown-item icon="el-icon-circle-plus-outline">消息</el-dropdown-item>
                    <el-dropdown-item icon="el-icon-check">退出</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </el-col>
    </el-row>
</template>

<script>
import {getCurrentUser} from "@/api/api";

export default {
    name: "CommonHeader",
    data() {
        return {
            imgUrl: "",
        }
    },
    methods: {
        menuHandler() {

        },

    },
    async mounted() {
        let info = await getCurrentUser()
        this.imgUrl=info.user.avatar
    }
}
</script>

<style scoped>
.table {
    color: #C0C4CC;
    line-height: 60px;
}

.circle {
    height: 60px;
}

.el-avatar {
    margin-top: 7px;
    margin-left: 20px;
}
</style>