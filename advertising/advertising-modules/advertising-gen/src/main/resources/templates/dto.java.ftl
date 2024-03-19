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
import com.hgzp.core.page.BaseDTO;
import static com.hgzp.core.constant.ValidateParam.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;

/**
 * <p>
 * ${table.comment!} DTO 数据传输对象
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
public class ${entity}DTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    /**
     * ${field.comment}
     */
    <#if field.columnType == "STRING">
    @NotBlank(message = "${field.comment!""}不能为空", groups = {add.class, edit.class, detail.class})
    <#else>
    @NotNull(message = "${field.comment!""}不能为空", groups = {add.class, edit.class, detail.class})
    </#if>
    <#-- 添加@LogData -->
    <#-- @LogData(alias = "${field.comment!""}") -->
    private ${field.propertyType} ${field.propertyName};
</#list>

    public static ${entity}DTO parseToDTO(${entity} entity) {
        ${entity}DTO ${entity}DTO = new ${entity}DTO();
        BeanUtils.copyProperties(entity, ${entity}DTO);
        return ${entity}DTO;
    }

    public static ${entity} parseToEntity(${entity}DTO dto) {
        ${entity} entity = new ${entity}();
        BeanUtils.copyProperties(dto, entity);
        if (entity.getId() == null) { // 主动生成id
            entity.setId(IdWorker.getId());
        }
        return entity;
    }
}
