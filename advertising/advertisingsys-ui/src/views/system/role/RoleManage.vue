<template>
  <div class="app-container">
    <el-row class="menuTitle">
      <el-col :span="24">角色权限({{ props.params.roleName }})</el-col>
    </el-row>
    <div class="menuBody" :style="{ height: tableheight + 'px' }">
      <el-row class="borderbottom1-title">
        <el-col :xl="2" :lg="3" :md="3" :sm="4" :xs="4">
          <el-checkbox
            v-model="menuAllChk"
            style="margin: 0px 5px 0 16px"
            @change="handleMenuAllChk()"
          ></el-checkbox>
          资源（菜单）
        </el-col>
        <el-col :xl="16" :lg="15" :md="15" :sm="14" :xs="14"><div>操作（行为）</div></el-col>
        <el-col :span="6" style="text-align: right; padding-right: 10px">
          <el-button v-loading="loading" type="success" icon="check" @click="handleSave"
            ><i class="icon iconfont icon-31xuanze"></i>保存
          </el-button>
          <el-button type="warning" icon="back" @click="handleCancel"
            ><i class="icon iconfont icon-back_android"></i>返回
          </el-button>
        </el-col>
      </el-row>
      <el-row
        v-for="menu in menus"
        :key="menu.menuid"
        :class="
          menu.menutype == '经营'
            ? 'form-group borderbottom1 bg1'
            : menu.menutype == '业务'
            ? 'form-group borderbottom1 bg2'
            : menu.menutype == '采编'
            ? 'form-group borderbottom1 bg4'
            : 'form-group borderbottom1 bg3'
        "
      >
        <el-col :xl="2" :lg="3" :md="3" :sm="4" :xs="4" style="text-align: left">
          <el-checkbox
            v-model="menu.checked"
            style="margin-left: 10px"
            @change="handleMenuChk(menu)"
            >{{ menu.menuname }}
          </el-checkbox>
        </el-col>
        <el-col :xl="22" :lg="21" :md="21" :sm="20" :xs="20">
          <div v-for="active in menu.actives" :key="active.scopeid" class="checkboxlist">
            <el-checkbox v-model="active.checked" :disabled="!active.bmodify">
              {{ active.activename }}
            </el-checkbox>
            <template v-if="active.activeScopeClass === 'tbdept0'">
              <el-button
                v-if="active.treeScope"
                type="success"
                size="small"
                class="ml2 elsuccess"
                @click="handleSelectScope(menu.menuid, active)"
              >
                范围
              </el-button>
            </template>
            <template v-if="active.activeScopeClass === 'tbdept5'">
              <el-button
                v-if="active.treeScope"
                type="primary"
                size="small"
                class="ml2 elprimary"
                @click="handleSelectScope(menu.menuid, active)"
              >
                范围
              </el-button>
            </template>
            <template v-if="active.activeScopeClass === 'tbmedia1'">
              <el-button
                v-if="active.treeScope"
                type="info"
                size="small"
                class="ml2 elinfo"
                @click="handleSelectScope(menu.menuid, active)"
              >
                范围
              </el-button>
            </template>
            <template v-if="!active.treeScope && active.scopes.length">
              <el-popover width="200" trigger="click">
                <div class="el-popover_box">
                  <h5 v-for="scope in active.scopes" :key="scope.businessid">
                    <el-checkbox v-model="scope.checked">{{ scope.businessname }}</el-checkbox>
                  </h5>
                </div>

                <template #reference>
                  <el-button type="success" class="ml2 elsuccess" size="small">范围</el-button>
                </template>
              </el-popover>
            </template>
          </div>
        </el-col>
      </el-row>
    </div>
    <el-dialog
      v-model="selectDeptDialogVisible"
      title="选择部门"
      :close-on-click-modal="false"
      width="700"
      align-center
      :destroy-on-close="true"
    >
      <hg-role-scope
        :active-scope="activeScope"
        @selectActiveScope="handleSelectActiveScope"
      ></hg-role-scope>
    </el-dialog>
    <el-dialog
      v-model="selectMediaDialogVisible"
      title="选择部门"
      :close-on-click-modal="false"
      width="30%"
      align-center
      :destroy-on-close="true"
    >
      <hg-role-scope
        :active-scope="activeScope"
        @selectActiveScope="handleSelectActiveScope"
      ></hg-role-scope>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { getRoleActionScopeByRoleIdApi, saveRoleActionApi } from '@/api/system/role'
import modal from '@/plugins/modal'
import type { TRoleActionDTO } from '@/types/views/system/role'
import { EHgDeptZtreeUrl } from '@/types/common/enumindex'

import type {
  TRolePage,
  TRoleMenuModel,
  TActiveModel,
  TActiveScope
} from '@/types/views/system/role'
import HgRoleScope from '@/views/system/role/components/HgRoleScope.vue'
import { IAxios } from 'axios'
defineOptions({
  name: 'Rolemange'
})

const props = withDefaults(defineProps<{ params: TRolePage }>(), {
  params: () => {
    return { roleId: '', roleName: '', pageIndex: 0, compName: 'RoleList' }
  }
})
/** 返回事件 */
const emit = defineEmits<{
  selectRolePage: [data: { params: TRolePage }]
}>()
/** 菜单按钮范围对象初始化 */
const initMenus: TRoleMenuModel[] = [
  {
    menuid: '3730eb147ea04573a90a5d61a39ad6df',
    menuname: '采写中心',
    menutype: '采编',
    checked: false,
    actives: [
      {
        scopeid: '9c9eb7e1b2de437da78baf87c9774469',
        activeScopeClass: 'Tbdept5',
        activekeycode: 'capture_dept_select',
        activename: '部门选送',
        ball: false,
        bmodify: true,
        checked: true,
        isshow: false,
        idepth: 5,
        sfunctionurl: 'dept/initDeptPersonal',
        sgroupkey: 'select',
        treeScope: true,
        scopes: [
          {
            scopeid: '9c9eb7e1b2de437da78baf87c9774469',
            businessid: '4ebd16c7271f5a1a12adab8cc861b7f8',
            businessname: '日照融媒体中心',
            checked: true,
            businesspid: '',
            iconSkin: 'dept1'
          }
        ]
      }
    ]
  }
]
/**
 * 高度
 */
const tableheight = ref(window.innerHeight - 115)
/** 菜单、按钮、范围对象 */
const menus = ref<TRoleMenuModel[]>([])
/** 菜单全选 */
const menuAllChk = ref(false)
/** 保存loading */
const loading = ref(false)
/** 打开选择部门树 */
const selectDeptDialogVisible = ref(false)
/** 打开选择媒体树 */
const selectMediaDialogVisible = ref(false)
/** 选择对应按钮范围属性对象 */
const activeScope = ref<TActiveScope>({
  operate: true,
  checkboxtype: true,
  scopeflag: EHgDeptZtreeUrl.DEPT_GETSYSEMPDEPTTREE,
  menuid: '',
  activekeycode: '',
  idepth: 5,
  ids: '',
  isapplyother: false,
  scopeid: '',
  names: '',
  checkoperationList: []
})
/** 缓存key */
let cacheDTOKey = ''

onMounted(() => {
  getRoleActionScopeByRoleId()
})

/**
 * 获取菜单按钮范围
 */
const getRoleActionScopeByRoleId = () => {
  getRoleActionScopeByRoleIdApi(props.params.roleId).then(
    ({ success, obj, msg }: IAxios<TRoleActionDTO>) => {
      if (success) {
        menus.value = obj.roleMenuModelList
        cacheDTOKey = obj.cacheDTOKey ?? ''
      } else {
        modal.msgError(msg)
      }
    }
  )
}

/**
 * 菜单全选
 */
const handleMenuAllChk = () => {
  menus.value.forEach((m) => {
    m.checked = menuAllChk.value
    m.actives.forEach((a) => {
      if (a.bmodify) {
        a.checked = menuAllChk.value
      }
      if (a.scopes.length) {
        a.scopes.forEach((s) => {
          s.checked = menuAllChk.value
        })
      }
    })
  })
}
/**
 * 菜单按钮前checkbox
 * @param menu
 */
const handleMenuChk = (menu: TRoleMenuModel) => {
  const flag = menu.checked
  menu.actives.forEach((a) => {
    if (a.bmodify) {
      a.checked = flag
    }
    if (a.scopes.length) {
      a.scopes.forEach((s) => {
        s.checked = flag
      })
    }
  })
}
/**
 * 点击打开选择范围树dialog
 */
const handleSelectScope = (menuId: string, active: TActiveModel) => {
  const selectIds: string[] = []
  if (active.scopes.length) {
    active.scopes.forEach(function (scope) {
      selectIds.push(scope.businessid)
    })
  }
  activeScope.value = {
    operate: true,
    checkboxtype: true,
    scopeflag:
      active.activeScopeClass.toLocaleLowerCase() === 'tbdept5'
        ? EHgDeptZtreeUrl.DEPT_GETSYSDEPTTREE
        : EHgDeptZtreeUrl.MEDIA_GETSYSMEDIATREE,
    menuid: menuId,
    activekeycode: active.activekeycode,
    idepth: active.idepth,
    ids: selectIds.join(','),
    isapplyother: false,
    scopeid: active.scopeid,
    names: '',
    checkoperationList: []
  }

  selectDeptDialogVisible.value = true
}
/**
 * 按钮范围选择监听
 * @param activeScope
 */
const handleSelectActiveScope = (activeScope: TActiveScope, isColse: boolean) => {
  if (!isColse) {
    updateActiveScope(activeScope)
  }
  selectDeptDialogVisible.value = false
  console.log(activeScope)
}

const updateActiveScope = (activeScope: TActiveScope) => {
  const menuid = activeScope.menuid
  const activekeycode = activeScope.activekeycode
  const scopeArray = activeScope.ids === '' ? [] : activeScope.ids.split(',')
  const scopeNameArray = activeScope.names === '' ? [] : activeScope.names.split(',')
  const isapplyother = activeScope.isapplyother
  const scopeid = activeScope.scopeid
  // 操作按钮
  const checkoperationList = activeScope.checkoperationList

  menus.value.forEach((item) => {
    if (isapplyother) {
      // 跨页面
      // 跨页面
      if (checkoperationList.length === 0) {
        item.actives.forEach((active) => {
          if (active.scopeid === scopeid && active.sgroupkey === '') {
            // 07包非正常按钮
            active.scopes = []
            scopeArray.forEach((scope, index) => {
              active.scopes.push({
                scopeid: active.scopeid,
                businessid: scope,
                businessname: scopeNameArray[index],
                checked: true
              })
            })
          }
        })
      } else {
        checkoperationList.forEach((operation) => {
          item.actives.forEach((active) => {
            if (active.scopeid === scopeid && active.sgroupkey === operation) {
              // 07包正常按钮
              active.scopes = []
              scopeArray.forEach((scope, index) => {
                active.scopes.push({
                  scopeid: active.scopeid,
                  businessid: scope,
                  businessname: scopeNameArray[index],
                  checked: true
                })
              })
            }
          })
        })
      }
    } else {
      if (item.menuid === menuid) {
        // 当前页面
        if (checkoperationList.length === 0) {
          // 没有选择常用操作按钮
          item.actives.forEach((active) => {
            if (active.activekeycode === activekeycode) {
              // 单独按钮
              active.scopes = []
              scopeArray.forEach((scope, index) => {
                active.scopes.push({
                  scopeid: active.scopeid,
                  businessid: scope,
                  businessname: scopeNameArray[index],
                  checked: true
                })
              })
            }
          })
        } else {
          checkoperationList.forEach((operation) => {
            item.actives.forEach((active) => {
              if (active.scopeid === scopeid && active.sgroupkey === operation) {
                // 07包正常按钮
                active.scopes = []
                scopeArray.forEach((scope, index) => {
                  active.scopes.push({
                    scopeid: active.scopeid,
                    businessid: scope,
                    businessname: scopeNameArray[index],
                    checked: true
                  })
                })
              }
            })
          })
        }
      }
    }
  })
}

/**
 * 保存
 */
const handleSave = () => {
  const postData: TRoleActionDTO = {
    roleId: props.params.roleId,
    roleMenuModelList: menus.value,
    cacheDTOKey: cacheDTOKey
  }
  saveRoleActionApi(postData).then(({ success, msg }: IAxios) => {
    if (success) {
      modal.msgSuccess('操作成功')
    } else {
      modal.msgError(msg)
    }
    props.params.compName = 'RoleList'
    emit('selectRolePage', props)
  })
}
/** 返回 */
const handleCancel = () => {
  props.params.compName = 'RoleList'
  emit('selectRolePage', props)
}
</script>

<style scoped>
.app-container {
  padding: 10px;
}
.menuTitle {
  margin: 10px;
}
.menuBody {
  background-color: #fff;
  line-height: 30px;
  color: #fff;
  overflow: auto;
  padding: 10px;
  border-radius: 10px;
  overflow-y: auto;
}

.checkboxlist {
  float: left;
  /*margin-left: 5px;*/
  width: 200px;
  font-size: 14px;
  position: relative;
  border-left: solid 1px #409eff;
  line-height: 28px;
}

.checkboxlist input[type='checkbox'] {
  margin-left: 10px;
}

input[type='checkbox'] {
  margin-left: 5px;
}

.checkboxlist h4 {
  background-color: #fff;
  text-align: center;
  margin: 0px;
  padding: 0px;
  padding: 5px;
  font-size: 14px;
  width: 100px;
}

.checkboxlist .down {
  position: relative;
}

.checkboxlist .fanwei {
  display: none;
  padding: 5px;
  border: solid 1px #ddd;
  background-color: #fff;
  /*display: none;*/
  position: absolute;
  width: 100px;
  z-index: 999;
}

.checkboxlist .fanwei h5 {
  margin: 0px;
  padding: 0px;
}

.borderbottom1 {
  border-bottom: solid 1px #fff;
  padding: 5px;
  margin: 0px;
  overflow-y: auto;
}

.borderbottom1-title {
  border-bottom: solid 1px #fff;
  font-weight: bold;
  background-color: #4b8df8;
  line-height: 40px;
  color: #fff;
  font-size: 14px;
}

.bg1 {
  background-color: #f0f9eb;
}

.bg2 {
  background-color: #fdf6ec;
}

.bg3 {
  background-color: #ecf5ff;
}

.bg4 {
  background-color: #fef0f0;
}

.col-md-1 {
  color: #cc0000;
  font-size: 14px;
  /*border-right: solid 1px #4b8df8;*/
  padding-left: 0px;
  padding-right: 0px;
  /*min-width: 105px;*/
}

.borderbottom1 {
  border-bottom: solid 1px var(--el-border-color);
}

.el-button--mini,
.el-button--mini.is-round {
  padding: 1px 5px;
  line-height: 1.5;
}

.el-checkbox {
  margin-left: 15px;
  line-height: 28px;
}

.btn-xs,
.btn-group-xs > .btn {
  padding: 1px 5px;
  font-size: 12px;
  line-height: 1.5;
  border-radius: 3px;
}

.btn-primary.btn {
  color: #fff;
  background-color: #428bca;
  border-color: #357ebd;
  margin-left: 2px;
}

.green-meadow.btn {
  color: white;
  background-color: #1bbc9b;
  border-color: '';
}

.blue-hoki.btn {
  color: white;
  background-color: #566c88;
  border-color: '';
}

.purple-plum.btn {
  color: white;
  background-color: #8775a7;
  border-color: '';
}
.el-button--small {
  margin-top: -5px;
}
.el-popover_box {
  max-height: 400px;
  overflow-y: auto;
  padding: 0;
}
.elsuccess:focus {
  background-color: var(--el-color-success);
}
.elprimary:focus {
  background-color: var(--el-color-primary);
}
.elinfo:focus {
  background-color: var(--el-color-info);
}
</style>
