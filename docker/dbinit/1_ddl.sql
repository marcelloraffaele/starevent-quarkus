CREATE DATABASE eventdb;

CREATE USER 'eventdb' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON eventdb.* TO 'eventdb';
FLUSH PRIVILEGES;

USE eventdb;

DROP TABLE IF EXISTS `event`;
CREATE TABLE `event` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `artist` varchar(100) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `location` varchar(200) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `price` float NOT NULL,
  `availability` int NOT NULL,
  `img` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE DATABASE reservationdb;

CREATE USER 'reservationdb' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON reservationdb.* TO 'reservationdb';
FLUSH PRIVILEGES;

USE reservationdb;

DROP TABLE IF EXISTS `reservation`;
CREATE TABLE `reservation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `event_id` bigint NOT NULL,
  `user_id` varchar(100) NOT NULL,
  `secure_code` varchar(20) NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;