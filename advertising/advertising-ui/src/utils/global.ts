import { getToken, removeToken } from '@/utils/auth'
import { ElLoading, ElMessage, ElMessageBox } from 'element-plus'
import $ from 'jquery'
/**
 **全局配置
 **/
;(function ($) {
  let isShowError = false
  $.ajaxSetup({
    type: 'POST',
    cache: true,
    beforeSend: function (request: any) {
      if (this.url.indexOf(import.meta.env.VITE_APP_BASE_API) >= 0) {
        request.setRequestHeader('Authorization', 'Bearer ' + getToken())
      }
    },
    error: function (XMLHttpRequest: any) {
      if (
        XMLHttpRequest.responseText &&
        XMLHttpRequest.responseText !== '' &&
        XMLHttpRequest.responseText.indexOf('login.js') === -1
      ) {
        if (XMLHttpRequest.status === 504) {
          // LY.showErrorMsg("请求超时请稍候再试!")
        } else if (XMLHttpRequest.status === 403) {
          if (isShowError) {
            return
          }
          var s = '登录超时或您账号已在别处登录,确定跳转到登录页面或刷新页面重新登录'
          ElMessageBox.alert(s, '提示', {
            confirmButtonText: '确定',
            callback: (action: any) => {
              removeToken()
              window.location.href = '/'
            }
          })
          isShowError = true
        } else {
          if (/无权限/.test(XMLHttpRequest.responseText)) {
            ElMessage({
              dangerouslyUseHTMLString: true,
              message: XMLHttpRequest.responseText,
              type: 'error'
            })
          } else {
            ElMessage({
              dangerouslyUseHTMLString: true,
              message: XMLHttpRequest.responseText,
              type: 'error'
            })
          }
        }
      }
    }
  })
  let loadingInstance: any
  function showAjaxLoading() {
    loadingInstance = ElLoading.service({
      target: '.app-container',
      background: 'rgba(0, 0, 0, 0)'
    })
  }

  function hideAjaxLoading() {
    loadingInstance.close()
  }

  $(document).ajaxStart(showAjaxLoading).ajaxStop(hideAjaxLoading)
})($)
