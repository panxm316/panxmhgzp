<!--
 * @Author: wanghl
 * @Date: 2024-03-07 16:08:07
 * @LastEditTime: 2024-03-11 16:13:47
 * @LastEditors: wanghl
 * @Description: 客户查询
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <span style="margin-top: 5px">开始日期：</span>
        <el-date-picker
          v-model="queryParams.startTime"
          :editable="false"
          type="date"
          placeholder="选择开始日期"
          :clearable="false"
          style="width: 130px; margin-left: 5px"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          @change="handleQuery"
        />
        <span style="margin-top: 5px">截止日期：</span>
        <el-date-picker
          v-model="queryParams.endTime"
          :editable="false"
          type="date"
          placeholder="选择截止日期"
          :clearable="false"
          style="width: 130px; margin-left: 5px"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          @change="handleQuery"
        />
        <HgZtreeSelect
          v-if="user?.blead || user?.adminflag !== 0"
          :scopeflag="EHgDeptZtreeUrl.DEPT_GETDEPTTREE"
          :selectids="queryParams.deptid ? [queryParams.deptid] : []"
          style="width: 120px"
          :filterable="true"
          clearable
          :treeparams="{ showInactiveDepts: false }"
          placeholder="请选择部门"
          @selectionztree="onSelectionDept"
        ></HgZtreeSelect>
        <HgZtreeSelect
          v-if="user?.blead || user?.adminflag !== 0"
          :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYPERSON"
          :selectids="queryParams.employid ? [queryParams.employid] : []"
          style="width: 120px"
          :filterable="true"
          clearable
          :treeparams="{ bIgnorePermissions: false }"
          placeholder="请选择业务员"
          @selectionztree="onSelectionEmp"
          @change="handleQuery"
        ></HgZtreeSelect>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </div>
    </div>
    <div class="table_box">
      <!-- 客户信息 -->
      <el-table
        :data="resultList"
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
          <template #default="customer">
            <el-button
              link
              title="客户详情"
              type="primary"
              size="small"
              icon="View"
              @click="seecustomerdetail(customer.row.id)"
              >{{ customer.row.sname }}</el-button
            ></template
          >
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

        <el-table-column prop="itype" label="客户类型" min-width="120">
          <template #default="scope">
            <span v-if="scope.row.itype == ECustomerType.直接客户"> 直接客户 </span>
            <span v-if="scope.row.itype == ECustomerType.代理公司"> 代理公司 </span>
            <span v-if="scope.row.itype == ECustomerType.内容生产方"> 内容生产方 </span>
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
        <el-table-column
          prop="sapprovalopinions"
          label="最终审核意见"
          min-width="120"
          show-overflow-tooltip
        >
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
        <el-table-column prop="customerstatusname" label="客户状态" min-width="120" align="center">
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
        <el-table-column prop="sremark" label="备注" min-width="160" show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          label="操作"
          width="320"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="customer">
            <div style="padding-left: 5px">
              <el-button
                link
                type="success"
                icon="View"
                size="small"
                @click="handleAchievementDetailQuery(customer.row)"
                >业绩详情</el-button
              >
              <el-button
                link
                type="success"
                icon="View"
                size="small"
                @click="handleCustomerAccountDetailQuery(customer.row)"
                >账户详情</el-button
              >
              <el-button
                link
                type="success"
                icon="View"
                size="small"
                @click="handleInvoiceDetailQuery(customer.row)"
                >发票详情</el-button
              >
              <el-button
                link
                type="success"
                icon="View"
                size="small"
                @click="handleworkreportsQuery(customer.row, customer.$index)"
                >服务详情</el-button
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
              <!--              <el-button-->
              <!--                link-->
              <!--                type="danger"-->
              <!--                icon="Delete"-->
              <!--                size="small"-->
              <!--                @click="handleDelete(customer.row)"-->
              <!--              >删除</el-button-->
              <!--              >-->
            </div>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="queryParams.page"
        class="pagination"
        style="margin-left: 10px; width: 100%"
        :page-sizes="pageSizes"
        :page-size="queryParams.pageSize"
        small
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="pageTotal"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <!-- 客户详情 -->
    <el-dialog
      v-model="detailshow"
      :title="'客户详情'"
      :width="fenbianlv() > 1440 ? '80%' : '94%'"
      align-center
      :destroy-on-close="true"
      @close="handleDetailCancel"
    >
      <el-row :gutter="50">
        <el-col :span="24">
          <Hg-See-Customer :selfdetailid="selfdetailid"></Hg-See-Customer>
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 客户业绩详情 -->
    <el-dialog
      v-model="achievementamountdetailshow"
      :title="'客户业绩详情'"
      :width="fenbianlv() > 1440 ? '80%' : '94%'"
      align-center
      :destroy-on-close="true"
      @close="handleAchievementCancel"
    >
      <el-row :gutter="50">
        <el-col :span="24">
          <div class="search_box">
            <div class="flex" style="width: 94%">
              <HgDateIndex :initdate="timedata" @onresponse="ontimeAchieve"></HgDateIndex>
              <el-button type="primary" icon="Search" @click="handleAchievementDetailQuery()"
                >搜索</el-button
              >
            </div>
          </div>
          <el-table
            ref="dataTableRef"
            :data="achievementList"
            show-summary
            :summary-method="getAchievementSummaries"
            highlight-current-row
            :height="tableheight - 200"
            :header-cell-style="tableHeaderColor"
            :border="true"
          >
            <el-table-column
              label="项目名称"
              prop="adprojectname"
              width="140"
              show-overflow-tooltip
            />
            <el-table-column label="合同号" prop="scontractnum" width="140" show-overflow-tooltip />
            <el-table-column label="刊期号" prop="iitemcode" width="140" show-overflow-tooltip />
            <el-table-column
              label="客户"
              prop="customername"
              min-width="160"
              show-overflow-tooltip
            />
            <el-table-column label="主业务员" prop="employname" width="100" show-overflow-tooltip />
            <el-table-column label="代理公司" prop="agencyname" width="120" show-overflow-tooltip />
            <el-table-column
              label="内容生产方"
              prop="agentname"
              width="120"
              show-overflow-tooltip
            />
            <el-table-column
              label="广告行业"
              prop="adindustryname"
              width="100"
              show-overflow-tooltip
            />
            <el-table-column label="预见报开始日期" prop="prestartdate" width="120">
              <template #default="scope">
                <span v-if="scope.row.prestartdate">{{
                  dayjs(scope.row.prestartdate).format('YYYY-MM-DD')
                }}</span>
              </template>
            </el-table-column>
            <el-table-column label="广告标题" prop="sadtitle" width="160" show-overflow-tooltip />
            <el-table-column
              label="应收"
              prop="amountreceivable"
              width="120"
              show-overflow-tooltip
              align="right"
            >
              <template #default="scope">
                <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
              </template>
            </el-table-column>
            <el-table-column
              label="已收"
              prop="amountreceived"
              width="120"
              show-overflow-tooltip
              align="right"
            >
              <template #default="scope">
                <span>{{ formatMoney(scope.row.amountreceived, '2') }}</span>
              </template>
            </el-table-column>
            <el-table-column
              label="欠款"
              prop="amountarrearage"
              width="120"
              show-overflow-tooltip
              align="right"
            >
              <template #default="scope">
                <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
              </template>
            </el-table-column>
            <!-- <el-table-column
              label="业绩金额"
              prop="amountachievement"
              width="120"
              align="right"
              show-overflow-tooltip
            >
              <template #default="scope">
                <span>{{ formatMoney(scope.row.amountachievement, '2') }}</span>
              </template>
            </el-table-column> -->
            <el-table-column
              label="关联发票号"
              prop="invoices"
              min-width="160"
              show-overflow-tooltip
            >
              <template #default="scope">
                <span
                  v-if="scope.row.invoices"
                  v-html="scope.row.invoices.replaceAll(',', '<br />')"
                ></span>
                <span v-else>无</span>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            v-model:current-page="queryAchievementParams.page"
            class="pagination"
            style="margin-left: 10px; width: 100%"
            :page-sizes="pageSizes"
            :page-size="queryAchievementParams.pageSize"
            small
            background
            layout="total, sizes, prev, pager, next, jumper"
            :total="achievementListTotal"
            @size-change="handleAchievementSizeChange"
            @current-change="handleAchievementCurrentChange"
          />
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 客户账户详情 -->
    <el-dialog
      v-model="customeraccountdetailshow"
      :title="'客户账户详情'"
      :width="fenbianlv() > 1440 ? '80%' : '94%'"
      align-center
      :destroy-on-close="true"
      @close="handleCustomerAccountCancel"
    >
      <el-row :gutter="50">
        <el-col :span="24">
          <div class="search_box">
            <div class="flex" style="width: 94%">
              <span style="margin-top: 5px">开始日期：</span>
              <el-date-picker
                v-model="queryCustParams.startTime"
                :editable="false"
                type="date"
                placeholder="选择开始日期"
                :clearable="false"
                style="width: 130px; margin-left: 5px"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
                @change="handleCustomerAccountDetailQuery"
              />
              <span style="margin-top: 5px">截止日期：</span>
              <el-date-picker
                v-model="queryCustParams.endTime"
                :editable="false"
                type="date"
                placeholder="选择截止日期"
                :clearable="false"
                style="width: 130px; margin-left: 5px"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
                @change="handleCustomerAccountDetailQuery"
              />
              <el-button type="primary" icon="Search" @click="handleCustomerAccountDetailQuery()"
                >搜索</el-button
              >
            </div>
          </div>
          <el-table
            ref="dataTableRef"
            :data="accountList"
            show-summary
            :summary-method="getAccountSummaries"
            highlight-current-row
            :height="tableheight - 200"
            :header-cell-style="tableHeaderColor"
            :border="true"
          >
            <!-- 创建者名称 -->
            <!-- String createempname -->
            <el-table-column
              label="创建者名称"
              prop="createempname"
              width="140"
              show-overflow-tooltip
            />
            <!-- 创建时间 -->
            <!-- Date dcreatetime -->
            <el-table-column prop="dcreatetime" label="创建时间" width="160" sortable="custom">
              <template #default="scope">
                <span>{{ formatDate(scope.row.dcreatetime) }}</span>
              </template>
            </el-table-column>
            <!-- 业务员名称 -->
            <!-- String employname -->
            <el-table-column
              label="业务员名称"
              prop="employname"
              width="140"
              show-overflow-tooltip
            />
            <!-- 客户名称 -->
            <!-- String customername -->
            <el-table-column
              label="客户名称"
              prop="customername"
              width="140"
              show-overflow-tooltip
            />
            <!-- 入账金额 -->
            <!-- BigDecimal namountreceived -->
            <el-table-column
              label="入账金额"
              prop="namountreceived"
              width="150"
              show-overflow-tooltip
              align="right"
            >
              <template #default="scope">
                <span>{{ formatMoney(scope.row.namountreceived, '2') }}</span>
              </template>
            </el-table-column>
            <!-- 已用金额 -->
            <!-- BigDecimal namounspent -->
            <el-table-column
              label="已用金额"
              prop="namounspent"
              width="150"
              show-overflow-tooltip
              align="right"
            >
              <template #default="scope">
                <span>{{ formatMoney(scope.row.namounspent, '2') }}</span>
              </template>
            </el-table-column>
            <!-- 剩余金额 -->
            <!-- BigDecimal namountbalance -->
            <el-table-column
              label="剩余金额"
              prop="namountbalance"
              width="150"
              show-overflow-tooltip
              align="right"
            >
              <template #default="scope">
                <span>{{ formatMoney(scope.row.namountbalance, '2') }}</span>
              </template>
            </el-table-column>
            <!-- 付款时间 -->
            <!-- Date dpaydate -->
            <el-table-column prop="dpaydate" label="付款时间" width="160" sortable="custom">
              <template #default="scope">
                <span>{{ formatDate(scope.row.dpaydate) }}</span>
              </template>
            </el-table-column>
            <!-- 收款类型 -->
            <!-- String stype -->
            <el-table-column label="付款方式" prop="stype" width="140" show-overflow-tooltip />
            <!-- 是否指定订单 -->
            <!-- Boolean bassignorder -->
            <el-table-column prop="bassignorder" label="是否指定订单" width="120" align="center">
              <template #default="scope">
                <span v-if="scope.row.bassignorder == false"> 否 </span>
                <span v-if="scope.row.bassignorder == true"> 是 </span>
              </template>
            </el-table-column>
            <!-- 指定订单合同号 -->
            <!-- String scontractnum -->
            <el-table-column
              label="指定订单合同号"
              prop="scontractnum"
              width="140"
              show-overflow-tooltip
            />
            <!-- 描述 -->
            <!-- String sdescription -->
            <el-table-column label="描述" prop="sdescription" width="140" show-overflow-tooltip />
            <!-- 付款方式 -->
            <!-- String paymethodname -->
            <el-table-column
              label="付款方式"
              prop="paymethodname"
              width="140"
              show-overflow-tooltip
            />
            <!-- 发票号 -->
            <!-- String invoice -->
            <el-table-column label="发票号" prop="invoice" width="140" show-overflow-tooltip />
            <!-- 备注 -->
            <!-- String sremark -->
            <el-table-column label="备注" prop="sremark" width="140" show-overflow-tooltip />
            <!-- 状态(0-待提交 1-已提交 2-已确认 3-已退回 4-已核销 ） -->
            <!-- Integer istatus -->
            <el-table-column prop="istatus" label="状态" sortable="custom" min-width="120">
              <template #default="scope">
                {{ statusCombo.find((item) => item.id === scope.row.istatus)?.name }}
              </template>
            </el-table-column>
            <!-- 最后修改日期 -->
            <!-- Date dlastmodifydate -->
            <el-table-column
              prop="dlastmodifydate"
              label="最后修改日期"
              width="160"
              sortable="custom"
            >
              <template #default="scope">
                <span>{{ formatDate(scope.row.dlastmodifydate) }}</span>
              </template>
            </el-table-column>
            <!-- 最后操作员 -->
            <!-- String lastoperator -->
            <el-table-column
              label="最后操作员"
              prop="lastoperator"
              width="140"
              show-overflow-tooltip
            />
          </el-table>
          <el-pagination
            v-model:current-page="queryCustParams.page"
            class="pagination"
            style="margin-left: 10px; width: 100%"
            :page-sizes="pageSizes"
            :page-size="queryCustParams.pageSize"
            small
            background
            layout="total, sizes, prev, pager, next, jumper"
            :total="achievementListTotal"
            @size-change="handleCustSizeChange"
            @current-change="handleCustCurrentChange"
          />
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 发票详情 -->
    <el-dialog
      v-model="invoicedetailshow"
      :title="'发票详情'"
      :width="fenbianlv() > 1440 ? '80%' : '94%'"
      align-center
      :destroy-on-close="true"
      @close="handleInvoiceCancel"
    >
      <el-row :gutter="50">
        <el-col :span="24">
          <div class="search_box">
            <div class="flex" style="width: 94%">
              <span style="margin-top: 5px">开始日期：</span>
              <el-date-picker
                v-model="queryInvoiceParams.startTime"
                :editable="false"
                type="date"
                placeholder="选择开始日期"
                :clearable="false"
                style="width: 130px; margin-left: 5px"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
                @change="handleInvoiceDetailQuery"
              />
              <span style="margin-top: 5px">截止日期：</span>
              <el-date-picker
                v-model="queryInvoiceParams.endTime"
                :editable="false"
                type="date"
                placeholder="选择截止日期"
                :clearable="false"
                style="width: 130px; margin-left: 5px"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
                @change="handleInvoiceDetailQuery"
              />
              <el-button type="primary" icon="Search" @click="handleInvoiceDetailQuery()"
                >搜索</el-button
              >
            </div>
          </div>
          <el-table
            ref="dataTableRef"
            :data="invoiceList"
            show-summary
            :summary-method="getSummaries"
            highlight-current-row
            :height="tableheight - 200"
            :header-cell-style="tableHeaderColor"
            :border="true"
          >
            <el-table-column
              label="客户"
              prop="customername"
              min-width="120"
              show-overflow-tooltip
            />
            <el-table-column label="客户属性" prop="icusttype" width="100" show-overflow-tooltip>
              <template #default="scope">
                <span v-if="scope.row.icusttype == ECustomerType.直接客户"> 直接客户 </span>
                <span v-if="scope.row.icusttype == ECustomerType.代理公司"> 代理公司 </span>
                <span v-if="scope.row.icusttype == ECustomerType.内容生产方"> 内容生产方 </span>
              </template>
            </el-table-column>
            <el-table-column label="业务员" prop="employname" width="100" show-overflow-tooltip />
            <el-table-column label="发票金额" prop="namount" width="150" show-overflow-tooltip>
              <template #default="scope">
                <span>{{ formatMoney(scope.row.namount, '2') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="申请日期" prop="dcreatetime" width="100">
              <template #default="scope">
                <span>{{ dayjs(scope.row.dcreatetime).format('YYYY-MM-DD') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="发票类型" prop="itype" width="100" :align="'center'">
              <template #default="scope">
                <span v-if="scope.row.itype == EPreinvoiceApplyType.手开"> 手开 </span>
                <span v-if="scope.row.itype == EPreinvoiceApplyType.预开"> 预开 </span>
                <span v-if="scope.row.itype == EPreinvoiceApplyType.挂开"> 挂开 </span>
                <span v-if="scope.row.itype == EPreinvoiceApplyType.冲红"> 冲红 </span>
                <span v-if="scope.row.itype == EPreinvoiceApplyType.预收费"> 预收费 </span>
                <span v-if="scope.row.itype == EPreinvoiceApplyType.预开完成"> 预开完成 </span>
              </template>
            </el-table-column>
            <el-table-column label="发票样式" prop="istype" width="100" :align="'center'">
              <template #default="scope">
                <span v-if="scope.row.istype == EPreinvoiceStyle.专用发票"> 专用发票 </span>
                <span v-if="scope.row.istype == EPreinvoiceStyle.普通发票"> 普通发票 </span>
                <span v-if="scope.row.istype == EPreinvoiceStyle.电子发票"> 电子发票 </span>
                <span v-if="scope.row.istype == EPreinvoiceStyle.数电专票"> 数电专票 </span>
                <span v-if="scope.row.istype == EPreinvoiceStyle.数电普票"> 数电普票 </span>
              </template>
            </el-table-column>
            <el-table-column label="发票状态" prop="istatus" width="100" :align="'center'">
              <template #default="scope">
                <span v-if="scope.row.istatus == EInvoiceStatus.有效">有效</span>
                <span v-if="scope.row.istatus == EInvoiceStatus.无效">无效</span>
                <span v-if="scope.row.istatus == EInvoiceStatus.冲红退回">冲红退回</span>
              </template>
            </el-table-column>
            <el-table-column
              label="付款方式"
              prop="paymethodname"
              width="120"
              show-overflow-tooltip
            />
            <el-table-column label="开票项目" prop="printitemname" width="100" />
            <el-table-column
              label="税收编码"
              prop="staxcode"
              min-width="180"
              show-overflow-tooltip
            />
            <el-table-column label="发票号" prop="invoice" width="140" show-overflow-tooltip />
            <el-table-column
              label="发票编码"
              prop="invoicecode"
              width="120"
              show-overflow-tooltip
            />
            <el-table-column label="收款人" prop="scashier" width="120" show-overflow-tooltip />
            <el-table-column label="复核人" prop="schecker" width="120" show-overflow-tooltip />
          </el-table>
          <el-pagination
            v-model:current-page="queryAchievementParams.page"
            class="pagination"
            style="margin-left: 10px; width: 100%"
            :page-sizes="pageSizes"
            :page-size="queryAchievementParams.pageSize"
            small
            background
            layout="total, sizes, prev, pager, next, jumper"
            :total="invoiceTotal"
            @size-change="handleInvoiceSizeChange"
            @current-change="handleInvoiceCurrentChange"
          />
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 客户工作报告组件 -->
    <CustomerWorkreports ref="customerworkreportsref" />
  </div>
</template>

<script setup lang="ts">
import { computTableHeight, formatDate, formatMoney, tableHeaderColor, fenbianlv } from '@/utils'
import { dayjs } from 'element-plus'
import {
  ECustomerType,
  EHgDeptZtreeUrl,
  EInvoiceStatus,
  EPreinvoiceApplyType,
  EPreinvoiceStyle,
  EStatus
} from '@/types/common/enumindex'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { IOrderAndItemDTO, TOrderAndItemVO } from '@/types/views/business/orderitemcost'
import { TSelectZtree } from '@/types/common'
import {
  getCustomerChargeByCustomeridApi,
  getInvoiceByCustomeridApi,
  getMyAchievementCountApi,
  getMyCustomerChargeCountApi,
  getMyCustomerPageListApi,
  getOrderItemListByCustomerIdApi
} from '@/api/personal/PersonalStat'
import { IAxios } from 'axios'
import { TAdCustomer, TCustomerBelongVo } from '@/types/views/customer'
import useUserStore from '@/store/modules/user'
import { IInvoiceDTO, TInvoiceQuery } from '@/types/views/finance/invoice'
import { Twcustomercharge } from '@/types/views/business/bankaccountallocate'
import { getEnumCombo } from '@/api/common'
import { IDataCombo } from '@/types/common/DataCombo'
import { Tworderitem } from '@/types/views/ad/adtworder'
import CustomerWorkreports from './CustomerWorkreports.vue'
/** 刊发状态列表 */
const statusCombo: IDataCombo[] = getEnumCombo(EStatus)

const global = getCurrentInstance()?.appContext.config.globalProperties
const user = useUserStore().user
/** 表格高度 */
const tableheight = ref(0)
/** 客户详情显示隐藏 */
const detailshow = ref(false)
/** 账户详情显示隐藏 */
const achievementamountdetailshow = ref(false)
/** 客户账户详情显示隐藏 */
const customeraccountdetailshow = ref(false)
/** 发票详情显示隐藏 */
const invoicedetailshow = ref(false)
/** 工作报告显示隐藏 */
const customerworkreportsref = ref()
/** 修改时将自己的id传给客户选择组件 */
const selfdetailid = ref<string | undefined>('')
/** 查询总页数 */
const pageTotal = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([15, 20, 30, 40])
/** 选择的客户 */
const customerSelection = ref<TAdCustomer[]>()
/** 查询时间 */
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
/**
 * 业绩汇总
 */
const sumResult = ref<Tworderitem>()
/**
 * 客户账户汇总
 */
const sumAccountResult = ref<Twcustomercharge>()
/** 查询参数 */
const queryParams = reactive<TCustomerBelongVo>({
  sort: 'dcreatetime',
  order: 'desc',
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  // 是否只查询当前人员（0：否 1：是，如果是则表示只查询当前人员）
  bcurflag: ''
})
const initAchieveParams = {
  sort: 'startTime',
  order: 'desc',
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  // 是否只查询当前人员（0：否 1：是，如果是则表示只查询当前人员）
  bcurflag: undefined
}
const initInvoiceParams = {
  sort: 'dcreatetime',
  order: 'desc',
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  // 是否只查询当前人员（0：否 1：是，如果是则表示只查询当前人员）
  bcurflag: undefined
}
/** 查询业绩参数 */
const queryAchievementParams = reactive<TOrderAndItemVO>({
  ...initInvoiceParams
})
/** 客户账户详情参数 */
const queryCustParams = reactive<TOrderAndItemVO>({
  ...initInvoiceParams
})
/** 查询发票参数 */
const queryInvoiceParams = reactive<TInvoiceQuery>({
  ...initInvoiceParams
})
/**
 * 查询客户结果列表对象
 */
const resultList = ref<TAdCustomer[]>([])
/** 客户业绩列表对象 */
const achievementList = ref<IOrderAndItemDTO[]>([])
/** 客户账户详情列表对象 */
const accountList = ref<Twcustomercharge[]>([])
/** 发票详情列表对象 */
const invoiceList = ref<IInvoiceDTO[]>([])
/** 客户业绩列表数 */
const achievementListTotal = ref(0)
/** 客户账户列表数 */
const accountListTotal = ref(0)
/** 客户发票数 */
const invoiceTotal = ref(0)

onMounted(() => {
  if (user?.blead || user?.adminflag !== 0) {
    queryParams.bcurflag = 'false'
    queryCustParams.bcurflag = false
    queryAchievementParams.bcurflag = false
  } else {
    queryParams.bcurflag = 'true'
  }
  handleQuery()
  nextTick(() => {
    /**
     * 计算表格高度 - 50为页面上方导航条Navbar的高度
     */
    tableheight.value = computTableHeight()
  })
})
/**
 * 发票合计
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
    if (column.property !== 'namount') {
      sums[index] = ''
      return
    }
    const values = data.map((item: any) => Number(item[column.property]))
    if (!values.every((value: any) => Number.isNaN(value))) {
      sums[index] =
        '￥ ' +
        formatMoney(
          `${values.reduce((prev: any, curr: any) => {
            const value = Number(curr)
            if (!Number.isNaN(value)) {
              return prev + curr
            } else {
              return prev
            }
          }, 0)}`,
          '2'
        )
    } else {
      sums[index] = ''
    }
  })

  return sums
}
/**
 * @description: 表格合计
 */
import type { TableColumnCtx } from 'element-plus'
interface SummaryMethodProps<T = IOrderAndItemDTO> {
  columns: TableColumnCtx<T>[]
  data: T[]
}
/**
 * 业绩数据合计
 * @param param
 */
const getAchievementSummaries = (param: SummaryMethodProps) => {
  const { columns, data } = param
  const sums: string[] = []
  columns.forEach((column, index) => {
    if (index === 0) {
      sums[index] = '合计'
      return
    }
    if (column.property === 'amountreceivable') {
      sums[index] = sumResult.value?.amountreceivable
        ? '￥ ' + formatMoney(sumResult.value?.amountreceivable, '2')
        : '￥ ' + '0'
      return
    }
    if (column.property === 'amountreceived') {
      sums[index] = sumResult.value?.amountreceived
        ? '￥ ' + formatMoney(sumResult.value?.amountreceived, '2')
        : '￥ ' + '0'
      return
    }
    if (column.property === 'amountarrearage') {
      sums[index] = sumResult.value?.amountarrearage
        ? '￥ ' + formatMoney(sumResult.value?.amountarrearage, '2')
        : '￥ ' + '0'
      return
    }
    // if (column.property === 'amountachievement') {
    //   sums[index] = sumResult.value?.amountachievement
    //     ? '￥ ' + formatMoney(sumResult.value?.amountachievement, '2')
    //     : '￥ ' + '0'
    //   return
    // }
    if (column.property === 'namountcost') {
      sums[index] = sumResult.value?.namountcost
        ? '￥ ' + formatMoney(sumResult.value?.namountcost, '2')
        : '￥ ' + '0'
      return
    }
  })
  /**
   * 遍历数组保留两位小数
   */
  sums.forEach(function (element) {
    Number(element).toFixed(2)
  })
  return sums
}
/**
 * 客户账户数据合计
 * @param param
 */
const getAccountSummaries = (param: SummaryMethodProps) => {
  const { columns, data } = param
  const sums: string[] = []
  columns.forEach((column, index) => {
    if (index === 0) {
      sums[index] = '合计'
      return
    }
    if (column.property === 'namountreceived') {
      sums[index] =
        '￥ ' +
        (sumAccountResult.value?.namountreceived
          ? formatMoney(sumAccountResult.value?.namountreceived, '2')
          : '0')
      return
    }
    if (column.property === 'namounspent') {
      sums[index] =
        '￥ ' +
        (sumAccountResult.value?.namounspent
          ? formatMoney(sumAccountResult.value?.namounspent, '2')
          : '0')
      return
    }
    if (column.property === 'namountbalance') {
      sums[index] =
        '￥ ' +
        (sumAccountResult.value?.namountbalance
          ? formatMoney(sumAccountResult.value?.namountbalance, '2')
          : '0')
      return
    }
  })
  /**
   * 遍历数组保留两位小数
   */
  sums.forEach(function (element) {
    Number(element).toFixed(2)
  })
  return sums
}
/**
 * 排序
 * @param val
 */
const handleSortChange = (val: { prop: string; order: string }) => {
  queryParams.sort = val.prop

  if (val.order === 'ascending') {
    queryParams.order = 'asc'
  } else if (val.order === 'descending') {
    queryParams.order = 'desc'
  } else {
    queryParams.order = ''
  }
  handleQuery()
}

/**
 * @description: 页码总数变更
 * @param {*} val
 * @return {*}
 */
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  handleQuery()
}
/**
 * @description: 页码总数变更 - 客户业绩详情
 * @param {*} val
 * @return {*}
 */
const handleAchievementSizeChange = (val: number) => {
  queryAchievementParams.pageSize = val
  handleAchievementDetailQuery()
}
/**
 * @description: 页码总数变更 - 客户账户详情
 * @param {*} val
 * @return {*}
 */
const handleCustSizeChange = (val: number) => {
  queryCustParams.pageSize = val
  handleCustomerAccountDetailQuery()
}
/**
 * @description: 页码总数变更 - 发票详情
 * @param {*} val
 * @return {*}
 */
const handleInvoiceSizeChange = (val: number) => {
  queryInvoiceParams.pageSize = val
  handleInvoiceDetailQuery()
}

/**
 * @description: 页码变更
 * @param {*} val
 * @return {*}
 */
const handleCurrentChange = (val: number) => {
  queryParams.page = val
  handleQuery()
}
/**
 * @description: 页码变更 - 客户业绩详情
 * @param {*} val
 * @return {*}
 */
const handleAchievementCurrentChange = (val: number) => {
  queryAchievementParams.page = val
  handleAchievementDetailQuery()
}
/**
 * @description: 页码变更 - 客户业绩详情
 * @param {*} val
 * @return {*}
 */
const handleCustCurrentChange = (val: number) => {
  queryCustParams.page = val
  handleCustomerAccountDetailQuery()
}
/**
 * @description: 页码变更 - 发票详情
 * @param {*} val
 * @return {*}
 */
const handleInvoiceCurrentChange = (val: number) => {
  queryInvoiceParams.page = val
  handleInvoiceDetailQuery()
}

/**
 * 选择
 * @param rows
 */
const handleSelectionChange = (rows: TAdCustomer[]) => {
  customerSelection.value = rows
}

/**
 * 查询
 */
const handleQuery = () => {
  getMyCustomerPageListApi(queryParams)
    .then((res: IAxios<TAdCustomer[]>) => {
      if (res.success) {
        resultList.value = res.obj
        pageTotal.value = res.total
      }
    })
    .finally(() => {})
}
/**
 * 查看客户详情
 */
const seecustomerdetail = (data: string) => {
  detailshow.value = true
  selfdetailid.value = data
}
/**
 * 查看客户业绩详情
 * @param row
 */
const handleAchievementDetailQuery = (row?: TAdCustomer) => {
  if (row?.id) {
    queryAchievementParams.customerid = row.id
  }
  console.log('customerid', queryAchievementParams.customerid)
  achievementamountdetailshow.value = true
  nextTick(() => {
    getOrderItemListByCustomerIdApi(queryAchievementParams)
      .then((res: IAxios) => {
        if (res.success) {
          achievementList.value = res.obj
          achievementListTotal.value = res.total
        }
      })
      .finally(() => {})
    getMyAchievementCountApi({
      customerid: queryAchievementParams.customerid,
      startTime: queryAchievementParams.startTime,
      endTime: queryAchievementParams.endTime
    })
      .then((res: IAxios) => {
        if (res.success) {
          sumResult.value = res.obj
        }
      })
      .finally(() => {})
  })
}
/**
 * 查看客户账户详情
 * @param row
 */
const handleCustomerAccountDetailQuery = (row?: TAdCustomer) => {
  if (row?.id) {
    queryCustParams.customerid = row.id
  }
  customeraccountdetailshow.value = true
  nextTick(() => {
    getCustomerChargeByCustomeridApi(queryCustParams)
      .then((res: IAxios) => {
        if (res.success) {
          accountList.value = res.obj
          accountListTotal.value = res.total
        }
      })
      .finally(() => {})
    getMyCustomerChargeCountApi(queryCustParams)
      .then((res: IAxios) => {
        if (res.success) {
          sumAccountResult.value = res.obj
        }
      })
      .finally(() => {})
  })
}
/**
 * 查看发票详情
 * @param row
 */
const handleInvoiceDetailQuery = (row?: TAdCustomer) => {
  if (row?.id) {
    queryInvoiceParams.customerid = row.id
  }
  invoicedetailshow.value = true
  nextTick(() => {
    getInvoiceByCustomeridApi(queryInvoiceParams)
      .then((res: IAxios) => {
        if (res.success) {
          invoiceList.value = res.obj
          invoiceTotal.value = res.total
        }
      })
      .finally(() => {})
  })
}

/**
 * 关闭客户详情
 */
const handleDetailCancel = () => {
  detailshow.value = false
}
/**
 * 关闭客户业绩详情
 */
const handleAchievementCancel = () => {
  achievementamountdetailshow.value = false
  const custid = queryAchievementParams.customerid
  Object.assign(queryAchievementParams, initAchieveParams)
  queryAchievementParams.customerid = custid
  queryAchievementParams.page = 1
}
/**
 * 关闭客户账户详情
 */
const handleCustomerAccountCancel = () => {
  customeraccountdetailshow.value = false
  const custid = queryCustParams.customerid
  Object.assign(queryCustParams, initInvoiceParams)
  queryCustParams.customerid = custid
  queryCustParams.page = 1
  sumAccountResult.value = undefined
}
/**
 * 关闭发票详情
 */
const handleInvoiceCancel = () => {
  invoicedetailshow.value = false
  const custid = queryInvoiceParams.customerid
  Object.assign(queryInvoiceParams, initInvoiceParams)
  queryInvoiceParams.customerid = custid
  queryInvoiceParams.page = 1
}
/**
 * 选择部门
 * @param val
 */
const onSelectionDept = (val: TSelectZtree[]) => {
  const deptid = val.map((item) => item.id).join(',')
  queryParams.deptid = deptid
}
/**
 * 选择人员
 * @param val
 */
const onSelectionEmp = (val: TSelectZtree[]) => {
  const employid = val.map((item) => item.id).join(',')
  queryParams.employid = employid
}
/**
 * 时间选择
 * @param val
 */
const ontime = (val: TDateTimeType) => {
  let timev = false
  if (queryParams.startTime) {
    timev = true
  }
  queryParams.startTime = val.startTime
  queryParams.endTime = val.endTime
  if (timev) {
    handleQuery()
  }
}
/**
 * 时间选择
 * @param val
 */
const ontimeAchieve = (val: TDateTimeType) => {
  let timev = false
  if (queryAchievementParams.startTime) {
    timev = true
  }
  queryAchievementParams.startTime = val.startTime
  queryAchievementParams.endTime = val.endTime
  if (timev) {
    handleAchievementDetailQuery()
  }
}
/**
 * 工作报告详情查看
 */
const handleworkreportsQuery = (row?: any, number?: number) => {
  customerworkreportsref.value.view(row.id)
}
</script>

<style scoped lang="scss"></style>
