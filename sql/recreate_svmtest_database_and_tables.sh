#!/bin/bash

cd "$(dirname "$0")"

# DB neu erstellen
mysql -uadmin < recreate_svmtest_database.sql

# Tabellen neu erstellen
mysql --defaults-group-suffix=svmtest svmtest < create_tables.sql
