import { jQuery } from 'jquery'
// eslint-disable-next-line
/// <reference types="vite/client" />

declare module '*.vue' {
  import { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

declare module 'lodash-es'

declare module 'sm-crypto'

declare module 'jquery'

// 环境变量 TypeScript的智能提示
interface ImportMetaEnv {
  VITE_APP_TITLE: string
  VITE_APP_PORT: string
  VITE_APP_BASE_API: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}

declare global {
  interface Window {
    emitter: {
      emit(event: string, ...args: any[]): void
      on(event: string, handler: (eventObject: any, ...args: any[]) => any): void
      off(event: string, handler?: (eventObject: any, ...args: any[]) => any): void
      all: {
        clear(): void
      }
    }
  }
  interface JQuery {
    GetFilesAddress: (grid: []) => any[]
    destroyWebUpload: () => void
    refreshWebUpload: () => void
    pause: () => void
    radialIndicator: (options: any) => void
    powerWebUpload: (opts: any, types: any[]) => void
  }
}
