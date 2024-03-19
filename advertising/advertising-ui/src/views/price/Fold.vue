<!--
 * @Author: suny
 * @Date: 2023-09-19 15:47:44
 * @LastEditTime: 2023-11-23 16:57:51
 * @LastEditors: wanghl
 * @Description: 叠次管理
-->
<template>
  <div class="app-container">
    <el-row :gutter="5">
      <!--叠次数据-->
      <el-col :xs="8" :sm="7" :md="7" :lg="5" :xl="5">
        <div class="table_box left_box">
          <div class="fenge-box">
            <el-icon color="#409EFC"><star-filled /></el-icon><span>{{ props.typename }} 列表</span
            ><el-icon color="#409EFC"><star-filled /></el-icon>
          </div>
          <div id="scrolly">
            <div id="foldmediaztreeid" class="ztree"></div>
          </div>
        </div>
      </el-col>
      <!-- 用户数据 -->
      <el-col :xs="16" :sm="17" :md="17" :lg="19" :xl="19">
        <div class="search_box">
          <el-input
            v-model="queryKey"
            clearable
            :placeholder="'请输入' + typename + '关键字'"
            class="input-with-select"
            style="width: 200px"
            @keyup.enter="onSearch"
            @clear="onSearch"
          >
          </el-input>
          <el-button type="primary" icon="Search" @click="onSearch">搜索</el-button>
          <el-button type="danger" icon="delete" @click="onDelete(undefined)">删除</el-button>
          <el-tooltip class="item" effect="dark" content="帮助" placement="top">
            <el-button class="help" @click="showhelp()">?</el-button>
          </el-tooltip>
        </div>
        <div class="table_box">
          <el-table
            :data="foldList"
            row-key="id"
            :height="props.tableheight"
            :border="true"
            stripe
            :header-cell-style="tableHeaderColor"
            @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column prop="sname" :label="props.typename + '名称'"> </el-table-column>
            <el-table-column prop="medianame" label="所属媒体" :width="150"> </el-table-column>
            <el-table-column v-if="props.type === 'paper'" prop="pagecount" label="版数">
            </el-table-column>
            <el-table-column prop="isort" label="序号 " :width="60" align="center">
            </el-table-column>
            <el-table-column prop="buse" label="启用" :width="60" align="center">
              <template #default="scope">
                <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
              </template>
            </el-table-column>

            <el-table-column
              label="操作"
              align="center"
              width="210"
              class-name="small-padding fixed-width"
              fixed="right"
            >
              <template #default="scope">
                <el-button link type="success" icon="Edit" @click="onEdit(scope.row)"
                  >修改</el-button
                >
                <el-button link type="danger" icon="Delete" @click="onDelete(scope.row)"
                  >删除</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <!-- 页码 -->
          <el-pagination
            v-model:current-page="pageIndex"
            v-model:page-size="pageSize"
            :page-sizes="pageSizes"
            class="pagination"
            small
            background
            :disabled="pageDisabled"
            layout="total, sizes, prev, pager, next, jumper"
            :total="pageTotal"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-col>
    </el-row>
    <el-dialog v-model="editShow" :title="typename + '编辑'" width="680px" append-to-body>
      <el-form
        ref="mediaRef"
        :model="foldForm"
        :rules="rules"
        label-width="100px"
        @selection-change="handleSelectionChange"
      >
        <el-row :gutter="50">
          <el-col :span="16">
            <el-form-item :label="typename + '名称'" prop="sname">
              <el-input v-model="foldForm.sname"></el-input>
            </el-form-item>
            <el-form-item v-if="props.type === 'paper'" label="版数" prop="pagecount">
              <el-input-number v-model="foldForm.pagecount" :controls="false" :min="1" :max="100" />
            </el-form-item>
            <el-form-item label="序号" prop="isort">
              <el-input-number
                v-model="foldForm.isort"
                :min="1"
                :max="1000"
                label="序号"
              ></el-input-number>
            </el-form-item>

            <el-row :gutter="50">
              <el-col :span="8">
                <el-form-item label="启用" prop="buse">
                  <el-checkbox v-model="foldForm.buse"></el-checkbox>
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="onSave(mediaRef)">确 定</el-button>
          <el-button @click="onCancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { IDataCombo } from '@/types/common/DataCombo'
import variables from '@/assets/styles/variables.module.scss'
import { IAxios } from 'axios'
import { FormInstance, FormRules } from 'element-plus'
import { computTableHeight, computTreeHeighttab, tableHeaderColor } from '@/utils/index'
import { IFoldVO, TFoldQuery } from '@/types/views/media/fold'
import {
  deleteFoldByIdApi,
  getBFoldChildApi,
  getFoldMaxSortApi,
  getFoldPageListApi,
  saveFoldApi,
  updateFoldApi
} from '@/api/media/fold'
defineOptions({
  name: 'Fold'
})
import { useRouter } from 'vue-router'
type Props = {
  /** 传入的媒体类型key */
  type: string
  /** 传入的关键字名称 */
  typename: string
  /** 传入表格高度 */
  tableheight: number
}
const props = defineProps<Props>()
const router = useRouter()
/** Form名称 */
const mediaRef = ref<FormInstance>()
/** Form选中的行 */
const multipleSelection = ref<IFoldVO[]>([])
// 分页相关
const sort = ref('mediaid,isort')
const order = ref('asc,asc')
const pageSizes = [15, 20, 30, 40]
const pageIndex = ref(1)
const pageSize = ref(15)
const background = ref(false)
const pageDisabled = ref(false)
/** 查询总页数 */
const pageTotal = ref(0)
/** 叠次列表 */
const foldList = ref<IFoldVO[]>([])
/** 查询关键字 */
const queryKey = ref('')
const mediaid = ref('')
/** 叠次编辑显示状态 */
const editShow = ref(false)
/** 叠次编辑实体 */
const foldForm = ref<IFoldVO>({
  id: '',
  sname: '',
  mediaid: '',
  pagecount: 1,
  buse: true,
  isort: 1
})
/** 验证规则 */
const rules = reactive<FormRules<IFoldVO>>({
  sname: [
    { required: true, message: '叠次名称不能为空', trigger: 'blur' },
    { min: 1, max: 100, message: '叠次名称长度不能大于100个字符', trigger: 'change' }
  ],
  mediaid: [{ required: true, message: '媒体不能为空', trigger: 'change' }],
  pagecount: [{ required: true, message: '叠次版数不能为空', trigger: 'blur' }]
})
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
 * 在节点上显示添加节点图标按钮
 * @param {string} treeId 树id
 * @param {ITreeNode} treeNode 鼠标移动上的节点
 * @returns 显示添加按钮
 */
const addHoverDom = (treeId: string, treeNode: ITreeNode) => {
  if (treeNode.level === 0) {
    $('.remove')?.remove()
    $('.edit')?.remove()
    return
  }

  if (treeNode.level === 1) {
    $('.remove')?.remove()
    $('.edit')?.remove()
    // 5级节点限制添加按钮
    const sObj = $('#' + treeNode.tId + '_span') // 获取节点信息
    if (treeNode.editNameFlag || $('#addBtn_' + treeNode.tId).length > 0) return

    const addStr =
      "<span class='button add' id='addBtn_" +
      treeNode.tId +
      "' title='添加叠次' onfocus='this.blur();'></span>" // 定义添加按钮
    sObj?.after(addStr) // 加载添加按钮
    const btn = $('#addBtn_' + treeNode.tId)
    // 绑定添加事件，并定义添加操作
    if (btn) {
      btn.on('click', (event: any) => {
        event.stopPropagation()
        parentNode = treeNode
        onAdd(treeNode.id)
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
 * 叠次加载成功
 */
const onAsyncSuccess = () => {
  ztreeObj = $.fn.zTree.getZTreeObj!('foldmediaztreeid')
  // 根节点id的0值改为字符串
  const noderoot = ztreeObj.getNodeByParam('id', 0, null)
  if (noderoot) {
    noderoot.id = noderoot.id.toString()
  }
  // 默认展开第一级节点
  const nodes = ztreeObj.getNodesByFilter((node: ITreeNode) => {
    return node.level === 0
  })
  nodes?.forEach((node: ITreeNode) => {
    ztreeObj.expandNode(node, true, false, true)
  })
}
/**
 * 单击叠次节点
 * @param event
 * @param treeId
 * @param treeNode
 */
const onClick = (event: Event, treeId: string, treeNode: ITreeNode) => {
  // 清空勾选，隐藏调整按钮显示保存按钮
  ztreeObj.checkAllNodes(false)
  if (treeNode.level! > 1) {
    return
  }
  parentNode = treeNode.getParentNode!()
  if (treeNode.level === 1) {
    mediaid.value = treeNode.id
  } else {
    mediaid.value = ''
  }
  queryKey.value = ''
  onSearch()
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
    // ElMessage.error('节点不允许选择')
    // return
  }
  const ztree = $.fn.zTree.getZTreeObj!('foldmediaztreeid')
  const nodes = ztree.getCheckedNodes(true)

  selectNodeIds.value = []
  for (const i in nodes) {
    if (nodes[i].level === 1) {
      // nodes[i].checked = false
      continue
    }
    selectNodeIds.value.push(nodes[i].id)
  }

  ztree.refresh()
  if (selectNodeIds.value.length >= 1) {
    // 勾选多个节点，只显示调整和删除按钮
    bCheckDelFlag.value = true
  } else {
    // 不选节点，显示保存和删除按钮
    bCheckDelFlag.value = false
  }
}

/**
 * 删除叠次
 * @param treeId
 * @param treeNode
 */
const beforeRemove = (treeId: string, treeNode: ITreeNode) => {
  var flag = false
  const foldVO: IFoldVO = {
    id: treeNode.id,
    mediaid: treeNode.parentid,
    sname: treeNode.name as string
  }
  onDelete(foldVO)
  return flag
}
console.log(props.type)
/**
 * 树对象绑定
 */
const treeData: ISetting = {
  async: {
    enable: true,
    type: 'get',
    url: import.meta.env.VITE_APP_BASE_API + '/media/fold/getMediaFoldTree',
    otherParam: {
      mediaType: props.type,
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
    addHoverDom: addHoverDom,
    removeHoverDom: removeHoverDom,
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
  getFoldPageList()
  nextTick(() => {
    $.fn.zTree.init!($('#foldmediaztreeid'), treeData, [])
    treeheight.value = props.tableheight + 85 + 'px'

    ztreeObj = $.fn.zTree.getZTreeObj!('foldmediaztreeid')
    bCheckDelFlag.value = false
  })
})

const onSearch = () => {
  pageIndex.value = 1
  getFoldPageList()
}

/**
 * 获取叠次最大序号
 */
const getFoldMaxSort = () => {
  getFoldMaxSortApi().then((res: IAxios) => {
    if (res.success) {
      foldForm.value.isort = res.obj
    }
  })
}

/**
 * 获取叠次分页列表
 */
const getFoldPageList = () => {
  const data: TFoldQuery = {
    sort: sort.value,
    order: order.value,
    pageSize: pageSize.value,
    page: pageIndex.value,
    queryKey: queryKey.value,
    mediaid: mediaid.value,
    mediatypename: props.type
  }
  getFoldPageListApi(data).then((res: IAxios<IFoldVO[]>) => {
    if (res.success) {
      foldList.value = res.obj
      pageTotal.value = res.total
    }
  })
}
/**
 * 新增按钮事件
 */
const onAdd = (mediaid: string) => {
  mediaRef.value?.resetFields()
  clearForm()
  getFoldMaxSort()
  foldForm.value.mediaid = mediaid
  editShow.value = true
}
/**
 * 编辑叠次按钮事件
 * @param row
 */
const onEdit = (row: IFoldVO) => {
  editShow.value = true
  foldForm.value = { ...row }
  console.log(foldForm.value)
}
/**
 * 删除叠次按钮事件
 * @param row
 */
const onDelete = (row: IFoldVO | undefined) => {
  let ids: string[] = []
  if (row === undefined) {
    if (bCheckDelFlag.value) {
      ids = selectNodeIds.value
    }
    multipleSelection.value.forEach((item) => {
      ids.push(item.id?.toString() as string)
    })
  } else {
    ids.push(row.id?.toString() as string)
  }

  if (ids.length === 0) {
    ElMessage.warning('请选择要删除的数据')
    return
  }
  const data = {
    ids: ids.join(',')
  }
  getBFoldChildApi(data).then((res: IAxios) => {
    if (res.success) {
      if (res.msg !== '') {
        ElMessage.error(res.msg + ' 请先将叠次下的相关属性删除')
        return
      }
      ElMessageBox.confirm('是否删除选择的叠次？', '提示', {
        type: 'warning'
      })
        .then(() => {
          deleteFoldByIdApi(data)
            .then((res: IAxios) => {
              if (res.success) {
                const ztree = $.fn.zTree.getZTreeObj!('foldmediaztreeid')
                ids.forEach((item) => {
                  const node = ztree.getNodeByParam('id', item, undefined)
                  ztree.removeNode(node)
                })

                ElMessage.success('删除成功!')
                ztree.refresh()
                getFoldPageList()
                bCheckDelFlag.value = false
              }
            })
            .catch(() => {})
        })
        .catch(() => {})
    }
  })
}
/**
 * 新增修改保存事件
 * @param formEl
 */
const onSave = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid, fields) => {
    if (valid) {
      if (foldForm.value.sname.trim() === '') {
        ElMessage.warning('叠次名称不可为空')
        return
      }
      if (foldForm.value.pagecount === undefined) {
        ElMessage.warning('叠次版数不可为空')
        return
      }
      if (foldForm.value.id === '') {
        saveFoldApi(foldForm.value)
          .then((res: IAxios) => {
            if (res.success) {
              ElMessage.success('保存成功')
              getFoldPageList()
              ztreeObj.addNodes(parentNode, {
                name: foldForm.value.sname,
                iconSkin: 'cata',
                id: res.obj.id
              })
              mediaRef.value?.resetFields()
              editShow.value = false
            }
          })
          .catch(() => {})
      } else {
        updateFoldApi(foldForm.value)
          .then((res: IAxios) => {
            if (res.success) {
              const node = ztreeObj.getNodeByParam('id', foldForm.value.id)
              node.name = foldForm.value.sname
              ztreeObj.updateNode(node)
              ElMessage.success('更新保存成功')
              getFoldPageList()
              mediaRef.value?.resetFields()
              editShow.value = false
            }
          })
          .catch(() => {})
      }
    } else {
      console.log('error submit!', fields)
    }
  })
}
/**
 * 取消编辑
 */
const onCancel = () => {
  mediaRef.value?.resetFields()
  clearForm()
  editShow.value = false
}
/**
 * 清空编辑form内容
 */
const clearForm = () => {
  foldForm.value = {
    id: '',
    sname: '',
    mediaid: '',
    pagecount: 1,
    buse: true,
    isort: 1
  }
}
/**
 * 翻页
 * @param val
 */
const handleSizeChange = (val: number) => {
  // 分页当前页
  pageIndex.value = val
  getFoldPageList()
}
/**
 * 当前页
 * @param val
 */
const handleCurrentChange = (val: number) => {
  pageIndex.value = val
  getFoldPageList()
}
/**
 * 行选择事件
 * @param val
 */
const handleSelectionChange = (val: IFoldVO[]) => {
  multipleSelection.value = val
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'fold'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>

<style scoped>
.left_box {
  height: v-bind(treeheight);
}
</style>
