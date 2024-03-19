<!--
 * @Author: wanghl
 * @Date: 2024-03-08 10:18:22
 * @LastEditTime: 2024-03-08 14:32:09
 * @LastEditors: wanghl
 * @Description: 常用审批意见
-->

<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
        <el-button type="danger" icon="Delete" @click="handleDelete()">删除</el-button>
        <el-select
          v-model="queryInfo.sflowtype"
          placeholder="流程设置类型"
          style="width: 160px"
          @change="getopinionsList"
          clearable
          @clear="getopinionsList"
        >
          <el-option
            v-for="item in FlowTypeCombo"
            :key="item.id"
            :label="item.sname"
            :value="item.skey"
          />
        </el-select>
        <el-select
          v-model="queryInfo.ipasstype"
          placeholder="审核类型"
          @change="getopinionsList"
          clearable
          @clear="getopinionsList"
          style="width: 160px"
        >
          <el-option :value="0" :label="'拒绝'">拒绝</el-option>
          <el-option :value="1" :label="'同意'">同意</el-option>
        </el-select>
        <el-input
          v-model="queryInfo.queryKey"
          placeholder="请输入关键字"
          clearable
          style="width: 200px"
          @clear="getopinionsList"
          @keyup.enter="getopinionsList"
        />
        <el-button type="primary" icon="Search" @click="getopinionsList">搜索</el-button>
        <el-tooltip class="item" effect="dark" content="帮助" placement="top">
          <el-button class="help" link @click="showhelp()">?</el-button>
        </el-tooltip>
      </div>
    </div>
    <div class="table_box">
      <el-table
        :data="opinionsList"
        row-key="id"
        :height="tableheight"
        :border="true"
        stripe
        :header-cell-style="tableHeaderColor"
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <el-table-column type="selection" prop="ischeck" width="50" align="center">
        </el-table-column>
        <el-table-column
          prop="sflowtypename"
          label="流程设置类型"
          sortable="custom"
          width="160"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="sopinion"
          label="内容"
          sortable="custom"
          min-width="200"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column prop="createempname" label="创建者" width="200" show-overflow-tooltip>
        </el-table-column>
        <el-table-column prop="ipasstype" label="审核类型" :width="120" align="center">
          <template #default="scope">
            <span v-if="scope.row.ipasstype === 0">拒绝</span>
            <span v-if="scope.row.ipasstype === 1">同意</span>
          </template>
        </el-table-column>
        <el-table-column prop="bcommon" label="是否通用" :width="120" align="center">
          <template #default="scope">
            <el-checkbox v-model="scope.row.bcommon" disabled></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column prop="buse" label="启用" :width="80" align="center">
          <template #default="scope">
            <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column prop="isort" label="序号" :width="80" sortable="custom" align="center">
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="160"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button link type="success" icon="Edit" size="small" @click="handleUpdate(scope.row)"
              >修改</el-button
            >
            <el-button
              link
              type="danger"
              icon="Delete"
              size="small"
              @click="handleDelete(scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
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
    </div>
    <el-dialog
      v-model="dialogVisible"
      title="编辑审批意见"
      width="700px"
      align-center
      :close-on-click-modal="false"
    >
      <el-form ref="opinionsFormRef" :model="opinions" label-width="140px" :rules="rules">
        <el-row :gutter="50">
          <el-col :span="22">
            <el-form-item label="流程设置类型" prop="mediatypekey">
              <el-select
                v-model="opinions.sflowtype"
                placeholder="流程设置类型"
                style="width: 500px"
              >
                <el-option
                  v-for="item in FlowTypeCombo"
                  :key="item.id"
                  :label="item.sname"
                  :value="item.skey"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="审批意见内容" prop="sname">
              <el-input
                v-model="opinions.sopinion"
                maxlength="200"
                style="width: 500px"
                placeholder="请输入审批意见"
                show-word-limit
                type="textarea"
              />
            </el-form-item>
            <el-form-item label="审核类型" prop="ipasstype">
              <el-select v-model="opinions.ipasstype" placeholder="审核类型">
                <el-option :value="0" :label="'拒绝'">拒绝</el-option>
                <el-option :value="1" :label="'同意'">同意</el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="序号" prop="isort">
              <el-input-number v-model="opinions.isort" :min="1" controls-position="right" />
            </el-form-item>
            <el-form-item label="是否启用" prop="buse">
              <el-checkbox v-model="opinions.buse" />
            </el-form-item>
            <el-form-item label="是否通用" prop="bcommon">
              <el-checkbox v-model="opinions.bcommon" />
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
  </div>
</template>

<script setup lang="ts">
import { FormRules, FormInstance } from 'element-plus'
import { getFlowTypeListApi } from '@/api/flow/index'
import {
  getOpinionsPageListApi,
  getOpinionsListApi,
  getopinionsByIdApi,
  deleteOpinionsApi,
  saveOpinionsApi,
  updateOpinionsApi,
  getMaxSortApi
} from '@/api/task/opinions'
import { TwOpinionsPageList, TwOpinionsList, TwsaveOpinions } from '@/types/views/task/opinions'
import { required, computTableHeight, tableHeaderColor } from '@/utils/index'
import type { IAxios } from 'axios'
import modal from '@/plugins/modal'
import { IDataCombo } from '@/types/common/DataCombo'

defineOptions({
  name: 'Opinions'
})
import { useRouter } from 'vue-router'
import useUserStore from '@/store/modules/user'
const user = useUserStore().user
const router = useRouter()
const pageTotal = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([15, 20, 30, 40])
/** 编辑 dialog */
const dialogVisible = ref(false)
/** 审批意见列表 */
const opinionsList = ref<TwsaveOpinions[]>([])
/** 审批意见 */
const opinions = ref<TwsaveOpinions>({
  id: '',
  createempid: user.userid,
  createempname: user.username,
  createdate: '',
  sopinion: '',
  sflowtype: '',
  bcommon: true,
  ipasstype: 0,
  isort: 0,
  buse: true
})
/** 查询对象 */
const queryInfo = ref<TwOpinionsPageList>({
  /**
   * 业务对象缓存key
   */
  cacheDTOKey: '',
  /**
   * 查询结束时间
   */
  endTime: '',
  /**
   * 是否用于同意 0-否决 1 -同意
   */
  ipasstype: '',
  loginUserId: user.userid,
  /**
   * asc/desc
   */
  order: 'desc',
  /**
   * 当前页
   */
  page: 1,
  /**
   * 每页显示记录数
   */
  pageSize: 20,
  /**
   * 查询关键字
   */
  queryKey: '',
  /**
   * 审批流程类型
   */
  sflowtype: '',
  /**
   * 排序字段
   */
  sort: 'isort',
  startTime: ''
})

const opinionsFormRef = ref<FormInstance>()
const opinionsSelection = ref<TwsaveOpinions[]>()
/** 流程设置类别下拉列表 */
const FlowTypeCombo = ref<any[]>()
/** 表格高度 */
const tableheight = ref(0)
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<TwsaveOpinions>>({
  sopinion: [
    { validator: required, required: true, message: '请输入名称', trigger: 'blur' },
    { max: 20, message: '不得大于20个字符', trigger: 'blur' }
  ],
  sflowtype: [{ required: true, message: '请选择流程设置类型', trigger: 'change' }]
})

onMounted(() => {
  getopinionsList()
  getFlowType()
  tableheight.value = computTableHeight()
})

/**
 * 选择
 * @param rows
 */
const handleSelectionChange = (rows: TwsaveOpinions[]) => {
  opinionsSelection.value = rows
}
/**
 * 排序
 * @param val
 */
const handleSortChange = (val: { prop: string; order: string }) => {
  queryInfo.value.sort = val.prop

  if (val.order === 'ascending') {
    queryInfo.value.order = 'asc'
  } else if (val.order === 'descending') {
    queryInfo.value.order = 'desc'
  } else {
    queryInfo.value.order = ''
  }
  getopinionsList()
}
/**
 * 获取流程类型列表
 */
const getFlowType = () => {
  getFlowTypeListApi().then((res: IAxios<IDataCombo[]>) => {
    if (res.success) {
      console.log(res.obj)
      FlowTypeCombo.value = res.obj
    }
  })
}
/**
 * 获取审批意见列表
 */
const getopinionsList = () => {
  const data = queryInfo.value
  getOpinionsPageListApi(data).then(({ obj, total }: IAxios<TwsaveOpinions[]>) => {
    console.log(obj)
    opinionsList.value = obj
    pageTotal.value = total
  })
}
/**
 * 获取序号最大值
 */
const getMaxSort = () => {
  getMaxSortApi().then(({ success, obj }: IAxios<number>) => {
    opinions.value.isort = obj
  })
}
/**
 * 新增
 */
const handleAdd = () => {
  opinions.value = {
    id: '',
    createempid: user.userid,
    createempname: user.username,
    createdate: '',
    sopinion: '',
    sflowtype: '',
    bcommon: true,
    ipasstype: 0,
    isort: 0,
    buse: true
  }
  getMaxSort()
  dialogVisible.value = true
}
/**
 * 修改
 * @param row
 */
const handleUpdate = (row: TwsaveOpinions) => {
  opinions.value = { ...row }
  dialogVisible.value = true
}
/**
 * 保存
 */
const handleSave = () => {
  opinionsFormRef.value?.validate((valid) => {
    if (valid) {
      if (!opinions.value.id) {
        saveOpinionsApi(opinions.value).then(({ success, msg }: IAxios) => {
          if (success) {
            modal.msgSuccess('操作成功')
          } else {
            modal.msgError(msg)
          }
          getopinionsList()
        })
      } else {
        updateOpinionsApi(opinions.value).then(({ success, msg }: IAxios) => {
          if (success) {
            modal.msgSuccess('操作成功')
          } else {
            modal.msgError(msg)
          }
          getopinionsList()
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
    opinionsFormRef.value?.clearValidate(['sname'])
  }, 10)
}
/**
 * 删除
 * @param row
 */
const handleDelete = (row?: TwsaveOpinions) => {
  modal.confirm('是否删除?').then(() => {
    const ids = []
    if (row) {
      ids.push(row.id)
    } else {
      opinionsSelection.value?.forEach((color) => ids.push(color.id))
    }
    if (!ids.length) {
      modal.msgWarning('请选择删除记录')
      return
    }
    deleteOpinionsApi(ids.join(',')).then(({ success, msg }: IAxios) => {
      if (success) {
        modal.msgSuccess('操作成功')
      } else {
        modal.msgError(msg)
      }
      getopinionsList()
    })
  })
}
/**
 * 改变页码总数时
 */
const handleSizeChange = (val: number) => {
  queryInfo.value.pageSize = val
  getopinionsList()
}
/**
 * 改变页数时
 * @param val
 */
const handleCurrentChange = (val: number) => {
  queryInfo.value.page = val
  getopinionsList()
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'opinions'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>

<style scoped></style>
