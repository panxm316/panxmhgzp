<!--
 * @Date: 2022-08-26 16:29:24
 * @LastEditors: wanghl
 * @LastEditTime: 2023-10-14 15:30:58
 * @FilePath: /Workflow-Vue3/src/components/selectResult.vue
-->
<template>
  <div class="select-result l">
    <p class="clear">
      已选（{{ total }}）
      <a style="color: #f56c6c" @click="emits('del')">清空</a>
    </p>
    <ul>
      <template v-for="{ type, data, cancel } in list" :key="type">
        <template v-if="type === 'role'">
          <li v-for="item in data" :key="item.id">
            <div style="display: flex; flex-direction: row">
              <div class="f11">
                <svg-icon icon-class="peoplerole" size="18" />
              </div>
              <div class="f12">{{ item.name }}</div>
              <div class="f13">
                <el-button size="small" text :icon="CircleClose" @click="cancel(item)"></el-button>
              </div>
            </div>
          </li>
        </template>
        <template v-if="type === 'dept'">
          <li v-for="item in data" :key="item.id">
            <div style="display: flex; flex-direction: row">
              <div class="f11">
                <el-icon style="font-size: 20px"><Grid /></el-icon>
              </div>
              <div class="f12">{{ item.name }}</div>
              <div class="f13">
                <el-button size="small" text :icon="CircleClose" @click="cancel(item)"></el-button>
              </div>
            </div>
          </li>
        </template>
        <template v-if="type === 'user'">
          <li v-for="item in data" :key="item.id">
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
              <div class="f13">
                <el-button size="small" text :icon="CircleClose" @click="cancel(item)"></el-button>
              </div>
            </div>
          </li>
        </template>
      </template>
    </ul>
  </div>
</template>
<script setup>
import iconFile from '@/assets/images/icon_file.png'
import {
  CircleClose,
  Delete,
  Edit,
  Grid,
  Share,
  Message,
  Search,
  Star
} from '@element-plus/icons-vue'

defineProps({
  total: {
    type: Number,
    default: 0
  },
  list: {
    type: Array,
    default: () => [{ type: 'role', data, cancel }]
  }
})
const emits = defineEmits(['del'])
</script>

<style lang="scss" scoped>
.select-result {
  width: 326px;
  height: 100%;
  font-size: 12px;

  ul {
    height: 460px;
    overflow-y: auto;

    li {
      margin: 11px 26px 13px 19px;
      line-height: 17px;

      span {
        vertical-align: middle;
      }

      img {
        &:first-of-type {
          width: 14px;
          vertical-align: middle;
          margin-right: 5px;
        }

        &:last-of-type {
          float: right;
          margin-top: 2px;
          width: 14px;
        }
      }
    }
  }

  p {
    padding-left: 19px;
    padding-right: 20px;
    line-height: 37px;
    border-bottom: 1px solid #f2f2f2;

    a {
      float: right;
    }
  }
}

.f11 {
  width: 30px;
}

.f12 {
  width: calc(100% - 60px);
  height: 25px;
  line-height: 25px;
  font-size: 14px;
}

.f13 {
  width: 30px;
}
</style>
