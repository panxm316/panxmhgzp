<!--
 * @Author: caogd
 * @Date: 2023-11-11 13:12:28
 * @LastEditTime: 2024-01-17 14:57:42
 * @LastEditors: wanghl
 * @Description: 价格设置详情
-->
<template>
  <div>
    <el-space wrap :size="5">
      <div>
        <!-- 叠次 -->
        <el-card class="cardbox">
          <div>
            <span>{{ EMediaType[params.mediatypekey].foldName }}</span>
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
            :treeparams="{ mediaType: props.params.mediatypekey }"
            @change="onChange"
          ></HgSingleZtree>
        </el-card>
      </div>
      <div v-if="EMediaType[params.mediatypekey].foldareaverName">
        <!-- 叠次版本 -->
        <el-card class="cardbox">
          <div>
            <span>{{ EMediaType[params.mediatypekey].foldareaverName }}</span>
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
      <div v-if="EMediaType[params.mediatypekey].pagetypeName">
        <!-- 版本类别 -->
        <el-card class="cardbox">
          <div>
            <span>{{ EMediaType[params.mediatypekey].pagetypeName }}</span>
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
      <div v-if="EMediaType[params.mediatypekey].addisplayformName">
        <!-- 广告形式 -->
        <el-card class="cardbox">
          <div>
            <span>{{ EMediaType[params.mediatypekey].addisplayformName }}</span>
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
            :treeparams="{ mediaType: props.params.mediatypekey }"
            @change="onAddisplayChange"
          ></HgSingleZtree>
        </el-card>
      </div>
      <div v-if="EMediaType[params.mediatypekey].adspecName">
        <!-- 广告规格 -->
        <el-card class="cardbox">
          <div>
            <span>{{ EMediaType[params.mediatypekey].adspecName }}</span>
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
      <div v-if="EMediaType[params.mediatypekey].adcolorName">
        <!-- 色彩 -->
        <el-card class="cardbox">
          <div>
            <span>{{ EMediaType[params.mediatypekey].adcolorName }}</span>
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
            :treeheight="params.mediatypekey === 'paper' ? 122 : 300"
            :treeparams="{ mediaType: props.params.mediatypekey }"
            @change="onAdcolorChange"
          ></HgSingleZtree>
        </el-card>
        <el-card v-if="params.mediatypekey === 'paper'" class="cardbox">
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
      <el-form ref="priceItemRef" :inline="true" :model="priceItemForm" :rules="rules">
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
        <!-- <el-form-item label="序号" prop="isort">
          <el-input-number
            v-model="priceItemForm.isort"
            :min="1"
            :max="99999"
            controls-position="right"
            style="width: 100px"
          />
        </el-form-item> -->
        <!-- <el-form-item label="有效">
          <el-checkbox v-model="priceItemForm.buse"></el-checkbox>
        </el-form-item> -->
        <el-form-item label="备注" prop="sremark">
          <el-input v-model="priceItemForm.sremark" :maxlength="200" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="Plus" @click="handleAdd(priceItemRef)">新增</el-button>
          <el-button type="success" icon="Edit" @click="onEdit(priceItemRef)">修改</el-button>
          <el-button type="primary" icon="DocumentCopy" @click="onCopy(undefined)"
            >批量修改</el-button
          >
          <el-button type="danger" icon="Delete" @click="onDelete()">删除</el-button>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        </el-form-item>
      </el-form>

      <el-dialog
        v-model="dialogCopyVisible"
        title="批量修改"
        style="width: 860px"
        append-to-body
        :close-on-click-modal="false"
        :destroy-on-close="true"
      >
        <div>
          <el-space>
            <!-- <span>开始日期</span>
            <el-date-picker
              v-model="priceItemForm.dstartdate"
              type="date"
              placeholder="开始日期"
              value-format="YYYY-MM-DD"
              :clearable="false"
              style="width: 150px"
            /> -->

            <span>结束日期</span>
            <el-date-picker
              v-model="priceItemForm.denddate"
              type="date"
              placeholder="结束日期"
              value-format="YYYY-MM-DD"
              style="width: 150px"
            />
          </el-space>
        </div>
        <template #footer>
          <div class="dialog-footer">
            <el-button type="primary" @click="onSaveCopy">修 改</el-button>
            <el-button @click="dialogCopyVisible = false">取 消</el-button>
          </div>
        </template>
      </el-dialog>
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
          :label="EMediaType[params.mediatypekey].foldName"
          prop="foldname"
          align="center"
        >
          <template #default="scope">
            <span>{{ scope.row.foldname || scope.row.medianame }}</span>
          </template>
        </el-table-column>
        <el-table-column
          v-if="EMediaType[params.mediatypekey].foldareaverName"
          :min-width="100"
          :label="EMediaType[params.mediatypekey].foldareaverName"
          prop="foldareaname"
          align="center"
          show-overflow-tooltip
        />
        <el-table-column
          v-if="EMediaType[params.mediatypekey].pagetypeName"
          :min-width="100"
          :label="EMediaType[params.mediatypekey].pagetypeName"
          prop="pagesortname"
          align="center"
          show-overflow-tooltip
        />
        <el-table-column
          v-if="EMediaType[params.mediatypekey].addisplayformName"
          :min-width="120"
          :label="EMediaType[params.mediatypekey].addisplayformName"
          prop="addisplayformname"
          align="center"
          show-overflow-tooltip
        />
        <el-table-column
          v-if="EMediaType[params.mediatypekey].adspecName"
          :min-width="150"
          :label="EMediaType[params.mediatypekey].adspecName"
          prop="adspecname"
          align="center"
          show-overflow-tooltip
        />
        <el-table-column
          v-if="EMediaType[params.mediatypekey].adcolorName"
          :min-width="100"
          :label="EMediaType[params.mediatypekey].adcolorName"
          prop="adcolorname"
          align="center"
          show-overflow-tooltip
        />
        <el-table-column
          v-if="params.mediatypekey === 'paper'"
          :min-width="100"
          label="星期"
          prop="weeksettingname"
          align="center"
          show-overflow-tooltip
        />
        <el-table-column min-width="120" label="价格" prop="baseprice" align="right">
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
        <!-- <el-table-column prop="buse" label="启用" :width="60" align="center">
          <template #default="scope">
            <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
          </template>
        </el-table-column> -->
        <el-table-column
          label="操作"
          align="left"
          width="120"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              link
              style="margin-left: 10px"
              type="success"
              icon="Edit"
              size="small"
              title="选择"
              @click="onSelect(scope.row)"
              >选择</el-button
            >
            <!-- <el-button
              link
              style="margin-left: 10px"
              type="success"
              icon="DocumentCopy"
              size="small"
              title="复制"
              @click="onCopy(scope.row)"
              >复制</el-button
            > -->
            <el-button
              link
              type="danger"
              icon="Delete"
              size="small"
              title="删除"
              @click="onDelete(scope.row)"
              >删除</el-button
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
import { required, tableHeaderColor, hgFormatDate, formatMoney } from '@/utils'
import {
  TPriceItemCopy,
  TPriceItemDTO,
  TPriceItemQuery,
  TPriceItemVO
} from '@/types/views/price/priceitem'
import { IAxios } from 'axios'
import {
  getPriceItemPageListApi,
  savePriceItemApi,
  updatePriceItemApi,
  deletePriceItemApi,
  batchUpdatePriceItemApi
} from '@/api/price/priceitem'
import { ElTable, FormInstance, FormRules } from 'element-plus'
import dayjs from 'dayjs'

defineOptions({
  name: 'PriceItem'
})
type TPriceItemProps = {
  params: {
    /** 媒体类型key */
    mediatypekey: string
    /** 媒体类型名称 */
    mediatypename: string
    /** 价格组表id */
    pricegroupid: string
    /** 价格组表名称 */
    pricegroupname: string
  }
}
const props = defineProps<TPriceItemProps>()

const dataTableRef = ref<InstanceType<typeof ElTable>>()

/** 叠次版本树查询参数 */
const areavertreeparams = ref<{ foldId: string }>({
  foldId: ''
})
/** 版面类别树查询参数 */
const pagesortparams = ref<{ mediaType?: string; foldId?: string }>({
  mediaType: props.params.mediatypekey,
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
const tableheight = ref(400)
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
const rules = reactive<FormRules<TPriceItemDTO>>({
  baseprice: [{ required: true, message: '请填写标准价格', trigger: 'blur' }],
  dstartdate: [{ required: true, type: 'date', message: '请选择开始日期', trigger: 'change' }]
})
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
  medianame: '',
  mediatypekey: props.params.mediatypekey,
  mediatypename: props.params.mediatypename,
  pricegroupid: props.params.pricegroupid,
  pricegroupname: props.params.pricegroupname,
  isort: 1
}
const priceItemForm = ref<TPriceItemDTO>({ ...priceItemData })
onMounted(() => {
  // handleQuery()
})

/** 叠次树返回 叠次版本根据叠次查询，版面类别根据类型和叠次查询，规格根据媒体查询*/
const onChange = (data: ITreeNode) => {
  if (data === undefined) {
    return
  }
  if (data.stype === 'mediatype') {
    pagesortparams.value = {
      mediaType: props.params.mediatypekey,
      foldId: ''
    }
    priceItemForm.value.mediaid = ''
    priceItemForm.value.medianame = ''
    priceItemForm.value.foldid = ''
    priceItemForm.value.foldname = ''
  }
  if (data.stype === 'media') {
    adspecparams.value = {
      mediaId: data.id
    }
    priceItemForm.value.mediaid = data.id
    priceItemForm.value.medianame = data.name ?? ''
    priceItemForm.value.foldid = ''
    priceItemForm.value.foldname = ''
  }
  if (data.stype === 'fold') {
    areavertreeparams.value = {
      foldId: data.id
    }
    pagesortparams.value = {
      mediaType: props.params.mediatypekey,
      foldId: data.id
    }
    const parentNode = data.getParentNode!()
    adspecparams.value = {
      mediaId: parentNode?.id
    }
    priceItemForm.value.mediaid = parentNode?.id ?? ''
    priceItemForm.value.medianame = parentNode?.name ?? ''
    priceItemForm.value.foldid = data.id
    priceItemForm.value.foldname = data.name
  }
}
/** 叠次树取消选择 */
const onCancel = () => {
  foldselectid.value = ''
  areavertreeparams.value = {
    foldId: ''
  }
  pagesortparams.value = {
    mediaType: props.params.mediatypekey,
    foldId: ''
  }
  adspecparams.value = {
    mediaId: ''
  }
  priceItemForm.value.mediaid = ''
  priceItemForm.value.medianame = ''
  priceItemForm.value.foldid = ''
  priceItemForm.value.foldname = ''
}
/** 叠次版本树返回 */
const onAreaverChange = (data: ITreeNode) => {
  priceItemForm.value.foldareaid = data?.id ?? ''
  priceItemForm.value.foldareaname = data?.name ?? ''
}
/** 版面类别树返回 */
const onPagetypeChange = (data: ITreeNode) => {
  priceItemForm.value.pagesortid = data?.id ?? ''
  priceItemForm.value.pagesortname = data?.name ?? ''
}
/** 广告形式树返回 */
const onAddisplayChange = (data: ITreeNode) => {
  priceItemForm.value.addisplayformid = data?.id ?? ''
  priceItemForm.value.addisplayformname = data?.name ?? ''
}
/** 规格树返回 */
const onAdspecChange = (data: ITreeNode) => {
  priceItemForm.value.adspecid = data?.id ?? ''
  priceItemForm.value.adspecname = data?.name ?? ''
}
/** 色彩树返回 */
const onAdcolorChange = (data: ITreeNode) => {
  console.log(priceItemForm.value)
  priceItemForm.value.adcolorid = data?.id ?? ''
  priceItemForm.value.adcolorname = data?.name ?? ''
}
/** 星期选择返回 */
const onWeeksettingChange = (data: ITreeNode) => {
  priceItemForm.value.weeksettingid = data?.id ?? ''
  priceItemForm.value.weeksettingname = data?.name ?? ''
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
/** 新增详情 */
const handleAdd = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid, fields) => {
    if (valid) {
      const validform = validFormSubmit()
      if (!validform) {
        return
      }
      ElMessageBox.confirm('是否保存新增？', '提示', {
        type: 'warning'
      })
        .then(() => {
          priceItemForm.value.id = ''
          const data = { ...priceItemForm.value }
          data.id = ''
          savePriceItemApi(data).then((res: IAxios) => {
            if (res.success) {
              ElMessage.success('新增成功')
              // handleQuery()
              priceItemList.value.push(res.obj)
              total.value = total.value + 1
            }
          })
        })
        .catch(() => {})
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
 * 复制按钮事件
 * @param row
 */
const onCopy = (row?: TPriceItemVO) => {
  if (row !== undefined) {
    priceItemForm.value = { ...row } as TPriceItemDTO
    foldselectid.value = priceItemForm.value.foldid ?? ''
    priceItemForm.value.id = ''
  } else {
    if (multipleSelection.value.length === 0) {
      ElMessage.warning('请选择要复制的数据')
      return
    }
    dialogCopyVisible.value = true
  }
}
/**
 * 批量复制保存事件
 */
const onSaveCopy = () => {
  // if (!priceItemForm.value.dstartdate) {
  //   ElMessage.error('请选择开始日期')
  //   return
  // }
  if (!priceItemForm.value.denddate) {
    ElMessage.error('请选择结束日期')
    return
  }

  const ids: string[] = []
  multipleSelection.value.forEach((item) => {
    if (item.id) {
      if (dayjs(item.dstartdate).isAfter(dayjs(priceItemForm.value.denddate))) {
        ElMessage.error('开始日期不能大于结束日期')
        return
      }
      ids.push(item.id)
    }
  })
  ElMessageBox.confirm('是否保存修改？', '提示', {
    type: 'warning'
  })
    .then(() => {
      const data: TPriceItemCopy = {
        ids: ids.join(','),
        endTime: priceItemForm.value.denddate
      }
      batchUpdatePriceItemApi(data).then(({ success }: IAxios) => {
        if (success) {
          ElMessage.success('修改成功')
          dialogCopyVisible.value = false
          priceItemList.value.forEach((item) => {
            if (ids.includes(item.id!)) {
              item.denddate = priceItemForm.value.denddate
            }
          })
        }
      })
    })
    .catch(() => {})
}
/** 提交校验 */
const validFormSubmit = () => {
  if (!priceItemForm.value.mediaid) {
    ElMessage.error('请选择媒体')
    return false
  }
  if (!priceItemForm.value.foldid && props.params.mediatypekey !== 'wei') {
    ElMessage.error(`请选择${EMediaType[props.params.mediatypekey].foldName}`)
    return false
  }
  if (!priceItemForm.value.foldareaid && EMediaType[props.params.mediatypekey].foldareaverName) {
    ElMessage.error(`请选择${EMediaType[props.params.mediatypekey].foldareaverName}`)
    return false
  }
  if (!priceItemForm.value.pagesortid && EMediaType[props.params.mediatypekey].pagetypeName) {
    ElMessage.error(`请选择${EMediaType[props.params.mediatypekey].pagetypeName}`)
    return false
  }
  if (
    !priceItemForm.value.addisplayformid &&
    EMediaType[props.params.mediatypekey].addisplayformName
  ) {
    ElMessage.error(`请选择${EMediaType[props.params.mediatypekey].addisplayformName}`)
    return false
  }
  if (!priceItemForm.value.adspecid && EMediaType[props.params.mediatypekey].adspecName) {
    ElMessage.error(`请选择${EMediaType[props.params.mediatypekey].adspecName}`)
    return false
  }
  if (!priceItemForm.value.adcolorid && EMediaType[props.params.mediatypekey].adcolorName) {
    ElMessage.error(`请选择${EMediaType[props.params.mediatypekey].adcolorName}`)
    return false
  }
  if (!priceItemForm.value.weeksettingid && props.params.mediatypekey === 'paper') {
    ElMessage.error('请选择星期')
    return false
  }
  return true
}
/**
 * 编辑按钮事件
 * @param row
 */
const onEdit = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid, fields) => {
    if (valid) {
      const validform = validFormSubmit()
      if (!validform) {
        return
      }
      if (!priceItemForm.value.id) {
        ElMessage.error('修改数据id不能为空')
        return
      }
      ElMessageBox.confirm('是否保存修改？', '提示', {
        type: 'warning'
      })
        .then(() => {
          const data = { ...priceItemForm.value }
          updatePriceItemApi(data).then(({ success }: IAxios) => {
            if (success) {
              ElMessage.success('修改成功')
              priceItemList.value.forEach((item) => {
                if (item.id === priceItemForm.value.id) {
                  Object.assign(item, priceItemForm.value)
                }
              })
            }
          })
        })
        .catch(() => {})
    }
  })
}
/**
 * 删除按钮事件
 * @param row
 */
const onDelete = (row?: TPriceItemVO) => {
  const ids: string[] = []
  if (row === undefined) {
    multipleSelection.value.forEach((item) => {
      if (item.id) {
        ids.push(item.id)
      }
    })
  } else {
    if (row.id) {
      ids.push(row.id)
    }
  }

  if (ids.length === 0) {
    ElMessage.warning('请选择要删除的数据')
    return
  }
  ElMessageBox.confirm('是否删除选择的数据？', '提示', {
    type: 'warning'
  })
    .then(() => {
      deletePriceItemApi(ids.join(','))
        .then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('删除成功!')
            handleQuery()
          }
        })
        .catch(() => {})
    })
    .catch(() => {})
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
  width: 180px;
}
.el-form--inline .el-form-item {
  margin: 5px;
}
</style>
