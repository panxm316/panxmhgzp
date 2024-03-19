<template>
  <div>
    <el-tag
      v-for="(item, index) in data"
      :key="item.id"
      style="margin-right: 5px; margin-top: 5px"
      :closable="!disabled"
      :type="item.type === 'dept' ? 'primary' : item.type === 'user' ? 'warning' : 'success'"
      size="large"
      @close="removeItem(index, item.id, item.type)"
    >
      {{ item.name }}
    </el-tag>
  </div>
</template>

<script setup>
const emits = defineEmits(['update:data'])

const props = defineProps({
  data: {
    type: Array,
    default: () => []
  },
  disabled: {
    type: Boolean,
    default: false
  }
})

const removeItem = (index, id, type) => {
  emits(
    'update:data',
    props.data.filter((res) => !(res.id === id && res.type === type))
  )
}
</script>
<style></style>
