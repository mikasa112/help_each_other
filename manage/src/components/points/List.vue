<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-input v-model.trim="uuid" placeholder="用户uuid"></el-input>
      </el-col>
      <el-col :span="2">
        <el-input v-model.number.trim="point" placeholder="额度"></el-input>
      </el-col>
      <el-col :span="3">
        <el-button @click="add" type="primary">添加积分</el-button>
      </el-col>
    </el-row>
    <el-card class="box-card" style="margin-top: 25px">
      <el-table :data="points.list" border class="table"
                highlight-current-row
                :cell-style="{ textAlign: 'center' }"
                :header-cell-style="{ textAlign: 'center' }"
                height="476">
        <el-table-column
            prop="uuid"
            label="用户UUID"
            show-overflow-tooltip
        ></el-table-column>
        <el-table-column
            prop="orderId"
            label="订单ID"
            show-overflow-tooltip
        ></el-table-column>
        <el-table-column
            prop="record"
            label="积分记录"
            sortable
        ></el-table-column>
        <el-table-column
            prop="remark"
            label="备注"
        ></el-table-column>
      </el-table>
      <el-pagination
          style="margin-top: 25px"
          :total="points.total"
          @current-change="handleCurrentChange"
          :page-size="points.pageSize"
          layout="total, prev, pager, next, jumper"
      >
      </el-pagination>
    </el-card>
  </div>

</template>
<script>
import {addPoints, getPoints} from "@/api/api";

export default {
  name: "ServiceList",
  data() {
    return {
      uuid: "",
      point: 0,
      page: 1,
      pageSize: 10,
      points: [],
    }
  },
  methods: {
    async add() {
      if (this.uuid === "" || this.point === 0) {
        this.$message({
          message: "uuid不能为空或积分不能为0",
          type: "warning",
        })
        return
      }
      let data = await addPoints({"uuid": this.uuid, "record": this.point})
      if (data === null) {
        this.$message({
          message: "添加成功",
          type: "success",
        })
      }
    },
    //分页切换到当前页时的回调
    handleCurrentChange(currentPage) {
      this.page = currentPage;
      this.getData();
    },
    async getData() {
      this.points = await getPoints(this.params)
    }
  },
  computed: {
    params() {
      return `page=${this.page}&pageSize=${this.pageSize}`;
    }
  },
  created() {
    this.getData()
  }
}
</script>
<style scoped></style>