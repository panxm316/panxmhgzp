<!--
 * @Author: wanghl
 * @Date: 2023-09-19 16:12:27
 * @LastEditTime: 2023-11-11 15:34:02
 * @LastEditors: wanghl
 * @Description:广告类别
-->
<template>
  <div class="app-container">
    <el-row :gutter="5">
      <!--部门数据-->
      <el-col v-if="type != 'wei'" :xs="8" :sm="7" :md="7" :lg="5" :xl="5">
        <div class="table_box left_box">
          <div class="fenge-box">
            <el-icon color="#409EFC"><star-filled /></el-icon><span>{{ typename }}列表</span
            ><el-icon color="#409EFC"><star-filled /></el-icon>
          </div>
          <div id="scrolly">
            <div id="pagetypemediaztreeid" class="ztree"></div>
          </div>
        </div>
      </el-col>
      <!-- 用户数据 -->
      <el-col
        :xs="type != 'wei' ? 16 : 24"
        :sm="type != 'wei' ? 17 : 24"
        :md="type != 'wei' ? 17 : 24"
        :lg="type != 'wei' ? 19 : 24"
        :xl="type != 'wei' ? 19 : 24"
      >
        <!-- <el-col v-if="type != 'wei'" :xs="16" :sm="17" :md="17" :lg="19" :xl="19"> -->
        <div class="search_box">
          <div class="flex">
            <el-button v-if="type == 'wei'" type="primary" icon="Plus" @click="onAdd('')"
              >新增</el-button
            >
            <!-- <el-select
              v-model="mediaId"
              class="m-2"
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
            </el-select> -->
            <!-- <el-select v-model="foldId" class="m-2" placeholder="叠次信息" style="width: 100px">
              <el-option
                v-for="item in foldDatas"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select> -->
            <el-input
              v-model="queryInfo.queryKey"
              placeholder="请输版面类别名称"
              style="width: 200px"
              clearable
              @keyup.enter="loaddata"
              @clear="loaddata"
            />
            <el-button type="primary" icon="Search" @click="loaddata">搜索</el-button>
            <!-- <el-tooltip class="item" effect="dark" content="帮助" placement="top">
              <el-button class="help" @click="showhelp()">?</el-button>
            </el-tooltip> -->
          </div>
        </div>
        <div class="table_box">
          <el-table
            :data="pagetypeList"
            row-key="id"
            highlight-current-row
            :height="props.tableheight"
            :border="true"
            stripe
            :header-cell-style="tableHeaderColor"
            @sort-change="sortChange"
          >
            <el-table-column
              prop="sname"
              label="名称"
              min-width="160"
              sortable="custom"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="mediatypename"
              label="类型"
              min-width="160"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              v-if="type != 'wei'"
              prop="mediaName"
              label="媒体名称"
              min-width="160"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              v-if="type != 'wei'"
              prop="foldname"
              :label="type != 'app' ? '叠次名称' : '频道名称'"
              min-width="160"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="sremark"
              label="备注"
              min-width="160"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="isort"
              label="序号"
              width="80"
              sortable="custom"
              align="center"
            ></el-table-column>
            <el-table-column prop="buse" label="启用" width="80" align="center" sortable="custom">
              <template #default="scope">
                <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
              </template>
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
          <el-pagination
            v-model:current-page="currentPage"
            class="pagination"
            style="margin-left: 10px; width: 100%"
            :page-sizes="pageSizes"
            :page-size="queryInfo.pageSize"
            small
            background
            layout="total, sizes, prev, pager, next, jumper"
            :total="pageTotal"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-col>
    </el-row>
    <el-dialog
      v-model="dialogVisible"
      title="编辑"
      width="700px"
      :align-center="true"
      :close-on-click-modal="false"
      @close="closeDialog"
    >
      <el-form ref="ruleFormRef" :rules="rules" :model="pagetypeForm" label-width="120px">
        <el-row :gutter="50">
          <el-col :span="22">
            <el-form-item label="名称" prop="sname">
              <el-input v-model="pagetypeForm.sname" />
            </el-form-item>
            <!-- <el-form-item label="类型" prop="mediatypename">
              <el-input v-model="pagetypeForm.mediatypename" />
            </el-form-item> -->
            <el-form-item label="备注" prop="sremark">
              <el-input v-model="pagetypeForm.sremark" />
            </el-form-item>
            <!-- <el-form-item label="本级" prop="localid">
              <el-input v-model="pagetypeForm.localid" />
            </el-form-item> -->
            <!-- <el-form-item label="媒体" prop="mediaid">
              <el-select
                v-model="pagetypeForm.mediaid"
                placeholder="媒体信息"
                @change="getFoldInfo(pagetypeForm.mediaid)"
              >
                <el-option
                  v-for="item in mediaDatas"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="叠次" prop="foldid">
              <el-select v-model="pagetypeForm.foldid" placeholder="叠次信息">
                <el-option
                  v-for="item in foldDatasEdit"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item> -->
            <el-form-item label="序号" prop="isort">
              <el-input-number v-model="pagetypeForm.isort" :min="1" controls-position="right" />
            </el-form-item>
            <el-form-item label="启用" prop="buse">
              <el-checkbox v-model="pagetypeForm.buse" />
            </el-form-item>
            <el-form-item> </el-form-item>
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
import type { TPageSortQueryInfo, TPageSortRequest } from '@/types/views/media/PageSort'
import { IDataCombo } from '@/types/common/DataCombo'
import {
  getPagesortListApi,
  savePagesortApi,
  updatePagesortApi,
  deteleByIdApi,
  getMaxIsortApi
} from '@/api/media/PageSort'
import { getFoldComboApi, getMediaDataComboByTypeApi } from '@/api/media/foldareaver'
import type { IAxios } from 'axios'
import type { ElFormItem, FormInstance, FormRules } from 'element-plus'
import modal from '@/plugins/modal'
import {
  required,
  validatePhone,
  computTableHeight,
  tableHeaderColor,
  computTreeHeighttab
} from '@/utils/index'
import HgDateIndex from '@/components/HgDateIndex/index.vue'
import { TDateType, TDateTimeType } from '@/types/components/hgdateindex'
import dayjs from 'dayjs'
defineOptions({
  name: 'PageSort'
})
import { useRouter } from 'vue-router'
import { pa } from 'element-plus/es/locale'
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
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 页数 */
const currentPage = ref<number>(1)
/** 数据总条数 */
const pageTotal = ref<number>(0)
/** 查询叠次Id */
const foldId = ref('')
/** 表单ref */
const ruleFormRef = ref<FormInstance>()
/** 查询对象 */
const queryInfo = ref<TPageSortQueryInfo>({
  sort: 'isort',
  order: 'asc,asc',
  queryKey: '', // 查询关键字
  page: 1,
  pageSize: 20,
  mediatypekey: props.type,
  mediaid: '',
  foldid: ''
})
/** 校验设置参数 */
const rules = reactive<FormRules<TPageSortRequest>>({
  sname: [
    { validator: required, required: true, message: '请输入名称', trigger: 'change' },
    { max: 40, message: '不得大于40个字符', trigger: 'change' }
  ],
  foldid: [{ max: 80, message: '不得大于80个字符', trigger: 'change' }],
  mediatypekey: [{ max: 40, message: '不得大于40个字符', trigger: 'change' }]
})
/** 树高度 */
const treeheight = ref('')
/** 列表数据 */
const pagetypeList = ref<TPageSortRequest[]>()
/** 弹出添加修改标志 */
const dialogVisible = ref(false)
/** 表单对象 */
const pagetypeForm = ref<TPageSortRequest>({
  id: '',
  sname: '', // 类别名称
  buse: true,
  isort: 1, // 序号
  mediatypekey: props.type,
  mediatypename: '',
  foldid: '', // 叠次id
  foldname: '',
  sremark: '',
  mediaid: '' // 媒体id
})
/** 媒体数据 */
const mediaDatas = ref<IDataCombo[]>([])
/** 叠次数据编辑*/
const foldDatasEdit = ref<IDataCombo[]>([])
// ------------------------------------------
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
 * 在节点上显示添加节点图标按钮
 * @param {string} treeId 树id
 * @param {ITreeNode} treeNode 鼠标移动上的节点
 * @returns 显示添加按钮
 */
const addHoverDom = (treeId: string, treeNode: ITreeNode) => {
  if (treeNode.level === 1) {
    $('.remove')?.remove()
    $('.edit')?.remove()
    return
  }

  if (treeNode.level === 2 || treeNode.level === 0) {
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
        event.stopPropagation()
        parentNode = treeNode
        onAdd(treeNode)
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
  ztreeObj = $.fn.zTree.getZTreeObj!('pagetypemediaztreeid')
  const node = ztreeObj.getNodes()
  console.log(node)
  // 根节点id的0值改为字符串
  const noderoot = ztreeObj.getNodeByParam('id', foldId.value, null)
  console.log(noderoot)
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
  if (node.length > 0) {
    ztreeObj.selectNode(noderoot)
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
  if (treeNode.level === 2) {
    pagetypeForm.value.foldid = treeNode.id
    pagetypeForm.value.foldname = treeNode.name
    queryInfo.value.foldid = treeNode.id
    foldId.value = treeNode.id
  }
  if (treeNode.level === 0) {
    queryInfo.value.foldid = ''
    queryInfo.value.mediaid = ''
    pagetypeForm.value.mediaid = ''
    pagetypeForm.value.foldid = ''
    pagetypeForm.value.foldname = ''
  }
  if (treeNode.level === 1) {
    queryInfo.value.foldid = ''
    queryInfo.value.mediaid = treeNode.id
    pagetypeForm.value.mediaid = treeNode.id
    pagetypeForm.value.foldid = ''
    pagetypeForm.value.foldname = ''
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
  // queryInfo.value.mediatypekey = props.type.toString()
  // pagetypeForm.value.mediatypekey = props.type.toString()
  // getMediaInfo()
  treeheight.value = computTreeHeighttab()
  loaddata()
  nextTick(() => {
    $.fn.zTree.init!($('#pagetypemediaztreeid'), treeData, [])
    ztreeObj = $.fn.zTree.getZTreeObj!('pagetypemediaztreeid')
    bCheckDelFlag.value = true
  })
})
/** 监听传入数值变化 */
watch(
  () => props.type,
  (newValue, oldValue) => {
    loaddata()
  }
)
/**
 * @description: 经营主体分页查询
 * @return {*}
 */
const loaddata = () => {
  const data = queryInfo.value
  getPagesortListApi(data).then(({ obj, total, success }: IAxios<TPageSortRequest[]>) => {
    console.log(obj)
    if (success) {
      pagetypeList.value = obj
      pageTotal.value = total
      pagetypeForm.value.isort = obj.length + 1
    }
  })
}
/**
 * @description: 新增
 * @return {*}
 */
const onAdd = async (treeNode: any) => {
  // pagetypeForm.value.foldid = treeNode.id
  // pagetypeForm.value.foldname = treeNode.name
  // queryInfo.value.foldid = treeNode.id
  pagetypeForm.value.mediatypekey = props.type
  if (treeNode.level === 2) {
    pagetypeForm.value.foldid = treeNode.id
    pagetypeForm.value.foldname = treeNode.name
    queryInfo.value.foldid = treeNode.id
    foldId.value = treeNode.id
  }
  if (treeNode.level === 0) {
    queryInfo.value.foldid = ''
    queryInfo.value.mediaid = ''
    pagetypeForm.value.mediaid = ''
    pagetypeForm.value.foldid = ''
    pagetypeForm.value.foldname = ''
    foldId.value = treeNode.id
  }
  if (treeNode.level === 1) {
    queryInfo.value.foldid = ''
    queryInfo.value.mediaid = treeNode.id
    pagetypeForm.value.mediaid = treeNode.id
    pagetypeForm.value.foldid = ''
    pagetypeForm.value.foldname = ''
    foldId.value = treeNode.id
  }

  ruleFormRef.value?.resetFields()
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
const onEdit = (row: TPageSortRequest) => {
  pagetypeForm.value.mediaid = row.mediaid as string
  pagetypeForm.value.foldid = row.foldid
  dialogVisible.value = true
  nextTick(() => {
    pagetypeForm.value = { ...row }
  })
}
/**
 *
 * @param row 删除版面类型
 */
const onDelete = (row: TPageSortRequest) => {
  modal.confirm('是否删除选中数据？').then(() => {
    deteleByIdApi(row.id as string).then(({ success }: IAxios) => {
      if (success) {
        modal.msgSuccess('删除成功')
        loaddata()
      }
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
      if (pagetypeForm.value.id) {
        updatePagesortApi(pagetypeForm.value).then(({ success }: IAxios) => {
          if (success) {
            modal.msgSuccess('保存成功')
            onResetForm(formEl)
            loaddata()
          }
        })
      } else {
        savePagesortApi(pagetypeForm.value).then(({ success }: IAxios) => {
          if (success) {
            modal.msgSuccess('保存成功')
            onResetForm(formEl)
            loaddata()
          }
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
  if (!formEl) return
  formEl.resetFields()
  dialogVisible.value = false
  clearData()
}

const closeDialog = () => {
  ruleFormRef.value?.resetFields()
}
/**
 * 改变页码总数时
 */
const handleSizeChange = (val: number) => {
  queryInfo.value.pageSize = val
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
const sortChange = (val: { prop: string; order: string }) => {
  queryInfo.value.sort = val.prop
  if (val.order === 'ascending') {
    queryInfo.value.order = 'asc'
  } else if (val.order === 'descending') {
    queryInfo.value.order = 'desc'
  } else {
    queryInfo.value.order = 'desc'
    queryInfo.value.sort = 'isort'
  }
  loaddata()
}
// /** 获取媒体信息*/
// const getMediaInfo = () => {
//   getMediaDataComboByTypeApi(props.type).then((respose) => {
//     mediaDatas.value = respose.obj
//     pagetypeForm.value.mediaid = mediaDatas.value[0].id
//     getFoldInfo(pagetypeForm.value.mediaid)
//   })
// }
// /** 获取叠次信息*/
// const getFoldInfo = (curmediaId: string) => {
//   getFoldComboApi(curmediaId).then((respose) => {
//     foldDatasEdit.value = respose.obj
//     if (foldDatasEdit.value.length > 0) {
//       pagetypeForm.value.foldid = foldDatasEdit.value[0].id
//     }
//     loaddata()
//   })
// }
/** 数据制空 */
const clearData = () => {
  pagetypeForm.value = {
    id: '',
    sname: '', // 类别名称
    buse: true,
    isort: 1, // 序号
    mediatypekey: '',
    mediatypename: '',
    foldid: '', // 叠次id
    foldname: '',
    sremark: '',
    mediaid: '' // 媒体id
  }
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'pagetype'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>
<style scoped lang="scss">
.left_box {
  height: v-bind(treeheight);
}
</style>
