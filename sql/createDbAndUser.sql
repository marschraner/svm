-- Erzeugen von DB und User für svm
-- ********************************

-- Als root auszuführen.

-- hans@mars:.../svm/sql$ mysql -u root -p
-- Enter password: 
-- mysql> source createDbAndUser.sql

DROP DATABASE IF EXISTS svm;

-- Neue Datenbank erzeugen
-- ***********************
CREATE DATABASE IF NOT EXISTS svm
    DEFAULT CHARACTER SET utf8
    DEFAULT COLLATE utf8_general_ci;


-- Neuen User erzeugen und Rechte für DB zuweisen
-- **********************************************

-- DROP USER svm; Macht Probleme, wenn der User nicht existiert.
GRANT ALL ON svm.* TO 'svm'@'localhost' IDENTIFIED BY 'svm';
GRANT ALL ON svm.* TO 'svm'@'%' IDENTIFIED BY 'svm';
GRANT FILE ON *.* to 'svm'@'localhost';

