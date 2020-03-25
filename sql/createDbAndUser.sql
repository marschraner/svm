-- Erzeugen von DB und User für svm
-- ********************************

-- Als root auszuführen.

-- hans@mars:.../svm/sql$ mysql -u root -p
-- Enter password: 
-- mysql> source createDbAndUser.sql


-- Neue Datenbank erzeugen
-- ***********************
DROP DATABASE IF EXISTS svm;

CREATE DATABASE IF NOT EXISTS svm
    DEFAULT CHARACTER SET utf8
    DEFAULT COLLATE utf8_general_ci;


-- Neuen User erzeugen und Rechte für DB zuweisen
-- **********************************************
DROP USER IF EXISTS svm;

CREATE USER 'svm'@'%' IDENTIFIED BY 'svm';
GRANT ALL PRIVILEGES ON svm.* TO 'svm'@'%';

