package com.hgzp.flowable.core.expression.condition.impl;

import cn.hutool.core.util.StrUtil;
import com.hgzp.flowable.core.expression.condition.NodeConditionStrategy;
import com.hgzp.common.flowable.constants.FormTypeEnum;
import com.hgzp.common.flowable.dto.flow.Condition;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 字符类型处理器
 */
@Component
public class NumberNodeConditionStrategy implements NodeConditionStrategy, InitializingBean {
    /**
     * 抽象方法 处理表达式
     */
    @Override
    public String handle(Condition condition) {


        String compare = condition.getExpression();
        String id = condition.getKey();
        Object value = condition.getValue();

        return StrUtil.format("(expressionHandler.numberCompare(\"{}\",\"{}\",{},execution))", id, compare,
                value);


    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FormTypeEnum.NUMBER.getType());
    }
}
