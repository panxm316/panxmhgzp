<template>
  <div class="app-container">
    <div class="search_box">
      <el-select
        v-model="fieldSelect"
        multiple
        placeholder="筛选字段"
        clearable
        @change="handleFiled"
      >
        <el-option
          v-for="item in baseTask.SynField"
          :key="item.id"
          :label="item.id"
          :value="item.value"
        >
        </el-option>
      </el-select>
      <el-button type="primary" @click="addField">添加到查询结果</el-button>
      <el-select v-model="queryrange" placeholder="结果范围">
        <el-option
          v-for="item in baseTask.SynRange"
          :key="item.id"
          :label="item.value"
          :value="item.id"
        >
        </el-option>
      </el-select>
      <el-select
        v-model="sortFiled"
        placeholder="排序字段"
        clearable
        @change="handleSortFiled"
      >
        <el-option
          v-for="item in baseTask.SynField"
          :key="item.id"
          :label="item.id"
          :value="item.value"
        >
        </el-option>
      </el-select>
      <el-checkbox v-model="sorttype" style="width: 80px">降序排序</el-checkbox>
      <el-checkbox v-model="filterdata" style="width: 160px">过滤重复数据</el-checkbox>
      <el-button type="primary" @click="onQuery">查询</el-button>
      <el-button v-if="showTable === 1" type="primary" @click="onShowFieldTable()">显示字段表格</el-button>
    </div>
    <!--
      字段列表
    -->
    <div v-if="showTable === 0" class="table_box">
      <el-table :data="fieldList" highlight-current-row  border>
        <el-table-column label="字段名称" prop="fieldname" width="200" align="center" />
        <el-table-column label="条件" prop="condition" min-width="350" align="center" />
        <el-table-column
          label="操作"
          align="center"
          width="300"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              link
              style="margin-left: 10px"
              type="success"
              icon="Edit"
              size="small"
              title="设置条件"
              @click="onCondition(scope.row)"
              >设置条件</el-button
            >
            <el-button
              v-if="scope.row.condition != ''"
              link
              style="margin-left: 10px"
              type="success"
              icon="Edit"
              size="small"
              title="清空条件"
              @click="clearCondition(scope.row)"
              >清空条件</el-button
            >
            <el-button
              link
              style="margin-left: 10px"
              type="danger"
              icon="Edit"
              size="small"
              title="删除"
              @click="onDelete(scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <!--
      设置条件对话框
    -->
      <el-dialog
        v-model="dialogCondition"
        title="设置条件"
        :width="500"
        append-to-body
        :destroy-on-close="true"
      >
        <div>
          <el-row>
            <el-col :span="24">
              <el-form label-width="120px" class="demo-workReportForm" status-icon>
                <el-form-item label="字段" prop="field">
                  <el-input v-model="conditiondata.field" readonly style="width: 200px" />
                </el-form-item>
                <el-form-item label="条件">
                  <el-select
                    v-model="conditiondata.op"
                    placeholder="选择条件"
                    clearable
                    @change="handleFiled"
                  >
                    <el-option
                      v-for="item in baseTask.SynCondition"
                      :key="item.id"
                      :label="item.id"
                      :value="item.id"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item v-if="conditiondata.type === 0" label="值">
                  <el-input v-model="conditiondata.value1" style="width: 200px" />
                </el-form-item>
                <el-form-item v-if="conditiondata.type === 0 && showOtherValue===1" label="值">
                  <el-input v-model="conditiondata.value2" style="width: 200px" />
                </el-form-item>
                <el-form-item v-if="conditiondata.type === 1" label="值">
                  <el-input-number
                    v-model="conditiondata.value1"
                    :controls="false"
                    :precision="2"
                    :min="0.0001"
                    :max="1000000"
                    style="width: 200px"
                  /><span style="margin-left: 20px">元</span>
                </el-form-item>
                <el-form-item v-if="conditiondata.type === 1 && showOtherValue===1" label="值">
                  <el-input-number
                    v-model="conditiondata.value2"
                    :controls="false"
                    :precision="2"
                    :min="0.0001"
                    :max="1000000"
                    style="width: 200px"
                  /><span style="margin-left: 20px">元</span>
                </el-form-item>

                <el-form-item v-if="conditiondata.type === 2" label="值">
                  <el-date-picker
                    v-model="startTime"
                    :editable="false"
                    type="date"
                    placeholder="选择日期"
                    :clearable="false"
                    style="width: 130px; margin-right: 5px"
                    @change="onresponse"
                  >
                  </el-date-picker>
                </el-form-item>
                <el-form-item v-if="conditiondata.type === 2 && showOtherValue===1" label="值">
                  <el-date-picker
                    v-model="endTime"
                    :editable="false"
                    type="date"
                    placeholder="选择日期"
                    :clearable="false"
                    style="width: 130px; margin-right: 5px"
                    @change="onresponseother"
                  >
                  </el-date-picker>
                </el-form-item>
                <el-form-item>
                  <div style="text-align: right; margin-top: 60px; width: 100%">
                    <el-button type="primary" icon="Check" @click="saveCondition()">保存</el-button>
                    <el-button icon="Close" @click="dialogCondition = false">取消</el-button>
                  </div>
                </el-form-item>
              </el-form>
            </el-col>
          </el-row>
        </div>
      </el-dialog>
    </div>
    <!--
      查询结果
    -->
    <div v-if="showTable === 1" class="table_box">
      <el-table :data="dataList" highlight-current-row  :height="tableheight"  border>
        <el-table-column v-if="isShow('scontractnum')" label="合同号" prop="scontractnum"  align="center" />
        <el-table-column v-if="isShow('customername')" label="客户名称" prop="customername"  align="center" />
        <el-table-column v-if="isShow('createtime')" label="预定日期" prop="createtime"  align="center">
          <template #default="scope">
            <span>{{ dayjs(scope.row.createtime).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isShow('prestartdate')" label="见报日期" prop="prestartdate"  align="center">
          <template #default="scope">
            <span>{{ dayjs(scope.row.prestartdate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isShow('amountreceivable')" label="应收金额" prop="amountreceivable" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isShow('amountarrearage')" label="欠款金额" prop="amountarrearage" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isShow('amountreceived')" label="已付金额" prop="amountreceived" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceived, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isShow('namountapportion')" label="分摊金额" prop="namountapportion" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namountapportion, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isShow('dapportiondate')" label="分摊日期" prop="dapportiondate"  align="center">
          <template #default="scope">
            <span>{{ dayjs(scope.row.dapportiondate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isShow('namount')" label="发票金额" prop="namount" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namount, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isShow('invoice')" label="发票号" prop="invoice"  align="center" />
        <el-table-column v-if="isShow('dcreatetime')" label="开票日期" prop="dcreatetime"  align="center">
          <template #default="scope">
            <span>{{ dayjs(scope.row.dcreatetime).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isShow('deptname')" label="部门" prop="deptname"  align="center" />
        <el-table-column v-if="isShow('employname')" label="业务员" prop="employname"  align="center" />
        <el-table-column v-if="isShow('sadtitle')" label="广告标题" prop="sadtitle"  align="center" />
        <el-table-column v-if="isShow('agencyname')" label="代理名称" prop="agencyname"  align="center" />
        <el-table-column v-if="isShow('adtypename')" label="广告类型" prop="adtypename"  align="center" />
        <el-table-column v-if="isShow('adindustryname')" label="广告行业" prop="adindustryname"  align="center" />
        <el-table-column v-if="isShow('sremark')" label="订单备注" prop="sremark"  align="center" />
      </el-table>
      <el-pagination
        v-model:current-page="queryvo.pageNum"
        class="pagination"
        style="margin-left: 10px; width: 100%"
        :page-sizes="pageSizes"
        :page-size="queryvo.pageSize"
        small
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

  </div>
</template>
<script setup lang="ts">
import { IAxios } from 'axios'
import { computTableHeight,formatMoney } from '@/utils'
import baseTask, { ISynQueryItem } from '@/types/views/statistic/reportforms'
import dayjs from 'dayjs'
import {
  synQueryApi
} from '@/api/statistic/financereport'
onMounted(() => {
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})
/** 字段选择列表 */
const fieldSelect = ref<any>([])
/** 降序排序 */
const sorttype = ref(false)
/** 过滤重复数据 */
const filterdata = ref(false)
/** 表格显示控制  0：显示筛选字段表  1：显示查询结果表*/
const showTable = ref(0)
/** 显示第二个值*/
const showOtherValue = ref(0)
/** 结果范围 */
const queryrange = ref('0')
/** 排序字段 */
const sortFiled = ref('')
/** 字段列表 */
const fieldList = ref<ISynQueryItem[]>([])
/** 日期 */
const startTime = ref<Date | string>('')
/** 日期 */
const endTime = ref<Date | string>('')
/** 表格高度 */
const tableheight = ref(0)
/** 设置条件窗口 */
const dialogCondition = ref(false)
/** 列表 */
const dataList = ref<any>([])
/** 请求对象 */
const queryvo = ref({
  filterRepeat: 0,
  range: 0,
  sortName: '',
  enSortName: '',
  synQueryItemList: <any>[],
  pageNum: 0,
  pageSize: 20,
  sortType:'0'
})
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 查询记录总条数 */
const total = ref(0)
/**
 * 添加到查询结果
 */
const addField = () => {
  for (var i = 0; i < fieldSelect.value.length; i++) {
    for (var j = 0; j < baseTask.SynField.length; j++) {
      if (fieldSelect.value[i] === baseTask.SynField[j].value) {
        const item: ISynQueryItem = {
              fieldname: baseTask.SynField[j].id,
              enName: baseTask.SynField[j].value,
              conditionOp: '',
              enConditionOp: '',
              value: '',
              condition: '',
              othervalue: '',
              enPreName: ''
              }
        if( !isFiledSelectHave(item.enName) ) {
          if( item.enName === 'scontractnum' ) {
            item.enPreName = 'ro.scontractnum'
          } else if( item.enName === 'createtime' ) {
            item.enPreName = 'ro.createtime'
          } else if( item.enName === 'prestartdate' ) {
            item.enPreName = 'ro.prestartdate'
          } else if( item.enName === 'sadtitle' ) {
            item.enPreName = 'ro.sadtitle'
          } else if( item.enName === 'agencyname' ) {
            item.enPreName = 'so.agencyname'
          }  else if( item.enName === 'customername' ) {
            item.enPreName = 'so.customername'
          }  else if( item.enName === 'adtypename' ) {
            item.enPreName = 'so.adtypename'
          } else if( item.enName === 'adindustryname' ) {
            item.enPreName = 'so.adindustryname'
          } else if( item.enName === 'amountreceivable' ) {
            item.enPreName = 'ro.amountreceivable'
          }  else if( item.enName === 'amountarrearage' ) {
            item.enPreName = 'ro.amountarrearage'
          } else if( item.enName === 'amountreceived' ) {
            item.enPreName = 'ro.amountreceived'
          } else if( item.enName === 'deptname' ) {
            item.enPreName = 'so.deptname'
          } else if( item.enName === 'employname' ) {
            item.enPreName = 'so.employname'
          } else if( item.enName === 'invoice' ) {
            item.enPreName = 'vo.invoice'
          } else if( item.enName === 'namount' ) {
            item.enPreName = 'vo.namount'
          } else if( item.enName === 'dcreatetime' ) {
            item.enPreName = 'vo.dcreatetime'
          } else if( item.enName === 'dapportiondate' ) {
            item.enPreName = 'bo.dapportiondate'
          } else if( item.enName === 'namountapportion' ) {
            item.enPreName = 'bo.namountapportion'
          } else if( item.enName === 'sremark' ) {
            item.enPreName = 'so.sremark'
          }
          fieldList.value.push(item)
        }
      }
    }
  }
  showTable.value = 0
}
/** 设置条件数据 */
const conditiondata = ref<any>({
  field: '',
  op: '',
  value1: '',
  value2: '',
  type:0
})
/**
 * 事件
 */
const handleFiled = (event: any) => {
  if( event === '介于' ) {
     showOtherValue.value = 1
  } else {
    showOtherValue.value = 0
  }
}
const handleSortFiled = (event: any) => {
   if( event === 'createtime' || event === 'prestartdate' ) {
      queryvo.value.enSortName = "ro." + event
   } else {
      queryvo.value.enSortName = "so." + event
   }

}
/**
 * 删除按钮事件
 * @param row
 */
const onDelete = (row: ISynQueryItem) => {
  ElMessageBox.confirm('是否删除选择的字段？', '提示', {
    type: 'warning'
  })
    .then(() => {
      for (var i = 0; i < fieldList.value.length; i++) {
        if (row.enName === fieldList.value[i].enName) {
          fieldList.value.splice(i, 1)
        }
      }
    })
    .catch(() => {})
}
/**
 * 设置条件
 * @param row
 */
const onCondition = (row: ISynQueryItem) => {
  conditiondata.value.field = row.fieldname
  conditiondata.value.op = ''
  conditiondata.value.value1 = ''
  conditiondata.value.value1 = ''
  startTime.value = ''
  endTime.value = ''
  if( row.fieldname === '客户名称' || row.fieldname === '合同号' || row.fieldname === '广告标题' || row.fieldname === '代理名称' || row.fieldname === '广告类型' || row.fieldname === '广告行业' || row.fieldname === '部门' || row.fieldname === '业务员' || row.fieldname === '发票号' || row.fieldname === '订单备注') {
    conditiondata.value.type = 0
  } else if( row.fieldname === '应收金额' || row.fieldname === '欠款金额' || row.fieldname === '已付金额' || row.fieldname === '发票金额' || row.fieldname === '分摊金额') {
    conditiondata.value.type = 1
  } else if( row.fieldname === '预定日期' || row.fieldname === '见报日期' || row.fieldname === '开票日期' || row.fieldname === '分摊日期') {
    conditiondata.value.type = 2
  }
  dialogCondition.value = true
}
/**
 * 清空条件
 * @param row
 */
const clearCondition = (row: ISynQueryItem) => {
  for (var i = 0; i < fieldList.value.length; i++) {
    if (conditiondata.value.field === fieldList.value[i].fieldname) {
      fieldList.value[i].conditionOp = ""
      fieldList.value[i].value = ""
      fieldList.value[i].othervalue = ""
      fieldList.value[i].condition = ""
    }
  }
}
/**
 * 保存条件
 * @param formEl
 */
const saveCondition = () => {
  console.log(conditiondata.value.field)
  for (var i = 0; i < fieldList.value.length; i++) {
    if (conditiondata.value.field === fieldList.value[i].fieldname) {
      fieldList.value[i].conditionOp = conditiondata.value.op
      fieldList.value[i].value = conditiondata.value.value1
      fieldList.value[i].othervalue = conditiondata.value.value2
      if( conditiondata.value.op === '介于' ) {
        fieldList.value[i].condition =
        conditiondata.value.field + ' ' + conditiondata.value.op + ' ' + conditiondata.value.value1 + ' 与 ' + conditiondata.value.value2
      } else {
        fieldList.value[i].condition =
        conditiondata.value.field + ' ' + conditiondata.value.op + ' ' + conditiondata.value.value1

      }
      console.log(fieldList.value[i])
      dialogCondition.value = false
      break
    }
  }
}
/**
 * 日期回调
 */
const onresponse = () => {
  conditiondata.value.value1 = dayjs(startTime.value).format('YYYY-MM-DD')
}
/**
 * 日期回调
 */
 const onresponseother = () => {
  conditiondata.value.value2 = dayjs(endTime.value).format('YYYY-MM-DD')
}
/**
 * 查询
 */
const onQuery = () => {
  console.log(queryrange.value)
  if( filterdata.value ) {
    queryvo.value.filterRepeat = 1  
  } else {
    queryvo.value.filterRepeat = 0
  }
  if( sorttype.value ) {
    queryvo.value.sortType = '1'  
  } else {
    queryvo.value.sortType = '0'
  }
  queryvo.value.range = parseInt(queryrange.value)
  for (var i = 0; i < fieldList.value.length; i++) {
    queryvo.value.synQueryItemList.push(fieldList.value[i])
  }
  console.log(queryvo.value)
  synQueryApi(queryvo.value)
    .then((res: IAxios) => {
      if (res.success) {
        dataList.value = res.obj
        total.value = res.total
        showTable.value = 1
      }
    })
    .catch(() => {})

}
/**
 * 是否显示结果列
 */
const isShow = (name:string) => {
  for (var i = 0; i < fieldList.value.length; i++) {
    if( fieldList.value[i].enName == name ) {
      return true
    }
  }
  return false
}
/**
 * 判断字段是否在显示字段列表内
 */
const isFiledSelectHave = (name:string) => {
  for (var i = 0; i < fieldList.value.length; i++) {
    if( fieldList.value[i].enName == name ) {
      return true
    }
  }
  return false
}
/**
 * 显示字段表格
 */
const onShowFieldTable = () => {
  showTable.value = 0
}
/**
 * 改变页码总数调用
 */
const handleSizeChange = (val: number) => {
  queryvo.value.pageSize = val
  onQuery()
}
/**
 * 改变页数调用
 * @param val
 */
const handleCurrentChange = (val: number) => {
  queryvo.value.pageNum = val
  onQuery()
}
</script>
