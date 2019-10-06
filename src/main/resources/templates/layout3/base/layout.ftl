<#assign headerContent>
  <div class="col-lg-8 col-md-10 mx-auto">
    <div class="site-heading">
      <h1>Plague's Blog</h1>
      <span class="subheading">di Fabio Scotto di Santolo</span>
    </div>
  </div>
</#assign>

<#macro template header=headerContent>
  <!DOCTYPE html>
  <html lang="it">
    <head>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
      <meta name="description" content="">
      <meta name="author" content="">

      <title>${title} | Blog di Fabio Scotto di Santolo</title>

      <!-- Bootstrap core CSS -->
      <link href="/static/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

      <!-- Custom fonts for this template -->
      <link href="/static/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
      <link href='https://fonts.googleapis.com/css?family=Lora:400,700,400italic,700italic' rel='stylesheet'
            type='text/css'>
      <link href='https://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800'
            rel='stylesheet' type='text/css'>

      <!-- Custom styles for this template -->
      <link href="/static/css/clean-blog.css" rel="stylesheet">
    </head>

    <body>
      <!-- Navigation -->
      <nav class="navbar navbar-expand-lg navbar-light fixed-top" id="mainNav">
        <div class="container">
          <a class="navbar-brand" href="/">Plague</a>
          <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse"
                  data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false"
                  aria-label="Toggle navigation">
            Menu
            <i class="fas fa-bars"></i>
          </button>
          <div class="collapse navbar-collapse" id="navbarResponsive">
            <ul class="navbar-nav ml-auto">
              <li class="nav-item">
                <a class="nav-link" href="/about">About me</a>
              </li>
            </ul>
          </div>
        </div>
      </nav>

      <!-- Page Header -->
      <header class="masthead" style="background-image: url('/static/image/masterhead.png')">
        <div class="overlay"></div>
        <div class="container">
          <div class="row">
              ${header}
          </div>
        </div>
      </header>

      <!-- Main Content -->
      <div class="container">
        <div class="row">
            <#nested>
        </div>
      </div>

      <!-- Footer -->
      <footer>
          <#include "footer.ftl" encoding="utf-8">
      </footer>

      <!-- Bootstrap core JavaScript -->
      <script src="/static/vendor/jquery/jquery.min.js"></script>
      <script src="/static/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

      <!-- Custom scripts for this template -->
      <script src="/static/js/clean-blog.min.js"></script>
      <!-- UML JS Renderer Library -->
      <script src="/static/js/mermaid.min.js"></script>
    </body>
  </html>
</#macro>
