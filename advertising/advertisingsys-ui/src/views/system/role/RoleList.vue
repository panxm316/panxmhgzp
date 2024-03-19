<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button type="primary" icon="Plus" @click="handleAddRole">新增</el-button>
        <el-button type="primary" icon="DocumentCopy" @click="handleRoleCopy">复制</el-button>
        <el-button type="primary" icon="CopyDocument" @click="handleRoleManage()">操作</el-button>
        <el-button type="success" icon="Edit" @click="handleUpdateRole()">修改</el-button>
        <el-button type="danger" icon="Delete" @click="handleDeleteRole()">删除</el-button>
        <el-input
          v-model="queryInfo.queryKey"
          placeholder="请输入菜单名称"
          clearable
          style="width: 200px"
          @clear="getRolePageList"
          @keyup.enter="getRolePageList"
        />
        <el-button type="primary" icon="Search" @click="getRolePageList">搜索</el-button>
        <!-- <el-button icon="Refresh" @click="handleReset">重置</el-button> -->
        <el-tooltip class="item" effect="dark" content="帮助" placement="top">
          <el-button class="help" type="text" @click="showhelp()">?</el-button>
        </el-tooltip>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="roleTableRef"
        :data="gridData"
        row-key="id"
        :height="tableHeight"
        :border="true"
        stripe
        :header-cell-style="tableHeaderColor"
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        @row-click="handleRowClick"
      >
        <el-table-column type="selection" :width="50" align="center"></el-table-column>
        <el-table-column prop="sname" label="角色名称" :width="120" sortable="custom">
        </el-table-column>
        <el-table-column
          prop="deptNames"
          label="所属单位"
          :width="180"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column prop="menuNames" label="拥有菜单">
          <template #default="scope">
            <el-tooltip
              v-if="scope.row.menuNames.length > 50"
              class="item"
              effect="dark"
              placement="top"
            >
              <template #content>
                <div style="max-width: 700px">
                  {{ scope.row.menuNames }}
                </div>
              </template>
              <div class="oneLine">{{ scope.row.menuNames }}</div>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column prop="isort" label="序号" :width="80" sortable="custom" align="center">
        </el-table-column>
        <el-table-column prop="buse" label="启用" :width="80" sortable="custom" align="center">
          <template #default="scope">
            <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="280"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button link type="success" icon="Edit" @click="handleUpdateRole(scope.row)"
              >修改</el-button
            >
            <el-button link type="primary" icon="CopyDocument" @click="handleRoleManage(scope.row)"
              >操作</el-button
            >
            <el-button link type="danger" icon="User" @click="handleRoleEmploy(scope.row)"
              >人员</el-button
            >
            <el-button link type="danger" icon="Delete" @click="handleDeleteRole(scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <!-- 页码 -->
      <el-pagination
        small
        layout="total, sizes, prev, pager, next, jumper"
        class="pagination"
        :current-page="queryInfo.page"
        :page-size="queryInfo.pageSize"
        :page-sizes="paginationInfo.pageSizes"
        :total="paginationInfo.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <el-dialog
      v-model="roleDialogVisible"
      title="编辑角色"
      :close-on-click-modal="false"
      width="1000"
      align-center
      @open="handleOpenDialog"
    >
      <el-form ref="roleFormRef" :model="roleForm" label-width="100px" :rules="rules">
        <el-row :gutter="50">
          <el-col :span="15" style="margin-top: 40px">
            <el-form-item label="角色名称" prop="sname">
              <el-input v-model="roleForm.sname"></el-input>
            </el-form-item>
            <el-row>
              <el-form-item label="序号" prop="isort">
                <el-input-number v-model="roleForm.isort" :min="1" :max="9999"></el-input-number>
              </el-form-item>
            </el-row>

            <el-form-item label="所有权限" prop="ball">
              <el-checkbox v-model="roleForm.ball" label=""></el-checkbox>
            </el-form-item>

            <el-form-item label="启用" prop="buse">
              <el-checkbox v-model="roleForm.buse"></el-checkbox>
            </el-form-item>

            <el-form-item label="描述" prop="sdesc">
              <el-input v-model="roleForm.sdesc" type="textarea" :rows="3"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="9">
            <el-row :gutter="20">
              <el-col :span="24">
                <strong style="line-height: 40px">菜单权限:</strong>
                <el-checkbox
                  v-model="selectAllMenu"
                  style="margin-left: 30px"
                  @change="handleChangeSelectAllMenu"
                  >全选</el-checkbox
                >
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <div
                id="menuid"
                style="overflow: auto; height: 450px; border: solid 1px #dcdfe6; width: 100%"
                class="ztree"
              ></div>
            </el-row>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button icon="Close" @click="handleCloseMenuDialog()">取 消</el-button>
          <el-button type="primary" icon="Check" @click="handleSaveRole">保 存 </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { FormRules, ElTable } from 'element-plus'
import {
  getRolePageListApi,
  getMaxSortApi,
  saveRoleApi,
  deleteRoleIdApi,
  updateRoleApi,
  saveCopyRoleApi,
  getRoleByIdApi
} from '@/api/system/role'
import type { TQueryInfo, TPagination } from '@/types/common/index'
import type { TRolePage, TRoleVO, TRoleDTO } from '@/types/views/system/role'
import { required, computTableHeight, tableHeaderColor } from '@/utils/index'
import { IAxios } from 'axios'
import modal from '@/plugins/modal'
import { MessageBox } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
const router = useRouter()
defineOptions({
  name: 'RoleList'
})

const rolePage: TRolePage = {
  roleId: '',
  roleName: '',
  pageIndex: 0,
  compName: 'RoleManage'
}
const props = withDefaults(defineProps<{ params: TRolePage }>(), {
  params: () => {
    return { roleId: '', roleName: '', pageIndex: 0, compName: 'RoleManage' }
  }
})
const emit = defineEmits<{
  selectRolePage: [data: { params: TRolePage }]
}>()

/**
 * 菜单树加载成功方法
 */
const initAsyncSuccess = () => {
  $.fn.zTree.getZTreeObj!('menuid').expandAll(true)
  var ztreeObj = $.fn.zTree.getZTreeObj!('menuid')
  if (roleForm.value.id !== undefined) {
    // 修改
    roleForm.value.menuIds.split(',').forEach(function (id) {
      const node = ztreeObj.getNodeByParam('id', id, undefined)
      if (node) {
        // 当前节点打勾或去勾
        ztreeObj.checkNode(node, true, false)
      }
    })
  } else {
    ztreeObj.checkAllNodes(false)
  }
}

/**
 * ztree点击事件
 */
const ztreeClick = () => {
  const ztreeObj = $.fn.zTree.getZTreeObj!('menuid')
  const nodes = ztreeObj.getSelectedNodes(false)
  for (let i = 0, l = nodes.length; i < l; i++) {
    const ischecked = !nodes[i].checked
    // 当前节点打勾或去勾
    ztreeObj.checkNode(nodes[i], ischecked, false)
  }
}
/**
 * ztreeOnCheck
 * @param event
 * @param treeId
 * @param treeNode
 */
const ztreeOnCheck = (event: Event, treeId: string, treeNode: ITreeNode) => {
  checkNode(treeNode, treeNode.checked)
}

/**
 * //递归打勾或去勾 加入或移除节点数组
 * @param node
 * @param checked
 */
const checkNode = (node: any, checked?: boolean) => {
  const ztreeObj = $.fn.zTree.getZTreeObj!('menuid')
  if (node.isParent) {
    ztreeObj.checkNode(node, checked, false)
    ztreeObj.expandNode(node, true, false, false)
    // self.deptIdArrayAddRemove(node.id, checked);
    node.children.forEach((_node: any) => {
      checkNode(_node, checked)
    })
  } else {
    ztreeObj.checkNode(node, checked, false)
    // deptIdArrayAddRemove(node.id, checked);
    return
  }
}
/**
 * ztree属性
 */
const treeData: ISetting = {
  async: {
    enable: true,
    type: 'get',
    url: import.meta.env.VITE_APP_BASE_API + '/system/menu/getMenuTree',
    otherParam: {}
  },
  check: {
    enable: true,
    chkStyle: 'checkbox',
    chkboxType: {
      Y: 'ps',
      N: 'ps'
    }
  },
  view: {
    showLine: false,
    selectedMulti: false,
    showIcon: false,
    fontCss: function (treeid, treeNode) {
      return treeNode.buse
        ? { color: '#F56C6C', 'font-weight': '600' }
        : { color: '#606266', 'font-weight': '600' }
    }
  },
  data: {
    key: {
      children: 'childrenNodes'
    },
    simpleData: {
      enable: true,
      idKey: 'id', // id编号命名
      pIdKey: 'parentId', // 父id编号命名
      rootPId: 0
    }
  },
  callback: {
    onAsyncSuccess: initAsyncSuccess,
    onClick: ztreeClick,
    onCheck: ztreeOnCheck
  }
}

/**
 * 查询对象
 */
const queryInfo = ref<TQueryInfo>({
  sort: 'isort',
  order: 'desc',
  pageSize: 15,
  page: 1,
  queryKey: ''
})

/**
 * 分布对象
 */
const paginationInfo = ref<TPagination>({
  pageSizes: [15, 20, 30, 40],
  total: 0
})
/**
 * 角色列表
 */
const gridData = ref<TRoleVO[]>([])
/** 角色编辑dialog */
const roleDialogVisible = ref(false)
/** 选择全部菜单 */
const selectAllMenu = ref(false)
/**
 * 角色选择
 */
const multipleSelection = ref<TRoleVO[]>([])
const roleTableRef = ref<InstanceType<typeof ElTable>>()
const roleFormRef = ref<InstanceType<typeof ElForm>>()
/** 角色初值 */
const roleDTO: TRoleDTO = {
  ball: false,
  bselfrole: false,
  buse: true,
  id: '',
  isort: 0,
  menuIds: '',
  sdesc: '',
  sname: '',
  iroletype: null,
  employId: ''
}
/**
 * 角色表单
 */
const roleForm = ref<TRoleDTO>({ ...roleDTO })

const rules = reactive<FormRules<TRoleDTO>>({
  sname: [
    { validator: required, required: true, message: '请输入角色名称', trigger: 'blur' },
    { max: 200, message: '不得大于200个字符', trigger: 'blur' }
  ],
  sdesc: [{ max: 500, message: '描述长度在500字以内' }]
})

const tableHeight = ref(0)

onMounted(() => {
  tableHeight.value = computTableHeight()
  getRolePageList()
})

const handleReset = () => {
  queryInfo.value.queryKey = ''
}

/**
 * 列表选择
 * @param rows
 */
const handleSelectionChange = (rows: TRoleVO[]) => {
  multipleSelection.value = rows
}
/**
 * 列表排序
 * @param val
 */
const handleSortChange = (val: { prop: string; order: string }) => {
  queryInfo.value.sort = val.prop
  if (val.order === 'ascending') {
    queryInfo.value.order = 'asc'
  } else {
    queryInfo.value.order = 'desc'
  }
  getRolePageList()
}
/**
 * RowClick
 * @param row
 */
const handleRowClick = (row: TRoleVO) => {
  roleTableRef.value?.toggleRowSelection(row, true)
}
/**
 * 分页当前页
 * @param pageIndex
 */
const handleCurrentChange = (pageIndex: number) => {
  queryInfo.value.page = pageIndex
  getRolePageList()
}
/**
 * 改变每页显示条数
 * @param val
 */
const handleSizeChange = (val: number) => {
  queryInfo.value.pageSize = val
  getRolePageList()
}
/**
 * 获取角色列表
 */
const getRolePageList = () => {
  if (props.params.pageIndex) {
    queryInfo.value.page = props.params.pageIndex
  }
  getRolePageListApi(queryInfo.value).then(({ success, obj, total }: IAxios<TRoleVO[]>) => {
    if (success) {
      paginationInfo.value.total = total
      gridData.value = obj
      if (props.params.roleId) {
        const selectRole = gridData.value.find((r) => r.id === props.params.roleId)
        if (selectRole) {
          nextTick(() => {
            roleTableRef.value?.toggleRowSelection(selectRole, true)
            roleTableRef.value?.setCurrentRow(selectRole)
          })
        }
        props.params.roleId = ''
        props.params.roleName = ''
        props.params.pageIndex = 0
      }
    }
  })
}
/**
 * 角色删除
 */
const handleDeleteRole = (row?: TRoleVO) => {
  const ids: string[] = []
  if (row) {
    ids.push(row.id)
  } else {
    multipleSelection.value.forEach((r) => ids.push(r.id))
  }
  if (ids.length === 0) {
    modal.msgWarning('请选择你要删除的记录！')
    return
  }

  modal
    .confirm('是否删除该记录?')
    .then(() => {
      deleteRoleIdApi(ids.join(',')).then(({ success, msg }: IAxios) => {
        if (success) {
          modal.msgSuccess('操作成功')
          props.params.roleId = ''
          props.params.pageIndex = 0
          getRolePageList()
        } else {
          // modal.msgError(msg)
        }
      })
    })
    .catch(() => {})
}

/**
 * 新增角色
 */
const handleAddRole = () => {
  roleForm.value = { ...roleDTO }
  roleTableRef.value?.clearSelection()
  roleFormRef.value?.resetFields()
  roleFormRef.value?.clearValidate()
  multipleSelection.value = []
  roleDialogVisible.value = true
  // 获取角色最大序号
  getMaxSortApi().then(({ success, obj }: IAxios<number>) => {
    if (success) {
      roleForm.value.isort = obj
    }
  })
}

/**
 * 角色修改
 */
const handleUpdateRole = (row?: TRoleVO) => {
  if (!row && multipleSelection.value.length !== 1) {
    modal.msgWarning('请选择一项进行操作！')
    return
  }
  roleForm.value = row ? { ...row } : { ...multipleSelection.value[0] }
  getRoleByIdApi(roleForm.value.id).then(({ success, obj }: IAxios<TRoleDTO>) => {
    if (success) {
      roleForm.value = obj
      props.params.pageIndex = queryInfo.value.page!
      props.params.roleId = roleForm.value.id
      roleDialogVisible.value = true
    }
  })
}
/**
 * 操作
 */
const handleRoleManage = (row?: TRoleVO) => {
  if (!row && multipleSelection.value.length !== 1) {
    modal.msgWarning('请选择一项进行操作！')
    return
  }

  props.params.pageIndex = queryInfo.value.page!
  props.params.roleId = row ? row.id : multipleSelection.value[0].id
  props.params.roleName = row ? row.sname : multipleSelection.value[0].sname
  props.params.compName = 'RoleManage'
  emit('selectRolePage', props)
}
/**
 * 角色人员
 * @param row
 */
const handleRoleEmploy = (row?: TRoleVO) => {
  if (!row) {
    modal.msgWarning('请选择一项进行操作！')
    return
  }
  props.params.pageIndex = queryInfo.value.page!
  props.params.roleId = row ? row.id : multipleSelection.value[0].id
  props.params.roleName = row ? row.sname : multipleSelection.value[0].sname
  props.params.compName = 'RoleEmploy'
  emit('selectRolePage', props)
}

/**
 * 角色复制
 */
const handleRoleCopy = () => {
  if (multipleSelection.value.length !== 1) {
    modal.msgWarning('请选择一项进行操作！')
    return
  }
  ElMessageBox.prompt('请输入角色名称', '系统提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputValue: multipleSelection.value[0].sname,
    inputPattern: /\S/,
    inputErrorMessage: '角色名称不能为空'
  })
    .then((messageBoxData) => {
      saveCopyRoleApi({
        roleId: multipleSelection.value[0].id,
        roleName: messageBoxData.value.trim()
      }).then(({ success, msg }: IAxios) => {
        if (success) {
          modal.msgSuccess('操作成功')
          getRolePageList()
        }
      })
    })
    .catch(() => {})
}

/**
 * 编辑角色dialog
 */
const handleOpenDialog = () => {
  selectAllMenu.value = false
  nextTick(() => {
    $.fn.zTree.init!($('#menuid'), treeData, [])
  })
}
/**
 * 选择全部菜单
 */
const handleChangeSelectAllMenu = () => {
  const ztreeObj = $.fn.zTree.getZTreeObj!('menuid')
  if (selectAllMenu.value) {
    ztreeObj.checkAllNodes(true)
  } else {
    ztreeObj.checkAllNodes(false)
  }
}
/**
 * 关闭角色编辑
 */
const handleCloseMenuDialog = () => {
  roleDialogVisible.value = false
}
/**
 * 保存编辑角色
 */
const handleSaveRole = () => {
  roleFormRef.value?.validate(async (valid) => {
    if (valid) {
      const ztreeObj = $.fn.zTree.getZTreeObj!('menuid')
      const nodes = ztreeObj.getCheckedNodes(true)
      const ids: string[] = []
      roleForm.value.menuIds = ''
      for (let i = 0; i < nodes.length; i++) {
        if (!nodes[i].iconSkin) {
          ids[i] = nodes[i].id
        }
      }
      const half = ztreeObj.getNodesByFilter(
        (node) => node.level === 0 && (node.getCheckStatus!() as any).half
      )
      half.forEach((h: ITreeNode) => {
        if (ids.findIndex((id) => id === h.id) < 0) {
          ids.push(h.id)
        }
      })

      if (ids.length === 0) {
        modal.msgWarning('请选择菜单权限')
        return false
      }
      roleForm.value.menuIds = ids.join(',')
      if (roleForm.value.id === '') {
        await saveRoleApi(roleForm.value).then(({ success, msg }: IAxios) => {
          if (success) {
            modal.msgSuccess('操作成功')
            roleDialogVisible.value = false
          } else {
            // modal.msgError(msg)
          }
        })
      } else {
        await updateRoleApi(roleForm.value).then(({ success, msg }: IAxios) => {
          if (success) {
            modal.msgSuccess('操作成功')
            roleDialogVisible.value = false
          } else {
            // modal.msgError(msg)
          }
        })
      }
      getRolePageList()
    }
  })
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'role'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>

<style scoped></style>
