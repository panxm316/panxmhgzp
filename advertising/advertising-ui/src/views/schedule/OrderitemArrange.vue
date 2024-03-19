<!--
 * @Author: muyn
 * @Date: 2023-12-06
 * @Description: 广告安排 前端视图页面
-->
<template>
  <div id="OrderitemArrange" class="app-container">
    <el-row :gutter="5">
      <el-col :xs="6" :sm="5" :md="5" :lg="4" :xl="4">
        <div class="table_box left_box">
          <div class="fenge-box">
            <el-icon color="#409EFC"><star-filled /></el-icon><span>计划列表</span
            ><el-icon color="#409EFC"><star-filled /></el-icon>
          </div>
          <div id="scrolly">
            <el-date-picker
              v-model="spublishdate"
              type="date"
              placeholder="预定日期"
              :clearable="false"
              value-format="YYYY-MM-DD"
              style="width: 100%"
              @change="onSelectDate"
            />
            <HgSingleZtree
              v-model="queryInfo.foldareaverid"
              :show-icon="true"
              :scopeflag="EHgSingleZtreeUrl.PAGEPLANE_GETFOLDPAGEPLANETREELISTAll"
              :treeparams="plantreeparams"
              :treeheight="tableheight - 100"
              @change="onPlanChange"
            ></HgSingleZtree>
          </div>
        </div>
      </el-col>
      <el-col :xs="18" :sm="19" :md="19" :lg="20" :xl="20">
        <div class="search_box">
          <div class="flex" style="width: 94%">
            <el-button
              v-if="curMediaTypeKey === 'paper' && orderitemarrangechildren.length > 0"
              type="success"
              icon="Edit"
              @click="handleArrange()"
              >安排</el-button
            >
            <el-button
              v-if="curMediaTypeKey === 'app' && orderitemarrangeappchildren.length > 0"
              type="success"
              icon="Edit"
              @click="handleArrange()"
              >安排</el-button
            >
            <el-button
              v-if="curMediaTypeKey !== 'paper' && curMediaTypeKey !== 'app'"
              type="success"
              icon="Edit"
              @click="handleArrange()"
              >安排</el-button
            >
            <el-button
              v-if="curMediaTypeKey !== 'paper' && curMediaTypeKey !== 'app'"
              type="success"
              icon="Edit"
              @click="handleArrangeQuest()"
              >快速确定</el-button
            >
            <el-select
              v-model="queryInfo.adtypeid"
              placeholder="广告类型"
              style="width: 200px"
              clearable
              @clear="loaddata"
              @change="getadtypename"
            >
              <el-option
                v-for="item in adtypeCombo"
                :key="item.id"
                :label="item.sname"
                :value="item.id!"
              />
            </el-select>
            <span style="margin-top: 5px; font-size: 14px">编辑人员：</span>
            <HgZtreeSelect
              :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYPERSON"
              :selectids="queryInfo.editorid ? [queryInfo.editorid] : []"
              :filterable="true"
              :treeparams="{ bIgnorePermissions: true }"
              style="width: 160px"
              @selectionztree="onSelectionEmp"
            ></HgZtreeSelect>
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
              prop="ischeck"
              fixed="left"
              :width="40"
              align="center"
            >
            </el-table-column>
            <el-table-column
              label="合同号"
              prop="scontractnum"
              min-width="120"
              show-overflow-tooltip
            >
              <template #default="scope">
                <el-button
                  v-if="scope.row.scontractnum !== ''"
                  link
                  type="primary"
                  icon="view"
                  size="small"
                  @click="handleSee(scope.row, scope.$index)"
                  >{{ scope.row.scontractnum }}</el-button
                >
              </template>
            </el-table-column>
            <el-table-column prop="customername" label="客户名称" show-overflow-tooltip width="140">
            </el-table-column>
            <el-table-column
              prop="sadtitle"
              sortable="custom"
              label="广告标题"
              show-overflow-tooltip
              width="200"
            >
            </el-table-column>
            <el-table-column prop="dpublishdate" label="预刊时间" width="100">
              <template #default="scope">
                <span>{{ formatDatesm(scope.row.dpublishdate) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="addisplayformname" label="广告形式" width="80"></el-table-column>
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
              min-width="100"
              show-overflow-tooltip
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
            <el-table-column prop="presremark" label="订单备注" width="120" show-overflow-tooltip>
            </el-table-column>
            <el-table-column
              v-if="curMediaTypeKey === 'paper'"
              prop="foldname"
              label="安排叠次"
              fixed="right"
              align="center"
              width="90"
            >
            </el-table-column>
            <el-table-column
              v-if="curMediaTypeKey === 'app'"
              prop="foldname"
              label="安排位置"
              fixed="right"
              align="center"
              width="90"
            >
            </el-table-column>
            <el-table-column
              v-if="curMediaTypeKey === 'wei'"
              prop="foldname"
              label="安排媒体"
              fixed="right"
              align="center"
              width="90"
            >
            </el-table-column>
            <el-table-column
              v-if="curMediaTypeKey === 'multi'"
              prop="foldname"
              label="项目分类"
              fixed="right"
              align="center"
              min-width="100"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              v-if="curMediaTypeKey === 'paper'"
              prop="foldareavername"
              label="叠次版本"
              fixed="right"
              align="center"
              width="90"
            >
            </el-table-column>
            <el-table-column
              v-if="curMediaTypeKey === 'app'"
              prop="foldareavername"
              label="频道|分类"
              fixed="right"
              align="center"
              width="90"
            >
              <template #default="scope">
                <el-select
                  v-if="orderitemarrangeappchildren.length > 0"
                  v-model="scope.row.foldareaverid"
                  placeholder="频道|分类"
                  style="width: 80px"
                  size="small"
                  @change="handleArrange(scope.row, false)"
                >
                  <el-option
                    v-for="item in orderitemarrangeappchildren"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id!"
                  />
                </el-select>
                <span v-else>{{ scope.row.foldareavername }}</span>
              </template>
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
              min-width="100"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              v-if="curMediaTypeKey === 'paper'"
              prop="foldpageplanname"
              label="安排版面"
              fixed="right"
              align="center"
              width="100"
            >
              <template #default="scope">
                <!-- <el-select
                  v-if="orderitemarrangechildren.length > 0"
                  v-model="scope.row.foldpageplanid"
                  placeholder="版面"
                  style="width: 80px"
                  size="small"
                  @change="handleArrange(scope.row, false)"
                >
                  <el-option
                    v-for="item in orderitemarrangechildren"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id!"
                  />
                </el-select>
                <span v-else>{{ scope.row.foldpageplanname }}</span> -->
                <el-select
                  v-model="scope.row.foldpageplanid"
                  placeholder="版面"
                  style="width: 80px"
                  size="small"
                  @change="handleArrange(scope.row, false)"
                >
                  <el-option
                    v-for="item in scope.row.pageNumList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id!"
                  />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column
              prop="spublishstatusName"
              label="状态"
              width="80"
              fixed="right"
              align="center"
            >
              <template #default="scope">
                <el-tag
                  v-if="
                    scope.row.spublishstatusName === '预约' ||
                    scope.row.spublishstatusName === '预订'
                  "
                  >{{ scope.row.spublishstatusName }}</el-tag
                >
                <el-tag
                  v-if="
                    scope.row.spublishstatusName === '安排' ||
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
            <el-table-column
              v-if="curMediaTypeKey === 'paper'"
              prop="pagesortname"
              label="版面类别"
              width="100"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              v-if="curMediaTypeKey === 'app'"
              prop="pagesortname"
              label="内容|曝光率"
              width="100"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              v-if="curMediaTypeKey === 'wei'"
              prop="pagesortname"
              label="广告位置"
              width="100"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column prop="employname" label="业务员" width="100"> </el-table-column>
            <el-table-column prop="adtypename" label="广告类别" width="100"> </el-table-column>

            <el-table-column
              prop="editorname"
              fixed="right"
              label="编辑人员"
              width="90"
              align="center"
              show-overflow-tooltip
            >
            </el-table-column>

            <el-table-column prop="dlastmodifydate" label="修改时间" width="100">
              <template #default="scope">
                <span>{{ formatDatesm(scope.row.dlastmodifydate) }}</span>
              </template>
            </el-table-column>

            <el-table-column prop="sremark" label="安排备注" width="210" show-overflow-tooltip>
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
                  @click="handleArrange(scope.row, false)"
                ></el-button>
              </template>
            </el-table-column>
            <el-table-column
              label="操作"
              align="center"
              width="110"
              class-name="small-padding fixed-width"
              fixed="right"
            >
              <template #default="scope">
                <el-button
                  link
                  type="success"
                  icon="Edit"
                  size="small"
                  @click="handleArrange(scope.row, true)"
                  >安排</el-button
                >
                <el-button
                  v-if="scope.row.spublishstatusName !== '预订'"
                  link
                  type="danger"
                  icon="close"
                  size="small"
                  @click="resetArrange(scope.row.id)"
                  >取消</el-button
                >
                <!-- <el-button
                  link
                  type="danger"
                  icon="Delete"
                  size="small"
                  @click="handleShow(scope.$index, scope.row)"
                  >详情</el-button
                > -->
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
    <el-dialog
      v-model="dialogVisible"
      title="订单安排"
      align-center
      :close-on-click-modal="false"
      :draggable="fenbianlv() > 1440 ? true : false"
      width="700"
      @close="handleCancel"
    >
      <!-- <el-date-picker
      v-model="spublishdate"
      type="date"
      placeholder="预定日期"
      :clearable="true"
      value-format="YYYY-MM-DD"
      style="margin-left: 10%; width: 80%"
      @change="onorderitemDate"
    /> -->
      <div style="margin-bottom: 10px">
        <span style="margin-left: 5%; line-height: 30px">备注：</span>
        <el-input
          v-model="orderitemarrangeData.sremark"
          placeholder="请输入备注"
          clearable
          style="width: 80%"
        />
      </div>
      <el-row>
        <el-col v-if="isArrange === false" :span="12">
          <span style="margin-left: 13%; line-height: 30px">安排位置：</span>
          <el-select
            v-if="orderitempageNumList.length > 0 && curMediaTypeKey === 'paper'"
            v-model="orderitemarrangeData.foldpageplanid"
            placeholder="版面"
            style="width: 240px; margin-left: 40px"
            @change="paperArrange()"
          >
            <el-option
              v-for="item in orderitempageNumList"
              :key="item.id"
              :label="item.name"
              :value="item.id!"
            />
          </el-select>
          <el-select
            v-if="orderitemarrangeappchildren.length > 0 && curMediaTypeKey === 'app'"
            v-model="orderitemarrangeData.foldareaverid"
            placeholder="频道|分类"
            style="width: 240px; margin-left: 40px"
            @change="appArrange()"
          >
            <el-option
              v-for="item in orderitemarrangeappchildren"
              :key="item.id"
              :label="item.name"
              :value="item.id!"
            />
          </el-select>
          <HgSingleZtree
            v-if="curMediaTypeKey === 'wei'"
            v-model="orderitemarrangeData.foldid"
            style="margin-left: 13%; width: 80%"
            :show-icon="true"
            :scopeflag="EHgSingleZtreeUrl.PAGEPLANE_GETFOLDPAGEPLANETREELIST"
            :treeparams="orderitemreeparams"
            :treeheight="408"
            @change="onorderitemChange"
          ></HgSingleZtree>

          <HgSingleZtree
            v-if="curMediaTypeKey === 'multi'"
            v-model="orderitemarrangeData.foldareaverid"
            style="margin-left: 13%; width: 80%"
            :show-icon="true"
            :scopeflag="EHgSingleZtreeUrl.PAGEPLANE_GETFOLDPAGEPLANETREELIST"
            :treeparams="orderitemreeparams"
            :treeheight="408"
            @change="onorderitemChange"
          ></HgSingleZtree>
        </el-col>
        <el-col :span="isArrange === false ? 10 : 10" :class="isArrange === false ? '' : 'ml20'">
          <span style="line-height: 30px">编辑人员：</span>
          <HgZtree
            :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYPERSON"
            :selectids="orderitemarrangeData.editorid ? [orderitemarrangeData.editorid] : []"
            :filterable="true"
            style="width: 250px"
            :treeparams="{ bIgnorePermissions: true }"
            @selectionztree="onSelectioneditor"
          ></HgZtree>
        </el-col>
      </el-row>

      <div class="dialog-footer" style="text-align: center">
        <el-button v-if="isArrange === false" type="primary" icon="Check" @click="handleSave"
          >保存</el-button
        >
        <el-button v-if="isArrange === true" type="primary" icon="Check" @click="handleShow()"
          >保存</el-button
        >
        <el-button icon="Close" @click="handleCancel">取消</el-button>
      </div>
    </el-dialog>
    <!-- 根据合同号查看详情 -->
    <AdTworderdetails ref="viewImageRef"></AdTworderdetails>
  </div>
</template>

<script setup lang="ts">
import useUserStore from '@/store/modules/user'
import {
  TOrderitemarrange,
  TOrderitemarrangeSearch,
  TOrderitemarrangedata,
  TOrderitemarrangeId
} from '@/types/views/schedule/orderitemarrange'
import { getadtypelistApi } from '@/api/ad/adtype'
import { TAdtype } from '@/types/views/ad/adtype'
import {
  getOrderitemarrangePageListApi,
  getOrderitemarrangeByIdApi,
  saveOrderitemarrangeApi,
  updateOrderitemarrangeStatusApi
} from '@/api/schedule/orderitemarrange'
import { EHgDeptZtreeUrl } from '@/types/common/enumindex'
import { TSelectZtree } from '@/types/common'
import { useRouter } from 'vue-router'
import type { IAxios } from 'axios'
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
const userStore = useUserStore()
import { EHgSingleZtreeUrl } from '@/types/common/enumindex'
defineOptions({
  name: 'OrderitemArrange'
})
const queryInfo = reactive<TOrderitemarrangeSearch>({
  sort: 'scontractnum',
  order: 'desc',
  pageSize: 20,
  page: 1,
  dpublishdate: '',
  editorid: '',
  mediaid: '',
  foldid: '',
  foldareaverid: '',
  adtypeid: ''
})
/** 预定日期 */
const spublishdate = ref('')
/** 媒体类型 */
const curMediaTypeKey = ref('paper')
/** 媒体id */
const curMediaId = ref('')
/**
 * 安排查询数据参数
 */
const orderitemdate = ref('')
/** 树查询参数 */
const plantreeparams = ref<{
  publishdate: string
  mediaId: string
  isShowPagenum: boolean
  mediatypekey: string
}>({
  publishdate: '',
  mediaId: '',
  mediatypekey: 'paper',
  isShowPagenum: false
})
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
const router = useRouter()
const orderitemarrangeData = ref<TOrderitemarrangedata>({
  orderitemids: [],
  foldid: '',
  foldname: '',
  foldareaverid: '',
  foldareavername: '',
  foldpageplanid: '',
  foldpageplanname: '',
  editorid: '',
  editorname: '',
  sremark: ''
})
const orderitemarrangeList = ref<TOrderitemarrange[]>([])
const orderitemarrangeSelection = ref<TOrderitemarrange[]>()
/** 表格高度 */
const tableheight = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 数据总条数 */
const pageTotal = ref<number>(0)
/** 广告类型下拉 */
const adtypeCombo = ref<TAdtype[]>([])
/** 安排 dialog */
const dialogVisible = ref(false)
/** 树高度 */
const treeheight = ref('')
const selectItems = ref<TOrderitemarrangeId>({
  id: '',
  orderid: '',
  orderitemid: '',
  version: 0,
  nleftx: 0,
  ntopy: 0
})
type TotherParam = {
  bdefault?: boolean
  /** 查询关键字 */
  id?: string
  name?: string
}
/** 安排和快速安排区分false是普通安排 */
const isArrange = ref(false)
/** paper叠次版本下面带的版面 */
const orderitemarrangechildren = ref<ITreeNode[]>([])
/** paper叠次版本下面带的版面 */
const orderitempageNumList = ref<TotherParam[]>([])
/** app叠次版本下面带的版面 */
const orderitemarrangeappchildren = ref<ITreeNode[]>([])
onMounted(() => {
  treeheight.value = computTreeHeight()
  tableheight.value = computTableHeight() + 5
  getAdtypeCombo()
  spublishdate.value = nowTime()
  orderitemdate.value = nowTime()
  console.log(nowTime())
  // spublishdate.value = '2023-12-01'
  // getMediaType()
  nextTick(() => {
    onSelectDate()
  })
})
/**
 * 查询版面计划
 */
const onSelectDate = () => {
  queryInfo.dpublishdate = spublishdate.value
  plantreeparams.value = {
    publishdate: spublishdate.value,
    mediaId: '',
    mediatypekey: curMediaTypeKey.value,
    isShowPagenum: false
  }
  // getMediaType()
  // curMediaTypeKey.value = 'paper'
}
/**
 * 获取当前时间
 */
const nowTime = () => {
  const now = new Date()
  const year = now.getFullYear()
  const month = (now.getMonth() + 1).toString().padStart(2, '0')
  const day = now.getDate().toString().padStart(2, '0')
  return year + '-' + month + '-' + day
}
/**
 * 获取总表列表
 */
const loaddata = () => {
  getOrderitemarrangePageListApi(queryInfo).then(({ obj, total }: IAxios<TOrderitemarrange[]>) => {
    pageTotal.value = total
    orderitemarrangeList.value = []
    obj.forEach((item) => {
      if (item.addisplayformname !== '分类广告') {
        orderitemarrangeList.value.push(item)
      }
    })
    console.log(obj)
    // orderitemarrangeList.value = obj
    console.log(orderitemarrangeList.value)
  })
}
/**
 * 查询的时候选择业务人员
 * @param val
 */
const onSelectionEmp = (val: TSelectZtree[]) => {
  console.log(val)
  const employid = val.map((item) => item.id).join(',')
  queryInfo.editorid = employid
  loaddata()
}
/**
 * 查询的时候选择业务人员
 * @param val
 */
const onSelectioneditor = (val: TSelectZtree[]) => {
  console.log(val)
  const employid = val.map((item) => item.id).join(',')
  const employname = val.map((item) => item.name).join(',')
  orderitemarrangeData.value.editorid = employid
  orderitemarrangeData.value.editorname = employname
}
/**
 * 选择
 * @param rows
 */
const handleSelectionChange = (rows: TOrderitemarrange[]) => {
  console.log(rows)
  orderitemarrangeSelection.value = rows
}
// 安排，val为true是快速安排  false修改版面
const handleArrange = (row?: TOrderitemarrange, val?: boolean) => {
  if (curMediaTypeKey.value === 'paper' && val === true) {
    orderitempageNumList.value = row!.pageNumList as TotherParam[]
  }
  if (curMediaTypeKey.value === 'app' && orderitemarrangeappchildren.value.length === 0) {
    ElMessage.error('请选择栏目')
    return false
  }
  isArrange.value = false
  orderitemreeparams.value = {
    publishdate: spublishdate.value,
    mediaId: curMediaId.value,
    mediatypekey: curMediaTypeKey.value,
    isShowPagenum: true
  }
  console.log(row)
  if (row) {
    selectItems.value.id = row?.id as string
    selectItems.value.orderid = row?.orderid as string
    selectItems.value.orderitemid = row?.orderitemid as string
    selectItems.value.version = row?.version
    orderitemarrangeData.value.orderitemids?.push(selectItems.value)
    if (val === true) {
      orderitemarrangeData.value.editorid = row?.editorid as string
      orderitemarrangeData.value.editorname = row?.editorname as string
      orderitemarrangeData.value.sremark = row?.sremark as string
      orderitemarrangeData.value.foldpageplanid = row?.foldpageplanid as string
      orderitemarrangeData.value.foldpageplanname = row?.foldpageplanname as string
      orderitemarrangeData.value.foldareaverid = row?.prefoldareaverid as string
      orderitemarrangeData.value.foldareavername = row?.prefoldareavername as string
      orderitemarrangeData.value.foldid = row?.prefoldid as string
      orderitemarrangeData.value.foldname = row?.prefoldname as string
    } else {
      if (curMediaTypeKey.value === 'paper') {
        orderitemarrangeData.value.editorid = row?.editorid as string
        orderitemarrangeData.value.editorname = row?.editorname as string
        orderitemarrangeData.value.sremark = row?.sremark as string
        orderitemarrangeData.value.foldpageplanid = row?.foldpageplanid as string
        orderitemarrangechildren.value.forEach((item) => {
          if (item.id === row?.foldpageplanid) {
            orderitemarrangeData.value.foldpageplanname = item.name
          }
        })
        orderitemarrangeData.value.foldareavername = row?.foldareavername as string
        orderitemarrangeData.value.foldareaverid = row?.foldareaverid as string
        orderitemarrangeData.value.foldid = row?.foldid as string
        orderitemarrangeData.value.foldname = row?.foldname as string
      }
      if (curMediaTypeKey.value === 'app') {
        orderitemarrangeData.value.editorid = row?.editorid as string
        orderitemarrangeData.value.editorname = row?.editorname as string
        orderitemarrangeData.value.sremark = row?.sremark as string
        orderitemarrangeData.value.foldareaverid = row?.foldareaverid as string
        orderitemarrangeappchildren.value.forEach((item) => {
          if (item.id === row?.foldareaverid) {
            orderitemarrangeData.value.foldareavername = item.name
          }
        })
        orderitemarrangeData.value.foldid = row?.foldid as string
        orderitemarrangeData.value.foldname = row?.foldname as string
      }
      handleShow(val)
    }
  } else {
    val = true
    const ids = []
    orderitemarrangeSelection.value?.forEach((color) => ids.push(color.id))
    if (!ids.length) {
      modal.msgWarning('请选择要安排的订单')
      return
    } else {
      orderitempageNumList.value = orderitemarrangeSelection.value![0].pageNumList as TotherParam[]
      orderitemarrangeSelection.value!.forEach((element) => {
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
    }
  }
  if (val === true) {
    dialogVisible.value = true
  }
}
/**
 * 重置安排
 */
const resetArrange = (id: string) => {
  modal
    .confirm('是否取消安排？')
    .then(() => {
      updateOrderitemarrangeStatusApi(id).then(() => {
        modal.msgSuccess('取消安排成功')
        loaddata()
      })
    })
    .catch(() => {})
}
// 快速安排
const handleArrangeQuest = () => {
  isArrange.value = true
  orderitemreeparams.value = {
    publishdate: spublishdate.value,
    mediaId: curMediaId.value,
    mediatypekey: curMediaTypeKey.value,
    isShowPagenum: true
  }
  const ids = []
  orderitemarrangeSelection.value?.forEach((color) => ids.push(color.id))
  if (!ids.length) {
    modal.msgWarning('请选择要安排的订单')
    return
  } else {
    orderitemarrangeSelection.value!.forEach((element) => {
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
  }
  dialogVisible.value = true
}
/** 版面赋值 */
const paperArrange = () => {
  orderitemarrangechildren.value.forEach((item) => {
    if (item.id === orderitemarrangeData.value.foldpageplanid) {
      orderitemarrangeData.value.foldpageplanname = item.name
    }
  })
}
/** 频道|分类赋值 */
const appArrange = () => {
  orderitemarrangeappchildren.value.forEach((item) => {
    if (item.id === orderitemarrangeData.value.foldareaverid) {
      orderitemarrangeData.value.foldareavername = item.name
    }
  })
}
/**
 * 取消
 */
const handleCancel = () => {
  loaddata()
  reset()
  orderitempageNumList.value = [] as TotherParam[]
  dialogVisible.value = false
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
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'Discount'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
/**
 *
 * @param data 查询时选择叠次版本
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
    curMediaTypeKey.value = data.skey as string
    if (curMediaTypeKey.value === 'paper') {
      orderitemarrangechildren.value = [] as ITreeNode[]
    }
    if (curMediaTypeKey.value === 'app') {
      orderitemarrangeappchildren.value = [] as ITreeNode[]
    }
  }
  if (data.stype === 'fold') {
    const parentNode = data.getParentNode!()
    curMediaId.value = parentNode.id
    queryInfo.mediaid = parentNode.id
    queryInfo.foldid = data.id
    queryInfo.foldareaverid = ''
    curMediaTypeKey.value = data.skey as string
    if (curMediaTypeKey.value === 'paper') {
      orderitemarrangechildren.value = [] as ITreeNode[]
    }
    if (curMediaTypeKey.value === 'app') {
      orderitemarrangeappchildren.value = data.children as ITreeNode[]
    }
  }
  if (data.stype === 'foldarea') {
    const parentNode = data.getParentNode!()
    /** 媒体 */
    const parentfoldNode = parentNode.getParentNode!()
    curMediaId.value = parentfoldNode.id
    queryInfo.mediaid = parentfoldNode.id
    queryInfo.foldid = parentNode.id
    queryInfo.foldareaverid = data.id
    curMediaTypeKey.value = data.skey as string
    if (curMediaTypeKey.value === 'paper') {
      orderitemarrangechildren.value = data.children as ITreeNode[]
    }
  }
  loaddata()
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
 * 获取广告类别
 */
const getAdtypeCombo = () => {
  getadtypelistApi().then(({ success, obj }: IAxios<TAdtype[]>) => {
    if (success) {
      adtypeCombo.value = obj
      // queryInfo.adtypeid = obj[0].id
    }
  })
}
/**
 * 给广告类别名称赋值
 */
const getadtypename = () => {
  loaddata()
}
/**
 * 保存
 */
const handleSave = () => {
  console.log(orderitemarrangeData.value)
  if (curMediaTypeKey.value === 'paper') {
    // if (
    //   orderitemarrangeData.value.foldpageplanid !== '' &&
    //   orderitemarrangeData.value.foldpageplanid !== 'null'
    // ) {
    //   handleShow()
    // } else {
    //   modal.msgWarning('请选择版面')
    //   return
    // }
    handleShow()
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
const handleShow = (val?: boolean) => {
  console.log(orderitemarrangeData.value)
  saveOrderitemarrangeApi(orderitemarrangeData.value).then(({ success, msg }: IAxios) => {
    if (success) {
      modal.msgSuccess('操作成功')
    } else {
      modal.msgError(msg)
    }
    loaddata()
  })
  if (val === true || val === undefined) {
    dialogVisible.value = false
  }
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
  orderitemreeparams.value = {
    publishdate: '',
    mediaId: '',
    mediatypekey: '',
    isShowPagenum: true
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
}
/**
 * 获取媒体类型下拉列表
 */
const getMediaType = () => {
  getMediaTypeComboApi().then((res: IAxios<TAmedia[]>) => {
    if (res.success) {
      /** 遍历 */
      res.obj = res.obj.filter((item) => {
        return item.id === 'paper' || item.id === 'app' || item.id === 'wei' || item.id === 'multi'
      })
      mediaTypeCombo.value = res.obj
      curMediaTypeKey.value = res.obj[0].id as string
    }
  })
}
/**
 * 给媒体类型名称赋值
 */
const getMediaTypename = () => {
  plantreeparams.value = {
    publishdate: spublishdate.value as string,
    mediaId: '',
    mediatypekey: curMediaTypeKey.value as string,
    isShowPagenum: false
  }
}
import { getMediaTypeComboApi } from '@/api/media/mediatype'
import { TAmedia } from '@/types/views/media/media'
import { fa, tr } from 'element-plus/es/locale'
/**
 * 媒体类别下拉列表
 */
const mediaTypeCombo = ref<TAmedia[]>()
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
