<!--
 * @Author: songly
 * @Date: 2023-08-30 16:08:23
 * @LastEditTime: 2023-10-23 15:23:11
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
            <span>基础信息</span>
          </template>
          <el-menu-item index="mediatype" @click="handleOpen('mediatype')">媒体类型</el-menu-item>
          <el-menu-item index="media" @click="handleOpen('media')">媒体管理</el-menu-item>
          <el-menu-item index="pagesize" @click="handleOpen('pagesize')">报纸版心</el-menu-item>
          <el-menu-item index="duties" @click="handleOpen('duties')">职位设置</el-menu-item>
          <el-menu-item index="mediapublicnum" @click="handleOpen('mediapublicnum')"
            >媒体刊期</el-menu-item
          >
        </el-sub-menu>
        <el-sub-menu index="2">
          <template #title>
            <el-icon><icon-menu /></el-icon>
            <span>广告属性</span>
          </template>
          <el-menu-item index="adcolor" @click="handleOpen('adcolor')">广告色彩</el-menu-item>
          <el-menu-item index="adtype" @click="handleOpen('adtype')">广告类别</el-menu-item>
          <el-menu-item index="adindustry" @click="handleOpen('adindustry')">广告行业</el-menu-item>
          <el-menu-item index="adfrom" @click="handleOpen('adfrom')">广告来源</el-menu-item>
          <el-menu-item index="pricepackagetype" @click="handleOpen('pricepackagetype')"
            >打包类型</el-menu-item
          >
          <el-menu-item index="weeksetting" @click="handleOpen('weeksetting')"
            >星期定义</el-menu-item
          >
        </el-sub-menu>
        <el-sub-menu index="3">
          <template #title>
            <el-icon><icon-menu /></el-icon>
            <span>财务参数</span>
          </template>
          <el-menu-item index="businessentity" @click="handleOpen('businessentity')"
            >经营主体</el-menu-item
          >
          <el-menu-item index="paymethod" @click="handleOpen('paymethod')">付款方式</el-menu-item>
          <el-menu-item index="paysort" @click="handleOpen('paysort')">付款时间</el-menu-item>
          <el-menu-item index="adprintitem" @click="handleOpen('adprintitem')"
            >开票项目</el-menu-item
          >
        </el-sub-menu>
        <el-sub-menu index="4">
          <template #title>
            <el-icon><icon-menu /></el-icon>
            <span>流程管理</span>
          </template>
          <el-menu-item index="createFlow" @click="handleOpen('createFlow')">创建流程</el-menu-item>
          <el-menu-item index="flowlist" @click="handleOpen('flowlist')">流程列表</el-menu-item>
          <el-menu-item index="FlowCondition" @click="handleOpen('FlowCondition')"
            >流程条件</el-menu-item
          >
        </el-sub-menu>
        <el-sub-menu index="5">
          <template #title>
            <el-icon><icon-menu /></el-icon>
            <span>组织架构</span>
          </template>
          <el-menu-item index="dept" @click="handleOpen('dept')">部门管理</el-menu-item>
          <el-menu-item index="emp" @click="handleOpen('emp')">人员管理</el-menu-item>
          <el-menu-item index="role" @click="handleOpen('role')">角色管理</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="6">
          <template #title>
            <el-icon><icon-menu /></el-icon>
            <span>系统配置</span>
          </template>
          <el-menu-item index="menu" @click="handleOpen('menu')">菜单管理</el-menu-item>
          <el-menu-item index="log" @click="handleOpen('log')">登录日志</el-menu-item>
          <el-menu-item index="sysoperatelog" @click="handleOpen('sysoperatelog')"
            >操作日志</el-menu-item
          >
        </el-sub-menu>
        <!-- <el-menu-item index="4">
          <el-icon><setting /></el-icon>
          <span>Navigator Four</span>
        </el-menu-item> -->
      </el-menu>
    </div>
    <div>
      <h1 id="mubanyemian">系统使用帮助手册</h1>
      <!-- 系统介绍 -->
      <div>
        <h2>系统介绍</h2>
        <h3 id="index"><router-link to="/index">模块划分</router-link></h3>
        <table class="ne-table" width="750">
          <tbody>
            <tr class="ne-tr firstRow" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                  background-color: rgb(242, 242, 242);
                "
              >
                <p>序号<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="1"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                  background-color: rgb(242, 242, 242);
                "
              >
                <p>模块名称<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                  background-color: rgb(242, 242, 242);
                "
              >
                <p>功能<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                  background-color: rgb(242, 242, 242);
                "
              >
                <p>功能描述<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                  background-color: rgb(242, 242, 242);
                "
              >
                <p>对应需求<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>1.<br /></p>
              </td>
              <td
                class="ne-td"
                rowspan="9"
                data-col="1"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>基础管理<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>配置部门<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>部门增删改查<br /></p>
              </td>
              <td
                class="ne-td"
                rowspan="9"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>系统管理员<br />用例<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>2.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>配置权限<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>权限增删改查<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>3.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>配置角色<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>角色增删改查<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>4.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>用户管理<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>用户增删改查<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>5.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>配置审批流程<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>审批流程增删改查<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>6.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>配置媒体<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>媒体类型、媒体、媒体刊期<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>7.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>配置广告参数<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>行业、来源等广告参数<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>8.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>配置广告价格<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>纸媒、南方+广告价格<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>9.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>配置财务参数<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>经营主体、付款方式等财务参数<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>10.<br /></p>
              </td>
              <td
                class="ne-td"
                rowspan="14"
                data-col="1"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>广告业务<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>项目信息<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>项目增删改查询<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>操作员用例<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>11.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>查看广告位<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>查看纸媒、南方+广告位<br /></p>
              </td>
              <td
                class="ne-td"
                rowspan="7"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>业务员用例<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>12.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>广告预约<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>预约广告位<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>13.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>快速预定<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>广告快速上刊或上线<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>14.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>认刊合同<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>
                  合同申请、合同折扣申请、合同变动（加刊、改刊、停刊）、补刊申请、合同查询<br />
                </p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>15.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>客户备案<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>新广告客户申请<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>16.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>客户信息<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>客户列表、客户详情、客户查询<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>17.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>广告成本<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>认刊合同范围内支出的费用<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>18.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>出版计划<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>配置版面计划<br /></p>
              </td>
              <td
                class="ne-td"
                rowspan="4"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>版面管理员用例<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>19.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>排期安排<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>安排广告到版面<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>20.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>分类广告排版<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>分类广告排版<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>21.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>媒体参数<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>配置媒体参数<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>22.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>南方+广告发布<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>在南方+发布广告<br /></p>
              </td>
              <td
                class="ne-td"
                rowspan="2"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>新媒体广告操作员<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>23.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>两微广告发布<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>在微博、微信发布广告<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>24.<br /></p>
              </td>
              <td
                class="ne-td"
                rowspan="2"
                data-col="1"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>订单处理<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>订单录入<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>录入订单数据<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>操作员用例<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>25.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>订单跟踪<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>查看订单执行情况<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>操作员、业务员用例<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>26.<br /></p>
              </td>
              <td
                class="ne-td"
                rowspan="8"
                data-col="1"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>审批业务<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>认刊合同审批<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>认刊合同审批流程<br /></p>
              </td>
              <td
                class="ne-td"
                rowspan="8"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>
                  公司领导、财务主管、业务主管、广告数据管理员、操作操作员、广告审核员用例<br />
                </p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>27.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>广告内容审批<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>广告内容审批流程<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>28.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>快速预定审批<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>广告紧复急上刊/上线审批流程<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>29.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>合同变动审批<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>合同（加刊、改刊、停刊）审批流程<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>30.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>客户备案审批<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>客户备案审批流程<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>31.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>预开发票审批<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>预开发票审批流程<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>32.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>合同折扣审批<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>合同折扣审批流程<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>33.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>加急上版审批<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>快递上版审批<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>34.<br /></p>
              </td>
              <td
                class="ne-td"
                rowspan="8"
                data-col="1"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>发票处理<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>预开发票<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>预开发票申请、预开发票查询<br /></p>
              </td>
              <td
                class="ne-td"
                rowspan="2"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>业务员<br />用例<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>35.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>申请发票核销<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>业务员申请核销预开的发票<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>36.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>发票核销<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>核销预开的发票<br /></p>
              </td>
              <td
                class="ne-td"
                rowspan="2"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>发票核销员<br />用例<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>37.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>导入银行流水<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>银行流水导入到系统<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>38.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>开具发票<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>开具电子发票<br /></p>
              </td>
              <td
                class="ne-td"
                rowspan="3"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>开票员用例<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>39.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>发票冲红<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>作废已开具的电子发票<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>40.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>退票重开<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>作废已开具的电子发票并重开<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>41.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>发票查询<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>查询已开具的发票<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>开票员、发票核销员、复核员、财务主管用例<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>42.<br /></p>
              </td>
              <td
                class="ne-td"
                rowspan="5"
                data-col="1"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>佣金处理<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>成本复核<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>复核业务员录入的广告成本<br /></p>
              </td>
              <td
                class="ne-td"
                rowspan="2"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>佣金核算员用例<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>43.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>佣金核算<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>核算业务员应提取的佣金<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>44.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>佣金查询<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>查询佣金信息<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>佣金核算员、佣金复核员用例<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>45.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>佣金复核<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>复核业务员应提取的佣金<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>佣金复核员用例<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>46.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>佣金比例<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>设置佣金比例<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>佣金核算员用例<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>47.<br /></p>
              </td>
              <td
                class="ne-td"
                rowspan="5"
                data-col="1"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>统计分析<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>经营数据汇总分析<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>经营数据比对分析<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>公司领导<br />用例<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>48.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>各类汇总报表<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>广告应收汇总、欠款汇总等报表<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>广告数据管理员用例<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>49.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>客户欠款汇总与分析<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>对欠款的客户进行汇总与分析<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>财务主管用例<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>50.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>订单汇总统计<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>对订单进行汇总统计<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>客服主管用例<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>51.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>期间结账<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>锁定期间的订单数据<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>广告数据管理员用例<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>52.<br /></p>
              </td>
              <td
                class="ne-td"
                rowspan="5"
                data-col="1"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>业务员<br />工作台<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>业绩查询<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>业绩汇总、欠款汇总、佣金查询<br /></p>
              </td>
              <td
                class="ne-td"
                rowspan="5"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>业务员用例<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>53.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>客户跟单备忘录<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>记录业务员与客户洽谈的各种事项<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>54.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>工作日志<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>填写日报、周报、月报等内容<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>55.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>工作报告<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>填写工作报告<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>56.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>查看任务<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>查看分配的任务<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>57.<br /></p>
              </td>
              <td
                class="ne-td"
                rowspan="4"
                data-col="1"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>业务主管<br />工作台<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>部门业绩汇总<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>部门业绩汇总报表<br /></p>
              </td>
              <td
                class="ne-td"
                rowspan="4"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>业务主管<br />用例<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>58.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>分配欠款订单<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>分配欠款订单到业务员<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>59.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>任务进度查询<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>查询业务员工作进度<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>60.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>任务管理<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>分配任务、查询任务<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>61.<br /></p>
              </td>
              <td
                class="ne-td"
                rowspan="3"
                data-col="1"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>公司领导<br />工作台<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>查看客户跟单备忘录<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>查询业务员记录的跟单备忘录<br /></p>
              </td>
              <td
                class="ne-td"
                rowspan="3"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>公司领导<br />用例<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>62.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>查看项目进度<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>查询项目完成情况<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>63.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>设置大客户<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>设置普通客户为大客户<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>64.<br /></p>
              </td>
              <td
                class="ne-td"
                rowspan="6"
                data-col="1"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>系统集成<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>小南系统<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>统一登录<br /></p>
              </td>
              <td
                class="ne-td"
                rowspan="6"
                data-col="4"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>系统集成<br />需求<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>65.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>OA系统<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>调用接口将审批流程信息发送给OA<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>66.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>短信验证<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>接入短信验证<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>67.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>税务局电子发票系统<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>对接税务局电子发票系统<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>68.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>南方+<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>对接南方+系统<br /></p>
              </td>
            </tr>
            <tr class="ne-tr" style="box-sizing: border-box; height: 33px">
              <td
                class="ne-td"
                data-col="0"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>69.<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="2"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>集团AD域系统<br /></p>
              </td>
              <td
                class="ne-td"
                data-col="3"
                style="
                  box-sizing: border-box;
                  position: relative;
                  border: 1px solid var(--lakex-editor-border-primary);
                  vertical-align: top;
                "
              >
                <p>对接集团AD域系统<br /></p>
              </td>
            </tr>
          </tbody>
        </table>
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
          <img src="@/assets/images/helpimg/zongbuju0.png" />
          <img src="@/assets/images/helpimg/zongbuju.png" />
        </div>
      </div>
      <!-- 基础信息 -->
      <div>
        <h2>基础信息</h2>
        <h3 id="mediatype"><router-link to="/basic/mediatype">媒体类型</router-link></h3>
        <div class="img_box">
          <p style="text-align: justify">
            <!-- <strong>媒体类型介绍</strong><br /> -->
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;业务员根据自己线下了解，接触新客户，发展成功后，上报客户信息（上传客户营业执照、联系人名片），流程为
            部门总监
            审批（非部门经理，多个部门有一个部门总监），确认是否为新客户，如果为新客户，则归属给此业务员。围绕此客户相关的服务日志要填写上报，副总可能会看业务员如何围绕此客户开展业务的。<br />并非所有的新客户都走审批流程，政务与记者站等新客户不需要走流程。<br />
          </p>
          <!-- <img src="@/assets/images/helpimg/zongbuju0.png" />
          <h4>业务图</h4> -->
        </div>
        <h3 id="media"><router-link to="/basic/media">媒体管理</router-link></h3>
        <h3 id="pagesize"><router-link to="/basic/pagesize">报纸版心</router-link></h3>
        <h3 id="duties"><router-link to="/basic/duties">职务设置</router-link></h3>
        <h3 id="mediapublicnum"><router-link to="/basic/mediapublicnum">媒体刊期</router-link></h3>
      </div>
      <!-- 广告管理 -->
      <div>
        <h2>广告管理</h2>
        <div class="img_box">
          <div>
            <p style="text-align: justify">
              <strong>广告参数设置</strong><br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;设置不同媒体相关的广告参数，报纸媒体参考原系统的设置（基础参数设置（媒体类型、媒体、媒体刊期、职务）、广告参数设置（行业、来源、色彩、星期、打包类型）、财务参数设置（经营主体、付款方式、付款时间、开票项目））等。
              新媒体待与南方+技术公司调研确认是否通过接口方式获取。
            </p>
          </div>
        </div>
        <h3 id="adcolor"><router-link to="/attribute/adcolor">广告色彩</router-link></h3>
        <h3 id="adtype"><router-link to="/attribute/adtype">广告类别</router-link></h3>
        <h3 id="adindustry"><router-link to="/attribute/adindustry">广告行业</router-link></h3>
        <h3 id="adfrom"><router-link to="/attribute/adfrom">广告来源</router-link></h3>
        <h3 id="pricepackagetype">
          <router-link to="/attribute/pricepackagetype">打包类型</router-link>
        </h3>
        <h3 id="weeksetting"><router-link to="/attribute/weeksetting">星期定义</router-link></h3>
      </div>
      <!-- 财务参数 -->
      <div>
        <h2>财务参数</h2>
        <h3 id="businessentity">
          <router-link to="/finance/businessentity">经营主体</router-link>
        </h3>
        <h3 id="paymethod"><router-link to="/finance/paymethod">付款方式</router-link></h3>
        <h3 id="paysort"><router-link to="/finance/paysort">付款时间</router-link></h3>
        <h3 id="adprintitem"><router-link to="/finance/adprintitem">开票项目</router-link></h3>
      </div>
      <!-- 流程管理 -->
      <div>
        <h2>流程管理</h2>
        <h3 id="createFlow">
          <router-link to="/flow/createFlow">创建流程</router-link>
        </h3>
        <div class="img_box">
          <div>
            <p style="text-align: justify">
              <strong
                >系统支持的审批业务：广告订单审批流程、广告内容审批流程、广告订单变动审批流程、预开发票审批流程、客户备案审批流程、广告折扣审批流程。</strong
              ><br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1、不同的审批业务建立不同的审批流程，审批人员可以按角色、按部门、按人员设置。<br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2、删除审批流程需要满足删除条件（审批流程在系统中没有任何审批记录的情况下允许删除）。<br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3、禁用的审批流程无法进行审批操作。
            </p>
          </div>
          <div>
            <p style="text-align: justify">
              <strong>新增业务流程</strong><br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;系统管理员在“审批流程”页面可以添加审批流程。在“添加审批流程”页面，首先选择审批业务（如广告订单审批流程等），然后录入审批流程名称，配置审批步骤，配置完成提交即可。
            </p>
            <img src="@/assets/images/helpimg/createFlow.png" />
            <h4>添加审核流程业务流程图</h4>
          </div>
          <div>
            <p style="text-align: justify">
              <strong>编辑业务流程</strong><br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;系统管理员登录系统，进入审批流程页面，输入查询条件查询流程，在查询结果列表，选择需要编辑的审批流程，在审批流程配置页面配置流程，输入完成提交即可。
            </p>
            <img src="@/assets/images/helpimg/createFlow1.png" />
            <h4>编辑审批流程业务流程图</h4>
          </div>
          <div>
            <p style="text-align: justify">
              <strong>删除业务流程</strong><br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;系统管理员登录系统，进入审批流程页面，输入查询条件查询审批流程，在查询结果列表选择要删除或禁用的审批流程。执行删除审批流程或禁用审批流程操作，系统会给出确认提示，若是删除操作，系统会判断该审批流程是否满足删除条件，如果不满足删除条件，则禁止删除审批流程，在这种情况下，可以禁用审批流程。
            </p>
            <img src="@/assets/images/helpimg/createFlow2.png" />
            <h4>删除或禁用审批流程业务流程图</h4>
          </div>
        </div>
        <h3 id="flowlist"><router-link to="/flow/flowlist">流程列表</router-link></h3>
        <h3 id="FlowCondition"><router-link to="/flow/FlowCondition">流程条件</router-link></h3>
      </div>
      <!-- 组织架构 -->
      <div>
        <h2>组织架构</h2>
        <h3 id="dept">
          <router-link to="/users/dept">部门管理</router-link>
        </h3>
        <div class="img_box">
          <div>
            <p style="text-align: justify">
              <strong>业务规则--管理部门组织结构，包含增删改调整等。</strong><br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1、同级部门下的部门名称不能重复<br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2、存在下级的部门不能删除<br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3、部门下存在人员的不能删除<br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4、部门存在业务数据的不能删除
            </p>
          </div>
          <div>
            <p style="text-align: justify">
              <strong>新增业务流程</strong><br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;系统管理员登录系统，在部门管理页面选择添加部门，在添加部门页面录入部门基础信息、若不是一级部门需要选择隶属上级部门，最后提交部门信息。
            </p>
            <img src="@/assets/images/helpimg/createFlow.png" />
            <h4>添加部门业务流程图</h4>
          </div>
          <div>
            <p style="text-align: justify">
              <strong>编辑业务流程</strong><br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;系统管理员登录系统，进入部门管理页面，输入查询条件查询部门，在查询结果列表，选择需要修改的部门，在部门修改页面编辑部门基础信息，输入完成提交即可。
            </p>
            <img src="@/assets/images/helpimg/createFlow1.png" />
            <h4>编辑部门业务流程图</h4>
          </div>
          <div>
            <p style="text-align: justify">
              <strong>删除业务流程</strong><br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;系统管理员登录系统，进入部门管理页面，输入查询条件查询部门，在查询结果列表选择要删除的部门。执行删除部门操作，系统会给出确认提示，操作者确认删除后，系统会判断该部门是否满足删除条件，如果不满足删除条件，则禁止删除部门，并输出不能删除部门的原因。
            </p>
            <img src="@/assets/images/helpimg/createFlow2.png" />
            <h4>删除部门业务流程图</h4>
          </div>
        </div>
        <h3 id="emp"><router-link to="/users/emp">人员管理</router-link></h3>
        <div class="img_box">
          <div>
            <p style="text-align: justify">
              <strong>业务规则--系统所有人员的增删改管理。</strong><br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1、一个用户可以拥有多个角色。<br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2、一个用户只能隶属一个部门。可设置其兼职部门。<br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3、新用户与已有用户存在重名，若和重名用户不隶属同一个部门，系统会给出提醒，但允许提交用户信息；若隶属同一个部门，需要在用户名称上加以区分，否则系统不允许提交用户信息。<br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4、用户名称不是登录名称，需要额外输入登录名称。<br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5、可设置人员是否为管理员，普通人员，部门领导，业务员等属性。<br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6、人员禁用或删除要判断此人员是否有业务关联，否则不允许。<br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7、人员部门调整可以在此功能中调整，但仅限于系统初使化人员配置时，如果系统已运行一段时间后的部门调整则需要通过另外人人员部门调整功能调整，因为牵扯到相关业务的调整。<br />
            </p>
          </div>
          <div>
            <p style="text-align: justify">
              <strong>新增业务流程</strong><br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;人员添加有两种情况，一种是系统内独立添加，一种是通过接口从域或第三方系统获取。系统内独立添加流程为，系统管理员登录系统，在用户管理页面选择添加用户，在添加用户页面录入用户基础信息、选择隶属部门、选择职务、选择用户角色（可多选），最后提交用户信息。<br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;从第三方接口获取，系统管理员登录系统，在用户管理页面选择添加用户，在添加用户页面左侧为域信息，选则要添加的域用户，右侧选择本地系统部门，占添加完成人员信息的导入，最后提交用户信息。
            </p>
            <img src="@/assets/images/helpimg/dept.png" />
            <h4>添加用户业务流程图</h4>
            <img src="@/assets/images/helpimg/dept1.png" />
            <h4>从域中添加用户业务流程图</h4>
          </div>
          <div>
            <p style="text-align: justify">
              <strong>编辑用户业务流程</strong><br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;系统管理员登录系统，进入用户管理页面，输入查询条件查询用户，在查询结果列表选择要修改的用户，进入修改用户页面，完成用户信息的修改。
            </p>
            <img src="@/assets/images/helpimg/dept2.png" />
            <h4>修改用户业务流程图</h4>
          </div>
          <div>
            <p style="text-align: justify">
              <strong>删除业务流程</strong><br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;系统管理员登录系统，进入用户管理页面，输入查询条件查询用户，在查询结果列表选择要删除或禁用的用户。执行删除用户或禁用用户操作，系统会给出确认提示，若是删除操作，系统会判断该用户是否满足删除条件，如果不满足删除条件，则禁止删除用户，在这种情况下，可以禁用用户。
            </p>
            <img src="@/assets/images/helpimg/dept3.png" />
            <h4>删除或禁用用户业务流程图</h4>
          </div>
        </div>
        <h3 id="role"><router-link to="/users/role">角色管理</router-link></h3>
        <div class="img_box">
          <div>
            <p style="text-align: justify">
              <strong
                >业务规则--系统的重要设置之一，按照实际业务分组，每组业务即为一个角色，可以设置此角色允许使用菜单功能，允许查询的数据范围，允许修改的数据范围，及功能中有些操作按纽是否允许操作等。</strong
              ><br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1、角色有内置的管理员角色，不允许删除，但允许编辑其角色功能<br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2、通过角色可以直接设置相关人员
            </p>
          </div>
          <div>
            <p style="text-align: justify">
              <strong>新增业务流程</strong><br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;系统管理员登录系统，进入角色管理页面，选择添加角色操作，在角色添加页面输入角色名称和备注，关联一个或多个权限，输入完成提交即可。
            </p>
            <img src="@/assets/images/helpimg/role.png" />
            <h4>添加角色业务流程图</h4>
          </div>
          <div>
            <p style="text-align: justify">
              <strong>编辑业务流程</strong><br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;系统管理员登录系统，进入角色管理页面，输入查询条件查询角色，在查询结果列表，选择需要修改的角色，在角色修改页面编辑角色名称、备注，关联一个或多个权限，输入完成提交即可。
            </p>
            <img src="@/assets/images/helpimg/role1.png" />
            <h4>修改角色业务流程图</h4>
          </div>
          <div>
            <p style="text-align: justify">
              <strong>删除业务流程</strong><br />
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;系统管理员登录系统，进入角色管理页面，输入查询条件查询角色，在查询结果列表选择要删除或禁用的角色。执行删除角色或禁用角色操作，系统会给出确认提示，若是删除操作，系统会判断该角色是否满足删除条件，如果不满足删除条件，则禁止删除角色，在这种情况下，可以禁用角色。
            </p>
            <img src="@/assets/images/helpimg/role2.png" />
            <h4>删除或禁用角色业务流程图</h4>
          </div>
        </div>
      </div>
      <!-- 系统设置 -->
      <div>
        <h2>系统设置</h2>
        <h3 id="menu"><router-link to="/system/menu">菜单管理</router-link></h3>
        <h3 id="log"><router-link to="/system/log">登录日志</router-link></h3>
        <h3 id="sysoperatelog">
          <router-link to="/system/sysoperatelog">系统操作日志</router-link>
        </h3>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
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
