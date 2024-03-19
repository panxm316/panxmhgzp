package com.hgzp.advertisingsys.pagemodel.system.vo;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DutiesVO
 * 创建人：yanz
 * 类描述：人员职务 VO
 * 创建日期：2023/8/31 15:32
 */
@AllArgsConstructor
@NoArgsConstructor
public class DutiesVO extends BaseQueryInfo {
    /**
     * 是否启用
     */
    private Boolean buse;

    public Boolean getBuse() {
        return buse;
    }

    public void setBuse(String buse) {
        switch (buse.toLowerCase()) {
            case "true":
            case "1":
                this.buse = true;
                break;
            case "false":
            case "0":
                this.buse = false;
        }

    }
}