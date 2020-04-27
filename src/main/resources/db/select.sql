SELECT
  id,
  first_name,
  last_name
FROM itnet.user
WHERE id ILIKE 'us%' AND first_name ILIKE '%' AND last_name ILIKE '%';

SELECT *
FROM
  (SELECT
     id,
     first_name,
     last_name,
     ROW_NUMBER()
     OVER (
       ORDER BY id )
   FROM itnet.user
   WHERE id ILIKE 'use%' AND first_name ILIKE '%' AND last_name ILIKE '%') X
WHERE ROW_NUMBER BETWEEN 1 AND 2;

SELECT DISTINCT sender_id
FROM
  (SELECT *
   FROM
     (SELECT DISTINCT
        sender_id,
        sending_time
      FROM itnet.messages
      WHERE receiver_id = 'user1'
      UNION
      SELECT DISTINCT
        receiver_id,
        sending_time
      FROM itnet.messages
      WHERE sender_id = 'user1') s1
   ORDER BY sending_time DESC) s2;

--сообщения
SELECT *
FROM itnet.messages
WHERE (sender_id = 'user2' AND receiver_id = 'user1')
      OR (sender_id = 'user1' AND receiver_id = 'user2')
ORDER BY sending_time;

--выводит id собеседников
SELECT DISTINCT sender_id
FROM itnet.messages
WHERE receiver_id = 'user1'
UNION
SELECT DISTINCT receiver_id
FROM itnet.messages
WHERE sender_id = 'user1';

--выводит id отправителя и число непрочитанных от него сообщений
SELECT DISTINCT
  sender_id,
  count(sender_id)
FROM itnet.messages
WHERE receiver_id = 'user1'
      AND status = 'SENT'
GROUP BY sender_id;

--выводит id отправителя новых сообщений
SELECT DISTINCT sender_id
FROM itnet.messages
WHERE receiver_id = 'user1'
      AND status = 'SENT';

--число новых сообщений от разных собеседников
SELECT count(DISTINCT sender_id)
FROM itnet.messages
WHERE receiver_id = 'user2'
      AND status = 'SENT';

--сортированный по дате список диалогов с числом новых сообщений
SELECT DISTINCT
  id,
  sender_id,
  count(*)
FROM itnet.messages
WHERE receiver_id = 'user2'
      AND status = 'SENT'
GROUP BY sender_id, id
ORDER BY id DESC;

SELECT *
FROM itnet.messages
WHERE (receiver_id, sending_time) = ANY (SELECT
                                           receiver_id,
                                           MAX(sending_time)
                                         FROM itnet.messages
                                         WHERE sender_id = 'user2'
                                         GROUP BY receiver_id)
      OR (sender_id, sending_time) = ANY (SELECT
                                            sender_id,
                                            MAX(sending_time)
                                          FROM itnet.messages
                                          WHERE receiver_id = 'user2'
                                          GROUP BY sender_id)
ORDER BY sending_time DESC;

SELECT receiver_id
FROM (
       SELECT
         receiver_id,
         sending_time
       FROM itnet.messages
       WHERE (receiver_id, sending_time) = ANY (SELECT
                                                  receiver_id,
                                                  MAX(sending_time)
                                                FROM itnet.messages
                                                WHERE sender_id = 'user2'
                                                GROUP BY receiver_id)
       UNION
       SELECT
         sender_id,
         sending_time
       FROM itnet.messages
       WHERE (sender_id, sending_time) = ANY (SELECT
                                                sender_id,
                                                MAX(sending_time)
                                              FROM itnet.messages
                                              WHERE receiver_id = 'user2'
                                              GROUP BY sender_id)
       ORDER BY sending_time DESC) AS pp;

--последние диалоги
SELECT receiver_id
FROM (
       SELECT
         receiver_id,
         text,
         sending_time
       FROM itnet.messages
       WHERE sender_id = 'user2'
       GROUP BY receiver_id, text, sending_time
       UNION
       SELECT
         sender_id,
         text,
         sending_time
       FROM itnet.messages
       WHERE receiver_id = 'user2'
       GROUP BY sender_id, text, sending_time
       ORDER BY sending_time DESC) AS p;

--число новых сообщений от пользователя
SELECT count(*)
FROM itnet.messages
WHERE sender_id = 'user1' AND receiver_id = 'user2' AND messages.status = 'SENT';

SELECT
  p1.sender_id,
  p1.receiver_id,
  count(*)
FROM (
       SELECT *
       FROM itnet.messages
       WHERE sender_id = 'user2' OR receiver_id = 'user2'
       ORDER BY sending_time DESC) AS p1
WHERE p1.receiver_id = 'user2' AND p1.status = 'SENT'
GROUP BY p1.sender_id, p1.receiver_id, p1.status;

SELECT DISTINCT sender_id
FROM (
       SELECT
         id,
         sender_id
       FROM itnet.messages
       WHERE receiver_id = 'user2'
       UNION
       SELECT
         id,
         receiver_id
       FROM itnet.messages
       WHERE sender_id = 'user2'
       ORDER BY id DESC) AS poc;

--выводит id друзей
SELECT sender_id
FROM itnet.friends
WHERE receiver_id = 'user1'
      AND status = 'ACCEPTED'
UNION
SELECT receiver_id
FROM itnet.friends
WHERE sender_id = 'user1'
      AND status = 'ACCEPTED';

SELECT *
FROM (
       SELECT sender_id
       FROM itnet.friends
       WHERE receiver_id = 'user1'
             AND status = 'ACCEPTED'
       UNION
       SELECT receiver_id
       FROM itnet.friends
       WHERE sender_id = 'user1'
             AND status = 'ACCEPTED') AS X
WHERE sender_id IN (SELECT id
                   FROM itnet.user
                   WHERE id ILIKE '%' AND first_name ILIKE '%' AND last_name ILIKE '%');

SELECT count(*)
FROM (
       SELECT sender_id
       FROM itnet.friends
       WHERE receiver_id = 'user1'
             AND status = 'ACCEPTED'
       UNION
       SELECT receiver_id
       FROM itnet.friends
       WHERE sender_id = 'user1'
             AND status = 'ACCEPTED') AS X;

SELECT receiver_id
FROM itnet.friends
WHERE (sender_id = 'user1'
       AND (status = 'WAITING' OR status = 'DENIED'))
      AND receiver_id = (SELECT id
                         FROM itnet.user
                         WHERE id ILIKE '%' AND first_name ILIKE 'екатер%' AND last_name ILIKE '%');

--выводит id друзей (возвращает список юзеров)
SELECT *
FROM itnet."user"
WHERE id IN
      (SELECT sender_id
       FROM itnet.friends
       WHERE receiver_id = 'user1'
             AND status = 'ACCEPTED'
       UNION
       SELECT receiver_id
       FROM itnet.friends
       WHERE sender_id = 'user1'
             AND status = 'ACCEPTED');

--входящие заявки
SELECT sender_id
FROM itnet.friends
WHERE receiver_id = 'user4'
      AND status = 'WAITING';

--исходящие заявки
SELECT receiver_id
FROM itnet.friends
WHERE sender_id = 'user2'
      AND (status = 'WAITING' OR status = 'DENIED');

--входящие заявки (возвращает список юзеров)
SELECT *
FROM itnet."user"
WHERE id IN
      (SELECT sender_id
       FROM itnet.friends
       WHERE receiver_id = 'user4'
             AND status = 'WAITING');

--число новых заявок
SELECT count(*)
FROM itnet.friends
WHERE receiver_id = 'user2'
      AND status = 'WAITING';


SELECT status
FROM itnet.friends
WHERE sender_id = 'user2'
      AND receiver_id = 'user1'
UNION
SELECT status
FROM itnet.friends
WHERE sender_id = 'user1'
      AND receiver_id = 'user2';

UPDATE itnet.friends
SET
  sender_id = 'user3', receiver_id = 'user1', status = 'DENIED'
WHERE sender_id = 'user1' AND receiver_id = 'user3';