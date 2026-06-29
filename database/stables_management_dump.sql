-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Create tables in the correct order to satisfy foreign key constraints
--

-- 1. Stables (no dependencies)
DROP TABLE IF EXISTS `stables`;
CREATE TABLE `stables` (
  `stable_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `max_capacity` int NOT NULL,
  PRIMARY KEY (`stable_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB;

--
-- 2. Users (no dependencies)
--
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `user_role` enum('ADMIN','USER') NOT NULL DEFAULT 'USER',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB;

--
-- 3. Horses (depends on stables)
--
DROP TABLE IF EXISTS `horses`;
CREATE TABLE `horses` (
  `horse_id` int NOT NULL AUTO_INCREMENT,
  `stable_id` int NOT NULL,
  `name` varchar(50) NOT NULL,
  `breed` varchar(50) NOT NULL,
  `type` enum('COLDBLOODED','WARMBLOODED') NOT NULL,
  `status` enum('HEALTHY','SICK','TRAINING','SOLD') NOT NULL,
  `age` int NOT NULL,
  `price` double NOT NULL,
  `weight` double NOT NULL,
  PRIMARY KEY (`horse_id`),
  KEY `stable_id` (`stable_id`),
  CONSTRAINT `horses_ibfk_1` FOREIGN KEY (`stable_id`) REFERENCES `stables` (`stable_id`),
  CONSTRAINT `horses_chk_1` CHECK ((`age` >= 0)),
  CONSTRAINT `horses_chk_2` CHECK ((`price` >= 0.0)),
  CONSTRAINT `horses_chk_3` CHECK ((`weight` > 0.0))
) ENGINE=InnoDB;

--
-- 4. Ratings (depends on horses)
--
DROP TABLE IF EXISTS `ratings`;
CREATE TABLE `ratings` (
  `rating_id` int NOT NULL AUTO_INCREMENT,
  `value` int NOT NULL,
  `horse_id` int NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`rating_id`),
  KEY `FKpq7ymxxmtlnl884xcpuhqudxc` (`horse_id`),
  CONSTRAINT `FKpq7ymxxmtlnl884xcpuhqudxc` FOREIGN KEY (`horse_id`) REFERENCES `horses` (`horse_id`)
) ENGINE=InnoDB;


--
-- Insert data into tables
--

INSERT INTO `stables` VALUES (1,'stajnia1',20),(2,'stajnia2',10),(10,'stajnia3',20);

INSERT INTO `users` VALUES (3,'admin','$2a$10$BhJiaLsV10AQj2a/737E2Opnp86ShcI9hxxAsQEjDwLPT5b8s6Yce','ADMIN');

INSERT INTO `horses` VALUES (1,1,'Bob','Arabian','WARMBLOODED','HEALTHY',20,5500,800),(2,1,'Dob','Thoroughbred','COLDBLOODED','TRAINING',12,21000,500),(3,2,'Gob','Andalusian','COLDBLOODED','SICK',8,1400,300),(10,10,'Dob','Thoroughbred','COLDBLOODED','TRAINING',12,21000,500),(11,10,'Bob','Arabian','WARMBLOODED','HEALTHY',20,5500,800);

INSERT INTO `ratings` VALUES (1,2,1,NULL);
