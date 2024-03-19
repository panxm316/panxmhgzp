<!--
 * @Author: wanghl
 * @Date: 2023-11-30 09:44:40
 * @LastEditTime: 2024-03-15 15:24:50
 * @LastEditors: wanghl
 * @Description:AdTworder根据id获取广告预定详情，审核时使用
-->
<template>
  <div class="app-container" style="background-color: #ffffff; margin: -20px 8px 8px">
    <el-button
      v-if="FlowManagertype !== 'stop'"
      style="position: absolute; top: 0px; right: 170px"
      icon="edit"
      type="success"
      @click="adtoworeredit()"
      >点击修改</el-button
    >
    <el-button
      style="position: absolute; top: 0px; right: 10px"
      icon="Printer"
      type="primary"
      @click="printMeShow()"
      >打印发布认刊书</el-button
    >
    <div style="width: 100%">
      <el-form ref="TwordwrFormRef" :model="TworderData" label-width="120px" :rules="rules">
        <el-collapse v-model="activeNames" style="width: 100%">
          <el-collapse-item name="1">
            <template #title>
              广告订单数据<el-icon class="header-icon">
                <info-filled />
              </el-icon>
            </template>
            <el-row :gutter="22" style="padding-right: 40px">
              <el-col :span="6">
                <el-form-item label="订单编号:" prop="sordernum">
                  {{ TworderData.sordernum }}
                </el-form-item>
                <el-form-item label="合同号:" prop="scontractnum">
                  {{ TworderData.scontractnum }}
                </el-form-item>
                <el-form-item label="经营主体:" prop="businessentityname">
                  {{ TworderData.businessentityname }}
                </el-form-item>
                <el-form-item label="创建时间:" prop="createtime">
                  {{ TworderData.createtime!.substring(0, 10) }}
                </el-form-item>
                <el-form-item label="创建者:" prop="createempname">
                  {{ TworderData.createempname }}
                </el-form-item>
                <el-form-item label="广告标题:" prop="sadtitle">
                  {{ TworderData.sadtitle }}
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="订单类别:" prop="iorderkind">
                  <span v-if="TworderData.iorderkind === 0">本部广告</span>
                  <span v-if="TworderData.iorderkind === 1">记者站广告</span>
                  <span v-if="TworderData.iorderkind === 2">编辑广告</span>
                  <span v-if="TworderData.iorderkind === 3">上门广告</span>
                </el-form-item>
                <el-form-item label="内容生产方:" prop="agentname">
                  {{ TworderData.agentname }}
                </el-form-item>
                <el-form-item label="广告类型:" prop="adtypeid">
                  {{ TworderData.adtypename }}
                </el-form-item>
                <el-form-item label="录入方式:" prop="ibooktype">
                  <span v-if="TworderData.ibooktype === 0"> 正常 </span>
                  <span v-if="TworderData.ibooktype === 1"> 广告预约 </span>
                  <span v-if="TworderData.ibooktype === 2"> 快速预约 </span>
                  <span v-if="TworderData.ibooktype === 3"> 补刊 </span>
                </el-form-item>
                <el-form-item label="地址:" prop="saddress">
                  {{ TworderData.saddress }}
                </el-form-item>

                <el-form-item label="广告资源:" prop="adresourceapplicationid">
                  <div>
                    <!-- <el-button
                      v-if="TworderData.adresourceapplicationid!.length === 0"
                      type="primary"
                      size="small"
                      @click="dialogReource = true"
                      >选择资源</el-button
                    > -->
                    <div v-if="TworderData.adresourceapplicationid!.length > 0">
                      <div v-for="file in TworderData.adresourceapplicationid" :key="file">
                        <el-link type="primary" :underline="false" @click="onDetail(file)">
                          <el-icon><Document /></el-icon> 资源详情</el-link
                        >
                      </div>
                    </div>
                  </div>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="选择客户:" prop="customername">
                  {{ TworderData.customername }}
                </el-form-item>
                <el-form-item label="行业名称:" prop="adindustyid">
                  {{ TworderData.adindustryname }}
                </el-form-item>
                <el-form-item label="广告项目:" prop="adprojectid">
                  {{ TworderData.adprojectname }}
                </el-form-item>

                <el-form-item label="移动电话:" prop="smobilephone">
                  {{ TworderData.smobilephone }}
                </el-form-item>
                <el-form-item label="广告内容:" prop="sadcontent">
                  {{ TworderData.sadcontent }}
                </el-form-item>
                <el-form-item label="负责人意见:" prop="sopinion">
                  {{ TworderData.sopinion }}
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="代理公司:" prop="agencyname">
                  {{ TworderData.agencyname }}
                </el-form-item>
                <el-form-item label="经营主体:" prop="businessentityid">
                  {{ TworderData.businessentityname }}
                </el-form-item>
                <el-form-item label="联系人:" prop="scontacts">
                  {{ TworderData.scontacts }}
                </el-form-item>
                <el-form-item label="邮政编码:" prop="spostcode">
                  {{ TworderData.spostcode }}
                </el-form-item>
                <el-form-item label="备注:" prop="sremark">
                  {{ TworderData.sremark }}
                </el-form-item>
              </el-col>
            </el-row>
          </el-collapse-item>
        </el-collapse>
        <el-row :span="24" style="margin: 0">
          <el-col :span="24">
            <AdTworderItemdetails
              style="width: 100%"
              :data="TworderData"
              :resourceid="Resourceid"
              :belongdata="worderitembelonglsit"
            ></AdTworderItemdetails>
          </el-col>
        </el-row>
      </el-form>
    </div>
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
    <!-- 打印组件 -->
    <printMe ref="printMeRef" />
    <!-- 领导修改组件 -->
    <AdTworderEditer
      ref="adtoworereditref"
      :redetdata="false"
      :bottomshow="true"
      :isbu="0"
      @closedialogVisible="Refreshdata"
    />
  </div>
</template>

<script setup lang="ts">
import { FormRules, FormInstance } from 'element-plus'
import AdTworderEditer from './AdTworderEditer.vue'
import AdTworderItemdetails from './AdTworderItemdetails.vue'
import printMe from '../task/printMe.vue'
import AdTworderItem from './AdTworder.vue'
import type { TAdCustomer, TCustomerApprove } from '@/types/views/customer'
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
  Refresh?: boolean
}>()
/** 保存关闭弹窗 */
const emit = defineEmits<{
  closedialogVisible: [data: '']
}>()
/** 表格高度 */
const tableheight = ref(0)
const dialogVisible = ref(false)
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
  sspecialreason: ''
})
const worderitembelonglsit = ref<Tworderitembelong[]>([])
const TwordwrFormRef = ref<FormInstance>()
/** 客户选择分类显示 */
const Customershowfenlei = ref('')
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
 * 改刊停刊类型
 */
const FlowManagertype = ref('')
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<Tworder>>({})
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
/**
 * 监听刷新
 */
watch(
  () => props.Refresh,
  (newValue, oldValue) => {
    getorderById()
  },
  { deep: true }
)
onMounted(() => {
  WorderId.value = props.data!
  if (props.data !== '') {
    getorderById()
  } else {
    reset()
  }
  if (props.Refresh !== null) {
    getorderById()
  }
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})
/**
 * 获取根据id获取订单信息
 */
const getorderById = () => {
  getorderByIdApi(WorderId.value).then(({ obj, success, msg }: IAxios) => {
    console.log(obj)
    if (success) {
      const databelonglsit = ref<Tworderitembelong[]>([])
      TworderData.value = obj
      selfdetailid.value = obj.id
      databelonglsit.value = obj.orderitem[0].orderitembelong
      databelonglsit.value.forEach((item) => {
        const data = { ...item }
        data.id = ''
        data.orderitemid = ''
        worderitembelonglsit.value.push(data)
      })
      /**
       * 给审核类型赋值
       */
      if (obj.istopapprovestatus === 1) {
        FlowManagertype.value = 'stop'
      } else if (obj.ichangeapprovestatus === 1) {
        FlowManagertype.value = 'change'
      } else if (obj.iaddapprovestatus === 1) {
        FlowManagertype.value = 'add'
      }
    }
  })
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
    bagentVip: false
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
const isActive = ref(0)
/**
 * 切换查看历史
 */
const seecustomerdetailSelect = (val?: any, number?: number) => {
  isActive.value = number as number
  currentData.value.processInstanceId = val?.processInstanceId
  currentData.value.flowId = val?.flowId
}
// ---------------------------------------------------------------历史区域
import FlowNodeFormat from '@/components/Flow/FlowNodeFormatData.vue'
/**
 * 显示历史数据
 */
const currentData = ref<TypeCurrentData>({
  processInstanceId: '',
  flowId: '',
  formData: {},
  createTime: '',
  result: ''
})
const customerProcessInstancelist = ref<TypeCurrentData[]>([])
const Seelistdetail = (val: TypeCurrentData) => {
  currentData.value.processInstanceId = val.processInstanceId
  currentData.value.flowId = val.flowId
}
// --------------------------------------------------------
/**
 * 打印发布认刊书
 */
const selfdetailid = ref<string | undefined>('')
const printMeRef = ref()
const printMeShow = () => {
  printMeRef.value.view(WorderId.value)
}
/**
 * 领导修改
 */
const adtoworereditref = ref()
const adtoworeredit = () => {
  adtoworereditref.value.view(selfdetailid.value, FlowManagertype.value)
}
/**
 * 刷新数据
 */
const Refreshdata = () => {
  getorderById()
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
.active {
  padding: 10px;
  font-size: 16px;
}
</style>
