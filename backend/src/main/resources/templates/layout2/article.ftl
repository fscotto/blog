<#import "base/layout.ftl" as layout>

<@layout.template>
    <div class="container">
        <div class="col-md-4">
            <h2 class="blog-post-title">${article.title}</h2>
        </div>
        <div class="col-md-4">
            <p class="blog-post-meta">
                ${formatDateTime(article.created, 'dd/MM/yyyy HH:mm')} da
                <a href="#">${article.createdBy.name} ${article.createdBy.lastName}</a>
            </p>
        </div>
    </div>
    <div class="col-md-8 text-justify">
        <p>${markdownToHtml(article.content)}</p>
    </div>
</@layout.template>
