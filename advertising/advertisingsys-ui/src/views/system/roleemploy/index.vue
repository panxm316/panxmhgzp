<template>
  <div class="app-container">
    <el-row :gutter="5">
      <el-col :xs="10" :sm="9" :md="9" :lg="7" :xl="7">
        <div class="table_box left_box" style="padding: 10px 5px 5px">
          <div class="fenge-box" style="line-height: 42px !important">
            <el-icon color="#409EFC"><star-filled /></el-icon><span>角色列表</span
            ><el-icon color="#409EFC"><star-filled /></el-icon>
          </div>
          <el-table
            ref="roleTableRef"
            :data="roleList"
            row-key="id"
            :height="tableHeight"
            :border="true"
            highlight-current-row
            :header-cell-style="tableHeaderColor"
          >
            <el-table-column prop="sname" label="角色名称"></el-table-column>
            <el-table-column prop="buse" label="有效" :width="60" align="center">
              <template #default="scope">
                <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column
              label="操作"
              align="center"
              class-name="small-padding fixed-width"
              fixed="right"
            >
              <template #default="scope">
                <el-button link type="success" icon="Edit" @click="handleUpdateRole(scope.row)"
                  >修改</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <!-- 页码 -->
          <el-pagination
            small
            layout="total,  prev, pager, next, jumper"
            class="pagination"
            :current-page="queryInfo.page"
            :page-size="queryInfo.pageSize"
            :page-sizes="paginationInfo.pageSizes"
            :total="paginationInfo.total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-col>
      <el-col :xs="14" :sm="15" :md="15" :lg="17" :xl="17">
        <div class="search_box">
          <div class="flex">
            <el-button type="success" icon="Plus" @click="handleAddEmploy()">添加人员</el-button>
            <el-button type="danger" icon="Close" @click="handleRemoveEmploy()">取消授权</el-button>
            <el-button type="warning" icon="Refresh" @click="handleResetRole()">重设角色</el-button>
            <HgZtreeSelect
              style="width: 200px"
              :scopeflag="EHgDeptZtreeUrl.DEPT_GETSYSEMPDEPTTREE"
              :selectids="employQueryInfo.deptid ? [employQueryInfo.deptid] : []"
              :clearable="true"
              @selectionztree="onSelectionZtree"
            ></HgZtreeSelect>
            <el-input
              v-model="employQueryInfo.queryKey"
              placeholder="请输入人员名称"
              clearable
              style="width: 200px"
              @clear="getEmployByRole"
              @keyup.enter="getEmployByRole"
            />
            <el-button type="primary" icon="Search" @click="getEmployByRole">搜索</el-button>
            <el-button type="primary" icon="back" @click="handleCancel">返回</el-button>
          </div>
        </div>
        <div class="table_box">
          <el-table
            :data="employList"
            row-key="id"
            :height="empTableHeight"
            :border="true"
            stripe
            :header-cell-style="tableHeaderColor"
            @sort-change="sortChange"
            @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="55" align="center"> </el-table-column>
            <el-table-column prop="susername" label="用户名" sortable="custom"> </el-table-column>
            <el-table-column prop="deptName" label="部门"></el-table-column>
            <el-table-column prop="bsex" label="性别" :width="100" align="center">
              <template #default="scope">
                <span>{{ scope.row.bsex ? '女' : '男' }}</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="isort"
              label="序号"
              :width="100"
              align="center"
              sortable="custom"
            >
            </el-table-column>
            <el-table-column prop="buse" label="启用" :width="100" align="center" sortable="custom">
              <template #default="scope">
                <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
              </template>
            </el-table-column>

            <el-table-column
              label="操作"
              align="center"
              width="150"
              class-name="small-padding fixed-width"
              fixed="right"
            >
              <template #default="scope">
                <el-button link type="danger" icon="Close" @click="handleRemoveEmploy(scope.row)"
                  >取消授权</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <!-- 页码 -->
          <el-pagination
            small
            layout="total, sizes, prev, pager, next, jumper"
            class="pagination"
            :current-page="employQueryInfo.page"
            :page-size="employQueryInfo.pageSize"
            :page-sizes="employPaginationInfo.pageSizes"
            :total="employPaginationInfo.total"
            @size-change="handleEmploySizeChange"
            @current-change="handleEmployCurrentChange"
          />
        </div>
      </el-col>
    </el-row>
    <el-dialog
      v-model="dialogVisible"
      title="选择人员"
      width="800"
      align-center
      :close-on-click-modal="false"
    >
      <div class="search_box">
        <div class="flex">
          <HgZtreeSelect
            style="width: 200px"
            :scopeflag="EHgDeptZtreeUrl.DEPT_GETSYSEMPDEPTTREE"
            :selectids="addEmployQueryInfo.deptid ? [addEmployQueryInfo.deptid] : []"
            :clearable="true"
            @selectionztree="onAddSelectionZtree"
          ></HgZtreeSelect>

          <el-input
            v-model="addEmployQueryInfo.queryKey"
            placeholder="请输入人员名称"
            clearable
            style="width: 200px"
            @clear="getAddEmployList"
            @keyup.enter="getAddEmployList"
          />
          <el-button type="primary" icon="Search" @click="getAddEmployList">搜索</el-button>
        </div>
      </div>
      <div class="table_box">
        <el-table
          :data="addEmployList"
          row-key="id"
          :height="400"
          :border="true"
          stripe
          :header-cell-style="tableHeaderColor"
          @sort-change="addSortChange"
          @selection-change="handleAddSelectionChange"
        >
          <el-table-column type="selection" width="55" align="center"></el-table-column>
          <el-table-column prop="susername" label="用户名" sortable="custom"> </el-table-column>
          <el-table-column prop="deptName" label="部门"></el-table-column>
          <el-table-column prop="bsex" label="性别" :width="100" align="center">
            <template #default="scope">
              <span>{{ scope.row.bsex ? '女' : '男' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="isort" label="序号" :width="100" align="center" sortable="custom">
          </el-table-column>
          <el-table-column prop="buse" label="启用" :width="100" align="center" sortable="custom">
            <template #default="scope">
              <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
            </template>
          </el-table-column>
        </el-table>
        <!-- 页码 -->
        <el-pagination
          small
          layout="total, sizes, prev, pager, next, jumper"
          class="pagination"
          :current-page="addEmployQueryInfo.page"
          :page-size="addEmployQueryInfo.pageSize"
          :page-sizes="addEmployPaginationInfo.pageSizes"
          :total="addEmployPaginationInfo.total"
          @size-change="handleAddEmploySizeChange"
          @current-change="handleAddEmployCurrentChange"
        />
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" icon="Check" @click="onSubmitForm()">确 定</el-button>
          <el-button icon="close" @click="dialogVisible = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { required, computTableHeight, tableHeaderColor } from '@/utils/index'
import { IAxios } from 'axios'
import type { TQueryInfo, TPagination, TSelectZtree } from '@/types/common/index'
import { getRolePageListApi } from '@/api/system/role'
import type { TRoleVO, TRolePage } from '@/types/views/system/role'
import { TQEmployByRoleVO, TSEmployVO } from '@/types/views/system/employ'
import {
  deleteRoleByEmployApi,
  deleteRoleByIdApi,
  getEmployPageListByRoleIdApi,
  getEmployPageListNotByRoleIdApi,
  saveEmployByRoleApi
} from '@/api/system/user'
import { EHgDeptZtreeUrl } from '@/types/common/enumindex'
import { ElTable } from 'element-plus'
import modal from '@/plugins/modal'

defineOptions({
  name: 'RoleEmploy'
})

const props = withDefaults(defineProps<{ params: TRolePage }>(), {
  params: () => {
    return { roleId: '', roleName: '', pageIndex: 0, compName: 'RoleEmploy' }
  }
})

/** 返回事件 */
const emit = defineEmits<{
  selectRolePage: [data: { params: TRolePage }]
}>()

/** 角色列表对象 */
const roleList = ref<TRoleVO[]>([])
/** 角色列表ref */
const roleTableRef = ref<InstanceType<typeof ElTable>>()

const tableHeight = ref(0)

const empTableHeight = ref(0)
/**
 * 查询对象
 */
const queryInfo = ref<TQueryInfo>({
  sort: 'isort',
  order: 'desc',
  pageSize: 20,
  page: 1,
  queryKey: ''
})
/**
 * 分布对象
 */
const paginationInfo = ref<TPagination>({
  pageSizes: [15, 20, 30, 40],
  total: 0
})
/** 人员列表对象 */
const employList = ref<TSEmployVO[]>([])
/** 当前选中的角色id */
const selectRoleId = ref('')
/**
 * 人员查询对象
 */
const employQueryInfo = ref<TQEmployByRoleVO>({
  sort: 'isort',
  order: 'desc',
  pageSize: 15,
  page: 1,
  queryKey: ''
})
/**
 * 人员分布对象
 */
const employPaginationInfo = ref<TPagination>({
  pageSizes: [15, 20, 30, 40],
  total: 0
})

/** 列表多选数据 */
const multipleSelection = ref<TSEmployVO[]>([])
/** 选择人员弹出 */
const dialogVisible = ref(false)

/** 添加人员弹出列表信息 */
const addEmployList = ref<TSEmployVO[]>([])
/**
 * 弹出人员查询对象
 */
const addEmployQueryInfo = ref<TQEmployByRoleVO>({
  sort: 'isort',
  order: 'desc',
  pageSize: 15,
  page: 1,
  queryKey: ''
})
/**
 * 弹出人员分布对象
 */
const addEmployPaginationInfo = ref<TPagination>({
  pageSizes: [15, 20, 30, 40],
  total: 0
})
/** 添加人员列表多选数据 */
const addMultipleSelection = ref<TSEmployVO[]>([])

onMounted(() => {
  tableHeight.value = computTableHeight()
  empTableHeight.value = computTableHeight()
  getRolePageList()
})
/**
 * @description: 角色列表
 * @return {*}
 */
const getRolePageList = () => {
  getRolePageListApi(queryInfo.value).then(({ success, obj, total }: IAxios<TRoleVO[]>) => {
    if (success) {
      paginationInfo.value.total = total
      roleList.value = obj
      let role
      // 列表传入角色
      if (props.params.roleId) {
        role = roleList.value.find((item) => item.id === props.params.roleId)
        if (role) {
          roleTableRef.value?.setCurrentRow(role)
          handleUpdateRole(role)
        }
      }
      // 查询如果有值默认选择第一个
      if (!role && roleList.value && roleList.value.length > 0) {
        roleTableRef.value?.setCurrentRow(roleList.value[0])
        handleUpdateRole(roleList.value[0])
      }
    }
  })
}
/**
 * 点击角色
 */
const handleUpdateRole = (row: TRoleVO) => {
  selectRoleId.value = row.id
  employQueryInfo.value.page = 1
  getEmployByRole()
}
/**
 * @description: 根据角色展示查询人员列表
 * @return {*}
 */
const getEmployByRole = () => {
  employQueryInfo.value.roleIds = selectRoleId.value
  getEmployPageListByRoleIdApi(employQueryInfo.value).then(
    ({ success, obj, total }: IAxios<TSEmployVO[]>) => {
      employList.value = obj
      employPaginationInfo.value.total = total
    }
  )
}
/**
 * @description: 弹出选择人员
 * @param {*} val 表示是否重新赋角色标志 true表示重新赋角色 false表示只添加
 * @return {*}
 */
const handleAddEmploy = () => {
  if (!selectRoleId.value) {
    modal.msgError('请选择角色')
    return
  }
  addEmployQueryInfo.value = {
    sort: 'isort',
    order: 'desc',
    pageSize: 15,
    page: 1,
    queryKey: '',
    deptid: ''
  }
  getAddEmployList()
  dialogVisible.value = true
}

/**
 * @description: 添加人员列表查询
 * @return {*}
 */
const getAddEmployList = () => {
  addEmployQueryInfo.value.roleIds = selectRoleId.value
  getEmployPageListNotByRoleIdApi(addEmployQueryInfo.value).then(
    ({ success, obj, total }: IAxios<TSEmployVO[]>) => {
      addEmployList.value = obj
      addEmployPaginationInfo.value.total = total
    }
  )
}
/**
 * @description: 添加提交
 * @return {*}
 */
const onSubmitForm = () => {
  const empIds = addMultipleSelection.value?.map((item) => item.id).join(',')
  // 角色添加人员
  saveEmployByRoleApi(empIds, selectRoleId.value).then(({ success }: IAxios) => {
    if (success) {
      modal.msgSuccess('保存成功')
      getAddEmployList()
      getEmployByRole()
    }
  })
}
/**
 * @description: 重设角色
 * @return {*}
 */
const handleResetRole = () => {
  modal.confirm('是否重设角色？').then(() => {
    deleteRoleByIdApi(selectRoleId.value).then(({ success }: IAxios) => {
      if (success) {
        modal.msgSuccess('重置成功请选择人员添加')
        handleAddEmploy()
        getEmployByRole()
      }
    })
  })
}
/**
 * @description: 取消角色授权
 * @param {*} row
 * @return {*}
 */
const handleRemoveEmploy = (row?: TSEmployVO) => {
  const ids: (string | undefined)[] = []
  if (row) {
    ids.push(row.id)
  } else {
    multipleSelection.value?.forEach((item) => {
      ids.push(item.id)
    })
  }
  if (ids.length < 1) {
    modal.msgWarning(`请选的需要取消的人员`)
    return false
  }
  modal.confirm('是否取消选中人员角色？').then(() => {
    deleteRoleByEmployApi(ids.join(','), selectRoleId.value).then(({ success }: IAxios) => {
      if (success) {
        modal.msgSuccess('操作成功')
        getEmployByRole()
      }
    })
  })
}
/**
 * @description: 人员部门筛选
 * @param {*} val
 * @return {*}
 */
const onSelectionZtree = (val: TSelectZtree[]) => {
  employQueryInfo.value.deptid = val.map((item) => item.id).join(',')
}
/**
 * @description: 添加人员部门筛选
 * @param {*} val
 * @return {*}
 */
const onAddSelectionZtree = (val: TSelectZtree[]) => {
  addEmployQueryInfo.value.deptid = val.map((item) => item.id).join(',')
}

/**
 * 改变每页显示条数
 * @param val
 */
const handleSizeChange = (val: number) => {
  queryInfo.value.pageSize = val
  getRolePageList()
}
/**
 * 分页当前页
 * @param pageIndex
 */
const handleCurrentChange = (pageIndex: number) => {
  queryInfo.value.page = pageIndex
  getRolePageList()
}

/**
 * 排序
 * @param val
 */
const sortChange = (val: any) => {
  employQueryInfo.value.sort = val.prop
  if (val.order === 'ascending') {
    employQueryInfo.value.order = 'asc'
  } else if (val.order === 'descending') {
    employQueryInfo.value.order = 'desc'
  } else {
    employQueryInfo.value.order = 'desc'
    employQueryInfo.value.sort = 'isort'
  }
  getEmployByRole()
}
/**
 * 添加人员列表排序
 * @param val
 */
const addSortChange = (val: any) => {
  addEmployQueryInfo.value.sort = val.prop
  if (val.order === 'ascending') {
    addEmployQueryInfo.value.order = 'asc'
  } else if (val.order === 'descending') {
    addEmployQueryInfo.value.order = 'desc'
  } else {
    addEmployQueryInfo.value.order = 'desc'
    addEmployQueryInfo.value.sort = 'isort'
  }
  getAddEmployList()
}

/**
 * 列表多选
 * @param val
 */
const handleSelectionChange = (val: TSEmployVO[]) => {
  multipleSelection.value = val
}
/**
 * 列表多选
 * @param val
 */
const handleAddSelectionChange = (val: TSEmployVO[]) => {
  addMultipleSelection.value = val
}

/**
 * 人员列表改变每页显示条数
 * @param val
 */
const handleEmploySizeChange = (val: number) => {
  employQueryInfo.value.pageSize = val
  getEmployByRole()
}
/**
 * 人员列表分页当前页
 * @param pageIndex
 */
const handleEmployCurrentChange = (pageIndex: number) => {
  employQueryInfo.value.page = pageIndex
  getEmployByRole()
}

/**
 * 添加人员列表改变每页显示条数
 * @param val
 */
const handleAddEmploySizeChange = (val: number) => {
  addEmployQueryInfo.value.pageSize = val
  getAddEmployList()
}
/**
 * 添加人员列表分页当前页
 * @param pageIndex
 */
const handleAddEmployCurrentChange = (pageIndex: number) => {
  addEmployQueryInfo.value.page = pageIndex
  getAddEmployList()
}
/**
 * 返回角色列表
 */
const handleCancel = () => {
  props.params.compName = 'RoleList'
  emit('selectRolePage', props)
}
</script>
<style scoped lang="scss"></style>
