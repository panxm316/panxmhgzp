/*
 * @Author: peij
 * @Date: 2023-08-07 10:25:03
 * @LastEditTime: 2023-08-30 10:16:51
 * @LastEditors: peij
 * @Description: 用户接口
 */

import request from '@/utils/request'
import qs from 'qs'
import { IUserInfo } from '@/store/modules/type/index'
import { sm2Encrypt } from '@/utils/smcrypto'

/**
 * 登录方法
 * @param userInfo
 * @returns
 */
export function login(userInfo: IUserInfo) {
  const params = qs.stringify({
    account: userInfo.username,
    // sm2公钥对密码进行加密
    password: sm2Encrypt(userInfo.password),
    code: userInfo.code,
    uuid: userInfo.uuid
  })
  return request({
    url: '/main/login',
    headers: {
      isToken: false
    },
    method: 'post',
    data: params
  })
}

/**
 * 获取用户详细信息
 * @returns
 */
export function getInfo() {
  return request({
    url: '/main/getInfo',
    method: 'get'
  })
}

/**
 * 退出方法
 * @returns
 */
export function logout() {
  return request({
    url: '/main/logOut',
    method: 'post'
  })
}

/**
 * 获取验证码
 * @returns
 */
export function getCodeImg() {
  return request({
    url: '/main/captcha-image',
    headers: {
      isToken: false
    },
    method: 'get',
    timeout: 20000
  })
}

/**
 *
 * @returns 获取统一登陆token
 */
export const getSsologinToken = async () => {
  return await request({
    url: '/main/ssologin',
    method: 'post',
    headers: { repeatSubmit: false }
  })
}

/**
 * 获取公钥
 * @returns
 */
export const getpublicKey = async () => {
  return await request({
    url: '/main/publicKey',
    method: 'post'
  })
}

// 获取路由
export const getRouters = () => {
  return request({
    url: '/main/getRouters',
    method: 'get'
  })
}
