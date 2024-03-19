package com.hgzp.advertisingsys.service.flow.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertisingsys.service.flow.TbflowServiceI;
import com.hgzp.common.flowable.vo.FormItemVO;
import com.hgzp.common.flowable.vo.ProcessVO;
import com.hgzp.advertisingsys.service.flow.IProcessService;
import com.hgzp.advertisingsys.service.flow.IProcessStarterService;
import com.hgzp.advertisingsys.utils.CoreHttpUtil;
import com.hgzp.common.flowable.constants.FormTypeEnum;
import com.hgzp.common.flowable.constants.ProcessInstanceConstant;
import com.hgzp.common.flowable.dto.R;
import com.hgzp.common.flowable.dto.flow.Node;
import com.hgzp.common.flowable.dto.flow.NodeUser;
import com.hgzp.common.flowable.utils.CommonUtil;
import com.hgzp.common.flowable.utils.NodeUtil;
import com.hgzp.core.emnus.FlowTypes;
import com.hgzp.core.model.ProcessStarter;
import com.hgzp.core.model.Tbflow;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.flow.ProcessMapper;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.springframework.stereotype.Service;
import com.hgzp.core.model.Process;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements IProcessService {
    @Resource
    private IProcessStarterService processStarterService;
    @Resource
    private TbflowServiceI tbflowServiceI;

    /**
     * 获取详细数据
     *
     * @param flowId
     * @return
     */
    @Override
    public Json<ProcessVO> getDetail(String flowId) {
        ProcessVO processVO = this.getProcessVO(flowId);
        return Json.success(processVO);
    }

    private ProcessVO getProcessVO(String flowId) {
        Process oaForms = getByFlowId(flowId);
        String process = oaForms.getProcess();
        String formItems = oaForms.getFormItems();
        Node startNode = CommonUtil.toObj(process, Node.class);


        Map<String, String> formPerms = startNode.getFormPerms();
        List<FormItemVO> formItemVOList = JSON.parseArray(formItems, FormItemVO.class);

        for (FormItemVO formItemVO : formItemVOList) {
            String perm = MapUtil.getStr(formPerms, formItemVO.getId(), ProcessInstanceConstant.FormPermClass.EDIT);
            formItemVO.setPerm(perm);

            if (StrUtil.equals(formItemVO.getType(), FormTypeEnum.LAYOUT.getType())) {
                //明细
                Object value = formItemVO.getProps().getValue();
                List<FormItemVO> subList = Convert.toList(FormItemVO.class, value);
                for (FormItemVO itemVO : subList) {
                    String perm1 = MapUtil.getStr(formPerms, itemVO.getId(),
                            ProcessInstanceConstant.FormPermClass.EDIT);
                    itemVO.setPerm(perm1);
                }


                formItemVO.getProps().setValue(subList);
                formItemVO.getProps();

            }

        }
        oaForms.setFormItems(CommonUtil.toJson(formItemVOList));


        List<String> selectUserNodeId = NodeUtil.selectUserNodeId(startNode);

        ProcessVO processVO = BeanUtil.copyProperties(oaForms, ProcessVO.class);
        processVO.setSelectUserNodeId(selectUserNodeId);
        processVO.setGroupName(FlowTypes.getNameByKey(processVO.getGroupId()));
        return processVO;
    }


    @Override
    public Process getByFlowId(String flowId) {
        return this.lambdaQuery().eq(Process::getFlowId, flowId).one();
    }

    @Override
    public void updateByFlowId(Process process) {
        this.lambdaUpdate().eq(Process::getFlowId, process.getFlowId()).update(process);
    }

    @Override
    public void hide(String flowId) {
        this.lambdaUpdate().set(Process::getHidden, true).eq(Process::getFlowId, flowId).update(new Process());
    }

    /**
     * 创建流程
     *
     * @param processVO
     * @return
     */
    @Override
    public Json create(Process processVO) throws Exception {
        LoginUser loginUser = WebUtil.getLoginUser();

        String processStr = processVO.getProcess();


        Node node = JSON.parseObject(processStr, Node.class);
        com.hgzp.advertisingsys.utils.NodeUtil.handleStarterNode(node, JSON.parseArray(processVO.getFormItems(),
                FormItemVO.class));
        com.hgzp.advertisingsys.utils.NodeUtil.handleApproveForm(node, JSON.parseArray(processVO.getFormItems(),
                FormItemVO.class));
        com.hgzp.advertisingsys.utils.NodeUtil.handleApprove(node);


        R<String> r = CoreHttpUtil.createFlow(node, loginUser.getUserid() + "");
        if (!r.isSuccess()) {
            return Json.fail(r.getMsg());
        }
        String flowId = r.getObj();


        NodeUser nodeUser = CommonUtil.toArray(processVO.getAdmin(), NodeUser.class).get(0);

        if (StrUtil.isNotBlank(processVO.getFlowId())) {

            Process oldProcess = this.getByFlowId(processVO.getFlowId());
            this.hide(processVO.getFlowId());
            //修改所有的管理员
            this.lambdaUpdate().set(Process::getAdminId, nodeUser.getId()).eq(Process::getUniqueId,
                    oldProcess.getUniqueId()).update(new Process());

        }

        Node startNode = CommonUtil.toObj(processStr, Node.class);


        List<NodeUser> nodeUserList = startNode.getNodeUserList();

        StringBuilder stringBuilder = new StringBuilder("");
        if (CollUtil.isNotEmpty(nodeUserList)) {
            int index = 0;

            for (NodeUser user : nodeUserList) {
                if (index > 0) {
                    stringBuilder.append(",");
                }
                stringBuilder.append(user.getName());
                index++;
                if (index > 5) {
                    break;
                }

            }
        }

        Process p = new Process();
        p.setFlowId(flowId);
        p.setName(processVO.getName());
        p.setLogo(processVO.getLogo());
        p.setSettings(processVO.getSettings());
        p.setGroupId(processVO.getGroupId());
        p.setFormItems(processVO.getFormItems());
        p.setProcess(processStr);
        p.setRemark(processVO.getRemark());
        p.setSort(0);
        p.setHidden(false);
        p.setStop(false);
        p.setAdminId(nodeUser.getId());
        p.setUniqueId(IdUtil.fastSimpleUUID());
        p.setAdmin(processVO.getAdmin());
        p.setRangeShow(stringBuilder.toString());


        this.save(p);

        //保存范围

        for (NodeUser nodeUserDto : nodeUserList) {
            ProcessStarter processStarter = new ProcessStarter();

            processStarter.setProcessId(p.getId());
            processStarter.setTypeId((nodeUserDto.getId()));
            processStarter.setType(nodeUserDto.getType());
            processStarterService.save(processStarter);

        }


        return Json.success();
    }

    /**
     * 编辑表单
     *
     * @param flowId  摸板ID
     * @param type    类型 stop using delete
     * @param groupId
     * @return 操作结果
     */
    @Override
    public Json update(String flowId, String type, String groupId) {
        Process process = new Process();
        process.setFlowId(flowId);
        process.setStop("stop".equals(type));
        process.setHidden("delete".equals(type));
        process.setGroupId(groupId);


        this.updateByFlowId(process);

        return Json.success();
    }

    @Override
    public List<ProcessVO> listProcess(String groupId) {
        //过滤有效的流程
        List<Tbflow> tbflows = tbflowServiceI.getFlowTypeValid();
        List<String> flowKeys = CollUtil.getFieldValues(tbflows, "skey", String.class);
        List<Process> list = this.lambdaQuery()
                .eq(StrUtil.isNotBlank(groupId), Process::getGroupId, groupId)
                .eq(Process::getHidden, false)
                .in(Process::getGroupId, flowKeys)
                .orderByAsc(Process::getSort).orderByDesc(Process::getCreateTime).list();

        List<ProcessVO> processVOList = BeanUtil.copyToList(list, ProcessVO.class);


        processVOList.forEach(vo -> {
                    Tbflow bflow = tbflowServiceI.getFlowTypeByKey(vo.getGroupId());
                    if (ObjectUtil.isNotNull(bflow)) {
                        vo.setGroupName(bflow.getSname());
                    }
                }
        );

        return processVOList;
    }

}
