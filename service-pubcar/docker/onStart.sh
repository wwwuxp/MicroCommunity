#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t hezong/pubcar .

docker run -ti --name pubcar_test -p8020:8020 -idt hezong/pubcar:latest

docker logs -f pubcar_test
