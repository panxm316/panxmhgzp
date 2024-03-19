<!--
 * @Author: wanghl
 * @Date: 2023-11-15 09:32:33
 * @LastEditTime: 2024-03-13 16:18:49
 * @LastEditors: wanghl
 * @Description:版面计划
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex" style="width: 94%">
        <el-button type="primary" icon="View" @click="handleSee()">详情</el-button>
        <el-button type="primary" icon="Plus" @click="handleAddBatch">新增</el-button>
        <HgSelectMedFoldFoldare @selectiondata="selectiondata" />

        <!-- <el-select
          v-model="queryInfo.mediaid"
          placeholder="选择媒体"
          clearable
          style="width: 140px"
          @change="loaddata"
        >
          <el-option
            v-for="item in mediaCombo"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select> -->
        <el-date-picker
          v-model="queryInfo.startTime"
          style="width: 140px"
          type="date"
          placeholder="开始日期"
          value-format="YYYY-MM-DD"
          @change="onisAfterTime"
        />
        <span style="margin-right: 10px; font-size: 14px; line-height: 30px">至</span>
        <el-date-picker
          v-model="queryInfo.endTime"
          style="width: 140px"
          type="date"
          placeholder="结束日期"
          value-format="YYYY-MM-DD"
          @change="onisAfterTime"
        />
        <el-button type="primary" icon="Refresh" @click="refresh()" />

        <el-tooltip class="item" effect="dark" content="帮助" placement="top">
          <el-button class="help" type="text" @click="showhelp()">?</el-button>
        </el-tooltip>
      </div>
    </div>
    <div class="table_box">
      <el-row>
        <el-table
          :data="FoldPagePlanQList"
          row-key="id"
          :height="tableheight"
          :border="true"
          stripe
          :header-cell-style="tableHeaderColor"
          @selection-change="handleSelectionChange"
          @sort-change="handleSortChange"
        >
          <el-table-column prop="publishdate" label="刊登日期" width="160" sortable="custom">
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
          <el-table-column prop="ipagewidth" label="报纸版心" min-width="160">
            <template #default="scope">
              <span> 宽{{ scope.row.ipagewidth }}--高{{ scope.row.ipageheight }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="publishnum" label="刊期" align="center" sortable="custom">
          </el-table-column>
          <el-table-column prop="pagenum" label="板数" width="100" align="center" sortable="custom">
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
                icon="edit"
                size="small"
                @click="handlepagesize(adproject.row)"
                >版心修改</el-button
              >
              <el-button
                link
                type="primary"
                icon="View"
                size="small"
                @click="handleSee(adproject.row)"
                >详情</el-button
              >
              <el-button
                link
                type="danger"
                icon="Delete"
                size="small"
                @click="handleDeleteBatch(adproject.row)"
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
    <!-- 批量新增 -->
    <el-dialog
      v-model="dialogVisibleBatch"
      title="版面计划批量添加"
      width="700"
      align-center
      :close-on-click-modal="false"
      draggable
      @close="handleCancelBatch"
    >
      <el-form
        ref="FoldPagePlanBatchFormRef"
        :model="FoldPagePlan"
        label-width="120px"
        :rules="rulesBatch"
        style="height: 520px"
      >
        <el-row :gutter="20">
          <el-col :span="22">
            <el-form-item label="选择">
              <el-radio-group v-model="FoldPagePlan.addType">
                <el-radio :label="'PageColor'">按色彩结构</el-radio>
                <el-radio :label="'Duplicate'">按时间段复制</el-radio>
                <el-radio :label="'PageNum'">按版数</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item
              v-if="FoldPagePlan.addType === 'PageColor'"
              label="色彩结构"
              prop="pagecolorid"
            >
              <el-select
                v-model="FoldPagePlan.pagecolorid"
                placeholder="色彩结构"
                style="width: 400px"
                @change="pagecolorchange"
              >
                <el-option
                  v-for="(item, index) in FoldPagePlandata"
                  :key="index"
                  :label="item.sname"
                  :value="item.id!"
                  >{{ item.sname }}</el-option
                >
              </el-select>
              <span style="margin-left: 10px">共{{ lsColorslsitnumber }}版</span>
            </el-form-item>
            <el-form-item label="报纸版心" prop="pagesizeid">
              <el-select
                v-model="FoldPagePlan.pagesizeid"
                placeholder="报纸版心"
                style="width: 500px"
                @change="pagecolorchange"
              >
                <el-option
                  v-for="(item, index) in PageSizeList"
                  :key="index"
                  :label="item.sname"
                  :value="item.id!"
                  >{{ item.sname }}---宽*高{{ item.ipagewidth }}*{{ item.ipageheight }}</el-option
                >
              </el-select>
            </el-form-item>
            <el-form-item v-if="FoldPagePlan.addType === 'PageNum'" label="版数" prop="pagenum">
              <el-input-number
                v-model="FoldPagePlan.pagenum"
                style="width: 500px"
                :min="1"
                :max="99"
                controls-position="right"
              />
            </el-form-item>
            <el-form-item v-if="FoldPagePlan.addType === 'Duplicate'" label="">
              <el-divider content-position="center">复制日期</el-divider>
            </el-form-item>
            <el-row>
              <el-col :span="12">
                <el-form-item prop="startTime" label="开始日期">
                  <el-date-picker
                    v-model="FoldPagePlan.startTime"
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
                <el-form-item prop="endTime" label="结束日期">
                  <el-date-picker
                    v-model="FoldPagePlan.endTime"
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
            <el-form-item v-if="FoldPagePlan.addType === 'Duplicate'" label="">
              <el-divider content-position="center">目标日期</el-divider>
            </el-form-item>
            <el-row v-if="FoldPagePlan.addType === 'Duplicate'">
              <el-col :span="12">
                <el-form-item prop="duplicateStartTime" label="开始时间">
                  <el-date-picker
                    v-model="FoldPagePlan.duplicateStartTime"
                    style="width: 180px"
                    type="date"
                    placeholder="开始时间"
                    :clearable="false"
                    value-format="YYYY-MM-DD"
                    @change="onisAfterTimeduplicate"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item prop="duplicateEndTime" label="结束时间">
                  <el-date-picker
                    v-model="FoldPagePlan.duplicateEndTime"
                    style="width: 180px"
                    type="date"
                    :clearable="false"
                    placeholder="结束时间"
                    value-format="YYYY-MM-DD"
                    @change="onisAfterTimeduplicate"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item
              v-if="FoldPagePlan.addType != 'Duplicate'"
              label="媒体叠次"
              prop="colorlist"
            >
              <el-row :gutter="20" style="margin-left: 3px">
                <el-col :span="13" class="lilistbox">
                  <li>媒体叠次</li>
                  <el-button
                    type="text"
                    icon="edit"
                    style="position: absolute; right: 240px; top: 5px"
                    size="small"
                    @click="handleAddfold"
                    >编辑</el-button
                  >
                  <HgSingleZtree
                    v-model="FoldPagePlan.foldid"
                    :scopeflag="EHgSingleZtreeUrl.PRICE_GETMEDIAFOLDTREE"
                    :show-icon="true"
                    :treeparams="{ mediaType: 'paper' }"
                    @change="oncangefold"
                  ></HgSingleZtree>
                </el-col>
                <el-col :span="9" class="lilistbox" :offset="1">
                  <li>叠次版本</li>
                  <HgSingleZtree
                    v-model="FoldPagePlan.foldareaverid"
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
          <el-button
            v-if="
              FoldPagePlan.addType === 'Duplicate' &&
              FoldPagePlan.startTime === FoldPagePlan.endTime
            "
            type="primary"
            icon="Check"
            @click="handleSaveCopy(false)"
            >按天复制</el-button
          >
          <el-button
            v-if="
              FoldPagePlan.addType === 'Duplicate' && FoldPagePlan.startTime != FoldPagePlan.endTime
            "
            type="primary"
            icon="Check"
            @click="handleSaveCopy(true)"
            >按周复制</el-button
          >
          <el-button
            v-if="FoldPagePlan.addType != 'Duplicate'"
            type="primary"
            icon="Check"
            @click="handleSaveBatch"
            >保存</el-button
          >
          <el-button icon="Close" @click="handleCancelBatch">取消</el-button>
        </div>
      </template>
    </el-dialog>
    <!-- 查看详情 -->
    <el-dialog
      v-model="dialogtable"
      title="计划详情"
      width="90%"
      align-center
      :close-on-click-modal="false"
      @close="handleCanceltable"
    >
      <FoldPagePlanDelete
        :data="FoldPagePlanOne"
        :addshow="addshow"
        style="margin-top: -20px"
      ></FoldPagePlanDelete>
    </el-dialog>
    <!-- 修改叠次版本 -->
    <el-dialog
      v-model="dialogfold"
      title="修改叠次---版本"
      width="90%"
      align-center
      :close-on-click-modal="false"
      @close="handleCancelfold"
    >
      <FoldData style="margin-top: -40px" :foldtype="'paper'"></FoldData>
    </el-dialog>
    <!-- 修改报纸版心 -->
    <el-dialog
      v-model="dialogpagesize"
      title="修改报纸版心"
      width="600"
      align-center
      :close-on-click-modal="false"
      @close="handleCancelpagesize"
    >
      <el-form :model="FoldPagePlanOne" label-width="120px">
        <el-form-item label="报纸版心" prop="pagesizeid">
          <el-select
            v-model="FoldPagePlanOne.pagesizeid"
            placeholder="报纸版心"
            style="width: 360px"
            @change="pagecolorchange"
          >
            <el-option
              v-for="(item, index) in PageSizeList"
              :key="index"
              :label="item.sname"
              :value="item.id!"
              >{{ item.sname }}---宽*高{{ item.ipagewidth }}*{{ item.ipageheight }}</el-option
            >
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button
            v-if="FoldPagePlanOne.pagesizeid !== ''"
            type="primary"
            icon="Check"
            @click="handleSavepagesize"
            >保存</el-button
          >
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import FoldPagePlanDelete from './FoldPagePlanDetail.vue'
import FoldData from '../price/index.vue'
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
  getMeiaPageplanListApi,
  saveBatchPagePlaneApi,
  deleteMediaPageplanApi,
  updatePagePlanePageSizeApi
} from '@/api/schedule/foldpageplan'
import { getPageSizeListApi } from '@/api/media/pagesize'
import type { TPageSize } from '@/types/views/media/pagesize'
import { getAdColorTreeListApi } from '@/api/price/adcolor'
import { required, hgFormatDate, tableHeaderColor, computTableHeight } from '@/utils/index'
import type { IAxios } from 'axios'
import modal from '@/plugins/modal'
import { EPageColor, EHgSingleZtreeUrl, EPaperType } from '@/types/common/enumindex'
import useUserStore from '@/store/modules/user'
const userStore = useUserStore()
import { useRouter } from 'vue-router'
import { getMediaDataComboByTypeApi } from '@/api/media/media'
import { IDataCombo } from '@/types/common/DataCombo'
import dayjs from 'dayjs'
/** 媒体选择组件,选择媒体，叠次，叠次版本 */
import HgSelectMedFoldFoldare from '@/components/HgSelectMedFoldFoldare/index.vue'
defineOptions({
  name: 'Foldpageplan'
})
const router = useRouter()
/** 组件添加显示隐藏 */
const addshow = ref(true)
/** 表格高度 */
const tableheight = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 数据总条数 */
const pageTotal = ref<number>(0)
/** 客户归属列表列表 */
const FoldPagePlanQList = ref<TAFoldPagePlan[]>([])
/** 查询全部 */
const queryInfo = reactive<TQFoldPagePlan>({
  sort: 'publishdate',
  order: 'desc',
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
/** 版数 */
const lsColorslsitnumber = ref(1)
/** 默认颜色 */
const Coloridshow = ref({
  id: '',
  name: ''
})
/**
 * 报纸版心下拉列表
 */
const PageSizeList = ref<TPageSize[]>([])
/** 版面计划数据 */
const FoldPagePlan = ref<TAFoldPagePlan>({
  mediatypekey: 'paper',
  mediatypename: '报刊',
  addType: 'PageColor',
  pagecolorid: '',
  startTime: '',
  endTime: '',
  duplicateStartTime: '',
  duplicateEndTime: '',
  bworkDup: false,
  id: '',
  mediaid: '',
  medianame: '',
  foldid: '',
  foldname: '',
  foldareaverid: '',
  foldareavername: '',
  publishdate: '',
  publishnum: 0,
  pagenum: 0,
  pagetitle: '',
  adcolorid: '',
  adcolorname: '',
  adflag: true,
  stoptime: '',
  stopflag: false,
  sremark: '',
  pagesizeid: '',
  pagesizename: ''
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
/** 批量添加 dialog */
const dialogVisibleBatch = ref(false)
/** 计划详情 dialog */
const dialogtable = ref(false)
const dialogfold = ref(false)
const dialogpagesize = ref(false)
const FoldPagePlanBatchFormRef = ref<FormInstance>()
const FoldPagePlanSelection = ref<TAFoldPagePlanOne[]>()
/**
 * 媒体列表
 */
const mediaCombo = ref<IDataCombo[]>([])
/**
 * 叠次版本id
 */
const areavertreeparams = ref<{ foldId: string | undefined }>({
  foldId: ''
})
/**  版面计划列表 */
const FoldPagePlandata = ref<TAPageColor[]>([])
/**  色彩参数 */
const Colordatalist = ref<TAdcolorList[]>([])
/** 当前时间 */
const nowtimedata = ref()
const rulesBatch = reactive<FormRules<TAFoldPagePlan>>({
  pagecolorid: [{ required: true, message: '请选择色彩结构', trigger: 'change' }],
  pagesizeid: [{ required: true, message: '请选报纸版心', trigger: 'change' }]
})
onMounted(() => {
  loaddata()
  getMediaDataCombo()
  FoldPagePlandatalist()
  colordata()
  getPageSizeList()
  nowtimedata.value = nowTime()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})
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
 * @description: 获取媒体，叠次，叠次版本组件传值
 */
const selectiondata = (data: TQFoldPagePlan) => {
  queryInfo.mediaid = data.mediaid
  queryInfo.medianame = data.medianame
  queryInfo.foldid = data.foldid
  queryInfo.foldname = data.foldname
  queryInfo.foldareaverid = data.foldareaverid
  queryInfo.foldareavername = data.foldareavername
  loaddata()
}
/**
 * @description: 开始日期结束日期比较如果开始日期小于结束日期则颠倒
 * @param {*} val
 * @return {*}
 */
const onisAfterTime = (val: string) => {
  const isAfter: boolean = dayjs(queryInfo.startTime).isAfter(queryInfo.endTime)
  const startTime = queryInfo.startTime
  const endTime = queryInfo.endTime
  if (isAfter) {
    queryInfo.startTime = startTime
    queryInfo.endTime = startTime
  }
  loaddata()
}
const onisAfterTimeAdd = (val: string) => {
  const isAfter: boolean = dayjs(FoldPagePlan.value.startTime).isAfter(FoldPagePlan.value.endTime)
  const startTime = FoldPagePlan.value.startTime
  const endTime = FoldPagePlan.value.endTime
  if (isAfter) {
    FoldPagePlan.value.startTime = startTime
    FoldPagePlan.value.endTime = startTime
  }
}
const onisAfterTimeduplicate = (val: string) => {
  const isAfter: boolean = dayjs(FoldPagePlan.value.duplicateStartTime).isAfter(
    FoldPagePlan.value.duplicateEndTime
  )
  const startTime = FoldPagePlan.value.duplicateStartTime
  const endTime = FoldPagePlan.value.duplicateEndTime
  if (isAfter) {
    FoldPagePlan.value.duplicateStartTime = startTime
    FoldPagePlan.value.duplicateEndTime = startTime
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
 * 全部列表
 */
const loaddata = () => {
  getMeiaPageplanListApi(queryInfo).then(({ obj, total }: IAxios<TAFoldPagePlan[]>) => {
    console.log(obj)
    FoldPagePlanQList.value = obj
    pageTotal.value = total
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
const nowTime1 = () => {
  const now = new Date()
  const year = now.getFullYear()
  const month = now.getMonth() + 1
  const day = now.getDate() + 1
  return year + '-' + month + '-' + day
}
/**
 * 查看版本详情
 */
const handleSee = (row?: TAFoldPagePlan) => {
  console.log(row)
  dialogtable.value = true

  if (row) {
    addshow.value = true
    FoldPagePlanOne.value.mediaid = row?.mediaid
    FoldPagePlanOne.value.medianame = row?.medianame
    FoldPagePlanOne.value.foldid = row?.foldid
    FoldPagePlanOne.value.foldname = row?.foldname
    FoldPagePlanOne.value.foldareaverid = row?.foldareaverid
    FoldPagePlanOne.value.foldareavername = row?.foldareavername
    FoldPagePlanOne.value.publishdate = row?.publishdate
    FoldPagePlanOne.value.stoptime = row?.publishdate
    areavertreeparams.value = {
      foldId: row?.foldid
    }
  } else {
    addshow.value = false
    FoldPagePlanOne.value.mediaid = queryInfo.mediaid
    FoldPagePlanOne.value.medianame = queryInfo.medianame
    FoldPagePlanOne.value.publishdate = queryInfo.startTime
    FoldPagePlanOne.value.stoptime = queryInfo.endTime
    FoldPagePlanOne.value.foldid = queryInfo.foldid
    FoldPagePlanOne.value.foldname = queryInfo.foldname
    FoldPagePlanOne.value.foldareaverid = queryInfo.foldareaverid
    FoldPagePlanOne.value.foldareavername = queryInfo.foldareavername
  }
  loaddata()
}
/**
 * 批量新增
 */
const handleAddBatch = () => {
  FoldPagePlan.value.startTime = nowtimedata.value
  FoldPagePlan.value.endTime = nowtimedata.value
  FoldPagePlan.value.duplicateStartTime = nowTime1()
  FoldPagePlan.value.duplicateEndTime = nowTime1()
  dialogVisibleBatch.value = true
}

/**
 * 新增叠次版本
 */
const handleAddfold = () => {
  dialogfold.value = true
}
/** 提交校验 */
const validFormSubmitBatch = () => {
  if (!FoldPagePlan.value.mediaid) {
    ElMessage.error('请选择媒体')
    return false
  }
  if (!FoldPagePlan.value.foldid) {
    ElMessage.error('请选择叠次')
    return false
  }
  if (!FoldPagePlan.value.foldareaverid) {
    ElMessage.error('请选择叠次版本')
    return false
  }
  return true
}
/**
 * 开始时间结束结束时间小于7天验证
 * */
const validFormSubmitBatchTime = () => {
  const startTime = dayjs(FoldPagePlan.value.startTime)
  const endTime = dayjs(FoldPagePlan.value.endTime)
  const duplicateStartTime = dayjs(FoldPagePlan.value.duplicateStartTime)
  const day = endTime.diff(startTime, 'day')
  const day2 = duplicateStartTime.diff(endTime, 'day')
  if (day > 7) {
    ElMessage.error('复制日期的开始时间结束时间间隔不能大于7天')
    return false
  }
  if (day2 < 1) {
    ElMessage.error('目标日期需要设置在复制日期之后')
    return false
  }
  return true
}
/**
 * 批量保存
 */
const handleSaveBatch = () => {
  const validform = validFormSubmitBatch()
  if (!validform) {
    return
  }
  ElMessageBox.confirm('确定要这样操作吗', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      if (FoldPagePlan.value.addType === 'PageNum') {
        FoldPagePlan.value.adcolorid = Coloridshow.value.id
        FoldPagePlan.value.adcolorname = Coloridshow.value.name
      }
      FoldPagePlan.value.duplicateStartTime = FoldPagePlan.value.startTime
      FoldPagePlan.value.duplicateEndTime = FoldPagePlan.value.endTime
      FoldPagePlanBatchFormRef.value?.validate((valid) => {
        if (valid) {
          saveBatchPagePlaneApi(FoldPagePlan.value).then(({ success, msg }: IAxios) => {
            if (success) {
              modal.msgSuccess('操作成功')
              loaddata()
            } else {
              modal.msgError(msg)
            }
          })
          dialogVisibleBatch.value = false
        }
      })
    })
    .catch(() => {})
}
/**
 * 批量复制
 */
const handleSaveCopy = (val: boolean) => {
  FoldPagePlan.value.bworkDup = val
  const validform = validFormSubmitBatchTime()
  if (!validform) {
    return
  }
  ElMessageBox.confirm('确定要这样操作吗', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      FoldPagePlanBatchFormRef.value?.validate((valid) => {
        if (valid) {
          saveBatchPagePlaneApi(FoldPagePlan.value).then(({ success, msg }: IAxios) => {
            if (success) {
              modal.msgSuccess('操作成功')
              loaddata()
            } else {
              modal.msgError(msg)
            }
          })
          dialogVisibleBatch.value = false
        }
      })
    })
    .catch(() => {})
}
/**
 * 报纸版心修改
 */
const handleSavepagesize = () => {
  ElMessageBox.confirm('确定要这样操作吗', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      updatePagePlanePageSizeApi(FoldPagePlanOne.value).then(({ success, msg }: IAxios) => {
        if (success) {
          modal.msgSuccess('操作成功')
        } else {
          modal.msgError(msg)
        }
        loaddata()
      })
      dialogpagesize.value = false
    })
    .catch(() => {})
}
/**
 * 修改报纸版心
 */
const handlepagesize = (row?: TAFoldPagePlanOne) => {
  console.log(row)
  FoldPagePlanOne.value = { ...row }
  FoldPagePlanOne.value.ipageheight = ''
  FoldPagePlanOne.value.ipagewidth = ''
  dialogpagesize.value = true
}
/**
 * 关闭批量添加
 */
const handleCancelBatch = () => {
  redet()
  dialogVisibleBatch.value = false
  loaddata()
  areavertreeparams.value = {
    foldId: ''
  }
  setTimeout(() => {
    FoldPagePlanBatchFormRef.value?.clearValidate(['pagesizeid'])
  }, 10)
}
/**
 * 关闭详情页
 */
const handleCanceltable = () => {
  dialogtable.value = false
  loaddata()
  areavertreeparams.value = {
    foldId: ''
  }
}
/**
 * 关闭修改叠次版本
 */
const handleCancelfold = () => {
  dialogfold.value = false
  dialogVisibleBatch.value = false
  loaddata()
  // router.go(0)
  window.location.reload()
  areavertreeparams.value = {
    foldId: ''
  }
}
/**
 * 关闭修改报纸版心
 */
const handleCancelpagesize = () => {
  dialogpagesize.value = false
  loaddata()
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
 * 批量删除
 * @param row
 */
const handleDeleteBatch = (row?: TAFoldPagePlan) => {
  modal.confirm('是否删除?').then(() => {
    deleteMediaPageplanApi(row).then(({ success, msg }: IAxios) => {
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
 * 关闭是清空数据
 */
const redet = () => {
  FoldPagePlan.value = {
    mediatypekey: 'paper',
    mediatypename: '报刊',
    addType: 'PageColor',
    pagecolorid: '',
    startTime: '',
    endTime: '',
    duplicateStartTime: '',
    duplicateEndTime: '',
    bworkDup: false,
    id: '',
    mediaid: '',
    medianame: '',
    foldid: '',
    foldname: '',
    foldareaverid: '',
    foldareavername: '',
    publishdate: '',
    publishnum: 0,
    pagenum: 0,
    pagetitle: '',
    adcolorid: '',
    adcolorname: '',
    adflag: true,
    stoptime: '',
    stopflag: false,
    sremark: '',
    pagesizeid: '',
    pagesizename: ''
  }
  FoldPagePlandatalist()
}
/**
 * 叠次赋权
 */
const oncangefold = (data: ITreeNode) => {
  console.log(data)
  if (data !== undefined) {
    if (data.stype === 'media') {
      FoldPagePlan.value.mediaid = data.id
      FoldPagePlan.value.medianame = data.name
    }
    if (data.stype === 'fold') {
      FoldPagePlan.value.foldid = data.id
      FoldPagePlan.value.foldname = data.name
      mediaCombo.value.forEach((item) => {
        if (item.id === data.parentId) {
          FoldPagePlan.value.medianame = item.name
        }
      })
      FoldPagePlan.value.mediaid = data.parentId
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
    FoldPagePlan.value.foldareaverid = data?.id ?? ''
    FoldPagePlan.value.foldareavername = data?.name ?? ''
  }
}
/**
 * 获色彩结构
 */
const FoldPagePlandatalist = () => {
  getPagecolorAllApi().then(({ obj }: IAxios<TAPageColor[]>) => {
    console.log(obj)
    FoldPagePlandata.value = obj
    FoldPagePlan.value.pagecolorid = obj[0].id
    lsColorslsitnumber.value = obj[0].lsColors?.length ?? 1
  })
}
/**
 * 获取色彩列表
 */
const colordata = () => {
  getAdColorTreeListApi('paper').then(({ obj }: IAxios<TAdcolorList[]>) => {
    console.log(obj)
    Colordatalist.value = obj
    FoldPagePlan.value.adcolorid = obj[0].id
    FoldPagePlan.value.adcolorname = obj[0].name
    Coloridshow.value.id = obj[0].id ?? ''
    Coloridshow.value.name = obj[0].name ?? ''
  })
}
/**
 * 色彩结构获取板数
 */
const pagecolorchange = () => {
  FoldPagePlandata.value.forEach((item) => {
    if (item.id === FoldPagePlan.value.pagecolorid) {
      lsColorslsitnumber.value = item.lsColors?.length ?? 1
    }
  })
}

// 刷新
const refresh = () => {
  loaddata()
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'Customer'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
/**
 * 获取报纸版心下拉列表
 */
const getPageSizeList = () => {
  getPageSizeListApi().then(({ obj }: IAxios<TPageSize[]>) => {
    console.log(obj)
    PageSizeList.value = obj
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
@/api/media/pagesize
