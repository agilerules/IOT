CREATE DATABASE agilerulesdb;
USE agilerulesdb;
CREATE USER ‘admin’@’localhost’ IDENTIFIED BY ‘password’;
GRANT ALL PRIVILEGES ON agilerulesdb.* TO 'admin'@'localhost';
--Relogin with the new user "admin" that is created and execute the following scripts
mysql -u admin –p
use agilerulesdb;
CREATE TABLE led_details 
(
ID int NOT NULL AUTO_INCREMENT,
BlinkStatus varchar(3), 
DateTimeStamp DATETIME, 
BlinkCounter INT, 
JsonPayload varchar(255),
PRIMARY KEY (ID)
);
