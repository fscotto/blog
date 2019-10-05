<#import "base/layout.ftl" as layout>

<#assign headerContent>
  <!-- Page Header -->
  <div class="col-lg-8 col-md-10 mx-auto">
    <div class="post-heading">
      <h1>${article.title}</h1>
      <h2 class="subheading">Problems look mighty small from 150 miles up</h2>
      <span class="meta">${formatDateTime(article.created, 'dd/MM/yyyy HH:mm')} da
          <a href="#">${article.createdBy.name} ${article.createdBy.lastName}</a></span>
    </div>
  </div>
</#assign>

<@layout.template header=headerContent>
  <!-- Post Content -->
  <article>
    <div class="container">
      <div class="row">
        <div class="col-lg-8 col-md-10 mx-auto">
            ${markdownToHtml(article.content)}
        </div>
      </div>
    </div>
  </article>
</@layout.template>
