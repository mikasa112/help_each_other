<template>
    <el-card class="box-card">
        <el-table :data="newsletter.list" border style="width: 100%"
                  highlight-current-row
                  height="540">
            <el-table-column
                    sortable
                    prop="id"
                    width="50px"
                    label="id"
            ></el-table-column>
            <el-table-column
                    prop="content"
                    label="内容"
                    show-overflow-tooltip
            ></el-table-column>
            <el-table-column
                    sortable
                    prop="createdAt"
                    width="150px"
                    label="创建时间"
            ></el-table-column>
            <el-table-column width="60px" fixed="right">
                <template v-slot="scope">
                    <el-button type="text" @click="remove(scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
                style="margin-top: 25px"
                :total="newsletter.total"
                @current-change="handleCurrentChange"
                :page-size="newsletter.pageSize"
                layout="total, prev, pager, next, jumper"
        >
        </el-pagination>
    </el-card>
</template>

<script>
import {getNewsLetterList, removeNewsLetterById} from "@/api/api";

export default {
    name: "NewsLetterList",
    data() {
        return {
            page: 1,
            pageSize: 10,
            newsletter: {}
        }
    },
    methods: {
        //分页切换到当前页时的回调
        handleCurrentChange(currentPage) {
            this.page = currentPage;
            this.getData(this.params);
        },
        async getData(params) {
            this.newsletter = await getNewsLetterList(params)
        },
        remove(id) {
            this.$confirm("是否删除此资讯?", '提示', {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(async () => {
                await removeNewsLetterById(id)
                await this.getData(this.params)
                this.$message({
                    type: "success",
                    message: "删除成功",
                })
            }).catch(() => {
                this.$message({
                    type: "info",
                    message: "已取消删除",
                })
            });
        }
    },
    mounted() {
        this.getData(this.params)
    }
    ,
    computed: {
        params() {
            return `page=${this.page}&pageSize=${this.pageSize}`;
        }
    }
}
</script>

<style scoped>

</style>