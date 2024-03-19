<!--
 * @Author: suny
 * @Date: 2023-08-24 11:14:06
 * @LastEditTime: 2024-02-28 09:31:20
 * @LastEditors: songly
 * @Description: 媒体类型管理
-->
<template>
  <div id="MediaType" class="app-container">
    <div class="search_box">
      <div class="flex">
        <!-- <el-button type="primary" icon="Plus" @click="onAdd">新增</el-button>
        <el-button type="danger" icon="delete" @click="onDelete(undefined)">删除</el-button> -->
        <el-input
          v-model="queryKey"
          clearable
          placeholder="请输入类型名称"
          style="width: 200px"
          @keyup.enter="getMediaTypePageList"
          @clear="getMediaTypePageList"
        >
        </el-input>
        <el-button type="primary" icon="Search" @click="getMediaTypePageList">搜索</el-button>
        <el-tooltip class="item" effect="dark" content="帮助" placement="top">
          <el-button class="help" type="text" @click="showhelp()">?</el-button>
        </el-tooltip>
      </div>
    </div>
    <div class="table_box">
      <el-table
        :data="mediaTypeList"
        row-key="id"
        :height="tableHeight"
        :border="true"
        stripe
        :header-cell-style="tableHeaderColor"
        @selection-change="handleSelectionChange"
      >
        <!-- <el-table-column type="selection" width="50" align="center" /> -->
        <el-table-column prop="sname" label="类型名称" :width="150"> </el-table-column>
        <el-table-column prop="skey" label="类型关键字" :width="150"> </el-table-column>
        <el-table-column prop="sdesc" label="类型描述"> </el-table-column>
        <el-table-column prop="isort" label="序号 " :width="80" align="center"> </el-table-column>
        <el-table-column prop="buse" label="启用" :width="80" align="center">
          <template #default="scope">
            <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column prop="bdefault" label="默认" :width="60" align="center">
          <template #default="scope">
            <el-checkbox v-model="scope.row.bdefault" disabled></el-checkbox>
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
            <!-- <el-button link type="danger" icon="Delete" @click="onDelete(scope.row)"
              >删除</el-button
            > -->
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
        :disabled="pageDisabled"
        :background="background"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pageTotal"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <el-dialog v-model="editShow" title="类型编辑" width="700px" append-to-body>
      <el-form ref="mediaTypeRef" :model="mediaTypeForm" :rules="rules" label-width="100px">
        <el-row :gutter="50">
          <el-col :span="22">
            <el-form-item label="名称" prop="sname">
              <el-input
                v-model="mediaTypeForm.sname"
                :disabled="mediaTypeForm.id !== ''"
              ></el-input>
            </el-form-item>
            <el-form-item label="关键字" prop="skey">
              <el-input v-model="mediaTypeForm.skey" :disabled="true"></el-input>
            </el-form-item>
            <el-form-item label="序号" prop="isort">
              <el-input-number
                v-model="mediaTypeForm.isort"
                :min="1"
                :max="1000"
                label="序号"
              ></el-input-number>
            </el-form-item>
            <el-form-item label="类型描述" prop="sdesc">
              <el-input v-model="mediaTypeForm.sdesc" :rows="5" type="textarea"></el-input>
            </el-form-item>

            <el-row :gutter="50">
              <el-col :span="4">
                <el-form-item label="启用" prop="buse">
                  <el-checkbox v-model="mediaTypeForm.buse"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item label="默认" prop="bdefault">
                  <el-checkbox v-model="mediaTypeForm.bdefault"></el-checkbox>
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" icon="Check" @click="onSave(mediaTypeRef)">确 定</el-button>
          <el-button icon="Close" @click="onCancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { getMediaTypeMaxSortApi } from '@/api/media/mediatype'
import {
  getMediaTypePageListApi,
  saveMediaTypeApi,
  updateMediaTypeApi,
  deleteMediaTypeApi
} from '@/api/media/mediatype'
import { IBaseQueryInfo } from '@/types/common'
import { IMediaType, IMediaTypeQuery } from '@/types/views/media/mediatype'
import { deepClone } from '@/utils'
import { IAxios } from 'axios'
import { FormInstance, FormRules } from 'element-plus'
import { computTableHeight, tableHeaderColor } from '@/utils/index'
defineOptions({
  name: 'MediaType'
})
import { useRouter } from 'vue-router'
const router = useRouter()
/** Form名称 */
const mediaTypeRef = ref<FormInstance>()
/** Form选中的行 */
const multipleSelection = ref<IMediaType[]>([])
// 分页相关
const sort = ref('isort')
const order = ref('asc')
const pageSizes = [15, 20, 30, 40]
const pageIndex = ref(1)
const pageSize = ref(15)
const background = ref(false)
const pageDisabled = ref(false)
/** 查询总页数 */
const pageTotal = ref(0)

/** 表格高度 */
const tableHeight = ref<number>(0)
/** 类型列表 */
const mediaTypeList = ref<IMediaType[]>([])
/** 查询关键字 */
const queryKey = ref('')
/** 类型编辑显示状态 */
const editShow = ref(false)
/** 类型编辑实体 */
const mediaTypeForm = ref<IMediaType>({
  id: '',
  sname: '',
  skey: '',
  sdesc: '',
  buse: true,
  bdefault: false,
  isort: 1
})
/** 验证规则 */
const rules = reactive<FormRules<IMediaType>>({
  sname: [
    { required: true, message: '类型名称不能为空', trigger: 'blur' },
    { min: 1, max: 50, message: '类型名称长度不能大于50个字符', trigger: 'change' }
  ],
  skey: [
    { required: true, message: '类型关键字不能为空', trigger: 'blur' },
    { min: 1, max: 50, message: '类型关键字长度不能大于50个字符', trigger: 'change' }
  ]
})

onMounted(() => {
  getMediaTypePageList()
  /**
   * 计算表格高度
   */
  tableHeight.value = computTableHeight()
})

/**
 * 获取媒体类型最大序号
 */
const getMediaTypeISort = () => {
  getMediaTypeMaxSortApi().then((res: IAxios) => {
    if (res.success) {
      mediaTypeForm.value.isort = res.obj
    }
  })
}

/**
 * 获取类型分页列表
 */
const getMediaTypePageList = () => {
  const data: IMediaTypeQuery = {
    sort: sort.value,
    order: order.value,
    pageSize: pageSize.value,
    page: pageIndex.value,
    queryKey: queryKey.value
  }
  getMediaTypePageListApi(data).then((res: IAxios<IMediaType[]>) => {
    if (res.success) {
      mediaTypeList.value = res.obj
      pageTotal.value = res.total
    }
  })
}
/**
 * 新增按钮事件
 */
const onAdd = () => {
  mediaTypeRef.value?.resetFields()
  clearForm()
  getMediaTypeISort()
  editShow.value = true
}
/**
 * 编辑类型信息按钮事件
 * @param row
 */
const onEdit = (row: IMediaType) => {
  editShow.value = true
  mediaTypeForm.value = deepClone(row) as IMediaType
}
/**
 * 删除类型信息按钮事件
 * @param row
 */
const onDelete = (row: IMediaType | undefined) => {
  const ids = []
  if (row === undefined) {
    multipleSelection.value.forEach((item) => {
      ids.push(item.id.toString())
    })
  } else {
    ids.push(row.id.toString())
  }

  if (ids.length === 0) {
    ElMessage.warning('请选择要删除的数据')
    return
  }

  const data = {
    ids: ids.join(',')
  }

  ElMessageBox.confirm('是否删除选择的媒体类型？', '提示', {
    type: 'warning'
  })
    .then(() => {
      deleteMediaTypeApi(data)
        .then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('删除成功')
            getMediaTypePageList()
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
      if (mediaTypeForm.value.sname.trim() === '') {
        ElMessage.warning('媒体类型名称不可为空')
        return
      }
      if (mediaTypeForm.value.skey.trim() === '') {
        ElMessage.warning('媒体类型关键字不可为空')
        return
      }
      if (mediaTypeForm.value.id === '') {
        saveMediaTypeApi(mediaTypeForm.value)
          .then((res: IAxios) => {
            if (res.success) {
              ElMessage.success('保存成功')
              getMediaTypePageList()
              mediaTypeRef.value?.resetFields()
              editShow.value = false
            }
          })
          .catch(() => {})
      } else {
        updateMediaTypeApi(mediaTypeForm.value)
          .then((res: IAxios) => {
            if (res.success) {
              ElMessage.success('更新保存成功')
              getMediaTypePageList()
              mediaTypeRef.value?.resetFields()
              editShow.value = false
            }
          })
          .catch(() => {})
      }
    } else {
      console.log('error submit!', fields)
    }
  })
}
/**
 * 取消编辑
 */
const onCancel = () => {
  mediaTypeRef.value?.resetFields()
  clearForm()
  editShow.value = false
}
/**
 * 清空编辑form内容
 */
const clearForm = () => {
  mediaTypeForm.value = {
    id: '',
    sname: '',
    skey: '',
    sdesc: '',
    buse: true,
    bdefault: false,
    isort: 1
  }
}
/**
 * 翻页
 * @param val
 */
const handleSizeChange = (val: number) => {
  // 分页当前页
  pageIndex.value = val
  getMediaTypePageList()
}
/**
 * 当前页
 * @param val
 */
const handleCurrentChange = (val: number) => {
  pageIndex.value = val
  getMediaTypePageList()
}
/**
 * 行选择事件
 * @param val
 */
const handleSelectionChange = (val: IMediaType[]) => {
  multipleSelection.value = val
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'mediatype'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>

<style lang="scss" scoped></style>
