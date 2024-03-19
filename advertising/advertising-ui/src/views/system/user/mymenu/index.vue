<!--
 * @Author: wanghl
 * @Date: 2024-01-23 10:58:47
 * @LastEditTime: 2024-02-17 14:40:14
 * @LastEditors: wanghl
 * @Description:
-->
<template>
  <div class="app-container userbox">
    <el-row :gutter="30">
      <el-col :span="10">
        <div class="menustyle">常用菜单</div>
        <el-card class="left_box">
          <el-checkbox-group v-model="checkList">
            <el-checkbox
              v-for="item in mymenutree"
              :key="item.menuid"
              draggable="true"
              :label="item.menuid"
              style="margin-bottom: 10px; width: 100%"
              @dragstart="dragStart(item, $event)"
              @dragover="dragOver($event)"
              @drop="drop(item, $event)"
              @dragend="dragEnd(item)"
            >
              {{ item.menuName }}
              <el-button
                type="primary"
                icon="Top"
                link
                @click="onUp(item)"
                title="上移"
              ></el-button>
              <el-button
                title="下移"
                type="warning"
                link
                icon="Bottom"
                @click="onDown(item)"
                style="margin-left: 0px"
              ></el-button>
            </el-checkbox>
          </el-checkbox-group>

          <!-- {{ mymenutree }} -->
        </el-card>
      </el-col>
      <el-col :span="4" style="text-align: center; margin-top: 20%">
        <el-button v-if="mymenutree.length === 0" type="primary" @click="onAdd"
          ><el-icon><DArrowLeft /></el-icon>添加到常用</el-button
        >
        <el-button v-else type="success" @click="onAdd"
          ><el-icon><DArrowLeft /></el-icon>更新常用</el-button
        >
        <div style="width: 100%; text-align: center; margin-top: 20px">
          <el-button type="danger" @click="ondelete()"
            >移出常用<el-icon><DArrowRight /></el-icon
          ></el-button>
        </div>
        <!-- <div style="width: 100%; text-align: center; margin-top: 20px">
          <el-button type="primary" @click="onsure()"
            ><el-icon><Sort /></el-icon>确认顺序</el-button
          >
        </div> -->
      </el-col>
      <el-col :span="10">
        <div class="menustyle">拥有权限的菜单</div>
        <el-card class="left_box">
          <div id="scrolly">
            <div id="foldmediaztreeid" class="ztree"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script setup lang="ts">
import { required, computTableHeight, isNumberStr, formatDate, tableHeaderColor } from '@/utils'
import { EHgSingleZtreeUrl } from '@/types/common/enumindex'
import { TMymenu, TSelectmenu } from '@/types/views/system'

import { saveMymenuApi, deleteMymenuApi, getMyRolemenuApi, getMymenuApi } from '@/api/system/user'
import type { IAxios } from 'axios'
import modal from '@/plugins/modal'
import HgDateIndex from '@/components/HgDateIndex/index.vue'
import { TDateType, TDateTimeType } from '@/types/components/hgdateindex'
import dayjs from 'dayjs'
import { add } from 'lodash'
defineOptions({
  name: 'MyMenu'
})
import { useRouter } from 'vue-router'
import { fa } from 'element-plus/es/locale'

/**
 * 页面高度
 */
const boxheight = ref(window.innerHeight - 45 + 'px')
/**
 * 添加时选中的id
 */
const menuid = ref<TSelectmenu>({
  menuid: '',
  isort: 0
})
/**
 * 删除时选中的id
 */
const menuiddelete = ref({
  sMenuIds: ''
})
/**
 * 排序时选中的id
 */
const menuidsort = ref<TSelectmenu>({
  menuid: '',
  isort: 0
})
/** 需要添加到左侧的数组 */
const menuidlistdata = ref<TMymenu>({
  lsSelectmenu: [] as TSelectmenu[]
})
const menuidlist = ref<TSelectmenu[]>([])
/** 我的菜单树数据 */
const mymenutree = ref<any[]>([])
/** 我的常用列表 */
const mymenuidlist = ref<TSelectmenu[]>([])
/** 左侧选中的数据列表 */
const checkList = ref([])
/** 树高度 */
const treeheight = ref('')
/** ztree对象 */
let ztreeObj: any
/** 编辑节点时的父节点 */
let parentNode: ITreeNode
/** 点击或者勾选的id数组 */
const selectNodeIds = ref<string[]>([])
/** 树结构check多选状态 */
const bCheckDelFlag = ref(true)
/**
 * 获取菜单树数据
 */
const getTreeData = () => {
  getMyRolemenuApi().then((res: any) => {
    console.log(res.obj)
  })
}
/**
 * 获取我的常用菜单
 */
const getMyMenu = () => {
  getMymenuApi().then((res: any) => {
    mymenuidlist.value = []
    mymenutree.value = res.obj
    res.obj.forEach((item: any) => {
      mymenuidlist.value.push(item.menuid)
    })
    console.log(mymenuidlist.value)
    nextTick(() => {
      $.fn.zTree.init!($('#foldmediaztreeid'), treeData, [])
      ztreeObj = $.fn.zTree.getZTreeObj!('foldmediaztreeid')
      bCheckDelFlag.value = false
    })
  })
}
/**
 * 叠次加载成功
 */
const onAsyncSuccess = () => {
  ztreeObj = $.fn.zTree.getZTreeObj!('foldmediaztreeid')
  console.log(ztreeObj)
  // 根节点id的0值改为字符串
  const noderoot = ztreeObj.getNodeByParam('id', 0, null)
  if (noderoot) {
    noderoot.id = noderoot.id.toString()
  }
  console.log(noderoot)
  // 默认展开第一级节点
  const nodes = ztreeObj.getNodesByFilter((node: ITreeNode) => {
    return node.level === 0
  })
  nodes?.forEach((node: ITreeNode) => {
    ztreeObj.expandNode(node, true, false, true)
  })
  // 常用的时候拥有的默认选中，并不能取消
  if (mymenuidlist.value !== undefined) {
    mymenuidlist.value.forEach(function (id) {
      const node = ztreeObj.getNodeByParam('id', id, undefined)
      if (node) {
        // 当前节点打勾或去勾
        ztreeObj.checkNode(node, true, false)
        // node.chkDisabled = true
      }
    })
  } else {
    ztreeObj.checkAllNodes(false)
  }
}
/**
 * 勾选要批量删除叠次
 * @param event
 * @param treeId
 * @param treeNode
 */
const onCheck = (event: Event, treeId: string, treeNode: ITreeNode) => {
  if (treeNode.level === 0) {
    treeNode.checked = false
  }
  menuidlist.value = []
  const ztree = $.fn.zTree.getZTreeObj!('foldmediaztreeid')
  const nodes = ztree.getCheckedNodes(true)
  console.log(nodes)
  nodes.forEach((node: ITreeNode, index: number) => {
    if (node.level === 0) {
      node.checked = false
    }
    menuidlist.value.push({
      menuid: node.id,
      isort: index
    })
  })
  console.log(menuidlist.value)
}

/**
 * 树对象绑定
 */
const treeData: ISetting = {
  async: {
    enable: true,
    type: 'get',
    url: import.meta.env.VITE_APP_BASE_API + '/system/mymenu/getMyRolemenu',
    otherParam: {
      mediaType: '',
      getType: ''
    }
  },
  check: {
    enable: true,
    chkStyle: 'checkbox',
    chkboxType: {
      Y: 's',
      N: 's'
    }
  },
  view: {
    // addHoverDom: addHoverDom,
    // removeHoverDom: removeHoverDom,
    showLine: false,
    selectedMulti: false,
    showIcon: true,
    fontCss: function (treeid, treeNode) {
      return treeNode.buse
        ? { color: '#F56C6C', 'font-weight': '600' }
        : { color: '#606266', 'font-weight': '600' }
    }
  },
  edit: {
    // 单独设置为true时，可加载修改、删除图标
    enable: false,
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
    // onClick: onClick,
    onCheck: onCheck,
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
onMounted(() => {
  getTreeData()
  getMyMenu()
  treeheight.value = computTableHeight() - 70 + 'px'
  /* 阻止浏览器在 vue-draggable组件时拖动 打开新窗口 */
  document.body.ondrop = function (event) {
    event.preventDefault()
    event.stopPropagation()
  }
  // nextTick(() => {
  //   $.fn.zTree.init!($('#foldmediaztreeid'), treeData, [])
  //   ztreeObj = $.fn.zTree.getZTreeObj!('foldmediaztreeid')
  //   bCheckDelFlag.value = false
  // })
})
/**
 * 添加到左侧
 */
const onAdd = () => {
  if (menuidlist.value.length === 0 || menuidlist.value === undefined) {
    modal.msgWarning('请选择要添加的菜单')
    return
  }
  menuidlistdata.value.lsSelectmenu = menuidlist.value
  saveMymenuApi(menuidlistdata.value).then((res: any) => {
    if (res.success) {
      modal.msgSuccess('添加成功')
      menuidlist.value = []
      getMyMenu()
    }
  })
}
/**
 * 删除
 * @param id
 */
const ondelete = () => {
  if (checkList.value.length === 0 || checkList.value === undefined) {
    modal.msgWarning('请选择要移除的菜单')
    return
  }
  menuiddelete.value.sMenuIds = checkList.value.join(',')
  deleteMymenuApi(menuiddelete.value).then((res: any) => {
    if (res.success) {
      modal.msgSuccess('删除成功')
      mymenuidlist.value = []
      getMyMenu()
    }
  })
}
// 创建dragItem变量，用于保存正在拖拽的元素
const dragItem = ref(null)

// 在拖拽开始时，通过dragStart函数将当前拖拽的元素保存到dragItem变量中，
// 并将拖拽的数据以字符串形式存储在数据传输对象中。
function dragStart(item: any, event: any) {
  dragItem.value = item
  // 设置拖拽操作的效果为移动,
  // 这里也可以说一下拖拽的几个效果
  // 'none': 不允许拖拽操作。
  // 'copy': 拖拽操作会复制被拖拽的数据。
  // 'move': 拖拽操作会移动被拖拽的数据。
  // 'link': 拖拽操作会创建一个指向被拖拽数据的链接。
  // 在设置 effectAllowed 属性后，可以在 dragstart 和 dragover 事件中使用 dropEffect 属性来指定拖拽操作的效果。
  event.dataTransfer.effectAllowed = 'move'
  // 并将拖拽的数据以字符串形式存储在数据传输对象中。
  // 其中，item是一个JavaScript对象，通过JSON.stringify()方法将其转换为字符串。
  event.dataTransfer.setData('text/plain', JSON.stringify(item))
}

// 在拖拽过程中，使用dragOver函数监听dragover事件，
// 并调用event.preventDefault()方法，以允许元素被拖拽到新的位置。
function dragOver(event: any) {
  event.preventDefault()
  event.dataTransfer.dropEffect = 'move'
}

// 在拖拽完成时，使用drop函数将拖拽的元素替换到目标位置，并更新list数组。
function drop(item: any, event: any) {
  event.preventDefault()
  /**
   * 从数据传输对象中获取之前通过 setData() 方法存储的数据，
   * 通过 JSON.parse() 方法将其转换为对象。
   * 用于获取在拖拽操作中传递的数据。
   */
  const data = JSON.parse(event.dataTransfer.getData('text/plain'))
  console.log('data', data)
  console.log('item', item)
  // 通过 findIndex() 方法查找 list 数组中第一个满足条件的元素的索引，这里的条件是该元素的 id 属性等于 item 对象的 id 属性。
  // 被替换的元素
  const index1 = mymenutree.value.findIndex((i) => i.id === item.id)
  // 通过 findIndex() 方法查找 data 对象在 list 数组中的索引
  const index2 = mymenutree.value.findIndex((i) => i.id === data.id)
  mymenutree.value[index1] = data
  mymenutree.value[index2] = item
}
// 在拖拽结束时，通过dragEnd函数将dragItem变量重置为null。
function dragEnd(item: any) {
  dragItem.value = null
}
/**
 * 上移一位
 */
const onUp = (item: any) => {
  const index = mymenutree.value.findIndex((i) => i.id === item.id)
  if (index === 0) {
    modal.msgWarning('已经是第一位了')
    return
  }
  const temp = mymenutree.value[index]
  mymenutree.value[index] = mymenutree.value[index - 1]
  mymenutree.value[index - 1] = temp
  nextTick(() => {
    onsure()
  })
}
/**
 * 下移一位
 */
const onDown = (item: any) => {
  const index = mymenutree.value.findIndex((i) => i.id === item.id)
  if (index === mymenutree.value.length - 1) {
    modal.msgWarning('已经是最后一位了')
    return
  }
  const temp = mymenutree.value[index]
  mymenutree.value[index] = mymenutree.value[index + 1]
  mymenutree.value[index + 1] = temp
  nextTick(() => {
    onsure()
  })
}
/**
 * 确认顺序
 * */
const onsure = () => {
  var datalist = ref<TMymenu>({
    lsSelectmenu: [] as TSelectmenu[]
  })
  mymenutree.value.forEach((item: any, index: number) => {
    datalist.value.lsSelectmenu.push({
      menuid: item.menuid,
      isort: index
    })
  })
  saveMymenuApi(datalist.value).then((res: any) => {
    if (res.success) {
      modal.msgSuccess('顺序保存成功')
      getMymenuApi().then((res: any) => {
        mymenuidlist.value = []
        mymenutree.value = res.obj
        res.obj.forEach((item: any) => {
          mymenuidlist.value.push(item.menuid)
        })
      })
    }
  })
}
</script>
<style scoped>
ul {
  padding-left: 10px;
}
.userbox {
  height: v-bind(boxheight);
  padding: 20px 14% 0;
}
.left_box {
  height: v-bind(treeheight);
  overflow-y: auto;
}
.menustyle {
  line-height: 40px;
  font-size: 16px;
  color: #409eff;
  font-weight: bold;
}
</style>
