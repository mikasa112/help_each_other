<template>
    <el-card class="box-card">
        <el-table :data="orders.list" border class="table"
                  highlight-current-row
                  :cell-style="{ textAlign: 'center' }"
                  :header-cell-style="{ textAlign: 'center' }"
                  height="540">
            <el-table-column
                    prop="orderId"
                    label="订单ID"
                    show-overflow-tooltip
            ></el-table-column>
            <el-table-column
                    prop="serviceId"
                    label="服务ID"
                    show-overflow-tooltip
            >
                <template v-slot="scope">
                    <a href="javascript:void(0)">{{ scope.row.serviceId }}</a>
                </template>
            </el-table-column>
            <el-table-column
                    label="顾客UUID"
                    show-overflow-tooltip
            >
                <template v-slot="scope">
                    <a href="javascript:void(0)"
                       @click.stop="routeToUser(scope.row.customerUuid) ">{{ scope.row.customerUuid }}</a>
                </template>
            </el-table-column>
            <el-table-column
                    prop="providerUuid"
                    label="接单人UUID"
                    show-overflow-tooltip
            >
                <template v-slot="scope">
                    <a href="javascript:void(0)"
                       @click.stop="routeToUser(scope.row.providerUuid)">{{ scope.row.providerUuid }}</a>
                </template>
            </el-table-column>
            <el-table-column
                    label="订单状态"
            >
                <template v-slot="scope">
                    <el-tag
                            :type="chooseStatus(scope.row.status)">
                        {{ scope.row.status }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column
                    prop="pay"
                    label="订单支付状态"
            ></el-table-column>
            <el-table-column
                    prop="startAt"
                    show-overflow-tooltip
                    label="订单开始时间"
                    sortable
            ></el-table-column>
            <el-table-column
                    prop="endAt"
                    show-overflow-tooltip
                    label="订单结束时间"
                    sortable
            ></el-table-column>
            <el-table-column
                    label="评价"
                    sortable
            >
                <template v-slot="scope">
                    <el-rate :colors="colors" v-model="scope.row.evaluate" disabled></el-rate>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
                style="margin-top: 25px"
                :total="orders.total"
                @current-change="handleCurrentChange"
                :page-size="orders.pageSize"
                layout="total, prev, pager, next, jumper"
        >
        </el-pagination>
    </el-card>
</template>

<script>
import {getOrders} from "@/api/api";

export default {
    name: "OrderList",
    data() {
        return {
            status: ["等待顾客同意", "正在进行", "已完成", "异常", "取消"],
            statusColor: ["success", "warning", "danger"],
            pay: ["未支付", "已支付"],
            page: 1,
            pageSize: 10,
            orders: [],
            colors: ['#99A9BF', '#F7BA2A', '#FF9900']
        }
    },
    methods: {
        async getData(params) {
            let data = await getOrders(params)
            this.orders = data
            this.orders.list = data.list.filter(order => {
                order.status = this.status[order.status]
                order.pay = this.pay[order.pay]
                return order
            })
        },
        //分页切换到当前页时的回调
        handleCurrentChange(currentPage) {
            this.page = currentPage;
            this.getData(this.params);
        },
        routeToUser(uuid) {
            this.$router.push({name: "updateUser", params: {uuid: uuid}})
        },
        chooseStatus(status) {
            if (status === "已完成") {
                return this.statusColor[0];
            } else if (status === "异常") {
                return this.statusColor[1];
            } else if (status === "取消") {
                return this.statusColor[2];
            }
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
a {
    color: #606266;
    text-decoration: none;
}

a:hover {
    color: #409EFF;
    text-decoration: underline;
}
</style>