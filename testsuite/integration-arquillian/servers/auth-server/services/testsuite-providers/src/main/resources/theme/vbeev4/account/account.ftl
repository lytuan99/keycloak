<#import "template.ftl" as layout>
<@layout.mainLayout active='account' title=msg('detailInformation') bodyClass='user'; section >
    <form action="${url.accountUrl}"  method="post">
       <div class="grid grid-cols-1 md:grid-cols-2 gap-2 sm:gap-8">
           <input type="hidden" id="stateChecker" name="stateChecker" value="${stateChecker}">
           <div class="flex flex-col">
               <label clas="mb-2">${msg("firstName")}</label>
               <input
                    type="text"
                    class="input-control text-sm w-full border-gray-300
                           <#if messagesPerField.existsError('firstName')>border-red-500</#if>"
                    id="firstName"
                    name="firstName"
                    value="${(account.firstName!'')}"
                />
           </div>
           <div class="flex flex-col">
               <label clas="mb-2">${msg("lastName")}</label>
               <input
                    type="text"
                    class="input-control text-sm w-full border-gray-300
                           <#if messagesPerField.existsError('lastName')>border-red-500</#if>"
                    id="lastName"
                    name="lastName"
                    value="${(account.lastName!'')}"
                />
           </div>
           <div class="flex flex-col">
               <label clas="mb-2">${msg("email")}</label>
               <input
                    type="text"
                    class="input-control focus:border-gray-300 text-dark bg-gray-100"
                    id="email"
                    readonly
                    name="email"
                    value="${(account.email!'')}"
                />
           </div>
           <div class="flex flex-col">
               <label for="phonenumber" clas="mb-2">${msg('phoneNumber')}</label>
               <input
                    type="text"
                    id="user.attributes.phoneNumber"
                    name="user.attributes.phoneNumber"
                    value="${(account.attributes.phoneNumber!'')}"
                    class="input-control"
                    placeholder="(+84) 123 995 323"
                />
           </div>
           <div class="flex flex-col sm:flex-row pb-12 gap-4">
               <button class="bg-primary px-5 py-1 rounded hover:bg-yellow-700 text-white font-semibold"
                       type="submit" value="Save">${msg('save')}</button>
               <button class="bg-transparent border border-gray-500 px-5 py-1 rounded hover:bg-red-500 hover:text-white"
                       type="submit" value="Cancel">${msg('cancel')}</button>
           </div>
       </div>
    </form>
</@layout.mainLayout>
