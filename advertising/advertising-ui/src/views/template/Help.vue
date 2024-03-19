<!--
 * @Author: songly
 * @Date: 2023-08-30 16:08:23
 * @LastEditTime: 2023-10-23 15:21:36
 * @LastEditors: wanghl
 * @Description:范围
-->
<template>
  <div
    style="
      background-color: #fff;
      height: 100%;
      padding-left: 200px;
      overflow: auto;
      position: fixed;
      top: 0px;
    "
  >
    <div class="navbox">
      <el-menu :default-active="dingwei" class="el-menu-vertical-demo">
        <el-menu-item index="index" @click="handleOpen('index')">
          <el-icon><icon-menu /></el-icon>
          <span>系统介绍</span>
        </el-menu-item>
        <el-sub-menu index="1">
          <template #title>
            <el-icon><icon-menu /></el-icon>
            <span>广告排期</span>
          </template>
          <el-menu-item index="adproject" @click="handleOpen('adproject')">广告项目</el-menu-item>
          <el-menu-item index="fold" @click="handleOpen('fold')">广告叠次</el-menu-item>
          <el-menu-item index="foldareaver" @click="handleOpen('foldareaver')"
            >叠次版本</el-menu-item
          >
          <el-menu-item index="pagetype" @click="handleOpen('pagetype')">版本类别</el-menu-item>
          <el-menu-item index="adspec" @click="handleOpen('adspec')">广告类型</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="2">
          <template #title>
            <el-icon><icon-menu /></el-icon>
            <span>任务管理</span>
          </template>
          <el-menu-item index="pending" @click="handleOpen('pending')">我的待办</el-menu-item>
          <el-menu-item index="started" @click="handleOpen('started')">我的发起</el-menu-item>
          <el-menu-item index="completed" @click="handleOpen('completed')">我的已办</el-menu-item>
        </el-sub-menu>
        <!-- <el-menu-item index="4">
          <el-icon><setting /></el-icon>
          <span>Navigator Four</span>
        </el-menu-item> -->
      </el-menu>
    </div>
    <div>
      <h1>系统使用帮助手册</h1>
      <!-- 系统介绍 -->
      <div>
        <h2>系统介绍</h2>
        <h3 id="index"><router-link to="/index">模块划分</router-link></h3>
        <div class="img_box">
          <p>
            <strong>总体业务流程</strong>
          </p>
          <p style="text-align: justify">
            <strong>业务员新客户挂牌流程</strong
            ><br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;业务员根据自己线下了解，接触新客户，发展成功后，上报客户信息（上传客户营业执照、联系人名片），流程为
            部门总监
            审批（非部门经理，多个部门有一个部门总监），确认是否为新客户，如果为新客户，则归属给此业务员。围绕此客户相关的服务日志要填写上报，副总可能会看业务员如何围绕此客户开展业务的。<br />并非所有的新客户都走审批流程，政务与记者站等新客户不需要走流程。<br /><strong
              >项目管理流程</strong
            ><br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;报社所有的广告都按项目制录入，报社领导会制定近期开展哪些项目，并下发给相关人员进行项目的录入与修改，项目主要属性有名称，日期范围，预收入，成本等，最终项目还有结项功能，并能提交相关附件。<br /><strong
              >总体业务流程：</strong
            ><br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1、业务员首先进行排期查看，是否有合适的广告位置，然后与客户洽谈近期有何合作计划，如果是重要位置的（比如A叠头版、末版、南方+开屏广告等），经领导审批后才能下单（线下审批，资源预审时上传审批图片），其余版面不限制，然后与客户签合同（合同管理不需要在广告系统中处理，有专门的合同管理系统，目前还未使用，将来可能要与合同管理系统对接，获取合同的内容等，现在先不用）<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2、资源预审：合同签订后先进行资源（广告相关的资源，包含文字等）预审，需要经过3审，1审为业务员自己，2审为客户服务部，3审为版面部，需要提交与客户签署的合同扫描件及其它相关的审批意见电子文档。<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3、口头预定：业务员可以先通过口头预定的方式提前占好广告位置，因后续的审批流程时间可能较长。<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4、认刊合同审批：资源预审审核通过后，生成电子认刊合同（原来的纸质的认刊书，现在转为电子版的，格式要按照纸质的来），如果有关联的项目要选择项目，如果没有选无（后续如果有关联的项目，需要通过认刊合同的修改功能再关联上相关项目），认刊合同要根据设置的审批流程进行电子审批（只是相关审批者点击同意或否决，并填写审批意见）。<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5、订单录入：电子认刊合同审批完后由客户服务部（或业务员自己，具体谁录入最终由报社设置）录入完整的订单信息，生成认刊合同号，如果有折扣，则先进入折扣审批流程（根据报社提供的折扣方案文件），不同的折扣率有不同的审批者，折扣审批完成后再提交至认刊合同的审批环节，审批流转中需附带之前所有的相关联的资源及附件文件。<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6、订单审批通过后，生成电子出版流程单（有3种格式、报纸的，新媒体的）发送给业务员，业务员签字后提交至版面部（两微的提交至媒体实验室人员），版面部经过审核审批后进行版位安排（报纸的为版面部人员处理，南方+的由版面部人员与南方+发布系统对接），流程处理完后见报或发布。<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7、预开发票申请：生成认刊合同后，业务员可申请预开发票，生成电子申请书（需附加经营主体、专票普票、客户机构代码、开票项目等），提交相关审批流程。<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;8、预开发票打印：申请通过后，财务相关人员可自动匹配申请中的相关信息，进行简单的调整，通过
            第三方税务局发票系统
            开出电子发票并下载（发票文件名中带有发票代码及开票名称），再将电子发票上载至广告系统，业务员可通过申请信息查看并下载电子发票。<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;9、预开发票核销：客户汇款后，需要对预开发票进行核销，原流程为财务人员每天都通过银行网站下载下来银行流水（excel格式），将银行流程放入共享盘，业务员自己查看银行流水是否有自己客户的汇款，根据汇款金额，自己指定这笔汇总要核销哪张发票哪个合同，整理成EXCEL表，到财务，财务根据业务员的EXCEL表进行平账核销，因为时间跨度大，经常出错，造成发票与合同对应不上，新系统流程为财务人员导入银行流水，系统根据信息能大部分自动匹配到相关的合同及发票，业务员通过系统进行人工调整匹配，确认后，财务人员再根据相应的信息进行平账核销。<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;财务重点强调，这个台账一定要清晰明了，就是通过认刊合同
            能关联到合同相关的所有信息，一目了然，如合同相关的申请，发票信息，欠款信息，收款信息等。<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;如查询一段时间内的认刊合同，表格中要显示认刊合同所关联的发票，每条记录点进去看明细能看到具体的发票信息。<br />查询一段时间内的发票，表格中能显示此发票相关联的合同号，每条记录点进去看明细能看到具体的合同信息。<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;另外财务提供了其它一些相关的报表模板。<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;10、佣金计提：见报且到款后（目前是完全到款），财务人员先核对待计提的数据，并通知业务员填写每笔数据的成本（提交证明图片），核实准确后，通知行政人员进行佣金计提，行政部门人员进行佣金核算（业务员计提佣金与代理人计提佣金两种类型，提成点不同，有代理人的，不能给业务员算提成），有相应的报表模板。<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;11、报表统计：行政部人员进行数据统计汇总，提供了很多报表模板
          </p>
        </div>
      </div>
      <!-- 基础信息 -->
      <div>
        <h2>广告排期</h2>
        <h3 id="adproject"><router-link to="/schedule/adproject">广告项目</router-link></h3>
        <div class="img_box">
          <p style="text-align: justify">
            <!-- <strong>媒体类型介绍</strong><br /> -->
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;业务员根据自己线下了解，接触新客户，发展成功后，上报客户信息（上传客户营业执照、联系人名片），流程为
            部门总监
            审批（非部门经理，多个部门有一个部门总监），确认是否为新客户，如果为新客户，则归属给此业务员。围绕此客户相关的服务日志要填写上报，副总可能会看业务员如何围绕此客户开展业务的。<br />并非所有的新客户都走审批流程，政务与记者站等新客户不需要走流程。<br />
          </p>
        </div>
        <h3 id="fold"><router-link to="/schedule/fold">广告叠次</router-link></h3>
        <h3 id="foldareaver"><router-link to="/schedule/foldareaver">叠次版本</router-link></h3>
        <h3 id="pagetype"><router-link to="/schedule/pagetype">版本类别</router-link></h3>
        <h3 id="adspec"><router-link to="/schedule/adspec">广告类型</router-link></h3>
      </div>
      <!-- 广告管理 -->
      <div>
        <h2>任务管理</h2>
        <div class="img_box">
          <div>
            <p style="text-align: justify">
              <strong>广告参数设置</strong><br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;设置不同媒体相关的广告参数，报纸媒体参考原系统的设置（基础参数设置（媒体类型、媒体、媒体刊期、职务）、广告参数设置（行业、来源、色彩、星期、打包类型）、财务参数设置（经营主体、付款方式、付款时间、开票项目））等。
              新媒体待与南方+技术公司调研确认是否通过接口方式获取。
            </p>
            <img src="@/assets/images/helpimg/adproject.png" />
            <h4>业务图</h4>
          </div>
        </div>
        <h3 id="adcolor"><router-link to="/task/pending">我的待办</router-link></h3>
        <h3 id="started"><router-link to="/task/started">我的发起</router-link></h3>
        <h3 id="completed"><router-link to="/task/completed">我的已办</router-link></h3>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, nextTick, ref } from 'vue'
import { useRouter, useRoute, LocationQueryValue, LocationQuery } from 'vue-router'
import { Document, Menu as IconMenu, Location, Setting } from '@element-plus/icons-vue'
const router = useRouter()
const route = useRoute()
defineOptions({
  name: 'Help'
})
const dingwei = ref('')
onMounted(() => {
  const query: LocationQuery = route.query
  const namehelp = (query.name as LocationQueryValue) ?? ''
  dingwei.value = namehelp
  console.log(namehelp)
  nextTick(function () {
    document.getElementById(`${namehelp}`)?.scrollIntoView()
  })
})
const handleOpen = (key: string) => {
  console.log(key)
  document.getElementById(`${key}`)?.scrollIntoView()
}
</script>

<style scoped lang="scss">
/* 表格边框 */
.ne-table {
  border-collapse: collapse;
  margin: 0 auto;
  width: 80%;
}

.ne-table .ne-tr,
.ne-table .ne-td {
  border: 1px black solid !important;
  padding: 0px 30px;
}
h1 {
  text-align: center;
}
h2 {
  color: #000;
  cursor: pointer;
  margin-left: 10%;
  font-weight: bold;
}
h3 {
  color: #409eff;
  cursor: pointer;
  margin-left: 10%;
  font-weight: 600;
}
h4 {
  text-align: center;
}
.navbox {
  width: 200px;
  position: fixed;
  z-index: 10000;
  background: white;
  border-right: 1px solid var(--el-menu-border-color);
  top: 0px;
  left: 0px;
  height: 100%;
  overflow-y: auto;
  padding-bottom: 60px;
}
.img_box {
  margin: 0 auto;
  width: 80%;
  img {
    width: 80%;
    margin-left: 10%;
  }
  p {
    line-height: 40px;
  }
}
</style>
