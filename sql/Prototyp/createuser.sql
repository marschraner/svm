
-- running this from command line:
-- hans@mars:~/MAS-SE/Masterarbeit/workspace/MySQL-Test$ mysql -u root -p
-- Enter password: admin
-- mysql> source createdb.sql

drop user 'svmtest'@'localhost';
drop user 'svmtest'@'%';

CREATE USER 'hanstest'@'localhost' IDENTIFIED BY 'hanstest';

GRANT ALL PRIVILEGES ON *.* TO 'hanstest'@'localhost' WITH GRANT OPTION;

CREATE USER 'hanstest'@'%' IDENTIFIED BY 'hanstest';

GRANT ALL PRIVILEGES ON *.* TO 'hanstest'@'%' WITH GRANT OPTION;

