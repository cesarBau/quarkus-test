## Stage 1 : build with maven builder image with native capabilities
FROM quay.io/quarkus/ubi9-quarkus-graalvmce-builder-image:jdk-21 AS build
USER root
RUN microdnf install make gcc
COPY --chown=quarkus:quarkus mvnw /code/mvnw
COPY --chown=quarkus:quarkus .mvn /code/.mvn
COPY --chown=quarkus:quarkus pom.xml /code/
RUN mkdir /musl && \
    curl -L -o musl.tar.gz https://more.musl.cc/11.2.1/x86_64-linux-musl/x86_64-linux-musl-native.tgz && \
    tar -xvzf musl.tar.gz -C /musl --strip-components 1 && \
    curl -L -o zlib.tar.gz https://github.com/madler/zlib/releases/download/v1.2.13/zlib-1.2.13.tar.gz && \
    mkdir zlib && tar -xvzf zlib.tar.gz -C zlib --strip-components 1 && \
    cd zlib && ./configure --static --prefix=/musl && \
    make && make install && \
    cd .. && rm -rf zlib && rm -f zlib.tar.gz && rm -f musl.tar.gz
ENV PATH="/musl/bin:${PATH}"
USER quarkus
WORKDIR /code
RUN ./mvnw -B org.apache.maven.plugins:maven-dependency-plugin:3.8.1:go-offline
COPY src /code/src
RUN ./mvnw package -Dnative -DskipTests -Dquarkus.native.additional-build-args="--static","--libc=musl"

## Stage 2 : create the final image
FROM debian:12-slim
RUN mkdir -p /opt/app/tmp && chmod -R 777 /opt/app/tmp
ENV JAVA_OPTIONS="-Djava.io.tmpdir=/opt/app/tmp"
ENV QUARKUS_VERTX_CACHE_DIR="/opt/app/tmp/vertx-cache"
COPY --from=build /code/target/*-runner /application
EXPOSE 8080
ENTRYPOINT [ "/application" ]