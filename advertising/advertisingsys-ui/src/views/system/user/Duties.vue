<!--
 * @Author: wanghl yanz
 * @Date: 2023-08-28 09:39:47
 * @LastEditors: wanghl
 * @LastEditTime: 2023-10-23 15:39:42
 * @Description: 人员职务设置
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button type="primary" icon="plus" @click="onAdd">新增</el-button>
        <el-button type="danger" class="ml10" icon="delete" @click="onDelete()">删除</el-button>
        <el-select
          v-model="buse"
          placeholder="启用状态"
          clearable
          style="width: 130px"
          @clear="loaddata"
          @change="loaddata"
        >
          <el-option label="未启用" value="0"></el-option>
          <el-option label="已启用" value="1"></el-option>
        </el-select>
        <el-input
          v-model="queryKey"
          placeholder="请输入人员职务名称"
          style="width: 200px"
          clearable
          @clear="loaddata"
          @keyup.enter="loaddata"
        />
        <el-button type="primary" icon="search" @click="loaddata">搜索</el-button>
        <el-tooltip class="item" effect="dark" content="帮助" placement="top">
          <el-button class="help" type="text" @click="showhelp()">?</el-button>
        </el-tooltip>
      </div>
    </div>
    <div class="table_box">
      <el-table
        :data="dutiesList"
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
        <el-table-column
          prop="sname"
          label="名称"
          min-width="360"
          sortable="custom"
        ></el-table-column>
        <el-table-column
          prop="isort"
          label="序号"
          width="80"
          sortable="custom"
          align="center"
        ></el-table-column>
        <el-table-column prop="buse" label="启用" width="80" align="center">
          <template #default="scope">
            <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="200"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button link type="success" icon="edit" @click="onEdit(scope.row)">修改</el-button>
            <el-button link type="danger" icon="delete" @click="onDelete(scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
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
      />
    </div>
    <el-dialog
      v-model="dialogVisible"
      title="人员职务编辑"
      width="700px"
      :align-center="true"
      :close-on-click-modal="false"
      @close="closeDialog"
    >
      <el-form ref="ruleFormRef" :rules="rules" :model="dutiesForm" label-width="120px">
        <el-row :gutter="50">
          <el-col :span="22">
            <el-form-item label="名称" prop="sname">
              <el-input v-model="dutiesForm.sname" />
            </el-form-item>
            <el-form-item label="序号" prop="isort">
              <el-input-number
                v-model="dutiesForm.isort"
                :min="1"
                :max="99999"
                controls-position="right"
              />
            </el-form-item>
            <el-form-item label="启用" prop="buse">
              <el-checkbox v-model="dutiesForm.buse" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" icon="check" @click="onSubmitForm(ruleFormRef)">保存</el-button>
          <el-button icon="close" @click="onResetForm(ruleFormRef)">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { IDuties } from '@/types/views/system/duties'
import {
  getDutyPageListApi,
  deleteDutyByIdApi,
  saveDutyApi,
  updateDutyApi,
  getDutyMaxSortApi
} from '@/api/system/duties'
import type { IAxios } from 'axios'
import type { ElFormItem, FormInstance, FormRules } from 'element-plus'
import modal from '@/plugins/modal'
import { required, computTableHeight, tableHeaderColor } from '@/utils/index'
import { TQueryInfo } from '@/types/common'

defineOptions({
  name: 'Duties'
})
import { useRouter } from 'vue-router'
const router = useRouter()
/** 每页选择下拉 */
const pageSizes = ref<number[]>([15, 20, 30, 40])
/** 页数 */
const currentPage = ref<number>(1)
/** 每页条数 */
const pageSize = ref<number>(15)
/** 数据总数 */
const pageTotal = ref<number>(0)
/** 排序字段 */
const sort = ref('isort')
/** 正序倒序 */
const order = ref('desc')
/** 查询职务名称 */
const queryKey = ref('')
/** 是否启用 */
const buse = ref('')
/** 表单ref */
const ruleFormRef = ref<FormInstance>()

/** 列表高度 */
const tableheight = ref(0)
/** 列表数据 */
const dutiesList = ref<IDuties[]>()
/** 弹出添加修改标志 */
const dialogVisible = ref(false)
/** 表单对象 */
const dutiesForm = ref<IDuties>({
  id: undefined,
  sname: '',
  isort: 1,
  buse: true
})

/** 校验输入 */
const rules = reactive<FormRules<IDuties>>({
  sname: [
    { validator: required, required: true, message: '请输入名称', trigger: 'change' },
    { max: 40, message: '不得大于40个字符', trigger: 'change' }
  ]
} as FormRules<IDuties>)
/** 列表多选数据 */
const multipleSelection = ref<IDuties[]>([])

onMounted(() => {
  loaddata()
  tableheight.value = computTableHeight()
})

/**
 * @description: 分页查询：职务名称
 * @return {*}
 */
const loaddata = () => {
  const data: TQueryInfo & { buse?: string } = {
    pageSize: pageSize.value,
    page: currentPage.value,
    sort: sort.value,
    order: order.value,
    queryKey: queryKey.value,
    buse: buse.value
  }

  getDutyPageListApi(data).then((res: IAxios<IDuties[]>) => {
    if (res.success) {
      dutiesList.value = res.obj
      pageTotal.value = res.total
    }
  })
}

/**
 * @description: 获取人员职务最大序号
 * @return {*}
 */
const getDutyMaxSort = async () => {
  await getDutyMaxSortApi().then((res: IAxios) => {
    if (res.success) {
      dutiesForm.value.isort = res.obj
    }
  })
}

/**
 * @description: 新增
 * @return {*}
 */
const onAdd = async () => {
  ruleFormRef.value?.resetFields()
  await getDutyMaxSort()
  dialogVisible.value = true
}

/**
 * @description: 修改选中
 * @param {*} row
 * @return {*}
 */
const onEdit = (row: IDuties) => {
  dialogVisible.value = true
  nextTick(() => {
    dutiesForm.value = { ...row }
  })
}

/**
 * @description: 删除选中
 * @param {*} row
 * @return {*}
 */
const onDelete = (row?: IDuties) => {
  const ids: (number | undefined)[] = []
  if (row) {
    ids.push(row.id)
  } else {
    multipleSelection.value?.forEach((item) => {
      ids.push(item.id)
    })
  }

  if (ids.length < 1) {
    modal.msgWarning('请选择需要删除的信息')
    return false
  }

  modal.confirm('是否删除选中数据？').then(() => {
    deleteDutyByIdApi(ids.join(',')).then(({ success }: IAxios) => {
      if (success) {
        modal.msgSuccess('删除成功')

        loaddata()
      }
    })
  })
}

/**
 * @description: 保存提交(saveOrUpdate)
 * @param {*} formEl
 * @return {*}
 */
const onSubmitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) {
    return
  }
  await formEl.validate((valid, fields) => {
    if (valid) {
      if (dutiesForm.value.id) {
        updateDutyApi(dutiesForm.value).then(({ success }: IAxios) => {
          if (success) {
            modal.msgSuccess('保存成功')
            onResetForm(formEl)
            loaddata()
          }
        })
      } else {
        saveDutyApi(dutiesForm.value).then(({ success }: IAxios) => {
          if (success) {
            modal.msgSuccess('保存成功')
            onResetForm(formEl)
            loaddata()
          }
        })
      }
    }
  })
}

/**
 * @description: 重置表格
 * @param {*} formEl
 * @return {*}
 */
const onResetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
  dialogVisible.value = false
}

/**
 * @description: 关闭编辑框（？）
 * @return {*}
 */
const closeDialog = () => {}

/**
 * @description: 页码总数变更
 * @param {*} val
 * @return {*}
 */
const handleSizeChange = (val: number) => {
  pageSize.value = val
  loaddata()
}

/**
 * @description: 页码变更
 * @param {*} val
 * @return {*}
 */
const handleCurrentChange = (val: number) => {
  currentPage.value = val
  loaddata()
}

/**
 * @description: 排序变动
 * @param {*} val
 * @return {*}
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
 * @description: 列表多选变动
 * @param {*} val
 * @return {*}
 */
const handleSelectionChange = (val: IDuties[]) => {
  multipleSelection.value = val
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'duties'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>

<style scoped></style>
