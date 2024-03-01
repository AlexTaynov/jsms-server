INSERT INTO users (password, roles)
VALUES ('$2a$10$/7xDoZLpDfCH0TKfaLlYg..hwXagdy0B5/zye05YYAUt84Z0SzhHy', 'USER');
INSERT INTO user_data (user_id, email, first_name, second_name)
VALUES (lastval(), 'user@mail.ru', 'User', 'Userov');

INSERT INTO users (password, roles)
VALUES ('$2a$10$/7xDoZLpDfCH0TKfaLlYg..hwXagdy0B5/zye05YYAUt84Z0SzhHy', 'ADMIN');
INSERT INTO user_data (user_id, email, first_name, second_name)
VALUES (lastval(), 'admin@mail.ru', 'Admin', 'Adminov');