<#macro vt>
  <meta charset="utf-8" />
  <meta name="robots" content="noindex, nofollow" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />

  <#if properties.meta?has_content>
    <#list properties.meta?split(" ") as meta>
      <meta name="${meta?split('==')[0]}" content="${meta?split('==')[1]}"/>
    </#list>
  </#if>

  <link rel="icon" href="${url.resourcesPath}/images/favicon.png">

  <link rel="stylesheet" href="//use.fontawesome.com/releases/v5.0.7/css/all.css">
  <#if properties.styles?has_content>
    <#list properties.styles?split(" ") as style>
      <link href="${url.resourcesPath}/${style}" rel="stylesheet" />
    </#list>
  </#if>
  <link href="${url.resourcesPath}/style-select.css" rel="stylesheet" />
</#macro>
