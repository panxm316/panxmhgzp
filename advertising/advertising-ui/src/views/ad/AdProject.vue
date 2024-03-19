<!--
 * @Author: wanghl
 * @Date: 2024-02-22 15:56:36
 * @LastEditTime: 2024-03-18 19:49:07
 * @LastEditors: wanghl
 * @Description: 广告项目管理
-->

<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
        <el-button type="danger" icon="Delete" @click="handleDelete()">删除</el-button>
        <el-button type="warning" icon="CircleCheck" @click="handleEnd()">结项</el-button>
        <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
        <el-input
          v-model="queryInfo.queryKey"
          placeholder="请输广告项目名称"
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
          <el-table-column prop="projectcode" label="项目编码" sortable="custom" min-width="130">
            <!-- <template #default="scope">
              <el-button
                link
                type="primary"
                icon="view"
                size="small"
                @click="handleSee(scope.row.id)"
                >{{ scope.row.projectcode }}</el-button
              >
            </template> -->
          </el-table-column>
          <el-table-column prop="employname" label="申请人" min-width="80"> </el-table-column>
          <el-table-column prop="deptname" label="部门" min-width="100"> </el-table-column>
          <el-table-column
            prop="nprojectbudget"
            label="项目预算"
            sortable="custom"
            min-width="120"
            align="right"
          >
            <template #default="scope">
              <span>{{ formatMoney(scope.row.nprojectbudget, '2') }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="nprojectcost"
            label="项目成本"
            sortable="custom"
            min-width="120"
            align="right"
          >
            <template #default="scope">
              <span>{{ formatMoney(scope.row.nprojectcost, '2') }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="nprojectcost"
            label="完成金额"
            sortable="custom"
            min-width="120"
            align="right"
          >
            <template #default="scope">
              <span>{{ formatMoney(scope.row.nprojectcost, '2') }}</span>
            </template>
          </el-table-column>

          <el-table-column prop="iapprovestatus" label="审核状态" width="100" align="center">
            <template #default="scope">
              <span v-if="scope.row.iapprovestatus == 0">
                <el-tag>待审</el-tag>
              </span>
              <span v-if="scope.row.iapprovestatus == 1">
                <el-tag type="danger">在审</el-tag>
              </span>
              <span v-if="scope.row.iapprovestatus == 2">
                <el-tag type="success">通过</el-tag>
              </span>
              <span v-if="scope.row.iapprovestatus == 3">
                <el-tag type="warning">否决</el-tag>
              </span>
              <span v-if="scope.row.iapprovestatus == 4">
                <el-tag type="success">撤销</el-tag>
              </span>
              <span v-if="scope.row.iapprovestatus == 5">
                <el-tag type="success">无效</el-tag>
              </span>
            </template>
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
          <el-table-column
            prop="businessentityname"
            label="经营主体"
            min-width="160"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column prop="bpayed" label="是否付款开票" width="90" align="center">
            <template #default="scope">
              <span v-if="scope.row.bpayed == false">
                <el-tag type="danger">否</el-tag>
              </span>
              <span v-if="scope.row.bpayed == true">
                <el-tag type="success">是</el-tag>
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="scontractname"
            label="合同名称"
            min-width="160"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column
            prop="scontractnum"
            label="合同编号"
            sortable="custom"
            min-width="160"
            show-overflow-tooltip
          >
          </el-table-column>

          <el-table-column
            prop="newbelongname"
            label="业绩归属人"
            min-width="120"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column prop="bauthorizer" label="是否委托授权人" width="120" align="center">
            <template #default="scope">
              <span v-if="scope.row.bauthorizer == false">
                <el-tag type="danger">否</el-tag>
              </span>
              <span v-if="scope.row.bauthorizer == true">
                <el-tag type="success">是</el-tag>
              </span>
            </template>
          </el-table-column>

          <el-table-column
            prop="authorizername"
            label="授权人名称"
            min-width="120"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="contracttypename"
            label="合同类型"
            min-width="120"
            show-overflow-tooltip
            align="center"
          >
          </el-table-column>
          <el-table-column
            prop="salecontracttypename"
            label="销售合同类型"
            min-width="120"
            show-overflow-tooltip
            align="center"
          >
          </el-table-column>
          <el-table-column
            prop="stamptypename"
            label="用章类型"
            min-width="120"
            show-overflow-tooltip
            align="center"
          >
          </el-table-column>
          <el-table-column prop="isort" label="序号" width="80" sortable="custom" align="center">
          </el-table-column>
          <el-table-column prop="screatename" label="创建人" sortable="custom" min-width="80">
          </el-table-column>
          <el-table-column prop="dcreatedate" label="创建时间" width="120">
            <template #default="scope">
              <span>{{ formatDatesm(scope.row.dcreatedate) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="sremark" label="备注" min-width="160" show-overflow-tooltip>
          </el-table-column>
          <el-table-column
            label="操作"
            align="left"
            width="230"
            class-name="small-padding fixed-width"
            fixed="right"
          >
            <template #default="adproject">
              <el-button
                v-if="adproject.row.bprojectcomplete == false"
                link
                type="primary"
                icon="plus"
                size="small"
                @click="showbukan(adproject.row)"
                >预定</el-button
              >

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
                v-if="
                  (adproject.row.bprojectcomplete == false && adproject.row.iapprovestatus === 0) ||
                  adproject.row.iapprovestatus === 3 ||
                  adproject.row.iapprovestatus === 4
                "
                link
                type="success"
                icon="Top"
                size="small"
                @click="handlechange(adproject.row.id)"
                >审核</el-button
              >
              <el-button
                v-if="
                  adproject.row.iapprovestatus === 0 ||
                  adproject.row.iapprovestatus === 3 ||
                  adproject.row.iapprovestatus === 4 ||
                  adproject.row.iapprovestatus === 5
                "
                link
                type="danger"
                icon="Delete"
                size="small"
                @click="handleDelete(adproject.row)"
                >删除</el-button
              >

              <el-button
                v-if="adproject.row.iapprovestatus == 2 && adproject.row.bprojectcomplete == false"
                link
                size="small"
                icon="CircleCheck"
                type="warning"
                @click="handleEnd(adproject.row)"
                >结项</el-button
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
      title="广告项目编辑"
      :width="fenbianlv() > 1440 ? '60%' : '84%'"
      align-center
      :close-on-click-modal="false"
    >
      <el-form ref="AdProjectFormRef" :model="adprojectData" label-width="150px" :rules="rules">
        <el-row :gutter="20">
          <el-col :span="11">
            <el-form-item label="名称" prop="sname">
              <el-input v-model="adprojectData.sname" />
            </el-form-item>
            <el-form-item label="经营主体" prop="businessentityid">
              <el-select
                v-model="adprojectData.businessentityid"
                placeholder="经营主体"
                style="width: 100%"
                @change="getbusinessentityname"
              >
                <el-option
                  v-for="t in businessentityCombo"
                  :key="t.id"
                  :label="t.sname"
                  :value="t.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="合同类型" prop="icontracttype">
              <el-select
                v-model="adprojectData.icontracttype"
                placeholder="合同类型"
                style="width: 100%"
                clearable
                @change="seleticontracttype()"
              >
                <el-option
                  v-for="t in icontracttypelist"
                  :key="t.id"
                  :label="t.name"
                  :value="t.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="销售合同类型" prop="isalecontracttype">
              <el-select
                v-model="adprojectData.isalecontracttype"
                placeholder="合同类型"
                style="width: 100%"
                clearable
                @change="seletisalecontracttype()"
              >
                <el-option
                  v-for="t in isalecontracttypelist"
                  :key="t.id"
                  :label="t.name"
                  :value="t.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="合同名称" prop="scontractname">
              <el-input v-model="adprojectData.scontractname" />
            </el-form-item>
            <el-form-item label="合同编号" prop="scontractnum">
              <el-input v-model="adprojectData.scontractnum" />
            </el-form-item>

            <!-- <el-form-item v-show="adprojectData.sname !== ''" label="项目编码" prop="projectcode">
              <el-input v-model="adprojectData.projectcode" disabled />
            </el-form-item> -->

            <!-- <el-form-item label="结项" prop="bprojectcomplete">
              <el-checkbox v-model="adprojectData.bprojectcomplete"></el-checkbox>
            </el-form-item> -->
            <!-- <el-form-item v-show="!addFlag" label="审核状态">
              <el-radio-group v-model="adprojectData.iapprovestatus" disabled>
                <el-radio :label="0">待审</el-radio>
                <el-radio :label="1">在审</el-radio>
                <el-radio :label="2">通过</el-radio>
                <el-radio :label="3">否决</el-radio>
                <el-radio :label="4">撤销</el-radio>
                <el-radio :label="5">无效</el-radio>
              </el-radio-group>
            </el-form-item> -->
            <el-form-item label="项目说明" prop="sprojectcontent">
              <el-input
                v-model="adprojectData.sprojectcontent"
                maxlength="500"
                placeholder="请输入项目说明"
                show-word-limit
                type="textarea"
              />
            </el-form-item>
            <el-form-item label="业务员" prop="authorizername">
              <HgZtreeSelect
                :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYPERSON"
                :selectids="adprojectData.authorizername ? [adprojectData.authorizername] : []"
                :filterable="true"
                :treeparams="{ bIgnorePermissions: true }"
                style="width: 560px"
                @selectionztree="onSelectionEmp"
              ></HgZtreeSelect>
            </el-form-item>
            <el-form-item prop="sremark" label="备注">
              <el-input
                v-model="adprojectData.sremark"
                maxlength="500"
                placeholder="请输入备注"
                show-word-limit
                type="textarea"
              />
            </el-form-item>
            <el-row>
              <el-col :span="11">
                <el-form-item label="是否付款开票" prop="bpayed">
                  <el-checkbox v-model="adprojectData.bpayed" />
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-form-item label="是否委托授权人" prop="bauthorizer">
                  <el-checkbox v-model="adprojectData.bauthorizer" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
          <el-col :span="11">
            <el-form-item label="业务归属" prop="newbelongid">
              <el-input
                v-model="adprojectData.newbelongname"
                placeholder="请选择客户信息"
                style="width: 100%"
                readonly
              >
                <template #append>
                  <el-button icon="Search" circle title="选择客户" @click="SeeCustomerlist()" />
                </template>
              </el-input>
            </el-form-item>
            <el-form-item label="用章类型" prop="istamptype">
              <el-select
                v-model="adprojectData.istamptype"
                placeholder="合同类型"
                style="width: 100%"
                clearable
                @change="seletistamptype()"
              >
                <el-option
                  v-for="t in istamptypelist"
                  :key="t.id"
                  :label="t.name"
                  :value="t.id"
                ></el-option>
              </el-select>
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
            <el-form-item label="项目预算" prop="nprojectbudget">
              <el-input-number
                v-model="adprojectData.nprojectbudget"
                :precision="2"
                :min="1"
                style="width: 220px"
                :controls="false"
                controls-position="right"
              />
            </el-form-item>
            <!-- <el-form-item label="">
              <el-text> 大写： </el-text>
              <el-text type="info">
                {{ convertCurrency(adprojectData.nprojectbudget) }}
              </el-text>
            </el-form-item> -->

            <el-form-item label="项目成本" prop="nprojectbudget">
              <el-input-number
                v-model="adprojectData.nprojectcost"
                :precision="2"
                :min="1"
                style="width: 220px"
                controls-position="right"
                :controls="false"
              />
            </el-form-item>
            <!-- <el-form-item label="">
              <el-text> 大写： </el-text>
              <el-text type="info">
                {{ convertCurrency(adprojectData.nprojectcost) }}
              </el-text>
            </el-form-item> -->
            <el-form-item label="开票总金额" prop="invoiceamount">
              <el-input-number
                v-model="adprojectData.invoiceamount"
                :precision="2"
                :min="1"
                style="width: 220px"
                controls-position="right"
                :controls="false"
              />
            </el-form-item>
            <el-form-item label="支付总金额" prop="payedamount">
              <el-input-number
                v-model="adprojectData.payedamount"
                :precision="2"
                :min="1"
                style="width: 220px"
                controls-position="right"
                :controls="false"
              />
            </el-form-item>
            <el-form-item label="序号" prop="isort">
              <el-input-number
                v-model="adprojectData.isort"
                :min="1"
                controls-position="right"
                style="width: 220px"
              />
            </el-form-item>
            <el-form-item label="项目资料" prop="projectfiles">
              <el-row v-if="dialogVisible" style="width: 100%">
                <el-col :span="6">
                  <HgFileUpload
                    :server="hgfileuploadparam.server"
                    :accept="hgfileuploadparam.accept"
                    :multiple="true"
                    :storytypes="hgfileuploadparam.storytypes"
                    @getupfile="getUpFile"
                  ></HgFileUpload>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <div v-for="(file, index) in adprojectData.projectfiles" :key="file.sfileid">
                    <el-link type="primary" :underline="false" @click="previewFile(file)">
                      <el-icon><Document /></el-icon>
                      {{ file.soriginalfile?.substring(file.soriginalfile.length - 40) }}</el-link
                    >
                    <el-button
                      link
                      type="danger"
                      icon="CircleClose"
                      @click="onUpfileDel(index)"
                    ></el-button>
                  </div>
                  <span v-show="adprojectfilelsit" style="color: #e6a23c; font-size: 13px"
                    >请上传项目相关资料:一般为word</span
                  >
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
    <!-- 订单预定 -->
    <el-dialog
      v-model="dialogVisibleWorder"
      title="编辑订单"
      align-center
      :close-on-click-modal="false"
      :width="fenbianlv() > 1440 ? '80%' : '94%'"
      @close="handleCancelworder"
    >
      <!-- /* 编辑 */ -->
      <AdTworder
        style="width: 100%"
        :dataadproject="AdProjectIdAndName"
        :redetdata="true"
        :bottomshow="true"
        :isbu="0"
        @closedialogVisible="closedialogVisible"
      ></AdTworder>
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
    <!-- 选择流程 -->
    <HgFlowTypeSelectSecond
      ref="HgFlowTypeSelectSecondref"
      @selecflowtype="selecflowtype"
    ></HgFlowTypeSelectSecond>
    <!-- 选择客户 -->
    <el-dialog
      v-model="Customershow"
      title="选择客户"
      width="1000"
      align-center
      :destroy-on-close="true"
      :draggable="fenbianlv() > 1440 ? true : false"
      @close="Customershow = false"
    >
      <el-row :gutter="50">
        <el-col :span="24">
          <Hg-Sales-Customer
            :buse="true"
            :showbuse="false"
            :iapprovestatus="'2'"
            @selectiondata="selectiondata"
          ></Hg-Sales-Customer>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { FormRules, FormInstance } from 'element-plus'
import { computTreeHeight, fenbianlv } from '@/utils/index'
import type { TAdProject, TAdProjectFile } from '@/types/views/ad/adproject'
import type { TQueryInfo } from '@/types/common'
import {
  getAdProjectPageListApi,
  getAdProjectMaxSortApi,
  saveAdProjectApi,
  updateAdProjectApi,
  deleteAdProjectApi,
  endAdProjectApi
} from '@/api/ad/adproject'
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
import AdProjectdetails from './AdProjectdetails.vue'
import HgFlowTypeSelectSecond from '@/components/HgFlowTypeSelectSecond/index.vue'
import useUserStore from '@/store/modules/user'
import { IDataCombo } from '@/types/common/DataCombo'
import { getEnumCombo } from '@/api/common'
import { IBusinessentity } from '@/types/views/finance/businessentity'
import { getBusinessentityComboApi } from '@/api/finance/businessentity'
defineOptions({
  name: 'AdProject'
})
import { useRouter } from 'vue-router'
const router = useRouter()
const userStore = useUserStore()
/** 订单预定 dialog */
const dialogVisibleWorder = ref(false)
/** 项目对应订单列表弹出框 */
const dialogVisibleOrder = ref(false)
/** 表格高度 */
const tableheight = ref(0)
/** 编辑 dialog */
const dialogVisible = ref(false)
/** 广告项目列表 */
const AdProjectList = ref<TAdProject[]>([])
/** 广告详情 */
const AdProjectdetailsref = ref()
/** 广告项目 */
const adprojectData = ref<TAdProject>({
  flowId: '',
  projectfiles: [],
  id: undefined,
  sname: '',
  projectcode: '',
  dstartdate: '',
  denddate: '',
  createempid: '',
  screatename: '',
  dcreatedate: '',
  sprojectcontent: '',
  nprojectbudget: 0,
  nprojectcost: 0,
  bprojectcomplete: 0,
  iapprovestatus: 0,
  sremark: '',
  isort: 0,
  employid: userStore.user?.userid.toString(),
  employname: userStore.user?.username.toString(),
  deptid: userStore.user.deptid!.toString(),
  deptname: userStore.user.deptname!.toString(),
  businessentityid: '',
  businessentityname: '',
  bpayed: true,
  icontracttype: 0,
  isalecontracttype: 0,
  scontractnum: '',
  newbelongid: '',
  newbelongname: '',
  scontractname: '',
  istamptype: 0,
  bauthorizer: true,
  authorizername: '',
  invoiceamount: 0,
  payedamount: 0,
  myreource: '',
  myresourceworth: '',
  sideresource: '',
  sireresourceworth: '',
  equipresource: '',
  giveresource: '',
  contracttypename: '',
  salecontracttypename: '',
  stamptypename: '',
  nprojectcostresidue: 0
})
/**
 * 项目文件
 */
const adprojectfile = ref<TAdProjectFile>({
  id: '',
  adprojectid: '',
  sfileformat: '',
  sfileid: '',
  sfilesize: '',
  soriginalfile: '',
  sfiletype: '',
  dcreatetime: '',
  employid: '',
  sdescription: ''
})
/** 合同类型列表 */
const icontracttypelist: IDataCombo[] = getEnumCombo(ContractType)
/** 销售合同类型 */
const isalecontracttypelist: IDataCombo[] = getEnumCombo(SalesContractType)
/** 用章类型列表 */
const istamptypelist: IDataCombo[] = getEnumCombo(StampType)
const pickerOptions = ref({
  disabledDate: function (time: Date) {
    return time.getTime() < Date.now() - 8.64e7
  }
})
/**
 * 经营主体列表
 */
const businessentityCombo = ref<IBusinessentity[]>([])
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
const AdProjectSelection = ref<TAdProject[]>()
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<TAdProject>>({
  sname: [
    { validator: required, required: true, message: '请输入名称', trigger: 'blur' },
    { max: 100, message: '不得大于100个字符', trigger: 'blur' }
  ],
  dstartdate: [{ validator: required, required: true, message: '请选择开始日期', trigger: 'blur' }],
  denddate: [{ validator: required, required: true, message: '请选择结束日期', trigger: 'blur' }],
  scontractnum: [{ validator: isNumberStr, message: '请输入数字', trigger: 'change' }],
  sprojectcontent: [{ required: true, message: '请输入项目说明', trigger: 'blur' }],
  newbelongid: [{ required: true, message: '请选择归属业务员', trigger: 'blur' }],
  projectfiles: [{ required: true, message: '请选择项目资料', trigger: 'blur' }]
  // ,
  // authorizername: [{ required: true, message: '请选择业务员', trigger: 'blur' }]
})
onMounted(() => {
  loaddata()
  getFlowlistComboflowcontract()
  getBusinessentityCombo()
  adprojectData.value.contracttypename = icontracttypelist[0].name
  adprojectData.value.salecontracttypename = isalecontracttypelist[0].name
  adprojectData.value.stamptypename = istamptypelist[0].name
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})

/**
 * 选择
 * @param rows
 */
const handleSelectionChange = (rows: TAdProject[]) => {
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
  getAdProjectPageListApi(data).then(({ obj }: IAxios<TAdProject[]>) => {
    console.log(obj)
    AdProjectList.value = obj
    pageTotal.value = obj.length
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
  getAdProjectMaxSortApi().then(({ success, obj }: IAxios<number>) => {
    adprojectData.value.isort = obj
  })
}

/** 获取经营主体下拉 */
const getBusinessentityCombo = () => {
  getBusinessentityComboApi().then(({ success, obj }: IAxios<IBusinessentity[]>) => {
    if (success) {
      console.log(obj)
      businessentityCombo.value = obj
      var filterData = obj.filter((res) => res.bdefault === true)
      if (filterData.length > 0) {
        adprojectData.value.businessentityid = filterData[0].id
        adprojectData.value.businessentityname = filterData[0].sname
      }
    }
  })
}
/**
 * 给经营主体名称赋值
 */
const getbusinessentityname = () => {
  businessentityCombo.value.forEach((item) => {
    if (item.id === adprojectData.value.businessentityid) {
      adprojectData.value.businessentityname = item.sname
    }
  })
}
const Customershow = ref(false)
import type { TAdCustomer } from '@/types/views/customer'
/** 查看客户列表信息 */
const SeeCustomerlist = () => {
  Customershow.value = true
}
/**
 * 客户赋值
 */
const selectiondata = (val: TAdCustomer) => {
  adprojectData.value.newbelongid = val.id as string
  adprojectData.value.newbelongname = val.sname as string
  Customershow.value = false
}
import { TSelectZtree } from '@/types/common'
/**
 * 查询的时候选择业务人员
 * @param val
 */
const onSelectionEmp = (val: TSelectZtree[]) => {
  const employid = val.map((item) => item.name).join(',')
  adprojectData.value.authorizername = employid
}
/**
 * 合同类型名称赋值
 */
const seleticontracttype = () => {
  icontracttypelist.forEach((item) => {
    if (item.id === adprojectData.value.icontracttype.toString()) {
      adprojectData.value.contracttypename = item.name
    }
  })
}
/**
 * 销售合同类型名称赋值
 */
const seletisalecontracttype = () => {
  isalecontracttypelist.forEach((item) => {
    if (item.id === adprojectData.value.isalecontracttype.toString()) {
      adprojectData.value.salecontracttypename = item.name
    }
  })
}
/**
 * 用章类型合同类型名称赋值
 */
const seletistamptype = () => {
  istamptypelist.forEach((item) => {
    if (item.id === adprojectData.value.istamptype.toString()) {
      adprojectData.value.stamptypename = item.name
    }
  })
}
/**
 * 新增
 */
const handleAdd = () => {
  reset()
  addFlag.value = true
  getMaxSort()
  getBusinessentityCombo()
  dialogVisible.value = true
}
/**
 * 重置
 */
const reset = () => {
  adprojectData.value = {
    sname: '',
    projectcode: '',
    dstartdate: '',
    denddate: '',
    createempid: '',
    screatename: '',
    dcreatedate: '',
    sprojectcontent: '',
    nprojectbudget: 0,
    nprojectcost: 0,
    bprojectcomplete: 0,
    iapprovestatus: 0,
    isort: 0,
    sremark: '',
    employid: userStore.user?.userid.toString(),
    employname: userStore.user?.username.toString(),
    deptid: userStore.user.deptid!.toString(),
    deptname: userStore.user.deptname!.toString(),
    businessentityid: '',
    businessentityname: '',
    bpayed: true,
    icontracttype: 0,
    isalecontracttype: 0,
    scontractnum: '',
    newbelongid: '',
    newbelongname: '',
    scontractname: '',
    istamptype: 0,
    bauthorizer: true,
    authorizername: '',
    invoiceamount: 0,
    payedamount: 0,
    myreource: '',
    myresourceworth: '',
    sideresource: '',
    sireresourceworth: '',
    equipresource: '',
    giveresource: '',
    contracttypename: '',
    salecontracttypename: '',
    stamptypename: '',
    nprojectcostresidue: 0
  }
  AdProjectFormRef.value?.clearValidate([
    'sname',
    'dstartdate',
    'denddate',
    'scontractnum',
    'sprojectcontent',
    'newbelongid',
    'projectfiles',
    'authorizername'
  ])
}
/**
 * 修改
 * @param row
 */
const handleUpdate = (row: TAdProject) => {
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
      adprojectData.value.iapprovestatus = 0
      if (!adprojectData.value.id) {
        saveAdProjectApi(adprojectData.value).then(({ success, msg }: IAxios) => {
          if (success) {
            modal.msgSuccess('操作成功')
          } else {
            modal.msgError(msg)
          }
          loaddata()
        })
      } else {
        updateAdProjectApi(adprojectData.value).then(({ success, msg }: IAxios) => {
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
    AdProjectFormRef.value?.clearValidate([
      'sname',
      'dstartdate',
      'denddate',
      'scontractnum',
      'sprojectcontent',
      'newbelongid',
      'projectfiles',
      'authorizername'
    ])
  }, 10)
}
/**
 * 删除
 * @param row
 */
const handleDelete = (row?: TAdProject) => {
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
    deleteAdProjectApi(ids.join(',')).then(({ success, msg }: IAxios) => {
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
 * 结项
 * @param row
 */
const handleEnd = (row?: TAdProject) => {
  modal.confirm('是否结项?').then(() => {
    const ids = []
    if (row) {
      ids.push(row.id)
    } else {
      // AdProjectSelection.value?.forEach((color) => ids.push(color.id))
      if (AdProjectSelection.value !== undefined) {
        AdProjectSelection.value.forEach((column) => {
          if ((column.bprojectcomplete as any) === true) {
            modal.alert('已结项的数据不能再次结项')
            return
          }
          ids.push(column.id)
        })
      }
    }
    if (!ids.length) {
      modal.msgWarning('请选择要结项的记录')
      return
    }
    endAdProjectApi(ids.join(',')).then(({ success, msg }: IAxios) => {
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
const adprojectfilelsit = ref<TAdProjectFile[]>([])
const global = getCurrentInstance()?.appContext.config.globalProperties
/**
 * 获取上传后返回的文件列表
 * @param res
 */
const getUpFile = (res: IAxios<TUpFile>) => {
  console.log(res.obj)
  if (res.success) {
    const workreport = res.obj as TAdProjectFile
    /**
     * 遍历如果sfileid！==adprojectfilelsit.value.sfileid则添加
     */
    const ishave = adprojectfilelsit.value.some((item) => item.sfileid === workreport.sfileid)
    if (ishave) {
      modal.msgError('文件已存在')
      return
    } else {
      adprojectfilelsit.value.push(workreport)
    }
    adprojectData.value.projectfiles = adprojectfilelsit.value.map((item) => {
      return {
        id: '',
        adprojectid: adprojectData.value.id,
        sfileformat: item.sfileformat,
        sfileid: item.sfileid,
        sfilesize: item.sfilesize,
        soriginalfile: item.soriginalfile,
        sfiletype: item.sfiletype,
        dcreatetime: '',
        employid: userStore.user?.userid.toString(),
        sdescription: ''
      }
    })
    return
  }
}
/**
 * 删除上传的文件，如果是新上传文件则直接删除，已经存入数据库中的则修改删除标记，用户数据库文件删除
 * @param index
 */
const onUpfileDel = (index: number) => {
  adprojectData.value.projectfiles!.splice(index, 1)
  adprojectfilelsit.value!.splice(index, 1)
}
/**
 * 预览上传的文件
 * @param file
 */
const previewFile = (file: TAdProjectFile) => {
  console.log(file)
  if (file.sfiletype === 'Photo') {
    window.open(file.sdurl)
  } else if (file.sfiletype === 'Office') {
    const url = `${global?.officePreviewUrl}?src=${file.sdurl}`
    window.open(url)
  }
}
/**
 * 补刊
 */
const showbukan = (row?: TAdProject) => {
  dialogVisibleWorder.value = true
  AdProjectIdAndName.value.id = row!.id as string
  AdProjectIdAndName.value.sname = row!.sname as string
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
/**
 * 获取审批流程下拉列表------------------------------------------------
 */
import { getFlowlistComboByFlowTypeApi } from '@/api/flow'
import {
  EHgDeptZtreeUrl,
  EApproveStatus,
  EFlowType,
  ContractType,
  SalesContractType,
  StampType
} from '@/types/common/enumindex'
import { Tadindustrylist, TypeCurrentData } from '@/types/components/hgindustry'
/** 项目审批流程 */
const flowTypeList = ref<Tadindustrylist[]>([])
/**
 * 根据流程类型获取流程下拉
 */
const getFlowlistComboflowcontract = () => {
  getFlowlistComboByFlowTypeApi(EFlowType.项目审批流程).then((res: IAxios<Tadindustrylist[]>) => {
    console.log('res', res)
    if (res.success) {
      flowTypeList.value = res.obj
    }
  })
}
/** 选择流程组件 */
const HgFlowTypeSelectSecondref = ref()
/** 流程数据 */
const stopFlowData = ref<any>({
  orderitemid: '',
  Flowid: '',
  Flowname: ''
})
/**
 * 流程赋值
 */
const selecflowtype = (row?: Tadindustrylist) => {
  stopFlowData.value.Flowid = row!.id
  stopFlowData.value.Flowname = row!.name
}
/**
 * 审核
 * @param row
 */
const handlechange = (row: string) => {
  stopFlowData.value.orderitemid = row
  if (flowTypeList.value.length === 1) {
    HgFlowTypeSelectSecondref.value.view(EFlowType.项目审批流程)
  } else if (flowTypeList.value.length > 1) {
    stopFlowData.value.Flowid = flowTypeList.value[0].id
    stopFlowData.value.Flowname = flowTypeList.value[0].name
  } else {
    modal.msgError('请先配置项目审批流程')
  }
}
/**
 * 获取审批流程下拉列表------------------------------------------------
 */
</script>

<style scoped>
ul {
  padding-left: 10px;
}
.el-form-item {
  margin-bottom: 15px;
}
</style>
