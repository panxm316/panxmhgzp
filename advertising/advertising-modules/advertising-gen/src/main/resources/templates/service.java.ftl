package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    /**
     * 新增${entity}
     *
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}DTO ${table.comment!}数据传输对象
     * @return void
     */
    void save(${entity}DTO ${entity?uncap_first}DTO);

    /**
     * 删除${entity}
     *
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}DTO ${table.comment!}数据传输对象
     * @return void
     */
    void delete(${entity}DTO ${entity?uncap_first}DTO);

    /**
     * 修改${entity}
     *
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}DTO ${table.comment!}数据传输对象
     * @return void
     */
    void update(${entity}DTO ${entity?uncap_first}DTO);

    /**
     * 根据ID查询${entity}
     *
     * @author ${author}
     * @since ${date}
     * @param id ${entity}.id
     * @return {@link ${entity}}
     */
    @Override
    ${entity} getById(Serializable id);

    /**
     * 根据${entity}的某些值查询${entity}
     *
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}DTO ${table.comment!}数据传输对象
     * @return {@link ${entity}VO}
     */
    ${entity}VO get(${entity}DTO ${entity?uncap_first}DTO);

    /**
     * 根据${entity}的某些值查询${entity}列表
     *
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}DTO ${table.comment!}数据传输对象
     * @return {@link List<${entity}VO>}
     */
    List<${entity}VO> list(${entity}DTO ${entity?uncap_first}DTO);

    /**
     * 根据${entity}的某些值查询${entity}分页数据
     *
     * @author ${author}
     * @since ${date}
     * @param pageRequest 分页请求参数
     * @param ${entity?uncap_first}DTO ${table.comment!}数据传输对象
     * @return {@link IPage<${entity}VO>}
     */
    IPage<${entity}VO> get${entity}PageList(Page<${entity}> page, ${entity}DTO ${entity?uncap_first}DTO);

}
</#if>
