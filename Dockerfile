FROM ubuntu:bionic
LABEL maintainer="Michael Hansen <hansen.mike@gmail.com>"

RUN apt-get update && \
    apt-get install -y openjdk-8-jdk-headless unzip

COPY gradle-5.2.1-bin.zip /gradle.zip
RUN cd / && unzip /gradle.zip && ln -s /gradle-5.2.1/bin/gradle /usr/bin/gradle

COPY build.gradle /rhasspy/
COPY settings.gradle /rhasspy/
COPY src /rhasspy/src/

RUN cd /rhasspy/ && gradle installDist

COPY run.sh /rhasspy/
RUN chmod +x /rhasspy/run.sh

COPY config.xml /rhasspy/config.xml

ENV ACOUSTIC_MODEL=/config/en/acoustic_model
ENV LANGUAGE_MODEL=/config/en/language_model.lm
ENV DICTIONARY=/config/en/dictionary.txt

ENTRYPOINT ["/rhasspy/run.sh"]