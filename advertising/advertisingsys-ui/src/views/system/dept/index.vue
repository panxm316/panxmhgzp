<!--
 * @Author: wanghl suny
 * @Date: 2023-08-16 15:11:03
 * @LastEditTime: 2024-01-18 14:02:39
 * @LastEditors: suny suny@hgzp.com.cn
 * @Description:
-->
<template>
  <div id="dept" class="app-container">
    <el-row :gutter="5">
      <!--部门数据-->
      <el-col :xs="8" :sm="7" :md="7" :lg="5" :xl="5">
        <div class="table_box left_box">
          <div class="fenge-box">
            <el-icon color="#409EFC"><star-filled /></el-icon><span>部门列表</span
            ><el-icon color="#409EFC"><star-filled /></el-icon>
          </div>
          <div class="head-container">
            <el-input
              v-model="queryKey"
              clearable
              placeholder="请输入部门关键字"
              class="input-with-select"
              style="margin-bottom: 20px"
              @keyup.enter="searchDept"
              @clear="searchDept"
            >
              <template #append>
                <el-button icon="Search" @click="searchDept" />
              </template>
            </el-input>
          </div>
          <div id="scrolly">
            <div id="deptztreeid" class="ztree"></div>
          </div>
        </div>
      </el-col>
      <!-- 用户数据 -->
      <el-col :xs="16" :sm="17" :md="17" :lg="19" :xl="19">
        <div class="search_box">
          <el-button type="primary" icon="Upload" @click="openImportDialog">导入</el-button>
          <el-tooltip class="item" effect="dark" content="帮助" placement="top">
            <el-button class="help" type="text" @click="showhelp()">?</el-button>
          </el-tooltip>
        </div>
        <div class="table_box edit_box">
          <el-form ref="deptRef" :model="formData" :rules="rules" label-width="100px">
            <el-row :gutter="50">
              <el-col :span="14" :offset="4">
                <el-form-item label="隶属部门" prop="parentid" style="width: 400px">
                  <HgZtreeSelect
                    :selectids="formData.parentid === '' ? [] : [formData.parentid]"
                    :treeparams="{
                      showRoot: adminFlag !== 1,
                      excludeIds: selectNodeIds
                    }"
                    @selectionztree="selectionZtree"
                  ></HgZtreeSelect>
                </el-form-item>
                <el-form-item label="部门名称" prop="sdeptname">
                  <el-input v-model="formData.sdeptname" :disabled="bFormEditFlag"></el-input>
                </el-form-item>
                <el-form-item label="别名" prop="sdeptalias">
                  <el-input v-model="formData.sdeptalias" :disabled="bFormEditFlag"></el-input>
                </el-form-item>
                <el-form-item label="序号" prop="isort">
                  <el-input-number
                    v-model="formData.isort"
                    :min="1"
                    :max="10000"
                    label="序号"
                    :disabled="bFormEditFlag"
                  ></el-input-number>
                </el-form-item>
                <!-- <el-form-item label="域组织映射" prop="sndomainou">
                  <el-input v-model="formData.sndomainou" :disabled="bFormEditFlag"></el-input>
                </el-form-item>
                <el-form-item label="cas部门id" prop="sguidcas">
                  <el-input v-model="formData.sguidcas" :disabled="bFormEditFlag"></el-input>
                </el-form-item> -->
                <el-row :gutter="50">
                  <!-- <el-col :span="8">
                    <el-form-item label="默认角色标志" prop="bflagrole">
                      <el-checkbox
                        v-model="formData.bflagrole"
                        :disabled="bFormEditFlag"
                      ></el-checkbox>
                    </el-form-item>
                  </el-col> -->
                  <el-col :span="8">
                    <el-form-item label="启用" prop="buse">
                      <el-checkbox v-model="formData.buse" :disabled="bFormEditFlag"></el-checkbox>
                    </el-form-item>
                  </el-col>
                  <!-- <el-col :span="8">
                    <el-form-item label="导航节点" prop="bflagroot">
                      <el-checkbox
                        v-model="formData.bflagroot"
                        :disabled="bFormEditFlag"
                      ></el-checkbox>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="内外部门" prop="binner">
                      <el-checkbox
                        v-model="formData.binner"
                        :disabled="bFormEditFlag"
                      ></el-checkbox>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="cas部门推送" prop="bcaspush">
                      <el-checkbox
                        v-model="formData.bcaspush"
                        :disabled="bFormEditFlag"
                      ></el-checkbox>
                    </el-form-item>
                  </el-col> -->
                </el-row>
                <el-form-item>
                  <el-button
                    type="primary"
                    icon="Check"
                    :disabled="bFormEditFlag && checkTreeNodes.length > 0"
                    @click="submitForm"
                    >确 定</el-button
                  >
                  <el-button
                    :disabled="bFormEditFlag && checkTreeNodes.length > 0"
                    icon="close"
                    @click="onReset"
                    >取 消</el-button
                  >
                  <el-button
                    type="danger"
                    :style="{ display: checkTreeNodes.length < 1 ? 'none' : 'block' }"
                    :disabled="bFormEditFlag && checkTreeNodes.length < 1"
                    icon="delete"
                    @click="batchDeleteDept"
                    >删 除</el-button
                  >
                  <el-button
                    type="warning"
                    :style="{ display: checkTreeNodes.length < 1 ? 'none' : 'block' }"
                    icon="Switch"
                    :disabled="bFormEditFlag && checkTreeNodes.length < 1"
                    @click="batchUpdateDept"
                    >批量调整部门</el-button
                  >
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </el-col>
    </el-row>
    <el-dialog v-model="importDeptAndEmpDialog" title="部门人员导入" width="680px" append-to-body>
      <div>
        <el-row>
          <el-col :span="4">
            <a :href="downFileUrl" download="deptemp.xlsx" style="color: #67c23a">
              <el-icon><Download /></el-icon>下载模板
            </a>
          </el-col>
          <el-col :span="16">
            <HgFileUpload
              :server="hgfileuploadparam.server"
              :accept="hgfileuploadparam.accept"
              :multiple="false"
              :filenumlimit="1"
              :storytypes="hgfileuploadparam.storytypes"
              @getupfile="getUpFile"
            ></HgFileUpload>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { getCurrentInstance, ref, reactive, toRefs, nextTick } from 'vue'
const { proxy } = getCurrentInstance() as any
import {
  getSysDeptTreeApi,
  saveDeptApi,
  updateDeptApi,
  getParentDeptBuseApi,
  deleteDeptApi,
  getDeptInfoByIdApi,
  getMaxIsortApi,
  batchUdpateDeptApi
} from '@/api/system/dept'
import { TbatchDeptData, TsystemDept } from '@/types/views/system/dept'
import useUserStore from '@/store/modules/user'
import { IAxios } from 'axios'
import { TSelectZtree } from '@/types/common'
import { FormRules, UploadInstance, UploadProps, UploadRawFile, genFileId } from 'element-plus'
import { THgFileUploadParam, TUpFile } from '@/types/components/hgfileupload'
import { computEditHeight, computTreeHeight } from '@/utils/index'
import { useRouter } from 'vue-router'
const router = useRouter()
defineOptions({
  name: 'Dept'
})
/** 用户身份信息 */
const { user } = useUserStore()
/** 表格高度 */
const editheight = ref('')
/** 树高度 */
const treeheight = ref('')
/** 用户是否超管 */
const adminFlag = user?.adminflag
/** 用户管理部门列表 */
const smanagedepts = user?.smanagedepts
/** ztree对象 */
let ztreeObj: any
/** 编辑节点时的父节点 */
let parentNode: ITreeNode
/** 验证规则 */
const rules = reactive<FormRules<TsystemDept>>({
  sdeptname: [
    { required: true, message: '请输入部门名称', trigger: 'blur' },
    { min: 2, max: 15, message: '部门名称长度在2-15个字符之间', trigger: 'blur' }
  ]
})
/** 远程搜索的数组 */
const remoteData = ref<TSelectZtree[]>([])
/** 查询参数 */
const remoteDept = ref<string[]>([])
/** 点击或者勾选的部门id数组 */
const selectNodeIds = ref('')
/** 被勾选的节点 */
const checkTreeNodes = ref<ITreeNode[]>([])
/** 导入部门人员窗口 */
const importDeptAndEmpDialog = ref(false)
/** form允许编辑状态 */
const bFormEditFlag = ref(true)
/** 查询部门关键字 */
const queryKey = ref('')
/** 原始parentid */
let oldParentId: string = ''
/** 导入部门Ref */
const uploadRef = ref<UploadInstance>()
/** excel模板下载地址 */
const downFileUrl = new URL('@public/static/file/deptemp.xlsx', import.meta.url).href
/** 上传后返回的文件列表 */
const responsedata = ref<TUpFile[]>()
/** 上传组件参数 */
const hgfileuploadparam = ref<THgFileUploadParam>({
  server: import.meta.env.VITE_APP_BASE_API + '/system/dept/importDeptAndEmpData',
  accept: {
    title: 'Excel',
    extensions: 'xlsx,xls',
    mimeTypes: '.xlsx,.xls'
  },
  multiple: false,
  filenumlimit: 1,
  storytypes: []
})
/** 所选部门最深子部门级别 */
let childLevel: number = 0
/** 所选部门与最深子部门级别差 */
let numbLevel: number = 0

/** 部门编辑数据 */
const formData = ref<TsystemDept>({
  id: '',
  sdeptname: '',
  parentid: '',
  idepth: 1,
  sndomainou: '',
  bflagrole: true,
  bflagroot: false,
  sdeptalias: '',
  binner: true,
  sguidcas: '',
  bcaspush: false,
  idataversion: 0,
  isort: 1,
  buse: true
})

/**
 * 在节点上显示添加节点图标按钮
 * @param {string} treeId 树id
 * @param {ITreeNode} treeNode 鼠标移动上的节点
 * @returns 显示添加按钮
 */
const addHoverDom = (treeId: string, treeNode: ITreeNode) => {
  if (treeNode.level === 0 || treeNode.nocheck || smanagedepts!.indexOf(treeNode.id) > -1) {
    $('.remove')?.remove()
  }
  if ((treeNode.level === 0 && adminFlag === 1) || treeNode.nocheck) {
    $('.edit').hide
    return
  }
  // if (treeNode.level === 0 && treeNode.children && treeNode.children.length > 0) {
  //   $('.edit')?.hide
  //   return
  // }
  if (treeNode.level! < 5) {
    // 5级节点限制添加按钮
    const sObj = $('#' + treeNode.tId + '_span') // 获取节点信息
    if (treeNode.editNameFlag || $('#addBtn_' + treeNode.tId).length > 0) return

    const addStr =
      "<span class='button add' id='addBtn_" +
      treeNode.tId +
      "' title='添加子部门' onfocus='this.blur();'></span>" // 定义添加按钮
    sObj?.after(addStr) // 加载添加按钮
    const btn = $('#addBtn_' + treeNode.tId)
    // 绑定添加事件，并定义添加操作
    if (btn) {
      btn.on('click', (event: any) => {
        event.stopPropagation()
        onReset()
        bFormEditFlag.value = false
        parentNode = treeNode
        oldParentId = treeNode.id
        formData.value.parentid = treeNode.id
        formData.value.buse = false
        formData.value.idepth = treeNode.level !== undefined ? treeNode.level + 1 : 1
        getMaxIsortApi().then((res: IAxios) => {
          if (res.success) {
            formData.value.isort = res.obj
          }
        })
        if (treeNode.level === 0) {
          formData.value.buse = true
        } else {
          getParentDeptBuseApi({ id: treeNode.id }).then((res: IAxios) => {
            if (res.success) {
              formData.value.buse = res.obj
            }
          })
        }
      })
    }
  }
}
/**
 * 移除节点上显示的图标按钮
 * @param treeId
 * @param treeNode
 */
const removeHoverDom = (treeId: string, treeNode: ITreeNode) => {
  $('#addBtn_' + treeNode.tId)
    .unbind()
    .remove()
}

/**
 * 部门加载成功
 */
const onAsyncSuccess = () => {
  ztreeObj = $.fn.zTree.getZTreeObj!('deptztreeid')
  // 根节点id的0值改为字符串
  const noderoot = ztreeObj.getNodeByParam('id', 0, null)
  if (noderoot) {
    noderoot.id = noderoot.id.toString()
  }
  // 默认展开第一级节点
  const nodes = ztreeObj.getNodesByFilter((node: ITreeNode) => {
    return node.level === 1
  })
  nodes?.forEach((node: ITreeNode) => {
    ztreeObj.expandNode(node, true, false, true)
  })
  if (remoteDept.value.length > 0) {
    remoteDept.value.forEach((d) => {
      // 后台打勾节点展开
      const node = ztreeObj.getNodeByParam('id', d, null)
      ztreeObj.selectNode(node)
    })
  }
  if (formData.value.parentid !== '') {
    const treenode = ztreeObj.getNodeByParam('id', formData.value.parentid, null)
    ztreeObj.expandNode(treenode, true, false, true)
  }
}
/**
 * 单击部门节点
 * @param event
 * @param treeId
 * @param treeNode
 */
const onClick = (event: Event, treeId: string, treeNode: ITreeNode) => {
  // 清空勾选，隐藏调整按钮显示保存按钮
  ztreeObj.checkAllNodes(false)
  checkTreeNodes.value = []
  // 单击根节点返回
  if (treeNode.level === 0) {
    return
  }
  if (smanagedepts!.indexOf(treeNode.id) > -1 && adminFlag === 1) {
    ElMessage.warning('没有权限修改本级部门')
    return
  }
  // 点击树保存状态改为修改
  parentNode = treeNode.getParentNode!()
  oldParentId = parentNode.id
  selectNodeIds.value = treeNode.id
  if (treeNode.level !== 0) {
    bFormEditFlag.value = false

    getDeptInfoByIdApi({ id: treeNode.id }).then((res: IAxios) => {
      if (res.success) {
        formData.value = res.obj
        const pnode = treeNode.getParentNode!()
        if (pnode != null) {
          // 隶属部门赋值
          formData.value.parentid = pnode.id
        } else {
          formData.value.parentid = ''
        }
      }
    })
  }
}
/**
 * 勾选要批量删除、调整的部门
 * @param event
 * @param treeId
 * @param treeNode
 */
const onCheck = (event: Event, treeId: string, treeNode: ITreeNode) => {
  if (treeNode.id === '0') {
    treeNode.checked = false
    ElMessage.error('根节点不允许选择')
    return
  }
  if (smanagedepts!.indexOf(treeNode.id) > -1 && adminFlag === 1) {
    ElMessage.warning('没有权限修改本级部门')
    return
  }
  const ztree = $.fn.zTree.getZTreeObj!('deptztreeid')
  const nodes = ztree.getCheckedNodes(true)
  checkTreeNodes.value = nodes

  const arr = []
  for (const i in nodes) {
    arr.push(nodes[i].id)
  }
  selectNodeIds.value = arr.join(',')

  if (arr.length >= 1) {
    // 勾选多个节点，只显示调整和删除按钮
    bFormEditFlag.value = true
  } else {
    // 不选节点，显示保存和删除按钮
    bFormEditFlag.value = false
    onReset()
  }
}

/**
 * 删除部门
 * @param treeId
 * @param treeNode
 */
const beforeRemove = (treeId: string, treeNode: ITreeNode) => {
  // 删除前判断是否有子节点
  if (treeNode.childrenNodes && treeNode.childrenNodes.length > 0) {
    ElMessage.warning('请先删除子部门')
    return false
  } else {
    if (smanagedepts!.indexOf(treeNode.id) > -1 && adminFlag === 1) {
      ElMessage.error('无权删除一级部门')
      return false
    }

    var flag = false
    ElMessageBox.confirm('确定删除该部门?', '提示', {
      type: 'warning'
    })
      .then(() => {
        const data = {
          ids: treeNode.id
        }
        deleteDeptApi(data).then((res: IAxios) => {
          if (res.success) {
            onReset()

            const ztree = $.fn.zTree.getZTreeObj!('deptztreeid')
            ztree.removeNode(treeNode)

            ElMessage.success('删除成功!')
            ztree.refresh()
            // 删除后重置 批量按钮显示隐藏
            const nodes = ztree.getCheckedNodes(true)
            if (nodes.length >= 1) {
              // 勾选多个节点，只显示调整和删除按钮
              bFormEditFlag.value = true
            } else {
              // 勾选一个或不选节点，显示保存和删除按钮
              bFormEditFlag.value = false
            }
            flag = true
          }
        })
      })
      .catch(() => {})
    return flag
  }
}
/**
 * 树对象绑定
 */
const treeData: ISetting = {
  async: {
    enable: true,
    type: 'get',
    url: import.meta.env.VITE_APP_BASE_API + '/system/dept/getSysDeptTree',
    otherParam: {
      queryKey: '',
      showRoot: true
    }
  },
  check: {
    enable: true,
    chkStyle: 'checkbox',
    chkboxType: {
      Y: '',
      N: ''
    }
  },
  view: {
    addHoverDom: addHoverDom,
    removeHoverDom: removeHoverDom,
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
    // 单独设置为true时，可加载修改、删除图标
    enable: true,
    removeTitle: '删除节点',
    editNameSelectAll: false,
    showRemoveBtn: true,
    showRenameBtn: false
  },
  data: {
    key: {
      children: 'childrenNodes'
    },
    simpleData: {
      enable: true,
      idKey: 'id', // id编号命名
      pIdKey: 'parentId', // 父id编号命名
      rootPId: '0'
    }
  },
  callback: {
    onAsyncSuccess: onAsyncSuccess,
    onClick: onClick,
    onCheck: onCheck,
    beforeRemove: beforeRemove,
    beforeDrop: (treeId: string, treeNodes: any, targetNode: any, moveType: string) => {
      // 禁止拖拽
      return false
    },
    beforeDrag: (treeId: string, treeNodes: any) => {
      // 禁止被拖拽
      return false
    }
  }
}

onMounted(() => {
  /**
   * 计算表格高度
   */
  editheight.value = computEditHeight()
  treeheight.value = computTreeHeight()
  nextTick(() => {
    $.fn.zTree.init!($('#deptztreeid'), treeData, [])

    ztreeObj = $.fn.zTree.getZTreeObj!('deptztreeid')
    bFormEditFlag.value = true
    onReset()
  })
})

/**
 * 根据部门关键字检索数据
 */
const searchDept = () => {
  treeData.async!.otherParam = {
    queryKey: queryKey.value,
    showRoot: true
  }
  $.fn.zTree.init!($('#deptztreeid'), treeData, [])
  ztreeObj = $.fn.zTree.getZTreeObj!('deptztreeid')
  onReset()
}

/**
 * 表单重置
 */
const onReset = () => {
  formData.value = {
    id: '',
    sdeptname: '',
    parentid: '',
    idepth: 1,
    sndomainou: '',
    bflagrole: true,
    bflagroot: false,
    sdeptalias: '',
    binner: true,
    sguidcas: '',
    bcaspush: false,
    idataversion: 0,
    isort: 1,
    buse: true
  }
  proxy.resetForm('deptRef')
  oldParentId = ''
  bFormEditFlag.value = true
  checkTreeNodes.value = []
  selectNodeIds.value = ''
}
/**
 * 获取当前节点的子节点集合
 */
const getzTreeChildrenNode = (treeNode: ITreeNode, result: string[]) => {
  // 递归查询当前节点所有子节点拼接成ID字符串
  if (treeNode.isParent) {
    // 检测是否是父节点
    const childrenNodes = treeNode.childrenNodes
    if (childrenNodes) {
      for (let i = 0; i < childrenNodes.length; i++) {
        if (childrenNodes[i].level && childrenNodes[i].level > childLevel) {
          childLevel = childrenNodes[i].level
        }
        if (!result.includes(childrenNodes[i])) {
          result.push(childrenNodes[i].id)
        }
        result = getzTreeChildrenNode(childrenNodes[i], result)
      }
    }
  }
  return result
}
/**
 * 提交按钮
 */
const submitForm = () => {
  proxy.$refs['deptRef'].validate((valid: any) => {
    if (valid) {
      // 获取当前选中部门子节点校验
      const treenode = ztreeObj.getNodeByParam('id', formData.value.id, null)
      if (treenode && formData.value.parentid !== oldParentId) {
        numbLevel = 0
        let idlist: string[] = []
        idlist.push(formData.value.id)
        childLevel = treenode.level === undefined ? 0 : treenode.level
        idlist = getzTreeChildrenNode(treenode, idlist)
        const childdepth = childLevel - (treenode.level === undefined ? 0 : treenode.level) + 1
        if (numbLevel < childdepth) {
          numbLevel = childdepth
        }
        if (parentNode.level && numbLevel + parentNode.level > 5) {
          ElMessage.warning('部门不能超过5级！')
          return false
        }
        if (
          formData.value.parentid !== '' &&
          formData.value.parentid !== '0' &&
          idlist.includes(formData.value.parentid)
        ) {
          ElMessage.warning('隶属部门不能是自己或其下子节点！')
          return false
        }

        if (
          (parentNode.id === '0' || smanagedepts!.indexOf(parentNode.id) > -1) &&
          adminFlag === 1
        ) {
          ElMessage.success({
            message: '无权限设置一级部门！',
            type: 'warning',
            duration: 0,
            showClose: true
          })
          return false
        }
      }

      console.log(formData.value)
      // 更新节点
      if (formData.value.id !== '') {
        updateDeptApi(formData.value).then((res: IAxios) => {
          if (res.success) {
            ztreeObj = $.fn.zTree.getZTreeObj!('deptztreeid')
            if (formData.value.buse === false) {
              $.fn.zTree.init!($('#deptztreeid'), treeData, [])
            } else {
              if (formData.value.parentid === oldParentId) {
                const node = ztreeObj.getNodeByParam('id', formData.value.id)
                node.name = formData.value.sdeptname
                ztreeObj.updateNode(node)
              } else {
                const node = ztreeObj.getNodeByParam('id', formData.value.id)
                node.name = formData.value.sdeptname
                ztreeObj.removeNode(node)
                ztreeObj.refresh()
                node.iconSkin = 'unit' // 修改图标
                ztreeObj.addNodes(parentNode, node)
              }
            }
            ElMessage.success('修改成功')
            onReset()
          }
        })
      } else {
        // 新增节点
        console.log(formData.value)
        saveDeptApi(formData.value).then((res: IAxios) => {
          if (res.success) {
            ElMessage.success('新增成功')
            ztreeObj.addNodes(parentNode, {
              name: formData.value.sdeptname,
              iconSkin: 'unit',
              id: res.obj.id
            })
            onReset()
          }
        })
      }
    }
  })
}
/**
 * 隶属部门下拉框返回数据
 * @param trees
 */
const selectionZtree = (trees: TSelectZtree[]) => {
  console.log(trees)
  if (trees.length > 0) {
    formData.value.parentid = trees[0].id
    formData.value.idepth = adminFlag === 1 ? trees[0].level + 2 : trees[0].level + 1
    ztreeObj = $.fn.zTree.getZTreeObj!('deptztreeid')
    const node = ztreeObj.getNodeByParam('id', trees[0].id)
    parentNode = node
  }
}
/**
 * 批量设置部门
 */
const batchUpdateDept = () => {
  numbLevel = 0
  if (checkTreeNodes.value.length < 1) {
    ElMessage.warning('请选择要调整的部门')
    return false
  }
  if (formData.value.parentid === '') {
    ElMessage.warning('请选择调整的目标部门')
    return false
  }
  if ((parentNode.id === '0' || smanagedepts!.indexOf(parentNode.id) > -1) && adminFlag === 1) {
    ElMessage.warning('无权限设置一级部门！')
    return false
  }
  // 选择的部门和子部门集合
  let checkids: string[] = []
  checkTreeNodes.value.forEach((treenode: ITreeNode) => {
    checkids.push(treenode.id)
    childLevel = treenode.level === undefined ? 0 : treenode.level
    checkids = getzTreeChildrenNode(treenode, checkids)
    const childdepth = childLevel - (treenode.level === undefined ? 0 : treenode.level) + 1
    if (numbLevel < childdepth) {
      numbLevel = childdepth
    }
  })

  if (formData.value.parentid !== '0' && checkids.includes(formData.value.parentid)) {
    ElMessage.warning('隶属部门不能是自己或其下子节点！')
    return false
  }
  if (parentNode.level && numbLevel + parentNode.level > 5) {
    ElMessage.warning('部门不能超过5级！')
    return false
  }
  const data: TbatchDeptData = {
    ids: selectNodeIds.value,
    pid: formData.value.parentid
  }
  batchUdpateDeptApi(data).then((res: IAxios) => {
    if (res.success) {
      var nodes = ztreeObj.getCheckedNodes(true)
      for (var i in nodes) {
        if (nodes[i].id === '0') {
          continue
        }
        if (formData.value.parentid === nodes[i].id || formData.value.parentid === nodes[i].pid) {
          continue
        }
        ztreeObj.removeNode(nodes[i])
        ztreeObj.refresh()
        ztreeObj.addNodes(parentNode, nodes[i])
      }
      ztreeObj.checkAllNodes(false) // 清空勾选
      ElMessage.success('调整成功')
      onReset()
    }
  })
}
/**
 * 批量删除部门
 */
const batchDeleteDept = () => {
  if (checkTreeNodes.value.length < 1) {
    ElMessage.warning('请选择要删除的部门')
    return false
  }
  // 是否有子节点
  let isParentFlag = false
  let isRootFlag = false
  checkTreeNodes.value.forEach((treeNode: ITreeNode) => {
    if (treeNode.isParent) {
      isParentFlag = true
      return false
    }
    if (smanagedepts!.indexOf(treeNode.id) > -1 && adminFlag === 1) {
      isRootFlag = true
      return false
    }
  })
  if (isParentFlag) {
    ElMessage.warning('请先删除子部门')
    return false
  }
  if (isRootFlag) {
    ElMessage.warning('无权删除一级部门')
    return false
  }
  ElMessageBox.confirm('确定删除选择的部门?', '提示', {
    type: 'warning'
  })
    .then(() => {
      const data = {
        ids: selectNodeIds.value
      }
      deleteDeptApi(data).then((res: IAxios) => {
        if (res.success) {
          const ztree = $.fn.zTree.getZTreeObj!('deptztreeid')
          const treeNodes = ztree.getCheckedNodes(true)
          treeNodes.forEach((treeNode: ITreeNode) => {
            ztree.removeNode(treeNode)
          })

          ElMessage.success('删除成功!')
          ztree.refresh()
          onReset()
        }
      })
    })
    .catch(() => {})
}
/**
 * 打开导入文件窗口
 */
const openImportDialog = () => {
  importDeptAndEmpDialog.value = true
}
/** 获取上传后返回的文件列表 */
const getUpFile = (res: IAxios<TUpFile>) => {
  console.log(res.success)
  if (res.success) {
    ElMessage.success('导入成功')
  }
  // responsedata.value?.push(res.obj)
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'dept'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>

<style scoped lang="scss">
.el-form--inline .el-form-item {
  margin: 0 0 0 30px;
}
.table_box .el-form-item {
  margin: 24px 0;
}
.input-with-select .el-input-group__prepend {
  background-color: var(--el-fill-color-blank);
}
.left_box {
  height: v-bind(treeheight);
}
.edit_box {
  height: v-bind(editheight);
}
</style>
