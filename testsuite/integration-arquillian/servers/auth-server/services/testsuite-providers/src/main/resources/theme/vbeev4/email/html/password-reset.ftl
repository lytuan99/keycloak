<html lang="en">
<body style="margin: 0;">
    <div style="background-color: #F6F6F6; padding: 3rem;">
        <div style="margin: auto;min-width: 320px !important;width: 50% !important;">
            <div style="text-align: center; padding: 1rem;">
                <img src="https://vbee.s3.ap-southeast-1.amazonaws.com/images/iam/logo-vbee.png" alt="logo">
            </div>
            <div style="background: white;padding: 2rem;border-radius: 10px;margin: 2rem;">
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
                    color: #242424;">${msg("requestResetPasswordTitle")}</p>
                <img style="width: 100%;" src="https://vbee.s3.ap-southeast-1.amazonaws.com/images/iam/reset-password-bg.png" alt="bg image">
                <p style="
                    font-size: 14px;
                    font-style: italic;
                    font-weight: 400;
                    color: #6E6B7B;
                    margin-bottom: 25px;
                ">${msg('resetPasswordContent')}</p>
                <a href="${link}"
                   onmouseover="this.style.background='#e94710'; this.style.cursor='pointer'"
                   onmouseleave="this.style.background='#FC6634'; this.style.cursor='default'"
                   style="
                    background: #FC6634;
                    text-decoration: none;
                    border-radius: 5px;
                    border: none;
                    padding: 10px 28px;
                    color: white;
                    font-style: normal;
                    font-weight: 600;
                    font-size: 16px;
                    line-height: 19px;">${msg('resetPassword')}</a>
            </div>
        </div>
    </div>
</body>
</html>
