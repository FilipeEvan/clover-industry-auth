create table users_accesslevels(

    user_id uuid,
    accesslevel_id uuid,
    primary key(user_id, accesslevel_id),

    foreign key (user_id)
    references users (id)
    on delete set default
    on update set default,

    foreign key (accesslevel_id)
    references accesslevels (id)
    on delete set default
    on update set default

);