<template>
    <div class="category-list">
        <el-tag
                :key="category.id"
                v-for="category in categories"
                closable
                effect="dark"
                :disable-transitions="false"
                @close="handleClose(category.id)">
            {{ category.category }}
        </el-tag>
        <el-input
                class="input-new-tag"
                v-if="inputVisible"
                v-model="category.category"
                ref="saveTagInput"
                size="small"
                @keyup.enter.native="createCategory(category)"
                @blur="createCategory(category)"
        >
        </el-input>
        <el-button v-else class="button-new-tag" size="small" @click="showInput">+ 新建分类</el-button>
    </div>
</template>

<script>
import {addCategory, getCategories, removeCategoryById} from "@/api/api";

export default {
    name: "CategoryList",
    data() {
        return {
            inputVisible: false,
            category: {
                category: "",
            },
            categories: {},
        }
    },
    methods: {
        async getData() {
            this.categories = await getCategories()
        },
        handleClose(id) {
            this.$confirm("是否删除此分类?", '提示', {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(async () => {
                await removeCategoryById(id)
                await this.getData()
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
        },
        async createCategory(data) {
            if (!data.category) {
                return;
            }
            let isExist = false;
            this.categories.forEach(e => {
                if (e.category === data.category) {
                    this.$message.warning("该分类已存在")
                    this.clear()
                    isExist = true;
                }
            })
            if (!isExist) {
                await addCategory(data)
                this.$message.success("添加成功")
                this.clear()
                await this.getData()
            }
        },
        clear() {
            this.inputVisible = false;
            this.category.category = "";
        },
        showInput() {
            this.inputVisible = true;
            this.$nextTick(() => {
                this.$refs.saveTagInput.$refs.input.focus();
            });
        }
    },
    mounted() {
        this.getData()
    }
}
</script>

<style scoped>
.category-list {
    margin-top: 30px;
    margin-left: 30px;
    display: flex;
}

.el-tag + .el-tag {
    margin-left: 10px;
}

.button-new-tag {
    margin-left: 10px;
    height: 32px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
}

.input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
}
</style>