<template>
  <div>
    <el-row :gutter="20" :span="24">
      <el-col :span="12" class="col_box"
        ><hg-ztree
          :operate="ztreeProps.operate"
          :checkboxtype="ztreeProps.checkboxtype"
          :selectids="ztreeProps.selectids"
          :scopeflag="ztreeProps.scopeflag"
          @selectionztree="handleSelectionDeptZtree"
        ></hg-ztree
      ></el-col>
      <el-col :offset="1" :span="11" class="col_box">
        <el-row style="width: 100%; border-bottom: 1px solid var(--el-border-color)">
          <el-col :offset="2">
            <el-checkbox @change="handleCheckAppliction">应用到相同范围设置</el-checkbox>
          </el-col>
        </el-row>

        <el-row>
          <el-col :offset="2">
            <el-checkbox
              v-model="checkAll"
              :indeterminate="isIndeterminate"
              @change="handleCheckAllChange"
              >全选</el-checkbox
            >
          </el-col>
        </el-row>
        <el-row>
          <el-col :offset="2">
            <el-checkbox-group v-model="activeScope.checkoperationList" @change="handleCheckChange">
              <el-checkbox :label="EHgActiveOperation.QUERY" :border="true">查询</el-checkbox>
            </el-checkbox-group>
          </el-col>
        </el-row>
        <el-row>
          <el-col :offset="2">
            <el-checkbox-group v-model="activeScope.checkoperationList" @change="handleCheckChange">
              <el-checkbox :label="EHgActiveOperation.MODIFY" :border="true">修改</el-checkbox>
            </el-checkbox-group>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-row :span="24">
      <div style="text-align: right; width: 100%">
        <el-button icon="Close" @click="handleCloseDeptDialog">取 消</el-button>
        <el-button type="primary" icon="Check" @click="handleSaveDeptDialog">保 存 </el-button>
      </div>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { CheckboxValueType } from 'element-plus'
import { TSelectZtree } from '@/types/common/index'
import { EHgActiveOperation, EHgDeptZtreeUrl } from '@/types/common/enumindex'
import { TActiveScope } from '@/types/views/system/role'
import type { THgZtreeProps } from '@/types/components/hgztree'

defineOptions({
  name: 'HgRoleScope'
})
const props = withDefaults(defineProps<{ activeScope: TActiveScope }>(), {
  activeScope: () => {
    return {
      operate: true,
      checkboxtype: true,
      scopeflag: EHgDeptZtreeUrl.DEPT_GETSYSDEPTTREE,
      menuid: '',
      activekeycode: '',
      idepth: 5,
      ids: '',
      isapplyother: false,
      scopeid: '',
      names: '',
      checkoperationList: []
    }
  }
})

/** 导出返回数据 */
const emit = defineEmits<{
  selectActiveScope: [data: TActiveScope, isColse: boolean]
}>()
/** 按钮范围 */
const activeScope = ref<TActiveScope>({ ...props.activeScope })
/** deptZtree属性 */
const ztreeProps = ref<THgZtreeProps>({
  operate: activeScope.value.operate,
  checkboxtype: activeScope.value.checkboxtype,
  selectids: activeScope.value.ids.split(','),
  scopeflag: activeScope.value.scopeflag
})
/** 全选 */
const checkAll = ref(false)
/** 半选 */
const isIndeterminate = ref(false)
/** 操作类型 */
const operation = [EHgActiveOperation.QUERY, EHgActiveOperation.MODIFY]
/** 选择范围监听 */
const selectZtrees = ref<TSelectZtree[]>([])

onMounted(() => {})

/**
 * 部门选择监听
 * @param selectZtree
 */
const handleSelectionDeptZtree = (selectZtree: TSelectZtree[]) => {
  selectZtrees.value = selectZtree
  console.log(selectZtree)
}
/**
 * 应用到相同范围
 */
const handleCheckAppliction = (value: CheckboxValueType) => {
  activeScope.value.isapplyother = value as boolean
}

/**
 * 全选
 */
const handleCheckAllChange = (value: CheckboxValueType) => {
  activeScope.value.checkoperationList = value ? operation : []
  isIndeterminate.value = false
}
/**
 * 查询、修改check
 */
const handleCheckChange = (value: CheckboxValueType[]) => {
  const checkedCount = value.length
  checkAll.value = checkedCount === operation.length
  isIndeterminate.value = checkedCount > 0 && checkedCount < operation.length
}

/**
 * 部门范围选择确定
 */
const handleSaveDeptDialog = () => {
  activeScope.value.ids = selectZtrees.value.map((item) => item.id).join(',')
  activeScope.value.names = selectZtrees.value.map((item) => item.name).join(',')
  emit('selectActiveScope', activeScope.value, false)
}
/**
 * 部门范围选择关闭
 */
const handleCloseDeptDialog = () => {
  emit('selectActiveScope', activeScope.value, false)
}
</script>

<style scoped>
a span {
  font-size: 14px;
}

.checkbox-list .checkbox-inline {
  margin-left: 0px;
}

.el-row {
  margin-bottom: 20px;
  /*&:last-child {
                margin-bottom: 0;
            }*/
}
.col_box {
  border: 1px solid var(--el-border-color);
  padding: 10px;
}
</style>
