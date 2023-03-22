FROM mariadb:10.11

# copy configuration
COPY ./mariadb.conf.d/server.cnf /etc/mysql/mariadb.conf.d/server.cnf

# copy query
COPY ./init/ /docker-entrypoint-initdb.d
