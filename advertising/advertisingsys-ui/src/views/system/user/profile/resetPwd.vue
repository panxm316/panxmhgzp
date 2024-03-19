<!--
 * @Author: suny
 * @Date: 2023-08-21 14:00:51
 * @LastEditTime: 2023-09-13 16:50:14
 * @LastEditors: wanghl
 * @Description:
-->
<template>
  <el-form ref="pwdRef" :model="user" :rules="rules" label-width="80px">
    <el-form-item label="旧密码" prop="oldPassword">
      <el-input
        v-model="user.oldPassword"
        placeholder="请输入旧密码"
        type="password"
        show-password
      />
    </el-form-item>
    <el-form-item label="新密码" prop="newPassword">
      <el-input
        v-model="user.newPassword"
        placeholder="请输入新密码"
        type="password"
        show-password
      />
    </el-form-item>
    <el-form-item label="确认密码" prop="confirmPassword">
      <el-input
        v-model="user.confirmPassword"
        placeholder="请确认新密码"
        type="password"
        show-password
      />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit">保存</el-button>
      <el-button type="danger" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import { updatePassWordApi } from '@/api/system/user'
import { passwordvalidator, required } from '@/utils'
import { sm2Encrypt } from '@/utils/smcrypto'
import { getCurrentInstance, reactive, ref } from 'vue'
// import { updateUserPwd } from '@/api/system/user'

const { proxy } = getCurrentInstance() as any
/** 密码实体对象 */
const user = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})
/**
 * 验证两次密码是否一致
 * @param rule
 * @param value
 * @param callback
 */
const equalToPassword = (rule: any, value: any, callback: any) => {
  if (user.newPassword !== value) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}
/** 验证规则 */
const rules = ref({
  oldPassword: [
    { validator: required, required: true, message: '旧密码不能为空', trigger: 'blur' }
  ],
  newPassword: [
    { validator: required, required: true, message: '新密码不能为空', trigger: 'blur' },
    { validator: passwordvalidator, message: '长度8位以上至少一位数字一位字母', trigger: 'blur' },
    { max: 80, message: '不得大于80个字符', trigger: 'change' }
  ],
  confirmPassword: [
    { required: true, message: '确认密码不能为空', trigger: 'blur' },
    { required: true, validator: equalToPassword, trigger: 'blur' }
  ]
})

/** 提交按钮 */
const submit = () => {
  proxy.$refs.pwdRef.validate((valid: any) => {
    if (valid) {
      const oldPassword = sm2Encrypt(user.oldPassword)
      const newPassword = sm2Encrypt(user.newPassword)
      updatePassWordApi(oldPassword, newPassword).then((response) => {
        proxy.$modal.msgSuccess('修改成功')
        user.oldPassword = ''
        user.newPassword = ''
        user.confirmPassword = ''
      })
    }
  })
}
/** 关闭按钮 */
const close = () => {
  proxy.$tab.closePage()
}
</script>
<style scoped>
.el-form-item {
  margin: 20px 0;
  width: 60%;
}
</style>
