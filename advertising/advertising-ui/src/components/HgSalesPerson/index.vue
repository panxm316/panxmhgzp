<!--
 * @Author: wanghl
 * @Date: 2023-10-28 09:32:06
 * @LastEditTime: 2023-10-30 16:00:57
 * @LastEditors: wanghl
 * @Description:选择人员
-->
<template>
  <div class="fenge-box">
    <el-icon color="#409EFC"><star-filled /></el-icon><span>业务员</span
    ><el-icon color="#409EFC"><star-filled /></el-icon>
  </div>
  <div class="head-container">
    <el-input
      v-model="qsalesperson.queryKey"
      clearable
      placeholder="请输入名称"
      class="input-with-select"
      style="margin-bottom: 20px"
      @keyup.enter="searchPerson"
      @clear="searchPerson"
    >
      <template #append>
        <el-button icon="Search" @click="searchPerson" />
      </template>
    </el-input>
  </div>
  <div id="scrolly">
    <div id="personztreeid" class="ztree"></div>
  </div>
</template>
<script setup lang="ts">
import { ref, watch, defineComponent, reactive, onMounted } from 'vue'
import { getAdIndustryTreeApi } from '@/api/system/Industry'
import { Tadindustrylist, Tadindustry } from '@/types/components/hgindustry'
import type { IAxios } from 'axios'
defineOptions({
  name: 'HgSalesPerson'
})
const emit = defineEmits<{ (e: 'salesperson', salespersonid: string): void }>()

const salespersonid = ref('')
const adindustrylist = ref<Tadindustrylist[]>([])
type TotherParam = {
  accessibleNodes?: string
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 包含显示的部门id字符串，多个用逗号分隔
   */
  containsIds?: string
  /**
   * 查询结束时间
   */
  endTime?: string
  /**
   * 要排除不显示的id字符串，多个用逗号分隔
   */
  excludeIds?: string
  loginUserId?: string
  /**
   * 查询关键字
   */
  queryKey?: string
  /**
   * 是否显示根节点【所有部门】
   */
  showRoot?: string
  startTime?: string
}

/** 查询树结构 */
const qsalesperson = ref<TotherParam>({
  accessibleNodes: '',
  cacheDTOKey: '',
  containsIds: '',
  endTime: '',
  excludeIds: '',
  loginUserId: '',
  queryKey: '',
  showRoot: '',
  startTime: ''
})
// ------------------------------------------
/**
 * 点击选项id
 */
const sguiddept = ref(true)
/** ztree对象 */
let ztreeObj: any
/** 编辑节点时的父节点 */
let parentNode: ITreeNode
/** 点击或者勾选的id数组 */
const selectNodeIds = ref<string[]>([])
/** 树结构check多选状态 */
const bCheckDelFlag = ref(true)
/**
 * 在节点上显示添加节点图标按钮
 * @param {string} treeId 树id
 * @param {ITreeNode} treeNode 鼠标移动上的节点
 * @returns 显示添加按钮
 */
const addHoverDom = (treeId: string, treeNode: ITreeNode) => {
  $('.remove')?.remove()
  $('.edit')?.remove()
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
  ztreeObj = $.fn.zTree.getZTreeObj!('personztreeid')
  const node = ztreeObj.getNodes()
  console.log(node)
  // 根节点id的0值改为字符串
  const noderoot = ztreeObj.getNodeByParam('id', '', null)
  console.log(noderoot)
  if (noderoot) {
    noderoot.id = noderoot.id.toString()
  }
  // 默认展开第一级节点
  // const nodes = ztreeObj.getNodesByFilter((node: ITreeNode) => {
  //   return node.level === 0
  // })
  // nodes?.forEach((node: ITreeNode) => {
  //   ztreeObj.expandNode(node, true, false, true)
  // })
  ztreeObj.selectNode(node[0].children[0])
  if (node.length > 0) {
    ztreeObj.selectNode(noderoot)
    ztreeObj.expandNode(node[0].children[0], true, false, true)
    // mediaId.value = node[0].children[0].id
    loaddata()
  }
}
/**
 * 单击部门节点
 * @param event
 * @param treeId
 * @param treeNode
 */
const onClick = (event: Event, treeId: string, treeNode: ITreeNode) => {
  console.log(treeNode)
  // if (treeNode.level === 1) {
  //   onChangeIndustry(treeNode.id)
  // }
  // if (treeNode.level === 2) {
  //   onChangeIndustry(treeNode.id)
  // }
  onChangeIndustry(treeNode.id)
  loaddata()
}

/**
 * 树对象绑定
 */
const treeData: ISetting = {
  async: {
    enable: true,
    type: 'get',
    url: import.meta.env.VITE_APP_BASE_API + 'system/emp/getSalespersonTree',
    otherParam: {
      queryKey: qsalesperson.value.showRoot,
      showRoot: false
    }
  },
  check: {
    enable: false,
    chkStyle: 'checkbox',
    chkboxType: {
      Y: 's',
      N: 's'
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
    // onCheck: onCheck,
    // beforeRemove: beforeRemove,
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
// -------------------------------------------------
onMounted(() => {
  // getMediaInfo()
  loaddata()
  nextTick(() => {
    $.fn.zTree.init!($('#personztreeid'), treeData, [])
    ztreeObj = $.fn.zTree.getZTreeObj!('personztreeid')
    bCheckDelFlag.value = true
  })
})
// watch(
//   () => props.industry,
//   () => {
//     adindustry.adindustryid = props.industry?.adindustryid
//     adindustry.adindustryname = props.industry?.adindustryname
//     adindustry.show = props.industry?.show
//   },
//   { deep: true }
// )
/**
 * 获取客户列表
 */
const loaddata = () => {
  getAdIndustryTreeApi().then(({ obj, total }: IAxios<Tadindustrylist[]>) => {
    console.log(obj)
    adindustrylist.value = obj
  })
}
/**
 * 选择时候传值
 */
const onChangeIndustry = (e: string) => {
  emit('salesperson', e)
}
/**
 * 根据部门关键字检索数据
 */
const searchPerson = () => {
  treeData.async!.otherParam = {
    queryKey: qsalesperson.value.queryKey,
    showRoot: true
  }
  $.fn.zTree.init!($('#personztreeid'), treeData, [])
  ztreeObj = $.fn.zTree.getZTreeObj!('personztreeid')
  onReset()
}
/**
 * 表单重置
 */
const onReset = () => {
  qsalesperson.value = {
    accessibleNodes: '',
    cacheDTOKey: '',
    containsIds: '',
    endTime: '',
    excludeIds: '',
    loginUserId: '',
    queryKey: '',
    showRoot: '',
    startTime: ''
  }
}
</script>

<style scoped lang="scss">
.zhi {
  line-height: 24px;
  margin-right: 10px;
  font-size: 10px;
}
.zhi_b {
  line-height: 32px;
  margin-right: 10px;
}
</style>
