<!--
 * @Author: muyn
 * @Date: 2023-08-24 15:06:40
 * @LastEditTime: 2024-03-18 14:55:53
 * @LastEditors: wanghl
 * @Description:广告来源
-->
<template>
  <div id="adfromform" class="app-container">
    <el-row :gutter="5">
      <!--来源数据-->
      <el-col :xs="8" :sm="7" :md="6" :lg="5" :xl="5">
        <div class="table_box left_box">
          <div class="fenge-box">
            <el-icon color="#409EFC"><star-filled /></el-icon><span>来源列表</span
            ><el-icon color="#409EFC"><star-filled /></el-icon>
          </div>
          <div class="head-container">
            <el-input
              v-model="queryKey"
              placeholder="请输入来源名称"
              class="input-with-select"
              style="margin-bottom: 20px"
            >
              <template #append>
                <el-button icon="Search" @click="searchAdFrom" />
              </template>
            </el-input>
          </div>
          <div id="scrolly">
            <div id="adFromztreeid" class="ztree"></div>
          </div>
        </div>
      </el-col>
      <!-- 用户数据 -->
      <el-col :xs="16" :sm="17" :md="18" :lg="19" :xl="19">
        <div class="search_box">
          <div class="flex">
            <el-button type="primary" icon="Refresh" @click="resetExtend">重置扩展</el-button>
            <el-tooltip class="item" effect="dark" content="帮助" placement="top">
              <el-button class="help" type="text" @click="showhelp()">?</el-button>
            </el-tooltip>
          </div>
        </div>
        <div class="table_box edit_box">
          <el-form ref="adFromRef" :model="adFromData" :rules="rules" label-width="100px">
            <el-row :gutter="50">
              <el-col :span="14" :offset="4">
                <el-form-item label="隶属来源" prop="parentid">
                  <el-input v-model="currParentName" readonly></el-input>
                </el-form-item>
                <el-form-item label="序号" prop="isort">
                  <el-input-number
                    v-model="adFromData.isort"
                    :min="1"
                    :max="10000"
                    label="序号"
                    :disabled="bFormEditFlag"
                  ></el-input-number>
                </el-form-item>
                <el-form-item label="来源名称" prop="sname">
                  <el-input v-model="adFromData.sname" :disabled="bFormEditFlag"></el-input>
                </el-form-item>
                <el-form-item label="备注" prop="sremark">
                  <el-input v-model="adFromData.sremark" :disabled="bFormEditFlag"></el-input>
                </el-form-item>
                <el-form-item label="启用" prop="buse">
                  <el-checkbox v-model="adFromData.buse" :disabled="bFormEditFlag"></el-checkbox>
                </el-form-item>
                <el-form-item>
                  <el-button
                    type="primary"
                    icon="Check"
                    :disabled="bFormEditFlag"
                    @click="submitForm"
                    >确 定</el-button
                  >
                  <el-button :disabled="bFormEditFlag" icon="close" @click="cancel"
                    >取 消</el-button
                  >
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { getCurrentInstance, ref, reactive, toRefs, nextTick } from 'vue'
import { IAxios } from 'axios'
import modal from '@/plugins/modal'
import {
  getTbAdFromByIdApi,
  deleteTbAdFromByIdApi,
  saveTbAdFromApi,
  updateTbAdFromApi,
  getMaxSortApi,
  resetAdFromExtendApi
} from '@/api/ad/adfrom'
import useUserStore from '@/store/modules/user'
import { TAdFrom, TAdFromSearch } from '@/types/views/ad/adfrom'
import { ElFormItem, FormInstance, FormRules } from 'element-plus'
import { computEditHeight, computTreeHeight } from '@/utils/index'

defineOptions({
  name: 'AdFrom'
})
import { useRouter } from 'vue-router'
const router = useRouter()
const adFromData = ref<TAdFrom>({
  /**
   * id
   */
  id: '',
  /**
   * 父id
   */
  parentid: '',
  /**
   * 类别名称
   */
  sname: '',
  /**
   * 类别解释
   */
  sdesc: '',
  /**
   * 本级编号
   */
  slocalid: '',
  /**
   * 类别级别
   */
  idepth: 1,
  /**
   * 备注
   */
  sremark: '',
  /**
   * 序号
   */
  isort: 1,
  /**
   * 节点禁用
   */
  buse: true
})
/** 用户身份信息 */
const { user } = useUserStore()
/** 表格高度 */
const editheight = ref('')
/** 树高度 */
const treeheight = ref('')
/** 用户是否超管 */
const adminFlag = user?.adminflag
/** 节点上新增、删除按钮显示状态 */
let showAddBtnFlag: boolean = false
/** ztree对象 */
let ztreeObj: any
/** 1:新增根节点，2：新增子节点, 0:修改 */
let addOrUpdate: number = 1
/** 编辑节点时的父节点 */
let parentNode: ITreeNode
const adFromRef = ref<FormInstance>()
/** 验证规则 */
const rules = reactive<FormRules<TAdFrom>>({
  sname: [
    { required: true, message: '请输入来源名称', trigger: 'blur' },
    { min: 2, max: 15, message: '来源名称长度在2-15个字符之间', trigger: 'blur' }
  ]
})
/** 允许编辑状态 */
const bFormEditFlag = ref(true)
/** 当前选中节点的名称 */
const currParentName = ref<string | undefined>('')
/** 查询来源关键字 */
const queryKey = ref('')
/** 原始parentid */
let oldParentId: string = ''
onMounted(() => {
  // getList()
  /**
   * 计算表格高度
   */
  editheight.value = computEditHeight()
  treeheight.value = computTreeHeight()
  nextTick(() => {
    $.fn.zTree.init!($('#adFromztreeid'), treedata, [])
    ztreeObj = $.fn.zTree.getZTreeObj!('adFromztreeid')
    bFormEditFlag.value = true
  })
})

/**
 * 在节点上显示添加、删除图标按钮
 * @param {string} treeId 树id
 * @param {ITreeNode} treeNode 鼠标移动上的节点
 * @returns 显示添加按钮
 */
const addHoverDom = (treeId: string, treeNode: ITreeNode) => {
  if (treeNode.level === 0) {
    $('.remove')?.remove()
  }
  // if (treeNode.level === 0 && adminFlag !== 2) {
  //   $('.edit').hide
  //   return
  // }
  if (treeNode.level! < 5) {
    // 5级节点限制添加按钮
    const sObj = $('#' + treeNode.tId + '_span') // 获取节点信息
    if (treeNode.editNameFlag || $('#addBtn_' + treeNode.tId).length > 0) return

    const addStr =
      "<span class='button add' id='addBtn_" +
      treeNode.tId +
      "' title='添加子来源' onfocus='this.blur();'></span>" // 定义添加按钮
    sObj?.after(addStr) // 加载添加按钮
    const btn = $('#addBtn_' + treeNode.tId)
    // 绑定添加事件，并定义添加操作
    if (btn) {
      btn.on('click', (event: any) => {
        event.stopPropagation()
        reset()
        showAddBtnFlag = true
        bFormEditFlag.value = false
        addOrUpdate = 2 // 保存状态改为新增子节点
        parentNode = treeNode
        oldParentId = treeNode.id
        currParentName.value = treeNode.name
        adFromData.value.parentid = treeNode.id
        adFromData.value.buse = false
        adFromData.value.idepth = treeNode.level !== undefined ? treeNode.level + 1 : 1
        getMaxSortApi().then((res: IAxios) => {
          if (res.success) {
            adFromData.value.isort = res.obj
          }
        })
        if (treeNode.level === 0) {
          adFromData.value.buse = true
        } else {
          getTbAdFromByIdApi(treeNode.id).then((res: IAxios) => {
            if (res.success) {
              adFromData.value.buse = res.obj.buse
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
 * 来源加载成功
 */
const onAsyncSuccess = () => {
  ztreeObj = $.fn.zTree.getZTreeObj!('adFromztreeid')
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
  if (adFromData.value.parentid !== '') {
    const treenode = ztreeObj.getNodeByParam('id', adFromData.value.parentid, null)
    ztreeObj.expandNode(treenode, true, false, true)
  }
}
/**
 * 单击来源节点
 * @param event
 * @param treeId
 * @param treeNode
 */
const onClick = (event: Event, treeId: string, treeNode: ITreeNode) => {
  // 清空勾选
  ztreeObj.checkAllNodes(false)
  if (treeNode.level === 0) {
    return
  }
  adFromRef.value?.clearValidate()
  // 点击树保存状态改为修改
  parentNode = treeNode.getParentNode!()
  oldParentId = parentNode.id
  // updatepidflag = treeNode.id // 是否调整了来源  记录来源id
  if (treeNode.level !== 0) {
    bFormEditFlag.value = false
    showAddBtnFlag = true

    getTbAdFromByIdApi(treeNode.id).then((res: IAxios) => {
      if (res.success) {
        adFromData.value = res.obj
        const pnode = treeNode.getParentNode!()
        if (pnode != null) {
          // 隶属来源赋值
          currParentName.value = pnode.name
          adFromData.value.parentid = pnode.id
        } else {
          currParentName.value = ''
          adFromData.value.parentid = ''
        }
        addOrUpdate = 0 // 保存按钮置为修改状态
      }
    })
  }
}

/**
 * 删除来源
 * @param treeId
 * @param treeNode
 */
const beforeRemove = (treeId: string, treeNode: ITreeNode) => {
  // 删除前判断
  if (treeNode.childrenNodes && treeNode.childrenNodes.length > 0) {
    // 是否有子节点
    flag = false
    modal.msgWarning('请先删除子来源')
    return false
  } else {
    if (treeNode.level === 1 && adminFlag !== 2) {
      flag = false
      modal.msgWarning('无权删除一级来源')
      return false
    }

    var flag = false
    modal
      .confirm('确定删除该来源?')
      .then(() => {
        deleteTbAdFromByIdApi(treeNode.id).then((res: IAxios) => {
          if (res.success) {
            reset()
            const ztree = $.fn.zTree.getZTreeObj!('adFromztreeid')
            ztree.removeNode(treeNode)
            modal.msgSuccess('删除成功')
            ztree.refresh()
            flag = true
          }
        })
      })
      .catch(() => {})
    return flag
  }
}
/**
 * 数对象绑定
 */
const treedata: ISetting = {
  async: {
    enable: true,
    type: 'get',
    url: import.meta.env.VITE_APP_BASE_API + '/ad/adfrom/getTbAdFromTree',
    otherParam: {
      queryKey: '',
      rootName: '来源',
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
    /**
     *
     *
     * @param {string} treeId 树id
     * @param {ITreeNode} treeNode 鼠标移动上的节点
     * @returns 显示添加按钮
     */
    addHoverDom: addHoverDom,
    removeHoverDom: removeHoverDom,
    showLine: false,
    selectedMulti: false,
    showIcon: false,
    fontCss: function (treeid, treeNode) {
      return treeNode.buse
        ? { color: '#606266', 'font-weight': '600' }
        : { color: '#F56C6C', 'font-weight': '600' }
    }
  },
  edit: {
    enable: true, // 单独设置为true时，可加载修改、删除图标
    removeTitle: '删除节点',
    //            	        renameTitle: "编辑节点名称",
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

    /**
     *
     *
     * @param {string} treeId
     * @param {ITreeNode} treeNode
     * @returns 返回false删除操作停止，返回true删除操作执行
     */
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
/**
 * 根据来源关键字检索数据
 */
const searchAdFrom = () => {
  treedata.async!.otherParam = {
    queryKey: queryKey.value,
    rootName: '来源',
    showRoot: true
  }
  $.fn.zTree.init!($('#adFromztreeid'), treedata, [])
}
/**
 * 获取最大序号
 */
const getMaxSort = async () => {
  await getMaxSortApi().then(({ success, obj }: IAxios) => {
    if (success) {
      adFromData.value.isort = obj
    }
  })
}

/**
 * 取消按钮
 */
const cancel = () => {
  reset()
}
/**
 * 表单重置
 */
const reset = () => {
  adFromData.value = {
    id: '',
    parentid: '',
    sname: '',
    sdesc: '',
    slocalid: '',
    idepth: 1,
    sremark: '',
    isort: 1,
    buse: true
  }
  adFromRef.value?.resetFields()
  currParentName.value = ''
  oldParentId = ''
  bFormEditFlag.value = true
}
/**
 * 获取当前节点的子节点集合
 */
const getzTreeChildrenNode = (treeNode: ITreeNode, result: string) => {
  // 递归查询当前节点所有子节点拼接成ID字符串
  if (treeNode.isParent) {
    // 检测是否是父节点
    const childrenNodes = treeNode.children
    if (childrenNodes) {
      for (let i = 0; i < childrenNodes.length; i++) {
        result += ',' + childrenNodes[i].id
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
  adFromRef.value?.validate((valid: any) => {
    if (valid) {
      // 获取当前选中来源子节点校验
      const treenode = ztreeObj.getNodeByParam('id', adFromData.value.id, null)
      if (treenode && adFromData.value.parentid !== oldParentId) {
        let strid = adFromData.value.id
        strid = getzTreeChildrenNode(treenode, strid)
        if (
          adFromData.value.parentid !== '' &&
          adFromData.value.parentid !== '0' &&
          strid?.indexOf(adFromData.value.parentid, 0) >= 0
        ) {
          modal.msgWarning('隶属来源不能是自己或其下子节点！')
          return false
        }
      }
      // if (parentNode.id === '0' && adminFlag !== 2) {
      //   modal.msgWarning('无权限设置一级来源！')
      //   return false
      // }

      console.log(adFromData.value)
      // 更新节点
      if (adFromData.value.id !== '') {
        updateTbAdFromApi(adFromData.value).then((res: IAxios) => {
          if (res.success) {
            ztreeObj = $.fn.zTree.getZTreeObj!('adFromztreeid')
            if (adFromData.value.buse === false) {
              $.fn.zTree.init!($('#adFromztreeid'), treedata, [])
            } else {
              if (adFromData.value.parentid === oldParentId) {
                const node = ztreeObj.getNodeByParam('id', adFromData.value.id)
                node.name = adFromData.value.sname
                ztreeObj.updateNode(node)
              } else {
                const node = ztreeObj.getNodeByParam('id', adFromData.value.id)
                node.name = adFromData.value.sname
                ztreeObj.removeNode(node)
                ztreeObj.refresh()
                node.iconSkin = 'unit' // 修改图标
                ztreeObj.addNodes(parentNode, node)
              }
            }
            modal.msgSuccess('修改成功!')
            reset()
          }
        })
      } else {
        // 新增节点
        console.log(adFromData.value)
        saveTbAdFromApi(adFromData.value).then((res: IAxios) => {
          if (res.success) {
            modal.msgSuccess('新增成功!')
            ztreeObj.addNodes(parentNode, {
              name: adFromData.value.sname,
              id: res.obj.id,
              buse: res.obj.buse
            })
            reset()
          }
        })
      }
    }
  })
}
/**
 * 重置来源扩展
 */
const resetExtend = () => {
  modal.confirm('确定要重置行业扩展吗').then(() => {
    resetAdFromExtendApi().then((res: IAxios) => {
      if (res.success) {
        modal.msgSuccess('重置成功!')
      }
    })
  })
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'adfrom'
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
