<!--
 * @Author: wanghl
 * @Date: 2023-08-16 15:11:03
 * @LastEditTime: 2023-09-08 14:50:31
 * @LastEditors: wanghl
 * @Description:菜单行为管理
-->
<template>
  <div id="menuAction">
    <div style="margin-top: -30px; margin-bottom: 10px; font-size: 16px">
      <el-button type="primary" icon="Plus" @click="handleAdd(meunActionForm)">新增</el-button>
    </div>
    <div>
      <el-table :data="menuList" :height="tableheight" :border="true">
        <el-table-column prop="sname" label="行为名称 " show-overflow-tooltip> </el-table-column>
        <el-table-column prop="skeycode" label="操作码 " show-overflow-tooltip> </el-table-column>

        <el-table-column prop="sgroupkey" label="同组赋权"> </el-table-column>
        <el-table-column prop="scopeName" label="范围"> </el-table-column>
        <el-table-column prop="sfunctionurl" label="按钮url "> </el-table-column>
        <el-table-column prop="isort" label="序号 " :width="60"> </el-table-column>
        <el-table-column prop="bmodify" label="是否可修改" :width="100">
          <template #default="scope">
            <span v-if="scope.row.bmodify" style="color: #67c23a">可修改</span>
            <span v-else style="color: #f56c6c">不可修改</span>
          </template>
        </el-table-column>
        <el-table-column prop="buse" label="有效" :width="60">
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
            <el-button link type="success" icon="Edit" @click="handleUpdate(scope.row)"
              >修改</el-button
            >
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 添加或修改菜单行为对话框 -->
    <el-dialog v-model="open" :title="title" width="700px" append-to-body draggable>
      <el-form ref="menuRef" :model="meunActionForm" :rules="rules" label-width="100px">
        <el-row :gutter="50">
          <el-col :span="21">
            <el-form-item label="名称" prop="sname">
              <el-input v-model="meunActionForm.sname"></el-input>
            </el-form-item>
            <el-form-item label="序号" prop="isort">
              <el-input-number
                v-model="meunActionForm.isort"
                :min="1"
                :max="1000"
                label="描述文字"
              ></el-input-number>
            </el-form-item>
            <el-form-item label="操作码" prop="skeycode">
              <el-input v-model="meunActionForm.skeycode" @blur="checkAction"></el-input>
            </el-form-item>
            <el-form-item label="是否可修改" prop="bmodify">
              <el-select v-model="meunActionForm.bmodify" placeholder="可修改" style="width: 150px">
                <el-option label="可修改" :value="true"></el-option>
                <el-option label="不可修改" :value="false"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="按钮url" prop="sfunctionurl">
              <el-input v-model="meunActionForm.sfunctionurl"></el-input>
            </el-form-item>
            <el-form-item label="同组赋权" prop="sgroupkey">
              <el-input v-model="meunActionForm.sgroupkey"></el-input>
            </el-form-item>
            <el-form-item label="范围" prop="scopeId">
              <el-select
                v-model="meunActionForm.scopeId"
                placeholder="选择范围"
                style="width: 150px"
                clearable
              >
                <el-option key="" label="选择范围" value="">选择范围</el-option>
                <el-option
                  v-for="item in scopeList"
                  :key="item.id"
                  :label="item.sname"
                  :value="item.id"
                  >{{ item.sname }}
                </el-option>
              </el-select>
            </el-form-item>
            <el-row :gutter="50">
              <el-col :span="8">
                <el-form-item label="有效" prop="buse">
                  <el-checkbox v-model="meunActionForm.buse"></el-checkbox>
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
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, nextTick } from 'vue'
import { FormRules, FormInstance } from 'element-plus'
import {
  updateMenuActionApi,
  deleteMenuActionApi,
  getMenuActionListdApi,
  saveMenuActionApi,
  checkActionKeyCodeOnlyApi
} from '@/api/system/menu'
import { getScopePageListApi, getScopeMaxSortApi } from '@/api/system/scope'
import type { TQueryInfo } from '@/types/common/index'
import type { TScope } from '@/types/views/system/scope'
import type { TMenuAction, TCheckActionKey } from '@/types/views/system/menu'
import { required, computTableHeight } from '@/utils/index'
import modal from '@/plugins/modal'
import { IAxios } from 'axios'
defineOptions({
  name: 'MenuAction'
})
type Props = {
  //  微信账号id
  pmenuId: string
}
const props = defineProps<Props>()
/** 范围列表 */
const scopeList = ref<TScope[]>([])
/** 获取范围数据 */
const queryInfo = ref<TQueryInfo>({
  sort: 'isort',
  order: 'asc',
  pageSize: 15,
  page: 1,
  queryKey: ''
})
/**
 *  菜单行为数据
 */
const menuList = ref<TMenuAction[]>([])
/** 表格高度 */
const tableheight = ref(0)
const open = ref(false)
const title = ref('')
// 微信号id
const menuaccountid = ref(props.pmenuId)
/** 菜单行为数据 */
const meunActionForm = ref<TMenuAction>({
  id: '',
  /**
   *  名称
   */
  sname: '',
  /**
   *  菜单行为id
   */
  menuid: props.pmenuId,
  /**
   *操作码
   */
  skeycode: '',
  /**
   * 设置中是否可以修改1可修改0不可修改
   */
  bmodify: true,
  isort: 0, // 序号
  buse: true, // 有效
  /**
   * 按钮url
   */
  sfunctionurl: '',
  /**
   * 范围id
   */
  scopeId: '',
  /**
   * 按钮组，用于同组赋权
   */
  sgroupkey: ''
})
/**
 * 校验设置参数
 */
const rules = reactive<FormRules<TMenuAction>>({
  sname: [
    { validator: required, required: true, message: '行为名称不能为空', trigger: 'blur' },
    { max: 20, message: '不得大于20个字符', trigger: 'blur' }
  ],
  skeycode: [
    { validator: required, required: true, message: '操作码不能为空', trigger: 'blur' },
    { max: 20, message: '不得大于20个字符', trigger: 'blur' }
  ]
})
/** 规则验证 */
const menuRef = ref<FormInstance>()
/** 初始加载 */
onMounted(() => {
  getMaxSort()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight(false) - 200
  nextTick(() => {
    getList()
    loaddata()
  })
})
/**
 * 获取菜单行为列表
 */
const getList = () => {
  console.log(props.pmenuId)
  getMenuActionListdApi(props.pmenuId).then((response) => {
    menuList.value = response.obj
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
  meunActionForm.value = {
    id: '',
    /**
     *  名称
     */
    sname: '',
    /**
     *  菜单行为id
     */
    menuid: props.pmenuId,
    /**
     *操作码
     */
    skeycode: '',
    /**
     * 设置中是否可以修改1可修改0不可修改
     */
    bmodify: true,
    isort: 0, // 序号
    buse: true, // 有效

    sfunctionurl: '', // url
    /**
     * 范围id
     */
    scopeId: '',
    /**
     * 按钮组，用于同组赋权
     */
    sgroupkey: ''
  }
}
/**
 * 新增按钮操作
 */
const handleAdd = (row: TMenuAction) => {
  reset()
  open.value = true
  title.value = '添加菜单行为'
}
/**
 * 修改按钮操作
 */
const handleUpdate = (row: TMenuAction) => {
  reset()
  meunActionForm.value = row
  open.value = true
  title.value = '修改菜单行为'
}
/**
 * 提交按钮
 */
const submitForm = () => {
  menuRef.value?.validate((valid) => {
    if (valid) {
      if (meunActionForm.value.id !== '') {
        updateMenuActionApi(meunActionForm.value).then((response) => {
          modal.msgSuccess('修改成功')
          open.value = false
          getList()
        })
      } else {
        console.log(meunActionForm.value)
        saveMenuActionApi(meunActionForm.value).then(({ success }: IAxios) => {
          if (success) {
            modal.msgSuccess('新增成功')
            open.value = false
            getList()
          }
        })
      }
    }
  })
}
/**
 * 删除按钮操作
 */
const handleDelete = (row: TMenuAction) => {
  modal
    .confirm('是否确认删除名称为"' + row.sname + '"的数据项?')
    .then(() => {
      deleteMenuActionApi(row.id).then(({ success }: IAxios) => {
        if (success) {
          getList()
          modal.msgSuccess('删除成功')
        }
      })
    })
    .catch(() => {})
}
/**
 * 获取范围列表
 */
const loaddata = () => {
  const data = queryInfo.value
  getScopePageListApi(data).then(({ obj }: IAxios<TScope[]>) => {
    console.log(obj)
    scopeList.value = obj
  })
}
/**
 * 获取范围长度
 */
const getMaxSort = () => {
  getScopeMaxSortApi().then(({ success, obj }: IAxios<number>) => {
    queryInfo.value.pageSize = obj
    console.log(obj)
  })
}
/**
 * 验证操作码的唯一性
 */
const checkAction = () => {
  const data: TCheckActionKey = {
    actionId: meunActionForm.value.id,
    keyCode: meunActionForm.value.skeycode
  }
  checkActionKeyCodeOnlyApi(data).then(({ success }: IAxios) => {
    if (!success) {
      modal.msgWarning('操作码已有，请更换操作码！')
      // modal.msgSuccess('操作码可使用')
      // loaddata()
    }
  })
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
