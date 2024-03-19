/**  WebSocket
 * @description:
 * @author: peij
 * @time: 2023-01-16 09:18:40
 */
import { useWebSocket, UseWebSocketReturn } from '@vueuse/core'
import useUserStore from '@/store/modules/user'
import useMessageStore from '@/store/modules/message'
import { TMessageDataType, EMessageType } from '@/store/modules/type/index'

type WebSocketMessageType = {
  // 消息发送者
  fromUser?: string
  // 消息接收者
  toUser?: string
  // 消息类型 0正常返回 -1心跳返回
  type: number
  // 消息内容
  content?: string
  // 发送日期
  date?: number
}

const wsProtocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:'
let webSocketReturn: UseWebSocketReturn<TMessageDataType>
let setMessageData: (value: TMessageDataType) => void
const initWebSocket = () => {
  const { user } = useUserStore()
  const messageStore = useMessageStore()
  setMessageData = messageStore.setMessageData
  const wsuri = `${wsProtocol}//${location.host}${import.meta.env.VITE_APP_BASE_API}/websocket/${
    user?.userid
  }`
  webSocketReturn = useWebSocket<TMessageDataType>(wsuri, {
    autoReconnect: {
      retries: 3,
      delay: 5000,
      onFailed() {
        console.log('Failed to connect WebSocket after 3 retries')
      }
    },
    heartbeat: {
      message: 'heartCheck',
      interval: 58 * 1000,
      pongTimeout: 1000
    },
    onMessage: onMessage
  })
}

const onMessage = (ws: WebSocket, { data: jsonStr }: MessageEvent<string>) => {
  console.log(jsonStr)
  const data = JSON.parse(jsonStr) as TMessageDataType
  // console.log(data)
  /** 0系统消息  */
  if (
    data.type === EMessageType.SYSTEM_MSG ||
    data.type === EMessageType.FLOW_ApprovePass_MSG ||
    data.type === EMessageType.FLOW_ApproveReject_MSG ||
    data.type === EMessageType.FLOW_TodoTask_MSG
  ) {
    setMessageData(data)
  }
}

export { webSocketReturn, initWebSocket }
