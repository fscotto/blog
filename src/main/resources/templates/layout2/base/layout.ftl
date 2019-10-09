<#macro template>
<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta http-equiv="x-ua-compatible" content="ie=edge">
        <title>${title} | Blog di Fabio Scotto di Santolo</title>

        <!-- Bootstrap core CSS -->
        <link href="/static/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

        <!-- Custom styles for this template -->
        <link href="https://fonts.googleapis.com/css?family=Playfair+Display:700,900" rel="stylesheet">
        <!-- Custom styles for this template -->
        <link href="/static/css/blog.css" rel="stylesheet">
    </head>
    <body>

        <#include "header.ftl" encoding="utf-8">

        <main role="main" class="container">
            <div class="row">
                <#nested>

                <#include "sidebar.ftl" encoding="utf-8">
            </div>
        </main>

        <footer class="blog-footer">
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
