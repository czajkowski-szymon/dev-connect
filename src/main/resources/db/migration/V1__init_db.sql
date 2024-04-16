create table if not exists public.user_
(
    id               serial
        primary key,
    email            varchar(255)
        constraint user_email_unique
            unique,
    enabled          boolean not null,
    first_name       varchar(255),
    locked           boolean not null,
    password         varchar(255),
    profile_image_id varchar(255)
        constraint profile_image_id_unique
            unique,
    role             varchar(255)
        constraint user__role_check
            check ((role)::text = ANY ((ARRAY ['USER'::character varying, 'ADMIN'::character varying])::text[]))
);

-- alter table public.user_
--     owner to root;

create table if not exists public.project
(
    id                 serial
        primary key,
    description        varchar(255),
    project_name       varchar(255),
    project_manager_id integer
        constraint fkpaaciy8iaxjvhhxtx5eksrn7f
            references public.user_
            on update cascade on delete cascade
);

-- alter table public.project
--     owner to root;

create table if not exists public.task
(
    id         serial
        primary key,
    body       varchar(255),
    deadline   date,
    is_done    boolean not null,
    project_id integer
        constraint fkk8qrwowg31kx7hp93sru1pdqa
            references public.project
            on update cascade on delete cascade,
    user_id    integer
        constraint fk6uc2pbxr1jfrgqtxlefkcshil
            references public.user_
            on update cascade on delete set null
);

-- alter table public.task
--     owner to root;

create table if not exists public.user_project
(
    user_id    integer not null
        constraint fk9fphmq6fwsavmphyn3qgi2qol
            references public.project
            on update cascade on delete cascade,
    project_id integer not null
        constraint fk6ow7l7mg8e7gp1ss10xn5ij2o
            references public.user_
            on update cascade on delete cascade
);

-- alter table public.user_project
--     owner to root;