FROM mysql:8.0.32

# copy configuration
COPY ./conf.d/my.cnf /etc/mysql/conf.d/my.cnf

# copy query
COPY ./init/ /docker-entrypoint-initdb.d
