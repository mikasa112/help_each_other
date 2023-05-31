<template>
    <el-card class="box-card">
        <el-table :data="services" border class="table"
                  highlight-current-row
                  :cell-style="{ textAlign: 'center' }"
                  :header-cell-style="{ textAlign: 'center' }"
                  height="540">
            <el-table-column
                    prop="serviceId"
                    label="服务ID"
            ></el-table-column>
            <el-table-column
                    prop="name"
                    label="服务名称"
                    show-overflow-tooltip
            ></el-table-column>
            <el-table-column
                    prop="category"
                    label="分类"
            ></el-table-column>
            <el-table-column
                    prop="pointsPrice"
                    label="积分酬劳"
                    sortable
            ></el-table-column>
            <el-table-column
                    prop="status"
                    label="状态"
                    sortable
            ></el-table-column>
            <el-table-column
                    prop="visited"
                    label="热度"
                    sortable
            ></el-table-column>
        </el-table>
        <el-pagination
                style="margin-top: 25px"
                :total="total"
                @current-change="handleCurrentChange"
                :page-size="pageSize"
                layout="total, prev, pager, next, jumper"
        >
        </el-pagination>
    </el-card>
</template>

<script>
import {getCategories, getServiceList} from "@/api/api";

export default {
    name: "ServiceList",
    data() {
        return {
            page: 1,
            pageSize: 10,
            services: [],
            total: 0,
        }
    },
    methods: {
        async getData(params) {
            let res = await getServiceList(params)
            this.total = res.total
            let category = await getCategories()
            this.services = res.list.map(item => {
                let flag = ""
                category.forEach(i => {
                    if (i.id === item.category) {
                        flag = i.category
                    }
                })
                if (item.status===0){
                    item.status="待接单"
                }else if (item.status === 1) {
                    item.status = "正在服务"
                } else if (item.status === 2) {
                    item.status = "已完成"
                }
                item.category = flag
                return item
            })
        },
        //分页切换到当前页时的回调
        handleCurrentChange(currentPage) {
            this.page = currentPage;
            this.getData(this.params);
        },
    },
    mounted() {
        this.getData(this.params)
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
