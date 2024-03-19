<!--
 * @Author: songly
 * @Date: 2024-02-16 12:57:55
 * @LastEditTime: 2024-02-16 17:00:15
 * @LastEditors: songly
 * @Description: 流程类型管理
-->

<template>
  <div id="FlowTypeform" class="app-container">
    <div class="search_box">
      <div class="flex">
        <!-- <el-button type="primary" icon="Plus" @click="onAdd">新增</el-button>
        <el-button type="danger" icon="Delete" @click="onDelete()">删除</el-button> -->
        <el-input
          v-model="queryKey"
          placeholder="请输工作流类型名称"
          clearable
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
          :data="flowtypeList"
          row-key="id"
          highlight-current-row
          :height="tableheight"
          :border="true"
          stripe
          :header-cell-style="tableHeaderColor"
          @sort-change="sortChange"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="50" align="center"></el-table-column>
          <el-table-column prop="sname" label="流程名称" min-width="100" sortable="custom">
          </el-table-column>
          <el-table-column prop="skey" label="流程关键字" min-width="100" sortable="custom">
          </el-table-column>
          <el-table-column
            prop="isort"
            label="序号"
            width="80"
            sortable="custom"
            align="center"
          ></el-table-column>
          <el-table-column prop="bactive" label="启用" width="80" align="center">
            <template #default="scope">
              <el-checkbox v-model="scope.row.bactive" disabled></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column prop="buse" label="有效" width="80" align="center">
            <template #default="scope">
              <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column prop="sdesc" label="描述" min-width="100"> </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="200"
            class-name="small-padding fixed-width"
          >
            <template #default="scope">
              <el-button link type="success" icon="Edit" @click="onEdit(scope.row)">修改</el-button>
              <!-- <el-button link type="danger" icon="Delete" @click="onDelete(scope.row)"
                >删除</el-button
              > -->
            </template>
          </el-table-column>
        </el-table>
      </el-row>
      <el-row>
        <el-pagination
          v-model:current-page="currentPage"
          class="pagination"
          style="margin-left: 10px; width: 100%"
          :page-sizes="pageSizes"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pageTotal"
          small
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        >
        </el-pagination>
      </el-row>
    </div>
    <!-- 添加或修改工作流类型对话框 -->
    <el-dialog v-model="dialogVisible" width="700px" align-center title="基础工作流编辑">
      <el-form ref="ruleFormRef" :rules="rules" :model="flowtypeData" label-width="100px">
        <el-row :gutter="50">
          <el-col :span="22">
            <el-form-item label="名称" prop="sname">
              <el-input v-model="flowtypeData.sname" />
            </el-form-item>
            <el-form-item label="序号" prop="isort">
              <el-input-number v-model="flowtypeData.isort" :min="1" controls-position="right" />
            </el-form-item>
            <el-form-item label="关键字" prop="skey">
              <el-input v-model="flowtypeData.skey" :maxlength="100" disabled />
            </el-form-item>
            <el-form-item label="描述">
              <el-input
                v-model="flowtypeData.sdesc"
                maxlength="300"
                style="width: 400px"
                placeholder="描述"
                show-word-limit
                type="textarea"
              />
            </el-form-item>
            <el-row :gutter="50">
              <el-col :span="4">
                <el-form-item label="启用" prop="bactive">
                  <el-checkbox v-model="flowtypeData.bactive"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item label="有效" prop="buse">
                  <el-checkbox v-model="flowtypeData.buse"></el-checkbox>
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" icon="Check" @click="onSubmitForm(ruleFormRef)">保存</el-button>
          <el-button icon="Close" @click="onResetForm(ruleFormRef)">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import modal from '@/plugins/modal'
import { required, computTableHeight, tableHeaderColor } from '@/utils/index'
import type { TQueryInfo } from '@/types/common/index'
import { IDataCombo } from '@/types/common/DataCombo'
import type { IAxios } from 'axios'
import {
  getFlowTypeListApi,
  getFlowTypePageListApi,
  deleteFlowTypeByIdApi,
  saveFlowTypeApi,
  updateFlowTypeApi,
  getFlowTypeMaxSortApi
} from '@/api/flow/flowmanager'
import { TFlowType } from '@/types/views/flow/flowmanager'
import { ElFormItem, FormInstance, FormRules } from 'element-plus'
import { useRouter } from 'vue-router'
const router = useRouter()
defineOptions({
  name: 'FlowManager'
})
const flowtypeData = ref<TFlowType>({
  id: undefined,
  sname: '',
  isort: 0,
  buse: true,
  skey: '',
  bactive: true,
  sdesc: ''
})
/**
 * 列表数据
 */
const flowtypeList = ref([]) // 菜单数据
/** * 列表高度 */
const tableheight = ref<number>(window.innerHeight - 205)
const refreshTable = ref(true)
/** 启用状态 */
const buse = ref('')
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 页数 */
const currentPage = ref<number>(1)
/** 每页条数 */
const pageSize = ref<number>(20)
/** 数据总条数 */
const pageTotal = ref<number>(0)
/** 排序字段 */
const sort = ref('isort')
/** 正序倒叙 */
const order = ref('')
/** 关键字 */
const queryKey = ref('')
/** 表单ref */
const ruleFormRef = ref<FormInstance>()
/** 校验设置参数 */
const rules = reactive<FormRules<TFlowType>>({
  sname: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  sdesc: [{ min: 1, max: 300, message: '备注长度在 1 到 300 个字符', trigger: 'blur' }]
})
/** 弹出添加修改标志 */
const dialogVisible = ref(false)
/** 列表多选数据 */
const multipleSelection = ref<TFlowType[]>([])
onMounted(() => {
  loaddata()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})
/** 查询 */
const loaddata = () => {
  const data: TQueryInfo = {
    order: order.value,
    pageSize: pageSize.value,
    page: currentPage.value,
    queryKey: queryKey.value
  }
  getFlowTypePageListApi(data).then((response) => {
    flowtypeList.value = response.obj
    pageTotal.value = response.total
  })
}
/**
 * 获取最大序号
 */
const getMaxSort = async () => {
  getFlowTypeMaxSortApi().then(({ success, obj }: IAxios) => {
    if (success) {
      flowtypeData.value.isort = obj
    }
  })
}
/**
 * @description: 新增
 * @return {*}
 */
const onAdd = async () => {
  ruleFormRef.value?.resetFields()
  reset()
  getMaxSort()
  dialogVisible.value = true
}
/**
 * 修改
 * @param row
 */
const onEdit = (row: TFlowType) => {
  dialogVisible.value = true
  nextTick(() => {
    flowtypeData.value = { ...row }
  })
}
/** 删除
 * @param row
 */
const onDelete = (row?: TFlowType) => {
  const ids: (string | undefined)[] = []
  if (row) {
    ids.push(row.id)
  } else {
    multipleSelection.value?.forEach((item) => {
      ids.push(item.id)
    })
  }
  if (ids.length < 1) {
    modal.msgWarning('请选的需要删除的信息')
    return false
  }
  modal.confirm('是否确认删除名称为"' + row?.sname + '"的数据项?').then(() => {
    deleteFlowTypeByIdApi(ids.join(',')).then(({ success }: IAxios) => {
      modal.msgSuccess('删除成功')
      loaddata()
    })
  })
}
/**
 * 保存提交
 * @param formEl
 */
const onSubmitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      if (flowtypeData.value.id) {
        updateFlowTypeApi(flowtypeData.value).then(({ success }: IAxios) => {
          modal.msgSuccess('保存成功')
          onResetForm(formEl)
          loaddata()
        })
      } else {
        saveFlowTypeApi(flowtypeData.value).then(({ success }: IAxios) => {
          modal.msgSuccess('保存成功')
          onResetForm(formEl)
          loaddata()
        })
      }
    }
  })
}
/**
 * 取消
 * @param formEl
 */
const onResetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  reset()
  formEl.resetFields()
  dialogVisible.value = false
}

/**
 * 表单重置
 */
const reset = () => {
  flowtypeData.value = {
    id: undefined,
    sname: '',
    isort: 0,
    buse: true,
    skey: '',
    sdesc: '',
    bactive: true
  }
}
/** 改变页码总数时 */
const handleSizeChange = (val: number) => {
  pageSize.value = val
  loaddata()
}
/**
 * 改变页数时
 * @param val
 */
const handleCurrentChange = (val: number) => {
  currentPage.value = val
  loaddata()
}
/**
 * 排序
 * @param val
 */
const sortChange = (val: any) => {
  sort.value = val.prop
  if (val.order === 'ascending') {
    order.value = 'asc'
  } else if (val.order === 'descending') {
    order.value = 'desc'
  } else {
    order.value = 'desc'
    sort.value = 'isort'
  }
  loaddata()
}
/**
 * 列表多选
 * @param val
 */
const handleSelectionChange = (val: TFlowType[]) => {
  multipleSelection.value = val
}

/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'FlowType'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>

<style scoped>
.el-select {
  margin-bottom: 0px;
}
.el-form-item {
  margin-left: 10px;
  margin-right: 10px;
}
</style>
@/types/views/flow/flowmanager
