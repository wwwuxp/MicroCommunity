#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t docker_frontserivce .

docker run -ti --name frontserivce-1 -p8012:8012 -idt docker_frontserivce:latest

docker-compose up -d 

docker logs -f --tail 100 frontserivce-1
