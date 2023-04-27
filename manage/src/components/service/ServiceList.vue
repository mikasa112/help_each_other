<template>
    <el-card class="box-card">
        <el-table :data="services.list" border class="table"
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
            ></el-table-column>
            <el-table-column
                    prop="status"
                    label="状态"
            ></el-table-column>
            <el-table-column
                    prop="visited"
                    label="热度"
            ></el-table-column>
        </el-table>
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
            services: {}
        }
    },
    methods: {
        async getData(params) {
            this.services = await getServiceList(params)
            console.log(this.services)
        }
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