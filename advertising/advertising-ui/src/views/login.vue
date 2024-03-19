<!--
 * @Author: wanghl
 * @Date: 2023-08-08 09:39:16
 * @LastEditTime: 2023-09-21 08:31:05
 * @LastEditors: wanghl
 * @Description: login
-->
<template>
  <div class="login">
    <div class="login_gg"></div>
    <el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="login-form">
      <h3 class="title text-success">全媒体运营管理平台</h3>
      <el-form-item prop="username">
        <el-input
          ref="refUserName"
          v-model="loginForm.username"
          type="text"
          size="large"
          auto-complete="off"
          placeholder="账号"
        >
          <template #prefix
            ><svg-icon icon-class="user" class="el-input__icon input-icon"
          /></template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          size="large"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter="handleLogin"
        >
          <template #prefix
            ><svg-icon icon-class="password" class="el-input__icon input-icon"
          /></template>
        </el-input>
      </el-form-item>
      <el-form-item v-if="captchaEnabled" prop="code">
        <el-input
          v-model="loginForm.code"
          size="large"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter="handleLogin"
        >
          <template #prefix
            ><svg-icon icon-class="validCode" class="el-input__icon input-icon"
          /></template>
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" class="login-code-img" @click="getCode" />
        </div>
      </el-form-item>
      <!-- <el-checkbox v-model="loginForm.rememberMe" style="margin: 0px 0px 25px 0px"
        >记住密码</el-checkbox
      > -->
      <el-form-item style="width: 100%">
        <el-button
          :loading="loading"
          size="large"
          type="primary"
          style="width: 100%"
          @click.prevent="handleLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>技术支持，潍坊北大青鸟华光照排有限公司</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ElInput, FormRules } from 'element-plus'
import { useRouter } from 'vue-router'
import { getCodeImg } from '@/api/login'
import Cookies from 'js-cookie'
import useUserStore from '@/store/modules/user'
import { lodashFunc } from '@/utils/ruoyi'

defineOptions({
  name: 'Login'
})

const userStore = useUserStore()
const router = useRouter()
const { proxy } = getCurrentInstance() as any

const loginForm: any = ref({
  username: '',
  password: '',
  rememberMe: false,
  code: '',
  uuid: ''
})

const loginRules = reactive<FormRules<any>>({
  username: [{ required: true, trigger: 'blur', message: '请输入您的账号' }],
  password: [{ required: true, trigger: 'blur', message: '请输入您的密码' }],
  code: [{ required: true, trigger: 'change', message: '请输入验证码' }]
})

const codeUrl = ref('')
const loading = ref(false)
/**
 * 验证码开关
 */
const captchaEnabled = ref(true)
const redirect = ref(undefined)
const refUserName = ref<InstanceType<typeof ElInput>>()

/**
 * login
 */
const handleLogin = () => {
  proxy.$refs.loginRef.validate((valid: any) => {
    if (valid) {
      loading.value = true
      Cookies.set('Advertising-UserName', loginForm.value.username, { expires: 30 })
      // 调用action的登录方法
      userStore
        .login(loginForm.value)
        .then((data) => {
          if (data.success) {
            router.push({ path: redirect.value || '/' })
          } else {
            loginForm.value.password = ''
            loginForm.value.code = ''
            refUserName.value?.focus()
            loading.value = false
            // 重新获取验证码
            if (captchaEnabled.value) {
              getCode()
            }
          }
        })
        .catch(() => {
          loginForm.value.password = ''
          loginForm.value.code = ''
          refUserName.value?.focus()
          loading.value = false
          // 重新获取验证码
          if (captchaEnabled.value) {
            getCode()
          }
        })
    }
  })
}

/**
 * 验证码
 */
const getCodeBase64 = () => {
  getCodeImg().then((res: any) => {
    captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled
    if (captchaEnabled.value) {
      const data = res.obj
      codeUrl.value = 'data:image/gif;base64,' + data.img
      loginForm.value.uuid = data.uuid
    }
  })
}

/**
 * 验证码防抖，设置每700毫秒才能点击一次
 */
const getCode = lodashFunc(getCodeBase64, 700)

/**
 * getCookie
 */
const getCookie = () => {
  const username = Cookies.get('Advertising-UserName')
  const password = Cookies.get('password')
  const rememberMe = Cookies.get('rememberMe')
  loginForm.value = {
    username: username === undefined ? loginForm.value.username : username,
    password: password === undefined ? loginForm.value.password : password,
    rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
  }
}

getCode()
getCookie()
</script>

<style lang="scss" scoped>
.login {
  display: flex;
  justify-content: space-around;
  align-items: center;
  height: 100%;
  background-image: url('../assets/images/login-background.jpg');
  background-size: cover;
}
.login_gg {
  width: 600px;
  height: 350px;
  background-image: url('../assets/images/login_gg.png');
  margin-top: -100px;
  margin-left: 10%;
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  font-size: 20px;
  color: var(--el-color-primary);
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;
  margin-top: -100px;
  margin-right: 10%;
  .el-input {
    height: 40px;
    input {
      height: 40px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 0px;
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 40px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
.login-code-img {
  height: 40px;
  padding-left: 12px;
}
.el-form-item {
  margin-bottom: 28px;
}
</style>
