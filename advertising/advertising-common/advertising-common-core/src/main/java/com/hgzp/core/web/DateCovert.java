package com.hgzp.core.web;

import cn.hutool.core.convert.Convert;

import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * 项目名：hgcb-parent
 * 类全名：cpsn.configuration.DateCovter
 * 创建人：wangwk
 * 类描述：
 * 创建日期：2021/4/26 15:04
 */
public class DateCovert extends PropertyEditorSupport {


    @Override
    public void setAsText(String text) throws IllegalArgumentException {

        Date date = Convert.toDate(text);

        setValue(date);
    }
}
