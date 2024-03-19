<!--
 * @Author: suny
 * @Date: 2023-10-24 10:53:29
 * @LastEditTime: 2024-02-19 10:45:54
 * @LastEditors: suny suny@hgzp.com.cn
 * @Description: 假期管理
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
        <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
        <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="dataTableRef"
        :data="HolidayList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        border
      >
        <el-table-column label="休假人员" prop="employname" />
        <el-table-column label="接手人员" prop="newemployname" />
        <el-table-column label="开始时间" prop="dstartdate" width="200">
          <template #default="scope">
            {{ dayjs(scope.row.dstartdate).format('YYYY-MM-DD') }}
          </template>
        </el-table-column>

        <el-table-column label="结束时间" prop="denddate" width="200">
          <template #default="scope">
            {{ dayjs(scope.row.denddate).format('YYYY-MM-DD') }}
          </template>
        </el-table-column>
        <el-table-column label="操作人员" prop="operatorname" />
        <el-table-column label="申请时间" prop="dcreatedate" width="200">
          <template #default="scope">
            {{ dayjs(scope.row.dcreatedate).format('YYYY-MM-DD') }}
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="buse" width="150">
          <template #default="scope">
            <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="250">
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
              type="danger"
              icon="Delete"
              title="删除"
              @click="onDelete(scope.row)"
            ></el-button>
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
    <el-dialog
      v-model="dialogEditVisible"
      title="休假编辑"
      width="700px"
      align-center
      :close-on-click-modal="false"
      :destroy-on-close="true"
    >
      <el-form ref="holidayFormRef" :model="holidayForm" label-width="150px" :rules="rules">
        <el-row :gutter="50">
          <el-col :span="22">
            <el-form-item label="休假人员" prop="employid">
              <HgZtreeSelect
                :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOY"
                :selectids="holidayForm.employid ? [holidayForm.employid] : []"
                @selectionztree="onSelectionEmp"
              ></HgZtreeSelect>
            </el-form-item>
            <el-form-item label="接手人员" prop="newemployid">
              <HgZtreeSelect
                :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOY"
                :selectids="holidayForm.newemployid ? [holidayForm.newemployid] : []"
                @selectionztree="onSelectionNewEmp"
              ></HgZtreeSelect>
            </el-form-item>
            <el-form-item label="日期范围" required>
              <el-row>
                <el-col :span="11">
                  <el-form-item prop="dstartdate">
                    <el-date-picker
                      v-model="holidayForm.dstartdate"
                      type="date"
                      placeholder="开始日期"
                      :clearable="false"
                      value-format="YYYY-MM-DD"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
                <el-col class="text-center" :span="2">至</el-col>
                <el-col :span="11">
                  <el-form-item prop="denddate">
                    <el-date-picker
                      v-model="holidayForm.denddate"
                      type="date"
                      placeholder="结束日期"
                      :clearable="false"
                      value-format="YYYY-MM-DD"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item label="备注" prop="sremark">
              <el-input v-model="holidayForm.sremark" type="textarea" :maxlength="200" />
            </el-form-item>
            <el-form-item label="启用" prop="buse">
              <el-checkbox v-model="holidayForm.buse" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" icon="Check" @click="saveHoliday(holidayFormRef)"
            >保存</el-button
          >
          <el-button icon="Close" @click="dialogEditVisible = false">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import {
  deleteHolidayByIdApi,
  getEmpHolidayPageListApi,
  saveEmpHolidayApi,
  updateEmpHolidayApi
} from '@/api/task/empholiday'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { ITwempholiday } from '@/types/views/task/empholiday'
import { PageDto } from '@/types/views/task/start'
import { computTableHeight, required, tableHeaderColor } from '@/utils'
import { IAxios } from 'axios'
import { FormInstance, FormRules, dayjs } from 'element-plus'
import { EHgDeptZtreeUrl } from '@/types/common/enumindex'
import { TSelectZtree } from '@/types/common'

/** 假期ref */
const holidayFormRef = ref<FormInstance>()
const total = ref(0)
/** 表格高度 */
const tableheight = ref(0)
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
/** 查询条件 */
const queryParams = reactive<PageDto>({
  pageNum: 1,
  pageSize: 20,
  startTime: '',
  endTime: ''
})
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<ITwempholiday>>({
  employid: [{ validator: required, required: true, message: '请选择休假人员', trigger: 'change' }],
  newemployid: [
    { validator: required, required: true, message: '请选择接手人员', trigger: 'change' }
  ],
  dstartdate: [{ required: true, message: '开始日期不能为空', trigger: 'change' }],
  denddate: [{ required: true, message: '结束日期不能为空', trigger: 'change' }]
})
const holidayinfo: ITwempholiday = {
  id: '',
  employid: '',
  employname: '',
  sremark: '',
  buse: true,
  dstartdate: dayjs().format('YYYY-MM-DD'),
  denddate: dayjs().format('YYYY-MM-DD'),
  newemployid: '',
  newemployname: ''
}
/** 休假data */
const holidayForm = ref<ITwempholiday>({ ...holidayinfo })
/** 人员假期列表 */
const HolidayList = ref<ITwempholiday[]>([])
onMounted(() => {
  handleQuery()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})
/**
 * 查询
 */
const handleQuery = () => {
  getEmpHolidayPageListApi(queryParams)
    .then((res: IAxios) => {
      if (res.success) {
        HolidayList.value = res.obj
        total.value = res.total
      }
    })
    .finally(() => {})
}
/**
 * 重置form表单验证
 * @param formEl
 */
const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  holidayFormRef.value?.clearValidate([
    'employid',
    'employname',
    'newemployid',
    'newemployname',
    'dstartdate',
    'denddate'
  ])
  formEl.resetFields()
}
/**
 * 新增
 */
const handleAdd = () => {
  resetForm(holidayFormRef.value)
  holidayForm.value = { ...holidayinfo }
  dialogEditVisible.value = true
}
/**
 * 编辑
 * @param row
 */
const onEdit = (row: ITwempholiday) => {
  holidayForm.value = { ...row }
  dialogEditVisible.value = true
}
/**
 * 删除休假
 * @param row
 */
const onDelete = (row: ITwempholiday) => {
  if (row.id === undefined) {
    ElMessage.warning('请选择要删除的数据')
    return
  }
  const data = {
    ids: row.id?.toString()
  }
  ElMessageBox.confirm('是否删除选择的休假信息？', '提示', {
    type: 'warning'
  })
    .then(() => {
      deleteHolidayByIdApi(data)
        .then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('删除成功')
            handleQuery()
          }
        })
        .catch(() => {})
    })
    .catch(() => {})
}
/**
 * 保存
 */
const saveHoliday = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  if (dayjs(holidayForm.value.dstartdate).isAfter(holidayForm.value.denddate)) {
    ElMessage.warning('开始日期不能大于结束日期')
    return
  }
  if (holidayForm.value.employid === holidayForm.value.newemployid) {
    ElMessage.error('休假人员与接手人员相同')
    return
  }
  await formEl.validate((valid) => {
    if (valid) {
      if (!holidayForm.value.id) {
        saveEmpHolidayApi(holidayForm.value).then(({ success, msg }: IAxios) => {
          if (success) {
            ElMessage.success('操作成功')
            dialogEditVisible.value = false
            handleQuery()
          }
        })
      } else {
        updateEmpHolidayApi(holidayForm.value).then(({ success, msg }: IAxios) => {
          if (success) {
            ElMessage.success('操作成功')
            dialogEditVisible.value = false
            handleQuery()
          }
        })
      }
    }
  })
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
 * 选择请假人员
 * @param val
 */
const onSelectionEmp = (val: TSelectZtree[]) => {
  const employid = val.map((item) => item.id).join(',')
  if (employid !== '' && employid === holidayForm.value.newemployid) {
    ElMessage.error('休假人员与接手人员相同')
    return
  }
  holidayForm.value.employid = employid
  holidayForm.value.employname = val.map((item) => item.name).join(',')
}
/**
 * 选择接手人员
 * @param val
 */
const onSelectionNewEmp = (val: TSelectZtree[]) => {
  const newemployid = val.map((item) => item.id).join(',')
  if (newemployid !== '' && newemployid === holidayForm.value.employid) {
    ElMessage.error('休假人员与接手人员相同')
    return
  }
  holidayForm.value.newemployid = newemployid
  holidayForm.value.newemployname = val.map((item) => item.name).join(',')
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
</script>

<style lang="scss" scoped></style>
