package com.hgzp.flowable.core.expression.condition.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.hgzp.flowable.core.expression.condition.NodeConditionStrategy;
import com.hgzp.common.flowable.constants.FormTypeEnum;
import com.hgzp.common.flowable.dto.flow.Condition;
import com.hgzp.common.flowable.dto.flow.SelectValue;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 字符类型处理器
 */
@Component
public class SingleSelectNodeConditionStrategy implements NodeConditionStrategy, InitializingBean {
    /**
     * 抽象方法 处理表达式
     */
    @Override
    public String handle(Condition condition) {


        String compare = condition.getExpression();
        String id = condition.getKey();
        Object value = condition.getValue();

        List<SelectValue> list = Convert.toList(SelectValue.class,value);


        StringBuilder sb = new StringBuilder();

        for (SelectValue o : list) {
            sb.append(",\"").append(o.getKey()).append("\"");
        }
        String string = sb.toString();
        if (CollUtil.isNotEmpty(list)) {
            string = string.substring(1);
        }
        if ("in".equals(compare)) {
            return StrUtil.format("(expressionHandler.singleSelectHandler(\"{}\", execution,{}))", id, string);
        }

        return StrUtil.format("(!expressionHandler.singleSelectHandler(\"{}\", execution,{}))", id, string);


    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FormTypeEnum.SINGLE_SELECT.getType());
    }
}
