<#import "base/layout.ftl" as layout>

<@layout.template>
    <div class="col-md-4">
        <p>${article.title}</p>
    </div>
    <div class="col-md-4">
        Pubblicato il ${formatDateTime(article.created, 'dd/MM/yyyy HH:mm')} da ${article.createdBy.name}
        ${article.createdBy.lastName}
    </div>
    <div class="col-md-8">
        <p>${markdownToHtml(article.content)}</p>
    </div>
</@layout.template>