import router from '@/router'

/**
 * 跳转到帮助文档页面
 * @param path 帮助文档路径
 */
export const showHelp = (path: String) => {
  const to = '/template/Help?name=' + path
  const routeData = router.resolve(to)
  window.open(routeData.href, '_blank')
}
