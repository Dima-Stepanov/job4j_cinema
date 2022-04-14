/*схема таблицы TICKET для хранения купленных билетов.*/
create table if not exists ticket(
    ticket_id serial primary key,
    session_id int not null references sessions (session_id) on delete cascade,
    rowt int not null,
    cell int not null,
    user_id int not null references users (user_id) on delete cascade,
    constraint unique_row_cell unique (session_id, rowt, cell)
);