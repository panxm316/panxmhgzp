<!--
 * @Author: suny
 * @Date: 2023-12-04 09:16:31
 * @LastEditTime: 2024-01-17 15:10:52
 * @LastEditors: wanghl
 * @Description: 价格查询
-->
<template>
  <div class="app-container">
    <el-space
      :size="10"
      style="width: 100%; overflow-x: auto; background-color: #fff; padding: 0 8px"
    >
      <div>
        <!-- 叠次 -->
        <el-card class="cardbox">
          <div>
            <span>{{ EMediaType[curMediaTypeKey].foldName }}</span>
            <el-button
              v-if="foldselectid !== undefined && foldselectid!.length > 0"
              type="primary"
              link
              @click="onCancel"
              >取消</el-button
            >
          </div>
          <HgSingleZtree
            v-model="foldselectid"
            :scopeflag="EHgSingleZtreeUrl.PRICE_GETMEDIAFOLDTREE"
            :show-icon="true"
            :treeparams="{ mediaType: 'paper,app,wei,multi' }"
            disabled
            @change="onChange"
          ></HgSingleZtree>
        </el-card>
      </div>
      <div v-if="EMediaType[curMediaTypeKey].foldareaverName">
        <!-- 叠次版本 -->
        <el-card class="cardbox">
          <div>
            <span>{{ EMediaType[curMediaTypeKey].foldareaverName }}</span>
            <el-button
              v-if="priceItemForm.foldareaid!.length > 0"
              type="primary"
              link
              @click="priceItemForm.foldareaid = ''"
              >取消</el-button
            >
          </div>
          <HgSingleZtree
            v-model="priceItemForm.foldareaid"
            :scopeflag="EHgSingleZtreeUrl.PRICE_GETFOLDAREAVERTREELIST"
            :treeparams="areavertreeparams"
            @change="onAreaverChange"
          ></HgSingleZtree>
        </el-card>
      </div>
      <div v-if="EMediaType[curMediaTypeKey].pagetypeName">
        <!-- 版本类别 -->
        <el-card class="cardbox">
          <div>
            <span>{{ EMediaType[curMediaTypeKey].pagetypeName }}</span>
            <el-button
              v-if="priceItemForm.pagesortid!.length > 0"
              type="primary"
              link
              @click="priceItemForm.pagesortid = ''"
              >取消</el-button
            >
          </div>
          <HgSingleZtree
            v-model="priceItemForm.pagesortid"
            :scopeflag="EHgSingleZtreeUrl.PRICE_GETPAGESORTTREELIST"
            :treeparams="pagesortparams"
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
              v-if="priceItemForm.addisplayformid!.length > 0"
              type="primary"
              link
              @click="priceItemForm.addisplayformid = ''"
              >取消</el-button
            >
          </div>
          <HgSingleZtree
            v-model="priceItemForm.addisplayformid"
            :scopeflag="EHgSingleZtreeUrl.PRICE_GETADDISPLAYFORMTREELIST"
            :treeparams="{ mediaType: curMediaTypeKey!}"
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
              v-if="priceItemForm.adspecid!.length > 0"
              type="primary"
              link
              @click="priceItemForm.adspecid = ''"
              >取消</el-button
            >
          </div>
          <HgSingleZtree
            v-model="priceItemForm.adspecid"
            :scopeflag="EHgSingleZtreeUrl.PRICE_GETADSPECTREELIST"
            :treeparams="{ mediaId: adspecparams?.mediaId }"
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
              v-if="priceItemForm.adcolorid!.length > 0"
              type="primary"
              link
              @click="priceItemForm.adcolorid = ''"
              >取消</el-button
            >
          </div>
          <HgSingleZtree
            v-model="priceItemForm.adcolorid"
            :scopeflag="EHgSingleZtreeUrl.PRICE_GETADCOLORTREELIST"
            :treeheight="curMediaTypeKey === 'paper' ? 122 : 300"
            :treeparams="{ mediaType: curMediaTypeKey }"
            @change="onAdcolorChange"
          ></HgSingleZtree>
        </el-card>
        <el-card v-if="curMediaTypeKey === 'paper'" class="cardbox">
          <div>
            <span>星期</span>
            <el-button
              v-if="priceItemForm.weeksettingid!.length > 0"
              type="primary"
              link
              @click="priceItemForm.weeksettingid = ''"
              >取消</el-button
            >
          </div>
          <HgSingleZtree
            v-model="priceItemForm.weeksettingid"
            :scopeflag="EHgSingleZtreeUrl.PRICE_GETWEEKSETTINGTREE"
            :treeheight="122"
            @change="onWeeksettingChange"
          ></HgSingleZtree>
        </el-card>
      </div>
    </el-space>

    <div>
      <el-form ref="priceItemRef" :inline="true" :model="priceItemForm">
        <el-form-item label="标准价" prop="baseprice">
          <el-input-number
            v-model="priceItemForm.baseprice"
            :precision="2"
            placeholder="标准价"
            :min="0"
            :max="9999999999"
            :controls="false"
            style="width: 120px"
          />
        </el-form-item>
        <el-form-item label="开始日期" prop="dstartdate">
          <el-date-picker
            v-model="priceItemForm.dstartdate"
            type="date"
            placeholder="开始日期"
            value-format="YYYY-MM-DD"
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker
            v-model="priceItemForm.denddate"
            type="date"
            placeholder="结束日期"
            value-format="YYYY-MM-DD"
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="备注" prop="sremark">
          <el-input v-model="priceItemForm.sremark" :maxlength="200" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button type="primary" icon="Delete" @click="onCancelAll">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="table_box">
      <el-table
        ref="dataTableRef"
        :data="priceItemList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        :border="true"
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <el-table-column type="selection" prop="ischeck" width="50" align="center">
        </el-table-column>
        <el-table-column
          :min-width="100"
          :label="EMediaType[curMediaTypeKey].foldName"
          prop="foldname"
          align="center"
        >
          <template #default="scope">
            <span>{{ scope.row.foldname || scope.row.medianame }}</span>
          </template>
        </el-table-column>
        <el-table-column
          v-if="EMediaType[curMediaTypeKey].foldareaverName"
          :min-width="100"
          :label="EMediaType[curMediaTypeKey].foldareaverName"
          prop="foldareaname"
          align="center"
        />
        <el-table-column
          v-if="EMediaType[curMediaTypeKey].pagetypeName"
          :min-width="100"
          :label="EMediaType[curMediaTypeKey].pagetypeName"
          prop="pagesortname"
          align="center"
        />
        <el-table-column
          v-if="EMediaType[curMediaTypeKey].addisplayformName"
          :min-width="100"
          :label="EMediaType[curMediaTypeKey].addisplayformName"
          prop="addisplayformname"
          align="center"
        />
        <el-table-column
          v-if="EMediaType[curMediaTypeKey].adspecName"
          :min-width="150"
          :label="EMediaType[curMediaTypeKey].adspecName"
          prop="adspecname"
          align="center"
        />
        <el-table-column
          v-if="EMediaType[curMediaTypeKey].adcolorName"
          :min-width="100"
          :label="EMediaType[curMediaTypeKey].adcolorName"
          prop="adcolorname"
          align="center"
        />
        <el-table-column
          v-if="curMediaTypeKey === 'paper'"
          :min-width="100"
          label="星期"
          prop="weeksettingname"
          align="center"
        />
        <el-table-column :min-width="100" label="价格" prop="baseprice" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.baseprice, '2') }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="dstartdate" label="开始日期" width="120">
          <template #default="scope">
            <span>{{ hgFormatDate(scope.row.dstartdate, 'YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="denddate" label="结束日期" width="120">
          <template #default="scope">
            <span>{{ hgFormatDate(scope.row.denddate, 'YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column :min-width="60" label="序号" prop="isort" align="center" />
        <el-table-column :min-width="100" label="备注" prop="sremark" align="center" />
        <el-table-column
          label="操作"
          width="80"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              link
              style="margin-left: 10px"
              type="success"
              icon="Document"
              size="small"
              title="查看详情"
              @click="onSelect(scope.row)"
              >查看</el-button
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
</template>
<script setup lang="ts">
import { EHgSingleZtreeUrl, EMediaType } from '@/types/common/enumindex'
import { tableHeaderColor, hgFormatDate, formatMoney } from '@/utils'
import { TPriceItemDTO, TPriceItemQuery, TPriceItemVO } from '@/types/views/price/priceitem'
import { IAxios } from 'axios'
import { getPriceItemPageListApi } from '@/api/price/priceitem'
import { ElTable, FormInstance, FormRules } from 'element-plus'
import dayjs from 'dayjs'

defineOptions({
  name: 'PriceQuery'
})

const dataTableRef = ref<InstanceType<typeof ElTable>>()
/** 当前媒体类型 */
const curMediaTypeKey = ref('paper')
/** 叠次版本树查询参数 */
const areavertreeparams = ref<{ foldId: string }>({
  foldId: ''
})
/** 版面类别树查询参数 */
const pagesortparams = ref<{ mediaType?: string; foldId?: string }>({
  mediaType: '',
  foldId: ''
})
/** 批量复制窗口显示状态 */
const dialogCopyVisible = ref(false)
/** 叠次选中id */
const foldselectid = ref('')
/** 广告规格树查询参数 */
const adspecparams = ref<{ mediaId?: string }>()
/** 每页选择下拉 */
const pageSizes = ref<number[]>([40, 60, 80, 100])
const total = ref(0)
/** 表格高度 */
const tableheight = ref(520)
/** 列表查询参数 */
const queryParams = ref<TPriceItemQuery>({
  page: 1,
  pageSize: 100,
  startTime: '',
  endTime: '',
  queryKey: ''
})
/** 列表数据 */
const priceItemList = ref<TPriceItemVO[]>([])
/** 列表多选数据 */
const multipleSelection = ref<TPriceItemVO[]>([])
/** Form名称 */
const priceItemRef = ref<FormInstance>()
const priceItemData = {
  baseprice: 0,
  foldid: '',
  foldareaid: '',
  pagesortid: '',
  adcolorid: '',
  adspecid: '',
  addisplayformid: '',
  weeksettingid: '',
  dstartdate: dayjs().format('YYYY-MM-DD'),
  denddate: '',
  sremark: '',
  buse: true,
  mediaid: '',
  mediatypekey: curMediaTypeKey.value
}
const priceItemForm = ref<TPriceItemDTO>({ ...priceItemData })

/** 叠次树返回 叠次版本根据叠次查询，版面类别根据类型和叠次查询，规格根据媒体查询*/
const onChange = (data: ITreeNode) => {
  if (data === undefined) {
    return
  }
  const nodes: ITreeNode[] = data.getPath!()
  if (curMediaTypeKey.value !== nodes[0].skey) {
    priceItemForm.value = { ...priceItemData }
    priceItemForm.value.mediatypekey = nodes[0].skey
    handleQuery()
  }
  curMediaTypeKey.value = nodes[0].skey
  priceItemForm.value.mediatypekey = curMediaTypeKey.value
  if (data.stype === 'mediatype') {
    pagesortparams.value = {
      mediaType: data.skey,
      foldId: ''
    }
    areavertreeparams.value = {
      foldId: data.id
    }
    priceItemForm.value.mediaid = ''
    priceItemForm.value.foldid = ''
  }
  if (data.stype === 'media') {
    pagesortparams.value = {
      mediaType: curMediaTypeKey.value,
      foldId: ''
    }
    areavertreeparams.value = {
      foldId: data.id
    }
    adspecparams.value = {
      mediaId: data.id
    }
    priceItemForm.value.mediaid = data.id
    priceItemForm.value.foldid = ''
  }
  if (data.stype === 'fold') {
    areavertreeparams.value = {
      foldId: data.id
    }
    pagesortparams.value = {
      mediaType: curMediaTypeKey.value,
      foldId: data.id
    }
    const parentNode = data.getParentNode!()
    adspecparams.value = {
      mediaId: parentNode?.id
    }
    priceItemForm.value.mediaid = parentNode?.id ?? ''
    priceItemForm.value.foldid = data.id
  }
}
/** 叠次树取消选择 */
const onCancel = () => {
  foldselectid.value = ''
  areavertreeparams.value = {
    foldId: ''
  }
  pagesortparams.value = {
    mediaType: curMediaTypeKey.value,
    foldId: ''
  }
  adspecparams.value = {
    mediaId: ''
  }
  priceItemForm.value.mediaid = ''
  priceItemForm.value.foldid = ''
}
const onCancelAll = () => {
  foldselectid.value = ''
  areavertreeparams.value = {
    foldId: ''
  }
  pagesortparams.value = {
    mediaType: curMediaTypeKey.value,
    foldId: ''
  }
  adspecparams.value = {
    mediaId: ''
  }
  priceItemForm.value = { ...priceItemData }
}
/** 叠次版本树返回 */
const onAreaverChange = (data: ITreeNode) => {
  priceItemForm.value.foldareaid = data?.id ?? ''
}
/** 版面类别树返回 */
const onPagetypeChange = (data: ITreeNode) => {
  priceItemForm.value.pagesortid = data?.id ?? ''
}
/** 广告形式树返回 */
const onAddisplayChange = (data: ITreeNode) => {
  priceItemForm.value.addisplayformid = data?.id ?? ''
}
/** 规格树返回 */
const onAdspecChange = (data: ITreeNode) => {
  priceItemForm.value.adspecid = data?.id ?? ''
}
/** 色彩树返回 */
const onAdcolorChange = (data: ITreeNode) => {
  priceItemForm.value.adcolorid = data?.id ?? ''
}
/** 星期选择返回 */
const onWeeksettingChange = (data: ITreeNode) => {
  priceItemForm.value.weeksettingid = data?.id ?? ''
}

/**
 * 价格明细列表查询
 */
const handleQuery = () => {
  const data = {
    ...queryParams.value,
    ...priceItemForm.value
  }
  getPriceItemPageListApi(data).then((res: IAxios<TPriceItemVO[]>) => {
    if (res.success) {
      priceItemList.value = res.obj
      total.value = res.total
    }
  })
}
/**
 * 选中按钮事件
 */
const onSelect = (row: TPriceItemVO) => {
  foldselectid.value = row.foldid ?? ''
  setTimeout(() => {
    priceItemForm.value = { ...row } as TPriceItemDTO
    dataTableRef.value!.clearSelection()
    dataTableRef.value!.toggleRowSelection(row, true)
  }, 50)
}
/**
 * 行选择事件
 * @param val
 */
const handleSelectionChange = (val: TPriceItemVO[]) => {
  multipleSelection.value = val
}
/**
 * 排序
 * @param val
 */
const handleSortChange = (val: { prop: string; order: string }) => {
  queryParams.value.sort = val.prop

  if (val.order === 'ascending') {
    queryParams.value.order = 'asc'
  } else if (val.order === 'descending') {
    queryParams.value.order = 'desc'
  } else {
    queryParams.value.order = ''
  }
  handleQuery()
}
/**
 * 改变页码总数时
 */
const handleSizeChange = (val: number) => {
  queryParams.value.pageSize = val
  handleQuery()
}
/**
 * 改变页数时
 * @param val
 */
const handleCurrentChange = (val: number) => {
  queryParams.value.page = val
  handleQuery()
}
</script>
<style scoped lang="scss">
.cardbox {
  width: 220px;
}
.el-form--inline .el-form-item {
  margin: 5px;
}
</style>
