<template>
    <div>
        <el-row :gutter="20">
            <el-col :span="6">
                <el-input v-model.trim="uuid" placeholder="用户uuid"></el-input>
            </el-col>
            <el-col :span="2">
                <el-input v-model.number.trim="points" placeholder="额度"></el-input>
            </el-col>
            <el-col :span="3">
                <el-button @click="add" type="primary">添加积分</el-button>
            </el-col>
        </el-row>
    </div>

</template>
<script>
import {addPoints} from "@/api/api";

export default {
    name: "ServiceList",
    data() {
        return {
            uuid: "",
            points: 0,
        }
    },
    methods: {
        async add() {
            if (this.uuid === "" || this.points === 0) {
                this.$message({
                    message: "uuid不能为空或积分不能为0",
                    type: "warning",
                })
                return
            }
            let data = await addPoints({"uuid": this.uuid, "record": this.points})
            if (data === null) {
                this.$message({
                    message: "添加成功",
                    type: "success",
                })
            }
        }
    }
}
</script>
<style scoped></style>