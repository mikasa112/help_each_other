<template>
    <div class="container">
        <div class="loginBox animate__animated animate__jello">
            <el-form
                    @submit.native.prevent
                    style="margin-top: 38px"
                    :model="user"
                    :rules="rules"
                    ref="ruleForm"
            >
                <el-form-item class="inputBox" prop="username">
                    <el-input
                            prefix-icon="el-icon-user"
                            v-model="user.username"
                            placeholder="用户名"
                    ></el-input>
                </el-form-item>
                <el-form-item class="inputBox" prop="password">
                    <el-input
                            prefix-icon="el-icon-lock"
                            v-model="user.password"
                            type="password"
                            @keydown.enter.native.stop="login"
                            placeholder="密码"
                    ></el-input>
                </el-form-item>
                <el-checkbox label="记住我" class="rememberMe" v-model="user.remember"></el-checkbox>
                <el-form-item class="btnGroup">
                    <el-button size="small" type="primary" @click="login">登录</el-button>

                    <el-button size="small" style="margin-left: 50px" @click="cancel"
                    >取消
                    </el-button
                    >
                </el-form-item>
                <Vcode
                        :puzzleScale="0.8"
                        :sliderSize="30"
                        :range="5"
                        :show="isShow"
                        @success="onSuccess"
                        @close="onClose"
                />
                `
            </el-form>
        </div>
    </div>
</template>

<script>
import "animate.css";
import {loginApi} from "@/api/api";
import Vcode from "vue-puzzle-vcode";

export default {
    name: "login_view",
    data() {
        return {
            isShow: false,
            user: {
                username: "",
                password: "",
                remember: false,
            },
            rules: {
                username: [
                    {required: true, message: "请输入用户名", trigger: "blur"},
                    {
                        min: 3,
                        max: 21,
                        message: "长度在 3 到 21 个字符",
                        trigger: "blur",
                    },
                ],
                password: [
                    {required: true, message: "请输入密码", trigger: "blur"},
                    {
                        min: 8,
                        max: 26,
                        message: "长度在 8 到 26 个字符",
                        trigger: "blur",
                    },
                ],
            },
        };
    },
    components: {Vcode},
    methods: {
        login() {
            this.$refs["ruleForm"].validate((valid) => {
                if (valid) {
                    this.isShow = true;
                } else {
                    return false;
                }
            });
        },
        cancel() {
            this.$refs["ruleForm"].resetFields();
        },
        onSuccess() {
            this.isShow = false;
            loginApi(this.user)
                .then((res) => {
                    sessionStorage.setItem("token", res.data);
                    // this.$router.replace("admin");
                })
                .catch(() => {
                });
        },
        onClose() {
            this.isShow = false;
        },
    },
};
</script>

<style scoped>
.container {
    height: 100%;
    width: 100%;
    background-color: #39c5bb;
    position: absolute;
    display: flex;
    justify-content: center;
    align-items: center;
}

.loginBox {
    background-color: #fff;
    width: 380px;
    height: 260px;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 8px;
    box-shadow: 5px 5px 5px rgba(0, 0, 0, 0.2);
}

.inputBox {
    width: 320px;
}

.rememberMe {
    margin-bottom: 5px;
    text-align: center;;
}

.btnGroup {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 0;
    margin: 0;
}
</style>