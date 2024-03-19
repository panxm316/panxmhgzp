<!--
 * @Author: muyn
 * @Date: 2023-08-24 15:06:40
 * @LastEditTime: 2024-03-19 08:35:31
 * @LastEditors: songly
 * @Description:广告行业
-->
<template>
  <div id="adindustryform" class="app-container">
    <el-row :gutter="5">
      <!--行业数据-->
      <el-col :xs="8" :sm="7" :md="6" :lg="5" :xl="5">
        <div class="table_box left_box">
          <div class="fenge-box">
            <el-icon color="#409EFC"><star-filled /></el-icon><span>行业列表</span
            ><el-icon color="#409EFC"><star-filled /></el-icon>
          </div>
          <div class="head-container">
            <el-input
              v-model="queryKey"
              placeholder="请输入行业名称"
              class="input-with-select"
              style="margin-bottom: 20px"
            >
              <template #append>
                <el-button icon="Search" @click="searchAdIndustry" />
              </template>
            </el-input>
          </div>
          <div id="scrolly">
            <div id="adIndustryztreeid" class="ztree"></div>
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
          <el-form ref="adIndustryRef" :model="adIndustryData" :rules="rules" label-width="100px">
            <el-row :gutter="50">
              <el-col :span="14" :offset="4">
                <el-form-item label="隶属行业" prop="parentid">
                  <el-input v-model="currParentName" readonly></el-input>
                </el-form-item>
                <el-form-item label="序号" prop="isort">
                  <el-input-number
                    v-model="adIndustryData.isort"
                    :min="1"
                    :max="10000"
                    label="序号"
                    :disabled="bFormEditFlag"
                  ></el-input-number>
                </el-form-item>
                <el-form-item label="行业名称" prop="sname">
                  <el-input v-model="adIndustryData.sname" :disabled="bFormEditFlag"></el-input>
                </el-form-item>
                <el-form-item label="备注" prop="sremark">
                  <el-input v-model="adIndustryData.sremark" :disabled="bFormEditFlag"></el-input>
                </el-form-item>
                <el-form-item label="是否分类广告" prop="bclassified">
                  <el-checkbox
                    v-model="adIndustryData.bclassified"
                    :disabled="editbclassified !== 'true' ? true : false"
                  ></el-checkbox>
                </el-form-item>
                <el-form-item label="启用" prop="buse">
                  <el-checkbox
                    v-model="adIndustryData.buse"
                    :disabled="bFormEditFlag"
                  ></el-checkbox>
                </el-form-item>
                <el-form-item label="文件上传">
                  <span v-for="(item, index) in adindustrylist" :key="index"
                    ><a :href="item" target="_blank" style="color: #409eff">{{ item }}</a
                    ><el-button
                      link
                      type="danger"
                      icon="CircleClose"
                      @click="adindustrylistDel(index)"
                    ></el-button
                  ></span>

                  <el-row v-if="!bFormEditFlag" style="width: 100%">
                    <el-col :span="6">
                      <HgFileUpload
                        :server="hgfileuploadparam.server"
                        :accept="hgfileuploadparam.accept"
                        :multiple="true"
                        :filenumlimit="5"
                        :storytypes="hgfileuploadparam.storytypes"
                        @getupfile="getUpFile"
                      ></HgFileUpload>
                    </el-col>
                  </el-row>
                  <el-row v-if="!bFormEditFlag">
                    <el-col :span="24">
                      <div v-for="(file, index) in WcustomerFileslsit" :key="file.sfileid">
                        <el-link type="primary" :underline="false" @click="previewFile(file)">
                          <el-icon><Document /></el-icon> {{ file.soriginalfile }}</el-link
                        >
                        <el-button
                          link
                          type="danger"
                          icon="CircleClose"
                          @click="onUpfileDel(index)"
                        ></el-button>
                      </div>
                      <span
                        v-if="WcustomerFileslsit.length < 1"
                        style="color: #e6a23c; font-size: 13px"
                        ><span
                          ><span
                            v-if="adIndustryData.picturepath !== ''"
                            style="font-size: 13px; color: red"
                            >重新上传</span
                          ></span
                        >请上传附件(图片格式)</span
                      >
                    </el-col>
                  </el-row>
                </el-form-item>

                <el-form-item>
                  <el-button
                    type="primary"
                    icon="Check"
                    :disabled="bFormEditFlag"
                    @click="submitForm"
                    >确 定</el-button
                  >
                  <el-button icon="close" :disabled="bFormEditFlag" @click="cancel"
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
  getTbAdIndustryByIdApi,
  deleteTbAdIndustryByIdApi,
  saveTbAdIndustryApi,
  updateTbAdIndustryApi,
  getMaxSortApi,
  resetAdIndustryExtendApi
} from '@/api/ad/adindustry'
import useUserStore from '@/store/modules/user'
import { TAdIndustry, TAdIndustrySearch } from '@/types/views/ad/adindustry'
import { ElFormItem, FormInstance, FormRules } from 'element-plus'
import { computEditHeight, computTreeHeight } from '@/utils/index'
import { THgFileUploadParam, TUpFile } from '@/types/components/hgfileupload'

defineOptions({
  name: 'AdIndustry'
})
import { useRouter } from 'vue-router'
import { fa } from 'element-plus/es/locale'
const router = useRouter()
const adIndustryData = ref<TAdIndustry>({
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
   * 本级编号
   */
  slocalid: '',
  /**
   * 类别级别
   */
  idepth: 1,
  /**
   * 类别解释
   */
  sdesc: '',
  /**
   * 备注
   */
  sremark: '',
  /**
   * 文件路径
   */
  sfilepath: '',
  /**
   * 文件名称
   */
  sfilename: '',
  /**
   * 序号
   */
  isort: 1,
  /**
   * 是否分类广告
   */
  bclassified: false,
  /**
   * 节点禁用
   */
  buse: true,
  /**
   * 链接
   */
  sdurl: '',
  /**
   * 文件
   */
  picturepath: ''
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
/** 编辑分类广告标志 */
const editbclassified = ref('')

const adIndustryRef = ref<FormInstance>()
/** 验证规则 */
const rules = reactive<FormRules<TAdIndustry>>({
  sname: [
    { required: true, message: '请输入行业名称', trigger: 'blur' },
    { min: 2, max: 15, message: '行业名称长度在2-15个字符之间', trigger: 'blur' }
  ]
})
/** 允许编辑状态 */
const bFormEditFlag = ref(true)
/** 当前选中节点的名称 */
const currParentName = ref<string | undefined>('')
/** 查询行业关键字 */
const queryKey = ref('')
/** 原始parentid */
let oldParentId: string = ''
/**
 * 上传图片后的转换数据
 */
const WcustomerFileslsit = ref<TUpFile[]>([])
/**
 * 点击左侧时候的返回数据接收
 */
const adindustrylist = ref([])
const adindustrynamelist = ref([])
onMounted(() => {
  // getList()
  /**
   * 计算表格高度
   */
  editheight.value = computEditHeight()
  treeheight.value = computTreeHeight()
  nextTick(() => {
    $.fn.zTree.init!($('#adIndustryztreeid'), treedata, [])
    ztreeObj = $.fn.zTree.getZTreeObj!('adIndustryztreeid')
    bFormEditFlag.value = true
  })
})

/** 上传组件参数 */
const hgfileuploadparam = ref<THgFileUploadParam>({
  server: import.meta.env.VITE_APP_BASE_API + '/ad/adindustry/upLoadIndustryFile',
  multiple: true,
  filenumlimit: 1,
  storytypes: []
})
/**
 * 获取上传后返回的文件列表
 * @param res
 */
const getUpFile = (res: IAxios<TUpFile>) => {
  console.log(res.obj)
  if (res.success) {
    const workreport = res.obj as TUpFile
    WcustomerFileslsit.value.push(workreport)
    WcustomerFileslsit.value.map((item) => {
      return {
        id: '',
        sfileformat: item.sfileformat,
        sfileid: item.sfileid,
        soriginalfile: ''
      }
    })
    console.log(WcustomerFileslsit.value)
    return
  }
}
/**
 * 删除上传的文件，如果是新上传文件则直接删除，已经存入数据库中的则修改删除标记，用户数据库文件删除
 * @param index
 */
const onUpfileDel = (index: number) => {
  WcustomerFileslsit.value!.splice(index, 1)
}
/**
 * 删除选中的
 */
const adindustrylistDel = (index: number) => {
  adindustrylist.value!.splice(index, 1)
  adindustrynamelist.value!.splice(index, 1)
}
/**
 * 预览上传的文件
 * @param file
 */
const previewFile = (file: TUpFile) => {
  console.log(file)
  if (file.sfiletype === 'Photo') {
    window.open(file.durl)
  }
}
/**
 * 在节点上显示添加、删除图标按钮
 * @param {string} treeId 树id
 * @param {ITreeNode} treeNode 鼠标移动上的节点
 * @returns 显示添加按钮
 */
const addHoverDom = (treeId: string, treeNode: ITreeNode) => {
  console.log(treeNode)
  if (treeNode.level === 0) {
    $('.remove')?.remove()
    editbclassified.value = 'true'
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
      "' title='添加子行业' onfocus='this.blur();'></span>" // 定义添加按钮
    sObj?.after(addStr) // 加载添加按钮
    const btn = $('#addBtn_' + treeNode.tId)
    // 绑定添加事件，并定义添加操作
    if (btn) {
      btn.on('click', (event: any) => {
        adindustrylist.value = []
        adindustrynamelist.value = []
        event.stopPropagation()
        reset()
        showAddBtnFlag = true
        bFormEditFlag.value = false
        addOrUpdate = 2 // 保存状态改为新增子节点
        parentNode = treeNode
        oldParentId = treeNode.id
        currParentName.value = treeNode.name
        adIndustryData.value.parentid = treeNode.id
        adIndustryData.value.buse = false
        // 一级行业如果设置为分类广告，那么子行业都是分类广告，子行业的上级如果不是分类广告，则子行业不能修改为分类广告
        adIndustryData.value.bclassified = treeNode.stype
        editbclassified.value = treeNode.stype
        if (editbclassified.value === 'true') {
          adIndustryData.value.bclassified = true
        } else {
          adIndustryData.value.bclassified = false
        }
        if (treeNode.level === 0) {
          editbclassified.value = 'true'
        } else {
          editbclassified.value = 'false'
        }
        adIndustryData.value.idepth = treeNode.level !== undefined ? treeNode.level + 1 : 1
        getMaxSortApi().then((res: IAxios) => {
          if (res.success) {
            adIndustryData.value.isort = res.obj
          }
        })
        if (treeNode.level === 0) {
          adIndustryData.value.buse = true
        } else {
          getTbAdIndustryByIdApi(treeNode.id).then((res: IAxios) => {
            if (res.success) {
              adIndustryData.value = res.obj
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
 * 行业加载成功
 */
const onAsyncSuccess = () => {
  ztreeObj = $.fn.zTree.getZTreeObj!('adIndustryztreeid')
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
  if (adIndustryData.value.parentid !== '') {
    const treenode = ztreeObj.getNodeByParam('id', adIndustryData.value.parentid, null)
    ztreeObj.expandNode(treenode, true, false, true)
  }
}
/**
 * 单击行业节点
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
  adIndustryRef.value?.clearValidate()
  // 点击树保存状态改为修改
  parentNode = treeNode.getParentNode!()
  oldParentId = parentNode.id
  // updatepidflag = treeNode.id // 是否调整了行业  记录行业id
  if (treeNode.level !== 0) {
    bFormEditFlag.value = false
    showAddBtnFlag = true
    getTbAdIndustryByIdApi(treeNode.id).then((res: IAxios) => {
      if (res.success) {
        adIndustryData.value = res.obj
        if (res.obj.sdurl !== '') {
          adindustrylist.value = res.obj.sdurl.split(';')
        } else {
          adindustrylist.value = []
        }
        if (res.obj.picturepath !== '') {
          adindustrynamelist.value = res.obj.picturepath.split(';')
        } else {
          adindustrynamelist.value
        }
        // editbclassified.value = adIndustryData.value.bclassified.toString()
        editbclassified.value = 'false'
        const pnode = treeNode.getParentNode!()
        if (pnode != null) {
          // 隶属行业赋值
          currParentName.value = pnode.name
          adIndustryData.value.parentid = pnode.id
        } else {
          currParentName.value = ''
          adIndustryData.value.parentid = ''
        }
        addOrUpdate = 0 // 保存按钮置为修改状态
      }
    })
  }
}

/**
 * 删除行业
 * @param treeId
 * @param treeNode
 */
const beforeRemove = (treeId: string, treeNode: ITreeNode) => {
  // 删除前判断
  if (treeNode.childrenNodes && treeNode.childrenNodes.length > 0) {
    // 是否有子节点
    flag = false
    modal.msgWarning('请先删除子行业')
    return false
  } else {
    if (treeNode.level === 1 && adminFlag !== 2) {
      flag = false
      modal.msgWarning('无权删除一级行业')
      return false
    }

    var flag = false
    modal
      .confirm('确定删除该行业?')
      .then(() => {
        deleteTbAdIndustryByIdApi(treeNode.id).then((res: IAxios) => {
          if (res.success) {
            reset()
            const ztree = $.fn.zTree.getZTreeObj!('adIndustryztreeid')
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
    url: import.meta.env.VITE_APP_BASE_API + '/ad/adindustry/getTbAdIndustryTree',
    otherParam: {
      queryKey: '',
      rootName: '行业',
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
 * 根据行业关键字检索数据
 */
const searchAdIndustry = () => {
  treedata.async!.otherParam = {
    queryKey: queryKey.value,
    rootName: '行业',
    showRoot: true
  }
  $.fn.zTree.init!($('#adIndustryztreeid'), treedata, [])
}
/**
 * 获取最大序号
 */
const getMaxSort = async () => {
  await getMaxSortApi().then(({ success, obj }: IAxios) => {
    if (success) {
      adIndustryData.value.isort = obj
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
  adIndustryData.value = {
    id: '',
    parentid: '',
    sname: '',
    slocalid: '',
    idepth: 1,
    sdesc: '',
    sremark: '',
    sfilepath: '',
    sfilename: '',
    isort: 1,
    bclassified: false,
    buse: true,
    sdurl: '',
    picturepath: ''
  }
  adIndustryRef.value?.resetFields()
  WcustomerFileslsit.value = []
  adindustrynamelist.value = []
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
  adIndustryRef.value?.validate((valid: any) => {
    if (valid) {
      // 获取当前选中行业子节点校验
      const treenode = ztreeObj.getNodeByParam('id', adIndustryData.value.id, null)
      if (treenode && adIndustryData.value.parentid !== oldParentId) {
        let strid = adIndustryData.value.id
        strid = getzTreeChildrenNode(treenode, strid)
        if (
          adIndustryData.value.parentid !== '' &&
          adIndustryData.value.parentid !== '0' &&
          strid?.indexOf(adIndustryData.value.parentid, 0) >= 0
        ) {
          modal.msgWarning('隶属行业不能是自己或其下子节点！')
          return false
        }
      }
      // if (parentNode.id === '0' && adminFlag !== 2) {
      //   modal.msgWarning('无权限设置一级行业！')
      //   return false
      // }
      /**
       * 遍历数组转换为字符串
       */
      const str = ref<string[]>([])
      const data = ref('')
      WcustomerFileslsit.value.forEach((item) => {
        str.value.push(item.sfileid + item.sfileformat)
      })
      if (adindustrynamelist.value.length > 0) {
        data.value = ';' + adindustrynamelist.value.join(';')
      }
      adIndustryData.value.picturepath = str.value.join(';') + data.value
      console.log(adIndustryData.value)
      // 更新节点
      if (adIndustryData.value.id !== '') {
        updateTbAdIndustryApi(adIndustryData.value).then((res: IAxios) => {
          if (res.success) {
            ztreeObj = $.fn.zTree.getZTreeObj!('adIndustryztreeid')
            if (adIndustryData.value.buse === false) {
              $.fn.zTree.init!($('#adIndustryztreeid'), treedata, [])
            } else {
              if (adIndustryData.value.parentid === oldParentId) {
                const node = ztreeObj.getNodeByParam('id', adIndustryData.value.id)
                node.name = adIndustryData.value.sname
                ztreeObj.updateNode(node)
              } else {
                const node = ztreeObj.getNodeByParam('id', adIndustryData.value.id)
                node.name = adIndustryData.value.sname
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
        console.log(adIndustryData.value)
        saveTbAdIndustryApi(adIndustryData.value).then((res: IAxios) => {
          if (res.success) {
            modal.msgSuccess('新增成功!')
            ztreeObj.addNodes(parentNode, {
              name: adIndustryData.value.sname,
              iconSkin: 'unit',
              id: res.obj.id,
              buse: res.obj.buse,
              stype: adIndustryData.value.bclassified.toString()
            })
            reset()
          }
        })
      }
    }
  })
}
/**
 * 重置行业扩展
 */
const resetExtend = () => {
  modal.confirm('确定要重置行业扩展吗').then(() => {
    resetAdIndustryExtendApi().then((res: IAxios) => {
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
  const to = '/template/Help?name=' + 'adindustry'
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
