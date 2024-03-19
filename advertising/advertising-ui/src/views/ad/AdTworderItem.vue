<!--
 * @Author: wanghl
 * @Date: 2023-11-30 09:44:40
 * @LastEditTime: 2024-03-14 19:09:19
 * @LastEditors: wanghl
 * @Description:AdTworder广告预定明细
-->
<template>
  <div class="app-container">
    <div>
      <div style="width: 100%; margin: -10px 0 10px 5px">
        <span v-if="props.data?.employid === ''" style="color: #e6a23c"
          >填写明细，请先选择客户</span
        >
        <div v-if="props.bottomshow !== false" style="display: flex">
          <el-button
            v-if="props.data?.employid !== ''"
            type="primary"
            icon="Plus"
            @click="handleAdd"
            >新增明细</el-button
          >
          <el-button
            v-if="props.data?.employid !== ''"
            type="danger"
            icon="delete"
            @click="handleDeleteBatch"
            >删除</el-button
          >
          <span v-if="props.data?.employid !== ''" style="color: #232323; margin: 5px 10px 0 20px"
            >复制时间段:</span
          >
          <div v-if="props.idbukan === 3">
            <el-date-picker
              v-if="props.data?.employid !== ''"
              v-model="copydata.copystartTime"
              style="width: 140px"
              type="date"
              :disabled-date="disabledDate"
              :clearable="false"
              placeholder="开始日期"
              value-format="YYYY-MM-DD"
              @change="onisAfterTime"
            />
            <span
              v-if="props.data?.employid !== ''"
              style="margin-right: 10px; font-size: 14px; line-height: 30px"
              >至</span
            >
            <el-date-picker
              v-if="props.data?.employid !== ''"
              v-model="copydata.copyendTime"
              style="width: 140px"
              :disabled-date="disabledDate"
              type="date"
              :clearable="false"
              placeholder="结束日期"
              value-format="YYYY-MM-DD"
              @change="onisAfterTime"
            />
          </div>
          <div v-if="props.idbukan === 0">
            <el-date-picker
              v-if="props.data?.employid !== ''"
              v-model="copydata.copystartTime"
              style="width: 140px"
              type="date"
              :clearable="false"
              placeholder="开始日期"
              value-format="YYYY-MM-DD"
              @change="onisAfterTime"
            />
            <span
              v-if="props.data?.employid !== ''"
              style="margin-right: 10px; font-size: 14px; line-height: 30px"
              >至</span
            >
            <el-date-picker
              v-if="props.data?.employid !== ''"
              v-model="copydata.copyendTime"
              style="width: 140px"
              type="date"
              :clearable="false"
              placeholder="结束日期"
              value-format="YYYY-MM-DD"
              @change="onisAfterTime"
            />
          </div>
        </div>
      </div>
      <el-row :span="22">
        <el-table
          :data="Worderitemlsit"
          row-key="id"
          :border="true"
          stripe
          :max-height="400"
          :summary-method="getSummaries"
          show-summary
          :header-cell-style="tableHeaderColor"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" prop="ischeck" :width="60" align="center">
          </el-table-column>

          <el-table-column prop="index" :width="70" label="序号" align="center">
            <template #default="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="sadtitle"
            label="广告明细标题"
            min-width="250"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column prop="iitemcode" label="编码" align="center" width="60">
          </el-table-column>
          <el-table-column prop="iapprovestatus" label="状态" align="center" width="60">
            <template #default="scope">
              <span>{{ scope.row.iapprovestatus === 0 ? '未审核' : '已审核' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="prestartdate" label="预刊时间" sortable width="120">
            <template #default="scope">
              <span>{{ formatDatesm(scope.row.prestartdate) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="baseprice"
            label="刊例价"
            sortable="custom"
            width="120"
            align="right"
          >
            <template #default="scope">
              <span>{{ formatMoney(scope.row.baseprice, '2') }}</span>
            </template>
          </el-table-column>

          <el-table-column
            prop="normalprice"
            align="right"
            label="标准价格"
            sortable="custom"
            width="120"
          >
            <template #default="scope">
              <span>{{ formatMoney(scope.row.normalprice, '2') }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="ndiscountrate"
            label="折扣率"
            align="right"
            sortable="custom"
            width="120"
          >
            <template #default="scope">
              <span>{{ parseFloat(scope.row.ndiscountrate).toFixed(2) }}%</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="amountreceivable"
            label="签订金额"
            align="right"
            sortable="custom"
            width="120"
          >
            <template #default="scope">
              <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="medianame" label="媒体" min-width="100" show-overflow-tooltip>
          </el-table-column>
          <el-table-column prop="foldname" label="叠次版本" min-width="100" show-overflow-tooltip>
          </el-table-column>
          <el-table-column
            prop="foldareavername"
            label="版面类别"
            min-width="100"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column
            prop="addisplayformname"
            label="广告形式"
            min-width="100"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column prop="adspecname" label="广告规格" min-width="100" show-overflow-tooltip>
          </el-table-column>
          <el-table-column prop="nwidth" label="宽度" min-width="100" show-overflow-tooltip>
          </el-table-column>
          <el-table-column prop="nheight" label="高度" min-width="100" show-overflow-tooltip>
          </el-table-column>
          <el-table-column prop="adcolorname" label="色彩" min-width="100" show-overflow-tooltip>
          </el-table-column>
          <el-table-column
            prop="weeksettingname"
            label="星期"
            min-width="100"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column
            prop="createempname"
            label="创建者"
            min-width="100"
            show-overflow-tooltip
          >
          </el-table-column>

          <el-table-column prop="sremark" label="备注" min-width="160" show-overflow-tooltip>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="180"
            class-name="small-padding fixed-width"
            fixed="right"
          >
            <template #default="scope">
              <el-button
                link
                type="success"
                icon="Edit"
                size="small"
                @click="handleUpdate(scope.$index, scope.row)"
                >修改</el-button
              >
              <el-button
                v-if="props.bottomshow !== false"
                link
                type="primary"
                icon="Edit"
                size="small"
                @click="handcopybelong(scope.$index, scope.row)"
                >复制</el-button
              >
              <el-button
                v-if="props.bottomshow !== false"
                link
                type="danger"
                icon="Delete"
                size="small"
                @click="handleDelete(scope.$index, scope.row)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </el-row>
    </div>

    <!-- 订单明细 -->
    <el-dialog
      v-model="Worderitemshow"
      title="订单明细"
      width="1200"
      align-center
      :destroy-on-close="true"
      :draggable="fenbianlv() > 1440 ? true : false"
      @close="handleCancel"
    >
      <el-form ref="TwordwrFormRef" :model="Worderitem" label-width="100px" :rules="rules">
        <el-row style="margin-right: 40px">
          <el-col :span="8">
            <el-form-item prop="prestartdate" label="预刊时间">
              <el-date-picker
                v-if="props.idbukan === 3"
                v-model="Worderitem.prestartdate"
                type="date"
                :disabled-date="disabledDate"
                placeholder="预刊时间"
                :clearable="false"
                value-format="YYYY-MM-DD"
                style="width: 100%"
                @change="onisAfterTimeduplicate"
              />
              <el-date-picker
                v-if="props.idbukan === 0"
                v-model="Worderitem.prestartdate"
                type="date"
                placeholder="预刊时间"
                :clearable="false"
                value-format="YYYY-MM-DD"
                style="width: 100%"
                @change="onisAfterTimeduplicate"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="明细标题" prop="sadtitle">
              <el-input v-model="Worderitem.sadtitle" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="广告内容" prop="sadcontent">
              <el-input v-model="Worderitem.sadcontent" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="margin-left: 30px">
          <el-col :span="24">
            <el-space wrap :size="8">
              <div>
                <!-- 叠次 -->
                <el-card class="cardbox" style="width: 260px">
                  <HgSingleZtree
                    v-if="curMediaTypeKeyshow === 'paper' && foldpageplanidshow === true"
                    v-model="Worderitem.foldpageplanid"
                    :show-icon="true"
                    :scopeflag="EHgSingleZtreeUrl.PAGEPLANE_GETFOLDPAGEPLANETREELISTAll"
                    :treeparams="plantreeparams"
                    :treeheight="220"
                    @change="onPlanChange"
                  ></HgSingleZtree>
                  <HgSingleZtree
                    v-if="curMediaTypeKeyshow === 'paper' && foldpageplanidshow === false"
                    v-model="Worderitem.foldareaverid"
                    :show-icon="true"
                    :scopeflag="EHgSingleZtreeUrl.PAGEPLANE_GETFOLDPAGEPLANETREELISTAll"
                    :treeparams="plantreeparams"
                    :treeheight="220"
                    @change="onPlanChange"
                  ></HgSingleZtree>
                  <HgSingleZtree
                    v-if="curMediaTypeKeyshow === 'wei'"
                    v-model="Worderitem.foldid"
                    :show-icon="true"
                    :scopeflag="EHgSingleZtreeUrl.PAGEPLANE_GETFOLDPAGEPLANETREELISTAll"
                    :treeparams="plantreeparams"
                    :treeheight="220"
                    @change="onPlanChange"
                  ></HgSingleZtree>
                  <HgSingleZtree
                    v-if="curMediaTypeKeyshow === 'multi'"
                    v-model="Worderitem.foldareaverid"
                    :show-icon="true"
                    :scopeflag="EHgSingleZtreeUrl.PAGEPLANE_GETFOLDPAGEPLANETREELISTAll"
                    :treeparams="plantreeparams"
                    :treeheight="220"
                    @change="onPlanChange"
                  ></HgSingleZtree>
                  <HgSingleZtree
                    v-if="curMediaTypeKeyshow === 'app' && foldareaidshow === true"
                    v-model="Worderitem.foldareaverid"
                    :show-icon="true"
                    :scopeflag="EHgSingleZtreeUrl.PAGEPLANE_GETFOLDPAGEPLANETREELISTAll"
                    :treeparams="plantreeparams"
                    :treeheight="220"
                    @change="onPlanChange"
                  ></HgSingleZtree>
                  <HgSingleZtree
                    v-if="curMediaTypeKeyshow === 'app' && foldareaidshow === false"
                    v-model="Worderitem.foldid"
                    :show-icon="true"
                    :scopeflag="EHgSingleZtreeUrl.PAGEPLANE_GETFOLDPAGEPLANETREELISTAll"
                    :treeparams="plantreeparams"
                    :treeheight="220"
                    @change="onPlanChange"
                  ></HgSingleZtree>
                </el-card>
              </div>
              <div v-if="EMediaType[curMediaTypeKey].pagetypeName">
                <!-- 版本类别 -->
                <el-card class="cardbox">
                  <div>
                    <span>{{ EMediaType[curMediaTypeKey].pagetypeName }}</span>
                    <el-button
                      v-if="Worderitem.pagesortid !== ''"
                      type="primary"
                      link
                      @click="onPagesortCancel()"
                      >取消</el-button
                    >
                  </div>
                  <HgSingleZtree
                    v-model="Worderitem.pagesortid"
                    :scopeflag="EHgSingleZtreeUrl.PRICE_GETPAGESORTTREELIST"
                    :treeparams="pagesortparams"
                    :treeheight="200"
                    @change="onPagetypeChange"
                  ></HgSingleZtree>
                </el-card>
              </div>
              <div v-if="EMediaType[curMediaTypeKey].addisplayformName">
                <!-- 广告形式 -->
                <el-card class="cardbox">
                  <div>
                    <span>{{ EMediaType[curMediaTypeKey].addisplayformName }}</span>
                    <el-button
                      v-if="Worderitem.addisplayformid !== ''"
                      type="primary"
                      link
                      @click="onAddisplayCancel()"
                      >取消</el-button
                    >
                  </div>
                  <HgSingleZtree
                    v-model="Worderitem.addisplayformid"
                    :scopeflag="EHgSingleZtreeUrl.PRICE_GETADDISPLAYFORMTREELIST"
                    :treeparams="{ mediaType: Worderitem.mediatypekey!}"
                    :treeheight="200"
                    @change="onAddisplayChange"
                  ></HgSingleZtree>
                </el-card>
              </div>
              <div v-if="EMediaType[curMediaTypeKey].adspecName">
                <!-- 广告规格 -->
                <el-card class="cardbox">
                  <div>
                    <span>{{ EMediaType[curMediaTypeKey].adspecName }}</span>
                    <el-button
                      v-if="Worderitem.adspecid !== ''"
                      type="primary"
                      link
                      @click="onAdspecCancel()"
                      >取消</el-button
                    >
                  </div>
                  <HgSingleZtree
                    v-model="Worderitem.adspecid"
                    :scopeflag="EHgSingleZtreeUrl.PRICE_GETADSPECTREELIST"
                    :treeparams="{ mediaId: Worderitem.mediaid }"
                    :treeheight="200"
                    @change="onAdspecChange"
                  ></HgSingleZtree>
                </el-card>
              </div>
              <div v-if="EMediaType[curMediaTypeKey].adcolorName">
                <!-- 色彩 -->
                <el-card class="cardbox">
                  <div>
                    <span>{{ EMediaType[curMediaTypeKey].adcolorName }}</span>
                    <el-button
                      v-if="Worderitem.adcolorid !== ''"
                      type="primary"
                      link
                      @click="onAdcolorCancel()"
                      >取消</el-button
                    >
                  </div>
                  <HgSingleZtree
                    v-model="Worderitem.adcolorid"
                    :scopeflag="EHgSingleZtreeUrl.PRICE_GETADCOLORTREELIST"
                    :treeheight="curMediaTypeKey === 'paper' ? 84 : 200"
                    :treeparams="{ mediaType: Worderitem.mediatypekey }"
                    @change="onAdcolorChange"
                  ></HgSingleZtree>
                </el-card>
                <el-card v-if="curMediaTypeKey === 'paper'" class="cardbox" style="margin-top: 4px">
                  <div>
                    <span>星期</span>
                    <el-button
                      v-if="Worderitem.weeksettingid !== ''"
                      type="primary"
                      link
                      @click="onWeeksettingCancel()"
                      >取消</el-button
                    >
                  </div>
                  <HgSingleZtree
                    v-model="Worderitem.weeksettingid"
                    :scopeflag="EHgSingleZtreeUrl.PRICE_GETWEEKSETTINGTREE"
                    :treeheight="50"
                    @change="onWeeksettingChange"
                  ></HgSingleZtree>
                </el-card>
              </div>
            </el-space>
          </el-col>
        </el-row>
        <el-row style="padding-right: 40px">
          <el-col :span="8">
            <el-form-item prop="baseprice" label="刊例价">
              <el-input-number
                v-model="Worderitem.baseprice"
                :precision="2"
                :step="0.1"
                :min="0"
                :controls="false"
                style="width: 100%"
                @change="getnormalprice"
              />
            </el-form-item>
            <el-form-item prop="normalprice" label="标准价格">
              <el-input-number
                v-model="Worderitem.normalprice"
                :precision="2"
                :step="0.1"
                :min="0"
                :controls="false"
                style="width: 100%"
                disabled
              />
            </el-form-item>
            <el-form-item prop="basepriceyear" label="价格表" v-if="showprice === true">
              <el-select
                v-model="basepriceyear"
                placeholder="价格表"
                style="width: 100%"
                @change="onbasepriceyearchange"
              >
                <el-option
                  v-for="item in priceGroupList"
                  :key="item.id"
                  :label="item.sname"
                  :value="item.id!"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="ndiscountrate" label="折扣率">
              <el-input-number
                v-model="Worderitem.ndiscountrate"
                :precision="1"
                :step="0.1"
                :min="0"
                :max="100"
                :controls="false"
                style="width: 230px"
                disabled
              />
              <span style="margin-left: 10px">%</span>
            </el-form-item>
            <el-form-item prop="amountreceivable" label="签订金额">
              <el-input-number
                v-model="Worderitem.amountreceivable"
                :precision="2"
                :step="0.1"
                :min="0"
                :controls="false"
                style="width: 100%"
                @change="getndiscountrate"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="备注" prop="sremark">
              <el-input v-model="Worderitem.sremark" :maxlength="200" />
            </el-form-item>
            <el-form-item label="显示价格表" prop="sremark">
              <el-switch
                v-model="showprice"
                active-text="是"
                inactive-text="否"
                :active-value="true"
                :inactive-value="false"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <div style="padding: 0 20px; margin: 20px 0">
          <el-divider content-position="center">
            <span style="color: #409eff">归属详情</span>
          </el-divider>
        </div>

        <AdTworderItemBelong
          style="width: 100%; padding: 0 20px; margin: 20px 0 0"
          :data="Worderitem.orderitembelong"
          :addshow="true"
          :bottomshow="props.bottomshow"
          @worderitembelongdata="changeworderitembelong"
        ></AdTworderItemBelong>
      </el-form>

      <div
        v-if="props.bottomshow !== false"
        style="width: 100%; text-align: center; margin: 20px 0"
      >
        <el-button type="primary" icon="Check" @click="handleSaveclose()">保存并关闭</el-button>
        <el-button v-if="addorup === false" type="primary" icon="Check" @click="handleSave()"
          >保存并继续新增</el-button
        >
        <el-button icon="Close" @click="handleCancel">取消</el-button>
      </div>
    </el-dialog>
    <el-dialog
      v-model="Worderitembelongshow"
      title="归属详情"
      width="1200"
      align-center
      :destroy-on-close="true"
      :draggable="fenbianlv() > 1440 ? true : false"
      @close="Worderitembelongshow = false"
    >
      <AdTworderItemBelong
        style="width: 100%; padding: 0 20px; margin: 20px 0 0"
        :data="Worderitem.orderitembelong"
        :addshow="false"
        @worderitembelongdata="changeworderitembelong"
      ></AdTworderItemBelong>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { FormRules, FormInstance, menuItemEmits } from 'element-plus'
import AdTworderItemBelong from './AdTworderItemBelong.vue'
import type { TQPageplanList, TAFoldPagePlanOne } from '@/types/views/schedule/foldpageplan'
import { getPageplanListApi } from '@/api/schedule/foldpageplan'
import type {
  TworderCustomer,
  Tworderitembelong,
  Tworderitem,
  Tworder
} from '@/types/views/ad/adtworder'
import {
  required,
  computTableHeight,
  isNumberStr,
  formatDate,
  hgFormatDate,
  formatDatesm,
  tableHeaderColor,
  validatePhone,
  computTreeHeight,
  fenbianlv,
  sortNumber,
  myGetTime,
  formatMoney
} from '@/utils/index'
import {
  getorderByIdApi,
  getContractNumByEmployIdAndCustomerIdApi,
  saveorderApi,
  updateorderApi,
  deleteorderApi,
  getApproveTypeComboApi
} from '@/api/ad/adtworder'
import type { IAxios } from 'axios'
import modal from '@/plugins/modal'
import useUserStore from '@/store/modules/user'
import { EHgDeptZtreeUrl, EHgSingleZtreeUrl, EMediaType } from '@/types/common/enumindex'
import dayjs from 'dayjs'
import { IDataCombo } from '@/types/common/DataCombo'
import { TAmedia } from '@/types/views/media/media'
import {
  getMediaDataComboByTypeApi,
  getMediaDataComboApi,
  getMediaDataListApi
} from '@/api/media/media'
import { getMediaTypeComboApi } from '@/api/media/mediatype'
import type { TQDiscountGroup, TDiscountItem } from '@/types/views/price/discountitem'
import type { TQIPriceGroupD, IPriceGroupDTO } from '@/types/views/price/pricegroup'
import { getdiscountItemListApi, getNdiscountApi } from '@/api/price/discountgroup'
import type { TPriceItemQuerylist, TPriceItemVO } from '@/types/views/price/priceitem'
import { getPriceItemListApi } from '@/api/price/priceitem'
import type { TDiscountGroup } from '@/types/views/price/discountgroup'
import { getdiscountItemApi } from '@/api/price/discountitem'
import { getPriceGroupListApi } from '@/api/price/pricegroup'
import { number } from 'echarts'
import { da } from 'element-plus/es/locale'
import { data } from 'jquery'
defineOptions({
  name: 'AdTworderItem'
})
const userStore = useUserStore()
/** 当前媒体类型 */
const curMediaTypeKey = ref('paper')
/** 叠次树显示类型控制 */
const curMediaTypeKeyshow = ref('paper')
/** 叠次版本树查询参数 */
const areavertreeparams = ref<{ foldId: string | undefined }>({
  foldId: ''
})
/** 版面类别树查询参数 */
const pagesortparams = ref<{ mediaType?: string; foldId?: string }>({
  mediaType: '',
  foldId: ''
})
/** 叠次选中id */
const foldselectid = ref('')
/** 广告规格树查询参数 */
const adspecparams = ref<{ mediaId?: string }>()
const props = defineProps<{
  /**
   * 传入数据
   */
  data?: Tworder
  /**
   * 资源id
   */
  resourceid?: string
  /**
   * 归属数据
   */
  belongdata?: Tworderitembelong[]
  /**
   * 保存按钮是否显示，true显示，false不显示
   */
  bottomshow?: boolean
  /**
   * 是否是补刊，3是，0不是
   */
  idbukan?: number
}>()
/** 导出返回数据 */
const emit = defineEmits<{
  selectionorderitem: [data: Tworderitem[]]
}>()
const global = getCurrentInstance()?.appContext.config.globalProperties
/** 表格高度 */
const tableheight = ref(0)
/** 是否显示价格表 */
const showprice = ref(false)
/** 订单刊期表 */
const Worderitem = ref<Tworderitem>({
  orderitembelong: [],
  id: '',
  orderid: '',
  sordernum: '',
  scontractnum: '',
  createempid: userStore.user?.userid.toString(),
  createempname: userStore.user?.username.toString(),
  createtime: '',
  ibooktype: props.data!.ibooktype as number,
  mediatypekey: '',
  mediatypename: '',
  mediaid: '',
  medianame: '',
  prestartdate: '',
  preenddate: '',
  foldid: '',
  foldname: '',
  foldareaverid: '',
  foldareavername: '',
  addisplayformid: '',
  addisplayformname: '',
  pagesortid: '',
  pagesortname: '',
  adcolorid: '',
  adcolorname: '',
  adspecid: '',
  adspecname: '',
  nwidth: 0,
  nheight: 0,
  weeksettingid: '',
  weeksettingname: '',
  foldpageplanid: '',
  sadtitle: '',
  sadcontent: '',
  foldpageplanname: '',
  baseprice: 0,
  priceitemid: '', // ---
  normalprice: 0,
  amountreceivable: 0,
  amountreceived: 0,
  amountarrearage: 0,
  ndiscountrate: 0,
  amountachievement: 0,
  namountcost: 0,
  iaddapprovestatus: 0,
  ichangeapprovestatus: 0,
  istopapprovestatus: 0,
  idiscountapprovestatus: 0,
  iapprovestatus: 0,
  ipublishstatus: 0,
  buse: true,
  bdelete: false,
  breportreason: false,
  sremark: '',
  version: 0,
  ipublishcheckstatus: 0,
  spublishcheckcontent: '',
  dachievementdate: ''
})
const Worderitemlsit = ref<Tworderitem[]>([])
/** 价格获取年份 */
const basepriceyear = ref<string>('')
const TwordwrFormRef = ref<FormInstance>()
const discountGroupSelection = ref<Tworderitem[]>()
/** 归属修改数据 */
const Tworderitembelonglist = ref<Tworderitembelong>()
/** 订单明细 */
const Worderitemshow = ref(false)
/**
 * 版面计划显示隐藏
 */
const Pageplanshow = ref(false)
/**
 * 归属显示隐藏
 */
const Worderitembelongshow = ref(false)
/**
 * 版面计划列表
 */
const PageplanList = ref<TAFoldPagePlanOne[]>([])
/**
 * 版面计划查询数据
 */
const PageplanData = ref<TQPageplanList>({
  mediatypekey: '',
  mediaid: '',
  mediatypename: '',
  medianame: '',
  foldid: '',
  foldname: '',
  foldareaverid: '',
  foldareavername: '',
  publishdate: '',
  adcolorid: '',
  startTime: '',
  endTime: '',
  foldpageplanid: ''
})
/** 当前时间 */
const nowtimedata = ref()
/**
 * 媒体类别下拉列表
 */
const mediaTypeCombo = ref<TAmedia[]>()

/**
 * 媒体列表
 */
const mediaCombo = ref<TAmedia[]>([])
/**
 * 价格列表查询参数
 */
const priceGroup = ref<TQIPriceGroupD>({
  mediatypekey: 'paper',
  mediatypename: '',
  syear: '',
  queryKey: '',
  bdefault: true,
  startTime: '',
  buse: true,
  endTime: '',
  loginUserId: '',
  cacheDTOKey: ''
})
/**
 * 价格列表数据
 */
const priceGroupList = ref<IPriceGroupDTO[]>([])
/**
 * 价格表明细查询参数
 */
const priceItem = ref<TPriceItemQuerylist>({
  id: '',
  syear: '',
  pricegroupid: '',
  pricegroupname: '',
  mediatypekey: 'paper',
  mediatypename: '',
  mediaid: '',
  medianame: '',
  foldid: '',
  foldname: '',
  foldareaid: '',
  foldareaname: '',
  addisplayformid: '',
  addisplayformname: '',
  pagesortid: '',
  pagesortname: '',
  adcolorid: '',
  adcolorname: '',
  adspecid: '',
  adspecname: '',
  weeksettingid: '',
  weeksettingname: '',
  baseprice: '',
  dstartdate: '',
  denddate: '',
  isort: '',
  buse: true,
  sremark: '',
  queryKey: '',
  startTime: '',
  endTime: '',
  loginUserId: '',
  cacheDTOKey: ''
})
/**
 * 价格表明细列表
 */
const priceItemList = ref<TPriceItemVO[]>([])
// /**
//  * 折扣组列表
//  */
const discountGroupList = ref<TDiscountGroup[]>([])
/**
 * 折扣明细列表查询参数
 */
const discountItem = ref<TQDiscountGroup>({
  discountgroupid: '',
  syear: '',
  id: '',
  mediaid: '',
  medianame: '',
  adindustryid: '',
  adindustryname: '',
  bcustomer: true,
  bagency: false,
  bagent: false,
  bvip: false,
  ndiscount: 0,
  sremark: ''
})
/**
 * 刊例价
 */
const baseprice = ref(0)
/**
 * 标准价格
 */
const normalprice = ref(0)
/**
 * 签订金额
 */
const amountreceivable = ref(0)
/**
 * 折扣明细列表
 */
// const discountItemList = ref<TDiscountItem[]>([])
/**
 * 修改行数据
 */
const handleUpdateRowindex = ref(0)
/**
 * 判断是修改还是删除：false是新增
 */
const addorup = ref(false)
/** 查询版面计划树 */
const plantreeparams = ref<{
  publishdate: string
  mediaId: string
  mediatypekey: string
  isShowPagenum: boolean
}>({
  publishdate: '',
  mediaId: '',
  mediatypekey: 'paper',
  isShowPagenum: true
})
/**
 * 复制时间段
 */
const copydata = ref<{ copystartTime: string; copyendTime: string }>({
  copystartTime: '',
  copyendTime: ''
})
/**
 * 复制时间段
 */
const intervalTimes = ref<string[]>([])
/**
 * 媒体类型检索
 */
const mediatypekeyselect = ref<string>('paper')
/** paper判断是否选择版本 */
const foldpageplanidshow = ref(false)
/** app判断是否位置 */
const foldareaidshow = ref(false)
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<Tworderitem>>({
  sadtitle: [
    { validator: required, required: true, message: '', trigger: 'change' },
    { max: 48, message: '不得大于48个字符', trigger: 'change' }
  ],
  prestartdate: [{ required: true, message: '请选择预刊时间', trigger: 'change' }],
  mediaid: [{ required: true, message: '请选媒体', trigger: 'change' }],
  foldid: [{ required: true, message: '请选叠次', trigger: 'change' }]
})
/** 监听传入的详情 */
watch(
  () => props.data,
  (newValue, oldValue) => {
    if (newValue) {
      console.log(newValue!.orderitem)
      if (props.data!.orderitem!.length === 0) {
        Worderitemlsit.value = []
      } else {
        Worderitemlsit.value = props.data!.orderitem!
        getjiansuo(props.data!.orderitem![0])
      }
      Worderitem.value.sadtitle = newValue!.sadtitle
      Worderitem.value.sadcontent = newValue!.sadcontent
      Worderitem.value.orderid = newValue!.id
      Worderitem.value.sordernum = newValue!.sordernum
      Worderitem.value.scontractnum = newValue!.scontractnum
      Worderitem.value.ibooktype = newValue!.ibooktype as number
      console.log(Worderitem.value.ibooktype)
      redetdiscountItem(newValue)
      copydata.value = {
        copystartTime: '',
        copyendTime: ''
      }
      getSummarieslist()
    }
  },
  { deep: true }
)
/** 监听传入的归属 */
watch(
  () => props.belongdata,
  (newValue, oldValue) => {
    if (newValue) {
      if (props.belongdata?.length === 0) {
        Worderitem.value.orderitembelong = []
      } else {
        Worderitem.value.orderitembelong = props.belongdata!
      }
      getSummarieslist()
    }
  }
)
/** 监听传入资源id */
watch(
  () => props.resourceid,
  (newValue, oldValue) => {
    if (newValue) {
      // getMediaDataCombo()
    }
  }
)
onMounted(() => {
  console.log(props.data!.orderitem)
  getSummarieslist()
  if (props.data!.orderitem!.length === 0) {
    Worderitemlsit.value = []
  } else {
    Worderitemlsit.value! = props.data!.orderitem!
    getjiansuo(props.data!.orderitem![0])
  }
  Worderitem.value.sadtitle = props.data!.sadtitle
  Worderitem.value.sadcontent = props.data!.sadcontent
  Worderitem.value.orderid = props.data!.id
  Worderitem.value.sordernum = props.data!.sordernum
  Worderitem.value.scontractnum = props.data!.scontractnum
  Worderitem.value.ibooktype = props.data!.ibooktype as number
  redetdiscountItem(props.data!)
  discountItem.value.syear = nowTime().substring(0, 4)
  copydata.value = {
    copystartTime: '',
    copyendTime: ''
  }
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})

/**
 * 补刊的时候限制当前时间以前
 */
const disabledDate = (time: Date) => {
  return time.getTime() > Date.now()
}
/**
 * 折扣明细列表查询参数复制
 */
const redetdiscountItem = (data: Tworder) => {
  discountItem.value.adindustryid = data.adindustyid
  discountItem.value.adindustryname = data.adindustryname
  discountItem.value.bcustomer = true
  /**
   * 判断是否存在代理公司
   */
  if (data.agencytid !== '') {
    discountItem.value.bagency = true
  } else {
    discountItem.value.bagency = false
  }
  /**
   * 判断是否存在代理公司
   */
  if (data.agentid !== '') {
    discountItem.value.bagent = true
  } else {
    discountItem.value.bagent = false
  }
  /**
   * 判断是否是大客户
   */
  if (data.bcustomeVip === true) {
    discountItem.value.bvip = true
  } else {
    if (data.bagencyVip === true) {
      discountItem.value.bvip = true
    } else {
      if (data.bagentVip === true) {
        discountItem.value.bvip = true
      } else {
        discountItem.value.bvip = false
      }
    }
  }
}
/**
 * 选择
 * @param rows
 */
const handleSelectionChange = (rows: Tworderitem[]) => {
  discountGroupSelection.value = rows
}
/**
 * 求和行数
 */
const getSummarieslist = () => {
  baseprice.value = 0
  normalprice.value = 0
  amountreceivable.value = 0
  Worderitemlsit.value.forEach((item) => {
    baseprice.value += Number(item.baseprice)
    normalprice.value += Number(item.normalprice)
    amountreceivable.value += Number(item.amountreceivable)
  })
}
/**
 * 求和行数
 */
import type { TableColumnCtx } from 'element-plus'
interface SummaryMethodProps<T = Tworderitem> {
  columns: TableColumnCtx<T>[]
  data: T[]
}
const getSummaries = (param: SummaryMethodProps) => {
  const { columns, data } = param
  const sums: string[] = []
  columns.forEach((column, index) => {
    if (index === 0) {
      sums[index] = '合计'
      return
    }
    if (index === 1) {
      sums[index] = '共' + Worderitemlsit.value.length.toString() + '条'
      return
    }
    if (
      column.property !== 'baseprice' &&
      column.property !== 'normalprice' &&
      column.property !== 'amountreceivable'
    ) {
      sums[index] = ''
      return
    }
    const values = data.map((item: any) => Number(item[column.property]))
    if (!values.every((value) => Number.isNaN(value))) {
      sums[index] = `￥ ${values.reduce((prev: any, curr: any) => {
        const value = Number(curr)
        if (!Number.isNaN(value)) {
          return Math.floor((prev + curr) * 100) / 100
        } else {
          return Math.floor(prev * 100) / 100
        }
      }, 0)}`
    } else {
      sums[index] = ''
    }
  })
  /**
   * 遍历数组保留两位小数
   */
  sums.forEach(function (element) {
    Number(element).toFixed(2)
  })
  // console.log(sums)
  return sums
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
 * 给检索条件赋值
 */
const getjiansuo = (val: any) => {
  PageplanData.value.publishdate = val.prestartdate
  PageplanData.value.mediatypekey = val.mediatypekey
  PageplanData.value.mediatypename = val.mediatypename
  PageplanData.value.mediaid = val.mediaid
  PageplanData.value.medianame = val.medianame
  PageplanData.value.foldid = val.foldid
  PageplanData.value.foldname = val.foldname
  PageplanData.value.foldareaverid = val.foldareaverid
  PageplanData.value.foldareavername = val.foldareavername
  areavertreeparams.value = {
    foldId: val.foldid
  }
}
/**
 * @description: 开始日期结束日期比较如果开始日期小于结束日期则颠倒
 * @param {*} val
 * @return {*}
 */
const onisAfterTimeduplicate = (val: string) => {
  console.log(val)
  if (val !== '') {
    plantreeparams.value = {
      publishdate: val,
      mediaId: '',
      mediatypekey: curMediaTypeKey.value,
      isShowPagenum: true
    }
    PageplanData.value.publishdate = val
    Worderitem.value.prestartdate = val
    Worderitem.value.mediatypekey = curMediaTypeKey.value
    priceGroup.value.syear = val.substring(0, 4)
    discountItem.value.syear = val.substring(0, 4)
    priceItem.value.syear = val.substring(0, 4)
  }
}
/**
 * 修改
 */
const handleUpdate = (index: number, row: Tworderitem) => {
  console.log(row)
  if (row.foldpageplanid === '' || row.foldpageplanid === null) {
    foldpageplanidshow.value = false
  } else {
    foldpageplanidshow.value = true
  }
  if (row.foldareaverid === '' || row.foldareaverid === null) {
    foldareaidshow.value = false
  } else {
    foldareaidshow.value = true
  }
  getMediaType()
  getWeeksetting()
  curMediaTypeKey.value = row.mediatypekey as string
  curMediaTypeKeyshow.value = row.mediatypekey as string
  Worderitem.value.mediatypekey = row.mediatypekey as string
  plantreeparams.value = {
    publishdate: row.prestartdate as string,
    mediaId: row.mediaid as string,
    mediatypekey: row.mediatypekey as string,
    isShowPagenum: true
  }
  handleUpdateRowindex.value = index
  addorup.value = true
  Worderitem.value = { ...row }
  getjiansuo(Worderitem.value)
  Worderitemshow.value = true
  pagesortparams.value = {
    mediaType: row.mediatypekey,
    foldId: row.foldid
  }
  priceGroup.value.mediatypekey = row.mediatypekey
  priceGroup.value.mediatypename = row.mediatypename
  getPriceGroupListdata()
}

/**
 * @description: 开始日期结束日期比较如果开始日期小于结束日期则颠倒
 * @param {*} val
 * @return {*}
 */
const onisAfterTime = (val: string) => {
  const isAfter: boolean = dayjs(copydata.value.copystartTime).isAfter(copydata.value.copyendTime)
  const startTime = copydata.value.copystartTime
  const endTime = copydata.value.copyendTime
  if (isAfter) {
    copydata.value.copystartTime = startTime
    copydata.value.copyendTime = startTime
  }
  console.log(copydata.value.copystartTime)
  console.log(copydata.value.copyendTime)
  if (copydata.value.copyendTime !== '') {
    getBetweenDate(copydata.value.copystartTime, copydata.value.copyendTime)
  }
}

/**
 * 开始时间到结束时间中间所有时间合并数组
 */
const getBetweenDate = (start: string, end: string) => {
  intervalTimes.value = []
  const startTime = dayjs(start)
  const endTime = dayjs(end)
  const diffTime = endTime.diff(startTime, 'day')

  for (let i = 0; i <= diffTime; i++) {
    const intervalTime = startTime.add(i, 'day')
    intervalTimes.value.push(intervalTime.format('YYYY-MM-DD'))
  }
  console.log(data)
  console.log(intervalTimes.value)
}
/**
 * 复制
 */
const handcopybelong = (index: number, row: Tworderitem) => {
  console.log(row)
  if (intervalTimes.value.length === 0) {
    modal.msgWarning('请选择复制时间段')
    return
  } else {
    intervalTimes.value.forEach((itemtime) => {
      const data = ref<Tworderitem>()
      const databelong = ref<Tworderitembelong[]>([])
      data.value = { ...row }
      data.value.id = ''
      data.value.prestartdate = itemtime
      row.orderitembelong!.forEach((item) => {
        const datalist = { ...item }
        datalist.id = ''
        datalist.orderitemid = ''
        databelong.value?.push(datalist)
      })
      data.value.orderitembelong = databelong.value
      console.log(data.value)
      console.log(Worderitemlsit.value)
      Worderitemlsit.value.push(data.value!)
      emit('selectionorderitem', Worderitemlsit.value)
      copydata.value = {
        copystartTime: '',
        copyendTime: ''
      }
    })
  }
}
/**
 * 归属
 */
const handleUpdatebelong = (number: number, row: Tworderitem) => {
  Worderitem.value = { ...row }
  Worderitembelongshow.value = true
}
/** 添加 */
const handleAdd = () => {
  Worderitem.value.sadtitle = props.data!.sadtitle
  curMediaTypeKeyshow.value = 'paper'
  curMediaTypeKey.value = 'paper'
  getMediaType()
  getWeeksetting()
  if (props.belongdata?.length === 0) {
    Worderitem.value.orderitembelong = []
    reset()
  } else {
    Worderitem.value.orderitembelong = props.belongdata!
  }
  Worderitem.value.prestartdate = nowTime()
  plantreeparams.value = {
    publishdate: nowTime(),
    mediaId: '',
    mediatypekey: 'paper',
    isShowPagenum: true
  }
  getPriceGroupListdata()
  Worderitemshow.value = true
}
/** 提交校验 */
const validFormSubmitBatch = () => {
  if (Worderitem.value.orderitembelong?.length === 0) {
    ElMessage.error('请填写订单明细归属')
    return false
  }
  if (Worderitem.value.mediaid === '') {
    ElMessage.error('请选择媒体')
    return false
  }
  if (curMediaTypeKey.value === 'paper') {
    if (Worderitem.value.foldareaverid === '') {
      ElMessage.error('叠次版本不能为空')
      return false
    }
    if (Worderitem.value.pagesortid === '') {
      ElMessage.error('版面类型不能为空')
      return false
    }
    if (Worderitem.value.ibooktype === 3) {
      if (Worderitem.value.foldpageplanid === '') {
        ElMessage.error('版面不能为空')
        return false
      }
    }
    if (Worderitem.value.addisplayformid === '') {
      ElMessage.error('广告形式不能为空')
      return false
    }
    if (Worderitem.value.adspecid === '') {
      ElMessage.error('广告规格不能为空')
      return false
    }
    if (Worderitem.value.adcolorid === '') {
      ElMessage.error('色彩不能为空')
      return false
    }
    if (Worderitem.value.weeksettingid === '') {
      ElMessage.error('星期不能为空')
      return false
    }
  }
  if (curMediaTypeKey.value === 'app') {
    if (Worderitem.value.foldid === '') {
      ElMessage.error('位置不能为空')
      return false
    }
    if (Worderitem.value.ibooktype === 3) {
      if (Worderitem.value.foldareaverid === '') {
        ElMessage.error('频道|分类不能为空')
        return false
      }
    }
    if (Worderitem.value.pagesortid === '') {
      ElMessage.error('内容|曝光率不能为空')
      return false
    }
    if (Worderitem.value.addisplayformid === '') {
      ElMessage.error('广告形式不能为空')
      return false
    }
    if (Worderitem.value.adspecid === '') {
      ElMessage.error('尺寸|周期不能为空')
      return false
    }
    if (Worderitem.value.adcolorid === '') {
      ElMessage.error('广告样式不能为空')
      return false
    }
  }
  if (curMediaTypeKey.value === 'wei') {
    if (Worderitem.value.foldid === '') {
      ElMessage.error('微信媒体不能为空')
      return false
    }
    if (Worderitem.value.pagesortid === '') {
      ElMessage.error('广告位置不能为空')
      return false
    }
    if (Worderitem.value.addisplayformid === '') {
      ElMessage.error('广告形式不能为空')
      return false
    }
    if (Worderitem.value.adspecid === '') {
      ElMessage.error('尺寸不能为空')
      return false
    }
    // if (Worderitem.value.adcolorid === '') {
    //   ElMessage.error('广告样式不能为空')
    //   return false
    // }
  }
  if (curMediaTypeKey.value === 'multi') {
    if (Worderitem.value.foldareaverid === '') {
      ElMessage.error('项目分类名称不能为空')
      return false
    }
    // if (Worderitem.value.pagesortid === '') {
    //   ElMessage.error('内容|曝光率不能为空')
    //   return false
    // }
    // if (Worderitem.value.addisplayformid === '') {
    //   ElMessage.error('广告形式不能为空')
    //   return false
    // }
    // if (Worderitem.value.adspecid === '') {
    //   ElMessage.error('尺寸|周期不能为空')
    //   return false
    // }
    // if (Worderitem.value.adcolorid === '') {
    //   ElMessage.error('广告样式不能为空')
    //   return false
    // }
  }
  return true
}
/**
 * 保存
 */
const handleSave = (data?: string) => {
  TwordwrFormRef.value?.validate((valid) => {
    const validform = validFormSubmitBatch()
    if (valid) {
      if (!validform) {
        return
      }
      if (addorup.value === false) {
        // Worderitemlsit.value.push(Worderitem.value)
        // 需要深拷贝
        Worderitemlsit.value.push(JSON.parse(JSON.stringify(Worderitem.value)))
        emit('selectionorderitem', Worderitemlsit.value)
      } else if (addorup.value === true) {
        Worderitemlsit.value.splice(handleUpdateRowindex.value, 1, {
          ...Worderitem.value
        })
        handleUpdateRowindex.value = 0
      }
    }
  })
  getSummarieslist()
}
/**
 * 保存并关闭
 */
const handleSaveclose = (data?: string) => {
  TwordwrFormRef.value?.validate((valid) => {
    const validform = validFormSubmitBatch()
    if (valid) {
      if (!validform) {
        return
      }
      if (addorup.value === false) {
        console.log(Worderitem.value)
        Worderitemlsit.value.push(Worderitem.value)
        reset()
        emit('selectionorderitem', Worderitemlsit.value)
      } else if (addorup.value === true) {
        Worderitemlsit.value.splice(handleUpdateRowindex.value, 1, {
          ...Worderitem.value
        })
        handleUpdateRowindex.value = 0
        reset()
      }
      Worderitemshow.value = false
    }
  })
  getSummarieslist()
}
/**
 * 取消
 */
const handleCancel = () => {
  Worderitemshow.value = false
  curMediaTypeKeyshow.value = 'paper'
  reset()
  addorup.value = false
}
/**
 * 删除
 * @param row
 */
const handleDelete = (index: number, row?: Tworderitem) => {
  modal.confirm('是否删除?').then(() => {
    Worderitemlsit.value.splice(index, 1)
  })
}
/**
 * 批量删除
 */
const handleDeleteBatch = () => {
  if (discountGroupSelection.value?.length === 0) {
    ElMessage.error('请选择需要删除的数据')
    return
  }
  modal.confirm('是否删除?').then(() => {
    discountGroupSelection.value?.forEach((item) => {
      const index = Worderitemlsit.value.findIndex((val) => val.prestartdate === item.prestartdate)
      Worderitemlsit.value.splice(index, 1)
    })
  })
}
/**
 * 关闭是清空数据
 */
const reset = () => {
  Worderitem.value = {
    id: '',
    orderid: '',
    sordernum: '',
    scontractnum: '',
    createempid: userStore.user?.userid.toString(),
    createempname: userStore.user?.username.toString(),
    createtime: '',
    ibooktype: props.data!.ibooktype as number,
    mediatypekey: '',
    mediatypename: '',
    mediaid: '',
    medianame: '',
    prestartdate: '',
    preenddate: '',
    foldid: '',
    foldname: '',
    foldareaverid: '',
    foldareavername: '',
    addisplayformid: '',
    addisplayformname: '',
    pagesortid: '',
    pagesortname: '',
    adcolorid: '',
    adcolorname: '',
    adspecid: '',
    adspecname: '',
    nwidth: 0,
    nheight: 0,
    weeksettingid: '',
    weeksettingname: '',
    foldpageplanid: '',
    sadtitle: '',
    sadcontent: '',
    foldpageplanname: 0,
    baseprice: 0,
    priceitemid: '',
    normalprice: 0,
    amountreceivable: 0,
    amountreceived: 0,
    amountarrearage: 0,
    ndiscountrate: 0,
    amountachievement: 0,
    namountcost: 0,
    iaddapprovestatus: 0,
    ichangeapprovestatus: 0,
    istopapprovestatus: 0,
    idiscountapprovestatus: 0,
    iapprovestatus: 0,
    ipublishstatus: 0,
    buse: true,
    bdelete: false,
    breportreason: false,
    sremark: '',
    version: 0,
    ipublishcheckstatus: 0,
    spublishcheckcontent: '',
    dachievementdate: ''
  }
  Worderitem.value.baseprice = 0
  Worderitem.value.normalprice = 0
  setTimeout(() => {
    TwordwrFormRef.value?.clearValidate(['sadtitle'])
  }, 10)
  redetPageplanData()
  // mediaTypeCombo.value = []
  plantreeparams.value = {
    publishdate: nowTime(),
    mediaId: '',
    mediatypekey: '',
    isShowPagenum: true
  }
  pagesortparams.value = {
    mediaType: '',
    foldId: ''
  }
}
/**
 * 关闭是清空数据
 */
const redetPageplanData = () => {
  PageplanData.value = {
    mediatypekey: '',
    mediaid: '',
    mediatypename: '',
    medianame: '',
    foldid: '',
    foldname: '',
    foldareaverid: '',
    foldareavername: '',
    publishdate: '',
    adcolorid: '',
    startTime: '',
    endTime: '',
    foldpageplanid: ''
  }
  copydata.value = {
    copystartTime: '',
    copyendTime: ''
  }
}
/** 版本类别取消选择 */
const onPagesortCancel = () => {
  Worderitem.value.pagesortid = ''
  Worderitem.value.pagesortname = ''
  priceItem.value.pagesortid = ''
  priceItem.value.pagesortname = ''
}
/** 广告形式取消选择 */
const onAddisplayCancel = () => {
  Worderitem.value.addisplayformid = ''
  Worderitem.value.addisplayformname = ''
  priceItem.value.addisplayformid = ''
  priceItem.value.addisplayformname = ''
}
/** 规格取消选择 */
const onAdspecCancel = () => {
  Worderitem.value.adspecid = ''
  Worderitem.value.adspecname = ''
  priceItem.value.adspecid = ''
  priceItem.value.adspecname = ''
}
/** 色彩取消选择 */
const onAdcolorCancel = () => {
  Worderitem.value.adcolorid = ''
  Worderitem.value.adcolorname = ''
  priceItem.value.adcolorid = ''
  priceItem.value.adcolorname = ''
}
/** 星期取消选择 */
const onWeeksettingCancel = () => {
  Worderitem.value.weeksettingid = ''
  Worderitem.value.weeksettingname = ''
  priceItem.value.weeksettingid = ''
  priceItem.value.weeksettingname = ''
}

/** 版面类别树返回 */
const onPagetypeChange = (data: ITreeNode) => {
  Worderitem.value.pagesortid = data?.id ?? ''
  Worderitem.value.pagesortname = data?.name ?? ''
  priceItem.value.pagesortid = data?.id ?? ''
  priceItem.value.pagesortname = data?.name ?? ''
  getPriceGroupyn()
}
/** 广告形式树返回 */
const onAddisplayChange = (data: ITreeNode) => {
  Worderitem.value.addisplayformid = data?.id ?? ''
  Worderitem.value.addisplayformname = data?.name ?? ''
  priceItem.value.addisplayformid = data?.id ?? ''
  priceItem.value.addisplayformname = data?.name ?? ''
  getPriceGroupyn()
}
/** 规格树返回 */
import { listUsableAdSpecApi } from '@/api/media/adspec'
/** 表单上的广告规格列表 */
import { TAdspec } from '@/types/views/media/adspec'
const formSpecList = ref<TAdspec[]>([])
const onAdspecChange = (data: ITreeNode) => {
  console.log(data)
  listUsableAdSpecApi(Worderitem.value.mediaid as string).then(({ obj }: IAxios<TAdspec[]>) => {
    console.log(obj)
    obj.forEach((item) => {
      if (item.id === data?.id) {
        Worderitem.value.nwidth = item.nwidth
        Worderitem.value.nheight = item.nheight
      }
    })
  })
  Worderitem.value.adspecid = data?.id ?? ''
  Worderitem.value.adspecname = data?.name ?? ''
  priceItem.value.adspecid = data?.id ?? ''
  priceItem.value.adspecname = data?.name ?? ''
  getPriceGroupyn()
}
/** 色彩树返回 */
const onAdcolorChange = (data: ITreeNode) => {
  Worderitem.value.adcolorid = data?.id ?? ''
  PageplanData.value.adcolorid = data?.id ?? ''
  Worderitem.value.adcolorname = data?.name ?? ''
  priceItem.value.adcolorid = data?.id ?? ''
  priceItem.value.adcolorname = data?.name ?? ''
  getPriceGroupyn()
}
/** 星期选择返回 */
const onWeeksettingChange = (data: ITreeNode) => {
  Worderitem.value.weeksettingid = data?.id ?? ''
  Worderitem.value.weeksettingname = data?.name ?? ''
  priceItem.value.weeksettingid = data?.id ?? ''
  priceItem.value.weeksettingname = data?.name ?? ''
  getPriceGroupyn()
}
/**
 * 归属组件赋值
 */
const changeworderitembelong = (val: Tworderitembelong[]) => {
  Worderitemlsit.value[handleUpdateRowindex.value].orderitembelong = val
  Worderitem.value.orderitembelong = val
}
/**
 * 获取版面计划列表
 */
const getPageplanData = () => {
  PageplanData.value.mediaid = Worderitem.value.mediaid
  PageplanData.value.foldid = Worderitem.value.foldid
  PageplanData.value.foldareaverid = Worderitem.value.foldareaverid
  PageplanData.value.publishdate = Worderitem.value.prestartdate
  PageplanData.value.startTime = Worderitem.value.prestartdate
  PageplanData.value.endTime = Worderitem.value.preenddate
  // PageplanData.value.foldpageplanid = Worderitem.value.foldpageplanid
  getPageplanListApi(PageplanData.value).then(({ success, msg, obj }: IAxios) => {
    if (success) {
      if (obj.length === 0) {
        modal.msgWarning('所选时间不存在版面计划')
        PageplanList.value = []
      } else {
        console.log(obj)
        PageplanList.value = obj
        Worderitem.value.adcolorid = obj[0].adcolorid
        Worderitem.value.adcolorname = obj[0].adcolorname
        // Worderitem.value.foldpageplanid = obj[0].id
        Worderitem.value.sremark = obj[0].sremark as string
        if (obj[0].pagetitle !== '') {
          Worderitem.value.sadtitle = obj[0].pagetitle as string
        }
        // Worderitem.value.nwidth = obj[0].ipagewidth as number
        // Worderitem.value.nheight = obj[0].ipageheight as number
        // Worderitem.value.nwidth = 8
        // Worderitem.value.nheight = 10
      }
    } else {
      modal.msgError(msg)
    }
  })
}
/**
 * 获取媒体类型下拉列表
 */
const getMediaType = () => {
  getMediaTypeComboApi().then((res: IAxios<TAmedia[]>) => {
    if (res.success) {
      /** 遍历 */
      // res.obj = res.obj.filter((item) => {
      //   return item.id === 'paper' || item.id === 'app' || item.id === 'wei' || item.id === 'multi'
      // })
      mediaTypeCombo.value = res.obj
      console.log(mediaTypeCombo.value)
    }
  })
}
/**
 * 给媒体类型名称赋值
 */
const getMediaTypename = (data: string) => {
  mediaTypeCombo.value!.filter((item) => {
    if (item.id === data) {
      Worderitem.value.mediatypename = item.name
      priceGroup.value.mediatypename = item.name
    }
  })
  priceGroup.value.mediatypekey = data
  priceItem.value.mediatypekey = data
}
/**
 * 根据年份获取价格列表
 */
const onbasepriceyearchange = () => {
  priceGroup.value.syear = basepriceyear.value
  priceItem.value.syear = basepriceyear.value
  console.log(priceGroup.value.syear)
}
/**
 * 按类型获取价格列表数据
 */
const getPriceGroupListdata = () => {
  priceGroup.value.syear = ''
  priceGroup.value.bdefault = ''
  console.log(priceGroup.value)
  getPriceGroupListApi(priceGroup.value).then((res: IAxios<IPriceGroupDTO[]>) => {
    if (res.success) {
      priceGroupList.value = res.obj
      console.log(priceGroupList.value)
    }
  })
}
/**
 * 获取价格列表数据
 */
const getPriceGroupList = () => {
  getPriceGroupListApi(priceGroup.value).then((res: IAxios<IPriceGroupDTO[]>) => {
    if (res.success) {
      res.obj.filter((item) => {
        if (item.bdefault === true) {
          priceItem.value.pricegroupid = item.id
          priceItem.value.pricegroupname = item.sname
        }
      })
      getPriceItemList()
    }
  })
}
/**
 * 获取价格表明细列表数据
 */
const getPriceItemList = () => {
  getPriceItemListApi(priceItem.value).then((res: IAxios<TPriceItemVO[]>) => {
    if (res.success) {
      priceItemList.value = res.obj
      /**
       * 判断是否有返回数据
       */
      if (res.obj.length !== 0) {
        Worderitem.value.baseprice = res.obj[0].baseprice
        Worderitem.value.normalprice = res.obj[0].baseprice
        Worderitem.value.priceitemid = res.obj[0].id
        getamountreceivable()
      }
    }
  })
}
/**
 * 获取折扣明细列表数据
 */
const getdiscountItemList = () => {
  getNdiscountApi(discountItem.value).then((res: IAxios<number>) => {
    if (res.success) {
      console.log(res.obj)
      Worderitem.value.ndiscountrate = res.obj
      getamountreceivable()
    }
  })
}
/**
 * 计算签订金额
 */
const getamountreceivable = () => {
  Worderitem.value.amountreceivable =
    (Worderitem.value.ndiscountrate! * Worderitem.value.baseprice!) / 100
  Worderitem.value.amountachievement = Worderitem.value.amountreceivable
  Worderitem.value.amountarrearage = Worderitem.value.amountreceivable
}
/**
 *修改签订金额时自动计算折扣率并保留两位小数
 */
const getndiscountrate = () => {
  Worderitem.value.ndiscountrate =
    (Worderitem.value.amountreceivable! / Worderitem.value.baseprice!) * 100
  Worderitem.value.amountachievement = Worderitem.value.amountreceivable
  Worderitem.value.amountarrearage = Worderitem.value.amountreceivable
}
/**
 *刊例价改版时自动计算签订金额
 */
const getnormalprice = () => {
  Worderitem.value.normalprice = Worderitem.value.baseprice
  Worderitem.value.amountreceivable =
    (Worderitem.value.ndiscountrate! * Worderitem.value.baseprice!) / 100
  Worderitem.value.amountarrearage = Worderitem.value.amountreceivable
}
/**
 * 版面计划树赋值
 */
const onPlanChange = (data: ITreeNode) => {
  if (data === undefined) {
    return
  }
  curMediaTypeKey.value = data.skey as string
  Worderitem.value.mediatypekey = data.skey as string
  getMediaTypename(data.skey as string)
  if (data.stype === 'media') {
    pagesortparams.value = {
      mediaType: data.skey as string,
      foldId: ''
    }
    areavertreeparams.value = {
      foldId: data.id
    }
    adspecparams.value = {
      mediaId: data.id
    }
    Worderitem.value.mediaid = data.id
    Worderitem.value.medianame = data.name as string
    Worderitem.value.foldid = ''
    Worderitem.value.foldname = ''
    Worderitem.value.foldpageplanid = ''
    Worderitem.value.foldpageplanname = ''
    Worderitem.value.foldareaverid = ''
    Worderitem.value.foldareavername = ''
    priceItem.value.mediaid = data.id as string
    priceItem.value.medianame = data.name as string
    priceItem.value.foldid = ''
    priceItem.value.foldname = ''
  }
  if (data.stype === 'fold') {
    areavertreeparams.value = {
      foldId: data.id
    }
    pagesortparams.value = {
      mediaType: data.skey as string,
      foldId: data.id
    }
    const parentNode = data.getParentNode!()
    adspecparams.value = {
      mediaId: parentNode?.id
    }
    Worderitem.value.mediaid = parentNode?.id as string
    Worderitem.value.foldid = data.id
    Worderitem.value.medianame = parentNode?.name as string
    Worderitem.value.foldname = data.name
    Worderitem.value.foldpageplanid = ''
    Worderitem.value.foldpageplanname = ''
    Worderitem.value.foldareaverid = ''
    Worderitem.value.foldareavername = ''
    priceItem.value.mediaid = parentNode?.id as string
    priceItem.value.medianame = parentNode?.name as string
    priceItem.value.foldid = data.id
    priceItem.value.foldname = data.name as string
    foldareaidshow.value = false
  }
  if (data.stype === 'foldarea') {
    /** 叠次 */
    const parentNode = data.getParentNode!()
    /** 媒体 */
    const parentfoldNode = parentNode.getParentNode!()
    areavertreeparams.value = {
      foldId: parentNode?.id as string
    }
    pagesortparams.value = {
      mediaType: data.skey as string,
      foldId: parentNode?.id as string
    }
    adspecparams.value = {
      mediaId: parentfoldNode?.id
    }
    Worderitem.value.mediaid = parentfoldNode?.id
    Worderitem.value.medianame = parentfoldNode?.name as string
    Worderitem.value.foldid = parentNode?.id as string
    Worderitem.value.foldname = parentNode?.name as string
    Worderitem.value.foldpageplanid = ''
    Worderitem.value.foldpageplanname = ''
    Worderitem.value.foldareaverid = data.id
    Worderitem.value.foldareavername = data.name
    priceItem.value.mediaid = parentfoldNode?.id
    priceItem.value.medianame = parentfoldNode?.name as string
    priceItem.value.foldid = parentNode?.id as string
    priceItem.value.foldname = parentNode?.name as string
    priceItem.value.foldareaid = data.id
    priceItem.value.foldareaname = data.name
    foldpageplanidshow.value = false
    foldareaidshow.value = true
  }
  if (data.stype === 'pagenum') {
    /** 叠次版本 */
    const parentNode = data.getParentNode!()
    /** 叠次 */
    const parentfoldNode = parentNode.getParentNode!()
    /** 媒体 */
    const parentmediaNode = parentfoldNode.getParentNode!()
    areavertreeparams.value = {
      foldId: parentfoldNode?.id as string
    }
    pagesortparams.value = {
      mediaType: data.skey as string,
      foldId: parentfoldNode?.id as string
    }

    adspecparams.value = {
      mediaId: parentmediaNode?.id
    }
    Worderitem.value.mediaid = parentmediaNode?.id as string
    Worderitem.value.medianame = parentmediaNode?.name as string
    Worderitem.value.foldid = parentfoldNode?.id
    Worderitem.value.foldname = parentfoldNode?.name as string
    Worderitem.value.foldareaverid = parentNode?.id as string
    Worderitem.value.foldareavername = parentNode?.name as string
    Worderitem.value.foldpageplanid = data.id
    Worderitem.value.foldpageplanname = data.name
    foldpageplanidshow.value = true
    priceItem.value.mediaid = parentmediaNode?.id as string
    priceItem.value.medianame = parentmediaNode?.name as string
    priceItem.value.foldid = parentfoldNode?.id as string
    priceItem.value.foldname = parentfoldNode?.name as string
    priceItem.value.foldareaid = parentNode?.id as string
    priceItem.value.foldareaname = parentNode?.name as string
  }

  nextTick(() => {
    priceGroup.value.mediatypekey = Worderitem.value.mediatypekey as string
    priceGroup.value.mediatypename = Worderitem.value.mediatypename as string
    priceItem.value.mediatypekey = Worderitem.value.mediatypekey as string
    priceItem.value.mediatypename = Worderitem.value.mediatypename as string
    if ((data.skey as string) === 'paper') {
      getPageplanData()
    }
    getdiscountItemList()
    getPriceGroupyn()
    getPriceGroupListdata()
  })
}
/** 获取可用的星期设置 */
import { listUsableWeekSettingApi } from '@/api/ad/weeksetting'
const weeksettingList = ref<any[]>([])
const getWeeksetting = () => {
  listUsableWeekSettingApi().then((res: IAxios<any[]>) => {
    if (res.success) {
      console.log(res.obj)
      weeksettingList.value = res.obj
      Worderitem.value.weeksettingid = res.obj[0].id
      Worderitem.value.weeksettingname = res.obj[0].sname
    }
  })
}
/** 判断是否获取价格 */
const getPriceGroupyn = () => {
  if (curMediaTypeKey.value === 'paper') {
    if (
      priceItem.value.foldareaid !== '' &&
      priceItem.value.pagesortid !== '' &&
      priceItem.value.addisplayformid !== '' &&
      priceItem.value.adspecid !== '' &&
      priceItem.value.adcolorid !== '' &&
      priceItem.value.weeksettingid !== ''
    ) {
      getPriceGroupList()
    }
  }
  if (curMediaTypeKey.value === 'app') {
    if (
      priceItem.value.foldareaid !== '' &&
      priceItem.value.pagesortid !== '' &&
      priceItem.value.addisplayformid !== '' &&
      priceItem.value.adspecid !== '' &&
      priceItem.value.adcolorid !== ''
    ) {
      priceItem.value.weeksettingid = ''
      getPriceGroupList()
    }
  }
  if (curMediaTypeKey.value === 'wei') {
    if (
      priceItem.value.foldid !== '' &&
      priceItem.value.pagesortid !== '' &&
      priceItem.value.addisplayformid !== '' &&
      priceItem.value.adspecid !== ''
    ) {
      priceItem.value.adcolorid = ''
      priceItem.value.weeksettingid = ''
      getPriceGroupList()
    }
  }
  if (curMediaTypeKey.value === 'multi') {
    if (priceItem.value.foldareaid !== '') {
      priceItem.value.pagesortid = ''
      priceItem.value.addisplayformid = ''
      priceItem.value.adspecid = ''
      priceItem.value.adcolorid = ''
      priceItem.value.weeksettingid = ''
      getPriceGroupList()
    }
  }
}
</script>

<style scoped>
ul {
  padding-left: 10px;
}
.showSearch span {
  color: #606266;
  font-size: 14px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 0px;
}
.el-form-item {
  margin-bottom: 10px;
}
.box-card {
  width: 280px;
  max-height: 600px;
  overflow-y: auto;
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
.cardbox {
  width: 200px;
  box-shadow: none;
  margin-bottom: 10px;
  /* height: 220px; */
}
</style>
