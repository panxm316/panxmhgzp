import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'

import { UserConfig, ConfigEnv, loadEnv, defineConfig } from 'vite'
import inject from '@rollup/plugin-inject'

import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

import Icons from 'unplugin-icons/vite'
import IconsResolver from 'unplugin-icons/resolver'

import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'
import { createHtmlPlugin } from 'vite-plugin-html'

import UnoCSS from 'unocss/vite'
import path from 'path'
import dayjs from 'dayjs'

import viteCompression from 'vite-plugin-compression'

const pathSrc = path.resolve(__dirname, 'src')
const pathPublic = path.resolve(__dirname, 'public')

const getViteEnv = (mode, target) => {
  return loadEnv(mode, process.cwd())[target]
}

export default defineConfig(({ mode }: ConfigEnv): UserConfig => {
  const env = loadEnv(mode, process.cwd())
  return {
    base: env.VITE_APP_ENV === 'production' ? '/adcloud' : '/',
    resolve: {
      alias: {
        '@': pathSrc,
        '@public': pathPublic
      }
    },
    // publicDir: 'public',
    css: {
      // CSS 预处理器
      preprocessorOptions: {
        // define global scss variable
        scss: {
          javascriptEnabled: true,
          additionalData: `
            @use "@/assets/styles/variables.scss" as *;
          `
        }
      }
    },
    server: {
      host: '0.0.0.0',
      port: Number(env.VITE_APP_PORT),
      open: true, // 运行是否自动打开浏览器
      proxy: {
        // 反向代理解决跨域
        [env.VITE_APP_BASE_API]: {
          // target: 'http://vapi.youlai.tech', // 线上接口地址
          target: 'http://localhost:8111/advertising', // 本地接口地址 , 后端工程仓库地址：https://gitee.com/youlaiorg/youlai-boot
          ws: true,
          changeOrigin: true,
          rewrite: (path) => path.replace(new RegExp('^' + env.VITE_APP_BASE_API), '') // 替换 /dev-api 为 target 接口地址
        }
      }
    },
    plugins: [
      vue(),
      vueJsx(),
      UnoCSS({}),
      AutoImport({
        // 自动导入 Vue 相关函数，如：ref, reactive, toRef 等
        imports: ['vue', 'vue-router', 'pinia', '@vueuse/core'],
        eslintrc: {
          enabled: false,
          filepath: './.eslintrc-auto-import.json',
          globalsPropValue: true
        },
        resolvers: [
          // 自动导入 Element Plus 相关函数，如：ElMessage, ElMessageBox... (带样式)
          ElementPlusResolver(),
          IconsResolver({})
        ],
        vueTemplate: true,
        // 配置文件生成位置(false:关闭自动生成)
        dts: false
        // dts: 'src/types/auto-imports.d.ts'
      }),

      Components({
        resolvers: [
          // 自动导入 Element Plus 组件
          // ElementPlusResolver(),
          // 自动导入图标组件
          IconsResolver({
            // @iconify-json/ep 是 Element Plus 的图标库
            enabledCollections: ['ep']
          })
        ],
        // 指定自定义组件位置(默认:src/components)
        dirs: ['src/components'],
        // 搜索子目录
        deep: true,
        // 配置文件位置(false:关闭自动生成)
        dts: false
        // dts: 'src/types/components.d.ts'
      }),

      Icons({
        // 自动安装图标库
        autoInstall: true,
        scale: 1
      }),

      createSvgIconsPlugin({
        // 指定需要缓存的图标文件夹
        iconDirs: [path.resolve(pathSrc, 'assets/icons/svg')],
        // 指定symbolId格式
        symbolId: 'icon-[dir]-[name]'
      }),
      // 代码压缩
      viteCompression({
        verbose: true, // 默认即可
        disable: false, // 是否禁用压缩，默认禁用，true为禁用,false为开启，打开压缩需配置nginx支持
        deleteOriginFile: false, // 删除源文件
        threshold: 10240, // 压缩前最小文件大小
        algorithm: 'gzip', // 压缩算法
        ext: '.gz' // 文件类型
      }),
      inject({
        // $: 'jquery', // 这里会自动载入 node_modules 中的 jquery jquery全局变量
        jQuery: 'jquery',
        'windows.jQuery': 'jquery'
      }),
      createHtmlPlugin({
        inject: {
          data: {
            title: getViteEnv(mode, 'VITE_APP_TITLE'),
            buildDate: dayjs(new Date()).format('YYYY-MM-DD HH:mm:ss')
          }
        },
        minify: {
          collapseWhitespace: true,
          keepClosingSlash: true,
          removeComments: false,
          removeRedundantAttributes: true,
          removeScriptTypeAttributes: true,
          removeStyleLinkTypeAttributes: true,
          useShortDoctype: true,
          minifyCSS: true
        }
      })
    ],
    // 预加载项目必需的组件
    optimizeDeps: {
      include: [
        'vue',
        'vue-router',
        'pinia',
        'axios',
        'jquery',
        'element-plus/es/components/form/style/css',
        'element-plus/es/components/form-item/style/css',
        'element-plus/es/components/button/style/css',
        'element-plus/es/components/input/style/css',
        'element-plus/es/components/input-number/style/css',
        'element-plus/es/components/switch/style/css',
        'element-plus/es/components/upload/style/css',
        'element-plus/es/components/menu/style/css',
        'element-plus/es/components/col/style/css',
        'element-plus/es/components/icon/style/css',
        'element-plus/es/components/row/style/css',
        'element-plus/es/components/tag/style/css',
        'element-plus/es/components/dialog/style/css',
        'element-plus/es/components/loading/style/css',
        'element-plus/es/components/radio/style/css',
        'element-plus/es/components/radio-group/style/css',
        'element-plus/es/components/popover/style/css',
        'element-plus/es/components/scrollbar/style/css',
        'element-plus/es/components/tooltip/style/css',
        'element-plus/es/components/dropdown/style/css',
        'element-plus/es/components/dropdown-menu/style/css',
        'element-plus/es/components/dropdown-item/style/css',
        'element-plus/es/components/sub-menu/style/css',
        'element-plus/es/components/menu-item/style/css',
        'element-plus/es/components/divider/style/css',
        'element-plus/es/components/card/style/css',
        'element-plus/es/components/link/style/css',
        'element-plus/es/components/breadcrumb/style/css',
        'element-plus/es/components/breadcrumb-item/style/css',
        'element-plus/es/components/table/style/css',
        'element-plus/es/components/tree-select/style/css',
        'element-plus/es/components/table-column/style/css',
        'element-plus/es/components/select/style/css',
        'element-plus/es/components/option/style/css',
        'element-plus/es/components/pagination/style/css',
        'element-plus/es/components/tree/style/css',
        'element-plus/es/components/alert/style/css',
        '@vueuse/core',
        'path-to-regexp',
        'echarts',
        'vue-i18n'
      ]
    },
    build: {
      sourcemap: false, // 构建后是否生成 source map 文件
      chunkSizeWarningLimit: 1500,
      rollupOptions: {
        output: {
          manualChunks(id) {
            if (id.includes('node_modules')) {
              return id.toString().split('node_modules/')[1].split('/')[0].toString()
            }
            // 把 src 文件里面的文件都打包到 app.js 中
            // if (id.includes('src')) {
            //   return 'app'
            // }
          },
          chunkFileNames: 'assets/js/[name]-[hash].js',
          entryFileNames: 'assets/js/[name]-[hash].js',
          assetFileNames: 'assets/static/[name]-[hash].[ext]'
        }
      }
    },
    esbuild: {
      // drop: ['console', 'debugger']
    }
  }
})
