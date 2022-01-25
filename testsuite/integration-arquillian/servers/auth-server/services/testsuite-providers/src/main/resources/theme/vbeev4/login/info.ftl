<#import "template.ftl" as layout>
<#import "components/button/primary.ftl" as buttonPrimary>

<@layout.notification title="Vbee Studio" ; section>
    <#if section = "title">
        <#if messageHeader??>
            ${messageHeader}
        </#if>
    </#if>
    <#if section = "content">
<#--        message.summary alreadyLoggedIn -->
        <p class="text-md mb-4">${message.summary}
            <#if requiredActions??>
                <#list requiredActions>: <b><#items as reqActionItem>${msg("requiredAction.${reqActionItem}")}<#sep>, </#items></b></#list><#else></#if></p>
    </#if>
    <#if section = "action">
        <#if pageRedirectUri?has_content>
            <@buttonPrimary.vt component="a" href="${pageRedirectUri}">${kcSanitize(msg("backToApplication"))?no_esc}</@buttonPrimary.vt>
        <#elseif actionUri?has_content>
            <@buttonPrimary.vt component="a" href="${actionUri}">${kcSanitize(msg("proceedWithAction"))?no_esc}</@buttonPrimary.vt>
        <#elseif (client.baseUrl)?has_content>
            <@buttonPrimary.vt component="a" href="${client.baseUrl}">${kcSanitize(msg("backToApplication"))?no_esc}</@buttonPrimary.vt>
        </#if>
    </#if>
</@layout.notification>
