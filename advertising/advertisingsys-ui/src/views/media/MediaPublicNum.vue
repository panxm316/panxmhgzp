<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <!-- <HgDateIndex @onresponse="handleTimeChange"></HgDateIndex> -->
        <el-button type="primary" icon="Plus" @click="handleBatchAdd">批量新增</el-button>
        <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
        <el-button type="danger" icon="Delete" @click="handleDelete()">删除</el-button>

        <el-date-picker
          v-model="queryInfo.startTime"
          style="width: 160px"
          type="date"
          placeholder="开始日期"
          value-format="YYYY-MM-DD"
          @change="onisAfterTime"
        />
        <span style="margin-right: 10px; font-size: 14px; line-height: 30px">至</span>
        <el-date-picker
          v-model="queryInfo.endTime"
          style="width: 160px"
          type="date"
          placeholder="结束日期"
          value-format="YYYY-MM-DD"
          @change="onisAfterTime"
        />

        <el-select
          v-model="queryInfo.mediaid"
          placeholder="媒体"
          clearable
          style="width: 160px"
          @change="loaddata"
        >
          <el-option
            v-for="item in mediaCombo"
            :key="item.id"
            :value="item.id"
            :label="item.name"
          />
        </el-select>
        <el-input
          v-model="queryInfo.spublishno"
          placeholder="请输入期号"
          clearable
          style="width: 200px"
          @clear="loaddata"
          @keyup.enter="loaddata"
        />
        <el-button type="primary" icon="Search" @click="loaddata">搜索</el-button>
        <el-tooltip class="item" effect="dark" content="帮助" placement="top">
          <el-button class="help" type="text" @click="showhelp()">?</el-button>
        </el-tooltip>
      </div>
    </div>
    <div class="table_box">
      <el-table
        :data="mediaPublicNumList"
        row-key="id"
        :height="tableheight"
        :border="true"
        stripe
        :header-cell-style="tableHeaderColor"
        @sort-change="handleSortChange"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" prop="ischeck" width="50" align="center">
        </el-table-column>
        <el-table-column prop="medianame" label="媒体名称" sortable="custom"> </el-table-column>

        <el-table-column prop="dpublishtime" label="发布日期" sortable="custom">
          <template #default="scope">
            <span>{{ hgFormatDate(scope.row.dpublishtime, 'YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="spublishno" label="期号" sortable="custom"> </el-table-column>
        <el-table-column prop="buse" label="启用" :width="80" align="center" sortable="custom">
          <template #default="scope">
            <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="200"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button link type="success" icon="Edit" @click="handleUpdate(scope.row)"
              >修改</el-button
            >
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="queryInfo.page"
        class="pagination"
        :page-sizes="pageSizes"
        :page-size="queryInfo.pageSize"
        small
        layout="total, sizes, prev, pager, next, jumper"
        :total="pageTotal"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <el-dialog
      v-model="dialogVisible"
      title="媒体刊期编辑"
      width="600px"
      :align-center="true"
      :close-on-click-modal="false"
    >
      <el-form ref="ruleFormRef" :rules="rules" :model="mediapublicnumForm" label-width="200px">
        <el-row :gutter="50">
          <el-col :span="22">
            <el-form-item label="媒体" prop="mediaid">
              <el-select
                v-model="mediapublicnumForm.mediaid"
                placeholder="媒体"
                style="width: 200px"
              >
                <el-option
                  v-for="item in mediaCombo"
                  :key="item.id"
                  :value="item.id"
                  :label="item.name"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="发布日期" prop="dpublishtime">
              <el-date-picker
                v-model="mediapublicnumForm.dpublishtime"
                type="date"
                placeholder="选择日期"
                :clearable="false"
                value-format="YYYY-MM-DD"
                style="width: 200px"
              />
            </el-form-item>
            <el-form-item label="期号" prop="spublishno">
              <el-input
                v-model="mediapublicnumForm.spublishno"
                style="width: 200px"
                :formatter="testSpublishno"
                :maxlength="10"
              />
            </el-form-item>

            <el-form-item label="启用" prop="buse">
              <el-checkbox v-model="mediapublicnumForm.buse" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" icon="Check" @click="handleSave(ruleFormRef)">保存</el-button>
          <el-button icon="Close" @click="handleCancel(ruleFormRef)">取消</el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog
      v-model="dialogVisibleBatch"
      title="批量新增"
      width="700px"
      :align-center="true"
      :close-on-click-modal="false"
    >
      <el-form
        ref="batchRuleFormRef"
        :rules="batchRules"
        :model="batchMediaPublicNumForm"
        label-width="120px"
      >
        <el-row :gutter="50">
          <el-col :span="22">
            <el-form-item label="媒体" prop="mediaid">
              <el-select
                v-model="batchMediaPublicNumForm.mediaid"
                placeholder="媒体"
                style="width: 200px"
              >
                <el-option
                  v-for="item in mediaCombo"
                  :key="item.id"
                  :value="item.id"
                  :label="item.name"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="刊期类型" prop="mediapublictype">
              <el-space direction="vertical" alignment="flex-start">
                <el-select
                  v-model="batchMediaPublicNumForm.mediapublictype"
                  placeholder="刊期类型"
                  style="width: 200px"
                  @change="onMediaPublicType"
                >
                  <el-option
                    v-for="item in mediaPublicTypeCombo"
                    :key="item"
                    :value="item"
                    :label="item"
                  />
                </el-select>
                <span>填写：星期数/日期/月份-日期，多项用;分隔</span>
              </el-space>
            </el-form-item>
            <el-form-item label="间隔时间" prop="spublishnum">
              <el-space direction="vertical" alignment="flex-start">
                <el-input
                  v-model="batchMediaPublicNumForm.spublishnum"
                  placeholder="间隔时间"
                  style="width: 200px"
                />
                <span>{{ spublishnumHint }}</span>
              </el-space>
            </el-form-item>
            <el-form-item label="起始期号" prop="spublishno">
              <el-input
                v-model="batchMediaPublicNumForm.spublishno"
                style="width: 200px"
                :formatter="testSpublishno"
                :maxlength="10"
              />
            </el-form-item>
            <el-form-item label="起始日期" required>
              <el-row>
                <el-col :span="11">
                  <el-form-item prop="startTime">
                    <el-date-picker
                      v-model="batchMediaPublicNumForm.startTime"
                      type="date"
                      placeholder="开始日期"
                      :clearable="false"
                      value-format="YYYY-MM-DD"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
                <el-col class="text-center" :span="2">至</el-col>
                <el-col :span="11">
                  <el-form-item prop="endTime">
                    <el-date-picker
                      v-model="batchMediaPublicNumForm.endTime"
                      type="date"
                      placeholder="结束日期"
                      :clearable="false"
                      value-format="YYYY-MM-DD"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item label="启用" prop="buse">
              <el-checkbox v-model="batchMediaPublicNumForm.buse" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" icon="Check" @click="handleBatchSave(batchRuleFormRef)"
            >保存</el-button
          >
          <el-button icon="Close" @click="handleBatchCancel(batchRuleFormRef)">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { getMediaDataComboApi } from '@/api/media/media'
import {
  deleteMediaPublicNumApi,
  getMediaPublicNumPageListApi,
  saveBatchMediaPublicNumApi,
  saveMediaPublicNumApi,
  updateMediaPublicNumApi
} from '@/api/media/mediapublicnum'
import { IDataCombo } from '@/types/common/DataCombo'
import {
  TMediaPublicNumDto,
  TMediapublicnumQueryInfo,
  Tmediapublicnum
} from '@/types/views/media/mediapublicnum'
import { computTableHeight } from '@/utils'
import { IAxios } from 'axios'
import type { ElFormItem, FormInstance, FormRules } from 'element-plus'
import modal from '@/plugins/modal'
import { hgFormatDate, tableHeaderColor } from '@/utils/index'
import { InternalRuleItem } from 'async-validator'
import dayjs from 'dayjs'

defineOptions({
  name: 'MediaPublicNum'
})
import { useRouter } from 'vue-router'
const router = useRouter()
/** 表格高度 */
const tableheight = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 数据总条数 */
const pageTotal = ref<number>(0)
/** 列表展示对象 */
const mediaPublicNumList = ref<Tmediapublicnum[]>([])
/** 媒体下拉数据 */
const mediaCombo = ref<IDataCombo[]>([])
/** 查询对象 */
const queryInfo = ref<TMediapublicnumQueryInfo>({
  sort: 'dpublishtime',
  order: 'desc',
  queryKey: '',
  startTime: '',
  endTime: '',
  page: 1,
  pageSize: 20,
  mediaid: ''
})
/** 列表多选数据 */
const multipleSelection = ref<Tmediapublicnum[]>([])

/** 表单ref */
const ruleFormRef = ref<FormInstance>()
/** 初始化数据 */
const initFormData: Tmediapublicnum = {
  id: undefined,
  buse: true,
  dpublishtime: '',
  mediaid: '',
  medianame: '',
  spublishno: ''
}
/** 表单对象 */
const mediapublicnumForm = ref<Tmediapublicnum>(initFormData)
/** 校验设置参数 */
const rules = reactive<FormRules<Tmediapublicnum>>({
  mediaid: [{ required: true, message: '请选择媒体', trigger: 'change' }],
  dpublishtime: [
    {
      type: 'date',
      required: true,
      message: '请选择发行时间',
      trigger: 'blur'
    }
  ],
  spublishno: [{ required: true, message: '请输入期号', trigger: 'change' }]
})

/** 弹出添加修改标志 */
const dialogVisible = ref(false)
const batchInitFormData = {
  buse: true,
  mediaid: '',
  medianame: '',
  spublishno: '',
  mediapublictype: '日报',
  spublishnum: '0',
  startTime: '',
  endTime: ''
}
/** 批量新增表单 */
const batchMediaPublicNumForm = ref<TMediaPublicNumDto>(batchInitFormData)
/** 批量新增表单ref */
const batchRuleFormRef = ref<FormInstance>()
/** 弹出批量新增标志 */
const dialogVisibleBatch = ref(false)
/** 刊期类型下拉 */
const mediaPublicTypeCombo = ref([
  '日报',
  '周刊',
  '双周刊',
  '旬刊',
  '半月刊',
  '月刊',
  '双月刊',
  '季刊',
  '半年刊',
  '年刊'
])
/**
 * 批量添加校验
 */
const isnanvalidator = (
  rule: InternalRuleItem,
  value: string,
  callback: (param?: Error) => void
) => {
  if (value) {
    let reg = new RegExp('^[1-7];[1-7]$')
    switch (batchMediaPublicNumForm.value.mediapublictype) {
      case '日报': {
        reg = new RegExp('^[0]$')
        break
      }
      case '周刊': {
        reg = new RegExp('^[1-7];[1-7]$')
        const wlist = value.split(';')
        if (wlist.length > 6) {
          callback(new Error(rule.message as string))
        } else {
          for (let i = 0; i < wlist.length; i++) {
            try {
              if (!wlist[i]) {
                callback(new Error(rule.message as string))
              } else {
                if (/^[-]?\d+$/.test(wlist[i])) {
                  if (Number(wlist[i]) < 1 || Number(wlist[i]) > 7) {
                    callback(new Error(rule.message as string))
                  }
                } else {
                  callback(new Error(rule.message as string))
                }
              }
            } catch (e) {
              callback(new Error(rule.message as string))
            }
          }
        }
        callback()
        break
      }
      case '双周刊':
        reg = new RegExp('^[1-7]$')
        break
      case '旬刊':
        reg = new RegExp('^([1-9]|10);(1[1-9]|20);(2[1-8])$')
        break
      case '半月刊':
        reg = new RegExp('^([1-9]|(1[0-5]));(1[6-9]|2[0-8])$')
        break
      case '月刊':
        reg = new RegExp('^([1-9]\\b|1\\d|2[0-8])$')
        break
      case '双月刊':
        reg = new RegExp('^([1-9]|1[0-2])-([1-9]|1[0-9]|2[0-8])$')
        break
      case '季刊':
        reg = new RegExp(
          '^(2-([1-9]\\b|1\\d|2[0-8])|[13]-([1-9]\\b|[12]\\d|3[01]));([5]-([1-9]\\b|[12]\\d|3[01])|[46]-([1-9]\\b|[12]\\d|30));([78]-([1-9]\\b|[12]\\d|3[01])|9-([1-9]\\b|[12]\\d|30));(1[02]-([1-9]\\b|[12]\\d|3[01])|11-([1-9]\\b|[12]\\d|30))'
        )
        break
      case '半年刊':
        reg = new RegExp(
          '^(2-([1-9]\\b|1\\d|2[0-8])|[135]-([1-9]\\b|[12]\\d|3[01])|[46]-([1-9]\\b|[12]\\d|30));(([78]|1[02])-([1-9]\\b|[12]\\d|3[01])|(9|11)-([1-9]\\b|[12]\\d|30))$'
        )
        break
      case '年刊':
        reg = new RegExp(
          '^(2-([1-9]\\b|1\\d|2[0-8])|([13578]|1[02])-([1-9]\\b|[12]\\d|3[01])|([469]|11)-([1-9]\\b|[12]\\d|30))$'
        )
        break
      default:
        break
    }
    if (!reg.test(value.trim())) {
      callback(new Error(rule.message as string))
    } else {
      callback()
    }
  } else {
    callback()
  }
}
const batchRules = reactive<FormRules<TMediaPublicNumDto>>({
  mediaid: [{ required: true, message: '请选择媒体', trigger: 'change' }],
  mediapublictype: [{ required: true, message: '请选择刊期类型', trigger: 'change' }],
  spublishnum: [
    { required: true, message: '请输入间隔时间', trigger: 'change' },
    { validator: isnanvalidator, message: '请输入正确的格式', trigger: 'change' }
  ],
  spublishno: [{ required: true, message: '请输入起始期号', trigger: 'change' }],
  startTime: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束日期', trigger: 'change' }]
})
/** 发布日期提示信息 */
const spublishnumHint = ref('日报请填写0')
onMounted(() => {
  loaddata()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
  getMediaDataCombo()
})

/**
 * 获取媒体刊期列表
 */
const loaddata = () => {
  const data: TMediapublicnumQueryInfo = queryInfo.value
  getMediaPublicNumPageListApi(data).then(({ obj, total }: IAxios<Tmediapublicnum[]>) => {
    mediaPublicNumList.value = obj
    pageTotal.value = total
  })
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
    queryInfo.value.order = 'datdoperatordatee'
  }
  loaddata()
}
/**
 * @description: 媒体下拉
 * @return {*}
 */
const getMediaDataCombo = () => {
  getMediaDataComboApi().then(({ obj }: IAxios<IDataCombo[]>) => {
    mediaCombo.value = obj
  })
}
/**
 * 新增
 */
const handleAdd = () => {
  mediapublicnumForm.value = { ...initFormData }
  setTimeout(() => {
    ruleFormRef.value?.clearValidate(['mediaid', 'dpublishtime', 'spublishno'])
    dialogVisible.value = true
  }, 10)
}
/**
 * 修改
 * @param row
 */
const handleUpdate = (row: Tmediapublicnum) => {
  ruleFormRef.value?.clearValidate(['mediaid', 'dpublishtime', 'spublishno'])
  dialogVisible.value = true
  nextTick(() => {
    mediapublicnumForm.value = { ...row }
  })
}
/**
 * 删除
 * @param row
 */
const handleDelete = (row?: Tmediapublicnum) => {
  modal.confirm('是否删除?').then(() => {
    const ids = []
    if (row) {
      ids.push(row.id)
    } else {
      multipleSelection.value?.forEach((item) => ids.push(item.id))
    }
    if (!ids.length) {
      modal.msgWarning('请选择删除记录')
      return
    }
    deleteMediaPublicNumApi(ids.join(',')).then(({ success, msg }: IAxios) => {
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
 * @description: 日期组件返回
 * @param {*} val
 * @return {*}
 */
// const handleTimeChange = (val: DateTimeType) => {
//   queryInfo.value.startTime = val.startTime
//   queryInfo.value.endTime = val.endTime
//   loaddata()
// }

const handleSave = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      const medias = mediaCombo.value.filter((item) => item.id === mediapublicnumForm.value.mediaid)
      mediapublicnumForm.value.medianame = medias.length > 0 ? medias[0].name : ''
      if (mediapublicnumForm.value.id) {
        updateMediaPublicNumApi(mediapublicnumForm.value).then(({ success }: IAxios) => {
          if (success) {
            modal.msgSuccess('保存成功')
            loaddata()
            dialogVisible.value = false
          }
        })
      } else {
        saveMediaPublicNumApi(mediapublicnumForm.value).then(({ success }: IAxios) => {
          if (success) {
            modal.msgSuccess('保存成功')
            loaddata()
            dialogVisible.value = false
          }
        })
      }
    }
  })
}
/**
 * 取消
 * @param formEl
 */
const handleCancel = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.clearValidate()
  dialogVisible.value = false
}
/**
 * @description: 批量新增
 * @return {*}
 */
const handleBatchAdd = () => {
  batchMediaPublicNumForm.value = { ...batchInitFormData }
  setTimeout(() => {
    batchRuleFormRef.value?.clearValidate([
      'mediaid',
      'mediapublictype',
      'spublishno',
      'spublishnum',
      'startTime',
      'endTime'
    ])
    dialogVisibleBatch.value = true
  }, 10)
}
/**
 * @description: 批量新增下选择刊期类型联动提示
 * muyn 231205加上了日报
 * @param {*} val
 * @return {*}
 */
const onMediaPublicType = (val: string) => {
  switch (val) {
    case '日报':
      spublishnumHint.value = '日报请填写0'
      // batchInitFormData.spublishnum = '0'
      break
    case '周双刊':
      spublishnumHint.value = '周双刊有效示例: 1;4'
      break
    case '周刊':
      spublishnumHint.value = '周刊有效示例: 1;2;3;4;5;6;7 任意组合'
      break
    case '双周刊':
      spublishnumHint.value = '双周刊有效示例: 1'
      break
    case '旬刊':
      spublishnumHint.value = '旬刊有效示例: 1;11;21'
      break
    case '半月刊':
      spublishnumHint.value = '半月刊有效示例: 1;16'
      break
    case '月刊':
      spublishnumHint.value = '月刊有效示例: 1'
      break
    case '双月刊':
      spublishnumHint.value = '双月刊有效示例: 月-日,日不能大于28，例：7-15'
      break
    case '季刊':
      spublishnumHint.value = '季刊有效示例: 1-2;4-3;7-4;10-5'
      break
    case '半年刊':
      spublishnumHint.value = '半年刊有效示例: 2-1;8-10'
      break
    case '年刊':
      spublishnumHint.value = '年刊有效示例: 1-2'
      break
  }
  batchMediaPublicNumForm.value.spublishnum = ''
}
/**
 * @description: 批量新增保存
 * @param {*} formEl
 * @return {*}
 */
const handleBatchSave = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      if (
        dayjs(batchMediaPublicNumForm.value.startTime).isAfter(
          batchMediaPublicNumForm.value.endTime
        )
      ) {
        modal.msgError('开始日期不能大于结束日期')
        return
      }
      const medias = mediaCombo.value.filter(
        (item) => item.id === batchMediaPublicNumForm.value.mediaid
      )
      batchMediaPublicNumForm.value.medianame = medias.length > 0 ? medias[0].name : ''
      saveBatchMediaPublicNumApi(batchMediaPublicNumForm.value).then(({ success }: IAxios) => {
        if (success) {
          modal.msgSuccess('保存成功')
          loaddata()
          dialogVisibleBatch.value = false
        }
      })
    }
  })
}
/**
 * @description: 批量新增取消
 * @param {*} formEl
 * @return {*}
 */
const handleBatchCancel = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.clearValidate()
  dialogVisibleBatch.value = false
}
/**
 * 改变页码总数时
 */
const handleSizeChange = (val: number) => {
  queryInfo.value.pageSize = val
  loaddata()
}
/**
 * 改变页数时
 * @param val
 */
const handleCurrentChange = (val: number) => {
  queryInfo.value.page = val
  loaddata()
}
/**
 * 列表多选
 * @param val
 */
const handleSelectionChange = (val: Tmediapublicnum[]) => {
  multipleSelection.value = val
}
/**
 * 匹配期号过滤规则
 */
const testSpublishno = (val: string) => {
  // 正整数并且开头不能是0
  return val.replace(/[^0-9]|^(0+)/, '')
}
/**
 * @description: 开始日期结束日期比较如果开始日期小于结束日期则颠倒
 * @param {*} val
 * @return {*}
 */
const onisAfterTime = (val: string) => {
  const isAfter: boolean = dayjs(queryInfo.value.startTime).isAfter(queryInfo.value.endTime)
  const startTime = queryInfo.value.startTime
  const endTime = queryInfo.value.endTime
  if (isAfter) {
    queryInfo.value.startTime = endTime
    queryInfo.value.endTime = startTime
  }
  loaddata()
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'mediapublicnum'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>

<style scoped></style>
