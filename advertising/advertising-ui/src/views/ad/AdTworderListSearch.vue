<!--
 * @Author: songly
 * @Date: 2023-11-22 15:41:47
 * @LastEditTime: 2024-03-12 14:09:09
 * @LastEditors: wanghl
 * @Description: 订单查询
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex" style="width: 94%">
        <HgDateIndex :initdate="initDate" @onresponse="onChangeDate"></HgDateIndex>
        <el-input
          v-model="queryInfo.queryKey"
          placeholder="广告预定检索"
          clearable
          title="支持广告标题，订单编号，合同号，经营主体。客户，业务员等检索条件"
          style="width: 200px"
          @keyup.enter="loaddata"
          @clear="loaddata"
        />
        <el-button type="primary" icon="Search" @click="loaddata">搜索</el-button>
        <el-tooltip class="item" effect="dark" content="帮助" placement="top">
          <el-button class="help" type="text" @click="showhelp()">?</el-button>
        </el-tooltip>
      </div>
    </div>
    <div class="table_box">
      <el-row>
        <el-table
          :data="WorderList"
          row-key="id"
          :height="tableheight"
          :border="true"
          stripe
          :header-cell-style="tableHeaderColor"
          @selection-change="handleSelectionChange"
          @sort-change="handleSortChange"
        >
          <!-- <el-table-column type="selection" prop="ischeck" :width="50" align="center">
          </el-table-column> -->
          <el-table-column
            prop="sadtitle"
            label="广告标题"
            sortable="custom"
            min-width="200"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column prop="sordernum" label="订单编号" sortable="custom" width="120">
          </el-table-column>
          <el-table-column
            label="合同号"
            prop="scontractnum"
            min-width="140"
            align="center"
            show-overflow-tooltip
          >
            <template #default="scope">
              <el-button
                link
                type="primary"
                icon="view"
                size="small"
                @click="handleSee(scope.row, scope.$index)"
                >{{ scope.row.scontractnum }}</el-button
              >
            </template>
          </el-table-column>

          <el-table-column
            prop="businessentityname"
            label="经营主体"
            sortable="custom"
            min-width="140"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column prop="createtime" label="创建时间" width="110" sortable="custom">
            <template #default="scope">
              <span>{{ formatDatesm(scope.row.createtime) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="idiscountapprovestatus" label="折扣状态" width="90" align="center">
            <template #default="scope">
              <span v-if="scope.row.idiscountapprovestatus === 0">
                <el-tag>待审</el-tag>
              </span>
              <span v-if="scope.row.idiscountapprovestatus === 1">
                <el-tag type="warning">在审</el-tag>
              </span>
              <span v-if="scope.row.idiscountapprovestatus === 2">
                <el-tag type="success">通过</el-tag>
              </span>
              <span v-if="scope.row.idiscountapprovestatus === 3">
                <el-tag type="danger">否决</el-tag>
              </span>
              <span v-if="scope.row.idiscountapprovestatus === 4">
                <el-tag type="warning">撤销</el-tag>
              </span>
              <span v-if="scope.row.idiscountapprovestatus === 5">
                <el-tag type="danger">无效</el-tag>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="iapprovestatus" label="订单状态" width="90" align="center">
            <template #default="scope">
              <span v-if="scope.row.iapprovestatus === 0">
                <el-tag>待审</el-tag>
              </span>
              <span v-if="scope.row.iapprovestatus === 1">
                <el-tag type="warning">在审</el-tag>
              </span>
              <span v-if="scope.row.iapprovestatus === 2">
                <el-tag type="success">通过</el-tag>
              </span>
              <span v-if="scope.row.iapprovestatus === 3">
                <el-tag type="danger">否决</el-tag>
              </span>
              <span v-if="scope.row.iapprovestatus === 4">
                <el-tag type="warning">撤销</el-tag>
              </span>
              <span v-if="scope.row.iapprovestatus === 5">
                <el-tag type="danger">无效</el-tag>
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="customername"
            label="客户名称"
            sortable="custom"
            min-width="180"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column
            prop="employname"
            label="业务员"
            sortable="custom"
            width="100"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column
            prop="agencyname"
            label="代理公司"
            sortable="custom"
            width="140"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column
            prop="agencyemployname"
            label="代理公司业务员"
            sortable="custom"
            width="140"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column
            prop="agentname"
            label="内容生产方"
            sortable="custom"
            width="140"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column
            prop="agentemployname"
            label="生产方业务员"
            sortable="custom"
            width="140"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column prop="adprojectname" label="项目名称" width="150" show-overflow-tooltip>
          </el-table-column>
          <el-table-column prop="ibooktype" label="录入方式" width="90" align="center">
            <template #default="scope">
              <span v-if="scope.row.ibooktype === 0"> 正常 </span>
              <span v-if="scope.row.ibooktype === 1"> 广告预约 </span>
              <span v-if="scope.row.ibooktype === 2"> 快速预约 </span>
              <span v-if="scope.row.ibooktype === 3"> 补刊 </span>
            </template>
          </el-table-column>
          <el-table-column prop="createempname" label="创建者" sortable="custom" width="120">
            <template #default="scope"> {{ scope.row.createempname }} </template>
          </el-table-column>

          <el-table-column
            label="操作"
            width="160"
            class-name="small-padding fixed-width"
            fixed="right"
            align="center"
          >
            <template #default="scope">
              <div style="padding-left: 10px">
                <!-- <el-button
                  v-if="
                    // scope.row.iapprovestatus === 0 ||
                    // scope.row.iapprovestatus === 3 ||
                    // scope.row.iapprovestatus === 4 ||
                    scope.row.idiscountapprovestatus === 0 ||
                    scope.row.idiscountapprovestatus === 3 ||
                    scope.row.idiscountapprovestatus === 4
                  "
                  link
                  type="success"
                  icon="Edit"
                  size="small"
                  @click="handleUpdate(scope.row)"
                  >修改</el-button
                > -->
                <!-- <el-button
                  v-if="
                    // scope.row.iapprovestatus === 1 ||
                    // scope.row.iapprovestatus === 2 ||
                    // scope.row.iapprovestatus === 5
                    scope.row.idiscountapprovestatus === 1 ||
                    scope.row.idiscountapprovestatus === 2 ||
                    scope.row.idiscountapprovestatus === 5
                  "
                  link
                  type="primary"
                  icon="view"
                  size="small"
                  @click="handleSee(scope.row, scope.$index)"
                  >详情</el-button
                > -->
                <el-button
                  link
                  :disabled="scope.row.adresourceapplicationid.length === 0"
                  type="primary"
                  icon="view"
                  size="small"
                  @click="onDetail(scope.row.adresourceapplicationid)"
                  >查看资源</el-button
                >
                <!-- <el-button
                  link
                  type="primary"
                  icon="Top"
                  v-if="scope.row.idiscountapprovestatus !== 2"
                  :disabled="
                    scope.row.idiscountapprovestatus === 1 || scope.row.idiscountapprovestatus === 5
                  "
                  size="small"
                  @click="onshowflowzk(scope.row.id)"
                  >折扣审批</el-button
                >
                <el-button
                  link
                  type="success"
                  icon="Top"
                  v-if="scope.row.idiscountapprovestatus === 2"
                  :disabled="
                    scope.row.iapprovestatus === 1 ||
                    scope.row.iapprovestatus === 2 ||
                    scope.row.iapprovestatus === 5
                  "
                  size="small"
                  @click="onshowflow(scope.row.id)"
                  >订单审批</el-button
                >
                <el-button
                  link
                  type="danger"
                  icon="Delete"
                  size="small"
                  @click="handleDelete(scope.row)"
                  >删除</el-button
                > -->
              </div>
            </template>
          </el-table-column>
        </el-table>
      </el-row>
      <el-row>
        <el-pagination
          v-model:current-page="queryInfo.page"
          class="pagination"
          style="margin-left: 10px; width: 100%"
          :page-sizes="pageSizes"
          :page-size="queryInfo.pageSize"
          small
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="pageTotal"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        >
        </el-pagination>
      </el-row>
    </div>
    <!-- 订单预定 -->
    <el-dialog
      v-model="dialogVisible"
      title="编辑订单"
      align-center
      :close-on-click-modal="false"
      :width="fenbianlv() > 1440 ? '80%' : '94%'"
      @close="handleCancel"
    >
      <!-- /* 编辑 */ -->
      <AdTworder
        v-if="editsee === false"
        style="width: 100%"
        :data="WorderId"
        :resourceid="Resourceid"
        :redetdata="redetdata"
        :bottomshow="true"
        :isbu="isbukan"
        @closedialogVisible="closedialogVisible"
      ></AdTworder>
    </el-dialog>
    <!-- /* 查看详情 */ -->
    <AdTworderdetails ref="viewImageRef"></AdTworderdetails>
    <!-- 详情 -->
    <el-dialog
      v-model="dialogDetailVisible"
      title="资源详情"
      width="800"
      align-center
      :close-on-click-modal="false"
      :destroy-on-close="true"
    >
      <HgAdSourceDetail :id="adresourceapplicationid" :showflag="false"></HgAdSourceDetail>
    </el-dialog>
    <!-- 选择流程 -->
    <el-dialog
      v-model="flowshow"
      title="选择流程"
      width="500"
      align-center
      :destroy-on-close="true"
      @close="handleCancelflow"
    >
      <HgFlowTypeSelect
        v-if="onflowtype === false"
        style="width: 400px; margin-left: 30px; margin-bottom: 30px"
        :flowtypename="EFlowType.订单审批流程"
        @selecflowtype="selecflowtype"
      ></HgFlowTypeSelect>
      <HgFlowTypeSelect
        v-if="onflowtype === true"
        style="width: 400px; margin-left: 30px; margin-bottom: 30px"
        :flowtypename="EFlowType.电子认刊书审批流程"
        @selecflowtype="selecflowtype"
      ></HgFlowTypeSelect>
      <div style="width: 93%; text-align: right">
        <el-button v-if="flowdata.flowId !== ''" type="success" icon="Top" @click="onToCheck"
          >提交审核</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { FormRules, FormInstance, menuItemEmits } from 'element-plus'
import AdTworder from './AdTworder.vue'
import AdTworderdetails from './AdTworderdetails.vue'
import type { TCustomerApprove } from '@/types/views/customer'
import type {
  TworderCustomer,
  Tworderitembelong,
  Tworderitem,
  Tworder,
  QTworder
} from '@/types/views/ad/adtworder'
import {
  required,
  computTableHeight,
  isNumberStr,
  formatDate,
  formatDatesm,
  tableHeaderColor,
  fenbianlv
} from '@/utils/index'
import modal from '@/plugins/modal'
import { EHgDeptZtreeUrl, EApproveStatus, EFlowType } from '@/types/common/enumindex'
import { LocationQuery, LocationQueryValue, useRouter, useRoute } from 'vue-router'
import {
  getorderPagelistApi,
  deleteorderApi,
  discountApproveOrderApi,
  approveOrderApi
} from '@/api/ad/adtworder'
import { Tadindustrylist, TypeCurrentData } from '@/types/components/hgindustry'
import { getEnumCombo } from '@/api/common/index'
import { IDataCombo } from '@/types/common/DataCombo'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { getInitDateCombo } from '@/utils/getdate'
import dayjs from 'dayjs'
import useUserStore from '@/store/modules/user'
const userStore = useUserStore()
defineOptions({
  name: 'AdTworderListSearch'
})
import type { IAxios } from 'axios'
import { tr } from 'element-plus/es/locale'
const route = useRoute()
const queryInfo = reactive<QTworder>({
  sort: 'createtime',
  order: 'desc',
  pageSize: 20,
  page: 1,
  startTime: '',
  endTime: '',
  loginUserId: userStore.user?.userid.toString(),
  cacheDTOKey: '',
  adresourceapplicationid: '', // 资源Id
  iapprovestatus: '', // 订单状态
  queryKey: '', // 查询关键字
  bshowedit: false
})
const viewImageRef = ref()
const discountGroupFormRef = ref<FormInstance>()
const discountGroupSelection = ref<Tworder[]>()
const router = useRouter()
/** 表格高度 */
const tableheight = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 数据总条数 */
const pageTotal = ref<number>(0)
/** 编辑 dialog */
const dialogVisible = ref(false)
/** 查看详情窗口显示状态 */
const dialogDetailVisible = ref(false)
/** 客户选择 */
const flowshow = ref(false)
/** 修改和详情切换 */
const editsee = ref(false)
/** 流程id */
const flowdata = ref<TCustomerApprove>({
  flowId: '',
  id: '',
  iapproveType: 5
})
/** 审批状态下拉列表 */
const approveStatusList: IDataCombo[] = getEnumCombo(EApproveStatus)
/** 获取流程参数 */
const flowEFlowType: IDataCombo[] = getEnumCombo(EFlowType)
/** 资源id */
const adresourceapplicationid = ref('')
/**
 * 资源id
 */
const Resourceid = ref('')
/** 订单列表 */
const WorderList = ref<Tworder[]>([])
/** 订单id */
const WorderId = ref('')
/** 控制清空弹窗 */
const redetdata = ref(false)
/** 是否补刊 */
const isbukan = ref(0)
/** 时间 */
const initDate = reactive<TDateType>(getInitDateCombo('6'))

onMounted(() => {
  const query: LocationQuery = route.query
  Resourceid.value = (query.Resourceid as LocationQueryValue) ?? 'null'
  queryInfo.adresourceapplicationid = (query.Resourceid as LocationQueryValue) ?? ''
  queryInfo.endTime = nowTime()
  queryInfo.startTime = initData()
  nextTick(() => {
    loaddata()
    if (Resourceid.value !== 'null') {
      dialogVisible.value = true
      redetdata.value = true
    }
  })
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})

/**
 * 查看详情按钮事件
 * @param row
 */
const onDetail = (data: [string]) => {
  adresourceapplicationid.value = data![0] as string
  dialogDetailVisible.value = true
}
/**
 * 获取总表列表
 */
const loaddata = () => {
  getorderPagelistApi(queryInfo).then(({ obj, total }: IAxios<Tworder[]>) => {
    pageTotal.value = total
    WorderList.value = obj
    // WorderList.value = obj.filter((item) => item.idiscountapprovestatus !== 0)
    redetdata.value = true
    console.log(WorderList.value)
  })
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'Discount'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
/**
 * 选择
 * @param rows
 */
const handleSelectionChange = (rows: Tworder[]) => {
  discountGroupSelection.value = rows
}
/**
 * 改变页码总数时
 */
const handleSizeChange = (val: number) => {
  queryInfo.pageSize = val
  loaddata()
}
/**
 * 改变页数时
 * @param val
 */
const handleCurrentChange = (val: number) => {
  queryInfo.page = val
  loaddata()
}
/**
 * 排序
 * @param val
 */
const handleSortChange = (val: { prop: string; order: string }) => {
  queryInfo.sort = val.prop

  if (val.order === 'ascending') {
    queryInfo.order = 'asc'
  } else if (val.order === 'descending') {
    queryInfo.order = 'desc'
  } else {
    queryInfo.order = ''
  }
  loaddata()
}

/**
 * 新增
 */
const handleAdd = () => {
  dialogVisible.value = true
  redetdata.value = false
  isbukan.value = 0
}
/**
 * 补刊
 */
const handleBukan = () => {
  dialogVisible.value = true
  redetdata.value = false
  isbukan.value = 3
}
/**
 * 修改
 * @param row
 */
const handleUpdate = (row: Tworder) => {
  WorderId.value = row.id!
  redetdata.value = true
  editsee.value = false
  dialogVisible.value = true
}
/**
 * 查看详情
 */
const handleSee = (row?: any, number?: number) => {
  viewImageRef.value.view(row.scontractnum)
  console.log(row)
}
/**
 * 删除
 * @param row
 */
const handleDelete = (row?: Tworder) => {
  modal.confirm('是否删除?').then(() => {
    const ids = []
    if (row) {
      ids.push(row.id)
    } else {
      discountGroupSelection.value?.forEach((item) => ids.push(item.id))
    }
    if (!ids.length) {
      modal.msgWarning('请选择删除记录')
      return
    }
    deleteorderApi(ids.join(',')).then(({ success, msg }: IAxios) => {
      if (success) {
        modal.msgSuccess('操作成功')
      } else {
        modal.msgError(msg)
      }
      loaddata()
    })
  })
}
/**
 * 取消
 */
const handleCancel = () => {
  discountGroupFormRef.value?.resetFields()
  loaddata()
  WorderId.value = ''
  dialogVisible.value = false
}
/**
 * 关闭流程
 */
const handleCancelflow = () => {
  flowshow.value = false
  flowdata.value.flowId = ''
  loaddata()
}
/**
 * 保存信息关闭订单弹窗
 */
const closedialogVisible = (val: string) => {
  dialogVisible.value = false
}
/** 选择流程组件 */
const selecflowtype = (val: Tadindustrylist) => {
  flowdata.value.flowId = val.id
}
/**
 * 审核流程类型,true为折扣审核流程，false为订单审核流程
 */
const onflowtype = ref(true)
/**
 * 选择折扣审核流程
 */
const onshowflowzk = (id: string) => {
  onflowtype.value = true
  flowshow.value = true
  flowdata.value.id = id
}
/**
 * 选择订单审核流程
 */
const onshowflow = (id: string) => {
  onflowtype.value = false
  flowshow.value = true
  flowdata.value.id = id
}
/**
 * 提交审核
 * @param row
 */
const onToCheck = () => {
  ElMessageBox.confirm('确定提交审核吗？提交后将不能进行修改', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      if (onflowtype.value === true) {
        flowdata.value.iapproveType = 4
        discountApproveOrderApi(flowdata.value).then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('操作成功')
            flowshow.value = false
            loaddata()
          }
        })
      } else if (onflowtype.value === false) {
        flowdata.value.iapproveType = 5
        approveOrderApi(flowdata.value).then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('操作成功')
            flowshow.value = false
            loaddata()
          }
        })
      }
    })
    .catch(() => {})
}
/**
 * @description: 开始日期结束日期比较如果开始日期小于结束日期则颠倒
 * @param {*} val
 * @return {*}
 */
const onisAfterTimeduplicate = (val: string) => {
  const isAfter: boolean = dayjs(queryInfo.startTime).isAfter(queryInfo.endTime)
  const startTime = queryInfo.startTime
  const endTime = queryInfo.endTime
  if (isAfter) {
    queryInfo.startTime = startTime
    queryInfo.endTime = startTime
  }

  loaddata()
}
/**
 * 时间选择
 */
const onChangeDate = (val: TDateTimeType) => {
  queryInfo.startTime = dayjs(val.startTime).startOf('day').format('YYYY-MM-DD HH:mm:ss')
  queryInfo.endTime = dayjs(val.endTime).endOf('day').format('YYYY-MM-DD HH:mm:ss')
  if (queryInfo.startTime && queryInfo.endTime) {
    loaddata()
  }
}
/**
 * 获取当前时间
 */
const nowTime = () => {
  const now = new Date()
  const year = now.getFullYear()
  const month = (now.getMonth() + 1).toString().padStart(2, '0')
  const day = now.getDate().toString().padStart(2, '0')
  return year + '-' + month + '-' + day
}
const initData = () => {
  // 当前日期的前七天
  var myDate = new Date()
  var dateArray = dayjs(myDate.setTime(myDate.getTime() - 3600 * 1000 * 24 * parseInt('6'))).format(
    'YYYY-MM-DD'
  )
  return dateArray
}
</script>
<style scoped></style>
