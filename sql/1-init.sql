create schema if not exists blog;

create table if not exists blog.authors
(
    id       serial unique not null primary key,
    name     varchar(50)   not null,
    lastname varchar(100)  not null
);

create table if not exists blog.users
(
    id       serial unique not null primary key,
    username varchar(50)   not null,
    password varchar(500)  not null
);

create table if not exists blog.articles
(
    id         serial unique not null primary key,
    createdBy  int8          not null references blog.users (id),
    created    timestamp     not null,
    modifiedBy int8 references blog.users (id),
    modified   timestamp,
    authorId   int8          not null references blog.authors (id),
    title      varchar(200)  not null,
    content    bytea
);
