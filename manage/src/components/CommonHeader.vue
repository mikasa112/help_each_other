<template>
    <el-row>
        <el-col :span="1">
            <div>
                <el-button icon="el-icon-menu" size="mini" @click="menuHandler"></el-button>
            </div>
        </el-col>
        <el-col :span="1">
            <div class="table">{{ this.username }}</div>
        </el-col>
        <el-col :span="20">
            <div class="table">&nbsp;</div>
        </el-col>
        <el-col :span="2">
            <!--   头像下拉菜单 -->
            <el-dropdown trigger="click" @command="handleCommand">
                <div class="circle">
                    <el-avatar :size="45" :src="imgUrl"></el-avatar>
                </div>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item icon="el-icon-close" command="logout">退出</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </el-col>
    </el-row>
</template>

<script>
import {getCurrentUser, logoutApi} from "@/api/api";

export default {
    name: "CommonHeader",
    data() {
        return {
            imgUrl: "",
            username: "",
        }
    },
    methods: {
        menuHandler() {
            var collapse = this.$store.getters.getCollapse;
            this.$store.commit("setCollapse", !collapse)
        },
        async handleCommand(command) {
            if (command === "logout") {
                await logoutApi();
                localStorage.removeItem("token")
                await this.$router.replace({name: "login"})
            }
        }
    },
    async mounted() {
        let info = await getCurrentUser()
        this.imgUrl = info.user.avatar
        this.username = info.user.username
    }
}
</script>

<style scoped>
.table {
    color: #C0C4CC;
    line-height: 60px;
    margin-left: 15px;
}

.circle {
    height: 60px;
}

.el-avatar {
    margin-top: 7px;
    margin-left: 20px;
}
</style>