<!--
自定义表格 HgTable (包含 el-table 和 el-pagination)
<hg-table
  ref="hgTableRef"
  :data-list="customerList" 数据集合
  :query-info="queryInfo" 查询参数(包含page和pageSize)
  :page-total="pageTotal" 总数
  :selection-change="handleSelectionChange" 多选
  :load-data="loadData" 加载数据函数
>
这里是原来的 <el-table-column />
</hg-table>

注意:
1. 传入的 loadData 函数会在此表格 mounted 时调用,所以不需要在父组件的 mounted 时单独调用
2. 修改表格高度后,在父组件中调用 reRender 函数,调用方法请参考 reRender()
-->
<template>
  <div class="table_box">
    <el-row>
      <el-table
        :data="dataList"
        row-key="id"
        :height="tableHeight"
        :border="true"
        stripe
        :header-cell-style="tableHeaderColor"
        @selection-change="handleSelectionChange"
        @sort-change="(val) => handleSortChange(val, queryInfo, loadData)"
      >
        <slot></slot>
      </el-table>
    </el-row>
    <el-row v-if="!hidePagination">
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
      />
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { defineProps, onMounted, ref } from 'vue'

import { handleSortChange } from '@/utils/table'
import { computTableHeight, tableHeaderColor } from '@/utils'

const props = defineProps({
  dataList: {
    required: true,
    type: Array,
    default: () => []
  },
  queryInfo: {
    required: true,
    default: {
      page: 1,
      pageSize: 20
    }
  },
  pageTotal: {
    required: true,
    type: Number,
    default: 1
  },
  loadData: {
    required: true,
    type: Function
  },
  selectionChange: {
    required: true,
    type: Function
  },
  hidePagination: {
    required: false,
    type: Boolean,
    default: false
  },
  height: {
    required: false,
    type: Number,
    default: undefined
  }
})

const tableHeight = ref(props.height || 0)
const pageSizes = ref<number[]>([10, 20, 30, 40])

onMounted(() => {
  // console.log('HgTable mounted', props.height)
  tableHeight.value = props.height || computTableHeight()
  props.loadData()
})

watchEffect(() => {
  // console.log('HgTable wEffect', props.height)
  tableHeight.value = props.height || computTableHeight()
})

/**
 * 重新渲染
 * 1. <hg-table ref="hgTableRef"></hg-table>
 * 2. const hgTableRef = ref<any>()
 * 3. hgTableRef.value.reRender()
 */
const reRender = () => {
  tableHeight.value = props.height || computTableHeight()
}

/**
 * 选择
 */
const handleSelectionChange = (rows: any[]) => {
  props.selectionChange(rows)
}

/**
 * 改变页码总数时的操作
 */
const handleSizeChange = (val: number) => {
  props.queryInfo.pageSize = val
  props.loadData()
}

/**
 * 改变页数时的操作
 */
const handleCurrentChange = (val: number) => {
  props.queryInfo.page = val
  props.loadData()
}

defineExpose({
  reRender
})
</script>

<style scoped lang="scss">
ul {
  padding-left: 10px;
}

.left_box {
  height: v-bind(treeHeight);
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

.box-card {
  width: 280px;
  max-height: 600px;
  overflow-y: auto;
}
</style>
