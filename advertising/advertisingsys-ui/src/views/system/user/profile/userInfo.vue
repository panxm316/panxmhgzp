<!--
 * @Author: suny
 * @Date: 2023-08-21 14:00:51
 * @LastEditTime: 2023-09-15 09:29:06
 * @LastEditors: suny
 * @Description:
-->
<template>
  <el-form ref="userRef" :model="emp" :rules="rules" label-width="80px">
    <el-form-item label="用户名称" prop="susename">
      <el-input v-model="emp.susername" maxlength="30" />
    </el-form-item>
    <el-form-item label="手机号码" prop="sphone">
      <el-input v-model="emp.sphone" maxlength="11" />
    </el-form-item>
    <el-form-item label="邮箱" prop="semail">
      <el-input v-model="emp.semail" maxlength="50" />
    </el-form-item>
    <el-form-item label="性别">
      <el-radio-group v-model="emp.bsex">
        <el-radio label="0">男</el-radio>
        <el-radio label="1">女</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit">保存</el-button>
      <el-button type="danger" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import { updatePersonSettingApi } from '@/api/system/user'
import { ILoginUser } from '@/store/modules/type'
import { TSEmployVO } from '@/types/views/system/employ'
import { IAxios } from 'axios'
import { getCurrentInstance, ref } from 'vue'
import { required } from '@/utils'
import { sm4Encrypt } from '@/utils/smcrypto'
import useUserStore from '@/store/modules/user'
interface IProps {
  user: TSEmployVO | undefined
}
const props = defineProps<IProps>()

const { proxy } = getCurrentInstance() as any
/** 当前人员信息 */
const userStore = useUserStore()
/** 校对规则 */
const rules = ref<any>({
  susername: [
    { validator: required, required: true, message: '用户名称不能为空', trigger: 'blur' }
  ],
  semail: [
    {
      type: 'email',
      message: '请输入正确的邮箱地址',
      trigger: ['blur', 'change']
    }
  ],
  sphone: [
    { required: true, message: '手机号码不能为空', trigger: 'blur' },
    {
      pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
      message: '请输入正确的手机号码',
      trigger: 'blur'
    }
  ]
})
/** 调用父组件方法 */
const emit = defineEmits<{
  setuserinfo: []
}>()
/** 当前修改人员信息 */
const emp = ref({
  susername: props.user?.susername,
  sphone: props.user?.sphone?.trim(),
  semail: props.user?.semail,
  bsex: props.user?.bsex === undefined || props.user.bsex === false ? '0' : '1'
})
/** 监听当前人员信息 */
watch(
  () => props.user,
  (val) => {
    console.log(val)
    emp.value = {
      susername: props.user?.susername,
      sphone: props.user?.sphone?.trim(),
      semail: props.user?.semail,
      bsex: props.user?.bsex === undefined || props.user.bsex === false ? '0' : '1'
    }
  }
)
/** 提交按钮 */
const submit = () => {
  proxy.$refs.userRef.validate((valid: any) => {
    if (valid) {
      const data = {
        id: props.user?.id,
        susername: emp.value.susername,
        sphone: sm4Encrypt(emp.value.sphone as string),
        semail:
          emp.value.semail === undefined || emp.value.semail.trim() === ''
            ? ''
            : sm4Encrypt(emp.value.semail as string),
        bsex: emp.value.bsex !== '0'
      } as TSEmployVO
      updatePersonSettingApi(data).then((res: IAxios) => {
        if (res.success) {
          proxy.$modal.msgSuccess('修改成功')
          userStore.user!.username = emp.value.susername as string
          userStore.user!.phone = emp.value.sphone
          userStore.user!.semail = emp.value.semail
          userStore.user!.bsex = emp.value.bsex !== '0'
          emit('setuserinfo')
        }
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
