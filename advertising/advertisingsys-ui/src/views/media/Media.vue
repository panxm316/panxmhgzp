<!--
 * @Author: suny
 * @Date: 2023-08-21 14:00:51
 * @LastEditTime: 2023-11-06 10:59:36
 * @LastEditors: peij
 * @Description: 媒体管理
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button type="primary" icon="Plus" @click="onAdd">新增</el-button>
        <el-button type="danger" icon="delete" @click="onDelete(undefined)">删除</el-button>
        <el-input
          v-model="queryKey"
          clearable
          placeholder="请输入媒体关键字"
          class="input-with-select"
          style="width: 200px"
          @keyup.enter="getMediaPageList"
          @clear="getMediaPageList"
        >
        </el-input>
        <el-button type="primary" icon="Search" @click="getMediaPageList">搜索</el-button>
        <el-tooltip class="item" effect="dark" content="帮助" placement="top">
          <el-button class="help" type="text" @click="showhelp()">?</el-button>
        </el-tooltip>
      </div>
    </div>
    <div class="table_box">
      <el-table
        :data="mediaList"
        row-key="id"
        :height="tableHeight"
        :border="true"
        stripe
        :header-cell-style="tableHeaderColor"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column prop="sname" label="媒体名称"> </el-table-column>
        <el-table-column prop="mediatypename" label="媒体类别" :width="150"> </el-table-column>
        <el-table-column prop="scode" label="媒体编码"> </el-table-column>
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
        :disabled="pageDisabled"
        :background="background"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pageTotal"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <el-dialog v-model="editShow" title="媒体编辑" width="680px" append-to-body>
      <el-form
        ref="mediaRef"
        :model="mediaForm"
        :rules="rules"
        label-width="100px"
        @selection-change="handleSelectionChange"
      >
        <el-row :gutter="50">
          <el-col :span="16">
            <el-form-item label="媒体类型" prop="stype">
              <el-select
                v-model="mediaForm.mediatypekey"
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
            </el-form-item>
            <el-form-item label="媒体名称" prop="sname">
              <el-input v-model="mediaForm.sname"></el-input>
            </el-form-item>
            <el-form-item label="媒体编码" prop="scode">
              <el-input v-model="mediaForm.scode" :disabled="mediaForm.id !== ''"></el-input>
            </el-form-item>
            <el-form-item label="序号" prop="isort">
              <el-input-number
                v-model="mediaForm.isort"
                :min="1"
                :max="1000"
                label="序号"
              ></el-input-number>
            </el-form-item>

            <el-row :gutter="50">
              <el-col :span="8">
                <el-form-item label="启用" prop="buse">
                  <el-checkbox v-model="mediaForm.buse"></el-checkbox>
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="onSave(mediaRef)">确 定</el-button>
          <el-button @click="onCancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { getMediaTypeComboApi } from '@/api/media/mediatype'
import {
  getMediaPageListApi,
  getBMediaChildApi,
  saveMediaApi,
  updateMediaApi,
  deleteMediaApi,
  getMediaMaxSortApi
} from '@/api/media/media'
import { IBaseQueryInfo } from '@/types/common'
import { IDataCombo } from '@/types/common/DataCombo'
import { IMedia, IMediaQuery } from '@/types/views/media/media'
import { IAxios } from 'axios'
import { FormInstance, FormRules } from 'element-plus'
import { deepClone } from '@/utils'
import { computTableHeight, tableHeaderColor } from '@/utils/index'
defineOptions({
  name: 'Media'
})
import { useRouter } from 'vue-router'
const router = useRouter()
/** Form名称 */
const mediaRef = ref<FormInstance>()
/** Form选中的行 */
const multipleSelection = ref<IMedia[]>([])
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
/** 媒体列表 */
const mediaList = ref<IMedia[]>([])
/** 查询关键字 */
const queryKey = ref('')
/** 媒体类别下拉列表 */
const mediaTypeCombo = ref<IDataCombo[]>()
/** 媒体编辑显示状态 */
const editShow = ref(false)
/** 媒体编辑实体 */
const mediaForm = ref<IMedia>({
  id: '',
  sname: '',
  mediatypename: '',
  mediatypekey: '',
  scode: '',
  buse: true,
  isort: 1
})
/** 验证规则 */
const rules = reactive<FormRules<IMedia>>({
  sname: [
    { required: true, message: '媒体名称不能为空', trigger: 'blur' },
    { min: 1, max: 100, message: '媒体名称长度不能大于100个字符', trigger: 'change' }
  ],
  mediatypekey: [{ required: true, message: '媒体类型不能为空', trigger: 'blur' }],
  scode: [
    { required: true, message: '媒体编码不能为空', trigger: 'blur' },
    { min: 1, max: 20, message: '媒体编码长度不能大于20个字符', trigger: 'change' }
  ]
})

onMounted(() => {
  getMediaPageList()
  getMediaType()
  /**
   * 计算表格高度
   */
  tableHeight.value = computTableHeight()
})

/**
 * 获取媒体最大序号
 */
const getMediaISort = () => {
  getMediaMaxSortApi().then((res: IAxios) => {
    if (res.success) {
      mediaForm.value.isort = res.obj
    }
  })
}

/**
 * 获取媒体分页列表
 */
const getMediaPageList = () => {
  const data: IMediaQuery = {
    sort: sort.value,
    order: order.value,
    pageSize: pageSize.value,
    page: pageIndex.value,
    queryKey: queryKey.value
  }
  getMediaPageListApi(data).then((res: IAxios<IMedia[]>) => {
    if (res.success) {
      mediaList.value = res.obj
      pageTotal.value = res.total
    }
  })
}
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
 * 新增按钮事件
 */
const onAdd = () => {
  mediaRef.value?.resetFields()
  clearForm()
  getMediaISort()
  const defaulttypes = mediaTypeCombo.value?.filter((type) => {
    return type.bdefault
  })
  if (defaulttypes && defaulttypes.length > 0) {
    mediaForm.value.mediatypekey = defaulttypes[0].id
  }
  editShow.value = true
}
/**
 * 编辑媒体信息按钮事件
 * @param row
 */
const onEdit = (row: IMedia) => {
  editShow.value = true
  mediaForm.value = deepClone(row) as IMedia
}
/**
 * 删除媒体信息按钮事件
 * @param row
 */
const onDelete = (row: IMedia | undefined) => {
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

  getBMediaChildApi(data).then((res: IAxios) => {
    if (res.success) {
      if (res.msg !== '') {
        ElMessage.error(res.msg + ' 请先将叠次下的相关属性删除')
        return
      }
      ElMessageBox.confirm('是否删除选择的媒体？', '提示', {
        type: 'warning'
      })
        .then(() => {
          deleteMediaApi(data)
            .then((res: IAxios) => {
              if (res.success) {
                ElMessage.success('删除成功')
                getMediaPageList()
              }
            })
            .catch(() => {})
        })
        .catch(() => {})
    }
  })
}
/**
 * 新增修改保存事件
 * @param formEl
 */
const onSave = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid, fields) => {
    if (valid) {
      if (mediaForm.value.sname.trim() === '') {
        ElMessage.warning('媒体名称不可为空')
        return
      }
      if (mediaForm.value.scode.trim() === '') {
        ElMessage.warning('媒体编码不可为空')
        return
      }
      const mediatypenameArray = mediaTypeCombo.value?.filter(
        (item) => item.id === mediaForm.value.mediatypekey
      )
      if (mediatypenameArray && mediatypenameArray?.length > 0) {
        mediaForm.value.mediatypename = mediatypenameArray?.at(0)?.name ?? ''
      }

      if (mediaForm.value.id === '') {
        saveMediaApi(mediaForm.value)
          .then((res: IAxios) => {
            if (res.success) {
              ElMessage.success('保存成功')
              getMediaPageList()
              mediaRef.value?.resetFields()
              editShow.value = false
            }
          })
          .catch(() => {})
      } else {
        updateMediaApi(mediaForm.value)
          .then((res: IAxios) => {
            if (res.success) {
              ElMessage.success('更新保存成功')
              getMediaPageList()
              mediaRef.value?.resetFields()
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
  mediaRef.value?.resetFields()
  clearForm()
  editShow.value = false
}
/**
 * 清空编辑form内容
 */
const clearForm = () => {
  mediaForm.value = {
    id: '',
    sname: '',
    mediatypekey: '',
    mediatypename: '',
    scode: '',
    buse: true,
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
  getMediaPageList()
}
/**
 * 当前页
 * @param val
 */
const handleCurrentChange = (val: number) => {
  pageIndex.value = val
  getMediaPageList()
}
/**
 * 行选择事件
 * @param val
 */
const handleSelectionChange = (val: IMedia[]) => {
  multipleSelection.value = val
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'media'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>

<style scoped></style>
