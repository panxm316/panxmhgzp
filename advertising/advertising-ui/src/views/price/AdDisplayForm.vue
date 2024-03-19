<!--
 * @Author: suny
 * @Date: 2023-11-06 09:34:30
 * @LastEditTime: 2023-11-09 08:37:29
 * @LastEditors: wanghl
 * @Description: 广告形式管理
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <el-button type="primary" icon="Plus" @click="onAdd">新增</el-button>
      <el-button type="danger" icon="delete" @click="onDelete(undefined)">删除</el-button>
      <!-- <el-select
        v-model="queryData.mediatypekey"
        placeholder="媒体类型"
        style="width: 150px"
        clearable
        @change="onSearch"
      >
        <el-option
          v-for="item in mediaTypeCombo"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        />
      </el-select> -->
      <el-input
        v-model="queryData.queryKey"
        clearable
        placeholder="请输入广告形式关键字"
        class="input-with-select"
        style="width: 200px"
        @keyup.enter="onSearch"
        @clear="onSearch"
      >
      </el-input>
      <el-button type="primary" icon="Search" @click="onSearch">搜索</el-button>
    </div>
    <div class="table_box">
      <el-table
        :data="adDisplayFormList"
        row-key="id"
        :height="props.tableheight"
        :border="true"
        stripe
        :header-cell-style="tableHeaderColor"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column prop="sname" label="广告形式名称"> </el-table-column>
        <el-table-column prop="mediatypename" label="所属类型" :width="150"> </el-table-column>
        <el-table-column prop="isort" label="序号 " :width="60" align="center"> </el-table-column>
        <el-table-column prop="buse" label="启用" :width="60" align="center">
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
            <el-button link type="success" icon="Edit" @click="onEdit(scope.row)">修改</el-button>
            <el-button link type="danger" icon="Delete" @click="onDelete(scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <!-- 页码 -->
      <el-pagination
        v-model:current-page="pageIndex"
        v-model:page-size="pageSize"
        :page-sizes="pageSizes"
        class="pagination"
        small
        background
        :disabled="pageDisabled"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pageTotal"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <el-dialog v-model="editShow" title="广告形式编辑" width="680px" append-to-body>
      <el-form
        ref="adDisplayFormRef"
        :model="adDisplayForm"
        :rules="rules"
        label-width="100px"
        @selection-change="handleSelectionChange"
      >
        <el-row :gutter="50">
          <el-col :span="16">
            <!-- <el-form-item label="媒体类型" prop="mediatypekey">
              <el-select
                v-model="adDisplayForm.mediatypekey"
                placeholder="媒体类型"
                style="width: 150px"
              >
                <el-option
                  v-for="item in mediaTypeCombo"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item> -->
            <el-form-item label="形式名称" prop="sname">
              <el-input v-model="adDisplayForm.sname"></el-input>
            </el-form-item>
            <el-form-item label="序号" prop="isort">
              <el-input-number
                v-model="adDisplayForm.isort"
                :min="1"
                :max="1000"
                label="序号"
              ></el-input-number>
            </el-form-item>
            <el-form-item label="形式描述" prop="sremark">
              <el-input v-model="adDisplayForm.sremark" :rows="5" type="textarea"></el-input>
            </el-form-item>
            <el-row :gutter="50">
              <el-col :span="8">
                <el-form-item label="启用" prop="buse">
                  <el-checkbox v-model="adDisplayForm.buse"></el-checkbox>
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="onSave(adDisplayFormRef)">确 定</el-button>
          <el-button @click="onCancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { getMediaTypeComboApi } from '@/api/media/mediatype'
import {
  deleteAdDisplayFormByIdApi,
  getAdDisplayFormMaxSortApi,
  getAdDisplayFormPageListApi,
  saveAdDisplayFormApi,
  updateAdDisplayFormApi
} from '@/api/price/addisplayform'
import { IDataCombo } from '@/types/common/DataCombo'
import { ITbaddisplayform, TAdDisplayFormQuery } from '@/types/views/price/addisplayform'
import { computTableHeight, tableHeaderColor } from '@/utils'
import { IAxios } from 'axios'
import { FormInstance, FormRules } from 'element-plus'
import { ref } from 'vue'

type Props = {
  /** 传入的媒体类型key */
  type: string
  /** 传入的关键字名称 */
  typename: string
  /** 传入表格高度 */
  tableheight: number
}
const props = defineProps<Props>()

/** Form名称 */
const adDisplayFormRef = ref<FormInstance>()
/** Form选中的行 */
const multipleSelection = ref<ITbaddisplayform[]>([])
// 分页相关
const sort = ref('isort')
const order = ref('asc')
const pageSizes = [15, 20, 30, 40]
const pageIndex = ref(1)
const pageSize = ref(15)
const pageDisabled = ref(false)
/** 查询总页数 */
const pageTotal = ref(0)
/** 广告形式列表 */
const adDisplayFormList = ref<ITbaddisplayform[]>([])
/** 媒体类型下拉列表 */
const mediaTypeCombo = ref<IDataCombo[]>()
/** 广告形式编辑显示状态 */
const editShow = ref(false)
/** 查询对象 */
const queryInfo = {
  queryKey: '',
  mediatypekey: props.type
}
/** 查询实体 */
const queryData = ref<TAdDisplayFormQuery>({ ...queryInfo })
/** 广告形式实体对象 */
const adDisplayFormInfo = {
  id: '',
  sname: '',
  mediatypekey: props.type,
  mediatypename: '',
  buse: true,
  isort: 1
}
/** 广告形式编辑实体 */
const adDisplayForm = ref<ITbaddisplayform>({ ...adDisplayFormInfo })
// const adDisplayFormQueryForm = ref<>
/** 验证规则 */
const rules = reactive<FormRules<ITbaddisplayform>>({
  sname: [
    { required: true, message: '广告形式名称不能为空', trigger: 'blur' },
    { min: 1, max: 100, message: '广告形式名称长度不能大于100个字符', trigger: 'change' }
  ],
  mediatypekey: [{ required: true, message: '媒体类型不能为空', trigger: 'change' }],
  sremark: [{ min: 1, max: 200, message: '描述长度不能大于200个字符', trigger: 'blur' }]
})
onMounted(() => {
  getMediaType()
  getAdDisplayFormPageList()
})
/**
 * 获取媒体类型下拉列表
 */
const getMediaType = () => {
  getMediaTypeComboApi()
    .then((res: IAxios<IDataCombo[]>) => {
      if (res.success) {
        mediaTypeCombo.value = res.obj
      }
    })
    .catch(() => {})
}
/**
 * 查询事件
 */
const onSearch = () => {
  pageIndex.value = 1
  getAdDisplayFormPageList()
}

/**
 * 获取广告形式最大序号
 */
const getAdDisplayFormMaxSort = () => {
  getAdDisplayFormMaxSortApi().then((res: IAxios) => {
    if (res.success) {
      adDisplayForm.value.isort = res.obj
    }
  })
}

/**
 * 获取广告形式分页列表
 */
const getAdDisplayFormPageList = () => {
  const data: TAdDisplayFormQuery = {
    sort: sort.value,
    order: order.value,
    pageSize: pageSize.value,
    page: pageIndex.value,
    queryKey: queryData.value.queryKey,
    mediatypekey: queryData.value.mediatypekey
  }
  getAdDisplayFormPageListApi(data).then((res: IAxios<ITbaddisplayform[]>) => {
    if (res.success) {
      adDisplayFormList.value = res.obj
      pageTotal.value = res.total
    }
  })
}
/**
 * 新增按钮事件
 */
const onAdd = () => {
  adDisplayFormRef.value?.resetFields()
  clearForm()
  getAdDisplayFormMaxSort()
  adDisplayForm.value.mediatypekey = queryData.value.mediatypekey
  editShow.value = true
}
/**
 * 编辑广告形式按钮事件
 * @param row
 */
const onEdit = (row: ITbaddisplayform) => {
  editShow.value = true
  adDisplayForm.value = { ...row }
}
/**
 * 删除广告形式按钮事件
 * @param row
 */
const onDelete = (row: ITbaddisplayform | undefined) => {
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
  ElMessageBox.confirm('是否删除选择的广告形式？', '提示', {
    type: 'warning'
  })
    .then(() => {
      deleteAdDisplayFormByIdApi(data)
        .then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('删除成功!')
            getAdDisplayFormPageList()
          }
        })
        .catch(() => {})
    })
    .catch(() => {})
}
/**
 * 新增修改保存事件
 * @param formEl
 */
const onSave = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid, fields) => {
    if (valid) {
      if (adDisplayForm.value.sname?.trim() === '') {
        ElMessage.warning('广告形式名称不可为空')
        return
      }
      const node = mediaTypeCombo.value?.find((item) => {
        return item.id === adDisplayForm.value.mediatypekey
      })
      if (node === undefined) {
        ElMessage.warning('媒体类型不可为空')
        return
      }
      adDisplayForm.value.mediatypename = node.name
      if (adDisplayForm.value.id === '') {
        saveAdDisplayFormApi(adDisplayForm.value)
          .then((res: IAxios) => {
            if (res.success) {
              ElMessage.success('保存成功')
              getAdDisplayFormPageList()
              adDisplayFormRef.value?.resetFields()
              editShow.value = false
            }
          })
          .catch(() => {})
      } else {
        updateAdDisplayFormApi(adDisplayForm.value)
          .then((res: IAxios) => {
            if (res.success) {
              ElMessage.success('更新保存成功')
              getAdDisplayFormPageList()
              adDisplayFormRef.value?.resetFields()
              editShow.value = false
            }
          })
          .catch(() => {})
      }
    } else {
      console.log('保存失败!', fields)
    }
  })
}
/**
 * 取消编辑
 */
const onCancel = () => {
  adDisplayFormRef.value?.resetFields()
  clearForm()
  editShow.value = false
}
/**
 * 清空编辑form内容
 */
const clearForm = () => {
  adDisplayForm.value = { ...adDisplayFormInfo }
}
/**
 * 翻页
 * @param val
 */
const handleSizeChange = (val: number) => {
  // 分页当前页
  pageIndex.value = val
  getAdDisplayFormPageList()
}
/**
 * 当前页
 * @param val
 */
const handleCurrentChange = (val: number) => {
  pageIndex.value = val
  getAdDisplayFormPageList()
}
/**
 * 行选择事件
 * @param val
 */
const handleSelectionChange = (val: ITbaddisplayform[]) => {
  multipleSelection.value = val
}
</script>

<style scoped></style>
