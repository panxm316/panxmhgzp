<!--
 * @Author: songly
 * @Date: 2023-08-29 16:49:39
 * @LastEditTime: 2024-02-28 09:59:47
 * @LastEditors: songly
 * @Description: 报纸版心设置
-->

<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
        <el-button type="danger" icon="Delete" @click="handleDelete()">删除</el-button>
        <el-input
          v-model="queryInfo.queryKey"
          placeholder="请输报纸版心名称"
          clearable
          style="width: 200px"
          @clear="loaddata"
          @keyup.enter="loaddata"
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
          :data="PageSizeList"
          row-key="id"
          :height="tableheight"
          :border="true"
          stripe
          :header-cell-style="tableHeaderColor"
          @selection-change="handleSelectionChange"
          @sort-change="handleSortChange"
        >
          <el-table-column type="selection" prop="ischeck" width="50" align="center">
          </el-table-column>
          <el-table-column prop="sname" label="名称" sortable="custom" :min-width="150">
          </el-table-column>
          <el-table-column
            prop="ipagewidth"
            label="宽度"
            sortable="custom"
            :width="100"
            align="center"
          >
          </el-table-column>
          <el-table-column
            prop="ipageheight"
            label="高度"
            sortable="custom"
            :width="100"
            align="center"
          >
          </el-table-column>
          <el-table-column
            prop="icolwidth"
            label="栏宽宽度"
            sortable="custom"
            :width="120"
            align="center"
          >
          </el-table-column>
          <el-table-column
            prop="isinglescale"
            label="单行显示个数"
            sortable="custom"
            :width="130"
            align="center"
          >
          </el-table-column>
          <el-table-column prop="smoveflag" label="版位安排版面规则" :width="130" align="center">
          </el-table-column>
          <el-table-column
            prop="iscale"
            label="标准显示比例"
            sortable="custom"
            width="140"
            align="center"
          >
          </el-table-column>
          <el-table-column
            prop="nfontsize"
            label="字号"
            sortable="custom"
            width="80"
            align="center"
          >
          </el-table-column>
          <el-table-column prop="isort" label="序号" :width="80" sortable="custom" align="center">
          </el-table-column>
          <el-table-column prop="buse" label="启用" :width="80" sortable="custom" align="center">
            <template #default="scope">
              <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column prop="bdefault" label="默认" :width="80" align="center">
            <template #default="scope">
              <el-checkbox v-model="scope.row.bdefault" disabled></el-checkbox>
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
      title="版心设置编辑"
      width="800"
      align-center
      :close-on-click-modal="false"
    >
      <el-form ref="PageSizeFormRef" :model="pageSizeData" label-width="160px" :rules="rules">
        <el-row :gutter="50">
          <el-col :span="22">
            <el-form-item label="序号" prop="isort">
              <el-input-number
                v-model="pageSizeData.isort"
                :min="1"
                :max="99999"
                controls-position="right"
              />
            </el-form-item>
            <el-form-item label="名称" prop="sname">
              <el-input v-model="pageSizeData.sname" />
            </el-form-item>
            <el-form-item prop="ipagewidth" label="宽度(mm)">
              <el-input
                v-model="pageSizeData.ipagewidth"
                show-word-limit
                maxlength="10"
                placeholder="不得大于10个数字"
              />
            </el-form-item>
            <el-form-item prop="ipageheight" label="高度(mm)">
              <el-input
                v-model="pageSizeData.ipageheight"
                show-word-limit
                maxlength="10"
                placeholder="不得大于10个数字"
              />
            </el-form-item>
            <el-form-item prop="icolwidth" label="栏宽宽度(mm)">
              <el-input
                v-model="pageSizeData.icolwidth"
                show-word-limit
                maxlength="10"
                placeholder="不得大于10个数字"
              />
            </el-form-item>
            <el-form-item prop="isinglescale" label="单行显示个数(推荐)">
              <el-input
                v-model="pageSizeData.isinglescale"
                show-word-limit
                maxlength="10"
                placeholder="不得大于10个数字"
              />
            </el-form-item>
            <el-form-item prop="smoveflag" label="版位安排版面规则">
              <el-input v-model="pageSizeData.smoveflag" />
              <span style="color: red">**必须是:{1,2,...4,末版,半叠}中的字样(半角)**</span>
            </el-form-item>
            <el-form-item prop="iscale" label="标准显示比例(推荐)%">
              <el-input
                v-model="pageSizeData.iscale"
                show-word-limit
                maxlength="3"
                placeholder="不得大于3个数字"
              />
            </el-form-item>
            <el-form-item prop="nfontsize" label="字号">
              <el-input
                v-model="pageSizeData.nfontsize"
                show-word-limit
                maxlength="2"
                placeholder="不得大于2个数字"
            /></el-form-item>
            <el-row :gutter="50">
              <el-col :span="4">
                <el-form-item label="启用" prop="buse">
                  <el-checkbox v-model="pageSizeData.buse"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item label="默认" prop="bdefault">
                  <el-checkbox v-model="pageSizeData.bdefault"></el-checkbox>
                </el-form-item>
              </el-col>
            </el-row>
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
import type { TPageSize } from '@/types/views/ad/pagesize'
import type { TQueryInfo } from '@/types/common/index'
import {
  getPageSizeListApi,
  getPageSizeMaxSortApi,
  savePageSizeApi,
  updatePageSizeApi,
  deletePageSizeByIdApi
} from '@/api/ad/pagesize'
import { required, computTableHeight, isNumberStr, tableHeaderColor } from '@/utils/index'
import type { IAxios } from 'axios'
import modal from '@/plugins/modal'

defineOptions({
  name: 'PageSize'
})
import { useRouter } from 'vue-router'
const router = useRouter()

/** 表格高度 */
const tableheight = ref(0)
/** 编辑 dialog */
const dialogVisible = ref(false)
/** 版心设置列表 */
const PageSizeList = ref<TPageSize[]>([])
/** 版心设置 */
const pageSizeData = ref<TPageSize>({
  id: undefined,
  sname: '',
  isort: 0,
  ipagewidth: 0,
  ipageheight: 0,
  isinglescale: 0,
  iscale: 0,
  smoveflag: '',
  icolwidth: 0,
  nfontsize: 0,
  buse: true,
  bdefault: false
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
const queryInfo = ref<TQueryInfo & { buse?: string }>({
  sort: 'isort',
  order: 'asc',
  queryKey: ''
})

const PageSizeFormRef = ref<FormInstance>()
const PageSizeSelection = ref<TPageSize[]>()
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<TPageSize>>({
  sname: [{ validator: required, required: true, message: '请输入名称', trigger: 'blur' }],
  ipagewidth: [{ validator: isNumberStr, required: true, message: '请输入数字', trigger: 'blur' }],
  ipageheight: [{ validator: isNumberStr, required: true, message: '请输入数字', trigger: 'blur' }],
  isinglescale: [
    { validator: isNumberStr, required: true, message: '请输入数字', trigger: 'blur' }
  ],
  icolwidth: [{ validator: isNumberStr, required: true, message: '请输入数字', trigger: 'blur' }],
  smoveflag: [
    { validator: required, required: true, message: '请输入版位安排版面规则', trigger: 'blur' },
    { max: 180, message: '不得大于180个字符', trigger: 'blur' }
  ],
  iscale: [{ validator: isNumberStr, required: true, message: '请输入数字', trigger: 'blur' }],
  nfontsize: [{ validator: isNumberStr, required: true, message: '请输入数字', trigger: 'blur' }]
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
const handleSelectionChange = (rows: TPageSize[]) => {
  PageSizeSelection.value = rows
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
  const data: TQueryInfo & { buse?: string } = {
    sort: queryInfo.value.sort,
    order: queryInfo.value.order,
    pageSize: pageSize.value,
    page: currentPage.value,
    queryKey: queryInfo.value.queryKey
  }

  getPageSizeListApi(data).then(({ obj }: IAxios<TPageSize[]>) => {
    console.log(obj)
    PageSizeList.value = obj
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
  getPageSizeMaxSortApi().then(({ success, obj }: IAxios<number>) => {
    pageSizeData.value.isort = obj
  })
}
/**
 * 新增
 */
const handleAdd = () => {
  pageSizeData.value = {
    sname: '',
    isort: 0,
    buse: true,
    ipagewidth: 0,
    ipageheight: 0,
    isinglescale: 0,
    iscale: 0,
    smoveflag: '',
    icolwidth: 0,
    nfontsize: 0,
    bdefault: false
  }
  getMaxSort()
  dialogVisible.value = true
}
/**
 * 修改
 * @param row
 */
const handleUpdate = (row: TPageSize) => {
  pageSizeData.value = { ...row }
  dialogVisible.value = true
}
/**
 * 保存
 */
const handleSave = () => {
  PageSizeFormRef.value?.validate((valid) => {
    if (valid) {
      if (!pageSizeData.value.id) {
        savePageSizeApi(pageSizeData.value).then(({ success, msg }: IAxios) => {
          if (success) {
            modal.msgSuccess('操作成功')
          } else {
            // modal.msgError(msg)
          }
          loaddata()
        })
      } else {
        updatePageSizeApi(pageSizeData.value).then(({ success, msg }: IAxios) => {
          if (success) {
            modal.msgSuccess('操作成功')
          } else {
            // modal.msgError(msg)
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
    PageSizeFormRef.value?.clearValidate(['sname'])
  }, 10)
}
/**
 * 删除
 * @param row
 */
const handleDelete = (row?: TPageSize) => {
  modal.confirm('是否删除?').then(() => {
    const ids = []
    if (row) {
      ids.push(row.id)
    } else {
      PageSizeSelection.value?.forEach((color) => ids.push(color.id))
    }
    if (!ids.length) {
      modal.msgWarning('请选择删除记录')
      return
    }
    deletePageSizeByIdApi(ids.join(',')).then(({ success, msg }: IAxios) => {
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
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'pagesize'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>

<style scoped>
ul {
  padding-left: 10px;
}
</style>
