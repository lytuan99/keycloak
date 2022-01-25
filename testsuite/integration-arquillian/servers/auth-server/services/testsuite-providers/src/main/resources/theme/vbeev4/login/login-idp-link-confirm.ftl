<#import "template.ftl" as layout>
<#import "components/button/primary.ftl" as buttonPrimary>

<@layout.notification title=msg("linkConfirm") + " | Vbee Studio" ; section>
    <#if section = "title">
        <h4 class="text-blue-500 font-medium">${msg("confirmLinkIdpTitle")}</h4>
    </#if>
    <#if section = "content">
       ${msg("accountExistsConfirmToLogin")}
    </#if>
    <form id="kc-register-form" action="${url.loginAction}" method="post" class="m-0">
        <#if section = "action">
            <@buttonPrimary.vt component="button" type="submit" name="submitAction" id="linkAccount" value="linkAccount">${msg("confirm")}</@buttonPrimary.vt>
        </#if>
    </form>
</@layout.notification>
