package com.hgzp.advertising.service.flow;

import com.hgzp.advertising.pagemodel.flow.dto.MytodoDTO;
import com.hgzp.common.flowable.dto.PageResultDto;
import com.hgzp.common.flowable.dto.ProcessInstancePageDto;
import com.hgzp.common.flowable.dto.TaskDto;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;

/**
 * @author songly
 * @date 2024/3/6 10:00
 * @description 我的待办 移动端用
 */
public interface MyToDoServiceI  {
    Json<PageResultDto<TaskDto>> queryMyToDo(MytodoDTO mytodoDTO) throws Exception;
}
