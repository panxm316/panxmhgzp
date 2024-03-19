<!--
 * @Author: songly
 * @Date: 2023-08-30 16:08:23
 * @LastEditTime: 2023-09-14 16:09:59
 * @LastEditors: wanghl
 * @Description:范围
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button type="warning" icon="Back" link text
          ><router-link to="/system/menu">返回菜单</router-link></el-button
        >
        <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
        <el-button type="danger" icon="Delete" @click="handleDelete()">删除</el-button>
      </div>
    </div>
    <div class="table_box">
      <el-row>
        <el-table
          :data="scopeList"
          row-key="id"
          :height="tableheight"
          :border="true"
          stripe
          :header-cell-style="tableHeaderColor"
          @selection-change="handleSelectionChange"
          @sort-change="handleSortChange"
        >
          <el-table-column type="selection" prop="ischeck" :width="50" align="center">
          </el-table-column>
          <el-table-column prop="sname" label="名称" sortable="custom" min-width="150">
          </el-table-column>
          <el-table-column
            prop="stablename"
            label="范围表名(设置)"
            min-width="150"
            sortable="custom"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column
            prop="scolumn"
            label="范围列名(设置)"
            min-width="150"
            sortable="custom"
            show-overflow-tooltip
          >
          </el-table-column>
          <!-- <el-table-column
            prop="squeryname"
            label="范围表名(查询sql)"
            min-width="160"
            sortable="custom"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column
            prop="squerycolumn"
            label="范围列名(查询sql)"
            min-width="160"
            sortable="custom"
            show-overflow-tooltip
          >
          </el-table-column> -->
          <el-table-column prop="idepth" label="深度" :width="80"> </el-table-column>
          <el-table-column prop="isort" label="序号" :width="80" sortable="custom" align="center">
          </el-table-column>
          <el-table-column prop="buse" label="有效" :width="80" sortable="custom" align="center">
            <template #default="scope">
              <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="210"
            class-name="small-padding fixed-width"
            fixed="right"
          >
            <template #default="scope">
              <el-button link type="success" icon="Edit" @click="handleUpdate(scope.row)"
                >修改</el-button
              >
              <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                >删除</el-button
              >
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
          small
          layout="total, sizes, prev, pager, next, jumper"
          :total="pageTotal"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        >
        </el-pagination>
      </el-row>
    </div>
    <el-dialog
      v-model="dialogVisible"
      title="范围编辑"
      width="700"
      align-center
      :close-on-click-modal="false"
    >
      <el-form ref="ScopeFormRef" :model="scopeData" label-width="150px" :rules="rules">
        <el-row :gutter="50">
          <el-col :span="22">
            <el-form-item label="序号" prop="isort">
              <el-input-number
                v-model="scopeData.isort"
                :min="1"
                :max="99999"
                controls-position="right"
              />
            </el-form-item>
            <el-form-item label="名称" prop="sname">
              <el-input v-model="scopeData.sname" />
            </el-form-item>
            <el-form-item prop="stablename" label="范围表名(设置)">
              <el-input v-model="scopeData.stablename" />
            </el-form-item>
            <el-form-item prop="scolumn" label=" 范围列名(设置)">
              <el-input v-model="scopeData.scolumn" />
            </el-form-item>
            <!-- <el-form-item prop="squeryname" label="范围表名(查询sql)">
              <el-input v-model="scopeData.squeryname" />
            </el-form-item>
            <el-form-item prop="squerycolumn" label="范围列名(查询sql)">
              <el-input v-model="scopeData.squerycolumn" />
            </el-form-item> -->
            <el-form-item prop="idepth" label="深度">
              <el-input v-model="scopeData.idepth" />
            </el-form-item>
            <el-form-item label="序号" prop="isort">
              <el-input-number
                v-model="scopeData.isort"
                :min="1"
                :max="99999"
                controls-position="right"
              />
            </el-form-item>
            <el-form-item label="有效" prop="buse">
              <el-checkbox v-model="scopeData.buse"></el-checkbox>
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
import { FormRules, FormInstance } from 'element-plus'
import type { TScope } from '@/types/views/system/scope'
import type { TQueryInfo } from '@/types/common/index'
import {
  getScopePageListApi,
  getScopeMaxSortApi,
  saveScopeApi,
  updateScopeApi,
  deleteScopeApi
} from '@/api/system/scope'
import { required, computTableHeight, isNumberStr, tableHeaderColor } from '@/utils/index'
import type { IAxios } from 'axios'
import modal from '@/plugins/modal'

defineOptions({
  name: 'Scope'
})
/** 表格高度 */
const tableheight = ref(0)
/** 编辑 dialog */
const dialogVisible = ref(false)
/** 范围列表 */
const scopeList = ref<TScope[]>([])
/** 范围 */
const scopeData = ref<TScope>({
  id: '',
  sname: '',
  stablename: '',
  scolumn: '',
  squeryname: '',
  squerycolumn: '',
  idepth: 1,
  isort: 0,
  buse: false
})
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 页数 */
const currentPage = ref<number>(1)
/** 每页条数 */
const pageSize = ref<number>(20)
/** 数据总条数 */
const pageTotal = ref<number>(0)

/** 查询对象 */
const queryInfo = ref<TQueryInfo>({
  sort: 'isort',
  order: 'asc',
  pageSize: pageSize.value,
  page: currentPage.value,
  queryKey: ''
})

const ScopeFormRef = ref<FormInstance>()
const ScopeSelection = ref<TScope[]>()
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<TScope>>({
  sname: [
    { validator: required, required: true, message: '请输入名称', trigger: 'blur' },
    { max: 180, message: '不得大于180个字符', trigger: 'blur' }
  ],
  idepth: [
    { validator: isNumberStr, required: true, message: '请输入数字', trigger: 'blur' }
    // { max: 10, message: '不得大于10个数字', trigger: 'blur' }
  ],
  stablename: [
    { validator: required, required: true, message: '请输入范围表名(设置)', trigger: 'blur' },
    { max: 180, message: '不得大于180个字符', trigger: 'blur' }
  ],
  scolumn: [
    { validator: required, required: true, message: '请输入范围列名(设置)', trigger: 'blur' },
    { max: 180, message: '不得大于180个字符', trigger: 'blur' }
  ]
  // squeryname: [
  //   { validator: required, required: true, message: '请输入范围表名(查询sql)', trigger: 'blur' },
  //   { max: 180, message: '不得大于180个字符', trigger: 'blur' }
  // ],
  // squerycolumn: [
  //   { validator: required, required: true, message: '请输入范围列名(查询sql)', trigger: 'blur' },
  //   { max: 180, message: '不得大于180个字符', trigger: 'blur' }
  // ]
})
onMounted(() => {
  loaddata()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})

/**
 * 选择
 * @param rows
 */
const handleSelectionChange = (rows: TScope[]) => {
  ScopeSelection.value = rows
}
/**
 * 排序
 * @param val
 */
const handleSortChange = (val: { prop: string; order: string }) => {
  queryInfo.value.sort = val.prop

  if (val.order === 'ascending') {
    queryInfo.value.order = 'asc'
  } else if (val.order === 'descending') {
    queryInfo.value.order = 'desc'
  } else {
    queryInfo.value.order = ''
  }
  loaddata()
}

/**
 * 获取色彩列表
 */
const loaddata = () => {
  const data = queryInfo.value
  getScopePageListApi(data).then(({ obj }: IAxios<TScope[]>) => {
    console.log(obj)
    scopeList.value = obj
  })
}
/**
 * 改变页码总数时
 */
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
 * 获取序号最大值
 */
const getMaxSort = () => {
  getScopeMaxSortApi().then(({ success, obj }: IAxios<number>) => {
    scopeData.value.isort = obj
  })
}
/**
 * 新增
 */
const handleAdd = () => {
  scopeData.value = {
    id: '',
    sname: '',
    stablename: '',
    scolumn: '',
    squeryname: '',
    squerycolumn: '',
    idepth: 1,
    isort: 0,
    buse: false
  }
  getMaxSort()
  dialogVisible.value = true
}
/**
 * 修改
 * @param row
 */
const handleUpdate = (row: TScope) => {
  scopeData.value = { ...row }
  dialogVisible.value = true
}
/**
 * 保存
 */
const handleSave = () => {
  ScopeFormRef.value?.validate((valid) => {
    if (valid) {
      if (!scopeData.value.id) {
        saveScopeApi(scopeData.value).then(({ success, msg }: IAxios) => {
          if (success) {
            modal.msgSuccess('操作成功')
          } else {
            modal.msgError(msg)
          }
          loaddata()
        })
      } else {
        updateScopeApi(scopeData.value).then(({ success, msg }: IAxios) => {
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
  dialogVisible.value = false
  setTimeout(() => {
    ScopeFormRef.value?.clearValidate(['sname'])
  }, 10)
}
/**
 * 删除
 * @param row
 */
const handleDelete = (row?: TScope) => {
  modal.confirm('是否删除?').then(() => {
    const ids = []
    if (row) {
      ids.push(row.id)
    } else {
      ScopeSelection.value?.forEach((color) => ids.push(color.id))
    }
    if (!ids.length) {
      modal.msgWarning('请选择删除记录')
      return
    }
    deleteScopeApi(ids.join(',')).then(({ success, msg }: IAxios) => {
      if (success) {
        modal.msgSuccess('操作成功')
      } else {
        // modal.msgError(msg)
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
