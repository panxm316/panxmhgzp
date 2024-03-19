<!--
 * @Author: caogd
 * @Date: 2023-10-30 09:02:02
 * @LastEditTime: 2024-02-19 14:20:37
 * @LastEditors: suny suny@hgzp.com.cn
 * @Description: 客户预收款
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex" style="width: 94%">
        <el-button type="primary" icon="Plus" @click="onAdd">新增</el-button>
        <HgDateIndex @onresponse="ontime"></HgDateIndex>

        <el-input
          v-model="queryKey"
          clearable
          placeholder="支持客户、业务员等关键词"
          style="width: 260px"
          @keyup.enter="getCustomerChargePageList"
          @clear="getCustomerChargePageList"
        >
        </el-input>
        <el-button type="primary" icon="Search" @click="getCustomerChargePageList">搜索</el-button>
        <el-tooltip
          class="item"
          effect="dark"
          :content="showSearch ? '隐藏非常用' : '显示非常用'"
          placement="top"
        >
          <el-button
            circle
            style="position: static"
            :icon="showSearch == false ? 'ArrowRightBold' : 'ArrowDownBold'"
            @click="toggleSearch()"
          />
        </el-tooltip>
        <el-tooltip class="item" effect="dark" content="帮助" placement="top">
          <el-button class="help" type="text" @click="showhelp()">?</el-button>
        </el-tooltip>
      </div>
      <div v-show="showSearch" class="showSearch">
        <span>金额区间：</span>
        <el-space :size="10" spacer="-">
          <el-input-number
            v-model="namountreceivedge"
            :precision="2"
            placeholder="最低价"
            :min="0"
            :controls="false"
            @blur="namountreceivedBlur"
          />

          <el-input-number
            v-model="namountreceivedle"
            :precision="2"
            placeholder="最高价"
            :min="0"
            :controls="false"
            @blur="namountreceivedBlur"
          />
        </el-space>
        <span>客户查询：</span>
        <el-input
          v-model="customerQuery.sname"
          style="width: 260px"
          placeholder="请选择客户"
          readonly
          @click="onSelectCustomer('query')"
        >
          <template #append>
            <el-button
              style="margin: -5px !important"
              icon="Search"
              circle
              title="选择客户"
              @click="onSelectCustomer('query')"
            ></el-button>
          </template>
          <template #suffix>
            <el-button
              v-show="customerQuery.sname"
              style="margin: 0px !important"
              link
              icon="CircleClose"
              @click="deletCustomerQuery"
            ></el-button>
          </template>
        </el-input>
      </div>
    </div>
    <div class="table_box">
      <el-table
        :data="customerChargeList"
        row-key="id"
        :height="tableHeight"
        :border="true"
        stripe
        :header-cell-style="tableHeaderColor"
        @sort-change="handleSortChange"
      >
        <el-table-column prop="businessentityname" label="经营主体" :width="150"></el-table-column>
        <el-table-column prop="customername" label="客户" :min-width="150" sortable="custom">
        </el-table-column>
        <el-table-column prop="custtypeName" label="客户类型" :width="150"> </el-table-column>
        <el-table-column prop="dpaydate" label="付款时间" :width="200" sortable="custom">
        </el-table-column>
        <el-table-column prop="invoice" label="发票号" :min-width="200" sortable="custom">
        </el-table-column>
        <el-table-column prop="namountreceived" label="入账金额 " :min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namountreceived, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="namountbalance" label="剩余金额 " :min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namountbalance, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="employname"
          label="业务员 "
          :width="150"
          sortable="custom"
          align="center"
        >
        </el-table-column>
        <el-table-column prop="paymethodname" label="付款方式 " :width="150" align="center">
        </el-table-column>
        <el-table-column prop="printitemname" label="开票项目 " :width="150" align="center">
        </el-table-column>
        <el-table-column prop="sremark" label="备注 " :width="200" align="center">
        </el-table-column>
        <el-table-column
          label="操作"
          :align="'left'"
          width="180"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              v-if="!scope.row.invoicecode"
              link
              type="success"
              icon="Edit"
              size="small"
              style="margin-left: 5px"
              @click="onEdit(scope.row)"
              >修改</el-button
            >
            <el-button
              v-if="!scope.row.invoicecode"
              link
              type="primary"
              icon="Printer"
              size="small"
              title="打印"
              @click="handlePrint(scope.row)"
              >打印</el-button
            >
            <el-button
              v-if="!scope.row.invoicecode"
              link
              type="danger"
              icon="Delete"
              size="small"
              @click="deleteCustomerCharge(scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <!-- 页码 -->
      <el-pagination
        v-model:current-page="pageIndex"
        v-model:page-size="pageSize"
        :page-sizes="pageSizes"
        class="pagination"
        small
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="pageTotal"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <!-- 编辑 -->
    <el-dialog
      v-model="editShow"
      title="预收费录入"
      width="700px"
      append-to-body
      destroy-on-close
      :close-on-click-modal="false"
    >
      <el-form
        ref="customerChargeRef"
        :model="customerChargeForm"
        :rules="rules"
        label-width="140px"
      >
        <el-row :gutter="50">
          <el-col :span="22">
            <el-form-item label="客户名称" prop="customername">
              <el-input v-model="customerChargeForm.customername" placeholder="请选择客户" disabled>
                <template #append>
                  <el-button
                    icon="Search"
                    circle
                    title="选择客户"
                    @click="onSelectCustomer('from')"
                  />
                </template>
              </el-input>
            </el-form-item>
            <el-form-item label="预付金额" prop="namountreceived">
              <HgMoney
                v-model="customerChargeForm.namountreceived"
                placeholder="请输入预付金额"
              ></HgMoney>
            </el-form-item>
            <el-form-item label="业务员" prop="employid">
              <HgZtreeSelect
                :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYPERSON"
                :selectids="customerChargeForm.employid ? [customerChargeForm.employid] : []"
                :filterable="true"
                :treeparams="{ bIgnorePermissions: true }"
                style="width: 560px"
                @selectionztree="onSelectionNewEmp"
              ></HgZtreeSelect>
            </el-form-item>
            <el-form-item label="经营主体" prop="businessentityid">
              <el-select
                v-model="customerChargeForm.businessentityid"
                placeholder="请选择经营主体"
                clearable
                filterable
                style="width: 560px"
              >
                <el-option
                  v-for="t in businessentityCombo"
                  :key="t.id"
                  :label="t.sname"
                  :value="t.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="付款日期" prop="dpaydate">
              <el-date-picker
                v-model="customerChargeForm.dpaydate"
                type="date"
                :clearable="false"
                value-format="YYYY-MM-DD"
                placeholder="请选择"
                :shortcuts="shortcuts"
                style="width: 560px"
              />
            </el-form-item>
            <el-form-item label="付款方式" prop="paymethodid">
              <el-select
                v-model="customerChargeForm.paymethodid"
                placeholder="请选择付款方式"
                filterable
                style="width: 560px"
              >
                <el-option
                  v-for="t in paymethodCombo"
                  :key="t.id"
                  :label="t.name"
                  :value="t.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="开票项目" prop="printitemid">
              <el-select
                v-model="customerChargeForm.printitemid"
                placeholder="请选择开票项目"
                clearable
                filterable
                style="width: 560px"
              >
                <el-option
                  v-for="t in adprintitemCombo"
                  :key="t.id"
                  :label="t.sname"
                  :value="t.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="备注" prop="sremark">
              <el-input v-model="customerChargeForm.sremark"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" icon="Check" @click="onSave(customerChargeRef)"
            >确 定</el-button
          >
          <el-button icon="Close" @click="onCancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
    <!-- 选择客户 -->
    <el-dialog
      v-model="customershow"
      title="选择客户"
      width="1000"
      align-center
      destroy-on-close
      :close-on-click-modal="false"
      append-to-body
    >
      <el-row :gutter="50">
        <el-col :span="24">
          <HgSalesCustomer
            :buse="true"
            :showbuse="false"
            @selectiondata="onselectiondata"
          ></HgSalesCustomer>
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 打印 -->
    <el-dialog
      v-model="dialogPrintShow"
      title="发票打印"
      width="1100"
      append-to-body
      destroy-on-close
      draggable
      :close-on-click-modal="false"
    >
      <HgInvoicePrint
        :invoiceid="invoiceID"
        style="max-width: 100%; overflow: auto"
      ></HgInvoicePrint>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import HgInvoicePrint from '@/components/HgInvoicePrint/index.vue'
import { deepClone } from '@/utils'
import { IAxios } from 'axios'
import { FormInstance, FormRules } from 'element-plus'
import { computTableHeight, tableHeaderColor, formatMoney } from '@/utils/index'
import {
  TCustomerChargeQuery,
  ICustomerChargeDTO,
  TCustomerChargeVO
} from '@/types/views/finance/customercharge'
import {
  getCustomerChargePageListApi,
  saveCustomerChargeApi,
  getCustomerChargeDetailApi,
  updateCustomerChargeApi,
  deleteCustomerChargeApi
} from '@/api/finance/customercharge'
import { IBusinessentity } from '@/types/views/finance/businessentity'
import { getBusinessentityComboApi } from '@/api/finance/businessentity'
import { getPaymethodComboApi } from '@/api/finance/paymethod'
import { getAdPrintItemComboApi } from '@/api/finance/adprintitem'
import { IAdprintitem } from '@/types/views/finance/adprintitem'
import { EHgDeptZtreeUrl, ESType } from '@/types/common/enumindex'
import { TSelectZtree } from '@/types/common'
import { useRouter } from 'vue-router'
import dayjs from 'dayjs'
import { IDataCombo } from '@/types/common/DataCombo'
import type { TAdCustomer } from '@/types/views/customer'
import { TDateTimeType } from '@/types/components/hgdateindex'
import modal from '@/plugins/modal'

defineOptions({
  name: 'ResourceType'
})
const router = useRouter()
/** 显示隐藏非常用**/
const showSearch = ref(false)
/** Form名称 */
const customerChargeRef = ref<FormInstance>()

// 分页相关
const sort = ref('dpaydate')
const order = ref('desc')
const pageSizes = [15, 20, 30, 40]
const pageIndex = ref(1)
const pageSize = ref(15)
/** 查询总页数 */
const pageTotal = ref(0)
/** 打印窗口显示状态 */
const dialogPrintShow = ref(false)
/** 选择打印时返回的发票表id */
const invoiceID = ref('')
/** 表格高度 */
const tableHeight = ref<number>(0)
/** 列表 */
const customerChargeList = ref<TCustomerChargeVO[]>([])
/** 查询关键字 */
const queryKey = ref('')
const timeParams = reactive({
  startTime: '',
  endTime: ''
})
/** 编辑显示状态 */
const editShow = ref(false)
/** 经营主体下拉 */
const businessentityCombo = ref<IBusinessentity[]>([])
/** 付款方式下拉 */
const paymethodCombo = ref<IDataCombo[]>([])
/** 开票项目下拉 */
const adprintitemCombo = ref<IAdprintitem[]>([])
/** from表单初始化对象 */
const customerChargeInit: ICustomerChargeDTO = {
  id: '',
  businessentityid: '',
  businessentityname: '',
  customerid: '',
  customername: '',
  employid: '',
  employname: '',
  icusttype: 0,
  itype: ESType.预收款,
  itypeinvoice: 4,
  namountreceived: 0,
  paymethodid: '',
  paymethodname: '',
  printitemid: '',
  printitemname: '',
  dpaydate: dayjs().format('YYYY-MM-DD')
}
/** 添加form */
const customerChargeForm = ref<ICustomerChargeDTO>(customerChargeInit)
/** 验证规则 */
const rules = reactive<FormRules<ICustomerChargeDTO>>({
  customername: [{ required: true, message: '客户不能为空', trigger: 'change' }],
  namountreceived: [{ required: true, message: '预付金额不能为空', trigger: 'blur' }],
  employid: [{ required: true, message: '业务员不能为空', trigger: 'change' }],
  businessentityid: [{ required: true, message: '经营主体不能为空', trigger: 'change' }],
  dpaydate: [{ required: true, message: '付款日期不能为空', trigger: 'blur' }],
  paymethodid: [{ required: true, message: '付款方式不能为空', trigger: 'change' }],
  printitemid: [{ required: true, message: '开票项目不能为空', trigger: 'change' }],
  sremark: [{ max: 200, message: '最大输入不超过200字符', trigger: 'blur' }]
})
/** 客户选择弹出 */
const customershow = ref(false)
/** 最低价 */
const namountreceivedge = ref()
/** 最高价 */
const namountreceivedle = ref()
/** 当前返回选中的客户 */
const customerQuery = ref<TAdCustomer>({})
/** 点击的from上的客户还是 */
let customerFlag = ''
const shortcuts = [
  {
    text: '今天',
    value: new Date()
  }
]
onMounted(() => {
  getCustomerChargePageList()
  getBusinessentityCombo()
  getPaymethodCombo()
  getAdPrintItemCombo()
  /**
   * 计算表格高度
   */
  tableHeight.value = computTableHeight()
})
/**
 * 打印按钮
 * @param row
 */
const handlePrint = (row: TCustomerChargeVO) => {
  invoiceID.value = row.invoiceid!
  dialogPrintShow.value = true
}
/**
 * 经营主体下拉查询
 */
const getBusinessentityCombo = () => {
  getBusinessentityComboApi().then(({ success, obj }: IAxios<IBusinessentity[]>) => {
    if (success) {
      businessentityCombo.value = obj
    }
  })
}
/**
 * 付款方式下拉
 */
const getPaymethodCombo = () => {
  getPaymethodComboApi().then(({ success, obj }: IAxios<IDataCombo[]>) => {
    if (success) {
      paymethodCombo.value = obj
    }
  })
}
/**
 * 开票项目下拉
 */
const getAdPrintItemCombo = () => {
  getAdPrintItemComboApi().then(({ success, obj }: IAxios<IAdprintitem[]>) => {
    if (success) {
      adprintitemCombo.value = obj
    }
  })
}
/**
 * 预收费分页列表
 */
const getCustomerChargePageList = () => {
  const data: TCustomerChargeQuery = {
    sort: sort.value,
    order: order.value,
    pageSize: pageSize.value,
    page: pageIndex.value,
    queryKey: queryKey.value,
    startTime: timeParams.startTime,
    endTime: timeParams.endTime,
    namountreceivedge: namountreceivedge.value,
    namountreceivedle: namountreceivedle.value,
    customerid: customerQuery.value.id
  }
  getCustomerChargePageListApi(data).then((res: IAxios<TCustomerChargeVO[]>) => {
    if (res.success) {
      customerChargeList.value = res.obj
      pageTotal.value = res.total
    }
  })
}
/**
 * 时间选择
 * @param val
 */
const ontime = (val: TDateTimeType) => {
  let timev = false
  if (timeParams.startTime) {
    timev = true
  }
  timeParams.startTime = val.startTime
  timeParams.endTime = val.endTime
  if (timev) {
    getCustomerChargePageList()
  }
}
const onAdd = () => {
  editShow.value = true
  clearForm()
}
/** 弹出客户选择 */
const onSelectCustomer = (val: string) => {
  customerFlag = val
  customershow.value = true
}
/** 客户组件返回函数 */
const onselectiondata = (val: TAdCustomer) => {
  if (customerFlag === 'from') {
    customerChargeForm.value.customerid = val.id ?? ''
    customerChargeForm.value.customername = val.sname ?? ''
    customerChargeForm.value.icusttype = val.itype ?? 0
  } else {
    customerQuery.value = val
    getCustomerChargePageList()
  }

  customershow.value = false
}
/** 清空查询选择的客户 */
const deletCustomerQuery = () => {
  customerQuery.value = {}
  getCustomerChargePageList()
}
/**
 * 编辑类型信息按钮事件
 * @param row
 */
const onEdit = (row: TCustomerChargeVO) => {
  if (!row.id) {
    ElMessage.warning('请选择预付款')
    return
  }
  getCustomerChargeDetailApi(row.id).then(({ success, obj }: IAxios<TCustomerChargeVO>) => {
    if (success) {
      customerChargeForm.value = deepClone(obj) as ICustomerChargeDTO
      editShow.value = true
    }
  })
}
/**
 * 编辑的时候选择业务人员
 * @param val
 */
const onSelectionNewEmp = (val: TSelectZtree[]) => {
  const employname = val.map((item) => item.name).join(',')
  const employid = val.map((item) => item.id).join(',')
  customerChargeForm.value.employid = employid
  customerChargeForm.value.employname = employname
}
/**
 * 新增修改保存事件
 * @param formEl
 */
const onSave = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid, fields) => {
    if (valid) {
      if (customerChargeForm.value.namountreceived <= 0) {
        ElMessage.warning('预付金额必须大于0')
        return
      }
      const data: ICustomerChargeDTO = { ...customerChargeForm.value }
      data.businessentityname =
        businessentityCombo.value.find((item) => item.id === data.businessentityid)?.sname ?? ''
      data.paymethodname =
        paymethodCombo.value.find((item) => item.id === data.paymethodid)?.name ?? ''
      data.printitemname =
        adprintitemCombo.value.find((item) => item.id === data.printitemid)?.sname ?? ''
      if (data.id) {
        updateCustomerChargeApi(data).then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('修改成功')
            getCustomerChargePageList()
            customerChargeRef.value?.resetFields()
            editShow.value = false
          }
        })
      } else {
        saveCustomerChargeApi(data).then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('保存成功')
            getCustomerChargePageList()
            customerChargeRef.value?.resetFields()
            editShow.value = false
            dialogPrintShow.value = true
            invoiceID.value = res.obj
          }
        })
      }
    } else {
      console.log('error submit!', fields)
    }
  })
}

const deleteCustomerCharge = (row: TCustomerChargeVO) => {
  modal.confirm('是否删除选中数据？').then(() => {
    deleteCustomerChargeApi(row.id!).then(({ success }: IAxios) => {
      if (success) {
        modal.msgSuccess('删除成功')
        getCustomerChargePageList()
      }
    })
  })
}
/**
 * 取消编辑
 */
const onCancel = () => {
  clearForm()
  editShow.value = false
}
/** 显示非常用**/
const toggleSearch = () => {
  showSearch.value = !showSearch.value
  nextTick(function () {
    tableHeight.value = computTableHeight()
  })
}
/**
 * 清空编辑form内容
 */
const clearForm = () => {
  customerChargeForm.value = { ...customerChargeInit }
  customerChargeRef.value?.resetFields()
}
/**
 * 排序
 * @param val
 */
const handleSortChange = (val: { prop: string; order: string }) => {
  sort.value = val.prop

  if (val.order === 'ascending') {
    order.value = 'asc'
  } else if (val.order === 'descending') {
    order.value = 'desc'
  } else {
    order.value = ''
  }
  getCustomerChargePageList()
}
/**
 * 翻页
 * @param val
 */
const handleSizeChange = (val: number) => {
  // 分页当前页
  pageIndex.value = val
  getCustomerChargePageList()
}
/**
 * 当前页
 * @param val
 */
const handleCurrentChange = (val: number) => {
  pageIndex.value = val
  getCustomerChargePageList()
}
/**
 * 价格区间鼠标离开事件
 */
const namountreceivedBlur = () => {
  // 如果低价高择互相反过来
  if (namountreceivedge.value > namountreceivedle.value) {
    const lodnamountreceivedge = namountreceivedge.value
    const lodnamountreceivedle = namountreceivedle.value
    namountreceivedge.value = lodnamountreceivedle
    namountreceivedle.value = lodnamountreceivedge
  }
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'resourcetype'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>

<style lang="scss" scoped>
.showSearch span {
  color: #606266;
  font-size: 14px;
}
.el-form-item {
  margin: 15px 0;
}
</style>
