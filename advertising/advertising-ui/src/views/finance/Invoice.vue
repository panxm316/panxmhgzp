<!--
 * @Author: suny
 * @Date: 2023-12-28 10:22:08
 * @LastEditTime: 2024-01-29 11:02:13
 * @LastEditors: suny suny@hgzp.com.cn
 * @Description: 发票管理
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button type="primary" icon="Back" @click="handleUpfile">批量上传</el-button>
        <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
        <!-- <el-input
          v-model="queryParams.customername"
          placeholder="请选择客户信息"
          style="width: 200px"
          clearable
          @input="queryParams.customerid = ''"
        >
          <template #append>
            <el-button
              icon="Search"
              circle
              title="选择客户"
              @click="dialogCustomerVisible = true"
            />
          </template>
        </el-input> -->
        <el-input
          v-model="queryParams.queryKey"
          clearable
          placeholder="请输入发票号"
          class="input-with-select"
          style="width: 200px"
          @keyup.enter="handleQuery"
          @clear="handleQuery"
        >
        </el-input>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="preInvoiceTableRef"
        :data="invoiceList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        :border="true"
      >
        <el-table-column label="客户" prop="customername" min-width="120" show-overflow-tooltip />
        <el-table-column label="客户属性" prop="icusttype" width="100" show-overflow-tooltip>
          <template #default="scope">
            <span v-if="scope.row.icusttype == ECustomerType.直接客户"> 直接客户 </span>
            <span v-if="scope.row.icusttype == ECustomerType.代理公司"> 代理公司 </span>
            <span v-if="scope.row.icusttype == ECustomerType.内容生产方"> 内容生产方 </span>
          </template>
        </el-table-column>
        <el-table-column label="业务员" prop="employname" width="100" show-overflow-tooltip />
        <el-table-column label="申请日期" prop="dapplytime" width="100">
          <template #default="scope">
            <span>{{ dayjs(scope.row.dapplytime).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="发票类型" prop="itype" width="100" :align="'center'">
          <template #default="scope">
            <span v-if="scope.row.itype == EPreinvoiceApplyType.手开"> 手开 </span>
            <span v-if="scope.row.itype == EPreinvoiceApplyType.预开"> 预开 </span>
            <span v-if="scope.row.itype == EPreinvoiceApplyType.挂开"> 挂开 </span>
            <span v-if="scope.row.itype == EPreinvoiceApplyType.冲红"> 冲红 </span>
            <span v-if="scope.row.itype == EPreinvoiceApplyType.预收费"> 预收费 </span>
            <span v-if="scope.row.itype == EPreinvoiceApplyType.预开完成"> 预开完成 </span>
          </template>
        </el-table-column>
        <el-table-column label="发票样式" prop="istype" width="100" :align="'center'">
          <template #default="scope">
            <span v-if="scope.row.istype == EPreinvoiceStyle.专用发票"> 专用发票 </span>
            <span v-if="scope.row.istype == EPreinvoiceStyle.普通发票"> 普通发票 </span>
            <span v-if="scope.row.istype == EPreinvoiceStyle.电子发票"> 电子发票 </span>
            <span v-if="scope.row.istype == EPreinvoiceStyle.数电专票"> 数电专票 </span>
            <span v-if="scope.row.istype == EPreinvoiceStyle.数电普票"> 数电普票 </span>
          </template>
        </el-table-column>
        <el-table-column label="发票状态" prop="istatus" width="100" :align="'center'">
          <template #default="scope">
            <span v-if="scope.row.istatus == EInvoiceStatus.有效">有效</span>
            <span v-if="scope.row.istatus == EInvoiceStatus.无效">无效</span>
            <span v-if="scope.row.istatus == EInvoiceStatus.冲红退回">冲红退回</span>
          </template>
        </el-table-column>
        <el-table-column label="金额" prop="namount" min-width="120" :align="'right'">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namount, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="付款方式" prop="paymethodname" width="120" show-overflow-tooltip />
        <el-table-column label="开票项目" prop="printitemname" width="100" />
        <el-table-column label="税收编码" prop="staxcode" min-width="180" show-overflow-tooltip />
        <el-table-column label="发票号" prop="invoice" width="140" show-overflow-tooltip />
        <el-table-column label="发票编码" prop="invoicecode" width="120" show-overflow-tooltip />
        <el-table-column label="收款人" prop="scashier" width="120" show-overflow-tooltip />
        <el-table-column label="复核人" prop="schecker" width="120" show-overflow-tooltip />
        <el-table-column
          label="操作"
          align="center"
          width="160"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              v-if="scope.row.invoicecode === ''"
              link
              title="修改"
              type="success"
              size="small"
              icon="Edit"
              @click="onEdit(scope.row)"
              >修改</el-button
            >
            <el-button
              link
              type="primary"
              icon="Printer"
              size="small"
              title="重新打印"
              @click="handlePrint(scope.row)"
              >打印</el-button
            >
            <el-button
              v-if="!scope.row.sinvoicefilesha1 && scope.row.invoicecode !== ''"
              link
              type="primary"
              icon="Upload"
              size="small"
              title="上传发票"
              @click="onUpload(scope.row)"
              >上传</el-button
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
    <!-- 打印 -->
    <el-dialog
      v-model="dialogPrintShow"
      title="发票打印"
      width="1100"
      append-to-body
      destroy-on-close
      draggable
      :close-on-click-modal="false"
    >
      <HgInvoicePrint :invoiceid="invoiceID"></HgInvoicePrint>
    </el-dialog>
    <!-- 修改 -->
    <el-dialog
      v-model="dialogEditShow"
      title="编辑"
      width="600"
      append-to-body
      destroy-on-close
      draggable
      :close-on-click-modal="false"
    >
      <el-row style="padding: 0 20px">
        <el-col :span="24">
          <el-form
            ref="invoiceEditFormRef"
            :model="invoiceEditForm"
            :rules="rules"
            label-width="100px"
            label-position="right"
          >
            <el-form-item label="发票号" prop="invoice">
              <el-input
                v-model="invoiceEditForm.invoice"
                style="width: 400px"
                placeholder="请输入发票号"
                minlength="8"
                maxlength="8"
              ></el-input>
            </el-form-item>
            <el-form-item label="发票编码" prop="invoicecode">
              <el-input
                v-model="invoiceEditForm.invoicecode"
                style="width: 400px"
                placeholder="请输入发票编码"
                minlength="12"
                maxlength="12"
                controls="false"
              ></el-input>
            </el-form-item>
            <div style="width: 100%; text-align: right">
              <el-button type="primary" icon="Check" @click="saveEditForm(invoiceEditFormRef)"
                >保存</el-button
              >
              <el-button icon="Close" @click="dialogEditShow = false">取消</el-button>
            </div>
          </el-form>
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 发票文件管理 -->
    <el-dialog
      v-model="dialogFileShow"
      title="发票文件管理"
      :width="1000"
      append-to-body
      :destroy-on-close="true"
    >
      <div style="padding: 0 20px">
        <el-row>
          <el-col :span="24">
            <el-table
              ref="invoiceFileTableRef"
              :data="curInvioce!.fileList"
              highlight-current-row
              :border="true"
              style="max-height: 400px; overflow-y: auto"
            >
              <el-table-column label="创建人" prop="createempname" width="100" />
              <el-table-column label="创建日期" prop="dcreatedate" width="100">
                <template #default="scope">
                  <span>{{ dayjs(scope.row.dcreatedate).format('YYYY-MM-DD') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="发票号" prop="invoice" />
              <el-table-column label="文件格式" prop="sfileformat" width="100" />
              <el-table-column label="文件名" prop="soriginalfile" />
              <el-table-column label="说明" prop="sdescription" />
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
          </el-col>
        </el-row>
        <el-row style="margin-top: 20px">
          <el-col :span="24">
            <el-form ref="invoiceFileFormRef" class="demo-curInvioce" status-icon>
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
                      <span style="color: #dbdbdb; border: none"
                        >支持扩展名：.pdf .zip .ofd，且每种格式只支持一个文件</span
                      >
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
                            <!-- <div class="name-wrapper">
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
                            </div> -->
                          </template>
                        </el-table-column>
                        <el-table-column v-if="!bDetailFlag" width="60" :align="'center'">
                          <template #default="scope">
                            <li>
                              <el-button
                                type="danger"
                                link
                                @click="onUpfileDel(scope.$index, scope.row.sfileid)"
                              >
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
              <div style="width: 100%; text-align: right">
                <el-button v-if="!bDetailFlag" type="primary" icon="Check" @click="saveFileForm"
                  >保存</el-button
                >
                <el-button icon="Close" @click="onCancel">重置</el-button>
              </div>
            </el-form>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
    <!-- 发票文件批量上传 -->
    <el-dialog
      v-model="dialogAllFileShow"
      title="发票文件管理"
      :width="1000"
      append-to-body
      :destroy-on-close="true"
    >
      <div style="padding: 0 20px">
        <el-row>
          <el-col :span="24">
            <el-form
              ref="invoiceAllFileFormRef"
              label-width="100px"
              class="demo-curInvioce"
              status-icon
            >
              <el-form-item label="选择文件">
                <div style="width: 100%">
                  <el-row width="100%">
                    <el-col :span="24">
                      <HgFileUpload
                        :server="hgfileuploadparam.server"
                        :accept="hgfileuploadparam.accept"
                        :multiple="true"
                        :storytypes="hgfileuploadparam.storytypes"
                        @getupfile="getUpFile"
                      ></HgFileUpload>
                      <span style="color: #dbdbdb; border: none"
                        >支持扩展名：.pdf .zip .ofd，且每种格式只支持一个文件</span
                      >
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
                            <!-- <div class="name-wrapper">
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
                            </div> -->
                          </template>
                        </el-table-column>
                        <el-table-column v-if="!bDetailFlag" width="60" :align="'center'">
                          <template #default="scope">
                            <li>
                              <el-button
                                type="danger"
                                link
                                @click="onUpfileDel(scope.$index, scope.row.sfileid)"
                              >
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
              <div style="width: 100%; text-align: right">
                <el-button type="primary" icon="Check" @click="saveAllFileForm">保存</el-button>
                <el-button icon="Close" @click="onCancel">重置</el-button>
              </div>
            </el-form>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import Office from '@/assets/images/Office.png'
import Audio from '@/assets/images/Audio.png'
import Video from '@/assets/images/Video.png'
import Application from '@/assets/images/Application.png'
import HgInvoicePrint from '@/components/HgInvoicePrint/index.vue'
import useUserStore from '@/store/modules/user'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { IAxios } from 'axios'
import {
  ECustomerType,
  EPreinvoiceStyle,
  EPreinvoiceApplyType,
  EStatus,
  EInvoiceStatus
} from '@/types/common/enumindex'
import { computTableHeight, tableHeaderColor, formatMoney } from '@/utils'
import { dayjs } from 'element-plus'
import { TInvoiceQuery, IInvoiceDTO, IInvoiceFilesDTO } from '@/types/views/finance/invoice'
import { saveInvoiceCodeApi } from '@/api/finance/invoiceprint'
import { THgFileUploadParam, TUpFile } from '@/types/components/hgfileupload'
import {
  getInvoicePageListApi,
  deleteInvoiceFilesByIdsApi,
  updateInvoiceByInvoiceDTOApi,
  saveInfoiceFilesApi
} from '@/api/finance/invoice'
import type { FormInstance, FormRules } from 'element-plus'

const invoiceEditFormRef = ref<FormInstance>()
const global = getCurrentInstance()?.appContext.config.globalProperties
const user = useUserStore().user
/** 打印窗口显示状态 */
const dialogPrintShow = ref(false)
/** 编辑窗口显示状态 */
const dialogEditShow = ref(false)
/** 发票文件管理窗口显示状态 */
const dialogFileShow = ref(false)
/** 批量上传窗口显示状态 */
const dialogAllFileShow = ref(false)
/** 当前选择的预开发票对象 */
const curInvioce = ref<IInvoiceDTO>()
const total = ref(0)
/** 表格高度 */
const tableheight = ref(0)
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
const queryParams = reactive<TInvoiceQuery>({
  sort: 'dcreatetime',
  order: 'desc',
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: ''
})
/** 列表对象 */
const invoiceList = ref<IInvoiceDTO[]>([])
/** 选择打印时返回的发票表id */
const invoiceID = ref('')
// /** 发票号 */
// const invoice = ref('')
// /** 发票编码 */
// const invoiceCode = ref('')
/** 上传文件列表 */
const bDetailFlag = ref(false)
/** 当前编辑的文件列表 */
const curFileList = ref<IInvoiceFilesDTO[]>([])
/** 选择的文件列表 */
const multipleSelection = ref<IInvoiceFilesDTO[]>([])
/** 上传组件参数 */
const hgfileuploadparam = ref<THgFileUploadParam>({
  server: import.meta.env.VITE_APP_BASE_API + '/common/UploadFile/importFile',
  multiple: true,
  accept: {
    title: 'all',
    extensions: 'pdf,ofd,zip',
    mimeTypes: '.pdf,.ofd,.zip'
  },
  filenumlimit: 20,
  storytypes: []
})
interface EditForm {
  /** 发票号 */
  invoice: string
  /** 发票编码 */
  invoicecode: string
}
/** 修改发票号form对象 */
const invoiceEditForm = reactive<EditForm>({
  invoice: '',
  invoicecode: ''
})

// 定义校验规则
const validatePositiveInteger = (rules: any, value: any, callback: any) => {
  console.log(value)
  const reg = /^[1-9]\d*$/
  if (reg.test(value)) {
    callback()
  } else {
    callback(new Error('请输入正整数'))
  }
}
const rules = reactive<FormRules<EditForm>>({
  invoice: [
    { required: true, message: '请输入发票号', trigger: 'blur' },
    { min: 8, max: 8, message: '发票号长度为8', trigger: 'blur' }
  ],
  invoicecode: [
    {
      required: true,
      message: '请输入发票编码',
      trigger: 'blur'
    },
    { validator: validatePositiveInteger, message: '发票编码必须是数字', trigger: 'blur' },
    { min: 12, max: 12, message: '发票编码长度为12', trigger: 'blur' }
  ]
})

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
  getInvoicePageListApi(queryParams)
    .then((res: IAxios<IInvoiceDTO[]>) => {
      if (res.success) {
        invoiceList.value = res.obj
        total.value = res.total
      }
    })
    .finally(() => {})
}
/**
 * 发票文件批量上传
 */
const handleUpfile = () => {
  dialogAllFileShow.value = true
  curFileList.value = []
}
/**
 * 打印按钮
 * @param row
 */
const handlePrint = (row: IInvoiceDTO) => {
  invoiceID.value = row.id!
  dialogPrintShow.value = true
}
/**
 * 上传按钮事件
 * @param row
 */
const onUpload = (row: IInvoiceDTO) => {
  curInvioce.value = row
  invoiceID.value = row.id!
  curFileList.value = []
  dialogFileShow.value = true
}
/**
 * 清空编辑数据
 */
const onCancel = () => {
  curFileList.value = []
  bDetailFlag.value = false
}
/**
 * 编辑
 * @param row
 */
const onEdit = (row: IInvoiceDTO) => {
  invoiceID.value = row.id!
  invoiceEditForm.invoice = ''
  invoiceEditForm.invoicecode = ''
  dialogEditShow.value = true
}
/**
 * 保存修改
 */
const saveEditForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      if (invoiceEditForm.invoice === '') {
        ElMessage.warning('请输入发票号')
        return
      }
      if (invoiceEditForm.invoicecode === '') {
        ElMessage.warning('请输入发票编码')
        return
      }
      const data = {
        invoiceId: invoiceID.value,
        invoice: invoiceEditForm.invoice,
        invoiceCode: invoiceEditForm.invoicecode
      }
      saveInvoiceCodeApi(data).then((res: IAxios) => {
        if (res.success) {
          invoiceID.value = ''
          invoiceEditForm.invoice = ''
          invoiceEditForm.invoicecode = ''
          dialogEditShow.value = false
          handleQuery()
        }
      })
    } else {
      console.log('error submit!', fields)
    }
  })
}
/**
 * 删除按钮事件
 * @param row
 */
const onDelete = (row: IInvoiceFilesDTO | undefined) => {
  const ids: string[] = []
  if (row === undefined) {
    multipleSelection.value.forEach((item) => {
      ids.push(item.id?.toString() as string)
    })
  } else {
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
      deleteInvoiceFilesByIdsApi(data)
        .then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('删除成功!')
            handleQuery()
            setTimeout(() => {
              curInvioce.value = {
                ...invoiceList.value.find((item) => {
                  return item.id === curInvioce.value!.id
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
    let index
    if (!dialogAllFileShow.value) {
      index = curInvioce.value!.fileList!.findIndex((file) => {
        // 每个格式的文件只能上传一个
        return file.sfileid === res.obj.sfileid || file.sfileformat === res.obj.sfileformat
      })
    } else {
      index = curFileList.value.findIndex((file) => {
        // 每个格式的文件只能上传一个
        return file.sfileid === res.obj.sfileid || file.sfileformat === res.obj.sfileformat
      })
    }

    if (index === undefined || index === null || index === -1) {
      const workreport = res.obj as IInvoiceFilesDTO
      const filenames = workreport.soriginalfile!.split('_')
      if (filenames.length < 2) {
        ElMessage.warning('文件名格式不正确')
        return
      }
      if (!dialogAllFileShow.value) {
        let invoicename = ''
        if (workreport.sfileformat === 'zip') {
          invoicename = filenames[1]
        } else {
          invoicename = filenames[0]
        }
        const invoicecode = invoicename.substring(0, 12)
        const invoice = invoicename.substring(12, 20)
        if (
          invoicecode !== curInvioce.value!.invoicecode ||
          invoice !== curInvioce.value!.invoice
        ) {
          ElMessage.warning('文件名中发票号或者发票编码与当前不一致')
          return
        }
      }
      workreport.bdelete = false
      curFileList.value.push(workreport)
    } else {
      ElMessage.warning('文件已存在')
      return
    }
  }
}
/**
 * 删除上传的文件，如果是新上传文件则直接删除，已经存入数据库中的则修改删除标记，用户数据库文件删除
 * @param index
 */
const onUpfileDel = (index: number, sfileid: string) => {
  curFileList.value.splice(index, 1)
}
/**
 * 预览上传的文件
 * @param file
 */
const previewFile = (file: IInvoiceFilesDTO) => {
  console.log(file)
  if (file.sfiletype !== 'Office') {
    window.open(file.durl)
  } else if (file.sfiletype === 'Office') {
    const url = `${global?.officePreviewUrl}?src=${file.durl}`
    window.open(url)
  }
}
/**
 * 批量上传发票文件保存
 */
const saveAllFileForm = () => {
  if (curFileList.value.length === 0) {
    ElMessage.warning('请上传发票文件')
    return
  }
  saveInfoiceFilesApi(curFileList.value!).then((res: IAxios) => {
    if (res.success) {
      curFileList.value = []
      handleQuery()
    }
  })
}
/**
 * 保存上传的文件
 */
const saveFileForm = () => {
  if (curFileList.value.length === 0) {
    ElMessage.warning('请上传发票文件')
    return
  }
  curFileList.value.forEach((item) => {
    curInvioce.value!.fileList!.push(item)
  })
  updateInvoiceByInvoiceDTOApi(curInvioce.value!).then((res: IAxios) => {
    if (res.success) {
      curFileList.value = []
      bDetailFlag.value = false
      handleQuery()
      setTimeout(() => {
        curInvioce.value = {
          ...invoiceList.value.find((item) => {
            return item.id === curInvioce.value!.id
          })
        }
      }, 500)
    }
  })
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
</script>

<style lang="scss" scoped></style>
