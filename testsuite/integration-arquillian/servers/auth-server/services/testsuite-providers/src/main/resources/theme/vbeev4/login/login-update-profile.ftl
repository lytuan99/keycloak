<#import "template.ftl" as layout >
<#import "components/button/primary.ftl" as buttonPrimary>

<@layout.authLayout title=msg("updateProfile") + " | Vbee Studio">
    <div class="flex font-sfd text-dark h-screen font-regular" >
        <div class="relative hidden md:block">
            <div class="absolute top-6 left-8">
                <img class="" src="${url.resourcesPath}/images/logo-vbee.png"/>
            </div>
            <img src="${url.resourcesPath}/images/bg-register.png" class="h-screen max-w-sm lg:max-w-lg"/>
        </div>
        <div class="lg:w-2/5 w-full m-auto flex flex-col">
            <div class="h-screen flex justify-center items-center">
                <form id="kc-update-profile-form" class="w-3/5" action="${url.loginAction}" method="post">
                    <div class="flex flex-col justify-start my-3 md:my-1 mx-3 md:mx-1">
                        <p class="text-2xl flex justify-start font-semibold text-darkBlack">${msg("updateProfileTitle")}</p>
                        <p class="text-md pt-2.5 text-gray-600 whitespace-nowrap">${msg("updateProfileDescription")}</p>
                    </div>
                    <div class="pt-8">
                        <div class="flex flex-col gap-3">
                            <div class="flex flex-col">
                                <label class="text-xs font-medium pb-1">Email (*)</label>
                                <input
                                    type="email"
                                    id="email" name="email" value="${(user.email!'')}"
                                   class="input-control text-sm w-full border-gray-300
                                    <#if messagesPerField.existsError('email')>border-red-500</#if>"
                                   placeholder="abcd@gmail.com"
                                />
                                <#if messagesPerField.existsError('email')>
                                    <div class="mt-2 text-red-600 text-xs font-semibold">
                                        ${kcSanitize(messagesPerField.get('email'))?no_esc}
                                    </div>
                                </#if>
                            </div>
                            <div class="flex flex-col">
                                <label class="text-xs font-medium pb-1">${msg("firstName")} (*)</label>
                                <input
                                    type="text"
                                    id="firstName"
                                    name="firstName"
                                    value="${(user.firstName!'')}"
                                    class="input-control text-sm w-full border-gray-300
                                        <#if messagesPerField.existsError('firstName')>border-red-500</#if>"
                                    placeholder="${msg('placeholderFirstName')}" />
                                <#if messagesPerField.existsError('firstName')>
                                    <div class="mt-2 text-red-600 text-xs font-semibold">
                                        ${kcSanitize(messagesPerField.get('firstName'))?no_esc}
                                    </div>
                                </#if>
                            </div>
                            <div class="flex flex-col">
                                <label class="text-xs font-medium pb-1">${msg("lastName")} (*)</label>
                                <input
                                    type="text"
                                    id="lastName"
                                    name="lastName"
                                    value="${(user.lastName!'')}"
                                    class="input-control text-sm w-full border-gray-300
                                        <#if messagesPerField.existsError('lastName')>border-red-500</#if>"
                                    placeholder="${msg('placeholderLastName')}"/>
                                <#if messagesPerField.existsError('lastName')>
                                    <div class="mt-2 text-red-600 text-xs font-semibold">
                                        ${kcSanitize(messagesPerField.get('lastName'))?no_esc}
                                    </div>
                                </#if>
                            </div>
                            <div class="flex justify-center mt-10">
                                <button type="submit" class="bg-primary text-white font-semibold hover:bg-red-600 rounded py-2.5 px-5">
                                    ${msg("complete")}
                                </button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</@layout.authLayout>
