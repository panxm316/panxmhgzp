import router from './router'
import { ElMessage } from 'element-plus'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken, setToken } from '@/utils/auth'
import { isHttp } from '@/utils/validate'
import { isRelogin } from '@/utils/request'
import useUserStore from '@/store/modules/user'
import useSettingsStore from '@/store/modules/settings'
import usePermissionStore from '@/store/modules/permission'
import defaultSettings from '@/settings'
import Cookies from 'js-cookie'
import { getSsologinToken } from '@/api/login'
import { ILoginInfoVo } from './store/modules/type'
import { IAxios } from 'axios'
import _ from 'lodash'

NProgress.configure({ showSpinner: false })
const { casEnable } = defaultSettings
const whiteList = ['/login', '/auth-redirect', '/bind']

router.beforeEach((to: any, from, next) => {
  NProgress.start()
  if (getToken()) {
    to.meta.title && useSettingsStore().setTitle(to.meta.title)
    if (to.query.ssoappid) {
      localStorage.setItem('ssoappid', to.query.ssoappid)
    }
    if (to.path === '/login' || to.path === '/caslogin') {
      next({ path: '/' })
      NProgress.done()
    } else {
      if (useUserStore().roles.length === 0) {
        if (
          casEnable &&
          !localStorage.getItem('casEnableRedirect') &&
          to.path.indexOf('/tencentCme') === -1 &&
          to.path !== '/printstory' &&
          to.path !== '/write135' &&
          to.path !== '/wxXiuMi' &&
          to.path !== '/liveVideo' &&
          to.path !== '/printView'
        ) {
          localStorage.setItem('casEnableRedirect', 'true')
          location.href = import.meta.env.VITE_APP_BASE_API + '/main/index' // 否则全部重定向到登录页
          return
          // NProgress.done()
        }
        localStorage.removeItem('casEnableRedirect')
        isRelogin.show = true
        // 判断当前用户是否已拉取完user_info信息
        // prettier-ignore
        useUserStore().getInfo().then(() => {
          isRelogin.show = false
          // prettier-ignore
          usePermissionStore().generateRoutes().then((accessRoutes: any) => {
            // roles权限生成可访问的路由表
            accessRoutes.forEach((route: any) => {
              if (!isHttp(route.path)) {
                // 动态添加可访问路由表
                router.addRoute(route)
              }
            })
            // 地址是index并且参数ssoappid有值统一门户页面跳转过来根据参数跳转路由
            const appid = to.query.ssoappid || localStorage.getItem('ssoappid')
            if (to.path === '/index' && appid) {
              localStorage.removeItem('ssoappid')
              next({ name: _.upperFirst(appid) })
            } else {
              // hack方法 确保addRoutes已完成
              next({ ...to, replace: true })
            }
          })
        })
          .catch(async err => {
            await useUserStore().logOut().then(() => {
              ElMessage.error(err)
              next({ path: '/' })
            })
          })
      } else {
        next()
      }
    }
  } else {
    // 没有token
    if (to.path === '/index' && to.query.ssoappid) {
      localStorage.setItem('ssoappid', to.query.ssoappid)
    }
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      if (to.path === '/caslogin') {
        getSsologinToken()
          .then((res: IAxios<ILoginInfoVo>) => {
            if (res.success) {
              const data = res.obj
              setToken(data.loginuser.accessToken)
              // Cookies.set('jsessionid', to.query?.JSESSIONID)
              // peij 20230413 口令过期强制修改
              Cookies.set(
                'Advertisingsys-Passexpired',
                data.loginuser.passexpired === true ? 'true' : 'false'
              )
              console.log('cas登录成功')
              // 如果是第一次没有token登录 获取用户时就不重新获取token
              localStorage.setItem('casFirstLogin', 'true')
              next({ path: '/' })
            }
          })
          .catch((error: any) => {
            console.log('获取token失败' + error)
            ElMessage({
              type: 'error',
              message: '获取token失败'
            })
          })
      } else {
        next()
      }
    } else {
      if (casEnable) {
        location.href = import.meta.env.VITE_APP_BASE_API + '/main/index' // 否则全部重定向到登录页
        NProgress.done()
      } else {
        next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
        NProgress.done()
      }
    }
  }
})

router.afterEach(() => {
  NProgress.done()
  // if ($('#loader-wrapper').length > 0) {
  //   $('#loader-wrapper').remove()
  // }
})
