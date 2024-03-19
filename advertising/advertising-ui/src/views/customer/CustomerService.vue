<!--
 * @Author: suny
 * @Date: 2023-11-09 12:47:47
 * @LastEditTime: 2024-03-12 10:36:10
 * @LastEditors: wanghl
 * @Description: 客户服务管理
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button
          type="primary"
          icon="Plus"
          @click="handleAdd"
          v-if="props.customerid === undefined"
          >新增</el-button
        >
        <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
        <el-select
          v-model="queryParams.iservicetype"
          placeholder="请选择服务类型"
          style="width: 100px; margin-right: 5px"
          clearable
          @change="handleQuery"
          @clear="handleQuery"
        >
          <el-option
            v-for="t in customerServiceTypeList"
            :key="t.id"
            :label="t.name"
            :value="t.id"
          ></el-option>
        </el-select>
        <el-input
          v-model="queryParams.queryKey"
          clearable
          placeholder="请输入广告形式关键字"
          class="input-with-select"
          style="width: 200px"
          @keyup.enter="handleQuery"
          @clear="handleQuery"
        >
        </el-input>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="dataTableRef"
        :data="customerServiceList"
        highlight-current-row
        :height="props.customerid === undefined ? tableheight : tableheight - 200"
        :header-cell-style="tableHeaderColor"
        border
      >
        <el-table-column label="服务类型" prop="iservicetype" width="100" align="center">
          <template #default="scope">
            {{ customerServiceTypeList.find((item) => item.id === scope.row.iservicetype)?.name }}
          </template>
        </el-table-column>
        <el-table-column label="人员名称" prop="employname" width="120" show-overflow-tooltip />
        <el-table-column
          label="客户名称"
          prop="customername"
          min-width="160"
          show-overflow-tooltip
        />
        <el-table-column label="开始日期" prop="dstartdate" width="100">
          <template #default="scope">
            <span>{{ dayjs(scope.row.dstartdate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="结束日期" prop="denddate" width="100">
          <template #default="scope">
            <span>{{ dayjs(scope.row.denddate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="联系人" prop="scontacts" width="100" align="center" />
        <el-table-column label="联系方式" prop="sphone" width="100" align="center" />

        <el-table-column label="标题" prop="stitle" min-width="160" show-overflow-tooltip />
        <el-table-column label="内容" prop="scontent" min-width="160" show-overflow-tooltip />
        <el-table-column label="结果" prop="sresult" width="160" show-overflow-tooltip />
        <el-table-column label="状态" prop="sstatus" width="160" />
        <el-table-column label="创建时间" prop="dcreatetime" width="160" />
        <el-table-column label="操作人" prop="operatorname" width="100" align="center" />
        <el-table-column label="最后修改人" prop="lastoperator" width="120" />
        <el-table-column label="最后修改时间" prop="dlasttime" width="160" />
        <el-table-column
          v-if="props.customerid === undefined"
          label="操作"
          align="left"
          width="200"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              link
              style="margin-left: 10px"
              type="success"
              icon="Edit"
              size="small"
              title="修改"
              @click="onEdit(scope.row)"
              >修改</el-button
            >
            <el-button
              link
              type="danger"
              icon="Delete"
              size="small"
              title="删除"
              @click="onDelete(scope.row)"
              >删除</el-button
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
    <el-dialog
      v-model="dialogEditVisible"
      title="客户服务"
      style="width: 700px"
      append-to-body
      :destroy-on-close="true"
    >
      <div>
        <el-row>
          <el-col :span="22">
            <el-form
              ref="customerServiceFormRef"
              :model="customerServiceForm"
              :rules="rules"
              label-width="120px"
              class="demo-workReportForm"
              status-icon
            >
              <el-form-item label="服务类型">
                <el-radio-group
                  v-model="customerServiceForm.iservicetype"
                  :disabled="customerServiceForm.id !== ''"
                >
                  <el-radio
                    v-for="customerServiceType in customerServiceTypeList"
                    :key="customerServiceType.id"
                    :label="customerServiceType.id"
                    >{{ customerServiceType.name }}</el-radio
                  >
                </el-radio-group>
              </el-form-item>
              <el-form-item label="标题" prop="stitle">
                <el-input v-model="customerServiceForm.stitle" class="inputwindht" />
              </el-form-item>
              <el-form-item label="业务员" prop="employid">
                <HgZtreeSelect
                  :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYPERSON"
                  :selectids="customerServiceForm.employid ? [customerServiceForm.employid] : []"
                  style="width: 500px"
                  :filterable="true"
                  :treeparams="{ bIgnorePermissions: true }"
                  @selectionztree="onSelectionNewEmp"
                ></HgZtreeSelect>
              </el-form-item>
              <el-form-item label="客户选择" prop="customername">
                <el-row>
                  <el-col>
                    <el-input
                      v-model="customerServiceForm.customername"
                      placeholder="请选择客户信息"
                      class="inputwindht"
                      style="width: 485px"
                      @input="customerServiceForm.customerid = ''"
                    >
                      <template #append>
                        <el-button
                          icon="Search"
                          circle
                          title="选择客户"
                          @click="dialogCustomerVisible = true"
                        />
                      </template>
                      <template #suffix>
                        <el-button
                          v-show="customerServiceForm.customername"
                          link
                          icon="CircleClose"
                          @click="SeeCustomerdelete()"
                        ></el-button>
                      </template>
                    </el-input>
                  </el-col>
                </el-row>
              </el-form-item>
              <el-form-item label="联系人" prop="scontacts">
                <el-input v-model="customerServiceForm.scontacts" class="inputwindht" />
              </el-form-item>
              <el-form-item label="联系方式" prop="sphone">
                <el-input v-model="customerServiceForm.sphone" class="inputwindht" />
              </el-form-item>
              <el-form-item label="状态" prop="sstatus">
                <el-select
                  v-model="customerServiceForm.sstatus"
                  filterable
                  allow-create
                  default-first-option
                  :reserve-keyword="false"
                  placeholder="请选择状态"
                  class="inputwindht"
                >
                  <el-option label="已记录" value="已记录" />
                  <el-option label="已解决" value="已解决" />
                  <el-option label="已结束" value="已结束" />
                </el-select>
              </el-form-item>
              <el-form-item label="日期范围" required>
                <el-row :span="24">
                  <el-col :span="11">
                    <el-form-item prop="dstartdate">
                      <el-date-picker
                        v-model="customerServiceForm.dstartdate"
                        type="date"
                        placeholder="开始日期"
                        :clearable="false"
                        value-format="YYYY-MM-DD"
                        style="width: 262px"
                      />
                    </el-form-item>
                  </el-col>
                  <el-col class="text-center" :span="2">至</el-col>
                  <el-col :span="11">
                    <el-form-item prop="denddate">
                      <el-date-picker
                        v-model="customerServiceForm.denddate"
                        type="date"
                        placeholder="结束日期"
                        :clearable="false"
                        value-format="YYYY-MM-DD"
                        style="width: 262px"
                      />
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-form-item>
              <el-form-item label="工作内容" prop="scontent">
                <el-input
                  v-model="customerServiceForm.scontent"
                  :rows="3"
                  type="textarea"
                  class="inputwindht"
                  resize="none"
                  placeholder="请输入..."
                />
              </el-form-item>
              <el-form-item label="结果反馈" prop="sresult">
                <el-input
                  v-model="customerServiceForm.sresult"
                  :rows="3"
                  type="textarea"
                  resize="none"
                  class="inputwindht"
                  placeholder="请输入..."
                  style="margin-bottom: 20px"
                />
              </el-form-item>
              <el-form-item>
                <div style="position: absolute; right: 5px">
                  <el-button
                    type="primary"
                    icon="Check"
                    @click="saveCustomerServiceForm(customerServiceFormRef)"
                    >保存</el-button
                  >
                  <el-button icon="Close" @click="dialogEditVisible = false">取消</el-button>
                </div>
              </el-form-item>
            </el-form>
            <!-- 选择客户 -->
            <el-dialog
              v-model="dialogCustomerVisible"
              title="选择客户"
              width="1000"
              align-center
              :close-on-click-modal="false"
              :destroy-on-close="true"
            >
              <el-row :gutter="50">
                <el-col :span="24">
                  <HgSalesCustomer @selectiondata="selectCustomer"></HgSalesCustomer>
                </el-col>
              </el-row>
            </el-dialog>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from 'vue'
import { IAxios } from 'axios'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { THgFileUploadParam, TUpFile } from '@/types/components/hgfileupload'
import { computTableHeight, required, tableHeaderColor, formatDate } from '@/utils'
import useUserStore from '@/store/modules/user'
import { FormInstance, FormRules, dayjs } from 'element-plus'
import { TAdCustomer } from '@/types/views/customer'
import { IcustomerserviceDTO, Tcustomerservicequery } from '@/types/views/customer/customerservice'
import {
  deleteCustomerServiceByIdApi,
  getCustomerServicePageListApi,
  saveCustomerServiceApi,
  updateCustomerServiceApi
} from '@/api/common/customerservice'
import { TSelectZtree } from '@/types/common'
import { IDataCombo } from '@/types/common/DataCombo'
import { getEnumCombo } from '@/api/common'
import { ECustomerService } from '@/types/common/enumindex'
import { EHgDeptZtreeUrl } from '@/types/common/enumindex'

const user = useUserStore().user
const customerServiceFormRef = ref<FormInstance>()
const total = ref(0)
/** 表格高度 */
const tableheight = ref(0)
/** 选择客户显示状态 */
const dialogCustomerVisible = ref(false)
/** 编辑窗口显示状态 */
const dialogEditVisible = ref(false)
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
/** 组件传值-------------------- */
type Props = {
  customerid: string
}
const props = defineProps<Props>()
/** 查询条件 */
const queryParams = reactive<Tcustomerservicequery>({
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  queryKey: '',
  iservicetype: '',
  customerid: props.customerid
})
const customerServiceInfo: IcustomerserviceDTO = {
  id: '',
  iservicetype: 0,
  stitle: '',
  employid: '',
  employname: '',
  dstartdate: dayjs().format('YYYY-MM-DD'),
  denddate: dayjs().format('YYYY-MM-DD'),
  scontacts: '',
  sphone: '',
  scontent: '',
  sresult: '',
  sstatus: '',
  customerid: '',
  customername: ''
}
/** 编辑实体 */
const customerServiceForm = ref<IcustomerserviceDTO>({ ...customerServiceInfo })

/**
 * 校验设置参数
 */
const rules = reactive<FormRules<IcustomerserviceDTO>>({
  stitle: [{ validator: required, required: true, message: '请填写标题', trigger: 'blur' }],
  customername: [{ validator: required, required: true, message: '请填写客户', trigger: 'blur' }],
  iservicetype: [
    { validator: required, required: true, message: '请选择服务类型', trigger: 'change' }
  ],
  scontent: [{ validator: required, required: true, message: '请填写内容', trigger: 'blur' }],
  dstartdate: [{ required: true, message: '开始日期不能为空', trigger: 'change' }],
  denddate: [{ required: true, message: '结束日期不能为空', trigger: 'change' }]
})
/** Form选中的行 */
const multipleSelection = ref<IcustomerserviceDTO[]>([])

/** 列表对象 */
const customerServiceList = ref<IcustomerserviceDTO[]>([])
/** 客户服务类型下拉列表 */
const customerServiceTypeList: IDataCombo[] = getEnumCombo(ECustomerService)
onMounted(() => {
  handleQuery()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()

  /** 传值赋值 */
  queryParams.customerid = props.customerid
  console.log(props.customerid)
})
/** 监听传入数值变化 */
watch(
  () => props.customerid,
  (newValue, oldValue) => {
    nextTick(() => {
      queryParams.customerid = newValue
      console.log(newValue)
    })
  }
)
/**
 * 查询
 */
const handleQuery = () => {
  getCustomerServicePageListApi(queryParams)
    .then((res: IAxios<IcustomerserviceDTO[]>) => {
      if (res.success) {
        customerServiceList.value = res.obj
        total.value = res.total
      }
    })
    .finally(() => {})
}
/**
 * 新增
 */
const handleAdd = () => {
  resetForm(customerServiceFormRef.value)
  customerServiceForm.value = { ...customerServiceInfo }
  dialogEditVisible.value = true
}
/**
 * 编辑按钮事件
 * @param row
 */
const onEdit = (row: IcustomerserviceDTO) => {
  dialogEditVisible.value = true
  customerServiceForm.value = { ...row }
}
/**
 * 删除按钮事件
 * @param row
 */
const onDelete = (row: IcustomerserviceDTO | undefined) => {
  const ids: string[] = []
  if (row === undefined) {
    multipleSelection.value.forEach((item) => {
      ids.push(item.id?.toString() as string)
    })
  } else {
    ids.push(row.id?.toString() as string)
  }

  if (ids.length === 0) {
    ElMessage.warning('请选择要删除的数据')
    return
  }
  const data = {
    ids: ids.join(',')
  }
  ElMessageBox.confirm('是否删除选择的数据？', '提示', {
    type: 'warning'
  })
    .then(() => {
      deleteCustomerServiceByIdApi(data)
        .then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('删除成功!')
            handleQuery()
          }
        })
        .catch(() => {})
    })
    .catch(() => {})
}
/**
 * 编辑的时候选择业务人员
 * @param val
 */
const onSelectionNewEmp = (val: TSelectZtree[]) => {
  const employname = val.map((item) => item.name).join(',')
  const employid = val.map((item) => item.id).join(',')
  customerServiceForm.value.employid = employid
  customerServiceForm.value.employname = employname
}
/**
 * 编辑保存
 * @param formEl
 */
const saveCustomerServiceForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      if (customerServiceForm.value.iservicetype === ECustomerService.客户投诉) {
        if (customerServiceForm.value.customerid === '') {
          ElMessage.warning('请选择客户信息')
          return
        }
      }
      if (customerServiceForm.value.customername === '') {
        ElMessage.warning('请填写客户信息')
        return
      }
      if (customerServiceForm.value.id === '') {
        saveCustomerServiceApi(customerServiceForm.value)
          .then((res: IAxios) => {
            if (res.success) {
              ElMessage.success('保存成功')
              handleQuery()
              customerServiceFormRef.value?.resetFields()
              dialogEditVisible.value = false
            }
          })
          .catch(() => {})
      } else {
        updateCustomerServiceApi(customerServiceForm.value)
          .then((res: IAxios) => {
            if (res.success) {
              ElMessage.success('更新保存成功')
              handleQuery()
              customerServiceFormRef.value?.resetFields()
              dialogEditVisible.value = false
            }
          })
          .catch(() => {})
      }
    } else {
      console.log('error submit!', fields)
    }
  })
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
 * 选择客户信息后返回接收
 * @param val
 */
const selectCustomer = (val: TAdCustomer) => {
  customerServiceForm.value.customerid = val.id
  customerServiceForm.value.customername = val.sname
  dialogCustomerVisible.value = false
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
/** 修改时删除客户列表信息 */
const SeeCustomerdelete = () => {
  customerServiceForm.value.customername = ''
  customerServiceForm.value.customerid = ''
}
</script>

<style lang="scss" scoped>
.inputwindht {
  width: 540px;
}
.el-form-item {
  margin-bottom: 15px;
}
</style>
