FROM postgres:13
ENV POSTGRES_USER=testuser
ENV POSTGRES_PASSWORD=testuserpassword
ENV POSTGRES_DB=springsecurity
RUN echo "host all  all 0.0.0.0/0  md5" >> /var/lib/postgresql/data/pg_hba.conf
RUN echo "listen_addresses='*'" >> /var/lib/postgresql/data/postgresql.conf
EXPOSE 5432