import { Tworderitembelong } from '@/types/views/ad/adtworder'
import { PreOrder } from '@/types/views/ad/pre-order'
import { Mode } from '@/types/views/ad/mode'

export const parseOrderItemToBelong = (
  preOrder: PreOrder,
  mode: Mode,
  belong?: Tworderitembelong
): Tworderitembelong => {
  return {
    archievementrate: mode === Mode.Edit ? belong?.archievementrate : undefined,
    bprimary: true,
    commissionrate: mode === Mode.Edit ? belong?.commissionrate : undefined,
    createempid: preOrder.createempid,
    createempname: preOrder.createempname,
    createtime: mode === Mode.Edit ? belong?.createtime : undefined,
    deptid: preOrder.deptid,
    deptname: preOrder.deptname,
    employid: preOrder.employid,
    employname: preOrder.employname,
    employtype: preOrder.employtype,
    id: mode === Mode.Edit ? belong?.id : undefined,
    orderitemid: mode === Mode.Edit ? belong?.orderitemid : undefined,
    scontractnum: mode === Mode.Edit ? belong?.scontractnum : undefined,
    sordernum: mode === Mode.Edit ? belong?.sordernum : undefined,
    sremark: mode === Mode.Edit ? belong?.sremark : '',
    taskrate: mode === Mode.Edit ? belong?.taskrate : undefined
  }
}
