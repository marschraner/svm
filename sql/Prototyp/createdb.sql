
-- running this from command line:
-- mysql> quit
-- hans@mars:.../svm/sql/Prototyp$ mysql -u svmtest -p
-- Enter password: svmtest
-- mysql> source createdb.sql

SHOW DATABASES;

drop database svmtest;

create database svmtest;

use svmtest;

create table person (personid integer not null auto_increment, birth datetime, firstname varchar(255), lastname varchar(255), primary key (personid));

show table status where name = 'person';
SELECT TABLE_NAME, ENGINE FROM information_schema.TABLES where TABLE_SCHEMA = 'svmtest';

describe person;

insert into person (lastname, firstname, birth) values ('Stamm', 'Hans', '1960-05-31');
insert into person (lastname, firstname, birth) values ('Büenzli', 'Fritz', '1980-03-22');

select * from person;


