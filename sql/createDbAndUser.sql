-- Erzeugen von DB und User für svm
-- ********************************

-- Als root auszuführen.

-- hans@mars:.../svm/sql$ mysql -u root -p
-- Enter password: 
-- mysql> source createDbAndUser.sql

DROP DATABASE IF EXISTS svm;
DROP DATABASE IF EXISTS svmtest;

-- Neue Datenbank erzeugen
-- ***********************
CREATE DATABASE IF NOT EXISTS svm
    DEFAULT CHARACTER SET utf8
    DEFAULT COLLATE utf8_general_ci;
CREATE DATABASE IF NOT EXISTS svmtest
    DEFAULT CHARACTER SET utf8
    DEFAULT COLLATE utf8_general_ci;


-- Neue Users erzeugen und Rechte für DB zuweisen
-- **********************************************

-- DROP USER svm; Macht Probleme, wenn der User nicht existiert.
GRANT ALL ON svm.* TO 'svm'@'%' IDENTIFIED BY 'svm';
GRANT ALL ON svmtest.* TO 'svmtest'@'%' IDENTIFIED BY 'svmtest';

