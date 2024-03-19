<!--
 * @Author: yanz
 * @Date: 2023-11-08 13:01:31
 * @LastEditors: wanghl
 * @LastEditTime: 2024-03-18 14:36:00
 * @Description: 预开发票申请
 *
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button type="primary" icon="Plus" @click="onAdd">新增</el-button>
        <el-button type="success" icon="Top" @click="onSubmit()">提交申请</el-button>
        <el-button type="danger" icon="Delete" @click="onDelete()">删除</el-button>
        <HgDateIndex @onresponse="ontime"></HgDateIndex>
        <el-checkbox
          v-model="debtOnly"
          inline-prompt
          style="margin-top: 5px; margin-right: 10px"
          label="只查欠款"
          @change="getPreInvoiceApplicationPageList"
        />
        <el-input
          v-model="queryKey"
          placeholder="支持客户名称,发票号,客户合同号查询"
          clearable
          style="width: 240px"
          @keyup.enter="getPreInvoiceApplicationPageList"
          @clear="getPreInvoiceApplicationPageList"
        />
        <el-button type="primary" icon="Search" @click="getPreInvoiceApplicationPageList"
          >搜索
        </el-button>
        <el-tooltip class="item" effect="dark" content="帮助" placement="top">
          <el-button class="help" type="text" @click="showhelp()">?</el-button>
        </el-tooltip>
      </div>
    </div>
    <div class="table_box">
      <el-row>
        <el-table
          :data="preinvoiceApplicationList"
          row-key="id"
          :height="tableHeight"
          :border="true"
          stripe
          :header-cell-style="tableHeaderColor"
          @sort-change="handleSortChange"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="50" align="center"></el-table-column>
          <el-table-column
            prop="deptname"
            label="部门"
            min-width="150"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="employname"
            label="主业务员"
            min-width="100"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="customername"
            label="客户名称"
            min-width="150"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column prop="icusttype" label="客户类型" width="120" align="center">
            <template #default="scope">
              <span v-if="scope.row.icusttype == 0"> 直接客户 </span>
              <span v-if="scope.row.icusttype == 1"> 代理公司 </span>
              <span v-if="scope.row.icusttype == 2"> 内容生产方 </span>
            </template>
          </el-table-column>
          <el-table-column prop="dapplytime" label="申请日期" width="160"></el-table-column>
          <el-table-column prop="namountapply" label="申请金额" min-width="120" align="right">
            <template #default="scope">
              <span>{{ formatMoney(scope.row.namountapply, '2') }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="printitemname" label="开票项目 " width="120" show-overflow-tooltip>
          </el-table-column>
          <el-table-column
            prop="namountreceived"
            label="已还金额"
            sortable="custom"
            min-width="120"
            align="right"
          >
            <template #default="scope">
              <span>{{ formatMoney(scope.row.namountreceived, '2') }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="invoice" label="发票号" :min-width="200" sortable="custom">
          </el-table-column>
          <el-table-column prop="sprintname" label="开票名称" sortable="custom" min-width="150">
          </el-table-column>
          <el-table-column
            prop="businessentityname"
            label="经营主体"
            sortable="custom"
            min-width="150"
          >
          </el-table-column>
          <el-table-column label="审批状态" prop="iapprovestatus" width="100" align="center">
            <template #default="scope">
              <span v-if="scope.row.iapprovestatus === EApproveStatus.待审"
                ><el-tag type="warning">待审</el-tag></span
              >
              <span v-if="scope.row.iapprovestatus === EApproveStatus.在审">
                <el-tag>在审</el-tag></span
              >
              <span v-if="scope.row.iapprovestatus === EApproveStatus.通过"
                ><el-tag type="success">通过</el-tag></span
              >
              <span v-if="scope.row.iapprovestatus === EApproveStatus.否决"
                ><el-tag type="danger">否决</el-tag></span
              >
              <span v-if="scope.row.iapprovestatus === EApproveStatus.撤销"
                ><el-tag type="danger">撤销</el-tag></span
              >
              <span v-if="scope.row.iapprovestatus === EApproveStatus.无效"
                ><el-tag type="danger">无效</el-tag></span
              >
            </template>
          </el-table-column>
          <el-table-column
            prop="sapprovalopinions"
            label="审批意见"
            min-width="200"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column prop="sremark" label="备注 " :width="200" align="center">
          </el-table-column>
          <el-table-column
            label="操作"
            align="left"
            width="220"
            class-name="small-padding fixed-width"
            fixed="right"
          >
            <template #default="scope">
              <el-button
                v-if="
                  scope.row.iapprovestatus === EApproveStatus.待审 ||
                  scope.row.iapprovestatus === EApproveStatus.否决
                "
                size="small"
                link
                type="success"
                icon="Top"
                @click="onSubmit(scope.row)"
                >提交
              </el-button>
              <el-button
                link
                title="详情"
                type="primary"
                size="small"
                icon="View"
                style="margin: 0 5px"
                @click="onDetail(scope.row, scope.$index)"
                >详情
              </el-button>
              <el-button
                v-if="
                  scope.row.iapprovestatus === EApproveStatus.待审 ||
                  scope.row.iapprovestatus === EApproveStatus.否决
                "
                link
                type="warning"
                icon="Edit"
                size="small"
                @click="onEdit(scope.row)"
                >修改
              </el-button>
              <el-button
                v-if="scope.row.iapprovestatus === EApproveStatus.待审"
                link
                type="danger"
                icon="Delete"
                size="small"
                style="margin-left: 5px"
                @click="onDelete(scope.row)"
                >删除
              </el-button>
              <!-- <el-button
                v-if="scope.row.iapprovestatus !== EApproveStatus.待审"
                link
                type="text"
                icon="RefreshRight"
                size="small"
                @click="seelist(scope.row, scope.$index)"
                >历史</el-button
              > -->
            </template>
          </el-table-column>
        </el-table>
      </el-row>
      <el-row>
        <!-- 页码 -->
        <el-pagination
          v-model:current-page="pageIndex"
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
    <el-dialog
      v-model="editShow"
      title="预开票申请录入"
      width="1000"
      append-to-body
      destroy-on-close
      :close-on-click-modal="false"
      @close="onCancel"
    >
      <el-form
        ref="preinvoApplyRef"
        :model="preinvoApplyForm"
        :rules="rules"
        label-width="120px"
        style="padding-right: 30px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="业务员" prop="employid">
              <HgZtreeSelect
                v-if="user?.blead || user?.adminflag !== 0"
                :placeholder="user.username"
                :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYPERSON"
                :selectids="preinvoApplyForm.employid ? [preinvoApplyForm.employid] : []"
                :filterable="true"
                clearable
                :treeparams="{ bIgnorePermissions: true }"
                style="width: 560px"
                @selectionztree="onSelectionNewEmp"
              ></HgZtreeSelect>
              <el-input v-else :value="user?.username" placeholder="业务员" disabled></el-input>
            </el-form-item>
            <el-form-item label="客户名称" prop="customername">
              <el-input v-model="preinvoApplyForm.customername" placeholder="请选择客户" readonly>
                <template #append>
                  <el-button
                    icon="Search"
                    circle
                    title="选择客户"
                    @click="onSelectCustomer('from')"
                  />
                </template>
              </el-input>
            </el-form-item>
            <el-form-item label="开票类型" prop="iinvoicetype">
              <el-select
                v-model="preinvoApplyForm.iinvoicetype"
                placeholder="请选择开票类型"
                style="width: 560px"
                @change="optionChanged"
              >
                <!-- <el-option
                  v-for="t in preinvoiceStyleCombo"
                  :key="t.id"
                  :label="t.name"
                  :value="t.id"
                ></el-option> -->
                <el-option
                  v-for="(value, name) in filteredEPreinvoiceStyle"
                  :key="value"
                  :label="name"
                  :value="value"
                  :class="{ 'gray-font': value <= 80 }"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item prop="sprintname" label="开票名称" sortable="custom" min-width="150">
              <el-input v-model="preinvoApplyForm.sprintname"></el-input>
            </el-form-item>
            <el-form-item label="开票项目" prop="printitemid">
              <el-select
                v-model="preinvoApplyForm.printitemid"
                placeholder="请选择开票项目"
                clearable
                filterable
                style="width: 560px"
              >
                <el-option
                  v-for="t in adprintitemCombo"
                  :key="t.id"
                  :label="t.sname"
                  :value="t.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="经营主体" prop="businessentityid">
              <el-select
                v-model="preinvoApplyForm.businessentityid"
                placeholder="请选择经营主体"
                clearable
                filterable
                style="width: 560px"
                @change="optionChanged"
              >
                <el-option
                  v-for="t in businessentityCombo"
                  :key="t.id"
                  :label="t.sname"
                  :value="t.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="开票金额" prop="namountapply">
              <HgMoney
                v-model="preinvoApplyForm.namountapply"
                placeholder="请输入开票金额"
              ></HgMoney>
            </el-form-item>
            <!-- 暂时不需要按类型显示合同 -->
            <!-- <div v-show="preinvoApplyForm.iapplytype?.toString() === '1'"> -->
            <!-- </div> -->
          </el-col>
          <el-col :span="12">
            <!--            <el-form-item label="申请类型" prop="iapplytype" min-width="150">-->
            <!--              <el-radio-group-->
            <!--                v-model="preinvoApplyForm.iapplytype"-->
            <!--                class="ml-4"-->
            <!--                @change="optionChanged"-->
            <!--              >-->
            <!--                <el-radio-button label="1">预开</el-radio-button>-->
            <!--                <el-radio-button label="2">挂开</el-radio-button>-->
            <!--              </el-radio-group>-->
            <!--            </el-form-item>-->
            <el-form-item prop="icusttype" label="客户类型" sortable="custom" min-width="150">
              <el-input
                v-if="preinvoApplyForm.icusttype === -1"
                value="未选择客户"
                disabled
              ></el-input>
              <span v-else>{{ ECustomerType[preinvoApplyForm.icusttype as number] }}</span>
            </el-form-item>
            <el-form-item label="申请日期" prop="dapplytime">
              <el-date-picker
                v-model="preinvoApplyForm.dapplytime"
                type="date"
                :clearable="false"
                value-format="YYYY-MM-DD"
                :shortcuts="shortcuts"
                style="width: 560px"
                disabled
              />
            </el-form-item>
            <el-form-item label="项目名称" prop="adprojectname">
              <HgAdproject
                style="width: 100%"
                ref="HgAdprojectdiv"
                v-model="preinvoApplyForm.adprojectname"
                @selectiondata="getadprojectname"
              ></HgAdproject>
              <!-- <el-input v-model="preinvoApplyForm.adprojectname" placeholder="请选择项目" readonly>
                <template #append>
                  <el-button icon="Search" circle title="选择项目" @click="onSelectAdproject()" />
                </template>
              </el-input> -->
            </el-form-item>
            <el-form-item
              prop="spayercreditcode"
              label="客户识别号"
              sortable="custom"
              min-width="150"
            >
              <el-input v-model="preinvoApplyForm.spayercreditcode"></el-input>
            </el-form-item>
            <el-form-item prop="spayeraddr" label="客户地址电话" sortable="custom" min-width="150">
              <el-input v-model="preinvoApplyForm.spayeraddr"></el-input>
            </el-form-item>
            <el-form-item
              prop="spayerbank"
              label="客户银行及账户"
              sortable="custom"
              min-width="150"
            >
              <el-input v-model="preinvoApplyForm.spayerbank"></el-input>
            </el-form-item>
            <el-form-item label="备注" prop="sremark">
              <el-input v-model="preinvoApplyForm.sremark"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="width: 100%">
          <el-col>
            <el-form-item label="关联合同" prop="contractVos">
              <el-button
                v-model="preinvoApplyForm.contractVos"
                type="primary"
                icon="Plus"
                readonly
                @click="onSelectContract('from')"
              >
                选择合同
              </el-button>
              <el-button type="danger" plain icon="Delete" @click="onDeleteContract()"
                >移除选中
              </el-button>
            </el-form-item>
          </el-col>
          <el-table
            :data="preinvoApplyForm.contractVos"
            row-key="id"
            :border="true"
            show-summary
            :summary-method="getSummaries"
            stripe
            style="margin-left: 50px"
            @selection-change="handleSelectOrderChange"
          >
            <el-table-column type="selection" prop="ischeck" :width="50" align="center">
            </el-table-column>
            <el-table-column label="合同编号" sortable="custom" min-width="150" prop="scontractnum">
            </el-table-column>
            <el-table-column label="广告标题" sortable="custom" min-width="150" prop="sadtitle">
            </el-table-column>
            <el-table-column label="广告分类" min-width="160" prop="adtypename"></el-table-column>
            <el-table-column
              label="欠款金额"
              sortable="custom"
              min-width="150"
              prop="amountarrearage"
              align="right"
            >
              <template #default="scope">
                <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
              </template>
            </el-table-column>
            <el-table-column
              label="广告行业"
              sortable="custom"
              min-width="150"
              prop="adindustryname"
            >
            </el-table-column>
            <el-table-column label="经营主体" min-width="160" prop="businessentityname">
            </el-table-column>
            <el-table-column
              label="直接客户"
              sortable="custom"
              min-width="150"
              prop="customername"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column label="代理公司" sortable="custom" min-width="150" prop="agencyname">
            </el-table-column>
            <el-table-column label="内容生产方" sortable="custom" min-width="150" prop="agentname">
            </el-table-column>
            <el-table-column
              label="操作"
              align="center"
              width="70"
              class-name="small-padding fixed-width"
              fixed="right"
            >
              <template #default="scope">
                <el-button link type="danger" icon="Delete" @click="onDeleteContract(scope.row)"
                  >移除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-row>
        <el-row :span="20">
          <el-col>
            <el-form-item v-if="flowlistsl === true" label="选择审核流程" prop="applicationid">
              <HgFlowTypeSelect
                style="width: 400px"
                :flowtypename="'flowpreinvoice'"
                :flowid="preinvoApplyForm.applicationid"
                @selecflowtype="selecflowtype"
              ></HgFlowTypeSelect>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :span="20" style="margin-top: 20px">
          <el-col>
            <el-form-item label="文件上传" prop="applyfiles">
              <el-row style="width: 100%">
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
                  <div v-for="(file, index) in preinvoApplyForm.applyfiles" :key="file.sfileid">
                    <el-link type="primary" :underline="false" @click="previewFile(file)">
                      <el-icon>
                        <Document />
                      </el-icon>
                      {{ file.soriginalfile }}
                    </el-link>
                    <el-button
                      link
                      type="danger"
                      icon="CircleClose"
                      @click="onUpfileDel(index)"
                    ></el-button>
                  </div>
                  <span
                    v-show="
                      Number(preinvoApplyForm.iapplytype) === EPreinvoiceApplyType.挂开 &&
                      preinvoApplyForm &&
                      preinvoApplyForm.applyfiles &&
                      preinvoApplyForm.applyfiles.length === 0
                    "
                    style="color: #e6a23c; font-size: 13px"
                    >请上传证明材料，图片格式</span
                  >
                </el-col>
              </el-row>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer" style="text-align: center">
          <el-button
            v-show="preinvoApplyForm.id === ''"
            type="primary"
            icon="Check"
            @click="saveAndContinue(preinvoApplyRef)"
            >保存，并继续新增
          </el-button>
          <el-button type="primary" icon="Check" @click="onSave(preinvoApplyRef)">保 存</el-button>
          <el-button icon="Close" @click="onCancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
    <!-- 查看详情 -->
    <el-dialog
      v-model="detailshow"
      title="申请详情"
      width="1200"
      append-to-body
      destroy-on-close
      :draggable="fenbianlv() > 1440 ? true : false"
      :close-on-click-modal="false"
    >
      <el-row :gutter="10">
        <el-col :span="customerProcessInstancelist.length > 0 ? 14 : 24">
          <HgPreInvoiceApplicationDetail
            :selfdetailid="preinvoApplyForm.id"
            style="max-width: 100%; overflow-y: auto"
          ></HgPreInvoiceApplicationDetail>
        </el-col>
        <el-col
          v-show="customerProcessInstancelist.length > 0"
          :span="10"
          style="background-color: #fff"
        >
          <flow-node-format
            ref="flowNodeFormatRef"
            :disable-select="true"
            :process-instance-id="currentData?.processInstanceId"
            :flow-id="currentData?.flowId"
          ></flow-node-format>
          <div
            v-if="customerProcessInstancelist.length >= 2"
            style="width: 100%; text-align: left; padding-left: 50px"
          >
            <li v-for="item in customerProcessInstancelist" :key="item.flowId">
              <el-button
                v-if="item.result === false"
                style="color: red"
                type="text"
                @click="Seelistdetail(item)"
                ><span>{{ item.createTime }}提交备案</span></el-button
              >
              <el-button
                v-if="item.result === true"
                style="color: green"
                type="text"
                @click="Seelistdetail(item)"
                ><span>{{ item.createTime }}提交备案</span></el-button
              >
              <el-button
                v-if="item.result === null"
                style="color: #409eff"
                type="text"
                @click="Seelistdetail(item)"
                ><span>{{ item.createTime }}提交备案</span></el-button
              >
            </li>
          </div>
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 选择客户 -->
    <el-dialog
      v-model="customershow"
      title="选择客户"
      width="1000"
      align-center
      destroy-on-close
      :close-on-click-modal="false"
      append-to-body
    >
      <el-row :gutter="50">
        <el-col :span="24">
          <HgSalesCustomer
            :buse="true"
            :showbuse="false"
            @selectiondata="onselectiondata"
          ></HgSalesCustomer>
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 选择项目 -->
    <!-- <el-dialog
      v-model="adprojectshow"
      title="选择项目"
      width="1000"
      align-center
      destroy-on-close
      :close-on-click-modal="false"
      append-to-body
    >
      <el-row :gutter="50">
        <el-col :span="24">
          <HgAdproject @selectiondata="onselectadprojectdata"></HgAdproject>
        </el-col>
      </el-row>
    </el-dialog> -->
    <!-- 关联合同 -->
    <el-dialog
      v-model="contractshow"
      title="选择关联合同"
      width="1000"
      align-center
      destroy-on-close
      :close-on-click-modal="false"
      append-to-body
    >
      <el-row :gutter="50">
        <el-col :span="24">
          <HgPreinvoapplyContract
            :buse="true"
            :showbuse="false"
            :employeeid="preinvoApplyForm.employid ? preinvoApplyForm.employid : user.userid"
            :customerid="preinvoApplyForm.customerid"
            @selectiondata="onselectioncontract"
          >
          </HgPreinvoapplyContract>
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 审核历史记录 -->
    <el-dialog v-model="rightDrawerVisible" width="700" title="审核详情" style="min-height: 800px">
      <flow-node-format
        ref="flowNodeFormatRef"
        :disable-select="true"
        :form-data="currentData.formData"
        :process-instance-id="currentData.processInstanceId"
        :flow-id="currentData.flowId"
        :customer-process-instance="customerProcessInstancelist"
      ></flow-node-format>
      <div
        v-if="customerProcessInstancelist.length >= 2"
        style="width: 100%; text-align: left; padding-left: 50px"
      >
        <li v-for="item in customerProcessInstancelist" :key="item.flowId">
          <el-button
            v-if="item.result === false"
            style="color: red"
            type="text"
            @click="Seelistdetail(item)"
            ><span>{{ item.createTime }}提交备案</span></el-button
          >
          <el-button
            v-if="item.result === true"
            style="color: green"
            type="text"
            @click="Seelistdetail(item)"
            ><span>{{ item.createTime }}提交备案</span></el-button
          >
          <el-button
            v-if="item.result === null"
            style="color: #409eff"
            type="text"
            @click="Seelistdetail(item)"
            ><span>{{ item.createTime }}提交备案</span></el-button
          >
        </li>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { deepClone } from '@/utils'
import { IAxios } from 'axios'
import { FormInstance, FormRules } from 'element-plus'
import { getFlowlistComboByFlowTypeApi } from '@/api/flow/index'
import { computTableHeight, tableHeaderColor, fenbianlv, formatMoney } from '@/utils/index'
import { THgFileUploadParam, TUpFile } from '@/types/components/hgfileupload'
import {
  TPreInvoiceApplicationQuery,
  IPreInvoiceApplicationDTO,
  IPreInvoiceApplicationVO,
  IPreInvoiceApplicationApprove,
  IPreinvoiceApplicationDetail,
  IPreInvoiceApplicationFile
} from '@/types/views/business/preinvoiceapplication'
import {
  EApproveStatus,
  EHgDeptZtreeUrl,
  EFlowType,
  EPreinvoiceStyle,
  EPreinvoiceApplyType,
  ECustomerType
} from '@/types/common/enumindex'
import { getEnumCombo } from '@/api/common/index'
import {
  getPreInvoiceApplicationPageListApi,
  savePreinvoapplyAsPendingApi,
  getPreinvoiceapplicationByIdApi,
  updatePreinvoiceapplicationApi,
  deletePreinvoiceapplicationApi,
  approvePreinvoiceapplicationApi,
  getPreinvoiceStyleComboApi
} from '@/api/business/preinvoiceapplication'
import { TworderforPreinvoapplyVO } from '@/types/views/ad/adorder'
import { IBusinessentity } from '@/types/views/finance/businessentity'
import { getBusinessentityComboApi } from '@/api/finance/businessentity'
import { getAdPrintItemComboApi } from '@/api/finance/adprintitem'
import { IAdprintitem } from '@/types/views/finance/adprintitem'
import { TSelectZtree } from '@/types/common'
import { useRouter } from 'vue-router'

/** 流程id */
const flowdata = ref<IPreInvoiceApplicationApprove>({
  ids: '',
  flowId: ''
})
/** 显示隐藏合同下拉**/
const showContract = ref('1')
/** 审批状态下拉列表 */
const approveStatusList: IDataCombo[] = getEnumCombo(EApproveStatus)
/** 判断流程是否一条 */
const flowlistsl = ref(false)
/** 获取流程参数 */
const flowEFlowType: IDataCombo[] = getEnumCombo(EFlowType)
import dayjs from 'dayjs'
import { IDataCombo } from '@/types/common/DataCombo'
import type { TAdCustomer } from '@/types/views/customer'
import { TDateTimeType } from '@/types/components/hgdateindex'
import modal from '@/plugins/modal'
import { Tadindustrylist, TypeCurrentData } from '@/types/components/hgindustry'
import useUserStore from '@/store/modules/user'
import FlowNodeFormat from '@/components/Flow/FlowNodeFormatData.vue'
import { TAdProject } from '@/types/views/ad/adproject'
defineOptions({
  name: 'TpreinvoiceApply'
})
// import { useRouter } from 'vue-router'
const global = getCurrentInstance()?.appContext.config.globalProperties
const user = useUserStore().user
const router = useRouter()
/** Form名称 */
const preinvoApplyRef = ref<FormInstance>()
/** Form选中的行 */
const multipleSelection = ref<IPreInvoiceApplicationVO[]>([])
const multipleSelectOrder = ref<TworderforPreinvoapplyVO[]>([])
/** 上传组件参数 */
const hgfileuploadparam = ref<THgFileUploadParam>({
  server: import.meta.env.VITE_APP_BASE_API + '/common/UploadFile/importFile',
  multiple: true,
  filenumlimit: 20,
  storytypes: []
})
/** 编辑状态 */
const isEditing = ref(false)
/**
 * 获取开票类型
 */
const preinvoiceStyleCombo = ref()
const filteredEPreinvoiceStyle = computed(() => {
  return Object.entries(EPreinvoiceStyle)
    .filter(([name, value]) => typeof value === 'number')
    .sort((a, b) => Number(b[1]) - Number(a[1])) // 倒序
    .reduce((acc, [name, value]) => ({ ...acc, [name]: value }), {})
})
// 分页相关
const sort = ref('dapplytime')
const order = ref('desc')
const pageSizes = [15, 20, 30, 40]
const pageIndex = ref(1)
const pageSize = ref(15)
/** 查询总页数 */
const pageTotal = ref(0)
/** 查询仅欠款 */
const debtOnly = ref(false)
/** 表格高度 */
const tableHeight = ref<number>(0)
/** 列表 */
// const customerChargeList = ref<TCustomerChargeVO[]>([])
const preinvoiceApplicationList = ref<IPreInvoiceApplicationVO[]>([])
/** 查询关键字 */
const queryKey = ref('')
const timeParams = reactive({
  startTime: '',
  endTime: ''
})
/** 编辑显示状态 */
const editShow = ref(false)
/** 详情显示状态 */
const detailshow = ref(false)
/** 经营主体下拉 */
const businessentityCombo = ref<IBusinessentity[]>([])
/** 开票项目下拉 */
const adprintitemCombo = ref<IAdprintitem[]>([])
/** from表单初始化对象 */
const preinvoApplyInit: IPreInvoiceApplicationDTO = {
  /**
   * 申请表id
   */
  id: '',
  // /**
  //  * 部门id
  //  */
  // deptid: '',
  // /**
  //  * 部门名称
  //  */
  // deptname: '',
  /**
   * 广告项目id
   */
  adprojectid: '',
  /**
   * 业务员id
   */
  employid: '',
  /**
   * 业务员名称
   */
  employname: '',
  /**
   * 客户id
   */
  customerid: '',
  /**
   * 客户名称
   */
  customername: '',
  /**
   * 客户地址电话
   */
  spayeraddr: '',
  /**
   * 客户银行账户
   */
  spayerbank: '',
  /**
   * 0-直接客户、1-代理公司、2-内容生产方
   */
  icusttype: -1,
  /**
   * 选中合同Vo
   */
  contractVos: [],
  /**
   * 申请时间
   */
  dapplytime: dayjs().format('YYYY-MM-DD'),
  /**
   * 申请金额
   */
  namountapply: 0,
  /**
   * 开票项目id
   */
  printitemid: '',
  /**
   * 开票项目
   */
  printitemname: '',
  /**
   * 已还金额
   */
  amountreceived: 0,
  /**
   * 申请类型 1-预开 2-挂开
   */
  iapplytype: 1,
  /**
   * 开票类型(82-数电普票 81-数电专票)
   */
  iinvoicetype: 82,
  // /**
  //  * 发票号
  //  */
  // invoice: '',
  // /**
  //  * 发票编号
  //  */
  // invoicecode: '',
  /**
   * 开票名称
   */
  sprintname: '',
  /**
   * 客户识别号
   */
  spayercreditcode: '',
  /**
   * 经营主体id
   */
  businessentityid: '',
  /**
   * 经营主体名称
   */
  businessentityname: '',
  /**
   * 申请表id
   */
  applicationid: '',
  // /**

  //  * 审批状态(0待审、1在审、2通过、3否决、4撤销、5无效)
  //  */
  // iapprovestatus: 0,
  /**
   * 文件列表
   */
  applyfiles: []
}
const applyFileslist = ref<IPreInvoiceApplicationFile[]>([])
/** 添加form */
const preinvoApplyForm = ref<IPreInvoiceApplicationDTO>({ ...preinvoApplyInit })
/** 通过选择合同情况确定开票类型 */
watch(
  () => preinvoApplyForm.value.contractVos,
  (newVal) => {
    // 如果 contractVos 数组为空，将 iapplytype 设置为 2（挂开）
    // 否则，将 iapplytype 设置为 1（预开）
    preinvoApplyForm.value.iapplytype =
      newVal && newVal.length ? EPreinvoiceApplyType.预开 : EPreinvoiceApplyType.挂开
    console.log('iapplytype', preinvoApplyForm.value.iapplytype)
  },
  { deep: true }
)
/** 验证规则 */
const rules = ref({
  customername: [{ required: true, message: '客户不能为空', trigger: 'change' }],
  // 不选合同（contractVos为空）时，项目名称必选
  adprojectname: [
    {
      validator: (rule: any, value: any, callback: (error?: Error) => void) => {
        if (!preinvoApplyForm.value.contractVos?.length ?? true) {
          if (!value) {
            callback(new Error('不选择合同时，广告项目必选'))
          } else {
            callback()
          }
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ],
  // 对namountapply校验，必须大于0
  namountapply: [
    { required: true, message: '开票金额不能为空', trigger: 'change' },
    {
      validator: (rule: any, value: any, callback: (error?: Error) => void) => {
        if (value <= 0) {
          callback(new Error('开票金额必须大于0'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  printitemid: [{ required: true, message: '开票项目不能为空', trigger: 'change' }],
  sprintname: [{ required: true, message: '开票名称不能为空', trigger: 'change' }],
  businessentityid: [{ required: true, message: '经营主体不能为空', trigger: 'change' }],
  applicationid: [{ required: true, message: '工作流申请表id不能为空', trigger: 'change' }],
  // iapplytype: [{ required: true, message: '申请类型不能为空', trigger: 'change' }],
  iinvoicetype: [{ required: true, message: '开票类型不能为空', trigger: 'change' }],
  contractVos: [
    {
      validator: (rule: any, value: any, callback: (error?: Error) => void) => {
        let warningMessage = '合同信息不能为空'
        let hasError = false
        if ((preinvoApplyForm.value.contractVos?.length ?? 0) > 0) {
          for (const item of preinvoApplyForm.value.contractVos ?? []) {
            if (item.businessentityid !== preinvoApplyForm.value.businessentityid) {
              warningMessage = '已选合同中存在与经营主体不符的合同，请重新选择'
              hasError = true
              break
            }
          }
        }
        if (hasError) {
          callback(new Error(warningMessage))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ],
  spayercreditcode: [
    {
      validator: (rule: any, value: any, callback: (error?: Error) => void) => {
        if (
          (preinvoApplyForm.value.iinvoicetype?.toString() === '81' ||
            preinvoApplyForm.value.iinvoicetype?.toString() === '0') &&
          preinvoApplyForm.value.spayercreditcode === ''
        ) {
          callback(new Error('专票时必须填写 客户代码'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ],
  spayeraddr: [
    {
      validator: (rule: any, value: any, callback: (error?: Error) => void) => {
        if (
          (preinvoApplyForm.value.iinvoicetype?.toString() === '81' ||
            preinvoApplyForm.value.iinvoicetype?.toString() === '0') &&
          preinvoApplyForm.value.spayeraddr === ''
        ) {
          callback(new Error('专票时必须填写 客户地址电话'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ],
  spayerbank: [
    {
      validator: (rule: any, value: any, callback: (error?: Error) => void) => {
        if (
          (Number(preinvoApplyForm.value.iinvoicetype) === EPreinvoiceStyle.数电专票 ||
            Number(preinvoApplyForm.value.iinvoicetype) === EPreinvoiceStyle.专用发票) &&
          preinvoApplyForm.value.spayerbank === ''
        ) {
          callback(new Error('专票时必须填写 客户银行账户'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ],
  sremark: [{ max: 200, message: '最大输入不超过200字符', trigger: 'blur' }],
  applyfiles: [
    {
      validator: (rule: any, value: any, callback: (error?: Error) => void) => {
        if (
          Number(preinvoApplyForm.value.iapplytype) === EPreinvoiceApplyType.挂开 &&
          (!preinvoApplyForm.value.applyfiles || preinvoApplyForm.value.applyfiles.length === 0)
        ) {
          callback(new Error('未选择合同时须提交证明材料'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ]
})
/** 客户选择弹出 */
const customershow = ref(false)
/** 项目选择弹出 */
const adprojectshow = ref(false)
/** 当前返回选中的客户 */
const customerQuery = ref<TAdCustomer>({})
/** 点击的from上的客户还是 */
let customerFlag = ''
/** 合同选择弹出 */
const contractshow = ref(false)
/** 当前返回选中的合同 */
// const contractQueryData = ref<TworderforPreinvoapplyVO>()
/** 点击的from上的合同还是 */
let contractFlag = ''
const shortcuts = [
  {
    text: '今天',
    value: new Date()
  }
]

onMounted(() => {
  getBusinessentityCombo()
  getAdPrintItemCombo()
  getPreinvoiceStyleCombo()
  getPreInvoiceApplicationPageList()
  /**
   * 计算表格高度
   */
  tableHeight.value = computTableHeight()
  flowlist()
})
/**
 * 经营主体下拉查询
 */
const getBusinessentityCombo = () => {
  getBusinessentityComboApi().then(({ success, obj }: IAxios<IBusinessentity[]>) => {
    if (success) {
      businessentityCombo.value = obj
    }
  })
}
/**
 * 开票项目下拉
 */
const getAdPrintItemCombo = () => {
  getAdPrintItemComboApi().then(({ success, obj }: IAxios<IAdprintitem[]>) => {
    if (success) {
      adprintitemCombo.value = obj
    }
  })
}
/**
 * 预开发票申请列表
 */
const getPreInvoiceApplicationPageList = () => {
  const data: TPreInvoiceApplicationQuery = {
    sort: sort.value,
    order: order.value,
    page: pageIndex.value,
    pageSize: pageSize.value,
    queryKey: queryKey.value,
    startTime: timeParams.startTime,
    endTime: timeParams.endTime,
    namountapply: preinvoApplyForm.value.namountapply,
    debtOnly: debtOnly.value
  }
  getPreInvoiceApplicationPageListApi(data).then((res: IAxios<IPreInvoiceApplicationVO[]>) => {
    if (res.success) {
      preinvoiceApplicationList.value = res.obj
      pageTotal.value = res.total
    }
  })
  nextTick(function () {
    tableHeight.value = computTableHeight()
  })
}
/**
 * 时间选择
 * @param val
 */
const ontime = (val: TDateTimeType) => {
  let timev = false
  if (timeParams.startTime) {
    timev = true
  }
  timeParams.startTime = val.startTime
  timeParams.endTime = val.endTime
  if (timev) {
    getPreInvoiceApplicationPageList()
  }
}
/**
 * 清空编辑form内容
 */
const clearForm = () => {
  preinvoApplyRef.value?.resetFields()
  applyFileslist.value = []
  preinvoApplyForm.value = { ...preinvoApplyInit }
  preinvoApplyForm.value.applyfiles = []
  preinvoApplyForm.value.contractVos = []
}
/**
 * 下拉菜单选中默认项
 */
const sortAndSelectFirst = (combo: any, form: any, key: any) => {
  if (combo.value.length > 0) {
    combo.value.sort((a: any, b: any) => {
      if (a.bdefault === b.bdefault) {
        return 0
      } else if (a.bdefault) {
        return -1
      } else {
        return 1
      }
    })
    form.value[key] = combo.value[0].id
  }
}

const onAdd = () => {
  editShow.value = true
  clearForm()
  sortAndSelectFirst(businessentityCombo, preinvoApplyForm, 'businessentityid')
  sortAndSelectFirst(adprintitemCombo, preinvoApplyForm, 'printitemid')

  // preinvoApplyForm.value.employid = user?.userid ?? ''
  // preinvoApplyForm.value.employname = user?.username ?? ''
}

/** 弹出客户选择 */
const onSelectCustomer = (val: string) => {
  customerFlag = val
  customershow.value = true
}
/** 弹出客户选择 */
const onSelectAdproject = () => {
  adprojectshow.value = true
}
/** 客户组件返回函数 */
const onselectiondata = (val: TAdCustomer) => {
  if (customerFlag === 'from') {
    preinvoApplyForm.value.customerid = val.id ?? ''
    preinvoApplyForm.value.customername = val.sname ?? ''
    preinvoApplyForm.value.icusttype = val.itype ?? 0
    preinvoApplyForm.value.spayeraddr = (val.saddress ?? '') + (val.sphone ?? '')
    preinvoApplyForm.value.spayerbank = val.sbankaccount ?? ''
    preinvoApplyForm.value.spayercreditcode = val.screditcode ?? ''
  } else {
    customerQuery.value = val
    getPreInvoiceApplicationPageList()
  }
  preinvoApplyForm.value.sprintname = preinvoApplyForm.value.customername
  customershow.value = false
}
/** 项目组件返回函数 */
// const onselectadprojectdata = (val: TAdProject) => {
//   preinvoApplyForm.value.adprojectid = val.id ?? ''
//   preinvoApplyForm.value.adprojectname = val.sname ?? ''
//   adprojectshow.value = false
// }
/** 广告项目传值 */
const HgAdprojectdiv = ref()
/**
 * 给广告项目名称赋值
 */
const getadprojectname = (val?: any) => {
  preinvoApplyForm.value.adprojectid = val.id ?? ''
  preinvoApplyForm.value.adprojectname = val.sname ?? ''
}
/** 清空查询选择的客户 */
const deletCustomerQuery = () => {
  customerQuery.value = {}
  getPreInvoiceApplicationPageList()
}

/** 弹出合同选择 */
const onSelectContract = (val: string) => {
  if (preinvoApplyForm.value.customerid === '') {
    modal.msgWarning('请先选择客户')
    return false
  }
  contractFlag = val
  contractshow.value = true
  isEditing.value = false
}
/** 合同组件返回函数 */
const onselectioncontract = (vals: TworderforPreinvoapplyVO[]) => {
  // 如果 contractVos 未定义，初始化为一个空数组
  if (!preinvoApplyForm.value.contractVos) {
    preinvoApplyForm.value.contractVos = []
  }

  // 创建一个 Set，用于检查 val 是否已经存在
  const contractNumSet = new Set(
    preinvoApplyForm.value.contractVos.map((item) => item.scontractnum)
  )

  vals.forEach((val) => {
    console.log('val', val)

    // 如果 contractFlag 不等于 'from'，或者 val 不存在，添加 val
    if (contractFlag !== 'from' || !contractNumSet.has(val.scontractnum)) {
      preinvoApplyForm.value.contractVos!.push(val)
      contractNumSet.add(val.scontractnum)
    }
  })

  contractshow.value = false
}
/** 清空查询选择的合同 TDOO 改个形式按钮-弹出选择-下挂展示 */
const deleteContractQuery = () => {
  customerQuery.value = {}
  getPreInvoiceApplicationPageList()
}

/**
 * 查看申请详情
 */
const onDetail = (val?: any, number?: number) => {
  console.log('val', val)
  console.log('number', number)
  preinvoApplyForm.value = { ...val }
  getPreinvoiceapplicationByIdApi(val.id ?? '').then(
    ({ success, obj }: IAxios<IPreInvoiceApplicationVO>) => {
      if (success) {
        preinvoApplyForm.value = deepClone(obj) as IPreInvoiceApplicationVO
        console.log(preinvoApplyForm.value)
      }
    }
  )
  customerProcessInstancelist.value = val.customerProcessInstance
  if (val.customerProcessInstance.length > 0) {
    // currentData.value.processInstanceId = val?.customerProcessInstance[number!].processInstanceId
    if (val && typeof val.customerProcessInstance === 'object' && number != null && number >= 0) {
      number = val?.customerProcessInstance.length - 1
      currentData.value.processInstanceId = val.customerProcessInstance[number].processInstanceId
    } else {
      console.log('Handle error', number)
      currentData.value.processInstanceId = val.customerProcessInstance[0].processInstanceId
    }
    currentData.value.flowId = val?.customerProcessInstance[number!].flowId
    // rightDrawerVisible.value = true
  } else {
    customerProcessInstancelist.value = []
    currentData.value.flowId = ''
    currentData.value.processInstanceId = ''
  }
  // 补充合同vo信息
  detailshow.value = true
}

/**
 * 编辑类型信息按钮事件
 * @param row
 */
const onEdit = (row: IPreInvoiceApplicationVO) => {
  preinvoApplyForm.value = { ...row }
  HgAdprojectdiv.value.view(preinvoApplyForm.value.adprojectname as string)
  console.log('row', row)
  optionChanged()
  console.log('files', preinvoApplyForm.value.applyfiles)
  if (!row.id) {
    ElMessage.warning('请选择预开票申请项')
    return
  }
  getPreinvoiceapplicationByIdApi(row.id).then(
    ({ success, obj }: IAxios<IPreInvoiceApplicationVO>) => {
      if (success) {
        preinvoApplyForm.value = deepClone(obj) as IPreInvoiceApplicationVO
        console.log(preinvoApplyForm.value)
        editShow.value = true
        isEditing.value = true
      }
    }
  )
}

/**
 * 编辑的时候选择业务人员
 * @param val
 */
const onSelectionNewEmp = (val: TSelectZtree[]) => {
  const employname = val.map((item) => item.name).join(',')
  const employid = val.map((item) => item.id).join(',')
  preinvoApplyForm.value.employid = employid
  preinvoApplyForm.value.employname = employname
}

/**
 * @description: 保存前处理数据
 * @param {*} data
 * @return {*}
 */
const processData = (data: IPreInvoiceApplicationDTO) => {
  data.businessentityname =
    businessentityCombo.value.find((item) => item.id === data.businessentityid)?.sname ?? ''
  data.printitemname =
    adprintitemCombo.value.find((item) => item.id === data.printitemid)?.sname ?? ''
  // if (preinvoApplyForm.value.applyfiles?.length === 0) {
  //   ElMessage.warning('请上传附件')
  //   return
  // }
  // TODO 选人员树的前端控件应该可以设置默认值，倒腾半天未遂，暂时这么处理，等问一下
  // 暂时：取消判空，空输入提交前赋值当前用户
  if (!data.employid) {
    data.employid = user.userid
    data.employname = user.username
  }
  return data
}
/**
 * @description: 调API 保存数据
 * @param {*} data
 * @param {*} isContinue
 * @return {*}
 */
const saveData = (data: IPreInvoiceApplicationDTO, isContinue: boolean) => {
  const apiFunc = data.id ? updatePreinvoiceapplicationApi : savePreinvoapplyAsPendingApi
  apiFunc(data).then((res: IAxios) => {
    if (res.success) {
      ElMessage.success(data.id ? '修改成功' : '保存成功')
      getPreInvoiceApplicationPageList()
      sortAndSelectFirst(businessentityCombo, preinvoApplyRef, 'businessentityid')
      sortAndSelectFirst(adprintitemCombo, preinvoApplyRef, 'printitemid')
      if (!isContinue) {
        editShow.value = false
      }
      // preinvoApplyRef.value?.resetFields()
    }
  })
}
/**
 * @description: 保存当前表格，并继续添加
 * @param {*} formEl
 * @return {*}
 */
const saveAndContinue = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid, fields) => {
    if (valid) {
      const data: IPreInvoiceApplicationDTO = processData({ ...preinvoApplyForm.value })
      saveData(data, true)
    } else {
      console.log('error submit!', fields)
    }
  })
  // clearForm()
}
/**
 * @description: 保存表格
 * @param {*} formEl
 * @return {*}
 */
const onSave = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid, fields) => {
    if (valid) {
      const data: IPreInvoiceApplicationDTO = processData({ ...preinvoApplyForm.value })
      saveData(data, false)
    } else {
      console.log('error submit!', fields)
    }
  })
}
const onDeleteContract = (row?: TworderforPreinvoapplyVO) => {
  const ids: (string | undefined)[] = []
  if (row) {
    ids.push(row.scontractnum)
  } else {
    multipleSelectOrder.value?.forEach((item) => {
      ids.push(item.scontractnum)
    })
  }
  if (ids.length < 1) {
    modal.msgWarning('请选择需要移除的信息')
    return false
  }
  modal.confirm('是否移除选中数据？').then(() => {
    preinvoApplyForm.value.contractVos = preinvoApplyForm.value.contractVos?.filter(
      (item) => !ids.includes(item.scontractnum)
    )
    if (preinvoApplyForm.value.contractVos) {
      preinvoApplyForm.value.contractVos
    } else {
      preinvoApplyForm.value.contractVos = []
    }
  })
}
const onDelete = (row?: IPreInvoiceApplicationVO) => {
  const ids: (string | undefined)[] = []
  if (row) {
    if (row.iapprovestatus !== 0) {
      // modal.msgWarning('只能删除待审的数据')
      return false
    }
    ids.push(row.id)
  } else {
    multipleSelection.value?.forEach((item) => {
      if (item.iapprovestatus !== 0) {
        // modal.msgWarning('只能删除待审的数据')
        return false
      }
      ids.push(item.id)
    })
  }
  if (ids.length !== 1 && ids.length !== multipleSelection.value.length) {
    modal.msgWarning('只能删除待审的数据')
    return false
  }
  if (ids.length < 1) {
    modal.msgWarning('请选择需要删除的信息')
    return false
  }
  modal.confirm('是否删除选中数据？').then(() => {
    deletePreinvoiceapplicationApi(ids.join(',')).then(({ success }: IAxios) => {
      if (success) {
        modal.msgSuccess('删除成功')
        getPreInvoiceApplicationPageList()
      }
    })
  })
}

const onSubmit = (row?: IPreInvoiceApplicationVO) => {
  const ids: (string | undefined)[] = []
  let warningMessage = ''
  if (row) {
    if (row.iapprovestatus !== EApproveStatus.待审 && row.iapprovestatus !== EApproveStatus.否决) {
      warningMessage = '只能提交待审或被驳回的数据'
    }
    ids.push(row.id)
  } else {
    for (const item of multipleSelection.value || []) {
      if (
        item.iapprovestatus !== EApproveStatus.待审 &&
        item.iapprovestatus !== EApproveStatus.否决
      ) {
        warningMessage = '只能提交待审或被驳回的数据'
        break
      }
      ids.push(item.id)
    }
  }
  if (!warningMessage && ids.length !== 1 && ids.length !== multipleSelection.value.length) {
    warningMessage = '只能提交待审或被驳回的数据'
  }
  if (!warningMessage && ids.length < 1) {
    modal.msgWarning('请选择需要提交的信息')
  }
  if (warningMessage) {
    modal.msgWarning(warningMessage)
    return false
  }
  flowdata.value.ids = ids.join(',')
  flowdata.value.flowId = flowbottowid.value
  modal.confirm('是否提交选中数据？提交后无法修改').then(() => {
    approvePreinvoiceapplicationApi(flowdata.value).then(({ success }: IAxios) => {
      if (success) {
        modal.msgSuccess('提交成功')
        getPreInvoiceApplicationPageList()
      }
    })
  })
}
/**
 * 预览上传的文件
 * @param file
 */
const previewFile = (file: IPreInvoiceApplicationFile) => {
  console.log('file', file)
  if (file.sfiletype === 'Photo') {
    window.open(file.durl)
  } else if (file.sfiletype === 'Office') {
    const url = `${global?.officePreviewUrl}?src=${file.durl}`
    window.open(url)
  }
}

/** 存放当前业务的流程id - flowId */
const flowbottowid = ref('')
const flowlist = () => {
  /** 这里用flowEFlowType[下标]定死了流程的名称 */
  getFlowlistComboByFlowTypeApi(flowEFlowType[5].id).then(({ obj }: IAxios<Tadindustrylist[]>) => {
    console.log('flowlist obj', obj)
    if (obj.length > 1) {
      flowlistsl.value = true
    } else {
      flowbottowid.value = obj[0].id
      flowlistsl.value = false
      console.log(
        'flowlist preinvoApplyForm.value.applicationid',
        preinvoApplyForm.value.applicationid,
        'flowbottowid.value',
        flowbottowid.value
      )
    }
  })
}
/** 选择流程组件 */
const selecflowtype = (val: Tadindustrylist) => {
  console.log('选择流程组件selecflowtype preinvoApplyForm.value', preinvoApplyForm.value)
  console.log('选择流程组件selecflowtype flowdata.value', flowdata.value)
  preinvoApplyForm.value.applicationid = val.id
  flowdata.value.flowId = val.id
}
/**
 * 获取上传后返回的文件列表
 * @param res
 */
const getUpFile = (res: IAxios<TUpFile>) => {
  console.log('getUpFile', res.success)
  if (res.success) {
    // const proofDocuments = res.obj as IPreInvoiceApplicationFile
    // applyFileslist.value.push(proofDocuments)
    // const index = preinvoApplyForm.value.applyfiles!.findIndex((file) => {
    //   return file.sfileid === res.obj.sfileid
    // })
    // if (index === undefined || index === null || index === -1) {
    //   // uploadFileList.value.push(res.obj)
    //   const proofDocuments = res.obj as IPreInvoiceApplicationFile
    //   proofDocuments.bdelete = false
    //   preinvoApplyForm.value.applyfiles?.push(proofDocuments)
    // } else {
    //   preinvoApplyForm.value.applyfiles![index].bdelete = false
    // }

    const proofDocuments = res.obj as IPreInvoiceApplicationFile
    applyFileslist.value.push(proofDocuments)
    const index = preinvoApplyForm.value.applyfiles!.findIndex((file) => {
      return file.sfileid === res.obj.sfileid
    })
    if (index === undefined || index === null || index === -1) {
      // uploadFileList.value.push(res.obj)
      const proofDocuments = res.obj as IPreInvoiceApplicationFile
      proofDocuments.bdelete = false
      preinvoApplyForm.value.applyfiles?.push(proofDocuments)
    } else {
      preinvoApplyForm.value.applyfiles![index].bdelete = false
    }
  }
}
/**
 * 删除上传的文件，如果是新上传文件则直接删除，已经存入数据库中的则修改删除标记，用户数据库文件删除
 * @param index
 */
const onUpfileDel = (index: number) => {
  console.log('onUpfileDel index', index)
  if (preinvoApplyForm.value.applyfiles![index].id === undefined) {
    preinvoApplyForm.value.applyfiles!.splice(index, 1)
    // console.log('onUpfileDel preinvoApplyForm.value.applyfiles', preinvoApplyForm.value.applyfiles)
    applyFileslist.value!.splice(index, 1)
  } else {
    preinvoApplyForm.value.applyfiles![index].bdelete = true
    preinvoApplyForm.value.applyfiles!.splice(index, 1)
    // console.log('onUpfileDel preinvoApplyForm.value.applyfiles', preinvoApplyForm.value.applyfiles)
  }
}

const optionChanged = () => {
  console.log(preinvoiceStyleCombo.value)
  console.log('iapplytype', preinvoApplyForm.value.iapplytype)

  preinvoApplyRef.value?.validate()
}
/**
 * 取消编辑
 */
const onCancel = () => {
  clearForm()
  editShow.value = false
}
/**
 * 排序
 * @param val
 */
const handleSortChange = (val: { prop: string; order: string }) => {
  sort.value = val.prop

  if (val.order === 'ascending') {
    order.value = 'asc'
  } else if (val.order === 'descending') {
    order.value = 'desc'
  } else {
    order.value = ''
  }
  getPreInvoiceApplicationPageList()
}
/**
 * 翻页
 * @param val
 */
const handleSizeChange = (val: number) => {
  // 分页当前页
  pageIndex.value = val
  getPreInvoiceApplicationPageList()
}
/**
 * 当前页
 * @param val
 */
const handleCurrentChange = (val: number) => {
  pageIndex.value = val
  getPreInvoiceApplicationPageList()
}
/**
 * 列表多选
 * @param val
 */
const handleSelectionChange = (val: IPreInvoiceApplicationVO[]) => {
  multipleSelection.value = val
}
const handleSelectOrderChange = (val: TworderforPreinvoapplyVO[]) => {
  multipleSelectOrder.value = val
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'resourcetype'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
/**
 * 获取开票类型（0-专用发票、2-普通发票、51-电子发票、81-数电专票、82-数电普票）
 */
const getPreinvoiceStyleCombo = () => {
  getPreinvoiceStyleComboApi().then(({ obj, success, msg }: IAxios) => {
    console.log(obj)
    preinvoiceStyleCombo.value = obj
  })
}
/**
 * 合计
 * @param param
 */
const getSummaries = (param: any) => {
  const { columns, data } = param
  const sums: string[] = []
  columns.forEach((column: any, index: number) => {
    if (index === 0) {
      sums[index] = '合计'
      return
    }
    if (column.property !== 'amountarrearage') {
      sums[index] = ''
      return
    }
    const values = data.map((item: any) => Number(item[column.property]))
    if (!values.every((value: any) => Number.isNaN(value))) {
      const sum = values.reduce((prev: any, curr: any) => {
        const value = Number(curr)
        if (!Number.isNaN(value)) {
          return prev + curr
        } else {
          return prev
        }
      }, 0)
      sums[index] = '￥ ' + formatMoney(`${sum}`, '2')
      if (!isEditing.value) {
        preinvoApplyForm.value.namountapply = sum
      }
    } else {
      sums[index] = ''
    }
  })

  return sums
}
// ---------------------------------------------------------------历史区域

/**
 * 历史显示
 */
const rightDrawerVisible = ref(false)
/**
 * 显示历史数据
 */
const currentData = ref<TypeCurrentData>({
  processInstanceId: '',
  flowId: '',
  formData: {},
  createTime: '',
  result: ''
})
const customerProcessInstancelist = ref<TypeCurrentData[]>([])
/** 查看审核历史 */
// const seelist = (val?: any) => {
//   console.log(val)
//   customerProcessInstancelist.value = val.customerProcessInstance
//   var number = val?.customerProcessInstance.length - 1
//   currentData.value.processInstanceId = val?.customerProcessInstance[number].processInstanceId
//   currentData.value.flowId = val?.customerProcessInstance[number].flowId
//   rightDrawerVisible.value = true
// }
const seelist = (val?: any, number?: number) => {
  console.log('val', val)
  console.log('number', number)
  customerProcessInstancelist.value = val.customerProcessInstance
  if (val.customerProcessInstance.length > 0) {
    // currentData.value.processInstanceId = val?.customerProcessInstance[number!].processInstanceId
    if (val && typeof val.customerProcessInstance === 'object' && number != null && number >= 0) {
      number = val?.customerProcessInstance.length - 1
      currentData.value.processInstanceId = val.customerProcessInstance[number].processInstanceId
    } else {
      console.log('Handle error', number)
      currentData.value.processInstanceId = val.customerProcessInstance[0].processInstanceId
    }
    currentData.value.flowId = val?.customerProcessInstance[number!].flowId
    rightDrawerVisible.value = true
  } else {
    ElMessage.warning('暂无审核历史')
  }
}
const Seelistdetail = (val: TypeCurrentData) => {
  currentData.value.processInstanceId = val.processInstanceId
  currentData.value.flowId = val.flowId
}
// --------------------------------------------------------
</script>

<style scoped>
ul {
  padding-left: 10px;
}

.showSearch span {
  color: #606266;
  font-size: 14px;
}

.el-form-item {
  margin-bottom: 15px;
}

.gray-font {
  color: #ccc;
}
</style>
