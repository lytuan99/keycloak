<#--<html>-->
<#--<body>-->
<#--${kcSanitize(msg("identityProviderLinkBodyHtml", identityProviderAlias, realmName, identityProviderContext.username, link, linkExpiration, linkExpirationFormatter(linkExpiration)))?no_esc}-->
<#--</body>-->
<#--</html>-->

<html lang="en">
<body style=" margin: 0;">
<div style="background-color: #F6F6F6;padding: 3rem;">
    <div style="margin: auto; min-width: 440px !important; max-width: 600px !important;">
        <div style="padding: 1rem;text-align: center;">
            <img src="https://vbee.s3.ap-southeast-1.amazonaws.com/images/iam/logo-vbee.png" alt="logo">
        </div>
        <div style="background: white;padding: 2rem;border-radius: 10px;margin-bottom: 2rem;">
            <p style="
                    font-style: normal;
                    font-weight: 600;
                    font-size: 18px;
                    line-height: 21px;
                    color: #242424;">
                <#if !user.getLastName()?? && !user.getFirstName()??>
                    ${msg("dearYou",msg("you"))}
                <#else>
                    ${msg("dearYou", user.getLastName()!'', user.getFirstName()!'')}
                </#if>
            </p>
            <p style="
                    font-style: normal;
                    font-weight: 500;
                    font-size: 18px;
                    line-height: 21px;
                    color: #242424;">${msg("emailLinkAccountTitle")}</p>
            <img style="width: 100%;" src="https://vbee.s3.ap-southeast-1.amazonaws.com/images/iam/bg-verify-email.png" alt="bg image">
            <p style="
                    font-size: 14px;
                    font-style: italic;
                    font-weight: 400;
                    color: #6E6B7B;
                    margin-bottom: 25px;">${msg("emailLinkAccountContent", identityProviderAlias, identityProviderContext.username)} <span style="color:rgb(48, 150, 197)">support@vbee.vn</span>
            </p>
            <p style="
                    font-size: 14px;
                    font-style: italic;
                    font-weight: 400;
                    color: #6E6B7B;
                    margin-bottom: 35px;
                    "> ${msg("home")}: <a href="www.vbee.vn">www.vbee.vn</a>
            </p>
            <a href="${link}" onmouseover="this.style.background='#e94710'; this.style.cursor='pointer'"
               onmouseleave="this.style.background='#FC6634'; this.style.cursor='default'"
               style="
                      text-decoration: none;
                      background: #FC6634;
                      border-radius: 5px;
                      border: none;
                      padding: 10px 28px;
                      color: white;
                      font-style: normal;
                      font-weight: 600;
                      font-size: 16px;
                      line-height: 19px;
                      margin: 0.75rem 0;">${msg("confirm")}</a>
            <div style="border: 1px solid #EBE9F1; margin-top: 3.5rem; margin-bottom: 1rem;"></div>
            <p style="font-style: normal;font-weight: normal;font-size: 14px;line-height: 21px;">
                <span style="color: #5E5873;">${msg("whyReceiveLetter")}</span>
                <a href="#" style="color: #0D6EFD; text-decoration: none; margin-left: 4rem;">${msg("notifyForUs")}</a>
            </p>
            <p style="font-style: normal;font-weight: normal;font-size: 14px;line-height: 21px; color: #5E5873;">
                <span style="">${msg("thanks")}</span>
                <br>
                <span>${msg("vbeeTeam")}</span>
            </p>
        </div>
    </div>
</div>
</body>
</html>
