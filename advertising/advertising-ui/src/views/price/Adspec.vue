<!--
 * @Author: caogd
 * @Date: 2023-09-19 15:41:26
 * @LastEditTime: 2023-11-09 13:28:47
 * @LastEditors: caogd
 * @Description: 广告规格
 报刊规格增加是否分类广告，当是否分类广告挑了勾后，要出现是否大分类广告的可选项，和格子数的必填项，分类广告的面积可能是（长*宽+0.07*格子数）这个目前先这样固定死，报刊类型的规格 长 宽必须填写，单位是厘米。
-->

<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <!-- <el-button type="primary" @click="handleUpdateDate">批量修改时间</el-button> -->
        <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
        <el-button type="danger" icon="Delete" @click="handleDelete()">删除</el-button>
        <el-select
          v-model="queryInfo.mediaid"
          placeholder="媒体"
          clearable
          @change="handleQueryMedia"
        >
          <el-option
            v-for="item in mediaCombo"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
        <el-input
          v-model="queryInfo.queryKey"
          placeholder="请输关键字查询"
          clearable
          style="width: 200px"
          @clear="getAdspecList"
          @keyup.enter="getAdspecList"
        />
        <el-button type="primary" icon="Search" @click="getAdspecList">搜索</el-button>
        <el-tooltip class="item" effect="dark" content="帮助" placement="top">
          <el-button class="help" @click="showhelp()">?</el-button>
        </el-tooltip>
      </div>
    </div>
    <div class="table_box">
      <el-table
        :data="adspecList"
        row-key="id"
        :height="props.tableheight"
        :border="true"
        stripe
        :header-cell-style="tableHeaderColor"
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <el-table-column type="selection" prop="ischeck" width="50" align="center">
        </el-table-column>
        <el-table-column
          prop="sname"
          label="名称"
          min-width="150"
          sortable="custom"
        ></el-table-column>
        <el-table-column prop="medianame" label="媒体" min-width="150"></el-table-column>
        <el-table-column prop="nheight" label="高" :width="100" sortable="custom"></el-table-column>
        <el-table-column prop="nwidth" label="宽" :width="100" sortable="custom"></el-table-column>
        <el-table-column prop="narea" label="面积" :width="100"></el-table-column>
        <el-table-column prop="ipknum" label="格子数" :width="100"></el-table-column>

        <el-table-column prop="dstartdate" label="开始日期" min-width="130" sortable="custom">
          <template #default="scope">
            <span>{{ hgFormatDate(scope.row.dstartdate, 'YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="denddate" label="结束日期" min-width="130" sortable="custom">
          <template #default="scope">
            <span>{{ hgFormatDate(scope.row.denddate, 'YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="isort" label="序号" :width="80" sortable="custom" align="center">
        </el-table-column>
        <el-table-column
          v-if="props.type === 'paper'"
          prop="bclassified"
          label="分类广告"
          :width="80"
          align="center"
        >
          <template #default="scope">
            <el-checkbox v-model="scope.row.bclassified" disabled></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column
          v-if="props.type === 'paper'"
          prop="bbigclassified"
          label="大分类广告"
          :width="90"
          align="center"
        >
          <template #default="scope">
            <el-checkbox v-model="scope.row.bbigclassified" disabled></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column prop="buse" label="启用" :width="80" sortable="custom" align="center">
          <template #default="scope">
            <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column prop="sremark" label="备注" :width="150"> </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="150"
          fixed="right"
          class-name="small-padding fixed-width"
          fixed-width
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
      <!-- 页码 -->
      <el-pagination
        small
        layout="total, sizes, prev, pager, next, jumper"
        class="pagination"
        :current-page="queryInfo.page"
        :page-size="queryInfo.pageSize"
        :page-sizes="paginationInfo.pageSizes"
        :total="paginationInfo.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <el-dialog
      v-model="dialogVisible"
      title="编辑"
      width="700px"
      align-center
      :close-on-click-modal="false"
    >
      <el-form ref="adspecFormRef" :model="adspecForm" label-width="150px" :rules="rules">
        <el-row :gutter="50">
          <el-col :span="22">
            <el-form-item label="名称" prop="sname">
              <el-input v-model="adspecForm.sname" />
            </el-form-item>
            <el-form-item label="媒体" prop="mediaid">
              <el-select v-model="adspecForm.mediaid" placeholder="媒体">
                <el-option
                  v-for="item in mediaCombo"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="高（cm）" prop="nheight">
              <el-input-number v-model="adspecForm.nheight" :min="0" controls-position="right" />
            </el-form-item>
            <el-form-item label="宽（cm）" prop="nwidth">
              <el-input-number v-model="adspecForm.nwidth" :min="0" controls-position="right" />
            </el-form-item>
            <el-form-item label="日期范围">
              <el-row>
                <el-col :span="11">
                  <el-form-item prop="dstartdate">
                    <el-date-picker
                      v-model="adspecForm.dstartdate"
                      type="date"
                      placeholder="开始日期"
                      value-format="YYYY-MM-DD"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
                <el-col class="text-center" :span="2">至</el-col>
                <el-col :span="11">
                  <el-form-item prop="denddate">
                    <el-date-picker
                      v-model="adspecForm.denddate"
                      type="date"
                      placeholder="结束日期"
                      value-format="YYYY-MM-DD"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item label="序号" prop="isort">
              <el-input-number v-model="adspecForm.isort" :min="1" controls-position="right" />
            </el-form-item>
            <el-form-item label="备注" prop="sremark">
              <el-input v-model="adspecForm.sremark" type="textarea" :maxlength="200" />
            </el-form-item>
            <el-form-item label="启用" prop="buse">
              <el-checkbox v-model="adspecForm.buse" />
            </el-form-item>
            <el-form-item v-if="props.type === 'paper'" label="是否分类广告" prop="bclassified">
              <el-checkbox v-model="adspecForm.bclassified" @change="onBclassified" />
            </el-form-item>
            <el-form-item
              v-if="adspecForm.bclassified"
              label="是否大分类广告"
              prop="bbigclassified"
            >
              <el-checkbox v-model="adspecForm.bbigclassified" />
            </el-form-item>
            <el-form-item v-if="adspecForm.bclassified" label="格子数" prop="ipknum">
              <el-input-number v-model="adspecForm.ipknum" :min="0" controls-position="right" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" icon="Check" @click="handleSave(adspecFormRef)">保存</el-button>
          <el-button icon="Close" @click="handleCancel">取消</el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog
      v-model="updateDateDialogVisible"
      title="批量修改日期"
      width="700px"
      align-center
      :close-on-click-modal="false"
    >
      <el-form :model="updateDateForm" label-width="150px">
        <el-row :gutter="50">
          <el-col :span="22">
            <el-form-item
              ><el-text class="mx-1" type="danger"
                >注：所有数据都会修改，请谨慎操作！</el-text
              ></el-form-item
            >
            <el-form-item label="将开始日期修改为" prop="dstartdate">
              <el-row :gutter="10">
                <el-col :span="19">
                  <el-date-picker
                    v-model="updateDateForm.dstartdate"
                    type="date"
                    placeholder="开始日期"
                    :clearable="false"
                    value-format="YYYY-MM-DD"
                    style="width: 100%"
                  />
                </el-col>
                <el-col :span="5"
                  ><el-button type="primary" @click="onUpdateAdspecdate('dstartdate')"
                    >修改</el-button
                  >
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item label="将结束日期修改为" prop="denddate">
              <el-row :gutter="10">
                <el-col :span="19">
                  <el-date-picker
                    v-model="updateDateForm.denddate"
                    type="date"
                    placeholder="结束日期"
                    :clearable="false"
                    value-format="YYYY-MM-DD"
                    style="width: 100%"
                  />
                </el-col>
                <el-col :span="5"
                  ><el-button type="primary" @click="onUpdateAdspecdate('denddate')"
                    >修改</el-button
                  >
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item label="将结束日期为">
              <el-row :gutter="10">
                <el-col :span="9">
                  <el-form-item prop="olddenddate">
                    <el-date-picker
                      v-model="updateDateForm.olddenddate"
                      type="date"
                      placeholder="结束日期"
                      :clearable="false"
                      value-format="YYYY-MM-DD"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
                <el-col class="text-center" :span="3">修改为</el-col>
                <el-col :span="9">
                  <el-form-item prop="newdenddate">
                    <el-date-picker
                      v-model="updateDateForm.newdenddate"
                      type="date"
                      placeholder="结束日期"
                      :clearable="false"
                      value-format="YYYY-MM-DD"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="3"
                  ><el-button type="primary" @click="onUpdateAdspecdate('olddenddate')"
                    >修改</el-button
                  >
                </el-col>
              </el-row>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button icon="Close" @click="updateDateDialogVisible = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { FormRules, FormInstance } from 'element-plus'
import type {
  TAdspecModelVO,
  TAdspec,
  TQAdspec,
  TAdspecDateModelDTO
} from '@/types/views/media/adspec'
import type { TPagination } from '@/types/common/index'
import {
  getMaxSortApi,
  saveAdspecApi,
  updateAdspecApi,
  deleteAdspecByIdApi,
  getAdspecPageListApi,
  updateAdspecDateApi
} from '@/api/media/adspec'
import { required, computTableHeight, tableHeaderColor, hgFormatDate } from '@/utils/index'
import type { IAxios } from 'axios'
import modal from '@/plugins/modal'
import { IDataCombo } from '@/types/common/DataCombo'
import { getMediaDataComboByTypeApi } from '@/api/media/media'
import dayjs from 'dayjs'

defineOptions({
  name: 'Adspec'
})
import { useRouter } from 'vue-router'
type Props = {
  /** 传入的媒体类型key */
  type: string
  /** 传入的关键字名称 */
  typename: string
  /** 传入表格高度 */
  tableheight: number
}
const props = defineProps<Props>()

const router = useRouter()
const mediaCombo = ref<IDataCombo[]>([])
/** 编辑 dialog */
const dialogVisible = ref(false)
/** 广告规格列表 */
const adspecList = ref<TAdspecModelVO[]>([])

const initAdspec = {
  mediaid: '',
  sname: '',
  isort: 1,
  sremark: '',
  buse: true,
  dstartdate: '', // dayjs().format('YYYY-MM-DD')
  denddate: '',
  nheight: 0,
  nwidth: 0,
  bclassified: false,
  bbigclassified: false,
  ipknum: 0
}
/** 广告规格 */
const adspecForm = ref<TAdspec>({ ...initAdspec })
/** 查询对象 */
const queryInfo = ref<TQAdspec>({
  sort: 'isort',
  order: 'asc',
  queryKey: '',
  page: 1,
  pageSize: 20,
  mediaid: ''
})
/** 分页对象 */
const paginationInfo = ref<TPagination>({
  pageSizes: [15, 20, 30, 40],
  total: 0
})
/** 规格ref */
const adspecFormRef = ref<FormInstance>()
/** 当前列表选中 */
const adspecSelection = ref<TAdspecModelVO[]>()
/** 批量修改日期弹出 */
const updateDateDialogVisible = ref(false)
/** 批量修改日期form */
const updateDateForm = ref<TAdspecDateModelDTO>({
  dstartdate: '',
  denddate: '',
  olddenddate: '',
  newdenddate: ''
})
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<TAdspec>>({
  sname: [
    { validator: required, required: true, message: '请输入名称', trigger: 'blur' },
    { max: 30, message: '不得大于30个字符', trigger: 'blur' }
  ],
  mediaid: [{ required: true, message: '媒体不能为空', trigger: 'change' }],
  nheight: [{ required: props.type === 'paper', message: '高不能为空', trigger: 'blur' }],
  nwidth: [{ required: props.type === 'paper', message: '宽不能为空', trigger: 'blur' }],
  ipknum: [{ required: true, message: '格子数不能为空', trigger: 'blur' }]
})

onMounted(() => {
  getAdspecList()
  getMediaDataCombo()
})
/**
 * 查询媒体下拉
 * @param val
 */
const handleQueryMedia = (val: string) => {
  getAdspecList()
}
/**
 * 点击批量修改日期
 */
const handleUpdateDate = () => {
  updateDateForm.value = {
    dstartdate: '',
    denddate: '',
    olddenddate: '',
    newdenddate: ''
  }
  updateDateDialogVisible.value = true
}
/**
 * 批量修改日期
 * @param val
 */
const onUpdateAdspecdate = (val: string) => {
  let data: TAdspecDateModelDTO = {}
  if (val === 'dstartdate') {
    if (!updateDateForm.value.dstartdate) {
      modal.msgError('请选择开始日期')
      return
    }
    data = {
      dstartdate: updateDateForm.value.dstartdate
    }
  } else if (val === 'denddate') {
    if (!updateDateForm.value.denddate) {
      modal.msgError('请选择结束日期')
      return
    }
    data = {
      denddate: updateDateForm.value.denddate
    }
  } else {
    if (!updateDateForm.value.olddenddate) {
      modal.msgError('请选择需要修改的结束日期')
      return
    }
    if (!updateDateForm.value.newdenddate) {
      modal.msgError('请选择结束日期')
      return
    }
    data = {
      olddenddate: updateDateForm.value.olddenddate,
      denddate: updateDateForm.value.newdenddate
    }
  }
  updateAdspecDateApi(data).then(({ success, msg }: IAxios) => {
    if (success) {
      modal.msgSuccess('操作成功')
      updateDateDialogVisible.value = false
      getAdspecList()
    }
  })
}
/**
 * 选择
 * @param rows
 */
const handleSelectionChange = (rows: TAdspecModelVO[]) => {
  adspecSelection.value = rows
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
  getAdspecList()
}
/**
 * @description: 获取媒体下拉
 * @return {*}
 */
const getMediaDataCombo = () => {
  getMediaDataComboByTypeApi({ type: props.type }).then((res: IAxios<IDataCombo[]>) => {
    if (res.success) {
      mediaCombo.value = res.obj
    }
  })
}
/**
 * 获取规格列表
 */
const getAdspecList = () => {
  queryInfo.value.mediatypekey = props.type
  getAdspecPageListApi(queryInfo.value).then(({ obj, total }: IAxios<TAdspecModelVO[]>) => {
    adspecList.value = obj
    paginationInfo.value.total = total
  })
}
/**
 * 获取序号最大值
 */
const getMaxSort = () => {
  getMaxSortApi().then(({ success, obj }: IAxios<number>) => {
    adspecForm.value.isort = obj
  })
}
/**
 * 是否分类广告勾选时初始化
 */
const onBclassified = () => {
  console.log(adspecForm.value.bclassified)

  if (!adspecForm.value.bclassified) {
    adspecForm.value.bbigclassified = false
    adspecForm.value.ipknum = 0
  }
}

/**
 * 新增
 */
const handleAdd = () => {
  adspecForm.value = { ...initAdspec }
  getMaxSort()
  setTimeout(() => {
    adspecFormRef.value?.clearValidate([
      'mediaid',
      'sname',
      'nheight',
      'nwidth',
      'dstartdate',
      'denddate'
    ])
    dialogVisible.value = true
  }, 10)
}
/**
 * 修改
 * @param row
 */
const handleUpdate = (row: TAdspecModelVO) => {
  adspecForm.value = { ...row } as TAdspec
  dialogVisible.value = true
}
/**
 * 保存
 */
const handleSave = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  if (dayjs(adspecForm.value.dstartdate).isAfter(adspecForm.value.denddate)) {
    modal.msgError('开始日期不能大于结束日期')
    return
  }
  await formEl.validate((valid) => {
    if (valid) {
      if (!adspecForm.value.id) {
        saveAdspecApi(adspecForm.value).then(({ success, msg }: IAxios) => {
          if (success) {
            modal.msgSuccess('操作成功')
          }
          getAdspecList()
        })
      } else {
        updateAdspecApi(adspecForm.value).then(({ success, msg }: IAxios) => {
          if (success) {
            modal.msgSuccess('操作成功')
          }
          getAdspecList()
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
    adspecFormRef.value?.clearValidate(['sname'])
  }, 10)
}
/**
 * 删除
 * @param row
 */
const handleDelete = (row?: TAdspecModelVO) => {
  modal.confirm('是否删除?').then(() => {
    const ids = []
    if (row) {
      ids.push(row.id)
    } else {
      adspecSelection.value?.forEach((color) => ids.push(color.id))
    }
    if (!ids.length) {
      modal.msgWarning('请选择删除记录')
      return
    }
    deleteAdspecByIdApi(ids.join(',')).then(({ success, msg }: IAxios) => {
      if (success) {
        modal.msgSuccess('操作成功')
      } else {
        modal.msgError(msg)
      }
      getAdspecList()
    })
  })
}
/**
 * 改变每页显示条数
 * @param val
 */
const handleSizeChange = (val: number) => {
  queryInfo.value.pageSize = val
  getAdspecList()
}
/**
 * 分页当前页
 * @param pageIndex
 */
const handleCurrentChange = (pageIndex: number) => {
  queryInfo.value.page = pageIndex
  getAdspecList()
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'adspec'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>

<style scoped></style>
