FROM postgres:13

# enter psql as:
#$ su - psotgres
#$ psql 

# these will be unused if data directory is not empty
# ENV POSTGRES_PASSWORD=forumuser
# ENV POSTGRES_USER=forumuser
# ENV POSTGRES_DB=forumdb


# RUN pg_createcluster --start 12 main
# RUN pg_ctlcluster 12 main start
# RUN service postgresql start

# USER postgres
COPY scripts docker-entrypoint-initdb.d

# Create a PostgreSQL role named ``docker`` with ``docker`` as the password and
# then create a database `docker` owned by the ``docker`` role.
# Note: here we use ``&&\`` to run commands one after the other - the ``\``
#       allows the RUN command to span multiple lines.
# RUN    /etc/init.d/postgresql start &&\
#     createdb -O postgres forum

# Adjust PostgreSQL configuration so that remote connections to the
# database are possible.
RUN echo "host all  all 0.0.0.0/0  md5" >> /var/lib/postgresql/data/pg_hba.conf

# And add ``listen_addresses`` to ``/etc/postgresql/9.3/main/postgresql.conf``
RUN echo "listen_addresses='*'" >> /var/lib/postgresql/data/postgresql.conf

# Expose the PostgreSQL port
EXPOSE 5432

# Add VOLUMEs to allow backup of config, logs and databases
# VOLUME  ["/etc/postgresql", "/var/log/postgresql", "/var/lib/postgresql"]

# Set the default command to run when starting the container
# CMD ["/usr/lib/postgresql/12/bin/postgres", "-D", "/var/lib/postgresql/12/main", "-c", "config_file=/etc/postgresql/12/main/postgresql.conf"]
