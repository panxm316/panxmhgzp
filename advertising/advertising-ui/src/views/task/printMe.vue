<!--
 * @Author: suny
 * @Date: 2023-10-17 10:53:29
 * @LastEditTime: 2024-03-07 11:03:08
 * @LastEditors: wanghl
 * @Description: 我的发起
-->
<template>
  <div class="app-container">
    <el-dialog v-model="dialogVisible" title="发布认刊书" width="1000">
      <el-button v-print="printObj" type="primary" style="margin-left: 90%">打印</el-button>
      <div id="printMe">
        <!-- <img
          src="../../assets/images/nanfang.jpg"
          alt=""
          style="height: 50px; position: absolute; z-index: 9999; left: 30px; margin: 20px 0"
        /> -->
        <table border="0" width="100%" style="margin: 50px auto 0 !important">
          <thead>
            <tr>
              <th colspan="8">全媒体发布认刊书</th>
            </tr>
            <tr style="height: 30px">
              <td colspan="8" style="font-size: 13px">
                <span>电话号码：{{ TworderData?.smobilephone }} </span>
                <span style="margin: 10px 40px">签订日期：{{ TworderData?.createtime }}</span>
                <span>合同编号：{{ TworderData?.scontractnum }} </span>
              </td>
            </tr>
          </thead>

          <tbody>
            <tr style="height: 40px">
              <td colspan="1">客户名称</td>
              <td colspan="3">
                {{ TworderData?.customername }}
              </td>
              <td colspan="1">类别</td>
              <td colspan="3">1</td>
            </tr>
            <tr style="height: 40px">
              <td colspan="1">发布内容</td>
              <td colspan="3">{{ TworderData?.sadcontent }}</td>
              <td colspan="1">项目</td>
              <td colspan="3">{{ TworderData?.adprojectname }}</td>
            </tr>
            <tr>
              <td colspan="8">
                <el-table
                  :data="TworderData?.orderitem"
                  row-key="id"
                  :border="true"
                  stripe
                  width="100%"
                >
                  <el-table-column prop="medianame" label="媒体"> </el-table-column>
                  <el-table-column prop="prestartdate" label="预刊时间" sortable width="120">
                    <template #default="scope">
                      <span>{{ formatDatesm(scope.row.prestartdate) }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column prop="foldname" label="叠次版本" min-width="120">
                  </el-table-column>
                  <el-table-column prop="adspecname" label="广告规格"> </el-table-column>
                  <el-table-column prop="baseprice" label="刊例价" sortable="custom" width="120">
                    <template #default="scope">
                      <span>{{ scope.row.baseprice }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="amountreceivable"
                    label="签订金额"
                    sortable="custom"
                    width="120"
                  >
                    <template #default="scope">
                      <span>{{ scope.row.amountreceivable }}</span>
                    </template>
                  </el-table-column>
                </el-table>
              </td>
            </tr>
            <tr style="height: 40px">
              <td colspan="1">合计应付金额（大写）</td>
              <td colspan="6">人民币：{{ convertCurrencyData }}</td>
              <td colspan="1"><span>￥</span>{{ price }}元</td>
            </tr>
            <tr style="height: 40px">
              <td colspan="8">
                开户账号：{{ TworderData?.adtypename }} 单位名称：{{
                  TworderData?.adtypename
                }}
                账号：{{ TworderData?.adtypename }}
              </td>
            </tr>
            <tr style="height: 100px">
              <td>备注</td>
              <td colspan="7">{{ TworderData?.sremark }}</td>
            </tr>
            <tr style="height: 160px">
              <td colspan="4">
                <div
                  style="
                    width: 100%;
                    text-align: left;
                    font-weight: bold;
                    line-height: 30px;
                    padding-left: 10px;
                  "
                >
                  甲方(签名盖章):{{ TworderData?.businessentityname }} <br />地址:{{
                    TworderData?.saddress
                  }}
                  <br />邮编:{{ TworderData?.spostcode }} <br />电话:{{
                    TworderData?.smobilephone
                  }}
                  <br />传真:{{ TworderData?.smobilephone }} <br />联系人:{{
                    TworderData?.scontacts
                  }}
                </div>
              </td>
              <td colspan="4">
                <div
                  style="
                    width: 100%;
                    text-align: left;
                    font-weight: bold;
                    line-height: 30px;
                    padding-left: 10px;
                  "
                >
                  甲方(签名盖章): <br />地址:广州市广州大道中289号 <br />地址: <br />邮编:
                  <br />电话: <br />传真: <br />
                  接稿联系人:
                </div>
              </td>
            </tr>
            <tr style="height: 180px">
              <td colspan="8">
                <div style="width: 100%; text-align: left; line-height: 30px; padding-left: 10px">
                  <span style="font-weight: bold">合同条款</span><br />
                  1，全媒体发布须知按有关法规办理。甲方办理认刊手续时，须持真实、有效营业执照等有关文件及证明材料并承担法律责任。发布内容、文字及形如有现行法律法规政策相违及不雅内容，乙方有删改和拒绝刊登权。甲方自行提供文稿、图片和设计须确保稿件文章或图片的合法性，如该文室侵犯他人的著作权、商标权、肖像权、名誉权等其他合法权利的，由此引起的全部法律责任均由甲方承担，<br />2、甲方须于产品发布前结付款项。否则，乙方有权推延或拒绝发布。
                  <br />3、各发布媒体及产品服务的详细条款见对应价目表，本合同条款解释权归广东南方日报经营有限公司。
                </div>
              </td>
            </tr>
            <!-- <tr style="height: 180px">
              <td><div style="width: 70px; margin: auto !important">报送单位预览</div></td>
              <td colspan="7">
                <div class="right">
                  <div class="top">
                    <span class="leader">领导签字:{{}}</span>
                    <span class="official_seal">(公章)</span>
                  </div>
                  <div class="bottom">
                    <span class="year">{{}}年</span>
                    <span class="month">{{}}2月</span>
                    <span class="day">{{}}日</span>
                  </div>
                </div>
              </td>
            </tr> -->
          </tbody>
        </table>
      </div>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { getorderByIdApi } from '@/api/ad/adtworder'
import type { IAxios } from 'axios'
import type { Tworder } from '@/types/views/ad/adtworder'
import { formatDatesm, convertCurrency } from '@/utils/index'
defineOptions({
  name: 'printMe'
})
const props = defineProps<{
  id?: string
}>()
const dialogVisible = ref(false)
const printObj = ref({
  id: 'printMe', // 这里是要打印元素的ID
  popTitle: '&nbsp', // 打印的标题
  extraCss: '', // 打印可引入外部的一个 css 文件
  extraHead: '', // 打印头部文字
  preview: false, // 是否启动预览模式，默认是false
  previewTitle: '打印客户账单', // 打印预览的标题
  previewPrintBtnLabel: '预览结束，开始打印', // 打印预览的标题下方的按钮文本，点击可进入打印
  zIndex: 10002, // 预览窗口的z-index，默认是20002，最好比默认值更高
  previewBeforeOpenCallback() {
    console.log('正在加载预览窗口！')
  }, // 预览窗口打开之前的callback
  previewOpenCallback() {
    console.log('已经加载完预览窗口，预览打开了！')
  }, // 预览窗口打开时的callback
  beforeOpenCallback() {
    console.log('开始打印之前！')
  }, // 开始打印之前的callback
  openCallback() {
    console.log('执行打印了！')
  }, // 调用打印时的callback
  closeCallback() {
    console.log('关闭了打印工具！')
  }, // 关闭打印的callback(无法区分确认or取消)
  clickMounted() {
    console.log('点击v-print绑定的按钮了！')
  }
})
/** 订单信息 */
const TworderData = ref<Tworder>()
/** 价格综合 */
const price = ref(0)
/** 大写数 */
const convertCurrencyData = ref('')
onMounted(() => {})
/** 监听传入修改时的订单id */
watch(
  () => props.id,
  (newValue, oldValue) => {}
)
const view = (id: string) => {
  console.log(id)
  dialogVisible.value = true
  getorderByIdApi(id).then(({ obj, success, msg }: IAxios) => {
    console.log(obj)
    if (success) {
      TworderData.value = obj
      price.value = obj.orderitem.reduce((total: number, item: any) => {
        return total + item.amountreceivable
      }, 0)
      convertCurrencyData.value = convertCurrency(price.value)
    }
  })
}
defineExpose({ view })
</script>
<style lang="scss" scoped>
#printMe {
  width: 100%;
  display: flex;
  justify-content: center;
  align-content: center;
}

.borderForm {
  border: 1px solid #ccc;
  padding: 10px; /* 可选，用于增加表单与边框之间的间距 */
}

@media print {
  @page {
    size: auto;
    margin: 0;
  }
  body,
  html {
    //如果vue最外层id，默认是#app。如果设置了height:100%;，那么#app也加
    height: auto !important;
  }
}
.el-descriptions {
  margin-top: 20px;
}
//表格样式
#printMe .right .top .leader {
  float: left;
  margin-left: 30px !important;
}
#printMe .right .top {
  margin-top: 100px !important;
}
#printMe .right .top .official_seal {
  margin-right: 30px !important;
}
#printMe .bottom {
  margin-top: 10px !important;
}
#printMe .bottom .year,
#printMe .bottom .month {
  margin-right: 40px !important;
}
#printMe #title {
  padding-top: 50px !important;
  padding-bottom: 30px !important;
}
#printMe {
  width: 70%;
  margin: auto;
  background-image: url('../../assets/images/nanfang.png');
  background-repeat: no-repeat;
  background-size: 400px 50px;
  // border: 2px solid black;
  padding-bottom: 30px !important;
}
#printMe table {
  border-collapse: collapse;
}
#printMe table thead th {
  font-size: 20px;
  padding: 10px;
}
#printMe table tbody tr {
  height: 30px;
  font-size: 14px;
}
#printMe table tbody td {
  border: 1px solid black;
  text-align: center !important;
  width: 25%;
}
#printMe table tbody td span {
  margin-right: 20px;
}
</style>
