package ${package.Entity};

<#list table.importPackages as pkg>
import ${pkg};
</#list>
<#if springdoc>
import io.swagger.v3.oas.annotations.media.Schema;
<#elseif swagger>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
import lombok.*;
import lombok.experimental.Accessors;
<#-- import com.hgzp.core.annotation.LogData; -->
import static com.hgzp.core.constant.ValidateParam.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 * ${table.comment!} VO 前端页面视图对象
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
@Accessors(chain = true)
<#-- @LogData(alias = "${table.comment!}") -->
public class ${entity}VO {

    private static final long serialVersionUID = 1L;

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    /**
     * ${field.comment}
     */
    <#-- 添加@LogData -->
    <#-- @LogData(alias = "${field.comment!""}") -->
    private ${field.propertyType} ${field.propertyName};
</#list>

    public static ${entity}VO parseToVO(${entity} entity) {
        ${entity}VO ${entity}VO = new ${entity}VO();
        BeanUtils.copyProperties(entity, ${entity}VO);
        return ${entity}VO;
    }

    public static ${entity} parseToEntity(${entity}VO vo) {
        ${entity} entity = new ${entity}();
        BeanUtils.copyProperties(vo, entity);
        return entity;
    }
}
