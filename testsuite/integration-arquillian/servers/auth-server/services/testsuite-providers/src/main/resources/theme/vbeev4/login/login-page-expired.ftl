<#import "template.ftl" as layout>
<#import "components/button/primary.ftl" as buttonPrimary>

<@layout.notification title="Vbee Studio"; section>
    <#if section = "title">
        ${msg("pageExpiredTitle")}
    </#if>
    <#if section = "content">
        ${msg("pageExpiredMsg1")} <a id="loginRestartLink" class="font-medium text-blue-500" href="${url.loginRestartFlowUrl}">${msg("doClickHere")}</a> .<br/>
        ${msg("pageExpiredMsg2")} <a id="loginContinueLink" class="font-medium text-blue-500" href="${url.loginAction}">${msg("doClickHere")}</a> .
    </#if>
</@layout.notification>
