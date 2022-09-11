<#import "template.ftl" as layout >
<#import "components/button/primary.ftl" as buttonPrimary>

<@layout.authLayout title=msg("register") + " | Vbee Studio">
    <div class="flex font-sfd text-dark h-screen font-regular" >
        <div class="relative hidden md:block">
            <div id="logo-vbee" class="absolute top-6 left-8">
                <img class="" src="${url.resourcesPath}/images/logo-vbee.png"/>
            </div>
            <img src="${url.resourcesPath}/images/bg-register.png" class="h-screen max-w-sm lg:max-w-lg"/>
        </div>
        <div class=" w-full flex flex-col">
            <div class="h-screen flex justify-center items-center">
                <form id="kc-register-form" class="w-3/5" action="${url.registrationAction}" method="post">
                    <div class="flex flex-col justify-start my-3 md:my-1 mx-3 md:mx-1">
                        <p class="text-2xl flex justify-start font-semibold text-darkBlack">${msg("register")}</p>
                        <p class="text-md pt-2.5 text-gray-600 ">${msg("enterBaseInformation")}</p>
                    </div>
                    <div class="pt-8">
                        <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
                            <div>
                                <div class="flex">
                                    <label class="text-xs mb-2 font-medium" for="firstName">${msg("firstName")} (*)</label>
                                </div>
                                <input
                                    type="text"
                                    id="firstName"
                                    name="firstName"
                                    value="${(register.formData.firstName!'')}"
                                    class="input-control text-sm w-full border-gray-300
                                    <#if messagesPerField.existsError('firstName')>border-red-500</#if>"
                                    placeholder="${msg('placeholderFirstName')}"
                                />
                                <#if messagesPerField.existsError('firstName')>
                                    <div class="mt-2 text-red-600 text-xs font-semibold">
                                        ${kcSanitize(messagesPerField.get('firstName'))?no_esc}
                                    </div>
                                </#if>
                            </div>
                            <div>
                                <div class="flex">
                                    <label class="text-xs mb-2 font-medium" for="firstName">${msg("lastName")} (*)</label>
                                </div>
                                <input
                                    type="text"
                                    id="lastName"
                                    name="lastName"
                                    value="${(register.formData.lastName!'')}"
                                    class="input-control text-sm w-full border-gray-300
                                    <#if messagesPerField.existsError('lastName')>border-red-500</#if>"
                                    placeholder="${msg('placeholderLastName')}"
                                />
                                <#if messagesPerField.existsError('lastName')>
                                    <div class="mt-2 text-red-600 text-xs font-semibold">
                                        ${kcSanitize(messagesPerField.get('lastName'))?no_esc}
                                    </div>
                                </#if>
                            </div>
                            <div>
                                <div class="flex">
                                    <label class="text-xs mb-2 font-medium">${msg('email')} (*)</label>
                                </div>
                                <input
                                    type="text"
                                    id="email"
                                    name="email"
                                    value="${(register.formData.email!'')}"
                                    autocomplete="email"
                                    class="input-control text-sm w-full border-gray-300
                                    <#if messagesPerField.existsError('email')>border-red-500</#if>"
                                    placeholder="johndoe@gmail.com"
                                    aria-invalid="<#if messagesPerField.existsError('email')>true</#if>"
                                />
                                <#if messagesPerField.existsError('email')>
                                    <div class="mt-2 text-red-600 text-xs font-semibold">
                                        ${kcSanitize(messagesPerField.get('email'))?no_esc}
                                    </div>
                                </#if>
                            </div>
                            <div>
                                <div class="flex">
                                    <label class="text-xs mb-2 font-medium" for="user.attributes.phoneNumber">
                                        ${msg("phoneNumber")}
                                    </label>
                                </div>
                                <input
                                    type="text"
                                    id="user.attributes.phoneNumber"
                                    name="user.attributes.phoneNumber"
                                    value="${(register.formData['user.attributes.phoneNumber']!'')}"
                                    class="input-control text-sm w-full"
                                    placeholder="(+84) 823 995 323"
                                />
                                <#if messagesPerField.existsError('user.attributes.phoneNumber')>
                                    <div class="mt-2 text-red-600 text-xs font-semibold">
                                        ${kcSanitize(messagesPerField.get('user.attributes.phoneNumber'))?no_esc}
                                    </div>
                                </#if>
                            </div>
                            <div>
                                <div class="flex">
                                    <label class="text-xs mb-2 font-medium" for="password">
                                        ${msg("password")} (*)
                                    </label>
                                </div>
                                <div class="relative">
                                    <input
                                        type="password"
                                        autocomplete="new-password"
                                        id="password"
                                        name="password"
                                        class="input-control text-sm w-full border-gray-300
                                            <#if messagesPerField.existsError('password')>border-red-500</#if>"
                                        placeholder="${msg("password")}"
                                    />
                                    <div class="absolute inset-y-0 right-0 pr-4 flex items-center leading-5 cursor-pointer">
                                        <i class="fa fa-eye text-gray-500" id="togglePassword"></i>
                                    </div>
                                </div>
                                <#if messagesPerField.existsError('password')>
                                    <div class="mt-2 text-red-600 text-xs font-semibold">
                                        ${kcSanitize(messagesPerField.get('password'))?no_esc}
                                    </div>
                                </#if>
                            </div>
                            <div >
                                <div class="flex">
                                    <label class="text-xs mb-2 font-medium">${msg("passwordConfirm")} (*)</label>
                                </div>
                                <div class="relative">
                                    <input
                                        type="password"
                                        id="passwordConfirm"
                                        autocomplete="off"
                                        name="password-confirm"
                                        aria-invalid="<#if messagesPerField.existsError('password-confirm')>true</#if>"
                                        class="input-control text-sm w-full border-gray-300
                                                <#if messagesPerField.existsError('password-confirm')>border-red-500</#if>"
                                        placeholder="${msg("passwordConfirm")}"
                                    />
                                    <div class="absolute inset-y-0 right-0 pr-4 flex items-center leading-5 cursor-pointer">
                                        <i class="fa fa-eye text-gray-500" id="toggleConfirmPassword"></i>
                                    </div>
                                </div>
                                <#if messagesPerField.existsError('password-confirm')>
                                    <div class="mt-2 text-red-600 text-xs font-semibold">
                                        ${kcSanitize(messagesPerField.get('password-confirm'))?no_esc}
                                    </div>
                                </#if>
                            </div>
                        </div>
                        <div class="mt-12 flex justify-center items-center">
                            <div class="text-md text-gray-500 ">${msg("haveAccount")}</div>
                            <a href="${url.loginUrl}" class="text-primary text-sm hover:text-primary-500 ml-2.5 font-semibold">${msg("loginNow")}</a>
                        </div>
                        <#if realm.password && social.providers??>
                            <div class=" flex justify-center relative">
                                <hr class="absolute top-6 border-gray-200 w-full z-0" />
                                <span class="bg-white p-3 z-10 text-sm">${msg("or")}</span>
                            </div>
                            <div class="flex justify-center items-center flex-col gap-2 lg:gap-7 lg:flex-row">
                                <#list social.providers as p>
                                    <#if p.alias="google">
                                        <a id="social-${p.alias}" href="${p.loginUrl}" class="w-60 flex items-center border border-gray-300 rounded py-2 pl-4 pr-3 hover:bg-gray-100 whitespace-nowrap">
                                            <img class="mr-2" src="${url.resourcesPath}/images/gg-icon.png" alt="image"> Đăng ký với Google
                                        </a>
                                    <#elseif p.alias="facebook">
                                        <a id="social-${p.alias}" href="${p.loginUrl}" class="w-60 flex items-center border border-gray-300 rounded py-2 pl-4 pr-3 hover:bg-gray-100 text-blue-500 whitespace-nowrap">
                                            <img class="mr-2" src="${url.resourcesPath}/images/fb-icon.png" alt="image"> Đăng ký với Facebook
                                        </a>
                                    </#if>
                                </#list>
                            </div>
                        </#if>
                        <div class="flex justify-center md:justify-end mt-10">
                            <@buttonPrimary.vt component="button" name="login" type="submit">${msg("register")}
                                <span>
                                    <i class="fa fa-chevron-right ml-2"></i>
                                </span>
                            </@buttonPrimary.vt>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</@layout.authLayout>
