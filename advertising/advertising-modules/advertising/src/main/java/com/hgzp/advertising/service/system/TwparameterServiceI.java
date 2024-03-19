package com.hgzp.advertising.service.system;

import com.hgzp.core.model.Twparameter;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统参数表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-15
 */
public interface TwparameterServiceI extends IService<Twparameter> {
    /**
     * 根据key获取参数值
     * @param key
     * @return
     */
    public String getParameterByKey(String key);
}
