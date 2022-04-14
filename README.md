![ScreenShot](images/job4_logo.png)

<b>14.04.2024 </b><br>
<a href=https://github.com/Dima-Stepanov>My github</a>

<b>3. Мидл

3.2.9. Контрольные вопросы
2. Сервис - Кинотеатр [#504869]</b>

В этом задании вам нужно разработать сайт по покупки билетов в кинотеатр.

В этом задании нужно использовать Spring boot, Thymeleaf, Bootstrap, JDBC.

Опишем виды.

1. Главная страница форма с выбором фильма. Ряда и места.

Три списка. Если места заняты, то их в списках не показываем.

2. После загрузки формы отобразить результат покупки. Важно. Пользователь может не купить билет, потому что его купил другой пользователь. То есть одновременно выбрали одинаковые места.

3. Модели. User. Session (Фильтры). Ticket (Купленный билет).

Вычисление свободных мест для сеанса необходимо сделать в слое SessionService. Вычисления делаем по купленным билетам.

Задание.

1. У вас должно быть три слоя. Controller, Service, Persistence.

2. Создайте три таблицы. user, session, ticker, .


CREATE TABLE users (<br>
id SERIAL PRIMARY KEY,<br>
username VARCHAR NOT NULL,<br>
email VARCHAR NOT NULL UNIQUE,<br>
phone VARCHAR NOT NULL UNIQUE<br>
);<br>

CREATE TABLE sessions (<br>
id SERIAL PRIMARY KEY,<br>
name text<br>
);<br>

CREATE TABLE ticket (<br>
id SERIAL PRIMARY KEY,<br>
session_id INT NOT NULL REFERENCES sessions(id),<br>
row INT NOT NULL,<br>
cell INT NOT NULL,<br>
user_id INT NOT NULL REFERENCES users(id)<br>
);<br>

Создайте папку db и в ней разместите sql схем таблиц. Подумайте, какие поля тут нужно сделать уникальными.
Загрузку базы сделать через liquibase.

Важно. Клиенты могут забронировать одинаковые места. Чтобы этого избжать нужно добеавить ограничения.

Ограничения нужно добавить на три колонки session_id, row, cell (сеанс, ряд и место).


CREATE CONSTRAINT ...

В JDBC нужно добавить обработку ConstrainsViolationException.

4. Для проекта создайте новый репозиторий job4j_cinema

5. Загрузите код в репозиторий. Оставьте ссылку на коммит.

6. Переведите ответственного на Петра Арсентьева.







