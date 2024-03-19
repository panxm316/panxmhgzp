<!--
 * @Author: wanghl
 * @Date: 2023-08-30 13:10:06
 * @LastEditTime: 2023-11-01 13:10:41
 * @LastEditors: wanghl
 * @Description:开票项目
-->

<template>
  <div class="app-container">
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
          <el-option label="已启用" value="1" />
          <el-option label="未启用" value="0" />
        </el-select>
        <el-input
          v-model="sname"
          placeholder="请输开票项目名称"
          style="width: 200px"
          clearable
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
      <el-table
        :data="adprintItemlist"
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
          min-width="160"
          sortable="custom"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="staxcode"
          label="税收编码"
          min-width="200"
          sortable="custom"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="isort"
          label="序号"
          width="80"
          sortable="custom"
          align="center"
        ></el-table-column>
        <el-table-column prop="buse" label="启用" :width="80" align="center" sortable="custom">
          <template #default="scope">
            <!-- <span>{{ scope.row.buse ? '是' : '否' }}</span> -->
            <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column prop="bdefault" label="默认" width="60">
          <template #default="scope">
            <!-- <span>{{ scope.row.bdefault ? '是' : '否' }}</span> -->
            <el-checkbox v-model="scope.row.bdefault" disabled></el-checkbox>
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
            <el-button link type="success" icon="Edit" @click="onEdit(scope.row)">修改</el-button>
            <el-button link type="danger" icon="Delete" @click="onDelete(scope.row)"
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
      title="开票项目编辑"
      width="700px"
      :align-center="true"
      :close-on-click-modal="false"
      @close="closeDialog"
    >
      <el-form ref="ruleFormRef" :rules="rules" :model="adprintItemForm" label-width="120px">
        <el-row :gutter="50">
          <el-col :span="22">
            <el-form-item label="名称" prop="sname">
              <el-input v-model="adprintItemForm.sname" />
            </el-form-item>
            <el-form-item label="税收编码" prop="staxcode">
              <el-input v-model="adprintItemForm.staxcode" />
            </el-form-item>
            <el-form-item label="序号" prop="isort">
              <el-input-number
                v-model="adprintItemForm.isort"
                :min="1"
                :max="99999"
                controls-position="right"
              />
            </el-form-item>
            <el-form-item label="启用" prop="buse">
              <el-checkbox v-model="adprintItemForm.buse" />
            </el-form-item>
            <el-form-item label="默认" prop="bdefault">
              <el-checkbox v-model="adprintItemForm.bdefault" />
            </el-form-item>
            <el-form-item> </el-form-item>
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
import type { TAdprintitemQ, IAdprintitem } from '@/types/views/finance/Adprintitem'
import {
  getAdPrintItemPageListApi,
  saveAdPrintItemApi,
  updateAdPrintItemApi,
  getAdPrintItemByIdApi,
  deleteAdPrintItemByIdApi,
  getAdPrintItemMaxSortApi
} from '@/api/finance/Adprintitem'
import type { IAxios } from 'axios'
import type { ElFormItem, FormInstance, FormRules } from 'element-plus'
import modal from '@/plugins/modal'
import { required, computTableHeight, tableHeaderColor, isNumberStr } from '@/utils/index'
defineOptions({
  name: 'Adprintitem'
})
import { useRouter } from 'vue-router'
const router = useRouter()

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
const order = ref('desc')
/** 关键字 */
const sname = ref('')
/** 是否可选 */
const buse = ref('')
/** 表单ref */
const ruleFormRef = ref<FormInstance>()
/** 校验设置参数 */
const rules = reactive<FormRules<IAdprintitem>>({
  sname: [
    { validator: required, required: true, message: '请输入名称', trigger: 'change' },
    { max: 40, message: '不得大于40个字符', trigger: 'change' }
  ],
  staxcode: [{ validator: isNumberStr, required: true, message: '请输入数字', trigger: 'change' }]
})
/** 列表高度 */
const tableheight = ref(0)
/** 列表数据 */
const adprintItemlist = ref<IAdprintitem[]>()
/** 弹出添加修改标志 */
const dialogVisible = ref(false)
/** 表单对象 */
const adprintItemForm = ref<IAdprintitem>({
  id: undefined,
  sname: '',
  buse: true,
  isort: 1,
  bdefault: false,
  staxcode: ''
})
/** 列表多选数据 */
const multipleSelection = ref<IAdprintitem[]>([])

onMounted(() => {
  loaddata()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})

/**
 * @description: 经营主体分页查询
 * @return {*}
 */
const loaddata = () => {
  const data: TAdprintitemQ = {
    order: order.value,
    page: currentPage.value,
    pageSize: pageSize.value,
    sort: sort.value,
    sname: sname.value,
    buse: buse.value
  }
  getAdPrintItemPageListApi(data).then(({ obj, total, success }: IAxios<IAdprintitem[]>) => {
    if (success) {
      adprintItemlist.value = obj
      console.log(adprintItemlist.value)
      pageTotal.value = total
    }
  })
}
/**
 * 获取最大序号
 */
const getMaxSort = async () => {
  await getAdPrintItemMaxSortApi().then(({ success, obj }: IAxios) => {
    if (success) {
      adprintItemForm.value.isort = obj
    }
  })
}
/**
 * @description: 新增
 * @return {*}
 */
const onAdd = async () => {
  // ruleFormRef.value?.resetFields()
  reset()
  await getMaxSort()
  dialogVisible.value = true
}
/**
 * 修改
 * @param row
 */
const onEdit = (row: IAdprintitem) => {
  dialogVisible.value = true
  nextTick(() => {
    adprintItemForm.value = { ...row }
  })
}

const onDelete = (row?: IAdprintitem) => {
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
  modal.confirm('是否删除选中数据？').then(() => {
    deleteAdPrintItemByIdApi(ids.join(',')).then(({ success }: IAxios) => {
      if (success) {
        modal.msgSuccess('删除成功')

        loaddata()
      }
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
      if (adprintItemForm.value.id) {
        updateAdPrintItemApi(adprintItemForm.value).then(({ success }: IAxios) => {
          if (success) {
            modal.msgSuccess('保存成功')
            onResetForm(formEl)
            loaddata()
          }
        })
      } else {
        saveAdPrintItemApi(adprintItemForm.value).then(({ success }: IAxios) => {
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
 * 取消
 * @param formEl
 */
const onResetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
  dialogVisible.value = false
}

const closeDialog = () => {
  // ruleFormRef.value?.resetFields()
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
const handleSelectionChange = (val: IAdprintitem[]) => {
  multipleSelection.value = val
}
/**
 * 表单重置
 */
const reset = () => {
  adprintItemForm.value = {
    id: undefined,
    sname: '',
    buse: true,
    isort: 1,
    bdefault: false,
    staxcode: ''
  }
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'adprintitem'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>
<style scoped lang="scss"></style>
