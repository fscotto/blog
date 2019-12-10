<#import "base/layout.ftl" as layout>

<@layout.template>
  <div class="container">
    <div class="row">
      <div class="col-lg-8 col-md-10 mx-auto">
          ${markdownToHtml(contact)}
      </div>
    </div>
  </div>
</@layout.template>