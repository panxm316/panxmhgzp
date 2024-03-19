<!--
 * @Author: wanghl
 * @Date: 2023-11-15 09:32:33
 * @LastEditTime: 2024-01-23 16:05:00
 * @LastEditors: wanghl
 * @Description:版面计划
-->
<template>
  <div id="FoldPagePlanDetail" class="app-container">
    <div style="padding-left: 5px">
      <el-button type="danger" icon="Delete" @click="handleDelete()">删除</el-button>
      <el-input-number
        v-if="props.addshow"
        v-model="FoldPagePlanOne.pagenum"
        style="width: 160px; margin: 0 20px"
        :min="1"
        :max="99"
        controls-position="right"
      />
      <el-button v-if="props.addshow" type="primary" icon="Plus" @click="handleAddBatchdetail"
        >添加</el-button
      >
    </div>
    <div class="table_box">
      <el-row>
        <el-table
          :data="FoldPagePlanList"
          row-key="id"
          :height="tableheight - 100"
          :border="true"
          stripe
          :header-cell-style="tableHeaderColor"
          @selection-change="handleSelectionChange"
          @sort-change="handleSortChange"
        >
          <el-table-column type="selection" prop="ischeck" :width="50" align="center">
          </el-table-column>

          <el-table-column prop="publishdate" label="刊登日期" min-width="160" sortable="custom">
            <template #default="scope">
              <span> {{ hgFormatDate(scope.row.publishdate, 'YYYY-MM-DD') }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="medianame" label="媒体" min-width="120" show-overflow-tooltip>
          </el-table-column>
          <el-table-column
            prop="foldname"
            label="叠次"
            min-width="120"
            show-overflow-tooltip
            sortable="custom"
          >
          </el-table-column>
          <el-table-column
            prop="foldareavername"
            label="叠次版本"
            min-width="120"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column prop="stoptime" label="截版日期" width="160" sortable="custom">
            <template #default="scope">
              <span>{{ hgFormatDate(scope.row.stoptime, 'YYYY-MM-DD') }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="adcolorname"
            label="版面色彩"
            min-width="160"
            align="center"
            show-overflow-tooltip
          >
            <template #default="scope">
              <el-select
                v-model="scope.row.adcolorid"
                placeholder="色彩"
                style="width: 120px"
                @change="colorchange(scope.row)"
              >
                <el-option
                  v-for="(item, index) in Colordatalist"
                  :key="index"
                  :label="item.name"
                  :value="item.id!"
                ></el-option>
              </el-select>
            </template>
          </el-table-column>

          <el-table-column
            prop="pagenum"
            label="版号"
            width="100"
            align="center"
            sortable="custom"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column prop="adflag" label="广告版标记" width="100" align="center">
            <template #default="scope">
              <span v-if="scope.row.adflag == false">
                <el-tag type="danger">否</el-tag>
              </span>
              <span v-if="scope.row.adflag == true">
                <el-tag type="success">是</el-tag>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="pagetitle" label="版面标题" width="210" align="left">
            <template #default="scope">
              <el-input
                v-model="scope.row.pagetitle"
                type="text"
                style="width: 80%; height: 30px; border: none; background-color: transparent"
              />
              <el-button
                v-if="scope.row.pagetitle"
                type="text"
                icon="check"
                size="small"
                @click="colorchange(scope.row)"
              ></el-button>
            </template>
          </el-table-column>
          <el-table-column prop="sremark" label="备注" width="210" align="left">
            <template #default="scope">
              <el-input
                v-model="scope.row.sremark"
                type="text"
                style="width: 80%; height: 30px; border: none; background-color: transparent"
              />
              <el-button
                v-if="scope.row.sremark"
                type="text"
                icon="check"
                size="small"
                @click="colorchange(scope.row)"
              ></el-button>
            </template>
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
          :total="pageTotallist"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        >
        </el-pagination>
      </el-row>
    </div>
    <!-- 单个新增 -->
    <el-dialog
      v-model="dialogVisible"
      title="版面计划编辑"
      width="700"
      align-center
      :close-on-click-modal="false"
      draggable
      @close="handleCancel"
    >
      <el-form
        ref="FoldPagePlanFormRef"
        :model="FoldPagePlanOne"
        label-width="120px"
        :rules="rules"
      >
        <el-row :gutter="20">
          <el-col :span="22">
            <el-form-item label="版面标题" prop="pagetitle">
              <el-input v-model="FoldPagePlanOne.pagetitle" :maxlength="200" />
            </el-form-item>
            <el-form-item label="色彩" prop="adcolorid">
              <el-select
                v-model="FoldPagePlanOne.adcolorid"
                placeholder="色彩"
                clearable
                style="width: 500px"
              >
                <el-option
                  v-for="(item, index) in Colordatalist"
                  :key="index"
                  :label="item.name"
                  :value="item.id!"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-row>
              <el-col :span="12">
                <el-form-item prop="publishdate" label="刊登日期">
                  <el-date-picker
                    v-model="FoldPagePlanOne.publishdate"
                    style="width: 180px"
                    type="date"
                    placeholder="开始日期"
                    :clearable="false"
                    value-format="YYYY-MM-DD"
                    @change="onisAfterTimeAdd"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item prop="stoptime" label="截版日期">
                  <el-date-picker
                    v-model="FoldPagePlanOne.stoptime"
                    style="width: 180px"
                    type="date"
                    :clearable="false"
                    placeholder="结束日期"
                    value-format="YYYY-MM-DD"
                    @change="onisAfterTimeAdd"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item prop="sremark" label="备注">
              <el-input v-model="FoldPagePlanOne.sremark" :maxlength="200" />
            </el-form-item>
            <el-form-item prop="pagenum" label="版号">
              <el-input v-model="FoldPagePlanOne.pagenum" :maxlength="200" />
            </el-form-item>
            <el-row>
              <el-col :span="6">
                <el-form-item label="设置标志" prop="adflag">
                  <el-checkbox v-model="FoldPagePlanOne.adflag"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="截版标志" prop="stopflag">
                  <el-checkbox v-model="FoldPagePlanOne.stopflag"></el-checkbox>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="媒体叠次" prop="colorlist">
              <el-row :gutter="20" style="margin-left: 3px">
                <el-col :span="13" class="lilistbox">
                  <li>媒体叠次</li>
                  <HgSingleZtree
                    v-model="FoldPagePlanOne.foldid"
                    :scopeflag="EHgSingleZtreeUrl.PRICE_GETMEDIAFOLDTREE"
                    :show-icon="true"
                    :treeparams="{ mediaType: 'paper' }"
                    @change="oncangefold"
                  ></HgSingleZtree>
                </el-col>
                <el-col :span="9" class="lilistbox" :offset="1">
                  <li>叠次版本</li>
                  <HgSingleZtree
                    v-model="FoldPagePlanOne.foldareaverid"
                    :scopeflag="EHgSingleZtreeUrl.PRICE_GETFOLDAREAVERTREELIST"
                    :treeparams="areavertreeparams"
                    @change="oncangefoldareaver"
                  ></HgSingleZtree>
                </el-col>
              </el-row>
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
import type {
  TAFoldPagePlan,
  TQFoldPagePlan,
  TAFoldPagePlanOne
} from '@/types/views/schedule/foldpageplan'
import type { TAPageColor } from '@/types/views/schedule/pagecolor'
import type { TAdcolorList } from '@/types/views/price/adcolor'
import { getPagecolorAllApi } from '@/api/schedule/pagecolor'
import {
  getFoldPageplanListApi,
  saveFoldPageplanApi,
  savePagePlaneByPageNumApi,
  updateFoldPageplanApi,
  deleteFoldPageplanApi,
  getFoldPageplanByIdApi
} from '@/api/schedule/foldpageplan'
import { getAdColorTreeListApi } from '@/api/price/adcolor'
import {
  required,
  hgFormatDate,
  tableHeaderColor,
  computTableHeight,
  isNumberStr
} from '@/utils/index'
import { getMediaDataComboByTypeApi } from '@/api/media/media'
import type { IAxios } from 'axios'
import modal from '@/plugins/modal'
import { EHgSingleZtreeUrl } from '@/types/common/enumindex'
import useUserStore from '@/store/modules/user'
const userStore = useUserStore()
import { IDataCombo } from '@/types/common/DataCombo'
import dayjs from 'dayjs'
import { data } from 'jquery'
defineOptions({
  name: 'FoldPagePlanDetail'
})
const props = defineProps<{ data?: TAFoldPagePlanOne; addshow: boolean }>()
/** 表格高度 */
const tableheight = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 数据总条数 */
const pageTotal = ref<number>(0)
/** 数据总条数 */
const pageTotallist = ref<number>(0)
/** 版面归属列表列表 */
const FoldPagePlanList = ref<TAFoldPagePlan[]>([])
/** 查询对象 */
const queryInfo = reactive<TQFoldPagePlan>({
  sort: 'pagenum',
  order: 'asc',
  startTime: '',
  endTime: '',
  pageSize: 20,
  page: 1,
  /**
   * 按周复制标志
   */
  bworkDup: '',
  /**
   * 叠次版本id
   */
  foldareaverid: '',
  /**
   * 叠次版本名称
   */
  foldareavername: '',
  /**
   * 叠次ID
   */
  foldid: '',
  /**
   * 叠次名称
   */
  foldname: '',
  /**
   * 媒体id
   */
  mediaid: '',
  /**
   * 媒体名称
   */
  medianame: '',
  /**
   * 媒体类型key
   */
  mediatypekey: 'paper',
  /**
   * 媒体类型
   */
  mediatypename: '报刊',
  /**
   * 版面色彩id
   */
  pagecolorid: ''
})
/** 默认颜色 */
const Coloridshow = ref({
  id: '',
  name: ''
})
/** 版面计划数据 */
const FoldPagePlanOne = ref<TAFoldPagePlanOne>({
  id: '',
  mediatypekey: 'paper',
  mediatypename: '报刊',
  mediaid: '',
  medianame: '',
  foldid: '',
  foldname: '',
  foldareaverid: '',
  foldareavername: '',
  publishdate: '',
  publishnum: 0,
  pagenum: 1,
  pagetitle: '',
  adcolorid: '',
  adcolorname: '',
  adflag: true,
  stoptime: '',
  stopflag: false,
  sremark: '',
  pagesizeid: '',
  pagesizename: '',
  ipagewidth: 0,
  ipageheight: 0
})
/** 编辑 dialog */
const dialogVisible = ref(false)
/**
 * 编辑显示隐藏
 * */
const edit = ref(false)
const FoldPagePlanFormRef = ref<FormInstance>()
const FoldPagePlanSelection = ref<TAFoldPagePlanOne[]>()
/**
 * 叠次版本id
 */
const areavertreeparams = ref<{ foldId: string | undefined }>({
  foldId: ''
})
/**  色彩参数 */
const Colordatalist = ref<TAdcolorList[]>([])
/** 当前时间 */
const nowtimedata = ref()
/**
 * 媒体列表
 */
const mediaCombo = ref<IDataCombo[]>([])
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<TAFoldPagePlanOne>>({
  adcolorid: [{ required: true, message: '请选择色彩结构', trigger: 'change' }],
  pagenum: [{ validator: isNumberStr, message: '请输入数字', trigger: 'change' }]
})

onMounted(() => {
  console.log(FoldPagePlanOne.value)
  FoldPagePlanOne.value = props.data!
  queryInfo.startTime = FoldPagePlanOne.value.publishdate?.toString()
  queryInfo.endTime = FoldPagePlanOne.value.stoptime?.toString()
  queryInfo.mediaid = FoldPagePlanOne.value.mediaid
  queryInfo.medianame = FoldPagePlanOne.value.medianame
  queryInfo.foldid = FoldPagePlanOne.value.foldid
  queryInfo.foldname = FoldPagePlanOne.value.foldname
  queryInfo.foldareaverid = FoldPagePlanOne.value.foldareaverid
  queryInfo.foldareavername = FoldPagePlanOne.value.foldareavername
  loaddata()
  colordata()
  getMediaDataCombo()
  nowtimedata.value = nowTime()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})
watch(
  () => props.data,
  (newValue, oldValue) => {
    FoldPagePlanOne.value = { ...newValue }
    queryInfo.startTime = FoldPagePlanOne.value.publishdate?.toString()
    queryInfo.endTime = FoldPagePlanOne.value.stoptime?.toString()
    queryInfo.mediaid = FoldPagePlanOne.value.mediaid
    queryInfo.medianame = FoldPagePlanOne.value.medianame
    queryInfo.foldid = FoldPagePlanOne.value.foldid
    queryInfo.foldname = FoldPagePlanOne.value.foldname
    queryInfo.foldareaverid = FoldPagePlanOne.value.foldareaverid
    queryInfo.foldareavername = FoldPagePlanOne.value.foldareavername
    loaddata()
  },
  { deep: true }
)
/**
 * @description: 获取媒体下拉
 * @return {*}
 */
const getMediaDataCombo = () => {
  getMediaDataComboByTypeApi({ type: 'paper' }).then((res: IAxios<IDataCombo[]>) => {
    if (res.success) {
      mediaCombo.value = res.obj
    }
  })
}
/**
 * @description: 开始日期结束日期比较如果开始日期小于结束日期则颠倒
 * @param {*} val
 * @return {*}
 */
const onisAfterTimeAdd = (val: string) => {
  const isAfter: boolean = dayjs(FoldPagePlanOne.value.publishdate).isAfter(
    FoldPagePlanOne.value.stoptime
  )
  const startTime = FoldPagePlanOne.value.publishdate
  const endTime = FoldPagePlanOne.value.stoptime
  if (isAfter) {
    FoldPagePlanOne.value.publishdate = endTime
    FoldPagePlanOne.value.stoptime = startTime
  }
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
 * 详情列表
 */
const loaddata = () => {
  getFoldPageplanListApi(queryInfo).then(({ obj, total }: IAxios<TAFoldPagePlan[]>) => {
    console.log(obj)
    FoldPagePlanList.value = obj
    FoldPagePlanOne.value = FoldPagePlanList.value[0]
    if (obj.length === 0) {
      FoldPagePlanOne.value = { ...props.data }
    }
    pageTotallist.value = total
  })
}

/**
 * 获取当前时间
 */
const nowTime = () => {
  const now = new Date()
  const year = now.getFullYear()
  const month = now.getMonth() + 1
  const day = now.getDate()
  return year + '-' + month + '-' + day
}

/**
 * 批量新增详情页
 */
const handleAddBatchdetail = () => {
  console.log(FoldPagePlanOne.value)
  ElMessageBox.confirm('确定要批量新增' + FoldPagePlanOne.value.pagenum + '个版吗', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    FoldPagePlanOne.value.adcolorid = Coloridshow.value.id
    FoldPagePlanOne.value.adcolorname = Coloridshow.value.name
    savePagePlaneByPageNumApi(FoldPagePlanOne.value).then(({ success, msg }: IAxios) => {
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
 * 修改
 * @param row
 */
const handleUpdate = (row: TAFoldPagePlanOne) => {
  FoldPagePlanOne.value = { ...row }
  areavertreeparams.value = {
    foldId: row.foldid
  }
  dialogVisible.value = true
}
/** 提交校验 */
const validFormSubmit = () => {
  if (!FoldPagePlanOne.value.mediaid) {
    ElMessage.error('请选择媒体')
    return false
  }
  if (!FoldPagePlanOne.value.foldid) {
    ElMessage.error('请选择叠次')
    return false
  }
  if (!FoldPagePlanOne.value.foldareaverid) {
    ElMessage.error('请选择叠次版本')
    return false
  }
  return true
}

/**
 * 保存
 */
const handleSave = () => {
  const validform = validFormSubmit()
  if (!validform) {
    return
  }
  ElMessageBox.confirm('确定要这样操作吗', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      FoldPagePlanFormRef.value?.validate((valid) => {
        if (valid) {
          Colordatalist.value.forEach((item) => {
            if (item.id === FoldPagePlanOne.value.adcolorid) {
              FoldPagePlanOne.value.adcolorname = item.name
            }
          })

          if (!FoldPagePlanOne.value.id) {
            saveFoldPageplanApi(FoldPagePlanOne.value).then(({ success, msg }: IAxios) => {
              if (success) {
                modal.msgSuccess('操作成功')
              } else {
                modal.msgError(msg)
              }
              loaddata()
            })
          } else {
            updateFoldPageplanApi(FoldPagePlanOne.value).then(({ success, msg }: IAxios) => {
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
  dialogVisible.value = false
  loaddata()
  areavertreeparams.value = {
    foldId: ''
  }
}
/**
 * 选择
 * @param rows
 */
const handleSelectionChange = (rows: TAFoldPagePlan[]) => {
  console.log(rows)
  FoldPagePlanSelection.value = rows
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
const handleDelete = (row?: TAFoldPagePlanOne) => {
  if (!row && !FoldPagePlanSelection.value?.length) {
    modal.msgWarning('请选择删除记录')
    return
  }
  modal.confirm('是否删除?').then(() => {
    const ids = []
    if (row) {
      ids.push(row.id)
    } else {
      FoldPagePlanSelection.value?.forEach((color) => ids.push(color.id))
    }
    if (!ids.length) {
      modal.msgWarning('请选择删除记录')
      return
    }
    deleteFoldPageplanApi(ids.join(',')).then(({ success, msg }: IAxios) => {
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
 * 叠次赋权
 */
const oncangefold = (data: ITreeNode) => {
  console.log(data)
  if (data !== undefined) {
    if (data.stype === 'media') {
      FoldPagePlanOne.value.mediaid = data.id
      FoldPagePlanOne.value.medianame = data.name
    }
    if (data.stype === 'fold') {
      FoldPagePlanOne.value.foldid = data.id
      FoldPagePlanOne.value.foldname = data.name
      mediaCombo.value.forEach((item) => {
        if (item.id === data.parentId) {
          FoldPagePlanOne.value.medianame = item.name
        }
      })
      FoldPagePlanOne.value.mediaid = data.parentId
      areavertreeparams.value = {
        foldId: data.id
      }
    }
  }
}
/**
 * 叠次版本赋权
 */
const oncangefoldareaver = (data: ITreeNode) => {
  console.log(data)
  if (data !== undefined) {
    FoldPagePlanOne.value.foldareaverid = data?.id ?? ''
    FoldPagePlanOne.value.foldareavername = data?.name ?? ''
  }
}
/**
 * 获取色彩列表
 */
const colordata = () => {
  getAdColorTreeListApi('paper').then(({ obj }: IAxios<TAdcolorList[]>) => {
    console.log(obj)
    Colordatalist.value = obj
    FoldPagePlanOne.value.adcolorid = obj[0].id
    Coloridshow.value.id = obj[0].id ?? ''
    Coloridshow.value.name = obj[0].name ?? ''
  })
}
/**
 * 色彩改变
 */
const colorchange = (row?: TAFoldPagePlanOne) => {
  const data = { ...row }
  Colordatalist.value.forEach((item) => {
    if (item.id === data.adcolorid) {
      data.adcolorname = item.name
    }
  })
  updateFoldPageplanApi(data).then(({ success, msg }: IAxios) => {
    if (success) {
      modal.msgSuccess('操作成功')
    } else {
      modal.msgError(msg)
    }
    loaddata()
    edit.value = !edit.value
  })
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
.lilistbox li {
  width: 100%;
  font-weight: bold;
  line-height: 40px;
}
</style>
