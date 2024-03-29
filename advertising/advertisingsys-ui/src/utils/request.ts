import axios, { AxiosError, AxiosResponse, IAxios } from 'axios'
import { ElNotification, ElMessageBox, ElMessage, ElLoading } from 'element-plus'
import qs from 'qs'
import { getToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import { tansParams, blobValidate } from '@/utils/ruoyi'
import cache from '@/plugins/cache'
import { saveAs } from 'file-saver'
import useUserStore from '@/store/modules/user'
import { IRspObj, TErrorCode, TOtherCode } from './type'
import defaultSettings from '@/settings'
import { openLoading, closeLoading } from '@/utils/loading'

const { casEnable } = defaultSettings
let downloadLoadingInstance: { close: () => void }
// 是否显示重新登录
export const isRelogin = { show: false }

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
axios.defaults.withCredentials = false
// 创建axios实例
const service = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: import.meta.env.VITE_APP_BASE_API,
  // 超时
  timeout: 10000 * 20
})

// request拦截器
service.interceptors.request.use(
  (config: any) => {
    // 是否需要设置 token
    const isToken = (config.headers || {}).isToken === false
    // 是否需要防止数据重复提交
    let isRepeatSubmit = (config.headers || {}).repeatSubmit === false
    // peij 20230801 前缀为get不加防抖
    const requestMethod = config.url.substring(config.url.lastIndexOf('/') + 1) as string
    if (!isRepeatSubmit && requestMethod.substring(0, 3).toLocaleLowerCase() === 'get') {
      isRepeatSubmit = true
    }
    if (getToken() && !isToken) {
      if (config.headers) {
        config.headers['Authorization'] = 'Bearer ' + getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
      }
    }
    // get请求映射params参数
    if (config.method === 'get' && config.params) {
      let url = config.url + '?' + tansParams(config.params)
      url = url.slice(0, -1)
      config.params = {}
      config.url = url
    }
    if (!isRepeatSubmit && (config.method === 'post' || config.method === 'put')) {
      const requestObj = {
        url: config.url,
        data: typeof config.data === 'object' ? JSON.stringify(config.data) : config.data,
        time: new Date().getTime()
      }
      const sessionObj = cache.session.getJSON('sessionObj')
      if (sessionObj === undefined || sessionObj === null || sessionObj === '') {
        cache.session.setJSON('sessionObj', requestObj)
      } else {
        const s_url = sessionObj.url // 请求地址
        const s_data = sessionObj.data // 请求数据
        const s_time = sessionObj.time // 请求时间
        const interval = 1000 // 间隔时间(ms)，小于此时间视为重复提交
        if (
          s_data === requestObj.data &&
          requestObj.time - s_time < interval &&
          s_url === requestObj.url
        ) {
          const message = '数据正在处理，请勿重复提交'
          console.warn(`[${s_url}]: ` + message)
          return Promise.reject(new Error(message))
        } else {
          cache.session.setJSON('sessionObj', requestObj)
        }
      }
    }
    // peij 20221207 根据参数设置Content-Type类型
    if (typeof config.data === 'string') {
      config.headers['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8'
    } else {
      config.headers['Content-Type'] = 'application/json;charset=utf-8'
    }
    openLoading()
    return config
  },
  (error) => {
    console.log(error)
    Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (res: AxiosResponse) => {
    closeLoading()
    if ($('#loader-wrapper').length > 0) {
      $('#loader-wrapper').remove()
    }
    // 未设置状态码则默认成功状态
    const code: TErrorCode = res.status || 200
    const otherCode: TOtherCode = res.status
    // 获取错误信息
    // @ts-ignore
    const msg = errorCode[code] || res.data.msg || errorCode['default']
    // 二进制数据则直接返回
    if (res.request.responseType === 'arraybuffer') {
      return res.data
    }
    if (res.request.responseType === 'blob') {
      return res
    }
    if (code === 401 || code === 403) {
      if (!isRelogin.show) {
        isRelogin.show = true

        ElMessageBox.alert(
          res.data.code === 50009 ? res.data.msg : '登录状态已过期，请重新登录',
          '系统提示',
          {
            // if you want to disable its autofocus
            // autofocus: false,
            confirmButtonText: '重新登录',
            callback: (action: any) => {
              isRelogin.show = false
              useUserStore()
                .reLogin()
                .then(() => {
                  if (!casEnable) {
                    location.href = import.meta.env.BASE_URL + 'index'
                  } else {
                    location.href = import.meta.env.VITE_APP_BASE_API + '/main/logOut'
                  }
                })
            }
          }
        )
      }
      return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
    } else if (otherCode === 500) {
      ElMessage({ message: msg, type: 'error' })
      return Promise.reject(new Error(msg))
    } else if (otherCode === 601) {
      ElMessage({ message: msg, type: 'warning' })
      return Promise.reject(new Error(msg))
    } else if (otherCode !== 200) {
      ElNotification.error({ title: msg })
      return Promise.reject('error')
    } else {
      // peij 20221215 如果code为70000前端自己处理错误信息，success:false,code不为null弹出后台msg,code为null有后台msg弹出后台msg,无msg需要相关请求自己处理消息
      if (res.data !== '') {
        const { success, code, msg: errorMsg } = res.data
        if (code !== 70000 && ((!success && code !== null) || (!success && errorMsg !== ''))) {
          ElMessage({
            message: errorMsg,
            type: 'error'
          })
        }
      }
      return res.data
    }
  },
  (error: AxiosError<IAxios>) => {
    closeLoading()
    // if ($('#loader-wrapper').length > 0) {
    //   $('#loader-wrapper').remove()
    // }
    console.log('err' + error)
    const { response } = error
    let { message } = error
    if (message === 'Network Error') {
      message = '后端接口连接异常'
    } else if (message.includes('timeout')) {
      message = '系统接口请求超时'
    } else if (message.includes('Request failed with status code')) {
      if (response && response.data && response.data.msg) {
        message = response.data.msg
      } else {
        // message = '系统接口' + message.substr(message.length - 3) + '异常'
        // message = '您还没有登录或登录已超时，请重新登录，然后再刷新本功能！'
      }
    }
    if (response?.status && (response.status === 401 || response.status === 403)) {
      if (!isRelogin.show) {
        isRelogin.show = true
        // prettier-ignore
        ElMessageBox.alert(response.data.code === 50009 ? response.data.msg : '登录状态已过期，请重新登录', '系统提示', {
          // if you want to disable its autofocus
          // autofocus: false,
          confirmButtonText: '重新登录',
          callback: (action: any) => {
            isRelogin.show = false
            useUserStore().reLogin().then(() => {
              if (!casEnable) {
                location.href = import.meta.env.BASE_URL + 'index'
              } else {
                location.href = import.meta.env.VITE_APP_BASE_API + '/main/logOut'
              }
            })
          }
        })
      }
    } else {
      ElMessage({
        message: message,
        type: 'error',
        duration: 5 * 1000
      })
    }
    return Promise.reject(error)
  }
)

// 通用下载方法
export const download = async (url: string, params: any, filename?: string) => {
  // prettier-ignore
  const downloadLoadingInstance = ElLoading.service({
    text: '正在下载数据，请稍候',
    spinner: 'loading',
    background: 'rgba(0, 0, 0, 0.7)'
  })
  let req = null
  if (params) {
    req = service.post(url, qs.stringify(params), {
      // transformRequest: [
      //   (params) => {
      //     return tansParams(params)
      //   }
      // ],
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      responseType: 'blob'
    })
  } else {
    req = service.get(url, {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      responseType: 'blob'
    })
  }
  return await req
    .then(async (res: AxiosResponse) => {
      const resp = res.data
      const isLogin = await blobValidate(resp)
      if (isLogin) {
        const blob = new Blob([resp])
        // saveAs(blob, filename, {
        // 	type: "application/octet-stream;charset=utf-8"
        // });
        if (!filename) {
          filename = decodeURIComponent(res.headers.filename) // 后去后端来的文件名
        }
        saveAs(blob, filename)
        // var blob2 = new Blob([resp], {type: "text/plain;charset=utf-8"});
        // saveAs(blob, resp.msg + ".xlsx");
        console.log('%s ====>>>导出成功', filename)
      } else {
        const resText = resp.text()
        const rspObj = JSON.parse(resText)
        // @ts-ignore
        const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default']
        ElMessage.error(errMsg)
      }
      downloadLoadingInstance.close()
    })
    .catch((r) => {
      console.error(r)
      ElMessage.error('下载文件出现错误，请联系管理员！')
      downloadLoadingInstance.close()
    })
}

export default service
