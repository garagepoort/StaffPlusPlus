FROM openjdk:17
ARG SPIGOT_VERSION=1.18
COPY spigot-${SPIGOT_VERSION}.jar .
RUN chmod 777 spigot-${SPIGOT_VERSION}.jar
RUN mkdir plugins
COPY staffplusplus-core-1.18.0-SNAPSHOT.jar /plugins
RUN echo "eula=true" > eula.txt
#COPY ../../../../target/staffplusplus-core-1.18.0-SNAPSHOT.jar /server/plugins
RUN java -jar spigot-1.18.jar nogui