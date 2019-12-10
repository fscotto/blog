<#import "base/layout.ftl" as layout>

<@layout.template>
    <!-- Blog Entries Column -->
    <div class="col-md-8">
        <h1 class="my-4">Blog</h1>

        <!-- Blog Post -->
        <#list articles as article>
            <div class="card mb-4">
                <img class="card-img-top" src="/static/image/header-post.png" alt="Card image cap">
                <div class="card-body">
                    <h2 class="card-title">${article.title}</h2>
                    <p class="card-text text-justify">
                        <#if article.content?length &gt;= 300>
                            ${article.content?substring(0, 300)}
                        <#else>
                            ${article.content}
                        </#if>
                    </p>
                    <a href="/article/${article.id}" class="btn btn-primary">Leggi &rarr;</a>
                </div>
                <div class="card-footer text-muted">
                    Pubblicato il ${formatDateTime(article.created, 'dd/MM/yyyy HH:mm')} da
                    <a href="#">${article.createdBy.name} ${article.createdBy.lastName}</a>
                </div>
            </div>
        <#else>
            <div class="card mb-4">
                <p>Nessun articolo da visualizzare</p>
            </div>
        </#list>

        <!-- Pagination -->
        <#if viewPagination>
            <ul class="pagination justify-content-center mb-4">
                <li class="page-item">
                    <a class="page-link" href="#">&larr; Precendenti</a>
                </li>
                <li class="page-item disabled">
                    <a class="page-link" href="#">Successivi &rarr;</a>
                </li>
            </ul>
        </#if>
    </div>
</@layout.template>
