<!--
 * @Author: songly
 * @Date: 2023-11-22 15:41:47
 * @LastEditTime: 2024-02-23 09:57:15
 * @LastEditors: wanghl
 * @Description: 折扣设置
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex" style="width: 94%">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
        <el-button type="danger" icon="Delete" @click="handleDelete()">删除</el-button>
        <el-date-picker
          v-model="queryInfo.syear"
          type="year"
          value-format="YYYY"
          placeholder="选择年度"
          style="width: 160px"
          @change="loaddata"
        />
        <el-input
          v-model="queryInfo.queryKey"
          placeholder="折扣名称检索"
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
          :data="discountGroupList"
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
          <el-table-column
            prop="discountgroupname"
            label="名称"
            min-width="150px"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column prop="syear" label="年份" sortable="custom" width="120" align="center">
          </el-table-column>
          <el-table-column
            prop="ncustomerdiscount"
            label="直接客户"
            sortable="custom"
            width="120"
            align="center"
          >
            <template #default="scope"> {{ scope.row.ncustomerdiscount }}% </template>
          </el-table-column>
          <el-table-column
            prop="nagencydiscount"
            label="代理公司"
            sortable="custom"
            width="120"
            align="center"
          >
            <template #default="scope"> {{ scope.row.nagencydiscount }}% </template>
          </el-table-column>
          <el-table-column
            prop="nagentdiscount"
            label="内容生产方"
            sortable="custom"
            width="120"
            align="center"
          >
            <template #default="scope"> {{ scope.row.nagentdiscount }}% </template>
          </el-table-column>
          <el-table-column prop="bhighest" label="最大折扣" width="100" align="center">
            <template #default="scope">
              <span v-if="scope.row.bhighest == true">
                <el-tag type="success">是</el-tag>
              </span>
              <span v-if="scope.row.bhighest == false">
                <el-tag type="warning">否</el-tag>
              </span>
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
          <el-table-column prop="createempname" label="创建人" width="160" show-overflow-tooltip>
          </el-table-column>
          <el-table-column prop="createdate" label="创建时间" width="160" sortable="custom">
            <template #default="scope">
              <span>{{ formatDate(scope.row.createdate) }}</span>
            </template>
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
                  title="折扣明细"
                  type="primary"
                  size="small"
                  icon="View"
                  @click="seediscountItem(scope.row)"
                  >明细</el-button
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
      <el-row>
        <el-pagination
          v-model:current-page="queryInfo.page"
          class="pagination"
          style="margin-left: 10px; width: 100%"
          :page-sizes="pageSizes"
          :page-size="queryInfo.pageSize"
          small
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="pageTotal"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        >
        </el-pagination>
      </el-row>
    </div>
    <!-- 折扣总表编辑 -->
    <el-dialog
      v-model="dialogVisible"
      title="折扣总表编辑"
      align-center
      :close-on-click-modal="false"
      width="700"
      @close="handleCancel"
    >
      <el-form
        ref="discountGroupFormRef"
        :model="discountGroupData"
        label-width="130px"
        :rules="rules"
      >
        <el-row :gutter="20">
          <el-col :span="21">
            <el-form-item label="名称" prop="discountgroupname">
              <el-input
                v-model="discountGroupData.discountgroupname"
                placeholder="请输入折扣名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="年份" prop="syear">
              <el-date-picker
                v-model="discountGroupData.syear"
                :editable="false"
                type="year"
                placeholder="选择年度"
                value-format="YYYY"
                :clearable="false"
                style="width: 500px"
              ></el-date-picker>
            </el-form-item>

            <el-form-item prop="ncustomerdiscount" label="直接客户">
              <el-input-number
                v-model="discountGroupData.ncustomerdiscount"
                :precision="1"
                :step="0.1"
                :min="0"
                :max="100"
                :controls="false"
                style="width: 160px"
              />
              <span style="margin-left: 10px">%</span>
            </el-form-item>
            <el-form-item prop="nagencydiscount" label="代理公司">
              <el-input-number
                v-model="discountGroupData.nagencydiscount"
                :precision="1"
                :step="0.1"
                :min="0"
                :max="100"
                :controls="false"
                style="width: 160px"
              />
              <span style="margin-left: 10px">%</span>
            </el-form-item>
            <el-form-item prop="nagentdiscount" label="内容生产方">
              <el-input-number
                v-model="discountGroupData.nagentdiscount"
                :precision="1"
                :step="0.1"
                :min="0"
                :max="100"
                :controls="false"
                style="width: 160px"
              />
              <span style="margin-left: 10px">%</span>
            </el-form-item>
            <el-row :gutter="10">
              <el-col :span="6">
                <el-form-item label="最大折扣" prop="bhighest">
                  <el-checkbox v-model="discountGroupData.bhighest"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="有效" prop="buse">
                  <el-checkbox v-model="discountGroupData.buse"></el-checkbox>
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
    <!-- 查看明细 -->
    <el-dialog
      v-model="dialogDiscountItem"
      title="折扣明细"
      width="90%"
      align-center
      :close-on-click-modal="false"
      @close="handleCancelDiscountItem"
    >
      <DiscountItem style="margin-top: -40px" :discountgroup="discountGroupDatalist"></DiscountItem>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { FormRules, FormInstance, menuItemEmits } from 'element-plus'
import DiscountItem from './DiscountItem.vue'
import {
  required,
  computTableHeight,
  isNumberStr,
  formatDate,
  tableHeaderColor
} from '@/utils/index'
import {
  getDiscountGroupListApi,
  getDiscountGroupByIdApi,
  saveDiscountGroupApi,
  updateDiscountGroup,
  deleteDiscountGroupApi
} from '@/api/price/discountgroup'
defineOptions({
  name: 'DiscountGroup'
})
import { useRouter } from 'vue-router'
import type { TDiscountGroup, TDiscountGroupQuery } from '@/types/views/price/discountgroup'
import type { IAxios } from 'axios'
import modal from '@/plugins/modal'

const queryInfo = reactive<TDiscountGroupQuery>({
  sort: 'syear',
  order: 'desc',
  syear: '',
  pageSize: 20,
  page: 1,
  queryKey: '' // 查询关键字
})

const discountGroupFormRef = ref<FormInstance>()
const discountGroupSelection = ref<TDiscountGroup[]>()

const router = useRouter()
const global = getCurrentInstance()?.appContext.config.globalProperties
/** 显示隐藏非常用**/
const showSearch = ref(false)
/** 表格高度 */
const tableheight = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 数据总条数 */
const pageTotal = ref<number>(0)
/** 编辑 dialog */
const dialogVisible = ref(false)
/** 明细 dialog */
const dialogDiscountItem = ref(false)
/** 年度选择 */
const syear = ref('')
const discountGroupList = ref<TDiscountGroup[]>([])
const discountGroupDatalist = ref<TDiscountGroup>({
  /** 主键   */
  id: '',
  /** 名称 */
  discountgroupname: '',
  /** 年份  */
  syear: '',
  /** 直接客户折扣   */
  ncustomerdiscount: 0,
  /** 代理公司折扣   */
  nagencydiscount: 0,
  /** 内容生产方折扣   */
  nagentdiscount: 0,
  /** 是否按最高  */
  bhighest: false,
  /** 是否有效 */
  buse: true,
  /** 备注   */
  sremark: '',
  /** 创建者id   */
  createempid: '',
  /** 创建者名称  */
  createempname: '',
  /**  创建日期   */
  createdate: ''
})
/** 折扣总表数据 */
const discountGroupData = ref<TDiscountGroup>({
  /** 主键   */
  id: '',
  /** 名称 */
  discountgroupname: '',
  /** 年份  */
  syear: '',
  /** 直接客户折扣   */
  ncustomerdiscount: 0,
  /** 代理公司折扣   */
  nagencydiscount: 0,
  /** 内容生产方折扣   */
  nagentdiscount: 0,
  /** 是否按最高  */
  bhighest: false,
  /** 是否有效 */
  buse: true,
  /** 备注   */
  sremark: '',
  /** 创建者id   */
  createempid: '',
  /** 创建者名称  */
  createempname: '',
  /**  创建日期   */
  createdate: ''
})
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<TDiscountGroup>>({
  discountgroupname: [
    { required: true, message: '请输入名称', trigger: 'change' },
    { max: 48, message: '不得大于48个字符', trigger: 'change' }
  ],
  syear: [{ required: true, message: '请选择年份', trigger: 'change' }],
  ncustomerdiscount: [{ required: true, message: '请填写折扣数', trigger: 'change' }],
  nagencydiscount: [{ required: true, message: '请填写折扣数', trigger: 'change' }],
  nagentdiscount: [{ required: true, message: '请填写折扣数', trigger: 'change' }]
})

onMounted(() => {
  loaddata()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})
/**
 * 获取折扣总表列表
 */
const loaddata = () => {
  getDiscountGroupListApi(queryInfo).then(({ obj, total }: IAxios<TDiscountGroup[]>) => {
    pageTotal.value = total
    discountGroupList.value = obj
  })
}
/**
 * 选择
 * @param rows
 */
const handleSelectionChange = (rows: TDiscountGroup[]) => {
  discountGroupSelection.value = rows
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'Discount'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
/**
 * 改变页码总数时
 */
const handleSizeChange = (val: number) => {
  queryInfo.pageSize = val
  loaddata()
}
/**
 * 改变页数时
 * @param val
 */
const handleCurrentChange = (val: number) => {
  queryInfo.page = val
  loaddata()
}
/**
 * 排序
 * @param val
 */
const handleSortChange = (val: { prop: string; order: string }) => {
  queryInfo.sort = val.prop

  if (val.order === 'ascending') {
    queryInfo.order = 'asc'
  } else if (val.order === 'descending') {
    queryInfo.order = 'desc'
  } else {
    queryInfo.order = ''
  }
  loaddata()
}

/**
 * 新增
 */
const handleAdd = () => {
  dialogVisible.value = true
}
/**
 * 修改
 * @param row
 */
const handleUpdate = (row: TDiscountGroup) => {
  discountGroupData.value = { ...row }
  dialogVisible.value = true
}
/**
 * 保存
 */
const handleSave = () => {
  discountGroupFormRef.value?.validate((valid) => {
    if (valid) {
      if (!discountGroupData.value.id) {
        saveDiscountGroupApi(discountGroupData.value).then(({ success, msg }: IAxios) => {
          if (success) {
            modal.msgSuccess('操作成功')
          } else {
            modal.msgError(msg)
          }
          loaddata()
        })
      } else {
        updateDiscountGroup(discountGroupData.value).then(({ success, msg }: IAxios) => {
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
const handleDelete = (row?: TDiscountGroup) => {
  modal.confirm('是否删除?').then(() => {
    const ids = []
    if (row) {
      ids.push(row.id)
    } else {
      discountGroupSelection.value?.forEach((item) => ids.push(item.id))
    }
    if (!ids.length) {
      modal.msgWarning('请选择删除记录')
      return
    }
    deleteDiscountGroupApi(ids.join(',')).then(({ success, msg }: IAxios) => {
      if (success) {
        modal.msgSuccess('操作成功')
      }
      // else {
      //   modal.msgError(msg)
      // }
      loaddata()
    })
  })
}
/**
 * 查看折扣明细
 */
const seediscountItem = (row: TDiscountGroup) => {
  dialogDiscountItem.value = true
  discountGroupDatalist.value = { ...row }
}
/**
 * 取消
 */
const handleCancel = () => {
  discountGroupFormRef.value?.resetFields()
  reset()
  loaddata()
  dialogVisible.value = false
}
/**
 * 关闭明细
 */
const handleCancelDiscountItem = () => {
  dialogDiscountItem.value = false
}
/** 重置*/
const reset = () => {
  discountGroupData.value = {
    id: '',
    discountgroupname: '',
    syear: '',
    ncustomerdiscount: 0,
    nagencydiscount: 0,
    nagentdiscount: 0,
    bhighest: false,
    buse: true,
    sremark: '',
    createempid: '',
    createempname: '',
    createdate: ''
  }
}
</script>
<style scoped></style>
