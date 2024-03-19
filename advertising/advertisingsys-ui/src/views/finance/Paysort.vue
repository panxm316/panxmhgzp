<!--
 * @Author: wanghl
 * @Date: 2023-08-29 13:31:45
 * @LastEditTime: 2023-10-23 15:57:44
 * @LastEditors: wanghl
 * @Description:付款时间
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <!-- <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button> -->
        <el-tooltip class="item" effect="dark" content="帮助" placement="top">
          <el-button class="help" type="text" @click="showhelp()">?</el-button>
        </el-tooltip>
      </div>
    </div>
    <div class="table_box">
      <el-table
        :data="paysortList"
        row-key="id"
        :height="tableheight"
        :border="true"
        stripe
        :header-cell-style="tableHeaderColor"
      >
        <el-table-column type="selection" prop="ischeck" width="50" align="center">
        </el-table-column>
        <el-table-column prop="sname" label="名称" show-overflow-tooltip> </el-table-column>
        <el-table-column prop="skey" label="付款方式" :width="120" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.skey === 'bepub'">刊前付款</el-tag>
            <el-tag v-else type="warning">刊后付款</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sdesc" label="描述" show-overflow-tooltip> </el-table-column>
        <el-table-column prop="sremark" label="备注" show-overflow-tooltip> </el-table-column>
        <el-table-column prop="isort" label="序号" :width="80" sortable align="center">
        </el-table-column>
        <el-table-column prop="buse" label="启用" :width="80" align="center" sortable="custom">
          <template #default="scope">
            <el-checkbox v-model="scope.row.buse" disabled></el-checkbox>
          </template>
        </el-table-column>
        <!-- <el-table-column
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
        </el-table-column> -->
      </el-table>
    </div>
    <!-- 添加或修改菜单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="广告色彩编辑"
      width="700"
      align-center
      :close-on-click-modal="false"
    >
      <el-form ref="paysortFormRef" :model="paysort" label-width="100px" :rules="rules">
        <el-row :gutter="50">
          <el-col :span="22">
            <el-form-item label="名称" prop="sname">
              <el-input v-model="paysort.sname" />
            </el-form-item>
            <el-form-item label="付款时间" prop="skey">
              <el-select v-model="paysort.skey" placeholder="付款时间" style="width: 110px">
                <el-option label="刊前付款" value="bepub"></el-option>
                <el-option label="刊后付款" value="afpub"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="描述" prop="sdesc">
              <el-input v-model="paysort.sdesc" />
            </el-form-item>
            <el-form-item label="备注" prop="sremark">
              <el-input v-model="paysort.sremark" type="textarea" :maxlength="200" />
            </el-form-item>
            <el-form-item label="序号" prop="isort">
              <el-input-number
                v-model="paysort.isort"
                :min="1"
                :max="99999"
                controls-position="right"
              />
            </el-form-item>
            <el-form-item label="启用" prop="buse">
              <el-checkbox v-model="paysort.buse" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" icon="Check" @click="handleSave">保存</el-button>
          <el-button icon="Close" @click="handleCancel">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick } from 'vue'
import { FormRules, FormInstance } from 'element-plus'
import type { TPaysort } from '@/types/views/finance/Paysort'
import {
  getPaysortListApi,
  savePaysortApi,
  updatepaysortAPi,
  deletepaysortApi
} from '@/api/finance/Paysort'
import { required, computTableHeight, tableHeaderColor } from '@/utils/index'
import type { IAxios } from 'axios'
import modal from '@/plugins/modal'
defineOptions({
  name: 'Paysort'
})
import { useRouter } from 'vue-router'
const router = useRouter()
/** 表格高度 */
const tableheight = ref(0)
/** 编辑 dialog */
const dialogVisible = ref(false)
/** 付款时间列表 */
const paysortList = ref<TPaysort[]>([])
/** 付款时间 */
const paysort = ref<TPaysort>({
  /**
   * 是否启用
   */
  buse: true,
  /**
   * id
   */
  id: '',
  /**
   * 序号
   */
  isort: 0,
  /**
   * 描述
   */
  sdesc: '',
  /**
   * 付款时间关键字 刊前付款 bepub 刊后付款 afpub
   */
  skey: '',
  /**
   * 类别名称
   */
  sname: '',
  /**
   * 备注
   */
  sremark: ''
})
const paysortFormRef = ref<FormInstance>()

/**
 * 校验设置参数
 */
const rules = reactive<FormRules<TPaysort>>({
  sname: [
    { validator: required, required: true, message: '请输入色彩名称', trigger: 'blur' },
    { max: 20, message: '不得大于20个字符', trigger: 'blur' }
  ]
})
onMounted(() => {
  getpaysortList()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight(false)
})

/**
 * 获取付款时间列表
 */
const getpaysortList = () => {
  getPaysortListApi().then(({ obj }: IAxios<TPaysort[]>) => {
    console.log(obj)
    paysortList.value = obj
  })
}
/**
 * 新增
 */
const handleAdd = () => {
  reset()
  dialogVisible.value = true
}
/**
 * 修改
 * @param row
 */
const handleUpdate = (row: TPaysort) => {
  paysort.value = { ...row }
  dialogVisible.value = true
}
/**
 * 表单重置
 */
const reset = () => {
  paysort.value = {
    /**
     * 是否启用
     */
    buse: true,
    /**
     * id
     */
    id: '',
    /**
     * 序号
     */
    isort: 1,
    /**
     * 描述
     */
    sdesc: '',
    /**
     * 付款时间关键字 刊前付款 bepub 刊后付款 afpub
     */
    skey: '',
    /**
     * 类别名称
     */
    sname: '',
    /**
     * 备注
     */
    sremark: ''
  }
}
/**
 * 保存
 */
const handleSave = () => {
  paysortFormRef.value?.validate((valid) => {
    if (valid) {
      if (!paysort.value.id) {
        savePaysortApi(paysort.value).then(({ success, msg }: IAxios) => {
          if (success) {
            modal.msgSuccess('操作成功')
          } else {
            modal.msgError(msg)
          }
          getpaysortList()
        })
      } else {
        updatepaysortAPi(paysort.value).then(({ success, msg }: IAxios) => {
          if (success) {
            modal.msgSuccess('操作成功')
          } else {
            modal.msgError(msg)
          }
          getpaysortList()
        })
      }
      dialogVisible.value = false
    }
  })
}
/**
 * 取消
 */
const handleCancel = () => {
  dialogVisible.value = false
  reset()
}
/**
 * 删除
 * @param row
 */
const handleDelete = (row: TPaysort) => {
  // const data = {
  //   id: row.id
  // }
  modal
    .confirm('是否确认删除名称为"' + row.sname + '"的数据项?')
    .then(function () {
      return deletepaysortApi(row.id)
    })
    .then(() => {
      getpaysortList()
      modal.msgSuccess('删除成功')
    })
    .catch(() => {})
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'paysort'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>

<style scoped lang="scss"></style>
