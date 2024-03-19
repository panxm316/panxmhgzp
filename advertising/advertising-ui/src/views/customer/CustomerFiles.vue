<!--
 * @Author: wanghl
 * @Date: 2023-10-26 10:11:02
 * @LastEditTime: 2023-11-02 12:55:42
 * @LastEditors: wanghl
 * @Description:客户文件表
-->
<template>
  <div class="app-container">
    <div style="width: 100%; margin: -10px 0 10px 5px">
      <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
    </div>
    <div class="table_box">
      <el-row>
        <el-table
          :data="WcustomerFilesList"
          row-key="id"
          :height="tableheight"
          :border="true"
          stripe
          :header-cell-style="tableHeaderColor"
        >
          <el-table-column type="selection" prop="ischeck" :width="50" align="center">
          </el-table-column>
          <el-table-column prop="sname" label="名称" sortable="custom" min-width="150">
          </el-table-column>
          <el-table-column prop="bdelete" label="是否删除" width="100" align="center">
            <template #default="scope">
              <span v-if="scope.row.bdelete == 0">
                <el-tag type="success">正常客户</el-tag>
              </span>
              <span v-if="scope.row.bdelete == 1">
                <el-tag type="danger">已删除</el-tag>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="dcreatedate" label="创建时间" width="160">
            <template #default="scope">
              <span>{{ formatDate(scope.row.dcreatedate) }}</span>
            </template>
          </el-table-column>

          <el-table-column prop="isort" label="序号" width="80" sortable="custom" align="center">
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="250"
            class-name="small-padding fixed-width"
            fixed="right"
          >
            <template #default="adproject">
              <el-button link type="success" icon="Edit" @click="handleUpdate(adproject.row)"
                >修改</el-button
              >
              <el-button link type="danger" icon="Delete" @click="handleDelete(adproject.row)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </el-row>
    </div>
    <el-dialog
      v-model="dialogVisible"
      title="客户编辑"
      width="1000"
      align-center
      :close-on-click-modal="false"
      :destroy-on-close="true"
      @close="handleCancel"
    >
      <el-button type="primary" icon="Plus" @click="importBankAccount">导入</el-button>
      <el-form ref="CustomerFormRef" :model="WcustomerFiles" label-width="150px" :rules="rules">
        <el-row :gutter="20">
          <el-col :span="22">
            <el-form-item label="文件描述" prop="sdescription">
              <el-input v-model="WcustomerFiles.sdescription" />
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
    <el-dialog v-model="dialogImportVisible" title="客户附件导入" width="680px" append-to-body>
      <div>
        <el-row>
          <el-col :span="24">
            <HgFileUpload
              :server="hgfileuploadparam.server"
              :accept="hgfileuploadparam.accept"
              :multiple="false"
              :filenumlimit="1"
              :storytypes="hgfileuploadparam.storytypes"
              @getupfile="getUpFile"
            ></HgFileUpload>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { FormRules, FormInstance } from 'element-plus'
import { computed, ref, watch } from 'vue'
import type { Twcustomerfiles } from '@/types/views/customer'
import { THgFileUploadParam, TUpFile } from '@/types/components/hgfileupload'
import {
  getCustomerFilesListApi,
  getCustomerFilesByIdApi,
  saveCustomerFilesApi,
  updateCustomerFilesApi,
  deleteCustomerFilesByIdApi,
  deleteCustomerFilesByCustomerIdApi,
  recoveryCustomerFilesByIdApi,
  recoveryCustomerFilesByCustomerIdApi
} from '@/api/customer/index'
import {
  required,
  isNumberStr,
  formatDate,
  tableHeaderColor,
  validatePhone,
  computTableHeight
} from '@/utils/index'
import type { IAxios } from 'axios'
import modal from '@/plugins/modal'
import useUserStore from '@/store/modules/user'
const userStore = useUserStore()
defineOptions({
  name: 'CustomerFiles'
})
type Props = {
  customerid: string
}
const props = defineProps<Props>()
/** 客户id */
const customerid = ref(props.customerid)
/** 表格高度 */
const tableheight = ref(0)
/** 客户文件列表 */
const WcustomerFilesList = ref<Twcustomerfiles[]>([])
/** 客户文件表 */
const WcustomerFiles = ref<Twcustomerfiles>({
  id: '',
  customerid: props.customerid,
  createempid: userStore.user?.userid.toString(),
  sfileformat: '',
  sfileid: '',
  sfilesize: '',
  soriginalfile: '',
  dcreatetime: '',
  sfiletype: '',
  sfilecategory: '',
  bdelete: true,
  sdescription: ''
})
/** 编辑 dialog */
const dialogVisible = ref(false)
/** 导入窗口显示状态 */
const dialogImportVisible = ref(false)
const CustomerFormRef = ref<FormInstance>()
const AdProjectSelection = ref<Twcustomerfiles[]>()
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<Twcustomerfiles>>({
  sdescription: [
    { validator: required, required: true, message: '请输入名称', trigger: 'change' },
    { max: 48, message: '不得大于48个字符', trigger: 'change' }
  ]
})
/** 上传组件参数 */
const hgfileuploadparam = ref<THgFileUploadParam>({
  server: import.meta.env.VITE_APP_BASE_API + '/twbankaccounts/importBankAccount',
  accept: {
    title: 'word',
    extensions: 'docx,doc',
    mimeTypes: '.docx,.doc'
  },
  multiple: false,
  filenumlimit: 1,
  storytypes: []
})
/** 导入按钮事件 */
const importBankAccount = () => {
  dialogImportVisible.value = true
}
/**
 * 获取上传后返回的文件列表
 * @param res
 */
const getUpFile = (res: IAxios<TUpFile>) => {
  console.log(res.success)
  if (res.success) {
    ElMessage.success('导入成功')
    loaddata()
  }
}
/** 监听传入数值变化 */
watch(
  () => props.customerid,
  (newValue, oldValue) => {
    nextTick(() => {
      customerid.value = newValue
      console.log(customerid.value)
    })
  }
)
onMounted(() => {
  loaddata()
  /**
   * 计算表格高度
   */
  tableheight.value = 400
  console.log(customerid.value)
})
/**
 * 获取客户文件列表
 */
const loaddata = () => {
  getCustomerFilesListApi(customerid.value).then(({ obj }: IAxios<Twcustomerfiles[]>) => {
    console.log(obj)
    WcustomerFilesList.value = obj
  })
}
/**
 * 新增
 */
const handleAdd = () => {
  loaddata()
  WcustomerFiles.value = { ...WcustomerFiles.value }
  dialogVisible.value = true
}
/**
 * 修改
 * @param row
 */
const handleUpdate = (row: Twcustomerfiles) => {
  WcustomerFiles.value = { ...row }
  dialogVisible.value = true
}
/**
 * 保存
 */
const handleSave = () => {
  CustomerFormRef.value?.validate((valid) => {
    if (valid) {
      if (!WcustomerFiles.value.id) {
        saveCustomerFilesApi(WcustomerFiles.value).then(({ success, msg }: IAxios) => {
          if (success) {
            modal.msgSuccess('操作成功')
          } else {
            modal.msgError(msg)
          }
          loaddata()
        })
      } else {
        updateCustomerFilesApi(WcustomerFiles.value).then(({ success, msg }: IAxios) => {
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
 * 取消
 */
const handleCancel = () => {
  CustomerFormRef.value?.resetFields()
  dialogVisible.value = false
  setTimeout(() => {
    CustomerFormRef.value?.clearValidate(['sname'])
  }, 10)
}
/**
 * 删除
 * @param row
 */
const handleDelete = (row?: Twcustomerfiles) => {
  modal.confirm('是否删除?').then(() => {
    const ids = []
    if (row) {
      ids.push(row.id)
    } else {
      AdProjectSelection.value?.forEach((color) => ids.push(color.id))
    }
    if (!ids.length) {
      modal.msgWarning('请选择删除记录')
      return
    }
    deleteCustomerFilesByIdApi(ids.join(',')).then(({ success, msg }: IAxios) => {
      if (success) {
        modal.msgSuccess('操作成功')
      } else {
        modal.msgError(msg)
      }
      loaddata()
    })
  })
}
</script>

<style scoped>
ul {
  padding-left: 10px;
}
</style>
