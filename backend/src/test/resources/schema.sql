create table technology
(
    id              serial
        primary key,
    technology_name varchar(255) not null
);

create table user_
(
    id               serial
        primary key,
    email            varchar(255) not null
        constraint user_email_unique
            unique,
    enabled          boolean      not null,
    first_name       varchar(255) not null,
    github_url       varchar(255),
    locked           boolean      not null,
    password         varchar(255) not null,
    profile_image_id varchar(255)
        constraint profile_image_id_unique
            unique,
    role             smallint     not null
        constraint user__role_check
            check ((role >= 0) AND (role <= 1))
);

create table user_technology
(
    user_id       integer not null
        constraint fkaag32n7d2ygf7apraoyi1710n
            references user_,
    technology_id integer not null
        constraint fkbqkmd2ids232jtc526g3jxijh
            references technology
);