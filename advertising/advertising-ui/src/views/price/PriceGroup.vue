<!--
 * @Author: suny
 * @Date: 2023-11-09 15:40:56
 * @LastEditTime: 2024-01-17 14:53:25
 * @LastEditors: wanghl
 * @Description: 价格组管理
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
        <el-select
          v-model="queryParams.mediatypekey"
          placeholder="媒体类型"
          clearable
          style="width: 140px"
        >
          <el-option
            v-for="item in mediaTypeCombo"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
        <el-input
          v-model="queryParams.syear"
          clearable
          placeholder="请输入查询年份"
          class="input-with-select"
          style="width: 140px"
          @keyup.enter="handleQuery"
          @clear="handleQuery"
        >
        </el-input>
        <el-input
          v-model="queryParams.queryKey"
          clearable
          placeholder="请输入价格组关键字"
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
        :data="priceGroupList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        border
      >
        <el-table-column label="媒体类型" prop="mediatypename" width="120" />
        <el-table-column label="价格表名称" prop="sname" align="center" />
        <el-table-column label="年份" prop="syear" width="100" align="center" />
        <el-table-column label="备注" prop="sremark" align="center" />

        <el-table-column prop="buse" label="启用" :width="60" align="center">
          <template #default="scope">
            <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column prop="bdefault" label="默认" :width="60" align="center">
          <template #default="scope">
            <el-checkbox v-model="scope.row.bdefault" disabled></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="left"
          width="240"
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
              style="margin-left: 10px"
              type="primary"
              icon="Setting"
              size="small"
              title="设置"
              @click="onSet(scope.row)"
              >设置</el-button
            >
            <el-button
              link
              style="margin-left: 10px"
              type="primary"
              icon="DocumentCopy"
              size="small"
              title="复制"
              @click="onCopy(scope.row)"
              >复制</el-button
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
      title="价格组"
      style="width: 700px"
      append-to-body
      :destroy-on-close="true"
    >
      <div>
        <el-row>
          <el-col :span="22">
            <el-form
              ref="priceGroupRef"
              :model="priceGroupForm"
              :rules="rules"
              label-width="120px"
              class="demo-workReportForm"
              status-icon
            >
              <el-form-item label="类型" prop="mediatypekey">
                <el-select
                  v-model="priceGroupForm.mediatypekey"
                  :disabled="priceGroupForm.id !== ''"
                  placeholder="媒体类型"
                  style="width: 485px"
                >
                  <el-option
                    v-for="item in mediaTypeCombo"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="名称" prop="sname">
                <el-input v-model="priceGroupForm.sname" class="inputwindht" />
              </el-form-item>
              <el-form-item label="年份" prop="syear">
                <el-input v-model="priceGroupForm.syear" style="width: 485px" />
              </el-form-item>
              <el-form-item v-if="copyFlag" label="有效时间" required>
                <el-row :span="24">
                  <el-col :span="11">
                    <el-form-item prop="dstartdate">
                      <el-date-picker
                        v-model="startTime"
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
                        v-model="endTime"
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
              <el-form-item label="备注" prop="sremark">
                <el-input
                  v-model="priceGroupForm.sremark"
                  :rows="3"
                  type="textarea"
                  class="inputwindht"
                  resize="none"
                  placeholder="请输入..."
                />
              </el-form-item>
              <el-form-item label="序号" prop="isort">
                <el-input-number
                  v-model="priceGroupForm.isort"
                  :min="1"
                  :max="1000"
                  label="序号"
                ></el-input-number>
              </el-form-item>
              <el-row :gutter="50">
                <el-col :span="4">
                  <el-form-item label="启用" prop="buse">
                    <el-checkbox v-model="priceGroupForm.buse"></el-checkbox>
                  </el-form-item>
                </el-col>
                <el-col :span="4">
                  <el-form-item label="默认" prop="buse">
                    <el-checkbox v-model="priceGroupForm.bdefault"></el-checkbox>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item>
                <div style="position: absolute; right: 5px">
                  <el-button type="primary" icon="Check" @click="savePriceGroupForm(priceGroupRef)"
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
    <!-- 明细设置 -->
    <el-dialog
      v-model="dialogSetVisible"
      :title="priceGroupParams.pricegroupname + '明细设置'"
      style="width: 1160px"
      append-to-body
      :close-on-click-modal="false"
      :destroy-on-close="true"
    >
      <div>
        <PriceItem :params="priceGroupParams"></PriceItem>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { getMediaTypeComboApi } from '@/api/media/mediatype'
import {
  deletePriceGroupApi,
  getPriceGroupMaxSortApi,
  getPriceGroupPageListApi,
  savePriceGroupApi,
  updatePriceGroupApi
} from '@/api/price/pricegroup'
import { IDataCombo } from '@/types/common/DataCombo'
import { IPriceGroupDTO, TPriceGroupQuery } from '@/types/views/price/pricegroup'
import { computTableHeight, required, tableHeaderColor } from '@/utils'
import { IAxios } from 'axios'
import dayjs from 'dayjs'
import { FormInstance, FormRules } from 'element-plus'
import PriceItem from '@/views/price/PriceItem.vue'

const total = ref(0)
const sort = ref('syear,mediatypekey')
const order = ref('desc,asc')
/** 编辑窗口显示状态 */
const dialogEditVisible = ref(false)
/** 复制标识 */
const copyFlag = ref(false)
/** 设置窗口显示状态 */
const dialogSetVisible = ref(false)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([20, 40, 60, 100])
/** Form名称 */
const priceGroupRef = ref<FormInstance>()
/** 查询条件 */
const queryParams = reactive<TPriceGroupQuery>({
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  queryKey: '',
  mediatypekey: ''
})
/** Form表单数据 */
const priceGroupFormInfo: IPriceGroupDTO = {
  id: '',
  sname: '',
  mediatypekey: 'paper',
  mediatypename: '',
  buse: true,
  bdefault: true,
  syear: dayjs().format('YYYY'),
  isort: 1
}
/** Form表单数据 */
const priceGroupForm = ref<IPriceGroupDTO>({ ...priceGroupFormInfo })
/** 媒体类别下拉列表 */
const mediaTypeCombo = ref<IDataCombo[]>()
/** 表格高度 */
const tableheight = ref(0)

/** 列表对象 */
const priceGroupList = ref<IPriceGroupDTO[]>([])
/** Form选中的行 */
const multipleSelection = ref<IPriceGroupDTO[]>([])
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<IPriceGroupDTO>>({
  sname: [{ validator: required, required: true, message: '请填写名称', trigger: 'blur' }],
  mediatypekey: [
    { validator: required, required: true, message: '请选择媒体类型', trigger: 'change' }
  ],
  syear: [{ validator: required, required: true, message: '请设置年限', trigger: 'blur' }]
})
const priceGroupParams = ref({
  mediatypekey: '',
  mediatypename: '',
  pricegroupid: '',
  pricegroupname: ''
})
/** 价格明细开始时间 */
const startTime = ref('')
/** 价格明细结束时间 */
const endTime = ref('')
onMounted(() => {
  getMediaType()
  handleQuery()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})
/**
 * 获取媒体类型下拉列表
 */
const getMediaType = () => {
  getMediaTypeComboApi()
    .then((res: IAxios<IDataCombo[]>) => {
      if (res.success) {
        mediaTypeCombo.value = res.obj
      }
    })
    .catch(() => {})
}

/**
 * 查询
 */
const handleQuery = () => {
  const data = {
    sort: sort.value,
    order: order.value,
    ...queryParams
  }
  getPriceGroupPageListApi(data)
    .then((res: IAxios<IPriceGroupDTO[]>) => {
      if (res.success) {
        priceGroupList.value = res.obj
        total.value = res.total
      }
    })
    .finally(() => {})
}
/**
 * 获取最大序号
 */
const getMaxSort = () => {
  getPriceGroupMaxSortApi().then((res: IAxios) => {
    if (res.success) {
      priceGroupForm.value.isort = res.obj
    }
  })
}
/**
 * 新增
 */
const handleAdd = () => {
  resetForm(priceGroupRef.value)
  priceGroupForm.value = { ...priceGroupFormInfo }
  getMaxSort()
  dialogEditVisible.value = true
  copyFlag.value = false
}
/**
 * 价格表设置按钮事件
 * @param row
 */
const onSet = (row: IPriceGroupDTO) => {
  console.log(row)
  dialogSetVisible.value = true
  priceGroupParams.value = {
    pricegroupid: row.id ?? '',
    mediatypekey: row.mediatypekey ?? '',
    mediatypename: row.mediatypename ?? '',
    pricegroupname: row.sname ?? ''
  }
}
/**
 * 编辑按钮事件
 * @param row
 */
const onEdit = (row: IPriceGroupDTO) => {
  dialogEditVisible.value = true
  priceGroupForm.value = { ...row }
  copyFlag.value = false
}
/**
 * 复制按钮事件
 * @param row
 */
const onCopy = (row: IPriceGroupDTO) => {
  dialogEditVisible.value = true
  copyFlag.value = true
  priceGroupForm.value = { ...row }
  // priceGroupForm.value.id = ''
}
/**
 * 删除按钮事件
 * @param row
 */
const onDelete = (row: IPriceGroupDTO | undefined) => {
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
      deletePriceGroupApi(data)
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
const savePriceGroupForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      priceGroupForm.value.mediatypename = mediaTypeCombo.value?.find(
        (item) => item.id === priceGroupForm.value.mediatypekey
      )?.name as string
      if (priceGroupForm.value.bdefault && !priceGroupForm.value.buse) {
        ElMessage.warning('默认数据不能禁用')
        return
      }
      if (priceGroupForm.value.id === '' || copyFlag.value) {
        if (copyFlag.value && !startTime.value) {
          ElMessage.warning('请设置有效时间')
          return
        }
        const data = {
          ...priceGroupForm.value,
          copyFlag: copyFlag.value,
          startTime: startTime.value,
          endTime: endTime.value
        }
        savePriceGroupApi(data)
          .then((res: IAxios) => {
            if (res.success) {
              ElMessage.success('保存成功')
              handleQuery()
              priceGroupRef.value?.resetFields()
              dialogEditVisible.value = false
            }
          })
          .catch(() => {})
      } else {
        updatePriceGroupApi(priceGroupForm.value)
          .then((res: IAxios) => {
            if (res.success) {
              ElMessage.success('更新保存成功')
              handleQuery()
              priceGroupRef.value?.resetFields()
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

<style scoped></style>
