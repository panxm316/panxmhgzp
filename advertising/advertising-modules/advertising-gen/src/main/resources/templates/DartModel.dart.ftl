import 'package:dart_mappable/dart_mappable.dart';

part '${entity?lower_case}.mapper.dart';

/// ${table.comment!}
@MappableClass()
class ${entity} with ${entity}Mappable {
<#list table.fields as field>
  /// ${field.comment!""}
    <#if field.columnType == "LONG">
  String? ${field.propertyName};
    <#elseif field.columnType == "INTEGER">
  int? ${field.propertyName};
    <#elseif field.columnType == "DOUBLE" || field.columnType == "FLOAT">
  double? ${field.propertyName};
    <#elseif field.columnType == "BOOLEAN">
  bool? ${field.propertyName};
    <#elseif field.propertyType == "BigDecimal" || field.propertyType == "BigInteger">
  String? ${field.propertyName};
    <#elseif field.columnType == "DATE">
  String? ${field.propertyName};
    <#else>
    <#-- String -->
  ${field.propertyType}? ${field.propertyName};
    </#if>

</#list>
  ${entity}({<#list table.fields as field>
    this.${field.propertyName},</#list>
  });
}
