<!--
 * @Author: caogd
 * @Date: 2023-08-24 09:55:48
 * @LastEditTime: 2024-03-07 14:32:56
 * @LastEditors: wanghl
 * @Description: 树组件
-->
<template>
  <el-row v-if="props.operate">
    <el-checkbox v-if="checkboxtype" v-model="whole" @change="wholeChange">全部</el-checkbox>
    <el-checkbox v-if="checkboxtype" v-model="checklinkage">联动</el-checkbox>
    <el-checkbox v-model="expandall" @change="expandAllChange">展开</el-checkbox>
  </el-row>
  <el-row v-if="props.filterable">
    <el-col :span="24">
      <el-input v-model="queryKey" clearable placeholder="请输入关键字" @change="searchZtree">
        <template #append>
          <el-button :icon="Search" @click="searchZtree" />
        </template>
      </el-input>
    </el-col>
  </el-row>
  <el-row>
    <div
      :id="ztreeId"
      :style="'height: ' + ztreeHeight + 'px;overflow: auto; width: 100%'"
      class="ztree"
    ></div>
  </el-row>
</template>

<script setup lang="ts">
import { Search } from '@element-plus/icons-vue'
import type { TSelectZtree } from '@/types/common/index'
import type { THgZtreeProps, THgTreeParams } from '@/types/components/hgztree'
import { EHgDeptZtreeUrl } from '@/types/common/enumindex'
import { getRandomId } from '@/utils/index'

defineOptions({
  name: 'HgZtree'
})

const props = withDefaults(defineProps<THgZtreeProps>(), { filterable: true })
/** 导出返回数据 */
const emit = defineEmits<{
  selectionztree: [data: TSelectZtree[]]
}>()
type TotherParam = {
  /** 包含显示的id字符串，多个用逗号分隔 */
  containsIds?: string
  /** 查询关键字 */
  queryKey?: string
} & THgTreeParams

/** 全部勾选 */
const whole = ref(false)
/** 联动勾选 */
const checklinkage = ref(false)
/** 展开全部 */
const expandall = ref(false)
/** 树高度 */
const ztreeHeight = ref(props.treeheight ? props.treeheight : 400)
/** ztreeId */
const ztreeId = ref(getRandomId() + 'treeid')
/** ztree请求url 根据相应需求url添加 */
let ztreeUrl = ''
switch (props.scopeflag) {
  case EHgDeptZtreeUrl.DEPT_GETDEPTTREE:
    ztreeUrl = import.meta.env.VITE_APP_BASE_API + '/system/dept/getDeptTree'
    break
  case EHgDeptZtreeUrl.MEDIA_GETSYSMEDIATREE:
    ztreeUrl = import.meta.env.VITE_APP_BASE_API + '/media/media/getSysMediaTree'
    break
  case EHgDeptZtreeUrl.EMP_GETDEPTEMPLOY:
    ztreeUrl = import.meta.env.VITE_APP_BASE_API + '/system/emp/getEmpTree'
    break
  case EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYPERSON:
    ztreeUrl = import.meta.env.VITE_APP_BASE_API + '/system/emp/getSalespersonTree'
    break
  case EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYINDUSTRY:
    ztreeUrl = import.meta.env.VITE_APP_BASE_API + '/system/adindustry/getAdIndustryTree'
    break
  case EHgDeptZtreeUrl.MEDIA_GETSYSMEDIAFLOADTREE:
    ztreeUrl = import.meta.env.VITE_APP_BASE_API + '/media/media/getMediaFloadTree'
    break
  case EHgDeptZtreeUrl.MEDIA_GETSYSMEDIAFLOADAREAVERTREE:
    ztreeUrl = import.meta.env.VITE_APP_BASE_API + '/media/media/getMediaFloadAreaverTree'
    break
  case EHgDeptZtreeUrl.DEPT_GETREPORTDEPTTREE:
    ztreeUrl = import.meta.env.VITE_APP_BASE_API + '/system/dept/getLeafDeptTree'
    break
  case EHgDeptZtreeUrl.CUSTOMER_GETCUSTOMERSOURCETREE:
    ztreeUrl = import.meta.env.VITE_APP_BASE_API + '/ad/adfrom/getTbAdFromTree'
    break
  default:
    ztreeUrl = ''
    break
}

/** ztree属性 */
const queryKey = ref('') // 部门名称
/** ztree对象 */
let hgZtreeObj: IzTreeObj
/** 当前选中 */
let selectZtreeId: string[] = props.selectids ? props.selectids : []
/** 查询树需要传的参数 */
let otherParamData: TotherParam = {
  ...{ containsIds: selectZtreeId?.join(',') },
  ...props.treeparams
}
/**
 * @description: 全选
 * @return {*}
 */
const wholeChange = () => {
  if (whole.value) {
    hgZtreeObj.checkAllNodes(true)
  } else {
    hgZtreeObj.checkAllNodes(false)
  }
  getInfo()
}
/**
 * @description: 全部展开
 * @return {*}
 */
const expandAllChange = () => {
  if (expandall.value) {
    hgZtreeObj.expandAll(true)
  } else {
    hgZtreeObj.expandAll(false)
  }
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
  // 带查询条件展开所有
  if (queryKey.value) {
    hgZtreeObj.expandAll(true)
  }
  for (let n = 0; n < nodes.length; n++) {
    // 如果有选中勾选展开
    if (selectZtreeId.length > 0) {
      // 选择时返回勾选状态
      if (selectZtreeId.indexOf(nodes[n].id) > -1) {
        hgZtreeObj.checkNode(nodes[n], true, false)
        const parent = nodes[n].getParentNode!()
        hgZtreeObj.expandNode(parent, true, false, false)
      }
    }
  }
  if (selectZtreeId.length > 0) {
    // 点击ztree返回的勾选数据
    getInfo()
  }
}

/**
 * ztree点击事件
 * @param event
 * @param treeId
 * @param treeNode
 */
const ztreeClick = (event: Event, treeId: string, treeNode: ITreeNode) => {
  const nodes = hgZtreeObj.getSelectedNodes(true)
  for (let i = 0, l = nodes.length; i < l; i++) {
    const ischecked = !nodes[i].checked
    hgZtreeObj.checkNode(nodes[i], ischecked, false) // 当前节点打勾或去勾
    if (checklinkage.value) {
      // 递归打勾或去勾
      checkZtreeNode(nodes[i], ischecked)
    }
  }
  // 点击ztree返回的勾选数据
  getInfo()
}
/**
 * @description: ztree选择事件
 * @param {*} event
 * @param {*} treeId
 * @param {*} treeNode
 * @return {*}
 */
const ztreeCheck = (event: Event, treeId: string, treeNode: ITreeNode) => {
  if (checklinkage.value) {
    // 递归打勾或去勾
    checkZtreeNode(treeNode, treeNode.checked)
  }
  // 勾选ztree返回的函数
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
    enable: true,
    chkStyle: props.checkboxtype ? 'checkbox' : 'radio',
    chkboxType: {
      Y: '',
      N: ''
    },
    radioType: 'all'
  },
  view: {
    // fontCss: setFontCss,
    showLine: false,
    selectedMulti: false,
    showIcon: false,
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
    onClick: ztreeClick,
    onCheck: ztreeCheck
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
  () => props.selectids,
  (newValue, oldValue) => {
    nextTick(() => {
      if (JSON.stringify(selectZtreeId) === JSON.stringify(newValue)) {
        return
      }
      selectZtreeId = newValue ?? []
      // 全部节点
      const node = hgZtreeObj.getNodes()
      if (node.length < 1) {
        return
      }
      // 平级全部节点
      const nodes: ITreeNode[] = hgZtreeObj.transformToArray(node)

      hgZtreeObj.expandAll(false)
      hgZtreeObj.checkAllNodes(false)
      // 如果有选中并且不是带条件查询勾选展开
      if (newValue && newValue.length > 0) {
        // 选择时返回勾选状态
        for (let n = 0; n < nodes.length; n++) {
          if (newValue.indexOf(nodes[n].id) > -1) {
            hgZtreeObj.checkNode(nodes[n], true, false)
            const parent = nodes[n].getParentNode!()
            hgZtreeObj.expandNode(parent, true, false, false)
          }
        }
      } else {
        queryKey.value = ''
        hgZtreeObj = $.fn.zTree.init!($('#' + ztreeId.value), treedata, [])
      }
      // 点击ztree返回的勾选数据
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
    selectZtreeId = props.selectids ?? []
    queryKey.value = ''
    otherParamData = {
      ...props.treeparams
    }
    searchZtree()
  },
  { deep: true }
)

/**
 * @description: 点击查询
 * @return {*}
 */
const searchZtree = () => {
  otherParamData.queryKey = queryKey.value
  otherParamData.containsIds = selectZtreeId?.join(',')
  treedata.async!.otherParam = otherParamData
  hgZtreeObj = $.fn.zTree.init!($('#' + ztreeId.value), treedata, [])
}

/**
 * 获取选中数据
 */
const getInfo = () => {
  const nodes = hgZtreeObj.getCheckedNodes(true)
  // console.log(nodes)
  const datas: TSelectZtree[] = []
  for (let i = 0; i < nodes.length; i++) {
    const data: TSelectZtree = {
      id: nodes[i].id,
      parentId: nodes[i].parentId,
      name: nodes[i].name ?? '',
      level: nodes[i].level ?? 0,
      parentName: nodes[i].getParentNode()?.name ?? ''
    }
    datas.push(data)
  }
  console.log(datas)
  // 当前选中id
  selectZtreeId = datas.map((item) => item.id)
  emit('selectionztree', datas)
}
</script>

<style scoped lang="scss"></style>
