<template>
    <el-card class="box-card" style="height: 640px">
        <el-form ref="form" :model="user" label-width="auto" label-position="left" :rules="rules">
            <el-col :span="8">
                <el-form-item label="用户名" prop="username">
                    <el-input v-model="user.username"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model="user.password" show-password></el-input>
                </el-form-item>
                <el-form-item label="确认密码" prop="repwd">
                    <el-input v-model="user.repwd" show-password></el-input>
                </el-form-item>
                <el-form-item label="用户权限">
                    <el-col :span="8">
                        <el-select v-model="user.role" placeholder="请选择用户权限">
                            <el-option label="管理员" value="admin"></el-option>
                            <el-option label="普通用户" value="user"></el-option>
                        </el-select>
                    </el-col>
                </el-form-item>
                <el-form-item label="用户昵称" prop="nickname">
                    <el-input v-model="user.nickname"></el-input>
                </el-form-item>
                <el-form-item label="用户性别">
                    <el-col :span="8">
                        <el-radio-group v-model="user.sex">
                            <el-radio label="男"></el-radio>
                            <el-radio label="女"></el-radio>
                        </el-radio-group>
                    </el-col>
                </el-form-item>
                <el-form-item label="头像">
                    <el-col :span="1">
                        <el-button
                                size="small"
                                @click="dialogVisible = true"
                                v-show="!user.avatar"
                        >上传头像
                        </el-button
                        >
                        <el-avatar shape="square" v-show="user.avatar" :size="150" fit="fill"
                                   :src="user.avatar"></el-avatar>
                        <AvatarCropper
                                :dialogVisible.sync="dialogVisible"
                                @closeAvatarDialog="closeAvatarDialog"
                        ></AvatarCropper>
                    </el-col>
                </el-form-item>
            </el-col>
            <el-col :span="8" style="margin-left: 100px">
                <el-form-item label="用户邮箱" prop="email">
                    <el-input v-model="user.email"></el-input>
                </el-form-item>
                <el-form-item label="用户号码" prop="phone">
                    <el-input v-model="user.phone"></el-input>
                </el-form-item>
                <el-form-item label="用户年龄" prop="age">
                    <el-input v-model.number="user.age"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="add">添加</el-button>
                </el-form-item>
            </el-col>
        </el-form>
    </el-card>
</template>

<script>
import AvatarCropper from "@/components/util/AvatarCropper.vue";
import {registerApi, uploadApi} from "@/api/api";

export default {
    name: "AddUser",
    components: {AvatarCropper},
    data() {
        let checkPwd = (rule, value, callback) => {
            if (value === "") {
                return callback(new Error("重复密码不能为空"));
            } else if (value !== this.user.password) {
                return callback(new Error("两次输入的密码不一致"));
            } else if (value.length < 8 || value.length > 26) {
                return callback(new Error("长度在8到26个字符之间"));
            } else {
                callback();
            }
        };
        return {
            dialogVisible: false,
            user: {
                username: "",
                password: "",
                repwd: "",
                role: "user",
                nickname: "",
                sex: "男",
                avatar: "",
                email: "",
                phone: "",
                age: 18,
            },
            rules: {
                username: [
                    {required: true, message: "用户名不能为空", trigger: "blur"},
                    {
                        min: 3,
                        max: 21,
                        message: "长度在 3 到 21 个字符",
                        trigger: "blur",
                    },
                ],
                password: [
                    {required: true, message: "密码不能为空", trigger: "blur"},
                    {
                        min: 8,
                        max: 26,
                        message: "长度在 8 到 26 个字符",
                        trigger: "blur",
                    },
                ],
                repwd: [
                    {
                        validator: checkPwd,
                        trigger: "blur",
                    },
                ],
                nickname: [{max: 21, message: "长度最大为21个字符", trigger: "blur"}],
                age: [{type: "number", message: "年龄必须为数字值"}],
                email: [{type: "email", message: "邮箱格式不正确", trigger: "blur"}],
                phone: [
                    {
                        message: "手机号格式不正确",
                        trigger: "blur",
                        pattern:
                            /^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/,
                    },
                ],
            },
        }
    },
    methods: {
        async closeAvatarDialog(data) {
            let fileType = data.type.split('/')[1]
            let fileName = Date.now() + "." + fileType;
            let f = new File([data], fileName, {
                type: data.type,
                lastModified: Date.now(),
            });
            //上传图片并将成功返回的路径赋值给user
            let msg = await uploadApi(f);
            this.user.avatar = msg.data;
        },
        add() {
            this.$refs["form"].validate(async (valid) => {
                if (valid) {
                    let user = {
                        username: this.user.username,
                        password: this.user.password,
                        role: this.user.role,
                    }
                    if (this.user.nickname !== "") {
                        user["nickname"] = this.user.nickname
                    }
                    if (this.user.avatar !== "") {
                        user["avatar"] = this.user.avatar
                    }
                    if (this.user.email !== "") {
                        user["email"] = this.user.email
                    }
                    if (this.user.phone !== "") {
                        user["phone"] = this.user.phone
                    }
                    if (this.user.age !== "") {
                        user["age"] = this.user.age
                    }
                    if (this.user.sex !== "") {
                        user["sex"] = this.user.sex
                    }
                    await registerApi(user)
                    this.$notify({
                        title: "成功",
                        message: "用户信息注册成功",
                        type: "success",
                    });
                }
            });
        }
    }
}
</script>

<style scoped>
.box-card {
}

</style>