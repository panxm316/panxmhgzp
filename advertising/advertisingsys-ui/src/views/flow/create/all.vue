<!--
 * @Author: peij
 * @Date: 2023-10-12 09:01:05
 * @LastEditTime: 2023-10-23 15:41:20
 * @LastEditors: wanghl
 * @Description: 工作流设置
-->
<template>
  <div>
    <div class="titlebar">
      <div class="f1">
        <el-text tag="b" size="large" type="primary"
          ><svg-icon icon-class="createFlow" size="16" style="margin-right: 10px" />{{
            flowName
          }}</el-text
        >
      </div>
      <div class="f2">
        <span class="center_t" effect="dark" :activeStep="activeStep == 0" @click="activeStep = 0">
          <span :activeStep="activeStep == 0">1</span>
          <span>基础信息</span>
        </span>
        <!-- <span class="center_t" effect="dark" :activeStep="activeStep == 1" @click="activeStep = 1">
          <span :activeStep="activeStep == 1">2</span>
          <span>表单设计</span>
        </span> -->
        <span class="center_t" effect="dark" :activeStep="activeStep == 1" @click="handleStep2">
          <span :activeStep="activeStep == 1">2</span>
          <span>流程设计</span>
        </span>
      </div>
      <div class="f3">
        <!--        <el-button @click.stop="clickNext">下一步</el-button>-->
        <el-button type="primary" @click="publish">发 布</el-button>
        <el-tooltip class="item" effect="dark" content="帮助" placement="top">
          <el-button class="help" type="text" @click="showhelp()">?</el-button>
        </el-tooltip>
      </div>
    </div>

    <!-- <div style="height: 5px; background-color: white; margin-bottom: 0px"></div> -->
    <step1
      v-show="activeStep === 0"
      ref="step1Ref"
      :group-id="paramGroupId"
      @clearStep3="handleClearStep3"
    />
    <!-- <step2 v-show="activeStep === 1" ref="step2Ref" /> -->
    <step3 v-show="activeStep === 1" ref="step3Ref" :node-config-obj="step3NodeConfig" />
    <!--			//验证每一步-->
    <el-dialog v-model="validateDialogShow" title="流程检查">
      <el-steps :active="validateFlowStep" finish-status="success" simple style="margin-top: 20px">
        <el-step title="基础信息" />
        <!-- <el-step title="表单设计" /> -->
        <el-step title="流程设计" />
      </el-steps>

      <div style="text-align: center">
        <el-result
          v-if="validateFlowStep == 2"
          icon="success"
          title="检查成功"
          sub-title="流程检查完成，现在提交？"
        >
          <template #extra>
            <el-button type="primary" @click="submitFlow">提交</el-button>
          </template>
        </el-result>
        <el-result
          v-if="
            validateErrMsg.length == 0 &&
            validateDialogShow &&
            validatingShow &&
            validateFlowStep < 2
          "
          title="检查中"
          sub-title="正在检查流程信息"
        >
          <template #icon>
            <span
              v-loading="true"
              style="display: inline-block; border: 0px solid red; width: 100px; height: 100px"
            >
            </span>
          </template>
        </el-result>

        <el-result v-if="validateErrMsg.length > 0" icon="error" title="检查失败">
          <template #sub-title>
            <div v-for="(item, index) in validateErrMsg" :key="index">
              <el-text type="danger">
                <el-icon>
                  <WarnTriangleFilled />
                </el-icon>
                {{ item }}
              </el-text>
            </div>
          </template>
          <template #extra>
            <el-button type="primary" @click="gotoEdit">去修改</el-button>
          </template>
        </el-result>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { Plus, WarnTriangleFilled } from '@element-plus/icons-vue'
import { addFlow, getFlowDetail } from '@/api/flow/index'
import Step1 from './step1.vue'
// import Step2 from './step2.vue'
import Step3 from './step3.vue'
import { useFlowStore } from '../workflow/stores/flow'
import { LocationQuery, LocationQueryValue, useRouter } from 'vue-router'
import { useRoute } from 'vue-router'
import useUserStore from '@/store/modules/user'
import { isBlank, isNotBlank, deepCopy } from '@/utils/index'
import { IAxios } from 'axios'

defineOptions({
  name: 'CreateFlow'
})

const { user } = useUserStore()
const route = useRoute()
const router = useRouter()
const store = useFlowStore()
const step1Ref = ref()
// const step2Ref = ref()
const step3Ref = ref()

const step3NodeConfig = ref()
const paramGroupId = ref()

const validateErrMsg = ref<string[]>([])

const flowName = computed(() => {
  const name1 = store.step1.name
  if (isBlank(name1)) {
    return '未命名表单'
  }
  return name1
})

const activeStep = ref(0)
const validateFlowStep = ref(0)
const validateDialogShow = ref(false)
const validatingShow = ref(false)

const gotoEdit = () => {
  activeStep.value = validateFlowStep.value
  validateDialogShow.value = false
}
const publish = () => {
  validateErrMsg.value = []

  validateFlowStep.value = 0
  validateDialogShow.value = true
  validatingShow.value = true

  setTimeout(function () {
    // 1
    checkStep1()
  }, 500)
}
onMounted(() => {
  const query: LocationQuery = route.query
  const groupId = (query.groupId as LocationQueryValue) ?? ''
  const flowId = (query.flowId as LocationQueryValue) ?? ''
  const cp = (query.cp as LocationQueryValue) ?? ''

  if (isNotBlank(groupId)) {
    paramGroupId.value = groupId
  }
  if (isNotBlank(flowId)) {
    getFlowDetail(flowId).then(({ success, obj: data }: IAxios) => {
      store.step1.admin = JSON.parse(data.admin)
      store.step1.name = data.name
      store.step1.logo = data.logo
      if (!cp || !(parseInt(cp) === 1)) {
        // 复制
        store.step1.flowId = flowId
      }
      store.step1.remark = data.remark
      store.step1.groupId = data.groupId

      store.setStep2(JSON.parse(data.formItems))
      step3NodeConfig.value = JSON.parse(data.process)
    })
  } else {
    // 新增
    const userId = user.userid
    const name = user.username
    // const avatar = user.simg
    store.step1.admin = [
      {
        id: userId,
        name: name,
        // avatar: avatar,
        type: 'user'
      }
    ]
    store.step1.groupId = ''
    store.step1.name = ''
    store.step1.remark = ''
  }
})

const handleStep2 = () => {
  step1Ref.value.validate((valid: boolean, arr: string[]) => {
    if (valid) {
      activeStep.value = 1
    } else {
      validatingShow.value = false
      // 错误信息
      validateErrMsg.value = arr
    }
  })
}
/**
 * 清除流程设置器数据
 */
const handleClearStep3 = () => {
  step3Ref.value.initProcessData()
}

const checkStep1 = () => {
  step1Ref.value.validate((valid: boolean, arr: string[]) => {
    if (valid) {
      validateFlowStep.value = 1

      setTimeout(() => {
        checkStep3()
      }, 500)
    } else {
      validatingShow.value = false
      // 错误信息
      validateErrMsg.value = arr
    }
  })
}
// const checkStep2 = () => {
//   step2Ref.value.validate(function (valid, arr) {
//     if (valid) {
//       setTimeout(function () {
//         validateFlowStep.value = 2
//         checkStep3()
//       })
//     } else {
//       validatingShow.value = false
//       // 错误信息
//       validateErrMsg.value = arr
//     }
//   })
// }
const checkStep3 = () => {
  setTimeout(function () {
    step3Ref.value.validate(function (valid: boolean, arr: string[]) {
      if (valid) {
        validateFlowStep.value = 2
      } else {
        validatingShow.value = false
        // 错误信息
        validateErrMsg.value = arr
      }
    })
  })
}

const submitFlow = () => {
  step3Ref.value.getProcessData().then((res: any) => {
    const step1 = store.step1
    const step2 = store.step2

    const flow = deepCopy(step1)
    flow.formItems = JSON.stringify(step2)
    flow.process = JSON.stringify(res)
    flow.admin = JSON.stringify(step1.admin)

    addFlow(flow).then((res) => {
      validateDialogShow.value = false
      store.$reset()
      router.push('/flow/flowlist')
    })
  })
}
/**
 * 页面跳转
 */
const showhelp = () => {
  const to = '/template/Help?name=' + 'createFlow'
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
</script>
<style scoped lang="scss">
$f2_width: 800px;
.titlebar {
  // padding-top: 10px;
  padding-bottom: 5px;
  // height: 60px;
  display: flex;
  flex-direction: row;
  margin: 5px;
  background-color: #fff;
  border-radius: 5px;
  margin-bottom: 10px;

  .f1 {
    width: calc(100% / 2 - $f2_width / 2);
    line-height: 46px;
    padding-left: 20px;
  }

  .f2 {
    width: $f2_width;
    text-align: center;
  }

  .f3 {
    width: calc(100% / 2 - $f2_width / 2);

    text-align: right;
    line-height: 46px;
    height: 46px;
    padding-right: 20px;
  }
}

.center_t {
  cursor: pointer;
  padding: 10px 20px;
  display: inline-block;
  span:first-child {
    margin-right: 6px;
    font-size: 16px;
    font-weight: 400;
    text-align: center;
    line-height: 22px;
    border: 1px solid;
    border-radius: 50%;
    width: 24px;
    height: 24px;
    display: inline-block;
  }

  span:first-child[activeStep='true'] {
    color: white;
    background-color: var(--el-color-primary);
  }

  span:last-child {
    font-weight: 500;
    font-size: 18px;
  }
}

.center_t[activeStep='true'] {
  border-bottom: 2px solid var(--el-color-primary);
  color: var(--el-color-primary);
}
</style>
