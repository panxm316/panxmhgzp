<!--
 * @Author: wanghl
 * @Date: 2024-02-22 15:56:36
 * @LastEditTime: 2024-03-14 11:14:59
 * @LastEditors: lhl
 * @Description: 特刊广告项目
-->

<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
        <el-button type="danger" icon="Delete" @click="handleDelete()">删除</el-button>
        <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
        <el-input
          v-model="queryInfo.queryKey"
          placeholder="请输入特刊项目名称"
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
          :data="AdProjectList"
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
            prop="sname"
            label="名称"
            sortable="custom"
            min-width="200"
            show-overflow-tooltip
          >
            <template #default="scope">
              <el-button
                link
                type="primary"
                icon="view"
                size="small"
                @click="handleSee(scope.row.id)"
                >{{ scope.row.sname }}</el-button
              >
            </template>
          </el-table-column>
          <el-table-column prop="screatename" label="创建人" sortable="custom" min-width="150">
          </el-table-column>

          <el-table-column
            prop="sprojectcontent"
            label="项目说明"
            min-width="150"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column prop="bprojectcomplete" label="结项" width="90" align="center">
            <template #default="scope">
              <el-tag v-if="scope.row.bprojectcomplete === false" type="success">未结项</el-tag>
              <el-tag v-if="scope.row.bprojectcomplete === true" type="danger">已结项</el-tag>
              <!-- <el-checkbox v-model="scope.row.bprojectcomplete" disabled></el-checkbox> -->
            </template>
          </el-table-column>

          <el-table-column prop="dstartdate" label="开始日期" width="100">
            <template #default="scope">
              <span>{{ formatDatesm(scope.row.dstartdate) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="denddate" label="结束日期" width="100">
            <template #default="scope">
              <span>{{ formatDatesm(scope.row.denddate) }}</span>
            </template>
          </el-table-column>

          <el-table-column prop="isort" label="序号" width="80" sortable="custom" align="center">
          </el-table-column>
          <el-table-column prop="dcreatedate" label="创建时间" width="160">
            <template #default="scope">
              <span>{{ formatDatesm(scope.row.dcreatedate) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="left"
            width="340"
            class-name="small-padding fixed-width"
          >
            <template #default="adproject">
              <el-button
                v-if="adproject.row.bprojectcomplete == false"
                link
                type="success"
                icon="Edit"
                size="small"
                @click="handleUpdate(adproject.row)"
                >修改</el-button
              >
              <el-button
                v-if="adproject.row.bprojectcomplete === false"
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
          v-model:current-page="currentPage"
          class="pagination"
          style="margin-left: 10px; width: 100%"
          :page-sizes="pageSizes"
          :page-size="pageSize"
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
    <!-- 项目编辑 -->
    <el-dialog
      v-model="dialogVisible"
      title="特刊项目编辑"
      width="700"
      align-center
      :close-on-click-modal="false"
    >
      <el-form ref="AdProjectFormRef" :model="adprojectData" label-width="150px" :rules="rules">
        <el-row :gutter="20">
          <el-col :span="22">
            <el-form-item label="名称" prop="sname">
              <el-input v-model="adprojectData.sname" />
            </el-form-item>
            <el-form-item label="序号" prop="isort">
              <el-input-number
                v-model="adprojectData.isort"
                :min="1"
                controls-position="right"
                style="width: 220px"
              />
            </el-form-item>
            <el-form-item label="开始日期" prop="dstartdate">
              <el-date-picker
                v-model="adprojectData.dstartdate"
                :editable="false"
                :picker-options="pickerOptions"
                type="datetime"
                placeholder="选择日期"
                value-format="YYYY-MM-DD HH:mm:ss"
                :clearable="false"
              ></el-date-picker>
            </el-form-item>
            <el-form-item label="结束日期" prop="denddate">
              <el-date-picker
                v-model="adprojectData.denddate"
                :editable="false"
                :picker-options="pickerOptions"
                type="datetime"
                placeholder="选择日期"
                value-format="YYYY-MM-DD HH:mm:ss"
                :clearable="false"
              ></el-date-picker>
            </el-form-item>

            <el-form-item label="项目说明" prop="sprojectcontent">
              <el-input v-model="adprojectData.sprojectcontent" />
            </el-form-item>

            <el-form-item prop="sremark" label="是否结项">
              <el-checkbox v-model="adprojectData.bprojectcomplete">结项</el-checkbox>
            </el-form-item>
            <el-form-item label="项目资料" prop="sprojectcontent">
              <HgFileUpload
                :server="hgfileuploadparam.server"
                :accept="hgfileuploadparam.accept"
                :multiple="true"
                :storytypes="hgfileuploadparam.storytypes"
                @getupfile="getUpFile"
              ></HgFileUpload>
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
    <!-- 项目对应订单列表弹出框 -->
    <el-dialog
      v-model="dialogVisibleOrder"
      title="项目对应订单列表"
      align-center
      :close-on-click-modal="false"
      :width="fenbianlv() > 1440 ? '80%' : '94%'"
      @close="dialogVisibleOrder = false"
    >
      <AdTworderList :adprojectid="adprojectData.id" />
    </el-dialog>
    <!-- 项目详情 -->
    <AdProjectdetails ref="AdProjectdetailsref"></AdProjectdetails>
  </div>
</template>

<script setup lang="ts">
import { FormRules, FormInstance } from 'element-plus'
import { computTreeHeight, fenbianlv } from '@/utils/index'
import type { TSpecialProject } from '@/types/views/ad/specialproject'
import type { TQueryInfo } from '@/types/common'
import {
  getSpecialProjectPageListApi,
  getSpecialProjectMaxSortApi,
  saveSpecialProjectApi,
  updateSpecialProjectApi,
  deleteSpecialProjectApi
} from '@/api/ad/specialproject'
import {
  required,
  computTableHeight,
  isNumberStr,
  formatDate,
  formatDatesm,
  tableHeaderColor,
  formatMoney,
  convertCurrency
} from '@/utils'
import type { IAxios } from 'axios'
import modal from '@/plugins/modal'
import HgDateIndex from '@/components/HgDateIndex/index.vue'
import { TDateType, TDateTimeType } from '@/types/components/hgdateindex'
import dayjs from 'dayjs'
import { add } from 'lodash'
import AdTworder from './AdTworder.vue'
import AdTworderList from './AdTworderList.vue'
import AdProjectdetails from './SpecialProjectDetails.vue'
defineOptions({
  name: 'SpecialAdproject'
})
import { useRouter } from 'vue-router'
const router = useRouter()
/** 订单预定 dialog */
const dialogVisibleWorder = ref(false)
/** 项目对应订单列表弹出框 */
const dialogVisibleOrder = ref(false)
/** 表格高度 */
const tableheight = ref(0)
/** 编辑 dialog */
const dialogVisible = ref(false)
/** 广告项目列表 */
const AdProjectList = ref<TSpecialProject[]>([])
/** 广告详情 */
const AdProjectdetailsref = ref()
/** 广告项目 */
const adprojectData = ref<TSpecialProject>({
  id: undefined,
  sname: '',
  dstartdate: '',
  denddate: '',
  createempid: '',
  screatename: '',
  dcreatedate: '',
  sprojectcontent: '',
  bprojectcomplete: 0,
  sremark: '',
  isort: 0
})
const pickerOptions = ref({
  disabledDate: function (time: Date) {
    return time.getTime() < Date.now() - 8.64e7
  }
})
/**
 * 项目id和名称
 */
const AdProjectIdAndName = ref<any>({
  id: '',
  sname: ''
})
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 页数 */
const currentPage = ref<number>(1)
/** 每页条数 */
const pageSize = ref<number>(20)
/** 数据总条数 */
const pageTotal = ref<number>(0)
/** 新增标志 */
const addFlag = ref(false)

/** 查询对象 */
const queryInfo = reactive<TQueryInfo>({
  sort: 'dcreatedate',
  order: 'desc',
  startTime: '',
  endTime: '',
  pageSize: pageSize.value,
  page: currentPage.value,
  queryKey: ''
})
const timedata = reactive<TDateType>({
  // 时间
  timetype: '2',
  timeoption: [
    {
      label: '今天',
      value: '0'
    },
    {
      label: '三天内',
      value: '2'
    },
    {
      label: '一周内',
      value: '6'
    },
    {
      label: '二周内',
      value: '13'
    },
    {
      label: '一月内',
      value: '30'
    },
    {
      label: '自定义',
      value: '00'
    }
  ],
  startTime: '',
  endTime: ''
})
const AdProjectFormRef = ref<FormInstance>()
const AdProjectSelection = ref<TSpecialProject[]>()
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<TSpecialProject>>({
  sname: [
    { validator: required, required: true, message: '请输入名称', trigger: 'blur' },
    { max: 48, message: '不得大于48个字符', trigger: 'blur' }
  ],
  dstartdate: [{ validator: required, required: true, message: '请选择开始日期', trigger: 'blur' }],
  denddate: [{ validator: required, required: true, message: '请选择结束日期', trigger: 'blur' }]
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
const handleSelectionChange = (rows: TSpecialProject[]) => {
  AdProjectSelection.value = rows
  console.log(AdProjectSelection.value)
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
 * 获取色彩列表
 */
const loaddata = () => {
  const data = queryInfo
  const curStartDate = new Date(dayjs(queryInfo.startTime).format('YYYY-MM-DD 00:00:00')).getTime()
  const curEndDate = new Date(dayjs(queryInfo.endTime).format('YYYY-MM-DD 00:00:00')).getTime()
  if (curEndDate < curStartDate) {
    modal.alertError('开始日期不能大于结束日期！')
    return
  }
  getSpecialProjectPageListApi(data).then(({ obj }: IAxios<TSpecialProject[]>) => {
    console.log(obj)
    AdProjectList.value = obj
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

const ontime = (val: TDateTimeType) => {
  let timev = false
  if (queryInfo.startTime) {
    timev = true
  }
  queryInfo.startTime = val.startTime
  queryInfo.endTime = val.endTime
  if (timev) {
    loaddata()
  }
}
/**
 * 获取序号最大值
 */
const getMaxSort = () => {
  getSpecialProjectMaxSortApi().then(({ success, obj }: IAxios<number>) => {
    adprojectData.value.isort = obj
  })
}
/**
 * 新增
 */
const handleAdd = () => {
  adprojectData.value = {
    sname: '',
    dstartdate: '',
    denddate: '',
    createempid: '',
    screatename: '',
    dcreatedate: '',
    sprojectcontent: '',
    bprojectcomplete: 0,
    isort: 0,
    sremark: ''
  }
  addFlag.value = true
  getMaxSort()
  dialogVisible.value = true
}
/**
 * 修改
 * @param row
 */
const handleUpdate = (row: TSpecialProject) => {
  adprojectData.value = { ...row }
  dialogVisible.value = true
  addFlag.value = false
}

/**
 * 保存
 */
const handleSave = () => {
  AdProjectFormRef.value?.validate((valid) => {
    if (valid) {
      const curStartDate = new Date(
        dayjs(adprojectData.value.dstartdate).format('YYYY-MM-DD 00:00:00')
      ).getTime()
      const curEndDate = new Date(
        dayjs(adprojectData.value.denddate).format('YYYY-MM-DD 00:00:00')
      ).getTime()
      if (curEndDate < curStartDate) {
        modal.alertError('开始日期不能大于结束日期！')
        return
      }
      if (!adprojectData.value.id) {
        saveSpecialProjectApi(adprojectData.value).then(({ success, msg }: IAxios) => {
          if (success) {
            modal.msgSuccess('操作成功')
          } else {
            modal.msgError(msg)
          }
          loaddata()
        })
      } else {
        updateSpecialProjectApi(adprojectData.value).then(({ success, msg }: IAxios) => {
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
 * 取消
 */
const handleCancel = () => {
  dialogVisible.value = false
  setTimeout(() => {
    AdProjectFormRef.value?.clearValidate(['sname'])
  }, 10)
}
/**
 * 删除
 * @param row
 */
const handleDelete = (row?: TSpecialProject) => {
  modal.confirm('是否删除?').then(() => {
    const ids = []
    if (row) {
      ids.push(row.id)
    } else {
      /**
       * 过滤到已结项的数据
       */
      AdProjectSelection.value?.forEach((item) => {
        if ((item.bprojectcomplete as any) === true) {
          modal.alert('已结项的数据不能删除')
          return
        }
        ids.push(item.id)
      })
    }
    if (!ids.length) {
      modal.msgWarning('请选择删除记录')
      return
    }
    deleteSpecialProjectApi(ids.join(',')).then(({ success, msg }: IAxios) => {
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
  const to = '/template/Help?name=' + 'adproject'
  // router.push(to)
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
/** 上传组件参数 */
const hgfileuploadparam = ref<THgFileUploadParam>({
  server: import.meta.env.VITE_APP_BASE_API + '/customer/customerfiles/upLoadCustomerFile',
  // server: import.meta.env.VITE_APP_BASE_API + '/common/UploadFile/importFile',
  multiple: true,
  filenumlimit: 20,
  storytypes: []
})
import { THgFileUploadParam, TUpFile } from '@/types/components/hgfileupload'
/**
 * 获取上传后返回的文件列表
 * @param res
 */
const getUpFile = (res: IAxios<TUpFile>) => {
  console.log(res.obj)
  // if (res.success) {
  //   const workreport = res.obj as Twcustomerfiles
  //   WcustomerFileslsit.value.push(workreport)
  //   CustomerData.value.customerfiles = WcustomerFileslsit.value.map((item) => {
  //     return {
  //       id: '',
  //       customerid: CustomerData.value.id,
  //       createempid: userStore.user?.userid.toString(),
  //       sfileformat: item.sfileformat,
  //       sfileid: item.sfileid,
  //       sfilesize: item.sfilesize,
  //       soriginalfile: item.soriginalfile,
  //       dcreatetime: '',
  //       sfiletype: item.sfiletype,
  //       sfilecategory: '',
  //       bdelete: false,
  //       sdescription: '',
  //       durl: ''
  //     }
  //   })
  //   return
  // }
}

/**
 * 取消
 */
const handleCancelworder = () => {
  dialogVisibleWorder.value = false
}
/**
 * 保存信息关闭订单弹窗
 */
const closedialogVisible = (val: string) => {
  loaddata()
  dialogVisibleWorder.value = false
}
/**
 * 详情
 * @param row
 */
const handleSee = (row: string) => {
  AdProjectdetailsref.value.view(row)
}
</script>

<style scoped>
ul {
  padding-left: 10px;
}
.el-form-item {
  margin-bottom: 15px;
}
</style>
