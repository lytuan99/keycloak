<#import "template.ftl" as layout>
<#import "components/button/primary.ftl" as buttonPrimary>

<@layout.notification title=msg("emailVerifyTitle") + " | Vbee Studio" ; section>
    <#if section = "title">
        ${msg("emailVerifyTitle")}
    </#if>
    <#if section = "content">
        <p>${msg("emailVerifyInstruction1")}</p>
        <p class="my-1">${msg("emailVerifyInstruction2")}</p>
        <a class="text-blue-500 hover:text-blue-600" href="${url.loginAction}">${msg("doClickHere")}</a>
        ${msg("emailVerifyInstruction3")}
    </#if>
</@layout.notification>
