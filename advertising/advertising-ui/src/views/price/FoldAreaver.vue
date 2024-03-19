<!--
 * @Author: songly
 * @Date: 2023-09-19 12:45:19
 * @LastEditTime: 2023-11-23 16:58:27
 * @LastEditors: wanghl
 * @Description:叠次版本
-->

<template>
  <div id="foldareaverform" class="app-container">
    <el-row :gutter="5">
      <!--部门数据-->
      <el-col :xs="8" :sm="7" :md="7" :lg="5" :xl="5">
        <div class="table_box left_box">
          <div class="fenge-box">
            <el-icon color="#409EFC"><star-filled /></el-icon><span> {{ typename }}列表</span
            ><el-icon color="#409EFC"><star-filled /></el-icon>
          </div>
          <div id="scrolly">
            <div id="areavermediaztreeid" class="ztree"></div>
          </div>
        </div>
      </el-col>
      <!-- 用户数据 -->
      <el-col :xs="16" :sm="17" :md="17" :lg="19" :xl="19">
        <div class="search_box">
          <div class="flex">
            <!-- <el-button type="primary" icon="Plus" @click="onAdd">新增</el-button> -->
            <el-button type="danger" icon="Delete" @click="onDelete()">删除</el-button>
            <!-- <el-select
              v-model="mediaId"
              placeholder="媒体信息"
              style="width: 160px"
              @change="getFoldInfo(mediaId, false)"
            >
              <el-option
                v-for="item in mediaDatas"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
            <el-select
              v-model="foldId"
              placeholder="叠次信息"
              style="width: 120px"
              @change="loaddata()"
            >
              <el-option label="全部" value=""></el-option>
              <el-option
                v-for="item in foldDatas"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select> -->
            <el-input
              v-model="queryKey"
              placeholder="请输名称"
              clearable
              style="width: 200px"
              @clear="loaddata"
              @keyup.enter="loaddata"
            />
            <el-button type="primary" icon="Search" @click="loaddata">搜索</el-button>
            <el-tooltip class="item" effect="dark" content="帮助" placement="top">
              <el-button class="help" @click="showhelp()">?</el-button>
            </el-tooltip>
          </div>
        </div>
        <div class="table_box">
          <el-row>
            <el-table
              :data="foldareaverList"
              row-key="id"
              highlight-current-row
              :height="props.tableheight"
              :border="true"
              stripe
              :header-cell-style="tableHeaderColor"
              @sort-change="sortChange"
              @selection-change="handleSelectionChange"
            >
              <el-table-column type="selection" width="50" align="center"></el-table-column>

              <el-table-column
                prop="sname"
                label="名称"
                min-width="120"
                sortable="custom"
                show-overflow-tooltip
              >
              </el-table-column>
              <el-table-column prop="medianame" label="媒体名称" min-width="120"> </el-table-column>

              <el-table-column
                prop="foldname"
                :label="typename"
                min-width="100"
                show-overflow-tooltip
              >
              </el-table-column>
              <el-table-column prop="dstartdate" label="开始日期" width="120">
                <template #default="scope">
                  <span>{{ hgFormatDate(scope.row.dstartdate, 'YYYY-MM-DD') }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="denddate" label="结束日期" width="120">
                <template #default="scope">
                  <span>{{ hgFormatDate(scope.row.denddate, 'YYYY-MM-DD') }}</span>
                </template>
              </el-table-column>
              <el-table-column
                prop="isort"
                label="序号"
                width="80"
                sortable="custom"
                align="center"
              ></el-table-column>
              <el-table-column prop="buse" label="启用" width="80" align="center">
                <template #default="scope">
                  <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
                </template>
              </el-table-column>
              <el-table-column
                prop="sremark"
                label="备注"
                min-width="110"
                :show-overflow-tooltip="true"
              >
              </el-table-column>
              <el-table-column
                label="操作"
                align="center"
                width="200"
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
          </el-row>
          <el-row>
            <el-pagination
              v-model:current-page="currentPage"
              background
              class="pagination"
              style="margin-left: 10px; width: 100%"
              :page-sizes="pageSizes"
              :page-size="pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="pageTotal"
              small
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            >
            </el-pagination>
          </el-row>
        </div>
      </el-col>
    </el-row>
    <!-- 添加或修改叠次版本对话框 -->
    <el-dialog v-model="dialogVisible" width="700px" align-center :title="typename + '编辑'">
      <el-form ref="ruleFormRef" :rules="rules" :model="foldareaverData" label-width="100px">
        <el-row :gutter="50">
          <el-col :span="22">
            <!-- <el-form-item label="媒体">
              <el-select
                v-model="mediaIdEdit"
                placeholder="媒体信息"
                @change="getFoldInfo(mediaIdEdit, true)"
              >
                <el-option
                  v-for="item in mediaDatas"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="叠次">
              <el-select v-model="foldIdEdit" placeholder="叠次信息">
                <el-option
                  v-for="item in foldDatasEdit"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item> -->
            <el-form-item label="名称" prop="sname">
              <el-input v-model="foldareaverData.sname" />
            </el-form-item>
            <el-form-item label="序号" prop="isort">
              <el-input-number v-model="foldareaverData.isort" :min="1" controls-position="right" />
            </el-form-item>
            <el-form-item label="开始日期" prop="dstartdate">
              <el-date-picker
                v-model="foldareaverData.dstartdate"
                :editable="false"
                :picker-options="pickerOptions"
                type="date"
                placeholder="选择日期"
                value-format="YYYY-MM-DD HH:mm:ss"
                :clearable="false"
              ></el-date-picker>
            </el-form-item>
            <el-form-item label="结束日期" prop="denddate">
              <el-date-picker
                v-model="foldareaverData.denddate"
                :editable="false"
                :picker-options="pickerOptions"
                type="date"
                placeholder="选择日期"
                value-format="YYYY-MM-DD HH:mm:ss"
                :clearable="false"
              ></el-date-picker>
            </el-form-item>
            <el-form-item label="备注" prop="sremark">
              <el-input v-model="foldareaverData.sremark" type="textarea" :maxlength="200" />
            </el-form-item>
            <el-form-item label="启用" prop="buse">
              <el-checkbox v-model="foldareaverData.buse"></el-checkbox>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" icon="Check" @click="onSubmitForm(ruleFormRef)">保存</el-button>
          <el-button icon="Close" @click="onResetForm(ruleFormRef)">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import type { IAxios } from 'axios'
import {
  getFoldAreaverPageListApi,
  deleteFoldAreaverByIdApi,
  saveFoldAreaverApi,
  updateFoldAreaverApi,
  getMaxSortApi,
  getFoldComboApi,
  getMediaDataComboByTypeApi
} from '@/api/media/foldareaver'
import {
  required,
  computTableHeight,
  tableHeaderColor,
  hgFormatDate,
  computTreeHeighttab
} from '@/utils/index'
import { IFoldAreaver, TFoldAreaverVo } from '@/types/views/media/foldareaver'
import { ElFormItem, FormInstance, FormRules } from 'element-plus'
import type { TQueryInfo } from '@/types/common/index'
import modal from '@/plugins/modal' // 提示统一用这个吧  就是要自己引入
import { IDataCombo } from '@/types/common/DataCombo'
import { number } from 'echarts'
import { all } from 'axios'

defineOptions({
  name: 'FoldAreaver'
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
const foldareaverData = ref<TFoldAreaverVo>({
  id: undefined,
  foldid: '',
  sname: '',
  isort: 0,
  dstartdate: '',
  denddate: '',
  foldname: '',
  medianame: '',
  mediaid: '',
  sremark: '',
  buse: true,
  mediatypekey: props.type
})

const pickerOptions = ref({
  disabledDate: function (time: Date) {
    return time.getTime() < Date.now() - 8.64e7
  }
})
/** 列表数据 */
const foldareaverList = ref([]) // 菜单数据
/** 树高度 */
const treeheight = ref('')
const refreshTable = ref(true)
/** 查询媒体Id */
const mediaId = ref('')
/** 媒体数据 */
const mediaDatas = ref<IDataCombo[]>([])
/** 查询叠次Id */
const foldId = ref('')
/** 叠次数据*/
const foldDatas = ref<IDataCombo[]>([])
/** 编辑媒体Id*/
const mediaIdEdit = ref('')
/** 编辑叠次Id*/
const foldIdEdit = ref('')
/** 叠次数据编辑*/
const foldDatasEdit = ref<IDataCombo[]>([])
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 页数 */
const currentPage = ref<number>(1)
/** 每页条数 */
const pageSize = ref<number>(20)
/** 数据总条数 */
const pageTotal = ref<number>(0)
/** 排序字段 */
const sort = ref('isort')
/** 正序倒叙 */
const order = ref('')
/** 关键字 */
const queryKey = ref('')
/** 表单ref */
const ruleFormRef = ref<FormInstance>()
/** 校验设置参数 */
const rules = reactive<FormRules<IFoldAreaver>>({
  sname: [{ required: true, message: '请输入名称', trigger: 'blur' }]
})
/** 弹出添加修改标志 */
const dialogVisible = ref(false)
/** 列表多选数据 */
const multipleSelection = ref<TFoldAreaverVo[]>([])
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
 * 媒体id
 */
const mediaid = ref('')
/**
 * 监听传入id
 */
watch(
  () => props.type,
  (newValue, oldValue) => {
    console.log(newValue)
    loaddata()
  }
)
/**
 * 在节点上显示添加节点图标按钮
 * @param {string} treeId 树id
 * @param {ITreeNode} treeNode 鼠标移动上的节点
 * @returns 显示添加按钮
 */
const addHoverDom = (treeId: string, treeNode: ITreeNode) => {
  if (treeNode.level === 0 || treeNode.level === 1) {
    $('.remove')?.remove()
    $('.edit')?.remove()
    return
  }
  // if (treeNode.level === 1) {
  //   onAdd(treeNode.id, '')
  // }
  if (treeNode.level === 2) {
    $('.remove')?.remove()
    $('.edit')?.remove()
    // 5级节点限制添加按钮
    const sObj = $('#' + treeNode.tId + '_span') // 获取节点信息
    if (treeNode.editNameFlag || $('#addBtn_' + treeNode.tId).length > 0) return

    const addStr =
      "<span class='button add' id='addBtn_" +
      treeNode.tId +
      "' title='添加版面类别' onfocus='this.blur();'></span>" // 定义添加按钮
    sObj?.after(addStr) // 加载添加按钮
    const btn = $('#addBtn_' + treeNode.tId)
    // 绑定添加事件，并定义添加操作
    if (btn) {
      btn.on('click', (event: any) => {
        Reset()

        event.stopPropagation()
        parentNode = treeNode
        onAdd(treeNode.parentId, treeNode.id)
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
  ztreeObj = $.fn.zTree.getZTreeObj!('areavermediaztreeid')
  const node = ztreeObj.getNodes()
  console.log(node)
  // 根节点id的0值改为字符串
  const noderoot = ztreeObj.getNodeByParam('id', foldId.value, null)
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
  if (node[0].childrenNodes != null) {
    ztreeObj.selectNode(node[0].childrenNodes[0])
  }
  if (node.length > 0) {
    ztreeObj.selectNode(noderoot)
    if (node[0].childrenNodes != null) {
      ztreeObj.expandNode(node[0].childrenNodes[0], true, false, true)
      mediaId.value = node[0].childrenNodes[0].id
    }
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
  if (treeNode.level === 0) {
    foldId.value = ''
    mediaId.value = ''
  }
  if (treeNode.level === 1) {
    foldId.value = ''
    mediaId.value = treeNode.id
  }
  if (treeNode.level === 2) {
    foldId.value = treeNode.id
    mediaId.value = treeNode.parentId
  }
  loaddata()
}

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
  // treeheight.value = computTreeHeighttab()
  nextTick(() => {
    $.fn.zTree.init!($('#areavermediaztreeid'), treeData, [])
    ztreeObj = $.fn.zTree.getZTreeObj!('areavermediaztreeid')
    bCheckDelFlag.value = true
    treeheight.value = props.tableheight + 85 + 'px'
  })
})
/** 获取媒体信息*/
// const getMediaInfo = () => {
//   getMediaDataComboByTypeApi('报刊').then((respose) => {
//     mediaDatas.value = respose.obj
//     mediaId.value = mediaDatas.value[0].id
//     getFoldInfo(mediaId.value, false)
//   })
// }
/** 获取叠次信息*/
// const getFoldInfo = (curmediaId: string, bEditFold: boolean) => {
//   getFoldComboApi(curmediaId).then((respose) => {
//     if (bEditFold) {
//       foldDatasEdit.value = respose.obj
//       if (foldDatasEdit.value.length > 0) {
//         foldIdEdit.value = foldDatasEdit.value[0].id
//       }
//     } else {
//       foldDatas.value = respose.obj
//       if (foldDatas.value.length > 0) {
//         foldId.value = '' // foldDatas.value[0].id
//       }
//       loaddata()
//     }
//   })
// }

/** 查询 */
const loaddata = () => {
  const data: TQueryInfo & { foldid?: string; mediaid: string; mediatypekey: string } = {
    sort: sort.value,
    order: order.value,
    pageSize: pageSize.value,
    page: currentPage.value,
    queryKey: queryKey.value,
    foldid: foldId.value,
    mediaid: mediaId.value,
    mediatypekey: props.type
  }
  getFoldAreaverPageListApi(data).then((response) => {
    console.log(response)
    foldareaverList.value = response.obj
    pageTotal.value = response.total
    foldareaverData.value.isort = response.obj.length + 1
  })
}
/**
 * 获取最大序号foldareaverList
 */
const getMaxSort = async () => {
  // getMaxSortApi().then(({ success, obj }: IAxios) => {
  //   if (success) {
  //     foldareaverData.value.isort = obj
  //   }
  // })
  // foldareaverData.value.isort = foldareaverList.value.length + 1
}
/**
 * @description: 新增
 * @return {*}
 */
const onAdd = async (mediaid: string, foldid: string) => {
  ruleFormRef.value?.resetFields()
  foldId.value = foldid
  mediaId.value = mediaid
  mediaIdEdit.value = mediaid
  foldIdEdit.value = foldid
  // getFoldInfo(mediaIdEdit.value, true)
  nextTick(() => {
    loaddata()
    onAsyncSuccess()
    dialogVisible.value = true
  })
}
/**
 * 修改
 * @param row
 */
const onEdit = (row: TFoldAreaverVo) => {
  mediaIdEdit.value = row.mediaid
  foldIdEdit.value = row.foldid
  // getFoldInfo(mediaIdEdit.value, true)
  dialogVisible.value = true
  nextTick(() => {
    foldareaverData.value = { ...row }
  })
}
/**
 * 删除
 * @param row
 */
const onDelete = (row?: TFoldAreaverVo) => {
  const ids: (string | undefined)[] = []
  if (row) {
    ids.push(row.id)
  } else {
    multipleSelection.value?.forEach((item) => {
      ids.push(item.id)
    })
  }
  if (ids.length < 1) {
    modal.msgWarning('请选的需要删除的信息')
    return false
  }
  modal.confirm('是否确认删除名称为"' + row?.sname + '"的数据项?').then(() => {
    deleteFoldAreaverByIdApi(ids.join(',')).then(({ success }: IAxios) => {
      modal.msgSuccess('删除成功')
      loaddata()
    })
  })
}
/**
 * 保存提交
 * @param formEl
 */
const onSubmitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      foldareaverData.value.foldid = foldIdEdit.value
      if (foldareaverData.value.id) {
        updateFoldAreaverApi(foldareaverData.value).then(({ success }: IAxios) => {
          modal.msgSuccess('保存成功')
          onResetForm(formEl)
          loaddata()
        })
      } else {
        saveFoldAreaverApi(foldareaverData.value).then(({ success }: IAxios) => {
          modal.msgSuccess('保存成功')
          onResetForm(formEl)
          loaddata()
        })
      }
    }
  })
}
/**
 * 取消
 * @param formEl
 */
const onResetForm = (formEl: FormInstance | undefined) => {
  Reset()
  if (!formEl) return
  formEl.resetFields()
  foldareaverData.value.isort = 1
  dialogVisible.value = false
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
  sort.value = val.prop
  if (val.order === 'ascending') {
    order.value = 'asc'
  } else if (val.order === 'descending') {
    order.value = 'desc'
  } else {
    order.value = 'desc'
    sort.value = 'isort'
  }
  loaddata()
}
/**
 * 列表多选
 * @param val
 */
const handleSelectionChange = (val: TFoldAreaverVo[]) => {
  multipleSelection.value = val
}
const Reset = () => {
  foldareaverData.value = {
    id: undefined,
    foldid: '',
    sname: '',
    isort: 0,
    dstartdate: '',
    denddate: '',
    foldname: '',
    medianame: '',
    mediaid: '',
    sremark: '',
    buse: true,
    mediatypekey: ''
  }
}

/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'foldareaver'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>

<style scoped>
.el-select {
  margin-bottom: 0px;
}
.el-form-item {
  margin-left: 10px;
  margin-right: 10px;
}
.left_box {
  height: v-bind(treeheight);
}
</style>
