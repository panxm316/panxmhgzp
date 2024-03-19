<!--
 * @Author: songly
 * @Date: 2023-11-22 15:41:47
 * @LastEditTime: 2023-11-25 11:17:33
 * @LastEditors: wanghl
 * @Description: 客户信誉度
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex" style="width: 94%">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
        <el-button type="danger" icon="Delete" @click="handleDelete()">删除</el-button>
        <el-tooltip class="item" effect="dark" content="帮助" placement="top">
          <el-button class="help" type="text" @click="showhelp()">?</el-button>
        </el-tooltip>
      </div>
    </div>
    <div class="table_box">
      <el-row>
        <el-table
          :data="CustomerCreditList"
          row-key="id"
          :height="tableheight"
          :border="true"
          stripe
          :header-cell-style="tableHeaderColor"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" prop="ischeck" :width="50" align="center">
          </el-table-column>
          <el-table-column prop="creditname" label="名称" min-width="150px" show-overflow-tooltip>
          </el-table-column>

          <el-table-column prop="createempname" label="创建人" width="160" show-overflow-tooltip>
          </el-table-column>
          <el-table-column prop="createtime" label="创建时间" width="160">
            <template #default="scope">
              <span>{{ formatDate(scope.row.createtime) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="buse" label="是否有效" width="90" align="center">
            <template #default="scope">
              <span v-if="scope.row.buse == false">
                <el-tag type="danger">无效</el-tag>
              </span>
              <span v-if="scope.row.buse == true">
                <el-tag type="success">有效</el-tag>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="isort" label="序号" width="100" align="center"> </el-table-column>
          <el-table-column prop="sremark" label="备注" width="200" show-overflow-tooltip>
          </el-table-column>
          <el-table-column
            label="操作"
            width="200"
            class-name="small-padding fixed-width"
            fixed="right"
          >
            <template #default="scope">
              <div style="padding-left: 10px">
                <el-button
                  link
                  type="success"
                  icon="Edit"
                  size="small"
                  @click="handleUpdate(scope.row)"
                  >修改</el-button
                >
                <el-button
                  link
                  type="danger"
                  icon="Delete"
                  size="small"
                  @click="handleDelete(scope.row)"
                  >删除</el-button
                >
              </div>
            </template>
          </el-table-column>
        </el-table>
      </el-row>
    </div>
    <!-- 便捷 -->
    <el-dialog
      v-model="dialogVisible"
      title="客户信誉度编辑"
      align-center
      :close-on-click-modal="false"
      width="700"
      @close="handleCancel"
    >
      <el-form
        ref="CustomerCreditFormRef"
        :model="CustomerCreditData"
        label-width="130px"
        :rules="rules"
      >
        <el-row :gutter="20">
          <el-col :span="21">
            <el-form-item label="名称" prop="creditname">
              <el-input
                v-model="CustomerCreditData.creditname"
                placeholder="请输入客户信誉度名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="序号" prop="isort">
              <el-input-number
                v-model="CustomerCreditData.isort"
                style="width: 160px"
                :min="1"
                controls-position="right"
              />
            </el-form-item>
            <el-form-item prop="sremark" label="备注">
              <el-input v-model="CustomerCreditData.sremark" :maxlength="200" />
            </el-form-item>
            <el-form-item label="有效" prop="buse">
              <el-checkbox v-model="CustomerCreditData.buse"></el-checkbox>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" icon="Check" @click="handleSave">保存</el-button>
          <el-button icon="Close" @click="handleCancel">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { FormRules, FormInstance, menuItemEmits } from 'element-plus'
import {
  required,
  computTableHeight,
  isNumberStr,
  formatDate,
  tableHeaderColor
} from '@/utils/index'
/**
 * 引入接口
 */
import {
  getcustomerCreditListApi,
  savecustomerCreditApi,
  updatecustomerCreditApi,
  deletecustomerCreditApi,
  getMaxSortApi
} from '@/api/customer/customercredit'
defineOptions({
  name: 'CustomerCredit'
})
import { useRouter } from 'vue-router'
import type { TAdCustomerCredit } from '@/types/views/customer/customergroup'
import type { IAxios } from 'axios'
import modal from '@/plugins/modal'
const CustomerCreditFormRef = ref<FormInstance>()
const CustomerCreditSelection = ref<TAdCustomerCredit[]>()

const router = useRouter()
const global = getCurrentInstance()?.appContext.config.globalProperties
/** 表格高度 */
const tableheight = ref(0)
const dialogVisible = ref(false)

const CustomerCreditList = ref<TAdCustomerCredit[]>([])

/** 折扣总表数据 */
const CustomerCreditData = ref<TAdCustomerCredit>({
  id: '',
  creditname: '',
  isort: 0,
  buse: true,
  sremark: '',
  createempid: '',
  createempname: '',
  createtime: ''
})
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<TAdCustomerCredit>>({
  creditname: [
    { required: true, message: '请输入名称', trigger: 'change' },
    { max: 48, message: '不得大于48个字符', trigger: 'change' }
  ]
})

onMounted(() => {
  loaddata()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight() - 42
})
/**
 * 获取折扣总表列表
 */
const loaddata = () => {
  getcustomerCreditListApi().then(({ obj, total }: IAxios<TAdCustomerCredit[]>) => {
    CustomerCreditList.value = obj
  })
}
/**
 * 选择
 * @param rows
 */
const handleSelectionChange = (rows: TAdCustomerCredit[]) => {
  CustomerCreditSelection.value = rows
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'Discount'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
// /**
//  * 排序
//  * @param val
//  */
// const handleSortChange = (val: { prop: string; order: string }) => {
//   queryInfo.sort = val.prop
//   if (val.order === 'ascending') {
//     queryInfo.order = 'asc'
//   } else if (val.order === 'descending') {
//     queryInfo.order = 'desc'
//   } else {
//     queryInfo.order = ''
//   }
//   loaddata()
// }

/**
 * 新增
 */
const handleAdd = () => {
  dialogVisible.value = true
  getMaxSort()
}
/**
 * 修改
 * @param row
 */
const handleUpdate = (row: TAdCustomerCredit) => {
  CustomerCreditData.value = { ...row }
  dialogVisible.value = true
}
/**
 * 保存
 */
const handleSave = () => {
  CustomerCreditFormRef.value?.validate((valid) => {
    if (valid) {
      if (!CustomerCreditData.value.id) {
        savecustomerCreditApi(CustomerCreditData.value).then(({ success, msg }: IAxios) => {
          if (success) {
            modal.msgSuccess('操作成功')
          } else {
            modal.msgError(msg)
          }
          loaddata()
        })
      } else {
        updatecustomerCreditApi(CustomerCreditData.value).then(({ success, msg }: IAxios) => {
          if (success) {
            modal.msgSuccess('操作成功')
          } else {
            modal.msgError(msg)
          }
          loaddata()
        })
      }
      dialogVisible.value = false
    }
  })
}
/**
 * 删除
 * @param row
 */
const handleDelete = (row?: TAdCustomerCredit) => {
  modal.confirm('是否删除?').then(() => {
    const ids = []
    if (row) {
      ids.push(row.id)
    } else {
      CustomerCreditSelection.value?.forEach((item) => ids.push(item.id))
    }
    if (!ids.length) {
      modal.msgWarning('请选择删除记录')
      return
    }
    deletecustomerCreditApi(ids.join(',')).then(({ success, msg }: IAxios) => {
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
  CustomerCreditFormRef.value?.resetFields()
  reset()
  loaddata()
  dialogVisible.value = false
}
/**
 * 获取最大序号
 */
const getMaxSort = () => {
  getMaxSortApi().then(({ obj }: IAxios) => {
    CustomerCreditData.value.isort = obj
  })
}
/** 重置*/
const reset = () => {
  CustomerCreditData.value = {
    id: '',
    creditname: '',
    isort: 0,
    buse: true,
    sremark: '',
    createempid: '',
    createempname: '',
    createtime: ''
  }
}
</script>
<style scoped></style>
