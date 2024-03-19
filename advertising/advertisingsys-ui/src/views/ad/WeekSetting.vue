<!--
 * @Author: songly
 * @Date: 2023-08-28 16:28:00
 * @LastEditTime: 2024-02-27 09:25:23
 * @LastEditors: songly
 * @Description: 星期定义
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
        <el-button type="danger" icon="Delete" @click="handleDelete()">删除</el-button>
        <el-input
          v-model="queryInfo.queryKey"
          placeholder="请输星期定义名称"
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
          :data="weeksettingList"
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
          <el-table-column prop="sname" label="名称" sortable="custom"> </el-table-column>
          <el-table-column prop="svalue" label="数值" :width="360"> </el-table-column>
          <el-table-column prop="isort" label="序号" :width="80" sortable="custom" align="center">
          </el-table-column>
          <el-table-column prop="buse" label="启用" :width="80" sortable="custom" align="center">
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
      title="星期定义编辑"
      width="700px"
      align-center
      :close-on-click-modal="false"
    >
      <el-form ref="weeksettingFormRef" :model="weeksetting" label-width="100px" :rules="rules">
        <el-row :gutter="50">
          <el-col :span="22">
            <el-form-item label="序号" prop="isort">
              <el-input-number
                v-model="weeksetting.isort"
                :min="1"
                :max="99999"
                controls-position="right"
              />
            </el-form-item>
            <el-form-item label="名称" prop="sname">
              <el-input v-model="weeksetting.sname" />
            </el-form-item>
            <el-form-item label="启用" prop="buse">
              <el-checkbox v-model="weeksetting.buse" />
            </el-form-item>
            <el-form-item label="数值" prop="svalue">
              <ul>
                <li>
                  <el-checkbox v-model="checkedValue1" true-label="1" label="星期一" size="large" />
                  <el-checkbox v-model="checkedValue5" true-label="5" label="星期五" size="large" />
                </li>
                <li>
                  <el-checkbox v-model="checkedValue2" label="星期二" true-label="2" size="large" />
                  <el-checkbox v-model="checkedValue6" label="星期六" true-label="6" size="large" />
                </li>
                <li>
                  <el-checkbox v-model="checkedValue3" label="星期三" true-label="3" size="large" />
                  <el-checkbox v-model="checkedValue7" label="星期日" true-label="7" size="large" />
                </li>
                <li>
                  <el-checkbox v-model="checkedValue4" label="星期四" true-label="4" size="large" />
                </li>
              </ul>
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
import type { TWeekSetting } from '@/types/views/ad/weeksetting'
import type { TQueryInfo } from '@/types/common/index'
import {
  getWeekSettingListApi,
  getWeekSettingMaxSortApi,
  saveWeekSettingApi,
  updateWeekSettingApi,
  deleteWeekSettingByIdApi
} from '@/api/ad/weeksetting'
import { required, computTableHeight, tableHeaderColor } from '@/utils/index'
import type { IAxios } from 'axios'
import modal from '@/plugins/modal'
/** 星期一值 */
const checkedValue1 = ref('')
const checkedValue2 = ref('')
const checkedValue3 = ref('')
const checkedValue4 = ref('')
const checkedValue5 = ref('')
const checkedValue6 = ref('')
const checkedValue7 = ref('')

defineOptions({
  name: 'WeekSetting'
})
import { useRouter } from 'vue-router'
const router = useRouter()

/** 表格高度 */
const tableheight = ref(0)
/** 编辑 dialog */
const dialogVisible = ref(false)
/** 星期定义列表 */
const weeksettingList = ref<TWeekSetting[]>([])
/** 星期定义 */
const weeksetting = ref<TWeekSetting>({
  id: undefined,
  sname: '',
  isort: 0,
  svalue: '',
  buse: true
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
  queryKey: ''
})

const weeksettingFormRef = ref<FormInstance>()
const weeksettingSelection = ref<TWeekSetting[]>()

/**
 * 校验设置参数
 */
const rules = reactive<FormRules<TWeekSetting>>({
  sname: [
    { validator: required, required: true, message: '请输入名称', trigger: 'blur' },
    { max: 20, message: '不得大于20个字符', trigger: 'blur' }
  ]
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
const handleSelectionChange = (rows: TWeekSetting[]) => {
  weeksettingSelection.value = rows
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
  const data: TQueryInfo = {
    sort: queryInfo.value.sort,
    order: queryInfo.value.order,
    pageSize: pageSize.value,
    page: currentPage.value,
    queryKey: queryInfo.value.queryKey
  }

  getWeekSettingListApi(data).then(({ obj }: IAxios<TWeekSetting[]>) => {
    console.log(obj)
    weeksettingList.value = obj
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
  getWeekSettingMaxSortApi().then(({ success, obj }: IAxios<number>) => {
    weeksetting.value.isort = obj
  })
}
/**
 * 新增
 */
const handleAdd = () => {
  weeksetting.value = {
    sname: '',
    isort: 0,
    svalue: '',
    buse: true
  }
  checkedValue1.value = ''
  checkedValue2.value = ''
  checkedValue3.value = ''
  checkedValue4.value = ''
  checkedValue5.value = ''
  checkedValue6.value = ''
  checkedValue7.value = ''

  getMaxSort()
  dialogVisible.value = true
}
/**
 * 修改
 * @param row
 */
const handleUpdate = (row: TWeekSetting) => {
  weeksetting.value = { ...row }
  checkedValue1.value = ''
  checkedValue2.value = ''
  checkedValue3.value = ''
  checkedValue4.value = ''
  checkedValue5.value = ''
  checkedValue6.value = ''
  checkedValue7.value = ''
  if (weeksetting.value.svalue != null) {
    const selvalue = weeksetting.value.svalue
    if (selvalue.indexOf('1') >= 0) checkedValue1.value = '1'
    if (selvalue.indexOf('2') >= 0) checkedValue2.value = '2'
    if (selvalue.indexOf('3') >= 0) checkedValue3.value = '3'
    if (selvalue.indexOf('4') >= 0) checkedValue4.value = '4'
    if (selvalue.indexOf('5') >= 0) checkedValue5.value = '5'
    if (selvalue.indexOf('6') >= 0) checkedValue6.value = '6'
    if (selvalue.indexOf('7') >= 0) checkedValue7.value = '7'
  }
  dialogVisible.value = true
}
/**
 * 保存
 */
const handleSave = () => {
  weeksettingFormRef.value?.validate((valid) => {
    if (valid) {
      let slevalue = ''
      if (checkedValue1.value !== '') {
        slevalue += ';' + checkedValue1.value
      }
      if (checkedValue2.value !== '') {
        slevalue += ';' + checkedValue2.value
      }
      if (checkedValue3.value !== '') {
        slevalue += ';' + checkedValue3.value
      }
      if (checkedValue4.value !== '') {
        slevalue += ';' + checkedValue4.value
      }
      if (checkedValue5.value !== '') {
        slevalue += ';' + checkedValue5.value
      }
      if (checkedValue6.value !== '') {
        slevalue += ';' + checkedValue6.value
      }
      if (checkedValue7.value !== '') {
        slevalue += ';' + checkedValue7.value
      }

      if (slevalue.length > 0) slevalue = slevalue.substring(1)

      weeksetting.value.svalue = slevalue
      if (!weeksetting.value.id) {
        saveWeekSettingApi(weeksetting.value).then(({ success, msg }: IAxios) => {
          if (success) {
            modal.msgSuccess('操作成功')
          } else {
            // modal.msgError(msg)
          }
          loaddata()
        })
      } else {
        updateWeekSettingApi(weeksetting.value).then(({ success, msg }: IAxios) => {
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
    weeksettingFormRef.value?.clearValidate(['sname'])
  }, 10)
}
/**
 * 删除
 * @param row
 */
const handleDelete = (row?: TWeekSetting) => {
  modal.confirm('是否删除?').then(() => {
    const ids = []
    if (row) {
      ids.push(row.id)
    } else {
      weeksettingSelection.value?.forEach((color) => ids.push(color.id))
    }
    if (!ids.length) {
      modal.msgWarning('请选择删除记录')
      return
    }
    deleteWeekSettingByIdApi(ids.join(',')).then(({ success, msg }: IAxios) => {
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
  const to = '/template/Help?name=' + 'weeksetting'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>

<style scoped>
ul {
  padding-left: 10px;
}
</style>
