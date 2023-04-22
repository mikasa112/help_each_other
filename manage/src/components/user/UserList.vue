<template>
    <el-card class="box-card">
        <el-row style="margin-bottom: 40px">
            <el-col :span="6">
                <el-input placeholder="输入姓名以搜索" v-model="name" @keydown.enter.m.native="search(name)"></el-input>
            </el-col>
            <el-col :span="2">
                <el-button type="primary" @click="search(name)">搜索</el-button>
            </el-col>
        </el-row>
        <el-table :data="users.list" border style="width: 100%"
                  height="450"
                  :default-sort="{prop:'createdAt',order:'ascending'}">
            <el-table-column
                    prop="uuid"
                    label="uuid"
            ></el-table-column>
            <el-table-column
                    prop="username"
                    label="姓名"
            ></el-table-column>
            <el-table-column
                    prop="role"
                    label="权限"
                    width="100px"
            ></el-table-column>
            <el-table-column
                    prop="points"
                    width="100px"
                    label="积分"
                    sortable
            ></el-table-column>
            <el-table-column
                    prop="createdAt"
                    sortable
                    label="创建时间"
            ></el-table-column>
            <el-table-column
                    fixed="right"
                    label="操作"
                    width="100">
                <template slot-scope="scope">
                    <el-button @click="handleEdit(scope.row)" type="text" size="small">编辑</el-button>
                    <el-button @click="handleRemove(scope.row.uuid)" type="text" size="small">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
                style="margin-top: 25px"
                :total="users.total"
                @current-change="handleCurrentChange"
                :page-size="users.pageSize"
                layout="total, prev, pager, next, jumper"
        >
        </el-pagination>
    </el-card>
</template>

<script>
import {getUserByName, getUserList, removeUserByUUID} from "@/api/api";

export default {
    name: "UserList",
    data() {
        return {
            name: "",
            users: {},
            page: 1,
            pageSize: 10,
        }
    },
    methods: {
        async getUsers() {
            this.users = await getUserList(this.params)
            console.log(this.users)
        },
        handleEdit(data) {
            console.log(data)
        },
        async handleRemove(uuid) {
            await removeUserByUUID(uuid)
            await this.getUsers()
        },
        //分页切换到当前页时的回调
        handleCurrentChange(currentPage) {
            // console.log(currentPage);
            this.page = currentPage;
            this.getUsers(this.params);
        },
        async search(name) {
            if (name !== "") {
                let user = await getUserByName(name)
                if (user) {
                    this.users.list = []
                    this.users.list.push(user);
                } else {
                    this.$message({
                        message: "用户不存在",
                        type: "error"
                    })
                }
            } else {
                this.$message({
                    message: "请输入名字",
                    type: "error"
                })
            }
        }
    },
    mounted() {
        this.getUsers()
    },
    computed: {
        params() {
            return `page=${this.page}&pageSize=${this.pageSize}`;
        }
    }
}

</script>

<style scoped>

</style>