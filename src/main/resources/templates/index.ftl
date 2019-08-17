<#import "base/layout.ftl" as layout>

<@layout.template>
    <!-- Blog Entries Column -->
    <div class="col-md-8">
        <h1 class="my-4">
            Articoli
            <small></small>
        </h1>

        <!-- Blog Post -->
        <#list articles as article>
            <div class="card mb-4">
                <img class="card-img-top" src="http://placehold.it/750x300" alt="Card image cap">
                <div class="card-body">
                    <h2 class="card-title">${article.title}</h2>
                    <p class="card-text">
                        ${article.content}
                    </p>
                    <a href="#" class="btn btn-primary">Leggi &rarr;</a>
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
        <ul class="pagination justify-content-center mb-4">
            <li class="page-item">
                <a class="page-link" href="#">&larr; Precendenti</a>
            </li>
            <li class="page-item disabled">
                <a class="page-link" href="#">Successivi &rarr;</a>
            </li>
        </ul>
    </div>
</@layout.template>
