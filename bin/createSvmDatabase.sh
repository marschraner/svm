#!/bin/bash

# Anlegen der Datenbank svm, des Users svm und der Tabellen in der Datenbank svm.

mysql -u root -p < sql/createDbAndUser.sql

mysql -u svm -psvm < sql/createTables.sql
