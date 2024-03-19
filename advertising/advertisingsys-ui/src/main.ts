/*
 * @Author: wanghl
 * @Date: 2023-08-08 16:38:43
 * @LastEditTime: 2023-09-02 16:10:55
 * @LastEditors: peij
 * @Description:
 */
import { createApp } from 'vue'
import Cookies from 'js-cookie'
import ElementPlus from 'element-plus'
import ElTableNext from 'el-table-next'
import mitt from 'mitt'

// jquery
import jQuery from 'jquery'
Object.assign(window, { $: jQuery, jQuery })
// 全局挂载
window.emitter = mitt()
// webuploader
import '@/assets/vendor/webuploader/radialIndicator.js'
import '@/assets/vendor/webuploader/hgwebuploader.js'

// ztree
import '@ztree/ztree_v3/js/jquery.ztree.core.min.js'
import '@ztree/ztree_v3/js/jquery.ztree.excheck.min.js'
import '@ztree/ztree_v3/js/jquery.ztree.exedit.min.js'
// import '@ztree/ztree_v3/css/metroStyle/metroStyle.css'
// import '@ztree/ztree_v3/css/zTreeStyle/zTreeStyle.css'
import '@/assets/zTreeStyle/metro.css'
import '@ztree/ztree_v3/index.d.ts'

// 中文语言
// import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import 'element-plus/dist/index.css'
// import 'element-plus/theme-chalk/dark/css-vars.css'
import '@/assets/styles/index.scss' // global css
import 'uno.css'

import App from './App.vue'
import store from './store'
import router from './router'
import directive from './directive' // directive

// 注册指令
import plugins from './plugins' // plugins
import { download } from '@/utils/request'

// svg图标
import 'virtual:svg-icons-register'
import SvgIcon from '@/components/SvgIcon/index.vue'
import elementIcons from '@/components/SvgIcon/svgicon'

// permission control
import './permission'
// 全局配置
import '@/utils/global'

import { useDict } from '@/utils/dict'
import {
  parseTime,
  resetForm,
  addDateRange,
  handleTree,
  selectDictLabel,
  selectDictLabels
} from '@/utils/ruoyi'

// 分页组件
import Pagination from '@/components/Pagination/index.vue'
// 自定义表格工具组件
import RightToolbar from '@/components/RightToolbar/index.vue'
// 文件上传组件
import FileUpload from '@/components/FileUpload/index.vue'
// 图片上传组件
import ImageUpload from '@/components/ImageUpload/index.vue'
// 图片预览组件
import ImagePreview from '@/components/ImagePreview/index.vue'
// 自定义树选择组件
import TreeSelect from '@/components/TreeSelect/index.vue'

const app = createApp(App)

// 全局方法挂载
app.config.globalProperties.useDict = useDict
app.config.globalProperties.download = download
app.config.globalProperties.parseTime = parseTime
app.config.globalProperties.resetForm = resetForm
app.config.globalProperties.handleTree = handleTree
app.config.globalProperties.addDateRange = addDateRange
app.config.globalProperties.selectDictLabel = selectDictLabel
app.config.globalProperties.selectDictLabels = selectDictLabels
// 全局组件挂载
app.component('Pagination', Pagination)
app.component('TreeSelect', TreeSelect)
app.component('FileUpload', FileUpload)
app.component('ImageUpload', ImageUpload)
app.component('ImagePreview', ImagePreview)
app.component('RightToolbar', RightToolbar)

app.use(router)
app.use(store)
app.use(plugins)
app.use(elementIcons)
app.component('SvgIcon', SvgIcon)

directive(app)

// 使用element-plus 并且设置全局的大小
app
  .use<any>(ElementPlus, {
    locale: zhCn,
    // 支持 large、default、small
    size: Cookies.get('size') || 'default'
  })
  .use<any>(ElTableNext)

app.mount('#app')
