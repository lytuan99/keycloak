<#macro mainLayout active bodyClass title="">
    <!doctype html>
    <html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="robots" content="noindex, nofollow">

        <title>${msg("accountManagementTitle")}</title>
        <link rel="stylesheet" href="//use.fontawesome.com/releases/v5.0.7/css/all.css">
        <link rel="icon" href="${url.resourcesPath}/images/favicon.png">
        <#if properties.stylesCommon?has_content>
            <#list properties.stylesCommon?split(' ') as style>
                <link href="${url.resourcesCommonPath}/${style}" rel="stylesheet" />
            </#list>
        </#if>
        <#if properties.styles?has_content>
            <#list properties.styles?split(' ') as style>
                <link href="${url.resourcesPath}/${style}" rel="stylesheet" />
            </#list>
        </#if>
       
    </head>
    <body class="font-regular font-sfd">
    <div class="flex h-screen bg-gray-100">
        <div class="w-60 md:w-96 bg-white my-3 mx-1 sm:mx-4 shadow ">
            <div class="mt-2 mx-4 flex items-center justify-between">
                <img src="${url.resourcesPath}/images/logo-vbee.png"/>
            </div>
            <div class="mx-1 sm:mx-4 mt-2 sm:mt-8">
                <ul id="list">
                    <a href="${url.accountUrl}" class="w-full py-2 flex items-center pl-4 rounded cursor-pointer text-lg <#if active=='account'>active</#if>" id="account">
                        <span><i class="fa fa-user mr-4"></i></span>
                       ${msg("account")}
                    </a>
                    <#if features.passwordUpdateSupported>
                        <a class="w-full py-2  flex items-center pl-4 rounded cursor-pointer text-lg <#if active=='password'>active</#if>" href="${url.passwordUrl}" id="change_password">
                            <span><i class="fas fa-unlock-alt mr-4"></i></span>${msg("password")}
                        </a>
                    </#if>
                    <a class="w-full py-2  flex items-center pl-4 rounded cursor-pointer text-lg <#if active=='sessions'>active</#if>" href="${url.sessionsUrl}" id="sessions">
                        <span><i class="fas fa-puzzle-piece mr-4"></i></span>
                        ${msg("sessions")}
                    </a>
                </ul>
            </div>
        </div>

        <div class="w-full">
            <#if message?has_content>
                <div class="alert-${message.type} mx-7 mt-3">
                    <#if message.type=='success' ><p class="font-bold">${msg("success")}</p></#if>
                    <#if message.type=='error' ><p class="font-bold">${msg("error")}</p></#if>
                    <p class="text-sm">${kcSanitize(message.summary)?no_esc}</p>
                </div>
            </#if>
            <div class="bg-white mt-3 mx-2 sm:mx-7">
                <div class="py-5 border-b border-gray-200">
                    <div class="ml-6 text-xl">${title}</div>
                </div>
                <div class="pt-8 px-1 sm:px-8">
                    <#nested "content">
                </div>
            </div>
        </div>
    </div>
    <#if properties.scripts?has_content>
            <#list properties.scripts?split(' ') as script>
                <script type="text/javascript" src="${url.resourcesPath}/${script}"></script>
            </#list>
        </#if>
    </body>
    </html>
</#macro>
