<template>
  <div>
    <el-dialog
      v-model="visible"
      :title="
        (mode === Mode.Add ? '新增' : '修改') + (dto.ibooktype === 1 ? '广告预约' : '快速预约')
      "
      align-center
      :close-on-click-modal="false"
      :width="fenbianlv() > 1440 ? '1400' : '94%'"
    >
      <el-form
        ref="form"
        :model="dto"
        label-width="100px"
        style="margin-top: -30px; padding: 0 40px 0px"
      >
        <el-divider content-position="left">基础信息</el-divider>
        <el-row :gutter="20" style="flex-wrap: wrap">
          <el-col :span="8">
            <el-form-item label="预定编号" prop="sordernum">
              {{ dto.sordernum || '-' }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="申请日期" prop="createtime">
              <el-date-picker
                v-if="mode !== Mode.View"
                v-model="dto.createtime"
                type="date"
                readonly
                style="width: 100%"
              />
              <span v-else>{{ dto.createtime.substring(0, 10) }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="订单类别" prop="iorderkind">
              <el-select
                v-if="mode !== Mode.View"
                v-model="dto.iorderkind"
                placeholder="订单类别"
                style="width: 100%"
              >
                <el-option label="本部广告" :value="0"></el-option>
                <el-option label="记者站广告" :value="1"></el-option>
                <el-option label="编辑广告" :value="2"></el-option>
                <el-option label="上门广告" :value="3"></el-option>
              </el-select>
              <div v-else>
                <span v-if="dto.iorderkind === 0">本部广告</span>
                <span v-else-if="dto.iorderkind === 1">记者站广告</span>
                <span v-else-if="dto.iorderkind === 2">编辑广告</span>
                <span v-else-if="dto.iorderkind === 3">上门广告</span>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="业务员" prop="employname">
              <el-input v-if="mode !== Mode.View" v-model="dto.employname" readonly />
              <span v-else>{{ dto.employname }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="业务员归属" prop="employtype">
              <el-select
                v-if="mode !== Mode.View"
                v-model="dto.employtype"
                style="width: 100%"
                placeholder="业务员归属"
                @change="(value: number) => {
                  if (value === 1) { // 选中代理公司,清空内容生产方业务员id和name
                    dto.agentemployid = userStore.user?.userid.toString()
                    dto.agentemployname = userStore.user?.username.toString()
                  } else if (value === 2) { // 选中内容生产方,清空代理公司业务员id和name
                    dto.agencyemployid = userStore.user?.userid.toString()
                    dto.agencyemployname = userStore.user?.username.toString()
                  }
                  formOrderItemList.forEach((i) => i.orderitembelong![0].employtype = value)
                }"
              >
                <el-option label="直接客户" :value="0"></el-option>
                <el-option label="代理公司" :value="1"></el-option>
                <el-option label="内容生产方" :value="2"></el-option>
              </el-select>
              <div v-else>
                <span v-if="dto.employtype === 0">直接客户</span>
                <span v-else-if="dto.employtype === 1">代理公司</span>
                <span v-else-if="dto.employtype === 2">内容生产方</span>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="经营主体" prop="businessentityname">
              <el-select
                v-if="mode !== Mode.View"
                v-model="dto.businessentityid"
                placeholder="经营主体"
                style="width: 100%"
                @change="(id: string) => {
                  dto.businessentityname = businessentityCombo.find((i) => i.id === id).sname
                }"
              >
                <el-option
                  v-for="i in businessentityCombo"
                  :key="i.id"
                  :label="i.sname"
                  :value="i.id"
                />
              </el-select>
              <span v-else>{{ dto.businessentityname }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="直接客户" prop="i2">
              <el-input
                v-if="mode !== Mode.View"
                v-model="dto.customername"
                placeholder="请选择直接客户"
                style="width: 450px"
                readonly
              >
                <template #append>
                  <el-button
                    icon="Search"
                    circletitle="选择客户"
                    @click="(_: any) => {
                      customerSelectionDialog = true
                      customerSelectionType = 0
                    }"
                  />
                </template>
                <template #suffix>
                  <el-button
                    link
                    icon="CircleClose"
                    @click="(_: any) => {
                      dto.customerid = ''
                      dto.customername = ''
                    }"
                  ></el-button>
                </template>
              </el-input>
              <span v-else>{{ dto.customername }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="代理公司" prop="i3">
              <el-input
                v-if="mode !== Mode.View"
                v-model="dto.agencyname"
                placeholder="请选择代理公司"
                style="width: 450px"
                readonly
              >
                <template #append>
                  <el-button
                    icon="Search"
                    circle
                    title="代理公司"
                    @click="(_: any) => {
                      customerSelectionDialog = true
                      customerSelectionType = 1
                    }"
                  />
                </template>
                <template #suffix>
                  <el-button
                    link
                    icon="CircleClose"
                    @click="(_: any) => {
                      dto.agencytid = ''
                      dto.agencyname = ''
                      dto.agencyemployid = ''
                      dto.agencyemployname = ''
                    }"
                  ></el-button>
                </template>
              </el-input>
              <span v-else>{{ dto.agencyname }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="内容生产方" prop="i1">
              <el-input
                v-if="mode !== Mode.View"
                v-model="dto.agentname"
                placeholder="请选择内容生产方"
                style="width: 450px"
                readonly
              >
                <template #append>
                  <el-button
                    icon="Search"
                    circle
                    title="内容生产方"
                    @click="(_: any) => {
                      customerSelectionDialog = true
                      customerSelectionType = 2
                    }"
                  />
                </template>
                <template #suffix>
                  <el-button
                    link
                    icon="CircleClose"
                    @click="(_: any) => {
                      dto.agentid = ''
                      dto.agentname = ''
                      dto.agentemployid = ''
                      dto.agentemployname = ''
                    }"
                  ></el-button>
                </template>
              </el-input>
              <span v-else>{{ dto.agentname }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="广告类型" prop="adtypeid">
              <el-select
                v-if="mode !== Mode.View"
                v-model="dto.adtypeid"
                placeholder="广告类型"
                style="width: 100%"
                @change="(id: string) => {
                  dto.adtypename = adtypeCombo.find(i => i.id == id)?.sname || ''
                }"
              >
                <el-option
                  v-for="item in adtypeCombo"
                  :key="item.id"
                  :label="item.sname"
                  :value="item.id!"
                />
              </el-select>
              <span v-else>{{ dto.adtypename }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="行业名称"
              prop="adindustyid"
              :rules="[
                { validator: required, required: true, message: '请选择行业', trigger: 'change' }
              ]"
            >
              <HgZtreeSelect
                v-if="mode !== Mode.View"
                :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYINDUSTRY"
                :selectids="dto.adindustyid ? [dto.adindustyid] : []"
                style="width: 100%"
                @selectionztree="(val: TSelectZtree[]) => {
                  dto.adindustyid = val.map((item) => item.id).join(',')
                  dto.adindustryname = val.map((item) => item.name).join(',')
                }"
              ></HgZtreeSelect>
              <span v-else>{{ dto.adindustryname }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="广告标题"
              prop="sadtitle"
              :rules="[
                {
                  validator: required,
                  required: true,
                  message: '请输入广告标题',
                  trigger: 'change'
                },
                { max: 500, message: '不得超过500个字符', trigger: 'change' }
              ]"
            >
              <el-input
                v-if="mode !== Mode.View"
                v-model="dto.sadtitle"
                placeholder="请输入广告标题"
                style="width: 100%"
                @click="() => {}"
              />
              <span v-else>{{ dto.sadtitle }}</span>
            </el-form-item>
          </el-col>

          <el-col v-if="mode !== Mode.Add" :span="24">
            <el-form-item label="负责人意见" prop="sopinion">
              <el-input v-if="mode !== Mode.View" v-model="dto.sopinion" type="textarea" readonly />
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider content-position="left" style="color: red">
          订单明细-编辑 (根据预刊日期和广告媒体切换后续选项)
        </el-divider>
        <el-row :gutter="20" style="flex-wrap: wrap">
          <el-col :span="8">
            <el-form-item label="预刊日期" prop="i1">
              <!-- :disabled-date="(time: Date) => time.getTime() < Date.now()"   -->
              <el-date-picker
                v-if="mode !== Mode.View"
                v-model="dto.prestartdate"
                style="width: 100%"
                type="date"
                placeholder="选择日期"
                :clearable="false"
                :editable="false"
                @change="
                  (v) => {
                    dto.prestartdate = dayjs(v).format('YYYY-MM-DD')
                    clearPreOrderDTOItems(true)
                  }
                "
              />
              <span v-else>{{ dto.prestartdate }}</span>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="广告媒体" prop="extId">
              <el-tree-select
                ref="treeRef"
                v-model="dto.extId"
                disable
                :data="foldTreeNodes"
                filterable
                @change="(label?: string) => {
                  changeAdMedia(Mode.Add)
                }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="明细标题" prop="saditemtitle">
              <el-input
                v-if="mode !== Mode.View"
                v-model="dto.saditemtitle"
                style="width: 100%"
                placeholder="请输入广告明细标题"
                @click="() => {}"
              />
              <span v-else>{{ dto.saditemtitle }}</span>
            </el-form-item>
          </el-col>
          <el-col v-if="isChecked(['paper', 'app', 'website', 'multi'])" :span="8">
            <el-form-item :label="getLabelByField('foldareavername')" prop="foldareavername">
              <el-select
                v-if="mode !== Mode.View"
                v-model="dto.foldareaverid"
                clearable
                style="width: 100%"
                @change="(foldareaverid: string) => {
                  // formFoldPageList = !foldareaverid ? [] : Array.from(new Set(planList.filter((plan) => plan.foldareaverid === foldareaverid)))
                  getFormFoldPageList()
                  dto.foldareavername = !foldareaverid ? '' : formfoldareavers.find(i => i.id == foldareaverid).name
                }"
              >
                <el-option
                  v-for="i in formfoldareavers"
                  :key="i.id"
                  :label="i.name"
                  :value="i.id"
                />
              </el-select>
              <span v-else>{{ dto.foldareavername }}</span>
            </el-form-item>
          </el-col>
          <el-col v-if="isChecked(['paper'])" :span="8">
            <el-form-item :label="getLabelByField('foldpageplanname')" prop="foldpageplanname">
              <el-select
                v-if="mode !== Mode.View"
                v-model="dto.foldpageplanid"
                placeholder="版面"
                style="width: 100%"
                clearable
                @change="(foldpageplanid: string) => {
                  dto.foldpageplanname = !foldpageplanid ? undefined : formFoldPageList.find(i => i.id == foldpageplanid)?.pagenum?.toString() || ''
                }"
              >
                <el-option
                  v-for="item in formFoldPageList"
                  :key="item.id"
                  :label="item.pagenum"
                  :value="item.id"
                />
              </el-select>
              <span v-else>{{ dto.foldpageplanname }}</span>
            </el-form-item>
          </el-col>
          <el-col v-if="isChecked(['paper', 'app', 'website', 'wei'])" :span="8">
            <el-form-item :label="getLabelByField('addisplayformname')" prop="addisplayformname">
              <el-select
                v-if="mode !== Mode.View"
                v-model="dto.addisplayformid"
                :placeholder="`请选择` + getLabelByField('addisplayformname')"
                clearable
                style="width: 100%"
                @change="(addisplayformid: string) => {
                  dto.addisplayformname = !addisplayformid ? '' : formDisplayFormList.find(i => i.id == addisplayformid)?.sname || ''
                }"
              >
                <el-option
                  v-for="item in formDisplayFormList"
                  :key="item.id"
                  :label="item.sname"
                  :value="item.id"
                />
              </el-select>
              <span v-else>{{ dto.addisplayformname }}</span>
            </el-form-item>
          </el-col>
          <el-col v-if="isChecked(['paper', 'app', 'website', 'wei'])" :span="8">
            <el-form-item :label="getLabelByField('pagesortname')" prop="pagesortname">
              <el-select
                v-if="mode !== Mode.View"
                v-model="dto.pagesortid"
                :placeholder="`请选择` + getLabelByField('pagesortname')"
                clearable
                style="width: 100%"
                @change="(pagesortid: string) => {
                  dto.pagesortname = !pagesortid ? '' : formPageSortList.find(i => i.id == pagesortid)?.sname || ''
                }"
              >
                <el-option
                  v-for="item in formPageSortList"
                  :key="item.id"
                  :label="item.sname"
                  :value="item.id"
                />
              </el-select>
              <span v-else>{{ dto.pagesortname }}</span>
            </el-form-item>
          </el-col>
          <el-col v-if="isChecked(['paper', 'app', 'website', 'wei'])" :span="8">
            <el-form-item :label="getLabelByField('adcolorname')" prop="adcolorname">
              <el-select
                v-if="mode !== Mode.View"
                v-model="dto.adcolorid"
                :placeholder="`请选择` + getLabelByField('adcolorname')"
                clearable
                style="width: 100%"
                @change="(adcolorid: string) => {
                  dto.adcolorname = !adcolorid ? '' : formColorList.find(i => i.id == adcolorid)?.sname || ''
                }"
              >
                <el-option
                  v-for="item in formColorList"
                  :key="item.id"
                  :label="item.sname"
                  :value="item.id"
                />
              </el-select>
              <span v-else>{{ dto.adcolorname }}</span>
            </el-form-item>
          </el-col>
          <el-col v-if="isChecked(['paper', 'app', 'website', 'wei'])" :span="8">
            <el-form-item :label="getLabelByField('adspecname')" prop="adspecname">
              <el-select
                v-if="mode !== Mode.View"
                v-model="dto.adspecid"
                :placeholder="`请选择` + getLabelByField('adspecname')"
                clearable
                style="width: 100%"
                @change="(adspecid: string) => {
                  dto.adspecname = !adspecid ? '' : formSpecList.find(i => i.id == adspecid)?.sname || ''
                }"
              >
                <el-option
                  v-for="item in formSpecList"
                  :key="item.id?.toString()"
                  :label="item.sname"
                  :value="item.id?.toString()"
                />
              </el-select>
              <span v-else>{{ dto.adspecname }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row justify="end">
          <div style="display: flex; justify-content: flex-end">
            <el-button
              v-if="currentModifiedOrderItemId && mode !== Mode.View"
              type="success"
              @click="(_: any) => {
                if (!canChecked) {
                  ElMessage({ message: '请重新选择广告媒体', type: 'warning' })
                  return
                }
                if (!dto.saditemtitle) {
                  ElMessage({ message: '请输入广告明细标题', type: 'warning' })
                  return
                }
                if (dto.saditemtitle.length > 500) {
                  ElMessage({ message: '广告明细标题不得超过500个字符', type: 'warning' })
                  return
                }
                formOrderItemList = formOrderItemList.map(item => {
                  return item.id === currentModifiedOrderItemId ? parsePreOrderToTworderitem(dto, Mode.Edit, item) : item
                })
                // 清空明细表单
                clearPreOrderDTOItems(true)
                dto.extId = ''
                currentModifiedOrderItemId = undefined
              }"
            >
              更新明细数据
            </el-button>
            <el-button
              v-if="!currentModifiedOrderItemId && mode !== Mode.View"
              type="primary"
              icon="Plus"
              @click="
                () => {
                  if (!canChecked) {
                    ElMessage({ message: '请重新选择广告媒体', type: 'warning' })
                    return
                  }
                  if (!dto.saditemtitle) {
                    ElMessage({ message: '请输入广告明细标题', type: 'warning' })
                    return
                  }
                  if (dto.saditemtitle.length > 500) {
                    ElMessage({ message: '广告明细标题不得超过500个字符', type: 'warning' })
                    return
                  }
                  const order = initPreOrder(props.bookType)
                  Object.assign(order, dto)
                  if (dto.mediatypekey === 'paper') {
                    if (dto.foldareaverid === '') {
                      ElMessage.error('叠次版本不能为空')
                      return false
                    }
                    if (dto.pagesortid === '') {
                      ElMessage.error('版面类型不能为空')
                      return false
                    }
                    if (dto.addisplayformid === '') {
                      ElMessage.error('广告形式不能为空')
                      return false
                    }
                    if (dto.adspecid === '') {
                      ElMessage.error('广告规格不能为空')
                      return false
                    }
                    if (dto.adcolorid === '') {
                      ElMessage.error('色彩不能为空')
                      return false
                    }
                  }
                  if (dto.mediatypekey === 'app') {
                    if (dto.foldid === '') {
                      ElMessage.error('位置不能为空')
                      return false
                    }
                    if (dto.pagesortid === '') {
                      ElMessage.error('内容|曝光率不能为空')
                      return false
                    }
                    if (dto.addisplayformid === '') {
                      ElMessage.error('广告形式不能为空')
                      return false
                    }
                    if (dto.adspecid === '') {
                      ElMessage.error('尺寸|周期不能为空')
                      return false
                    }
                    if (dto.adcolorid === '') {
                      ElMessage.error('广告样式不能为空')
                      return false
                    }
                  }
                  if (dto.mediatypekey === 'wei') {
                    if (dto.foldid === '') {
                      ElMessage.error('微信媒体不能为空')
                      return false
                    }
                    if (dto.pagesortid === '') {
                      ElMessage.error('广告位置不能为空')
                      return false
                    }
                    if (dto.addisplayformid === '') {
                      ElMessage.error('广告形式不能为空')
                      return false
                    }
                    if (dto.adspecid === '') {
                      ElMessage.error('尺寸不能为空')
                      return false
                    }
                  }
                  if (dto.mediatypekey === 'multi') {
                    if (dto.foldareaverid === '') {
                      ElMessage.error('项目分类名称不能为空')
                      return false
                    }
                  }
                  formOrderItemList.push(parsePreOrderToTworderitem(order, Mode.Add))
                  reRenderFormOrderItemList()
                }
              "
            >
              添加到广告明细列表
            </el-button>
          </div>
        </el-row>
        <el-divider content-position="left" style="color: red">订单明细-列表</el-divider>
        <hg-table
          ref="formOrderItemListRef"
          :data-list="getNotDeletedFormOrderItemList()"
          :query-info="{}"
          :page-total="getNotDeletedFormOrderItemList().length"
          :selection-change="() => {}"
          :load-data="() => {}"
          :hide-pagination="true"
          :height="formOrderItemListTableHeight"
        >
          <el-table-column
            prop="prestartdate"
            label="预刊日期"
            min-width="100"
            show-overflow-tooltip
            :formatter="(row) => dayjs(row.prestartdate).format('YYYY-MM-DD')"
          />
          <el-table-column prop="sadtitle" label="明细标题" min-width="150" show-overflow-tooltip />
          <el-table-column prop="medianame" label="媒体" min-width="100" show-overflow-tooltip />
          <el-table-column
            prop="foldname"
            label="叠次|位置"
            min-width="140"
            show-overflow-tooltip
          />
          <el-table-column
            prop="foldareavername"
            label="叠次版本|频道/分类"
            min-width="180"
            show-overflow-tooltip
          />
          <el-table-column
            prop="foldpageplanname"
            label="版面"
            min-width="60"
            show-overflow-tooltip
          />
          <el-table-column
            prop="addisplayformname"
            label="广告形式"
            min-width="120"
            show-overflow-tooltip
          />
          <el-table-column
            prop="pagesortname"
            label="版面类别|内容/曝光率|位置"
            min-width="180"
            show-overflow-tooltip
          />
          <el-table-column
            prop="adcolorname"
            label="色彩|广告样式"
            min-width="120"
            show-overflow-tooltip
          />
          <el-table-column
            prop="adspecname"
            label="规格|尺寸"
            min-width="120"
            show-overflow-tooltip
          />
          <el-table-column
            label="操作"
            width="180"
            align="center"
            class-name="small-padding fixed-width"
            fixed="right"
          >
            <template #default="i">
              <div v-if="mode !== Mode.View" style="padding-left: 10px">
                <el-button
                  link
                  type="success"
                  icon="Edit"
                  size="small"
                  @click="(_: any) => {
                    parseTworderitemToPreOrder(dto, i.row)
                    treeRef.setCurrentKey(getExtId(i.row))
                    changeAdMedia(Mode.Edit, i.row.id)
                  }"
                >
                  修改
                </el-button>
                <el-popconfirm
                  title="确定删除此条明细?"
                  width="200"
                  @confirm="(_: any) => {
                    formOrderItemList.forEach(item => {
                      if (item.id === i.row.id) {
                        item.bdelete = true
                      }
                    })
                  }"
                >
                  <template #reference>
                    <el-button link type="danger" icon="Delete" size="small"> 删除 </el-button>
                  </template>
                </el-popconfirm>
              </div>
              <div v-else>
                <el-button
                  link
                  type="primary"
                  icon="view"
                  size="small"
                  @click="(_: any) => {
                    parseTworderitemToPreOrder(dto, i.row)
                    treeRef.setCurrentKey(getExtId(i.row))
                    changeAdMedia(Mode.Edit, i.row.id)
                  }"
                >
                  详情
                </el-button>
              </div>
            </template>
          </el-table-column>
        </hg-table>
        <el-row style="margin-top: 1rem">
          <el-col v-if="props.bookType === 2" :span="8">
            <el-form-item label="申请资源预审">
              <el-button
                v-if="mode !== Mode.View"
                type="primary"
                size="small"
                icon="top"
                @click="openAdResourceApplicationDialog"
              >
                上传资源文件
              </el-button>
              <el-button
                v-if="mode === Mode.View"
                type="primary"
                size="small"
                icon="view"
                link
                @click="openAdResourceApplicationDetailDialog"
              >
                资源详情
              </el-button>
            </el-form-item>
          </el-col>
          <el-col v-if="props.bookType === 2" :span="16">
            <el-form-item label="选择广告资源">
              <el-button
                v-if="mode !== Mode.View"
                type="primary"
                size="small"
                icon="top"
                @click="
                  () => {
                    if (dto.employtype === 0 && (!dto.customerid || !dto.customername)) {
                      modal.msgWarning('请选择直接客户')
                      return
                    }
                    if (dto.employtype === 1 && (!dto.agencytid || !dto.agencyname)) {
                      modal.msgWarning('请选择代理公司')
                      return
                    }
                    if (dto.employtype === 2 && (!dto.agentid || !dto.agentname)) {
                      modal.msgWarning('请选择内容生产方')
                      return
                    }
                    resourceDialogVisible = true
                  }
                "
              >
                选择审核通过的广告资源
              </el-button>
              <!--  -->
              <el-tag
                v-if="approvedResourceApplicationForm.id"
                class="mx-1"
                closable
                type="danger"
                style="cursor: pointer"
                @click="openAdResourceApplicationDetailDialog"
                @close="(_: any) => {
                  if (mode === Mode.View) return
                  approvedResourceApplicationForm = { ...adResourceApplicationInfo }
                }"
              >
                {{ approvedResourceApplicationForm.sadtitle }}
              </el-tag>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button v-if="mode !== Mode.View" type="primary" icon="Check" @click="handleSave()">
            保存
          </el-button>
          <el-button v-if="mode !== Mode.View" icon="Close" @click="handleCancel">取消</el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog
      v-model="customerSelectionDialog"
      title="选择客户"
      width="1000"
      align-center
      :destroy-on-close="true"
      @close="(_: any) => {
        customerSelectionDialog = false
        customerSelectionType = 0
      }"
    >
      <el-row :gutter="50">
        <el-col :span="24">
          <!--  todo 切换业务员归属 是否需要清空其他两项业务员id+name        -->
          <Hg-Sales-Customer
            :buse="true"
            :selfid="selfid"
            :showbuse="false"
            :itype="customerSelectionType"
            @selectiondata="(customer: TAdCustomer) => {
              const customerId = customer.id?.toString()
              const customerName = customer?.sname || ''
              selfid = customerId
              if (customerSelectionType === 0) { // 直接客户
                dto.customerid = customerId
                dto.customername = customerName
              } else if (customerSelectionType === 1) { // 代理公司
                dto.agencytid = customerId
                dto.agencyname = customerName
                if (dto.employtype === 1) {
                  dto.agencyemployid = userStore.user?.userid.toString()
                  dto.agencyemployname = userStore.user?.username.toString()
                }
              } else if (customerSelectionType === 2) { // 内容生产方
                dto.agentid = customerId
                dto.agentname = customerName
                if (dto.employtype === 2) {
                  dto.agentemployid = userStore.user?.userid.toString()
                  dto.agentemployname = userStore.user?.username.toString()
                }
              }
              customerSelectionDialog = false
            }"
          />
        </el-col>
      </el-row>
    </el-dialog>
    <ad-resource-application-dialog
      ref="resourceDialogRef"
      :hide-approval-checkbox="true"
      @onCloseDialog="(resourceApplicationId: string) => {
        // 快速预约时申请资源预审保存成功后,设置id
        adResourceApplicationForm.id = resourceApplicationId
      }"
    />
    <el-dialog
      v-model="resourceDialogVisible"
      title="资源列表"
      width="1200"
      align-center
      :close-on-click-modal="false"
      :destroy-on-close="true"
    >
      <AdResourceComponent
        :customer-ids="customerIds"
        :approve-status="'2'"
        @selectionresourcedata="selectResource"
      ></AdResourceComponent>
    </el-dialog>
  </div>

  <el-dialog
    v-model="adResourceApplicationDetailDialogVisible"
    title="广告资源申请表详情"
    :width="adResourceApplicationForm.iapprovestatus !== EApproveStatus.待审 ? '1200' : '1000'"
    align-center
    :close-on-click-modal="false"
    :destroy-on-close="true"
  >
    <HgAdSourceDetail
      :id="adResourceApplicationForm.id"
      :showflag="adResourceApplicationForm.iapprovestatus !== EApproveStatus.待审 ? true : false"
    ></HgAdSourceDetail>
  </el-dialog>
</template>

<script setup lang="ts">
import { fenbianlv, required } from '@/utils'
import { EApproveStatus, ECustomerType, EFlowType, EHgDeptZtreeUrl } from '@/types/common/enumindex'
import { TSelectZtree } from '@/types/common'
import dayjs from 'dayjs'
import { ElMessage, FormInstance } from 'element-plus'
import {
  getExtId,
  initPreOrder,
  parseTworderitemToPreOrder,
  parseTworderToPreOrder
} from '@/types/views/ad/pre-order'
import { ref } from 'vue'
import modal from '@/plugins/modal'
import {
  ElTreeNodeVO,
  mediaTypeFieldLabels,
  parsePreOrderToTworderitem
} from '@/types/views/ad/adpreorder'
import { savePreOrderApi } from '@/api/ad/adtworder'
import { getBusinessentityComboApi } from '@/api/finance/businessentity'
import { listPlanOnPreOrderApi } from '@/api/schedule/foldpageplan'
import { getMediaFoldElTreeApi } from '@/api/media/foldareaver'
import { listUsableMediaTypeApi } from '@/api/media/mediatype'
import { IMediaTypeChecked } from '@/types/views/media/mediatype'
import { IBusinessentity } from '@/types/views/finance/businessentity'
import { getadtypelistApi } from '@/api/ad/adtype'
import { TAdtype } from '@/types/views/ad/adtype'
import { IAxios } from 'axios'
import { TreeNodeData } from 'element-plus/es/components/tree-v2/src/types'
import { TAFoldPagePlan } from '@/types/views/schedule/foldpageplan'
import { ITbaddisplayform } from '@/types/views/price/addisplayform'
import { TAdcolor } from '@/types/views/price/adcolor'
import { TAdspec } from '@/types/views/media/adspec'
import { Tbpagesort } from '@/types/views/media/PageSort'
import { listUsableAdDisplayFormApi } from '@/api/price/addisplayform'
import { listUsableAdColorApi } from '@/api/price/adcolor'
import { listUsablePageSortApi } from '@/api/media/PageSort'
import { listUsableAdSpecApi } from '@/api/media/adspec'
import type { TAdCustomer } from '@/types/views/customer'
import useUserStore from '@/store/modules/user'
import { Mode } from '@/types/views/ad/mode'
import { getPreOrderApi } from '@/api/ad/adpreorder'
import { Tworder, Tworderitem } from '@/types/views/ad/adtworder'
import AdResourceApplicationDialog from './AdResourceApplicationDialog.vue'
import { getFlowlistComboByFlowTypeApi } from '@/api/flow'
import { Tadindustrylist } from '@/types/components/hgindustry'
import {
  IAdResourceApplicationDTO,
  ResourceApplicationDialogMode
} from '@/types/views/ad/adresourceapplication'
import AdResourceComponent from '@/views/ad/AdResourceComponent.vue'
import { listAdResourceApplicationByQuickOrderIdApi } from '@/api/ad/adresourceapplication'
import HgAdSourceDetail from '@/components/HgAdSourceDetail/index.vue'

defineOptions({
  name: 'PreOrderAddEditView'
})
const props = withDefaults(
  defineProps<{
    bookType: number
  }>(),
  {
    bookType: 1 // 默认 1-广告预约, 快速预约传入2
  }
)
const userStore = useUserStore()
/** 关闭弹出框时,调用父组件的方法 */
const emit = defineEmits(['onCloseDialog'])
/** 是否显示弹出框 */
const visible = ref(false)
/** 弹出框表单中的数据对象 */
const dto = ref(initPreOrder(props.bookType))
/** 弹出框表单 */
const form = ref<FormInstance>()
/** 弹出框表单中的订单明细表格 */
const formOrderItemListRef = ref<any>()
/** 弹出框表单中的订单明细表格初始高度 */
const formOrderItemListTableHeight = ref<Number>(100)
/** 广告明细列表 */
const formOrderItemList = ref<Tworderitem[]>([])
/** 经营主体下拉 */
const businessentityCombo = ref<IBusinessentity[]>([])
/** 叠次版本树节点列表 */
const foldTreeNodes = ref<ElTreeNodeVO[]>([])
/** 媒体类型集合 */
const mediaTypes = ref<IMediaTypeChecked[]>([])
/** 广告类型下拉 */
const adtypeCombo = ref<TAdtype[]>([])
/** 是否可勾选叠次(广告媒体) */
const canChecked = ref(false)
/** 表单上的叠次版本下拉列表 */
const formfoldareavers = ref<{ id: string; name: string }[]>([])
/** 计划列表 */
const planList = ref<TAFoldPagePlan[]>([])
/** 表单上的选中的叠次版面计划列表 */
const formFoldPageList = ref<TAFoldPagePlan[]>([])
/** 表单上的广告形式列表 */
const formDisplayFormList = ref<ITbaddisplayform[]>([])
/** 表单上的广告颜色列表 */
const formColorList = ref<TAdcolor[]>([])
/** 表单上的广告规格列表 */
const formSpecList = ref<TAdspec[]>([])
/** 表单上的版面类别列表 */
const formPageSortList = ref<Tbpagesort[]>([])
/** 客户选择 */
const customerSelectionDialog = ref(false)
/** 客户类型 */
const customerSelectionType = ref<Number>(0)
/** 修改时将客户的id传给客户选择组件 */
const selfid = ref<string | undefined>('')
/** 当前的弹出框模式 */
const mode = ref<Mode>(Mode.Add)
/** 当前正在修改的明细id */
const currentModifiedOrderItemId = ref<string | undefined>(undefined)
const resourceDialogRef = ref()
/** 流程类型列表 */
const flowTypeList = ref<Tadindustrylist[]>([])
/** 选择资源列表 */
const resourceDialogVisible = ref(false)
/** 当前客户ids */
const customerIds = computed(() => {
  const ids: string[] = []
  if (dto.value.customerid) {
    ids.push(dto.value.customerid)
  }
  if (dto.value.agencytid) {
    ids.push(dto.value.agencytid)
  }
  if (dto.value.agentid) {
    ids.push(dto.value.agentid)
  }
  return ids
})
/** 资源详情弹出框 */
const adResourceApplicationDetailDialogVisible = ref(false)

// bookType 1-广告预约 2-快速预约
const init = (dialogMode: Mode, orderId?: string) => {
  adResourceApplicationForm.value = { ...adResourceApplicationInfo }
  approvedResourceApplicationForm.value = { ...adResourceApplicationInfo }
  mode.value = dialogMode
  getMediaFoldElTreeApi().then(({ obj }: IAxios<ElTreeNodeVO[]>) => {
    foldTreeNodes.value = obj
  })
  listUsableMediaTypeApi().then(({ obj }: IAxios<IMediaTypeChecked[]>) => {
    mediaTypes.value = obj
  })
  getBusinessentityComboApi().then(({ obj }: IAxios<IBusinessentity[]>) => {
    businessentityCombo.value = obj
    dto.value.businessentityid = obj.find((i) => i.bdefault)!.id
    dto.value.businessentityname = obj.find((i) => i.bdefault)!.sname
  })
  getadtypelistApi().then(({ obj }: IAxios<TAdtype[]>) => {
    adtypeCombo.value = obj
    dto.value.adtypeid = obj.find((i) => i.bdefault)!.id!
    dto.value.adtypename = obj.find((i) => i.bdefault)!.sname!
  })
  visible.value = true
  form.value?.resetFields()
  resetForm()
  dto.value.ibooktype = props.bookType
  if (props.bookType === 2) {
    // 获取审批流程下拉列表
    getFlowlistComboByFlowTypeApi(EFlowType.资源审批流程).then((res: IAxios<Tadindustrylist[]>) => {
      if (res.success) {
        flowTypeList.value = res.obj
      }
    })
    // 查询未审批通过的资源(包含文件)
    listAdResourceApplicationByQuickOrderIdApi(orderId!).then((res: IAxios) => {
      if (res.success) {
        if (res.obj) {
          const list = res.obj as IAdResourceApplicationDTO[]
          if (list.length > 2) {
            modal.msgWarning(
              '快速预约最多只能对应两条资源记录,一个是之前已经审批通过的资源,一个是本次新提交的资源'
            )
            return
          }
          list.forEach((item) => {
            if (item.iapprovestatus === 2) {
              approvedResourceApplicationForm.value = { ...item }
            } else {
              adResourceApplicationForm.value = { ...item }
            }
          })
        }
      }
    })
  }
  if (Array.of(Mode.Edit, Mode.View).includes(mode.value)) {
    // 查询order+orderItem+orderItemBelong
    getPreOrderApi({ orderId: orderId! }).then(({ obj }: IAxios<Tworder>) => {
      if (!obj) {
        ElMessage({ message: '查询订单失败', type: 'error' })
        return
      }
      if (!obj.orderitem || obj.orderitem.length === 0) {
        ElMessage({ message: '未找到广告明细', type: 'error' })
        return
      }
      if (!obj.orderitem[0].orderitembelong || obj.orderitem[0].orderitembelong.length === 0) {
        ElMessage({ message: '未找到业务员归属,查询失败', type: 'error' })
        return
      }
      dto.value = parseTworderToPreOrder(
        obj,
        obj.orderitem[0].orderitembelong[0].employtype!,
        obj.orderitem[0].prestartdate!,
        props.bookType
      )
      formOrderItemList.value = obj.orderitem!
    })
  }
  setTimeout(() => {
    reRenderFormOrderItemList()
  }, 500)
}

const getNotDeletedFormOrderItemList = () => {
  return formOrderItemList.value.filter((item) => !item.bdelete)
}

const reRenderFormOrderItemList = () => {
  formOrderItemListTableHeight.value = 100 + 30 * getNotDeletedFormOrderItemList().length
}

/** 切换媒体类型时,同时切换后面的选项 */
const changeAdMedia = async (mode: Mode, orderItemId?: string) => {
  currentModifiedOrderItemId.value = orderItemId
  const currentNode = await getCurrentNode()
  const prestartdate = dto.value.prestartdate
  const mediatypekey = currentNode.extObj.mediatypekey
  const mediatypename = currentNode.extObj.mediatypename
  const mediaid = currentNode.extObj.mediaid
  const medianame = currentNode.extObj.medianame
  const foldid = currentNode.extObj.foldid
  const foldname = currentNode.extObj.foldname
  const plans: IAxios<TAFoldPagePlan[]> = await listPlanOnPreOrderApi(
    prestartdate,
    mediatypekey,
    mediaid,
    foldid
  )
  // 只有报刊才校验计划
  if (plans.obj.length === 0 && mediatypekey === 'paper') {
    canChecked.value = false
    ElMessage({ message: '叠次' + currentNode.label + '无计划, 请重新选择', type: 'warning' })
    return
  }
  canChecked.value = true
  dto.value.mediatypekey = mediatypekey
  dto.value.mediatypename = mediatypename
  dto.value.mediaid = mediaid
  dto.value.medianame = medianame
  dto.value.foldid = foldid
  dto.value.foldname = foldname
  // 设置表单的叠次版本
  const map = new Map()
  plans.obj.forEach((item) => {
    if (!map.has(item.foldareaverid)) {
      map.set(item.foldareaverid, {
        id: item.foldareaverid,
        name: item.foldareavername
      })
    }
  })
  formfoldareavers.value = Array.from(map.values())
  planList.value = plans.obj
  if (mode === Mode.Add) {
    // 清空相关表单值
    clearPreOrderDTOItems(false)
  }
  if (mode === Mode.Edit) {
    getFormFoldPageList()
  }
  // 根据选择的媒体类型,动态显示后面的选项
  mediaTypes.value.forEach((item) => {
    item.checked = item.skey === mediatypekey
  })
  listUsableAdDisplayFormApi(mediatypekey).then(({ obj }: IAxios<ITbaddisplayform[]>) => {
    formDisplayFormList.value = obj
  })
  listUsableAdColorApi(mediatypekey).then(({ obj }: IAxios<TAdcolor[]>) => {
    formColorList.value = obj
  })
  listUsablePageSortApi(mediatypekey, dto.value.foldid).then(({ obj }: IAxios<Tbpagesort[]>) => {
    formPageSortList.value = obj
  })
  listUsableAdSpecApi(dto.value.mediaid).then(({ obj }: IAxios<TAdspec[]>) => {
    formSpecList.value = obj
  })
}
/** 资源赋值 */
const selectResource = (val: IAdResourceApplicationDTO) => {
  approvedResourceApplicationForm.value = { ...val }
  resourceDialogVisible.value = false
}
const getFormFoldPageList = () => {
  // prettier-ignore
  formFoldPageList.value = !dto.value.foldareaverid
    ? []
    : Array.from(new Set(planList.value.filter((plan) => plan.foldareaverid === dto.value.foldareaverid)))
}

/**
 * 清空表单中订单明细<br>
 * 切换日期时，清空明细里的所有选项（日期除外）<br>
 * 切换媒体-叠次时，清空明细里的所与选项（日期、媒体-叠次除外）<br>
 * @param onChangeDate true-切换日期 false-切换媒体
 */
const clearPreOrderDTOItems = (onChangeDate: boolean) => {
  if (onChangeDate) {
    canChecked.value = false
    dto.value.foldid = ''
    dto.value.foldname = ''
  }
  dto.value.foldareaverid = ''
  dto.value.foldareavername = ''
  dto.value.foldpageplanid = ''
  dto.value.foldpageplanname = undefined
  dto.value.addisplayformid = ''
  dto.value.addisplayformname = ''
  dto.value.pagesortid = ''
  dto.value.pagesortname = ''
  dto.value.adcolorid = ''
  dto.value.adcolorname = ''
  dto.value.adspecid = ''
  dto.value.adspecname = ''
  dto.value.saditemtitle = ''
}

const treeRef = ref<InstanceType<typeof ElTree>>()
const getCurrentNode = (): TreeNodeData | never => {
  const currentNode = treeRef.value?.getCurrentNode()
  if (!currentNode) {
    ElMessage({ message: '请选择叠次版面', type: 'error' })
    throw new Error('请选择叠次版面')
  }
  return currentNode
}
/** 动态获取label */
const getLabelByField = (field: string) => {
  const mediaTypeKey = getCurrentNode().extObj.mediatypekey
  // @ts-ignore
  return mediaTypeFieldLabels[mediaTypeKey][field]
}

/**
 * 根据选择的媒体类型的key,判断某些选项是否可以显示
 * @param mediaTypeKeys 媒体类型key
 */
const isChecked = (mediaTypeKeys: String[]) => {
  const item: IMediaTypeChecked | undefined = mediaTypes.value.find(
    // 给定媒体已经被勾选上,才会显示
    (item) => mediaTypeKeys.includes(item.skey) && item.checked
  )
  const checked = item?.checked || false
  return canChecked.value && checked
}

/**
 * 资源预审初始对象
 */
const adResourceApplicationInfo: IAdResourceApplicationDTO = {
  id: '',
  icusttype: ECustomerType.直接客户,
  deptid: userStore.user!.deptid!.toString(),
  deptname: userStore.user!.deptname!,
  employid: userStore.user!.userid,
  employname: userStore.user!.username,
  dstartdate: dayjs().format('YYYY-MM-DD'),
  denddate: '',
  sadtitle: '',
  sadcontent: '',
  sremark: '',
  customerid: '',
  customername: '',
  bquickly: true,
  iapprovestatus: EApproveStatus.待审,
  filelist: []
}
const adResourceApplicationForm = ref<IAdResourceApplicationDTO>({ ...adResourceApplicationInfo })
/** 修改时选择的已经审核通过的广告资源 */
const approvedResourceApplicationForm = ref<IAdResourceApplicationDTO>({
  ...adResourceApplicationInfo
})
/**
 * 打开资源预审dialog
 */
const openAdResourceApplicationDialog = () => {
  if (props.bookType !== 2) {
    return
  }
  if (!dto.value.prestartdate) {
    modal.msgWarning('请先选择预刊日期')
    return
  }
  if (dto.value.employtype === 0 && (!dto.value.customerid || !dto.value.customername)) {
    modal.msgWarning('请选择直接客户')
    return
  }
  if (dto.value.employtype === 1 && (!dto.value.agencytid || !dto.value.agencyname)) {
    modal.msgWarning('请选择代理公司')
    return
  }
  if (dto.value.employtype === 2 && (!dto.value.agentid || !dto.value.agentname)) {
    modal.msgWarning('请选择内容生产方')
    return
  }
  if (!dto.value.sadtitle) {
    modal.msgWarning('请先填写广告标题')
    return
  }

  // 直接客户
  if (dto.value.employtype === 0) {
    adResourceApplicationForm.value.customerid = dto.value.customerid
    adResourceApplicationForm.value.customername = dto.value.customername
  } else if (dto.value.employtype === 1) {
    // 代理公司
    adResourceApplicationForm.value.customerid = dto.value.agencytid
    adResourceApplicationForm.value.customername = dto.value.agencyname
  } else if (dto.value.employtype === 2) {
    // 内容生产方
    adResourceApplicationForm.value.customerid = dto.value.agentid
    adResourceApplicationForm.value.customername = dto.value.agentname
  }
  adResourceApplicationForm.value.dstartdate = dto.value.prestartdate
  adResourceApplicationForm.value.sadtitle = dto.value.sadtitle
  adResourceApplicationForm.value.icusttype = dto.value.employtype
  const isAdd = ![Mode.Edit, Mode.View].includes(mode.value)
  if (isAdd) {
    resourceDialogRef.value.init(
      ResourceApplicationDialogMode.PreOrderAdd,
      flowTypeList.value,
      adResourceApplicationForm.value
    )
  } else {
    resourceDialogRef.value.init(
      ResourceApplicationDialogMode.PreOrderEdit,
      flowTypeList.value,
      adResourceApplicationForm.value
    )
  }
}

/**
 * 资源详情
 */
const openAdResourceApplicationDetailDialog = () => {
  if (approvedResourceApplicationForm.value.id !== '') {
    adResourceApplicationForm.value.id = approvedResourceApplicationForm.value.id
  }
  adResourceApplicationDetailDialogVisible.value = true
}

/** 保存 */
const handleSave = () => {
  form.value?.validate((valid) => {
    if (valid) {
      if (!dto.value.customerid || !dto.value.customername) {
        ElMessage({ message: '请选择直接客户', type: 'warning' })
        return
      }
      if (dto.value.employtype === 1 && (!dto.value.agencytid || !dto.value.agencyname)) {
        ElMessage({ message: '请选择代理公司', type: 'warning' })
        return
      }
      if (dto.value.employtype === 2 && (!dto.value.agentid || !dto.value.agentname)) {
        ElMessage({ message: '请选择内容生产方', type: 'warning' })
        return
      }
      if (formOrderItemList.value.filter((i) => i.bdelete === false).length === 0) {
        ElMessage({ message: '广告明细列表不可为空', type: 'warning' })
        return
      }
      // 校验快速预约
      if (props.bookType === 2) {
        if (dto.value.ibooktype !== 2) {
          modal.msgWarning('当前录入方式必须设置为快速预约')
          return
        }
        if (!adResourceApplicationForm.value.id && !approvedResourceApplicationForm.value.id) {
          modal.msgWarning('请上传资源文件或者选择已经审核通过的广告资源文件')
          return
        }
      }
      // 处理业务员归属
      // 直接客户
      if (dto.value.employtype === 0) {
        dto.value.agencyemployid = ''
        dto.value.agencyemployname = ''
        dto.value.agentemployid = ''
        dto.value.agentemployname = ''
      } else if (dto.value.employtype === 1) {
        // 代理公司
        dto.value.agentemployid = ''
        dto.value.agentemployname = ''
      } else if (dto.value.employtype === 2) {
        // 内容生产方
        dto.value.agencyemployid = ''
        dto.value.agencyemployname = ''
      }
      formOrderItemList.value.forEach((item) => {
        item.ibooktype = props.bookType
      })
      savePreOrderApi({
        order: dto.value,
        orderItems: formOrderItemList.value,
        adResourceApplicationId: adResourceApplicationForm.value.id || '',
        approvedResourceApplicationId: approvedResourceApplicationForm.value.id || ''
      }).then(({ success, msg }: IAxios) => {
        if (success) {
          modal.msgSuccess('操作成功')
          emit('onCloseDialog')
        } else {
          modal.msgError(msg)
        }
      })
      visible.value = false
      resetForm()
    }
  })
}
/** 取消 */
const handleCancel = () => {
  visible.value = false
  emit('onCloseDialog')
  form.value?.resetFields()
  resetForm()
}
/** 重置 */
const resetForm = () => {
  dto.value = initPreOrder(props.bookType)
  formOrderItemList.value = []
  currentModifiedOrderItemId.value = undefined
  formOrderItemListTableHeight.value = 100
}

defineExpose({
  init
})
</script>

<style scoped lang="scss">
.el-form-item {
  margin-bottom: 10px;
}
.el-select {
  width: 100%;
}
</style>
