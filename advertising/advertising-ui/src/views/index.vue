<!--
 * @Author: wanghl
 * @Date: 2023-08-08 09:39:16
 * @LastEditTime: 2023-10-07 16:59:09
 * @LastEditors: wanghl
 * @Description:
-->
<template>
  <div class="app-container home"></div>
</template>

<script setup lang="ts">
defineOptions({
  name: 'Index'
})
const version = ref('3.6.1')
function goTarget(url: any) {
  window.open(url, '__blank')
}
</script>

<style scoped lang="scss">
.home {
  background-image: url('../assets/images/shouye.png');
  background-repeat: no-repeat;
  background-size: 100%;
  height: 100%;
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: 'open sans', 'Helvetica Neue', Helvetica, Arial, sans-serif, 'Helvetica Neue',
    Helvetica, Tahoma, Arial, 'PingFang SC', 'Source Han Sans CN', 'Source Han Sans',
    'Source Han Serif', 'Hiragino Sans GB', 'WenQuanYi Micro Hei', 'Microsoft YaHei', sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>
