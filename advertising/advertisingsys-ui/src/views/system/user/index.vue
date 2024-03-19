<!--
 * @Author: caogd
 * @Date: 2023-08-28 16:44:12
 * @LastEditTime: 2024-02-21 09:17:06
 * @LastEditors: suny suny@hgzp.com.cn
 * @Description: 人员管理
-->
<template>
  <div class="app-container">
    <el-row :gutter="5">
      <!--部门数据-->
      <el-col :xs="8" :sm="7" :md="6" :lg="5" :xl="4">
        <div class="table_box left_box">
          <div class="fenge-box">
            <el-icon color="#409EFC"><star-filled /></el-icon><span>人员列表</span
            ><el-icon color="#409EFC"><star-filled /></el-icon>
          </div>

          <div id="empztreeid" class="ztree"></div>
        </div>
      </el-col>
      <!-- 用户数据 -->
      <el-col :xs="16" :sm="17" :md="18" :lg="19" :xl="20">
        <div class="search_box">
          <div class="flex">
            <el-button type="success" icon="Check" @click="handleBuse(true)">启用</el-button>
            <el-button type="danger" icon="Close" @click="handleBuse(false)">禁用</el-button>
            <el-button type="warning" @click="handleEmpRoles()"
              ><svg-icon icon-class="peoples" />设置角色</el-button
            >
            <el-input
              v-model="queryInfo.queryKey"
              placeholder="请输入人员名称"
              clearable
              style="width: 200px"
              @clear="handleQuery"
              @keyup.enter="handleQuery"
            />
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
            <el-tooltip class="item" effect="dark" content="帮助" placement="top">
              <el-button class="help" type="text" @click="showhelp()">?</el-button>
            </el-tooltip>
          </div>
        </div>
        <div class="table_box">
          <el-table
            :data="userList"
            row-key="id"
            :height="tableheight"
            :border="true"
            stripe
            :header-cell-style="tableHeaderColor"
            @sort-change="sortChange"
            @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="55" align="center"> </el-table-column>
            <el-table-column
              prop="susername"
              label="用户名"
              min-width="180"
              sortable="custom"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="sloginname"
              label="登录名"
              :width="150"
              sortable="custom"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column prop="deptName" label="部门" :width="200" show-overflow-tooltip>
            </el-table-column>
            <el-table-column prop="sdutiesname" label="职务" :width="200" show-overflow-tooltip>
            </el-table-column>
            <el-table-column prop="roleNames" label="角色" :min-width="200" show-overflow-tooltip>
            </el-table-column>
            <el-table-column prop="bsex" label="性别" :width="80" align="center">
              <template #default="scope">
                <span>{{ scope.row.bsex ? '女' : '男' }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="isort" label="序号" :width="80" align="center" sortable="custom">
            </el-table-column>
            <el-table-column prop="buse" label="启用" :width="80" align="center" sortable="custom">
              <template #default="scope">
                <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column prop="bselfrole" label="赋权" :width="60" align="center">
              <template #default="scope">
                <el-checkbox v-model="scope.row.bselfrole" disabled></el-checkbox>
              </template>
            </el-table-column>

            <!-- <el-table-column prop="bmenushow" label="微信" :width="60">
              <template #default="scope">
                <el-checkbox v-model="scope.row.bmenushow" disabled></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column prop="sweixin" label="微信号 " :width="100"></el-table-column> -->
            <el-table-column
              label="操作"
              align="center"
              width="210"
              class-name="small-padding fixed-width"
              fixed="right"
            >
              <template #default="scope">
                <el-button link type="success" icon="Edit" @click="handleUpdate(scope.row)"
                  >修改</el-button
                >
                <el-button
                  v-if="scope.row.buse"
                  link
                  type="danger"
                  icon="Close"
                  @click="handleBuse(false, scope.row)"
                  >禁用</el-button
                ><el-button
                  v-if="!scope.row.buse"
                  link
                  type="success"
                  icon="Check"
                  @click="handleBuse(true, scope.row)"
                  >启用</el-button
                >
                <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                  >删除</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <!-- 页码 -->
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 30, 40]"
            layout="total, sizes, prev, pager, next, jumper"
            class="pagination"
            small
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-col>
    </el-row>
    <!-- 添加或修改菜单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="人员编辑"
      width="700"
      align-center
      :close-on-click-modal="false"
    >
      <el-form ref="ruleFormRef" :model="employForm" :rules="rules" label-width="100px">
        <el-row :gutter="50">
          <el-col :span="22">
            <el-form-item label="名称" prop="susername">
              <el-input v-model="employForm.susername"></el-input>
            </el-form-item>
            <el-form-item label="登录名" prop="sloginname">
              <el-input v-model="employForm.sloginname"></el-input>
            </el-form-item>
            <el-form-item label="登录密码" prop="spassword">
              <el-input
                v-model="employForm.spassword"
                show-password
                @keyup.delete="employForm.spassword = ''"
              ></el-input>
            </el-form-item>
            <el-form-item label="电话" prop="sphone">
              <el-input v-model="employForm.sphone"></el-input>
            </el-form-item>
            <el-form-item label="隶属部门" prop="deptid">
              <HgZtreeSelect
                :scopeflag="EHgDeptZtreeUrl.DEPT_GETSYSEMPDEPTTREE"
                :selectids="employForm.deptid ? [employForm.deptid] : []"
                @selectionztree="onselectionztree"
              ></HgZtreeSelect>
            </el-form-item>
            <el-form-item label="兼职部门" prop="parttimedeptid">
              <HgZtreeSelect
                :scopeflag="EHgDeptZtreeUrl.DEPT_GETSYSEMPDEPTTREE"
                :checkboxtype="true"
                :selectids="treeSelectid"
                @selectionztree="onParttimeSelection"
              ></HgZtreeSelect>
            </el-form-item>
            <!-- <el-form-item label="微信账号" prop="sweixin">
              <el-input v-model="employForm.sweixin"></el-input>
            </el-form-item> -->
            <el-form-item label="职务" prop="dutiesid">
              <el-select v-model="employForm.dutiesid" placeholder="职务" clearable>
                <el-option
                  v-for="item in dutiesCombo"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item v-if="adminflag !== 1" label="管理员类型" prop="badminflag">
              <el-select
                v-model="employForm.badminflag"
                placeholder="管理员类型"
                @change="onBadminflag"
              >
                <el-option label="一般人员" :value="0"></el-option>
                <el-option label="普通管理员" :value="1"></el-option>
                <!-- <el-option label="超级管理员" :value="2"></el-option> -->
                <el-option label="兼职超管" :value="3"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item v-if="employForm.badminflag === 1" label="管理部门" prop="parttimedeptid">
              <HgZtreeSelect
                :scopeflag="EHgDeptZtreeUrl.DEPT_GETSYSEMPDEPTTREE"
                :checkboxtype="true"
                :selectids="smanagedeptsid"
                :disabled="adminflag === 1"
                @selectionztree="onSmanageDeptSelection"
              ></HgZtreeSelect>
            </el-form-item>
            <el-form-item label="角色" prop="roleIds">
              <el-select v-model="employRoleIds" multiple placeholder="角色" style="width: 60%">
                <el-option
                  v-for="item in roleCombo"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="邮箱" prop="semail">
              <el-input v-model="employForm.semail"></el-input>
            </el-form-item>
            <el-form-item label="序号" prop="isort">
              <el-input-number v-model="employForm.isort" :min="1" :max="99999" />
            </el-form-item>
            <el-form-item label="性别" prop="bsex" required>
              <el-radio-group v-model="employForm.bsex">
                <el-radio :label="false">男</el-radio>
                <el-radio :label="true">女</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-row :gutter="50">
              <el-col :span="6">
                <el-form-item label="启用" prop="buse">
                  <el-checkbox v-model="employForm.buse"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="业务员" prop="bsalesman">
                  <el-checkbox v-model="employForm.bsalesman"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="单独赋权" prop="bselfrole">
                  <el-checkbox v-model="employForm.bselfrole"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="负责人" prop="blead">
                  <el-checkbox v-model="employForm.blead"></el-checkbox>
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" icon="Check" @click="onSubmitForm(ruleFormRef)"
            >确 定</el-button
          >
          <el-button icon="close" @click="handleCancel(ruleFormRef)">取 消</el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog
      v-model="roleDialogVisible"
      title="设置角色"
      width="500"
      align-center
      :close-on-click-modal="false"
    >
      <el-row :gutter="20">
        <el-col :span="12" :offset="6">
          <HgTransferPanel v-model="roleCheckList" :data="roleCombo"></HgTransferPanel>
        </el-col>
      </el-row>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="onSubmitFormRole()">确 定</el-button>
          <el-button @click="roleDialogVisible = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {
  getEmployPageListApi,
  getMaxIsortApi,
  saveEmployApi,
  updateEmployApi,
  deleteEmpApi,
  disableEmpApi,
  updateBatchEmpRolesApi,
  getEmployInfoByIdApi
} from '@/api/system/user'
import { TQueryInfo, TSelectZtree } from '@/types/common/index'
import { TQEmployVO, TSEmployVO, TEmployDTO } from '@/types/views/system/employ'
import { IAxios } from 'axios'
import type { ElFormItem, FormInstance, FormRules } from 'element-plus'
import {
  passwordvalidator,
  required,
  validatePhone,
  computTableHeight,
  computTreeHeight,
  tableHeaderColor,
  validateSemail
} from '@/utils/index'
import modal from '@/plugins/modal'
import { EHgDeptZtreeUrl } from '@/types/common/enumindex'

defineOptions({
  name: 'Emp'
})
import { sm2Encrypt, sm4Decrypt, sm4Encrypt } from '@/utils/smcrypto'
import useUserStore from '@/store/modules/user'
import { getDutiesComboApi } from '@/api/system/duties'
import { IDataCombo } from '@/types/common/DataCombo'
import { getRoleComboApi } from '@/api/system/role'
import HgTransferPanel from '@/components/HgTransferPanel/index.vue'
import { useRouter } from 'vue-router'
const router = useRouter()
/** 用户身份信息 */
const { user } = useUserStore()
/** 当前登陆用户管理员信息 */
const adminflag = ref(user?.adminflag)
/** 列表高度 */
const tableheight = ref(0)
/** 树高度 */
const treeheight = ref()
/** 当前页 */
const currentPage = ref(1)
/** 每页条数 */
const pageSize = ref(20)
/** 当前总条数 */
const total = ref(0)

/** 菜单数据 */
const userList = ref<TSEmployVO[]>([])
/** 编辑弹出 */
const dialogVisible = ref(false)
/** 当前选中部门id */
const deptid = ref('')
/** 查询条件排序 */
const queryInfo = ref<TQueryInfo>({
  sort: 'isort',
  order: 'asc',
  queryKey: ''
})
/** 列表多选数据 */
const multipleSelection = ref<TSEmployVO[]>([])
/** 树对象 */
let hgZtreeObj: IzTreeObj
/** 表单ref */
const ruleFormRef = ref<FormInstance>()
/** 校验设置参数 */
const rules = reactive<FormRules<TEmployDTO>>({
  sloginname: [
    { validator: required, required: true, message: '请输入登录名', trigger: 'change' },
    { max: 40, message: '不得大于40个字符', trigger: 'change' }
  ],
  susername: [
    { validator: required, required: true, message: '请输入姓名', trigger: 'change' },
    { max: 80, message: '不得大于80个字符', trigger: 'change' }
  ],
  spassword: [
    { validator: required, required: true, message: '请输入登录密码', trigger: 'blur' },
    { validator: passwordvalidator, message: '长度8位以上至少一位数字一位字母', trigger: 'blur' },
    { max: 80, message: '不得大于80个字符', trigger: 'change' }
  ],
  sphone: [
    { validator: required, required: true, message: '请输入电话', trigger: 'change' },
    { validator: validatePhone, trigger: 'blur' }
  ],
  semail: [{ validator: validateSemail, message: '请输入正确邮箱', trigger: 'blur' }]
})
/** 初始化表单对象值 */
const initEmpFormData: TEmployDTO = {
  id: '',
  deptid: '',
  sloginname: '',
  buse: true,
  isort: 1,
  spassword: '',
  sphone: '',
  susername: '',
  badminflag: 0,
  parttimedeptid: '',
  bsalesman: false,
  bsex: false
}
/** 表单对象 */
const employForm = ref<TEmployDTO>(initEmpFormData)
/** 旧密码 */
const oldpassword = ref('')
/** 兼职部门传入id */
const treeSelectid = ref<string[]>([])
/** 职务下拉 */
const dutiesCombo = ref<IDataCombo[]>([])
/** 角色下拉 */
const roleCombo = ref<IDataCombo[]>([])
/** 当前选中的角色 */
const employRoleIds = ref<string[]>([])
/** 设置角色弹出 */
const roleDialogVisible = ref(false)
/** 选中角色 */
const roleCheckList = ref<string[]>([])
/** 子管理员管理部门 */
const smanagedeptsid = ref<string[]>([])

/**
 * @description: 初始化表单值和状态
 * @return {*}
 */
const resetEmpForm = async () => {
  employForm.value = { ...initEmpFormData }
  await getMaxIsort()
  employForm.value.deptid = deptid.value
  oldpassword.value = ''
  treeSelectid.value = []
  employRoleIds.value = []
  smanagedeptsid.value = []
  ruleFormRef.value?.clearValidate()
}
/**
 * @description: 显示按钮
 * @param {*} treeId
 * @param {*} treeNode
 * @return {*}
 */
const ztreeAddHoverDom = (treeId: string, treeNode: ITreeNode) => {
  // 鼠标经过显示添加按钮
  if (treeNode.iconSkin && treeNode.level !== 0) {
    $('.edit').hide()
    $('.remove').hide()
    // 获取节点信息
    const sObj = $('#' + treeNode.tId + '_span')
    if (treeNode.editNameFlag || $('#addBtn_' + treeNode.tId).length > 0) return
    const addStr =
      "<span class='button add' id='addBtn_" +
      treeNode.tId +
      "' title='添加人员' onfocus='this.blur();'></span>" // 定义添加按钮
    sObj.after(addStr) // 加载添加按钮
    const btn = $('#addBtn_' + treeNode.tId)
    // 绑定添加事件，并定义添加操作
    if (btn) {
      btn.bind('click', async () => {
        await resetEmpForm()
        dialogVisible.value = true
      })
    }
  }
}
/**
 * @description: 隐藏按钮
 * @param {*} treeId
 * @param {*} treeNode
 * @return {*}
 */
const ztreeRemoveHoverDomfunction = (treeId: string, treeNode: ITreeNode) => {
  $('#addBtn_' + treeNode.tId)
    .unbind()
    .remove()
}

/**
 * @description: 树加载成功
 * @param {*} event
 * @param {*} treeId
 * @param {*} treeNode
 * @return {*}
 */
const initAsyncSuccess = (event: Event, treeId: string, treeNode: ITreeNode) => {
  const node = hgZtreeObj.getNodeByParam('id', '0')
  if (node) {
    hgZtreeObj.expandNode(node, true, false, true)
  }
  currentPage.value = 1
  loaddata()
}
/**
 * @description: 点击树选择
 * @param {*} event
 * @param {*} treeId
 * @param {*} treeNode
 * @return {*}
 */
const ztreeClick = (event: Event, treeId: string, treeNode: ITreeNode) => {
  if (treeNode.iconSkin) {
    // 部门id用于查询
    deptid.value = treeNode.id
    currentPage.value = 1
    // 参数：页数
    loaddata()
  }
}
const ztreeRightClick = (event: Event, treeId: string, treeNode: any) => {
  // if (vm.checkemploys.length > 0) {
  //   vm.$confirm('确定调整到该部门下？', '提示', {
  //     type: 'warning',
  //     callback: function (action, instance) {
  //       if (action == 'confirm') {
  //         $.ajax({
  //           url: __rootPath + 'cpsn/employtree/updateBatchDept',
  //           data: {
  //             sguid: vm.checkemploys.join(','), // 被调整部门id数组
  //             sguiddept: treeNode.id // 目标部门id
  //           },
  //           dataType: 'json',
  //           success: function () {
  //             for (var n in vm.employ) {
  //               if (vm.checkemploys.join(',').indexOf(vm.employ[n].sguid) != -1) {
  //                 // 及时修改表格部门
  //                 vm.employ[n].deptnames = treeNode.name
  //               }
  //             }
  //             vm.initableload()
  //             vm.$message({
  //               type: 'success',
  //               message: '调整成功!'
  //             })
  //           }
  //         })
  //       }
  //     }
  //   })
  // }
}
/** ztree配置参数 */
const treedata: ISetting = {
  async: {
    enable: true,
    type: 'get',
    url: import.meta.env.VITE_APP_BASE_API + '/system/dept/getSysEmpDeptTree',
    otherParam: {
      showRoot: true
    }
  },
  check: {
    enable: false,
    chkStyle: 'checkbox',
    chkboxType: {
      Y: '',
      N: ''
    }
  },
  view: {
    addHoverDom: ztreeAddHoverDom,
    removeHoverDom: ztreeRemoveHoverDomfunction,
    showLine: false,
    selectedMulti: false,
    showIcon: false,
    fontCss: function (treeid, treeNode) {
      return treeNode.buse
        ? { color: '#F56C6C', 'font-weight': '600' }
        : { color: '#606266', 'font-weight': '600' }
    }
  },
  edit: {
    enable: false, // 单独设置为true时，可加载修改、删除图标
    removeTitle: '删除节点',
    renameTitle: '编辑节点名称',
    editNameSelectAll: true,
    showRemoveBtn: true,
    showRenameBtn: true
  },
  data: {
    key: {
      children: 'childrenNodes'
    },
    simpleData: {
      enable: true,
      idKey: 'id', // id编号命名
      pIdKey: 'parentId' // 父id编号命名
    }
  },
  callback: {
    onAsyncSuccess: initAsyncSuccess,
    onClick: ztreeClick,
    onRightClick: ztreeRightClick
  }
}

onMounted(() => {
  /**
   * 计算高度
   */
  tableheight.value = computTableHeight()
  treeheight.value = computTreeHeight()
  hgZtreeObj = $.fn.zTree.init!($('#empztreeid'), treedata, [])
  getDutiesCombo()
  getRoleCombo()
})
/**
 * @description: 获取职务下拉
 * @return {*}
 */
const getDutiesCombo = () => {
  getDutiesComboApi().then(({ success, obj }: IAxios<IDataCombo[]>) => {
    if (success) {
      dutiesCombo.value = obj
    }
  })
}
/**
 * @description: 获取角色下拉
 * @return {*}
 */
const getRoleCombo = () => {
  getRoleComboApi().then(({ success, obj }: IAxios<IDataCombo[]>) => {
    if (success) {
      roleCombo.value = obj
    }
  })
}

/**
 * 隶属部门树返回函数
 */
const onselectionztree = (val: TSelectZtree[]) => {
  employForm.value.deptid = val.map((item) => item.id).join(',')
}
/**
 * @description: 兼职树返回
 * @param {*} val
 * @return {*}
 */
const onParttimeSelection = (val: TSelectZtree[]) => {
  employForm.value.parttimedeptid = val.map((item) => item.id).join(',')
  employForm.value.snameparttimedept = val.map((item) => item.name).join(',')
}
/**
 * 选择管理员类型
 * 选择的类型是管理员并且是空的时候默认隶属部门
 */
const onBadminflag = () => {
  if (employForm.value.badminflag === 1) {
    if (smanagedeptsid.value?.length < 1) {
      smanagedeptsid.value = [employForm.value.deptid]
    }
  } else {
    if (!employForm.value.id) {
      smanagedeptsid.value = []
    }
  }
}
/**
 * @description: 管理员部门返回
 * @param {*} val
 * @return {*}
 */
const onSmanageDeptSelection = (val: TSelectZtree[]) => {
  employForm.value.smanagedepts = val.map((item) => item.id).join(',')
}

/**
 * @description: 获取菜单列表
 * @return {*}
 */
const loaddata = () => {
  const tqEmployVO: TQEmployVO = {
    deptid: deptid.value === '0' ? undefined : deptid.value,
    ...queryInfo.value
  }
  tqEmployVO.page = currentPage.value
  tqEmployVO.pageSize = pageSize.value
  getEmployPageListApi(tqEmployVO).then((res: IAxios<TSEmployVO[]>) => {
    if (res.success) {
      userList.value = res.obj
      total.value = res.total
    }
  })
}
/**
 * @description: 搜索按钮操作
 * @return {*}
 */
const handleQuery = () => {
  loaddata()
}
/**
 * 提交
 */
const onSubmitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid: any) => {
    if (valid) {
      const data: TEmployDTO = { ...employForm.value }

      if (data.spassword === data.sloginname) {
        modal.msgError('用户名不能与密码相同')
        return
      }
      if (data.parttimedeptid && data.parttimedeptid.indexOf(data.deptid) >= 0) {
        modal.msgError('兼职部门不能选择自己所在部门')
        return
      }
      if (data.badminflag === 1 && !data.smanagedepts) {
        modal.msgError('管理部门不能为空')
        return
      }
      if (oldpassword.value !== data.spassword) {
        data.spassword = sm2Encrypt(data.spassword)
      }
      data.sphone = sm4Encrypt(data.sphone)
      if (data.semail) {
        data.semail = sm4Encrypt(data.semail)
      }
      // 内部人员
      data.binner = true
      data.roleIds = employRoleIds.value.join(',')
      if (data.dutiesid) {
        const duties = dutiesCombo.value.filter((item) => item.id === data.dutiesid)
        data.sdutiesname = duties.length > 0 ? duties[0].name : ''
      }
      if (data.id) {
        updateEmployApi(data).then(({ success }: IAxios) => {
          if (success) {
            modal.msgSuccess('修改成功')
            dialogVisible.value = false
            loaddata()
          }
        })
      } else {
        saveEmployApi(data).then(({ success }: IAxios) => {
          if (success) {
            modal.msgSuccess('保存成功')
            dialogVisible.value = false
            loaddata()
          }
        })
      }
    }
  })
}
/**
 * @description: 编辑取消
 * @param {*} formEl
 * @return {*}
 */
const handleCancel = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
  dialogVisible.value = false
}
/**
 * @description: 点击修改
 * @param {*} row
 * @return {*}
 */
const handleUpdate = (row: TSEmployVO) => {
  if (!row.id) {
    modal.msgError('请选择人员')
    return
  }
  getEmployInfoByIdApi(row.id).then(({ success, obj }: IAxios<TSEmployVO>) => {
    if (success) {
      treeSelectid.value = obj.parttimedeptid ? obj.parttimedeptid.split(',') : []
      smanagedeptsid.value = obj.smanagedepts ? obj.smanagedepts.split(',') : []
      employRoleIds.value = obj.roleIds ? obj.roleIds.split(',') : []
      dialogVisible.value = true
      nextTick(() => {
        employForm.value = { ...obj } as TEmployDTO
        if (employForm.value.sphone) {
          employForm.value.sphone = sm4Decrypt(employForm.value.sphone)
        }
        if (employForm.value.semail) {
          employForm.value.semail = sm4Decrypt(employForm.value.semail)
        }
        oldpassword.value = employForm.value.spassword
      })
    }
  })
}
/**
 * @description: 设置角色
 * @return {*}
 */
const handleEmpRoles = () => {
  const ids: (string | undefined)[] = multipleSelection.value?.map((item) => item.id)
  if (ids.length < 1) {
    modal.msgWarning(`请选的需要设置角色的人员`)
    return false
  }
  roleCheckList.value = []
  roleDialogVisible.value = true
}
/**
 * @description: 提交设置角色
 * @return {*}
 */
const onSubmitFormRole = () => {
  const empIds = multipleSelection.value?.map((item) => item.id).join(',')
  const roleIds = roleCheckList.value.join(',')
  if (!roleIds) {
    modal.msgError('请选择角色')
    return
  }
  updateBatchEmpRolesApi(empIds, roleIds).then(({ success }: IAxios) => {
    if (success) {
      modal.msgSuccess('设置成功')
      loaddata()
      roleDialogVisible.value = false
    }
  })
}
/**
 * @description: 禁用启用
 * @param {*} val
 * @param {*} row
 * @return {*}
 */
const handleBuse = (val: boolean, row?: TSEmployVO) => {
  const ids: (string | undefined)[] = []
  const busemsg = val ? '启用' : '禁用'
  if (row) {
    ids.push(row.id)
  } else {
    multipleSelection.value?.forEach((item) => {
      ids.push(item.id)
    })
  }
  if (ids.length < 1) {
    modal.msgWarning(`请选的需要${busemsg}的信息`)

    return false
  }

  modal.confirm(`是否${busemsg}选中人员？`).then(() => {
    disableEmpApi(ids.join(','), val).then(({ success }: IAxios) => {
      if (success) {
        modal.msgSuccess(`${busemsg}成功`)
        loaddata()
      }
    })
  })
}
/**
 * @description: 删除
 * @param {*} row
 * @return {*}
 */
const handleDelete = (row: TSEmployVO) => {
  const id = row.id
  if (!id) {
    modal.msgWarning('请选的需要删除的信息')
    return false
  }
  modal.confirm('是否删除选中数据？').then(() => {
    deleteEmpApi(id).then(({ success }: IAxios) => {
      if (success) {
        modal.msgSuccess('删除成功')
        loaddata()
      }
    })
  })
}

/**
 * @description: 获取最大序号
 * @return {*}
 */
const getMaxIsort = async () => {
  await getMaxIsortApi().then(({ success, obj }: IAxios) => {
    if (success) {
      employForm.value.isort = obj
    }
  })
}

/**
 * 改变页码总数时
 */
const handleSizeChange = (val: number) => {
  pageSize.value = val
  loaddata()
}
/**
 * 改变页数时
 * @param val
 */
const handleCurrentChange = (val: number) => {
  currentPage.value = val
  loaddata()
}
/**
 * 排序
 * @param val
 */
const sortChange = (val: any) => {
  queryInfo.value.sort = val.prop
  if (val.order === 'ascending') {
    queryInfo.value.order = 'asc'
  } else if (val.order === 'descending') {
    queryInfo.value.order = 'desc'
  } else {
    queryInfo.value.order = 'desc'
    queryInfo.value.sort = 'isort'
  }
  loaddata()
}
/**
 * 列表多选
 * @param val
 */
const handleSelectionChange = (val: TSEmployVO[]) => {
  multipleSelection.value = val
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'emp'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>

<style scoped lang="scss">
.left_box {
  height: v-bind(treeheight);
}
</style>
