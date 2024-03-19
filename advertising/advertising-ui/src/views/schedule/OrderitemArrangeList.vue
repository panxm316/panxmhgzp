<!--
 * @Author: songly
 * @Date: 2024-01-30 10:23:31
 * @LastEditTime: 2024-03-11 18:58:29
 * @LastEditors: wanghl
 * @Description:
-->
<!--
 * @Author: muyn
 * @Date: 2023-12-06
 * @Description: 广告安排 前端视图页面
-->
<template>
  <div>
    <div id="OrderitemArrangeList" class="app-container">
      <el-row :gutter="5">
        <el-col :xs="7" :sm="6" :md="6" :lg="4" :xl="4">
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
                :clearable="true"
                value-format="YYYY-MM-DD"
                style="width: 100%"
                @change="onSelectDate"
              />
              <HgSingleZtree
                :show-icon="true"
                :scopeflag="EHgSingleZtreeUrl.PAGEPLANE_GETFOLDPAGEPLANETREELIST"
                :treeparams="plantreeparams"
                :treeheight="computTableHeight() - 200"
                @change="onPlanChange"
              ></HgSingleZtree>
              <el-select
                v-model="queryInfo.adtypeid"
                placeholder="广告类型"
                style="width: 100%; margin-bottom: 10px"
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
              <HgZtreeSelect
                v-if="queryInfo.foldareaverid !== ''"
                :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYPERSON"
                :selectids="queryInfo.editorid ? [queryInfo.editorid] : []"
                :filterable="true"
                :treeparams="{ bIgnorePermissions: true }"
                style="width: 100%"
                @selectionztree="onSelectionEmp"
              ></HgZtreeSelect>
            </div>
          </div>
        </el-col>
        <el-col :xs="17" :sm="18" :md="18" :lg="20" :xl="20">
          <el-button
            type="primary"
            circle
            size="large"
            style="
              position: absolute;
              right: 20px;
              top: 20px;
              z-index: 99;
              height: 50px;
              width: 50px;
            "
            @click="handleOpen"
            >安排</el-button
          >
          <el-button
            v-if="orderitemarrangeList.length > 0"
            type="success"
            circle
            size="large"
            style="
              position: absolute;
              right: 20px;
              top: 80px;
              z-index: 99;
              height: 50px;
              width: 50px;
            "
            @click="handlestatus()"
            >已发布</el-button
          >
          <el-button
            v-if="orderitemarrangeList.length > 0"
            type="danger"
            circle
            size="large"
            style="
              position: absolute;
              right: 20px;
              top: 140px;
              z-index: 99;
              height: 50px;
              width: 50px;
            "
            @click="handlestatuscancel()"
            >撤回</el-button
          >
          <el-button
            v-if="orderitemarrangeList.length > 0"
            type="warning"
            circle
            size="large"
            style="
              position: absolute;
              right: 20px;
              top: 200px;
              z-index: 99;
              height: 50px;
              width: 50px;
            "
            @click="printVisible = true"
            >输出</el-button
          >
          <el-button
            v-if="OrderItemArrangeInfo.pagenum !== '0' && orderitemarrangeList.length > 0"
            v-print="printObj"
            type="warning"
            circle
            size="large"
            style="
              position: absolute;
              right: 20px;
              top: 200px;
              z-index: 99;
              height: 50px;
              width: 50px;
            "
            >打印</el-button
          >
          <div
            id="div_box"
            class="table_box left_box_list"
            style="
              width: 100%;
              text-align: center;
              display: flex;
              flex-wrap: wrap;
              justify-content: flex-start;
              overflow-y: auto;
            "
          >
            <div
              id="printMe"
              style="
                width: 100%;
                display: flex;
                flex-wrap: wrap;
                justify-content: flex-start;
                overflow-y: auto;
              "
            >
              <div
                v-for="(item, index) in pageList"
                :id="item.id"
                :key="item.id"
                ref="chatbox"
                style="position: relative; margin-top: 10px"
                @mousemove="handlgetMoveel(index, item.id)"
              >
                <div
                  :style="pageStyle(item)"
                  class="box_list"
                  unselectable="on"
                  style="position: relative"
                >
                  <!-- :style="pageStylechlidren(subitem)" -->
                  <div
                    v-for="(subitem, a) in item.orderitemarrangeList.filter(
                      (x:any) => x.ipublishstatusName !== '预订'
                    )"
                    :key="subitem.id"
                    style="
                      text-align: left;
                      line-height: 14px;
                      margin-bottom: 1px;
                      margin-left: 1px;
                      position: relative;
                      border: 1px solid #c45656;
                    "
                    :class="subitem.ibooktype == '1' ? 'box_list_see' : ''"
                    :style="
                      subitem.relateorderid === null
                        ? pageStylechlidren(subitem)
                        : pageStylechlidrenQ(subitem)
                    "
                    unselectable="on"
                    @mousedown="
                      subitem.ipublishstatusName !== '已发布' ? dragx($event, a, subitem) : ''
                    "
                    @mousemove="handleMouseMove($event)"
                    @mouseout="handleMouseOut($event)"
                  >
                    <div
                      style="width: 100%; height: 100%; position: relative; z-index: 99999"
                      @mousemove="handleMouseMoveQ(subitem.id)"
                      @mouseout="handleMouseOutQ()"
                    >
                      <p style="font-size: 12px">明细：{{ subitem.sadtitle }}</p>
                      <p style="font-size: 12px">内容：{{ subitem.sadcontent }}</p>
                      <p style="font-size: 12px">{{ subitem.scontractnum }}</p>
                      <p style="font-size: 12px">{{ subitem.ipublishstatusName }}</p>
                      <div
                        v-show="subitem.id === detail"
                        style="
                          background-color: aquamarine;
                          position: absolute;
                          width: 200px;
                          left: 20px;
                          line-height: 16px;
                          font-size: 12px;
                          z-index: 999999999999;
                          top: 50px;
                          left: 50px;
                        "
                      >
                        <el-button
                          v-if="
                            subitem.ipublishstatusName !== '已发布' &&
                            subitem.ipublishstatusName !== '预订' &&
                            subitem.relateorderid === null &&
                            subitem.ibooktype !== 1
                          "
                          style="
                            width: 20px;
                            height: 20px;
                            position: absolute;
                            top: 0px;
                            right: 0px;
                            background-color: #fff;
                            z-index: 99999999;
                          "
                          title="已发布"
                          link
                          type="success"
                          icon="top-right"
                          size="small"
                          @click="handlestatus(subitem.orderitemid)"
                        ></el-button>
                        <el-button
                          v-if="
                            subitem.ipublishstatusName === '已发布' &&
                            subitem.relateorderid === null &&
                            subitem.ibooktype !== 1
                          "
                          style="
                            width: 20px;
                            height: 20px;
                            position: absolute;
                            top: 0px;
                            right: 0px;
                            background-color: #fff;
                            z-index: 99999999;
                          "
                          title="取消发布"
                          link
                          type="danger"
                          icon="bottom-left"
                          size="small"
                          @click="handlestatuscancel(subitem.orderitemid)"
                        ></el-button>

                        <p style="font-size: 12px">位置：{{ subitem.pagesortname }}</p>
                        <p style="font-size: 12px">
                          规格色彩：{{ subitem.adcolorname }}{{ subitem.adspecname }}
                        </p>
                        <p style="font-size: 12px">备注：{{ subitem.sremark }}</p>
                        <p style="font-size: 12px">
                          客户：{{ subitem.customername }}{{ subitem.agentname
                          }}{{ subitem.agencyemployname }}
                        </p>
                      </div>
                    </div>
                  </div>
                </div>
                <p
                  v-if="OrderItemArrangeInfo.pagenum === '0'"
                  style="
                    position: absolute;
                    top: -24px;
                    left: 46%;
                    z-index: 9;
                    font-size: 12px;
                    color: red;
                    background-color: #fff;
                  "
                >
                  第{{ index + 1 }}版
                </p>
                <p
                  v-if="OrderItemArrangeInfo.pagenum !== '0'"
                  style="
                    position: absolute;
                    top: -24px;
                    left: 46%;
                    z-index: 9;
                    font-size: 12px;
                    color: red;
                    background-color: #fff;
                  "
                >
                  第{{ foldpageplannum }}版
                </p>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
    <el-dialog
      id="modelDialogRef"
      v-model="show"
      title="版面安排"
      width="900"
      :modal="false"
      :show-close="true"
      :close-on-click-modal="false"
      :draggable="true"
      left
    >
      <div class="app-container" style="margin-top: -30px">
        <div style="width: 100%; margin-bottom: 5px; text-align: right; padding-right: 5px">
          <span>批量安排：</span>
          <el-select
            v-model="orderitemarrangeData.foldpageplanid"
            placeholder="版面"
            style="width: 80px; margin: 0 5px 0"
            size="small"
          >
            <el-option
              v-for="item in orderitemarrangeListpage"
              :key="item.id"
              :label="item.name"
              :value="item.id!"
            />
          </el-select>
          <el-button type="success" icon="Top" size="small" @click="handleArrangepage()"
            >批量安排</el-button
          >
        </div>

        <el-row :gutter="5">
          <el-col :span="24">
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
                <el-table-column type="selection" prop="ischeck" :width="40" align="center">
                </el-table-column>
                <el-table-column prop="ibooktype" label="类型" :width="40" align="center">
                  <template #default="scope">
                    <span v-if="scope.row.ibooktype == '0'" type="success">预订</span>
                    <span v-if="scope.row.ibooktype == '1'" type="warning">预约</span>
                  </template>
                </el-table-column>
                <el-table-column prop="sadtitle" label="广告标题" show-overflow-tooltip width="200">
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
                      v-if="scope.row.ibooktype != '1'"
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
                  width="140"
                >
                </el-table-column>

                <el-table-column
                  prop="addisplayformname"
                  label="广告形式"
                  show-overflow-tooltip
                  width="120"
                >
                </el-table-column>
                <el-table-column prop="dpublishdate" label="预刊时间" width="120">
                  <template #default="scope">
                    <span>{{ formatDatesm(scope.row.dpublishdate) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="adcolorname" label="色彩" width="80"> </el-table-column>
                <el-table-column
                  prop="adspecname"
                  label="广告规格"
                  width="120"
                  show-overflow-tooltip
                >
                </el-table-column>
                <el-table-column prop="prefoldname" label="预定叠次" width="90"> </el-table-column>
                <el-table-column
                  prop="prefoldareavername"
                  label="叠次版本"
                  sortable="custom"
                  align="center"
                  width="90"
                >
                </el-table-column>
                <el-table-column prop="prefoldpageplanname" label="版面" width="80" align="center">
                </el-table-column>
                <el-table-column
                  prop="presremark"
                  label="订单备注"
                  width="120"
                  show-overflow-tooltip
                >
                </el-table-column>
                <el-table-column
                  prop="spublishstatusName"
                  label="状态"
                  width="60"
                  align="center"
                  fixed="right"
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
                  prop="foldname"
                  label="安排叠次"
                  align="center"
                  show-overflow-tooltip
                  width="80"
                  fixed="right"
                >
                </el-table-column>
                <el-table-column
                  prop="foldareavername"
                  label="叠次版本"
                  align="center"
                  width="80"
                  show-overflow-tooltip
                  fixed="right"
                >
                </el-table-column>
                <el-table-column
                  prop="foldpageplanname"
                  show-overflow-tooltip
                  label="安排版面"
                  align="center"
                  width="100"
                  fixed="right"
                >
                  <template #default="scope">
                    <el-select
                      v-model="scope.row.foldpageplanid"
                      placeholder="版面"
                      style="width: 80px"
                      size="small"
                      @change="handleArrange(scope.row, true)"
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
                <el-table-column label="操作" align="center" width="88" fixed="right">
                  <template #default="scope">
                    <el-button
                      v-if="treedept === true"
                      title="安排到该版面"
                      link
                      type="success"
                      icon="Edit"
                      size="small"
                      @click="handleArrange(scope.row, false)"
                    ></el-button>
                    <el-button
                      v-if="scope.row.spublishstatusName === '安排'"
                      title="取消安排"
                      link
                      type="danger"
                      icon="close"
                      size="small"
                      @click="resetArrange(scope.row.id)"
                    ></el-button>
                    <el-button
                      v-if="
                        scope.row.spublishstatusName !== '已发布' &&
                        scope.row.spublishstatusName !== '预订' &&
                        scope.row.ibooktype !== 1
                      "
                      title="已发布"
                      link
                      type="success"
                      icon="top-right"
                      size="small"
                      @click="handlestatus(scope.row.orderitemid)"
                    ></el-button>
                    <el-button
                      v-if="scope.row.spublishstatusName === '已发布' && scope.row.ibooktype !== 1"
                      title="取消发布"
                      link
                      type="danger"
                      icon="bottom-left"
                      size="small"
                      @click="handlestatuscancel(scope.row.orderitemid)"
                    ></el-button>
                  </template>
                </el-table-column>

                <el-table-column
                  prop="pagesortname"
                  label="版面类别"
                  width="100"
                  show-overflow-tooltip
                >
                </el-table-column>
                <el-table-column prop="employname" label="业务员" width="100" show-overflow-tooltip>
                </el-table-column>
                <el-table-column
                  prop="adtypename"
                  label="广告类别"
                  width="100"
                  show-overflow-tooltip
                >
                </el-table-column>
                <el-table-column
                  prop="  lastoperator"
                  label="编辑人员"
                  width="100"
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
                      @click="handleArrange(scope.row, true)"
                    ></el-button>
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
              <!-- <el-table
                :data="orderitemarrangeListselect"
                row-key="id"
                :border="true"
                stripe
                :height="tableheight"
                style="margin-top: 10px"
                :header-cell-style="tableHeaderColor"
                @selection-change="handleSelectionChange"
                @sort-change="handleSortChange"
              >
                <el-table-column type="selection" prop="ischeck" :width="40" align="center">
                </el-table-column>
                <el-table-column
                  prop="scontractnum"
                  label="合同号"
                  width="120"
                  show-overflow-tooltip
                ></el-table-column>
                <el-table-column
                  prop="customername"
                  label="客户名称"
                  sortable="custom"
                  show-overflow-tooltip
                  width="140"
                >
                </el-table-column>
                <el-table-column
                  prop="sadtitle"
                  label="广告标题"
                  show-overflow-tooltip
                  sortable="custom"
                  width="120"
                >
                </el-table-column>
                <el-table-column prop="dpublishdate" label="预刊时间" width="100">
                  <template #default="scope">
                    <span>{{ formatDatesm(scope.row.dpublishdate) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="adcolorname" label="色彩" sortable="custom" width="90">
                </el-table-column>
                <el-table-column
                  prop="adspecname"
                  label="广告规格"
                  sortable="custom"
                  width="120"
                  show-overflow-tooltip
                >
                </el-table-column>
                <el-table-column prop="prefoldname" label="预定叠次" sortable="custom" width="90">
                </el-table-column>
                <el-table-column
                  prop="prefoldareavername"
                  label="叠次版本"
                  sortable="custom"
                  align="center"
                  width="90"
                >
                </el-table-column>
                <el-table-column
                  prop="prefoldpageplanname"
                  label="版面"
                  sortable="custom"
                  width="80"
                  align="center"
                >
                </el-table-column>
                <el-table-column
                  prop="presremark"
                  label="订单备注"
                  width="120"
                  show-overflow-tooltip
                >
                </el-table-column>
                <el-table-column
                  prop="foldname"
                  label="安排叠次"
                  align="center"
                  sortable="custom"
                  width="90"
                  fixed="right"
                >
                </el-table-column>
                <el-table-column
                  prop="foldareavername"
                  label="叠次版本"
                  align="center"
                  sortable="custom"
                  width="90"
                  fixed="right"
                >
                </el-table-column>
                <el-table-column
                  prop="foldpageplanname"
                  label="安排版面"
                  align="center"
                  sortable="custom"
                  width="90"
                  fixed="right"
                >
                </el-table-column>
                <el-table-column
                  prop="spublishstatusName"
                  label="状态"
                  sortable="custom"
                  width="80"
                  align="center"
                >
                  <template #default="scope">
                    <el-tag
                      v-if="
                        scope.row.spublishstatusName === '预约' ||
                        scope.row.spublishstatusName === '预订' ||
                        scope.row.spublishstatusName === '安排'
                      "
                      >{{ scope.row.spublishstatusName }}</el-tag
                    >
                    <el-tag
                      v-if="
                        scope.row.spublishstatusName === '安排' ||
                        scope.row.spublishstatusName === '已发布' ||
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
                  prop="pagesortname"
                  label="版面类别"
                  sortable="custom"
                  width="100"
                  show-overflow-tooltip
                >
                </el-table-column>
                <el-table-column prop="employname" label="业务员" sortable="custom" width="100">
                </el-table-column>
                <el-table-column prop="adtypename" label="广告类别" sortable="custom" width="100">
                </el-table-column>
                <el-table-column
                  prop="  lastoperator"
                  label="编辑人员"
                  sortable="custom"
                  width="100"
                >
                </el-table-column>

                <el-table-column prop="dlastmodifydate" label="修改时间" width="100">
                  <template #default="scope">
                    <span>{{ formatDatesm(scope.row.dlastmodifydate) }}</span>
                  </template>
                </el-table-column>

                <el-table-column prop="sremark" label="安排备注" width="100" show-overflow-tooltip>
                </el-table-column>
                <el-table-column
                  label="操作"
                  align="center"
                  width="90"
                  class-name="small-padding fixed-width"
                  fixed="right"
                >
                  <template #default="scope">
                    <el-button
                      link
                      type="success"
                      icon="Edit"
                      size="small"
                      @click="handleArrange(scope.row)"
                      >安排</el-button
                    >
                  </template>
                </el-table-column>
              </el-table> -->
            </div>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
    <el-dialog v-model="printVisible" title="输出预览" width="1100px">
      <el-table
        id="printMetable"
        :data="orderitemarrangeList"
        row-key="id"
        :border="true"
        stripe
        style="min-height: 300px"
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <el-table-column prop="scontractnum" label="合同号" width="100"></el-table-column>
        <el-table-column prop="customername" label="客户名称" min-width="100"> </el-table-column>
        <el-table-column prop="sadtitle" label="广告标题" width="100"> </el-table-column>
        <el-table-column prop="dpublishdate" label="预刊时间" width="100">
          <template #default="scope">
            <span>{{ formatDatesm(scope.row.dpublishdate) }}</span>
            <span>{{ formatDatesm(scope.row.dlastmodifydate) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="adcolorname" label="色彩" width="60"> </el-table-column>
        <el-table-column prop="adspecname" label="广告规格" width="100"> </el-table-column>

        <el-table-column prop="foldname" label="叠次" align="center" width="60"> </el-table-column>
        <el-table-column prop="foldareavername" label="版本" align="center" width="60">
        </el-table-column>
        <el-table-column prop="foldpageplanname" label="版面" align="center" width="60">
        </el-table-column>

        <el-table-column prop="pagesortname" label="版面类别" width="60"> </el-table-column>
        <el-table-column prop="employname" label="业务员" width="60"> </el-table-column>
        <!-- <el-table-column prop="adtypename" label="广告类别" width="60"> </el-table-column> -->
        <el-table-column prop="  lastoperator" label="编辑人员" width="60"> </el-table-column>

        <!-- <el-table-column prop="dlastmodifydate" label="修改时间" width="60">
          <template #default="scope">
            <span>{{ formatDatesm(scope.row.dlastmodifydate) }}</span>
          </template>
        </el-table-column> -->
        <el-table-column prop="sremark" label="安排备注" width="80"> </el-table-column>
      </el-table>
      <div style="width: 100%; text-align: right; margin-top: 20px">
        <el-button v-print="printObjtable" type="primary">确认输出</el-button>
        <el-button type="danger" @click="printVisible = false">关闭</el-button>
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
  TOrderitemarrangeId,
  TOrderItemArrangeInfo,
  TupdateOrderitemarrangePos,
  TupdateOrderitemPublishStatus
} from '@/types/views/schedule/orderitemarrange'
import { getadtypelistApi } from '@/api/ad/adtype'
import { TAdtype } from '@/types/views/ad/adtype'
import {
  getOrderitemarrangePageListApi,
  getOrderitemarrangeByIdApi,
  saveOrderitemarrangeApi,
  getPageOrderItemArrangeInfoApi,
  updateOrderitemarrangePosApi,
  updateOrderitemPublishStatusApi,
  updateOrderitemarrangeStatusApi
} from '@/api/schedule/orderitemarrange'
import {
  getFoldAreaverComboApi,
  getFoldPagePlanListApi,
  getPageListtApi,
  queryOredrPredetermineListAPI
} from '@/api/schedule/seekschedule'
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
  computTreeHeighttab,
  validatePhone,
  computTreeHeight,
  fenbianlv
} from '@/utils/index'
const userStore = useUserStore()
import { EHgSingleZtreeUrl } from '@/types/common/enumindex'
defineOptions({
  name: 'OrderitemArrangeList'
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
const tableHeaderColor = () => {
  return { background: 'linear-gradient(0deg, #409eff, #409eff)', color: '#fff', padding: '0px' }
}
/**
 * 控制style
 */
const isActive = ref(false)
/**
 * 显示组件
 */
const show = ref(true)
/** 预定日期 */
const spublishdate = ref('')
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
  editorname: ''
})
const orderitemarrangeList = ref<TOrderitemarrange[]>([])
/** 安排版面列表 */
const orderitemarrangeListpage = ref<any>([])
/** 安排版面id */
const orderitemarrangeListpageid = ref('')
const orderitemarrangeListselect = ref<TOrderitemarrange[]>([])
const orderitemarrangeSelection = ref<TOrderitemarrange[]>()
/** 版面列表 */
const pageList = ref<any>([])
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
  nleftx: 30,
  ntopy: 40
})
/** 版面请求对象 */
const pagevo = ref({
  mediaId: '',
  foldId: '',
  areaverId: '',
  publishTime: ''
})
/**
 * 获取广告板数列表
 */
const OrderItemArrangeInfo = ref<TOrderItemArrangeInfo>({
  /**
   * 广告类型id
   */
  adtypeid: '',
  /**
   * 刊发日期
   */
  dpublishdate: '',
  /**
   * 编辑人员id
   */
  editorid: '',
  /**
   * 叠次版本id
   */
  foldareaverid: '',
  /**
   * 叠次id
   */
  foldid: '',
  /**
   * 录入方式 0-正常 1-广告预约 2-快速预约 3-补刊
   */
  ibooktype: '',
  /**
   * 媒体id
   */
  mediaid: '',
  /**
   * 媒体类型key
   */
  mediatypekey: 'paper',
  /**
   * 版号
   */
  pagenum: '0',
  /**
   * 查询关键字
   */
  queryKey: ''
})
/**
 * 详情显示隐藏
 */
const detailVisible = ref(false)
/**
 * 已发布状态
 */
const publishstatus = ref(false)
/**
 * 设置为已发布数据
 */
const publishstatusData = ref<TupdateOrderitemPublishStatus>({
  id: '',
  bPublish: false
})
/**
 * 设置为已发布数据接收数组
 */
const publishstatusDataList = ref<string[]>([])
/** 详情显示隐藏 */
const detail = ref('')
/**
 * 版面列表显示
 */
const handleOpen = () => {
  show.value = true
  // loaddata()
}
/**
 * 鼠标经过拖拽时阻止默认点击事件
 */
const handleMouseMove = (e: any) => {
  e.style.pointerEvents = 'none'
}
const handleMouseOut = (e: any) => {
  e.style.pointerEvents = null
}
/**
 * 鼠标经过显示详情
 */
const handleMouseMoveQ = (id: string) => {
  detailVisible.value = true
  detail.value = id
}
const handleMouseOutQ = () => {
  detailVisible.value = false
  detail.value = ''
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
/**
 * 已发布状态
 */
const handlestatus = (data?: string) => {
  publishstatusData.value.bPublish = true
  if (data === undefined) {
    handaddChange()
  } else {
    publishstatusData.value.id = data
  }
  /**
   * 没有已发布版面时跳出提示
   */
  if (publishstatusData.value.id === '') {
    modal.msgWarning('没有需要已发布的版面')
    return
  }
  modal
    .confirm('是否设置已发布状态？')
    .then(() => {
      updateOrderitemPublishStatusApi(publishstatusData.value).then(() => {
        modal.msgSuccess('设置已发布状态成功')
        loaddata()
      })
      publishstatus.value = true
    })
    .catch(() => {})
}
/**
 * 取消已发布
 */
const handlestatuscancel = (data?: string) => {
  publishstatusData.value.bPublish = false
  if (data === undefined) {
    handaddChange()
  } else {
    publishstatusData.value.id = data
  }
  /**
   * 没有已发布版面时跳出提示
   */
  if (publishstatusData.value.id === '') {
    modal.msgWarning('没有需要取消已发布的版面')
    return
  }
  modal
    .confirm('是否取消已发布状态？')
    .then(() => {
      updateOrderitemPublishStatusApi(publishstatusData.value).then(() => {
        modal.msgSuccess('设置已发布状态成功')
        loaddata()
      })
      publishstatus.value = false
    })
    .catch(() => {})
}
/**
 * 已发布数据
 */
const handaddChange = () => {
  publishstatusDataList.value = [] as string[]
  orderitemarrangeList.value.forEach((item) => {
    if ((item.foldpageplanname !== null || item.foldpageplanname !== '') && item.ibooktype !== 1) {
      publishstatusDataList.value.push(item.orderitemid as string)
    }
  })

  publishstatusData.value.id = publishstatusDataList.value.join(',')
}
/**
 * 版本列表
 */
const orderitemarrangechildren = ref<ITreeNode[]>([])
onMounted(() => {
  treeheight.value = computTreeHeight()
  tableheight.value = 300
  getAdtypeCombo()
  spublishdate.value = nowTime()
  orderitemdate.value = nowTime()
  nextTick(() => {
    onSelectDate()
    // 把弹窗父亲的父亲 dom 元素添加 pointer-events: none; 防止穿透
    const elDialog = document.getElementById('modelDialogRef')
    // @ts-ignore
    elDialog?.parentNode?.parentNode?.setAttribute('style', 'pointer-events: none;')
  })
})
/**
 * 查询版面计划
 */
const onSelectDate = () => {
  queryInfo.dpublishdate = spublishdate.value
  OrderItemArrangeInfo.value.dpublishdate = spublishdate.value
  plantreeparams.value = {
    publishdate: spublishdate.value,
    mediaId: '',
    mediatypekey: 'paper',
    isShowPagenum: true
  }
  queryInfo.foldareaverid = ''
  pageList.value = []
  orderitemarrangeList.value = []
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
 *
 * @param data 查询时选择叠次版本
 */
const onPlanChange = (data: ITreeNode) => {
  console.log(data)
  if (data === undefined) {
    return
  }
  if (data.stype === 'media') {
    queryInfo.mediaid = data.id
    queryInfo.foldid = ''
    queryInfo.foldareaverid = ''
  }
  if (data.stype === 'fold') {
    const parentNode = data.getParentNode!()
    queryInfo.mediaid = parentNode.id
    queryInfo.foldid = data.id
    queryInfo.foldareaverid = ''
  }
  if (data.stype === 'foldarea') {
    treedept.value = false
    pageNumList.value = [] as ITreeNode[]
    const parentNode = data.getParentNode!()
    /** 媒体 */
    const parentfoldNode = parentNode.getParentNode!()
    queryInfo.mediaid = parentfoldNode.id
    queryInfo.foldid = parentNode.id
    queryInfo.foldareaverid = data.id
    orderitemarrangechildren.value = data.children as ITreeNode[]
    orderitemarrangeListpage.value = data.childrenNodes
    orderitemarrangeData.value.foldid = parentNode.id
    orderitemarrangeData.value.foldname = parentNode.name
    orderitemarrangeData.value.foldareaverid = data.id
    orderitemarrangeData.value.foldareavername = data.name
    orderitemarrangeData.value.foldpageplanid = ''
    orderitemarrangeData.value.foldpageplanname = ''
    OrderItemArrangeInfo.value.mediaid = parentfoldNode.id
    OrderItemArrangeInfo.value.foldid = parentNode.id
    OrderItemArrangeInfo.value.foldareaverid = data.id
    OrderItemArrangeInfo.value.pagenum = '0'
    loaddata()
  }
  if (data.stype === 'pagenum') {
    treedept.value = true
    pageNumList.value = [] as ITreeNode[]
    const parentNode = data.getParentNode!()
    /** 媒体 */
    const parentfoldNode = parentNode.getParentNode!()
    const parentfoldDareaver = parentfoldNode.getParentNode!()
    queryInfo.mediaid = parentfoldDareaver.id
    queryInfo.foldid = parentfoldNode.id
    queryInfo.foldareaverid = parentNode.id
    orderitemarrangechildren.value = parentNode.children as ITreeNode[]
    orderitemarrangeData.value.foldid = parentfoldNode.id
    orderitemarrangeData.value.foldname = parentfoldNode.name
    orderitemarrangeData.value.foldareaverid = parentNode.id
    orderitemarrangeData.value.foldareavername = parentNode.name
    orderitemarrangeData.value.foldpageplanid = data.id
    orderitemarrangeData.value.foldpageplanname = data.name
    OrderItemArrangeInfo.value.mediaid = parentfoldDareaver.id
    OrderItemArrangeInfo.value.foldid = parentfoldNode.id
    OrderItemArrangeInfo.value.foldareaverid = parentNode.id
    OrderItemArrangeInfo.value.pagenum = data.name
    foldpageplannum.value = Number(data.name)
    pageNumList.value.push(data)
    loaddata()
  }
}
/**
 * 点击版本的时候记录版号
 */
const foldpageplannum = ref(1)
/** 版面数量 */
const publishNum = ref('')
/** 当前版信息 */
const pageNumList = ref<ITreeNode[]>([])
/** 判断点击层级 false是点击叠次版本 */
const treedept = ref(false)
/**
 * 获取版面列表
 */
const getPageList = () => {
  getPageOrderItemArrangeInfoApi(OrderItemArrangeInfo.value).then((res) => {
    if (res.success) {
      console.log(res.obj)
      pageList.value = res.obj
      publishNum.value = '版面数量: ' + pageList.value.length
    }
  })
}

/**
 * 计算版面显示属性
 */
const pageStyle = (item: any) => {
  var tempPageWidth = 0
  var tempPageHeight = 0
  paperheart.value.height = item.ipageheight
  paperheart.value.width = item.ipagewidth
  if (OrderItemArrangeInfo.value.pagenum === '0') {
    tempPageWidth = item.ipagewidth
    tempPageHeight = item.ipageheight
  } else if (OrderItemArrangeInfo.value.pagenum !== '0') {
    tempPageWidth = item.ipagewidth * 2
    tempPageHeight = item.ipageheight * 2
  }

  var color = ''
  if (item.adcolorname === '彩色') {
    color = '#a0cfff'
  } else if (item.adcolorname === '黑白') {
    color = '#e9e9eb'
  } else {
    color = '#fab6b6'
  }
  return {
    width: tempPageWidth + 'px',
    height: tempPageHeight + 'px',
    backgroundColor: color
  }
}
const pageStylechlidren = (item?: any) => {
  var tempPageWidth = 0
  var tempPageHeight = 0
  var nleftx = 0
  var ntopy = 0
  if (OrderItemArrangeInfo.value.pagenum === '0') {
    tempPageWidth = item.nwidth * 10
    tempPageHeight = item.nheight * 10
    nleftx = item.nleftx
    ntopy = item.ntopy
  } else if (OrderItemArrangeInfo.value.pagenum !== '0') {
    tempPageWidth = item.nwidth * 10 * 2
    tempPageHeight = item.nheight * 10 * 2
    nleftx = item.nleftx * 2
    ntopy = item.ntopy * 2
  }
  return {
    width: tempPageWidth + 'px',
    height: tempPageHeight + 'px',
    left: nleftx + 'px',
    top: ntopy + 'px',
    position: 'absolute',
    zIndex: '999999',
    backgroundColor: 'rgba(241, 33, 41, 0.8)'
  }
}
const pageStylechlidrenQ = (item?: any) => {
  var tempPageWidth = 0
  var tempPageHeight = 0

  if (OrderItemArrangeInfo.value.pagenum === '0') {
    tempPageWidth = item.nwidth * 10
    tempPageHeight = item.nheight * 10
  } else if (OrderItemArrangeInfo.value.pagenum !== '0') {
    tempPageWidth = item.nwidth * 10 * 2
    tempPageHeight = item.nheight * 10 * 2
  }
  return {
    width: tempPageWidth + 'px',
    height: tempPageHeight + 'px',
    float: 'left',
    zIndex: '999',
    backgroundColor: 'rgba(228, 41, 145, 0.8)'
  }
}
/**
 * 获取总表列表
 */
const loaddata = () => {
  // show.value = true
  getOrderitemarrangePageListApi(queryInfo).then(({ obj, total }: IAxios<TOrderitemarrange[]>) => {
    pageTotal.value = total
    orderitemarrangeList.value = []
    orderitemarrangeListselect.value = []
    // 过滤需要的面积广告和专栏广告
    // obj.forEach((item) => {
    //   if (item.addisplayformname !== '分类广告') {
    //     orderitemarrangeList.value.push(item)
    //   }
    // })
    console.log(orderitemarrangeList.value)
    obj.forEach((item) => {
      orderitemarrangeList.value.push(item)
    })
    // orderitemarrangeListpage.value = orderitemarrangeList.value[0].pageNumList
    getPageList()
    reset()
  })
}
/**
 * 查询的时候选择业务人员
 * @param val
 */
const onSelectionEmp = (val: TSelectZtree[]) => {
  const employid = val.map((item) => item.id).join(',')
  queryInfo.editorid = employid
  loaddata()
}
/**
 * 选择
 * @param rows
 */
const handleSelectionChange = (rows: TOrderitemarrange[]) => {
  orderitemarrangeSelection.value = rows
}
// 安排,data是true是多版安排，data=false时，单板安排
const handleArrange = (row?: TOrderitemarrange, data?: boolean) => {
  selectItems.value.id = row?.id as string
  selectItems.value.orderid = row?.orderid as string
  selectItems.value.orderitemid = row?.orderitemid as string
  selectItems.value.version = row?.version
  if (row?.nleftx !== 0 && row?.ntopy !== 0) {
    selectItems.value.nleftx = row?.nleftx as number
    selectItems.value.ntopy = row?.ntopy as number
  }
  orderitemarrangeData.value.orderitemids?.push(selectItems.value)
  if (data === true) {
    orderitemarrangeData.value.foldpageplanid = row?.foldpageplanid as string
    orderitemarrangechildren.value.forEach((item) => {
      if (item.id === row?.foldpageplanid) {
        orderitemarrangeData.value.foldpageplanname = item.name
      }
    })
  } else {
    orderitemarrangeData.value.foldpageplanid = pageNumList.value[0].id
    orderitemarrangeData.value.foldpageplanname = pageNumList.value[0].name
  }
  orderitemarrangeData.value.editorid = row?.employid as string
  orderitemarrangeData.value.editorname = row?.employname as string
  orderitemarrangeData.value.sremark = row?.sremark as string
  orderitemarrangeData.value.foldareaverid = row?.prefoldareaverid as string
  orderitemarrangeData.value.foldareavername = row?.prefoldareavername as string
  orderitemarrangeData.value.foldid = row?.prefoldid as string
  orderitemarrangeData.value.foldname = row?.prefoldname as string
  handleSave()
}
/**
 * 选择版面批量安排
 */
const handleArrangepage = () => {
  if (orderitemarrangeSelection.value) {
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
    handleSave()
  } else {
    modal.msgWarning('请选择需要安排的订单')
    return
  }
}
/**
 * 保存
 */
const handleSave = () => {
  if (
    orderitemarrangeData.value.foldpageplanid !== '' &&
    orderitemarrangeData.value.foldpageplanid !== 'null'
  ) {
    saveOrderitemarrangeApi(orderitemarrangeData.value).then(({ success, msg }: IAxios) => {
      if (success) {
        modal.msgSuccess('操作成功')
      } else {
        modal.msgError(msg)
      }
      loaddata()
    })
  } else {
    modal.msgWarning('请选择版面')
    return
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
 * 获取广告类别
 */
const getAdtypeCombo = () => {
  getadtypelistApi().then(({ success, obj }: IAxios<TAdtype[]>) => {
    if (success) {
      adtypeCombo.value = obj
      queryInfo.adtypeid = obj[0].id
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
}
// 重置版面查询参数
const resetpagevo = () => {
  pagevo.value = {
    mediaId: '',
    foldId: '',
    areaverId: '',
    publishTime: ''
  }
}

// 拖拽功能-------------------------
/** 定位数据 */
const position = ref<TupdateOrderitemarrangePos>({
  id: '',
  nleftx: '0',
  ntopy: '0'
})
/**
 * 更新位置
 */
const updateOrderitemarrangePos = () => {
  updateOrderitemarrangePosApi(position.value).then(({ success, msg }) => {
    if (success) {
      modal.msgSuccess('操作成功')
    } else {
      modal.msgError(msg)
    }
  })
}

const chatbox = ref<any[]>([])
const chatboxselect = ref<any[]>([])
// const chatbox = ref()
/**
 * 鼠标移入遍历chatbox数组对应页面
 */
const handlgetMoveel = (index: number, id: string) => {
  chatboxselect.value = []
  chatbox.value.forEach((item) => {
    // if (item.__vnode.key === id) {
    if (item.id === id) {
      chatboxselect.value.push(...item.children[0].children)
    }
  })
}
/**
 * 报纸版心
 */
const paperheart = ref({
  width: 0,
  height: 0
})
/**
 * 是退拽还是初始排版false是初始排版
 */
const isdrag = ref(false)
const dragx = (el: any, index: number, item: any) => {
  const oDiv = chatboxselect.value
  // position.value.id = oDiv[index].__vnode.key
  position.value.id = item.id
  const disX = el.clientX - oDiv[index].offsetLeft
  const disY = el.clientY - oDiv[index].offsetTop
  document.onmouseover = (e) => {
    e.preventDefault
  }
  document.onmousemove = function (e) {
    e.preventDefault()
    // 通过事件委托，计算移动的距离
    let l = e.clientX - disX
    let t = e.clientY - disY
    if (OrderItemArrangeInfo.value.pagenum === '0') {
      if (l < 0) {
        // 如果左侧的距离小于0，就让距离等于0.不能超出屏幕左侧。如果需要磁性吸附，把0改为100或者想要的数字即可
        l = 0
      } else if (l > paperheart.value.width - oDiv[index].offsetWidth) {
        // 如果左侧的距离>屏幕的宽度-元素的宽度。也就是说元素的右侧超出屏幕的右侧，就让元素的右侧在屏幕的右侧上
        l = paperheart.value.width - oDiv[index].offsetWidth
      }
      if (t < 0) {
        // 和左右距离同理
        t = 0
      } else if (t > paperheart.value.height - oDiv[index].offsetHeight) {
        t = paperheart.value.height - oDiv[index].offsetHeight
      }
    } else if (OrderItemArrangeInfo.value.pagenum !== '0') {
      if (l < 0) {
        // 如果左侧的距离小于0，就让距离等于0.不能超出屏幕左侧。如果需要磁性吸附，把0改为100或者想要的数字即可
        l = 0
      } else if (l > paperheart.value.width * 2 - oDiv[index].offsetWidth) {
        // 如果左侧的距离>屏幕的宽度-元素的宽度。也就是说元素的右侧超出屏幕的右侧，就让元素的右侧在屏幕的右侧上
        l = paperheart.value.width * 2 - oDiv[index].offsetWidth
      }
      if (t < 0) {
        // 和左右距离同理
        t = 0
      } else if (t > paperheart.value.height * 2 - oDiv[index].offsetHeight) {
        t = paperheart.value.height * 2 - oDiv[index].offsetHeight
      }
    }
    // 移动当前元素
    // oDiv[index].style.left = l + 'px'
    // oDiv[index].style.top = t + 'px'
    if (OrderItemArrangeInfo.value.pagenum === '0') {
      position.value.nleftx = l + ''
      position.value.ntopy = t + ''
      item.nleftx = l
      item.ntopy = t
    } else if (OrderItemArrangeInfo.value.pagenum !== '0') {
      position.value.nleftx = l / 2 + ''
      position.value.ntopy = t / 2 + ''
      item.nleftx = l / 2
      item.ntopy = t / 2
    }
  }

  document.onmouseup = function (e) {
    e.preventDefault()

    document.onmousemove = null
    document.onmouseup = null
    updateOrderitemarrangePos()
    loaddata()
  }
  // 解决有些时候,在鼠标松开的时候,元素仍然可以拖动;
  document.ondragstart = function (ev) {
    ev.preventDefault()
  }
  document.ondragend = function (ev) {
    ev.preventDefault()
  }
  return false
}
// 拖拽功能-------------------------
/**
 * 打印区域-------------------------
 */
/**
 * 控制打印区域显示隐藏
 */
const printVisible = ref(false)
/**
 * 打印区域数据
 */
const printObj = ref({
  id: 'printMe', // 这里是要打印元素的ID
  popTitle: '&nbsp', // 打印的标题
  extraCss: '', // 打印可引入外部的一个 css 文件
  extraHead: '', // 打印头部文字
  preview: false, // 是否启动预览模式，默认是false
  previewTitle: '打印客户账单', // 打印预览的标题
  previewPrintBtnLabel: '预览结束，开始打印', // 打印预览的标题下方的按钮文本，点击可进入打印
  zIndex: 10002, // 预览窗口的z-index，默认是20002，最好比默认值更高
  previewBeforeOpenCallback() {
    console.log('正在加载预览窗口！')
  }, // 预览窗口打开之前的callback
  previewOpenCallback() {
    console.log('已经加载完预览窗口，预览打开了！')
  }, // 预览窗口打开时的callback
  beforeOpenCallback() {
    console.log('开始打印之前！')
  }, // 开始打印之前的callback
  openCallback() {
    console.log('执行打印了！')
  }, // 调用打印时的callback
  closeCallback() {
    console.log('关闭了打印工具！')
  }, // 关闭打印的callback(无法区分确认or取消)
  clickMounted() {
    console.log('点击v-print绑定的按钮了！')
  }
})
/**
 * 打印区域数据
 */
const printObjtable = ref({
  id: 'printMetable', // 这里是要打印元素的ID
  popTitle: '&nbsp', // 打印的标题
  extraCss: '', // 打印可引入外部的一个 css 文件
  extraHead: '', // 打印头部文字
  preview: false, // 是否启动预览模式，默认是false
  previewTitle: '打印客户账单', // 打印预览的标题
  previewPrintBtnLabel: '预览结束，开始打印', // 打印预览的标题下方的按钮文本，点击可进入打印
  zIndex: 10002, // 预览窗口的z-index，默认是20002，最好比默认值更高
  previewBeforeOpenCallback() {
    console.log('正在加载预览窗口！')
  }, // 预览窗口打开之前的callback
  previewOpenCallback() {
    console.log('已经加载完预览窗口，预览打开了！')
  }, // 预览窗口打开时的callback
  beforeOpenCallback() {
    console.log('开始打印之前！')
  }, // 开始打印之前的callback
  openCallback() {
    console.log('执行打印了！')
  }, // 调用打印时的callback
  closeCallback() {
    console.log('关闭了打印工具！')
  }, // 关闭打印的callback(无法区分确认or取消)
  clickMounted() {
    console.log('点击v-print绑定的按钮了！')
  }
})
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
.left_box_list {
  height: v-bind(treeheight);
}
:deep(.el-dialog__wrapper) {
  pointer-events: none;
}
:deep(.el-dialog) {
  pointer-events: auto !important;
}
#div_box {
  background-image: url('@/assets/images/dibu.png');
  opacity: 0.8;
  /* background-color: black; */
}

/* ---------- */
.page_box {
  min-height: 635px;
  width: 98%;
  margin: 0 auto;
}
.page_box_title {
  width: 100%;
}
.page_box_plan {
  width: 100%;
  margin-top: 15px;
}
.page_box_col {
  background-color: #ffffff;
  width: 100%;
  padding: 15px;
  border-radius: 4px;
}
.page_box_gray {
  background-color: #e8e8e8;
  width: 95%;
  padding: 15px;
  border-radius: 4px;
  margin-left: 0px;
}
.page_box_gray_bar {
  width: 100%;
  height: 35px;
}
.page_box_gray_bar_color {
  width: 18px;
  height: 18px;
  background-color: deepskyblue;
  float: left;
}

.page_box_gray_bar_text {
  width: auto;
  height: 35px;
  line-height: 35px;
  float: left;
  margin-left: 5px;
  margin-top: -8px;
  font-size: 14px;
}

.page_box_gray_content {
  width: 100%;
  height: auto;
  overflow-y: hidden;
}

.ul {
  margin: 0;
  padding: 0;
}

.pageli {
  float: left;
  margin-left: 20px;
  margin-top: 20px;
}
.orderdetail_box {
  max-height: 200px;
  width: 100%;
  margin: 0 auto;
  overflow: hidden;
  background-color: #ffffff;
  margin-bottom: 15px;
}
.box_list p {
  margin: 0 0 5px;
  padding: 0;
}
.box_list_see {
  background-color: #f5f5f5 !important;
}
</style>
