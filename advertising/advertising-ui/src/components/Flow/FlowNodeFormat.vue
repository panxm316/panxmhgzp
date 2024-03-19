<!--
 * @Author: peij
 * @Date: 2023-10-16 16:20:14
 * @LastEditTime: 2023-10-24 15:36:26
 * @LastEditors: peij
 * @Description:
-->
<template>
  <div style="padding: 10px">
    <el-timeline :reverse="false">
      <el-timeline-item
        v-for="(node, index) in row"
        :key="index"
        size="large"
        :color="node.status != 2 ? (node.status == 1 ? '#F56C6C' : '#67C23A') : '#409EFF'"
        :icon="node.status == 2 ? Check : node.status == 1 ? Plus : Refresh"
      >
        <template v-if="node.selectUser && (!nodeUser[node.id] || nodeUser[node.id].length == 0)">
          <p style="color: red">
            {{ node.name }}
            <template v-if="node.placeholder && node.placeholder.length > 0"
              >[{{ node.placeholder }}]</template
            >
          </p>
        </template>
        <template v-else>
          <p>
            {{ node.name }}
            <template v-if="node.placeholder && node.placeholder.length > 0"
              >[{{ node.placeholder }}]</template
            >
          </p>
        </template>
        <!--					渲染用户头像列表-->
        <!-- <div
          v-if="node.userVoList && node.userVoList.length > 0"
          style="display: flex; flex-direction: row; flex-wrap: wrap"
        >
          <div
            v-for="(item1, index1) in node.userVoList"
            :key="index1"
            class="box-card"
            style="margin-bottom: 10px; border: 0px solid red; width: 40px; text-align: center"
          >
            <div class="node-show">
              <div style="overflow: hidden">
                <div class="d1">
                  <div>
                    <el-avatar shape="square" :size="30" :src="item1.avatar"></el-avatar>
                  </div>
                  <div
                    style="
                      font-size: 8px;
                      overflow: hidden;
                      white-space: nowrap;
                      text-overflow: ellipsis;
                      text-align: center;
                    "
                  >
                    {{ item1.name }}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div> -->
        <!--					渲染审批评论-->
        <template v-for="item in node.userVoList" :key="item.id">
          <template v-if="item?.approveDesc && item?.approveDesc?.length > 0">
            <div style="display: flex; flex-direction: row">
              <div style="width: 40px; text-align: center">
                <el-avatar
                  shape="square"
                  :size="30"
                  :src="item.avatar === '' ? getAssetsImage('userimg.jpg') : item.avatar"
                ></el-avatar>
              </div>
              <div style="height: 40px; line-height: 40px; font-size: 10px">
                {{ item.name }}
              </div>
              <div style="height: 40px; line-height: 40px; font-size: 10px">(添加了评论)</div>
            </div>
            <div
              class="box-card"
              style="
                margin-bottom: 10px;
                padding: 5px;
                background-color: var(--el-fill-color-light);
              "
            >
              {{ item.approveDesc }}
            </div>
          </template>
        </template>
        <!--					选择用户-->
        <template v-if="node.selectUser">
          <select-show
            v-model:orgList="nodeUser[node.id]"
            :disabled="disableSelect"
            type="user"
            :multiple="node.multiple"
          ></select-show>
        </template>

        <el-tabs v-if="node.branch && node.branch.length > 0" type="border-card">
          <el-tab-pane
            v-for="(node1, index1) in node.branch"
            :key="index1"
            :label="'分支' + (index1 + 1)"
            :name="index1 + ''"
          >
            <template v-if="node1.placeholder && node1.placeholder.length > 0"
              >[{{ node1.placeholder }}]</template
            >
            <div style="padding: 0px 5px">
              <flow-node-format
                :node-user="nodeUser"
                :disable-select="disableSelect"
                :row="node1.children"
              ></flow-node-format>
            </div>
          </el-tab-pane>
        </el-tabs>
        <div
          v-for="item in node.children"
          :key="item.id"
          style="line-height: 36px; font-size: 15px"
        >
          <el-row>
            <el-col :span="2"
              ><el-icon
                size="large"
                :color="item.status != 2 ? (item.status == 1 ? '#F56C6C' : '#67C23A') : '#409EFF'"
                style="padding-top: 16px; position: absolute"
                ><component
                  :is="
                    item.status == 2
                      ? 'circle-check-filled'
                      : item.status == 1
                      ? 'circle-plus-filled'
                      : 'circle-refresh-filled'
                  "
                ></component>
              </el-icon>
            </el-col>
            <el-col :span="22">
              <span>{{ item.name }}</span
              ><span style="margin-left: 5px">[{{ item.placeholder }}]</span>
            </el-col>
          </el-row>
          <template v-for="user in item.userVoList" :key="user.id">
            <template v-if="user?.approveDesc && user?.approveDesc?.length > 0">
              <div style="display: flex; flex-direction: row">
                <div style="width: 40px; text-align: center">
                  <el-avatar
                    shape="square"
                    :size="30"
                    :src="user.avatar === '' ? getAssetsImage('userimg.jpg') : user.avatar"
                  ></el-avatar>
                </div>
                <div style="height: 40px; line-height: 40px; font-size: 10px">
                  {{ user.name }}
                </div>
                <div style="height: 40px; line-height: 40px; font-size: 10px">(添加了评论)</div>
              </div>
              <div
                class="box-card"
                style="
                  margin-bottom: 10px;
                  padding: 5px;
                  background-color: var(--el-fill-color-light);
                "
              >
                {{ user.approveDesc }}
              </div>
            </template>
          </template>
        </div>
      </el-timeline-item>
    </el-timeline>
  </div>
</template>

<script setup lang="ts">
import { Check, Plus, Refresh } from '@element-plus/icons-vue'
import { TNodeVo } from '@/types/views/flow/index'
import selectShow from '@/views/flow/workflow/components/dialog/selectAndShow.vue'
import { getAssetsImage } from '@/utils/index'

const props = withDefaults(
  defineProps<{
    nodeUser: { [key: string]: any }
    row: TNodeVo[]
    disableSelect: boolean
  }>(),
  {
    nodeUser: () => ({} as { [key: string]: any }),
    row: () => [],
    disableSelect: false
  }
)
</script>

<style scoped lang="scss"></style>
