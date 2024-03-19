<!--
 * @Author: wanghl
 * @Date: 2023-12-13 16:13:49
 * @LastEditTime: 2024-02-21 09:47:06
 * @LastEditors: wanghl
 * @Description:广告资源组件
-->

<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
        <!-- <el-select
          v-model="queryParams.iapprovestatus"
          placeholder="请选择审核状态"
          clearable
          style="width: 100px; margin-right: 5px"
          @change="handleQuery"
          @clear="handleQuery"
        >
          <el-option
            v-for="t in approveStatusList"
            :key="t.id"
            :label="t.name"
            :value="t.id"
          ></el-option>
        </el-select> -->
        <HgZtreeSelect
          :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYPERSON"
          :selectids="queryParams.employid ? [queryParams.employid] : []"
          style="width: 100px"
          :filterable="true"
          :treeparams="{ bIgnorePermissions: true }"
          @selectionztree="onSelectionEmp"
        ></HgZtreeSelect>
        <el-input
          v-model="queryParams.queryKey"
          clearable
          placeholder="请输入标题关键字"
          class="input-with-select"
          style="width: 200px"
          @keyup.enter="handleQuery('quanbu')"
          @clear="handleQuery('quanbu')"
        >
        </el-input>
        <el-button type="primary" icon="Search" @click="handleQuery('quanbu')">搜索</el-button>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="dataTableRef"
        :data="adResourcApplicationList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        border
      >
        <el-table-column label="部门" prop="deptname" min-width="100" show-overflow-tooltip />
        <el-table-column label="人员名称" prop="employname" width="120" show-overflow-tooltip />
        <el-table-column label="客户类型" prop="icusttype" width="100" align="center">
          <template #default="scope">
            <span v-if="scope.row.icusttype === ECustomerType.直接客户">直接客户</span>
            <span v-if="scope.row.icusttype === ECustomerType.代理公司">代理公司</span>
            <span v-if="scope.row.icusttype === ECustomerType.内容生产方">内容生产方</span>
          </template>
        </el-table-column>
        <el-table-column
          label="客户名称"
          prop="customername"
          min-width="160"
          show-overflow-tooltip
        />
        <el-table-column label="广告标题" prop="sadtitle" width="160" show-overflow-tooltip />
        <el-table-column label="开始日期" prop="dstartdate" width="100">
          <template #default="scope">
            <span>{{ dayjs(scope.row.dstartdate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="结束日期" prop="denddate" width="100">
          <template #default="scope">
            <span>{{
              scope.row.denddate !== null ? dayjs(scope.row.denddate).format('YYYY-MM-DD') : ''
            }}</span>
          </template>
        </el-table-column>
        <el-table-column label="广告内容" prop="sadcontent" width="160" show-overflow-tooltip />
        <el-table-column label="快速上版" prop="bquickly" width="160" align="center">
          <template #default="scope">
            <el-checkbox v-model="scope.row.bquickly" disabled></el-checkbox>
          </template>
        </el-table-column>

        <el-table-column
          label="操作"
          align="center"
          width="140"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <!-- v-if="scope.row.iapprovestatus === EApproveStatus.通过" -->
            <el-button
              link
              type="success"
              icon="Edit"
              size="small"
              title="修改"
              @click="onEdit(scope.row)"
              >选择资源</el-button
            >
            <el-button
              link
              type="primary"
              icon="view"
              size="small"
              title="查看详情"
              @click="onDetail(scope.row)"
              >详情</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="queryParams.page"
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
    <!-- 详情 -->
    <el-dialog
      v-model="dialogDetailVisible"
      title="广告资源申请表详情"
      :width="adResourceApplicationForm.iapprovestatus !== EApproveStatus.待审 ? '1200' : '1000'"
      align-center
      :close-on-click-modal="false"
      :destroy-on-close="true"
    >
      <HgAdSourceDetail
        :id="adResourceApplicationForm.id"
        :showflag="adResourceApplicationForm.iapprovestatus !== EApproveStatus.待审 ? true : false"
      ></HgAdSourceDetail>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from 'vue'
import { IAxios } from 'axios'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { THgFileUploadParam, TUpFile } from '@/types/components/hgfileupload'
import { computTableHeight, required, tableHeaderColor, formatDate } from '@/utils'
import {
  deleteAdResourceApplicationByIdApi,
  getAdResourceApplicationPageListApi,
  getProcessInstanceByIdApi,
  saveAdResourceApplicationApi,
  saveCheckInfoApi,
  updateAdResourceApplicationApi,
  updateCheckApi
} from '@/api/ad/adresourceapplication'
import {
  EApproveStatus,
  ECustomerType,
  EFileCategory,
  EFlowType,
  EHgDeptZtreeUrl
} from '@/types/common/enumindex'
import useUserStore from '@/store/modules/user'
import { getEnumCombo } from '@/api/common/index'
import { IDataCombo } from '@/types/common/DataCombo'
import { TSelectZtree } from '@/types/common'
import { FormInstance, FormRules, dayjs } from 'element-plus'
import { TAdCustomer } from '@/types/views/customer'
import {
  IAdResourceApplicationDTO,
  TAdResourceApplicationQuery
} from '@/types/views/ad/adresourceapplication'
import { getMediaDataComboApi } from '@/api/media/media'
import { Tadindustrylist, TypeCurrentData } from '@/types/components/hgindustry'
import { getFlowlistComboByFlowTypeApi } from '@/api/flow'

const user = useUserStore().user
const props = defineProps({
  /** 直接客户id */
  customerIds: {
    type: Array as () => string[],
    default: () => []
  },
  /** 审核状态(0-待审、1-在审、2-通过、3-否决、4-撤销、5-无效） */
  approveStatus: {
    type: String,
    default: ''
  }
})
/** 导出返回数据 */
const emit = defineEmits<{
  selectionresourcedata: [data: IAdResourceApplicationDTO]
}>()
const total = ref(0)
/** 表格高度 */
const tableheight = ref(0)
/** 选择客户显示状态 */

/** 编辑窗口显示状态 */
const dialogEditVisible = ref(false)

/** 查看详情窗口显示状态 */
const dialogDetailVisible = ref(false)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 时间 */
const timedata = reactive<TDateType>({
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
watch(
  () => props.customerIds,
  (val) => {
    queryParams.customerIds = val
    console.log('props.customerIds', val)
    handleQuery()
  },
  {
    deep: true
  }
)
/** 查询条件 */
const queryParams = reactive<TAdResourceApplicationQuery>({
  sort: 'dapplytime',
  order: 'desc',
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  customerIds: props.customerIds || [],
  employid: '',
  icusttype: '',
  iapprovestatus: props.approveStatus || ''
})
const adResourceApplicationInfo: IAdResourceApplicationDTO = {
  id: '',
  icusttype: ECustomerType.直接客户,
  deptid: user!.deptid!.toString(),
  deptname: user!.deptname!,
  employid: user!.userid,
  employname: user!.username,
  dstartdate: dayjs().format('YYYY-MM-DD'),
  denddate: '',
  sadtitle: '',
  sadcontent: '',
  sremark: '',
  customerid: '',
  customername: '',
  bquickly: false,
  iapprovestatus: EApproveStatus.待审,
  filelist: []
}
/** 编辑实体 */
const adResourceApplicationForm = ref<IAdResourceApplicationDTO>({ ...adResourceApplicationInfo })
/** 列表对象 */
const adResourcApplicationList = ref<IAdResourceApplicationDTO[]>([])
/** 审核列表 */
const checkList = ref<IAdResourceApplicationDTO[]>([])
/** 媒体下拉列表 */
const mediaCombo = ref<IDataCombo[]>([])
/** 流程类型列表 */
const flowTypeList = ref<Tadindustrylist[]>([])
onMounted(() => {
  handleQuery()
  getMediaDataCombo()
  getFlowlistCombo()
  queryParams.customerIds = props.customerIds
  console.log('props.customerIds', props.customerIds)
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight() - 200
})
/**
 * 获取审批流程下拉列表
 */
const getFlowlistCombo = () => {
  getFlowlistComboByFlowTypeApi(EFlowType.资源审批流程).then((res: IAxios<Tadindustrylist[]>) => {
    if (res.success) {
      flowTypeList.value = res.obj
    }
  })
}
/**
 * 获取媒体下拉列表
 */
const getMediaDataCombo = () => {
  getMediaDataComboApi().then((res: IAxios<IDataCombo[]>) => {
    if (res.success) {
      mediaCombo.value = res.obj
    }
  })
}
/**
 * 查询
 */
const handleQuery = (data?: string) => {
  if (data === 'quanbu') {
    queryParams.customerIds = []
  }
  getAdResourceApplicationPageListApi(queryParams)
    .then((res: IAxios<IAdResourceApplicationDTO[]>) => {
      if (res.success) {
        adResourcApplicationList.value = res.obj
        total.value = res.total
      }
    })
    .finally(() => {})
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
      const ids = []
      ids.push(adResourceApplicationForm.value.id)
      const data = {
        ids: ids.join(','),
        flowid: adResourceApplicationForm.value.flowid as string
      }
      updateCheckApi(data).then((res: IAxios) => {
        if (res.success) {
          ElMessage.success('操作成功')
          handleQuery()
        }
      })
    })
    .catch(() => {})
}
/**
 * 编辑按钮事件
 * @param row
 */
const onEdit = (row: IAdResourceApplicationDTO) => {
  emit('selectionresourcedata', row)
}
/**
 * 查看详情按钮事件
 * @param row
 */
const onDetail = (row: IAdResourceApplicationDTO) => {
  dialogDetailVisible.value = true
  adResourceApplicationForm.value = { ...row }
}
/**
 * 选择人员
 * @param val
 */
const onSelectionEmp = (val: TSelectZtree[]) => {
  const employid = val.map((item) => item.id).join(',')
  queryParams.employid = employid
}
/**
 * 重置form表单
 * @param formEl
 */
const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
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
  queryParams.page = val
  handleQuery()
}
</script>

<style scoped></style>
