<!--
 * @Author: suny
 * @Date: 2023-11-30 12:49:54
 * @LastEditTime: 2024-01-17 15:09:42
 * @LastEditors: wanghl
 * @Description: 折扣降点
-->
<template>
  <div>
    <div class="search_box">
      <div class="flex">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="dataTableRef"
        :data="discountReduceList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        :border="true"
      >
        <el-table-column
          label="组名称"
          prop="commissionrategroupname"
          width="160"
          show-overflow-tooltip
        />
        <el-table-column label="开始折扣" prop="ndiscountstart" min-width="100" align="center">
          <template #default="scope"> {{ scope.row.ndiscountstart }}% </template>
        </el-table-column>
        <el-table-column label="结束折扣" prop="ndiscountend" min-width="100" align="center">
          <template #default="scope"> {{ scope.row.ndiscountend }}% </template>
        </el-table-column>
        <el-table-column label="直接客户" prop="nrateofcustomer" min-width="100" align="center">
          <template #default="scope"> {{ scope.row.nrateofcustomer }}% </template>
        </el-table-column>
        <el-table-column
          label="代理公司"
          prop="nrateofagency"
          width="100"
          min-width="100"
          align="center"
        >
          <template #default="scope"> {{ scope.row.nrateofagency }}% </template>
        </el-table-column>
        <el-table-column label="内容生产方" prop="nrateofagent" min-width="100" align="center">
          <template #default="scope"> {{ scope.row.nrateofagent }}% </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="160"
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
    <!-- 编辑 -->
    <el-dialog
      v-model="dialogEditVisible"
      title="折扣降点设置"
      style="width: 800px"
      append-to-body
      :destroy-on-close="true"
    >
      <div>
        <el-row>
          <el-col>
            <el-form
              ref="discountReduceRef"
              :model="discountReduceForm"
              :rules="rules"
              label-width="140px"
              class="demo-workReportForm"
              status-icon
            >
              <el-form-item label="起始折扣" prop="ndiscountstart" required>
                <el-input-number
                  v-model="discountReduceForm.ndiscountstart"
                  :controls="false"
                  style="width: 200px"
                  :min="0"
                  :max="100"
                />%
              </el-form-item>
              <el-form-item label="结束折扣" prop="ndiscountend" required>
                <el-input-number
                  v-model="discountReduceForm.ndiscountend"
                  :controls="false"
                  style="width: 200px"
                  :min="0"
                  :max="100"
                />%
              </el-form-item>
              <el-row>
                <el-col>下降点数</el-col>
              </el-row>
              <el-form-item label="直接客户" prop="nrateofcustomer">
                <el-input-number
                  v-model="discountReduceForm.nrateofcustomer"
                  :controls="false"
                  style="width: 200px"
                  :min="0"
                  :max="100"
                />%
              </el-form-item>
              <el-form-item label="代理公司" prop="nrateofagency">
                <el-input-number
                  v-model="discountReduceForm.nrateofagency"
                  :controls="false"
                  style="width: 200px"
                  :min="0"
                  :max="100"
                />%
              </el-form-item>
              <el-form-item label="内容生产方" prop="nrateofagent">
                <el-input-number
                  v-model="discountReduceForm.nrateofagent"
                  :controls="false"
                  style="width: 200px"
                  :min="0"
                  :max="100"
                />%
              </el-form-item>
              <el-form-item>
                <div style="position: absolute; right: 5px">
                  <el-button
                    type="primary"
                    icon="Check"
                    @click="saveDiscountReduceForm(discountReduceRef)"
                    >保存</el-button
                  >
                  <el-button icon="Close" @click="dialogEditVisible = false">取消</el-button>
                </div>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { computTableHeight, required, tableHeaderColor } from '@/utils'
import { IAxios } from 'axios'
import { FormInstance, FormRules } from 'element-plus'
import { IDiscountReduceDTO, TDiscountReduceQuery } from '@/types/views/price/discountreduce'
import { ITbcommissionrategroup } from '@/types/views/price/commissionrategroup'
import {
  deleteDiscountReduceApi,
  getDiscountReducePageListApi,
  saveDiscountReduceApi,
  updateDiscountReduceApi
} from '@/api/price/discountreduce'

defineOptions({
  name: 'DiscountReduce'
})
interface Props {
  commissionrategroup: ITbcommissionrategroup
}
const props = defineProps<Props>()

const total = ref(0)
const sort = ref('ndiscountstart')
const order = ref('asc')
/** 编辑窗口显示状态 */
const dialogEditVisible = ref(false)
/** 设置窗口显示状态 */
const dialogSetVisible = ref(false)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([20, 40, 60, 100])
/** Form名称 */
const discountReduceRef = ref<FormInstance>()
/** 查询条件 */
const queryParams = reactive<TDiscountReduceQuery>({
  page: 1,
  pageSize: 20,
  commissionrategroupid: props.commissionrategroup.id,
  queryKey: ''
})
/** Form表单数据 */
const discountReduceInfo: IDiscountReduceDTO = {
  id: '',
  commissionrategroupname: props.commissionrategroup.commissionrategroupname,
  nrateofagent: 0,
  nrateofagency: 0,
  nrateofcustomer: 0,
  commissionrategroupid: props.commissionrategroup.id,
  ndiscountstart: 0,
  ndiscountend: 0
}
/** Form表单数据 */
const discountReduceForm = ref<IDiscountReduceDTO>({ ...discountReduceInfo })
/** 表格高度 */
const tableheight = ref(0)

/** 列表对象 */
const discountReduceList = ref<IDiscountReduceDTO[]>([])
/** Form选中的行 */
const multipleSelection = ref<IDiscountReduceDTO[]>([])
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<IDiscountReduceDTO>>({
  //   ndiscountstart: [
  //     { validator: required, required: true, message: '请设置起始折扣', trigger: 'change' }
  //   ],
  //   ndiscountend: [
  //     { validator: required, required: true, message: '请设置截止折扣', trigger: 'change' }
  //   ]
})

watch(
  () => props.commissionrategroup.id,
  (val) => {
    if (val) {
      queryParams.commissionrategroupid = val
      handleQuery()
    }
  }
)
onMounted(() => {
  handleQuery()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight() - 200
})
/**
 * 查询
 */
const handleQuery = () => {
  const data = {
    sort: sort.value,
    order: order.value,
    ...queryParams
  }
  getDiscountReducePageListApi(data)
    .then((res: IAxios<IDiscountReduceDTO[]>) => {
      if (res.success) {
        discountReduceList.value = res.obj
        total.value = res.total
      }
    })
    .finally(() => {})
}
/**
 * 新增
 */
const handleAdd = () => {
  resetForm(discountReduceRef.value)
  discountReduceForm.value = { ...discountReduceInfo }
  dialogEditVisible.value = true
}
/**
 * 编辑按钮事件
 * @param row
 */
const onEdit = (row: IDiscountReduceDTO) => {
  dialogEditVisible.value = true
  discountReduceForm.value = { ...row }
}
/**
 * 删除按钮事件
 * @param row
 */
const onDelete = (row: IDiscountReduceDTO | undefined) => {
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
      deleteDiscountReduceApi(data)
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
 * 编辑保存
 * @param formEl
 */
const saveDiscountReduceForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      if (discountReduceForm.value.id === '') {
        saveDiscountReduceApi(discountReduceForm.value)
          .then((res: IAxios) => {
            if (res.success) {
              ElMessage.success('保存成功')
              handleQuery()
              discountReduceRef.value?.resetFields()
              dialogEditVisible.value = false
            }
          })
          .catch(() => {})
      } else {
        updateDiscountReduceApi(discountReduceForm.value)
          .then((res: IAxios) => {
            if (res.success) {
              ElMessage.success('更新保存成功')
              handleQuery()
              discountReduceRef.value?.resetFields()
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
<style lang="scss" scoped></style>
