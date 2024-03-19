package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
<#if table.serviceInterface>
import ${package.Service}.${table.serviceName};
</#if>
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>()<#if table.serviceInterface>, ${table.serviceName}</#if> {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}><#if table.serviceInterface> implements ${table.serviceName}</#if> {

    @Autowired
    private HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(${entity}DTO ${entity?uncap_first}DTO) {
        ${entity} ${entity?uncap_first} = ${entity}DTO.parseToEntity(${entity?uncap_first}DTO);
        innerInterceptor.recoredLog();
        boolean success = save(${entity?uncap_first});
        if (!success) {
            throw new RuntimeException("${entity}保存失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(${entity}DTO ${entity?uncap_first}DTO) {
        ${entity} ${entity?uncap_first} = ${entity}DTO.parseToEntity(${entity?uncap_first}DTO);
        innerInterceptor.recoredLog();
        boolean success = removeById(${entity?uncap_first});
        if (!success) {
            throw new RuntimeException("${entity}删除失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(${entity}DTO ${entity?uncap_first}DTO) {
        ${entity} ${entity?uncap_first} = ${entity}DTO.parseToEntity(${entity?uncap_first}DTO);
        innerInterceptor.recoredLog();
        boolean success = updateById(${entity?uncap_first});
        if (!success) {
            throw new RuntimeException("${entity}修改失败");
        }
    }

    @Override
    public ${entity} getById(Serializable id) {
        ${entity} ${entity?uncap_first} = super.getById(id);
        if (ObjectUtil.isNull(${entity?uncap_first})) {
            throw new RuntimeException("${entity}不存在");
        }
        return ${entity?uncap_first};
    }

    @Override
    public ${entity}VO get(${entity}DTO ${entity?uncap_first}DTO) {
        ${entity} ${entity?uncap_first} = getById(${entity?uncap_first}DTO.getId());
        return ${entity}VO.parseToVO(${entity?uncap_first});
    }

    @Override
    public List<${entity}VO> list(${entity}DTO ${entity?uncap_first}DTO) {
        List<${entity}> ${entity?uncap_first}List = list(Wrappers.<${entity}>lambdaQuery());
        if (CollectionUtil.isEmpty(${entity?uncap_first}List)) {
            return new ArrayList();
        }
        return ${entity?uncap_first}List.stream().map(${entity}VO::parseToVO).collect(Collectors.toList());
    }

    @Override
    public IPage<${entity}VO> get${entity}PageList(Page<${entity}> page, ${entity}DTO ${entity?uncap_first}DTO) {
        Page<${entity}> p = page(page, Wrappers.<${entity}>lambdaQuery());
        Page<${entity}VO> voPage = new Page();
        voPage.setCurrent(p.getCurrent());
        voPage.setTotal(p.getTotal());
        voPage.setRecords(p.getRecords().stream().map(${entity}VO::parseToVO).collect(Collectors.toList()));
        return voPage;
    }

 }
</#if>
