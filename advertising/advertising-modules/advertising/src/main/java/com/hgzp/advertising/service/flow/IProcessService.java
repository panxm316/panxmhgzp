package com.hgzp.advertising.service.flow;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.common.flowable.vo.ProcessVO;
import com.hgzp.core.model.Process;
import com.hgzp.core.page.Json;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-25
 */
public interface IProcessService extends IService<Process> {
    /**
     * 获取详细数据
     * @param flowId
     * @return
     */
    Json<ProcessVO> getDetail(String flowId);

    Process getByFlowId(String flowId);

    void updateByFlowId(Process process);

    void hide(String flowId);





    /**
     * 编辑表单
     *
     * @param flowId 摸板ID
     * @param type       类型 stop using delete
     * @return 操作结果
     */
    Json update(String flowId, String type, String groupId);

    List<ProcessVO> listProcess(String groupId);
}
