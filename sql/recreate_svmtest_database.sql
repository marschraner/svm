-- Erzeugen von DB und User für svm
-- ********************************

-- Als root auszuführen.

-- hans@mars:.../svm/sql$ mysql -u admin -p
-- Enter password: 
-- mysql> source recreate_svmtest_database.sql


-- Neue Datenbank erzeugen
-- ***********************
DROP DATABASE IF EXISTS svmtest;

CREATE DATABASE svmtest
    DEFAULT CHARACTER SET utf8
    DEFAULT COLLATE utf8_general_ci;


-- Neuen User erzeugen und Rechte für DB zuweisen
-- **********************************************
DROP USER IF EXISTS svmtest;

CREATE USER 'svmtest'@'%' IDENTIFIED BY 'svmtest';
GRANT ALL PRIVILEGES ON svmtest.* TO 'svmtest'@'%';

