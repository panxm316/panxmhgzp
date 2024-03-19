<!--
 * @Author: suny
 * @Date: 2023-08-21 14:00:51
 * @LastEditTime: 2023-09-13 16:52:00
 * @LastEditors: wanghl
 * @Description:
-->
<template>
  <div class="app-container userbox">
    <el-row :gutter="20">
      <el-col :xs="10" :sm="8" :md="8" :lg="6" :xl="6">
        <el-card class="box-card userrightbox">
          <template #header>
            <div class="clearfix">
              <span>个人信息</span>
            </div>
          </template>
          <div>
            <div class="text-center">
              <userAvatar :user="user" />
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <svg-icon icon-class="user" />用户名称
                <div class="pull-right">{{ user?.susername }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="phone" />手机号码
                <div class="pull-right">{{ user?.sphone }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="email" />用户邮箱
                <div class="pull-right">{{ user?.semail }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="tree" />所属部门
                <div v-if="user?.deptName" class="pull-right">
                  {{ user.deptName }}
                </div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="peoples" />所属角色
                <div class="pull-right">{{ user?.roleNames }}</div>
              </li>
              <!-- <li class="list-group-item">
                <svg-icon icon-class="date" />创建日期
                <div class="pull-right">{{ state.user.createTime }}</div>
              </li> -->
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="14" :sm="16" :md="16" :lg="18" :xl="18">
        <el-card class="userrightbox">
          <template #header>
            <div class="clearfix">
              <span>基本资料</span>
            </div>
          </template>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userinfo">
              <userInfo :user="user" @setuserinfo="getEmployInfo" />
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <resetPwd />
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Profile" lang="ts">
import { ref, reactive } from 'vue'
import userAvatar from './userAvatar.vue'
import userInfo from './userInfo.vue'
import resetPwd from './resetPwd.vue'
import { IAxios } from 'axios'
import { getEmployInfoApi } from '@/api/system/user'
import { TSEmployVO } from '@/types/views/system/employ'
import { sm4Decrypt } from '@/utils/smcrypto'
const activeTab = ref('userinfo')
/** 用户身份信息 */
const user = ref<TSEmployVO>()
onMounted(() => {
  getEmployInfo()
})
/**
 * 页面高度
 */
const boxheight = ref(window.innerHeight - 45 + 'px')
const rightheight = ref(window.innerHeight - 85 + 'px')
/** 获取当前人员信息 */
const getEmployInfo = () => {
  getEmployInfoApi().then((res: IAxios<TSEmployVO>) => {
    if (res.success) {
      if (res.obj.sphone !== undefined) {
        res.obj.sphone = sm4Decrypt(res.obj.sphone.trim())
      }
      if (res.obj.semail !== undefined) {
        res.obj.semail = sm4Decrypt(res.obj.semail.trim())
      }
      user.value = res.obj
    }
  })
}
</script>
<style scoped>
.userbox {
  height: v-bind(boxheight);
}
.userrightbox {
  height: v-bind(rightheight);
}
.el-card {
  box-shadow: none;
}
.list-group-item {
  height: 46px;
}
</style>
