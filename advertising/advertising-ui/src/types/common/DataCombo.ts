export interface IDataCombo {
  /**
   * id
   */
  id: string
  /**
   *名称
   */
  name: string
  /**
   * 是否默认
   */
  bdefault: boolean
  /**
   * 图片路径
   */
  picpath: string
  /**
   * 标题
   */
  title: string
  /**
   * 子集
   */
  list: Array<IDataCombo>
  /**
   * text
   */
  text: string
  /**
   * value
   */
  value: string
  /**
   * 媒体类型key
   */
  mediatypekey?: string
  /** 媒体类型名称 */
  mediatypename?: string
}
