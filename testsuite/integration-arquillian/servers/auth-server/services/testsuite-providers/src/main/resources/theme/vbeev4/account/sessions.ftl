<#import "template.ftl" as layout>
<@layout.mainLayout active='sessions' title=msg('inforSessions') bodyClass='sessions'; section>
    <table class="w-full border-collapse">
        <thead>
            <tr class="bg-primary font-semibold text-white">
                <td class="pl-4 py-2 pr-2">${msg("ip")}</td>
                <td class="pl-4 py-2 pr-2">${msg("started")}</td>
                <td class="pl-4 py-2 pr-2">${msg("lastAccess")}</td>
                <td class="pl-4 py-2 pr-2">${msg("expires")}</td>
                <td class="pl-4 py-2 pr-2">${msg("clients")}</td>
            </tr>
        </thead>
        <tbody>
            <#list sessions.sessions as session>
                <tr class="border-b border-gray-500 text-sm">
                    <td class="pl-4 py-4 pr-2">${session.ipAddress}</td>
                    <td class="pl-4 py-4 pr-2">${session.started?datetime}</td>
                    <td class="pl-4 py-4 pr-2">${session.lastAccess?datetime}</td>
                    <td class="pl-4 py-4 pr-2">${session.expires?datetime}</td>
                    <td class="pl-4 py-4 pr-2">
                        <#list session.clients as client>
                            ${client}<br/>
                        </#list>
                    </td>
                </tr>
            </#list>
        </tbody>
    </table>
    <form action="${url.sessionsUrl}" method="post">
        <input type="hidden" id="stateChecker" name="stateChecker" value="${stateChecker}">
        <div class="py-6">
            <button id="logout-all-sessions" class="bg-primary px-5 py-1 rounded hover:bg-yellow-700 font-semibold text-white">${msg("doLogOutAllSessions")}</button>
        </div>
    </form>
</@layout.mainLayout>
