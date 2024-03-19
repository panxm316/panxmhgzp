package ${package.Controller};

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import static com.hgzp.core.constant.ValidateParam.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Validated
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    @Autowired
    private ${entity}ServiceI ${entity?uncap_first}Service;

    /**
     * 新增${entity}
     *
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}DTO ${table.comment!}数据传输对象
     * @return {@link Json}
     */
    @PostMapping("/save")
    public Json save(@RequestBody @Validated(add.class) ${entity}DTO ${entity?uncap_first}DTO) {
        ${entity?uncap_first}Service.save(${entity?uncap_first}DTO);
        return Json.success();
    }

    /**
     * 删除${entity}
     *
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}DTO ${table.comment!}数据传输对象
     * @return {@link Json}
     */
    @PostMapping("/delete")
    public Json delete(@RequestBody @Validated(delete.class) ${entity}DTO ${entity?uncap_first}DTO) {
        ${entity?uncap_first}Service.delete(${entity?uncap_first}DTO);
        return Json.success();
    }

    /**
     * 修改${entity}
     *
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}DTO ${table.comment!}数据传输对象
     * @return {@link Json}
     */
    @PostMapping("/update")
    public Json update(@RequestBody @Validated(edit.class) ${entity}DTO ${entity?uncap_first}DTO) {
        ${entity?uncap_first}Service.update(${entity?uncap_first}DTO);
        return Json.success();
    }

    /**
     * 根据${entity}的某些值查询${entity}
     *
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}DTO ${table.comment!}数据传输对象
     * @return {@link Json}
     */
    @GetMapping("/get")
    public Json get(@RequestBody @Validated(detail.class) ${entity}DTO ${entity?uncap_first}DTO) {
        return Json.success(${entity?uncap_first}Service.get(${entity?uncap_first}DTO));
    }

    /**
     * 根据${entity}的某些值查询${entity}列表
     *
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}DTO ${table.comment!}数据传输对象
     * @return {@link Json}
     */
    @GetMapping("/list")
    public Json list(@RequestBody ${entity}DTO ${entity?uncap_first}DTO) {
        return Json.success(${entity?uncap_first}Service.list(${entity?uncap_first}DTO));
    }

    /**
     * 根据${entity}的某些值查询${entity}分页数据
     *
     * @author ${author}
     * @since ${date}
     * @param pageRequest 分页请求参数
     * @param ${entity?uncap_first}DTO ${table.comment!}数据传输对象
     * @return {@link Json}
     */
    @GetMapping("/get${entity}PageList")
    public Json get${entity}PageList(@RequestBody PageRequest pageRequest, @RequestBody ${entity}DTO ${entity?uncap_first}DTO) {
        Page<${entity}> page = getPage(pageRequest);
        return Json.success(${entity?uncap_first}Service.get${entity}PageList(page, ${entity?uncap_first}DTO));
    }

}
</#if>
