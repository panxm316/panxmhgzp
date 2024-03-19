/**
 * ${table.comment!} TS类型
 *
 * @author ${author}
 * @since ${date}
 */
export interface ${entity} {
<#list table.fields as field>
    /**
     * ${field.comment}
     */
    <#if field.columnType == "STRING" || field.columnType == "CHARACTER" || field.columnType == "DATE">
    ${field.propertyName}?: string
    <#elseif field.columnType == "INTEGER" || field.columnType == "LONG" || field.columnType == "DOUBLE"
        || field.columnType == "FLOAT" || field.columnType == "SHORT" || field.columnType == "BYTE"
        || field.columnType == "BIGDECIMAL" || field.columnType == "BIGINTEGER">
    ${field.propertyName}?: string
    <#elseif field.columnType == "BOOLEAN">
    ${field.propertyName}?: boolean
    <#else>
    ${field.propertyName}?: any
    </#if>
</#list>
}
