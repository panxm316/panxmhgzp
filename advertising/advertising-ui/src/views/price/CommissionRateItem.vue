<!--
 * @Author: suny
 * @Date: 2023-11-29 12:49:54
 * @LastEditTime: 2024-01-17 15:06:42
 * @LastEditors: wanghl
 * @Description: 计提比例明细
-->
<template>
  <div>
    <el-space wrap :size="20" style="padding-left: 12px">
      <!-- 叠次 -->
      <el-card class="cardbox">
        <div>
          <span>媒体</span>
          <el-button
            v-if="commissionRateItemForm.mediaid !== ''"
            :selectids="commissionRateItemForm.mediaid ? [commissionRateItemForm.mediaid] : []"
            type="primary"
            link
            @click="onMediaCancel"
            >取消</el-button
          >
        </div>
        <HgZtree
          :scopeflag="EHgDeptZtreeUrl.MEDIA_GETSYSMEDIATREE"
          :selectids="commissionRateItemForm.mediaid ? [commissionRateItemForm.mediaid] : []"
          :treeheight="300"
          @selectionztree="qSelectMedia"
        ></HgZtree>
      </el-card>
      <!-- 行业 -->
      <el-card class="cardbox">
        <div>
          <span>行业</span>
          <el-button
            v-if="commissionRateItemForm.adindustryid !== ''"
            type="primary"
            link
            @click="onAdindustryCancel"
            >取消</el-button
          >
        </div>
        <HgZtree
          :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYINDUSTRY"
          :selectids="
            commissionRateItemForm.adindustryid ? [commissionRateItemForm.adindustryid] : []
          "
          :treeheight="300"
          @selectionztree="qSelectionindustry"
        ></HgZtree>
      </el-card>
      <!-- 部门 -->
      <el-card class="cardbox">
        <div>
          <span>部门</span>
          <el-button
            v-if="commissionRateItemForm.deptid !== ''"
            type="primary"
            link
            @click="onDeptCancel"
            >取消</el-button
          >
        </div>
        <HgZtree
          :scopeflag="EHgDeptZtreeUrl.DEPT_GETDEPTTREE"
          :selectids="commissionRateItemForm.deptid ? [commissionRateItemForm.deptid] : []"
          :treeheight="300"
          :filterable="true"
          :treeparams="{ bIgnorePermissions: true }"
          @selectionztree="onSelectionDept"
        ></HgZtree>
      </el-card>
      <!-- 人员 -->
      <el-card class="cardbox">
        <div>
          <span>人员</span>
          <el-button
            v-if="commissionRateItemForm.employid !== ''"
            type="primary"
            link
            @click="onEmpCancel"
            >取消</el-button
          >
        </div>
        <HgZtree
          :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYPERSON"
          :selectids="commissionRateItemForm.employid ? [commissionRateItemForm.employid] : []"
          :treeheight="300"
          :filterable="true"
          :treeparams="{ bIgnorePermissions: true }"
          @selectionztree="onSelectionEmp"
        ></HgZtree>
      </el-card>
    </el-space>

    <div>
      <el-form
        ref="commissionRateItemRef"
        :inline="true"
        :model="commissionRateItemForm"
        :rules="rules"
        style="margin-left: 10px"
      >
        <el-form-item label="直接客户提成" prop="nrateofcustomer">
          <el-input-number
            v-model="commissionRateItemForm.nrateofcustomer"
            :controls="false"
            style="width: 100px"
            :min="0"
            :max="100"
          />%
        </el-form-item>
        <el-form-item label="代理公司提成" prop="nrateofagency">
          <el-input-number
            v-model="commissionRateItemForm.nrateofagency"
            :controls="false"
            style="width: 100px"
            :min="0"
            :max="100"
          />%
        </el-form-item>
        <el-form-item label="内容生产方提成" prop="nrateofagent">
          <el-input-number
            v-model="commissionRateItemForm.nrateofagent"
            :controls="false"
            style="width: 100px"
            :min="0"
            :max="100"
          />%
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="Plus" @click="handleAdd(commissionRateItemRef)"
            >新增</el-button
          >
          <el-button type="success" icon="Edit" @click="onEdit(commissionRateItemRef)"
            >修改</el-button
          >
          <el-button type="danger" icon="Delete" @click="onDelete()">删除</el-button>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button type="primary" icon="Close" @click="handleCancel">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="table_box">
      <el-table
        ref="dataTableRef"
        :data="commissionRateItemList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        :border="true"
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <el-table-column type="selection" prop="ischeck" width="50" />
        <el-table-column
          label="组名称"
          prop="commissionrategroupname"
          min-width="140"
          show-overflow-tooltip
        />
        <el-table-column label="媒体名称" prop="medianame" width="120" show-overflow-tooltip />
        <el-table-column label="行业名称" prop="adindustryname" width="120" show-overflow-tooltip />
        <el-table-column label="部门名称" prop="deptname" width="120" show-overflow-tooltip />
        <el-table-column label="人员名称" prop="employname" width="120" show-overflow-tooltip />
        <el-table-column label="直接客户" prop="nrateofcustomer" width="100" align="center">
          <template #default="scope"> {{ scope.row.nrateofcustomer }}% </template>
        </el-table-column>
        <el-table-column label="代理公司" prop="nrateofagency" width="100" align="center">
          <template #default="scope"> {{ scope.row.nrateofagency }}% </template>
        </el-table-column>
        <el-table-column label="内容生产方" prop="nrateofagent" width="100" align="center">
          <template #default="scope"> {{ scope.row.nrateofagent }}% </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="120"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              link
              style="margin-left: 10px"
              type="success"
              icon="Edit"
              size="small"
              title="选择"
              @click="onSelect(scope.row)"
              >选择</el-button
            >
            <el-button
              link
              type="danger"
              icon="Delete"
              size="small"
              title="删除"
              @click="onDelete(scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="queryParams.page"
        class="pagination"
        style="margin-left: 10px; width: 100%"
        :page-sizes="pageSizes"
        :page-size="queryParams.pageSize"
        small
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>
<script lang="ts" setup>
import { EHgDeptZtreeUrl } from '@/types/common/enumindex'
import { computTableHeight, required, tableHeaderColor } from '@/utils'
import { IAxios } from 'axios'
import { FormInstance, FormRules } from 'element-plus'
import {
  ITbcommissionrateitem,
  TCommissionRateItemQuery
} from '@/types/views/price/commissionrateitem'
import { ITbcommissionrategroup } from '@/types/views/price/commissionrategroup'
import {
  deleteCommissionRateItemApi,
  getCommissionRateItemPageListApi,
  saveCommissionRateItemApi,
  updateCommissionRateItemApi
} from '@/api/price/commissionrateitem'
import { IDataCombo } from '@/types/common/DataCombo'
import { TSelectZtree } from '@/types/common'

defineOptions({
  name: 'CommissionRateItem'
})
interface Props {
  commissionrategroup: ITbcommissionrategroup
}
const props = defineProps<Props>()

const total = ref(0)
const sort = ref('mediaid')
const order = ref('asc')
/** 每页选择下拉 */
const pageSizes = ref<number[]>([20, 40, 60, 100])
/** Form名称 */
const commissionRateItemRef = ref<FormInstance>()
/** 查询条件 */
const queryParams = reactive<TCommissionRateItemQuery>({
  page: 1,
  pageSize: 20,
  commissionrategroupid: props.commissionrategroup.id,
  queryKey: ''
})
/** Form表单数据 */
const commissionRateItemFormInfo: ITbcommissionrateitem = {
  id: '',
  commissionrategroupname: props.commissionrategroup.commissionrategroupname,
  nrateofagent: 0,
  nrateofagency: 0,
  nrateofcustomer: 0,
  commissionrategroupid: props.commissionrategroup.id,
  mediaid: '',
  medianame: '',
  adindustryid: '',
  adindustryname: '',
  deptid: '',
  deptname: ''
}
/** Form表单数据 */
const commissionRateItemForm = ref<ITbcommissionrateitem>({ ...commissionRateItemFormInfo })
/** 表格高度 */
const tableheight = ref(0)

/** 列表对象 */
const commissionRateItemList = ref<ITbcommissionrateitem[]>([])
/** Form选中的行 */
const multipleSelection = ref<ITbcommissionrateitem[]>([])
/** 媒体下拉列表 */
const mediaList = ref<IDataCombo[]>([])
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<ITbcommissionrateitem>>({
  mediaid: [{ validator: required, required: true, message: '请选择媒体', trigger: 'change' }],
  adindustryid: [{ validator: required, required: true, message: '请选择行业', trigger: 'change' }],
  deptid: [{ validator: required, required: true, message: '请选择部门', trigger: 'change' }]
})

watch(
  () => props.commissionrategroup.id,
  (val) => {
    if (val) {
      queryParams.commissionrategroupid = val
      handleQuery()
    }
  }
)
onMounted(() => {
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight() - 300
})
/**
 * 查询时查看媒体
 * @param val
 */
const qSelectMedia = (val: TSelectZtree[]) => {
  const mediaid = val.map((item) => item.id).join(',')
  const medianame = val.map((item) => item.name).join(',')
  commissionRateItemForm.value.mediaid = mediaid
  commissionRateItemForm.value.medianame = medianame
  queryParams.mediaid = mediaid
}
/**
 * 取消媒体选择
 */
const onMediaCancel = () => {
  commissionRateItemForm.value.mediaid = ''
  commissionRateItemForm.value.medianame = ''
}
/**
 * 查询时查看行业
 * @param val
 */
const qSelectionindustry = (val: TSelectZtree[]) => {
  const adindustryid = val.map((item) => item.id).join(',')
  const adindustryname = val.map((item) => item.name).join(',')
  commissionRateItemForm.value.adindustryid = adindustryid
  commissionRateItemForm.value.adindustryname = adindustryname
  queryParams.adindustryid = adindustryid
}
/**
 * 取消行业选择
 */
const onAdindustryCancel = () => {
  commissionRateItemForm.value.adindustryid = ''
  commissionRateItemForm.value.adindustryname = ''
}
/**
 * 查询
 */
const handleQuery = () => {
  const data = {
    sort: sort.value,
    order: order.value,
    ...queryParams
  }
  getCommissionRateItemPageListApi(data)
    .then((res: IAxios<ITbcommissionrateitem[]>) => {
      if (res.success) {
        commissionRateItemList.value = res.obj
        total.value = res.total
      }
    })
    .finally(() => {})
}
/**
 * 取消选择
 */
const handleCancel = () => {
  commissionRateItemForm.value = { ...commissionRateItemFormInfo }
}
/**
 * 选择部门
 * @param val
 */
const onSelectionDept = (val: TSelectZtree[]) => {
  const deptid = val.map((item) => item.id).join(',')
  const deptname = val.map((item) => item.name).join(',')
  commissionRateItemForm.value.deptid = deptid
  commissionRateItemForm.value.deptname = deptname
  queryParams.deptid = deptid
}
/**
 * 取消部门选择
 */
const onDeptCancel = () => {
  commissionRateItemForm.value.deptid = ''
  commissionRateItemForm.value.deptname = ''
}
/**
 * 选择人员
 * @param val
 */
const onSelectionEmp = (val: TSelectZtree[]) => {
  const employid = val.map((item) => item.id).join(',')
  const employname = val.map((item) => item.name).join(',')
  commissionRateItemForm.value.employid = employid
  commissionRateItemForm.value.employname = employname
  queryParams.employid = employid
}
/**
 * 取消人员选择
 */
const onEmpCancel = () => {
  commissionRateItemForm.value.employid = ''
  commissionRateItemForm.value.employname = ''
}
/**
 * 新增
 */
const handleAdd = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      ElMessageBox.confirm('是否保存新增？', '提示', {
        type: 'warning'
      })
        .then(() => {
          commissionRateItemForm.value.id = ''
          mediaList.value.forEach((item) => {
            if (item.id === commissionRateItemForm.value.mediaid) {
              commissionRateItemForm.value.medianame = item.name
            }
          })
          saveCommissionRateItemApi(commissionRateItemForm.value)
            .then((res: IAxios) => {
              if (res.success) {
                ElMessage.success('保存成功')
                commissionRateItemList.value.push(res.obj)
                total.value = total.value + 1
              }
            })
            .catch(() => {})
        })
        .catch(() => {})
    } else {
      console.log('error submit!', fields)
    }
  })
}
/**
 * 修改保存事件
 * @param row
 */
const onEdit = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      ElMessageBox.confirm('是否保存修改？', '提示', {
        type: 'warning'
      })
        .then(() => {
          updateCommissionRateItemApi(commissionRateItemForm.value)
            .then((res: IAxios) => {
              if (res.success) {
                ElMessage.success('更新保存成功')
                commissionRateItemList.value.forEach((item) => {
                  if (item.id === commissionRateItemForm.value.id) {
                    Object.assign(item, commissionRateItemForm.value)
                  }
                })
              }
            })
            .catch(() => {})
        })
        .catch(() => {})
    } else {
      console.log('error submit!', fields)
    }
  })
}
/**
 * 选中按钮事件
 */
const onSelect = (row: ITbcommissionrateitem) => {
  commissionRateItemForm.value = { ...row }
}
/**
 * 删除按钮事件
 * @param row
 */
const onDelete = (row?: ITbcommissionrateitem) => {
  const ids: string[] = []
  if (row === undefined) {
    multipleSelection.value.forEach((item) => {
      ids.push(item.id?.toString() as string)
    })
  } else {
    ids.push(row.id?.toString() as string)
  }

  if (ids.length === 0) {
    ElMessage.warning('请选择要删除的数据')
    return
  }
  const data = {
    ids: ids.join(',')
  }
  ElMessageBox.confirm('是否删除选择的数据？', '提示', {
    type: 'warning'
  })
    .then(() => {
      deleteCommissionRateItemApi(data)
        .then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('删除成功!')
            handleQuery()
          }
        })
        .catch(() => {})
    })
    .catch(() => {})
}
/**
 * 重置form表单
 * @param formEl
 */
const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}
/**
 * 改变页码总数时
 */
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  handleQuery()
}
/**
 * 改变页数时
 * @param val
 */
const handleCurrentChange = (val: number) => {
  queryParams.page = val
  handleQuery()
}
/**
 * 行选择事件
 * @param val
 */
const handleSelectionChange = (val: ITbcommissionrateitem[]) => {
  multipleSelection.value = val
}
/**
 * 排序
 * @param val
 */
const handleSortChange = (val: { prop: string; order: string }) => {
  queryParams.sort = val.prop

  if (val.order === 'ascending') {
    queryParams.order = 'asc'
  } else if (val.order === 'descending') {
    queryParams.order = 'desc'
  } else {
    queryParams.order = ''
  }
  handleQuery()
}
</script>
<style lang="scss" scoped>
.cardbox {
  //   width: 180px;
  height: 340px;
  margin-bottom: 10px;
}
.el-form-item {
  margin-right: 10px;
}
.el-input-number {
  padding-right: 5px;
}
</style>
