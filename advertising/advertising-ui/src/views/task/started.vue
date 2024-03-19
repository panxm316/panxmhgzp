<!--
 * @Author: suny
 * @Date: 2023-10-17 10:53:29
 * @LastEditTime: 2024-03-15 14:28:13
 * @LastEditors: wanghl
 * @Description: 我的发起
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <!-- <el-input
      v-model="queryKey"
      clearable
      placeholder="请输入叠次关键字"
      class="input-with-select"
      style="width: 200px"
      @keyup.enter="handleQuery"
      @clear="handleQuery"
    >
    </el-input> -->
        <!-- <el-checkbox v-model="queryParams.bendFlag">创建时间</el-checkbox> -->
        <HgDateIndex ref="hgDateRef" :initdate="timedata" @onresponse="ontime"></HgDateIndex>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-tooltip class="item" effect="dark" content="帮助" placement="top">
          <el-button class="help" type="text" @click="showhelp()">?</el-button>
        </el-tooltip>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="dataTableRef"
        :data="myProcessList"
        highlight-current-row
        :height="tableheight - 10"
        :header-cell-style="tableHeaderColor"
        border
      >
        <el-table-column label="分组" prop="groupName" show-overflow-tooltip />
        <el-table-column label="流程" prop="name" show-overflow-tooltip />
        <el-table-column label="业务名称" prop="businessName" show-overflow-tooltip />
        <el-table-column label="发起时间" prop="createTime" width="160" />
        <el-table-column label="结束时间" prop="endTime" width="160" />
        <el-table-column label="状态" prop="taskCreateTime" width="100" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.status == 1" type="success">进行中</el-tag>
            <el-tag v-else type="info">已结束</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="审批结果" prop="taskCreateTime" width="100" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.result === true" type="success">通过</el-tag>
            <el-tag v-if="scope.row.result === false" type="danger">拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="250">
          <template #default="scope">
            <el-button type="primary" size="small" link @click="deal(scope.row)">
              <i-ep-position />
              查看
            </el-button>
            <el-button
              :disabled="scope.row.status != 1"
              type="primary"
              size="small"
              link
              @click="stop(scope.row)"
            >
              <i-ep-lock />
              终止流程
            </el-button>

            <el-button type="primary" size="small" link @click="viewImage(scope.row)">
              <i-ep-picture />
              流程图
            </el-button>
          </template>
        </el-table-column>
      </el-table>
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
      <!--			右侧抽屉-->
      <!-- <el-drawer v-model="rightDrawerVisible" direction="rtl" size="500px">
        <template #header>
          <h3>{{ currentData?.name }}</h3>
        </template>
        <template #default>
          <el-card class="box-card">
            <form-render ref="formRenderRef" :form-list="currentOpenFlowForm"></form-render>
            详情
          </el-card>
          <flow-node-format
            ref="flowNodeFormatRef"
            :disable-select="true"
            :process-instance-id="currentData?.processInstanceId"
            :flow-id="currentData?.flowId"
          ></flow-node-format>
        </template>
      </el-drawer> -->
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
          <el-button icon="Printer" type="primary" @click="printMeShow()">打印发布认刊书</el-button>
        </div> -->
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
            <AdTworderdetailsId
              v-if="
                showdetail === EFlowType.电子认刊书审批流程 ||
                showdetail === EFlowType.订单审批流程 ||
                showdetail === EFlowType.改加停刊审批流程
              "
              style="margin-top: 20px"
              :Refresh="Refresh"
              :data="selfdetailid"
            ></AdTworderdetailsId>
          </el-col>
          <el-col
            :span="
              showdetail === EFlowType.电子认刊书审批流程 ||
              showdetail === EFlowType.订单审批流程 ||
              showdetail === EFlowType.改加停刊审批流程
                ? 24
                : 10
            "
            style="background-color: #fff; max-height: 700px; overflow-y: auto"
          >
            <flow-node-format
              ref="flowNodeFormatRef"
              :disable-select="true"
              :process-instance-id="currentData?.processInstanceId"
              :flow-id="currentData?.flowId"
            ></flow-node-format>
          </el-col>
        </el-row>
        <!-- <template #footer>
          <div class="dialog-footer">
            <el-button
              v-if="
                showdetail === EFlowType.电子认刊书审批流程 || showdetail === EFlowType.订单审批流程
              "
              type="primary"
              @click="printMeShow()"
              >打印发布认刊书</el-button
            >
          </div>
        </template> -->
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
  </div>
</template>
<script setup lang="ts">
import HgAdSourceDetail from '@/components/HgAdSourceDetail/index.vue'
import FormRender from '@/views/flow/form/render/FormRender.vue'
import ViewProcessInstanceImage from '@/components/Task/ViewProcessInstanceImage.vue'
import FlowNodeFormat from '@/components/Flow/FlowNodeFormatData.vue'
import { queryMineStarted, stopProcessInstance } from '@/api/task/index'
import { detail } from '@/api/processInstance'
import {
  IFormItemVO,
  TProcessInstancePageDto,
  ProcessInstanceRecord
} from '@/types/views/task/start'
import { IAxios } from 'axios'
import { computTableHeight, tableHeaderColor, fenbianlv } from '@/utils/index'
import { useRouter, useRoute } from 'vue-router'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import useMessageStore from '@/store/modules/message'
import { EMessageType } from '@/store/modules/type'
import { getEnumCombo } from '@/api/common/index'
import { EFlowType } from '@/types/common/enumindex'
import { IDataCombo } from '@/types/common/DataCombo'
import AdTworderdetailsId from '@/views/ad/AdTworderdetailsId.vue'
import printMe from './printMe.vue'
/** 获取流程参数 */
const flowEFlowType: IDataCombo[] = getEnumCombo(EFlowType)
const router = useRouter()
const messageStore = useMessageStore()
const viewImageRef = ref()
const printMeRef = ref()
const hgDateRef = ref()
/** 右侧详情显示状态 */
const rightDrawerVisible = ref(false)
const total = ref(0)
/** 表格高度 */
const tableheight = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
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
/** 查询条件 */
const queryParams = reactive<TProcessInstancePageDto>({
  pageNum: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  processInstanceId: ''
})
/** 我的发起列表 */
const myProcessList = ref()
/** 当前选择查看的对象 */
const currentOpenFlowForm = ref<IFormItemVO[]>([])
/** 当前选中行 */
const currentData = ref<ProcessInstanceRecord>()
/** 广告预定id */
const selfdetailid = ref<string | undefined>('')
const bgboxright = ref('')
const showdetail = ref<string | undefined>('')
onMounted(() => {
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
  bgboxright.value = computTableHeight() + 'px'
  processInstanceQuery()
})
watch(
  () => messageStore.params.processInstanceId,
  (processInstanceId) => {
    processInstanceId && processInstanceQuery()
  },
  { deep: true }
)
/**
 * 消息提醒跳转查询
 */
const processInstanceQuery = () => {
  const { processInstanceId, processInstanceCreate, processInstanceType } = messageStore.params
  console.log(processInstanceId)
  if (
    processInstanceId &&
    (processInstanceType === EMessageType.FLOW_ApprovePass_MSG ||
      processInstanceType === EMessageType.FLOW_ApproveReject_MSG)
  ) {
    timedata.value.startTime = processInstanceCreate
    timedata.value.endTime = processInstanceCreate
    queryParams.processInstanceId = processInstanceId.toString()
    hgDateRef.value.inittime()
    hgDateRef.value.onresponse()
    messageStore.setParams({
      processInstanceId: 0,
      processInstanceCreate: '',
      processInstanceType: EMessageType.SYSTEM_MSG
    })
    queryParams.processInstanceId = ''
  } else {
    handleQuery()
  }
}
/**
 * 时间选择
 * @param val
 */
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
  queryMineStarted(queryParams)
    .then((res: IAxios) => {
      if (res.success) {
        console.log(res)
        myProcessList.value = res.obj
        total.value = res.total
      }
    })
    .finally(() => {})
}
/**
 * 结束流程
 * @param row
 */
const stop = (row: ProcessInstanceRecord) => {
  stopProcessInstance({
    processInstanceId: row.processInstanceId
  }).then((res) => {
    handleQuery()
  })
}
/**
 * 点击开始处理
 * @param row
 */
const deal = (row: ProcessInstanceRecord) => {
  selfdetailid.value = row.businessId
  console.log(row)
  showdetail.value = row.groupId
  currentData.value = row
  detail({
    processInstanceId: row.processInstanceId
  }).then((res) => {
    console.log(res)
    currentOpenFlowForm.value = res.obj.formItems
    rightDrawerVisible.value = true
  })
}

/**
 * 点击查看流程图
 */
const viewImage = (row: ProcessInstanceRecord) => {
  viewImageRef.value.view(row)
}
/**
 * 打印发布认刊书
 */
const printMeShow = () => {
  printMeRef.value.view(selfdetailid.value)
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

// const formValue = computed(() => {
//   var obj = {}

//   for (const item of currentOpenFlowForm.value) {
//     obj[item.id] = item.props.value
//   }
//   return obj
// })
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'started'
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
<style lang="scss" scoped>
.bgboxright {
  max-height: v-bind(bgboxright);
  min-height: 700px;
  overflow-y: auto;
  background-color: rgb(249, 252, 254);
  width: 98%;
  margin-top: -30px;
}
</style>
