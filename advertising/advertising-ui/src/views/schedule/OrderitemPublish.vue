<!--
 * @Author: lhl
 * @Date: 2023-12-13
 * @Description:广告刊发
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-select
          v-model="mediaId"
          placeholder="请选择媒体"
          clearable
          style="width: 120px"
          @change="handleMedia($event)"
        >
          <el-option
            v-for="item in mediaCombo"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
        <el-select
          v-if="showflag === 0"
          v-model="publistStatus"
          placeholder="刊发状态"
          clearable
          style="width: 120px"
          @change="handlePublishStatus"
          @clear="handlePublishStatus"
        >
          <el-option
            v-for="item in baseTask.OrderPublishType"
            :key="item.id"
            :label="item.value"
            :value="item.id"
          ></el-option>
        </el-select>

        <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
        <el-select
          v-if="showflag === 0"
          v-show="mediaType === 0 || mediaType === 1 || mediaType === 3"
          v-model="floadId"
          :placeholder="floadName"
          style="width: 100px"
          clearable
          @change="handleFlod"
        >
          <el-option v-for="item in foldList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
        <el-select
          v-if="showflag === 0"
          v-show="mediaType === 0 || mediaType === 1 || mediaType === 3"
          v-model="floaAreaverdId"
          :placeholder="floaAreaverdName"
          clearable
          style="width: 120px"
          @change="handleFlodAreaver"
        >
          <el-option
            v-for="item in foldAreaverList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
        <el-button
          v-if="showflag === 0 && batcharrange === 1"
          type="primary"
          :disabled="isPrintButton"
          @click="handleArrangeClick"
          >发布</el-button
        >
        <el-button
          v-if="showflag === 0"
          type="primary"
          :disabled="isPrintButton"
          @click="handlePublish"
          >设置刊发</el-button
        >
        <el-button
          v-if="showflag === 0"
          v-show="mediaType === 0"
          type="primary"
          :disabled="isPrintButton"
          @click="handlePrint"
          >打印邮签</el-button
        >
        <el-button v-if="showflag === 1" type="primary" @click="exportExcel">导出EXCEL</el-button>
        <el-radio-group v-model="showtype" style="margin-top: -5px" @change="changeTheme">
          <el-radio-button label="刊发"></el-radio-button>
          <el-radio-button label="核查"></el-radio-button>
        </el-radio-group>
      </div>
    </div>
    <!--
      查询列表
    -->
    <div v-if="showflag === 0" class="table_box">
      <el-table
        :data="dataList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        border
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          type="selection"
          prop="ischeck"
          :width="50"
          align="center"
        ></el-table-column>
        <el-table-column type="index" label="序号" :index="table_index" width="60" align="center">
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
              v-if="scope.row.scontractnum"
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
          label="刊发状态"
          prop="ipublishstatus"
          show-overflow-tooltip
          min-width="100"
          align="center"
        >
          <template #default="scope">
            <span v-if="scope.row.ipublishstatus === 0">预约</span>
            <span v-if="scope.row.ipublishstatus === 1">预定</span>
            <span v-if="scope.row.ipublishstatus === 2">待发布</span>
            <span v-if="scope.row.ipublishstatus === 3">安排</span>
            <span v-if="scope.row.ipublishstatus === 4">见报</span>
            <span v-if="scope.row.ipublishstatus === 5">已发布</span>
            <span v-if="scope.row.ipublishstatus === 6">上架</span>
            <span v-if="scope.row.ipublishstatus === 7">下架</span>
          </template>
        </el-table-column>
        <el-table-column
          label="核查状态"
          prop="ipublishcheckstatus"
          show-overflow-tooltip
          min-width="100"
          align="center"
        >
          <template #default="scope">
            <span v-if="scope.row.ipublishcheckstatus === 0">正常</span>
            <span v-if="scope.row.ipublishcheckstatus === 1">未刊发</span>
            <span v-if="scope.row.ipublishcheckstatus === 2">刊发错误</span>
            <span v-if="scope.row.ipublishcheckstatus === 3">已解决</span>
          </template>
        </el-table-column>
        <el-table-column
          label="项目名称"
          prop="adprojectname"
          show-overflow-tooltip
          min-width="160"
          align="center"
        />
        <el-table-column
          label="直接客户"
          prop="customername"
          show-overflow-tooltip
          min-width="160"
          align="center"
        />
        <el-table-column label="代理公司" prop="agencyname" show-overflow-tooltip min-width="160" />
        <el-table-column
          label="内容生产方"
          prop="agentname"
          show-overflow-tooltip
          min-width="160"
          align="center"
        />
        <el-table-column
          label="广告行业"
          prop="adindustryname"
          show-overflow-tooltip
          min-width="160"
          align="center"
        />
        <el-table-column label="应收金额" prop="amountreceivable" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="已收金额" prop="amountreceived" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceived, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="欠款金额" prop="amountarrearage" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="刊发日期" prop="prestartdate" min-width="100" align="center">
          <template #default="scope">
            <span>{{ dayjs(scope.row.prestartdate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="明细标题" prop="sadtitle" min-width="300" align="center" />
        <el-table-column
          v-if="mediaType === 0 || mediaType === 1 || mediaType === 3"
          :label="getLabel(0)"
          prop="foldname"
          min-width="160"
          align="center"
        />
        <el-table-column
          v-if="mediaType === 0 || mediaType === 1 || mediaType === 3"
          :label="getLabel(1)"
          prop="foldareavername"
          min-width="160"
          align="center"
        />
        <el-table-column
          v-if="mediaType === 0 || mediaType === 1 || mediaType === 2"
          label="广告形式"
          prop="addisplayformname"
          min-width="160"
          align="center"
        />
        <el-table-column
          v-if="mediaType === 0"
          label="版面名称"
          prop="foldpageplanname"
          min-width="160"
          align="center"
        />
        <el-table-column
          v-if="mediaType === 0 || mediaType === 1 || mediaType === 2"
          :label="getLabel(2)"
          prop="pagesortname"
          min-width="160"
          align="center"
        />
        <el-table-column
          v-if="mediaType === 0 || mediaType === 1 || mediaType === 2"
          :label="getLabel(3)"
          prop="adcolorname"
          min-width="160"
          align="center"
        />
        <el-table-column
          v-if="mediaType === 0 || mediaType === 1 || mediaType === 2"
          :label="getLabel(4)"
          prop="adspecname"
          min-width="160"
          align="center"
        />
        <el-table-column
          v-if="mediaType === 2"
          label="安排媒体"
          fixed="right"
          prop="arrangefoldname"
          min-width="160"
          align="center"
        />
        <el-table-column
          v-if="mediaType === 0 || mediaType === 1 || mediaType === 3"
          :label="getLabel(6)"
          fixed="right"
          prop="arrangefoldname"
          min-width="160"
          align="center"
        />
        <el-table-column
          v-if="mediaType === 0"
          :label="getLabel(7)"
          fixed="right"
          prop="arrangefoldareavername"
          min-width="160"
          align="center"
        />
        <el-table-column
          v-if="mediaType === 0"
          :label="getLabel(8)"
          fixed="right"
          prop="arrangefoldpageplanname"
          width="120"
          align="center"
        >
          <template #default="scope">
            <el-select
              v-model="scope.row.foldpageplanid"
              placeholder="版面"
              :disabled="isDisabled(scope.row)"
              style="width: 80px"
              size="small"
              @change="handleArrangeSelect(scope.row)"
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
          v-if="mediaType === 1"
          :label="getLabel(8)"
          fixed="right"
          prop="arrangefoldpageplanname"
          width="120"
          align="center"
        >
          <template #default="scope">
            <el-select
              v-model="scope.row.arrangefoldareaverid"
              placeholder="栏目"
              :disabled="isDisabled(scope.row)"
              style="width: 80px"
              size="small"
              @change="handleArrangeSelect(scope.row)"
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
          v-if="mediaType === 3"
          :label="getLabel(8)"
          fixed="right"
          prop="arrangefoldpageplanname"
          width="120"
          align="center"
        >
          <template #default="scope">
            <el-select
              v-model="scope.row.arrangefoldareaverid"
              placeholder="项目名称"
              :disabled="isDisabled(scope.row)"
              style="width: 80px"
              size="small"
              @change="handleArrangeSelect(scope.row)"
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

        <el-table-column label="备注" prop="sremark" min-width="160" fixed="right" align="center">
          <template #default="scope">
            <el-input
              v-model="scope.row.sremark"
              :disabled="isDisabled(scope.row)"
              placeholder="备注"
              size="small"
              :title="scope.row.sremark"
            ></el-input>
          </template>
        </el-table-column>
        <el-table-column
          v-if="editurl === 1"
          label="链接"
          prop="sremark"
          min-width="160"
          fixed="right"
          align="center"
        >
          <template #default="scope">
            <el-input
              v-model="scope.row.spublishedurl"
              :disabled="isDisabled(scope.row)"
              placeholder="广告链接"
              size="small"
              :title="scope.row.spublishedurl"
            ></el-input>
          </template>
        </el-table-column>

        <el-table-column
          label="操作"
          align="center"
          width="160"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              v-if="scope.row.ipublishstatus === 3 || scope.row.ipublishstatus === 2"
              link
              style="margin-left: 10px"
              type="success"
              size="small"
              title="发布"
              @click="handleSingleArrange(scope.row)"
              >发布</el-button
            >
            <el-button
              v-if="scope.row.ipublishstatus === 4 || scope.row.ipublishstatus === 5"
              link
              style="margin-left: 10px"
              type="warning"
              icon="close"
              size="small"
              title="取消刊发"
              @click="updateOrderStatus(scope.row, 3)"
              >取消刊发</el-button
            >
            <el-button
              v-if="
                scope.row.ipublishstatus === 0 ||
                scope.row.ipublishstatus === 2 ||
                scope.row.ipublishstatus === 3
              "
              link
              style="margin-left: 10px"
              type="success"
              icon="check"
              size="small"
              title="设置刊发"
              @click="updateOrderStatus(scope.row, 4)"
              >设置刊发</el-button
            >
            <el-button
              v-if="mediaType === 0"
              link
              style="margin-left: 10px"
              type="primary"
              icon="view"
              size="small"
              title="核查"
              @click="checkOrder(scope.row)"
              >核查</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="queryvo.pageNum"
        class="pagination"
        style="margin-left: 10px; width: 100%"
        :page-sizes="pageSizes"
        :page-size="queryvo.pageSize"
        small
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <!--
      核查列表
    -->
    <div v-if="showflag === 1" class="table_box">
      <el-table
        :data="checkdataList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        border
        @selection-change="handleSelectionChange"
      >
        <el-table-column label="合同号" prop="scontractnum" min-width="160" align="center" />
        <el-table-column
          label="核查状态"
          prop="ipublishcheckstatus"
          show-overflow-tooltip
          min-width="100"
          align="center"
        >
          <template #default="scope">
            <span v-if="scope.row.ipublishcheckstatus === 1" style="color: #e6a23c">未刊发</span>
            <span v-if="scope.row.ipublishcheckstatus === 2" style="color: #409eff">刊发错误</span>
          </template>
        </el-table-column>
        <el-table-column
          label="核查报告"
          prop="spublishcheckcontent"
          show-overflow-tooltip
          min-width="300"
          align="center"
        />
        <el-table-column
          label="直接客户"
          prop="customername"
          show-overflow-tooltip
          min-width="160"
          align="center"
        />
        <el-table-column
          label="代理公司"
          prop="agencyname"
          show-overflow-tooltip
          min-width="160"
          align="center"
        />
        <el-table-column
          label="内容生产方"
          prop="agentname"
          show-overflow-tooltip
          min-width="160"
          align="center"
        />
        <el-table-column
          label="广告行业"
          prop="adindustryname"
          show-overflow-tooltip
          min-width="160"
          align="center"
        />
        <el-table-column label="应收金额" prop="amountreceivable" min-width="100" align="center" />
        <el-table-column label="已收金额" prop="amountreceived" min-width="100" align="center" />
        <el-table-column label="欠款金额" prop="amountarrearage" min-width="100" align="center" />
        <el-table-column label="刊发日期" prop="prestartdate" min-width="100" align="center" />
        <el-table-column label="明细标题" prop="sadtitle" min-width="300" align="center" />
      </el-table>
      <el-pagination
        v-model:current-page="queryvo.pageNum"
        class="pagination"
        style="margin-left: 10px; width: 100%"
        :page-sizes="pageSizes"
        :page-size="queryvo.pageSize"
        small
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!--
      订单核查对话框
    -->
    <el-dialog
      v-model="dialogCheckVisible"
      title="核查订单"
      :width="700"
      append-to-body
      :destroy-on-close="true"
    >
      <div>
        <el-row>
          <el-col :span="24">
            <el-form
              ref="orderCheckFormRef"
              :model="orderCheckForm"
              label-width="120px"
              :rules="rules"
              class="demo-workReportForm"
              status-icon
            >
              <el-form-item label="核查状态" prop="ipublishcheckstatus">
                <el-select
                  v-model="orderCheckForm.ipublishcheckstatus"
                  placeholder="请选择核查状态"
                  style="width: 160px; margin-right: 5px"
                >
                  <el-option
                    v-for="item in baseTask.OrderCheckType"
                    :key="item.id"
                    :label="item.value"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="核查报告" prop="spublishcheckcontent">
                <el-input
                  v-model="orderCheckForm.spublishcheckcontent"
                  style="width: 90%; margin-right: 5px"
                  :rows="3"
                  type="textarea"
                  class="inputwindht"
                  resize="none"
                  placeholder="请输入..."
                />
              </el-form-item>
              <div style="text-align: center; width: 100%; margin-top: 30px">
                <el-button type="primary" icon="Check" @click="updateCheckStatus()">保存</el-button>
                <el-button icon="Close" @click="dialogCheckVisible = false">取消</el-button>
              </div>
            </el-form>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
    <!--
      打印邮签
    -->
    <div id="printform" class="printdiv"></div>
    <!-- 根据合同号查看详情 -->
    <AdTworderdetails ref="viewImageRef"></AdTworderdetails>
    <!--
      安排广告订单对话框
    -->
    <el-dialog
      v-model="dialogArrangeOrder"
      title="广告订单安排且发布"
      :width="700"
      append-to-body
      :destroy-on-close="true"
    >
      <el-form
        :model="arrangeBatchDTO"
        label-width="120px"
        :rules="rules"
        class="demo-workReportForm"
        status-icon
      >
        <el-form-item label="媒体">
          <span>{{ mediaName }}</span>
        </el-form-item>
        <el-form-item v-if="mediaType === 0" label="安排版面">
          <el-select v-model="columnid" placeholder="版面" style="width: 200px">
            <el-option
              v-for="item in columnlist"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="mediaType === 1" label="安排栏目">
          <el-select v-model="columnid" placeholder="栏目" style="width: 200px">
            <el-option
              v-for="item in columnlist"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="mediaType === 3" label="安排项目">
          <el-select v-model="columnid" placeholder="项目名称" style="width: 200px">
            <el-option
              v-for="item in columnlist"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="batchremark" placeholder="请输入备注" clearable style="width: 80%" />
        </el-form-item>
        <el-form-item v-if="editurl === 1" label="广告链接">
          <el-input v-model="batchlink" placeholder="请输入广告链接" clearable style="width: 80%" />
        </el-form-item>
        <div style="text-align: center; width: 100%; margin-top: 30px">
          <el-button type="primary" icon="Check" @click="batchArrangeClick()">确定发布</el-button>
          <el-button icon="Close" @click="dialogArrangeOrder = false">取消</el-button>
        </div>
      </el-form>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { IDataCombo } from '@/types/common/DataCombo'
import { getMediaDataComboApi } from '@/api/media/media'
import { FormInstance, FormRules, dayjs } from 'element-plus'
import { computTableHeight, required, tableHeaderColor, formatMoney } from '@/utils'
import {
  getPendPublishOrderItemListApi,
  modifyOrderItemStatusApi,
  checkOrderItemApi,
  getCheckOrderListApi,
  exportCheckOrderExcelApi,
  arrangeOrderItemApi,
  getAdUrlParameterApi
} from '@/api/schedule/orderpublish'
import { IFoldVO } from '@/types/views/media/fold'
import { getFoldComboApi } from '@/api/media/foldareaver'
import { getFoldAreaverComboApi } from '@/api/schedule/seekschedule'
import baseTask from '@/types/views/schedule/orderpublish'
import { IOrderCheckDTO } from '@/types/views/schedule/orderpublish'
import { IAxios } from 'axios'
import print from 'print-js'
import { fa } from 'element-plus/es/locale'
defineOptions({
  name: 'OrderitemPublish'
})
onMounted(() => {
  getMediaDataCombo()
  getFoldPageList()
  getOrderPublistList()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})
// 批量安排叠次版本ID
const columnid = ref('')
// 批量安排广告链接
const batchlink = ref('')
// 批量安排备注
const batchremark = ref('')
/** 位置列表对象 */
const columnlist = ref<any>([])
/** 请求对象 */
const queryvo = ref({
  mediaId: '',
  foldId: '',
  areaverId: '',
  stratTime: '',
  endTime: '',
  pageNum: 0,
  pageSize: 20,
  publishstatus: ''
})
/** 订单安排请求对象 */
const arrangeDTO = ref({
  id: '',
  mediaType: '',
  arrangeData: '',
  remark: '',
  adURL: ''
})
/** 订单安排请求对象 */
const arrangeBatchDTO = ref({
  id: '',
  mediaType: '',
  arrangeData: '',
  remark: '',
  adURL: ''
})
/** 广告订单是否配置URL请求对象 */
const adurlvo = ref({
  mediaId: ''
})
/** 打印对象 */
const printObj = ref({
  scontacts: '',
  saddress: '',
  smobilephone: '',
  spostcode: ''
})

/** 媒体类型 *
 * 0:南方日报 南方晚报
 * 1：南方+
 * 2：微信、微博
 **/
const mediaType = ref(0)
/** 时间 */
const timedata = reactive<TDateType>({
  timetype: '30',
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
/** 刊发状态 */
const publistStatus = ref('')
/** 刊发/核查 */
const showtype = ref('刊发')
/** 是否禁用打印邮签按钮 */
const isPrintButton = ref(true)
/** 邮签打印 */
const printTag = ref(true)
/** 核查订单 */
const dialogCheckVisible = ref(false)
/** 安排广告订单 */
const dialogArrangeOrder = ref(false)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 查询记录总条数 */
const total = ref(0)
/** 表格高度 */
const tableheight = ref(0)
/** 订单批量安排允许标志 */
const batcharrange = ref(0)
/** 媒体 */
const mediaId = ref('1694885889376604160')
/** 媒体名称 */
const mediaName = ref('南方日报')
/** 媒体下拉列表 */
const mediaCombo = ref<IDataCombo[]>([])
/** 起始日期 */
const startTime = ref('')
/** 列表 */
const dataList = ref<any>([])
/** 列表 */
const checkdataList = ref<any>([])
/** 结束日期 */
const endTime = ref('')
/** 叠次ID */
const floadId = ref('')
/** 叠次名 */
const floadName = ref('叠次')
/** 叠次版本ID */
const floaAreaverdId = ref('')
/** 叠次版本名 */
const floaAreaverdName = ref('叠次版本')
/** 叠次列表 */
const foldList = ref<any>([])
/** 叠次版本列表 */
const foldAreaverList = ref<any>([])
/** 刊发/核查 */
const showflag = ref(0)
/** 是否编辑广告URL */
const editurl = ref(0)
/** 核查请求对象 */
const orderCheckForm = ref<IOrderCheckDTO>({
  id: '0',
  ipublishcheckstatus: '0',
  spublishcheckcontent: ''
})
const mimeMap = {
  xlsx: 'application/vnd.ms-excel',
  zip: 'application/zip'
}
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<IOrderCheckDTO>>({
  ipublishcheckstatus: [{ required: true, message: '请选择核查状态', trigger: 'blur' }],
  spublishcheckcontent: [{ required: true, message: '核查报告不能为空', trigger: 'blur' }]
})

/**
 * 选中数据存放
 * @protected
 * @return array
 */
let multipleSelection: Array<any> = []
/**
 * 获取媒体下拉列表
 */
const getMediaDataCombo = () => {
  getMediaDataComboApi().then((res: IAxios<IDataCombo[]>) => {
    if (res.success) {
      mediaCombo.value = res.obj
      console.log('媒体')
      console.log(mediaCombo.value)
    }
  })
}
/**
 * 媒体事件
 */
const handleMedia = (event: any) => {
  var obj = mediaCombo.value.find((item) => {
    return item.id === event
  })
  console.log(obj?.name)
  mediaName.value = obj!.name
  if (obj?.name === '南方日报' || obj?.name === '南方晚报') {
    mediaType.value = 0
    floadId.value = ''
    floaAreaverdId.value = ''
    floaAreaverdName.value = '叠次版本'
    floadName.value = '叠次'
    getFoldPageList()
  } else if (obj?.name === '南方+' || obj?.name === '南方网') {
    mediaType.value = 1
    floadId.value = ''
    floaAreaverdId.value = ''
    floaAreaverdName.value = '栏目'
    floadName.value = '频道'
    getFoldPageList()
  } else if (obj?.name === '日报微信' || obj?.name === '日报微博') {
    mediaType.value = 2
  } else if (obj?.name === '多元化') {
    floaAreaverdName.value = '项目名称'
    floadName.value = '项目分类'
    mediaType.value = 3
    floadId.value = ''
    floaAreaverdId.value = ''
    getFoldPageList()
  }
  getAdURLParameter()
  getOrderPublistList()
}
/**
 * 刊发状态事件
 */
const handlePublishStatus = (event: any) => {
  getOrderPublistList()
}
/**
 * 时间选择
 * @param val
 */
const ontime = (val: TDateTimeType) => {
  startTime.value = val.startTime
  endTime.value = val.endTime
  if (startTime.value === endTime.value) {
    if (floadId.value !== '') {
      batcharrange.value = 1
    } else {
      batcharrange.value = 0
    }
  } else {
    batcharrange.value = 0
  }
  if (showflag.value === 0) {
    getOrderPublistList()
  } else {
    getCheckOrderList()
  }
}
/**
 * 叠次选择事件
 */
const handleFlod = () => {
  if (startTime.value === endTime.value) {
    if (floadId.value !== '') {
      batcharrange.value = 1
    } else {
      batcharrange.value = 0
    }
  } else {
    batcharrange.value = 0
  }
  floaAreaverdId.value = ''
  getFoldAreaverList()
  getOrderPublistList()
}
/**
 * 叠次版本选择事件
 */
const handleFlodAreaver = () => {
  getOrderPublistList()
}
/**
 * 获取待处理广告订单
 */

const getOrderPublistList = () => {
  if (mediaId === null || mediaId.value === '') {
    ElMessage.info('请选择媒体')
    return false
  }

  queryvo.value.mediaId = mediaId.value
  queryvo.value.foldId = floadId.value
  queryvo.value.areaverId = floaAreaverdId.value
  queryvo.value.stratTime = startTime.value
  queryvo.value.endTime = endTime.value
  queryvo.value.publishstatus = publistStatus.value
  getPendPublishOrderItemListApi(queryvo.value).then((res) => {
    if (res.success) {
      dataList.value = res.obj
      if (res.total > 0) {
        columnlist.value.length = 0
        var temparray = dataList.value[0].pageNumList
        for (var i = 0; i < temparray.length; i++) {
          columnlist.value.push(temparray[i])
        }
      }
      total.value = res.total
      console.log(dataList.value)
    }
  })
}
/**
 * 获取核查报告
 */

const getCheckOrderList = () => {
  if (mediaId === null || mediaId.value === '') {
    ElMessage.info('请选择媒体')
    return false
  }
  queryvo.value.mediaId = mediaId.value
  queryvo.value.stratTime = startTime.value
  queryvo.value.endTime = endTime.value
  getCheckOrderListApi(queryvo.value).then((res) => {
    if (res.success) {
      checkdataList.value = res.obj
      total.value = res.total
      console.log(dataList.value)
    }
  })
}
/**
 * 检查框选择
 * @param row,index
 */
const handleSelectionChange = (val: any) => {
  console.log(val)
  multipleSelection = val
  if (multipleSelection.length > 0) {
    isPrintButton.value = false
  } else {
    isPrintButton.value = true
  }
}
const table_index = (val: number) => {
  // eslint-disable-next-line eqeqeq
  const pagenum =
    queryvo.value.pageSize! * (queryvo.value.pageNum === 0 ? 0 : queryvo.value.pageNum! - 1) + 1
  return val + pagenum
}
/**
 * 设置刊发
 * @param row
 */
const onSetPublish = (row: any) => {}
/**
 * 改变页码总数调用
 */
const handleSizeChange = (val: number) => {
  queryvo.value.pageSize = val
  getOrderPublistList()
}
/**
 * 改变页数调用
 * @param val
 */
const handleCurrentChange = (val: number) => {
  queryvo.value.pageNum = val
  getOrderPublistList()
}

/**
 * 获取叠次列表
 */
const getFoldPageList = () => {
  getFoldComboApi(mediaId.value).then((res: IAxios<IFoldVO[]>) => {
    if (res.success) {
      foldList.value = res.obj
      console.log(foldList.value)
    }
  })
}

/**
 * 获取叠次版本列表
 */
const getFoldAreaverList = () => {
  getFoldAreaverComboApi(floadId.value).then((res: IAxios<IFoldVO[]>) => {
    if (res.success) {
      foldAreaverList.value = res.obj
      console.log(foldAreaverList.value)
    }
  })
}
/**
 * 更新订单状态
 */
const updateOrderStatus = async (row: any, status: number) => {
  var obj = { id: row.id, ipublishstatus: status }
  ElMessageBox.confirm('是否更改刊发状态？', '提示', {
    type: 'warning'
  })
    .then(() => {
      modifyOrderItemStatusApi(obj)
        .then((res: IAxios) => {
          if (res.success) {
            ElMessage.success(res.msg)
            getOrderPublistList()
          }
        })
        .catch(() => {})
    })
    .catch(() => {})
}

/**
 * 核查订单
 */
const checkOrder = (row: any) => {
  orderCheckForm.value.id = row.id
  if (row.ipublishcheckstatus != null) {
    orderCheckForm.value.ipublishcheckstatus = row.ipublishcheckstatus.toString()
  } else {
    orderCheckForm.value.ipublishcheckstatus = '0'
  }
  orderCheckForm.value.spublishcheckcontent = row.spublishcheckcontent
  dialogCheckVisible.value = true
}

/**
 * 打印邮签
 */
const printPostTag = (row: any) => {
  console.log(row)
  var printFrom = document.getElementById('printform')
  const focuser = setInterval(() => window.dispatchEvent(new Event('focus')), 500)
  var content =
    "<div class='printrow'><div class='itemclass'>邮编：</div><div class='itemclasstt'>" +
    row.spostcode +
    '</div></div>'
  content +=
    "<div class='printrow'><div class='itemclass'>地址：</div><div class='itemclasstt'>" +
    row.saddress +
    '</div></div>'
  content +=
    "<div class='printrow'><div class='itemclass'>联系电话：</div><div class='itemclasstt'>" +
    row.smobilephone +
    '</div></div>'
  content +=
    "<div class='printrow'><div class='itemclass'>联系人：</div><div class='itemclasstt'>" +
    row.scontacts +
    '</div></div>'
  const styletemp =
    '.itemclass{ width: 100px;text-align: right;  float: left;}.itemclasstt{ width: 100px;text-align:left;  float: left;}.printrow{ height: 45px;line-height: 45px;text-align: center; clear: both; margin-left: 35%;}'
  printFrom!.innerHTML = content
  print({
    printable: 'printform',
    type: 'html',
    style: styletemp,
    scanStyles: false,
    targetStyles: ['*'],
    maxWidth: 1920,
    onPrintDialogClose: () => {
      console.log('打印完成')
      clearInterval(focuser)
      printTag.value = false
      printFrom!.innerHTML = ''
    }
  })
}

/**
 * 订单核查
 */
const updateCheckStatus = async () => {
  checkOrderItemApi(orderCheckForm.value)
    .then((res: IAxios) => {
      if (res.success) {
        ElMessage.success(res.msg)
        getOrderPublistList()
        dialogCheckVisible.value = false
      }
    })
    .catch(() => {})
}
/**
 * 设置刊发
 */
const handlePublish = async () => {
  ElMessageBox.confirm('是否将选择的记录设置刊发？', '提示', {
    type: 'warning'
  })
    .then(() => {
      for (var i = 0; i < multipleSelection.length; i++) {
        var obj = { id: multipleSelection[i].id, ipublishstatus: 4 }
        modifyOrderItemStatusApi(obj)
          .then((res: IAxios) => {
            getOrderPublistList()
          })
          .catch(() => {})
      }
      ElMessage.success('刊发设置成功')
    })
    .catch(() => {})
}
/**
 * 安排事件
 */
const handleArrangeClick = async () => {
  columnid.value = ''
  batchlink.value = ''
  batchremark.value = ''
  dialogArrangeOrder.value = true
}
/**
 * 安排
 */
const handleArrange = async () => {
  ElMessageBox.confirm('是否安排选择的广告订单？', '提示', {
    type: 'warning'
  })
    .then(() => {
      for (var i = 0; i < multipleSelection.length; i++) {
        var obj = { id: multipleSelection[i].id, ipublishstatus: 4 }
        modifyOrderItemStatusApi(obj)
          .then((res: IAxios) => {
            getOrderPublistList()
          })
          .catch(() => {})
      }
      ElMessage.success('已选订单发布成功')
    })
    .catch(() => {})
}
/**
 * 打印邮签
 */
const handlePrint = () => {
  var printFrom = document.getElementById('printform')
  const focuser = setInterval(() => window.dispatchEvent(new Event('focus')), 500)
  var content = ''
  for (var i = 0; i < multipleSelection.length; i++) {
    content += "<div class='printitem'>"
    content +=
      "<div class='printrow'><div class='itemclass'>邮编：</div><div class='itemclasstt'>" +
      multipleSelection[i].spostcode +
      '</div></div>'
    content +=
      "<div class='printrow'><div class='itemclass'>地址：</div><div class='itemclasstt'>" +
      multipleSelection[i].saddress +
      '</div></div>'
    content +=
      "<div class='printrow'><div class='itemclass'>联系电话：</div><div class='itemclasstt'>" +
      multipleSelection[i].smobilephone +
      '</div></div>'
    content +=
      "<div class='printrow'><div class='itemclass'>联系人：</div><div class='itemclasstt'>" +
      multipleSelection[i].scontacts +
      '</div></div>'
    content += '</div>'
  }
  const styletemp =
    '.printitem{width: 100;height: auto;margin-top: 30px;  border-bottom: 1px solid #000000; padding-bottom: 15px;}.itemclass{ width: 100px;text-align: right;  float: left;}.itemclasstt{ width: 100px;text-align:left;  float: left;}.printrow{ height: 45px;line-height: 45px;text-align: center; clear: both; margin-left: 35%;}'
  printFrom!.innerHTML = content
  print({
    printable: 'printform',
    type: 'html',
    style: styletemp,
    scanStyles: false,
    targetStyles: ['*'],
    maxWidth: 1920,
    onPrintDialogClose: () => {
      console.log('打印完成')
      clearInterval(focuser)
      printTag.value = false
      printFrom!.innerHTML = ''
    }
  })
}

/**
 * 刊发/核查变换
 */
const changeTheme = (val: any) => {
  if (val === '刊发') {
    showflag.value = 0
    getOrderPublistList()
  } else {
    showflag.value = 1
    getCheckOrderList()
  }
  console.log(val)
}

/**
 * 导出EXCEL
 */
const exportExcel = async () => {
  if (mediaId === null || mediaId.value === '') {
    ElMessage.info('请选择媒体')
    return false
  }
  queryvo.value.mediaId = mediaId.value
  queryvo.value.stratTime = startTime.value
  queryvo.value.endTime = endTime.value
  exportCheckOrderExcelApi(queryvo.value)
    .then((res: IAxios) => {
      console.log(res)
      downExcel(res, mimeMap.xlsx)
    })
    .catch(() => {})
}

/**
 * 下载EXCEL
 */
const downExcel = (res: any, mimeType: any) => {
  // 创建a标签，并处理二进制数据
  const aLink = document.createElement('a')
  const blob = new Blob([res.data], { type: mimeType })
  // 生成下载链接
  const URL = window.URL || window.webkitURL
  aLink.href = URL.createObjectURL(blob)
  // 设置下载文件名称
  let fileName = ''
  if (res.headers['content-disposition']) fileName = res.headers['content-disposition']
  if (res.headers['Content-Disposition']) fileName = res.headers['Content-Disposition']
  aLink.setAttribute('download', fileName)
  // 下载
  document.body.appendChild(aLink)
  aLink.click()
  // 释放URL对象
  window.URL.revokeObjectURL(aLink.href)
  document.body.removeChild(aLink)
}

/**
 * 获取lable
 */
const getLabel = (val: any) => {
  if (val === 0) {
    if (mediaType.value === 0) return '叠次'
    if (mediaType.value === 1) return '频道'
    if (mediaType.value === 2) return '广告形式'
    if (mediaType.value === 3) return '项目分类'
  } else if (val === 1) {
    if (mediaType.value === 0) return '叠次版本'
    if (mediaType.value === 1) return '栏目'
    if (mediaType.value === 2) return '广告形式'
    if (mediaType.value === 3) return '项目名称'
  } else if (val === 2) {
    if (mediaType.value === 0) return '版面类别'
    if (mediaType.value === 1) return '内容/曝光率'
    if (mediaType.value === 2) return '位置'
    if (mediaType.value === 3) return '项目分类'
  } else if (val === 3) {
    if (mediaType.value === 0) return '色彩'
    if (mediaType.value === 1) return '广告样式'
    if (mediaType.value === 2) return '广告样式'
    if (mediaType.value === 3) return '项目分类'
  } else if (val === 4) {
    if (mediaType.value === 0) return '规格'
    if (mediaType.value === 1) return '尺寸'
    if (mediaType.value === 2) return '尺寸'
    if (mediaType.value === 3) return '项目名称'
  } else if (val === 6) {
    if (mediaType.value === 0) return '排后叠次'
    if (mediaType.value === 1) return '排后频道'
    if (mediaType.value === 2) return '广告形式'
    if (mediaType.value === 3) return '项目分类'
  } else if (val === 7) {
    if (mediaType.value === 0) return '叠次版本'
    if (mediaType.value === 1) return '栏目'
    if (mediaType.value === 2) return '广告形式'
    if (mediaType.value === 3) return '项目分类'
  } else if (val === 8) {
    if (mediaType.value === 0) return '安排版面'
    if (mediaType.value === 1) return '栏目'
    if (mediaType.value === 2) return '广告形式'
    if (mediaType.value === 3) return '项目名称'
  }
}
// 安排广告订单
const handleSingleArrange = (row: any) => {
  ElMessageBox.confirm('是否将广告订单设置为发布状态？', '提示', {
    type: 'warning'
  })
    .then(() => {
      arrangeDTO.value.id = row.id
      arrangeDTO.value.mediaType = mediaType.value.toString()
      if (mediaType.value === 0) {
        arrangeDTO.value.arrangeData = row.foldpageplanid
      } else if (mediaType.value === 1 || mediaType.value === 3) {
        arrangeDTO.value.arrangeData = row.arrangefoldareaverid
      }
      arrangeDTO.value.remark = row.sremark
      arrangeDTO.value.adURL = row.spublishedurl
      handleOrderArrange()
    })
    .catch(() => {})
}
// 处理订单安排
const handleOrderArrange = () => {
  arrangeOrderItemApi(arrangeDTO.value).then((res) => {
    if (res.success) {
      getOrderPublistList()
    }
  })
}
// 获取订单配置URL参数
const getAdURLParameter = () => {
  adurlvo.value.mediaId = mediaId.value
  getAdUrlParameterApi(adurlvo.value).then((res) => {
    if (res.success) {
      if (res.msg === '0') {
        editurl.value = 0
      } else {
        editurl.value = 1
      }
    }
  })
}
// 订单安排位置选择事件
const handleArrangeSelect = (row: any) => {}
// 是否禁用位置列表框
const isDisabled = (row: any) => {
  if (row.ipublishstatus === 5 || row.ipublishstatus === 4) {
    return true
  }
  return false
}

// 批量安排事件
const batchArrangeClick = () => {
  arrangeDTO.value.remark = batchremark.value
  arrangeDTO.value.adURL = batchlink.value
  arrangeDTO.value.mediaType = mediaType.value.toString()
  arrangeDTO.value.arrangeData = columnid.value
  for (var i = 0; i < multipleSelection.length; i++) {
    arrangeDTO.value.id = multipleSelection[i].id
    arrangeOrderItemApi(arrangeDTO.value).then((res) => {
      if (res.success) {
        getOrderPublistList()
      }
    })
  }
  dialogArrangeOrder.value = false
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

<style lang="scss" scoped>
.printdiv {
  width: 100%;
  text-align: center;
  margin-left: 400px;
}

.printitem {
  width: 100;
  height: auto;
  margin-top: 30px;
  margin-bottom: 15px;
  border-bottom: 1px solid #000000;
}

.printrow {
  height: 45px;
  line-height: 45px;
  text-align: center;
  clear: both;
}

.itemclass {
  width: 100px;
  text-align: right;
  float: left;
  font-size: 30px;
}

.itemclasstt {
  width: 100px;
  text-align: left;
  float: left;
}
</style>
