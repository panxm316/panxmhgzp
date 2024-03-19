<!--
 * @Author: songly
 * @Date: 2023-11-23 09:45:48
 * @LastEditTime: 2024-02-23 09:22:59
 * @LastEditors: wanghl
 * @Description: 折扣明细
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
        <el-button type="danger" icon="Delete" @click="handleDelete()">删除</el-button>
        <el-select
          v-model="queryInfo.mediaid"
          placeholder="请选择媒体"
          clearable
          style="width: 200px"
          @change="loaddata"
        >
          <el-option
            v-for="item in mediaCombo"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
        <span style="line-height: 30px; margin: 0 5px">行业:</span>
        <HgZtreeSelect
          :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYINDUSTRY"
          :selectids="queryInfo.adindustryid ? [queryInfo.adindustryid] : []"
          style="width: 200px"
          @selectionztree="querySelectionindustry"
        ></HgZtreeSelect>
        <!-- <el-input
          v-model="queryInfo.queryKey"
          placeholder="折扣名称检索"
          clearable
          style="width: 200px"
          @keyup.enter="loaddata"
          @clear="loaddata"
        /> -->
        <!-- <el-button type="primary" icon="Search" @click="loaddata">搜索</el-button> -->
      </div>
    </div>
    <div class="table_box">
      <el-row>
        <el-table
          :data="discountItemList"
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

          <el-table-column prop="medianame" label="媒体名称" width="160" show-overflow-tooltip>
          </el-table-column>
          <el-table-column prop="adindustryname" label="行业名称" width="160" show-overflow-tooltip>
          </el-table-column>
          <el-table-column
            prop="ndiscount"
            label="折扣"
            sortable="custom"
            width="100"
            align="center"
          >
            <template #default="scope"> {{ scope.row.ndiscount }}% </template>
          </el-table-column>
          <el-table-column prop="bcustomer" label="直接客户" width="120" align="center">
            <template #default="scope">
              <span v-if="scope.row.bcustomer == true">
                <el-tag type="success">是</el-tag>
              </span>
              <span v-if="scope.row.bcustomer == false">
                <el-tag type="warning">否</el-tag>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="bagency" label="代理公司" width="120" align="center">
            <template #default="scope">
              <span v-if="scope.row.bagency == true">
                <el-tag type="success">是</el-tag>
              </span>
              <span v-if="scope.row.bagency == false">
                <el-tag type="warning">否</el-tag>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="bagent" label="内容生产方" width="120" align="center">
            <template #default="scope">
              <span v-if="scope.row.bagent == true">
                <el-tag type="success">是</el-tag>
              </span>
              <span v-if="scope.row.bagent == false">
                <el-tag type="warning">否</el-tag>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="bvip" label="大客户" width="100" align="center">
            <template #default="scope">
              <span v-if="scope.row.bvip == true">
                <el-tag>是</el-tag>
              </span>
              <span v-if="scope.row.bvip == false">
                <el-tag type="warning">否</el-tag>
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="discountgroupname"
            label="组名称"
            min-width="150px"
            show-overflow-tooltip
          >
            {{ props.discountgroup.discountgroupname }}
          </el-table-column>
          <el-table-column prop="sremark" label="备注" min-width="100"> </el-table-column>
          <el-table-column
            label="操作"
            width="240"
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
      title="折扣明细编辑"
      align-center
      :close-on-click-modal="false"
      width="700"
      @close="handleCancel"
    >
      <el-form
        ref="discountItemFormRef"
        :model="discountItemData"
        label-width="130px"
        :rules="rules"
      >
        <el-row :gutter="20">
          <el-col :span="21">
            <el-form-item label="名称" prop="sname">
              <el-select
                v-model="discountItemData.mediaid"
                placeholder="请选择媒体"
                clearable
                style="width: 500px"
              >
                <el-option
                  v-for="item in mediaCombo"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="行业名称" prop="adindustryid">
              <HgZtreeSelect
                :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYINDUSTRY"
                :selectids="discountItemData.adindustryid ? [discountItemData.adindustryid] : []"
                style="width: 100%"
                @selectionztree="onSelectionindustry"
              ></HgZtreeSelect>
            </el-form-item>
            <el-row :gutter="18">
              <el-col :span="6">
                <el-form-item label="直接客户" prop="bcustomer">
                  <el-checkbox v-model="discountItemData.bcustomer"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="代理公司" prop="bagency">
                  <el-checkbox v-model="discountItemData.bagency"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="内容生产方" prop="bagent">
                  <el-checkbox v-model="discountItemData.bagent"></el-checkbox>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :span="6">
                <el-form-item label="大客户" prop="bvip">
                  <el-checkbox v-model="discountItemData.bvip"></el-checkbox>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item prop="ndiscount" label="折扣">
              <el-input-number
                v-model="discountItemData.ndiscount"
                :precision="1"
                :step="0.1"
                :min="0"
                :max="100"
                :controls="false"
                style="width: 160px"
              />
              <span style="margin-left: 10px">%</span>
            </el-form-item>
            <el-form-item prop="sremark" label="备注">
              <el-input v-model="discountItemData.sremark" :maxlength="200" />
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
import { EHgDeptZtreeUrl } from '@/types/common/enumindex'
import type { TSelectZtree } from '@/types/common/index'
import {
  required,
  computTableHeight,
  isNumberStr,
  formatDate,
  tableHeaderColor
} from '@/utils/index'
import {
  getDiscountItemListApi,
  getDiscountItemByIdApi,
  saveDiscountItemApi,
  updateDiscountItemApi,
  deleteDiscountItemApi
} from '@/api/price/discountitem'
defineOptions({
  name: 'DiscountItem'
})
import { useRouter } from 'vue-router'
import type { TDiscountItem, TDiscountItemQuery } from '@/types/views/price/discountitem'
import type { IAxios } from 'axios'
import modal from '@/plugins/modal'
import { getMediaDataComboApi } from '@/api/media/media'
import { IDataCombo } from '@/types/common/DataCombo'
import type { TDiscountGroup, TDiscountGroupQuery } from '@/types/views/price/discountgroup'
const props = defineProps<{ discountgroup: TDiscountGroup }>()
const queryInfo = reactive<TDiscountItemQuery>({
  id: '',
  discountgroupid: '',
  discountgroupname: '',
  mediaid: '',
  medianame: '',
  adindustryid: '',
  adindustryname: '',
  bcustomer: false,
  bagency: false,
  bagent: false,
  bvip: false,
  ndiscount: 0,
  sremark: '',
  sort: '',
  order: 'desc',
  pageSize: 20,
  page: 1,
  queryKey: '' // 查询关键字
})

const discountItemFormRef = ref<FormInstance>()
const discountItemSelection = ref<TDiscountItem[]>()

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
/**
 * 媒体列表
 */
const mediaCombo = ref<IDataCombo[]>([])
/** 年度选择 */
const syear = ref('')
const discountItemList = ref<TDiscountItem[]>([])
const discountItemData = ref<TDiscountItem>({
  id: '',
  discountgroupid: '',
  discountgroupname: '',
  mediaid: '',
  medianame: '',
  adindustryid: '',
  adindustryname: '',
  bcustomer: false,
  bagency: false,
  bagent: false,
  bvip: false,
  ndiscount: 0,
  sremark: ''
})
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<TDiscountItem>>({
  adindustryid: [{ required: true, message: '请选择行业', trigger: 'change' }],
  ndiscount: [{ required: true, message: '请填写折扣数', trigger: 'change' }]
})

onMounted(() => {
  getMediaDataCombo()
  queryInfo.discountgroupid = props.discountgroup.id
  queryInfo.discountgroupname = props.discountgroup.discountgroupname
  discountItemData.value.discountgroupid = props.discountgroup.id
  discountItemData.value.discountgroupname = props.discountgroup.discountgroupname
  nextTick(() => {
    loaddata()
  })
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight() - 160
})
/**
 * 监听传入对象编写
 */
watch(
  () => props.discountgroup,
  (val) => {
    queryInfo.discountgroupid = val.id
    queryInfo.discountgroupname = val.discountgroupname
    discountItemData.value.discountgroupid = val.id
    discountItemData.value.discountgroupname = val.discountgroupname
    loaddata()
  },
  { deep: true }
)
/**
 * @description: 获取媒体下拉
 * @return {*}
 */
const getMediaDataCombo = () => {
  getMediaDataComboApi().then((res: IAxios<IDataCombo[]>) => {
    if (res.success) {
      mediaCombo.value = res.obj
    }
  })
}
/**
 * 行业选择
 * @param val
 */
const onSelectionindustry = (val: TSelectZtree[]) => {
  if (val.length !== 0) {
    discountItemData.value.adindustryid = val[0].id
    discountItemData.value.adindustryname = val[0].name
  } else {
    discountItemData.value.adindustryid = ''
    discountItemData.value.adindustryname = ''
  }
}
/**
 * 行业查询
 */
const querySelectionindustry = (val: TSelectZtree[]) => {
  if (val.length !== 0) {
    queryInfo.adindustryid = val[0].id
    queryInfo.adindustryname = val[0].name
  } else {
    queryInfo.adindustryid = ''
    queryInfo.adindustryname = ''
  }
  loaddata()
}
/**
 * 获取折扣总表列表
 */
const loaddata = () => {
  getDiscountItemListApi(queryInfo).then(({ obj, total }: IAxios<TDiscountItem[]>) => {
    pageTotal.value = total
    discountItemList.value = obj
  })
}
/**
 * 选择
 * @param rows
 */
const handleSelectionChange = (rows: TDiscountItem[]) => {
  discountItemSelection.value = rows
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
  discountItemData.value.mediaid = mediaCombo.value[0].id
  discountItemData.value.medianame = mediaCombo.value[0].name
  dialogVisible.value = true
}
/**
 * 修改
 * @param row
 */
const handleUpdate = (row: TDiscountItem) => {
  discountItemData.value = { ...row }
  dialogVisible.value = true
}
/**
 * 保存
 */
const handleSave = () => {
  discountItemData.value.medianame = mediaCombo.value.find(
    (item) => item.id === discountItemData.value.mediaid
  )?.name
  discountItemFormRef.value?.validate((valid) => {
    if (valid) {
      if (!discountItemData.value.id) {
        saveDiscountItemApi(discountItemData.value).then(({ success, msg }: IAxios) => {
          if (success) {
            modal.msgSuccess('操作成功')
          } else {
            modal.msgError(msg)
          }
          loaddata()
        })
      } else {
        updateDiscountItemApi(discountItemData.value).then(({ success, msg }: IAxios) => {
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
const handleDelete = (row?: TDiscountItem) => {
  modal.confirm('是否删除?').then(() => {
    const ids = []
    if (row) {
      ids.push(row.id)
    } else {
      discountItemSelection.value?.forEach((item) => ids.push(item.id))
    }
    if (!ids.length) {
      modal.msgWarning('请选择删除记录')
      return
    }
    deleteDiscountItemApi(ids.join(',')).then(({ success, msg }: IAxios) => {
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
  discountItemFormRef.value?.resetFields()
  reset()
  loaddata()
  dialogVisible.value = false
  setTimeout(() => {
    discountItemFormRef.value?.clearValidate(['adindustryid'])
  }, 10)
}
/** 重置*/
const reset = () => {
  discountItemData.value = {
    id: '',
    discountgroupid: '',
    discountgroupname: '',
    mediaid: '',
    medianame: '',
    adindustryid: '',
    adindustryname: '',
    bcustomer: false,
    bagency: false,
    bagent: false,
    bvip: false,
    ndiscount: 0,
    sremark: ''
  }
}
</script>
<style scoped></style>
