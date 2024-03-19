/*
 * @Author: peij
 * @Date: 2023-08-07 10:25:03
 * @LastEditTime: 2023-09-18 10:59:48
 * @LastEditors: peij
 * @Description: user store
 */

import Cookies from 'js-cookie'
import { login, logout, getInfo, getSsologinToken } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'
import defAva from '@/assets/images/profile.jpg'
import { defineStore } from 'pinia'
import { IUserInfo, ILoginInfoVo, ILoginUser } from './type/index'
import { AxiosResponse, IAxios } from 'axios'
import defaultSettings from '@/settings'
import { getAssetsImage } from '@/utils/index'

const { casEnable } = defaultSettings
const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken(),
    user: {} as ILoginUser,
    name: '',
    avatar: '',
    roles: [] as string[],
    permissions: [] as string[]
  }),
  actions: {
    /**
     * 登录
     * @param userInfo
     * @returns
     */
    login(userInfo: IUserInfo) {
      const username = userInfo.username.trim()
      const password = userInfo.password
      const code = userInfo.code
      const uuid = userInfo.uuid

      return new Promise<IAxios<ILoginInfoVo>>((resolve, reject) => {
        login(userInfo)
          .then((res: IAxios<ILoginInfoVo>) => {
            if (res.success) {
              const data = res.obj
              setToken(data.loginuser.accessToken)
              this.token = data.loginuser.accessToken
              if (!casEnable) {
                // peij 20230413 口令过期强制修改
                Cookies.set(
                  'Advertisingsys-Passexpired',
                  data.loginuser.passexpired === true ? 'true' : 'false'
                )
              }
            }
            resolve(res)
          })
          .catch((error) => {
            reject(error)
          })
      })
    },
    /**
     * 获取用户信息
     * @returns
     */
    async getInfo() {
      if (casEnable && !localStorage.getItem('casFirstLogin')) {
        await getSsologinToken()
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
            }
          })
          .catch((error: any) => {
            console.log('获取token失败' + error)
          })
      }
      localStorage.removeItem('casFirstLogin')
      return new Promise((resolve, reject) => {
        getInfo()
          .then((res: IAxios<ILoginInfoVo>) => {
            if (res.success) {
              const loginInfoVo = res.obj

              const { loginuser: user } = loginInfoVo
              // user.phone = sm4Decrypt(user.phone)
              // prettier-ignore
              const avatar =
                user.simg === '' || user.simg == null ? getAssetsImage('userimg.jpg') : user.simg
              if (loginInfoVo.roles && loginInfoVo.roles.length > 0) {
                // 验证返回的roles是否是一个非空数组
                this.roles = loginInfoVo.roles
              } else {
                this.roles = ['ROLE_DEFAULT']
              }
              this.permissions = loginInfoVo.permissions
              this.name = user.username
              this.avatar = avatar
              this.user = user

              // globals.$slanguage = user.slanguage
              // globals.$sysVersion = appData.sysVersion
              // globals.$casResetPasswordUrl = appData.casResetPasswordUrl
              // globals.$ssohgportal = appData.ssohgportal
              Cookies.set('Advertisingsys-UserId', user.userid.toString())
            }

            resolve(res.obj)
          })
          .catch((error) => {
            reject(error)
          })
      })
    },
    /**
     * 退出系统
     * @returns
     */
    logOut() {
      return new Promise<void>((resolve, reject) => {
        if (casEnable) {
          this.token = ''
          this.roles = []
          this.permissions = []
          removeToken()
          location.href = import.meta.env.VITE_APP_BASE_API + '/main/logOut' // cas重定向
          // resolve()
        } else {
          logout()
            .then(() => {
              this.token = ''
              this.roles = []
              this.permissions = []
              removeToken()
              resolve()
            })
            .catch((error) => {
              reject(error)
            })
        }
      })
    },
    /**
     * 重新登录
     * @returns
     */
    reLogin() {
      return new Promise<void>((resolve, reject) => {
        this.token = ''
        this.roles = []
        this.permissions = []
        removeToken()
        resolve()
      })
    }
  }
})

export default useUserStore
