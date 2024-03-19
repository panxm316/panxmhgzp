/*
 * @Author: wanghl
 * @Date: 2023-08-08 09:39:16
 * @LastEditTime: 2024-02-22 15:44:24
 * @LastEditors: wanghl
 * @Description:
 */
import { defineStore } from 'pinia'
import Cookies from 'js-cookie'
import { IWithoutAnimation, TSize } from './type'
const useAppStore = defineStore('app', {
  state: () => ({
    sidebar: {
      opened: Cookies.get('sidebarStatus')
        ? !!+Cookies.get('sidebarStatus')!
        : window.screen.width > 1444,
      withoutAnimation: false,
      hide: false
    },
    device: 'desktop',
    size: Cookies.get('size') || 'default',
    showMenu: true // 是否折叠展开常用菜单
  }),
  actions: {
    toggleSideBar(withoutAnimation?: any) {
      if (this.sidebar.hide) {
        return false
      }
      this.sidebar.opened = !this.sidebar.opened
      this.sidebar.withoutAnimation = withoutAnimation
      if (this.sidebar.opened) {
        Cookies.set('sidebarStatus', '1')
      } else {
        Cookies.set('sidebarStatus', '0')
      }
    },
    closeSideBar({ withoutAnimation }: IWithoutAnimation) {
      Cookies.set('sidebarStatus', '0')
      this.sidebar.opened = false
      this.sidebar.withoutAnimation = withoutAnimation
    },
    toggleDevice(device: string) {
      this.device = device
    },
    setSize(size: TSize) {
      this.size = size
      Cookies.set('size', size)
    },
    toggleSideBarHide(status: boolean) {
      this.sidebar.hide = status
    },
    /**控制菜单方法 */
    toggleShowMenu(status: boolean) {
      this.showMenu = status
    }
  }
})

export default useAppStore
