#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t docker_communityserivce .

docker run -ti --name communityserivce-1 -p8009:8009 -idt docker_communityserivce:latest

docker-compose up -d

docker logs -f --tail 100 communityserivce-1
