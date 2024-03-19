<template>
  <div>
    <el-input
      v-if="type != 'role'"
      v-model="searchVal"
      class="w-50 m-2"
      style="width: 100%"
      placeholder="搜索成员"
      :prefix-icon="Search"
      @input="getDebounceData($event)"
    />
    <p v-if="!searchVal && type != 'role'" class="ellipsis tree_nav">
      <span class="ellipsis" style="margin-left: 10px" @click="queryData(0)">根节点</span>
      <span
        v-for="(item, index) in departments.titleDepartments"
        :key="index + 'a'"
        class="ellipsis"
        @click="queryData(item.id)"
        >{{ item.name }}</span
      >
    </p>

    <ul class="select-box">
      <template v-for="(elem, i) in dataList" :key="i">
        <template v-if="elem.type === 'role'">
          <li v-for="item in elem.data" :key="item.id" style="line-height: 26px">
            <el-checkbox
              v-model="item.selected"
              :disabled="item.status == 0"
              @change="changeEvent(item)"
            >
              <div style="display: flex; flex-direction: row" title="角色">
                <div class="f11">
                  <svg-icon icon-class="peoplerole" size="18" />
                </div>
                <div class="f12">{{ item.name }}</div>
              </div>
            </el-checkbox>
          </li>
        </template>
        <template
          v-if="elem.type === 'dept' && (type === 'org' || type === 'dept' || type === 'user')"
        >
          <li v-for="item in elem.data" :key="item.id" style="line-height: 40px">
            <div style="display: flex; flex-direction: row">
              <div class="d11">
                <el-checkbox
                  v-model="item.selected"
                  :disabled="!(type === 'org' || type === 'dept') || item.status == 0"
                  @change="changeEvent(item)"
                >
                  <div style="display: flex; flex-direction: row">
                    <div class="f11">
                      <svg-icon icon-class="dept" size="18" />
                    </div>
                    <div class="f12">{{ item.name }}</div>
                  </div>
                </el-checkbox>
              </div>
              <div class="d22" title="下级" @click="queryData(item.id)">
                <svg-icon icon-class="xiaji" size="18" />
              </div>
            </div>
          </li>
        </template>
        <template v-if="elem.type === 'user' && (type === 'org' || type === 'user')">
          <li v-for="item in elem.data" :key="item.id" class="check_box">
            <el-checkbox
              v-model="item.selected"
              :disabled="item.status == 0 || (!selectSelf && currentUserId === item.id)"
              @change="changeEvent(item)"
            >
              <div style="display: flex; flex-direction: row">
                <div class="f11">
                  <el-avatar
                    shape="square"
                    :size="20"
                    :src="item.avatar"
                    style="background-color: #38adff"
                    >{{ item.name.substring(0, 1) }}</el-avatar
                  >
                </div>
                <div class="f12">{{ item.name }}</div>
              </div>
            </el-checkbox>
          </li>
        </template>
      </template>
    </ul>
  </div>
</template>
<script setup>
var props = defineProps({
  selectedList: {
    type: Array,
    default: () => []
  },
  type: {
    type: String,
    default: 'org'
  },
  multiple: {
    type: Boolean,
    default: true
  },
  selectSelf: {
    type: Boolean,
    default: true
  }
})
import useUserStore from '@/store/modules/user'
import { deepCopy } from '@/utils/index'

const userStore = useUserStore()

const currentUserId = computed(() => {
  return userStore.userId
})

import { Delete, Edit, Search, Share, OfficeBuilding, Grid } from '@element-plus/icons-vue'

const queryData = (pid) => {
  getDepartmentList(pid, props.type).then((res) => {
    const selectedList = props.selectedList

    for (var it of dataList.value) {
      console.log(it, '))))))))))))))')

      for (var item of it.data) {
        var b =
          selectedList.filter((res) => res.id === item.id && res.type === item.type).length > 0
        item.selected = b
      }
    }
  })
}

const deptList = computed(() => {
  return departments.value.childDepartments
})
const userList = computed(() => {
  return departments.value.employees
})
const roleList = computed(() => {
  return departments.value.roleList
})

var dataList = computed(() => {
  const newVar = [
    {
      type: 'dept',
      data: deptList.value
    },
    {
      type: 'user',
      data: userList.value
    },
    {
      type: 'role',
      data: roleList.value
    }
  ]
  return newVar
})

const { proxy } = getCurrentInstance()
import { departments, getDebounceData, getDepartmentList, searchVal } from './dialog/common'
const emits = defineEmits(['update:selectedList'])
onMounted(() => {
  queryData(0)
})
const changeEvent = (e) => {
  let selectedList = deepCopy(props.selectedList)

  if (e.selected) {
    if (!props.multiple) {
      userList.value.forEach((res) => (res.selected = false))
      selectedList = []
    }
    e.selected = true
    selectedList.push(e)
  } else {
    for (var it of dataList.value) {
      const filter = it.data.filter((res) => res.id === e.id && res.type === e.type)
      if (filter.length > 0) {
        filter[0].selected = false
      }
    }
    selectedList = selectedList.filter((res) => !(res.id === e.id && res.type === e.type))
  }
  emits('update:selectedList', selectedList)
}

const refreshData = () => {
  const selectedList = props.selectedList

  for (var it of dataList.value) {
    for (var item of it.data) {
      var b = selectedList.filter((res) => res.id === item.id && res.type === item.type).length > 0
      item.selected = b
    }
  }
}
defineExpose({ queryData, changeEvent, refreshData })

watch(
  () => props.selectedList,
  (val) => {
    refreshData()
  }
)
</script>
<style lang="scss" scoped>
@import '../css/dialog.css';

.select-box {
  height: 420px;
  overflow-y: auto;
  padding-left: 26px;
  li {
    padding: 5px 0;

    //i {
    //	float: right;
    //	padding-left: 24px;
    //	padding-right: 10px;
    //	color: #3195f8;
    //	font-size: 12px;
    //	cursor: pointer;
    //	background: url(../assets/images/next_level_active.png) no-repeat 10px center;
    //	border-left: 1px solid rgb(238, 238, 238);
    //}

    //
    //a.active+i {
    //  color: rgb(197, 197, 197);
    //  background-image: url(../assets/images/next_level.png);
    //  pointer-events: none;
    //}

    img {
      //width: 14px;
      //vertical-align: middle;
      //margin-right: 5px;
    }
  }
}
.radio_box a,
.check_box a {
  font-size: 12px;
  position: relative;
  padding-left: 20px;
  margin-right: 30px;
  cursor: pointer;
  color: #333;
  white-space: pre;
}

.check_box.not a:hover {
  color: #333;
}

.check_box.not a::before,
.check_box.not a:hover::before {
  border: none;
}

.check_box.not.active {
  background: #f3f3f3;
}

.radio_box a:hover::before,
.check_box a:hover::before {
  border: 1px solid #46a6fe;
}

.radio_box a::before,
.check_box a::before {
  position: absolute;
  width: 14px;
  height: 14px;
  border: 1px solid #dcdfe6;
  border-radius: 2px;
  left: 0;
  top: 1px;
  content: '';
}

.radio_box a::before {
  border-radius: 50%;
}

.check-dot.active::after,
.radio_box a.active::after,
.check_box a.active::after {
  position: absolute;
  width: 10px;
  height: 10px;
  border-radius: 50%;
  top: 3px;
  left: 3px;
  content: '';
}

.radio_box a.active::after {
  background: #46a6fe;
}

.check_box a.active::after {
  background: url(@/assets/images/check_box.png) no-repeat center;
}

.f11 {
  width: 30px;
}

.f12 {
  width: calc(100% - 30px);
  height: 20px;
  line-height: 20px;
  font-size: 14px;
}

.d11 {
  width: calc(100% - 30px);
}

.d22 {
  width: 30px;
  line-height: 40px;
  cursor: pointer;
}
</style>
