<!--
 * @Author: wanghl
 * @Date: 2023-11-30 09:44:40
 * @LastEditTime: 2024-03-15 15:17:44
 * @LastEditors: wanghl
 * @Description:AdTworder广告预定添加
-->
<template>
  <el-dialog
    v-model="dialogVisible"
    title="订单修改"
    :width="fenbianlv() > 1440 ? '80%' : '94%'"
    :z-index="9999"
    @close="dialogVisible = false"
  >
    <div class="app-container" style="background-color: #ffffff; margin: -35px 8px 8px">
      <div style="width: 100%">
        <el-form ref="TwordwrFormRef" :model="TworderData" label-width="120px" :rules="rules">
          <el-collapse v-model="activeNames" style="width: 100%">
            <el-collapse-item name="1">
              <template #title>
                广告订单数据{{ FlowManagerdata }}
                <el-icon class="header-icon">
                  <info-filled />
                </el-icon>
              </template>
              <el-row :gutter="22" style="padding-right: 40px">
                <el-col :span="8">
                  <el-form-item label="订单类别" prop="iorderkind">
                    {{ TworderData.iorderkind === 0 ? '本部广告' : '' }}
                    {{ TworderData.iorderkind === 1 ? '区域广告' : '' }}
                    {{ TworderData.iorderkind === 2 ? '内容生产方广告' : '' }}
                    {{ TworderData.iorderkind === 3 ? '上门广告' : '' }}
                    <!-- <el-select
                      v-model="TworderData.iorderkind"
                      placeholder="订单类别"
                      style="width: 100%"

                    >
                      <el-option label="本部广告" :value="0"></el-option>
                      <el-option label="区域广告" :value="1"></el-option>
                      <el-option label="内容生产方广告" :value="2"></el-option>
                      <el-option label="上门广告" :value="3"></el-option>
                    </el-select> -->
                  </el-form-item>
                  <el-form-item label="内容生产方" prop="agentname">
                    <span v-if="FlowManagerdata === 'add' || FlowManagerdata === 'stop'">{{
                      TworderData.agentname
                    }}</span>
                    <el-input
                      v-else
                      v-model="TworderData.agentname"
                      placeholder="请选择客户信息"
                      style="width: 100%"
                      readonly
                    >
                      <template #append>
                        <el-button
                          icon="Search"
                          circle
                          title="选择客户"
                          @click="SeeCustomerlistagencyemploy()"
                        />
                      </template>
                      <template #suffix>
                        <el-button
                          v-show="TworderData.agentname"
                          link
                          icon="CircleClose"
                          @click="SeeCustomerdeleteagencyemploy()"
                        ></el-button>
                      </template>
                    </el-input>
                  </el-form-item>
                  <el-form-item label="广告类型" prop="adtypeid">
                    {{ TworderData.adtypename }}
                    <!-- <el-select
                      v-model="TworderData.adtypeid"
                      placeholder="广告类型"
                      style="width: 100%"
                      @change="getadtypename"
                      
                    >
                      <el-option
                        v-for="item in adtypeCombo"
                        :key="item.id"
                        :label="item.sname"
                        :value="item.id!"
                      />
                    </el-select> -->
                  </el-form-item>
                  <el-form-item label="录入方式" prop="ibooktype">
                    <span v-if="TworderData.ibooktype === 0">正常</span>
                    <span v-if="TworderData.ibooktype === 3">补刊</span>
                    <!-- <el-select
                      v-model="TworderData.ibooktype"
                      placeholder="录入方式"
                      style="width: 100%"
                      
                    >
                      <el-option label="正常" :value="0"></el-option>
                      <el-option label="广告预约" :value="1"></el-option>
                      <el-option label="快速预约" :value="2"></el-option>
                      <el-option label="补刊" :value="3"></el-option>
                    </el-select> -->
                  </el-form-item>
                  <el-form-item label="地址" prop="saddress">
                    {{ TworderData.saddress }}
                    <!-- <el-input v-model="TworderData.saddress" :maxlength="200"  /> -->
                  </el-form-item>
                  <el-form-item label="广告标题" prop="sadtitle">
                    {{ TworderData.sadtitle }}
                    <!-- <el-input v-model="TworderData.sadtitle" /> -->
                  </el-form-item>
                  <el-form-item label="广告资源" prop="adresourceapplicationid">
                    <div>
                      <el-button
                        v-if="TworderData.adresourceapplicationid!.length === 0"
                        type="primary"
                        size="small"
                        @click="dialogReource = true"
                        disabled
                        >选择资源</el-button
                      >
                      <div>
                        <div
                          v-for="(file, index) in TworderData.adresourceapplicationid"
                          :key="file"
                        >
                          <el-link type="primary" :underline="false" @click="onDetail(file)">
                            <el-icon><Document /></el-icon> 资源详情</el-link
                          >
                          <el-button
                            link
                            type="danger"
                            icon="CircleClose"
                            @click="deleteResource(index)"
                          ></el-button>
                        </div>
                      </div>
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="选择客户" prop="customername">
                    <span v-if="FlowManagerdata === 'add' || FlowManagerdata === 'stop'">{{
                      TworderData.customername
                    }}</span>
                    <el-input
                      v-else
                      v-model="TworderData.customername"
                      placeholder="请选择客户信息"
                      style="width: 100%"
                      readonly
                    >
                      <template #append>
                        <el-button
                          icon="Search"
                          circle
                          title="选择客户"
                          @click="SeeCustomerlist()"
                        />
                      </template>
                      <!-- <template #suffix>
                      <el-button
                        v-show="TworderData.customername"
                        link
                        icon="CircleClose"
                        @click="SeeCustomerdelete()"
                      ></el-button>
                    </template> -->
                    </el-input>
                  </el-form-item>
                  <el-form-item label="行业名称" prop="adindustyid">
                    <span v-if="FlowManagerdata === 'add' || FlowManagerdata === 'stop'">{{
                      TworderData.adindustryname
                    }}</span>
                    <HgZtreeSelect
                      v-else
                      :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYINDUSTRY"
                      :selectids="TworderData.adindustyid ? [TworderData.adindustyid] : []"
                      style="width: 100%"
                      @selectionztree="onSelectionindustry"
                    ></HgZtreeSelect>
                  </el-form-item>
                  <el-form-item label="广告项目" prop="adprojectid">
                    {{ TworderData.adprojectname }}
                    <!-- <HgAdproject
                      style="width: 100%"
                      ref="HgAdprojectdiv"
                      v-model="TworderData.adprojectid"
                      @selectiondata="getadprojectname"
                    ></HgAdproject> -->
                    <!-- <el-select
                    v-model="TworderData.adprojectid"
                    placeholder="广告项目"
                    style="width: 100%"
                    @change="getadprojectname"
                  >
                    <el-option
                      v-for="item in AdProjectList"
                      :key="item.id"
                      :label="item.sname"
                      :value="item.id!"
                    />
                  </el-select> -->
                  </el-form-item>

                  <el-form-item label="移动电话" prop="smobilephone">
                    {{ TworderData.smobilephone }}
                    <!-- <el-input v-model="TworderData.smobilephone"  /> -->
                  </el-form-item>
                  <el-form-item label="广告内容" prop="sadcontent">
                    {{ TworderData.sadcontent }}
                    <!-- <el-input v-model="TworderData.sadcontent"  /> -->
                  </el-form-item>
                  <el-form-item label="负责人意见" prop="sopinion">
                    {{ TworderData.sopinion }}
                    <!-- <el-input v-model="TworderData.sopinion" :maxlength="200"  /> -->
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="代理公司" prop="agencyname">
                    <span v-if="FlowManagerdata === 'add' || FlowManagerdata === 'stop'">{{
                      TworderData.agencyname
                    }}</span>
                    <el-input
                      v-else
                      v-model="TworderData.agencyname"
                      placeholder="请选择客户信息"
                      style="width: 100%"
                      readonly
                    >
                      <template #append>
                        <el-button
                          icon="Search"
                          circle
                          title="选择客户"
                          @click="SeeCustomerlistagency()"
                        />
                      </template>
                      <template #suffix>
                        <el-button
                          v-show="TworderData.agencyname"
                          link
                          icon="CircleClose"
                          @click="SeeCustomerdeleteagency()"
                        ></el-button>
                      </template>
                    </el-input>
                  </el-form-item>
                  <el-form-item label="经营主体" prop="businessentityid">
                    {{ TworderData.businessentityname }}
                    <!-- <el-select
                      v-model="TworderData.businessentityid"
                      placeholder="经营主体"
                      style="width: 100%"
                      @change="getbusinessentityname"
                    >
                      <el-option
                        v-for="t in businessentityCombo"
                        :key="t.id"
                        :label="t.sname"
                        :value="t.id"
                      ></el-option>
                    </el-select> -->
                  </el-form-item>
                  <el-form-item label="联系人" prop="scontacts">
                    {{ TworderData.scontacts }}
                    <!-- <el-input v-model="TworderData.scontacts" /> -->
                  </el-form-item>
                  <el-form-item label="邮政编码" prop="spostcode">
                    {{ TworderData.spostcode }}
                    <!-- <el-input v-model="TworderData.spostcode" /> -->
                  </el-form-item>
                  <el-form-item label="备注" prop="sremark">
                    {{ TworderData.sremark }}
                    <!-- <el-input v-model="TworderData.sremark" :maxlength="200" /> -->
                  </el-form-item>
                </el-col>
              </el-row>
            </el-collapse-item>
          </el-collapse>

          <!-- <el-row :span="24" style="margin-top: 20px">
          <el-col :span="24">
            <AdTworderItemBelong
              style="width: 100%"
              :data="worderitembelonglsit"
            ></AdTworderItemBelong>
          </el-col>
        </el-row> -->

          <el-row :span="24" style="margin: 20px 0">
            <el-col :span="24">
              <AdTworderItemEditer
                style="width: 100%"
                :data="TworderData"
                :resourceid="Resourceid"
                :belongdata="worderitembelonglsit"
                :bottomshow="props.bottomshow"
                :idbukan="TworderData.ibooktype"
                :FlowManager="FlowManagerdata"
                @selectionorderitem="selectionorderitem"
              ></AdTworderItemEditer>
            </el-col>
          </el-row>
          <div
            style="width: 100%; text-align: center; margin-bottom: 20px"
            v-if="FlowManagerdata === 'add' || FlowManagerdata === 'change'"
          >
            <el-button
              v-if="props.bottomshow !== false"
              type="primary"
              icon="Check"
              @click="handleSave('')"
              >保存</el-button
            >
            <el-button v-if="props.bottomshow !== false" icon="Close" @click="handleCancel"
              >重置</el-button
            >
          </div>
        </el-form>
      </div>
      <!-- 选择客户 -->
      <el-dialog
        v-model="Customershow"
        title="选择客户"
        width="1000"
        align-center
        :destroy-on-close="true"
        :draggable="fenbianlv() > 1440 ? true : false"
        @close="handleCancelfiles"
      >
        <el-row :gutter="50">
          <el-col :span="24">
            <Hg-Sales-Customer
              :buse="true"
              :selfid="selfid"
              :showbuse="false"
              :itype="itype"
              :iapprovestatus="iapprovePass"
              @selectiondata="selectiondata"
            ></Hg-Sales-Customer>
          </el-col>
        </el-row>
      </el-dialog>
      <!-- 详情 -->
      <el-dialog
        v-model="dialogDetailVisible"
        title="资源详情"
        width="800"
        align-center
        :close-on-click-modal="false"
        :destroy-on-close="true"
      >
        <HgAdSourceDetail :id="Resourceid" :showflag="false"></HgAdSourceDetail>
      </el-dialog>
      <!-- 选择资源 -->
      <el-dialog
        v-model="dialogReource"
        title="资源列表"
        width="1200"
        align-center
        :close-on-click-modal="false"
        :destroy-on-close="true"
      >
        <AdResourceComponent
          :customer-ids="customerids"
          :approve-status="'2'"
          @selectionresourcedata="selectionresourcedata"
        ></AdResourceComponent>
      </el-dialog>
    </div>
  </el-dialog>
</template>

<script setup lang="ts">
import { FormRules, FormInstance, menuItemEmits } from 'element-plus'
import AdTworderItemEditer from './AdTworderItemEditer.vue'
import AdTworderItemBelong from './AdTworderItemBelong.vue'
import AdResourceComponent from './AdResourceComponent.vue'
import type {
  TAdCustomer,
  TQueryCustomer,
  Twcustomerfiles,
  Twcustomerbelong,
  TCustomerApprove
} from '@/types/views/customer'
import type {
  TworderCustomer,
  Tworderitembelong,
  Tworderitem,
  Tworder
} from '@/types/views/ad/adtworder'
import {
  getorderByIdApi,
  getContractNumByEmployIdAndCustomerIdApi,
  saveorderApi,
  updateorderApi,
  deleteorderApi,
  getApproveTypeComboApi
} from '@/api/ad/adtworder'
import {
  required,
  computTableHeight,
  isNumberStr,
  formatDate,
  tableHeaderColor,
  validatePhone,
  computTreeHeight,
  fenbianlv
} from '@/utils/index'
import { getAdProjectListApi } from '@/api/ad/adproject'
import { getadtypelistApi } from '@/api/ad/adtype'
import { TAdtype } from '@/types/views/ad/adtype'
import { getBusinessentityComboApi } from '@/api/finance/businessentity'
import { IBusinessentity } from '@/types/views/finance/businessentity'
import type { IAxios } from 'axios'
import modal from '@/plugins/modal'
import useUserStore from '@/store/modules/user'
import { EHgDeptZtreeUrl } from '@/types/common/enumindex'
import { TSelectZtree } from '@/types/common'
import { getEnumCombo } from '@/api/common/index'
import { EFlowType } from '@/types/common/enumindex'
import { IDataCombo } from '@/types/common/DataCombo'
import { getFlowlistComboByFlowTypeApi } from '@/api/flow/index'
import { IAdResourceApplicationDTO } from '@/types/views/ad/adresourceapplication'
import HgAdproject from '@/components/HgAdproject/index.vue'
/** 获取流程参数 */
const flowEFlowType: IDataCombo[] = getEnumCombo(EFlowType)
const userStore = useUserStore()
defineOptions({
  name: 'AdTworder'
})
import type { TAdProject } from '@/types/views/ad/adproject'
import { Tadindustrylist, TypeCurrentData } from '@/types/components/hgindustry'
import { Bottom } from '@element-plus/icons-vue'
const props = defineProps<{
  /**
   * 传入数据
   */
  data?: string
  /**
   * 资源id
   */
  resourceid?: string
  /**
   * 重置数据,判断是否是添加
   */
  redetdata?: boolean
  /**
   * 保存按钮是否显示，true显示，false不显示
   */
  bottomshow?: boolean
  /**
   * 是否是补刊，3是，0不是
   */
  isbu?: number
  /**
   * 资源预定传值
   */
  IAdResourceApplication?: IAdResourceApplicationDTO
  /**
   * 项目id和名称
   */
  dataadproject?: any
  /**
   * 审核类型
   */
  FlowManager?: string
}>()
/** 保存关闭弹窗 */
const emit = defineEmits<{
  closedialogVisible: [data: '']
}>()
/** 表格高度 */
const tableheight = ref(0)
/**
 * 默认展开项
 */
const activeNames = ref<string[]>(['1'])
/** 流程id */
const flowdata = ref<TCustomerApprove>({
  flowId: '',
  id: '',
  iapproveType: 4
})
/** 订单信息 */
const TworderData = ref<Tworder>({
  adresourceapplicationid: [],
  iapproveType: 0,
  flowId: '',
  orderitem: [],
  id: '',
  sordernum: '',
  adprojectid: '',
  adprojectname: '无',
  scontractnum: '',
  createempid: userStore.user?.userid.toString(),
  createempname: userStore.user?.username.toString(),
  createtime: '',
  ibooktype: 0,
  businessentityid: '',
  businessentityname: '',
  customerid: '',
  customername: '',
  bcustomeVip: false,
  scontacts: '',
  saddress: '',
  smobilephone: '',
  spostcode: '',
  deptid: userStore.user.deptid!.toString(),
  deptname: userStore.user.deptname!.toString(),
  iorderkind: 0,
  employid: '',
  employname: '',
  agencytid: '',
  agencyname: '', // 代理公司名称
  bagencyVip: false,
  agencyemployid: '',
  agencyemployname: '',
  agentid: '',
  agentname: '', // 内容生产方
  bagentVip: false,
  agentemployid: '',
  agentemployname: '',
  adindustyid: '',
  adindustryname: '',
  adtypeid: '',
  adtypename: '',
  sadtitle: '',
  sadcontent: '',
  buse: true,
  bdelete: false,
  ipreapprovestatus: 0,
  iaddapprovestatus: 0,
  ichangeapprovestatus: 0,
  istopapprovestatus: 0,
  idiscountapprovestatus: 0,
  version: 0,
  sopinion: '',
  sremark: '',
  iapprovestatus: 0,
  bspecial: 0,
  sspecialreason: '',
  relateorderid: ''
})
const worderitembelonglsit = ref<Tworderitembelong[]>([])
const TwordwrFormRef = ref<FormInstance>()
/** 客户选择分类显示 */
const Customershowfenlei = ref('')
/** 广告项目传值 */
const HgAdprojectdiv = ref()
/**
 * 资源id
 */
const Resourceid = ref('')
/** 客户选择 */
const Customershow = ref(false)
/** 查看资源详情窗口显示状态 */
const dialogDetailVisible = ref(false)
/** 选择资源列表 */
const dialogReource = ref(false)
/** 修改时将自己的id传给客户选择组件 */
const selfid = ref<string | undefined>('')
/** 广告项目列表 */
const AdProjectList = ref<TAdProject[]>([])
/** 订单id */
const WorderId = ref('')
/** 经营主体下拉 */
const businessentityCombo = ref<IBusinessentity[]>([])
/** 广告类型下拉 */
const adtypeCombo = ref<TAdtype[]>([])
/**
 * 客户类型传值
 */
const itype = ref(0)
const iapprovePass = ref('')
/**
 * 是否是大客户
 */
const bvip = ref(false)
/** 流程类型列表 */
const FlowTypeList = ref<Tadindustrylist[]>([])
const showbottom = ref(false)
/** 选择客户id集合 */
const customerids = ref<string[]>([])

/**
 * 校验设置参数
 */
const rules = reactive<FormRules<Tworder>>({
  adprojectname: [
    { validator: required, required: true, message: '请输入项目名称', trigger: 'change' },
    { max: 48, message: '不得大于48个字符', trigger: 'change' }
  ],
  sadtitle: [
    { validator: required, required: true, message: '请输入广告标题', trigger: 'change' },
    { max: 48, message: '不得大于48个字符', trigger: 'change' }
  ],
  flowId: [{ message: '请选择审核流程', trigger: 'change' }],
  sordernum: [
    { validator: isNumberStr, message: '请输入数字', trigger: 'change' },
    { required: true, message: '请输入订单编号', trigger: 'change' }
  ],
  adprojectid: [{ required: true, message: '请选择广告项目', trigger: 'change' }],
  // adresourceapplicationid: [{ required: true, message: '请上传资源', trigger: 'change' }],
  scontractnum: [
    { validator: isNumberStr, message: '请输入数字', trigger: 'change' },
    { required: true, message: '请输入合同号', trigger: 'change' }
  ],
  customername: [{ required: true, message: '请选择客户', trigger: 'change' }],
  adindustyid: [{ required: true, message: '请选择行业', trigger: 'change' }],
  scontacts: [{ max: 48, message: '不得大于48个字符', trigger: 'change' }],
  smobilephone: [{ validator: validatePhone, message: '请输入电话', trigger: 'change' }],
  spostcode: [{ validator: isNumberStr, message: '请输入数字', trigger: 'change' }]
})
/** 组件传值---------------------------------------- */
/**
 * 审核中修改显示隐藏
 */
const dialogVisible = ref(false)
/**
 * 审核类型控制显示隐藏add加刊 change修改 stop停刊
 */
const FlowManagerdata = ref('')
import { defineExpose } from 'vue'
/** 根据合同号获取订单详情 */
const view = (row: string, data?: string) => {
  dialogVisible.value = true
  WorderId.value = row
  console.log('row', data)
  FlowManagerdata.value = data!
  getorderById()
}
defineExpose({ view })
/** 组件传值---------------------------------------- */
/** 监听项目id */
watch(
  () => props.dataadproject,
  (newValue, oldValue) => {
    if (newValue) {
      TworderData.value.adprojectid = newValue.id
      TworderData.value.adprojectname = newValue.sname
      // HgAdprojectdiv.value.view(newValue.sname as string, true)
    }
  },
  { deep: true }
)
/** 监听传入修改时的订单id */
watch(
  () => props.data,
  (newValue, oldValue) => {
    if (newValue && newValue !== '') {
      WorderId.value = props.data!
      getorderById()
      activeNames.value = ['1']
    } else {
      reset()
    }
  },
  { deep: true }
)
/** 监听传入资源id编号 */
watch(
  () => props.resourceid,
  (newValue, oldValue) => {
    if (newValue) {
      TworderData.value.adresourceapplicationid!.push(props.resourceid!)
      Resourceid.value = TworderData.value.adresourceapplicationid![0]
    }
  },
  { deep: true }
)
/** 添加时清空数据 */
watch(
  () => props.redetdata,
  (newValue, oldValue) => {
    if (newValue === false) {
      reset()
      getBusinessentityCombo()
      getAdtypeCombo()
      activeNames.value = ['1']
    }
  },
  { deep: true }
)
/**
 * 监听是否是补刊
 */
watch(
  () => props.isbu,
  (newValue, oldValue) => {
    TworderData.value.ibooktype = newValue
  }
)
/**
 * 监听资源预定传值
 */
watch(
  () => props.IAdResourceApplication,
  (newValue, oldValue) => {
    if (newValue !== undefined) {
      if (TworderData.value.sadtitle === '') {
        TworderData.value.sadtitle = newValue.sadtitle
      }
      TworderData.value.sadcontent = newValue.sadcontent
      TworderData.value.sremark = newValue.sremark
      TworderData.value.adresourceapplicationid = []
      TworderData.value.adresourceapplicationid!.push(newValue.id!)
    }
  },
  { deep: true }
)
onMounted(() => {
  WorderId.value = props.data!
  if (props.redetdata === false) {
    reset()
  }
  /** 判断是否传入id */
  if (props.data !== '' && props.data !== undefined) {
    getorderById()
  } else {
    reset()
  }
  /** 判断资源预定是否传入资源数据 */
  if (props.IAdResourceApplication !== undefined) {
    if (TworderData.value.sadtitle === '') {
      TworderData.value.sadtitle = props.IAdResourceApplication.sadtitle
    }
    TworderData.value.sadcontent = props.IAdResourceApplication.sadcontent
    TworderData.value.sremark = props.IAdResourceApplication.sremark
    TworderData.value.adresourceapplicationid = []
    TworderData.value.adresourceapplicationid!.push(props.IAdResourceApplication.id!)
  }
  getBusinessentityCombo()
  getAdtypeCombo()
  // getApproveTypeCombo()
  getadprojectlist()
  if (props.dataadproject !== undefined) {
    TworderData.value.adprojectid = props.dataadproject.id
    TworderData.value.adprojectname = props.dataadproject.sname
    // HgAdprojectdiv.value.view(props.dataadproject.sname as string, true)
  }
  TworderData.value.ibooktype = props.isbu
  nextTick(() => {
    /**
     * 计算表格高度
     */
    tableheight.value = computTableHeight()
  })
})
/**
 * 获取根据id获取订单信息
 */
const getorderById = () => {
  getorderByIdApi(WorderId.value).then(({ obj, success, msg }: IAxios) => {
    if (success) {
      // HgAdprojectdiv.value.view(obj.adprojectname as string, true)
      const databelonglsit = ref<Tworderitembelong[]>([])
      var dataid = obj.id
      /**
       * 判断是否是补刊
       */
      if (props.isbu === 3) {
        obj.id = ''
        obj.relateorderid = dataid
        obj.ibooktype = 3
        obj.orderitem.forEach((item: any) => {
          item.id = ''
          item.iitemcode = ''
          item.ibooktype = 3
        })
      }
      TworderData.value = obj
      // TworderData.value.relateorderid = dataid
      databelonglsit.value = obj.orderitem[0].orderitembelong
      databelonglsit.value.forEach((item) => {
        const data = { ...item }
        data.id = ''
        data.orderitemid = ''
        worderitembelonglsit.value.push(data)
      })
    }
  })
}
/** 查看客户列表信息------------------------------------ */
const SeeCustomerlist = () => {
  itype.value = 0
  iapprovePass.value = '2'
  Customershow.value = true
  Customershowfenlei.value = 'customer'
}
/** 查看客户列代理公司------------------------------------ */
const SeeCustomerlistagency = () => {
  itype.value = 1
  Customershow.value = true
  Customershowfenlei.value = 'agency'
}
/** 查看客户内容生产方------------------------------------ */
const SeeCustomerlistagencyemploy = () => {
  itype.value = 2
  Customershow.value = true
  Customershowfenlei.value = 'agencyemploy'
}
/**
 * 查看详情按钮事件
 * @param row
 */
const onDetail = (data: string) => {
  dialogDetailVisible.value = true
  if (data !== '') {
    Resourceid.value = data
  }
}
/**
 * 关闭附件弹窗
 */
const handleCancelfiles = () => {
  Customershow.value = false
  Customershowfenlei.value = ''
}
/**
 * 客户赋值
 */
const selectiondata = (val: TAdCustomer) => {
  if (Customershowfenlei.value === 'customer') {
    TworderData.value.customername = val.sname
    TworderData.value.customerid = val.id
    TworderData.value.scontacts = val.scontacts
    TworderData.value.saddress = val.saddress
    TworderData.value.smobilephone = val.smobilephone
    TworderData.value.spostcode = val.spostcode
    TworderData.value.employid = val.employid
    TworderData.value.employname = val.employname
    TworderData.value.adindustyid = val.adindustryid
    TworderData.value.adindustryname = val.adindustryname
    TworderData.value.bcustomeVip = val.bvip as boolean
    customeridsdata()
    /**
     * 遍历数组业务员类型为0的删除
     */
    remove(0)
  } else if (Customershowfenlei.value === 'agency') {
    TworderData.value.agencyname = val.sname
    TworderData.value.agencytid = val.id
    TworderData.value.agencyemployid = val.employid
    TworderData.value.agencyemployname = val.employname
    TworderData.value.bagencyVip = val.bvip as boolean
    customeridsdata()

    /**
     * 遍历数组业务员类型为1的删除
     */
    remove(1)
  } else if (Customershowfenlei.value === 'agencyemploy') {
    TworderData.value.agentname = val.sname
    TworderData.value.agentid = val.id
    TworderData.value.agentemployid = val.employid
    TworderData.value.agentemployname = val.employname
    TworderData.value.bagentVip = val.bvip as boolean
    customeridsdata()
    /**
     * 遍历数组业务员类型为2的删除
     */
    remove(2)
  }
  const workreport = val.customerbelong as any
  const data = ref<any[]>([])
  const worderitembelong = ref<Tworderitembelong[]>([])
  data.value.push(...workreport)
  worderitembelong.value = data.value.map((item) => {
    return {
      id: '',
      orderitemid: '',
      sordernum: '',
      scontractnum: '',
      createempid: userStore.user?.userid.toString(),
      createempname: userStore.user?.username.toString(),
      createtime: item.dcreatetime,
      deptid: item.deptid,
      deptname: item.deptname,
      employid: item.employid,
      employname: item.customerName,
      employtype: val.itype,
      archievementrate: 100,
      taskrate: 100,
      commissionrate: 100,
      bprimary: item.bprimary,
      sremark: ''
    }
  })
  for (let i = 0; i < worderitembelonglsit.value.length; i++) {
    for (let j = 0; j < worderitembelong.value.length; j++) {
      if (
        worderitembelonglsit.value[i].employid === worderitembelong.value[j].employid &&
        worderitembelonglsit.value[i].employtype === worderitembelong.value[j].employtype
      ) {
        modal.msgWarning('该业务员已存在')
        return
      }
    }
  }
  nextTick(() => {
    worderitembelonglsit.value.push(...worderitembelong.value)
  })
  Customershow.value = false
}
/**
 * 客户id数组赋值
 */
const customeridsdata = () => {
  customerids.value = []
  if (TworderData.value.customerid !== '') {
    customerids.value.push(TworderData.value.customerid as string)
  }
  if (TworderData.value.agencytid !== '') {
    customerids.value.push(TworderData.value.agencytid as string)
  }
  if (TworderData.value.agentid !== '') {
    customerids.value.push(TworderData.value.agentid as string)
  }
}

/**
 * 遍历数组符合条件的移除
 */
const remove = (item: number) => {
  for (let i = 0; i < worderitembelonglsit.value.length; i++) {
    if (worderitembelonglsit.value[i].employtype === item) {
      worderitembelonglsit.value.splice(i, worderitembelonglsit.value.length)
    }
  }
}
/**
 * 资源赋值
 */
const selectionresourcedata = (val: IAdResourceApplicationDTO) => {
  if (TworderData.value.adresourceapplicationid!.length === 0) {
    if (TworderData.value.sadtitle === '') {
      TworderData.value.sadtitle = val.sadtitle
    }
    TworderData.value.sadcontent = val.sadcontent
    TworderData.value.sremark = val.sremark
    TworderData.value.adresourceapplicationid!.push(val.id!)
  } else {
    TworderData.value.adresourceapplicationid!.forEach((element) => {
      if (element === val.id) {
        modal.msgWarning('该资源已存在')
        return
      } else {
        TworderData.value.adresourceapplicationid!.push(val.id!)
      }
    })
  }
  if (Resourceid.value === 'null') {
    Resourceid.value = val.id!
  }
  dialogReource.value = false
}
/**
 * 删除资源
 */
const deleteResource = (index: number) => {
  TworderData.value.adresourceapplicationid!.splice(index, 1)
  if (TworderData.value.adresourceapplicationid!.length === 0) {
    Resourceid.value = 'null'
  }
}
/** 修改时删除客户列表信息 */
const SeeCustomerdelete = () => {
  TworderData.value.customername = ''
  TworderData.value.customerid = ''
  TworderData.value.employid = ''
  TworderData.value.employname = ''
  customeridsdata()

  /**
   * 遍历数组业务员类型为2的删除
   */
  remove(0)
}
/** 代理公司名称 */
const SeeCustomerdeleteagency = () => {
  TworderData.value.agencyname = ''
  TworderData.value.agencytid = ''
  TworderData.value.agencyemployname = ''
  TworderData.value.agencyemployid = ''
  customeridsdata()

  /**
   * 遍历数组业务员类型为1的删除
   */
  remove(1)
}
/** 内容生产方 */
const SeeCustomerdeleteagencyemploy = () => {
  TworderData.value.agentname = ''
  TworderData.value.agentid = ''
  TworderData.value.agentemployname = ''
  TworderData.value.agentemployid = ''
  customeridsdata()

  /**
   * 遍历数组业务员类型为2的删除
   */
  remove(2)
}
// -----------------------------------------------------------------
/**
 * 编辑时查看行业
 * @param val
 */
const onSelectionindustry = (val: TSelectZtree[]) => {
  const adindustryid = val.map((item) => item.id).join(',')
  const adindustryname = val.map((item) => item.name).join(',')
  TworderData.value.adindustyid = adindustryid
  TworderData.value.adindustryname = adindustryname
}
/** 提交校验 */
const validFormSubmitBatch = () => {
  if (TworderData.value.orderitem?.length === 0) {
    ElMessage.error('请填写订单明细')
    return false
  }
  return true
}
/**
 * 保存
 */
const handleSave = (val?: string) => {
  TwordwrFormRef.value?.validate((valid) => {
    const validform = validFormSubmitBatch()
    if (!validform) {
      return
    }
    if (valid) {
      if (val === 'shenhe' && TworderData.value.flowId === '') {
        TworderData.value.flowId = FlowTypeList.value[0].id
        Save()
      } else if (val === 'shenhe' && TworderData.value.flowId !== '') {
        TworderData.value.iapproveType = 4
        Save()
      } else if (val === '') {
        TworderData.value.flowId = ''
        Save()
      }
    }
  })
}
/**
 * 保存
 */
const Save = () => {
  if (!TworderData.value.id) {
    saveorderApi(TworderData.value).then(({ success, msg }: IAxios) => {
      if (success) {
        modal.msgSuccess('操作成功')
        dialogVisible.value = false
        emit('closedialogVisible', '')
      } else {
        modal.msgError(msg)
      }
    })
  } else {
    updateorderApi(TworderData.value).then(({ success, msg }: IAxios) => {
      if (success) {
        modal.msgSuccess('操作成功')
        dialogVisible.value = false
        emit('closedialogVisible', '')
      } else {
        modal.msgError(msg)
      }
    })
  }
}
/**
 * 取消
 */
const handleCancel = () => {
  TwordwrFormRef.value?.resetFields()
  reset()
  setTimeout(() => {
    TwordwrFormRef.value?.clearValidate([
      'adprojectname',
      'sordernum',
      'adprojectid',
      'scontractnum',
      'customername',
      'flowId',
      'sadtitle',
      'adindustyid'
    ])
  }, 10)
}
/**
 * 关闭是清空数据
 */
const reset = () => {
  TworderData.value = {
    adresourceapplicationid: [Resourceid.value],
    iapproveType: 0,
    flowId: '',
    orderitem: [],
    id: '',
    sordernum: '',
    adprojectid: '',
    adprojectname: '无',
    scontractnum: '',
    createempid: userStore.user?.userid.toString(),
    createempname: userStore.user?.username.toString(),
    createtime: '',
    ibooktype: 0,
    businessentityid: '',
    businessentityname: '',
    customerid: '',
    customername: '',
    scontacts: '',
    saddress: '',
    smobilephone: '',
    spostcode: '',
    deptid: userStore.user.deptid!.toString(),
    deptname: userStore.user.deptname!.toString(),
    iorderkind: 0,
    employid: '',
    employname: '',
    agencytid: '',
    agencyname: '',
    agencyemployid: '',
    agencyemployname: '',
    agentid: '',
    agentname: '',
    agentemployid: '',
    agentemployname: '',
    adindustyid: '',
    adindustryname: '',
    adtypeid: '',
    adtypename: '',
    sadtitle: '',
    sadcontent: '',
    buse: true,
    bdelete: false,
    ipreapprovestatus: 0,
    iaddapprovestatus: 0,
    ichangeapprovestatus: 0,
    istopapprovestatus: 0,
    idiscountapprovestatus: 0,
    version: 0,
    sopinion: '',
    sremark: '',
    iapprovestatus: 0,
    bspecial: 0,
    sspecialreason: '',
    bcustomeVip: false,
    bagencyVip: false,
    bagentVip: false,
    relateorderid: ''
  }
  worderitembelonglsit.value = []
  TworderData.value.adresourceapplicationid = []
  setTimeout(() => {
    TwordwrFormRef.value?.clearValidate([
      'adprojectname',
      'sordernum',
      'adprojectid',
      'scontractnum',
      'customername',
      'flowId',
      'sadtitle',
      'adindustyid'
    ])
  }, 10)
}
/**
 * 获取广告项目getAdProjectListApi
 */
const getadprojectlist = () => {
  getAdProjectListApi().then(({ success, msg, obj }: IAxios) => {
    /**
     * 过滤掉已经结项的项目
     */
    if (success) {
      AdProjectList.value = obj.filter((item: any) => item.bprojectcomplete === false)
    }
  })
}
/**
 * 给广告项目名称赋值
 */
const getadprojectname = (val?: any) => {
  TworderData.value.adprojectname = val.sname
  TworderData.value.adprojectid = val.id
  // AdProjectList.value.forEach((item) => {
  //   if (item.id === TworderData.value.adprojectid) {
  //     TworderData.value.adprojectname = item.sname
  //   }
  // })
}
/**
 * 经营主体下拉查询
 */
const getBusinessentityCombo = () => {
  getBusinessentityComboApi().then(({ success, obj }: IAxios<IBusinessentity[]>) => {
    if (success) {
      businessentityCombo.value = obj
      // TworderData.value.businessentityid = obj.filter((res) => res.bdefault === true)[0].id
      // TworderData.value.businessentityname = obj.filter((res) => res.bdefault === true)[0].sname
      var filterData = obj.filter((res) => res.bdefault === true)
      if (filterData.length > 0) {
        TworderData.value.businessentityid = filterData[0].id
        TworderData.value.businessentityname = filterData[0].sname
      }
    }
  })
}
/**
 * 给经营主体名称赋值
 */
const getbusinessentityname = () => {
  businessentityCombo.value.forEach((item) => {
    if (item.id === TworderData.value.businessentityid) {
      TworderData.value.businessentityname = item.sname
    }
  })
}
/**
 * 获取广告类别
 */
const getAdtypeCombo = () => {
  getadtypelistApi().then(({ success, obj }: IAxios<TAdtype[]>) => {
    if (success) {
      adtypeCombo.value = obj
      // TworderData.value.adtypeid = obj.filter((res) => res.bdefault === true)[0].id
      // TworderData.value.adtypename = obj.filter((res) => res.bdefault === true)[0].sname
      var filterData = obj.filter((res) => res.bdefault === true)
      if (filterData.length > 0) {
        TworderData.value.adtypeid = filterData[0].id
        TworderData.value.adtypename = filterData[0].sname
      }
    }
  })
}
/**
 * 给广告类别名称赋值
 */
const getadtypename = () => {
  adtypeCombo.value.forEach((item) => {
    if (item.id === TworderData.value.adtypeid) {
      TworderData.value.adtypename = item.sname
    }
  })
}
/**
 * 明细组件赋值
 */
const selectionorderitem = (val: Tworderitem[]) => {
  TworderData.value.orderitem = val
}
/**
 * 获取流程
 */
const getApproveTypeCombo = () => {
  getFlowlistComboByFlowTypeApi(flowEFlowType[3].id).then(
    ({ obj, total }: IAxios<Tadindustrylist[]>) => {
      FlowTypeList.value = obj
    }
  )
}
</script>

<style scoped>
ul {
  padding-left: 10px;
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

.el-form-item {
  margin-bottom: 20px;
}

.box-card {
  width: 280px;
  max-height: 600px;
  overflow-y: auto;
}
.el-form-item {
  margin-bottom: 10px;
}
</style>
