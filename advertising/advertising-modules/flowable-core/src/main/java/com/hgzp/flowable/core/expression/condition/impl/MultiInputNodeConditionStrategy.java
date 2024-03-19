package com.hgzp.flowable.core.expression.condition.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.hgzp.common.flowable.constants.FormTypeEnum;
import com.hgzp.common.flowable.dto.flow.Condition;
import com.hgzp.common.flowable.dto.flow.SelectValue;
import com.hgzp.flowable.core.expression.condition.NodeConditionStrategy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 字符类型处理器（其中数据是以英文逗号分隔）
 */
@Component
public class MultiInputNodeConditionStrategy implements NodeConditionStrategy, InitializingBean {
    /**
     * 抽象方法 处理表达式
     *
     */
    @Override
    public String handle(Condition condition) {


        String compare = condition.getExpression();
        String id = condition.getKey();
        Object value = condition.getValue();

        String[] splitValue = value.toString().split(",");
        StringBuilder sb = new StringBuilder();

        for (String o : splitValue) {
            sb.append(",\"").append(o).append("\"");
        }
        String string = sb.toString();
        if (splitValue.length > 0) {
            string = string.substring(1);
        }
        String format = StrUtil.format("(expressionHandler.multiStringCompare(\"{}\", \"{}\",execution,{}))", id, compare, string);

        return format;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FormTypeEnum.MULTI_INPUT.getType());
    }
}
