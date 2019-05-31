#!/bin/sh
docker run --name magentadesk -v `PWD`/conf.d:/etc/mysql/conf.d -p 13306:3306 -e MYSQL_ROOT_PASSWORD=pass -d mariadb:10.3