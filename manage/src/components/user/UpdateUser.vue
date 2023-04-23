<template>
    <el-card class="box-card">
        <el-form ref="form" :model="user" label-width="auto" label-position="left" :rules="rules">
            <el-col :span="8">
                <el-form-item label="UUID" prop="username">
                    <el-input v-model="user.uuid" disabled></el-input>
                </el-form-item>
                <el-form-item label="用户名" prop="username">
                    <el-input v-model="user.username" disabled></el-input>
                </el-form-item>
                <el-form-item label="原密码" prop="password">
                    <el-input v-model="user.password" show-password></el-input>
                </el-form-item>
                <el-form-item label="用户权限">
                    <el-col :span="8">
                        <el-select v-model="user.role" placeholder="请选择用户权限" disabled>
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
                <el-form-item label="用户邮箱" prop="email">
                    <el-input v-model="user.email"></el-input>
                </el-form-item>
            </el-col>
            <el-col :span="8" style="margin-left: 100px">
                <el-form-item label="用户号码" prop="phone">
                    <el-input v-model="user.phone"></el-input>
                </el-form-item>
                <el-form-item label="头像">
                    <el-col :span="1">
                        <el-button
                                size="small"
                                @click="dialogVisible = true"
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

                <el-form-item label="用户年龄" prop="age">
                    <el-input v-model.number="user.age"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="update(user,copyInfo)">更新</el-button>
                </el-form-item>
            </el-col>
        </el-form>
    </el-card>
</template>

<script>
import AvatarCropper from "@/components/AvatarCropper.vue";
import {updateUserInfo, uploadApi} from "@/api/api";

export default {
    name: "UpdateUser",
    components: {AvatarCropper},
    data() {
        return {
            dialogVisible: false,
            user: {},
            copyInfo: {},
            rules: {
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
        async update(user, info) {
            let changeInfo = {}
            if (user.password !== info.password)
                changeInfo["password"] = user.password
            if (user.nickname !== info.nickname)
                changeInfo["nickname"] = user.nickname
            if (user.sex !== info.sex)
                changeInfo["sex"] = user.sex
            if (user.avatar !== info.avatar)
                changeInfo["avatar"] = user.avatar
            if (user.email !== info.email)
                changeInfo["email"] = user.email
            if (user.phone !== info.phone)
                changeInfo["phone"] = user.phone
            if (user.age !== info.age)
                changeInfo["age"] = user.age
            await updateUserInfo(user.uuid, changeInfo)
            this.$notify({
                title: "成功",
                message: "更新成功",
                type: "success",
            });
        },
        getData() {
            if (this.$route.params) {
                this.user = this.$route.params
                //将原本的数据复制一下
                this.copyInfo = {
                    password: this.user.password,
                    nickname: this.user.nickname,
                    sex: this.user.sex,
                    avatar: this.user.avatar,
                    email: this.user.email,
                    phone: this.user.phone,
                    age: this.user.age
                };
            }
        },
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
    },
    mounted() {
        this.getData()
    },
}
</script>

<style scoped>

</style>