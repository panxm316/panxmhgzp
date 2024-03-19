package com.hgzp.advertising.service.customer;

import com.hgzp.advertising.pagemodel.customer.vo.CustomerfilesVo;
import com.hgzp.core.model.Twcustomerfiles;
import com.hgzp.service.common.IMyService;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 * 客户文件表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-10-25
 */
public interface TwcustomerfilesServiceI extends IMyService<Twcustomerfiles> {
    /**
     * getCustomerFiles
     * 方法功能: 根据客户Id获取资料信息
     *
     * @param sCustomerId
     * @return java.util.List<com.hgzp.core.model.Twcustomerfiles>
     * @author songly
     * @date 2023/10/25 12:32
     */
    List<CustomerfilesVo> getCustomerFiles(String sCustomerId);

    /**
     * saveCustomerFiles
     * 方法功能:  保存
     *
     * @param lsCustomerfiles
     * @return void
     * @author songly
     * @date 2023/10/26 8:50
     */
    void saveCustomerFiles(@RequestBody List<Twcustomerfiles> lsCustomerfiles) throws Exception;

    /**
     * updateCustomerFiles
     * 方法功能: 修改更新
     *
     * @param lsCustomerfiles
     * @return void
     * @author hgsongly
     * @date 2023/10/26 8:50
     */
    void updateCustomerFiles(@RequestBody List<Twcustomerfiles> lsCustomerfiles) throws Exception;

    /**
     * deleteFilesByCustomerIds
     * 方法功能: 根据客户id删除附件
     *
     * @param ids
     * @return void
     * @author songly
     * @date 2023/10/25 12:34
     */
    void deleteFilesByIds(String ids, boolean bCustomerId) throws Exception;
//      /**
//       * recoveryFilesByIds
//       * 方法功能:恢复已删除附件
//       * @author songly
//       * @date 2023/10/25 14:29
//       * @param ids
//       * @param bCustomerId
//       * @return void
//       */
//      void recoveryFilesByIds(String ids,boolean bCustomerId) throws  Exception;

    /**
     * isDuplicateInfo
     * 方法功能: 判断是否存在
     *
     * @param customerfiles
     * @return boolean
     * @author songly
     * @date 2023/10/25 12:53
     */
    boolean isDuplicateInfo(Twcustomerfiles customerfiles);

    /**
     * 合并客户资料
     * 方法功能:
     * @author hgsongly
     * @date 2024/1/18 10:58
     * @param customerIds
     * @param newcustomerId
     * @return void
     */
    void combineCustomerFiles(String customerIds, Long newcustomerId) throws Exception;
}
