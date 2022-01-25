<#import "template.ftl" as layout >
<#import "components/button/primary.ftl" as buttonPrimary>

<@layout.authLayout title=msg("updatePassword") + " | Vbee Studio">
    <div class="grid grid-cols-2 h-screen font-sfd font-regular">
        <div class="absolute top-6 left-8">
            <img class="w-full" src="${url.resourcesPath}/images/logo-vbee.png"/>
        </div>
        <div class="flex justify-center items-center bg-gray-0">
            <img src="${url.resourcesPath}/images/bg-update-password.png" alt="vbee-ai-studio">
        </div>
        <div class="flex justify-center items-center">
            <form
                action="${url.loginAction}"
                method="post"
                class="w-1/2"
            >
                <input type="text" id="username" name="username" value="${username}" autocomplete="username"
                    readonly="readonly" style="display:none;"/>
                <input type="password" id="password" name="password" autocomplete="current-password" style="display:none;"/>
                <div class="flex flex-col justify-start gap-3">
                    <h3 class="text-2xl font-semibold">${msg('updatePassword')}</h3>
                    <p class="mb-9">${msg("captionUpdatePassword")}</p>

                    <div class="mb-3">
                        <div class="flex">
                            <label class="text-xs mb-2 font-medium" for="password-new">
                                ${msg('newPassword')}
                            </label>
                        </div>
                        <div class="relative">
                            <input
                                tabindex="1"
                                class="input-control text-sm w-full border-gray-300
                                            <#if messagesPerField.existsError('password')>border-red-500</#if>"
                                id="passwordNew"
                                name="password-new"
                                placeholder="*******"
                                autofocus
                                type="password"
                                autocomplete="new-password"
                                aria-invalid="<#if messagesPerField.existsError('password','password-confirm')>true</#if>"
                            />
                            <div class="absolute inset-y-0 right-0 pr-4 flex items-center leading-5 cursor-pointer">
                                <i class="fa fa-eye text-gray-500" id="toggleNewPassword"></i>
                            </div>
                        </div>
                        <#if messagesPerField.existsError('password')>
                            <p class="mt-2 text-red-600 text-xs font-semibold">
                                ${kcSanitize(messagesPerField.getFirstError('password'))?no_esc}
                            </p>
                        </#if>
                    </div>
                    <div class="mb-2">
                        <div class="flex justify-between">
                            <label class="text-xs mb-2 font-medium" for="password-confirm">
                                ${msg('confirmNewPassword')}
                            </label>
                        </div>
                        <div class="relative">
                            <input
                                tabindex="1"
                                class="input-control text-sm w-full border-gray-300
                                        <#if messagesPerField.existsError('password-confirm')>border-red-500</#if>"
                                type="password"
                                id="passwordConfirm"
                                name="password-confirm"
                                placeholder="*******"
                                autofocus
                                autocomplete="new-password"
                                aria-invalid="<#if messagesPerField.existsError('password-confirm')>true</#if>"
                            />
                            <div class="absolute inset-y-0 right-0 pr-4 flex items-center leading-5 cursor-pointer">
                                <i class="fa fa-eye text-gray-500" id="toggleConfirmPassword"></i>
                            </div>
                        </div>
                        <#if messagesPerField.existsError('password-confirm')>
                            <p id="input-error-password-confirm" class="mt-2 text-red-600 text-xs font-semibold" aria-live="polite">
                                ${kcSanitize(messagesPerField.get('password-confirm'))?no_esc}
                            </p>
                        </#if>
                    </div>
                </div>
                <div class="flex justify-center mt-12">
                    <button class="bg-primary text-white font-semibold hover:bg-red-600 rounded py-2.5 px-5" type="submit">${msg('setPassword')}</button>
                </div>
            </form>
        </div>
    </div>
</@layout.authLayout>
