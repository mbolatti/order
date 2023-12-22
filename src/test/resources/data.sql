CREATE TABLE IF NOT EXISTS t_client (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  email_address VARCHAR(255),
  phone_number VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS t_order (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  client_id BIGINT NOT NULL,
  delivery_address VARCHAR(255),
  quantity_pilotes INT,
  unit_price DOUBLE,
  order_date TIMESTAMP
);
INSERT INTO t_client (first_name, last_name, email_address, phone_number) VALUES ('first name One', 'last name One', 'email One', '123456789');
INSERT INTO t_client (first_name, last_name, email_address, phone_number) VALUES ('first name Two', 'last name Two', 'email Two', '987456321');
INSERT INTO t_client (first_name, last_name, email_address, phone_number) VALUES ('first name Three', 'last name Three', 'email Three', '589745522');
INSERT INTO t_order (order_date, quantity_pilotes, client_id, delivery_address, unit_price) VALUES (CURRENT_TIMESTAMP, 5, 1, 'some place there 1 ', 1.49);
INSERT INTO t_order (order_date, quantity_pilotes, client_id, delivery_address, unit_price) VALUES (CURRENT_TIMESTAMP, 5, 2, 'some place there 2 ', 1.50);
INSERT INTO t_order (order_date, quantity_pilotes, client_id, delivery_address, unit_price) VALUES (DATEADD('MINUTE', -6, CURRENT_TIMESTAMP()), 5, 3, 'some place there 3', 1.30);
