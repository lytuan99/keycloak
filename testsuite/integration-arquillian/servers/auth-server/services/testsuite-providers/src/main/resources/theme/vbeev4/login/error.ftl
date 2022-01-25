<#import "template.ftl" as layout>
<#import "components/button/primary.ftl" as buttonPrimary>

<@layout.notification title=msg("error") + " | Vbee Studio" error=true; section>
    <#if section = "title">
        ${msg("errorTitle")}
    </#if>
    <#if section = "content">
        ${message.summary?no_esc}
    </#if>
    <#if section = "action">
        <#if client?? && client.baseUrl?has_content>
            <@buttonPrimary.vt component="a" href="${client.baseUrl}">${kcSanitize(msg("backToApplication"))?no_esc}</@buttonPrimary.vt>
        </#if>
    </#if>
</@layout.notification>
