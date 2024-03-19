<!--
 * @Author: suny
 * @Date: 2023-10-12 08:38:32
 * @LastEditTime: 2024-01-25 09:44:35
 * @LastEditors: songly
 * @Description:流程列表
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <!-- <el-button type="primary" icon="Plus" @click="onAdd">新增</el-button> -->
        <!-- <el-button type="danger" icon="delete" @click="onDelete(undefined)">删除</el-button> -->
        <el-select
          v-model="curFlowTypeId"
          placeholder="请选择流程组"
          clearable
          @change="getFlowPageList"
          @clear="getFlowPageList"
        >
          <el-option v-for="item in flowTypes" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
        <el-input
          v-model="queryKey"
          clearable
          placeholder="请输入关键字"
          class="input-with-select"
          style="width: 200px"
          @keyup.enter="getFlowPageList"
          @clear="getFlowPageList"
        >
        </el-input>
        <el-button type="primary" icon="Search" @click="getFlowPageList">搜索</el-button>
        <el-tooltip class="item" effect="dark" content="帮助" placement="top">
          <el-button class="help" type="text" @click="showhelp()">?</el-button>
        </el-tooltip>
      </div>
    </div>
    <div class="table_box">
      <el-table
        :data="flowList"
        row-key="id"
        :height="tableHeight"
        :border="true"
        stripe
        :header-cell-style="tableHeaderColor"
        :default-sort="{ prop: 'name', order: 'descending' }"
      >
        <!-- <el-table-column type="selection" width="50" align="center" /> -->
        <el-table-column prop="stop" label="状态" :width="120" align="center">
          <template #default="scope">
            <span>{{ scope.row.stop ? '已停用' : '启用' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="名称" min-width="150" sortable> </el-table-column>
        <el-table-column prop="groupName" label="分组" min-width="150" sortable> </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160"> </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" width="160"> </el-table-column>
        <el-table-column prop="rangeShow" label="范围 " :width="120" align="center">
          <template #default="scope">
            <span>{{
              scope.row.rangeShow && scope.row.rangeShow.length > 0 ? scope.row.rangeShow : '所有人'
            }}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column prop="buse" label="启用" :width="60" align="center">
          <template #default="scope">
            <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
          </template>
        </el-table-column> -->

        <el-table-column
          label="操作"
          align="center"
          width="210"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              link
              type="success"
              icon="Edit"
              title="修改"
              @click="onEdit(scope.row)"
            ></el-button>
            <el-button
              link
              type="success"
              icon="CopyDocument"
              title="复制"
              @click="toCopyFlow(scope.row)"
            ></el-button>
            <el-button
              v-if="!scope.row.stop"
              link
              type="danger"
              icon="Close"
              title="禁用"
              @click="handleBuse(false, scope.row)"
            ></el-button
            ><el-button
              v-if="scope.row.stop"
              link
              type="success"
              icon="Check"
              title="启用"
              @click="handleBuse(true, scope.row)"
            ></el-button>
            <el-button
              link
              type="danger"
              icon="Delete"
              title="删除"
              @click="onDelete(scope.row)"
            ></el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 页码 -->
      <!-- <el-pagination
        v-model:current-page="pageIndex"
        v-model:page-size="pageSize"
        :page-sizes="pageSizes"
        class="pagination"
        small
        :disabled="pageDisabled"
        :background="background"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pageTotal"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      /> -->
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { FormInstance, FormRules } from 'element-plus'
import { IProcessVO } from '../../types/views/flow/flow'
import { computTableHeight, tableHeaderColor } from '../../utils/index'
import type { IAxios } from 'axios'
import { getFlowTypesComboApi } from '@/api/flow/flowcondition'
import { deleteFlow, disableFlow, enableFlow, getListProcessApi } from '@/api/flow'
import { IDataCombo } from '@/types/common/DataCombo'

import { useRouter } from 'vue-router'
const router = useRouter()

defineOptions({
  name: 'FlowList'
})
/** Form名称 */
const mediaRef = ref<FormInstance>()
/** Form选中的行 */
const multipleSelection = ref<IProcessVO[]>([])
// 分页相关
const sort = ref('name')
const order = ref('asc')
const pageSizes = [15, 20, 30, 40]
const pageIndex = ref(1)
const pageSize = ref(15)
const background = ref(false)
const pageDisabled = ref(false)
/** 查询总页数 */
const pageTotal = ref(0)
/** 表格高度 */
const tableHeight = ref<number>(200)
/** 工作量列表 */
const flowList = ref<IProcessVO[]>([])
/** 分组下拉列表 */
const flowTypes = ref<IDataCombo[]>([])
/** 查询关键字 */
const queryKey = ref('')
/** 当前选中分组id */
const curFlowTypeId = ref('')

onMounted(() => {
  /**
   * 计算表格高度
   */
  tableHeight.value = computTableHeight()
  getFlowTypes()
  getFlowPageList()
})

/**
 * 获取流程组下拉列表
 */
const getFlowTypes = () => {
  getFlowTypesComboApi().then((res: IAxios) => {
    if (res.success) {
      flowTypes.value = res.obj
    }
  })
}
/**
 * 获取工作流程列表
 */
const getFlowPageList = () => {
  const data = {
    groupId: curFlowTypeId.value
  }
  getListProcessApi(data).then((res: IAxios) => {
    console.log(res)
    if (res.success) {
      console.log(res.obj)
      flowList.value = res.obj
      pageTotal.value = res.obj.length
    }
  })
}
/**
 * 创建流程
 */
const onAdd = () => {
  let to = '/flow/createFlow'
  if (curFlowTypeId.value) {
    to = to + '?groupId=' + curFlowTypeId.value
  }
  router.push(to)
}
/**
 * 编辑
 * @param row
 */
const onEdit = (row: IProcessVO) => {
  const to = '/flow/createFlow?flowId=' + row.flowId
  router.push(to)
}
/**
 * 复制
 * @param row
 */
const toCopyFlow = (row: IProcessVO) => {
  const to = '/flow/createFlow?cp=1&flowId=' + row.flowId
  router.push(to)
}
/**
 * 删除流程
 * @param row
 */
const onDelete = (row: IProcessVO) => {
  ElMessageBox.confirm('是否删除选择的流程？', '提示', {
    type: 'warning'
  })
    .then(() => {
      if (row.flowId === undefined) {
        ElMessage.warning('请选择要删除的数据')
        return
      }

      deleteFlow(row.flowId?.toString())
        .then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('删除成功')
            getFlowPageList()
          }
        })
        .catch(() => {})
    })
    .catch(() => {})
}
/**
 * 停用/启用流程
 * @param val
 * @param row
 */
const handleBuse = (val: boolean, row?: IProcessVO) => {
  let type = '禁用'
  if (val) {
    type = '启用'
  }
  ElMessageBox.confirm('是否' + type + '选择的流程？', '提示', {
    type: 'warning'
  })
    .then(() => {
      if (row === undefined || row.flowId === undefined) {
        ElMessage.warning('请选择正确的数据')
        return
      }
      if (val) {
        enableFlow(row.flowId?.toString()).then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('启用成功')
            getFlowPageList()
          }
        })
      } else {
        disableFlow(row.flowId?.toString()).then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('禁用成功')
            getFlowPageList()
          }
        })
      }
    })
    .catch(() => {})
}
// /**
//  * 翻页
//  * @param val
//  */
// const handleSizeChange = (val: number) => {
//   // 分页当前页
//   pageIndex.value = val
//   getFlowPageList()
// }
// /**
//  * 当前页
//  * @param val
//  */
// const handleCurrentChange = (val: number) => {
//   pageIndex.value = val
//   getFlowPageList()
// }
// /**
//  * 行选择事件
//  * @param val
//  */
// const handleSelectionChange = (val: IProcessVO[]) => {
//   multipleSelection.value = val
// }
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'flowlist'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>

<style lang="scss" scoped></style>
