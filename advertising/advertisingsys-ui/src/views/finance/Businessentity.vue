<!--
 * @Author: caogd
 * @Date: 2023-08-22 13:29:01
 * @LastEditTime: 2023-10-31 10:37:29
 * @LastEditors: caogd
 * @Description: 经营主体设置
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button type="primary" icon="Plus" @click="onAdd">新增</el-button>
        <el-button type="danger" icon="Delete" @click="onDelete()">删除</el-button>
        <el-input
          v-model="queryKey"
          placeholder="请输经营主体名称"
          style="width: 200px"
          clearable
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
      <el-table
        :data="businessentityList"
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
          sortable="custom"
          min-width="160"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="saddress"
          label="收款方地址"
          min-width="160"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="sbankaccount"
          label="收款方银行账号"
          min-width="200"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="sphone"
          label="收款方电话"
          min-width="160"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column prop="taxitems" label="税目" width="100"></el-table-column>
        <el-table-column prop="taxrate" label="税率" width="100"></el-table-column>
        <el-table-column
          prop="isort"
          label="序号"
          width="80"
          sortable="custom"
          align="center"
        ></el-table-column>
        <el-table-column prop="buse" label="启用" width="80" align="center" sortable="custom">
          <template #default="scope">
            <!-- <span>{{ scope.row.buse ? '是' : '否' }}</span> -->
            <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column prop="bdefault" label="默认" width="100">
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
      title="经营主体编辑"
      width="700px"
      :align-center="true"
      :close-on-click-modal="false"
      @close="closeDialog"
    >
      <el-form ref="ruleFormRef" :rules="rules" :model="businessentityForm" label-width="120px">
        <el-row :gutter="50">
          <el-col :span="22">
            <el-form-item label="名称" prop="sname">
              <el-input v-model="businessentityForm.sname" />
            </el-form-item>
            <el-form-item label="收款方地址" prop="saddress">
              <el-input v-model="businessentityForm.saddress" />
            </el-form-item>
            <el-form-item label="收款方银行账号" prop="sbankaccount">
              <el-input v-model="businessentityForm.sbankaccount" />
            </el-form-item>
            <el-form-item label="收款方电话" prop="sphone">
              <el-input v-model="businessentityForm.sphone" />
            </el-form-item>
            <el-form-item label="税目" prop="taxitems">
              <el-input v-model="businessentityForm.taxitems" />
            </el-form-item>
            <el-form-item label="税率(%)" prop="taxrate">
              <el-input-number v-model="businessentityForm.taxrate" :controls="false" :max="100" />
            </el-form-item>
            <el-form-item label="序号" prop="isort">
              <el-input-number
                v-model="businessentityForm.isort"
                :min="1"
                :max="99999"
                controls-position="right"
              />
            </el-form-item>
            <el-form-item label="启用" prop="buse">
              <el-checkbox v-model="businessentityForm.buse" />
            </el-form-item>
            <el-form-item label="默认" prop="bdefault">
              <el-checkbox v-model="businessentityForm.bdefault" />
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
import { IBusinessentity } from '@/types/views/finance/businessentity'
import {
  getBusinessentityPageListApi,
  getMaxSortApi,
  saveBusinessentityApi,
  updateBusinessentityApi,
  deleteBusinessentityApi
} from '@/api/finance/businessentity'
import type { IAxios } from 'axios'
import { IPageRequest } from '@/types/common/index'
import type { ElFormItem, FormInstance, FormRules } from 'element-plus'
import modal from '@/plugins/modal'
import { required, validatePhone, computTableHeight, tableHeaderColor } from '@/utils/index'
defineOptions({
  name: 'Businessentity'
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
const queryKey = ref('')
/** 表单ref */
const ruleFormRef = ref<FormInstance>()
/** 校验设置参数 */
const rules = reactive<FormRules<IBusinessentity>>({
  sname: [
    { validator: required, required: true, message: '请输入名称', trigger: 'change' },
    { max: 40, message: '不得大于40个字符', trigger: 'change' }
  ],
  saddress: [{ max: 80, message: '不得大于80个字符', trigger: 'change' }],
  sbankaccount: [{ max: 40, message: '不得大于40个字符', trigger: 'change' }],
  sphone: [{ validator: validatePhone, trigger: 'change' }],
  taxitems: [{ max: 40, message: '不得大于40个字符', trigger: 'change' }]
})
/** 列表高度 */
const tableheight = ref(0)
/** 列表数据 */
const businessentityList = ref<IBusinessentity[]>()
/** 弹出添加修改标志 */
const dialogVisible = ref(false)
/** 表单对象 */
const businessentityForm = ref<IBusinessentity>({
  id: undefined,
  sname: '',
  buse: true,
  isort: 1,
  saddress: '',
  sbankaccount: '',
  sphone: '',
  taxitems: '',
  taxrate: 0,
  bdefault: false
})
/** 列表多选数据 */
const multipleSelection = ref<IBusinessentity[]>([])

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
  const data: IPageRequest & { queryKey?: string } = {
    order: order.value,
    page: currentPage.value,
    pageSize: pageSize.value,
    sort: sort.value,
    queryKey: queryKey.value
  }
  getBusinessentityPageListApi(data).then(({ obj, total, success }: IAxios<IBusinessentity[]>) => {
    if (success) {
      businessentityList.value = obj
      pageTotal.value = total
    }
  })
}
/**
 * 获取最大序号
 */
const getMaxSort = async () => {
  await getMaxSortApi().then(({ success, obj }: IAxios) => {
    if (success) {
      businessentityForm.value.isort = obj
    }
  })
}
/**
 * @description: 新增
 * @return {*}
 */
const onAdd = async () => {
  ruleFormRef.value?.resetFields()
  businessentityForm.value = {
    id: undefined,
    sname: '',
    buse: true,
    isort: 1,
    saddress: '',
    sbankaccount: '',
    sphone: '',
    taxitems: '',
    taxrate: 0,
    bdefault: false
  }
  await getMaxSort()
  dialogVisible.value = true
}
/**
 * 修改
 * @param row
 */
const onEdit = (row: IBusinessentity) => {
  dialogVisible.value = true
  nextTick(() => {
    businessentityForm.value = { ...row }
  })
}

const onDelete = (row?: IBusinessentity) => {
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
    deleteBusinessentityApi(ids.join(',')).then(({ success }: IAxios) => {
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
      if (businessentityForm.value.id) {
        updateBusinessentityApi(businessentityForm.value).then(({ success }: IAxios) => {
          if (success) {
            modal.msgSuccess('保存成功')
            onResetForm(formEl)
            loaddata()
          }
        })
      } else {
        saveBusinessentityApi(businessentityForm.value).then(({ success }: IAxios) => {
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
const handleSelectionChange = (val: IBusinessentity[]) => {
  multipleSelection.value = val
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'businessentity'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>
<style scoped lang="scss"></style>
