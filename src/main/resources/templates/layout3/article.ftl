<#import "base/layout.ftl" as layout>

<#assign headerContent>
  <!-- Page Header -->
  <div class="col-lg-8 col-md-10 mx-auto">
    <div class="post-heading">
      <h1 class="post-title">${article.title}</h1>
      <span class="meta post-meta">${formatDateTime(article.created, 'dd/MM/yyyy HH:mm')} da
          ${article.createdBy.name} ${article.createdBy.lastName}</span>
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
