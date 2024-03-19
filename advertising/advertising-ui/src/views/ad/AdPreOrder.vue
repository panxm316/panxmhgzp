<!--
 * @Author: wangxk
 * @Date: 2023-11-30 08:52:02
 * @LastEditTime: 2024-03-06 10:04:22
 * @LastEditors: wanghl
 * @Description: 广告预约
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex" style="width: 94%">
        <el-button type="primary" icon="Plus" @click="() => form.init(Mode.Add, '', true)">
          新增
        </el-button>
        <el-button type="danger" icon="Delete" @click="handleDelete()">删除</el-button>
        <HgDateIndex :initdate="initDate" @onresponse="onChangeDate"></HgDateIndex>
        <el-input
          v-model="queryInfo.queryKey"
          placeholder="请输入客户或者标题关键字"
          clearable
          style="width: 200px"
          @keyup.enter="loadData"
          @clear="loadData"
        />
        <el-button type="primary" icon="Search" @click="loadData">搜索</el-button>
        <hg-help :path="'PreOrder'" />
      </div>
    </div>

    <hg-table
      ref="hgTableRef"
      :data-list="dataList"
      :query-info="queryInfo"
      :page-total="pageTotal"
      :selection-change="(rows: Tworder[]) => selectedRows = rows"
      :load-data="loadData"
    >
      <el-table-column type="selection" prop="isCheck" :width="50" align="center" />
      <!-- <el-table-column
        prop="adprojectname"
        label="广告项目"
        sortable="custom"
        min-width="200"
        show-overflow-tooltip
      >
      </el-table-column> -->
      <el-table-column prop="sadtitle" label="广告标题" min-width="200" show-overflow-tooltip />
      <el-table-column
        prop="sordernum"
        label="订单编号"
        sortable="custom"
        min-width="120"
        show-overflow-tooltip
      />
      <el-table-column prop="businessentityname" label="经营主体" min-width="150" />
      <el-table-column prop="createtime" label="创建时间" sortable="custom" min-width="120">
        <template #default="scope">{{ scope.row.createtime.substring(0, 10) }}</template>
      </el-table-column>
      <el-table-column prop="iapprovestatus" label="订单状态" min-width="80" align="center">
        <template #default="scope">
          <span v-if="scope.row.ipreapprovestatus === 0"><el-tag>待审</el-tag></span>
          <span v-if="scope.row.ipreapprovestatus === 1"><el-tag type="warning">在审</el-tag></span>
          <span v-if="scope.row.ipreapprovestatus === 2"><el-tag type="success">通过</el-tag></span>
          <span v-if="scope.row.ipreapprovestatus === 3"><el-tag type="danger">否决</el-tag></span>
          <span v-if="scope.row.ipreapprovestatus === 4"><el-tag type="warning">撤销</el-tag></span>
          <span v-if="scope.row.ipreapprovestatus === 5"><el-tag type="danger">无效</el-tag></span>
        </template>
      </el-table-column>
      <el-table-column
        prop="customername"
        label="客户名称"
        sortable="custom"
        min-width="150"
        show-overflow-tooltip
      />
      <el-table-column
        prop="employname"
        label="业务员"
        sortable="custom"
        min-width="90"
        align="center"
      />
      <el-table-column
        prop="agencyname"
        label="代理公司"
        sortable="custom"
        min-width="140"
        align="center"
      />
      <el-table-column
        prop="agencyemployname"
        label="代理公司业务员"
        min-width="140"
        align="center"
      />
      <el-table-column
        prop="agentname"
        label="内容生产方"
        sortable="custom"
        min-width="140"
        align="center"
      />
      <el-table-column
        prop="agentemployname"
        label="内容生产方业务员"
        min-width="140"
        align="center"
      />
      <el-table-column prop="createempname" label="创建者" sortable="custom" min-width="100" />
      <el-table-column
        label="操作"
        width="240"
        class-name="small-padding fixed-width"
        fixed="right"
      >
        <template #default="i">
          <div style="padding-left: 10px">
            <!--  1-在审 2-通过   -->
            <!--  仅限创建者修改   -->
            <el-button
              v-if="
                i.row.ipreapprovestatus !== 1 &&
                i.row.ipreapprovestatus !== 2 &&
                i.row.createempid === userStore.user?.userid
              "
              link
              type="success"
              icon="Edit"
              size="small"
              @click="(_: any) => form.init(Mode.Edit, i.row.id,true)"
            >
              修改
            </el-button>
            <!-- v-if=" -->
            <el-button
              v-if="
                props.bookType === 2 &&
                i.row.ipreapprovestatus == 2 &&
                i.row.createempid === userStore.user?.userid
              "
              link
              :disabled="i.row.relateorderid !== null"
              type="primary"
              icon="Plus"
              size="small"
              @click="showbukan(i.row.id)"
            >
              补刊
            </el-button>
            <el-button
              v-if="
                ((i.row.ipreapprovestatus == 1 || i.row.ipreapprovestatus == 2) &&
                  i.row.createempid === userStore.user?.userid) ||
                userStore.user?.blead
              "
              link
              type="primary"
              icon="View"
              size="small"
              @click="(_: any) => form.init(Mode.View, i.row.id,false)"
            >
              详情
            </el-button>

            <el-button
              v-if="
                i.row.ipreapprovestatus !== 1 &&
                i.row.ipreapprovestatus !== 2 &&
                i.row.createempid === userStore.user?.userid
              "
              link
              type="primary"
              icon="Top"
              size="small"
              @click="handleSubmit(i.row)"
            >
              提交
            </el-button>
            <el-button
              v-if="i.row.ipreapprovestatus === 1 && userStore.user?.blead"
              link
              type="primary"
              icon="Check"
              size="small"
              @click="() => approvalDialog.init(i.row.id)"
            >
              审核
            </el-button>
            <el-button
              v-if="
                i.row.ipreapprovestatus !== 1 &&
                i.row.ipreapprovestatus !== 2 &&
                i.row.createempid === userStore.user?.userid
              "
              link
              type="danger"
              icon="Delete"
              size="small"
              @click="handleDelete(i.row)"
            >
              删除
            </el-button>
          </div>
        </template>
      </el-table-column>
    </hg-table>
    <pre-order-add-edit ref="form" :book-type="props.bookType" @onCloseDialog="() => loadData()" />
    <pre-order-approval-dialog
      ref="approvalDialog"
      :book-type="props.bookType"
      @onCloseDialog="() => loadData()"
    />
    <!-- 订单预定 -->
    <el-dialog
      v-model="dialogVisible"
      title="编辑订单"
      align-center
      :close-on-click-modal="false"
      :width="fenbianlv() > 1440 ? '80%' : '94%'"
      @close="handleCancel"
    >
      <!-- /* 编辑 */ -->
      <AdTworder
        style="width: 100%"
        :data="WorderId"
        :redetdata="true"
        :bottomshow="true"
        :isbu="3"
        @closedialogVisible="closedialogVisible"
      ></AdTworder>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computTreeHeight, fenbianlv } from '@/utils/index'
import type { AxiosResponse, IAxios } from 'axios'
import modal from '@/plugins/modal'
import useUserStore from '@/store/modules/user'
import { useRouter } from 'vue-router'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { getInitDateCombo } from '@/utils/getdate'
import { ref } from 'vue'
import { initPreOrderQueryDTO, PreOrderQueryDTO } from '@/types/views/ad/pre-order-query-dto'
import PreOrderAddEdit from '@/views/ad/AdPreOrderAddEdit.vue'
import PreOrderApprovalDialog from '@/views/ad/AdPreOrderApprovalDialog.vue'
import { Mode } from '@/types/views/ad/mode'
import { getPreOrderPageListApi } from '@/api/ad/adpreorder'
import { Tworder } from '@/types/views/ad/adtworder'
import { PreOrder } from '@/types/views/ad/pre-order'
import { deleteorderApi, submitPreOrderApi } from '@/api/ad/adtworder'
import dayjs from 'dayjs'
import AdTworder from './AdTworder.vue'
defineOptions({
  name: 'PreOrderView'
})
const props = withDefaults(
  defineProps<{
    bookType: number
  }>(),
  {
    bookType: 1 // 默认 1-广告预约, 快速预约传入2
  }
)

onMounted(() => {
  treeHeight.value = computTreeHeight()
})

const userStore = useUserStore()
const router = useRouter()
const global = getCurrentInstance()?.appContext.config.globalProperties
const form = ref()
const approvalDialog = ref()
/** 树高度 */
const treeHeight = ref('')
/** 编辑 dialog */
const dialogVisible = ref(false)
/** 订单id */
const WorderId = ref('')
/** 订单列表 */
const dataList = ref<Tworder[]>([])
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 数据总条数 */
const pageTotal = ref<number>(0)
/** 时间 */
const initDate = reactive<TDateType>(getInitDateCombo('30'))
const selectedRows = ref<Tworder[]>()
/**
 * 时间选择
 */
const onChangeDate = (val: TDateTimeType) => {
  queryInfo.startTime = dayjs(val.startTime).startOf('day').format('YYYY-MM-DD HH:mm:ss')
  queryInfo.endTime = dayjs(val.endTime).endOf('day').format('YYYY-MM-DD HH:mm:ss')
  if (queryInfo.startTime && queryInfo.endTime) {
    loadData()
  }
}

const queryInfo = reactive<PreOrderQueryDTO>(initPreOrderQueryDTO(props.bookType))

/** 获取订单列表 */
const loadData = () => {
  getPreOrderPageListApi(queryInfo).then(({ obj, total }: IAxios<Tworder[]>) => {
    pageTotal.value = total
    dataList.value = obj
  })
}

/** 提交审核 */
const handleSubmit = (row: Tworder) => {
  modal.confirm('提交后将不能进行修改, 是否提交审核?').then(() => {
    submitPreOrderApi(row.id!).then(({ success, msg }: AxiosResponse) => {
      if (success) {
        modal.msgSuccess('操作成功')
      } else {
        modal.msgError(msg)
      }
      loadData()
    })
  })
}

/** 删除 */
const handleDelete = (row?: Tworder) => {
  modal.confirm('是否删除?').then(() => {
    const ids = []
    if (row) {
      ids.push(row.id!)
    } else {
      selectedRows.value?.forEach((item) => ids.push(item.id!))
    }
    deleteorderApi(ids.join(',')).then(({ success, msg }: IAxios) => {
      if (success) {
        modal.msgSuccess('操作成功')
      } else {
        modal.msgError(msg)
      }
      loadData()
    })
  })
}
/**
 * 补刊
 */
const showbukan = (id: string) => {
  dialogVisible.value = true
  WorderId.value = id
}
/**
 * 取消
 */
const handleCancel = () => {
  dialogVisible.value = false
}
/**
 * 保存信息关闭订单弹窗
 */
const closedialogVisible = (val: string) => {
  loadData()
  dialogVisible.value = false
}
</script>

<style scoped>
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
