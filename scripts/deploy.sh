#!/usr/bin/env bash

mvn clean package

echo 'Copy files...'

scp -i ~/.ssh/id_rsa \
    target/sweater-1.0-SNAPSHOT.jar \
    root@192.168.81.128:/home/root/

echo 'Restart server...'

ssh -i ~/.ssh/id_rsa root@192.168.81.128 << EOF

pgrep java | xargs kill -9
nohup java -jar /home/root/sweater-1.0-SNAPSHOT.jar

EOF

echo 'Bye'