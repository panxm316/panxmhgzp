<template>
  <div class="add-node-btn-box">
    <div class="add-node-btn">
      <el-popover v-model="visible" placement="right-start" width="250px">
        <div class="add-node-popover-body">
          <a class="add-node-popover-item approver" @click="addType(1)">
            <div class="item-wrapper">
              <span class="iconfont"></span>
            </div>
            <p>审批人</p>
          </a>
          <!-- <a class="add-node-popover-item notifier" @click="addType(2)">
            <div class="item-wrapper">
              <span class="iconfont"></span>
            </div>
            <p>抄送人</p>
          </a> -->

          <a class="add-node-popover-item condition" @click="addType(4)">
            <div class="item-wrapper">
              <span class="iconfont"></span>
            </div>
            <p>条件分支</p>
          </a>

          <a class="add-node-popover-item ParallelGateway" @click="addType(5)">
            <div class="item-wrapper">
              <span class="iconfont"></span>
            </div>
            <p>并行分支</p>
          </a>
        </div>
        <template #reference>
          <button class="btn" type="button">
            <span class="iconfont"></span>
          </button>
        </template>
      </el-popover>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { getRandomId } from '@/utils/index'

const props = defineProps({
  childNodeP: {
    type: Object,
    default: () => ({})
  },
  currentNode: {
    type: Object,
    default: () => ({})
  }
})

const emits = defineEmits(['update:childNodeP'])
const visible = ref(false)
const addType = (type) => {
  visible.value = false
  if (type !== 4 && type !== 5) {
    var data
    if (type === 1) {
      data = {
        id: getRandomId(),
        nodeName: '审批人',
        error: true,
        type: 1,
        parentId: props.currentNode.id,
        // 人员方式
        assignedType: 1,
        // 单选还是多选
        multiple: false,
        deptUserType: 'allUser',
        // 多人审批的选项
        multipleMode: 2,
        // 第几级部门负责人
        deptLeaderLevel: 1,
        // 人员选择
        formUserId: '',
        formUserName: '',
        // 表单权限
        formPerms: {},
        // 审批人为空
        nobody: {
          handler: 'TO_REFUSE',
          assignedUser: []
        },
        // 审批人与发起人一致
        sameAsStarter: {
          handler: 'TO_CONTINUE'
        },
        // 审批人拒绝
        refuse: {
          handler: 'TO_END',
          nodeId: ''
        },
        childNode: props.childNodeP,
        nodeUserList: []
      }
    } else if (type === 2) {
      data = {
        id: getRandomId(),
        parentId: props.currentNode.id,

        nodeName: '抄送人',
        type: 2,
        error: true,
        childNode: props.childNodeP,
        nodeUserList: [],
        // 表单权限
        formPerms: {}
      }
    }
    emits('update:childNodeP', data)
  } else if (type === 4) {
    const id = getRandomId()
    emits('update:childNodeP', {
      nodeName: '条件分支',
      type: 4,
      id: id,
      parentId: props.currentNode.id,

      childNode: props.childNodeP,
      conditionNodes: [
        {
          id: getRandomId(),
          nodeName: '条件1',
          groupMode: true,
          error: true,
          type: 3,
          parentId: id,

          priorityLevel: 1,
          conditionList: [
            {
              mode: true,
              conditionList: [
                {
                  key: '',
                  expression: ''
                }
              ]
            }
          ],
          nodeUserList: [],
          childNode: null
        },
        {
          nodeName: '默认条件',
          type: 3,
          id: getRandomId(),
          groupMode: true,
          error: false,
          parentId: id,
          priorityLevel: 2,
          conditionList: [
            {
              mode: true,
              conditionList: [
                {
                  key: '',
                  expression: ''
                }
              ]
            }
          ],
          nodeUserList: [],
          childNode: null
        }
      ]
    })
  } else if (type === 5) {
    const id = getRandomId()
    emits('update:childNodeP', {
      nodeName: '并行分支',
      type: 5,
      id: id,
      parentId: props.currentNode.id,

      childNode: props.childNodeP,
      conditionNodes: [
        {
          id: getRandomId(),
          nodeName: '分支1',
          placeHolder: '满足条件',
          parentId: id,

          error: false,
          type: 3,
          priorityLevel: 1,
          conditionList: [],
          nodeUserList: [],
          childNode: null
        },
        {
          nodeName: '分支2',
          type: 3,
          id: getRandomId(),
          parentId: id,

          error: false,
          placeHolder: '满足条件',

          priorityLevel: 2,
          conditionList: [],
          nodeUserList: [],
          childNode: null
        }
      ]
    })
  }
}
</script>
<style scoped lang="scss">
.add-node-btn-box {
  width: 240px;
  display: -webkit-inline-box;
  display: -ms-inline-flexbox;
  display: inline-flex;
  -ms-flex-negative: 0;
  flex-shrink: 0;
  -webkit-box-flex: 1;
  -ms-flex-positive: 1;
  position: relative;

  &:before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: -1;
    margin: auto;
    width: 2px;
    height: 100%;
    background-color: #cacaca;
  }

  .add-node-btn {
    user-select: none;
    width: 240px;
    padding: 20px 0 32px;
    display: flex;
    -webkit-box-pack: center;
    justify-content: center;
    flex-shrink: 0;
    -webkit-box-flex: 1;
    flex-grow: 1;

    .btn {
      outline: none;
      box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.1);
      width: 30px;
      height: 30px;
      background: #3296fa;
      border-radius: 50%;
      position: relative;
      border: none;
      line-height: 30px;
      -webkit-transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
      transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);

      .iconfont {
        color: #fff;
        font-size: 16px;
      }

      &:hover {
        transform: scale(1.3);
        box-shadow: 0 13px 27px 0 rgba(0, 0, 0, 0.1);
      }

      &:active {
        transform: none;
        background: #1e83e9;
        box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.1);
      }
    }
  }
}
</style>
<style lang="scss" scoped>
@import '@/views/flow/workflow/css/workflow.css';

.add-node-popover-body {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;

  .add-node-popover-item {
    margin-right: 10px;
    cursor: pointer;
    text-align: center;
    flex: 1;
    //color: #191f25!important;
    .item-wrapper {
      user-select: none;
      display: inline-block;
      width: 80px;
      height: 80px;
      margin-bottom: 5px;
      background: #fff;
      border: 1px solid #e2e2e2;
      border-radius: 50%;
      transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);

      .iconfont {
        font-size: 35px;
        line-height: 80px;
      }
    }

    &.approver {
      .item-wrapper {
        color: #ff943e;
      }
    }

    &.notifier {
      .item-wrapper {
        //color: #3296fa
      }
    }

    &.condition {
      .item-wrapper {
        color: #15bc83;
      }
    }

    &.ParallelGateway {
      .item-wrapper {
        color: rgb(255, 69, 0);
      }
    }

    &:hover {
      .item-wrapper {
        background: #3296fa;
        box-shadow: 0 10px 20px 0 rgba(50, 150, 250, 0.4);
      }

      .iconfont {
        color: #fff;
      }
    }

    &:active {
      .item-wrapper {
        box-shadow: none;
        background: #eaeaea;
      }

      .iconfont {
        //color: inherit
      }
    }
  }
}
</style>
