<!--
 * @Author: songly
 * @Date: 2023-08-25 10:38:06
 * @LastEditTime: 2023-10-23 16:27:27
 * @LastEditors: songly
 * @Description:价格打包类型
-->
<template>
  <div id="adtypeform" class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button type="primary" icon="Plus" @click="onAdd">新增</el-button>
        <el-button type="danger" icon="Delete" @click="onDelete()">删除</el-button>
        <el-select
          v-model="buse"
          placeholder="启用状态"
          clearable
          style="width: 130px"
          @clear="loaddata"
          @change="loaddata"
        >
          <el-option label="未启用" value="false"></el-option>
          <el-option label="已启用" value="true"></el-option>
        </el-select>
        <el-input
          v-model="queryKey"
          placeholder="请输打包类型名称"
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
          :data="pricepackagetypeList"
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
          <el-table-column prop="sname" label="名称" min-width="100" sortable="custom">
          </el-table-column>
          <el-table-column prop="iitemcount" label="次数" min-width="120"> </el-table-column>
          <el-table-column
            prop="isort"
            label="序号"
            width="80"
            sortable="custom"
            align="center"
          ></el-table-column>
          <el-table-column prop="buse" label="启用" width="80" align="center">
            <template #default="scope">
              <!-- <span>{{ scope.row.buse ? '是' : '否' }}</span> -->
              <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="200"
            class-name="small-padding fixed-width"
          >
            <template #default="scope">
              <el-button link type="success" icon="Edit" @click="onEdit(scope.row)">修改</el-button>
              <el-button link type="danger" icon="Delete" @click="onDelete(scope.row)"
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
          layout="total, sizes, prev, pager, next, jumper"
          :total="pageTotal"
          small
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        >
        </el-pagination>
      </el-row>
    </div>
    <!-- 添加或修改打包类型对话框 -->
    <el-dialog v-model="dialogVisible" width="700px" align-center title="打包类型编辑">
      <el-form ref="ruleFormRef" :rules="rules" :model="adtypeData" label-width="100px">
        <el-row :gutter="50">
          <el-col :span="22">
            <el-form-item label="名称" prop="sname">
              <el-input v-model="adtypeData.sname" />
            </el-form-item>
            <el-form-item label="序号" prop="isort">
              <el-input-number
                v-model="adtypeData.isort"
                :min="1"
                :max="99999"
                controls-position="right"
              />
            </el-form-item>
            <el-form-item label="启用" prop="buse">
              <el-checkbox v-model="adtypeData.buse" />
            </el-form-item>
            <el-form-item label="次数" prop="iitemcount">
              <el-input v-model="adtypeData.iitemcount" :maxlength="200" />
            </el-form-item>
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
import type { IAxios } from 'axios'
import {
  getPricePackageTypePageListApi,
  deletePricePackageTypeByIdApi,
  savePricePackageTypeApi,
  updatePricePackageTypeApi,
  getPricePackageTypeMaxSortApi
} from '@/api/ad/pricepackagetype'
import { TPricePackageType } from '@/types/views/ad/pricepackagetype'
import { ElFormItem, FormInstance, FormRules } from 'element-plus'

defineOptions({
  name: 'TPricePackageType'
})
import { useRouter } from 'vue-router'
const router = useRouter()
const adtypeData = ref<TPricePackageType>({
  id: undefined,
  sname: '',
  isort: 0,
  buse: true,
  iitemcount: 0
})
/**
 * 列表数据
 */
const pricepackagetypeList = ref([]) // 菜单数据
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
const rules = reactive<FormRules<TPricePackageType>>({
  sname: [{ required: true, message: '请输入名称', trigger: 'blur' }]
})
/** 弹出添加修改标志 */
const dialogVisible = ref(false)
/** 列表多选数据 */
const multipleSelection = ref<TPricePackageType[]>([])
onMounted(() => {
  loaddata()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})
/** 查询 */
const loaddata = () => {
  const data: TQueryInfo & { buse: string } = {
    sort: sort.value,
    order: order.value,
    pageSize: pageSize.value,
    page: currentPage.value,
    queryKey: queryKey.value,
    buse: buse.value
  }
  getPricePackageTypePageListApi(data).then((response) => {
    pricepackagetypeList.value = response.obj
    pageTotal.value = response.total
  })
}
/**
 * 获取最大序号
 */
const getMaxSort = async () => {
  getPricePackageTypeMaxSortApi().then(({ success, obj }: IAxios) => {
    if (success) {
      adtypeData.value.isort = obj
    }
  })
}
/**
 * @description: 新增
 * @return {*}
 */
const onAdd = async () => {
  ruleFormRef.value?.resetFields()
  adtypeData.value = {
    id: undefined,
    sname: '',
    isort: 0,
    buse: true,
    iitemcount: 0
  }
  getMaxSort()
  dialogVisible.value = true
}
/**
 * 修改
 * @param row
 */
const onEdit = (row: TPricePackageType) => {
  dialogVisible.value = true
  nextTick(() => {
    adtypeData.value = { ...row }
  })
}
/** 删除
 * @param row
 */
const onDelete = (row?: TPricePackageType) => {
  const ids: (number | undefined)[] = []
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
    deletePricePackageTypeByIdApi(ids.join(',')).then(({ success }: IAxios) => {
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
      if (adtypeData.value.id) {
        updatePricePackageTypeApi(adtypeData.value).then(({ success }: IAxios) => {
          modal.msgSuccess('保存成功')
          onResetForm(formEl)
          loaddata()
        })
      } else {
        savePricePackageTypeApi(adtypeData.value).then(({ success }: IAxios) => {
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
  formEl.resetFields()
  dialogVisible.value = false
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
const handleSelectionChange = (val: TPricePackageType[]) => {
  multipleSelection.value = val
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'pricepackagetype'
  router.push(to)
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
