<#import "template.ftl" as layout >
<#import "components/button/primary.ftl" as buttonPrimary>

<@layout.authLayout title=msg("reset-password") + " | Vbee Studio">
    <div class="grid grid-cols-2 h-screen font-sfd font-regular">
        <div class="absolute top-6 left-8">
            <img class="w-full" src="${url.resourcesPath}/images/logo-vbee.png"/>
        </div>
        <div class="flex justify-center items-center bg-gray-0">
            <img src="${url.resourcesPath}/images/bg-fogot-password.png" alt="vbee-ai-studio">
        </div>
        <div class="flex justify-center items-center">
            <#if realm.password>
                <form
                    action="${url.loginAction}"
                    method="post"
                    class="w-1/2"
                >

                    <div class="flex flex-col justify-start gap-3">
                        <h3 class="text-2xl font-semibold">${msg("forgotPassword")}</h3>
                        <p class="mb-9">${msg("captionResetPassword")}</p>

                        <div class="mb-3">
                            <div class="flex">
                                <label class="text-xs mb-2 font-medium" for="method">
                                    ${msg("Select reset credential method")}
                                </label>
                            </div>
                            <div class="relative">
                                <select name="resetCredentialMethod" id="myMulti" class="input-control appearance-none text-sm w-full border-gray-300">
                                    <option value="emailOtp">Nhan Otp qua email</option>
                                    <option value="phoneOtp">Nhan Otp qua so dien thoai</option>
                                    <option value="magicLink">Magic link</option>
                                </select>
                                <div class="pointer-events-none absolute inset-y-0 right-0 flex items-center px-2 text-gray-700">
                                    <svg class="fill-current h-4 w-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"><path d="M9.293 12.95l.707.707L15.657 8l-1.414-1.414L10 10.828 5.757 6.586 4.343 8z"/></svg>
                                </div>
                                <#if messagesPerField.existsError('method')>
                                    <p class="mt-2 text-red-600 text-xs font-semibold">
                                        ${kcSanitize(messagesPerField.getFirstError('method'))?no_esc}
                                    </p>
                                </#if>
                            </div>
                        </div>

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
                                placeholder="johndoe@gmail.com"
                                autofocus
                                type="text"
                                autocomplete="email"
                            />
                            <#if messagesPerField.existsError('username')>
                                <p class="mt-2 text-red-600 text-xs font-semibold">
                                    ${kcSanitize(messagesPerField.getFirstError('username'))?no_esc}
                                </p>
                            </#if>
                        </div>
                    </div>

                    <div class="flex justify-center mt-12">
                        <@buttonPrimary.vt component="button" name="login" type="submit">${msg("Continue")}</@buttonPrimary.vt>
                    </div>
                    <div class="flex justify-center mt-12">
                        <a href="${url.loginUrl}" class="text-md text-primary ml-2.5 font-semibold">${msg("backToLogin")}</a>
                    </div>
                </form>
            </#if>
        </div>
    </div>
</@layout.authLayout>
