import axios from 'axios'

declare module 'axios' {
  interface IAxios<D = any> {
    code: number
    success: boolean
    msg: string
    total: number
    url?: string // 用于HgTuiimaGeeditor.ts
    obj: D
  }
  export interface AxiosResponse<T = any, D = any> extends IAxios<D> {}
}
