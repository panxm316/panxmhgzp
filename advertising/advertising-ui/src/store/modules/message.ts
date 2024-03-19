/*
 * @Author: peij
 * @Date: 2023-10-23 16:29:44
 * @LastEditTime: 2024-01-08 13:54:32
 * @LastEditors: songly
 * @Description:系统消息的store
 */

import { defineStore } from 'pinia'
import {
  EMessageType,
  type TMessageDataType,
  type TMessageStoreType
} from '@/store/modules/type/index'
import { TMessageVo } from '@/types/views/system/message'
import { updateMessageStatusApi } from '@/api/system/message'
import { IAxios } from 'axios'
import { getMessageCountByEmpIdApi } from '@/api/system/message'

const useMessageStore = defineStore('message', {
  state: (): TMessageStoreType => ({
    messageData: {
      type: 0,
      date: new Date().getTime(),
      messageId: '',
      fromUser: '',
      toUser: '',
      messageTitle: '',
      messageContent: '',
      messageCount: 0,
      processInstanceId: 0,
      processInstanceCreate: ''
    },
    noticeCount: 0,
    taskCount: 0,
    params: {
      processInstanceId: 0,
      processInstanceCreate: '',
      processInstanceType: EMessageType.SYSTEM_MSG
    }
  }),
  actions: {
    // 更新系统消息
    setMessageData(value: TMessageDataType) {
      this.messageData = value
    },
    // 刷新系统消息提醒
    getMessageCount(userId: string) {
      getMessageCountByEmpIdApi(userId).then(({ obj }: IAxios<number>) => {
        this.messageData.messageCount = obj
      })
    },
    // 更新跳转参数
    setParams(value: {
      processInstanceId: number
      processInstanceCreate: string
      processInstanceType: EMessageType
    }) {
      this.params = value
    },
    updateMessageStatus(
      messageVo: TMessageVo,
      value: {
        processInstanceId: number
        processInstanceCreate: string
        processInstanceType: EMessageType
      }
    ) {
      updateMessageStatusApi(messageVo).then(({ success }: IAxios) => {
        if (success) {
          this.setParams(value)
        }
      })
    }
  }
})
export default useMessageStore
