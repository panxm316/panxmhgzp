<!--
 * @Author: songly
 * @Date: 2023-10-17 14:24:12
 * @LastEditTime: 2024-03-15 14:27:59
 * @LastEditors: wanghl
 * @Description: 我的已办任务
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-checkbox v-model="queryParams.bendFlag">结束时间</el-checkbox>
        <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-tooltip class="item" effect="dark" content="帮助" placement="top">
          <el-button class="help" type="text" @click="showhelp()">?</el-button>
        </el-tooltip>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="dataTableRef"
        :data="roleList"
        highlight-current-row
        :height="tableheight - 10"
        :border="true"
        stripe
        :header-cell-style="tableHeaderColor"
      >
        <el-table-column label="分组" prop="groupName" min-width="200" show-overflow-tooltip />
        <el-table-column label="流程" prop="processName" min-width="200" show-overflow-tooltip />
        <el-table-column label="业务名称" prop="businessName" width="200" show-overflow-tooltip />
        <el-table-column label="任务名称" prop="taskName" width="200" show-overflow-tooltip />
        <el-table-column label="任务开始时间" prop="taskCreateTime" width="160" />
        <el-table-column label="任务结束时间" prop="taskEndTime" width="160" />
        <el-table-column label="发起人" prop="rootUserName" width="150" />
        <el-table-column label="发起时间" prop="startTime" width="200" />
        <el-table-column fixed="right" label="操作" width="200" align="center">
          <template #default="scope">
            <el-button type="primary" size="small" link @click="deal(scope.row)">
              <i-ep-position />
              查看
            </el-button>
            <el-button type="primary" size="small" link @click="viewImage(scope.row)">
              <i-ep-picture />
              流程图
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- <pagination
        v-if="total > 0"
        v-model:total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        background
        small
        @pagination="handleQuery"
      /> -->
      <el-pagination
        v-model:current-page="queryParams.pageNum"
        class="pagination"
        style="margin-left: 10px; width: 100%"
        :page-sizes="pageSizes"
        :page-size="queryParams.pageSize"
        small
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <!--			右侧抽屉-->
    <el-dialog
      v-model="rightDrawerVisible"
      :width="fenbianlv() > 1440 ? '80%' : '94%'"
      title="查看详情"
      draggable
    >
      <!-- <div
        v-if="
          showdetail === EFlowType.电子认刊书审批流程 ||
          showdetail === EFlowType.订单审批流程 ||
          showdetail === EFlowType.改加停刊审批流程
        "
        style="position: absolute; z-index: 1100; right: 60px; top: 20px"
      >
        <el-button icon="edit" type="success" @click="adtoworeredit()">点击修改</el-button>
        <el-button type="primary" @click="printMeShow()">打印发布认刊书</el-button>
      </div> -->
      <!-- <template #header>
        <h3>{{ currentData?.processName }}</h3>
      </template>
      <template #default> -->
      <el-row class="bgboxright">
        <el-col
          :span="
            showdetail === EFlowType.电子认刊书审批流程 ||
            showdetail === EFlowType.订单审批流程 ||
            showdetail === EFlowType.改加停刊审批流程
              ? 24
              : 14
          "
          style="overflow-x: auto"
        >
          <Hg-See-Customer
            v-if="showdetail === EFlowType.客户挂牌审批流程"
            :selfdetailid="selfdetailid"
          ></Hg-See-Customer>

          <HgAdSourceDetail
            v-if="showdetail === EFlowType.资源审批流程"
            :id="selfdetailid"
          ></HgAdSourceDetail>
          <HgPreInvoiceApplicationDetail
            v-if="showdetail === EFlowType.预开发票审批流程"
            :selfdetailid="selfdetailid"
          ></HgPreInvoiceApplicationDetail>
          <AdTworderdetails
            v-if="
              showdetail === EFlowType.电子认刊书审批流程 ||
              showdetail === EFlowType.订单审批流程 ||
              showdetail === EFlowType.改加停刊审批流程
            "
            style="margin-top: 20px"
            :data="selfdetailid"
            :Refresh="Refresh"
          ></AdTworderdetails>
        </el-col>
        <el-col
          :span="
            showdetail === EFlowType.电子认刊书审批流程 ||
            showdetail === EFlowType.订单审批流程 ||
            showdetail === EFlowType.改加停刊审批流程
              ? 24
              : 10
          "
          style="background-color: #fff"
        >
          <!-- <el-card class="box-card"> -->
          <!-- <form-render ref="formRenderRef" :form-list="currentOpenFlowForm"></form-render> -->
          <!-- </el-card> -->
          <flow-node-format
            ref="flowNodeFormatRef"
            :disable-select="true"
            :form-data="formValue"
            :process-instance-id="currentData.processInstanceId"
            :flow-id="currentData.flowId"
          ></flow-node-format>
        </el-col>
      </el-row>
      <!-- </template> -->
      <!-- 领导修改组件 -->
      <AdTworderEditer
        ref="adtoworereditref"
        :redetdata="false"
        :bottomshow="true"
        :isbu="0"
        @closedialogVisible="Refreshdata"
      />
    </el-dialog>

    <!--			查看流程图-->
    <view-process-instance-image ref="viewImageRef" />
    <!-- 打印组件 -->
    <printMe ref="printMeRef" />
  </div>
</template>
<script setup lang="ts">
import HgAdSourceDetail from '@/components/HgAdSourceDetail/index.vue'
import FormRender from '@/views/flow/form/render/FormRender.vue'
import ViewProcessInstanceImage from '@/components/Task/ViewProcessInstanceImage.vue'
import AgreeHandle from './handler/agree.vue'
import RefuseHandle from './handler/refuse.vue'
import FlowNodeFormat from '@/components/Flow/FlowNodeFormatData.vue'

import { queryMineEndTask, queryTask, stopProcessInstance } from '@/api/task'
import { detail } from '@/api/processInstance'
import { IRoleQuery, ITaskDto } from '@/types/views/task/completed'
import { TDateType, TDateTimeType } from '@/types/components/hgdateindex'
import { computTableHeight, tableHeaderColor, fenbianlv } from '@/utils/index'
import { useRouter } from 'vue-router'
import { getEnumCombo } from '@/api/common/index'
import { EFlowType } from '@/types/common/enumindex'
import { IDataCombo } from '@/types/common/DataCombo'
import AdTworderdetails from '@/views/ad/AdTworderdetailsId.vue'
import printMe from './printMe.vue'

const printMeRef = ref()

/** 获取流程参数 */
const flowEFlowType: IDataCombo[] = getEnumCombo(EFlowType)
const router = useRouter()
const timedata = reactive<TDateType>({
  // 时间
  timetype: '2',
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
function stop(row: ITaskDto) {
  stopProcessInstance({
    processInstanceId: row.processInstanceId
  }).then((res) => {
    handleQuery()
  })
}
/** 客户id */
const selfdetailid = ref<string | undefined>('')
/** 显示详情 */
const showdetail = ref<string | undefined>('')
const bgboxright = ref('')
/** 列表高度 */
const tableheight = ref(0)
const rightDrawerVisible = ref(false)
/** 页数 */
const total = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 查询参数 */
const queryParams = reactive<IRoleQuery>({
  pageNum: 1,
  pageSize: 20,
  keywords: '',
  startTime: '',
  endTime: '',
  bendFlag: false
})
/** 查询列表数据 */
const roleList = ref<ITaskDto[]>([])
/** 当前行 */
const currentData = ref()
/**
 * 点击开始处理
 * @param row
 */
const deal = (row: ITaskDto) => {
  selfdetailid.value = row.businessId
  console.log(row)
  showdetail.value = row.groupId
  currentData.value = row
  queryTask(row.taskId, true).then((res) => {
    currentOpenFlowForm.value = res.obj.formItems
    rightDrawerVisible.value = true
  })
}
/**
 * 打印发布认刊书
 */
const printMeShow = () => {
  printMeRef.value.view(selfdetailid.value)
}
/** 表单 */
const currentOpenFlowForm = ref()
/** 流程图 */
const viewImageRef = ref()

/**
 * 点击查看流程图
 */
const viewImage = (row: ITaskDto) => {
  viewImageRef.value.view(row)
}
/** 提交 */
const agreeHandler = ref()
/** 拒绝 */
const refuseHandler = ref()
/** 时间选择 */
const ontime = (val: TDateTimeType) => {
  let timev = false
  if (queryParams.startTime) {
    timev = true
  }
  queryParams.startTime = val.startTime
  queryParams.endTime = val.endTime
  if (timev) {
    handleQuery()
  }
}
/**
 * 查询
 */
function handleQuery() {
  queryMineEndTask(queryParams)
    .then(({ obj }) => {
      roleList.value = obj.records
      total.value = obj.total
    })
    .finally(() => {})
}

const taskSubmitEvent = () => {
  rightDrawerVisible.value = false
  handleQuery()
}
/**
 * 改变页码总数时
 */
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  handleQuery()
}
/**
 * 改变页数时
 * @param val
 */
const handleCurrentChange = (val: number) => {
  queryParams.pageNum = val
  handleQuery()
}
/**
 * 提交任务
 */
const submitTask = () => {
  agreeHandler.value.handle(currentData.value, currentOpenFlowForm.value)
}
/**
 * 拒绝任务
 */
const refuseTask = () => {
  refuseHandler.value.handle(currentData.value, currentOpenFlowForm.value)
}
onMounted(() => {
  handleQuery()
})

const formValue = computed(() => {
  var obj = {}

  for (var item of currentOpenFlowForm.value) {
    // @ts-ignore
    obj[item.id] = item.props.value
  }
  return obj
})
onMounted(() => {
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
  bgboxright.value = computTableHeight() + 'px'
})
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'completed'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
/**
 * 领导修改
 */
import AdTworderEditer from '@/views/ad/AdTworderEditer.vue'
const adtoworereditref = ref()
const adtoworeredit = () => {
  adtoworereditref.value.view(selfdetailid.value)
}
/**
 * 刷新数据
 */
const Refresh = ref(true)
const Refreshdata = () => {
  Refresh.value = !Refresh.value
}
</script>
<style scoped lang="scss">
.el-checkbox {
  height: 30px;
  margin-right: 10px;
}
.bgboxright {
  max-height: v-bind(bgboxright);
  min-height: 700px;
  overflow-y: auto;
  background-color: rgb(249, 252, 254);
  width: 98%;
  margin-top: -30px;
}
</style>
