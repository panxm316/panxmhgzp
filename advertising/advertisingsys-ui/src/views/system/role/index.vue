<template>
  <div>
    <component :is="comp" :params="params" @selectRolePage="onSelectRolePage"></component>
  </div>
</template>

<script setup lang="ts">
import RoleList from '@/views/system/role/RoleList.vue'
import RoleManage from '@/views/system/role/RoleManage.vue'
import RoleEmploy from '@/views/system/roleemploy/index.vue'
import type { TRolePage } from '@/types/views/system/role'

defineOptions({
  name: 'Role'
})

const comp = shallowRef(RoleList)
const params = ref<TRolePage>({} as TRolePage)
params.value.pageIndex = 0

const onSelectRolePage = (data: { params: TRolePage }) => {
  console.log(data)

  if (data.params.compName === 'RoleManage') {
    comp.value = RoleManage
  }
  if (data.params.compName === 'RoleList') {
    comp.value = RoleList
  }
  if (data.params.compName === 'RoleEmploy') {
    comp.value = RoleEmploy
  }
}
</script>

<style scoped></style>
