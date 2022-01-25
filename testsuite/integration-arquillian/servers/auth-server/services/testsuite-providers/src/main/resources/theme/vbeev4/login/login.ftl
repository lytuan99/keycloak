<#import "template.ftl" as layout >
<#import "components/button/primary.ftl" as buttonPrimary>

<@layout.authLayout title=msg("login") + " | Vbee Studio">
    <div class="lg:grid lg:grid-cols-2 h-screen text-dark font-sfd font-regular">
        <div class="absolute top-6 left-8">
            <img class="w-full" src="${url.resourcesPath}/images/logo-vbee.png"/>
        </div>
        <div class="flex justify-center items-center bg-gray-0">
            <img src="${url.resourcesPath}/images/bg-login.png" alt="vbee-ai-studio">
        </div>
        <div class="flex justify-center items-center md:mt-3">
            <#if realm.password>
                <form
                    action="${url.loginAction}"
                    method="post"
                    onsubmit="login.disabled = true; return true;"
                    class="w-1/2"
                >
                    <#if message?has_content && (message.type != 'warning' || !isAppInitiatedAction??)>
                        <div class="alert-${message.type} w-full my-3" role="alert">
                            <#if message.type = 'success'><p class="font-bold">${msg("success")}</p></#if>
                            <#if message.type = 'warning'><p class="font-bold">${msg("warning")}</p></#if>
                            <#if message.type = 'error'><p class="font-bold">${msg("error")}</p></#if>
                            <#if message.type = 'info'><p class="font-bold">${msg("info")}</p></#if>
                            <p class="text-xs">${kcSanitize(message.summary)?no_esc}</p>
                        </div>
                    </#if>
                    <div class="flex flex-col justify-start gap-3">
                        <h3 class="text-2xl font-semibold">${msg("login")}</h3>
                        <p class="mb-9">${msg("welcomeTitle")}</p>
                        <input
                            name="credentialId"
                            type="hidden"
                            value="<#if auth.selectedCredential?has_content>${auth.selectedCredential}</#if>"
                        >
                        <div class="mb-3">
                            <div class="flex">
                                <label class="text-xs mb-2 font-medium" for="username">
                                    ${msg("emailOrPhoneNumber")} (*)
                                </label>
                            </div>
                            <input
                                tabindex="1"
                                class="input-control text-sm w-full border-gray-300
                                        <#if messagesPerField.existsError('username')>border-red-500</#if>"
                                id="username"
                                name="username"
                                value="${(login.username!'')}"
                                placeholder="johndoe@gmail.com"
                                autofocus
                                type="text"
                                autocomplete="email"
                            />
                            <#if messagesPerField.existsError('username')>
                                <div class="mt-2 text-red-600 text-xs font-semibold">
                                    ${kcSanitize(messagesPerField.getFirstError('username'))?no_esc}
                                </div>
                            </#if>
                        </div>
                        <div class="mb-2">
                            <div class="flex justify-between">
                                <label class="text-xs mb-2 font-medium" for="password">
                                    ${msg("password")} (*)
                                </label>
                                <#if realm.resetPasswordAllowed>
                                    <span class="text-xs mb-2 font-semibold text-blue-400 hover:text-blue-600">
                                        <a tabindex="5" href="${url.loginResetCredentialsUrl}">${msg("doForgotPassword")}</a>
                                    </span>
                                </#if>
                            </div>
                            <div class="relative">
                                <input
                                    tabindex="1"
                                    class="input-control text-sm w-full border-gray-300
                                        <#if messagesPerField.existsError('password')>border-red-500</#if>"
                                    id="password"
                                    name="password"
                                    value="${(login.password!'')}"
                                    placeholder="*******"
                                    autofocus
                                    type="password"
                                    autocomplete="off"
                                />
                                <div class="absolute inset-y-0 right-0 pr-4 flex items-center leading-5 cursor-pointer">
                                    <i class="fa fa-eye text-gray-500"  id="togglePassword"></i>
                                </div>
                            </div>
                            <#if messagesPerField.existsError('password')>
                                <div class="mt-2 text-red-600 text-xs font-medium">
                                    ${kcSanitize(messagesPerField.getFirstError('password'))?no_esc}
                                </div>
                            </#if>
                        </div>

                        <div class="flex gap-2">
                            <#if realm.rememberMe && !usernameEditDisabled??>
                                <div class="flex gap-2">
                                    <input class="border border-gray-300 w-4 h-4 rounded-sm" name="rememberMe" id="rememberMe" type="checkbox" checked=login.rememberMe??>
                                    <label class="mb-3 text-sm" for="rememberMe">${msg("rememberMe")}</label>
                                </div>
                            </#if>
                        </div>
                    </div>

                    <#if realm.password && realm.registrationAllowed && !registrationDisabled??>
                        <div class="flex justify-center gap-3 mb-3">
                            <span class="text-sm">${msg("noAccount")}</span>
                            <span class="text-primary text-sm hover:text-primary-500 font-semibold">
                                <a href="${url.registrationUrl}">${msg("doRegisterNow")}</a>
                            </span>
                        </div>
                    </#if>
                    <#if realm.password && social.providers??>
                        <div class=" flex justify-center relative">
                            <hr class="absolute top-6 border-gray-200 w-full z-0" />
                            <span class="bg-white p-3 z-10 text-sm">${msg("or")}</span>
                        </div>

                        <div class="flex justify-center items-center gap-2 flex-col 2xl:gap-5 2xl:flex-row">
                            <#list social.providers as p>
                                <#if p.alias="google">
                                    <a id="social-${p.alias}" href="${p.loginUrl}" class="w-60 flex items-center border border-gray-300 rounded py-2 pl-4 pr-3 hover:bg-gray-100 whitespace-nowrap">
                                        <img class="mr-2" src="${url.resourcesPath}/images/gg-icon.png" alt="image"> Đăng nhập với Google
                                    </a>
                                <#elseif p.alias="facebook">
                                    <a id="social-${p.alias}" href="${p.loginUrl}" class="w-60 flex items-center border border-gray-300 rounded py-2 pl-4 pr-3 hover:bg-gray-100 text-blue-500 whitespace-nowrap">
                                        <img class="mr-2" src="${url.resourcesPath}/images/fb-icon.png" alt="image"> Đăng nhập với Facebook
                                    </a>
                                </#if>
                            </#list>
                        </div>
                    </#if>
                    <div class="flex justify-center mt-12">
                        <@buttonPrimary.vt component="button" name="login" type="submit">${msg("login")}</@buttonPrimary.vt>
                    </div>
                </form>
            </#if>
        </div>
    </div>
</@layout.authLayout>
