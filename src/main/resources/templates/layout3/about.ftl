<#import "base/layout.ftl" as layout>

<@layout.template>
  <div class="col-md-8">
    <p>${markdownToHtml(info)}</p>
  </div>
</@layout.template>
