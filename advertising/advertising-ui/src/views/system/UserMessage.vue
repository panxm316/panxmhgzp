<!--
 * @Author: songly
 * @Date: 2024-01-08 11:12:55
 * @LastEditTime: 2024-02-21 13:58:32
 * @LastEditors: wanghl
 * @Description:消息通知管理
-->

<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex" style="width: 94%">
        <el-button type="primary" icon="Plus" @click="handleRead()">标记已读</el-button>
        <el-button type="danger" icon="Delete" @click="handleDelete()">删除</el-button>
        <HgDateIndex @onresponse="ontime"></HgDateIndex>
        <el-select
          v-model="filters.stype"
          placeholder="请选择类型"
          clearable
          style="width: 120px"
          @change="loaddata"
        >
          <el-option
            v-for="item in messagetypeData"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
        <el-select
          v-model="filters.breaded"
          placeholder="选择状态"
          clearable
          style="width: 120px"
          @change="loaddata"
        >
          <el-option label="未读" :value="0"></el-option>
          <el-option label="已读" :value="1"></el-option>
        </el-select>
        <el-input
          v-model="filters.queryKey"
          placeholder="请输入关键字"
          style="width: 180px"
          @keyup.enter="loaddata"
        >
        </el-input>
        <el-button type="primary" icon="Search" @click="loaddata"></el-button>
      </div>
    </div>
    <div class="table_box">
      <el-row>
        <el-table
          v-loading="listLoading"
          :data="messageData"
          row-key="id"
          :height="tableheight"
          :border="true"
          stripe
          :header-cell-style="tableHeaderColor"
          @selection-change="handleSelectionChange"
          @sort-change="handleSortChange"
        >
          <el-table-column type="selection" prop="ischeck" :width="50" align="center">
          </el-table-column>
          <el-table-column
            prop="stitle"
            label="标题"
            min-width="100"
            sortable
            show-overflow-tooltip
          >
            <template #default="scope">
              <span :title="scope.row.stitle">{{ scope.row.stitle }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="scontent" label="内容" min-width="300" show-overflow-tooltip>
            <template #default="scope">
              <span :title="scope.row.scontent">{{ scope.row.scontent }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="stype" label="类型" width="120" sortable>
            <template #default="scope">
              <span :title="scope.row.stype">{{ scope.row.stype }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="breaded" label="是否已读" width="120" sortable align="center">
            <template #default="scope">
              <span v-if="scope.row.breaded == true">
                <el-tag>已读</el-tag>
              </span>
              <span v-if="scope.row.breaded == false">
                <el-tag type="success">未读</el-tag>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="dcreatetime" label="发送时间" width="160" sortable>
            <template #default="scope">
              <span>{{ formatDate(scope.row.dcreatetime) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            width="120"
            class-name="small-padding fixed-width"
            fixed="right"
            align="center"
          >
            <template #default="scope">
              <div style="padding-left: 10px">
                <el-button
                  link
                  :disabled="scope.row.breaded"
                  type="primary"
                  icon="view"
                  size="small"
                  @click="handleRead(scope.row)"
                  >已读</el-button
                >
                <el-button
                  link
                  type="danger"
                  icon="Delete"
                  size="small"
                  @click="handleDelete(scope.row)"
                  >删除</el-button
                >
              </div>
            </template>
          </el-table-column>
        </el-table>
      </el-row>
      <el-row>
        <el-pagination
          v-model:current-page="pageIndex"
          class="pagination"
          style="margin-left: 10px; width: 100%"
          :page-sizes="pageSizes"
          :page-size="pageSize"
          small
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="pageIndexChange"
        >
        </el-pagination>
      </el-row>
    </div>
  </div>
</template>

<script lang="ts" setup>
import HgDateIndex from '@/components/HgDateIndex/index.vue'
import {
  defineComponent,
  onMounted,
  reactive,
  ref,
  nextTick,
  getCurrentInstance,
  computed
} from 'vue'
import useUserStore from '@/store/modules/user'
import {
  getMessagePageListApi,
  getMessageTypeComboApi,
  deleteMessageInfoByIdApi,
  updateMessageStatusByIdApi
} from '@/api/system/message'
import { IAxios } from 'axios'
import useMessageStore from '@/store/modules/message'
import { TMessageList, TMessageSearch } from '@/types/views/system/message'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import {
  required,
  computTableHeight,
  isNumberStr,
  formatDate,
  tableHeaderColor,
  validatePhone,
  computTreeHeight,
  fenbianlv
} from '@/utils/index'
import { IDataCombo } from '@/types/common/DataCombo'
import modal from '@/plugins/modal'
import useSettingsStore from '@/store/modules/settings' // 切换tab引入
defineOptions({
  name: 'UserMessage'
})
const messageStore = useMessageStore()
const sort = ref('dcreatetime')
const order = ref('desc')
const pageSizes = ref([10, 20, 30, 40])
const pageIndex = ref(1)
const pageSize = ref(20)
const total = ref(0)
const filters = reactive<TMessageSearch>({
  queryKey: '',
  startTime: '',
  endTime: '',
  pageSize: 20,
  page: 1,
  sort: 'dcreatetime',
  order: 'desc',
  stype: '',
  breaded: 0
})
/** 消息列表*/
const messageData = ref<TMessageList[]>([])
/** 选中的行 */
const messageDataSelection = ref<TMessageList[]>()
const listLoading = ref(false)
const tableheight = ref(0)
const messagetypeData = ref<IDataCombo[]>([])
/** 时间 */
const timedata = ref<TDateType>({
  timetype: '6',
  timeoption: [
    {
      label: '今天',
      value: '0'
    },
    {
      label: '三天内',
      value: '2'
    },
    {
      label: '一周内',
      value: '6'
    },
    {
      label: '二周内',
      value: '13'
    },
    {
      label: '一月内',
      value: '30'
    },
    {
      label: '自定义',
      value: '00'
    }
  ],
  startTime: '',
  endTime: ''
})
onMounted(() => {
  tableheight.value = computTableHeight()
  getMessageType()
  loaddata()
})
/**
 * 获取稿件分页列表
 */
const loaddata = () => {
  const data: TMessageSearch = {
    sort: sort.value,
    order: order.value,
    pageSize: pageSize.value,
    page: pageIndex.value,
    startTime: filters.startTime,
    endTime: filters.endTime,
    queryKey: filters.queryKey,
    stype: filters.stype,
    breaded: filters.breaded
  }
  getMessagePageListApi(data).then(({ total: msgTotal, obj }: IAxios<TMessageList[]>) => {
    total.value = msgTotal
    messageData.value = obj
    // 刷新系统消息提醒
    messageStore.getMessageCount(useUserStore().user.userid)
  })
}
/**
 * 获取消息类型下拉列表
 */
const getMessageType = () => {
  getMessageTypeComboApi().then((res: IAxios<IDataCombo[]>) => {
    if (res.success) {
      messagetypeData.value = res.obj
    }
  })
}
/**
 * 选择
 * @param rows
 */
const handleSelectionChange = (rows: TMessageList[]) => {
  messageDataSelection.value = rows
}
/**
 * 排序
 * @param val
 */
const handleSortChange = (val: { prop: string; order: string }) => {
  filters.sort = val.prop

  if (val.order === 'ascending') {
    filters.order = 'asc'
  } else if (val.order === 'descending') {
    filters.order = 'desc'
  } else {
    filters.order = ''
  }
  loaddata()
}
/**
 * 换页
 */
const pageIndexChange = (index: number) => {
  pageIndex.value = index
  loaddata()
}
/**
 * 更换每页条数
 */
const handleSizeChange = (val: number) => {
  pageSize.value = val
  loaddata()
}

const ontime = (val: TDateTimeType) => {
  let timev = false
  if (filters.startTime) {
    timev = true
  }
  filters.startTime = val.startTime
  filters.endTime = val.endTime
  if (timev) {
    loaddata()
  }
}
/** 删除 */
const handleDelete = (row?: TMessageList) => {
  const ids: (string | undefined)[] = []
  if (row) {
    ids.push(row.id!)
  } else {
    messageDataSelection.value?.forEach((item) => ids.push(item.id!))
  }
  if (!ids.length) {
    modal.msgWarning('请选择删除记录')
    return
  }
  modal.confirm('是否删除?').then(() => {
    deleteMessageInfoByIdApi(ids.join(',')).then(({ success, msg }: IAxios) => {
      if (success) {
        modal.msgSuccess('操作成功')
        // 刷新系统消息提醒
        messageStore.getMessageCount(useUserStore().user.userid)
      } else {
        modal.msgError(msg)
      }
      loaddata()
    })
  })
}
/** 标记已读 */
const handleRead = (row?: TMessageList) => {
  const ids: (string | undefined)[] = []
  if (row) {
    ids.push(row.id!)
  } else {
    messageDataSelection.value?.forEach((item) => ids.push(item.id!))
  }
  if (!ids.length) {
    modal.msgWarning('请选择记录')
    return
  }
  modal.confirm('是标记已读?').then(() => {
    updateMessageStatusByIdApi(ids.join(',')).then(({ success, msg }: IAxios) => {
      if (success) {
        modal.msgSuccess('操作成功')
        // 刷新系统消息提醒
        messageStore.getMessageCount(useUserStore().user.userid)
      } else {
        modal.msgError(msg)
      }
      loaddata()
    })
  })
}
</script>

<style scoped></style>
