<#import "template.ftl" as layout>

<@layout.notification title="Link email | Vbee Studio"; section>
    <#if section = "title">
        <h3 class="text-green-500 font-medium">${msg("emailLinkIdpTitle", idpDisplayName)}</h3>
    </#if>
    <#if section = "content">
        <p id="instruction1" class="text-md text-blackDark mb-2">
            ${msg("emailLinkIdp1", idpDisplayName, brokerContext.username, realm.displayName)}
        </p>
        <p id="instruction2" class="text-dark my-1">
            ${msg("emailLinkIdp2")} <a class="text-blue-500 hover:text-blue-600" href="${url.loginAction}">${msg("doClickHere")}</a> ${msg("emailLinkIdp3")}
        </p>
        <p id="instruction3" class="text-dark my-1">
            ${msg("emailLinkIdp4")} <a class="text-blue-500 hover:text-blue-600" href="${url.loginAction}">${msg("doClickHere")}</a> ${msg("emailLinkIdp5")}
        </p>
    </#if>
</@layout.notification>
