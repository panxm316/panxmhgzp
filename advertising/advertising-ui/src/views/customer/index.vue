<!--
 * @Author: wanghl
 * @Date: 2023-10-26 10:11:02
 * @LastEditTime: 2024-03-18 09:54:04
 * @LastEditors: wanghl
 * @Description:客户管理
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex" style="width: 94%">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
        <!-- <el-button type="success" icon="DocumentCopy" @click="handleCombine">合并</el-button> -->
        <el-button type="danger" icon="Delete" @click="handleDelete()">删除</el-button>
        <el-input
          v-model="queryInfo.queryKey"
          placeholder="支持客户名称，简码，编码，地址检索"
          clearable
          style="width: 300px"
          @keyup.enter="loaddata"
          @clear="loaddata"
        />
        <el-button type="primary" icon="Search" @click="loaddata">搜索</el-button>
        <el-tooltip
          class="item"
          effect="dark"
          :content="showSearch ? '隐藏非常用' : '显示非常用'"
          placement="top"
        >
          <el-button
            circle
            style="position: static"
            :icon="showSearch == false ? 'ArrowRightBold' : 'ArrowDownBold'"
            @click="toggleSearch()"
          />
        </el-tooltip>
        <el-tooltip class="item" effect="dark" content="帮助" placement="top">
          <el-button class="help" type="text" @click="showhelp()">?</el-button>
        </el-tooltip>
      </div>
      <!-- 非常用 -->
      <div v-show="showSearch" class="showSearch">
        <span>客户类型：</span>
        <el-select
          v-model="queryInfo.itype"
          placeholder="客户类型"
          clearable
          style="width: 160px"
          @clear="loaddata"
          @change="loaddata"
        >
          <el-option label="直接客户" :value="0"></el-option>
          <el-option label="代理公司" :value="1"></el-option>
          <el-option label="内容生产方" :value="2"></el-option>
        </el-select>
        <span>业务员：</span>
        <HgZtreeSelect
          :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYPERSON"
          :selectids="queryInfo.employid ? [queryInfo.employid] : []"
          :filterable="true"
          :treeparams="{ bIgnorePermissions: true }"
          style="width: 160px"
          @selectionztree="onSelectionEmp"
        ></HgZtreeSelect>
        <!-- <span>所属行业：</span>
        <HgZtreeSelect
          :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYINDUSTRY"
          :selectids="queryInfo.adindustryid ? [queryInfo.adindustryid] : []"
          style="width: 160px"
          @selectionztree="qSelectionindustry"
        ></HgZtreeSelect> -->
        <span>是否有效：</span>
        <el-select
          v-model="queryInfo.buse"
          placeholder="客户类型"
          style="width: 160px"
          @change="loaddata"
        >
          <el-option label="有效" :value="true"></el-option>
          <el-option label="全部" value=""></el-option>
          <el-option label="无效" :value="false"></el-option>
        </el-select>
      </div>
    </div>
    <div class="table_box">
      <el-row>
        <el-table
          :data="customerList"
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
            min-width="250px"
            show-overflow-tooltip
          >
            <template #default="scope">
              <el-button
                link
                title="客户详情"
                type="primary"
                size="small"
                icon="View"
                @click="seecustomerdetail(scope.row.id)"
                >{{ scope.row.sname }}</el-button
              >
            </template>
          </el-table-column>
          <!-- <el-table-column prop="sbrevitycode" label="简码" min-width="150" show-overflow-tooltip>
          </el-table-column> -->
          <el-table-column
            prop="icode"
            label="编码"
            sortable="custom"
            min-width="100"
            show-overflow-tooltip
          >
          </el-table-column>

          <el-table-column prop="itclearableshangype" label="客户类型" min-width="120">
            <template #default="scope">
              <span v-if="scope.row.itype == 0"> 直接客户 </span>
              <span v-if="scope.row.itype == 1"> 代理公司 </span>
              <span v-if="scope.row.itype == 2"> 内容生产方 </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="adindustryname"
            label="行业名称"
            sortable="custom"
            min-width="150"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column
            prop="employname"
            label="主业务员"
            sortable="custom"
            min-width="150"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column
            prop="adfromname"
            label="来源"
            sortable="custom"
            min-width="120"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column prop="iapprovestatus" label="审批状态" width="90" align="center">
            <template #default="scope">
              <span v-if="scope.row.iapprovestatus == 0">
                <el-tag>待审</el-tag>
              </span>
              <span v-if="scope.row.iapprovestatus == 1">
                <el-tag type="warning">在审</el-tag>
              </span>
              <span v-if="scope.row.iapprovestatus == 2">
                <el-tag type="success">通过</el-tag>
              </span>
              <span v-if="scope.row.iapprovestatus == 3">
                <el-tag type="danger">否决</el-tag>
              </span>
              <span v-if="scope.row.iapprovestatus == 4">
                <el-tag>撤销</el-tag>
              </span>
              <span v-if="scope.row.iapprovestatus == 5">
                <el-tag type="danger">无效</el-tag>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="scontacts" label="联系人" min-width="150" show-overflow-tooltip>
          </el-table-column>
          <el-table-column
            prop="customercategoryname"
            label="客户分类"
            min-width="120"
            align="center"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column
            prop="customerstatusname"
            label="客户状态"
            min-width="120"
            align="center"
          >
          </el-table-column>
          <el-table-column
            prop="customercreditname"
            label="客户信誉"
            min-width="120"
            align="center"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column prop="sbrand" label="客户品牌" min-width="150" show-overflow-tooltip>
          </el-table-column>
          <el-table-column prop="sphone" label="办公电话" min-width="150" show-overflow-tooltip>
          </el-table-column>
          <el-table-column prop="smobilephone" label="移动电话" min-width="150"> </el-table-column>
          <el-table-column prop="saddress" label="地址" min-width="180" show-overflow-tooltip>
          </el-table-column>
          <el-table-column prop="spostcode" label="邮政编码" min-width="150" show-overflow-tooltip>
          </el-table-column>
          <el-table-column
            prop="screditcode"
            label="统一社会信用代码"
            min-width="180"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column
            prop="sbankaccount"
            label="开户行及账号"
            min-width="180"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column
            prop="parentName"
            label="父级名称"
            min-width="150"
            align="center"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column prop="bvip" label="是否大客户" min-width="100" align="center">
            <template #default="scope">
              <span v-if="scope.row.bvip == false">
                <el-tag>普通客户</el-tag>
              </span>
              <span v-if="scope.row.bvip == true">
                <el-tag type="success">大客户</el-tag>
              </span>
            </template>
          </el-table-column>
          <!-- <el-table-column prop="sstatus" label="客户状态" min-width="90" align="center">
          </el-table-column> -->
          <el-table-column prop="dcreatetime" label="创建时间" width="160" sortable="custom">
            <template #default="scope">
              <span>{{ formatDate(scope.row.dcreatetime) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="buse" label="是否有效" width="90" align="center">
            <template #default="scope">
              <span v-if="scope.row.buse == false">
                <el-tag type="danger">无效</el-tag>
              </span>
              <span v-if="scope.row.buse == true">
                <el-tag type="success">有效</el-tag>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="bindividual" label="个人客户" width="100" align="center">
            <template #default="scope">
              <span v-if="scope.row.bindividual == false"> 非个人客户 </span>
              <span v-if="scope.row.bindividual == true"> 个人客户 </span>
            </template>
          </el-table-column>

          <el-table-column prop="isort" label="序号" width="80" sortable="custom" align="center">
          </el-table-column>
          <el-table-column
            prop="version"
            label="版本号"
            width="90"
            sortable="custom"
            align="center"
          >
          </el-table-column>
          <el-table-column prop="sremark" label="备注" min-width="160" show-overflow-tooltip>
          </el-table-column>
          <el-table-column
            label="操作"
            width="180"
            class-name="small-padding fixed-width"
            fixed="right"
          >
            <template #default="customer">
              <div style="padding-left: 10px">
                <el-button
                  v-if="customer.row.iapprovestatus == 2 || customer.row.iapprovestatus == 3"
                  link
                  type="success"
                  icon="Edit"
                  size="small"
                  @click="handleUpdate(customer.row)"
                  >修改</el-button
                >
                <el-button
                  link
                  type="success"
                  icon="DocumentCopy"
                  size="small"
                  @click="handleCombine(customer.row.id)"
                  >合并</el-button
                >

                <!-- <el-button
                  v-if="customer.row.iapprovestatus != 1"
                  link
                  type="primary"
                  icon="Place"
                  size="small"
                  @click="SeeCustomerbelong(customer.row.id)"
                  >归属</el-button
                > -->
                <el-button
                  link
                  type="danger"
                  icon="Delete"
                  size="small"
                  @click="handleDelete(customer.row)"
                  >删除</el-button
                >
              </div>
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
      v-model="detailshowSee"
      title="被合并客户"
      width="800"
      align-center
      :destroy-on-close="true"
      @close="detailshowSee = false"
    >
      <el-table
        :data="customerSelectionself"
        row-key="id"
        :height="tableheight - 300"
        :border="true"
        stripe
        :header-cell-style="tableHeaderColor"
        @sort-change="handleSortChange"
      >
        <el-table-column
          prop="sname"
          label="名称"
          sortable="custom"
          min-width="250px"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="icode"
          label="编码"
          sortable="custom"
          min-width="100"
          show-overflow-tooltip
        >
        </el-table-column>

        <el-table-column prop="itclearableshangype" label="客户类型" min-width="120">
          <template #default="scope">
            <span v-if="scope.row.itype == 0"> 直接客户 </span>
            <span v-if="scope.row.itype == 1"> 代理公司 </span>
            <span v-if="scope.row.itype == 2"> 内容生产方 </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="adindustryname"
          label="行业名称"
          min-width="150"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column prop="employname" label="主业务员" min-width="150" show-overflow-tooltip>
        </el-table-column>
      </el-table>
      <div style="width: 100%; text-align: right; margin-top: 20px">
        <el-button type="primary" icon="check" @click="handleCombinesure">确定合并</el-button>
      </div>
    </el-dialog>
    <!-- 客户编辑 -->
    <el-dialog
      v-model="dialogVisible"
      title="客户编辑"
      align-center
      :close-on-click-modal="false"
      width="1000"
      @close="handleCancel"
    >
      <el-form ref="CustomerFormRef" :model="CustomerData" label-width="150px" :rules="rules">
        <el-row :gutter="20">
          <el-col :span="11">
            <el-form-item label="名称" prop="sname">
              <!-- <el-autocomplete
                v-model="CustomerData.sname"
                :fetch-suggestions="querySearch"
                :trigger-on-focus="false"
                style="width: 100%"
                placeholder="请输入客户名称"
              /> -->
              <el-input
                v-model="CustomerData.sname"
                placeholder="请输入客户名称"
                @input="querySearch"
                @blur="showsname = false"
                @focus="showsnamecl"
              ></el-input>
              <el-card
                v-show="showsname"
                class="box-card"
                style="position: absolute; top: 40px; z-index: 1002"
              >
                <template #header>
                  <div class="card-header">
                    <span>已存在的相近客户</span>
                  </div>
                </template>
                <div v-for="file in restaurants" :key="file.id" class="text item">
                  {{ file.name }}
                </div>
              </el-card>
            </el-form-item>
            <el-form-item label="地址" prop="saddress">
              <el-input v-model="CustomerData.saddress" :maxlength="200" />
            </el-form-item>
            <el-form-item label="办公电话" prop="sphone">
              <el-input v-model="CustomerData.sphone" />
            </el-form-item>
            <el-form-item label="联系人" prop="scontacts">
              <el-input v-model="CustomerData.scontacts" />
            </el-form-item>
            <el-form-item label="移动电话" prop="smobilephone">
              <el-input v-model="CustomerData.smobilephone" />
            </el-form-item>

            <el-form-item label="统一社会信用代码" prop="screditcode">
              <el-input v-model="CustomerData.screditcode" />
            </el-form-item>
            <el-form-item label="邮政编码" prop="spostcode">
              <el-input v-model="CustomerData.spostcode" />
            </el-form-item>
            <el-form-item label="品牌" prop="sbrand">
              <el-input v-model="CustomerData.sbrand" />
            </el-form-item>
            <el-form-item label="客户信誉度" prop="customercreditid">
              <el-select
                v-model="CustomerData.customercreditid"
                placeholder="客户信誉度"
                style="width: 300px"
                @change="getcustomercreditname"
              >
                <el-option
                  v-for="item in CustomerCreditList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id!"
                />
              </el-select>
            </el-form-item>
            <el-row>
              <el-col :span="12">
                <el-form-item label="是否个人" prop="bindividual">
                  <el-radio-group
                    v-model="CustomerData.bindividual"
                    style="flex-wrap: nowrap"
                    @change="changebindividual"
                  >
                    <el-radio :label="false" style="margin-right: 5px">企事业</el-radio>
                    <el-radio :label="true">个人</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item v-if="CustomerData.bindividual == false" label="大客户" prop="bvip">
                  <el-checkbox v-model="CustomerData.bvip"></el-checkbox>
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
          <el-col :span="11">
            <!-- <el-form-item label="简码（拼音）" prop="sbrevitycode">
              <el-input v-model="CustomerData.sbrevitycode" />
            </el-form-item> -->
            <!-- <el-form-item label="编号" prop="icode">
              <el-input v-model="CustomerData.icode" />
            </el-form-item> -->

            <el-form-item label="开户行及账号" prop="sbankaccount">
              <el-input v-model="CustomerData.sbankaccount" />
            </el-form-item>
            <el-form-item prop="sremark" label="备注">
              <el-input v-model="CustomerData.sremark" :maxlength="200" />
            </el-form-item>
            <el-form-item label="上级客户" prop="parentName">
              <el-input
                v-model="CustomerData.parentName"
                placeholder="请选择客户信息"
                style="width: 450px"
                readonly
                @click="SeeCustomerlist()"
              >
                <template #append>
                  <el-button icon="Search" circle title="选择客户" @click="SeeCustomerlist()" />
                </template>
                <template #suffix>
                  <el-button
                    v-show="CustomerData.parentName"
                    link
                    icon="CircleClose"
                    @click="SeeCustomerdelete()"
                  ></el-button>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item label="序号" prop="isort">
              <el-input-number
                v-model="CustomerData.isort"
                style="width: 330px"
                :min="1"
                controls-position="right"
              />
            </el-form-item>
            <!-- <el-form-item v-if="ShowCustomerbelong == false" label="业务员" prop="employid">
              <HgZtreeSelect
                :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYPERSON"
                :selectids="CustomerData.employid ? [CustomerData.employid] : []"
                style="width: 330px"
                :filterable="true"
                :treeparams="{ bIgnorePermissions: true }"
                @selectionztree="onSelectionNewEmp"
              ></HgZtreeSelect>
            </el-form-item> -->

            <el-form-item label="行业名称" prop="adindustryid">
              <HgZtreeSelect
                :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYINDUSTRY"
                :selectids="CustomerData.adindustryid ? [CustomerData.adindustryid] : []"
                style="width: 300px"
                @selectionztree="onSelectionindustry"
              ></HgZtreeSelect>
            </el-form-item>
            <el-form-item label="客户来源" prop="adfromid">
              <HgZtreeSelect
                :scopeflag="EHgDeptZtreeUrl.CUSTOMER_GETCUSTOMERSOURCETREE"
                :selectids="CustomerData.adfromid ? [CustomerData.adfromid] : []"
                style="width: 300px"
                @selectionztree="onSelectionadfrom"
              ></HgZtreeSelect>
            </el-form-item>
            <el-form-item label="客户类型" prop="itype">
              <el-select v-model="CustomerData.itype" placeholder="客户类型" style="width: 300px">
                <el-option label="直接客户" :value="0"></el-option>
                <el-option label="代理公司" :value="1"></el-option>
                <el-option label="内容生产方" :value="2"></el-option>
              </el-select>
            </el-form-item>
            <!-- <el-form-item label="客户状态" prop="sstatus">
              <el-select v-model="CustomerData.sstatus" placeholder="客户状态" style="width: 300px">
                <el-option label="活跃" value="活跃"></el-option>
                <el-option label="不活跃" value="不活跃"></el-option>
              </el-select>
            </el-form-item> -->
            <el-form-item label="客户状态" prop="customerstatusid">
              <el-select
                v-model="CustomerData.customerstatusid"
                placeholder="客户状态"
                style="width: 300px"
                @change="getcustomerstatusname"
              >
                <el-option
                  v-for="item in CustomerStatusList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id!"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="客户分类" prop="customercategoryid">
              <el-select
                v-model="CustomerData.customercategoryid"
                placeholder="客户分类"
                style="width: 300px"
                @change="getcustomercategoryname"
              >
                <el-option
                  v-for="item in CustomerCategoryList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id!"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="是否有效" prop="buse">
              <el-checkbox v-model="CustomerData.buse"></el-checkbox>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="detailshow === false" :span="20">
          <el-col>
            <el-form-item label="客户归属" prop="customerbelong">
              <Customer-Belong-Add
                @WcustomerBelong="changeWcustomerBelong"
                :CustomerData="customerBelongList"
              ></Customer-Belong-Add>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :span="20">
          <el-col>
            <el-form-item label="文件上传" prop="customerfiles">
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
                  <div v-for="(file, index) in CustomerData.customerfiles" :key="file.sfileid">
                    <el-link type="primary" :underline="false" @click="previewFile(file)">
                      <el-icon><Document /></el-icon> {{ file.soriginalfile }}</el-link
                    >
                    <el-button
                      link
                      type="danger"
                      icon="CircleClose"
                      @click="onUpfileDel(index)"
                    ></el-button>
                  </div>
                  <span v-show="WcustomerFileslsit" style="color: #e6a23c; font-size: 13px"
                    >请上传客户资料和客户名片，word或者图片格式</span
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
    <!-- 选择客户 -->
    <el-dialog
      v-model="filesshow"
      title="选择客户"
      width="1000"
      align-center
      :destroy-on-close="true"
      @close="handleCancelfiles"
    >
      <el-row :gutter="50">
        <el-col :span="24">
          <Hg-Sales-Customer
            :buse="true"
            :selfid="selfid"
            :showbuse="false"
            @selectiondata="selectiondata"
          ></Hg-Sales-Customer>
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 选择归属 -->
    <el-dialog
      v-model="filesshowebelong"
      :title="detailshow == true ? '客户详情' : '客户归属'"
      width="1000"
      align-center
      :destroy-on-close="true"
      @close="handleCancelBelong"
    >
      <el-row :gutter="50">
        <el-col :span="24">
          <Customer-Belong
            v-if="detailshow == false"
            :customerid="CustomerBelongid"
          ></Customer-Belong>
          <Hg-See-Customer v-if="detailshow == true" :selfdetailid="selfdetailid"></Hg-See-Customer>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { FormRules, FormInstance, menuItemEmits } from 'element-plus'
import { TFlowType } from '@/types/views/flow/flow'
import type {
  TAdCustomerCategory,
  TAdCustomerStatus,
  TAdCustomerCredit
} from '@/types/views/customer/customergroup'
/** 获取客户分类下拉列表 */
import { getcustomerCategoryComboApi } from '@/api/customer/customercategory'
/** 获取客户状态下拉列表 */
import { getcustomerStatusComboApi } from '@/api/customer/customerstatus'
/** 获取客户信誉度下拉列表 */
import { getcustomerCreditComboApi } from '@/api/customer/customercredit'
// import CustomerFiles from './CustomerFiles.vue'
import CustomerBelong from './CustomerBelong.vue'
import CustomerBelongAdd from './CustomerBelongAdd.vue'
import { THgFileUploadParam, TUpFile } from '@/types/components/hgfileupload'
import type {
  TAdCustomer,
  TQueryCustomer,
  Twcustomerfiles,
  Twcustomerbelong,
  TcombineCustomer
} from '@/types/views/customer'
import {
  getCustomerPageListApi,
  getCustomerMaxSortApi,
  saveCustomeApi,
  updateCustomerApi,
  deleteCustomerByIdApi,
  getCustomerByIdApi,
  getCustomerCombo,
  combineCustomerApi
} from '@/api/customer/index'
import {
  required,
  computTableHeight,
  isNumberStr,
  formatDate,
  tableHeaderColor,
  validatePhone,
  computTreeHeight
} from '@/utils/index'
import type { IAxios } from 'axios'
import modal from '@/plugins/modal'
import useUserStore from '@/store/modules/user'
import { EHgDeptZtreeUrl } from '@/types/common/enumindex'
import { TSelectZtree } from '@/types/common'
const userStore = useUserStore()
defineOptions({
  name: 'Customer'
})
import { useRouter } from 'vue-router'
import { list, rule } from 'postcss'

const router = useRouter()
const global = getCurrentInstance()?.appContext.config.globalProperties
/** 显示隐藏非常用**/
const showSearch = ref(false)
/** 被合并客户显示隐藏 */
const detailshowSee = ref(false)
/** 表格高度 */
const tableheight = ref(0)
/** 编辑 dialog */
const dialogVisible = ref(false)
/** 客户选择 */
const filesshow = ref(false)
/** showsname显示隐藏下拉框 */
const showsname = ref(false)
/** 延时0.5秒检索 */
const timmer = ref(0)
/** 归属显示隐藏 */
const filesshowebelong = ref(false)
/** 客户id */
const CustomerBelongid = ref<string | undefined>('')
/** 客户列表 */
const customerList = ref<TAdCustomer[]>([])
/** 客户名称查重属性*/
interface RestaurantItem {
  name?: string
  id?: string
  bdefault?: string | null | boolean
}
/** 名称查询列表 */
const restaurants = ref<RestaurantItem[]>([])
/** 客户信息 */
const CustomerData = ref<TAdCustomer>({
  id: '',
  sname: '',
  /**  创建日期  */
  dcreatetime: '',
  /** 简码（拼音）    */
  sbrevitycode: '',
  /** 编号（默认是自增列） */
  icode: 1,
  /** 客户类型（0直接客户、1代理公司、3内容生产方）    */
  itype: 0,
  /** 地址    */
  saddress: '',
  /** 电话     */
  sphone: '',
  /** 统一社会信用代码/纳税人识别号     */
  screditcode: '',
  /** 开户行及账号     */
  sbankaccount: '',
  /** 联系人  */
  scontacts: '',
  /** 移动电话 */
  smobilephone: '',
  /** 邮政编码 */
  spostcode: '',
  /** 行业ID */
  adindustryid: '',
  /** 行业名称 */
  adindustryname: '',
  /** 归属主业务员ID */
  employid: '',
  /** 父级名称*/
  parentName: '',
  /** 归属主业务员名称 */
  employname: '',
  /** 父ID */
  parentid: '',
  /** 是否大客户 0-否 1-是 */
  bvip: false,
  /** 客户状态（活跃，不活跃）。。待定 */
  sstatus: '活跃',
  /** 审批状态 0-待审 1-在审 2-通过 3-否决  4-撤销 5-无效 ,2和3的是可以编辑的*/
  iapprovestatus: 2,
  /** 是否有效 0-无效 1-有效 */
  buse: true,
  /** 是否个人 0-否 1-是 */
  bindividual: false,
  /** 是否删除 0-否false 1-是true */
  bdelete: false,
  /** 序号 */
  isort: 1,
  /** 备注 */
  sremark: '',
  /** 客户归属*/
  customerbelong: [],
  /** 客户资料*/
  customerfiles: [],
  /** 并发标志 */
  version: '0',
  /** 最后操作员id */
  lastoperatorid: '',
  /** 最后操作员*/
  lastoperator: '',
  sbrand: '',
  customercategoryid: '',
  customercategoryname: '',
  customerstatusid: '',
  customerstatusname: '',
  customercreditid: '',
  customercreditname: '',
  adfromid: '',
  adfromname: ''
})
/** 修改时将自己的id传给客户选择组件 */
const selfid = ref<string | undefined>('')
/** 修改时将自己的id传给客户选择组件 */
const selfdetailid = ref<string | undefined>('')
/** 树高度 */
const treeheight = ref('')
/** 客户归属业务员列表 */
const customerBelongList = ref<Twcustomerbelong[]>([])
/** 客户归属业务员表 */
const WcustomerBelong = ref<Twcustomerbelong>({
  // customerName: '',
  // applicationName: '',
  // deptid: '',
  // deptname: '',
  // id: '',
  // customerid: '',
  // employid: '',
  // applicationid: '',
  // dcreatetime: '',
  // bprimary: true,
  // iapprovestatus: ''
  deptid: '',
  deptname: '',
  id: '',
  customerid: '',
  employid: '',
  employname: '',
  applicationid: '',
  dcreatetime: '',
  bprimary: false,
  iapprovestatus: ''
})
const WcustomerFileslsit = ref<Twcustomerfiles[]>([])
/** 客户文件表 */
const WcustomerFiles = ref<Twcustomerfiles>({
  id: '',
  customerid: '',
  createempid: '',
  sfileformat: '',
  sfileid: '',
  sfilesize: '',
  soriginalfile: '',
  dcreatetime: '',
  sfiletype: '',
  sfilecategory: '',
  bdelete: true,
  sdescription: '',
  durl: ''
})
/** 详情显示隐藏 */
const detailshow = ref(false)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 数据总条数 */
const pageTotal = ref<number>(0)
/** 查询对象 */
const queryInfo = reactive<TQueryCustomer>({
  sort: 'dcreatetime',
  order: 'desc',
  icode: '', // 编号(自增列)
  startTime: '',
  endTime: '',
  pageSize: 20,
  page: 1,
  queryKey: '', // 查询关键字
  employid: '', // 主业务员id
  adindustryid: '', // 行业id
  buse: true, // 是否启用
  bvip: '', // 是否大客户 0-否 1-是
  sstatus: '', // 客户状态（活跃，不活跃）。。待定
  iapprovestatus: '', // 审批状态 0-待审 1-在审 2-通过 3-否决  4-撤销 5-无效
  bdelete: false, // 是否删除 0-否 1-是
  bindividual: '', // 是否是个人 0-否 1-是
  itype: '', // 客户类型客户类型（直接客户、代理公司、内容生产方）
  loginUserId: userStore.user?.userid.toString(), // 登录id
  // loginUserId: '', // 登录id
  cacheDTOKey: '', // 业务对象缓存key
  customerstatusid: '', // 客户状态id
  customercreditid: '', // 客户信誉度id
  customercategoryid: '' // 客户分类id
})
const CustomerFormRef = ref<FormInstance>()
/** 选择的客户 */
const customerSelection = ref<TAdCustomer[]>()
/** 排除自己后选择的客户 */
const customerSelectionself = ref<TAdCustomer[]>()
/**
 *客户分类列表
 */
const CustomerCategoryList = ref<TFlowType[]>([])
/**
 *客户信誉度相关列表
 */
const CustomerStatusList = ref<TFlowType[]>([])
/**
 *客户状态列表
 */
const CustomerCreditList = ref<TFlowType[]>([])
/**
 * 校验合并参数
 */
const combineCustomerdata = ref<TcombineCustomer>({
  // 主客户id
  sMainId: '',
  // 被合并客户id，多个需要用，隔开
  subIds: ''
})
/**
 * 显示隐藏客户归属
 */
const ShowCustomerbelong = ref(false)
const rules = reactive<FormRules<TAdCustomer>>({
  sname: [
    { validator: required, required: true, message: '请输入名称', trigger: 'change' },
    { max: 48, message: '不得大于48个字符', trigger: 'change' }
  ],
  scontacts: [{ max: 48, message: '不得大于48个字符', trigger: 'change' }],
  smobilephone: [{ validator: validatePhone, message: '请输入电话', trigger: 'change' }],
  sphone: [
    {
      validator: validatePhone,
      required: true,
      message: '请输入正确的电话',
      trigger: 'change'
    },
    { validator: required, message: '请输入电话', trigger: 'change' }
  ],
  screditcode: [{ validator: isNumberStr, message: '请输入数字', trigger: 'change' }],
  // sbankaccount: [{ validator: isNumberStr, message: '请输入数字', trigger: 'change' }],
  spostcode: [{ validator: isNumberStr, message: '请输入数字', trigger: 'change' }],
  saddress: [{ validator: required, required: true, message: '请选择地址', trigger: 'change' }],
  sbrevitycode: [
    { validator: required, required: true, message: '请输入简码（拼音）', trigger: 'change' }
  ],
  customerfiles: [{ required: true, message: '请上传客户资料', trigger: 'change' }],
  employid: [
    {
      required: true,
      message: '请选择归属业务员',
      trigger: 'change'
    }
  ],
  adindustryid: [{ required: true, message: '请选择行业', trigger: 'change' }],
  adfromid: [{ required: true, message: '请选择客户来源', trigger: 'change' }]
})
onMounted(() => {
  loaddata()
  getCustomerCategory()
  getCustomerStatus()
  getCustomerCredit()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
  treeheight.value = computTreeHeight()
})

/**
 * 选择
 * @param rows
 */
const handleSelectionChange = (rows: TAdCustomer[]) => {
  customerSelection.value = rows
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
 * 获取客户列表
 */
const loaddata = () => {
  getCustomerPageListApi(queryInfo).then(({ obj, total }: IAxios<TAdCustomer[]>) => {
    console.log(obj)
    pageTotal.value = total
    customerList.value = obj
  })
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
 * 获取序号最大值
 */
const getMaxSort = () => {
  getCustomerMaxSortApi().then(({ success, obj }: IAxios<number>) => {
    CustomerData.value.isort = obj
    pageTotal.value = obj
  })
}

/**
 * 已选择客户查看
 */
const handleCombine = (id: string) => {
  console.log(customerSelection.value)
  selectionid.value = id
  if (customerSelection.value?.length === 0 || customerSelection.value === undefined) {
    modal.msgWarning('请选择需要被合并的客户')
    return
  }
  detailshowSee.value = true
  customerSelectionself.value = customerSelection.value?.filter((item) => item.id !== id)
}
/** 合并给的id */
const selectionid = ref('')
/**
 * 合并
 */
const handleCombinesure = () => {
  const ids = [] as string[]
  // 将选择的客户id放入字符串中并用，隔开
  customerSelection.value?.forEach((color) => ids.push(color.id as string))
  // 排除自身id
  ids.forEach((item) => {
    if (item === selectionid.value) {
      ids.splice(ids.indexOf(item), 1)
    }
  })
  combineCustomerdata.value.subIds = ids.join(',')
  if (combineCustomerdata.value.subIds === '') {
    modal.msgWarning('请选择需要被合并的客户')
    return
  }
  combineCustomerdata.value.sMainId = selectionid.value
  modal.confirm('是否要将选择客户合并到该客户').then(() => {
    combineCustomerApi(combineCustomerdata.value).then(({ success, msg }: IAxios) => {
      if (success) {
        modal.msgSuccess('操作成功')
      } else {
        modal.msgError(msg)
      }
      loaddata()
      detailshowSee.value = false
      selectionid.value = ''
    })
  })
}
/**
/**
 * 新增
 */
const handleAdd = () => {
  getMaxSort()
  customerBelongList.value = [] as Twcustomerbelong[]
  dialogVisible.value = true
  detailshow.value = false
  ShowCustomerbelong.value = false
}
/**
 * 修改
 * @param row
 */
const handleUpdate = (row: TAdCustomer) => {
  console.log(row)
  CustomerData.value = { ...row }
  customerBelongList.value = row.customerbelong!
  selfid.value = row.id
  dialogVisible.value = true
  detailshow.value = false
  CustomerBelongid.value = row.id
  ShowCustomerbelong.value = true
}
/**
 * 保存
 */
const handleSave = () => {
  if (CustomerData.value.customerbelong!.length === 0) {
    modal.msgWarning('请选择客户归属')
    return
  }
  CustomerFormRef.value?.validate((valid) => {
    if (valid) {
      if (!CustomerData.value.id) {
        saveCustomeApi(CustomerData.value).then(({ success, msg }: IAxios) => {
          if (success) {
            modal.msgSuccess('操作成功')
          } else {
            modal.msgError(msg)
          }
          loaddata()
        })
      } else {
        updateCustomerApi(CustomerData.value).then(({ success, msg }: IAxios) => {
          if (success) {
            modal.msgSuccess('操作成功')
          } else {
            modal.msgError(msg)
          }
          loaddata()
        })
      }
      dialogVisible.value = false
      redet()
    }
  })
}
/**
 * 取消
 */
const handleCancel = () => {
  CustomerFormRef.value?.resetFields()
  redet()
  loaddata()
  // WcustomerFileslsit.value = []
  dialogVisible.value = false
  setTimeout(() => {
    CustomerFormRef.value?.clearValidate([
      'sname',
      'scontacts',
      'saddress',
      'adindustryid',
      'sphone',
      'adfromid'
    ])
  }, 10)
}
/**
 * 是个人的时候取消验证
 */
const changebindividual = () => {
  if (CustomerData.value.bindividual === true) {
    rules.employid = []
    rules.adindustryid = []
    rules.customerfiles = []
  }
  if (CustomerData.value.bindividual === false) {
    rules.employid = [
      {
        required: true,
        message: '请选择归属业务员',
        trigger: 'change'
      }
    ]
    rules.adindustryid = [{ required: true, message: '请选择行业', trigger: 'change' }]
    rules.customerfiles = [{ required: true, message: '请上传客户资料', trigger: 'change' }]
  }
}
/**
 * 关闭附件弹窗
 */
const handleCancelfiles = () => {
  filesshow.value = false
  loaddata()
}
/**
 * 关闭归属
 */
const handleCancelBelong = () => {
  filesshowebelong.value = false
  loaddata()
}
/**
 * 删除
 * @param row
 */
const handleDelete = (row?: TAdCustomer) => {
  modal.confirm('是否删除?').then(() => {
    const ids = []
    if (row) {
      ids.push(row.id)
    } else {
      customerSelection.value?.forEach((color) => ids.push(color.id))
    }
    if (!ids.length) {
      modal.msgWarning('请选择删除记录')
      return
    }
    deleteCustomerByIdApi(ids.join(',')).then(({ success, msg }: IAxios) => {
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
  const to = '/template/Help?name=' + 'Customer'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
/** 显示非常用**/
const toggleSearch = () => {
  showSearch.value = !showSearch.value
  nextTick(function () {
    tableheight.value = computTableHeight()
  })
}

/** 查看客户列表信息 */
const SeeCustomerlist = () => {
  filesshow.value = true
}
/** 修改时删除客户列表信息 */
const SeeCustomerdelete = () => {
  CustomerData.value.parentName = ''
  CustomerData.value.parentid = ''
}
/** 查看归属客户信息 */
const SeeCustomerbelong = (id: string) => {
  CustomerBelongid.value = id
  detailshow.value = false
  nextTick(function () {
    filesshowebelong.value = true
  })
}
/**
 * 查询的时候选择业务人员
 * @param val
 */
const onSelectionEmp = (val: TSelectZtree[]) => {
  const employid = val.map((item) => item.id).join(',')
  queryInfo.employid = employid
  loaddata()
}
/**
 * 编辑的时候选择业务人员
 * @param val
 */
const onSelectionNewEmp = (val: TSelectZtree[]) => {
  const employname = val.map((item) => item.name).join(',')
  const employid = val.map((item) => item.id).join(',')
  CustomerData.value.employid = employid
  CustomerData.value.employname = employname
}
/**
 * 查询时查看行业
 * @param val
 */
const qSelectionindustry = (val: TSelectZtree[]) => {
  const adindustryid = val.map((item) => item.id).join(',')
  queryInfo.adindustryid = adindustryid
  loaddata()
}
/**
 * 编辑时查看行业
 * @param val
 */
const onSelectionindustry = (val: TSelectZtree[]) => {
  const adindustryid = val.map((item) => item.id).join(',')
  const adindustryname = val.map((item) => item.name).join(',')
  CustomerData.value.adindustryid = adindustryid
  CustomerData.value.adindustryname = adindustryname
}
/**
 * 编辑是查询客户来源
 */
const onSelectionadfrom = (val: TSelectZtree[]) => {
  const adfromid = val.map((item) => item.id).join(',')
  const adfromname = val.map((item) => item.name).join(',')
  CustomerData.value.adfromid = adfromid
  CustomerData.value.adfromname = adfromname
}
/**
 * 添加父id
 */
const selectiondata = (val: TAdCustomer) => {
  CustomerData.value.parentid = val.id
  CustomerData.value.parentName = val.sname
  filesshow.value = false
}

/**
 * 判断重名
 * @param data
 */
const querySearch = () => {
  // 输入框 input事件, 输入结束后0.5秒触发
  clearInterval(timmer.value)
  timmer.value = setInterval(() => {
    if (CustomerData.value.sname !== '') {
      getCustomerCombo(CustomerData.value.sname).then(({ obj, total }: IAxios<TAdCustomer[]>) => {
        restaurants.value = obj
      })
    }

    // 事件触发后,清除定时器
    clearInterval(timmer.value)
  }, 500)
}
/**
 * 清空查询列表
 */
const showsnamecl = () => {
  showsname.value = true
  restaurants.value = []
}
/**
 * 查看客户详情
 */
const seecustomerdetail = (data: string) => {
  detailshow.value = true
  filesshowebelong.value = true
  selfdetailid.value = data
}
/**
 * 关闭是清空数据
 */
const redet = () => {
  CustomerData.value = {
    id: '',
    sname: '',
    /**  创建日期  */
    dcreatetime: '',
    /** 简码（拼音）    */
    sbrevitycode: '',
    /** 编号（默认是自增列） */
    icode: 1,
    /** 客户类型（直接客户、代理公司、内容生产方）    */
    itype: 0,
    /** 地址    */
    saddress: '',
    /** 电话     */
    sphone: '',
    /** 统一社会信用代码/纳税人识别号     */
    screditcode: '',
    /** 开户行及账号     */
    sbankaccount: '',
    /** 联系人  */
    scontacts: '',
    /** 移动电话 */
    smobilephone: '',
    /** 邮政编码 */
    spostcode: '',
    /** 行业ID */
    adindustryid: '',
    /** 行业名称 */
    adindustryname: '',
    /** 归属主业务员ID */
    employid: '',
    /** 父级名称*/
    parentName: '',
    /** 归属主业务员名称 */
    employname: '',
    /** 父ID */
    parentid: '',
    /** 是否大客户 0-否 1-是 */
    bvip: false,
    /** 客户状态（活跃，不活跃）。。待定 */
    sstatus: '活跃',
    /** 审批状态 0-待审 1-在审 2-通过 3-否决  4-撤销 5-无效 */
    iapprovestatus: 2,
    /** 是否有效 0-无效 1-有效 */
    buse: true,
    /** 是否个人 0-否 1-是 */
    bindividual: false,
    /** 是否删除 0-否false 1-是true */
    bdelete: false,
    /** 序号 */
    isort: 1,
    /** 备注 */
    sremark: '',
    /** 客户归属*/
    customerbelong: [],
    /** 客户资料*/
    customerfiles: [],
    /** 并发标志 */
    version: '0',
    /** 最后操作员id */
    lastoperatorid: '',
    /** 最后操作员*/
    lastoperator: '',
    sbrand: '',
    customercategoryid: '',
    customercategoryname: '',
    customerstatusid: '',
    customerstatusname: '',
    customercreditid: '',
    customercreditname: '',
    adfromid: '',
    adfromname: ''
  }
  WcustomerFileslsit.value = []
  changebindividual()
}
/** 上传组件参数 */
const hgfileuploadparam = ref<THgFileUploadParam>({
  server: import.meta.env.VITE_APP_BASE_API + '/customer/customerfiles/upLoadCustomerFile',
  // server: import.meta.env.VITE_APP_BASE_API + '/common/UploadFile/importFile',
  multiple: true,
  filenumlimit: 20,
  storytypes: []
})
/**
 * 获取上传后返回的文件列表
 * @param res
 */
const getUpFile = (res: IAxios<TUpFile>) => {
  console.log(res.obj)
  if (res.success) {
    const workreport = res.obj as Twcustomerfiles
    WcustomerFileslsit.value.push(workreport)
    CustomerData.value.customerfiles = WcustomerFileslsit.value.map((item) => {
      return {
        id: '',
        customerid: CustomerData.value.id,
        createempid: userStore.user?.userid.toString(),
        sfileformat: item.sfileformat,
        sfileid: item.sfileid,
        sfilesize: item.sfilesize,
        soriginalfile: item.soriginalfile,
        dcreatetime: '',
        sfiletype: item.sfiletype,
        sfilecategory: '2',
        bdelete: false,
        sdescription: '',
        durl: ''
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
  CustomerData.value.customerfiles!.splice(index, 1)
  WcustomerFileslsit.value!.splice(index, 1)
}
/**
 * 预览上传的文件
 * @param file
 */
const previewFile = (file: Twcustomerfiles) => {
  console.log(file)
  if (file.sfiletype === 'Photo') {
    window.open(file.durl)
  } else if (file.sfiletype === 'Office') {
    const url = `${global?.officePreviewUrl}?src=${file.durl}`
    window.open(url)
  }
}
/**
 * 获取客户分类列表
 */
const getCustomerCategory = () => {
  getcustomerCategoryComboApi().then(({ obj, total }: IAxios<TFlowType[]>) => {
    console.log(obj)
    CustomerCategoryList.value = obj
  })
}
/**
 * 获取客户状态列表
 */
const getCustomerStatus = () => {
  getcustomerStatusComboApi().then(({ obj, total }: IAxios<TFlowType[]>) => {
    console.log(obj)
    CustomerStatusList.value = obj
  })
}
/**
 * 获取客户信誉度列表
 */
const getCustomerCredit = () => {
  getcustomerCreditComboApi().then(({ obj, total }: IAxios<TFlowType[]>) => {
    console.log(obj)
    CustomerCreditList.value = obj
  })
}
/**
 * 给客户分类赋值
 */
const getcustomercategoryname = () => {
  CustomerCategoryList.value.forEach((item) => {
    if (item.id === CustomerData.value.customercategoryid) {
      CustomerData.value.customercategoryname = item.name
    }
  })
}
/**
 * 给客户状态
 */
const getcustomerstatusname = () => {
  CustomerStatusList.value.forEach((item) => {
    if (item.id === CustomerData.value.customerstatusid) {
      CustomerData.value.customerstatusname = item.name
    }
  })
}
/**
 * 给客户信誉度
 */
const getcustomercreditname = () => {
  CustomerCreditList.value.forEach((item) => {
    if (item.id === CustomerData.value.customercreditid) {
      CustomerData.value.customercreditname = item.name
    }
  })
}
/**
 * 客户归属传值
 */
const changeWcustomerBelong = (val: Twcustomerbelong[]) => {
  console.log(val)
  CustomerData.value.customerbelong = val
}
</script>

<style scoped>
ul {
  padding-left: 10px;
}
.left_box {
  height: v-bind(treeheight);
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
</style>
