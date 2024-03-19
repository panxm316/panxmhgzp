<!--
 * @Author: caogd
 * @Date: 2023-10-27 08:55:22
 * @LastEditTime: 2023-10-28 12:55:17
 * @LastEditors: caogd
 * @Description: 资源文件类型
-->

<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-input
          v-model="queryKey"
          clearable
          placeholder="请输入类型名称"
          style="width: 200px"
          @keyup.enter="getResourceTypePageList"
          @clear="getResourceTypePageList"
        >
        </el-input>
        <el-button type="primary" icon="Search" @click="getResourceTypePageList">搜索</el-button>
        <el-tooltip class="item" effect="dark" content="帮助" placement="top">
          <el-button class="help" type="text" @click="showhelp()">?</el-button>
        </el-tooltip>
      </div>
    </div>
    <div class="table_box">
      <el-table
        :data="resourceTypeList"
        row-key="id"
        :height="tableHeight"
        :border="true"
        stripe
        :header-cell-style="tableHeaderColor"
      >
        <el-table-column prop="sname" label="类型名称" :width="150"> </el-table-column>
        <el-table-column prop="sfileformat" label="文件格式"> </el-table-column>
        <el-table-column prop="stypename" label="中文名称" :width="150"> </el-table-column>
        <el-table-column prop="isort" label="序号 " :width="80" align="center"> </el-table-column>
        <el-table-column prop="buse" label="启用" :width="80" align="center">
          <template #default="scope">
            <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="150"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button link type="success" icon="Edit" @click="onEdit(scope.row)">修改</el-button>
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
      <el-form ref="resourceTypeRef" :model="resourceTypeForm" :rules="rules" label-width="100px">
        <el-row :gutter="50">
          <el-col :span="22">
            <el-form-item label="类型名称" prop="sname">
              <el-input
                v-model="resourceTypeForm.sname"
                :disabled="resourceTypeForm.id !== ''"
              ></el-input>
            </el-form-item>
            <el-form-item label="文件格式" prop="sfileformat">
              <el-input v-model="resourceTypeForm.sfileformat"></el-input>
            </el-form-item>
            <el-form-item label="序号" prop="isort">
              <el-input-number
                v-model="resourceTypeForm.isort"
                :min="1"
                :max="1000"
                label="序号"
              ></el-input-number>
            </el-form-item>
            <el-form-item label="中文名称" prop="stypename">
              <el-input v-model="resourceTypeForm.stypename"></el-input>
            </el-form-item>

            <el-row :gutter="50">
              <el-col :span="4">
                <el-form-item label="启用" prop="buse">
                  <el-checkbox v-model="resourceTypeForm.buse"></el-checkbox>
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" icon="Check" @click="onSave(resourceTypeRef)">确 定</el-button>
          <el-button icon="Close" @click="onCancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { deepClone } from '@/utils'
import { IAxios } from 'axios'
import { FormInstance, FormRules } from 'element-plus'
import { computTableHeight, tableHeaderColor } from '@/utils/index'
defineOptions({
  name: 'ResourceType'
})
import { useRouter } from 'vue-router'
import { IResourceType, IResourceTypeQuery } from '@/types/views/system/resourcetype'
import { getResourceTypePageListApi, updateResourceTypeApi } from '@/api/system/resourcetype'
const router = useRouter()
/** Form名称 */
const resourceTypeRef = ref<FormInstance>()
/** Form选中的行 */
const multipleSelection = ref<IResourceType[]>([])
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
const resourceTypeList = ref<IResourceType[]>([])
/** 查询关键字 */
const queryKey = ref('')
/** 类型编辑显示状态 */
const editShow = ref(false)
/** 类型编辑实体 */
const resourceTypeForm = ref<IResourceType>({
  id: '',
  sname: '',
  sfileformat: '',
  stypename: '',
  buse: true,
  isort: 1
})
/** 验证规则 */
const rules = reactive<FormRules<IResourceType>>({
  stypename: [
    { required: true, message: '中文名称不能为空', trigger: 'blur' },
    { min: 1, max: 50, message: '中文名称长度不能大于50个字符', trigger: 'change' }
  ],
  sfileformat: [{ min: 1, max: 200, message: '文件格式长度不能大于200个字符', trigger: 'change' }]
})

onMounted(() => {
  getResourceTypePageList()
  /**
   * 计算表格高度
   */
  tableHeight.value = computTableHeight()
})

/**
 * 获取类型分页列表
 */
const getResourceTypePageList = () => {
  const data: IResourceTypeQuery = {
    sort: sort.value,
    order: order.value,
    pageSize: pageSize.value,
    page: pageIndex.value,
    queryKey: queryKey.value
  }
  getResourceTypePageListApi(data).then((res: IAxios<IResourceType[]>) => {
    if (res.success) {
      resourceTypeList.value = res.obj
      pageTotal.value = res.total
    }
  })
}

/**
 * 编辑类型信息按钮事件
 * @param row
 */
const onEdit = (row: IResourceType) => {
  editShow.value = true
  resourceTypeForm.value = deepClone(row) as IResourceType
}

/**
 * 新增修改保存事件
 * @param formEl
 */
const onSave = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid, fields) => {
    if (valid) {
      updateResourceTypeApi(resourceTypeForm.value).then((res: IAxios) => {
        if (res.success) {
          ElMessage.success('更新保存成功')
          getResourceTypePageList()
          resourceTypeRef.value?.resetFields()
          editShow.value = false
        }
      })
    } else {
      console.log('error submit!', fields)
    }
  })
}
/**
 * 取消编辑
 */
const onCancel = () => {
  resourceTypeRef.value?.resetFields()
  clearForm()
  editShow.value = false
}
/**
 * 清空编辑form内容
 */
const clearForm = () => {
  resourceTypeForm.value = {
    id: '',
    sname: '',
    sfileformat: '',
    stypename: '',
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
  getResourceTypePageList()
}
/**
 * 当前页
 * @param val
 */
const handleCurrentChange = (val: number) => {
  pageIndex.value = val
  getResourceTypePageList()
}

/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'resourcetype'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>

<style lang="scss" scoped></style>
