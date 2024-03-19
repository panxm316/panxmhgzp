<!--
 * @Author: peij
 * @Date: 2023-08-24 13:13:29
 * @LastEditTime: 2024-01-24 15:08:44
 * @LastEditors: songly
 * @Description: 广告色彩 报刊paper类型的不允许操作固定值
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button v-if="props.type !== 'paper'" type="primary" icon="Plus" @click="handleAdd"
          >新增</el-button
        >
        <el-button v-if="props.type !== 'paper'" type="danger" icon="Delete" @click="handleDelete()"
          >删除</el-button
        >
        <el-input
          v-model="queryInfo.queryKey"
          placeholder="请输入关键字"
          clearable
          style="width: 200px"
          @clear="getAdcolorList"
          @keyup.enter="getAdcolorList"
        />
        <el-button type="primary" icon="Search" @click="getAdcolorList">搜索</el-button>
        <el-tooltip class="item" effect="dark" content="帮助" placement="top">
          <el-button class="help" @click="showhelp()">?</el-button>
        </el-tooltip>
      </div>
    </div>
    <div class="table_box">
      <el-table
        :data="adcolorList"
        row-key="id"
        :height="props.tableheight + 30"
        :border="true"
        stripe
        :header-cell-style="tableHeaderColor"
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <el-table-column type="selection" prop="ischeck" width="50" align="center">
        </el-table-column>
        <el-table-column prop="sname" label="名称" sortable="custom"> </el-table-column>
        <el-table-column prop="mediatypename" label="媒体类型" sortable="custom"> </el-table-column>
        <el-table-column prop="sremark" label="备注" :width="360"> </el-table-column>
        <el-table-column prop="isort" label="序号" :width="80" sortable="custom" align="center">
        </el-table-column>
        <el-table-column prop="buse" label="启用" :width="80" sortable="custom" align="center">
          <template #default="scope">
            <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column
          v-if="props.type !== 'paper'"
          label="操作"
          align="center"
          width="210"
          class-name="small-padding fixed-width"
          fixed-width
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
    </div>
    <el-dialog
      v-model="dialogVisible"
      title="编辑"
      width="700px"
      align-center
      :close-on-click-modal="false"
    >
      <el-form ref="adcolorFormRef" :model="adcolor" label-width="100px" :rules="rules">
        <el-row :gutter="50">
          <el-col :span="22">
            <el-form-item label="名称" prop="sname">
              <el-input v-model="adcolor.sname" />
            </el-form-item>
            <el-form-item label="媒体类型" prop="mediatypekey">
              <el-select
                v-model="adcolor.mediatypekey"
                placeholder="媒体类型"
                disabled
                style="width: 150px"
              >
                <el-option
                  v-for="item in mediaTypeCombo"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="备注" prop="sremark">
              <el-input v-model="adcolor.sremark" type="textarea" :maxlength="200" />
            </el-form-item>
            <el-form-item label="序号" prop="isort">
              <el-input-number v-model="adcolor.isort" :min="1" controls-position="right" />
            </el-form-item>
            <el-form-item label="启用" prop="buse">
              <el-checkbox v-model="adcolor.buse" />
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
import type { TAdcolor } from '@/types/views/price/adcolor'
import type { TQueryInfo } from '@/types/common/index'
import {
  getAdcolorListApi,
  getMaxSortApi,
  saveAdcolorApi,
  updateAdcolor,
  deleteAdcolorApi
} from '@/api/price/adcolor'
import { required, computTableHeight, tableHeaderColor } from '@/utils/index'
import type { IAxios } from 'axios'
import modal from '@/plugins/modal'

defineOptions({
  name: 'Adcolor'
})
import { useRouter } from 'vue-router'
import { IDataCombo } from '@/types/common/DataCombo'
import { getMediaTypeComboApi } from '@/api/media/mediatype'
type Props = {
  /** 传入的媒体类型key */
  type: string
  /** 传入的关键字名称 */
  typename: string
  /** 传入表格高度 */
  tableheight: number
}
const props = defineProps<Props>()
const router = useRouter()
/** 编辑 dialog */
const dialogVisible = ref(false)
/** 广告色彩列表 */
const adcolorList = ref<TAdcolor[]>([])
/** 广告色彩 */
const adcolor = ref<TAdcolor>({
  sname: '',
  isort: 0,
  sremark: '',
  mediatypekey: props.type,
  mediatypename: '',
  buse: true
})
/** 查询对象 */
const queryInfo = ref<TQueryInfo & { mediatypekey?: string }>({
  sort: 'isort',
  order: 'asc',
  queryKey: ''
})

const adcolorFormRef = ref<FormInstance>()
const adcolorSelection = ref<TAdcolor[]>()
/** 媒体类别下拉列表 */
const mediaTypeCombo = ref<IDataCombo[]>()
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<TAdcolor>>({
  sname: [
    { validator: required, required: true, message: '请输入名称', trigger: 'blur' },
    { max: 20, message: '不得大于20个字符', trigger: 'blur' }
  ],
  mediatypekey: [{ required: true, message: '请选择媒体类型', trigger: 'change' }]
})

onMounted(() => {
  getAdcolorList()
  getMediaType()
})

/**
 * 选择
 * @param rows
 */
const handleSelectionChange = (rows: TAdcolor[]) => {
  adcolorSelection.value = rows
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
  getAdcolorList()
}
/**
 * 获取媒体类型下拉列表
 */
const getMediaType = () => {
  getMediaTypeComboApi().then((res: IAxios<IDataCombo[]>) => {
    if (res.success) {
      mediaTypeCombo.value = res.obj
    }
  })
}
/**
 * 获取色彩列表
 */
const getAdcolorList = () => {
  const data = queryInfo.value
  data.mediatypekey = props.type
  getAdcolorListApi(data).then(({ obj }: IAxios<TAdcolor[]>) => {
    console.log(obj)
    adcolorList.value = obj
  })
}
/**
 * 获取序号最大值
 */
const getMaxSort = () => {
  getMaxSortApi().then(({ success, obj }: IAxios<number>) => {
    adcolor.value.isort = obj
  })
}
/**
 * 新增
 */
const handleAdd = () => {
  adcolor.value = {
    sname: '',
    isort: 0,
    sremark: '',
    mediatypekey: props.type,
    mediatypename: '',
    buse: true
  }
  getMaxSort()
  dialogVisible.value = true
}
/**
 * 修改
 * @param row
 */
const handleUpdate = (row: TAdcolor) => {
  adcolor.value = { ...row }
  dialogVisible.value = true
}
/**
 * 保存
 */
const handleSave = () => {
  adcolorFormRef.value?.validate((valid) => {
    if (valid) {
      adcolor.value.mediatypename = mediaTypeCombo.value?.find(
        (item) => item.id === adcolor.value.mediatypekey
      )?.name
      if (!adcolor.value.id) {
        saveAdcolorApi(adcolor.value).then(({ success, msg }: IAxios) => {
          if (success) {
            modal.msgSuccess('操作成功')
          } else {
            modal.msgError(msg)
          }
          getAdcolorList()
        })
      } else {
        updateAdcolor(adcolor.value).then(({ success, msg }: IAxios) => {
          if (success) {
            modal.msgSuccess('操作成功')
          } else {
            modal.msgError(msg)
          }
          getAdcolorList()
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
    adcolorFormRef.value?.clearValidate(['sname'])
  }, 10)
}
/**
 * 删除
 * @param row
 */
const handleDelete = (row?: TAdcolor) => {
  modal.confirm('是否删除?').then(() => {
    const ids = []
    if (row) {
      ids.push(row.id)
    } else {
      adcolorSelection.value?.forEach((color) => ids.push(color.id))
    }
    if (!ids.length) {
      modal.msgWarning('请选择删除记录')
      return
    }
    deleteAdcolorApi(ids.join(',')).then(({ success, msg }: IAxios) => {
      if (success) {
        modal.msgSuccess('操作成功')
      } else {
        modal.msgError(msg)
      }
      getAdcolorList()
    })
  })
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'adcolor'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>

<style scoped></style>
