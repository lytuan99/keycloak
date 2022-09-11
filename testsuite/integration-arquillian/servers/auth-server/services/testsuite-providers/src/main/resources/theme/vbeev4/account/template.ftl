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
        <script>
            (function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
            new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
            j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
            'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
            })(window,document,'script','dataLayer', "${properties.GTM_ID}")
        </script>
    </head>
    <body class="font-regular font-vbee-sans">
        <!-- Google Tag Manager (noscript) -->
        <noscript>
        <iframe src="https://www.googletagmanager.com/ns.html?id=${properties.GTM_ID}"
                height="0" width="0" style="display:none;visibility:hidden"></iframe>
        </noscript>
        <!-- End Google Tag Manager (noscript) -->
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
        <#--  HEADER  -->
                <div class="flex justify-end items-center h-16 bg-white shadow mt-2.5 mx-7 rounded">
                    <div class="flex gap-x-11 pr-7 items-center">
                        <div>
                            <#if referrer?has_content && referrer.url?has_content>
                                <span><i class="fas fa-arrow-alt-circle-left text-primary"></i></span>
                                <a href="${referrer.url}" id="referrer">${msg("backTo",referrer.name)}</a>
                            </#if>
                        </div>
                        <#if realm.internationalizationEnabled>
                            <div class="relative inline-block text-left">
                                <div style="width: 7rem;">
                                    <button id="menu-button" aria-expanded="true" type="button" class="flex items-center gap-2 py-2 px-3 rounded hover:bg-gray-100">
                                        <#if locale.current = 'vi'>
                                            <img width="15" src="https://vbee.s3.ap-southeast-1.amazonaws.com/images/nations/VietNam.png"/>
                                            <p class="text-sm whitespace-nowrap">
                                                ${msg("vietnamese")}
                                            </p>
                                        <#else>
                                            <img width="15" src="https://vbee.s3.ap-southeast-1.amazonaws.com/images/nations/UnitedKingdom.png"/>
                                            <p class="text-sm whitespace-nowrap">
                                                ${msg("english")}
                                            </p>
                                        </#if>
                                    </button>
                                </div>
                                <div id="target-international-menu" class="hidden origin-top-right absolute right-0 mt-2 w-30 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none" role="menu" aria-orientation="vertical" aria-labelledby="menu-button" tabindex="-1">
                                    <div class="py-1" style="width: 7rem;" role="none">
                                        <#list locale.supported as l>
                                            <a href="${l.url}" class="flex gap-1 items-center text-gray-700 block pl-3 pr-2 py-2 text-sm hover:bg-gray-200" role="menuitem"
                                               tabindex="-1" id="menu-item-0">
                                                <#if l.label = 'vi'>
                                                    <img width="15" src="https://vbee.s3.ap-southeast-1.amazonaws.com/images/nations/VietNam.png"/>
                                                    <p>${msg("vietnamese")}</p>
                                                <#else>
                                                    <img width="15" src="https://vbee.s3.ap-southeast-1.amazonaws.com/images/nations/UnitedKingdom.png"/>
                                                    <p>${msg("english")}</p>
                                                </#if>
                                            </a>
                                        </#list>
                                    </div>
                                </div>
                            </div>
                        </#if>
                        <div class="flex items-center">
                            <div class="mr-2 text-right">
                                <p class="text-sm text-dark font-bold whitespace-nowrap">${(account.firstName!'')} ${(account.lastName!'')} </p>
                            </div>
                            <div id="avatar-button" role="button" class="relative cursor-pointer <#if account.attributes.avatar??>py-2<#else>py-4</#if> px-2 rounded-full hover:bg-gray-100">
                                <#if account.attributes.avatar??>
                                    <img src="${(account.attributes.avatar!'')}" class="h-10 w-10 rounded-full"/>
                                    <#else>
                                    <span class="rounded-full p-3 bg-gray-300"><i class="fas fa-user text-gray-600"></i></span>
                                </#if>
                                <div id="target-avatar-menu" style="left: -2rem; top: 2rem"
                                     class="hidden origin-top-right absolute mt-2 w-30 rounded-md shadow-lg bg-white" role="menu" aria-orientation="vertical" aria-labelledby="menu-button" tabindex="-1">
                                    <div class="py-1 text-center" style="width: 7rem;" role="none">
                                        <a href="${url.logoutUrl}" class="text-gray-700 block px-3 py-2 text-sm hover:bg-gray-200" role="menuitem" tabindex="-1" id="menu-item-0">${msg("doSignOut")}</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            <#--  CONTENT  -->
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
