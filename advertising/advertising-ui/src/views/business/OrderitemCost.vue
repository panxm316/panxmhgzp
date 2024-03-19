<!--
 * @Author: suny
 * @Date: 2023-12-07 15:08:30
 * @LastEditTime: 2024-03-18 18:49:16
 * @LastEditors: suny
 * @Description: 广告成本表
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button type="primary" icon="Plus" @click="importBankAccount">导入</el-button>
        <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="dataTableRef"
        :data="orderAndItemList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        :border="true"
      >
        <el-table-column
          label="广告项目名称"
          prop="adprojectname"
          min-width="160"
          show-overflow-tooltip
        />
        <el-table-column
          label="合同号"
          prop="scontractnum"
          min-width="140"
          align="center"
          show-overflow-tooltip
        >
          <template #default="scope">
            <el-button
              link
              type="primary"
              icon="view"
              size="small"
              @click="handleSee(scope.row, scope.$index)"
              >{{ scope.row.scontractnum }}</el-button
            >
          </template>
        </el-table-column>
        <el-table-column label="客户名称" prop="customername" width="100" show-overflow-tooltip />
        <el-table-column label="代理公司" prop="agencyname" width="100" show-overflow-tooltip />
        <el-table-column label="内容生成方" prop="agentname" width="100" show-overflow-tooltip />
        <el-table-column label="行业" prop="adindustryname" width="100" show-overflow-tooltip />
        <el-table-column label="媒体类型" prop="mediatypename" width="100" show-overflow-tooltip />
        <el-table-column label="媒体" prop="medianame" width="100" show-overflow-tooltip />
        <el-table-column label="广告标题" prop="sadtitle" width="100" show-overflow-tooltip />
        <el-table-column label="刊发时间" prop="prestartdate" width="100">
          <template #default="scope">
            <span>{{ dayjs(scope.row.prestartdate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="应收" prop="amountreceivable" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="已收" prop="amountreceived" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceived, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="欠款" prop="amountarrearage" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="成本" prop="namountcost" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namountcost, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="" prop="foldname" width="100" show-overflow-tooltip />
        <el-table-column label="" prop="foldareavername" width="100" show-overflow-tooltip />
        <el-table-column label="" prop="foldpageplanname" width="100" show-overflow-tooltip />
        <el-table-column label="" prop="addisplayformname" width="100" show-overflow-tooltip />
        <el-table-column label="" prop="pagesortname" width="100" show-overflow-tooltip />
        <el-table-column label="" prop="adcolorname" width="100" show-overflow-tooltip />
        <el-table-column label="" prop="adspecname" width="100" show-overflow-tooltip />
        <el-table-column label="" prop="weeksettingname" width="100" show-overflow-tooltip />

        <el-table-column
          label="操作"
          :align="'left'"
          width="100"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              link
              style="margin-left: 10px"
              type="success"
              icon="Plus"
              size="small"
              title="成本编辑"
              @click="handleAdd(scope.row)"
              >成本</el-button
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
    <el-dialog
      v-model="dialogEditVisible"
      title="成本编辑"
      :width="1000"
      append-to-body
      :destroy-on-close="true"
    >
      <div>
        <el-row>
          <el-col :span="24">
            <el-table
              ref="orderItemCostTableRef"
              :data="curOrderAndItem!.orderItemCostDTOList"
              highlight-current-row
              :border="true"
              style="max-height: 400px; overflow-y: auto"
            >
              <el-table-column
                label="创建人"
                prop="createempname"
                min-width="100"
                show-overflow-tooltip
              />
              <el-table-column label="创建日期" prop="dcreatedate" width="100">
                <template #default="scope">
                  <span>{{ dayjs(scope.row.dcreatedate).format('YYYY-MM-DD') }}</span>
                </template>
              </el-table-column>
              <el-table-column
                label="成本金额"
                prop="namountcost"
                min-width="100"
                show-overflow-tooltip
                align="right"
              >
                <template #default="scope">
                  <span>{{ formatMoney(scope.row.namountcost, '2') }}</span>
                </template>
              </el-table-column>
              <el-table-column
                label="说明"
                prop="sdescription"
                min-width="160"
                show-overflow-tooltip
              />
              <el-table-column label="状态" prop="istatus" width="100" :align="'center'">
                <template #default="scope">
                  {{ statusCombo.find((item) => item.id === scope.row.istatus)?.name }}
                </template>
              </el-table-column>
              <el-table-column
                label="操作"
                :align="'left'"
                width="240"
                class-name="small-padding fixed-width"
                fixed="right"
              >
                <template #default="scope">
                  <el-button
                    v-if="
                      scope.row.istatus === EStatus.待提交 || scope.row.istatus === EStatus.已退回
                    "
                    link
                    style="margin-left: 5px"
                    type="success"
                    icon="Edit"
                    size="small"
                    title="修改"
                    @click="onEdit(scope.row)"
                    >修改</el-button
                  >
                  <el-button
                    v-if="
                      scope.row.istatus === EStatus.待提交 || scope.row.istatus === EStatus.已退回
                    "
                    link
                    type="primary"
                    icon="Top"
                    size="small"
                    title="提交审核"
                    @click="onToCheck(scope.row)"
                    >提交</el-button
                  >
                  <el-button
                    v-if="
                      scope.row.istatus === EStatus.待提交 || scope.row.istatus === EStatus.已退回
                    "
                    link
                    type="danger"
                    icon="Delete"
                    size="small"
                    title="删除"
                    @click="onDelete(scope.row)"
                    >删除</el-button
                  >
                  <el-button
                    link
                    type="primary"
                    icon="view"
                    size="small"
                    title="查看详情"
                    @click="onDetail(scope.row)"
                    >详情</el-button
                  >
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form
              ref="orderitemCostFormRef"
              :model="orderItemCostForm"
              :rules="rules"
              label-width="100px"
              class="demo-orderItemCostForm"
              status-icon
            >
              <el-form-item label="成本金额" prop="namountcost">
                <el-input-number
                  v-model="orderItemCostForm.namountcost"
                  :precision="2"
                  :disabled="bDetailFlag"
                  placeholder="成本金额"
                  :min="0"
                  :max="9999999999"
                  :controls="false"
                  style="width: 120px"
                />
              </el-form-item>
              <el-form-item label="成本说明" prop="sdescription">
                <el-input
                  v-model="orderItemCostForm.sdescription"
                  :readonly="bDetailFlag"
                  :rows="4"
                  maxlength="200"
                  show-word-limit
                  placeholder="成本说明"
                  style="width: 400px"
                  type="textarea"
                >
                </el-input>
              </el-form-item>
              <el-form-item label="选择文件">
                <div style="width: 100%">
                  <el-row v-if="!bDetailFlag" width="100%">
                    <el-col :span="24">
                      <HgFileUpload
                        :server="hgfileuploadparam.server"
                        :accept="hgfileuploadparam.accept"
                        :multiple="true"
                        :storytypes="hgfileuploadparam.storytypes"
                        @getupfile="getUpFile"
                      ></HgFileUpload>
                    </el-col>
                  </el-row>
                  <el-row width="100%">
                    <el-col :span="24">
                      <el-table
                        v-if="curFileList.length > 0"
                        :data="curFileList"
                        :show-header="false"
                        style="width: 100%"
                      >
                        <template #empty>
                          <span style="color: #dbdbdb; border: none">暂无数据，请上传素材</span>
                        </template>
                        <el-table-column width="180">
                          <template #default="scope">
                            <div
                              class="img-div"
                              style="
                                height: 120px;
                                width: 180px;
                                display: flex;
                                align-items: center;
                                justify-content: center;
                              "
                              @click="previewFile(scope.row)"
                            >
                              <video
                                v-if="scope.row.sfiletype === 'Video'"
                                preload="none"
                                :src="scope.row.durl"
                                :poster="scope.row.url"
                                controls
                                style="height: 120px; width: 180px"
                              ></video>
                              <img
                                v-if="scope.row.sfiletype === 'Photo' && scope.row.url !== ''"
                                style="width: 100%"
                                :src="scope.row.url"
                              />
                              <img
                                v-if="scope.row.sfiletype === 'Audio'"
                                style="width: 100px"
                                :src="Audio"
                              />
                              <img
                                v-if="scope.row.sfiletype === 'Office'"
                                style="width: 100px"
                                :src="Office"
                              />
                              <img
                                v-if="scope.row.sfiletype === 'Application'"
                                style="width: 100px"
                                :src="Application"
                              />
                              <label v-show="scope.row.showflag" class="img-label_file checked">
                                <el-icon><Plus /></el-icon>
                              </label>
                            </div>
                          </template>
                        </el-table-column>
                        <el-table-column>
                          <template #default="scope">
                            <div style="font-size: 10pt; color: #8a9c9d; word-break: break-all">
                              {{ scope.row.soriginalfile }}
                            </div>
                            <div class="name-wrapper">
                              <el-input
                                v-model="scope.row.sdescription"
                                :readonly="bDetailFlag"
                                v-bind="{ id: 'textarea_sucai' + scope.row.sfileid }"
                                :rows="4"
                                maxlength="100"
                                show-word-limit
                                placeholder="文件说明"
                                type="textarea"
                              >
                              </el-input>
                            </div>
                          </template>
                        </el-table-column>
                        <el-table-column v-if="!bDetailFlag" width="60" :align="'center'">
                          <template #default="scope">
                            <li>
                              <el-button type="danger" link @click="onUpfileDel(scope.row.sfileid)">
                                <el-icon title="删除"><Delete /></el-icon>
                              </el-button>
                            </li>
                          </template>
                        </el-table-column>
                      </el-table>
                    </el-col>
                  </el-row>
                </div>
              </el-form-item>
              <el-form-item>
                <div style="position: absolute; right: 5px">
                  <el-checkbox v-if="!bDetailFlag" v-model="bflagUpdate" style="margin-right: 10px"
                    >同时提交(提交后不允许修改)</el-checkbox
                  >
                  <el-button
                    v-if="!bDetailFlag"
                    type="primary"
                    icon="Check"
                    @click="saveorderItemCostForm(orderitemCostFormRef)"
                    >保存</el-button
                  >
                  <el-button icon="Close" @click="onCancel">重置</el-button>
                </div>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
    <!-- 根据合同号查看详情 -->
    <AdTworderdetails ref="viewImageRef"></AdTworderdetails>
  </div>
</template>

<script setup lang="ts">
import Office from '@/assets/images/Office.png'
import Audio from '@/assets/images/Audio.png'
import Video from '@/assets/images/Video.png'
import Application from '@/assets/images/Application.png'
import { onMounted, reactive, ref } from 'vue'
import { IAxios } from 'axios'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { THgFileUploadParam, TUpFile } from '@/types/components/hgfileupload'
import { computTableHeight, tableHeaderColor, formatMoney } from '@/utils'

import { EStatus } from '@/types/common/enumindex'
import useUserStore from '@/store/modules/user'
import { FormInstance, FormRules, dayjs } from 'element-plus'
import {
  IOrderAndItemDTO,
  IOrderItemCostDTO,
  IOrderItemCostFilesDTO,
  TOrderAndItemVO
} from '@/types/views/business/orderitemcost'
import {
  deleteOrderItemCostByIdApi,
  getOrderAndItemPageListApi,
  saveOrderItemCostApi,
  updateOrderItemCostApi,
  updateOrderItemCostStatusApi
} from '@/api/business/orderitemcost'
import { IDataCombo } from '@/types/common/DataCombo'
import { getEnumCombo } from '@/api/common/index'
defineOptions({
  name: 'OrderitemCost'
})

/** 状态下拉 */
const statusCombo: IDataCombo[] = getEnumCombo(EStatus)
const global = getCurrentInstance()?.appContext.config.globalProperties
const user = useUserStore().user
const orderitemCostFormRef = ref<FormInstance>()
const total = ref(0)
/** 表格高度 */
const tableheight = ref(0)
/** 编辑窗口显示状态 */
const dialogEditVisible = ref(false)
/** 查看详情状态 */
const bDetailFlag = ref(false)
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
/** 查询条件 */
const queryParams = reactive<TOrderAndItemVO>({
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  orderitemid: ''
})
const orderItemCostInfo: IOrderItemCostDTO = {
  id: '',
  istatus: EStatus.待提交,
  namountcost: 0,
  sdescription: '',
  fileList: []
}
/** 是否提交审核 */
const bflagUpdate = ref(false)
/** 编辑实体 */
const orderItemCostForm = ref<IOrderItemCostDTO>({ ...orderItemCostInfo })
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<IOrderItemCostDTO>>({
  namountcost: [{ required: true, message: '请填写成本金额', trigger: 'blur' }]
})
/** Form选中的行 */
const multipleSelection = ref<IOrderItemCostDTO[]>([])
/** 上传组件参数 */
const hgfileuploadparam = ref<THgFileUploadParam>({
  server: import.meta.env.VITE_APP_BASE_API + '/common/UploadFile/importFile',
  multiple: true,
  filenumlimit: 20,
  storytypes: []
})

/** 订单详情列表对象 */
const orderAndItemList = ref<IOrderAndItemDTO[]>([])
/** 当前选择的订单详情 */
const curOrderAndItem = ref<IOrderAndItemDTO>()
/** 当前编辑form中不包括删除状态的文件列表 */
const curFileList = ref<IOrderItemCostFilesDTO[]>([])

onMounted(() => {
  handleQuery()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})

/**
 * 查询
 */
const handleQuery = () => {
  getOrderAndItemPageListApi(queryParams)
    .then((res: IAxios<IOrderAndItemDTO[]>) => {
      if (res.success) {
        orderAndItemList.value = res.obj
        total.value = res.total
      }
    })
    .finally(() => {})
}
/**
 * 新增
 */
const handleAdd = (row: IOrderAndItemDTO) => {
  curOrderAndItem.value = { ...row }
  resetForm(orderitemCostFormRef.value)
  orderItemCostForm.value = { ...orderItemCostInfo }
  orderItemCostForm.value.fileList = []
  curFileList.value = []
  orderItemCostForm.value.orderitemid = row.orderitemid
  dialogEditVisible.value = true
}
/**
 * 提交审核
 * @param row
 */
const onToCheck = (row: IOrderItemCostDTO) => {
  ElMessageBox.confirm('确定提交审核吗？提交后将不能进行修改', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      const data = {
        id: row.id!,
        istatus: EStatus.已提交
      }
      updateOrderItemCostStatusApi(data).then((res: IAxios) => {
        if (res.success) {
          ElMessage.success('操作成功')
          handleQuery()
          setTimeout(() => {
            curOrderAndItem.value = {
              ...orderAndItemList.value.find((item) => {
                return item.orderitemid === orderItemCostForm.value.orderitemid
              })
            }
          }, 500)
        }
      })
    })
    .catch(() => {})
}
/**
 * 编辑按钮事件
 * @param row
 */
const onEdit = (row: IOrderItemCostDTO) => {
  bDetailFlag.value = false
  orderItemCostForm.value = { ...row }
  curFileList.value = orderItemCostForm.value.fileList!.filter((file) => {
    return !file.bdelete
  })
}
/**
 * 查看详情按钮事件
 * @param row
 */
const onDetail = (row: IOrderItemCostDTO) => {
  bDetailFlag.value = true
  orderItemCostForm.value = { ...row }
  curFileList.value = orderItemCostForm.value.fileList!.filter((file) => {
    return !file.bdelete
  })
}
/**
 * 删除按钮事件
 * @param row
 */
const onDelete = (row: IOrderItemCostDTO | undefined) => {
  const ids: string[] = []
  if (row === undefined) {
    multipleSelection.value.forEach((item) => {
      if (item.istatus !== EStatus.待提交 && item.istatus !== EStatus.已退回) {
        ElMessage.warning('只允许删除未提交审核的数据')
        return
      }
      ids.push(item.id?.toString() as string)
    })
  } else {
    if (row.istatus !== EStatus.待提交 && row.istatus !== EStatus.已退回) {
      ElMessage.warning('只允许删除未提交审核的数据')
      return
    }
    ids.push(row.id?.toString() as string)
  }

  if (ids.length === 0) {
    ElMessage.warning('请选择要删除的数据')
    return
  }
  const data = {
    ids: ids.join(',')
  }
  ElMessageBox.confirm('是否删除选择的数据？', '提示', {
    type: 'warning'
  })
    .then(() => {
      deleteOrderItemCostByIdApi(data)
        .then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('删除成功!')
            handleQuery()
            setTimeout(() => {
              curOrderAndItem.value = {
                ...orderAndItemList.value.find((item) => {
                  return item.orderitemid === orderItemCostForm.value.orderitemid
                })
              }
            }, 500)
          }
        })
        .catch(() => {})
    })
    .catch(() => {})
}
/**
 * 获取上传后返回的文件列表
 * @param res
 */
const getUpFile = (res: IAxios<TUpFile>) => {
  console.log(res.success)
  if (res.success) {
    const index = orderItemCostForm.value.fileList!.findIndex((file) => {
      return file.sfileid === res.obj.sfileid
    })
    if (index === undefined || index === null || index === -1) {
      const workreport = res.obj as IOrderItemCostFilesDTO
      workreport.bdelete = false
      orderItemCostForm.value.fileList?.push(workreport)
    } else {
      orderItemCostForm.value.fileList![index].bdelete = false
    }
    curFileList.value = orderItemCostForm.value.fileList!.filter((file) => {
      return !file.bdelete
    })
  }
}
/**
 * 删除上传的文件，如果是新上传文件则直接删除，已经存入数据库中的则修改删除标记，用户数据库文件删除
 * @param index
 */
const onUpfileDel = (sfileid: string) => {
  const index = orderItemCostForm.value.fileList!.findIndex((file) => {
    return file.sfileid === sfileid
  })
  if (orderItemCostForm.value.fileList![index].id === undefined) {
    orderItemCostForm.value.fileList!.splice(index, 1)
  } else {
    orderItemCostForm.value.fileList![index].bdelete = true
  }
  curFileList.value = orderItemCostForm.value.fileList!.filter((file) => {
    return !file.bdelete
  })
}
/**
 * 编辑保存
 * @param formEl
 */
const saveorderItemCostForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      if (bflagUpdate.value) {
        orderItemCostForm.value.istatus = EStatus.已提交
      }
      const files = orderItemCostForm.value.fileList!.filter((file) => {
        return !file.bdelete
      })
      if (files.length <= 0) {
        ElMessage.warning('请上传证明文件')
        return
      }
      if (orderItemCostForm.value.id === '') {
        saveOrderItemCostApi(orderItemCostForm.value)
          .then((res: IAxios) => {
            if (res.success) {
              ElMessage.success('保存成功')
              handleQuery()
              onCancel()
              setTimeout(() => {
                curOrderAndItem.value = {
                  ...orderAndItemList.value.find((item) => {
                    return item.orderitemid === orderItemCostForm.value.orderitemid
                  })
                }
              }, 500)
            }
          })
          .catch(() => {})
      } else {
        updateOrderItemCostApi(orderItemCostForm.value)
          .then((res: IAxios) => {
            if (res.success) {
              ElMessage.success('更新保存成功')
              handleQuery()
              onCancel()
              setTimeout(() => {
                curOrderAndItem.value = {
                  ...orderAndItemList.value.find((item) => {
                    return item.orderitemid === orderItemCostForm.value.orderitemid
                  })
                }
              }, 500)
            }
          })
          .catch(() => {})
      }
    } else {
      console.log('error submit!', fields)
    }
  })
}
/**
 * 清空编辑数据
 */
const onCancel = () => {
  orderitemCostFormRef.value?.resetFields()
  orderItemCostForm.value.id = ''
  orderItemCostForm.value.istatus = 0
  orderItemCostForm.value.namountcost = 0
  orderItemCostForm.value.sdescription = ''
  orderItemCostForm.value.fileList = []
  curFileList.value = []
  bDetailFlag.value = false
}

/**
 * 重置form表单
 * @param formEl
 */
const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}
/**
 * 预览上传的文件
 * @param file
 */
const previewFile = (file: IOrderItemCostFilesDTO) => {
  console.log(file)
  if (file.sfiletype !== 'Office') {
    window.open(file.durl)
  } else if (file.sfiletype === 'Office') {
    const url = `${global?.officePreviewUrl}?src=${file.durl}`
    window.open(url)
  }
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
</style>
@/api/finance/orderitemcost
