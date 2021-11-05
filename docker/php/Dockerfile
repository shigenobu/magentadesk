FROM php:8.0-cli

# php.ini
RUN sed -ie "s/;date.timezone =/date.timezone = Asia\/Tokyo/g" /usr/local/etc/php/php.ini-production
RUN cp /usr/local/etc/php/php.ini-production /usr/local/etc/php/php.ini
