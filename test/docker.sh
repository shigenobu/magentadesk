#!/bin/sh
docker run --name magentadesk-`date +%s` -v `PWD`/conf.d:/etc/mysql/conf.d -p 13306:3306 -e MYSQL_ROOT_PASSWORD=pass -d mariadb:10.3
sleep 5
mysql -h 127.0.0.1 -P 13306 -u root -ppass < `PWD`/base.sql
mysql -h 127.0.0.1 -P 13306 -u root -ppass < `PWD`/compare.sql
