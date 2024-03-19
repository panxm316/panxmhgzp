/* eslint-disable */
<!--
 * @Author: suny
 * @Date: 2023-12-19 11:01:31
 * @LastEditTime: 2024-03-12 18:50:19
 * @LastEditors: muyn
 * @Description: 发票打印组件
-->
<template>
  <div style="padding: 20px">
    <el-form :inline="true" :model="invoiceDataForm" class="demo-form-inline" label-width="140px">
      <el-form-item label="临时票号：">
        <span style="width: 300px">{{ invoiceDataForm.invoice }}</span>
      </el-form-item>
      <el-form-item label="开票日期：">
        <span style="width: 300px">{{ invoiceDataForm.dcreatetime }}</span>
      </el-form-item>
      <el-form-item label="经营主体：">
        <span style="width: 300px">{{ invoiceDataForm.businessentityname }}</span>
      </el-form-item>
      <el-form-item label="收款方名称：">
        <span style="width: 300px">{{ invoiceDataForm.recipient }}</span>
      </el-form-item>
      <el-form-item label="开票客户：">
        <span style="width: 300px">{{ invoiceDataForm.customername }}</span>
      </el-form-item>
      <el-form-item label="税目：">
        <span style="width: 300px">{{ invoiceDataForm.taxitems }}</span>
      </el-form-item>
      <el-form-item label="付款方名称：">
        <el-input
          v-model="invoiceDataForm.sprintname"
          :disabled="invoiceDataForm.invoicecode !== ''"
          style="width: 300px"
          placeholder="付款方名称"
          clearable
          maxlength="50"
        ></el-input>
      </el-form-item>
      <el-form-item label="税率：">
        <span style="width: 200px">{{ invoiceDataForm.taxrate }}</span>
      </el-form-item>
      <el-form-item label="付款方识别号：">
        <el-input
          v-model="invoiceDataForm.spayercreditcode"
          :disabled="invoiceDataForm.invoicecode !== ''"
          style="width: 300px"
          placeholder="付款方识别号"
          clearable
          maxlength="50"
        ></el-input>
      </el-form-item>
      <el-form-item label="付款方地址电话：">
        <el-input
          v-model="invoiceDataForm.spayeraddr"
          :disabled="invoiceDataForm.invoicecode !== ''"
          style="width: 300px"
          placeholder="付款方地址电话"
          clearable
          maxlength="100"
        ></el-input>
      </el-form-item>
      <el-form-item label="付款方开户行账号：">
        <el-input
          v-model="invoiceDataForm.spayerbank"
          :disabled="invoiceDataForm.invoicecode !== ''"
          style="width: 300px"
          placeholder="付款方开户行账号"
          clearable
          maxlength="20"
        ></el-input>
      </el-form-item>
      <el-form-item label="开票项目：">
        <el-select
          v-model="invoiceDataForm.printitemname"
          :disabled="invoiceDataForm.invoicecode !== ''"
          placeholder="请选择开票项目"
          clearable
          filterable
          style="width: 300px"
          @change="onItemChange"
        >
          <el-option
            v-for="t in adprintitemCombo"
            :key="t.id"
            :label="t.sname"
            :value="t.id"
          ></el-option>
        </el-select>
        <!-- <el-input v-model="invoiceDataForm.printitemname" style="width: 300px"></el-input> -->
      </el-form-item>
      <el-form-item label="税收编码：">
        <el-input
          v-model="invoiceDataForm.staxcode"
          :disabled="invoiceDataForm.invoicecode !== ''"
          style="width: 300px"
          placeholder="税收编码"
          clearable
          maxlength="30"
        ></el-input>
      </el-form-item>
      <el-form-item label="开票金额：">
        <el-input
          v-model="invoiceDataForm.namount"
          :disabled="true"
          style="width: 300px"
        ></el-input>
      </el-form-item>
      <el-form-item label="金额大写：">
        <el-input
          v-model="invoiceDataForm.amountinwords"
          :disabled="true"
          style="width: 300px"
          placeholder="金额大写"
          clearable
          maxlength="50"
        ></el-input>
      </el-form-item>
      <el-form-item label="备注：">
        <el-input
          v-model="invoiceDataForm.sremark"
          :disabled="invoiceDataForm.invoicecode !== ''"
          maxlength="200"
          style="width: 300px"
          placeholder="备注"
          show-word-limit
          type="textarea"
        />
        <!-- <el-input
          v-model="invoiceDataForm.sremark"
          style="width: 600px"
          placeholder="备注"
          clearable
          maxlength="200"
        ></el-input> -->
      </el-form-item>
      <el-form-item label="开票人：">
        <el-input
          v-model="invoiceDataForm.lastoperator"
          :disabled="invoiceDataForm.invoicecode !== ''"
          placeholder="开票人"
          maxlength="20"
          style="width: 300px"
        ></el-input>
      </el-form-item>
      <el-form-item label="收款人：">
        <el-input
          v-model="invoiceDataForm.scashier"
          :disabled="invoiceDataForm.invoicecode !== ''"
          placeholder="收款人"
          maxlength="20"
          style="width: 300px"
        ></el-input>
      </el-form-item>
      <el-form-item label="复核人：">
        <el-input
          v-model="invoiceDataForm.schecker"
          :disabled="invoiceDataForm.invoicecode !== ''"
          placeholder="复核人"
          maxlength="20"
          style="width: 300px"
        ></el-input>
      </el-form-item>
      <el-form-item label="开票类型" prop="iinvoicetype">
        <el-select
          v-model="invoiceDataForm.istype"
          placeholder="请选择开票类型"
          style="width: 300px"
        >
          <el-option
            v-for="(value, name) in filteredEPreinvoiceStyle"
            :key="value"
            :label="name"
            :value="value"
            :class="{ 'gray-font': value <= 80 }"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="关联合同"></el-form-item>
      <el-form-item label="通知单号" prop="iinvoicetype">
        <el-input
          v-model="sNoticeNumber"
          :disabled="invoiceDataForm.invoicecode !== ''"
          placeholder="发票冲红时需要填写16位通知单号"
          maxlength="20"
          style="width: 300px"
        ></el-input>
      </el-form-item>
      <el-table
        :data="invoiceDataForm.contractVos"
        row-key="id"
        :border="true"
        stripe
        style="width: 90%; margin-left: 50px"
      >
        <el-table-column
          label="合同号"
          prop="scontractnum"
          min-width="140"
          align="center"
          show-overflow-tooltip
        >
          <template #default="scope">
            <el-button
              v-if="scope.row.scontractnum"
              link
              type="primary"
              icon="view"
              size="small"
              @click="handleSee(scope.row, scope.$index)"
              >{{ scope.row.scontractnum }}</el-button
            >
          </template>
        </el-table-column>
        <el-table-column
          label="广告标题"
          sortable="custom"
          min-width="150"
          prop="sadtitle"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column label="广告分类" width="100" prop="adtypename"> </el-table-column>
        <el-table-column
          label="欠款金额"
          sortable="custom"
          min-width="150"
          prop="amountarrearage"
          align="right"
        >
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="广告行业"
          sortable="custom"
          width="120"
          prop="adindustryname"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column label="经营主体" min-width="160" prop="businessentityname">
        </el-table-column>
        <el-table-column
          label="客户名称"
          sortable="custom"
          min-width="150"
          prop="customername"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          label="代理公司名称"
          sortable="custom"
          min-width="150"
          prop="agencyname"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          label="内容生产方名称"
          sortable="custom"
          min-width="150"
          prop="agentname"
          show-overflow-tooltip
        >
        </el-table-column>
      </el-table>
    </el-form>
    <div style="text-align: center; width: 100%; margin-top: 10px">
      <el-label class="el-form-item__label">电子发票状态</el-label
      ><el-input
        v-model="einvoicestatus"
        disabled
        style="width: 150px; margin-right: 10px"
      ></el-input>
      <el-button type="primary" @click="onOpen">打开电子发票</el-button>
      <el-button type="primary" @click="onClose">关闭电子发票</el-button>
      <el-button type="primary" @click="onEinvoicePrint">发票打印</el-button>
    </div>
  </div>
  <!-- 根据合同号查看详情 -->
  <AdTworderdetails ref="viewImageRef"></AdTworderdetails>
</template>
<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import useUserStore from '@/store/modules/user'
import { IInvoiceDTO } from '@/types/views/finance/invoice'
import { IAxios } from 'axios'
import { getInvoiceByIdApi, saveInvoiceCodeApi } from '@/api/finance/invoiceprint'
import { IAdprintitem } from '@/types/views/finance/adprintitem'
import { getAdPrintItemComboApi } from '@/api/finance/adprintitem'
import { updateInvoiceByInvoiceDTOApi } from '@/api/finance/invoice'
import { EPreinvoiceStyle } from '@/types/common/enumindex'
import { formatMoney } from '@/utils'
import dayjs from 'dayjs'

defineOptions({
  name: 'HgInvoicePrint'
})
const props = defineProps({
  /** 发票表id */
  invoiceid: {
    type: String,
    default: ''
  }
})
const invoiceInfo = {
  businessentityname: '',
  customername: '',
  dcreatetime: '',
  employname: '',
  id: '',
  invoice: '',
  lastoperator: '',
  namount: 0,
  printitemname: '',
  scashier: '',
  schecker: '',
  spayeraddr: '',
  spayerbank: '',
  spayercreditcode: '',
  sprintname: '',
  staxcode: '',
  recipient: '',
  taxitems: '',
  taxrate: '',
  invoiceCode: ''
}

/** 电子发票状态 */
const einvoicestatus = ref('电子发票已关闭')
/** 打印form对象 */
const invoiceDataForm = ref<IInvoiceDTO>({ ...invoiceInfo })
/** 开票项目下拉 */
const adprintitemCombo = ref<IAdprintitem[]>([])
const sNoticeNumber = ref('')
/** 用于判断是否已打印过发票的标记 */
const QInfoTypeCode = ref('')
const user = useUserStore().user
watch(
  () => props.invoiceid,
  (val) => {
    getInvoiceById(val)
  }
)

onMounted(() => {
  getAdPrintItemCombo()
  const id = props.invoiceid
  if (id) {
    getInvoiceById(id)
  }
})

const filteredEPreinvoiceStyle = computed(() => {
  return Object.entries(EPreinvoiceStyle)
    .filter(([name, value]) => typeof value === 'number')
    .sort((a, b) => Number(b[1]) - Number(a[1])) // 倒序
    .reduce((acc, [name, value]) => ({ ...acc, [name]: value }), {})
})

/**
 * 开票项目下拉
 */
const getAdPrintItemCombo = () => {
  getAdPrintItemComboApi().then(({ success, obj }: IAxios<IAdprintitem[]>) => {
    if (success) {
      adprintitemCombo.value = obj
    }
  })
}
/**
 * 获取打印发票详情
 * @param id
 */
const getInvoiceById = (id: string) => {
  const data = {
    id: id
  }
  getInvoiceByIdApi(data).then((res: IAxios) => {
    if (res.success) {
      invoiceDataForm.value = { ...res.obj }
      invoiceDataForm.value.amountinwords = numberToChinese(invoiceDataForm.value.namount!)
    }
  })
}
/**
 * 开票项目改变
 * @param val
 */
const onItemChange = (val: string) => {
  const item = adprintitemCombo.value.find((t) => t.id === val)
  if (item) {
    invoiceDataForm.value.staxcode = item.staxcode
  }
}
/**
 * 打开电子发票
 */
const onOpen = () => {
  if (einvoicestatus.value === '电子发票已开启' || einvoicestatus.value === '电子发票已查询') {
    return
  }
  /* eslint-disable */
  $.ajax({
    url: 'http://localhost:19863/TaxCardHttps/tax_openCard',
    method: 'POST',
    timeout: 0,    
    headers: {
      token:
        '839mkHas0we093s262Wfmsa+fds23ia13dfkRidMNo92Ul01266qasfDn636OoZ99xzZ5550ToMyHoo396332LxvIEbs520CxZ0109wLf',
      'Content-Type': 'application/json;charset=UTF-8',
      Accept: '*/*',
      Host: 'localhost:19863',
      Connection: 'keep-alive',
      'Access-Control-Allow-Orign': '*'
    },
    data: {"certPassWord":"12345678","uploadInvoiceAuto":0},
    success: function (data) {
      einvoicestatus.value = '电子发票已开启'
    },
    error: function () {
      einvoicestatus.value = '电子发票已开启'
    }
  })
  /* eslint-enable */
  // const httpRequest = new XMLHttpRequest() // 第一步：创建需要的对象localhost:9883
  // httpRequest.open('POST', 'http://localhost:19863/TaxCardHttps/tax_openCard', true) // 第二步：打开连接
  // httpRequest.setRequestHeader('Content-type', 'text/plain;charset=UTF-8') // 设置请求头 注：post方式必须设置请求头（在建立连接后设置请求头）

  // httpRequest.setRequestHeader(
  //   'token',
  //   // prettier-ignore
  //   '839mkHas0we093s262Wfmsa+fds23ia13dfkRidMNo92Ul01266qasfDn636OoZ99xzZ5550ToMyHoo396332LxvIEbs520CxZ0109wLf'
  // )
  // const sPost = { certPassWord: '12345678', uploadInvoiceAuto: 0 } as any
  // // ("{'certPassWord':'83000305','uploadInvoiceAuto':0}");
  // // httpRequest.send('{ "certPassWord": "12345678", "uploadInvoiceAuto": 0 }')
  // let sTmp = JSON.stringify(sPost) as String
  // sTmp = sTmp.replaceAll('"', "'")
  // // prettier-ignore
  // httpRequest.send({ 'certPassWord': '12345678', 'uploadInvoiceAuto': 0 })
  // httpRequest.onreadystatechange = function () {
  //   ElMessage.success('成功')
  //   einvoicestatus.value = '电子发票已开启'
  //   const json = httpRequest.responseText // 获取到服务端返回的数据
  //   if (httpRequest.readyState === 4 && httpRequest.status === 200) {
  //     ElMessage.success('失败')
  //     einvoicestatus.value = '电子发票已关闭'
  //   }
  // }
}
/**
 * 关闭电子发票
 */
const onClose = () => {
  if (einvoicestatus.value !== '电子发票已开启' && einvoicestatus.value !== '电子发票已查询') {
    return
  }
  $.ajax({
    url: 'http://localhost:19863/TaxCardHttps/tax_closeCard',
    headers: {
      'Content-type': 'text/plain;charset=UTF-8',
      // prettier-ignore
      'token':
        '839mkHas0we093s262Wfmsa+fds23ia13dfkRidMNo92Ul01266qasfDn636OoZ99xzZ5550ToMyHoo396332LxvIEbs520CxZ0109wLf'
    },
    type: 'POST',
    dataType: 'json',
    async: false,
    data: {},
    success: function (data) {
      einvoicestatus.value = '电子发票已关闭'
    },
    error: function () {
      einvoicestatus.value = '电子发票已关闭'
    }
  })
}

const toDecimal2 = (x: any) => {
  const x1 = parseFloat(x)
  if (isNaN(x1)) {
    return '0'
  }
  const f = Math.round(x1 * 100) / 100
  let s = f.toString()
  let rs = s.indexOf('.')
  if (rs < 0) {
    rs = s.length
    s += '.'
  }
  while (s.length <= rs + 2) {
    s += '0'
  }
  return s
}
/**
 * 发票打印
 */
const onEinvoicePrint = () => {
  if (invoiceInfo.invoiceCode !== '') {
    ElMessage.error('发票已打印，请勿重复打印')
    return
  }
  if (einvoicestatus.value !== '电子发票已开启') {
    ElMessage.error('电子发票未开启或发票数据已查询')
    return
  }
  if (!confirm('确定要打印发票吗?')) {
    return
  }

  // 客户名称100位字符
  const CName = invoiceInfo.customername

  // 付款方识别号
  const TaxCode = invoiceInfo.spayercreditcode
  // 地址+电话 100位字符
  const CAddressPhone = invoiceInfo.spayeraddr
  // 银行名称+帐号 100位 字符
  const CBankName = invoiceInfo.spayerbank
  if (invoiceDataForm.value.istype && invoiceDataForm.value.istype === 81) {
    if (TaxCode === '') {
      ElMessage.error('专用发票请填写付款方识别号')
      return
    }
  }

  // 税收分类编码
  const goodsTaxNo = invoiceDataForm.value.staxcode
  if (invoiceDataForm.value.istype && invoiceDataForm.value.istype === 81) {
    if (goodsTaxNo && (goodsTaxNo === '' || goodsTaxNo.length !== 19)) {
      ElMessage.error('请填写19位税收分类编码')
      return
    }
  }

  // 收款人 10位字符
  const Payee = invoiceDataForm.value.scashier
  // 复核人 10位字符
  const Checker = invoiceDataForm.value.schecker
  // 开票人 10位字符
  const BillMan = invoiceDataForm.value.lastoperator
  // 税率为0.XX或XX,都可以
  const TaxRate = invoiceDataForm.value.taxrate // parseFloat(toDecimal2(document.all.txtTax.value));//"0.06";
  // 清单名称,当明细大于8行时,会自动生成清单发票.此时该值不能为空,应该为"详见货物清单"字样,小于等于8行时,该值为空.
  const InfoListName = '' // "科税接口";
  // 销方地址+电话 100位字符
  const InfoSellAddreePhone = '' // document.all.lbAddressTel.value;//"广州市广州大道中289号南方日报社87373998";
  // 销方银行名称+帐号 100位字符
  const InfoSellBankCode = '' // document.all.lbBank.value ;//"建行股份有限公司广州五羊新城支行44001400905050081342";
  // 组织编号
  const InfoOrgID = 'Test'
  // 发票种类 s为专用发票 c为普通发票
  const InfoKind = invoiceDataForm.value.istype

  let strNote = ''
  let iNumber = 1
  const namount = invoiceDataForm.value.namount ?? 0
  if (namount === 0) {
    ElMessage.error('开票金额不能为0')
    return
  }
  if (namount < 0) {
    iNumber = -1
    if (InfoKind && InfoKind === 0) {
      if (sNoticeNumber.value === '' || sNoticeNumber.value.length !== 16) {
        alert('请填写16位通知单号')
        return
      }
      strNote = '开具红字增值税专用发票通知单号' + sNoticeNumber.value
    } else {
      strNote = '对应正数发票代码:' + invoiceInfo.invoice + '号码:' + invoiceInfo.invoiceCode
    }
  }

  // 备注 240位字符
  const Remark = strNote + invoiceDataForm.value.sremark

  const infoBillnumberTmp = dayjs(new Date()).format('YYYYMMDDHHmmss')

  let econtent = `{"infoBillNumber":${infoBillnumberTmp},"cAddress":"${CAddressPhone}","cBank":"${CBankName}","cName":"${CName}","cTaxCode":"${TaxCode}","cashier":"${Payee}",`
  econtent += `"checker":"${Checker}","correspondingNumber":"","correspondingTypeCode":"","discountFlag":0,"infoKind":${InfoKind},"invoicer":"${BillMan}","listName":"",`
  let nAmountSum = 0
  let nTaxAmountSum = 0

  // 发票明细超过8行，将自动开具销货清单
  // a.ClearInvList();
  // 第一行明细

  // 商品编号 20位字符
  const ListGoodsCode = '1'
  // 商品名称 40位字符
  const ListGoodsName = invoiceDataForm.value.printitemname ?? ''
  // 商品税目 4位字符
  const ListTaxItem = invoiceDataForm.value.taxitems ?? ''
  // 商品规格型号 40位字符
  const ListStandard = ''
  // 计量单位 10位字符
  const ListUnit = ''
  // 数量
  const ListNumber = ''

  // 税率 0.XX或XX
  const sTaxRate = invoiceDataForm.value.taxrate ?? '0'
  if (sTaxRate === '0') {
    ElMessage.error('税率不能为0')
    return
  }
  const ListTaxRate = parseFloat(sTaxRate) / 100

  // 不含税单价  取8位以上小数
  const ListPrice = namount / (1 + ListTaxRate) // 不含税单价 取8位以上小数

  // 不含税金额 保留2位小数
  const ListAmount = toDecimal2(namount / (1 + ListTaxRate))

  // 单价标志 0为不含税单价 1为含税单价   此地默认为一定是0
  const ListPriceKind = '0'
  // 税额 保留2位小数
  const ListTaxAmount = toDecimal2((namount * ListTaxRate) / (1 + ListTaxRate))

  // 不含税折扣金额 保留2位小数
  // let ListDisAmount = '0'
  // 折扣税额 保留2位小数
  // let ListDisTaxAmount = '0'

  econtent += `"negativeFlag":0,"negNoticeNo":"","notes":"${Remark}","sAddress":"${InfoSellAddreePhone}","sBank":"${InfoSellBankCode}","taxRate":${TaxRate},"details":[{`
  econtent += `"zeroTax":"","taxDeduction":"","goodsNoVer":"39.0","goodsTaxNo":"${goodsTaxNo}","taxPre":0,"taxPreCon":"","amount":${namount},"discountFlag":0,"discountRate":0,`
  econtent += `"goodsName":"${ListGoodsName}","number":${iNumber},"price":${Math.abs(
    parseFloat(ListAmount)
  )},"priceKind":0,"sequence":1,"standard":"","taxAmount":${ListTaxAmount},`
  econtent += `"taxItem":"","taxRate":${ListTaxRate},"unit":""}`
  nAmountSum += parseFloat(ListAmount) * 1
  nTaxAmountSum += parseFloat(ListTaxAmount) * 1
  econtent += `],"totalAmount":${nAmountSum},"totalTaxAmount":${nTaxAmountSum}}`
  if (InfoKind !== 81 && InfoKind !== 82) {
    alert('本系统仅支持全电发票')
    return
  }
  $.ajax({
    url: 'http://localhost:19863/TaxCardHttps/tax_invoice',
    headers: {
      'Content-type': 'text/plain;charset=UTF-8',
      // prettier-ignore
      'token':
        '839mkHas0we093s262Wfmsa+fds23ia13dfkRidMNo92Ul01266qasfDn636OoZ99xzZ5550ToMyHoo396332LxvIEbs520CxZ0109wLf'
    },
    type: 'POST',
    dataType: 'json',
    async: false,
    data: JSON.parse(econtent),
    success: function (data) {
      ElMessage.success('打印成功')
      QInfoTypeCode.value = ''
    },
    error: function () {
      ElMessage.error('打印失败')
    }
  })
}
/**
 * 发票打印
 */
const onPrint = () => {
  updateInvoiceByInvoiceDTOApi(invoiceDataForm.value).then((res: IAxios) => {
    if (res.success) {
      // -------------------调用第三方打印接口打印发票
      onEinvoicePrint()
    }
  })
}
/**
 * 打印发票后保存正式的发票号和发票编码---------------------待使用
 */
const saveInvoiceCode = () => {
  const data = {
    invoiceId: props.invoiceid,
    invoice: '',
    invoiceCode: ''
  }
  saveInvoiceCodeApi(data).then((res: IAxios) => {
    if (res.success) {
      ElMessage.success('保存成功')
    }
  })
}
/**
 * 发票额度大写
 * @param num
 */
const numberToChinese = (num: number) => {
  const chineseNum = ['零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖']
  const chineseUnit = [
    '',
    '拾',
    '佰',
    '仟',
    '万',
    '拾',
    '佰',
    '仟',
    '亿',
    '拾',
    '佰',
    '仟',
    '万',
    '拾',
    '佰',
    '仟'
  ]

  let result = ''
  let unitIndex = 0

  while (num > 0) {
    const remainder = num % 10
    if (remainder !== 0) {
      result = chineseNum[remainder] + chineseUnit[unitIndex] + result
    } else {
      if (result !== '') {
        result = chineseNum[remainder] + result
      }
    }
    num = Math.floor(num / 10)
    unitIndex++
  }

  return result
}
/**
 * 根据合同号查看订单项目--------------------------------------------------
 * @param row
 * @param index
 */
import AdTworderdetails from '@/views/ad/AdTworderdetails.vue'
const viewImageRef = ref()
const handleSee = (row?: any, number?: number) => {
  viewImageRef.value.view(row.scontractnum)
}
// ---------------------------------------------------------------------
</script>
<style lang="scss" scoped>
.el-form-item {
  margin-bottom: 15px;
}
.gray-font {
  color: #ccc;
}
</style>
