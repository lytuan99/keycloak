<#macro vt component="button" bgColor="bg-primary" textColor="text-white" hoverColor="bg-red-600" rest...>
    <${component}
    class="btn bg-primary font-semibold text-white hover:bg-red-600"
    <#list rest as attrName, attrValue>
        ${attrName}="${attrValue}"
    </#list>
    >
    <#nested>
    </${component}>
</#macro>
