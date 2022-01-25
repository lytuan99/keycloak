<#import "template.ftl" as layout>
<@layout.mainLayout active='password' title=msg('changePassword') bodyClass='password'; section>
    <form action="${url.passwordUrl}" class="w-10/12" method="post">
        <input type="text" id="username" name="username" value="${(account.username!'')}" autocomplete="username" readonly="readonly" style="display:none;">
        <#if password.passwordSet>
            <div class="relative">
                <label for="password">${msg("password")} (*)</label>
                <div>
                    <input 
                        type="password"
                        name="password"
                        id="password"
                        class="input-control w-full border-gray-300"
                        placeholder="********"
                    />
                    <div class="absolute inset-y-0 right-0 pt-7  pr-4 flex items-center leading-5 cursor-pointer">
                        <i class="fa fa-eye text-gray-500" id="togglePassword"></i>
                    </div>
                </div>
            </div>
        </#if>
        <input type="hidden" id="stateChecker" name="stateChecker" value="${stateChecker}">
        <div class="relative mt-6">
            <label for="passwordNew">${msg("passwordNew")} (*)</label>
            <div>
                <input 
                    type="password"
                    name="password-new"
                    id="passwordNew"
                    class="input-control w-full border-gray-300"
                    placeholder="********" 
                    autocomplete="new-password"
                />
                <div class="absolute inset-y-0 right-0 pt-7  pr-4 flex items-center leading-5 cursor-pointer">
                    <i class="fa fa-eye text-gray-500" id="toggleNewPassword"></i>
                </div>
            </div>
        </div>
        <div class="relative mt-6">
            <label for="passwordConfirm">${msg("passwordConfirm")} (*)</label>
            <div>
                <input 
                    type="password"
                    name="password-confirm"
                    id="passwordConfirm"
                    class="input-control w-full border-gray-300"
                    placeholder="********"
                    autocomplete="new-password"
                />
                <div class="absolute inset-y-0 right-0 pt-7  pr-4 flex items-center leading-5 cursor-pointer">
                    <i class="fa fa-eye text-gray-500" id="toggleConfirmPassword"></i>
                </div>
            </div>
        </div>
        <div class="flex pb-12 pt-6">
            <button class="bg-primary px-5 py-1 rounded hover:bg-yellow-700 text-white font-semibold"
                    type="submit" name="submitAction">${msg('save')}</button>
        </div>
    </form>
</@layout.mainLayout>
