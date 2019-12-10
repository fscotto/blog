INSERT INTO blog.users (id, username, password)
VALUES (default, 'plague', 'secret');
INSERT INTO blog.authors (id, name, lastname, userid)
VALUES (default, 'Fabio', 'Scotto di Santolo', 1);
INSERT INTO blog.articles (id, createdby, created, title, content)
VALUES (default, 1, now(), 'Articolo di Prova',
        'Gregorio Samsa, svegliandosi una mattina da sogni agitati, si trovò trasformato, nel suo letto, in un enorme insetto immondo. Riposava sulla schiena, dura come una corazza, e sollevando un poco il capo vedeva il suo ventre arcuato, bruno e diviso in tanti segmenti ricurvi, in cima a cui la coperta da letto, vicina a scivolar giù tutta, si manteneva a fatica. Le gambe, numerose e sottili da far pietà, rispetto alla sua corporatura normale, tremolavano senza tregua in un confuso luccichio dinanzi ai suoi occhi. Cosa m’è avvenuto? pensò. Non era un sogno. La sua camera, una stanzetta di giuste');