<!--
 * @Author: wanghl
 * @Date: 2023-08-16 15:11:03
 * @LastEditTime: 2023-12-09 09:33:35
 * @LastEditors: wanghl
 * @Description:菜单管理
-->
<template>
  <div id="menu" class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button type="info" icon="Sort" @click="toggleExpandAll">展开/折叠</el-button>
        <el-button type="primary" icon="Plus" @click="handleAdd(meunForm)">新增</el-button>
        <el-button type="warning" icon="LocationInformation" @click="toScope">
          <!-- <router-link to="/system/scope">菜单范围</router-link> -->菜单范围
        </el-button>
        <el-tooltip class="item" effect="dark" content="帮助" placement="top">
          <el-button class="help" type="text" @click="showhelp()">?</el-button>
        </el-tooltip>
      </div>
    </div>
    <div class="table_box">
      <el-table
        v-if="refreshTable"
        :data="menuList"
        row-key="id"
        :default-expand-all="isExpandAll"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        :height="tableheight"
        :border="true"
        :header-cell-style="tableHeaderColor"
      >
        <el-table-column :width="100" align="left">
          <template #default="scope">
            <span
              ><el-icon><Document /></el-icon>{{ scope.row.isort }}</span
            >
          </template>
        </el-table-column>
        <!-- <el-table-column prop="idepth" label="层级 " :width="60"> </el-table-column> -->
        <el-table-column prop="sname" label="名称 " :width="150"> </el-table-column>
        <el-table-column prop="sothername" label="别名 "> </el-table-column>
        <el-table-column prop="sgroup" label="组名"> </el-table-column>
        <el-table-column prop="simageurl" label="图片url " :width="200"> </el-table-column>
        <el-table-column :width="60" align="center" prop="simageurl" label="图标 ">
          <template #default="scope">
            <span><svg-icon :icon-class="scope.row.simageurl" /></span>
          </template>
        </el-table-column>
        <el-table-column prop="sfunctionurl" label="url " :width="200"> </el-table-column>
        <el-table-column prop="srouterpath" label="路径 " :width="200"> </el-table-column>
        <el-table-column prop="scomponent" label="组件 " :width="200"> </el-table-column>
        <el-table-column prop="sremark" label="类型 " :width="100"> </el-table-column>
        <el-table-column prop="sclass" label="分类 " :width="100"> </el-table-column>
        <el-table-column prop="broleflag" label="赋权" :width="60" align="center">
          <template #default="scope">
            <el-checkbox v-model="scope.row.broleflag" disabled></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column prop="buse" label="启用" :width="60" align="center">
          <template #default="scope">
            <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column prop="bmenushow" label="显示" :width="60" align="center">
          <template #default="scope">
            <el-checkbox v-model="scope.row.bmenushow" disabled></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="300"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              v-if="scope.row.idepth == '1'"
              link
              type="primary"
              icon="Plus"
              @click="handleAdd(scope.row)"
              >新增</el-button
            >
            <el-button link type="success" icon="Edit" @click="handleUpdate(scope.row)"
              >修改</el-button
            >
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
              >删除</el-button
            >
            <el-button link type="primary" icon="View" @click="sddAction(scope.row.id)"
              >行为</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 添加或修改菜单对话框 -->
    <el-dialog
      v-model="open"
      :title="title"
      width="700px"
      append-to-body
      :draggable="fenbianlv() > 1440 ? true : false"
    >
      <el-form ref="menuRef" :model="meunForm" :rules="rules" label-width="100px">
        <el-row :gutter="50">
          <el-col :span="21">
            <el-form-item label="名称" prop="sname">
              <el-input v-model="meunForm.sname"></el-input>
            </el-form-item>
            <el-form-item label="序号" prop="isort">
              <el-input-number
                v-model="meunForm.isort"
                :min="1"
                :max="1000"
                label="描述文字"
              ></el-input-number>
              <!-- <el-input v-model="form.isort" ></el-input> -->
            </el-form-item>
            <el-form-item label="别名" prop="sothername">
              <el-input v-model="meunForm.sothername"></el-input>
            </el-form-item>
            <el-form-item label="组名" prop="sgroup">
              <el-input v-model="meunForm.sgroup"></el-input>
            </el-form-item>
            <el-form-item label="图片url" prop="simageurl">
              <el-input v-model="meunForm.simageurl"></el-input>
            </el-form-item>
            <el-form-item label="url" prop="sfunctionurl">
              <el-input v-model="meunForm.sfunctionurl"></el-input>
            </el-form-item>
            <el-form-item label="路径" prop="srouterpath">
              <el-input v-model="meunForm.srouterpath"></el-input>
            </el-form-item>
            <el-form-item label="组件" prop="scomponent">
              <el-input v-model="meunForm.scomponent"></el-input>
            </el-form-item>
            <el-form-item label="类型" prop="sremark">
              <el-select v-model="meunForm.sremark" placeholder="类型" style="width: 150px">
                <el-option label="请选择..." value=""></el-option>
                <el-option label="全部管理员" value="全部管理员"></el-option>
                <el-option label="普通管理员" value="普通管理员"></el-option>
                <el-option label="超级管理员" value="超级管理员"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="分类" prop="sclass">
              <el-input v-model="meunForm.sclass"></el-input>
            </el-form-item>

            <el-row :gutter="50">
              <el-col :span="8">
                <el-form-item label="赋权" prop="broleflag">
                  <el-checkbox v-model="meunForm.broleflag"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="启用" prop="buse">
                  <el-checkbox v-model="meunForm.buse"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="显示" prop="bmenushow">
                  <el-checkbox v-model="meunForm.bmenushow"></el-checkbox>
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
    <!-- 行为编辑弹框 -->
    <el-dialog
      v-if="actionOpen"
      v-model="actionOpen"
      title="菜单行为编辑"
      width="90%"
      append-to-body
      :draggable="fenbianlv() > 1440 ? true : false"
      :close="closeAction"
    >
      <MenuAction :pmenu-id="meunId"></MenuAction>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { IAxios } from 'axios'
import { FormRules, FormInstance } from 'element-plus'
import { handleTree } from '@/utils/ruoyi'
import { getMenuListApi, saveMenuApi, updateMenuApi, deleteMenuByIdApi } from '@/api/system/menu'
import type { TMenu } from '@/types/views/system/menu'
import { required, computTableHeight, tableHeaderColor, fenbianlv } from '@/utils/index'
import modal from '@/plugins/modal'
import { useRouter } from 'vue-router'
import MenuAction from '@/views/system/menu/MenuAction.vue'
const router = useRouter()
defineOptions({
  name: 'Menu'
})
/** 菜单数据 */
const menuList = ref<TMenu[]>([])
/** 全部展开*/
const isExpandAll = ref(false)
/** 表格高度 */
const tableheight = ref(0)
const refreshTable = ref(true)
/**
 * 菜单弹框
 */
const open = ref(false)
/**
 * 菜单行为弹框
 */
const actionOpen = ref(false)
/**
 * 菜单id
 */
const meunId = ref('')
const title = ref('')
/** 菜单数据 */
const meunForm = ref<TMenu>({
  /** 备注 */
  sremark: '',
  /** 菜单权限使用别名*/
  sothername: '',
  /** 前端组件名称 */
  scomponent: '',
  /** 图片*/
  simageurl: '',
  /** 深度 */
  idepth: 1,
  /** 菜单所属模块*/
  sclass: '',
  /** 是否有效*/
  buse: true,
  sfunctionurl: '',
  /** 前端路由地址 */
  srouterpath: '',
  /** 父id */
  parentid: null,
  /** 菜单分组名称*/
  sgroup: '',
  /** 是否在菜单权限使用 */
  broleflag: true,
  /** 名称 */
  sname: '',
  /** 序号 */
  isort: 0,
  id: null,
  /** 是否需要在菜单列表中显示1显示0不显示(只赋权使用) */
  bmenushow: true
})
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<TMenu>>({
  sname: [
    { validator: required, required: true, message: '菜单名称不能为空', trigger: 'blur' },
    { max: 50, message: '不得大于50个字符', trigger: 'blur' }
  ],
  srouterpath: [
    { validator: required, required: true, message: '前端路由地址不能为空', trigger: 'blur' },
    { max: 50, message: '不得大于50个字符', trigger: 'blur' }
  ],
  simageurl: [
    { validator: required, required: true, message: '图片地址不能为空', trigger: 'blur' },
    { max: 50, message: '不得大于50个字符', trigger: 'blur' }
  ]
})
/** 规则验证 */
const menuRef = ref<FormInstance>()
/** 初始加载 */
onMounted(() => {
  getList()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight(false)
})
/**
 * 展开/折叠操作
 */
const toggleExpandAll = () => {
  refreshTable.value = false
  isExpandAll.value = !isExpandAll.value
  nextTick(() => {
    refreshTable.value = true
  })
}
/**
 * 获取菜单列表
 */
const getList = () => {
  getMenuListApi().then((response) => {
    menuList.value = handleTree(response.obj, 'id', 'parentid')
  })
}
/**
 * 取消按钮
 */
function cancel() {
  open.value = false
  reset()
}
/**
 * 表单重置
 */
const reset = () => {
  meunForm.value = {
    /** 备注 */
    sremark: '',
    /** 菜单权限使用别名*/
    sothername: '',
    /** 前端组件名称 */
    scomponent: '',
    /** 图片*/
    simageurl: '',
    /** 深度 */
    idepth: 1,
    /** 菜单所属模块*/
    sclass: '',
    /** 是否有效*/
    buse: true,
    sfunctionurl: '',
    /** 前端路由地址 */
    srouterpath: '',
    /** 父id */
    parentid: null,
    /** 菜单分组名称*/
    sgroup: '',
    /** 是否在菜单权限使用 */
    broleflag: true,
    /** 名称 */
    sname: '',
    /** 序号 */
    isort: 0,
    id: null,
    /** 是否需要在菜单列表中显示1显示0不显示(只赋权使用) */
    bmenushow: true
  }
}
/**
 * 新增按钮操作
 */
const handleAdd = (row: TMenu) => {
  reset()
  if (row.id !== null) {
    meunForm.value.parentid = row.id
    meunForm.value.idepth = row.idepth + 1
  } else {
    meunForm.value.parentid = null
    meunForm.value.idepth = 1
  }
  open.value = true
  title.value = '添加菜单'
}
/**
 * 修改按钮操作
 */
const handleUpdate = (row: TMenu) => {
  reset()
  meunForm.value = row
  open.value = true
  title.value = '修改菜单'
}
/**
 * 提交按钮
 */
const submitForm = () => {
  menuRef.value?.validate((valid) => {
    if (valid) {
      if (meunForm.value.id !== null) {
        updateMenuApi(meunForm.value).then((response) => {
          console.log(response)
          modal.msgSuccess('修改成功')
          open.value = false
          getList()
        })
      } else {
        console.log(meunForm.value)
        saveMenuApi(meunForm.value).then((response) => {
          modal.msgSuccess('新增成功')
          open.value = false
          getList()
        })
      }
    }
  })
}
/**
 * 删除按钮操作
 */
const handleDelete = (row: TMenu & { children: Array<string> | undefined }) => {
  const data = {
    id: row.id
  }
  console.log(row)
  modal
    .confirm('是否确认删除名称为"' + row.sname + '"的数据项?')
    .then(function () {
      if (row.children === undefined) {
        return deleteMenuByIdApi(data).then(({ success }: IAxios) => {
          if (success) {
            modal.msgSuccess('删除成功')
            getList()
          }
        })
      } else {
        modal.msgError('请先删除子节点')
      }
    })
    .catch(() => {})
}
/**
 * 页面跳转
 */
const toScope = () => {
  router.push({ path: '/system/scope', replace: true })
}
/**
 * 行为弹框
 */
const sddAction = (actionID: string) => {
  actionOpen.value = true
  meunId.value = actionID
  console.log(actionID)
}
/**
 * 关闭行为界面
 */
const closeAction = () => {
  // dialogWriteVisible.value = false
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'menu'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>

<style scoped lang="scss">
.el-form--inline .el-form-item {
  margin: 0 0 0 30px;
}
:global(#menu .el-table__row--level-0) {
  color: #409eff;
}
:global(#menu .el-table__row--level-1) {
  color: #67c23a;
}
:global(#menu .el-table__row--level-2) {
  color: #e6a23c;
}
</style>
