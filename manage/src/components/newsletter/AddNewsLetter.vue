<template>
    <el-card>
        <TinyMCEComponent ref="edit" v-model="data.content"></TinyMCEComponent>
        <el-button class="addBtn" type="primary" :disabled="!data.content" @click="add()">添加</el-button>
    </el-card>
</template>

<script>
import TinyMCEComponent from "@/components/util/TinyMCEComponent.vue";
import {addNewsLetter} from "@/api/api";

export default {
    name: "AddNewsLetter",
    components: {TinyMCEComponent},
    data() {
        return {
            data: {
                content: null
            }
        }
    },
    methods: {
        async add() {
            await addNewsLetter(this.data)
            this.$notify({
                title: "成功",
                message: "添加成功",
                type: "success",
            });
        }
    }
}
</script>

<style scoped>
.addBtn {
    margin-top: 30px;
}
</style>