<!--
 * @Author: wanghl
 * @Date: 2023-11-15 09:32:33
 * @LastEditTime: 2024-03-04 16:11:45
 * @LastEditors: songly
 * @Description:色彩结构
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex" style="width: 94%">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
        <!-- <el-button type="danger" icon="Delete" @click="handleDelete()">删除</el-button> -->
        <el-input
          v-model="queryInfo.queryKey"
          placeholder="请输入关键词"
          clearable
          style="width: 300px"
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
          :data="PageColorList"
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
          <el-table-column prop="sname" label="色彩结构名称" min-width="150" show-overflow-tooltip>
          </el-table-column>
          <el-table-column prop="lsColors" label="版数" width="100" align="center">
            <template #default="scope">
              <span>{{ scope.row.lsColors.length }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="isort" label="序号" width="100" sortable="custom" align="center">
          </el-table-column>
          <el-table-column prop="buse" label="是否有效" width="100" align="center">
            <template #default="scope">
              <span v-if="scope.row.buse == false">
                <el-tag type="danger">无效</el-tag>
              </span>
              <span v-if="scope.row.buse == true">
                <el-tag type="success">有效</el-tag>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="sremark" label="备注" width="200" show-overflow-tooltip>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="250"
            class-name="small-padding fixed-width"
            fixed="right"
          >
            <template #default="adproject">
              <el-button
                link
                type="success"
                icon="Edit"
                size="small"
                @click="handleUpdate(adproject.row)"
                >修改</el-button
              >
              <el-button
                link
                type="danger"
                icon="Delete"
                size="small"
                @click="handleDelete(adproject.row)"
                >删除</el-button
              >
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
    <el-dialog
      v-model="dialogVisible"
      title="色彩结构编辑"
      width="700"
      align-center
      :close-on-click-modal="false"
      @close="handleCancel"
    >
      <el-form ref="PageColorFormRef" :model="PageColor" label-width="120px" :rules="rules">
        <el-row :gutter="20">
          <el-col :span="21">
            <el-form-item label="色彩结构名称" prop="sname">
              <el-input v-model="PageColor.sname" :maxlength="200" />
            </el-form-item>
            <el-form-item label="序号" prop="isort">
              <el-input-number
                v-model="PageColor.isort"
                style="width: 500px; text-align: left"
                :min="1"
                controls-position="right"
              />
            </el-form-item>
            <el-form-item prop="sremark" label="备注">
              <el-input v-model="PageColor.sremark" :maxlength="200" />
            </el-form-item>
            <el-form-item label="是否有效" prop="buse">
              <el-checkbox v-model="PageColor.buse"></el-checkbox>
            </el-form-item>
            <el-form-item label="版数" prop="colorlist">
              <el-input-number
                v-model="lsColorslsitnumber"
                style="width: 152px; margin-right: 10px; margin-top: -20px"
                :min="1"
                :max="99"
                controls-position="right"
              />
              <div v-for="(item, index) in PagecolorType" :key="item.id">
                <el-button
                  :type="index === 0 ? 'info' : index === 2 ? 'danger' : 'danger'"
                  style="margin-right: 5px; margin-bottom: 20px"
                  @click="shezhi(index)"
                  >设置{{ item.name }}</el-button
                >
              </div>
              <div class="lilistbox">
                <li v-for="(item, index) in lsColorslsit" :key="index" class="lilist">
                  <el-link v-if="item == 0" :underline="false">
                    <el-icon><Document /></el-icon> {{ index + 1 }}
                    <span style="color: black">--黑白</span>
                  </el-link>
                  <el-link v-if="item == 1" type="danger" :underline="false">
                    <el-icon><Document /></el-icon> {{ index + 1 }}

                    <span style="color: #f56c6c">--彩色</span>
                  </el-link>
                  <el-link v-if="item == 2" type="danger" :underline="false">
                    <el-icon><Document /></el-icon> {{ index + 1 }}
                    <span style="color: #f56c6c">--套红</span>
                  </el-link>
                  <el-button
                    class="ml1"
                    link
                    type="danger"
                    icon="CircleClose"
                    @click="onUpfileDel(index)"
                  ></el-button>
                </li>
              </div>
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
import type { TAPageColor, TQPageColor } from '@/types/views/schedule/pagecolor'
import {
  getPagecolorListApi,
  getPagecolorByIdApi,
  savePagecolorApi,
  updatePagecolorApi,
  deletePagecolorApi,
  getMaxSortApi
} from '@/api/schedule/pagecolor'
import { required, formatDate, tableHeaderColor, computTableHeight } from '@/utils/index'
import type { IAxios } from 'axios'
import modal from '@/plugins/modal'
import { EPageColor } from '@/types/common/enumindex'
import { getEnumCombo } from '@/api/common/index'
import { TSelectZtree } from '@/types/common'
import useUserStore from '@/store/modules/user'
const userStore = useUserStore()
import { useRouter } from 'vue-router'
defineOptions({
  name: 'Pagecolor'
})
const router = useRouter()
/** 表格高度 */
const tableheight = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 数据总条数 */
const pageTotal = ref<number>(0)
/** 客户归属列表列表 */
const PageColorList = ref<TAPageColor[]>([])
/** 色彩枚举参数 */
const PagecolorType = getEnumCombo(EPageColor)
/** 色彩枚举中间过渡 */
const lsColorslsit = ref<any>([])
/** 版数 */
const lsColorslsitnumber = ref(1)
/** 查询对象 */
const queryInfo = reactive<TQPageColor>({
  sort: 'isort',
  order: 'desc',
  startTime: '',
  endTime: '',
  pageSize: 20,
  page: 1,
  queryKey: '', // 查询关键字
  loginUserId: userStore.user?.userid.toString(), // 登录id
  cacheDTOKey: ''
})
/** 色彩结构数据 */
const PageColor = ref<TAPageColor>({
  id: '',
  sname: '',
  colorlist: '',
  isort: 0,
  buse: true,
  sremark: ''
})
/** 编辑 dialog */
const dialogVisible = ref(false)
const PageColorFormRef = ref<FormInstance>()
const PageColorSelection = ref<TAPageColor[]>()
const AdProjectSelection = ref<TAPageColor[]>()
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<TAPageColor>>({
  sname: [{ required: true, message: '请选择业务员', trigger: 'change' }]
})
onMounted(() => {
  loaddata()
  console.log(PagecolorType)
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})
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
 * 获取序号最大值
 */
const getMaxSort = () => {
  getMaxSortApi().then(({ success, obj }: IAxios<number>) => {
    PageColor.value.isort = obj
    pageTotal.value = obj
  })
}
/**
 * 获取归属列表
 */
const loaddata = () => {
  getPagecolorListApi(queryInfo).then(({ obj }: IAxios<TAPageColor[]>) => {
    console.log(obj)
    PageColorList.value = obj
    pageTotal.value = obj.length
  })
}
/**
 * 新增
 */
const handleAdd = () => {
  redet()
  getMaxSort()
  dialogVisible.value = true
  var list = PageColorList.value.map((item) => item.id).join(',')
  console.log(list)
}
/**
 * 修改
 * @param row
 */
const handleUpdate = (row: TAPageColor) => {
  PageColor.value = { ...row }
  lsColorslsit.value = row.lsColors
  dialogVisible.value = true
}
/**
 * 保存
 */
const handleSave = () => {
  ElMessageBox.confirm('确定要这样操作吗', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      PageColorFormRef.value?.validate((valid) => {
        PageColor.value.colorlist = lsColorslsit.value.join()
        if (valid) {
          if (!PageColor.value.id) {
            savePagecolorApi(PageColor.value).then(({ success, msg }: IAxios) => {
              if (success) {
                modal.msgSuccess('操作成功')
              } else {
                modal.msgError(msg)
              }
              loaddata()
            })
          } else {
            updatePagecolorApi(PageColor.value).then(({ success, msg }: IAxios) => {
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
    })
    .catch(() => {})
}
/**
 * 取消
 */
const handleCancel = () => {
  PageColorFormRef.value?.resetFields()
  dialogVisible.value = false
  setTimeout(() => {
    PageColorFormRef.value?.clearValidate(['sname'])
  }, 10)
  redet()
}
/**
 * 选择
 * @param rows
 */
const handleSelectionChange = (rows: TAPageColor[]) => {
  PageColorSelection.value = rows
}
/**
 * 排序
 * @param val
 */
const handleSortChange = (val: { prop: string; order: string }) => {
  console.log(val)
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
 * 删除
 * @param row
 */
const handleDelete = (row?: TAPageColor) => {
  modal.confirm('是否删除?').then(() => {
    const ids = []
    if (row) {
      ids.push(row.id)
    } else {
      AdProjectSelection.value?.forEach((color) => ids.push(color.id))
    }
    if (!ids.length) {
      modal.msgWarning('请选择删除记录')
      return
    }
    deletePagecolorApi(ids.join(',')).then(({ success, msg }: IAxios) => {
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
 * 删除版面
 * @param index
 */
const onUpfileDel = (index: number) => {
  lsColorslsit.value!.splice(index, 1)
}
/** 设置版面 */
const shezhi = (data: number) => {
  for (let i = 0; i < lsColorslsitnumber.value; i++) {
    lsColorslsit.value.push(data)
  }
}
/**
 * 关闭是清空数据
 */
const redet = () => {
  PageColor.value = {
    lsColors: [],
    id: '',
    sname: '',
    colorlist: '',
    isort: 0,
    buse: true,
    sremark: ''
  }
  lsColorslsit.value = []
  lsColorslsitnumber.value = 1
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'Customer'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>

<style scoped>
ul {
  padding-left: 10px;
}
.lilist {
  width: 100%;
  padding-left: 20px;
}
.lilistbox {
  width: 500px;
  border: 1px solid #c0c4cc;
  overflow-y: auto;
  max-height: 400px;
  border-radius: 5px;
  min-height: 100px;
}
.lilist:hover {
  background-color: #e9e9e9;
  color: red;
}
.el-input__inner {
  text-align: left !important;
}
</style>
