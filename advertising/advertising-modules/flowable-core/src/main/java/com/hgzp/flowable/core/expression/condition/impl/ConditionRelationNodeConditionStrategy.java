package com.hgzp.flowable.core.expression.condition.impl;

import cn.hutool.core.util.EscapeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.hgzp.common.flowable.constants.FormTypeEnum;
import com.hgzp.common.flowable.dto.flow.Condition;
import com.hgzp.flowable.core.expression.condition.NodeConditionStrategy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * ConditionRelationNodeConditionStrategy
 * 创建人：wangwk
 * 类描述：条件组对应的条件处理器
 * 创建日期：2023/10/20 13:09
 */
@Component
public class ConditionRelationNodeConditionStrategy implements NodeConditionStrategy, InitializingBean {

    @Override
    public String handle(Condition condition) {

        String id = condition.getKey();
        Object value = condition.getValue();

        return StrUtil.format("(expressionHandler.conditionRelationCompare(\"{}\",\"{}\", execution))", id, EscapeUtil.escape(JSON.toJSONString(value)));


    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FormTypeEnum.CONDITION_RELATION.getType());
    }
}
