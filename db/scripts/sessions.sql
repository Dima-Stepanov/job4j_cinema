/*схема таблицы SESSIONS для хранения сеансов кинотеатра*/
create table if not exists sessions(
        session_id serial primary key,
        session_name varchar
);