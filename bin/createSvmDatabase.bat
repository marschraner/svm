@ECHO ON

@REM Anlegen der Datenbank svm, des Users svm und der Tabellen in der Datenbank svm.

mysql -u root -p < sql\createDbAndUser.sql

mysql -u svm -psvm < sql\createTables.sql

mysql --default-character-set=utf8 -u svm -psvm < sql\fillData.sql
