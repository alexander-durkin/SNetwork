INSERT INTO itnet.user (id, password, first_name, last_name, gender, birth_date)
  VALUES
    ('user1', '1234', 'Ivan', 'Ivanov', 'MALE', '1990-12-25'),
    ('user2', '12345', 'Petr', 'Petrov', 'MALE', '1990-01-02'),
    ('user3', '12345', 'Екатерина', 'Вторая', 'FEMALE', '1990-01-06'),
    ('user4', '123456', 'Семен', 'Семенов', 'MALE', '1990-01-03'),
    ('user5', '12345', 'Светлана', 'Слепакова', 'MALE', '1990-01-04'),
    ('user6', '12345', 'Анна', 'Каренина', 'FEMALE', '1990-01-08'),
    ('user7', '1234', 'Frodo', 'Baggins', 'MALE', '1991-01-18'),
    ('user8', '1234', 'Samwise', 'Gamgee', 'MALE', '1991-02-17'),
    ('user9', '123', 'Петр', 'Гланц', 'MALE', '1990-02-08'),
    ('user10', '123', 'Руслан', 'Габидуллин', 'MALE', '1990-05-08'),
    ('user11', '123', 'Василиса', 'Прекрасная', 'FEMALE', '1990-06-08'),
    ('user12', '123', 'Ольга', 'Румянцева', 'FEMALE', '1990-07-08'),
    ('user13', '123', 'Jena', 'Malone', 'FEMALE', '1990-07-09'),
    ('user14', '123', 'Elle', 'Fanning', 'FEMALE', '1997-07-19'),
    ('user15', '123', 'John', 'Doe', 'UNKNOWN', '1991-07-19'),
    ('user16', '123', 'Peter', 'Parker', 'MALE', '1992-09-13'),
    ('user17', '123', 'Gwen', 'Stacy', 'FEMALE', '1992-12-12'),
    ('user18', '123', 'Владимир', 'Володин', 'MALE', '1991-11-11'),
    ('user19', '123', 'Анастасия', 'Пивоварова', 'FEMALE', '1994-04-04'),
    ('user20', '123', 'Григорий', 'Кекс', 'MALE', '1995-05-05')
;

INSERT INTO itnet.friends (sender_id, receiver_id)
    VALUES
      ('user1', 'user2'),
      ('user3', 'user1')
;

INSERT INTO itnet.friends (sender_id, receiver_id, status)
VALUES
  ('user1', 'user4', 'ACCEPTED'),
  ('user2', 'user3', 'ACCEPTED'),
  ('user4', 'user2', 'ACCEPTED'),
  ('user2', 'user5', 'DENIED')
;

INSERT INTO itnet.messages (sender_id, receiver_id, text)
    VALUES
      ('user3', 'user4', 'привет, ты че?')
;