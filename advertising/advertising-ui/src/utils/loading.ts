/*
 * @Author: peij
 * @Date: 2023-02-07 15:09:08
 * @LastEditTime: 2023-02-07 16:05:33
 * @LastEditors: peij
 * @Description: 全局loading
 */
import { ElLoading } from 'element-plus'

let loading: { close: () => void }
function openLoading() {
  loading = ElLoading.service({
    // body: true,
    lock: true,
    // spinner: 'el-icon-loading',
    background: 'rgba(0, 0, 0, 0)'
    // customClass: 'osloading' // 重点，修改样式类名
  })
  // }
}
function closeLoading() {
  loading.close()
}
export { openLoading, closeLoading }
