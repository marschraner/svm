
-- running this from command line:
-- hans@mars:.../svm/sql/Prototyp$ mysql -u root -p
-- Enter password: admin
-- mysql> source createuser.sql

drop user 'svmtest'@'localhost';
drop user 'svmtest'@'%';

CREATE USER 'svmtest'@'localhost' IDENTIFIED BY 'svmtest';

GRANT ALL PRIVILEGES ON *.* TO 'svmtest'@'localhost' WITH GRANT OPTION;

CREATE USER 'svmtest'@'%' IDENTIFIED BY 'svmtest';

GRANT ALL PRIVILEGES ON *.* TO 'svmtest'@'%' WITH GRANT OPTION;

