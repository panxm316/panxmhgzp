<!--
 * @Author: caogd
 * @Date: 2023-10-17 10:56:15
 * @LastEditTime: 2024-03-15 14:27:24
 * @LastEditors: wanghl
 * @Description: 代办任务
-->
<template>
  <div class="app-container">
    <!-- <div class="search_box">
      <div class="flex">
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-tooltip class="item" effect="dark" content="帮助" placement="top">
          <el-button class="help" type="text" @click="showhelp()">?</el-button>
        </el-tooltip>
      </div>
    </div> -->
    <div class="table_box">
      <el-table
        ref="dataTableRef"
        :data="roleList"
        stripe
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        :border="true"
      >
        <el-table-column label="分组" prop="groupName" show-overflow-tooltip />
        <el-table-column label="流程" prop="processName" show-overflow-tooltip />
        <el-table-column label="发起人" prop="rootUserName" width="100" />
        <el-table-column label="业务名称" prop="businessName" width="200" />
        <el-table-column label="发起时间" prop="startTime" width="160" />
        <el-table-column label="当前节点" prop="taskName" />
        <el-table-column label="任务时间" prop="taskCreateTime" width="160" />
        <el-table-column fixed="right" label="操作" width="200">
          <template #default="scope">
            <el-button type="primary" size="small" link @click="deal(scope.row)">
              <i-ep-position />
              开始处理
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
          <flow-node-format
            ref="flowNodeFormatRef"
            :disable-select="true"
            :task-id="currentData?.taskId"
            :process-instance-id="currentData?.processInstanceId"
            :flow-id="currentData?.flowId"
          ></flow-node-format>
        </el-col>
      </el-row>
      <template #footer>
        <div style="flex: auto">
          <el-button size="large" type="danger" @click="refuseTask">拒绝</el-button>
          <el-button size="large" type="primary" @click="submitTask">同意</el-button>
        </div>
      </template>
      <!-- 领导修改组件 -->
      <AdTworderEditer
        ref="adtoworereditref"
        :redetdata="false"
        :bottomshow="true"
        :isbu="0"
        :FlowManager="FlowManagertype"
        @closedialogVisible="Refreshdata"
      />
    </el-dialog>

    <!--			查看流程图-->
    <view-process-instance-image ref="viewImageRef" />

    <!--			同意提交处理-->
    <agree-handle
      ref="agreeHandler"
      @taskSubmitEvent="taskSubmitEvent"
      :data="showCommonApproval"
    ></agree-handle>

    <!--			拒绝审核处理-->
    <refuse-handle
      ref="refuseHandler"
      @taskSubmitEvent="taskSubmitEvent"
      :data="showCommonApproval"
    ></refuse-handle>
    <!-- 打印组件 -->
    <printMe ref="printMeRef" />
  </div>
</template>
<script setup lang="ts">
import HgAdSourceDetail from '@/components/HgAdSourceDetail/index.vue'
import CustomerBelong from '../customer/CustomerBelong.vue'
import FormRender from '@/views/flow/form/render/FormRender.vue'
import ViewProcessInstanceImage from '@/components/Task/ViewProcessInstanceImage.vue'
import AgreeHandle from './handler/agree.vue'
import RefuseHandle from './handler/refuse.vue'
import FlowNodeFormat from '@/components/Flow/FlowNodeFormatData.vue'
import { queryMineTask, queryTask } from '@/api/task'
import { TProcessInstancePageDto } from '@/types/views/task/start'
import { cloneDeep } from 'lodash'
import { ITaskDto } from '@/types/views/task/completed'
import { IAxios } from 'axios'
import { computTableHeight, tableHeaderColor, fenbianlv } from '@/utils/index'
import { useRouter } from 'vue-router'
import useMessageStore from '@/store/modules/message'
import { EMessageType } from '@/store/modules/type'
import { getEnumCombo } from '@/api/common/index'
import { EFlowType } from '@/types/common/enumindex'
import { IDataCombo } from '@/types/common/DataCombo'
import AdTworderdetails from '@/views/ad/AdTworderdetailsId.vue'
import printMe from './printMe.vue'
const printMeRef = ref()
/** 获取流程参数 */
const flowEFlowType: IDataCombo[] = getEnumCombo(EFlowType)
/** 客户id */
const selfdetailid = ref<string | undefined>('')
const messageStore = useMessageStore()
const router = useRouter()
const rightDrawerVisible = ref(false)
/** 当前查看流程类型 */
const currentFlowType = ref('')

const loading = ref(false)
const ids = ref<number[]>([])
const total = ref(0)
/** 表格高度 */
const tableheight = ref(0)

const queryParams = reactive<TProcessInstancePageDto>({
  pageNum: 1,
  pageSize: 20,
  processInstanceId: ''
})
/** 显示详情 */
const showdetail = ref<string | undefined>('')
const roleList = ref<ITaskDto[]>()
const currentData = ref<ITaskDto>()
const bgboxright = ref('')
/**
 * 改刊停刊类型
 */
const FlowManagertype = ref('stop')
/** 树高度 */
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
onMounted(() => {
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
  bgboxright.value = computTableHeight() + 'px'

  // handleQuery()
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
  const { processInstanceId, processInstanceType } = messageStore.params
  console.log(processInstanceId)
  if (processInstanceId && processInstanceType === EMessageType.FLOW_TodoTask_MSG) {
    queryParams.processInstanceId = processInstanceId.toString()
    handleQuery()
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

const formValue = computed(() => {
  var obj: { [key: string]: any } = {}

  // for (var item of currentOpenFlowForm.value) {
  //   obj[item.id] = item.props.value
  // }
  return obj
})

// --------------------------------------------
const sflowtype = ref('')
/** 常用审批意见传值 */
const showCommonApproval = ref({
  sflowtype: '',
  ipasstype: ''
})
// --------------------------------------------
/**
 * 点击开始处理
 * @param row
 */
const deal = (row: ITaskDto) => {
  selfdetailid.value = row.businessId
  getorderById(row.businessId!)

  console.log(row)
  showdetail.value = row.groupId
  currentData.value = row
  rightDrawerVisible.value = true
  sflowtype.value = row.groupId as string //-------------
  // queryTask(row.taskId, false).then((res) => {
  //   currentOpenFlowForm.value = res.data.formItems
  //   rightDrawerVisible.value = true
  // })
}
const currentOpenFlowForm = ref()
const viewImageRef = ref()
const addLayoutOneItem = (id: string) => {
  for (var item of currentOpenFlowForm.value) {
    if (item.id !== id) {
      continue
    }
    const value = item.props.value
    const oriForm = item.props.oriForm
    value.push(cloneDeep(oriForm))
    item.props.value = value
  }
}
const deleteLayoutOneItem = (id: string, index: number) => {
  for (var item of currentOpenFlowForm.value) {
    if (item.id !== id) {
      continue
    }
    item.props.value.splice(index, 1)
  }
}
/**
 * 获取根据id获取订单信息
 */
import {
  getorderByIdApi,
  getContractNumByEmployIdAndCustomerIdApi,
  saveorderApi,
  updateorderApi,
  deleteorderApi,
  getApproveTypeComboApi
} from '@/api/ad/adtworder'
const getorderById = (data: string) => {
  getorderByIdApi(data).then(({ obj, success, msg }: IAxios) => {
    console.log(obj)
    if (success) {
      if (obj.istopapprovestatus === 1) {
        FlowManagertype.value = 'stop'
      } else if (obj.ischangemanagetype === 1) {
        FlowManagertype.value = 'change'
      } else if (obj.isaddmanagetype === 1) {
        FlowManagertype.value = 'add'
      }
    }
  })
  console.log(FlowManagertype.value)
}
/**
 * 点击查看流程图
 */
const viewImage = (row: any) => {
  viewImageRef.value.view(row)
}
/**
 * 打印发布认刊书
 */
const printMeShow = () => {
  printMeRef.value.view(selfdetailid.value)
}
const agreeHandler = ref()
const refuseHandler = ref()

/**
 * 查询
 */
function handleQuery() {
  loading.value = true
  queryMineTask(queryParams)
    .then(({ obj }: IAxios<{ records: ITaskDto[]; total: number }>) => {
      console.log(obj)

      roleList.value = obj.records
      total.value = obj.total
    })
    .finally(() => {
      loading.value = false
    })
}

const taskSubmitEvent = () => {
  rightDrawerVisible.value = false
  selfdetailid.value = ''
  handleQuery()
}

/**
 * 提交任务
 */
const submitTask = () => {
  agreeHandler.value.handle(currentData.value, currentOpenFlowForm.value)
  showCommonApproval.value = {
    ipasstype: '1', //-------------
    sflowtype: sflowtype.value //-------------
  }
}
/**
 * 拒绝任务
 */
const refuseTask = () => {
  refuseHandler.value.handle(currentData.value, currentOpenFlowForm.value)
  showCommonApproval.value = {
    ipasstype: '0', //-------------
    sflowtype: sflowtype.value //-------------
  }
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
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'pending'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
/**
 * 领导修改
 */
import AdTworderEditer from '@/views/ad/AdTworderEditer.vue'

const adtoworereditref = ref()
const adtoworeredit = () => {
  console.log(FlowManagertype.value)
  nextTick(() => {
    adtoworereditref.value.view(selfdetailid.value, FlowManagertype.value)
  })
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
