<!--
 * @Author: muyn
 * @Date: 2023-12-13
 * @Description: 补刊核查 前端视图页面
-->
<template>
  <div>
    <div id="OrderitemSupplementPublish" class="app-container">
      <el-row :gutter="5">
        <el-col :xs="6" :sm="5" :md="5" :lg="4" :xl="4">
          <div class="table_box left_box">
            <div class="fenge-box">
              <el-icon color="#409EFC"><star-filled /></el-icon><span>叠次版本</span
              ><el-icon color="#409EFC"><star-filled /></el-icon>
            </div>
            <div id="scrolly">
              <HgSingleZtree
                v-model="queryInfo.foldareaverid"
                :show-icon="true"
                :scopeflag="EHgSingleZtreeUrl.PRICE_GETMEDIAFOLDAREAVERTREE"
                :treeparams="{}"
                :treeheight="tableheight"
                @change="onPlanChange"
              ></HgSingleZtree>
            </div>
          </div>
        </el-col>
        <el-col :xs="18" :sm="19" :md="19" :lg="20" :xl="20">
          <div class="search_box">
            <div class="flex" style="width: 94%">
              <el-button type="success" icon="Edit" @click="handlePublish()">补刊确认</el-button>
              <el-button type="danger" icon="delete" @click="handleDelete()">删除</el-button>
              <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
              <el-select
                v-model="queryInfo.ibooktype"
                placeholder="录入方式"
                style="width: 160px"
                @change="loaddata"
              >
                <!-- <el-option label="正常" :value="0"></el-option>
                <el-option label="广告预约" :value="1"></el-option> -->
                <el-option label="快速预约" :value="2"></el-option>
                <el-option label="补刊" :value="3"></el-option>
              </el-select>
              <el-button type="primary" icon="Search" @click="loaddata">搜索</el-button>
              <el-tooltip class="item" effect="dark" content="帮助" placement="top">
                <el-button class="help" type="text" @click="showhelp()">?</el-button>
              </el-tooltip>
            </div>
          </div>
          <div class="table_box">
            <el-table
              :data="orderitemarrangeList"
              row-key="id"
              :border="true"
              stripe
              :height="tableheight"
              :header-cell-style="tableHeaderColor"
              @selection-change="handleSelectionChange"
              @sort-change="handleSortChange"
            >
              <el-table-column
                type="selection"
                fixed="left"
                prop="ischeck"
                :width="40"
                align="center"
              >
              </el-table-column>
              <el-table-column
                label="合同号"
                prop="scontractnum"
                min-width="140"
                align="center"
                show-overflow-tooltip
              >
                <template #default="scope">
                  <el-button
                    v-if="scope.row.spublishstatusName !== '预约'"
                    link
                    type="primary"
                    icon="view"
                    size="small"
                    @click="handleSee(scope.row, scope.$index)"
                    >{{ scope.row.scontractnum }}</el-button
                  >
                </template>
              </el-table-column>
              <el-table-column
                prop="customername"
                label="客户名称"
                show-overflow-tooltip
                width="120"
              >
              </el-table-column>
              <el-table-column
                prop="sadtitle"
                label="广告标题"
                sortable="custom"
                show-overflow-tooltip
                width="200"
              >
              </el-table-column>
              <el-table-column prop="dpublishdate" label="刊发时间" sortable width="90">
                <template #default="scope">
                  <span>{{ formatDatesm(scope.row.dpublishdate) }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="spublishstatusName" label="状态" width="80" align="center">
                <template #default="scope">
                  <el-tag
                    v-if="
                      scope.row.spublishstatusName === '预约' ||
                      scope.row.spublishstatusName === '预订' ||
                      scope.row.spublishstatusName === '待发布'
                    "
                    >{{ scope.row.spublishstatusName }}</el-tag
                  >
                  <el-tag
                    v-if="
                      scope.row.spublishstatusName === '安排' ||
                      scope.row.spublishstatusName === '见报' ||
                      scope.row.spublishstatusName === '已发布' ||
                      scope.row.spublishstatusName === '上架'
                    "
                    type="success"
                    >{{ scope.row.spublishstatusName }}</el-tag
                  >
                  <el-tag v-if="scope.row.spublishstatusName === '下架'" type="danger">{{
                    scope.row.spublishstatusName
                  }}</el-tag>
                </template>
              </el-table-column>
              <!-- <el-table-column prop="adcolorname" label="色彩" width="80"> </el-table-column>
              <el-table-column prop="adspecname" label="广告规格" width="110" show-overflow-tooltip>
              </el-table-column>
              <el-table-column prop="prefoldname" label="预定叠次" width="90" align="center">
              </el-table-column>
              <el-table-column prop="prefoldareavername" label="叠次版本" width="90">
              </el-table-column>
              <el-table-column prop="prefoldpageplanname" label="版面" width="80">
              </el-table-column> -->
              <el-table-column
                v-if="curMediaTypeKey === 'paper'"
                prop="adcolorname"
                label="色彩"
                width="70"
              >
              </el-table-column>
              <el-table-column
                v-if="curMediaTypeKey === 'app'"
                prop="adcolorname"
                label="广告样式"
                width="90"
              >
              </el-table-column>
              <el-table-column
                v-if="curMediaTypeKey === 'paper'"
                prop="adspecname"
                label="广告规格"
                width="120"
                show-overflow-tooltip
              >
              </el-table-column>
              <el-table-column
                v-if="curMediaTypeKey === 'app'"
                prop="adspecname"
                label="尺寸|周期"
                width="140"
                show-overflow-tooltip
              >
              </el-table-column>
              <el-table-column
                v-if="curMediaTypeKey === 'wei'"
                prop="adspecname"
                label="尺寸"
                width="120"
                show-overflow-tooltip
              >
              </el-table-column>
              <el-table-column
                v-if="curMediaTypeKey === 'paper'"
                prop="prefoldname"
                label="预定叠次"
                width="90"
              >
              </el-table-column>
              <el-table-column
                v-if="curMediaTypeKey === 'app'"
                prop="prefoldname"
                label="预定位置"
                width="90"
              >
              </el-table-column>
              <el-table-column
                v-if="curMediaTypeKey === 'wei'"
                prop="prefoldname"
                label="预定媒体"
                width="90"
              >
              </el-table-column>
              <el-table-column
                v-if="curMediaTypeKey === 'multi'"
                prop="prefoldname"
                label="项目分类"
                width="90"
              >
              </el-table-column>
              <el-table-column
                v-if="curMediaTypeKey === 'paper'"
                prop="prefoldareavername"
                label="叠次版本"
                align="center"
                width="90"
              >
              </el-table-column>
              <el-table-column
                v-if="curMediaTypeKey === 'app'"
                prop="prefoldareavername"
                label="频道|分类"
                align="center"
                width="120"
              >
              </el-table-column>
              <el-table-column
                v-if="curMediaTypeKey === 'multi'"
                prop="prefoldareavername"
                label="项目名称"
                align="center"
                width="120"
              >
              </el-table-column>
              <el-table-column
                v-if="curMediaTypeKey === 'paper'"
                prop="prefoldpageplanname"
                label="版面"
                width="80"
                align="center"
              >
              </el-table-column>
              <el-table-column
                prop="presremark"
                label="备注"
                align="center"
                width="120"
                show-overflow-tooltip
              >
              </el-table-column>

              <!-- <el-table-column
                prop="foldname"
                label="安排叠次"
                fixed="right"
                align="center"
                width="90"
              >
              </el-table-column>
              <el-table-column prop="foldareavername" label="叠次版本" fixed="right" width="90">
              </el-table-column>
              <el-table-column
                prop="foldpageplanname"
                label="安排版面"
                fixed="right"
                align="center"
                width="90"
              >
              </el-table-column> -->
              <el-table-column
                v-if="curMediaTypeKey === 'paper'"
                prop="foldname"
                label="安排叠次"
                fixed="right"
                align="center"
                min-width="90"
              >
              </el-table-column>
              <el-table-column
                v-if="curMediaTypeKey === 'app'"
                prop="foldname"
                label="安排位置"
                fixed="right"
                align="center"
                min-width="90"
              >
              </el-table-column>
              <el-table-column
                v-if="curMediaTypeKey === 'wei'"
                prop="foldname"
                label="安排媒体"
                fixed="right"
                align="center"
                min-width="90"
              >
              </el-table-column>
              <el-table-column
                v-if="curMediaTypeKey === 'multi'"
                prop="foldname"
                label="项目分类"
                fixed="right"
                align="center"
                min-width="90"
              >
              </el-table-column>
              <el-table-column
                v-if="curMediaTypeKey === 'paper'"
                prop="foldareavername"
                label="叠次版本"
                fixed="right"
                align="center"
                min-width="90"
              >
              </el-table-column>
              <el-table-column
                v-if="curMediaTypeKey === 'app'"
                prop="foldareavername"
                label="频道|分类"
                fixed="right"
                align="center"
                min-width="90"
              >
              </el-table-column>
              <!-- <el-table-column
              v-if="curMediaTypeKey === 'wei'"
              prop="foldareavername"
              label="叠次版本"
              fixed="right"
              align="center"
              width="90"
            >
            </el-table-column> -->
              <el-table-column
                v-if="curMediaTypeKey === 'multi'"
                prop="foldareavername"
                label="项目名称"
                fixed="right"
                align="center"
                min-width="90"
              >
              </el-table-column>
              <el-table-column
                v-if="curMediaTypeKey === 'paper'"
                prop="foldpageplanname"
                label="安排版面"
                fixed="right"
                align="center"
                min-width="90"
              >
              </el-table-column>
              <!-- <el-table-column
                prop="pagesortname"
                label="版面类别"
                width="90"
                show-overflow-tooltip
              >
              </el-table-column> -->
              <el-table-column
                v-if="curMediaTypeKey === 'paper'"
                prop="pagesortname"
                label="版面类别"
                min-width="100"
                show-overflow-tooltip
              >
              </el-table-column>
              <el-table-column
                v-if="curMediaTypeKey === 'app'"
                prop="pagesortname"
                label="内容|曝光率"
                min-width="100"
                show-overflow-tooltip
              >
              </el-table-column>
              <el-table-column
                v-if="curMediaTypeKey === 'wei'"
                prop="pagesortname"
                label="广告位置"
                min-width="100"
                show-overflow-tooltip
              >
              </el-table-column>
              <el-table-column prop="employname" label="业务员" min-width="90"> </el-table-column>
              <el-table-column prop="adtypename" label="广告类别" min-width="90"> </el-table-column>
              <!-- <el-table-column prop="  lastoperator" label="编辑人员" width="90"> </el-table-column> -->

              <el-table-column prop="dlastmodifydate" label="修改时间" width="90">
                <template #default="scope">
                  <span>{{ formatDatesm(scope.row.dlastmodifydate) }}</span>
                </template>
              </el-table-column>
              <el-table-column
                label="操作"
                align="center"
                min-width="120"
                class-name="small-padding fixed-width"
                fixed="right"
              >
                <template #default="scope">
                  <el-button
                    v-if="
                      scope.row.spublishstatusName === '预订' ||
                      scope.row.spublishstatusName === '待发布'
                    "
                    link
                    type="success"
                    icon="Edit"
                    size="small"
                    @click="handlePublish(scope.row)"
                    >确认</el-button
                  >
                  <el-button
                    v-if="
                      scope.row.spublishstatusName === '安排' ||
                      scope.row.spublishstatusName === '见报' ||
                      scope.row.spublishstatusName === '已发布'
                    "
                    link
                    type="warning"
                    icon="BottomLeft"
                    size="small"
                    @click="resetArrange(scope.row.orderitemid)"
                    >撤回</el-button
                  >
                  <el-button
                    v-if="scope.row.spublishstatusName === '预约'"
                    link
                    type="danger"
                    icon="delete"
                    size="small"
                    @click="handleDelete(scope.row)"
                    >删除</el-button
                  >
                </template>
              </el-table-column>
            </el-table>
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
        </el-col>
      </el-row>
    </div>
    <el-dialog
      v-model="dialogVisible"
      title="订单安排"
      align-center
      :close-on-click-modal="false"
      :draggable="fenbianlv() > 1440 ? true : false"
      width="500"
      @close="handleCancel"
    >
      <!-- <el-date-picker
        v-model="spublishdate"
        type="date"
        placeholder="预定日期"
        :clearable="true"
        value-format="YYYY-MM-DD"
        style="width: 100%"
        @change="onSelectDate"
      /> -->
      <HgSingleZtree
        v-if="curMediaTypeKey === 'paper'"
        v-model="orderitemarrangeData.foldpageplanid"
        style="margin-left: 10%; width: 80%"
        :show-icon="true"
        :scopeflag="EHgSingleZtreeUrl.PAGEPLANE_GETFOLDPAGEPLANETREELIST"
        :treeparams="orderitemreeparams"
        :treeheight="408"
        @change="onorderitemChange"
      ></HgSingleZtree>
      <HgSingleZtree
        v-if="curMediaTypeKey === 'wei'"
        v-model="orderitemarrangeData.foldid"
        style="margin-left: 10%; width: 80%"
        :show-icon="true"
        :scopeflag="EHgSingleZtreeUrl.PAGEPLANE_GETFOLDPAGEPLANETREELIST"
        :treeparams="orderitemreeparams"
        :treeheight="408"
        @change="onorderitemChange"
      ></HgSingleZtree>
      <HgSingleZtree
        v-if="curMediaTypeKey === 'app' || curMediaTypeKey === 'multi'"
        v-model="orderitemarrangeData.foldareaverid"
        style="margin-left: 10%; width: 80%"
        :show-icon="true"
        :scopeflag="EHgSingleZtreeUrl.PAGEPLANE_GETFOLDPAGEPLANETREELIST"
        :treeparams="orderitemreeparams"
        :treeheight="408"
        @change="onorderitemChange"
      ></HgSingleZtree>
      <div class="dialog-footer" style="text-align: center">
        <el-button type="primary" icon="Check" @click="handleSave">保存</el-button>
        <el-button icon="Close" @click="handleCancel">取消</el-button>
      </div>
    </el-dialog>
    <!-- 根据合同号查看详情 -->
    <AdTworderdetails ref="viewImageRef"></AdTworderdetails>
  </div>
</template>

<script setup lang="ts">
import useUserStore from '@/store/modules/user'
import { onMounted, reactive, ref } from 'vue'
import { IAxios } from 'axios'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { TOrderitemsupplementpublishSearch } from '@/types/views/schedule/orderitemsupplementpublish'
import { EHgSingleZtreeUrl } from '@/types/common/enumindex'
import {
  TOrderitemarrange,
  TOrderitemarrangedata,
  TOrderitemarrangeId
} from '@/types/views/schedule/orderitemarrange'
import {
  getOrderitemsupplementpublishListApi,
  getOrderitemsupplementpublishByIdApi,
  getOrderitemsupplementpublishPageListApi,
  saveOrderitemarrangeApi,
  deleteorderItemByIdApi,
  orderitemsupplementRecallApi
} from '@/api/schedule/orderitemsupplementpublish'
import modal from '@/plugins/modal'
import {
  required,
  computTableHeight,
  isNumberStr,
  formatDate,
  hgFormatDate,
  formatDatesm,
  tableHeaderColor,
  computTreeHeighttab,
  validatePhone,
  computTreeHeight,
  fenbianlv
} from '@/utils/index'
import { useRouter } from 'vue-router'
defineOptions({
  name: 'OrderitemSupplementPublish'
})
const router = useRouter()
const userStore = useUserStore()

/** 确认对象*/
const orderitemarrangeData = ref<TOrderitemarrangedata>({
  orderitemids: [],
  foldid: '',
  foldname: '',
  foldareaverid: '',
  foldareavername: '',
  foldpageplanid: '',
  foldpageplanname: '',
  editorid: '',
  editorname: ''
})
const selectItems = ref<TOrderitemarrangeId>({
  id: '',
  orderid: '',
  orderitemid: '',
  version: 0,
  nleftx: 0,
  ntopy: 0
})
const orderitemarrangeList = ref<TOrderitemarrange[]>([])
const orderitemarrangeSelection = ref<TOrderitemarrange[]>()
const orderitemarrangeSelectiondelete = ref<TOrderitemarrange[]>()
const queryInfo = reactive<TOrderitemsupplementpublishSearch>({
  sort: 'scontractnum',
  order: 'desc',
  pageSize: 20,
  page: 1,
  startTime: '',
  endTime: '',
  mediaid: '',
  foldid: '',
  foldareaverid: '',
  ibooktype: undefined,
  queryKey: '',
  scontractnum: '',
  mediatypekey: '',
  loginUserId: userStore.user?.userid.toString(),
  cacheDTOKey: ''
})
/** 表格高度 */
const tableheight = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 数据总条数 */
const pageTotal = ref<number>(0)
/** 核查 dialog */
const dialogVisible = ref(false)
/** 树高度 */
const treeheight = ref('')
/** 树查询参数 */
const orderitemreeparams = ref<{
  publishdate: string
  mediaId: string
  isShowPagenum: boolean
  mediatypekey: string
}>({
  publishdate: '',
  mediaId: '',
  mediatypekey: '',
  isShowPagenum: true
})
/** 媒体类型 */
const curMediaTypeKey = ref('paper')
/** 媒体id */
const curMediaId = ref('')
/** 预定日期 */
const spublishdate = ref('')
onMounted(() => {
  treeheight.value = computTreeHeight()
  tableheight.value = computTableHeight() + 5
})
/**
 * 获取总表列表
 */
const loaddata = () => {
  getOrderitemsupplementpublishPageListApi(queryInfo).then(
    ({ obj, total }: IAxios<TOrderitemarrange[]>) => {
      pageTotal.value = total
      console.log(obj)
      orderitemarrangeList.value = obj
      console.log(orderitemarrangeList.value)
    }
  )
}
/** 时间 */
const timedata = reactive<TDateType>({
  timetype: '6',
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
/**
 * 选择
 * @param rows
 */
const handleSelectionChange = (rows: TOrderitemarrange[]) => {
  /**不是预约的确认 */
  orderitemarrangeSelection.value = rows.filter((item) => item.spublishstatusName !== '预约')
  /**是预约的删除 */
  orderitemarrangeSelectiondelete.value = rows.filter((item) => item.spublishstatusName === '预约')
}
// 确认
const handlePublish = (row?: TOrderitemarrange) => {
  console.log(row)
  if (row) {
    orderitemreeparams.value = {
      publishdate: row?.dpublishdate as string,
      mediaId: curMediaId.value,
      mediatypekey: curMediaTypeKey.value,
      isShowPagenum: true
    }
    selectItems.value.id = row?.id as string
    selectItems.value.orderid = row?.orderid as string
    selectItems.value.orderitemid = row?.orderitemid as string
    selectItems.value.version = row?.version
    orderitemarrangeData.value.foldpageplanid = row?.prefoldpageplanid as string
    orderitemarrangeData.value.foldareaverid = row?.foldareaverid as string
    orderitemarrangeData.value.foldid = row?.foldid as string
    orderitemarrangeData.value.orderitemids?.push(selectItems.value)
    dialogVisible.value = true
  } else if (orderitemarrangeSelection.value !== undefined) {
    orderitemarrangeSelection.value.forEach((element) => {
      resetselectItems()
      if (element.id === null) {
        selectItems.value.id = ''
      } else {
        selectItems.value.id = element.id as string
      }
      selectItems.value.orderid = element.orderid as string
      selectItems.value.orderitemid = element.orderitemid as string
      selectItems.value.version = element.version
      orderitemarrangeData.value.orderitemids?.push(selectItems.value)
    })
    handleShow()
  } else {
    modal.msgWarning('请选择要安排的订单')
    return
  }
  // handleSave()
  // orderitemarrangeData.value.editorid = row?.employid as string
  // orderitemarrangeData.value.editorname = row?.employname as string
}
/**
 * 查询版面计划
 */
const onSelectDate = () => {
  orderitemreeparams.value = {
    publishdate: spublishdate.value,
    mediaId: '',
    mediatypekey: curMediaTypeKey.value,
    isShowPagenum: true
  }
}
/**
 *
 * @param data 安排时选择叠次版本
 */
const onorderitemChange = (data: ITreeNode) => {
  console.log(data)
  if (data === undefined) {
    return
  }
  if (data.stype === 'media') {
    orderitemarrangeData.value.foldpageplanid = ''
    orderitemarrangeData.value.foldpageplanname = ''
    orderitemarrangeData.value.foldareaverid = ''
    orderitemarrangeData.value.foldareavername = ''
    orderitemarrangeData.value.foldid = ''
    orderitemarrangeData.value.foldname = ''
    if (curMediaTypeKey.value === 'paper') {
      modal.msgWarning('请选择需要安排的版面')
    }
    if (curMediaTypeKey.value === 'app') {
      modal.msgWarning('请选择频道|分类')
    }
    return
  }
  if (data.stype === 'fold') {
    orderitemarrangeData.value.foldpageplanid = ''
    orderitemarrangeData.value.foldpageplanname = ''
    orderitemarrangeData.value.foldareaverid = ''
    orderitemarrangeData.value.foldareavername = ''
    orderitemarrangeData.value.foldid = data.id
    orderitemarrangeData.value.foldname = data.name as string
    if (curMediaTypeKey.value === 'paper') {
      modal.msgWarning('请选择需要安排的版面')
    }
    if (curMediaTypeKey.value === 'app') {
      modal.msgWarning('请选择频道|分类')
    }
    return
  }
  if (data.stype === 'foldarea') {
    orderitemarrangeData.value.foldpageplanid = ''
    orderitemarrangeData.value.foldpageplanname = ''
    const parentNode = data.getParentNode!()
    orderitemarrangeData.value.foldareaverid = data.id
    orderitemarrangeData.value.foldareavername = data.name as string
    orderitemarrangeData.value.foldid = parentNode.id
    orderitemarrangeData.value.foldname = parentNode.name as string
    if (curMediaTypeKey.value === 'paper') {
      modal.msgWarning('请选择需要安排的版面')
    }
    return
  }
  if (data.stype === 'pagenum') {
    orderitemarrangeData.value.foldpageplanid = data.id
    orderitemarrangeData.value.foldpageplanname = data.name as string
    const parentNode = data.getParentNode!()
    orderitemarrangeData.value.foldareaverid = parentNode.id
    orderitemarrangeData.value.foldareavername = parentNode.name as string
    const parentparentNode = parentNode.getParentNode!()
    orderitemarrangeData.value.foldid = parentparentNode.id
    orderitemarrangeData.value.foldname = parentparentNode.name as string
    return
  }
}
/**
 * 保存
 */
const handleSave = () => {
  console.log(orderitemarrangeData.value)
  if (curMediaTypeKey.value === 'paper') {
    if (
      orderitemarrangeData.value.foldpageplanid !== '' &&
      orderitemarrangeData.value.foldpageplanid !== 'null'
    ) {
      handleShow()
    } else {
      modal.msgWarning('请选择版面')
      return
    }
  } else if (curMediaTypeKey.value === 'app') {
    if (
      orderitemarrangeData.value.foldareaverid !== '' &&
      orderitemarrangeData.value.foldareaverid !== 'null'
    ) {
      handleShow()
    } else {
      modal.msgWarning('请选择频道|分类')
    }
  } else if (curMediaTypeKey.value === 'wei') {
    if (orderitemarrangeData.value.foldid !== '' && orderitemarrangeData.value.foldid !== 'null') {
      handleShow()
    } else {
      modal.msgWarning('请选择媒体')
      return
    }
  } else if (curMediaTypeKey.value === 'multi') {
    if (
      orderitemarrangeData.value.foldareaverid !== '' &&
      orderitemarrangeData.value.foldareaverid !== 'null'
    ) {
      handleShow()
    } else {
      modal.msgWarning('请选择项目名称')
      return
    }
  }
}
/**
 * 保存数据
 */
const handleShow = () => {
  saveOrderitemarrangeApi(orderitemarrangeData.value).then(({ obj, success, msg }: IAxios) => {
    if (success) {
      console.log(obj)
      modal.msgSuccess('操作成功')
    } else {
      modal.msgError(msg)
    }
    loaddata()
    dialogVisible.value = false
    reset()
  })
}
/**
 * 取消
 */
const handleCancel = () => {
  loaddata()
  dialogVisible.value = false
  reset()
}
/**
 * 关闭是清空数据
 */
const reset = () => {
  orderitemarrangeData.value = {
    orderitemids: [],
    foldid: '',
    foldname: '',
    foldareaverid: '',
    foldareavername: '',
    foldpageplanid: '',
    foldpageplanname: '',
    editorid: '',
    editorname: ''
  }
  resetselectItems()
}
const resetselectItems = () => {
  selectItems.value = {
    id: '',
    orderid: '',
    orderitemid: '',
    version: 0,
    nleftx: 0,
    ntopy: 0
  }
  orderitemreeparams.value = {
    publishdate: '',
    mediaId: '',
    mediatypekey: '',
    isShowPagenum: true
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
 * 版面计划树赋值
 */
const onPlanChange = (data: ITreeNode) => {
  console.log(data)

  if (data === undefined) {
    return
  }
  if (data.stype === 'media') {
    curMediaId.value = data.id
    queryInfo.mediaid = data.id
    queryInfo.foldid = ''
    queryInfo.foldareaverid = ''
    curMediaTypeKey.value = data.skey
  }
  if (data.stype === 'fold') {
    const parentNode = data.getParentNode!()
    curMediaId.value = parentNode.id
    queryInfo.mediaid = parentNode.id
    queryInfo.foldid = data.id
    queryInfo.foldareaverid = ''
    curMediaTypeKey.value = data.skey
    loaddata()
  }
  if (data.stype === 'foldareaver') {
    const parentNode = data.getParentNode!()
    /** 媒体 */
    const parentfoldNode = parentNode.getParentNode!()
    curMediaId.value = parentfoldNode.id
    queryInfo.mediaid = parentfoldNode.id
    queryInfo.foldid = parentNode.id
    queryInfo.foldareaverid = data.id
    curMediaTypeKey.value = data.skey
    loaddata()
  }
}
/**
 * 时间选择
 * @param val
 */
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
 * 删除
 * @param row
 */
const handleDelete = (row?: TOrderitemarrange) => {
  console.log(row)
  modal.confirm('是否删除?').then(() => {
    const ids = []
    if (row) {
      ids.push(row.orderitemid)
    } else {
      orderitemarrangeSelectiondelete.value?.forEach((item) => ids.push(item.orderitemid))
    }
    if (!ids.length) {
      modal.msgWarning('请选择删除记录')
      return
    }
    deleteorderItemByIdApi(ids.join(',')).then(({ success, msg }: IAxios) => {
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
 * 重置安排
 */
const resetArrange = (id: string) => {
  modal
    .confirm('是否取消安排？')
    .then(() => {
      orderitemsupplementRecallApi(id).then(() => {
        modal.msgSuccess('取消安排成功')
        loaddata()
      })
    })
    .catch(() => {})
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'Discount'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
/**
 * 根据合同号查看订单项目--------------------------------------------------
 * @param row
 * @param index
 */
import AdTworderdetails from '../ad/AdTworderdetails.vue'
const viewImageRef = ref()
const handleSee = (row?: any, number?: number) => {
  viewImageRef.value.view(row.scontractnum)
}
// ---------------------------------------------------------------------
</script>

<style scoped>
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 0px;
}

.box-card {
  width: 280px;
  max-height: 600px;
  overflow-y: auto;
}
.left_box {
  height: v-bind(treeheight);
}
</style>
