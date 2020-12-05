#!/usr/bin/env bash

mvn clean package -Dmaven.test.skip=true
java  -jar target/darian-lucene-vertx-1.0.0-SNAPSHOT-fat.jar -conf src/main/resources/application_dev.json
