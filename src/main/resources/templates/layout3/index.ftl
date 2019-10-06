<#import "base/layout.ftl" as layout>

<@layout.template>
  <div class="col-lg-8 col-md-10 mx-auto">
      <#list articles as article>
        <div class="post-preview">
          <a href="/article/${article.id}">
            <h2 class="post-title">
                ${article.title}
            </h2>
            <h3 class="post-subtitle"></h3>
          </a>
          <p class="post-meta">
            Pubblicato il ${formatDateTime(article.created, 'dd/MM/yyyy HH:mm')} da
              ${article.createdBy.name} ${article.createdBy.lastName}
          </p>
        </div>
        <hr>
      </#list>

      <#if false>
        <!-- Pager -->
        <div class="clearfix">
          <a class="btn btn-primary float-right" href="#">Older Posts &rarr;</a>
        </div>
      </#if>

  </div>
</@layout.template>
