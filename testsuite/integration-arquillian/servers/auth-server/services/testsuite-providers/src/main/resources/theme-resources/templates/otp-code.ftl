<#import "template.ftl" as layout>
<#import "components/button/primary.ftl" as buttonPrimary>

<@layout.authLayout title=msg("OTP") + " | Vbee Studio">
    <script>
        const future = ${otpModel.resendDuration};
    </script>
    <div class="grid grid-cols-2 h-screen font-sfd font-regular">
        <div class="absolute top-6 left-8">
            <img class="w-full" src="${url.resourcesPath}/images/logo-vbee.png"/>
        </div>
        <div class="flex justify-center items-center bg-gray-0">
            <img src="${url.resourcesPath}/images/bg-fogot-password.png" alt="vbee-ai-studio">
        </div>
        <div class="flex justify-center items-center">
            <form
                action="${url.loginAction}"
                method="post"
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
                    <h3 class="text-2xl font-semibold">${msg("Enter your code")}</h3>
                    <p class="mb-9">${msg("enterYourCodeDescription", msg(otpModel.method))}</p>
                    <div class="mb-3">
                        <div class="flex">
                            <label class="text-sm mb-2 font-medium" for="username">
                                ${msg("Your code")}
                            </label>
                        </div>
                        <input
                            tabindex="1"
                            class="input-control text-sm w-full border-gray-300
                                    <#if messagesPerField.existsError('code')>border-red-500</#if>"
                            id="code"
                            name="code"
                            placeholder="Enter your code"
                            autofocus
                            type="text"
                        />
                        <p id="resend-link" class="text-sm mt-2 text-gray-500 cursor-not-allowed">resend <span id="resend-duration"></span></p>
                    </div>
                </div>

                <div class="flex justify-center mt-12">
                    <@buttonPrimary.vt component="button" name="login" type="submit">${msg("continue")}</@buttonPrimary.vt>
                </div>

                <div class="flex justify-center mt-12">
                    <a href="${url.loginUrl}" class="text-md text-primary ml-2.5 font-semibold">${msg("backToLogin")}</a>
                </div>
            </form>
        </div>
    </div>
</@layout.authLayout>
