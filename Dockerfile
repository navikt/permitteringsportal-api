FROM navikt/java:17
COPY ./build/libs/permitteringsportal-api-all.jar app.jar

EXPOSE 8080
