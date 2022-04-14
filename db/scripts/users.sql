/*схема таблицы USERS для хранения покупателей билетов*/
create table if not exists users (
    user_id serial primary key,
    user_name varchar not null,
    email varchar not null constraint uneque_email unique,
    phone varchar not null constraint unique_email unique
);