Java проект использует фраймворки Spring и Vaadin. 
 Имеет 4 страницы :
 регистрации, входа, пользователя и администратора(Только для пользователей с ролью admin).
На странице регистрации можно ввести новый логин и пароль и сохранить в БД и перейти на страницу входа.
На странице входа можно пройти регистрацию  или перейти на главную.
На главной страницы будет одна (если это User) или две (если это Admin) вкладки, с личной информацией, 
где ее можно редактировать и загрузить фото, и с таблицей всех пользователей, редактированием, созданием нового.
В правом верхнем углу вкладка для выхода.

Для запуска потребуется создать в postgresql базу данных а в application.yml указать:
spring:
 datasource:
  url: jdbc:postgresql://~~localhost:5432/TestUser~~ - ваш порт и название базы
  username: username - ваше имя и пароль
  password: password
  driver-class-name: org.postgresql.Driver
 jpa:
  show-sql: true
  hibernate.ddl-auto: update - при первом запуске create...
(Саблюдайте отступы они важны)

Если в базе данных нет пользователя с ролью ADMIN то будет создан пользователь admin с поролем admin.



This Java project uses the Spring and Vaadin frameworks.
It has 4 pages:
registration, login, user and administrator (Only for users with the admin role).
On the registration page, you can enter a new username and password and save it to the database and go to the login page.
On the login page, you can register or go to the main page.
On the main page there will be one (if it is a User) or two (if it is an Admin) tabs with personal information,
where it can be edited and uploaded photos, and with a table of all users, editing, creating a new one.
There is an exit tab in the upper right corner.

To run, you will need to create a database in postgresql and specify in application.yml:
spring:
datasource:
url: jdbc:postgresql://~~localhost:5432/TestUser~~ - your port and the name of the database
username: username is your username and password
password: password
driver-class-name: org.postgresql.Driver
jpa:
show-sql: true
hibernate.ddl-auto: update - when you first start create...
(Observe the margins, they are important)

If there is no user with the ADMIN role in the database, an admin user with the admin password will be created.





 
Java project using Spring and Vaadin.
4 pages:
  1.Registration
  2.Login with password
  3.User page
  4.Administrator page, only for admin
