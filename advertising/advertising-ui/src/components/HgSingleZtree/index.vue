<!--
 * @Author: caogd
 * @Date: 2023-08-24 09:55:48
 * @LastEditTime: 2024-01-24 13:10:20
 * @LastEditors: wanghl
 * @Description: 点击单选树组件单选也可用（叠次）
-->
<template>
  <el-row>
    <div
      :id="ztreeId"
      :style="'height: ' + ztreeHeight + 'px;overflow: auto; width: 100%'"
      class="ztree"
    ></div>
  </el-row>
</template>

<script setup lang="ts">
import type { THgSingleZtreeProps, THgSingleZtreeParams } from '@/types/components/hgztree'
import { EHgSingleZtreeUrl } from '@/types/common/enumindex'
import { getRandomId } from '@/utils/index'
defineOptions({
  name: 'HgSingleZtree'
})

const props = defineProps<THgSingleZtreeProps & { modelValue?: string }>()

const emit = defineEmits<{
  /** 绑定值改变 */
  'update:modelValue': [data: string]
  /** 返回整个对象 返回值最好不要绑定到传入值中*/
  change: [data: ITreeNode]
}>()
/** 树高度 */
const ztreeHeight = ref(props.treeheight ? props.treeheight : 300)
/** ztreeId */
const ztreeId = ref(getRandomId() + 'treeid')
/** ztree请求url 根据相应需求url添加 */
let ztreeUrl = ''
switch (props.scopeflag) {
  case EHgSingleZtreeUrl.PRICE_GETMEDIAFOLDTREE:
    ztreeUrl = import.meta.env.VITE_APP_BASE_API + '/media/fold/getMediaFoldTree'
    break
  case EHgSingleZtreeUrl.PRICE_GETWEEKSETTINGTREE:
    ztreeUrl = import.meta.env.VITE_APP_BASE_API + '/price/weeksetting/getWeekSettingTree'
    break
  case EHgSingleZtreeUrl.PRICE_GETFOLDAREAVERTREELIST:
    ztreeUrl = import.meta.env.VITE_APP_BASE_API + '/media/foldareaver/getFoldAreaverTreeList'
    break
  case EHgSingleZtreeUrl.PRICE_GETPAGESORTTREELIST:
    ztreeUrl = import.meta.env.VITE_APP_BASE_API + '/media/pagesort/getPageSortTreeList'
    break
  case EHgSingleZtreeUrl.PRICE_GETADCOLORTREELIST:
    ztreeUrl = import.meta.env.VITE_APP_BASE_API + '/price/adcolor/getAdColorTreeList'
    break
  case EHgSingleZtreeUrl.PRICE_GETADDISPLAYFORMTREELIST:
    ztreeUrl = import.meta.env.VITE_APP_BASE_API + '/price/addisplayform/getAdDisplayFormTreeList'
    break
  case EHgSingleZtreeUrl.PRICE_GETADSPECTREELIST:
    ztreeUrl = import.meta.env.VITE_APP_BASE_API + '/media/adspec/getAdspecTreeList'
    break
  case EHgSingleZtreeUrl.PAGEPLANE_GETFOLDPAGEPLANETREELIST:
    ztreeUrl = import.meta.env.VITE_APP_BASE_API + '/schedule/foldpageplan/getFoldPagePlaneTree'
    break
  // 获取有效媒体的版面计划树（含有非报刊媒体）
  case EHgSingleZtreeUrl.PAGEPLANE_GETFOLDPAGEPLANETREELISTAll:
    ztreeUrl =
      import.meta.env.VITE_APP_BASE_API + '/schedule/foldpageplan/getMediaFoldPagePlaneTree'
    break
  case EHgSingleZtreeUrl.PRICE_GETMEDIAFOLDAREAVERTREE:
    ztreeUrl = import.meta.env.VITE_APP_BASE_API + '/media/foldareaver/getMediaFoldAreaverTree'
    break
  // 获取我的常用菜单
  case EHgSingleZtreeUrl.PAGEPLANE_GETMYMENU:
    ztreeUrl = import.meta.env.VITE_APP_BASE_API + '/system/mymenu/getMymenu'
    break
  default:
    ztreeUrl = ''
    break
}

/** ztree对象 */
let hgZtreeObj: IzTreeObj
/** 当前选中 */
const selectZtreeId = ref(props.modelValue)
/** 查询树需要传的参数 */
let otherParamData: THgSingleZtreeParams = {
  ...props.treeparams
}

/**
 * @description: 树加载成功方法
 * @return {*}
 */
const initAsyncSuccess = (event: Event, treeId: string, treeNode: ITreeNode) => {
  // 全部节点
  const node = hgZtreeObj.getNodes()
  // 平级全部节点
  const nodes: ITreeNode[] = hgZtreeObj.transformToArray(node)
  if (props.modelValue) {
    for (let n = 0; n < nodes.length; n++) {
      if (props.modelValue.indexOf(nodes[n].id) > -1) {
        hgZtreeObj.selectNode(nodes[n])
        const parent = nodes[n].getParentNode!()
        if (parent) {
          hgZtreeObj.expandNode(parent, true, false, false)
        }
      }
    }
    getInfo()
  } else {
    const node = hgZtreeObj.getNodes()
    if (node.length > 0 && node[0].childrenNodes?.length > 0) {
      hgZtreeObj.expandNode(node[0], true, false, false)
    }
    console.log(node)
  }
}

/**
 * ztree点击事件
 * @param event
 * @param treeId
 * @param treeNode
 */
const ztreeClick = (event: Event, treeId: string, treeNode: ITreeNode) => {
  // 点击ztree返回的勾选数据
  getInfo()
}
/**
 * @description: 递归树节点选中展开方法
 * @param {*} node
 * @param {*} checked
 * @return {*}
 */
const checkZtreeNode = (node: any, checked: any) => {
  if (node.isParent) {
    hgZtreeObj.checkNode(node, checked, false)
    hgZtreeObj.expandNode(node, true, false, false)
    node.childrenNodes.forEach(function (_node: any) {
      checkZtreeNode(_node, checked)
    })
  } else {
    hgZtreeObj.checkNode(node, checked, false)
    return
  }
}

/**
 * ztree配置参数
 */
const treedata: ISetting = {
  async: {
    enable: true,
    type: 'get',
    url: ztreeUrl,
    otherParam: otherParamData // 传参
  },
  check: {
    enable: false
  },
  view: {
    // fontCss: setFontCss,
    showLine: false,
    selectedMulti: false,
    showIcon: props.showIcon,
    fontCss: function (treeid, treeNode) {
      return treeNode.buse
        ? { color: '#606266', 'font-weight': '600', 'font-size': '16px!important' }
        : { color: '#606266', 'font-weight': '600', 'font-size': '16px' }
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
    onClick: ztreeClick
  }
}

onMounted(() => {
  nextTick(() => {
    hgZtreeObj = $.fn.zTree.init!($('#' + ztreeId.value), treedata, [])
  })
})
/**
 * 监听传入id
 */
watch(
  () => props.modelValue,
  (newValue, oldValue) => {
    nextTick(() => {
      if (selectZtreeId.value === newValue || newValue === '') {
        return
      }
      // 全部节点
      const node = hgZtreeObj.getNodes()
      if (node.length < 1) {
        return
      }
      // 平级全部节点
      const nodes: ITreeNode[] = hgZtreeObj.transformToArray(node)
      // hgZtreeObj.expandAll(false)
      // hgZtreeObj.cancelSelectedNode()
      // 如果有选中并且不是带条件查询展开
      if (newValue && newValue.length > 0) {
        // 选中节点
        for (let n = 0; n < nodes.length; n++) {
          if (newValue.indexOf(nodes[n].id) > -1) {
            hgZtreeObj.selectNode(nodes[n])
            const parent = nodes[n].getParentNode!()
            if (parent) {
              hgZtreeObj.expandNode(parent, true, false, false)
            }
          }
        }
      } else {
        hgZtreeObj = $.fn.zTree.init!($('#' + ztreeId.value), treedata, [])
      }
      getInfo()
    })
  }
)

/** 监听树的传入参数变化时初始化 */
watch(
  () => props.treeparams,
  (newValue, oldValue) => {
    if (JSON.stringify(newValue) === JSON.stringify(oldValue)) {
      return
    }
    selectZtreeId.value = props.modelValue
    otherParamData = {
      ...props.treeparams
    }
    searchZtree()
  },
  { deep: true }
)

watch(
  () => props.treeheight,
  (value) => {
    ztreeHeight.value = value || 300
  }
)

/**
 * @description: 点击查询
 * @return {*}
 */
const searchZtree = () => {
  treedata.async!.otherParam = otherParamData
  hgZtreeObj = $.fn.zTree.init!($('#' + ztreeId.value), treedata, [])
}

/**
 * 获取选中数据
 */
const getInfo = () => {
  console.log('getInfo')
  const nodes = hgZtreeObj.getSelectedNodes(true)
  const data: ITreeNode = nodes[0]
  selectZtreeId.value = data?.id
  emit('update:modelValue', data?.id)
  emit('change', data)
}
</script>

<style scoped lang="scss"></style>
