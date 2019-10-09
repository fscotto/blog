<#macro template>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta http-equiv="x-ua-compatible" content="ie=edge">
        <!-- Bootstrap core CSS -->
        <link href="/static/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

        <!-- Custom styles for this template -->
        <link href="/static/css/blog-home.css" rel="stylesheet">
        <title>${title} | Blog di Fabio Scotto di Santolo</title>
    </head>
    <body>
        <header>
            <#include "header.ftl" encoding="utf-8">
        </header>
        <div class="container">
            <div class="row">

                <#-- This processes the enclosed content: -->
                <#nested>

                <!-- Sidebar Widgets Column -->
                <div class="col-md-4">
                    <!-- Search Widget -->
                    <div class="card my-4">
                        <h5 class="card-header">Ricerca</h5>
                        <div class="card-body">
                            <div class="input-group">
                                <input type="text" class="form-control" placeholder="Cerca">
                                <span class="input-group-btn">
                                    <button class="btn btn-secondary" type="button">Vai!</button>
                                </span>
                            </div>
                        </div>
                    </div>

                    <!-- Categories Widget -->
                    <div class="card my-4">
                        <h5 class="card-header">Categorie</h5>
                        <div class="card-body">
                            <div class="row">
                                <!-- I'll fill it -->
                            </div>
                        </div>
                    </div>

                    <!-- Side Widget -->
                    <div class="card my-4">
                        <h5 class="card-header">Articoli recenti</h5>
                        <div class="card-body">
                            <!-- idem -->
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <footer class="py-5 bg-dark footer mt-auto">
            <#include "footer.ftl" encoding="utf-8">
        </footer>

        <!-- Bootstrap core JavaScript -->
        <script src="/static/vendor/jquery/jquery.min.js"></script>
        <script src="/static/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
        <!-- UML JS Renderer Library -->
        <script src="/static/js/mermaid.min.js"></script>
    </body>
</html>
</#macro>