@ECHO ON

@REM Anlegen der Datenbanken svm und svmtest mit User svm bzw. svmtest und der Tabellen. 

mysql -u root -p < sql\createDbAndUser.sql

mysql -u svm -psvm -e "USE svm; SOURCE sql\createTables.sql;"
mysql -u svmtest -psvmtest -e "USE svmtest; SOURCE sql\createTables.sql;"

mysql --default-character-set=utf8 -u svm -psvm < sql\fillData.sql
