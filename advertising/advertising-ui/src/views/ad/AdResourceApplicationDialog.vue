<!-- 广告资源申请表 dialog -->
<template>
  <!-- 编辑 -->
  <el-dialog
    v-model="dialogEditVisible"
    title="广告资源申请表"
    :width="800"
    append-to-body
    :destroy-on-close="true"
  >
    <div>
      <el-form
        ref="adResourceApplicationFormRef"
        :model="adResourceApplicationForm"
        :disabled="
          adResourceApplicationForm.iapprovestatus === EApproveStatus.在审 ||
          adResourceApplicationForm.iapprovestatus === EApproveStatus.通过
        "
        :rules="rules"
        label-width="140px"
        class="demo-workReportForm"
        status-icon
      >
        <!-- <el-form-item label="客户类型">
          <el-radio-group v-model="adResourceApplicationForm.icusttype">
            <el-radio
              v-for="customertype in customerTypeList"
              :key="customertype.id"
              :label="customertype.id"
              >{{ customertype.name }}</el-radio
            >
          </el-radio-group>
        </el-form-item> -->
        <el-form-item label="日期范围" required>
          <el-row :span="20">
            <el-col :span="10">
              <el-form-item prop="dstartdate">
                <el-date-picker
                  v-model="adResourceApplicationForm.dstartdate"
                  type="date"
                  placeholder="开始日期"
                  :clearable="false"
                  value-format="YYYY-MM-DD"
                  style="width: 240px"
                />
              </el-form-item>
            </el-col>
            <el-col class="text-center" :span="2">至</el-col>
            <el-col :span="10">
              <el-form-item prop="denddate">
                <el-date-picker
                  v-model="adResourceApplicationForm.denddate"
                  type="date"
                  placeholder="结束日期"
                  clearable
                  value-format="YYYY-MM-DD"
                  style="width: 240px"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="客户选择" prop="customerid">
          <el-row :span="21">
            <el-col :span="21">
              <el-input
                v-model="adResourceApplicationForm.customername"
                placeholder="请选择客户信息"
                class="inputwindht"
                style="width: 455px"
                @input="adResourceApplicationForm.customerid = ''"
              >
                <template #append>
                  <el-button
                    icon="Search"
                    circle
                    title="选择客户"
                    @click="dialogCustomerVisible = true"
                  />
                </template>
                <template #suffix>
                  <el-button
                    v-show="adResourceApplicationForm.customername"
                    link
                    icon="CircleClose"
                    @click="SeeCustomerdelete()"
                  ></el-button>
                </template>
              </el-input>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="广告标题" prop="sadtitle">
          <el-input
            v-model="adResourceApplicationForm.sadtitle"
            class="inputwindht"
            style="width: 455px"
          />
        </el-form-item>
        <el-form-item label="广告内容" prop="sadcontent">
          <el-input
            v-model="adResourceApplicationForm.sadcontent"
            style="width: 455px"
            :rows="3"
            type="textarea"
            class="inputwindht"
            resize="none"
            placeholder="请输入..."
          />
        </el-form-item>
        <el-form-item label="一审意见" prop="sfirstopinion">
          <el-input
            v-model="adResourceApplicationForm.sfirstopinion"
            style="width: 455px"
            :rows="2"
            type="textarea"
            class="inputwindht"
            resize="none"
            show-word-limit
            maxlength="200"
            placeholder="请输入..."
          />
        </el-form-item>
        <el-form-item label="文件上传">
          <el-row style="width: 100%">
            <el-col :span="24">
              <el-button type="primary" @click="onAddFile">附件上传</el-button>
            </el-col>
            <el-col :span="24">
              <el-table
                v-if="curFileList!.length > 0"
                ref="fileTableRef"
                :data="curFileList"
                highlight-current-row
                :header-cell-style="tableHeaderColor"
                border
              >
                <el-table-column
                  label="类型"
                  prop="ifilecategory"
                  min-width="100"
                  show-overflow-tooltip
                >
                  <template #default="scope">
                    <span
                      v-if="scope.row.ifilecategory === EFileCategory.广告合同"
                      style="color: #409eff"
                      >广告合同</span
                    >
                    <span
                      v-if="scope.row.ifilecategory === EFileCategory.广告资源"
                      style="color: #67c23a"
                      >广告资源</span
                    >
                    <span
                      v-if="scope.row.ifilecategory === EFileCategory.证明"
                      style="color: #b7768c"
                      >证明</span
                    >
                  </template>
                </el-table-column>
                <el-table-column
                  label="媒体"
                  prop="medianame"
                  min-width="100"
                  show-overflow-tooltip
                />
                <el-table-column
                  label="文件名"
                  prop="soriginalfile"
                  min-width="100"
                  show-overflow-tooltip
                >
                  <template #default="scope">
                    <el-link type="primary" :underline="false" @click="previewFile(scope.row)">
                      <el-icon>
                        <Document />
                      </el-icon>
                      {{ scope.row.soriginalfile }}
                    </el-link>
                  </template>
                </el-table-column>
                <el-table-column
                  label="说明"
                  prop="sdescription"
                  min-width="100"
                  show-overflow-tooltip
                />
                <el-table-column
                  label="操作"
                  align="left"
                  width="100"
                  class-name="small-padding fixed-width"
                  fixed="right"
                >
                  <template #default="scope">
                    <el-button
                      v-if="scope.row.iapprovestatus === EApproveStatus.待审"
                      link
                      type="danger"
                      icon="Delete"
                      size="small"
                      title="删除"
                      @click="onUpfileDel(scope.$index, adResourceApplicationForm.filelist)"
                      >删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-col>
          </el-row>
        </el-form-item>
        <!-- <el-form-item label="快速上版" prop="bquickly">
          <el-checkbox v-model="adResourceApplicationForm.bquickly"></el-checkbox>
        </el-form-item> -->
        <el-form-item v-if="flowTypeList.length > 1" label="选择审核流程" prop="flowid">
          <el-select
            v-model="adResourceApplicationForm.flowid"
            placeholder="请选择审核流程"
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="item in flowTypeList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <div style="position: absolute; right: 5px">
            <el-checkbox
              v-if="!props.hideApprovalCheckbox"
              v-model="bToCheckFlag"
              style="margin-right: 20px"
            >
              同时提交审核
            </el-checkbox>
            <el-button
              type="primary"
              icon="Check"
              @click="saveAdResourceApplicationForm(adResourceApplicationFormRef)"
              >保存
            </el-button>
            <el-button icon="Close" @click="dialogEditVisible = false">取消</el-button>
          </div>
        </el-form-item>
      </el-form>
      <!-- 选择客户 -->
      <el-dialog
        v-model="dialogCustomerVisible"
        title="选择客户"
        width="800"
        align-center
        :close-on-click-modal="false"
        :destroy-on-close="true"
      >
        <el-row :gutter="50">
          <el-col :span="24">
            <HgSalesCustomer @selectiondata="selectCustomer"></HgSalesCustomer>
          </el-col>
        </el-row>
      </el-dialog>
      <!-- 附件上传 -->
      <el-dialog
        v-model="dialogUpFileVisible"
        title="附件上传"
        width="800"
        align-center
        :close-on-click-modal="false"
        :destroy-on-close="true"
      >
        <el-row :gutter="50">
          <el-col :span="24">
            <el-form
              ref="adResourceFileFormRef"
              :model="fileDTO"
              :rules="rules"
              label-width="140px"
              class="demo-workReportForm"
              status-icon
            >
              <el-form-item label="附件类型">
                <!-- <el-radio-group v-model="fileDTO.ifilecategory">
                      <el-radio :label="1">广告合同</el-radio>
                      <el-radio :label="8">广告资源</el-radio>
                    </el-radio-group> -->
                <el-radio-group v-model="fileDTO.ifilecategory">
                  <el-radio
                    v-for="filecategory in fileCategoryList"
                    :key="filecategory.id"
                    :label="filecategory.id"
                    >{{ filecategory.name }}
                  </el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="使用媒体">
                <el-select v-model="fileDTO.mediaid" placeholder="媒体" style="width: 560px">
                  <el-option
                    v-for="item in mediaCombo"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="选择文件">
                <el-row width="100%">
                  <el-col :span="24">
                    <HgFileUpload
                      :server="hgfileuploadparam.server"
                      :accept="hgfileuploadparam.accept"
                      :multiple="true"
                      :storytypes="hgfileuploadparam.storytypes"
                      @getupfile="getUpFile"
                    ></HgFileUpload>
                  </el-col>
                  <el-col :span="24">
                    <el-table
                      v-if="fileList.length > 0"
                      :data="fileList"
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
                              <el-icon>
                                <Plus />
                              </el-icon>
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
                      <el-table-column width="60" :align="'center'">
                        <template #default="scope">
                          <li>
                            <el-button
                              type="danger"
                              link
                              @click="onUpfileDel(scope.$index, fileList)"
                            >
                              <el-icon title="删除">
                                <Delete />
                              </el-icon>
                            </el-button>
                          </li>
                        </template>
                      </el-table-column>
                    </el-table>
                  </el-col>
                </el-row>
              </el-form-item>
              <el-form-item>
                <div style="position: absolute; right: 5px">
                  <el-button
                    type="primary"
                    icon="Check"
                    @click="onSaveFileList(adResourceFileFormRef)"
                    >确定
                  </el-button>
                  <el-button icon="Close" @click="dialogUpFileVisible = false">取消</el-button>
                </div>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
      </el-dialog>
    </div>
  </el-dialog>
</template>

<script setup lang="ts">
import { EApproveStatus, ECustomerType, EFileCategory } from '@/types/common/enumindex'
import { required, tableHeaderColor } from '@/utils'
import Audio from '@/assets/images/Audio.png'
import Office from '@/assets/images/Office.png'
import Application from '@/assets/images/Application.png'
import Video from '@/assets/images/Video.png'
import { ref } from 'vue'
import { dayjs, FormInstance, FormRules } from 'element-plus'
import {
  IAdResourceApplicationDTO,
  ResourceApplicationDialogMode
} from '@/types/views/ad/adresourceapplication'
import useUserStore from '@/store/modules/user'
import { TAdCustomer } from '@/types/views/customer'
import { IAdResourceFilesDTO } from '@/types/views/ad/adresourcefiles'
import { IAxios } from 'axios'
import { THgFileUploadParam, TUpFile } from '@/types/components/hgfileupload'
import { IDataCombo } from '@/types/common/DataCombo'
import { getEnumCombo } from '@/api/common'
import {
  saveAdResourceApplicationApi,
  updateAdResourceApplicationApi
} from '@/api/ad/adresourceapplication'
import { Tadindustrylist } from '@/types/components/hgindustry'
import { getMediaDataComboApi } from '@/api/media/media'

defineOptions({
  name: 'AdResourceApplicationDialog'
})

const props = defineProps({
  /** 隐藏同时提交审核checkbox */
  hideApprovalCheckbox: {
    type: Boolean,
    default: false
  }
  // mode: {
  //   type: String as PropType<ResourceApplicationDialogMode>,
  //   default: ResourceApplicationDialogMode.Add
  // },
  // givenFlowTypeList: {
  //   type: Array as PropType<Tadindustrylist[]>,
  //   default: []
  // },
  // row: {
  //   type: Object as PropType<IAdResourceApplicationDTO>,
  //   default: () => ({})
  // }
})

/** 关闭弹出框时,调用父组件的方法 */
const emit = defineEmits(['onCloseDialog'])
const user = useUserStore().user
const global = getCurrentInstance()?.appContext.config.globalProperties
/** 编辑窗口显示状态 */
const dialogEditVisible = ref(false)
const adResourceApplicationFormRef = ref<FormInstance>()
/** 选择客户显示状态 */
const dialogCustomerVisible = ref(false)
const adResourceApplicationInfo: IAdResourceApplicationDTO = {
  id: '',
  icusttype: ECustomerType.直接客户,
  deptid: user!.deptid!.toString(),
  deptname: user!.deptname!,
  employid: user!.userid,
  employname: user!.username,
  dstartdate: dayjs().format('YYYY-MM-DD'),
  denddate: '',
  sadtitle: '',
  sadcontent: '',
  sremark: '',
  customerid: '',
  customername: '',
  bquickly: false,
  iapprovestatus: EApproveStatus.待审,
  filelist: []
}
/** 编辑实体 */
const adResourceApplicationForm = ref<IAdResourceApplicationDTO>({ ...adResourceApplicationInfo })
/** 当前编辑文件列表 */
const curFileList = ref<IAdResourceFilesDTO[]>([])
/** 上传的文件列表 */
// const uploadFileList = ref<TUpFile[]>([])
/** 同时提交审核标记 */
const bToCheckFlag = ref(false)
/** 文件上传窗口显示状态 */
const dialogUpFileVisible = ref(false)
/** 附件对象 */
const fileinfo: IAdResourceFilesDTO = {
  id: '',
  mediaid: '',
  ifilecategory: 1,
  sfileid: '',
  soriginalfile: '',
  sfiletype: '',
  bdelete: false
}
/** 附件对象 */
const fileDTO = ref<IAdResourceFilesDTO>({ ...fileinfo })
/** 客户类型下拉列表 */
const customerTypeList: IDataCombo[] = getEnumCombo(ECustomerType)
/** 文件类型下拉列表 */
const fileCategoryList: IDataCombo[] = getEnumCombo(EFileCategory)
const adResourceFileFormRef = ref<FormInstance>()
/** 流程类型列表 */
const flowTypeList = ref<Tadindustrylist[]>([])
/** 媒体下拉列表 */
const mediaCombo = ref<IDataCombo[]>([])
/** 上传组件参数 */
const hgfileuploadparam = ref<THgFileUploadParam>({
  server: import.meta.env.VITE_APP_BASE_API + '/common/UploadFile/importFile',
  multiple: true,
  filenumlimit: 20,
  storytypes: []
})
/** 附件列表 */
const fileList = ref<IAdResourceFilesDTO[]>([])
/** 当前的编辑模式 */
const currentMode = ref<ResourceApplicationDialogMode>()

/**
 * 校验设置参数
 */
const rules = reactive<FormRules<IAdResourceApplicationDTO>>({
  icusttype: [
    { validator: required, required: true, message: '请选择客户类型', trigger: 'change' }
  ],
  sadtitle: [{ validator: required, required: true, message: '请填写广告标题', trigger: 'blur' }],
  employid: [{ validator: required, required: true, message: '请选择人员', trigger: 'change' }],
  customerid: [{ validator: required, required: true, message: '请选择客户', trigger: 'change' }],
  dstartdate: [{ required: true, message: '开始日期不能为空', trigger: 'change' }]
})

/**
 * dialog 初始化并显示
 * @param mode ResourceApplicationDialogMode dialog 模式
 * @param givenFlowTypeList 父组件传来的流程类型列表
 * @param row 资源数据
 */
const init = (
  mode: ResourceApplicationDialogMode,
  givenFlowTypeList: Tadindustrylist[],
  row?: IAdResourceApplicationDTO
) => {
  fileList.value = []
  adResourceApplicationInfo.filelist = []
  adResourceApplicationForm.value.filelist = []
  resetForm(adResourceApplicationFormRef.value)
  flowTypeList.value = givenFlowTypeList
  currentMode.value = mode
  if (mode === ResourceApplicationDialogMode.Add) {
    adResourceApplicationForm.value = { ...adResourceApplicationInfo }
    curFileList.value = []
  }
  if (mode === ResourceApplicationDialogMode.Edit) {
    adResourceApplicationForm.value = { ...row! }
    curFileList.value = row!.filelist as IAdResourceFilesDTO[]
  }
  if (mode === ResourceApplicationDialogMode.PreOrderAdd) {
    adResourceApplicationForm.value = { ...row! }
    curFileList.value = []
  }
  if (mode === ResourceApplicationDialogMode.PreOrderEdit) {
    adResourceApplicationForm.value = { ...row! }
    curFileList.value = row!.filelist as IAdResourceFilesDTO[]
  }
  dialogEditVisible.value = true
  // 获取媒体下拉列表
  getMediaDataComboApi().then((res: IAxios<IDataCombo[]>) => {
    if (res.success) {
      mediaCombo.value = res.obj
    }
  })
}

/**
 * 编辑保存
 * @param formEl
 */
const saveAdResourceApplicationForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      if (!bToCheckFlag.value) {
        adResourceApplicationForm.value.flowid = ''
      } else if (flowTypeList.value.length === 1) {
        adResourceApplicationForm.value.flowid = flowTypeList.value[0].id
      }
      curFileList.value = adResourceApplicationForm.value.filelist?.filter(
        (item) => !item.bdelete
      ) as IAdResourceFilesDTO[]
      if (curFileList.value.length === 0) {
        ElMessage.warning('请上传附件')
        return
      }
      // 快速预约时,附件必须同时包含广告资源和证明
      if (
        currentMode.value === ResourceApplicationDialogMode.PreOrderAdd ||
        currentMode.value === ResourceApplicationDialogMode.PreOrderEdit
      ) {
        let c1 = 0
        let c2 = 0
        curFileList.value.forEach((item) => {
          if ((item.ifilecategory = EFileCategory.广告资源)) c1++
          if ((item.ifilecategory = EFileCategory.证明)) c2++
        })
        if (c1 < 1) {
          ElMessage.warning('请上传广告资源附件')
          return
        }
        if (c2 < 1) {
          ElMessage.warning('请上传证明附件')
          return
        }
      }
      if (bToCheckFlag.value) {
        ElMessageBox.confirm('确定提交审核吗？提交后将不能进行修改', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            saveForm()
          })
          .catch(() => {
            return
          })
      } else {
        saveForm()
      }
    } else {
      console.log('error submit!', fields)
    }
  })
}
/**
 * 保存
 */
const saveForm = () => {
  if (adResourceApplicationForm.value.id === '') {
    saveAdResourceApplicationApi(adResourceApplicationForm.value)
      .then((res: IAxios) => {
        if (res.success) {
          ElMessage.success('保存成功')
          emit('onCloseDialog', res.obj)
          adResourceApplicationFormRef.value?.resetFields()
          fileList.value = []
          adResourceApplicationInfo.filelist = []
          adResourceApplicationForm.value = { ...adResourceApplicationInfo }
          dialogEditVisible.value = false
        }
      })
      .catch(() => {})
  } else {
    updateAdResourceApplicationApi(adResourceApplicationForm.value)
      .then((res: IAxios) => {
        if (res.success) {
          ElMessage.success('更新保存成功')
          emit('onCloseDialog')
          adResourceApplicationFormRef.value?.resetFields()
          fileList.value = []
          adResourceApplicationInfo.filelist = []
          adResourceApplicationForm.value = { ...adResourceApplicationInfo }
          dialogEditVisible.value = false
        }
      })
      .catch(() => {})
  }
}

/**
 * 选择客户信息后返回接收
 * @param val
 */
const selectCustomer = (val: TAdCustomer) => {
  adResourceApplicationForm.value.customerid = val.id!
  adResourceApplicationForm.value.customername = val.sname!
  adResourceApplicationForm.value.icusttype = val.itype!
  adResourceApplicationForm.value.customername = val.sname!
  dialogCustomerVisible.value = false
}

/**
 * 删除上传的文件，如果是新上传文件则直接删除，已经存入数据库中的则修改删除标记，用户数据库文件删除
 * @param index
 */
const onUpfileDel = (index: number, FileList: IAdResourceFilesDTO[] | undefined) => {
  console.log(index)
  if (FileList![index].id === undefined) {
    FileList!.splice(index, 1)
  } else {
    FileList![index].bdelete = true
  }
  curFileList.value = adResourceApplicationForm.value.filelist?.filter(
    (item) => !item.bdelete
  ) as IAdResourceFilesDTO[]
}

/**
 * 保存上传的文件列表
 * @param formEl
 */
const onSaveFileList = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid, fields) => {
    if (valid) {
      // if (fileDTO.value.ifilecategory === EFileCategory.广告资源 && fileDTO.value.mediaid === '') {
      //   ElMessage.warning('请选择媒体')
      //   return
      // }

      if (adResourceApplicationForm.value.filelist === undefined) {
        adResourceApplicationForm.value.filelist = []
      }
      fileList.value.forEach((item) => {
        const index = adResourceApplicationForm.value.filelist?.findIndex((file) => {
          return (
            file.sfileid === item.sfileid &&
            file.ifilecategory === item.ifilecategory &&
            file.mediaid === item.mediaid
          )
        })
        if (index === undefined || index === null || index === -1) {
          // item.ifilecategory = fileDTO.value.ifilecategory
          // item.mediaid = fileDTO.value.mediaid
          // item.medianame = mediaCombo.value.find((media) => media.id === fileDTO.value.mediaid)?.name!
          adResourceApplicationForm.value.filelist?.push(item)
        } else {
          if (adResourceApplicationForm.value.filelist![index].bdelete !== undefined) {
            adResourceApplicationForm.value.filelist![index].bdelete = false
          }
        }
      })
      // dialogUpFileVisible.value = false
      formEl.resetFields()
      fileDTO.value = { ...fileinfo }
      fileList.value = []
      curFileList.value = adResourceApplicationForm.value.filelist?.filter(
        (item) => !item.bdelete
      ) as IAdResourceFilesDTO[]
    } else {
      console.log('error submit!', fields)
    }
  })
}

/**
 * 获取上传后返回的文件列表
 * @param res
 */
const getUpFile = (res: IAxios<TUpFile>) => {
  console.log(res.success)
  if (res.success) {
    const index = adResourceApplicationForm.value.filelist?.findIndex((file) => {
      return (
        file.sfileid === res.obj.sfileid &&
        file.ifilecategory === fileDTO.value.ifilecategory &&
        file.mediaid === fileDTO.value.mediaid
      )
    })
    if (index === undefined || index === null || index === -1) {
      const adResourcApplication = res.obj as IAdResourceFilesDTO
      adResourcApplication.bdelete = false
      adResourcApplication.ifilecategory = fileDTO.value.ifilecategory
      adResourcApplication.mediaid = fileDTO.value.mediaid
      adResourcApplication.iapprovestatus = EApproveStatus.待审
      adResourcApplication.medianame = mediaCombo.value.find(
        (item) => item.id === fileDTO.value.mediaid
      )?.name!
      fileList.value?.push(adResourcApplication)
    } else {
      ElMessage.warning('该文件已上传')
      return
    }
    curFileList.value = adResourceApplicationForm.value.filelist?.filter(
      (item) => !item.bdelete
    ) as IAdResourceFilesDTO[]
  }
}

/**
 * 附件上传按钮事件
 */
const onAddFile = () => {
  dialogUpFileVisible.value = true
  adResourceFileFormRef.value?.resetFields()
  fileDTO.value = { ...fileinfo }
  fileList.value = []
}

/**
 * 预览上传的文件
 * @param file
 */
const previewFile = (file: IAdResourceFilesDTO) => {
  console.log(file)
  if (file.sfiletype === 'Office') {
    const url = `${global?.officePreviewUrl}?src=${file.durl}`
    window.open(url)
  } else {
    window.open(file.durl)
  }
}

/** 修改时删除客户列表信息 */
const SeeCustomerdelete = () => {
  adResourceApplicationForm.value.customername = ''
  adResourceApplicationForm.value.customerid = ''
}

/**
 * 重置form表单
 * @param formEl
 */
const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}
defineExpose({ init })
</script>
