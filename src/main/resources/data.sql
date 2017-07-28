INSERT INTO user(login_name, pwd, enabled, role) VALUES ('publisher1', '$2a$10$RC1.YFSL8o2vCIg6kB3fjuBB639BdjY6AD8rIw9oyWJLws/1Q6Lku', TRUE, 'PUBLISHER');
INSERT INTO user(login_name, pwd, enabled, role) VALUES ('publisher2', '$2a$10$MUahUza86ErCxtsgpmMBDeR5VtoGHioRdl03/jQmkM/sk6L.Eg28e', TRUE, 'PUBLISHER');
INSERT INTO user(login_name, pwd, enabled, role) VALUES ('valexe54321@gmail.com', '$2a$10$WcgRF8VQ8DKt4h4Hz9pWv.6MXnIRmcPr0j9jqsseprsBwTD4w8WSm', TRUE, 'USER');
INSERT INTO user(login_name, pwd, enabled, role) VALUES ('mstroe76@gmail.com', '$2a$10$Q5bxyPXhHXFc1fRUMCRWR.GbgsXx9aGZdoEoEAz2JFEfckdyUKfOi', TRUE, 'USER');
INSERT INTO user(login_name, pwd, enabled, role) VALUES ('publisher3', '$2a$10$RC1.YFSL8o2vCIg6kB3fjuBB639BdjY6AD8rIw9oyWJLws/1Q6Lku', TRUE, 'PUBLISHER');

INSERT INTO publisher(user_id, name) VALUES (1, 'Test Publisher1');
INSERT INTO publisher(user_id, name) VALUES (2, 'Test Publisher2');
INSERT INTO publisher(user_id, name) VALUES (5, 'Test Publisher3');

INSERT INTO category(id, name) VALUES (1, 'immunology');
INSERT INTO category(id, name) VALUES (2, 'pathology');
INSERT INTO category(id, name) VALUES (3, 'endocrinology');
INSERT INTO category(id, name) VALUES (4, 'microbiology');
INSERT INTO category(id, name) VALUES (5, 'neurology');


INSERT INTO journal(id, publisher_id, category_id, name, publish_date, uuid) VALUES(1, 1, 3, 'Medicine', '2017-07-11 21:59:59', '8305d848-88d2-4cbd-a33b-5c3dcc548056');
INSERT INTO journal(id, publisher_id, category_id, name, publish_date, uuid) VALUES(2, 1, 4, 'Test Journal', '2017-07-12 16:34:59', '09628d25-ea42-490e-965d-cd4ffb6d4e9d');
INSERT INTO journal(id, publisher_id, category_id, name, publish_date, uuid) VALUES(3, 2, 5, 'Health', '2017-07-13 21:20:59', '75f29692-237b-4116-95ed-645de5c57b4d');

INSERT INTO subscription(id, user_id, category_id, date) VALUES(1, 3, 3, NOW());

--valexe54321@gmail.com password is user1
--mstroe76@gmail.com password is user2
--publisher3 password is publisher1