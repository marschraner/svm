#!/bin/bash

cd "$(dirname "$0")"

# DB neu erstellen
mysql -uadmin < recreate_svm_database.sql

# Tabellen neu erstellen
mysql --defaults-group-suffix=svm svm < create_tables.sql
