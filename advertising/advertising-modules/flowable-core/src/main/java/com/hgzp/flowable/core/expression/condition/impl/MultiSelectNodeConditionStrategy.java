package com.hgzp.flowable.core.expression.condition.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.hgzp.common.flowable.constants.FormTypeEnum;
import com.hgzp.common.flowable.dto.flow.Condition;
import com.hgzp.common.flowable.dto.flow.SelectValue;
import com.hgzp.flowable.core.expression.condition.NodeConditionStrategy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * SelectMultiMediaNodeConditionStrategy
 * 创建人：wangwk
 * 类描述：多个媒体对应条件的处理器
 * 创建日期：2023/10/16 16:36
 */
@Component
public class MultiSelectNodeConditionStrategy implements NodeConditionStrategy, InitializingBean {



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

        String format = StrUtil.format("(expressionHandler.multiStringCompare(\"{}\", \"{}\",execution,{}))", id, compare, string);

        return format;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FormTypeEnum.MULTI_SELECT.getType());
    }
}
