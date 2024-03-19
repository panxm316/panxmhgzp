// eslint-disable-next-line
/// <reference types="vite/client" />
import { _RouteRecordBase } from 'vue-router'
declare module 'vue-router' {
  interface _RouteRecordBase {
    hidden?: boolean | string | number
    permissions?: string[]
    roles?: string
  }
}
