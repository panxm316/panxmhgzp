<!--
 * @Author: yanz
 * @Date: 2024-01-16 17:07:57
 * @LastEditors: yanz
 * @LastEditTime: 2024-01-23 11:12:26
 * @Description:数据轧账
 *
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button type="primary" icon="Plus" @click="dialogAddVisible = true">新增</el-button>
        <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
        <el-input
          v-model="queryParams.queryKey"
          placeholder="请输入名称"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
          @clear="handleQuery"
        />
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="dataTableRef"
        :data="backupGroupList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        :border="true"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          label="创建者名称"
          prop="createempname"
          width="120"
          show-overflow-tooltip
        />
        <el-table-column label="创建日期" prop="dcreatetime" width="120">
          <template #default="scope">
            <span v-if="scope.row.dcreatetime">{{
              dayjs(scope.row.dcreatetime).format('YYYY-MM-DD')
            }}</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column label="名称" prop="databackupname" width="120" show-overflow-tooltip />
        <el-table-column label="汇总类型" prop="datatype" width="100" align="center">
          <template #default="scope">
            {{ databackupGroupDataTypeCombo.find((item) => item.id === scope.row.datatype)?.name }}
          </template>
        </el-table-column>
        <el-table-column label="开始日期" prop="dstartdate" width="120">
          <template #default="scope">
            <span v-if="scope.row.dstartdate">{{
              dayjs(scope.row.dstartdate).format('YYYY-MM-DD')
            }}</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column label="结束日期" prop="denddate" width="120">
          <template #default="scope">
            <span v-if="scope.row.denddate">{{
              dayjs(scope.row.denddate).format('YYYY-MM-DD')
            }}</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column label="记录数" prop="nrecordsize" min-width="160" show-overflow-tooltip />
        <el-table-column label="备注" prop="sdescription" min-width="160" show-overflow-tooltip />
        <el-table-column
          label="操作"
          align="center"
          width="220"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              link
              title="详情"
              type="primary"
              size="small"
              icon="View"
              style="margin: 0 5px"
              @click="onDetail(scope.row)"
              >详情</el-button
            >
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
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
  <el-dialog
    v-model="dialogDetailVisible"
    title="详情展示"
    width="1500"
    align-center
    destroy-on-close
    :close-on-click-modal="false"
    append-to-body
    draggable
    @opened="handleDetailQuery"
    @close="onCancelDetail"
  >
    <el-row :gutter="50">
      <el-col :span="24">
        <div class="search_box">
          <div class="flex" style="width: 94%">
            <HgDateIndex :initdate="timeDetaildata" @onresponse="onDetailtime"></HgDateIndex>
            <HgZtreeSelect
              :scopeflag="EHgDeptZtreeUrl.DEPT_GETDEPTTREE"
              :selectids="queryDetailParams.deptid ? [queryDetailParams.deptid] : []"
              style="width: 120px"
              :filterable="true"
              clearable
              :treeparams="{ bIgnorePermissions: false }"
              placeholder="请选择部门"
              @selectionztree="onSelectionDept"
            ></HgZtreeSelect>
            <HgZtreeSelect
              :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYPERSON"
              :selectids="queryDetailParams.employid ? [queryDetailParams.employid] : []"
              style="width: 120px"
              :filterable="true"
              clearable
              :treeparams="{ bIgnorePermissions: false }"
              placeholder="请选择业务员"
              @selectionztree="onSelectionEmp"
              @change="handleDetailQuery"
            ></HgZtreeSelect>
            <el-input
              v-model="queryDetailParams.queryKey"
              clearable
              placeholder="请选择客户"
              style="width: 150px"
              class="inputwindht"
            >
              <template #append>
                <el-button
                  icon="Search"
                  circle
                  title="选择客户"
                  @click="dialogCustomerVisible = true"
                />
              </template>
              <template #suffix>
                <el-button
                  v-show="queryDetailParams.queryKey"
                  link
                  icon="CircleClose"
                  @click="SeeCustomerdelete()"
                ></el-button>
              </template>
            </el-input>
            <!-- 选择客户 -->
            <el-dialog
              v-model="dialogCustomerVisible"
              title="选择客户"
              width="1000"
              align-center
              :close-on-click-modal="false"
              :destroy-on-close="true"
            >
              <el-row :gutter="50">
                <el-col :span="24">
                  <HgSalesCustomer @selectiondata="selectCustomer"></HgSalesCustomer>
                </el-col>
              </el-row>
            </el-dialog>
            <el-select
              v-model="queryDetailParams.mediaid"
              placeholder="请选择媒体"
              clearable
              style="width: 160px"
              @change="handleDetailQuery"
              @close="handleDetailQuery"
            >
              <el-option
                v-for="item in mediaCombo"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
            <el-button type="primary" icon="Search" @click="handleDetailQuery">搜索</el-button>
          </div>
        </div>
        <div class="table_box">
          <el-row>
            <el-table
              :data="backupDetailList"
              row-key="id"
              :height="tableheight"
              :border="true"
              stripe
              :header-cell-style="tableHeaderColor"
              @selection-change="handleDetailSelectionChange"
            >
              <el-table-column
                prop="sordernum"
                label="订单号"
                sortable="custom"
                min-width="160"
                show-overflow-tooltip
              >
              </el-table-column>
              <el-table-column
                prop="scontractnum"
                label="合同编号"
                sortable="custom"
                min-width="120"
              >
              </el-table-column>
              <el-table-column
                prop="adprojectname"
                label="广告项目名称"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <el-table-column
                prop="ordercreateempname"
                label="订单创建者名称"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <el-table-column label="订单创建日期" prop="ordercreatetime" width="120">
                <template #default="scope">
                  <span v-if="scope.row.ordercreatetime">{{
                    dayjs(scope.row.ordercreatetime).format('YYYY-MM-DD')
                  }}</span>
                  <span v-else>--</span>
                </template>
              </el-table-column>
              <el-table-column label="订单录入方式" prop="datatype" width="100" align="center">
                <template #default="scope">
                  {{ orderibooktypeCombo.find((item) => item.id === scope.row.datatype)?.name }}
                </template>
              </el-table-column>
              <el-table-column
                prop="businessentityname"
                label="经营主体"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- customername 客户名称 -->
              <el-table-column
                prop="customername"
                label="客户名称"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- scontacts	联系人 -->
              <el-table-column
                prop="scontacts"
                label="联系人"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- saddress	地址 -->
              <el-table-column
                prop="saddress"
                label="地址"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- smobilephone	联系人电话 -->
              <el-table-column
                prop="smobilephone"
                label="联系人电话"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- spostcode	邮编 -->
              <el-table-column
                prop="spostcode"
                label="邮编"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- deptname	部门名称 -->
              <el-table-column
                prop="deptname"
                label="部门名称"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- orderkindCombo -->
              <el-table-column label="订单类别" prop="iorderkind" width="100" align="center">
                <template #default="scope">
                  {{ orderkindCombo.find((item) => item.id === scope.row.iorderkind)?.name }}
                </template>
              </el-table-column>
              <!-- employname	主业务员名称 -->
              <el-table-column
                prop="employname"
                label="主业务员名称"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- agencyname	代理公司名称 -->
              <el-table-column
                prop="agencyname"
                label="代理公司名称"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- agencyemployname	代理公司业务员名称 -->
              <el-table-column
                prop="agencyemployname"
                label="代理公司业务员名称"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- agentname	内容生产方 -->
              <el-table-column
                prop="agentname"
                label="内容生产方"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- agentemployname	内容生产方业务员名称 -->
              <el-table-column
                prop="agentemployname"
                label="内容生产方业务员名称"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- adindustryname	行业名称 -->
              <el-table-column
                prop="adindustryname"
                label="行业名称"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- adtypename	广告类型名称 -->
              <el-table-column
                prop="adtypename"
                label="广告类型名称"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- sorderadtitle	广告标题 -->
              <el-table-column
                prop="sorderadtitle"
                label="广告标题"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- sorderadcontent	广告内容 -->
              <el-table-column
                prop="sorderadcontent"
                label="广告内容"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- borderuse	是否有效 -->
              <el-table-column prop="borderuse" label="是否有效" sortable="custom" min-width="120"
                ><template #default="scope">
                  <el-checkbox v-model="scope.row.borderuse" disabled></el-checkbox> </template
              ></el-table-column>
              <!-- borderdelete	是否删除 -->
              <el-table-column
                prop="borderdelete"
                label="是否删除"
                sortable="custom"
                min-width="120"
                ><template #default="scope">
                  <el-checkbox v-model="scope.row.borderdelete" disabled></el-checkbox> </template
              ></el-table-column>
              <!-- sopinion	负责人意见 -->
              <el-table-column
                prop="sopinion"
                label="负责人意见"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- iorderapprovestatus	审核状态(0-待审、1-在审、2-通过、3-否决、4-撤销、5-无效） -->
              <el-table-column
                prop="iorderapprovestatus"
                label="审核状态"
                sortable="custom"
                min-width="120"
                ><template #default="scope">
                  {{
                    approveStatusCombo.find((item) => item.id === scope.row.iorderapprovestatus)
                      ?.name
                  }}
                </template></el-table-column
              >
              <!-- bspecial	是否特殊订单(0 否 1-是) -->
              <el-table-column
                prop="bspecial"
                label="是否特殊订单"
                sortable="custom"
                min-width="120"
                ><template #default="scope">
                  <el-checkbox v-model="scope.row.bspecial" disabled></el-checkbox> </template
              ></el-table-column>
              <!-- sspecialreason	特殊原因 -->
              <el-table-column
                prop="sspecialreason"
                label="特殊原因"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- createempname	创建者名称 -->
              <el-table-column
                prop="createempname"
                label="创建者名称"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- createtime	创建日期 -->
              <el-table-column label="创建日期" prop="createtime" width="120">
                <template #default="scope">
                  <span v-if="scope.row.createtime">{{
                    dayjs(scope.row.createtime).format('YYYY-MM-DD')
                  }}</span>
                  <span v-else>--</span>
                </template>
              </el-table-column>
              <!-- ibooktype	录入方式 0-正常 1-广告预约 2-快速预约 3-补刊 -->
              <el-table-column prop="ibooktype" label="录入方式" sortable="custom" min-width="120"
                ><template #default="scope">
                  {{ orderibooktypeCombo.find((item) => item.id === scope.row.ibooktype)?.name }}
                </template></el-table-column
              >
              <!-- mediatypename	媒体类型名称 -->
              <el-table-column
                prop="mediatypename"
                label="媒体类型名称"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- medianame	媒体名称 -->
              <el-table-column
                prop="medianame"
                label="媒体名称"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- prestartdate	预见报开始日期 -->
              <el-table-column label="预见报开始日期" prop="prestartdate" width="120">
                <template #default="scope">
                  <span v-if="scope.row.prestartdate">{{
                    dayjs(scope.row.prestartdate).format('YYYY-MM-DD')
                  }}</span>
                  <span v-else>--</span>
                </template>
              </el-table-column>
              <!-- preenddate	预见报结束日期 -->
              <el-table-column label="预见报结束日期" prop="preenddate" width="120">
                <template #default="scope">
                  <span v-if="scope.row.preenddate">{{
                    dayjs(scope.row.preenddate).format('YYYY-MM-DD')
                  }}</span>
                  <span v-else>--</span>
                </template>
              </el-table-column>
              <!-- foldname	叠次名称 -->
              <el-table-column
                prop="foldname"
                label="叠次名称"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- foldareavername	叠次版本名称 -->
              <el-table-column
                prop="foldareavername"
                label="叠次版本名称"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- addisplayformname	广告形式名称 -->
              <el-table-column
                prop="addisplayformname"
                label="广告形式名称"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- pagesortname	版面类别名称 -->
              <el-table-column
                prop="pagesortname"
                label="版面类别名称"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- adcolorname	色彩名称 -->
              <el-table-column
                prop="adcolorname"
                label="色彩名称"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- adspecname	规格名称 -->
              <el-table-column
                prop="adspecname"
                label="规格名称"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- nheight	高 -->
              <el-table-column
                prop="nheight"
                label="高"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- weeksettingname	星期名称 -->
              <el-table-column
                prop="weeksettingname"
                label="星期名称"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- sadtitle	广告标题 -->
              <el-table-column
                prop="sadtitle"
                label="广告标题"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- sadcontent	广告内容 -->
              <el-table-column
                prop="sadcontent"
                label="广告内容"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- foldpageplanname	版面名称 -->
              <el-table-column
                prop="foldpageplanname"
                label="版面名称"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- baseprice	刊例价 -->
              <el-table-column
                prop="baseprice"
                label="刊例价"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- normalprice	标准价格 -->
              <el-table-column
                prop="normalprice"
                label="标准价格"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- amountreceivable	应收金额 -->
              <el-table-column label="应收金额" prop="amountreceivable" align="right" width="120">
                <template #default="scope">
                  <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
                </template>
              </el-table-column>
              <!-- amountreceived	已收金额 -->
              <el-table-column label="已收金额" prop="amountreceived" align="right" width="120">
                <template #default="scope">
                  <span>{{ formatMoney(scope.row.amountreceived, '2') }}</span>
                </template>
              </el-table-column>
              <!-- amountarrearage	欠款金额 -->
              <el-table-column label="欠款金额" prop="amountarrearage" align="right" width="120">
                <template #default="scope">
                  <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
                </template>
              </el-table-column>
              <!-- ndiscountrate	折扣率 -->
              <el-table-column label="折扣率" prop="ndiscountrate" align="center" width="100">
                <template #default="scope">
                  <span>{{ scope.row.ndiscountrate }}%</span>
                </template>
              </el-table-column>
              <!-- amountachievement	业绩金额 -->
              <el-table-column label="业绩金额" prop="amountachievement" align="right" width="120">
                <template #default="scope">
                  <span>{{ formatMoney(scope.row.amountachievement, '2') }}</span>
                </template>
              </el-table-column>
              <!-- dachievementdate	业绩时间 -->
              <el-table-column label="业绩时间" prop="dachievementdate" width="120">
                <template #default="scope">
                  <span v-if="scope.row.dachievementdate">{{
                    dayjs(scope.row.dachievementdate).format('YYYY-MM-DD')
                  }}</span>
                  <span v-else>--</span>
                </template>
              </el-table-column>
              <!-- namountcost	成本金额 -->
              <el-table-column label="成本金额" prop="namountcost" align="right" width="120">
                <template #default="scope">
                  <span>{{ formatMoney(scope.row.namountcost, '2') }}</span>
                </template>
              </el-table-column>
              <!-- iaddapprovestatus	加刊审批状态 -->
              <el-table-column
                prop="iaddapprovestatus"
                label="加刊审批状态"
                sortable="custom"
                min-width="120"
                ><template #default="scope">
                  {{
                    approveStatusCombo.find((item) => item.id === scope.row.iaddapprovestatus)?.name
                  }}
                </template></el-table-column
              >
              <!-- ichangeapprovestatus	改刊审批状态 -->
              <el-table-column
                prop="ichangeapprovestatus"
                label="改刊审批状态"
                sortable="custom"
                min-width="120"
                ><template #default="scope">
                  {{
                    approveStatusCombo.find((item) => item.id === scope.row.ichangeapprovestatus)
                      ?.name
                  }}
                </template></el-table-column
              >
              <!-- istopapprovestatus	停刊审批状态 -->
              <el-table-column
                prop="istopapprovestatus"
                label="停刊审批状态"
                sortable="custom"
                min-width="120"
                ><template #default="scope">
                  {{
                    approveStatusCombo.find((item) => item.id === scope.row.istopapprovestatus)
                      ?.name
                  }}
                </template></el-table-column
              >
              <!-- iapprovestatus	订单审批状态 -->
              <el-table-column
                prop="iapprovestatus"
                label="订单审批状态"
                sortable="custom"
                min-width="120"
                ><template #default="scope">
                  {{
                    approveStatusCombo.find((item) => item.id === scope.row.iapprovestatus)?.name
                  }}
                </template></el-table-column
              >
              <!-- ipublishstatus	发布状态0-预约 1-预订 2-待发布 3-安排 4-见报 5-已发布 6-上架 7-下架 -->
              <el-table-column
                prop="ipublishstatus"
                label="发布状态"
                sortable="custom"
                min-width="120"
                ><template #default="scope">
                  {{
                    publishStatusCombo.find((item) => item.id === scope.row.ipublishstatus)?.name
                  }}
                </template></el-table-column
              >
              <!-- buse	是否有效 -->
              <el-table-column prop="buse" label="是否有效" sortable="custom" min-width="120"
                ><template #default="scope">
                  <el-checkbox v-model="scope.row.buse" disabled></el-checkbox> </template
              ></el-table-column>
              <!-- bdelete	是否删除 -->
              <el-table-column prop="bdelete" label="是否删除" sortable="custom" min-width="120"
                ><template #default="scope">
                  <el-checkbox v-model="scope.row.bdelete" disabled></el-checkbox> </template
              ></el-table-column>
              <!-- breportreason	是否推送填报欠款原因 -->
              <el-table-column
                prop="breportreason"
                label="是否推送填报欠款原因"
                sortable="custom"
                min-width="120"
                ><template #default="scope">
                  <el-checkbox v-model="scope.row.breportreason" disabled></el-checkbox> </template
              ></el-table-column>
              <!-- sremark	备注 -->
              <el-table-column
                prop="sremark"
                label="备注"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- iitemcode	刊期编码(自增列) -->
              <el-table-column
                prop="iitemcode"
                label="刊期编码"
                sortable="custom"
                min-width="120"
              ></el-table-column>
              <!-- ipublishcheckstatus	刊发检测状态(0-正常 1-未刊发 2-刊发错误 -->
              <el-table-column
                prop="ipublishcheckstatus"
                label="刊发检测状态"
                sortable="custom"
                min-width="120"
                ><template #default="scope">
                  {{
                    ipublishCheckStatusCombo.find(
                      (item) => item.id === scope.row.ipublishcheckstatus
                    )?.name
                  }}
                </template></el-table-column
              >
              <!-- spublishcheckcontent	刊发检测报告 -->
              <el-table-column
                prop="spublishcheckcontent"
                label="刊发检测报告"
                sortable="custom"
                min-width="120"
              ></el-table-column>
            </el-table>
          </el-row>
          <el-row>
            <el-pagination
              v-model:current-page="queryDetailParams.page"
              class="pagination"
              style="margin-left: 10px; width: 100%"
              :page-sizes="pageSizes"
              :page-size="queryDetailParams.pageSize"
              small
              background
              layout="total, sizes, prev, pager, next, jumper"
              :total="totalDetail"
              @size-change="handleDetailSizeChange"
              @current-change="handleDetailCurrentChange"
            >
            </el-pagination>
          </el-row>
        </div>
      </el-col>
    </el-row>
  </el-dialog>
  <el-dialog
    v-model="dialogAddVisible"
    title="新增"
    width="515"
    append-to-body
    destroy-on-close
    draggable
    :close-on-click-modal="false"
    @close="onCancelAdd"
  >
    <el-form
      ref="saveDataBackupDetailRef"
      :model="saveParams"
      :rules="rules as Partial<Record<string, Arrayable<FormItemRule>>>"
      label-width="150px"
    >
      <el-form-item label="选择开始日期" prop="dstartdate">
        <el-date-picker
          v-model="saveParams.dstartdate"
          :editable="false"
          type="date"
          placeholder="选择开始日期"
          :clearable="true"
          style="width: 300px"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          @change="handleSaveQuery"
        />
      </el-form-item>
      <el-form-item label="截止日期：" prop="denddate">
        <el-date-picker
          v-model="saveParams.denddate"
          :editable="false"
          type="date"
          placeholder="选择截止日期"
          :clearable="false"
          style="width: 300px"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          @change="handleSaveQuery"
        />
      </el-form-item>
      <el-form-item label="订单详情总数：" prop="nrecordsize">
        {{ saveParams.nrecordsize }}
      </el-form-item>
      <el-form-item label="名称" prop="databackupname">
        <el-form-item prop="databackupname">
          <el-input
            v-model="saveParams.databackupname"
            placeholder="请输入名称"
            maxlength="50"
            show-word-limit
            clearable
            style="width: 300px"
          />
        </el-form-item>
      </el-form-item>
      <el-form-item label="备注" prop="sdescription">
        <el-form-item prop="sdescription">
          <el-input
            v-model="saveParams.sdescription"
            type="textarea"
            placeholder="请输入备注"
            maxlength="100"
            show-word-limit
            clearable
            style="width: 300px"
          />
        </el-form-item>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer" style="text-align: center">
        <el-button type="primary" icon="Search" @click="handleSaveQuery"
          >查询订单详情总数</el-button
        >
        <el-button type="primary" icon="Edit" @click="onSubmitSave(saveDataBackupDetailRef)"
          >保 存</el-button
        >
        <el-button icon="Close" type="info" @click="onCancelAdd">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script setup lang="ts">
import useUserStore from '@/store/modules/user'
import modal from '@/plugins/modal'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { IAxios } from 'axios'
import { FormInstance, FormItemRule } from 'element-plus'
import {
  ECommissionStatus,
  EPublishstatus,
  ECustomerType,
  EHgDeptZtreeUrl,
  EDatabackupGroupDataType,
  EOrderibooktype,
  EOrderkind,
  EApproveStatus,
  EIPublishCheckStatus
} from '@/types/common/enumindex'
import { computTableHeight, tableHeaderColor, formatMoney } from '@/utils'
import { dayjs } from 'element-plus'
import { IDataCombo } from '@/types/common/DataCombo'
import { getMediaDataComboApi } from '@/api/media/media'
import { getEnumCombo } from '@/api/common/index'
// import { Tworderitembelong } from '@/types/views/ad/adtworder'
import { IBaseQueryInfo, TSelectZtree } from '@/types/common'
import {
  getDataBackupGroupPageListApi,
  getDataBackupDetailPageListApi,
  getOrderItemCountApi,
  saveDataBackupDetailApi
} from '@/api/statistic/databackupgroup'
// import {
//   TOrderItemCommissionVO,
//   TOrderItemCommissionDTO
// } from '@/types/views/commission/commissioncalcu'
import {
  TDatabackupGroupVO,
  TDataBackupDetailVO,
  Twdatabackupdetail1,
  Twdatabackupgroup,
  IDatabackupGroupDTO
} from '@/types/views/statistic/databackupgroup'
import { TAdCustomer } from '@/types/views/customer'
import { Arrayable } from 'element-plus/lib/utils'
/** 刊发状态列表 */
const publishStatusCombo: IDataCombo[] = getEnumCombo(EPublishstatus)
/** 汇总类型列表 */
const databackupGroupDataTypeCombo: IDataCombo[] = getEnumCombo(EDatabackupGroupDataType)
/** 订单类别列表 */
const orderkindCombo: IDataCombo[] = getEnumCombo(EOrderkind)
/** 订单录入方式列表 */
const orderibooktypeCombo: IDataCombo[] = getEnumCombo(EOrderibooktype)
/** 审批状态列表 */
const approveStatusCombo: IDataCombo[] = getEnumCombo(EApproveStatus)
/** 审批状态列表 */
const ipublishCheckStatusCombo: IDataCombo[] = getEnumCombo(EIPublishCheckStatus)
/** 计提状态下拉列表 */
const commissionStatusCombo: IDataCombo[] = getEnumCombo(ECommissionStatus)
/** 客户类型列表 */
const customerTypeCombo: IDataCombo[] = getEnumCombo(ECustomerType)
// const user = useUserStore().user
const total = ref(0)
const totalDetail = ref(0)
/** 详情窗口显示状态 */
const dialogDetailVisible = ref(false)
/** 选择客户显示状态 */
const dialogCustomerVisible = ref(false)
/** 新增显示状态 */
const dialogAddVisible = ref(false)
// /** 确认窗口显示状态 */
// const dialogConfirmVisible = ref(false)
// /** 发放窗口显示状态 */
// const dialogPayVisible = ref(false)
/** 表格高度 */
const tableheight = ref(0)
/** Form名称 */
const saveDataBackupDetailRef = ref<FormInstance>()
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
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
/** 详情查询时间 */
const timeDetaildata = reactive<TDateType>({
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
/** 查询条件 */
const queryParams = reactive<TDatabackupGroupVO>({
  sort: 'dcreatetime',
  order: 'desc',
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: ''
})
const initQueryDetailParams: TDataBackupDetailVO = {
  sort: 'dcreatetime',
  order: 'desc',
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  databackupgroupid: ''
}
/** 查询详情条件 */
const queryDetailParams = ref<TDataBackupDetailVO>({
  ...initQueryDetailParams
})
/** 查询订单详情总数条件 */
const saveQueryParams = ref<IBaseQueryInfo>({
  startTime: '',
  endTime: new Date().toISOString().split('T')[0]
})
/** 保存订单详情总数条件 */
const saveParams = ref<IDatabackupGroupDTO>({
  dstartdate: '',
  denddate: new Date().toISOString().split('T')[0],
  nrecordsize: 0
})
const rules = ref({
  endTime: [{ required: true, message: '截止日期不能为空', trigger: 'change' }],
  databackupname: [
    { required: true, min: 1, max: 10, message: '名称长度在 1 到 10 个字符', trigger: 'blur' }
  ],
  sdescription: [{ min: 1, max: 50, message: '备注长度在 1 到 50 个字符', trigger: 'blur' }]
})
/** group列表对象 */
const backupGroupList = ref<Twdatabackupgroup[]>([])
/** detail列表对象 */
const backupDetailList = ref<Twdatabackupdetail1[]>([])
/** Form选中的行 */
const multipleSelection = ref<Twdatabackupgroup[]>([])
/** DetailForm选中的行 */
const multipleDetailSelection = ref<Twdatabackupdetail1[]>([])
/** groupForm对象 */
const formDTO = ref<Twdatabackupgroup>({})
/** groupForm对象 */
const detailFormDTO = ref<Twdatabackupdetail1>({})
/** 媒体列表 */
const mediaCombo = ref<IDataCombo[]>([])
// /** 业绩归属 */
// const belongEditForm = ref<Tworderitembelong>({})
// /** 计提比例 */
// const ncommissionrate = ref(0)
// /** 计提金额 */
// const ncommission = ref(0)
// /** 当前选择行 */
// const curIndex = ref(0)

onMounted(() => {
  getMediaDataCombo()
  handleQuery()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})
/**
 * 获取媒体下拉数据
 */
const getMediaDataCombo = () => {
  getMediaDataComboApi().then((res: IAxios<IDataCombo[]>) => {
    if (res.success) {
      mediaCombo.value = res.obj
    }
  })
}
/**
 * 查询
 */
const handleQuery = () => {
  getDataBackupGroupPageListApi(queryParams)
    .then((res: IAxios<Twdatabackupgroup[]>) => {
      if (res.success) {
        backupGroupList.value = res.obj
        total.value = res.total
      }
    })
    .finally(() => {})
  tableheight.value = computTableHeight()
}
/**
 * 查询详情
 */
const handleDetailQuery = () => {
  getDataBackupDetailPageListApi(queryDetailParams.value)
    .then((res: IAxios<Twdatabackupdetail1[]>) => {
      if (res.success) {
        backupDetailList.value = res.obj
        totalDetail.value = res.total
      }
    })
    .finally(() => {})
  tableheight.value = computTableHeight()
}
/**
 * 查看详情
 * @param row
 */
const onDetail = (row: Twdatabackupgroup) => {
  queryDetailParams.value.databackupgroupid = row.id as string
  timeDetaildata.startTime = queryParams.startTime as string
  timeDetaildata.endTime = queryParams.endTime as string
  // console.log('timeDetaildata', timeDetaildata.startTime, timeDetaildata.endTime)
  dialogDetailVisible.value = true
}
/**
 * 选择部门
 * @param val
 */
const onSelectionDept = (val: TSelectZtree[]) => {
  const deptid = val.map((item) => item.id).join(',')
  queryDetailParams.value.deptid = deptid
}
/**
 * 选择人员
 * @param val
 */
const onSelectionEmp = (val: TSelectZtree[]) => {
  const employid = val.map((item) => item.id).join(',')
  queryDetailParams.value.employid = employid
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
 * 详情时间选择
 * @param val
 */
const onDetailtime = (val: TDateTimeType) => {
  let timev = false
  if (queryDetailParams.value.startTime) {
    timev = true
  }
  queryDetailParams.value.startTime = val.startTime
  queryDetailParams.value.endTime = val.endTime
  // timeDetaildata.startTime = val.startTime
  // timeDetaildata.endTime = val.endTime
  if (timev) {
    handleDetailQuery()
  }
}
/**
 * 行选择事件
 * @param val
 */
const handleSelectionChange = (val: Twdatabackupgroup[]) => {
  multipleSelection.value = val
}
/**
 * 详情行选择事件
 * @param val
 */
const handleDetailSelectionChange = (val: Twdatabackupdetail1[]) => {
  multipleDetailSelection.value = val
}

/**
 * 改变页码总数时
 */
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  handleQuery()
}
/**
 * 改变页数时
 * @param val
 */
const handleCurrentChange = (val: number) => {
  queryParams.page = val
  handleQuery()
}
/**
 * 改变详情页码总数时
 */
const handleDetailSizeChange = (val: number) => {
  queryDetailParams.value.pageSize = val
  handleDetailQuery()
}
/**
 * 改变详情页数时
 * @param val
 */
const handleDetailCurrentChange = (val: number) => {
  queryDetailParams.value.page = val
  handleDetailQuery()
}
/** 修改时删除客户列表信息 */
const SeeCustomerdelete = () => {
  queryDetailParams.value.queryKey = ''
  queryDetailParams.value.customerid = ''
}
/**
 * 选择客户信息后返回接收
 * @param val
 */
const selectCustomer = (val: TAdCustomer) => {
  queryDetailParams.value.customerid = val.id
  queryDetailParams.value.queryKey = val.sname
  dialogCustomerVisible.value = false
}
/**
 * 新增
 */
const onSubmitSave = async (formEl: FormInstance | undefined) => {
  await handleSaveQuery()
  console.log('handleSaveQuery detailCount2', saveParams.value.nrecordsize)
  if (!formEl) return
  formEl.validate((valid, fields) => {
    if (valid) {
      saveParams.value.dstartdate = saveQueryParams.value.startTime as string
      saveParams.value.denddate = saveQueryParams.value.endTime as string
      saveDataBackupDetailApi(saveParams.value).finally(() => {
        modal.alert('保存请求已发送')
      })
      saveDataBackupDetailRef.value?.resetFields()
      onCancelAdd()
      handleQuery()
    } else {
      console.log('error submit!', fields)
    }
  })
}
/**
 * 查询订单详情总数
 */
const handleSaveQuery = async () => {
  saveQueryParams.value.startTime = saveParams.value.dstartdate
  saveQueryParams.value.endTime = saveParams.value.denddate
  await getOrderItemCountApi(saveQueryParams.value)
    .then((res: IAxios<number>) => {
      if (res.success) {
        saveParams.value.nrecordsize = res.obj
      }
    })
    .finally(() => {})
  console.log('detailCount in handleSaveQuery', saveParams.value.nrecordsize)
}
/**
 * 取消新增
 */
const onCancelAdd = () => {
  saveDataBackupDetailRef.value?.resetFields()
  saveQueryParams.value.startTime = ''
  saveQueryParams.value.endTime = new Date().toISOString().split('T')[0]
  saveParams.value = {}
  saveParams.value.nrecordsize = 0
  dialogAddVisible.value = false
  formDTO.value = {}
}
/**
 * 取消详情
 */
const onCancelDetail = () => {
  queryDetailParams.value = { ...initQueryDetailParams }
  dialogDetailVisible.value = false
  formDTO.value = {}
}
</script>
<style lang="scss" scoped></style>
