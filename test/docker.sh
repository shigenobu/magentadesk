#!/bin/sh
docker run --name magentadesk-`date +%s` -v `pwd`/conf.d:/etc/mysql/conf.d -p 13306:3306 -e MYSQL_ROOT_PASSWORD=pass -d mariadb:10.4
sleep 10

mysql -h 127.0.0.1 -P 13306 -u root -ppass -e "drop database if exists base; create database base default charset=utf8mb4;"
mysql -h 127.0.0.1 -P 13306 -u root -ppass base < `pwd`/base.sql

mysql -h 127.0.0.1 -P 13306 -u root -ppass -e "drop database if exists compare; create database compare default charset=utf8mb4;"
mysql -h 127.0.0.1 -P 13306 -u root -ppass compare < `pwd`/compare.sql
