#！/bin/sh

mvn clean package -Dmaven.test.skip=true
java -Dlog.path.prefix=/home/darian/software/logs -jar target/darian-lucene-vertx-1.0.0-SNAPSHOT-fat.jar -conf src/main/resources/application_dev_linux.json
