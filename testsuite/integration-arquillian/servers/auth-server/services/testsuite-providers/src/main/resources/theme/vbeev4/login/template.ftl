<#import "components/document.ftl" as document>

<#macro authLayout title>
    <html>
        <head>
            <title>${title}</title>
            <@document.vt />
        </head>
        <body class="font-sfd font-regular">
            <#nested>
        </body>
        <#if properties.scripts?has_content>
            <#list properties.scripts?split(" ") as script>
                <script defer src="${url.resourcesPath}/${script}" type="text/javascript"></script>
            </#list>
        </#if>
    </html>
</#macro>

<#macro notification title displayMessage=true error=false>
    <html>
        <head>
            <title>${title}</title>
            <@document.vt />
        </head>
        <body class="font-sfd font-regular">
            <div class="relative h-screen">
                <div class="flex flex-col items-center justify-center">
                    <img src="${url.resourcesPath}/images/bg-login.png"/>
                </div>
                <div class="absolute inset-0 bg-black bg-opacity-20 flex justify-center items-center">
                    <div class="bg-white flex gap-x-2 sm:gap-x-5 py-6 px-1 sm:px-9 rounded mx-1.5 sm:mx-0">
                        <#if error>
                            <i class="fa fa-exclamation-circle text-red-500 text-5xl sm:text-7xl"></i>
                        <#else>
                            <i class="fa fa-exclamation-circle text-green-500 text-5xl sm:text-7xl"></i>
                        </#if>
                        <div>
                        <#if error>
                            <div class="text-xl text-red-400"><#else><div class="text-xl text-blue-400">
                        </#if>
                                <#nested "title">
                            </div>
                            <div class="text-sm mt-4 w-72">
                                <#nested "content">
                            </div>
                            <div class="flex justify-end gap-x-12 ml-0">
                                <#nested "action">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </body>
    </html>
</#macro>
